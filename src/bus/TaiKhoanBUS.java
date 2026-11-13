
package bus;

import java.sql.Connection;
import java.sql.SQLException;

import connectDB.ConnectDB;
import dao.TaiKhoanDAO;
import entity.TaiKhoan;

public class TaiKhoanBUS {

	private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

	public TaiKhoan timTheoUsername(String username) throws SQLException {

		try (Connection conn = ConnectDB.getConnection()) {
			return taiKhoanDAO.timTheoUsername(conn, username);
		}
	}

	// Them tai khoan cho nhan vien da ton tai
	public boolean themTaiKhoan(TaiKhoan tk) throws SQLException {

		try (Connection conn = ConnectDB.getConnection()) {
			return taiKhoanDAO.themTaiKhoan(conn, tk);
		}
	}

	public boolean tonTaiTaiKhoanTheoMaNhanVien(String maNhanVien) throws SQLException {

		try (Connection conn = ConnectDB.getConnection()) {
			return taiKhoanDAO.tonTaiTaiKhoanTheoMaNhanVien(conn, maNhanVien);
		}
	}
}
