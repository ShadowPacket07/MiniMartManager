package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {

    private static final String SELECT_COLUMNS =
            "SELECT tk.tenDangNhap, tk.matKhau, tk.quyen, tk.trangThai, tk.maNV, "
            + "nv.hoTen, nv.gioiTinh, nv.ngaySinh, nv.soDienThoai, nv.diaChi, nv.vaiTro, nv.trangThai AS trangThaiNV "
            + "FROM dbo.TaiKhoan tk "
            + "INNER JOIN dbo.NhanVien nv ON tk.maNV = nv.maNV";

    // Lấy tất cả tài khoản
    public ArrayList<TaiKhoan> getAll() {
        ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY tk.maNV";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                dsTaiKhoan.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dsTaiKhoan;
    }

    // Tìm tài khoản theo tên đăng nhập
    public TaiKhoan getById(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE tk.tenDangNhap = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap.trim());

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

    // Thêm tài khoản
    public boolean them(TaiKhoan tk) {
        if (tk == null || tk.getNhanVien() == null) return false;
        String sql = "INSERT INTO dbo.TaiKhoan (tenDangNhap, matKhau, quyen, trangThai, maNV) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getTenDangNhap());
            ps.setString(2, tk.getMatKhau());
            ps.setString(3, tk.getQuyen());
            ps.setBoolean(4, tk.isTrangThai());
            ps.setString(5, tk.getNhanVien().getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Đổi trạng thái tài khoản (khóa / kích hoạt)
    public boolean doiTrangThai(String tenDangNhap, boolean trangThai) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return false;
        String sql = "UPDATE dbo.TaiKhoan SET trangThai = ? WHERE tenDangNhap = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, trangThai);
            ps.setString(2, tenDangNhap.trim());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa tài khoản theo tên đăng nhập
    public boolean xoa(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) return false;
        String sql = "DELETE FROM dbo.TaiKhoan WHERE tenDangNhap = ?";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap.trim());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra nhân viên đã có tài khoản chưa
    public boolean tonTaiTaiKhoanTheoMaNhanVien(String maNV) {
        if (maNV == null || maNV.trim().isEmpty()) return false;
        String sql = "SELECT 1 FROM dbo.TaiKhoan WHERE maNV = ?";

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

    // Đăng nhập hệ thống
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || matKhau == null) {
            return null;
        }
        String sql = SELECT_COLUMNS + " WHERE tk.tenDangNhap = ? AND tk.matKhau = ? AND tk.trangThai = 1";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tenDangNhap.trim());
            ps.setString(2, matKhau);

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

    private static TaiKhoan mapRow(ResultSet rs) throws SQLException {
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

        TaiKhoan tk = new TaiKhoan();
        tk.setNhanVien(nv);
        tk.setTenDangNhap(nullToEmpty(rs.getString("tenDangNhap")));
        tk.setMatKhau(nullToEmpty(rs.getString("matKhau")));
        tk.setQuyen(nullToEmpty(rs.getString("quyen")));
        tk.setTrangThai(rs.getBoolean("trangThai"));
        return tk;
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }
}
