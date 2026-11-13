package dao;

import java.sql.SQLException;

import entity.NhanVien;

public class TestNhanVienDAO {

	public static void main(String[] args) {

		NhanVienDAO dao = new NhanVienDAO();

		try {

			NhanVien nv = new NhanVien();

			nv.setMaNhanVien("NV007");
			nv.setHoTen("Nguyễn Văn An");
			nv.setSoDienThoai("0900000006");
			nv.setDiaChi("Thủ Đức, HCM");
			nv.setEmail("an@gmail.com");
			nv.setTienLuong(7500000);
			nv.setNgaySinh(java.time.LocalDate.of(2001, 6, 15));
			nv.setGioiTinh("Nam");

			boolean ketQua = dao.themNhanVien(nv);

			if (ketQua) {
				System.out.println("Them nhan vien thanh cong!");
			} else {
				System.out.println("Them nhan vien that bai!");
			}

		} catch (SQLException e) {
			System.out.println("Loi khi them nhan vien!");
			e.printStackTrace();
		}
	}
}