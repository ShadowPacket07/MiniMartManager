package bus;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import util.Session;

public class TaiKhoanBUS {
	private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

	public TaiKhoan timTheoUsername(String username) {
		if (username == null || username.trim().isEmpty()) {
			return null;
		}
		return taiKhoanDAO.getById(username.trim());
	}

	public boolean themTaiKhoan(TaiKhoan tk) {
		if (tk == null) {
			throw new IllegalArgumentException("Thông tin tài khoản không được để trống");
		}
		if (tk.getNhanVien() == null || tk.getNhanVien().getMaNV() == null) {
			throw new IllegalArgumentException("Tài khoản phải gắn liền với một nhân viên");
		}
		if (taiKhoanDAO.tonTaiTaiKhoanTheoMaNhanVien(tk.getNhanVien().getMaNV())) {
			throw new IllegalArgumentException("Nhân viên này đã có tài khoản trong hệ thống");
		}
		return taiKhoanDAO.them(tk);
	}

	public boolean tonTaiTaiKhoanTheoMaNhanVien(String maNhanVien) {
		if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
			return false;
		}
		return taiKhoanDAO.tonTaiTaiKhoanTheoMaNhanVien(maNhanVien.trim());
	}

	// Dang nhap he thong va tu dong luu Session
	public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập");
		}
		if (matKhau == null || matKhau.trim().isEmpty()) {
			throw new IllegalArgumentException("Vui lòng nhập mật khẩu");
		}

		tenDangNhap = tenDangNhap.trim();
		TaiKhoan tkTonTai = taiKhoanDAO.getById(tenDangNhap);

		if (tkTonTai == null) {
			throw new IllegalArgumentException("Tên đăng nhập không tồn tại trong hệ thống");
		}

		if (!tkTonTai.isTrangThai()) {
			throw new IllegalArgumentException("Tài khoản này đã bị khóa hoặc ngừng hoạt động");
		}

		TaiKhoan tk = taiKhoanDAO.dangNhap(tenDangNhap, matKhau);
		if (tk == null) {
			throw new IllegalArgumentException("Mật khẩu không chính xác");
		}

		// Luu tai khoan vao phien lam viec dung chung toan app
		Session.setTaiKhoan(tk);
		return tk;
	}

	public boolean doiTrangThai(String tenDangNhap, boolean trangThai) {
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
			return false;
		}
		return taiKhoanDAO.doiTrangThai(tenDangNhap.trim(), trangThai);
	}
}
