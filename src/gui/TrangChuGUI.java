package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.TrangChuDAO;
import util.Session;

/**
 * Trang chủ dạng dashboard. Một lớp dùng chung cho hai vai trò: - Quản lý: số
 * liệu toàn cửa hàng. - Nhân viên bán hàng: chỉ số liệu của hóa đơn do chính
 * mình lập.
 */
public class TrangChuGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final Color PRIMARY = new Color(21, 94, 239); // Xanh dương #155EEF
	private static final Color PRIMARY_SOFT = new Color(232, 240, 254);
	private static final Color BG = new Color(243, 246, 252);
	private static final Color TEXT = new Color(23, 43, 77);
	private static final Color MUTED = new Color(100, 116, 139);
	private static final Color BORDER = new Color(225, 230, 240);
	private static final Color ORANGE = new Color(234, 88, 12);
	private static final Color PURPLE = new Color(147, 51, 234);
	private static final Color ERROR = new Color(220, 38, 38);
	private static final Color DOANH_THU = new Color(220, 38, 38); // đỏ nổi bật, đúng mô tả màn hình Thống kê

	private static final DateTimeFormatter GIO_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final MainGUI main;
	private final TrangChuDAO dao = new TrangChuDAO();
	private final boolean quanLy;
	private final String maNV; // null nếu là Quản lý (xem toàn cửa hàng)

	// Bốn ô số liệu
	private final JLabel lblSo1 = new JLabel("—");
	private final JLabel lblSo2 = new JLabel("—");
	private final JLabel lblSo3 = new JLabel("—");
	private final JLabel lblSo4 = new JLabel("—");

	private final JLabel lblViec = nhan("Đang tải dữ liệu…", 18, true, PRIMARY);
	private final JLabel lblGoiY = nhan(" ", 13, false, MUTED);
	private final JLabel lblCapNhat = nhan(" ", 12, false, MUTED);
	private final JLabel lblLoi = nhan(" ", 12, false, ERROR);
	private final JLabel lblHoaDon = nhan(" ", 12, false, MUTED);
	private final JLabel lblTon = nhan(" ", 12, false, MUTED);

	private final JButton btnTai = nut("Tải lại · F5", false);
	private final JButton btnBatDau = nut("Bắt đầu", true);
	private final JButton btnHoaDon = nut("Mở hóa đơn", false);
	private final JButton btnSP = nut("Mở quản lý sản phẩm", false);

	private final DefaultTableModel modelHD = model("Mã HĐ", "Ngày lập", "Mã NV", "Tiền khách đưa", "Tổng tiền");
	private final DefaultTableModel modelSP = model("Mã SP", "Sản phẩm", "Tồn", "Chú ý");
	private final JTable tblHoaDon = bang(modelHD);
	private final JTable tblSP = bang(modelSP);
	private final BieuDo bieuDo = new BieuDo();
	private final JTabbedPane tabs = new JTabbedPane();

	private Runnable hanhDongDau = null;

	public TrangChuGUI(MainGUI main) {
		this.main = main;
		this.quanLy = Session.isQuanLy();
		this.maNV = quanLy ? null : Session.getMaNVHienTai();

		setLayout(new BorderLayout(0, 15));
		setBackground(BG);
		setBorder(new EmptyBorder(20, 25, 20, 25));

		// 1. Dòng cập nhật + nút tải lại
		JPanel pCapNhat = new JPanel(new BorderLayout());
		pCapNhat.setOpaque(false);
		pCapNhat.add(lblCapNhat, BorderLayout.CENTER);
		pCapNhat.add(btnTai, BorderLayout.EAST);

		// 2. Bốn ô số liệu (Quản lý: toàn cửa hàng, Nhân viên: chỉ hóa đơn do mình lập)
		JPanel pSoLieu = new JPanel(new GridLayout(1, 4, 12, 0));
		pSoLieu.setOpaque(false);
		pSoLieu.add(soLieu("Tiền thu hôm nay", lblSo1, DOANH_THU,
				quanLy ? "Tổng tiền các hóa đơn lập trong ngày" : "Các hóa đơn do bạn lập hôm nay"));
		pSoLieu.add(soLieu("Hóa đơn đã lập", lblSo2, PRIMARY, "Trong ngày hôm nay"));
		pSoLieu.add(soLieu("Sản phẩm đã bán", lblSo3, ORANGE, "Tổng số lượng bán ra trong ngày"));
		pSoLieu.add(
				soLieu("Sản phẩm tồn thấp", lblSo4, PURPLE, "Đang kinh doanh, tồn ≤ " + TrangChuDAO.NGUONG_TON_THAP));

		// 3. Thẻ "VIỆC ĐẦU TIÊN"
		JPanel viec = new JPanel(new BorderLayout(12, 0));
		viec.setOpaque(false);
		viec.add(doc(lblViec, lblGoiY), BorderLayout.CENTER);
		viec.add(btnBatDau, BorderLayout.EAST);
		JPanel pUuTien = the("VIỆC ĐẦU TIÊN", viec, null);
		pUuTien.setBackground(PRIMARY_SOFT);

		add(doc(pCapNhat, Box.createVerticalStrut(10), pSoLieu, Box.createVerticalStrut(12), pUuTien,
				Box.createVerticalStrut(4), lblLoi), BorderLayout.NORTH);

		// 4. Khu vực giữa: bảng hóa đơn (trái) | tab biểu đồ + tồn thấp (phải)
		btnHoaDon.setText(quanLy ? "Mở quản lý hóa đơn" : "Mở tra cứu hóa đơn");
		JPanel pHoaDon = the(quanLy ? "Hóa đơn gần đây" : "Hóa đơn gần đây của bạn", cuon(tblHoaDon),
				duoi(lblHoaDon, btnHoaDon));
		pHoaDon.setMinimumSize(new Dimension(380, 200));

		tabs.setFont(phong(Font.BOLD, 13));
		tabs.addTab(quanLy ? "Doanh thu · 7 ngày" : "Thu của bạn · 7 ngày", the(null, bieuDo, null));
		// Chỉ Quản lý có màn hình Quản lý sản phẩm nên nút chỉ hiện với Quản lý
		tabs.addTab("Sản phẩm tồn thấp", the(null, cuon(tblSP), duoi(lblTon, quanLy ? btnSP : null)));
		tabs.setMinimumSize(new Dimension(340, 200));

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pHoaDon, tabs);
		split.setResizeWeight(0.52);
		split.setBorder(null);
		split.setOpaque(false);
		split.setContinuousLayout(true);
		add(split, BorderLayout.CENTER);

		// Căn cột cho đẹp
		DefaultTableCellRenderer giua = new DefaultTableCellRenderer();
		giua.setHorizontalAlignment(SwingConstants.CENTER);
		DefaultTableCellRenderer phai = new DefaultTableCellRenderer();
		phai.setHorizontalAlignment(SwingConstants.RIGHT);
		tblHoaDon.getColumnModel().getColumn(0).setCellRenderer(giua);
		tblHoaDon.getColumnModel().getColumn(1).setCellRenderer(giua);
		tblHoaDon.getColumnModel().getColumn(2).setCellRenderer(giua);
		tblHoaDon.getColumnModel().getColumn(3).setCellRenderer(phai);
		tblHoaDon.getColumnModel().getColumn(4).setCellRenderer(phai);
		tblSP.getColumnModel().getColumn(0).setCellRenderer(giua);
		tblSP.getColumnModel().getColumn(2).setCellRenderer(giua);
		tblSP.getColumnModel().getColumn(3).setCellRenderer(giua);
		tblSP.getColumnModel().getColumn(1).setPreferredWidth(220);

		// Sự kiện
		btnTai.addActionListener(this);
		btnBatDau.addActionListener(this);
		btnHoaDon.addActionListener(this);
		btnSP.addActionListener(this);
		tblSP.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (quanLy && e.getClickCount() == 2 && tblSP.getSelectedRow() >= 0) {
					btnSP.doClick();
				}
			}
		});

		// Phím tắt F5 = Tải lại
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "taiLai");
		getActionMap().put("taiLai", new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				taiDuLieu();
			}
		});

		taiDuLieu();
	}

	// Nạp lại toàn bộ số liệu từ CSDL (MainGUI gọi mỗi lần bấm "Trang chủ")
	public void taiDuLieu() {
		try {
			double[] tongHop = dao.tongHopHomNay(maNV);
			List<Object[]> hoaDon = dao.hoaDonGanDay(maNV, 10);
			List<Object[]> tonThap = dao.sanPhamTonThap();

			lblSo1.setText(tien(tongHop[0]));
			lblSo2.setText(String.valueOf((int) tongHop[1]));
			lblSo3.setText(String.valueOf((int) tongHop[2]));
			lblSo4.setText(String.valueOf(tonThap.size()));

			modelHD.setRowCount(0);
			for (Object[] r : hoaDon) {
				modelHD.addRow(new Object[] { r[0], ((LocalDateTime) r[1]).format(GIO_NGAY), r[2], tien((Double) r[3]),
						tien((Double) r[4]) });
			}
			lblHoaDon.setText(
					hoaDon.isEmpty() ? "Chưa có hóa đơn nào." : "Hiển thị " + hoaDon.size() + " hóa đơn mới nhất.");

			modelSP.setRowCount(0);
			for (Object[] r : tonThap) {
				modelSP.addRow(r);
			}
			tabs.setTitleAt(1, "Sản phẩm tồn thấp" + (tonThap.isEmpty() ? "" : " (" + tonThap.size() + ")"));
			lblTon.setText(tonThap.isEmpty() ? "Các sản phẩm đang bán đều trên ngưỡng tồn thấp."
					: tonThap.size() + " sản phẩm đang bán có tồn ≤ " + TrangChuDAO.NGUONG_TON_THAP + ".");

			bieuDo.duLieu = dao.doanhThu7Ngay(maNV);
			bieuDo.repaint();

			capNhatViecDau(tongHop, tonThap);

			lblCapNhat.setText("Hôm nay " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
					+ " · Cập nhật " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			lblLoi.setText(" ");
		} catch (Exception e) {
			e.printStackTrace();
			lblLoi.setText("Chưa tải được dữ liệu. Nhấn Tải lại để thử tiếp.");
			lblLoi.setToolTipText(e.getMessage());
		}
	}

	private void capNhatViecDau(double[] tongHop, List<Object[]> tonThap) {
		if (quanLy) {
			if (!tonThap.isEmpty()) {
				Object[] thapNhat = tonThap.get(0);
				lblViec.setText(tonThap.size() + " sản phẩm cần xem lại tồn kho");
				lblGoiY.setText("Thấp nhất: " + thapNhat[1] + " (còn " + thapNhat[2] + ")");
				btnBatDau.setText("Xem sản phẩm");
				hanhDongDau = () -> main.moSanPham();
			} else {
				lblViec.setText("Kho hàng đang ổn định");
				lblGoiY.setText("Doanh thu hôm nay " + tien(tongHop[0]) + " từ " + (int) tongHop[1] + " hóa đơn.");
				btnBatDau.setText("Xem thống kê");
				hanhDongDau = () -> main.moThongKe();
			}
		} else {
			lblViec.setText("Xin chào, " + Session.getTenNguoiDung() + " — sẵn sàng bán hàng");
			lblGoiY.setText("Hôm nay bạn đã lập " + (int) tongHop[1] + " hóa đơn, thu " + tien(tongHop[0]) + ".");
			btnBatDau.setText("Bán hàng");
			hanhDongDau = () -> main.moBanHang();
		}
		btnBatDau.setPreferredSize(new Dimension(150, 38));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		try {
			if (o == btnTai) {
				taiDuLieu();
			} else if (main == null) {
				return; // chạy độc lập để thử giao diện, không có MainGUI để chuyển màn hình
			} else if (o == btnBatDau) {
				if (hanhDongDau != null) {
					hanhDongDau.run();
				}
			} else if (o == btnHoaDon) {
				main.moHoaDon();
			} else if (o == btnSP) {
				main.moSanPham();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Thông báo", JOptionPane.WARNING_MESSAGE);
		}
	}
	// ======================= Các hàm dựng giao diện dùng chung
	// =======================

	private static String tien(double v) {
		return String.format(Locale.US, "%,.0f đ", v); // Locale.US để không lệch dấu chấm/phẩy trên Windows tiếng Việt
	}

	private static Font phong(int kieu, int co) {
		return new Font("Times New Roman", kieu, co);
	}

	private static JLabel nhan(String text, int co, boolean dam, Color mau) {
		JLabel l = new JLabel(text);
		l.setFont(phong(dam ? Font.BOLD : Font.PLAIN, co));
		l.setForeground(mau);
		return l;
	}

	private static JButton nut(String text, boolean chinh) {
		JButton b = new JButton(text);
		b.setFont(phong(Font.BOLD, 13));
		b.setFocusPainted(false);
		b.setOpaque(true);
		if (chinh) {
			b.setBackground(PRIMARY);
			b.setForeground(Color.WHITE);
			b.setBorderPainted(false);
		} else {
			b.setBackground(Color.WHITE);
			b.setForeground(PRIMARY);
			b.setBorder(BorderFactory.createCompoundBorder(new LineBorder(PRIMARY, 1, true),
					new EmptyBorder(6, 14, 6, 14)));
		}
		return b;
	}

	// Xếp các thành phần theo chiều dọc, căn trái
	private static JPanel doc(Component... cs) {
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setOpaque(false);
		for (Component c : cs) {
			if (c instanceof JComponent) {
				((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
			}
			p.add(c);
		}
		return p;
	}

	// Thẻ trắng có viền bo, tiêu đề (có thể null), nội dung giữa, phần dưới (có thể
	// null)
	private static JPanel the(String tieuDe, JComponent giua, JComponent duoi) {
		JPanel p = new JPanel(new BorderLayout(0, 10));
		p.setBackground(Color.WHITE);
		p.setBorder(
				BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(14, 16, 14, 16)));
		if (tieuDe != null) {
			p.add(nhan(tieuDe, 15, true, TEXT), BorderLayout.NORTH);
		}
		p.add(giua, BorderLayout.CENTER);
		if (duoi != null) {
			p.add(duoi, BorderLayout.SOUTH);
		}
		return p;
	}

	// Dòng chú thích bên trái + nút bên phải (nút có thể null)
	private static JPanel duoi(JLabel ghiChu, JButton nutPhai) {
		JPanel p = new JPanel(new BorderLayout(10, 0));
		p.setOpaque(false);
		p.add(ghiChu, BorderLayout.CENTER);
		if (nutPhai != null) {
			p.add(nutPhai, BorderLayout.EAST);
		}
		return p;
	}

	private static JPanel soLieu(String tieuDe, JLabel lblGiaTri, Color mau, String ghiChu) {
		lblGiaTri.setFont(phong(Font.BOLD, 26));
		lblGiaTri.setForeground(mau);
		JPanel p = new JPanel(new BorderLayout(0, 4));
		p.setBackground(Color.WHITE);
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, mau),
				BorderFactory.createCompoundBorder(new LineBorder(BORDER, 1), new EmptyBorder(14, 16, 14, 16))));
		p.add(nhan(tieuDe, 14, false, MUTED), BorderLayout.NORTH);
		p.add(lblGiaTri, BorderLayout.CENTER);
		p.add(nhan(ghiChu, 12, false, MUTED), BorderLayout.SOUTH);
		return p;
	}

	private static DefaultTableModel model(String... cot) {
		return new DefaultTableModel(cot, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
	}

	private static JTable bang(DefaultTableModel m) {
		JTable t = new JTable(m);
		t.setRowHeight(30);
		t.setFont(phong(Font.PLAIN, 14));
		t.setForeground(TEXT);
		t.setGridColor(BORDER);
		t.setSelectionBackground(new Color(219, 234, 254));
		t.setSelectionForeground(TEXT);
		t.setFillsViewportHeight(true);
		t.getTableHeader().setFont(phong(Font.BOLD, 14));
		t.getTableHeader().setBackground(PRIMARY);
		t.getTableHeader().setForeground(Color.WHITE);
		t.getTableHeader().setPreferredSize(new Dimension(0, 34));
		return t;
	}

	private static JScrollPane cuon(JTable t) {
		JScrollPane sp = new JScrollPane(t);
		sp.setBorder(null);
		sp.getViewport().setBackground(Color.WHITE);
		return sp;
	}
	// ======================= Biểu đồ cột doanh thu 7 ngày =======================

	private static final class BieuDo extends JPanel {
		private static final long serialVersionUID = 1L;
		Map<LocalDate, Double> duLieu = new LinkedHashMap<>();

		BieuDo() {
			setOpaque(false);
			setPreferredSize(new Dimension(400, 180));
		}

		@Override
		protected void paintComponent(Graphics g0) {
			super.paintComponent(g0);
			Graphics2D g = (Graphics2D) g0.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(phong(Font.PLAIN, 12));
			g.setColor(MUTED);

			double max = 0;
			for (double v : duLieu.values()) {
				max = Math.max(max, v);
			}
			if (max <= 0) {
				g.drawString("Chưa có doanh thu trong 7 ngày gần nhất.", 12, 28);
				g.dispose();
				return;
			}

			// Đơn vị nhãn: triệu đồng nếu cột cao nhất ≥ 1 triệu, ngược lại nghìn đồng
			boolean theoTrieu = max >= 1_000_000;
			double donVi = theoTrieu ? 1_000_000.0 : 1_000.0;
			g.drawString(theoTrieu ? "Triệu đồng" : "Nghìn đồng", 10, 14);

			int n = duLieu.size();
			int w = Math.max(100, getWidth() - 20);
			int h = Math.max(20, getHeight() - 62);
			int top = 28;
			int base = top + h;
			int bar = Math.max(10, Math.min(40, w / n - 14));

			g.setColor(BORDER);
			g.drawLine(10, base, getWidth() - 10, base);

			int i = 0;
			for (Map.Entry<LocalDate, Double> e : duLieu.entrySet()) {
				int bh = (int) Math.round(h * e.getValue() / max);
				int x = 10 + i * w / n + (w / n - bar) / 2;
				int y = base - bh;

				g.setColor(PRIMARY);
				if (bh > 0) {
					g.fillRoundRect(x, y, bar, bh, 4, 4);
				}

				g.setFont(phong(Font.PLAIN, 11));
				g.setColor(MUTED);
				String giaTri = String.format(Locale.US, theoTrieu ? "%.1f" : "%.0f", e.getValue() / donVi);
				g.drawString(giaTri, x + (bar - g.getFontMetrics().stringWidth(giaTri)) / 2, Math.max(24, y - 4));

				String ngay = e.getKey().format(DateTimeFormatter.ofPattern("dd/MM"));
				g.drawString(ngay, x + (bar - g.getFontMetrics().stringWidth(ngay)) / 2, getHeight() - 10);
				i++;
			}
			g.dispose();
		}
	}

	// Chạy độc lập để xem thử giao diện (cần SQL Server đang bật)
	public static void main(String[] args) {
		javax.swing.JFrame f = new javax.swing.JFrame("Trang chủ - GọnMart");
		f.setSize(1100, 700);
		f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(new TrangChuGUI(null));
		f.setVisible(true);
	}
}