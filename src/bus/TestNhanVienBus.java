package bus;

import java.sql.SQLException;

public class TestNhanVienBus {
	public static void main(String[] args) {

		NhanVienBUS bus = new NhanVienBUS();

		try {
			boolean result = bus.xoaNhanVien("NV008");

			if (result) {
				System.out.println("Xoa nhan vien thanh cong!");
			} else {
				System.out.println("Khong tim thay nhan vien!");
			}

		} catch (SQLException e) {
			System.out.println("Xoa nhan vien that bai!");
			e.printStackTrace();
		}
	}
}