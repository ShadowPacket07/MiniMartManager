package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;

public class ChiTietHoaDonDAO {

    private static final String SELECT_COLUMNS =
            "SELECT ct.maHD, ct.maSP, ct.soLuong, ct.donGia, "
            + "sp.tenSP, sp.loaiSP, sp.soLuongTon, sp.trangThai, sp.hinhAnh "
            + "FROM dbo.ChiTietHoaDon ct "
            + "INNER JOIN dbo.SanPham sp ON ct.maSP = sp.maSP";

    // Lấy tất cả chi tiết hóa đơn
    public ArrayList<ChiTietHoaDon> getAll() {
        ArrayList<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY ct.maHD, ct.maSP";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsChiTiet.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsChiTiet;
    }

    // Lấy chi tiết theo mã hóa đơn
    public ArrayList<ChiTietHoaDon> getByMaHD(String maHD) {
        ArrayList<ChiTietHoaDon> dsChiTiet = new ArrayList<>();
        if (maHD == null || maHD.trim().isEmpty()) {
            return dsChiTiet;
        }
        String sql = SELECT_COLUMNS + " WHERE ct.maHD = ? ORDER BY ct.maSP";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsChiTiet.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsChiTiet;
    }

    // Thêm một dòng chi tiết hóa đơn
    public boolean them(ChiTietHoaDon ct) {
        if (ct == null || ct.getHoaDon() == null || ct.getSanPham() == null) {
            return false;
        }
        String sql = "INSERT INTO dbo.ChiTietHoaDon (maHD, maSP, soLuong, donGia) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getHoaDon().getMaHD());
            ps.setString(2, ct.getSanPham().getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static ChiTietHoaDon mapRow(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(nullToEmpty(rs.getString("maHD")));

        SanPham sp = new SanPham();
        sp.setMaSP(nullToEmpty(rs.getString("maSP")));
        sp.setTenSP(nullToEmpty(rs.getString("tenSP")));
        sp.setLoaiSP(nullToEmpty(rs.getString("loaiSP")));
        sp.setSoLuongTon(rs.getInt("soLuongTon"));
        sp.setTrangThai(rs.getBoolean("trangThai"));
        sp.setHinhAnh(rs.getString("hinhAnh"));

        double donGia = rs.getDouble("donGia");
        sp.setDonGia(donGia);

        ChiTietHoaDon ct = new ChiTietHoaDon();
        ct.setHoaDon(hd);
        ct.setSoLuong(rs.getInt("soLuong"));
        ct.setSanPham(sp);
        ct.setDonGia(donGia);
        return ct;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
