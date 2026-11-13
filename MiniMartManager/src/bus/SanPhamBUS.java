package bus;

import java.util.ArrayList;
import java.util.List;
import dao.SanPhamDAO;
import entity.SanPham;

public class SanPhamBUS {
	private SanPhamDAO sanPhamDAO;

	public SanPhamBUS() {
		this.sanPhamDAO = new SanPhamDAO();
	}

	public List<SanPham> getAllSanPham() {
		return sanPhamDAO.getAllSanPham();
	}

	public SanPham getSanPhamTheoMa(String maSP) {
		if (maSP == null || maSP.trim().isEmpty()) {
			return null;
		}
		return sanPhamDAO.getSanPhamTheoMa(maSP.trim());
	}

	public List<SanPham> getSanPhamDangKinhDoanh() {
		List<SanPham> ds = sanPhamDAO.getAllSanPham();
		List<SanPham> ketQua = new ArrayList<>();
		if (ds != null) {
			for (SanPham sp : ds) {
				if (sp != null && sp.isTrangThai()) {
					ketQua.add(sp);
				}
			}
		}
		return ketQua;
	}

	public List<SanPham> timKiemSanPham(String tuKhoa) {
		if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
			return getAllSanPham();
		}
		String tuKhoaLower = tuKhoa.trim().toLowerCase();
		List<SanPham> ds = sanPhamDAO.getAllSanPham();
		List<SanPham> ketQua = new ArrayList<>();
		if (ds != null) {
			for (SanPham sp : ds) {
				if (sp != null) {
					boolean khopMa = sp.getMaSP() != null && sp.getMaSP().toLowerCase().contains(tuKhoaLower);
					boolean khopTen = sp.getTenSP() != null && sp.getTenSP().toLowerCase().contains(tuKhoaLower);
					boolean khopLoai = sp.getLoaiSP() != null && sp.getLoaiSP().toLowerCase().contains(tuKhoaLower);
					if (khopMa || khopTen || khopLoai) {
						ketQua.add(sp);
					}
				}
			}
		}
		return ketQua;
	}

	public boolean kiemTraGiaHopLe(double donGia) {
		return donGia > 0;
	}

	public boolean kiemTraTonKho(String maSP, int soLuongMua) {
		if (maSP == null || soLuongMua <= 0) {
			return false;
		}
		SanPham sp = getSanPhamTheoMa(maSP);
		return kiemTraTonKho(sp, soLuongMua);
	}

	public boolean kiemTraTonKho(SanPham sp, int soLuongMua) {
		if (sp == null || soLuongMua <= 0) {
			return false;
		}
		if (!sp.isTrangThai()) {
			return false;
		}
		return sp.getSoLuongTon() >= soLuongMua;
	}

	public boolean themSanPham(SanPham sp) {
		if (sp == null) {
			throw new IllegalArgumentException("Thông tin sản phẩm không được để trống");
		}
		if (!kiemTraGiaHopLe(sp.getDonGia())) {
			throw new IllegalArgumentException("Đơn giá phải > 0");
		}
		if (sp.getSoLuongTon() < 0) {
			throw new IllegalArgumentException("Số lượng tồn không được âm");
		}
		if (getSanPhamTheoMa(sp.getMaSP()) != null) {
			throw new IllegalArgumentException("Mã sản phẩm " + sp.getMaSP() + " đã tồn tại trong hệ thống");
		}
		return sanPhamDAO.themSanPham(sp);
	}

	public boolean capNhatSanPham(SanPham sp) {
		if (sp == null) {
			throw new IllegalArgumentException("Thông tin sản phẩm không được để trống");
		}
		SanPham spCu = getSanPhamTheoMa(sp.getMaSP());
		if (spCu == null) {
			throw new IllegalArgumentException("Không tìm thấy sản phẩm " + sp.getMaSP() + " để cập nhật");
		}
		if (!kiemTraGiaHopLe(sp.getDonGia())) {
			throw new IllegalArgumentException("Đơn giá phải > 0");
		}
		if (sp.getSoLuongTon() < 0) {
			throw new IllegalArgumentException("Số lượng tồn không được âm");
		}
		return sanPhamDAO.capNhatSanPham(sp);
	}

	public boolean ngungKinhDoanh(String maSP) {
		SanPham sp = getSanPhamTheoMa(maSP);
		if (sp == null) {
			throw new IllegalArgumentException("Không tìm thấy sản phẩm có mã: " + maSP);
		}
		sp.setTrangThai(false);
		return sanPhamDAO.capNhatSanPham(sp);
	}

	public boolean capNhatTonKhoSauKhiBan(String maSP, int soLuongBan) {
		if (!kiemTraTonKho(maSP, soLuongBan)) {
			throw new IllegalArgumentException("Số lượng tồn kho không đủ để bán");
		}
		SanPham sp = getSanPhamTheoMa(maSP);
		sp.capNhatTonKho(soLuongBan);
		return sanPhamDAO.capNhatSoLuongTon(maSP, sp.getSoLuongTon());
	}
}
