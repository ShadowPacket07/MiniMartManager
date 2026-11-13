package dao;

import java.sql.Connection;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.TaiKhoan;

public class TestTaiKhoanDAO {

	public static void main(String[] args) {

		TaiKhoanDAO dao = new TaiKhoanDAO();

		try (Connection conn = ConnectDB.getConnection()) {

			TaiKhoan tk = dao.timTheoUsername(conn, "admin");

			if (tk != null) {

				System.out.println("Tim thay tai khoan!");
				System.out.println("Username: " + tk.getUsername());
				System.out.println("Role: " + tk.getRole());
				System.out.println("Ma nhan vien: " + tk.getMaNhanVien());

			} else {
				System.out.println("Khong tim thay tai khoan!");
			}

		} catch (SQLException e) {

			System.out.println("Loi khi truy van tai khoan!");
			e.printStackTrace();
		}
	}
}