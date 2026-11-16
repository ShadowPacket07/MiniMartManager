package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {

    private static final String SELECT_COLUMNS =
            "SELECT maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi, vaiTro, trangThai "
            + "FROM dbo.NhanVien";

    // Lấy danh sách tất cả nhân viên
    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> dsNhanVien = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY maNV";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsNhanVien.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsNhanVien;
    }

    // Tìm nhân viên theo mã
    public NhanVien getById(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE maNV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV.trim());

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

    // Thêm nhân viên mới
    public boolean them(NhanVien nv) {
        if (nv == null) return false;
        String sql = "INSERT INTO dbo.NhanVien (maNV, hoTen, gioiTinh, ngaySinh, soDienThoai, diaChi, vaiTro, trangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getGioiTinh());
            if (nv.getNgaySinh() != null) {
                ps.setDate(4, Date.valueOf(nv.getNgaySinh()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            ps.setString(5, nv.getSoDienThoai());
            ps.setString(6, nv.getDiaChi());
            ps.setString(7, nv.getVaiTro());
            ps.setBoolean(8, nv.isTrangThai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin nhân viên
    public boolean capNhat(NhanVien nv) {
        if (nv == null) return false;
        String sql = "UPDATE dbo.NhanVien SET hoTen = ?, gioiTinh = ?, ngaySinh = ?, "
                   + "soDienThoai = ?, diaChi = ?, vaiTro = ?, trangThai = ? "
                   + "WHERE maNV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getGioiTinh());
            if (nv.getNgaySinh() != null) {
                ps.setDate(3, Date.valueOf(nv.getNgaySinh()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            ps.setString(4, nv.getSoDienThoai());
            ps.setString(5, nv.getDiaChi());
            ps.setString(6, nv.getVaiTro());
            ps.setBoolean(7, nv.isTrangThai());
            ps.setString(8, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa nhân viên theo mã
    public boolean xoa(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        String sql = "DELETE FROM dbo.NhanVien WHERE maNV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV.trim());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đổi trạng thái nhân viên (ngừng hoạt động / đi làm lại)
    public boolean doiTrangThai(String maNV, boolean trangThai) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        String sql = "UPDATE dbo.NhanVien SET trangThai = ? WHERE maNV = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, trangThai);
            ps.setString(2, maNV.trim());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tìm kiếm nhân viên theo tên
    public ArrayList<NhanVien> timKiemTheoTen(String ten) {
        ArrayList<NhanVien> ds = new ArrayList<>();
        if (ten == null || ten.trim().isEmpty()) {
            return getAll();
        }
        String sql = SELECT_COLUMNS + " WHERE hoTen LIKE ? ORDER BY maNV";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + ten.trim() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    private static NhanVien mapRow(ResultSet rs) throws SQLException {
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
        nv.setTrangThai(rs.getBoolean("trangThai"));
        return nv;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
