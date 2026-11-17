package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import connectDB.ConnectDB;

/**
 * Truy vấn số liệu cho màn hình Trang chủ. Tham số maNV = null nghĩa là lấy số
 * liệu của toàn cửa hàng (Quản lý); maNV khác null nghĩa là chỉ lấy hóa đơn do
 * nhân viên đó lập (Thu ngân).
 */
public class TrangChuDAO {

	// Sản phẩm đang bán có tồn kho <= ngưỡng này sẽ hiện ở danh sách "tồn thấp"
	public static final int NGUONG_TON_THAP = 60;

	private static final String HOM_NAY = "CAST(hd.NgayLap AS DATE) = CAST(GETDATE() AS DATE)";

	// Trả về {doanh thu hôm nay, số hóa đơn hôm nay, số sản phẩm đã bán hôm nay}
	public double[] tongHopHomNay(String maNV) throws SQLException {
		String loc = (maNV == null) ? "" : " AND hd.MaNV = ?";
		double[] kq = new double[3];

		String sqlHD = "SELECT ISNULL(SUM(hd.TongTien), 0), COUNT(*) FROM HoaDon hd WHERE " + HOM_NAY + loc;
		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sqlHD)) {
			if (maNV != null) {
				ps.setString(1, maNV);
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					kq[0] = rs.getDouble(1);
					kq[1] = rs.getInt(2);
				}
			}
		}

		String sqlSP = "SELECT ISNULL(SUM(ct.SoLuong), 0) FROM ChiTietHoaDon ct "
				+ "JOIN HoaDon hd ON ct.MaHD = hd.MaHD WHERE " + HOM_NAY + loc;
		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sqlSP)) {
			if (maNV != null) {
				ps.setString(1, maNV);
			}
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					kq[2] = rs.getInt(1);
				}
			}
		}
		return kq;
	}

	// Mỗi dòng: {mã HĐ, ngày lập (LocalDateTime), mã NV, tiền khách đưa (Double),
	// tổng tiền (Double)}
	public List<Object[]> hoaDonGanDay(String maNV, int soDong) throws SQLException {
		List<Object[]> ds = new ArrayList<>();
		String sql = "SELECT TOP (?) MaHD, NgayLap, MaNV, TienKhachDua, TongTien FROM HoaDon"
				+ (maNV == null ? "" : " WHERE MaNV = ?") + " ORDER BY NgayLap DESC, MaHD DESC";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, soDong);
			if (maNV != null) {
				ps.setString(2, maNV);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					LocalDateTime ngayLap = rs.getTimestamp("NgayLap").toLocalDateTime();
					ds.add(new Object[] { rs.getString("MaHD"), ngayLap, rs.getString("MaNV"),
							rs.getDouble("TienKhachDua"), rs.getDouble("TongTien") });
				}
			}
		}
		return ds;
	}

	// Mỗi dòng: {mã SP, tên SP, tồn kho (Integer), ghi chú}
	public List<Object[]> sanPhamTonThap() throws SQLException {
		List<Object[]> ds = new ArrayList<>();
		String sql = "SELECT MaSP, TenSP, SoLuongTon FROM SanPham "
				+ "WHERE TrangThai = 1 AND SoLuongTon <= ? ORDER BY SoLuongTon, MaSP";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, NGUONG_TON_THAP);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int ton = rs.getInt("SoLuongTon");
					String chuY = ton == 0 ? "Hết hàng" : (ton <= 20 ? "Sắp hết" : "Theo dõi");
					ds.add(new Object[] { rs.getString("MaSP"), rs.getString("TenSP"), ton, chuY });
				}
			}
		}
		return ds;
	}

	// Doanh thu từng ngày trong 7 ngày gần nhất (kể cả hôm nay); ngày không có đơn
	// = 0
	public Map<LocalDate, Double> doanhThu7Ngay(String maNV) throws SQLException {
		Map<LocalDate, Double> kq = new LinkedHashMap<>();
		for (int i = 6; i >= 0; i--) {
			kq.put(LocalDate.now().minusDays(i), 0.0);
		}

		String sql = "SELECT CAST(hd.NgayLap AS DATE) AS Ngay, SUM(hd.TongTien) AS Tong FROM HoaDon hd "
				+ "WHERE hd.NgayLap >= DATEADD(DAY, -6, CAST(GETDATE() AS DATE))"
				+ (maNV == null ? "" : " AND hd.MaNV = ?") + " GROUP BY CAST(hd.NgayLap AS DATE)";

		try (Connection con = ConnectDB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			if (maNV != null) {
				ps.setString(1, maNV);
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					LocalDate ngay = rs.getDate("Ngay").toLocalDate();
					if (kq.containsKey(ngay)) {
						kq.put(ngay, rs.getDouble("Tong"));
					}
				}
			}
		}
		return kq;
	}
}