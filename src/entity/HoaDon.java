package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon {
	private String maHD;
	private LocalDateTime ngayLap;
	private NhanVien nhanVien;
	private double tienKhachDua;
	private double tongTien;
	private List<ChiTietHoaDon> dsChiTiet;

	public HoaDon() {
		super();
		this.ngayLap = LocalDateTime.now();
		this.dsChiTiet = new ArrayList<>();
		this.tongTien = 0.0;
	}

	public HoaDon(String maHD) {
		this();
		setMaHD(maHD);
	}

	public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVien, double tienKhachDua) {
		this();
		setMaHD(maHD);
		setNgayLap(ngayLap);
		setNhanVien(nhanVien);
		setTienKhachDua(tienKhachDua);
	}

	public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVien, double tienKhachDua, double tongTien) {
		this(maHD, ngayLap, nhanVien, tienKhachDua);
		setTongTien(tongTien);
	}

	public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVien, double tienKhachDua,
			List<ChiTietHoaDon> dsChiTiet) {
		super();
		setMaHD(maHD);
		setNgayLap(ngayLap);
		setNhanVien(nhanVien);
		setDsChiTiet(dsChiTiet);
		setTienKhachDua(tienKhachDua);
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		if (maHD == null || !maHD.trim().matches("^HD\\d{3}$")) {
			throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
		}
		this.maHD = maHD.trim();
	}

	public LocalDateTime getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDateTime ngayLap) {
		if (ngayLap == null || ngayLap.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Ngày lập không hợp lệ");
		}
		this.ngayLap = ngayLap;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null) {
			throw new IllegalArgumentException("Nhân viên không được rỗng");
		}
		this.nhanVien = nhanVien;
	}

	public double getTienKhachDua() {
		return tienKhachDua;
	}

	public void setTienKhachDua(double tienKhachDua) {
		if (tienKhachDua < 0) {
			throw new IllegalArgumentException("Tiền khách đưa không hợp lệ");
		}
		this.tienKhachDua = tienKhachDua;
	}

	public List<ChiTietHoaDon> getDsChiTiet() {
		return dsChiTiet;
	}

	public void setDsChiTiet(List<ChiTietHoaDon> dsChiTiet) {
		if (dsChiTiet == null) {
			throw new IllegalArgumentException("Danh sách chi tiết không hợp lệ");
		}
		this.dsChiTiet = dsChiTiet;
		for (ChiTietHoaDon ct : dsChiTiet) {
			if (ct != null && ct.getHoaDon() != this) {
				ct.setHoaDon(this);
			}
		}
	}

	public void themChiTiet(ChiTietHoaDon ct) {
		if (ct == null) {
			throw new IllegalArgumentException("Chi tiết không được rỗng");
		}
		ct.setHoaDon(this);
		this.dsChiTiet.add(ct);
	}

	public double getTongTien() {
		if (dsChiTiet != null && !dsChiTiet.isEmpty()) {
			return tinhTongTien();
		}
		return tongTien;
	}

	public void setTongTien(double tongTien) {
		if (tongTien < 0) {
			throw new IllegalArgumentException("Tổng tiền không được âm");
		}
		this.tongTien = tongTien;
	}

	public double tinhTongTien() {
		if (dsChiTiet != null && !dsChiTiet.isEmpty()) {
			double tong = 0;
			for (ChiTietHoaDon ct : dsChiTiet) {
				if (ct != null) {
					tong += ct.tinhThanhTien();
				}
			}
			this.tongTien = tong;
			return tong;
		}
		return this.tongTien;
	}

	public double tinhTienThua() {
		double tienThua = tienKhachDua - getTongTien();
		return tienThua >= 0 ? tienThua : 0.0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(maHD);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		return Objects.equals(maHD, other.maHD);
	}

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", ngayLap=" + ngayLap + ", nhanVien=" + nhanVien + ", tienKhachDua="
				+ tienKhachDua + ", dsChiTiet=" + dsChiTiet + "]";
	}
}