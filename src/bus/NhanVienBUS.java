package bus;

import java.util.List;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class NhanVienBUS {
	private NhanVienDAO nhanVienDAO = new NhanVienDAO();
	private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
	private HoaDonDAO hoaDonDAO = new HoaDonDAO();

	public List<NhanVien> getAllNhanVien() {
		return nhanVienDAO.getAll();
	}

	public NhanVien timTheoMa(String maNhanVien) {
		if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
			return null;
		}
		return nhanVienDAO.getById(maNhanVien.trim());
	}

	public List<NhanVien> timKiemTheoTen(String ten) {
		return nhanVienDAO.timKiemTheoTen(ten);
	}

	public boolean themNhanVien(NhanVien nv) {
		if (nv == null) {
			throw new IllegalArgumentException("Thông tin nhân viên không được để trống");
		}
		if (timTheoMa(nv.getMaNV()) != null) {
			throw new IllegalArgumentException("Mã nhân viên " + nv.getMaNV() + " đã tồn tại trong hệ thống");
		}
		boolean thanhCong = nhanVienDAO.them(nv);
		if (thanhCong) {
			// Tự động cấp tài khoản 1-1 cho nhân viên mới theo chuẩn nghiệp vụ Word
			String quyen = (nv.getVaiTro() != null && nv.getVaiTro().equalsIgnoreCase("Quản lý")) ? "QUAN_LY" : "BAN_HANG";
			String matKhau = "12345678"; // Mật khẩu mặc định đạt chuẩn >= 8 ký tự
			try {
				TaiKhoan tk = new TaiKhoan(nv.getMaNV(), matKhau, quyen, nv.isTrangThai(), nv);
				taiKhoanDAO.them(tk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return thanhCong;
	}

	public boolean chinhSuaNhanVien(NhanVien nv) {
		if (nv == null) {
			throw new IllegalArgumentException("Thông tin nhân viên không được để trống");
		}
		if (timTheoMa(nv.getMaNV()) == null) {
			throw new IllegalArgumentException("Không tìm thấy nhân viên " + nv.getMaNV() + " để cập nhật");
		}
		return nhanVienDAO.capNhat(nv);
	}

	// Xu ly xoa hoac ngung hoat dong theo dung nghiep vu trong Word
	public boolean xoaNhanVien(String maNhanVien) {
		if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
			throw new IllegalArgumentException("Mã nhân viên không hợp lệ");
		}
		maNhanVien = maNhanVien.trim();
		NhanVien nv = timTheoMa(maNhanVien);
		if (nv == null) {
			throw new IllegalArgumentException("Không tìm thấy nhân viên cần xóa: " + maNhanVien);
		}

		// Neu nhan vien da tung lap hoa don thi khong xoa cung, ma chuyen trang thai nghi viec
		if (hoaDonDAO.daTungLapHoaDon(maNhanVien)) {
			nhanVienDAO.doiTrangThai(maNhanVien, false);
			taiKhoanDAO.doiTrangThai(maNhanVien, false);
			return true;
		}

		// Neu chua tung phat sinh hoa don: xoa tai khoan va xoa ho so nhan vien
		taiKhoanDAO.xoa(maNhanVien);
		return nhanVienDAO.xoa(maNhanVien);
	}

	public boolean doiTrangThaiNhanVien(String maNV, boolean trangThai) {
		if (maNV == null || maNV.trim().isEmpty()) {
			return false;
		}
		maNV = maNV.trim();
		boolean kq = nhanVienDAO.doiTrangThai(maNV, trangThai);
		if (kq && !trangThai) {
			taiKhoanDAO.doiTrangThai(maNV, false);
		}
		return kq;
	}
}
