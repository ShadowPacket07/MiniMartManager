package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.HoaDonBUS;

public class ThongKeGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JLabel lblTieuDe;
	private JLabel lblDoanhThu, lblSoHoaDon, lblSoSanPham;
	private JButton btnLamMoi;

	private JTable tblBanChay;
	private DefaultTableModel tableModel;

	private HoaDonBUS hoaDonBUS = new HoaDonBUS();
	private DecimalFormat df = new DecimalFormat("#,###", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US));

	public ThongKeGUI() {
		setLayout(new BorderLayout());

		// Tiêu đề phía Bắc
		JPanel pN = new JPanel();
		lblTieuDe = new JLabel("THỐNG KÊ BÁO CÁO", JLabel.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(new Color(21, 94, 239));

		btnLamMoi = new JButton("Làm mới dữ liệu");
		btnLamMoi.setBackground(new Color(21, 94, 239));
		btnLamMoi.setForeground(Color.WHITE);
		btnLamMoi.setFocusPainted(false);
		btnLamMoi.addActionListener(this);

		pN.add(lblTieuDe);
		pN.add(btnLamMoi);
		add(pN, BorderLayout.NORTH);

		// Khu vực giữa
		JPanel pC = new JPanel(new BorderLayout(0, 15));
		pC.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

		// 3 thẻ số liệu tổng quan
		JPanel pSoLieu = new JPanel(new GridLayout(1, 3, 15, 0));
		pSoLieu.setPreferredSize(new Dimension(0, 100));

		lblDoanhThu = new JLabel("0 VNĐ", JLabel.CENTER);
		lblDoanhThu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblDoanhThu.setForeground(Color.RED);
		JPanel pDT = new JPanel(new BorderLayout());
		pDT.setBorder(BorderFactory.createTitledBorder("Tổng doanh thu"));
		pDT.add(lblDoanhThu, BorderLayout.CENTER);

		lblSoHoaDon = new JLabel("0", JLabel.CENTER);
		lblSoHoaDon.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblSoHoaDon.setForeground(new Color(21, 94, 239));
		JPanel pHD = new JPanel(new BorderLayout());
		pHD.setBorder(BorderFactory.createTitledBorder("Tổng số hóa đơn"));
		pHD.add(lblSoHoaDon, BorderLayout.CENTER);

		lblSoSanPham = new JLabel("0", JLabel.CENTER);
		lblSoSanPham.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblSoSanPham.setForeground(new Color(0, 128, 0));
		JPanel pSP = new JPanel(new BorderLayout());
		pSP.setBorder(BorderFactory.createTitledBorder("Tổng số sản phẩm đã bán"));
		pSP.add(lblSoSanPham, BorderLayout.CENTER);

		pSoLieu.add(pDT);
		pSoLieu.add(pHD);
		pSoLieu.add(pSP);
		pC.add(pSoLieu, BorderLayout.NORTH);

		// Bảng Top 5 sản phẩm bán chạy
		String[] cols = { "STT", "Mã SP", "Tên Sản Phẩm", "Số Lượng Đã Bán" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblBanChay = new JTable(tableModel);

		DefaultTableCellRenderer rCenter = new DefaultTableCellRenderer();
		rCenter.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		tblBanChay.getColumnModel().getColumn(0).setCellRenderer(rCenter);
		tblBanChay.getColumnModel().getColumn(1).setCellRenderer(rCenter);

		DefaultTableCellRenderer rRight = new DefaultTableCellRenderer();
		rRight.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		tblBanChay.getColumnModel().getColumn(3).setCellRenderer(rRight);

		tblBanChay.getColumnModel().getColumn(0).setPreferredWidth(60);
		tblBanChay.getColumnModel().getColumn(1).setPreferredWidth(100);
		tblBanChay.getColumnModel().getColumn(2).setPreferredWidth(350);
		tblBanChay.getColumnModel().getColumn(3).setPreferredWidth(150);

		JScrollPane scroll = new JScrollPane(tblBanChay);
		scroll.setBorder(BorderFactory.createTitledBorder("Top 5 sản phẩm bán chạy nhất"));
		pC.add(scroll, BorderLayout.CENTER);

		add(pC, BorderLayout.CENTER);

		capNhatThongKe();
	}

	public void capNhatThongKe() {
		try {
			double dt = hoaDonBUS.getTongDoanhThu();
			int soDon = hoaDonBUS.getTongSoDon();
			int slBan = hoaDonBUS.getTongSoLuongBan();
			List<Object[]> top5 = hoaDonBUS.getTop5BanChay();

			lblDoanhThu.setText(df.format(dt) + " VNĐ");
			lblSoHoaDon.setText(String.valueOf(soDon));
			lblSoSanPham.setText(String.valueOf(slBan));

			tableModel.setRowCount(0);
			if (top5 != null) {
				int stt = 1;
				for (Object[] r : top5) {
					tableModel.addRow(new Object[] { stt++, r[0], r[1], r[3] });
				}
			}
		} catch (Exception e) {
			lblDoanhThu.setText("0 VNĐ");
			lblSoHoaDon.setText("0");
			lblSoSanPham.setText("0");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnLamMoi)) {
			capNhatThongKe();
			JOptionPane.showMessageDialog(this, "Đã cập nhật lại số liệu thống kê mới nhất!");
		}
	}
}

