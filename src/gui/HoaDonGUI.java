package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entity.ChiTietHoaDon;
import entity.HoaDon;

public class HoaDonGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	// Màu sắc giao diện
	private Color primaryColor = new Color(21, 94, 239);
	private Color textColor = new Color(23, 43, 77);
	private Color subTextColor = new Color(102, 112, 133);
	private Color backgroundColor = new Color(243, 246, 252);
	private Color dangerColor = new Color(220, 38, 38);
	private Color successColor = new Color(16, 185, 129);

	// Định dạng tiền tệ và ngày giờ
	private DecimalFormat df = new DecimalFormat("#,###");
	private DateTimeFormatter dtfDisplay = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	// Collection lưu trữ danh sách hóa đơn trên ứng dụng (không tự bịa sẵn dữ liệu)
	public static ArrayList<HoaDon> dsHoaDon = new ArrayList<>();

	// 1. Thành phần Bộ lọc tìm kiếm
	private JLabel lblTimMaHD, lblTuNgay, lblDenNgay;
	private JTextField txtTimMaHD, txtTuNgay, txtDenNgay;
	private JButton btnTimKiem, btnLamMoi, btnXemPopup;
	private JTextField txtMessLoc;

	// 2. Thành phần Bảng danh sách hóa đơn
	private JTable tblHoaDon;
	private DefaultTableModel modelHoaDon;

	// 3. Thành phần Bảng con chi tiết mặt hàng
	private JPanel pChiTiet;
	private TitledBorder borderChiTiet;
	private JTable tblChiTiet;
	private DefaultTableModel modelChiTiet;
	private JLabel lblTongKetChiTiet;

	public HoaDonGUI() {
		// Thiết lập layout chính
		setLayout(new BorderLayout(10, 10));
		setBackground(backgroundColor);
		setBorder(new EmptyBorder(10, 15, 10, 15));

		// Tiêu đề phía trên
		JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pNorth.setBackground(backgroundColor);
		JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setForeground(textColor);
		pNorth.add(lblTitle);
		add(pNorth, BorderLayout.NORTH);

		// Khu vực trung tâm
		JPanel pCenter = new JPanel(new BorderLayout(10, 10));
		pCenter.setBackground(backgroundColor);

		// 1. Panel bộ lọc tìm kiếm
		JPanel pBoLoc = taoPanelBoLoc();
		pCenter.add(pBoLoc, BorderLayout.NORTH);

		// 2. Panel chứa 2 bảng: Bảng Hóa Đơn (trên) và Bảng Chi Tiết (dưới)
		JPanel pBang = new JPanel(new GridLayout(2, 1, 0, 10));
		pBang.setBackground(backgroundColor);

		JPanel pHoaDon = taoPanelBangHoaDon();
		pChiTiet = taoPanelBangChiTiet();

		pBang.add(pHoaDon);
		pBang.add(pChiTiet);

		pCenter.add(pBang, BorderLayout.CENTER);
		add(pCenter, BorderLayout.CENTER);

		// Đăng ký sự kiện
		dangKySuKien();

		// Cập nhật dữ liệu từ Collection
		capNhatDuLieu();
	}

	/**
	 * Tạo Panel bộ lọc tìm kiếm
	 */
	private JPanel taoPanelBoLoc() {
		JPanel pBoLoc = new JPanel(new BorderLayout(5, 5));
		pBoLoc.setBackground(Color.WHITE);
		pBoLoc.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Bộ lọc tìm kiếm hóa đơn", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 13),
				textColor));

		Box b = Box.createVerticalBox();
		b.setBorder(new EmptyBorder(5, 10, 5, 10));

		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();

		lblTimMaHD = new JLabel("Mã HĐ:");
		txtTimMaHD = new JTextField();

		lblTuNgay = new JLabel("Từ ngày:");
		txtTuNgay = new JTextField();

		lblDenNgay = new JLabel("Đến ngày:");
		txtDenNgay = new JTextField();

		btnTimKiem = new JButton("Tìm kiếm");
		btnTimKiem.setBackground(primaryColor);
		btnTimKiem.setForeground(Color.WHITE);
		btnTimKiem.setFont(new Font("Arial", Font.BOLD, 12));

		btnLamMoi = new JButton("Làm mới");
		btnXemPopup = new JButton("Xem phiếu in hóa đơn");

		b1.add(lblTimMaHD);
		b1.add(txtTimMaHD);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblTuNgay);
		b1.add(txtTuNgay);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblDenNgay);
		b1.add(txtDenNgay);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(btnTimKiem);
		b1.add(Box.createRigidArea(new Dimension(5, 0)));
		b1.add(btnLamMoi);
		b1.add(Box.createRigidArea(new Dimension(5, 0)));
		b1.add(btnXemPopup);

		// Dòng thông báo lỗi regex / trạng thái lọc
		txtMessLoc = new JTextField();
		txtMessLoc.setEditable(false);
		txtMessLoc.setBorder(null);
		txtMessLoc.setForeground(dangerColor);
		txtMessLoc.setFont(new Font("Arial", Font.ITALIC, 12));
		b2.add(txtMessLoc);

		lblTimMaHD.setPreferredSize(new Dimension(50, 22));
		lblTuNgay.setPreferredSize(new Dimension(60, 22));
		lblDenNgay.setPreferredSize(new Dimension(65, 22));

		b.add(b1);
		b.add(Box.createRigidArea(new Dimension(0, 4)));
		b.add(b2);

		pBoLoc.add(b, BorderLayout.CENTER);
		return pBoLoc;
	}

	/**
	 * Tạo Panel bảng danh sách hóa đơn
	 */
	private JPanel taoPanelBangHoaDon() {
		JPanel pPanel = new JPanel(new BorderLayout(5, 5));
		pPanel.setBackground(Color.WHITE);
		pPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Danh sách hóa đơn đã lập", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 13),
				textColor));

		String[] cols = { "Mã HĐ", "Ngày lập", "Nhân viên", "Tiền khách đưa", "Tổng tiền" };
		modelHoaDon = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblHoaDon = new JTable(modelHoaDon);
		dinhDangTable(tblHoaDon);

		JScrollPane scr = new JScrollPane(tblHoaDon);
		pPanel.add(scr, BorderLayout.CENTER);

		return pPanel;
	}

	/**
	 * Tạo Panel bảng con chi tiết hóa đơn
	 */
	private JPanel taoPanelBangChiTiet() {
		JPanel pPanel = new JPanel(new BorderLayout(5, 5));
		pPanel.setBackground(Color.WHITE);

		borderChiTiet = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Chi tiết mặt hàng đã mua (Click chọn hóa đơn để xem)", TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 13), textColor);
		pPanel.setBorder(borderChiTiet);

		String[] cols = { "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền" };
		modelChiTiet = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblChiTiet = new JTable(modelChiTiet);
		dinhDangTable(tblChiTiet);

		JScrollPane scr = new JScrollPane(tblChiTiet);
		pPanel.add(scr, BorderLayout.CENTER);

		JPanel pBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 6));
		pBottom.setBackground(Color.WHITE);
		lblTongKetChiTiet = new JLabel("Chưa chọn hóa đơn nào");
		lblTongKetChiTiet.setFont(new Font("Arial", Font.BOLD, 13));
		lblTongKetChiTiet.setForeground(textColor);
		pBottom.add(lblTongKetChiTiet);

		pPanel.add(pBottom, BorderLayout.SOUTH);

		return pPanel;
	}

	/**
	 * Định dạng bảng
	 */
	private void dinhDangTable(JTable table) {
		table.setRowHeight(26);
		table.setFont(new Font("Arial", Font.PLAIN, 13));
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(241, 245, 249));

		DefaultTableCellRenderer rRenderer = new DefaultTableCellRenderer();
		rRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

		table.getColumnModel().getColumn(3).setCellRenderer(rRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(rRenderer);
	}

	/**
	 * Đăng ký sự kiện
	 */
	private void dangKySuKien() {
		btnTimKiem.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnXemPopup.addActionListener(this);
		tblHoaDon.addMouseListener(this);
	}

	/**
	 * Cập nhật lại danh sách hóa đơn từ Collection
	 */
	public void capNhatDuLieu() {
		hienThiDanhSachHoaDon(dsHoaDon);
	}

	/**
	 * Đổ danh sách hóa đơn lên bảng
	 */
	private void hienThiDanhSachHoaDon(List<HoaDon> list) {
		modelHoaDon.setRowCount(0);
		for (HoaDon hd : list) {
			String ngayStr = hd.getNgayLap() != null ? hd.getNgayLap().format(dtfDisplay) : "";
			modelHoaDon.addRow(new Object[] { hd.getMaHD(), ngayStr, "NV001", df.format(hd.getTienKhachDua()),
					df.format(hd.tinhTongTien()) });
		}

		if (modelHoaDon.getRowCount() > 0) {
			tblHoaDon.setRowSelectionInterval(0, 0);
			fillChiTiet(0);
		} else {
			modelChiTiet.setRowCount(0);
			borderChiTiet.setTitle("Chi tiết mặt hàng đã mua (Chưa có hóa đơn nào)");
			pChiTiet.repaint();
			lblTongKetChiTiet.setText("Chưa có hóa đơn nào trong hệ thống");
		}
	}

	/**
	 * Hiển thị chi tiết mặt hàng xuống bảng con khi click chọn dòng
	 */
	private void fillChiTiet(int row) {
		if (row < 0 || row >= modelHoaDon.getRowCount()) {
			return;
		}

		String maHD = modelHoaDon.getValueAt(row, 0).toString();
		HoaDon hd = timHoaDonTheoMa(maHD);

		modelChiTiet.setRowCount(0);

		if (hd != null && hd.getDsChiTiet() != null) {
			borderChiTiet.setTitle("Chi tiết các mặt hàng đã mua của hóa đơn [" + maHD + "]");
			pChiTiet.repaint();

			int tongSoMon = 0;
			for (ChiTietHoaDon ct : hd.getDsChiTiet()) {
				String maSP = ct.getSanPham() != null ? ct.getSanPham().getMaSP() : "";
				String tenSP = ct.getSanPham() != null ? ct.getSanPham().getTenSP() : "";
				double donGia = ct.getDonGia();
				int sl = ct.getSoLuong();
				double thanhTien = ct.tinhThanhTien();

				tongSoMon += sl;
				modelChiTiet.addRow(new Object[] { maSP, tenSP, df.format(donGia), sl, df.format(thanhTien) });
			}

			double tongTien = hd.tinhTongTien();
			double tienThua = hd.tinhTienThua();

			lblTongKetChiTiet.setText("Tổng số món: " + tongSoMon + " | Tổng tiền: " + df.format(tongTien)
					+ " VNĐ | Tiền thừa: " + df.format(tienThua) + " VNĐ");
		}
	}

	/**
	 * Kiểm tra REGEX của bộ lọc tìm kiếm
	 */
	private boolean validDataBoLoc() {
		String tuNgayStr = txtTuNgay.getText().trim();
		String denNgayStr = txtDenNgay.getText().trim();

		// Kiểm tra regex ngày nếu có nhập
		if (tuNgayStr.length() > 0) {
			if (!tuNgayStr.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
				txtMessLoc.setText("Từ ngày phải có định dạng dd/MM/yyyy (ví dụ: 01/11/2026)");
				txtTuNgay.requestFocus();
				return false;
			}
			try {
				LocalDate.parse(tuNgayStr, dfDate);
			} catch (DateTimeParseException e) {
				txtMessLoc.setText("Từ ngày không hợp lệ");
				txtTuNgay.requestFocus();
				return false;
			}
		}

		if (denNgayStr.length() > 0) {
			if (!denNgayStr.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
				txtMessLoc.setText("Đến ngày phải có định dạng dd/MM/yyyy (ví dụ: 30/11/2026)");
				txtDenNgay.requestFocus();
				return false;
			}
			try {
				LocalDate.parse(denNgayStr, dfDate);
			} catch (DateTimeParseException e) {
				txtMessLoc.setText("Đến ngày không hợp lệ");
				txtDenNgay.requestFocus();
				return false;
			}
		}

		if (tuNgayStr.length() > 0 && denNgayStr.length() > 0) {
			LocalDate tu = LocalDate.parse(tuNgayStr, dfDate);
			LocalDate den = LocalDate.parse(denNgayStr, dfDate);
			if (tu.isAfter(den)) {
				txtMessLoc.setText("Từ ngày không được lớn hơn Đến ngày");
				txtTuNgay.requestFocus();
				return false;
			}
		}

		txtMessLoc.setText("");
		return true;
	}

	/**
	 * Xử lý lọc danh sách hóa đơn theo Mã HĐ và Khoảng thời gian
	 */
	private void xuLyTimKiem() {
		if (!validDataBoLoc()) {
			return;
		}

		String maHDCanTim = txtTimMaHD.getText().trim().toLowerCase();
		String tuNgayStr = txtTuNgay.getText().trim();
		String denNgayStr = txtDenNgay.getText().trim();

		LocalDate tuNgay = tuNgayStr.isEmpty() ? null : LocalDate.parse(tuNgayStr, dfDate);
		LocalDate denNgay = denNgayStr.isEmpty() ? null : LocalDate.parse(denNgayStr, dfDate);

		List<HoaDon> ketQua = new ArrayList<>();
		for (HoaDon hd : dsHoaDon) {
			boolean hopLeMa = true;
			boolean hopLeThoiGian = true;

			if (!maHDCanTim.isEmpty()) {
				hopLeMa = hd.getMaHD() != null && hd.getMaHD().toLowerCase().contains(maHDCanTim);
			}

			if (hd.getNgayLap() != null) {
				LocalDate ngayLap = hd.getNgayLap().toLocalDate();
				if (tuNgay != null && ngayLap.isBefore(tuNgay)) {
					hopLeThoiGian = false;
				}
				if (denNgay != null && ngayLap.isAfter(denNgay)) {
					hopLeThoiGian = false;
				}
			}

			if (hopLeMa && hopLeThoiGian) {
				ketQua.add(hd);
			}
		}

		hienThiDanhSachHoaDon(ketQua);
		txtMessLoc.setText("Tìm thấy " + ketQua.size() + " hóa đơn phù hợp");
	}

	/**
	 * Xem chi tiết dạng popup JDialog
	 */
	private void moDialogChiTiet(int row) {
		if (row < 0 || row >= modelHoaDon.getRowCount()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn cần xem!");
			return;
		}

		String maHD = modelHoaDon.getValueAt(row, 0).toString();
		HoaDon hd = timHoaDonTheoMa(maHD);
		if (hd == null) {
			return;
		}

		JDialog dialog = new JDialog((JFrame) null, "Phiếu Hóa Đơn - " + hd.getMaHD(), true);
		dialog.setSize(520, 480);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout(10, 10));
		dialog.getContentPane().setBackground(Color.WHITE);

		// Header
		JPanel pDHeader = new JPanel();
		pDHeader.setLayout(new BoxLayout(pDHeader, BoxLayout.Y_AXIS));
		pDHeader.setBackground(Color.WHITE);
		pDHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

		JLabel lblTenCH = new JLabel("CỬA HÀNG TIỆN LỢI MINIMART");
		lblTenCH.setFont(new Font("Arial", Font.BOLD, 16));
		lblTenCH.setForeground(primaryColor);
		lblTenCH.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblPhieu = new JLabel("HÓA ĐƠN BÁN LẺ");
		lblPhieu.setFont(new Font("Arial", Font.BOLD, 18));
		lblPhieu.setForeground(textColor);
		lblPhieu.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblInfo = new JLabel("Mã HĐ: " + hd.getMaHD() + " | Ngày: "
				+ (hd.getNgayLap() != null ? hd.getNgayLap().format(dtfDisplay) : ""));
		lblInfo.setFont(new Font("Arial", Font.PLAIN, 13));
		lblInfo.setForeground(subTextColor);
		lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

		pDHeader.add(lblTenCH);
		pDHeader.add(Box.createVerticalStrut(5));
		pDHeader.add(lblPhieu);
		pDHeader.add(Box.createVerticalStrut(5));
		pDHeader.add(lblInfo);

		// Table Dialog
		String[] colsD = { "Mã SP", "Tên mặt hàng", "Đơn giá", "Số lượng", "Thành tiền" };
		DefaultTableModel modelD = new DefaultTableModel(colsD, 0);
		JTable tblD = new JTable(modelD);
		dinhDangTable(tblD);

		if (hd.getDsChiTiet() != null) {
			for (ChiTietHoaDon ct : hd.getDsChiTiet()) {
				modelD.addRow(new Object[] { ct.getSanPham() != null ? ct.getSanPham().getMaSP() : "",
						ct.getSanPham() != null ? ct.getSanPham().getTenSP() : "", df.format(ct.getDonGia()),
						ct.getSoLuong(), df.format(ct.tinhThanhTien()) });
			}
		}

		JScrollPane scrD = new JScrollPane(tblD);
		scrD.setBorder(new EmptyBorder(0, 15, 0, 15));

		// Footer Dialog
		JPanel pDFooter = new JPanel();
		pDFooter.setLayout(new BoxLayout(pDFooter, BoxLayout.Y_AXIS));
		pDFooter.setBackground(Color.WHITE);
		pDFooter.setBorder(new EmptyBorder(10, 20, 15, 20));

		JPanel pTien = new JPanel(new GridLayout(3, 2, 5, 5));
		pTien.setBackground(Color.WHITE);

		pTien.add(new JLabel("Tổng tiền:"));
		JLabel lblTongVal = new JLabel(df.format(hd.tinhTongTien()) + " VNĐ", SwingConstants.RIGHT);
		lblTongVal.setFont(new Font("Arial", Font.BOLD, 15));
		lblTongVal.setForeground(dangerColor);
		pTien.add(lblTongVal);

		pTien.add(new JLabel("Tiền khách đưa:"));
		JLabel lblKhachVal = new JLabel(df.format(hd.getTienKhachDua()) + " VNĐ", SwingConstants.RIGHT);
		pTien.add(lblKhachVal);

		pTien.add(new JLabel("Tiền thừa:"));
		JLabel lblThuaVal = new JLabel(df.format(hd.tinhTienThua()) + " VNĐ", SwingConstants.RIGHT);
		lblThuaVal.setFont(new Font("Arial", Font.BOLD, 15));
		lblThuaVal.setForeground(successColor);
		pTien.add(lblThuaVal);

		JButton btnDong = new JButton("Đóng");
		btnDong.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDong.addActionListener(e -> dialog.dispose());

		pDFooter.add(pTien);
		pDFooter.add(Box.createVerticalStrut(10));
		pDFooter.add(btnDong);

		dialog.add(pDHeader, BorderLayout.NORTH);
		dialog.add(scrD, BorderLayout.CENTER);
		dialog.add(pDFooter, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}

	/**
	 * Tìm hóa đơn theo mã trong Collection
	 */
	private HoaDon timHoaDonTheoMa(String maHD) {
		for (HoaDon hd : dsHoaDon) {
			if (hd.getMaHD().equalsIgnoreCase(maHD)) {
				return hd;
			}
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblHoaDon.getSelectedRow();
		if (row != -1) {
			fillChiTiet(row);
			if (e.getClickCount() == 2) {
				moDialogChiTiet(row);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnTimKiem)) {
			xuLyTimKiem();
		} else if (o.equals(btnLamMoi)) {
			txtTimMaHD.setText("");
			txtTuNgay.setText("");
			txtDenNgay.setText("");
			txtMessLoc.setText("");
			capNhatDuLieu();
		} else if (o.equals(btnXemPopup)) {
			int row = tblHoaDon.getSelectedRow();
			moDialogChiTiet(row);
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("HoaDonGUI Test");
		f.setSize(1100, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(new HoaDonGUI());
		f.setVisible(true);
	}
}
