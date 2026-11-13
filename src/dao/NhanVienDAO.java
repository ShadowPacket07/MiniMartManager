package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {
	public ArrayList<NhanVien> getAllNhanVien() throws SQLException {

		ArrayList<NhanVien> danhSach = new ArrayList<>();

		String sql = "SELECT * FROM NhanVien";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {

				NhanVien nv = new NhanVien();

				nv.setMaNhanVien(rs.getString("maNhanVien"));
				nv.setHoTen(rs.getString("hoTen"));
				nv.setSoDienThoai(rs.getString("soDienThoai"));
				nv.setDiaChi(rs.getString("diaChi"));
				nv.setEmail(rs.getString("email"));
				nv.setTienLuong(rs.getDouble("tienLuong"));

				java.sql.Date ngaySinh = rs.getDate("ngaySinh");

				if (ngaySinh != null) {
					nv.setNgaySinh(ngaySinh.toLocalDate());
				}

				nv.setGioiTinh(rs.getString("gioiTinh"));

				danhSach.add(nv);
			}
		}

		return danhSach;
	}

	public NhanVien timTheoMa(String maNV) throws SQLException {

		String sql = "SELECT * FROM NhanVien WHERE maNhanVien = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, maNV);

			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {

					NhanVien nv = new NhanVien();

					nv.setMaNhanVien(rs.getString("maNhanVien"));
					nv.setHoTen(rs.getString("hoTen"));
					nv.setSoDienThoai(rs.getString("soDienThoai"));
					nv.setDiaChi(rs.getString("diaChi"));
					nv.setEmail(rs.getString("email"));
					nv.setTienLuong(rs.getDouble("tienLuong"));

					java.sql.Date ngaySinh = rs.getDate("ngaySinh");

					if (ngaySinh != null) {
						nv.setNgaySinh(ngaySinh.toLocalDate());
					}

					nv.setGioiTinh(rs.getString("gioiTinh"));

					return nv;
				}
			}
		}

		return null;
	}

	public boolean themNhanVien(NhanVien nv) throws SQLException {
		try (Connection conn = ConnectDB.getConnection()) {
			return themNhanVien(conn, nv);
		}
	}

	public boolean themNhanVien(Connection conn, NhanVien nv) throws SQLException {

		String sql = "INSERT INTO NhanVien "
				+ "(maNhanVien, hoTen, soDienThoai, diaChi, email, tienLuong, ngaySinh, gioiTinh) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nv.getMaNhanVien());
			stmt.setString(2, nv.getHoTen());
			stmt.setString(3, nv.getSoDienThoai());
			stmt.setString(4, nv.getDiaChi());
			stmt.setString(5, nv.getEmail());
			stmt.setDouble(6, nv.getTienLuong());

			if (nv.getNgaySinh() != null) {
				stmt.setDate(7, java.sql.Date.valueOf(nv.getNgaySinh()));
			} else {
				stmt.setNull(7, java.sql.Types.DATE);
			}

			stmt.setString(8, nv.getGioiTinh());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean chinhSuaNhanVien(NhanVien nv) throws SQLException {
		String sql = "UPDATE NhanVien SET " + "hoTen = ?, soDienThoai = ?, diaChi = ?, "
				+ "email = ?, tienLuong = ?, ngaySinh = ?, gioiTinh = ? " + "WHERE maNhanVien = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, nv.getHoTen());
			stmt.setString(2, nv.getSoDienThoai());
			stmt.setString(3, nv.getDiaChi());
			stmt.setString(4, nv.getEmail());
			stmt.setDouble(5, nv.getTienLuong());

			if (nv.getNgaySinh() != null) {
				stmt.setDate(6, java.sql.Date.valueOf(nv.getNgaySinh()));
			} else {
				stmt.setNull(6, java.sql.Types.DATE);
			}

			stmt.setString(7, nv.getGioiTinh());
			stmt.setString(8, nv.getMaNhanVien());

			return stmt.executeUpdate() > 0;
		}
	}

	public boolean xoaNhanVien(String maNhanVien) throws SQLException {
		String sql = "DELETE FROM NhanVien WHERE maNhanVien = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, maNhanVien);

			return stmt.executeUpdate() > 0;
		}
	}

	public List<NhanVien> timKiemTheoTen(String ten) throws SQLException {
		List<NhanVien> danhSach = new ArrayList<>();

		String sql = "SELECT * FROM NhanVien WHERE hoTen LIKE ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, "%" + ten + "%");

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					NhanVien nv = new NhanVien();

					nv.setMaNhanVien(rs.getString("maNhanVien"));
					nv.setHoTen(rs.getString("hoTen"));
					nv.setSoDienThoai(rs.getString("soDienThoai"));
					nv.setDiaChi(rs.getString("diaChi"));
					nv.setEmail(rs.getString("email"));
					nv.setTienLuong(rs.getDouble("tienLuong"));

					java.sql.Date ngaySinh = rs.getDate("ngaySinh");
					if (ngaySinh != null) {
						nv.setNgaySinh(ngaySinh.toLocalDate());
					}

					nv.setGioiTinh(rs.getString("gioiTinh"));

					danhSach.add(nv);
				}
			}
		}

		return danhSach;
	}
}
