package bus;

import java.sql.SQLException;
import java.util.List;

import dao.ThongKeDAO;

public class ThongKeBUS {
	private ThongKeDAO thongKeDAO = new ThongKeDAO();

	public double tongDoanhThu() throws SQLException {
		return thongKeDAO.tongDoanhThu();
	}

	public int tongSoHoaDon() throws SQLException {
		return thongKeDAO.tongSoHoaDon();
	}

	public int tongSanPhamDaBan() throws SQLException {
		return thongKeDAO.tongSanPhamDaBan();
	}

	// Mỗi dòng: {mã sản phẩm, tên sản phẩm, số lượng đã bán}
	public List<Object[]> getSanPhamBanChay() throws SQLException {
		return thongKeDAO.getSanPhamBanChay();
	}
}