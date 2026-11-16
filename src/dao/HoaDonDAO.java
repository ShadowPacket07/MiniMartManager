package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.NhanVien;

public class HoaDonDAO {

    private static final String SELECT_COLUMNS =
            "SELECT hd.maHD, hd.ngayLap, hd.tienKhachDua, hd.tongTien, hd.maNV, "
            + "nv.hoTen, nv.gioiTinh, nv.ngaySinh, nv.soDienThoai, nv.diaChi, nv.vaiTro, nv.trangThai AS trangThaiNV "
            + "FROM dbo.HoaDon hd "
            + "INNER JOIN dbo.NhanVien nv ON hd.maNV = nv.maNV";

    // Lấy tất cả hóa đơn
    public ArrayList<HoaDon> getAll() {
        ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY hd.ngayLap DESC, hd.maHD DESC";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsHoaDon.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsHoaDon;
    }

    // Tìm hóa đơn theo mã
    public HoaDon getById(String maHD) {
        if (maHD == null || maHD.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE hd.maHD = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maHD.trim());

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

    // Thêm hóa đơn
    public boolean them(HoaDon hd) {
        if (hd == null || hd.getNhanVien() == null) return false;
        String sql = "INSERT INTO dbo.HoaDon (maHD, ngayLap, maNV, tienKhachDua, tongTien) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hd.getMaHD());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap() != null ? hd.getNgayLap() : LocalDateTime.now()));
            ps.setString(3, hd.getNhanVien().getMaNV());
            ps.setDouble(4, hd.getTienKhachDua());
            ps.setDouble(5, hd.tinhTongTien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra nhân viên đã từng phát sinh hóa đơn bán hàng hay chưa
    public boolean daTungLapHoaDon(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        String sql = "SELECT 1 FROM dbo.HoaDon WHERE maNV = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lưu hóa đơn và chi tiết bằng Transaction
    public boolean luuHoaDonVaChiTiet(HoaDon hd) {
        if (hd == null || hd.getNhanVien() == null || hd.getDsChiTiet() == null || hd.getDsChiTiet().isEmpty()) {
            return false;
        }

        Connection conn = ConnectDB.getConnection();
        if (conn == null) return false;

        boolean oldAutoCommit = true;
        try {
            oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // 1. Thêm hóa đơn
            String sqlHD = "INSERT INTO dbo.HoaDon (maHD, ngayLap, maNV, tienKhachDua, tongTien) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psHD = conn.prepareStatement(sqlHD)) {
                psHD.setString(1, hd.getMaHD());
                psHD.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap() != null ? hd.getNgayLap() : LocalDateTime.now()));
                psHD.setString(3, hd.getNhanVien().getMaNV());
                psHD.setDouble(4, hd.getTienKhachDua());
                psHD.setDouble(5, hd.tinhTongTien());
                psHD.executeUpdate();
            }

            // 2. Thêm chi tiết và trừ tồn kho
            String sqlCT = "INSERT INTO dbo.ChiTietHoaDon (maHD, maSP, soLuong, donGia) VALUES (?, ?, ?, ?)";
            String sqlKho = "UPDATE dbo.SanPham SET soLuongTon = soLuongTon - ? WHERE maSP = ? AND soLuongTon >= ?";

            try (PreparedStatement psCT = conn.prepareStatement(sqlCT);
                 PreparedStatement psKho = conn.prepareStatement(sqlKho)) {

                for (ChiTietHoaDon ct : hd.getDsChiTiet()) {
                    psCT.setString(1, hd.getMaHD());
                    psCT.setString(2, ct.getSanPham().getMaSP());
                    psCT.setInt(3, ct.getSoLuong());
                    psCT.setDouble(4, ct.getDonGia());
                    psCT.executeUpdate();

                    psKho.setInt(1, ct.getSoLuong());
                    psKho.setString(2, ct.getSanPham().getMaSP());
                    psKho.setInt(3, ct.getSoLuong());
                    if (psKho.executeUpdate() == 0) {
                        throw new SQLException("Hết hàng trong kho: " + ct.getSanPham().getMaSP());
                    }
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(oldAutoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Các hàm thống kê số liệu
    public double getTongDoanhThu() {
        String sql = "SELECT SUM(tongTien) FROM dbo.HoaDon";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongSoDon() {
        String sql = "SELECT COUNT(maHD) FROM dbo.HoaDon";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTongSoLuongBan() {
        String sql = "SELECT SUM(soLuong) FROM dbo.ChiTietHoaDon";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getTop5BanChay() {
        List<Object[]> ds = new ArrayList<>();
        String sql = "SELECT TOP 5 sp.maSP, sp.tenSP, sp.loaiSP, SUM(ct.soLuong) AS daBan, SUM(ct.soLuong * ct.donGia) AS doanhThu "
                   + "FROM dbo.ChiTietHoaDon ct INNER JOIN dbo.SanPham sp ON ct.maSP = sp.maSP "
                   + "GROUP BY sp.maSP, sp.tenSP, sp.loaiSP "
                   + "ORDER BY daBan DESC";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ds.add(new Object[] {
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getString("loaiSP"),
                    rs.getInt("daBan"),
                    rs.getDouble("doanhThu")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    private static HoaDon mapRow(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNV(nullToEmpty(rs.getString("maNV")));
        nv.setHoTen(nullToEmpty(rs.getString("hoTen")));
        nv.setGioiTinh(nullToEmpty(rs.getString("gioiTinh")));
        Date ngaySinh = rs.getDate("ngaySinh");
        if (ngaySinh != null) {
            nv.setNgaySinh(ngaySinh.toLocalDate());
        }
        nv.setSoDienThoai(nullToEmpty(rs.getString("soDienThoai")));
        nv.setDiaChi(rs.getString("diaChi"));
        nv.setVaiTro(nullToEmpty(rs.getString("vaiTro")));
        nv.setTrangThai(rs.getBoolean("trangThaiNV"));

        HoaDon hd = new HoaDon();
        hd.setMaHD(nullToEmpty(rs.getString("maHD")));
        Timestamp ngayLap = rs.getTimestamp("ngayLap");
        if (ngayLap != null) {
            hd.setNgayLap(ngayLap.toLocalDateTime());
        }
        hd.setNhanVien(nv);
        hd.setTienKhachDua(rs.getDouble("tienKhachDua"));
        hd.setTongTien(rs.getDouble("tongTien"));
        return hd;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
