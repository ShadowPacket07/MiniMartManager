package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;

public class ThongKeDAO {

	// Tổng doanh thu của tất cả hóa đơn
	public double tongDoanhThu() throws SQLException {
		String sql = "SELECT ISNULL(SUM(tongTien), 0) FROM HoaDon";

		try (Connection con = ConnectDB.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getDouble(1);
			}
		}
		return 0;
	}

	// Tổng số hóa đơn đã lập
	public int tongSoHoaDon() throws SQLException {
		String sql = "SELECT COUNT(*) FROM HoaDon";

		try (Connection con = ConnectDB.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	// Tổng số lượng sản phẩm đã bán
	public int tongSanPhamDaBan() throws SQLException {
		String sql = "SELECT ISNULL(SUM(soLuong), 0) FROM ChiTietHoaDon";

		try (Connection con = ConnectDB.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	// Danh sách tất cả sản phẩm, sắp xếp từ bán chạy nhất đến ít nhất
	// Mỗi dòng gồm: {mã sản phẩm, tên sản phẩm, số lượng đã bán}
	// Dùng LEFT JOIN để sản phẩm chưa bán lần nào vẫn hiện (số lượng 0)
	public List<Object[]> getSanPhamBanChay() throws SQLException {
		List<Object[]> ds = new ArrayList<>();
		String sql = "SELECT sp.maSanPham, sp.tenSanPham, ISNULL(SUM(ct.soLuong), 0) AS tongBan "
				+ "FROM SanPham sp LEFT JOIN ChiTietHoaDon ct ON sp.maSanPham = ct.maSanPham "
				+ "GROUP BY sp.maSanPham, sp.tenSanPham " + "ORDER BY tongBan DESC, sp.maSanPham";

		try (Connection con = ConnectDB.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ds.add(new Object[] { rs.getString("maSanPham"), rs.getString("tenSanPham"), rs.getInt("tongBan") });
			}
		}
		return ds;
	}
}