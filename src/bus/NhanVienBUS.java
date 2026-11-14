package bus;

import java.sql.SQLException;
import java.util.List;

import dao.NhanVienDAO;
import entity.NhanVien;

public class NhanVienBUS {
	private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
	private NhanVienDAO nhanVienDAO = new NhanVienDAO();

	public boolean themNhanVien(NhanVien nv) throws SQLException {
		return nhanVienDAO.themNhanVien(nv);
	}

	public List<NhanVien> getAllNhanVien() throws SQLException {
		return nhanVienDAO.getAllNhanVien();
	}

	public NhanVien timTheoMa(String maNhanVien) throws SQLException {
		return nhanVienDAO.timTheoMa(maNhanVien);
	}

	public boolean chinhSuaNhanVien(NhanVien nv) throws SQLException {
		return nhanVienDAO.chinhSuaNhanVien(nv);
	}

	public boolean xoaNhanVien(String maNhanVien) throws SQLException {
		NhanVien nv = nhanVienDAO.timTheoMa(maNhanVien);
		if (nv == null) {
			System.out.println("Nhan vien khong ton tai!");
			return false;
		}
		boolean coTaiKhoan = taiKhoanBUS.tonTaiTaiKhoanTheoMaNhanVien(maNhanVien);
		if (coTaiKhoan) {
			System.out.println("Nhan vien dang co tai khoan, khong the xoa!");
			return false;
		}
		return nhanVienDAO.xoaNhanVien(maNhanVien);
	}

	public List<NhanVien> timKiemTheoTen(String ten) throws SQLException {
		return nhanVienDAO.timKiemTheoTen(ten);
	}
}
