package entity;

import java.util.Objects;

public class ChiTietHoaDon {
	private HoaDon hoaDon;
	private SanPham sanPham;
	private int soLuong;
	private double donGia;

	public ChiTietHoaDon() {
		this.soLuong = 1;
		this.donGia = 0.0;
	}

	public ChiTietHoaDon(SanPham sanPham, int soLuong, double donGia) {
		this();
		setSanPham(sanPham);
		setSoLuong(soLuong);
		setDonGia(donGia);
	}

	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong, double donGia) {
		this.sanPham = sanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
		setHoaDon(hoaDon);
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		if (hoaDon == null)
			throw new IllegalArgumentException("Hóa đơn không được rỗng");
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		if (sanPham == null)
			throw new IllegalArgumentException("Sản phẩm không được rỗng");
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		if (soLuong <= 0)
			throw new IllegalArgumentException("Số lượng phải > 0");
		this.soLuong = soLuong;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		if (donGia <= 0)
			throw new IllegalArgumentException("Đơn giá phải > 0");
		this.donGia = donGia;
	}

	public double getThanhTien() {
		return tinhThanhTien();
	}

	public double tinhThanhTien() {
		return soLuong * donGia;
	}

	@Override
	public int hashCode() {
		return Objects.hash(hoaDon, sanPham);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChiTietHoaDon other = (ChiTietHoaDon) obj;
		return Objects.equals(hoaDon, other.hoaDon) && Objects.equals(sanPham, other.sanPham);
	}

	@Override
	public String toString() {
		return "ChiTietHoaDon [hoaDon=" + hoaDon + ", sanPham=" + sanPham + ", soLuong=" + soLuong + ", donGia="
				+ donGia + "]";
	}
}
