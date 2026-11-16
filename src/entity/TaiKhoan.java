package entity;

import java.util.Objects;

public class TaiKhoan {
	private String tenDangNhap;
	private String matKhau;
	private String quyen;
	private boolean trangThai;
	private NhanVien nhanVien;

	public TaiKhoan() {
		super();
		this.quyen = "BAN_HANG";
		this.trangThai = true;
	}

	public TaiKhoan(String tenDangNhap) {
		this();
		setTenDangNhap(tenDangNhap);
	}

	public TaiKhoan(String tenDangNhap, String matKhau, String quyen, NhanVien nhanVien) {
		this(tenDangNhap, matKhau, quyen, true, nhanVien);
	}

	public TaiKhoan(String tenDangNhap, String matKhau, String quyen, boolean trangThai, NhanVien nhanVien) {
		super();
		setNhanVien(nhanVien);
		setTenDangNhap(tenDangNhap);
		setMatKhau(matKhau);
		setQuyen(quyen);
		setTrangThai(trangThai);
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên đăng nhập không hợp lệ");
		}
		if (this.nhanVien != null && this.nhanVien.getMaNV() != null) {
			if (!tenDangNhap.trim().equalsIgnoreCase(this.nhanVien.getMaNV())) {
				throw new IllegalArgumentException("Tên đăng nhập phải trùng với mã nhân viên");
			}
		}
		this.tenDangNhap = tenDangNhap.trim();
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		if (matKhau == null || matKhau.length() < 8) {
			throw new IllegalArgumentException("Mật khẩu phải có ít nhất 8 ký tự");
		}
		this.matKhau = matKhau;
	}

	public String getQuyen() {
		return quyen;
	}

	public void setQuyen(String quyen) {
		if (quyen == null || (!quyen.equalsIgnoreCase("QUAN_LY") && !quyen.equalsIgnoreCase("BAN_HANG")
				&& !quyen.equalsIgnoreCase("Quản lý") && !quyen.equalsIgnoreCase("Nhân viên"))) {
			throw new IllegalArgumentException("Quyền không hợp lệ");
		}
		if (quyen.equalsIgnoreCase("Quản lý")) {
			this.quyen = "QUAN_LY";
		} else if (quyen.equalsIgnoreCase("Nhân viên")) {
			this.quyen = "BAN_HANG";
		} else {
			this.quyen = quyen.toUpperCase();
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

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		if (nhanVien == null) {
			throw new IllegalArgumentException("Nhân viên không được rỗng");
		}
		this.nhanVien = nhanVien;
	}

	public boolean kiemTraDangNhap(String mk) {
		if (mk == null || this.matKhau == null) {
			return false;
		}
		return this.matKhau.equals(mk);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tenDangNhap);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(tenDangNhap, other.tenDangNhap);
	}
	@Override
	public String toString() {
		return "TaiKhoan [tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau + ", quyen=" + quyen + ", trangThai="
				+ trangThai + ", nhanVien=" + nhanVien + "]";
	}
}
