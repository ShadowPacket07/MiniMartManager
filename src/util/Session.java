package util;

import entity.NhanVien;
import entity.TaiKhoan;

public class Session {
    // Luu thong tin tai khoan dang dang nhap (dung chung toan ung dung)
    public static TaiKhoan taiKhoanHienTai = null;

    // Gan tai khoan khi dang nhap thanh cong
    public static void setTaiKhoan(TaiKhoan tk) {
        taiKhoanHienTai = tk;
    }

    public static TaiKhoan getTaiKhoan() {
        return taiKhoanHienTai;
    }

    public static boolean isDaDangNhap() {
        return taiKhoanHienTai != null;
    }

    // Kiem tra quyen: tra ve true neu la Quan ly, false neu la Nhan vien ban hang
    public static boolean isQuanLy() {
        if (taiKhoanHienTai == null) {
            return false;
        }
        String quyen = taiKhoanHienTai.getQuyen();
        if (quyen != null && quyen.equalsIgnoreCase("QUAN_LY")) {
            return true;
        }
        NhanVien nv = taiKhoanHienTai.getNhanVien();
        if (nv != null && nv.getVaiTro() != null) {
            return nv.getVaiTro().equalsIgnoreCase("Quản lý");
        }
        return false;
    }

    // Lay ho ten nhan vien de hien thi tren tieu de/giao dien
    public static String getTenNguoiDung() {
        if (taiKhoanHienTai != null && taiKhoanHienTai.getNhanVien() != null) {
            return taiKhoanHienTai.getNhanVien().getHoTen();
        }
        return "Chưa đăng nhập";
    }

    // Lay ma nhan vien hien tai (dung khi tao hoa don POS)
    public static String getMaNVHienTai() {
        if (taiKhoanHienTai != null && taiKhoanHienTai.getNhanVien() != null) {
            return taiKhoanHienTai.getNhanVien().getMaNV();
        }
        return null;
    }

    // Lay doi tuong NhanVien day du
    public static NhanVien getNhanVienHienTai() {
        return (taiKhoanHienTai != null) ? taiKhoanHienTai.getNhanVien() : null;
    }

    // Xoa phien lam viec khi dang xuat
    public static void clear() {
        taiKhoanHienTai = null;
    }
}
