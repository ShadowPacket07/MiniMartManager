package entity;

public class ChiTietHoaDon {

	private HoaDon hoaDon;
	private SanPham sanPham;
	private int soLuong;
	private double donGia;

	public ChiTietHoaDon() {
	}

	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, double donGia) {
		setHoaDon(hoaDon);
		setSanPham(sanPham);
		setSoLuong(soLuong);
		setDonGia(donGia);
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		if (hoaDon == null) {
			throw new IllegalArgumentException("Hóa đơn không được rỗng");
		}
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		if (sanPham == null) {
			throw new IllegalArgumentException("Sản phẩm không được rỗng");
		}
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng phải > 0");
		}
		this.soLuong = soLuong;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		if (donGia <= 0) {
			throw new IllegalArgumentException("Đơn giá phải > 0");
		}
		this.donGia = donGia;
	}

	public double tinhThanhTien() {
		return soLuong * donGia;
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [maHD=" + hoaDon.getMaHD() + ", maSP=" + sanPham.getMaSP() + ", soLuong=" + soLuong
				+ ", donGia=" + donGia + ", thanhTien=" + tinhThanhTien() + "]";
	}
}