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
	private List<ChiTietHoaDon> dsChiTiet;

	public HoaDon() {
		this.ngayLap = LocalDateTime.now();
		this.dsChiTiet = new ArrayList<>();
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

	public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVien, double tienKhachDua,
			List<ChiTietHoaDon> dsChiTiet) {
		this.maHD = maHD;
		this.ngayLap = ngayLap;
		this.nhanVien = nhanVien;
		this.tienKhachDua = tienKhachDua;
		setDsChiTiet(dsChiTiet);
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		if (maHD == null || !maHD.trim().matches("^HD\\d{3}$"))
			throw new IllegalArgumentException("Mã hóa đơn không hợp lệ");
		this.maHD = maHD.trim();
	}

	public LocalDateTime getNgayLap() {
		return ngayLap;
	}

	public void setNgayLap(LocalDateTime ngayLap) {
		if (ngayLap == null || ngayLap.isAfter(LocalDateTime.now()))
			throw new IllegalArgumentException("Ngày lập không hợp lệ");
		this.ngayLap = ngayLap;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null)
			throw new IllegalArgumentException("Nhân viên không được rỗng");
		this.nhanVien = nhanVien;
	}

	public double getTienKhachDua() {
		return tienKhachDua;
	}

	public void setTienKhachDua(double tienKhachDua) {
		if (tienKhachDua < 0)
			throw new IllegalArgumentException("Tiền khách đưa không hợp lệ");
		this.tienKhachDua = tienKhachDua;
	}

	public List<ChiTietHoaDon> getDsChiTiet() {
		return dsChiTiet;
	}

	public void setDsChiTiet(List<ChiTietHoaDon> dsChiTiet) {
		if (dsChiTiet == null)
			throw new IllegalArgumentException("Danh sách chi tiết không hợp lệ");
		this.dsChiTiet = dsChiTiet;
		for (ChiTietHoaDon ct : dsChiTiet) {
			if (ct != null && ct.getHoaDon() != this)
				ct.setHoaDon(this);
		}
	}

	public void themChiTiet(ChiTietHoaDon ct) {
		if (ct == null)
			throw new IllegalArgumentException("Chi tiết không được rỗng");
		ct.setHoaDon(this);
		this.dsChiTiet.add(ct);
	}

	public double getTongTien() {
		return tinhTongTien();
	}

	public double tinhTongTien() {
		double tong = 0;
		if (dsChiTiet != null) {
			for (ChiTietHoaDon ct : dsChiTiet) {
				if (ct != null)
					tong += ct.tinhThanhTien();
			}
		}
		return tong;
	}

	public double tinhTienThua() {
		double tienThua = tienKhachDua - tinhTongTien();
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
