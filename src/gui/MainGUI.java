
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	// Màu sắc giao diện
	private Color sidebarColor = new Color(21, 94, 239);

	// Các thành phần chính
	private JPanel pSidebar;
	private JPanel pMain;
	private JPanel pHeader;
	private JPanel pContent;
	private CardLayout cardLayout;

	// Các nút Menu
	JButton btnTrangChu = new JButton("Trang chủ");
	JButton btnNhanVien = new JButton("Quản lý nhân viên");
	JButton btnSanPham = new JButton("Quản lý sản phẩm");
	JButton btnBanHang = new JButton("Bán hàng");
	JButton btnHoaDon = new JButton("Quản lý hóa đơn");
	JButton btnDangXuat = new JButton("Đăng Xuất");

	// 2. Đưa các nút vào mảng để dễ thiết lập chung
	JButton[] menuButtons = { btnTrangChu, btnNhanVien, btnSanPham, btnBanHang, btnHoaDon };

	public MainGUI() {
		// 1. Thiết lập cửa sổ
		setTitle("MiniMart Manager");
		setSize(1200, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// 2. Thiết lập Layout cho cửa sổ chính
		setLayout(new BorderLayout());

		// 3. Khởi tạo Sidebar
		pSidebar = new JPanel();
		pSidebar.setBackground(sidebarColor);
		pSidebar.setPreferredSize(new Dimension(240, 750));
		pSidebar.setLayout(new BoxLayout(pSidebar, BoxLayout.Y_AXIS));

		ImageIcon orginalIcon = new ImageIcon(getClass().getResource("/images/minimart_logo.png"));
		Image scaledImage = orginalIcon.getImage().getScaledInstance(190, 137, Image.SCALE_SMOOTH);

		JLabel lblLogo = new JLabel(new ImageIcon(scaledImage));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		pSidebar.add(Box.createVerticalStrut(20));
		pSidebar.add(lblLogo);

		// Thiết kế giao diện nút Đăng Xuất
		btnDangXuat.setForeground(new Color(220, 38, 38));
		btnDangXuat.setBackground(Color.WHITE);
		btnDangXuat.setFont(new Font("Arial", Font.BOLD, 15));

		btnDangXuat.setFocusPainted(false);
		btnDangXuat.setBorderPainted(false);
		btnDangXuat.setOpaque(true);

		btnDangXuat.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDangXuat.setPreferredSize(new Dimension(210, 45));
		btnDangXuat.setMaximumSize(new Dimension(210, 45));

		// Đưa các nút vào mảng để dễ thiết lập chung
		JButton[] menuButtons = { btnTrangChu, btnNhanVien, btnSanPham, btnBanHang, btnHoaDon };

		// Thiết lập giao diện chung cho các nút
		for (JButton btn : menuButtons) {

			btn.setForeground(Color.WHITE);
			btn.setBackground(sidebarColor);

			btn.setFont(new Font("Arial", Font.PLAIN, 15));

			btn.setFocusPainted(false);
			btn.setOpaque(true);

			btn.setAlignmentX(Component.CENTER_ALIGNMENT);

			btn.setPreferredSize(new Dimension(210, 45));
			btn.setMaximumSize(new Dimension(210, 45));
		}
		// Đặt màu riêng cho nút Trang chủ -> để thể hiện trang đang được chọn là trang
		// nào?
		btnTrangChu.setBackground(Color.WHITE);
		btnTrangChu.setForeground(sidebarColor);

		// Thêm các nút vào Sidebar
		pSidebar.add(Box.createVerticalStrut(25));

		for (JButton btn : menuButtons) {
			pSidebar.add(btn);
			pSidebar.add(Box.createVerticalStrut(10));
		}

		// Đẩy nút đăng xuất xuống cuối Sidebar
		pSidebar.add(Box.createVerticalGlue());
		pSidebar.add(btnDangXuat);
		pSidebar.add(Box.createVerticalStrut(20));

		// 4. Khởi tạo khu vực nội dung chính
		pMain = new JPanel();
		pMain.setBackground(new Color(243, 246, 252));
		pMain.setLayout(new BorderLayout());

		pHeader = new JPanel(new BorderLayout());
		pHeader.setBackground(Color.WHITE);

		// Tạo khoảng cách bên trong Header(top,left,bottom,right)
		pHeader.setBorder(new EmptyBorder(15, 25, 15, 25));

		// Tạo tiêu đề trang
		JLabel lblTitle = new JLabel("Trang chủ");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setForeground(new Color(23, 43, 77));

		// Tạo thông tin tài khoản
		JLabel lblAccount = new JLabel("<html><b>Admin</b><br>Quản trị viên</html>");
		lblAccount.setFont(new Font("Arial", Font.PLAIN, 13));
		lblAccount.setForeground(new Color(102, 112, 133));

		// Đưa các thành phần vào Header
		pHeader.add(lblTitle, BorderLayout.WEST);
		pHeader.add(lblAccount, BorderLayout.EAST);
		pMain.add(pHeader, BorderLayout.NORTH);

		// Tạo khu vực nội dung
		// Khởi tạo CardLayout
		cardLayout = new CardLayout();
		pContent = new JPanel(cardLayout);

		// Tạo các màn hình tạm thời
		JPanel pTrangChu = new JPanel();
		JPanel pSanPham = new JPanel();
//		JPanel pBanHang = new JPanel();
		BanHangGUI banHangGUI = new BanHangGUI();
		JPanel pHoaDon = new JPanel();
		NhanVienGUI nhanVienGUI = new NhanVienGUI();

		// Thiết lập màu nền
		pTrangChu.setBackground(new Color(243, 246, 252));
		pSanPham.setBackground(Color.RED);
//		pBanHang.setBackground(Color.BLUE);
		pHoaDon.setBackground(Color.CYAN);

		// Thêm các màn hình vào CardLayout
		pContent.add(pTrangChu, "TRANG_CHU");
		pContent.add(nhanVienGUI, "NHAN_VIEN");
		pContent.add(pSanPham, "SAN_PHAM");
		pContent.add(banHangGUI, "BAN_HANG");
		pContent.add(pHoaDon, "HOA_DON");

		// Đưa Content Panel vào pMain
		pMain.add(pContent, BorderLayout.CENTER);

		// Mặc định hiển thị trang chủ
		cardLayout.show(pContent, "TRANG_CHU");

		// 5. Đặt các thành phần vào cửa sổ
		add(pSidebar, BorderLayout.WEST);
		add(pMain, BorderLayout.CENTER);

		// 6. Đăng ký sự kiện cho danh sách nút menu
		btnTrangChu.addActionListener(this);
		btnNhanVien.addActionListener(this);
		btnSanPham.addActionListener(this);
		btnBanHang.addActionListener(this);
		btnHoaDon.addActionListener(this);
		btnDangXuat.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnTrangChu) {
			cardLayout.show(pContent, "TRANG_CHU");
		} else if (o == btnNhanVien) {
			cardLayout.show(pContent, "NHAN_VIEN");
		}

		else if (o == btnSanPham) {
			cardLayout.show(pContent, "SAN_PHAM");
		}

		else if (o == btnBanHang) {
			cardLayout.show(pContent, "BAN_HANG");
		}

		else if (o == btnHoaDon) {
			cardLayout.show(pContent, "HOA_DON");
		}

		else if (o == btnDangXuat) {
			LoginGUI loginGUI = new LoginGUI();
			loginGUI.setVisible(true);
			this.dispose();
		}
	}
}
