
package entity;

public class TaiKhoan {

	private String username;
	private String password;
	private String role;
	private String maNhanVien;

	public TaiKhoan() {
	}

	public TaiKhoan(String username, String password, String role, String maNhanVien) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.maNhanVien = maNhanVien;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMaNhanVien() {
		return maNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
}
