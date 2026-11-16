package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Session;

public class TrangChuGUI extends JPanel {
	private static final long serialVersionUID = 1L;

	private Color primaryColor = new Color(21, 94, 239); // Xanh duong #155EEF chuan dong bo
	private Color bgColor = new Color(243, 246, 252);

	public TrangChuGUI() {
		setLayout(new BorderLayout(0, 20));
		setBackground(bgColor);
		setBorder(new EmptyBorder(25, 30, 25, 30));

		// 1. Banner chào mừng phía trên
		JPanel pnlBanner = new JPanel();
		pnlBanner.setLayout(new BoxLayout(pnlBanner, BoxLayout.Y_AXIS));
		pnlBanner.setBackground(Color.WHITE);
		pnlBanner.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(225, 230, 240), 1),
				new EmptyBorder(25, 30, 25, 30)));

		// Banner logo tương đối (fallback an toàn đa tầng)
		URL logoUrl = getClass().getResource("/imgs/logo.png");
		if (logoUrl == null) {
			logoUrl = getClass().getResource("/logo.png");
		}
		ImageIcon icon = null;
		if (logoUrl != null) {
			icon = new ImageIcon(logoUrl);
		} else if (new java.io.File("imgs/logo.png").exists()) {
			icon = new ImageIcon("imgs/logo.png");
		}
		if (icon != null) {
			Image img = icon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
			JLabel lblLogo = new JLabel(new ImageIcon(img));
			lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
			pnlBanner.add(lblLogo);
			pnlBanner.add(Box.createVerticalStrut(10));
		}

		JLabel lblTieuDe = new JLabel("HỆ THỐNG QUẢN LÝ SIÊU THỊ TIỆN LỢI GỌNMART");
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(primaryColor);
		lblTieuDe.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSlogan = new JLabel("Phục vụ nhanh chóng - Quản lý chuẩn xác - Đồng hành cùng tiện ích mỗi ngày");
		lblSlogan.setFont(new Font("Times New Roman", Font.ITALIC, 14));
		lblSlogan.setForeground(new Color(100, 116, 139));
		lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlBanner.add(lblTieuDe);
		pnlBanner.add(Box.createVerticalStrut(6));
		pnlBanner.add(lblSlogan);

		add(pnlBanner, BorderLayout.NORTH);

		// 2. Khu vực thông tin phiên & thẻ chức năng
		JPanel pnlCenter = new JPanel(new GridLayout(2, 2, 20, 20));
		pnlCenter.setBackground(bgColor);

		String tenNV = Session.getTenNguoiDung();
		String quyen = Session.isQuanLy() ? "Quản lý hệ thống" : "Nhân viên bán hàng (Thu ngân)";
		String ngayHienTai = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		pnlCenter.add(taoThe("Thông Tin Phiên Làm Việc", 
				"Nhân viên: " + tenNV, 
				"Vai trò: " + quyen, 
				"Ngày làm việc: " + ngayHienTai, 
				new Color(37, 99, 235)));

		pnlCenter.add(taoThe("Quầy Bán Hàng (POS)", 
				"Lập hóa đơn bán hàng trực tiếp tại quầy", 
				"Tự động tính tiền thừa & trừ tồn kho qua Transaction", 
				"Tra cứu nhanh sản phẩm đang kinh doanh", 
				new Color(22, 163, 74)));

		pnlCenter.add(taoThe("Quản Lý Kho Hàng", 
				"Theo dõi số lượng tồn kho khả dụng", 
				"Kiểm soát giá bán và phân loại ngành hàng", 
				"Cập nhật trạng thái ngừng kinh doanh an toàn", 
				new Color(234, 88, 12)));

		pnlCenter.add(taoThe("Báo Cáo & Thống Kê", 
				"Tổng hợp doanh thu thực tế theo thời gian", 
				"Thống kê tổng số đơn hàng đã hoàn tất", 
				"Theo dõi Top 5 sản phẩm bán chạy nhất", 
				new Color(147, 51, 234)));

		add(pnlCenter, BorderLayout.CENTER);
	}

	private JPanel taoThe(String tieuDe, String dong1, String dong2, String dong3, Color mauChuDao) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBackground(Color.WHITE);
		p.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 5, 0, 0, mauChuDao),
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(225, 230, 240), 1),
						new EmptyBorder(18, 20, 18, 20))));

		JLabel lblTieuDe = new JLabel(tieuDe);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblTieuDe.setForeground(mauChuDao);

		JLabel l1 = new JLabel("• " + dong1);
		l1.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		l1.setForeground(new Color(51, 65, 85));

		JLabel l2 = new JLabel("• " + dong2);
		l2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		l2.setForeground(new Color(51, 65, 85));

		JLabel l3 = new JLabel("• " + dong3);
		l3.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		l3.setForeground(new Color(51, 65, 85));

		p.add(lblTieuDe);
		p.add(Box.createVerticalStrut(12));
		p.add(l1);
		p.add(Box.createVerticalStrut(6));
		p.add(l2);
		p.add(Box.createVerticalStrut(6));
		p.add(l3);
		p.add(Box.createVerticalGlue());

		return p;
	}
}

