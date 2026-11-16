package entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class NhanVien {
	private String maNV;
	private String hoTen;
	private String gioiTinh;
	private LocalDate ngaySinh;
	private String soDienThoai;
	private String diaChi;
	private String vaiTro;
	private boolean trangThai;

	public NhanVien() {
		super();
		this.gioiTinh = "Nam";
		this.vaiTro = "Nhân viên bán hàng";
		this.trangThai = true;
	}

	public NhanVien(String maNV) {
		this();
		setMaNV(maNV);
	}

	public NhanVien(String maNV, String hoTen, String gioiTinh, LocalDate ngaySinh, String soDienThoai, String diaChi,
			String vaiTro, boolean trangThai) {
		super();
		setMaNV(maNV);
		setHoTen(hoTen);
		setGioiTinh(gioiTinh);
		setNgaySinh(ngaySinh);
		setSoDienThoai(soDienThoai);
		setDiaChi(diaChi);
		setVaiTro(vaiTro);
		setTrangThai(trangThai);
	}

	public String getMaNV() {
		return maNV;
	}

	public void setMaNV(String maNV) {
		if (maNV == null || !maNV.trim().matches("^(NV|QL)\\d{3}$")) {
			throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
		}
		this.maNV = maNV.trim();
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		if (hoTen == null || hoTen.trim().isEmpty() || !hoTen.trim().matches("^[\\p{L}\\s]+$")) {
			throw new IllegalArgumentException("Họ tên không hợp lệ");
		}
		this.hoTen = hoTen.trim();
	}

	public String getGioiTinh() {
		return gioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		if (gioiTinh == null || (!gioiTinh.equalsIgnoreCase("Nam") && !gioiTinh.equalsIgnoreCase("Nữ")
				&& !gioiTinh.equalsIgnoreCase("Khác"))) {
			throw new IllegalArgumentException("Giới tính không hợp lệ");
		}
		this.gioiTinh = gioiTinh.trim();
	}

	public LocalDate getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDate ngaySinh) {
		if (ngaySinh == null) {
			throw new IllegalArgumentException("Ngày sinh không được rỗng");
		}
		int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
		if (tuoi < 18 || tuoi > 65) {
			throw new IllegalArgumentException("Tuổi không hợp lệ");
		}
		this.ngaySinh = ngaySinh;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		if (soDienThoai == null || !soDienThoai.trim().matches("^0[35789]\\d{8}$")) {
			throw new IllegalArgumentException("Số điện thoại không hợp lệ");
		}
		this.soDienThoai = soDienThoai.trim();
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = (diaChi == null) ? null : diaChi.trim();
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		if (vaiTro == null || (!vaiTro.equalsIgnoreCase("Quản lý")
				&& !vaiTro.equalsIgnoreCase("Nhân viên bán hàng")
				&& !vaiTro.equalsIgnoreCase("Bán hàng"))) {
			throw new IllegalArgumentException("Vai trò không hợp lệ");
		}
		if (vaiTro.equalsIgnoreCase("Bán hàng") || vaiTro.equalsIgnoreCase("Nhân viên bán hàng")) {
			this.vaiTro = "Nhân viên bán hàng";
		} else {
			this.vaiTro = "Quản lý";
		}
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maNV);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NhanVien other = (NhanVien) obj;
		return Objects.equals(maNV, other.maNV);
	}
	@Override
	public String toString() {
		return "NhanVien [maNV=" + maNV + ", hoTen=" + hoTen + ", gioiTinh=" + gioiTinh + ", ngaySinh=" + ngaySinh
				+ ", soDienThoai=" + soDienThoai + ", diaChi=" + diaChi + ", vaiTro=" + vaiTro + ", trangThai="
				+ trangThai + "]";
	}
}
