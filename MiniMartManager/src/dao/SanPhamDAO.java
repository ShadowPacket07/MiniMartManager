package dao;

import java.util.ArrayList;
import java.util.List;
import entity.SanPham;

public class SanPhamDAO {

	public List<SanPham> getAllSanPham() {
		return new ArrayList<>();
	}

	public SanPham getSanPhamTheoMa(String maSP) {
		return null;
	}

	public boolean themSanPham(SanPham sp) {
		return false;
	}

	public boolean capNhatSanPham(SanPham sp) {
		return false;
	}

	public boolean capNhatSoLuongTon(String maSP, int soLuongTon) {
		return false;
	}
}
