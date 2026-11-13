
package bus;

import java.sql.SQLException;
import java.util.List;

import dao.NhanVienDAO;
import entity.NhanVien;

public class NhanVienBUS {
	private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
	private NhanVienDAO nhanVienDAO = new NhanVienDAO();
//	private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

	public boolean themNhanVien(NhanVien nv) throws SQLException {

		return nhanVienDAO.themNhanVien(nv);
	}

	// Lấy danh sách nhân viên
	public List<NhanVien> getAllNhanVien() throws SQLException {
		return nhanVienDAO.getAllNhanVien();
	}

	// Tìm nhân viên theo mã
	public NhanVien timTheoMa(String maNhanVien) throws SQLException {
		return nhanVienDAO.timTheoMa(maNhanVien);
	}

	// Chỉnh sửa thông tin nhân viên
	public boolean chinhSuaNhanVien(NhanVien nv) throws SQLException {
		return nhanVienDAO.chinhSuaNhanVien(nv);
	}

	// Xóa nhân viên
	public boolean xoaNhanVien(String maNhanVien) throws SQLException {

		// Kiểm tra nhân viên có tồn tại không
		NhanVien nv = nhanVienDAO.timTheoMa(maNhanVien);

		if (nv == null) {
			System.out.println("Nhan vien khong ton tai!");
			return false;
		}

		// Kiểm tra nhân viên có tài khoản không
		boolean coTaiKhoan = taiKhoanBUS.tonTaiTaiKhoanTheoMaNhanVien(maNhanVien);

		if (coTaiKhoan) {
			System.out.println("Nhan vien dang co tai khoan, khong the xoa!");
			return false;
		}

		// Tiến hành xóa nhân viên
		return nhanVienDAO.xoaNhanVien(maNhanVien);
	}

	// Tìm kiếm nhân viên theo tên
	public List<NhanVien> timKiemTheoTen(String ten) throws SQLException {
		return nhanVienDAO.timKiemTheoTen(ten);
	}

}
