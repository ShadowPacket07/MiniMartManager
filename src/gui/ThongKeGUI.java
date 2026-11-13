package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ThongKeGUI extends JPanel {
	private static final long serialVersionUID = 1L;

	private JLabel lblDoanhThu, lblSoHoaDon, lblSoSanPham;
	private DefaultTableModel model;
	private JTable tblBanChay;

	private Color nenColor = new Color(243, 246, 252);
	private Color theColor = Color.WHITE;
	private Color primaryColor = new Color(37, 99, 235);
	private Color textColor = new Color(23, 43, 77);
	private Color secondaryColor = new Color(102, 112, 133);
	private Color vienColor = new Color(208, 213, 221);
	private Color xanhLaColor = new Color(22, 163, 74);
	private Color camColor = new Color(234, 88, 12);

	// dữ liệu mẫu: mã SP, tên SP, số lượng đã bán
	private Object[][] dsBanChay = { { "SP009", "Nước tăng lực RedBull", 128 },
			{ "SP001", "Nước khoáng Lavie 500ml", 115 }, { "SP002", "Mì ly Hảo Hảo Tôm Chua Cay", 97 },
			{ "SP007", "Cà phê lon Highlands", 74 }, { "SP003", "Sữa tươi Vinamilk 180ml", 61 } };

	public ThongKeGUI() {
		setLayout(new BorderLayout(0, 15));
		setBackground(nenColor);
		setBorder(new EmptyBorder(20, 20, 20, 20));

		// tiêu đề và nút làm mới
		JLabel lblTitle = new JLabel("THỐNG KÊ BÁO CÁO");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(textColor);

		JButton btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setFont(new Font("Arial", Font.BOLD, 13));
		btnLamMoi.setForeground(Color.WHITE);
		btnLamMoi.setBackground(primaryColor);
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.setBorderPainted(false);
		btnLamMoi.setOpaque(true);
		btnLamMoi.setMargin(new Insets(0, 0, 0, 0));
		btnLamMoi.setPreferredSize(new Dimension(100, 34));

		JPanel pTieuDe = new JPanel(new BorderLayout());
		pTieuDe.setBackground(nenColor);
		pTieuDe.add(lblTitle, BorderLayout.WEST);
		pTieuDe.add(btnLamMoi, BorderLayout.EAST);

		// 3 ô số liệu
		lblDoanhThu = new JLabel();
		lblSoHoaDon = new JLabel();
		lblSoSanPham = new JLabel();

		JPanel pSoLieu = new JPanel(new GridLayout(1, 3, 15, 0));
		pSoLieu.setBackground(nenColor);
		pSoLieu.setPreferredSize(new Dimension(0, 110));
		pSoLieu.add(taoTheSoLieu("Tổng doanh thu", lblDoanhThu, xanhLaColor));
		pSoLieu.add(taoTheSoLieu("Tổng số hóa đơn", lblSoHoaDon, primaryColor));
		pSoLieu.add(taoTheSoLieu("Tổng sản phẩm đã bán", lblSoSanPham, camColor));

		// bảng sản phẩm bán chạy
		String[] columns = { "STT", "Mã SP", "Tên sản phẩm", "Số lượng đã bán" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblBanChay = new JTable(model);
		tblBanChay.setRowHeight(32);
		tblBanChay.setFont(new Font("Arial", Font.PLAIN, 14));
		tblBanChay.setForeground(textColor);
		tblBanChay.setGridColor(vienColor);
		tblBanChay.setSelectionBackground(new Color(219, 234, 254));
		tblBanChay.setSelectionForeground(textColor);
		tblBanChay.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		tblBanChay.getTableHeader().setBackground(primaryColor);
		tblBanChay.getTableHeader().setForeground(Color.WHITE);
		tblBanChay.getTableHeader().setPreferredSize(new Dimension(0, 36));

		// căn giữa cột STT, Mã SP và Số lượng
		DefaultTableCellRenderer canGiua = new DefaultTableCellRenderer();
		canGiua.setHorizontalAlignment(SwingConstants.CENTER);
		tblBanChay.getColumnModel().getColumn(0).setCellRenderer(canGiua);
		tblBanChay.getColumnModel().getColumn(1).setCellRenderer(canGiua);
		tblBanChay.getColumnModel().getColumn(3).setCellRenderer(canGiua);
		tblBanChay.getColumnModel().getColumn(0).setPreferredWidth(60);
		tblBanChay.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblBanChay.getColumnModel().getColumn(2).setPreferredWidth(350);
		tblBanChay.getColumnModel().getColumn(3).setPreferredWidth(150);

		JScrollPane scroll = new JScrollPane(tblBanChay);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(theColor);

		JLabel lblBanChay = new JLabel("Sản phẩm bán chạy");
		lblBanChay.setFont(new Font("Arial", Font.BOLD, 16));
		lblBanChay.setForeground(textColor);

		JPanel pBang = new JPanel(new BorderLayout(0, 10));
		pBang.setBackground(theColor);
		pBang.setBorder(BorderFactory.createCompoundBorder(new LineBorder(vienColor, 1, true),
				new EmptyBorder(15, 15, 15, 15)));
		pBang.add(lblBanChay, BorderLayout.NORTH);
		pBang.add(scroll, BorderLayout.CENTER);

		JPanel pGiua = new JPanel(new BorderLayout(0, 15));
		pGiua.setBackground(nenColor);
		pGiua.add(pSoLieu, BorderLayout.NORTH);
		pGiua.add(pBang, BorderLayout.CENTER);

		add(pTieuDe, BorderLayout.NORTH);
		add(pGiua, BorderLayout.CENTER);

		// bấm làm mới thì nạp lại số liệu (sau này gọi DAO ở đây)
		btnLamMoi.addActionListener(e -> {
			napDuLieuMau();
			JOptionPane.showMessageDialog(this, "Đã cập nhật số liệu thống kê!");
		});

		napDuLieuMau();
	}

	private JPanel taoTheSoLieu(String tieuDe, JLabel lblGiaTri, Color mau) {
		JLabel lblTieuDe = new JLabel(tieuDe);
		lblTieuDe.setFont(new Font("Arial", Font.PLAIN, 14));
		lblTieuDe.setForeground(secondaryColor);

		lblGiaTri.setFont(new Font("Arial", Font.BOLD, 26));
		lblGiaTri.setForeground(mau);

		JPanel the = new JPanel(new BorderLayout(0, 8));
		the.setBackground(theColor);
		the.setBorder(BorderFactory.createCompoundBorder(new LineBorder(vienColor, 1, true),
				new EmptyBorder(18, 20, 18, 20)));
		the.add(lblTieuDe, BorderLayout.NORTH);
		the.add(lblGiaTri, BorderLayout.CENTER);
		return the;
	}

	private void napDuLieuMau() {
		lblDoanhThu.setText(String.format("%,.0f đ", 12450000.0));
		lblSoHoaDon.setText("86");
		lblSoSanPham.setText("475");

		model.setRowCount(0);
		for (int i = 0; i < dsBanChay.length; i++) {
			model.addRow(new Object[] { i + 1, dsBanChay[i][0], dsBanChay[i][1], dsBanChay[i][2] });
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Thống kê - MiniMart");
		frame.setSize(960, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(new ThongKeGUI());
		frame.setVisible(true);
	}
}