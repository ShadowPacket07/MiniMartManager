package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.DatabaseConnection;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

public class ChiTietHoaDonDAO {

	// Thêm 1 dòng chi tiết vào hóa đơn
	public boolean themChiTiet(ChiTietHoaDon ct) {
		String sql = "INSERT INTO ChiTietHoaDon(maHoaDon, maSanPham, soLuong, giaTien) VALUES (?, ?, ?, ?)";
		Connection con = DatabaseConnection.getConnection();

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ct.getHoaDon().getMaHD());
			ps.setString(2, ct.getSanPham().getMaSP());
			ps.setInt(3, ct.getSoLuong());
			ps.setDouble(4, ct.getDonGia());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Lấy danh sách chi tiết của 1 hóa đơn (dùng cho HoaDonGUI xem chi tiết)
	public ArrayList<ChiTietHoaDon> getChiTietTheoHoaDon(HoaDon hd) {
		ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
		String sql = "SELECT ct.maSanPham, sp.tenSanPham, ct.soLuong, ct.giaTien "
				+ "FROM ChiTietHoaDon ct JOIN SanPham sp ON ct.maSanPham = sp.maSanPham "
				+ "WHERE ct.maHoaDon = ?";
		Connection con = DatabaseConnection.getConnection();

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, hd.getMaHD());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				SanPham sp = new SanPham();
				sp.setMaSP(rs.getString("maSanPham"));
				sp.setTenSP(rs.getString("tenSanPham"));

				ChiTietHoaDon ct = new ChiTietHoaDon(hd, sp, rs.getInt("soLuong"), rs.getDouble("giaTien"));
				ds.add(ct);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ds;
	}
}