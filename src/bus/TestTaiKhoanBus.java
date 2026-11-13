package bus;

import java.sql.SQLException;

import entity.TaiKhoan;

public class TestTaiKhoanBus {
	public static void main(String[] args) {

		TaiKhoanBUS bus = new TaiKhoanBUS();

		TaiKhoan tk = new TaiKhoan("nguyenvana", "123456", "Nhân viên", "NV007");

		try {
			boolean result = bus.themTaiKhoan(tk);

			if (result) {
				System.out.println("Them tai khoan thanh cong!");
			} else {
				System.out.println("Them tai khoan that bai!");
			}

		} catch (SQLException e) {
			System.out.println("Loi khi them tai khoan!");
			e.printStackTrace();
		}
	}
}