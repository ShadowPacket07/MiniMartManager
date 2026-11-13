package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.TaiKhoan;

public class TaiKhoanDAO {

	public TaiKhoan timTheoUsername(Connection conn, String username) throws SQLException {

		String sql = "SELECT * FROM TaiKhoan WHERE username = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, username);

			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {

					TaiKhoan tk = new TaiKhoan();

					tk.setUsername(rs.getString("username"));
					tk.setPassword(rs.getString("password"));
					tk.setRole(rs.getString("role"));
					tk.setMaNhanVien(rs.getString("maNhanVien"));

					return tk;
				}
			}
		}

		return null;
	}

	public boolean themTaiKhoan(Connection conn, TaiKhoan tk) throws SQLException {

		String sql = "INSERT INTO TaiKhoan " + "(username, password, role, maNhanVien) " + "VALUES (?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, tk.getUsername());
			stmt.setString(2, tk.getPassword());
			stmt.setString(3, tk.getRole());
			stmt.setString(4, tk.getMaNhanVien());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean tonTaiTaiKhoanTheoMaNhanVien(Connection conn, String maNhanVien) throws SQLException {

		String sql = "SELECT 1 FROM TaiKhoan WHERE maNhanVien = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, maNhanVien);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}

}