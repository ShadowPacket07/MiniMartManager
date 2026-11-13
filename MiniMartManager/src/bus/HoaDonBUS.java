package bus;

import java.time.LocalDateTime;
import java.util.List;
import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;

public class HoaDonBUS {
	private HoaDonDAO hoaDonDAO;
	private ChiTietHoaDonDAO chiTietHoaDonDAO;
	private SanPhamBUS sanPhamBUS;

	public HoaDonBUS() {
		this.hoaDonDAO = new HoaDonDAO();
		this.chiTietHoaDonDAO = new ChiTietHoaDonDAO();
		this.sanPhamBUS = new SanPhamBUS();
	}

	public List<HoaDon> getAllHoaDon() {
		return hoaDonDAO.getAllHoaDon();
	}

	public HoaDon getHoaDonTheoMa(String maHD) {
		if (maHD == null || maHD.trim().isEmpty()) {
			return null;
		}
		HoaDon hd = hoaDonDAO.getHoaDonTheoMa(maHD.trim());
		if (hd != null) {
			List<ChiTietHoaDon> dsChiTiet = chiTietHoaDonDAO.getChiTietTheoMaHD(hd.getMaHD());
			if (dsChiTiet != null) {
				hd.setDsChiTiet(dsChiTiet);
			}
		}
		return hd;
	}

	public boolean kiemTraTienKhachDua(double tienKhachDua, double tongTien) {
		return tienKhachDua >= 0 && tongTien >= 0 && tienKhachDua >= tongTien;
	}

	public boolean kiemTraTienKhachDua(HoaDon hd) {
		if (hd == null) {
			return false;
		}
		return kiemTraTienKhachDua(hd.getTienKhachDua(), hd.tinhTongTien());
	}

	public String taoMaHDTuDong() {
		List<HoaDon> ds = hoaDonDAO.getAllHoaDon();
		if (ds == null || ds.isEmpty()) {
			return "HD001";
		}
		int maxIndex = 0;
		for (HoaDon hd : ds) {
			if (hd != null && hd.getMaHD() != null) {
				String ma = hd.getMaHD().trim();
				if (ma.matches("^HD\\d{3}$")) {
					try {
						int index = Integer.parseInt(ma.substring(2));
						if (index > maxIndex) {
							maxIndex = index;
						}
					} catch (NumberFormatException e) {
					}
				}
			}
		}
		return String.format("HD%03d", maxIndex + 1);
	}

	public boolean thanhToan(HoaDon hd) {
		if (hd == null) {
			throw new IllegalArgumentException("Hóa đơn không được để trống");
		}
		if (hd.getNhanVien() == null) {
			throw new IllegalArgumentException("Nhân viên lập hóa đơn không được rỗng");
		}
		List<ChiTietHoaDon> dsChiTiet = hd.getDsChiTiet();
		if (dsChiTiet == null || dsChiTiet.isEmpty()) {
			throw new IllegalArgumentException("Hóa đơn phải có ít nhất 1 chi tiết sản phẩm");
		}
		for (ChiTietHoaDon ct : dsChiTiet) {
			if (ct == null || ct.getSanPham() == null) {
				throw new IllegalArgumentException("Sản phẩm trong chi tiết không hợp lệ");
			}
			if (ct.getSoLuong() <= 0) {
				throw new IllegalArgumentException("Số lượng mua của sản phẩm phải > 0");
			}
			if (ct.getDonGia() <= 0) {
				throw new IllegalArgumentException("Đơn giá bán phải > 0");
			}
			String maSP = ct.getSanPham().getMaSP();
			if (!sanPhamBUS.kiemTraTonKho(maSP, ct.getSoLuong())) {
				String tenSP = ct.getSanPham().getTenSP() != null ? ct.getSanPham().getTenSP() : maSP;
				throw new IllegalArgumentException("Sản phẩm [" + tenSP + "] không còn đủ số lượng tồn kho để bán");
			}
		}
		
		double tongTien = hd.tinhTongTien();

		if (!kiemTraTienKhachDua(hd.getTienKhachDua(), tongTien)) {
			throw new IllegalArgumentException("Tiền khách đưa (" + String.format("%,.0f", hd.getTienKhachDua())
					+ " VNĐ) không đủ để thanh toán tổng tiền (" + String.format("%,.0f", tongTien) + " VNĐ)");
		}

		if (hd.getMaHD() == null || hd.getMaHD().trim().isEmpty()) {
			hd.setMaHD(taoMaHDTuDong());
		}

		if (hd.getNgayLap() == null) {
			hd.setNgayLap(LocalDateTime.now());
		}

		boolean themHDOk = hoaDonDAO.themHoaDon(hd);
		if (!themHDOk) {
			return false;
		}

		for (ChiTietHoaDon ct : dsChiTiet) {
			ct.setHoaDon(hd);
			chiTietHoaDonDAO.themChiTietHoaDon(ct);
			sanPhamBUS.capNhatTonKhoSauKhiBan(ct.getSanPham().getMaSP(), ct.getSoLuong());
		}
		return true;
	}

	public double tinhTienThua(HoaDon hd) {
		if (hd == null) {
			return 0.0;
		}
		return hd.tinhTienThua();
	}
}
