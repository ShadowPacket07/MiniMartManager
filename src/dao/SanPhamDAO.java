package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.SanPham;

public class SanPhamDAO {

	private static final String SELECT_COLUMNS = "SELECT maSP, tenSP, loaiSP, donGia, soLuongTon, trangThai, hinhAnh FROM dbo.SanPham";

	// Lấy tất cả sản phẩm
	public ArrayList<SanPham> getAll() {
		ArrayList<SanPham> dsSanPham = new ArrayList<>();
		String sql = SELECT_COLUMNS + " ORDER BY maSP";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				dsSanPham.add(mapRow(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dsSanPham;
	}

	// Tìm sản phẩm theo mã
	public SanPham getById(String maSP) {
		if (maSP == null || maSP.trim().isEmpty()) {
			return null;
		}
		String sql = SELECT_COLUMNS + " WHERE maSP = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maSP.trim());

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Thêm sản phẩm mới
	public boolean them(SanPham sp) {
		if (sp == null) {
			return false;
		}
		String sql = "INSERT INTO dbo.SanPham (maSP, tenSP, loaiSP, donGia, soLuongTon, trangThai, hinhAnh) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, sp.getMaSP());
			ps.setString(2, sp.getTenSP());
			ps.setString(3, sp.getLoaiSP());
			ps.setDouble(4, sp.getDonGia());
			ps.setInt(5, sp.getSoLuongTon());
			ps.setBoolean(6, sp.isTrangThai());
			ps.setString(7, sp.getHinhAnh());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật thông tin sản phẩm
	public boolean capNhat(SanPham sp) {
		if (sp == null) {
			return false;
		}
		String sql = "UPDATE dbo.SanPham SET tenSP = ?, loaiSP = ?, donGia = ?, soLuongTon = ?, trangThai = ?, hinhAnh = ? "
				+ "WHERE maSP = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, sp.getTenSP());
			ps.setString(2, sp.getLoaiSP());
			ps.setDouble(3, sp.getDonGia());
			ps.setInt(4, sp.getSoLuongTon());
			ps.setBoolean(5, sp.isTrangThai());
			ps.setString(6, sp.getHinhAnh());
			ps.setString(7, sp.getMaSP());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Cập nhật tồn kho
	public boolean capNhatSoLuongTon(String maSP, int soLuongTon) {
		if (maSP == null || soLuongTon < 0) {
			return false;
		}
		String sql = "UPDATE dbo.SanPham SET soLuongTon = ? WHERE maSP = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, soLuongTon);
			ps.setString(2, maSP.trim());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Đổi trạng thái ngừng kinh doanh
	public boolean ngungKinhDoanh(String maSP) {
		if (maSP == null) {
			return false;
		}
		String sql = "UPDATE dbo.SanPham SET trangThai = 0 WHERE maSP = ?";

		try (Connection conn = ConnectDB.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, maSP.trim());
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static SanPham mapRow(ResultSet rs) throws SQLException {
		SanPham sp = new SanPham();
		sp.setMaSP(nullToEmpty(rs.getString("maSP")));
		sp.setTenSP(nullToEmpty(rs.getString("tenSP")));
		sp.setLoaiSP(nullToEmpty(rs.getString("loaiSP")));
		sp.setDonGia(rs.getDouble("donGia"));
		sp.setSoLuongTon(rs.getInt("soLuongTon"));
		sp.setTrangThai(rs.getBoolean("trangThai"));
		sp.setHinhAnh(rs.getString("HinhAnh"));
		return sp;
	}

	private static String nullToEmpty(String value) {
		return value != null ? value : "";
	}
}
