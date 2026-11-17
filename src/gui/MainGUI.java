package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.TaiKhoan;
import util.Session;

public class MainGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private TaiKhoan taiKhoan;
	private JLabel lblTitle;
	private JLabel lblAccount;

	// Màu sắc giao diện (Xanh dương #155EEF chuẩn đồng bộ)
	private Color sidebarColor = new Color(21, 94, 239);

	// Các thành phần chính
	private JPanel pSidebar;
	private JPanel pMain;
	private JPanel pHeader;
	private JPanel pContent;
	private CardLayout cardLayout;

	// Các màn hình con
	private TrangChuGUI trangChuGUI;
	private SanPhamGUI sanPhamGUI;
	private BanHangGUI banHangGUI;
	private HoaDonGUI hoaDonGUI;
	private NhanVienGUI nhanVienGUI;
	private ThongKeGUI thongKeGUI;

	// Các nút Menu
	private JButton btnTrangChu = new JButton("Trang chủ");
	private JButton btnNhanVien = new JButton("Quản lý nhân viên");
	private JButton btnSanPham = new JButton("Quản lý sản phẩm");
	private JButton btnBanHang = new JButton("Bán hàng (POS)");
	private JButton btnHoaDon = new JButton("Quản lý hóa đơn");
	private JButton btnThongKe = new JButton("Thống kê báo cáo");
	private JButton btnDangXuat = new JButton("Đăng Xuất");

	private JButton[] menuButtons = { btnTrangChu, btnNhanVien, btnSanPham, btnBanHang, btnHoaDon, btnThongKe };

	public MainGUI(TaiKhoan taiKhoan) {
		this.taiKhoan = taiKhoan;
		if (Session.getTaiKhoan() == null) {
			Session.setTaiKhoan(taiKhoan);
		}

		// 1. Thiết lập cửa sổ
		setTitle("GọnMart Manager - Hệ Thống Quản Lý Siêu Thị Tiện Lợi");
		setSize(1250, 780);
		setMinimumSize(new Dimension(1000, 650));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());

		// 2. Khởi tạo Sidebar
		pSidebar = new JPanel();
		pSidebar.setBackground(sidebarColor);
		pSidebar.setPreferredSize(new Dimension(240, 780));
		pSidebar.setLayout(new BoxLayout(pSidebar, BoxLayout.Y_AXIS));

		// Logo tương đối (fallback an toàn đa tầng)
		URL logoUrl = getClass().getResource("/imgs/logo.png");
		if (logoUrl == null) {
			logoUrl = getClass().getResource("/logo.png");
		}
		ImageIcon originalIcon = null;
		if (logoUrl != null) {
			originalIcon = new ImageIcon(logoUrl);
		} else if (new java.io.File("imgs/logo.png").exists()) {
			originalIcon = new ImageIcon("imgs/logo.png");
		}
		JLabel lblLogo = new JLabel();
		if (originalIcon != null) {
			Image scaledImage = originalIcon.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
			lblLogo.setIcon(new ImageIcon(scaledImage));
		}
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		pSidebar.add(Box.createVerticalStrut(15));
		pSidebar.add(lblLogo);

		JLabel lblBranch = new JLabel("GọnMart Convenience");
		lblBranch.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblBranch.setForeground(Color.WHITE);
		lblBranch.setAlignmentX(Component.CENTER_ALIGNMENT);
		pSidebar.add(Box.createVerticalStrut(5));
		pSidebar.add(lblBranch);

		// Nút Đăng Xuất
		btnDangXuat.setForeground(new Color(220, 38, 38));
		btnDangXuat.setBackground(Color.WHITE);
		btnDangXuat.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnDangXuat.setFocusPainted(false);
		btnDangXuat.setBorderPainted(false);
		btnDangXuat.setOpaque(true);
		btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDangXuat.setPreferredSize(new Dimension(210, 42));
		btnDangXuat.setMaximumSize(new Dimension(210, 42));

		// Giao diện chung các nút Menu
		for (JButton btn : menuButtons) {
			btn.setForeground(Color.WHITE);
			btn.setBackground(sidebarColor);
			btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
			btn.setFocusPainted(false);
			btn.setBorderPainted(false);
			btn.setOpaque(true);
			btn.setAlignmentX(Component.CENTER_ALIGNMENT);
			btn.setPreferredSize(new Dimension(210, 42));
			btn.setMaximumSize(new Dimension(210, 42));
		}

		// Nút Trang chủ được chọn mặc định
		btnTrangChu.setBackground(Color.WHITE);
		btnTrangChu.setForeground(sidebarColor);
		btnTrangChu.setFont(new Font("Times New Roman", Font.BOLD, 14));

		// Phân quyền theo vai trò trong Session
		boolean isManager = Session.isQuanLy();
		btnNhanVien.setVisible(isManager);
		btnSanPham.setVisible(isManager);
		btnThongKe.setVisible(isManager);

		if (!isManager) {
			btnHoaDon.setText("Tra cứu hóa đơn");
		}

		pSidebar.add(Box.createVerticalStrut(20));
		for (JButton btn : menuButtons) {
			pSidebar.add(btn);
			pSidebar.add(Box.createVerticalStrut(8));
		}

		pSidebar.add(Box.createVerticalGlue());
		pSidebar.add(btnDangXuat);
		pSidebar.add(Box.createVerticalStrut(20));

		// 3. Khởi tạo khu vực nội dung chính
		pMain = new JPanel(new BorderLayout());
		pMain.setBackground(new Color(243, 246, 252));

		pHeader = new JPanel(new BorderLayout());
		pHeader.setBackground(Color.WHITE);
		pHeader.setBorder(new EmptyBorder(12, 25, 12, 25));

		lblTitle = new JLabel("Trang chủ");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTitle.setForeground(new Color(23, 43, 77));

		String tenHienThi = Session.getTenNguoiDung();
		String vaiTroStr = isManager ? "Quản lý" : "Nhân viên bán hàng";
		lblAccount = new JLabel("<html><div style='text-align: right;'><b>" + tenHienThi
				+ "</b><br><span style='color: gray;'>" + vaiTroStr + "</span></div></html>");
		lblAccount.setFont(new Font("Times New Roman", Font.PLAIN, 13));

		pHeader.add(lblTitle, BorderLayout.WEST);
		pHeader.add(lblAccount, BorderLayout.EAST);
		pMain.add(pHeader, BorderLayout.NORTH);

		// Khởi tạo CardLayout & các panel
		cardLayout = new CardLayout();
		pContent = new JPanel(cardLayout);

		trangChuGUI = new TrangChuGUI(this);
		sanPhamGUI = new SanPhamGUI();
		banHangGUI = new BanHangGUI();
		hoaDonGUI = new HoaDonGUI();
		nhanVienGUI = new NhanVienGUI();
		thongKeGUI = new ThongKeGUI();

		pContent.add(trangChuGUI, "TRANG_CHU");
		pContent.add(nhanVienGUI, "NHAN_VIEN");
		pContent.add(sanPhamGUI, "SAN_PHAM");
		pContent.add(banHangGUI, "BAN_HANG");
		pContent.add(hoaDonGUI, "HOA_DON");
		pContent.add(thongKeGUI, "THONG_KE");

		pMain.add(pContent, BorderLayout.CENTER);
		cardLayout.show(pContent, "TRANG_CHU");

		add(pSidebar, BorderLayout.WEST);
		add(pMain, BorderLayout.CENTER);

		// Đăng ký sự kiện
		btnTrangChu.addActionListener(this);
		btnNhanVien.addActionListener(this);
		btnSanPham.addActionListener(this);
		btnBanHang.addActionListener(this);
		btnHoaDon.addActionListener(this);
		btnDangXuat.addActionListener(this);
		btnThongKe.addActionListener(this);
	}

	private void chonNut(JButton btnDuocChon, String tieuDe) {
		for (JButton btn : menuButtons) {
			btn.setBackground(sidebarColor);
			btn.setForeground(Color.WHITE);
			btn.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		}
		btnDuocChon.setBackground(Color.WHITE);
		btnDuocChon.setForeground(sidebarColor);
		btnDuocChon.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblTitle.setText(tieuDe);
	}

	// Các hàm để Trang chủ chuyển sang màn hình khác (giống bấm nút trên sidebar)
	public void moBanHang() {
		btnBanHang.doClick();
	}

	public void moHoaDon() {
		btnHoaDon.doClick();
	}

	public void moSanPham() {
		btnSanPham.doClick();
	}

	public void moThongKe() {
		btnThongKe.doClick();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnTrangChu) {
			chonNut(btnTrangChu, "Trang chủ");
			trangChuGUI.taiDuLieu();
			cardLayout.show(pContent, "TRANG_CHU");
		} else if (o == btnNhanVien) {
			chonNut(btnNhanVien, "Quản lý nhân viên");
			nhanVienGUI.loadData();
			cardLayout.show(pContent, "NHAN_VIEN");
		} else if (o == btnSanPham) {
			chonNut(btnSanPham, "Quản lý sản phẩm");
			sanPhamGUI.docDuLieuVaoTable();
			cardLayout.show(pContent, "SAN_PHAM");
		} else if (o == btnBanHang) {
			chonNut(btnBanHang, "Bán hàng (POS)");
			banHangGUI.capNhatBangSanPham();
			cardLayout.show(pContent, "BAN_HANG");
		} else if (o == btnHoaDon) {
			chonNut(btnHoaDon, Session.isQuanLy() ? "Quản lý hóa đơn" : "Tra cứu hóa đơn");
			hoaDonGUI.docDuLieuHoaDon();
			cardLayout.show(pContent, "HOA_DON");
		} else if (o == btnThongKe) {
			chonNut(btnThongKe, "Thống kê báo cáo");
			thongKeGUI.capNhatThongKe();
			cardLayout.show(pContent, "THONG_KE");
		} else if (o == btnDangXuat) {
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				Session.clear();
				new LoginGUI().setVisible(true);
				this.dispose();
			}
		}
	}
}
