package entity;

import java.util.Objects;

public class SanPham {
	private String maSP;
	private String tenSP;
	private String loaiSP;
	private double donGia;
	private int soLuongTon;
	private boolean trangThai;

	public SanPham() {
		this.trangThai = true;
		this.soLuongTon = 0;
		this.donGia = 0.0;
	}

	public SanPham(String maSP) {
		this();
		setMaSP(maSP);
	}

	public SanPham(String maSP, String tenSP, String loaiSP, double donGia, int soLuongTon, boolean trangThai) {
		setMaSP(maSP);
		setTenSP(tenSP);
		setLoaiSP(loaiSP);
		setDonGia(donGia);
		setSoLuongTon(soLuongTon);
		setTrangThai(trangThai);
	}

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		if (maSP == null || !maSP.trim().matches("^SP\\d{3}$")) {
			throw new IllegalArgumentException("Mã sản phẩm không hợp lệ");
		}
		this.maSP = maSP.trim();
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		if (tenSP == null || tenSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Tên sản phẩm không được rỗng");
		}
		this.tenSP = tenSP.trim();
	}

	public String getLoaiSP() {
		return loaiSP;
	}

	public void setLoaiSP(String loaiSP) {
		if (loaiSP == null || loaiSP.trim().isEmpty()) {
			throw new IllegalArgumentException("Loại sản phẩm không được rỗng");
		}
		this.loaiSP = loaiSP.trim();
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

	public int getSoLuongTon() {
		return soLuongTon;
	}

	public void setSoLuongTon(int soLuongTon) {
		if (soLuongTon < 0) {
			throw new IllegalArgumentException("Số lượng tồn không được âm");
		}
		this.soLuongTon = soLuongTon;
	}

	public boolean isTrangThai() {
		return trangThai;
	}

	public boolean getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}

	public void capNhatTonKho(int slBan) {
		if (slBan <= 0) {
			throw new IllegalArgumentException("Số lượng bán phải > 0");
		}
		if (slBan > this.soLuongTon) {
			throw new IllegalArgumentException("Số lượng tồn kho không đủ để bán");
		}
		this.soLuongTon -= slBan;
	}

	@Override
	public int hashCode() {
		return Objects.hash(maSP);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SanPham other = (SanPham) obj;
		return Objects.equals(maSP, other.maSP);
	}

	@Override
	public String toString() {
		return "SanPham [maSP=" + maSP + ", tenSP=" + tenSP + ", loaiSP=" + loaiSP + ", donGia=" + donGia
				+ ", soLuongTon=" + soLuongTon + ", trangThai=" + trangThai + "]";
	}
}