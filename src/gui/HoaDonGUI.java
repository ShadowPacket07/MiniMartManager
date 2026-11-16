package gui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.HoaDonBUS;
import entity.ChiTietHoaDon;
import entity.HoaDon;

public class HoaDonGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private JLabel lblTieuDe;
	private JLabel lblTimMaHD, lblTuNgay, lblDenNgay;
	private JTextField txtTimMaHD, txtTuNgay, txtDenNgay;
	private JButton btnTim, btnLamMoi, btnXemChiTiet;

	private JTable tblHoaDon;
	private DefaultTableModel modelHoaDon;

	private JTable tblChiTiet;
	private DefaultTableModel modelChiTiet;

	private HoaDonBUS hoaDonBUS = new HoaDonBUS();
	private DecimalFormat df = new DecimalFormat("#,###", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US));
	private DateTimeFormatter dtfDisplay = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private DateTimeFormatter dfDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public HoaDonGUI() {
		setLayout(new BorderLayout());

		// Tiêu đề phía Bắc
		JPanel pN = new JPanel();
		lblTieuDe = new JLabel("QUẢN LÝ HÓA ĐƠN", JLabel.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(new Color(21, 94, 239));
		pN.add(lblTieuDe);
		add(pN, BorderLayout.NORTH);

		// Khu vực giữa
		JPanel pC = new JPanel(new BorderLayout(0, 10));
		pC.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

		// Bộ lọc tìm kiếm
		JPanel pLoc = new JPanel();
		pLoc.setBorder(BorderFactory.createTitledBorder("Bộ lọc tìm kiếm"));

		lblTimMaHD = new JLabel("Mã HĐ:");
		txtTimMaHD = new JTextField(8);
		lblTuNgay = new JLabel("Từ ngày:");
		txtTuNgay = new JTextField(8);
		lblDenNgay = new JLabel("Đến ngày:");
		txtDenNgay = new JTextField(8);

		btnTim = new JButton("Tìm kiếm");
		btnTim.setBackground(new Color(21, 94, 239));
		btnTim.setForeground(Color.WHITE);
		btnTim.setFocusPainted(false);

		btnLamMoi = new JButton("Làm mới");
		btnLamMoi.setBackground(Color.WHITE);
		btnLamMoi.setFocusPainted(false);

		btnXemChiTiet = new JButton("Xem phiếu in HĐ");
		btnXemChiTiet.setBackground(new Color(21, 94, 239));
		btnXemChiTiet.setForeground(Color.WHITE);
		btnXemChiTiet.setFocusPainted(false);

		pLoc.add(lblTimMaHD);
		pLoc.add(txtTimMaHD);
		pLoc.add(lblTuNgay);
		pLoc.add(txtTuNgay);
		pLoc.add(lblDenNgay);
		pLoc.add(txtDenNgay);
		pLoc.add(btnTim);
		pLoc.add(btnLamMoi);
		pLoc.add(btnXemChiTiet);
		pC.add(pLoc, BorderLayout.NORTH);

		// Hai bảng dữ liệu
		JPanel pBang = new JPanel(new GridLayout(2, 1, 0, 10));

		// Bảng Hóa đơn
		String[] colsHD = { "Mã HĐ", "Ngày Lập", "Mã NV", "Tiền Khách Đưa", "Tổng Tiền" };
		modelHoaDon = new DefaultTableModel(colsHD, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblHoaDon = new JTable(modelHoaDon);
		dinhDangCotSo(tblHoaDon, 3, 4);

		JScrollPane scrHD = new JScrollPane(tblHoaDon);
		scrHD.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
		pBang.add(scrHD);

		// Bảng Chi tiết hóa đơn
		String[] colsCT = { "Mã SP", "Tên Mặt Hàng", "Đơn Giá", "Số Lượng", "Thành Tiền" };
		modelChiTiet = new DefaultTableModel(colsCT, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblChiTiet = new JTable(modelChiTiet);
		dinhDangCotSo(tblChiTiet, 2, 3, 4);

		JScrollPane scrCT = new JScrollPane(tblChiTiet);
		scrCT.setBorder(BorderFactory.createTitledBorder("Chi tiết mặt hàng đã mua"));
		pBang.add(scrCT);

		pC.add(pBang, BorderLayout.CENTER);
		add(pC, BorderLayout.CENTER);

		// Đăng ký sự kiện
		tblHoaDon.addMouseListener(this);
		btnTim.addActionListener(this);
		btnLamMoi.addActionListener(this);
		btnXemChiTiet.addActionListener(this);

		docDuLieuHoaDon();
	}

	private void dinhDangCotSo(JTable t, int... cols) {
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		for (int c : cols) {
			t.getColumnModel().getColumn(c).setCellRenderer(r);
		}
	}

	public void docDuLieuHoaDon() {
		hienThiDanhSachHoaDon(hoaDonBUS.getAllHoaDon());
	}

	private void hienThiDanhSachHoaDon(List<HoaDon> list) {
		modelHoaDon.setRowCount(0);
		if (list != null) {
			for (HoaDon hd : list) {
				String ngay = hd.getNgayLap() != null ? hd.getNgayLap().format(dtfDisplay) : "";
				String maNV = hd.getNhanVien() != null ? hd.getNhanVien().getMaNV() : "";
				modelHoaDon.addRow(new Object[] {
						hd.getMaHD(),
						ngay,
						maNV,
						df.format(hd.getTienKhachDua()),
						df.format(hd.getTongTien())
				});
			}
		}

		if (modelHoaDon.getRowCount() > 0) {
			tblHoaDon.setRowSelectionInterval(0, 0);
			fillChiTiet(0);
		} else {
			modelChiTiet.setRowCount(0);
		}
	}

	private void fillChiTiet(int row) {
		if (row < 0 || row >= modelHoaDon.getRowCount()) {
			return;
		}
		String maHD = modelHoaDon.getValueAt(row, 0).toString();
		HoaDon hd = hoaDonBUS.getHoaDonTheoMa(maHD);
		modelChiTiet.setRowCount(0);

		if (hd != null && hd.getDsChiTiet() != null) {
			for (ChiTietHoaDon ct : hd.getDsChiTiet()) {
				String maSP = ct.getSanPham() != null ? ct.getSanPham().getMaSP() : "";
				String tenSP = ct.getSanPham() != null ? ct.getSanPham().getTenSP() : "";
				modelChiTiet.addRow(new Object[] {
						maSP,
						tenSP,
						df.format(ct.getDonGia()),
						ct.getSoLuong(),
						df.format(ct.tinhThanhTien())
				});
			}
		}
	}

	private boolean validBoLoc() {
		String tuStr = txtTuNgay.getText().trim();
		String denStr = txtDenNgay.getText().trim();

		if (tuStr.length() > 0) {
			try {
				LocalDate.parse(tuStr, dfDate);
			} catch (DateTimeParseException e) {
				JOptionPane.showMessageDialog(this, "Từ ngày phải có định dạng dd/MM/yyyy!");
				txtTuNgay.requestFocus();
				return false;
			}
		}

		if (denStr.length() > 0) {
			try {
				LocalDate.parse(denStr, dfDate);
			} catch (DateTimeParseException e) {
				JOptionPane.showMessageDialog(this, "Đến ngày phải có định dạng dd/MM/yyyy!");
				txtDenNgay.requestFocus();
				return false;
			}
		}

		if (tuStr.length() > 0 && denStr.length() > 0) {
			LocalDate tu = LocalDate.parse(tuStr, dfDate);
			LocalDate den = LocalDate.parse(denStr, dfDate);
			if (tu.isAfter(den)) {
				JOptionPane.showMessageDialog(this, "Từ ngày không được lớn hơn Đến ngày!");
				txtTuNgay.requestFocus();
				return false;
			}
		}

		return true;
	}

	private void moDialogChiTiet(int row) {
		if (row < 0 || row >= modelHoaDon.getRowCount()) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn cần xem!");
			return;
		}

		String maHD = modelHoaDon.getValueAt(row, 0).toString();
		HoaDon hd = hoaDonBUS.getHoaDonTheoMa(maHD);
		if (hd == null) {
			return;
		}

		JDialog dialog = new JDialog((JFrame) null, "Phiếu Hóa Đơn - " + hd.getMaHD(), true);
		dialog.setSize(480, 450);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout(5, 5));

		JPanel pDHeader = new JPanel();
		JLabel lblTenCH = new JLabel("SIÊU THỊ TIỆN LỢI GỌNMART", JLabel.CENTER);
		lblTenCH.setFont(new Font("Times New Roman", Font.BOLD, 16));
		pDHeader.add(lblTenCH);
		dialog.add(pDHeader, BorderLayout.NORTH);

		String[] cols = { "Mã SP", "Tên Mặt Hàng", "Đơn Giá", "Số Lượng", "Thành Tiền" };
		DefaultTableModel modelD = new DefaultTableModel(cols, 0);
		JTable tblD = new JTable(modelD);
		dinhDangCotSo(tblD, 2, 3, 4);

		if (hd.getDsChiTiet() != null) {
			for (ChiTietHoaDon ct : hd.getDsChiTiet()) {
				modelD.addRow(new Object[] {
						ct.getSanPham() != null ? ct.getSanPham().getMaSP() : "",
						ct.getSanPham() != null ? ct.getSanPham().getTenSP() : "",
						df.format(ct.getDonGia()),
						ct.getSoLuong(),
						df.format(ct.tinhThanhTien())
				});
			}
		}
		dialog.add(new JScrollPane(tblD), BorderLayout.CENTER);

		JPanel pDFooter = new JPanel();
		JLabel lblTong = new JLabel("Tổng tiền: " + df.format(hd.tinhTongTien()) + " VNĐ | Khách đưa: "
				+ df.format(hd.getTienKhachDua()) + " VNĐ | Tiền thừa: " + df.format(hd.tinhTienThua()) + " VNĐ");
		lblTong.setFont(new Font("Times New Roman", Font.BOLD, 13));
		pDFooter.add(lblTong);
		dialog.add(pDFooter, BorderLayout.SOUTH);

		dialog.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTim)) {
			if (!validBoLoc()) {
				return;
			}
			String maHDCanTim = txtTimMaHD.getText().trim().toLowerCase();
			String tuStr = txtTuNgay.getText().trim();
			String denStr = txtDenNgay.getText().trim();

			LocalDate tuNgay = tuStr.isEmpty() ? null : LocalDate.parse(tuStr, dfDate);
			LocalDate denNgay = denStr.isEmpty() ? null : LocalDate.parse(denStr, dfDate);

			List<HoaDon> tatCa = hoaDonBUS.getAllHoaDon();
			List<HoaDon> ketQua = new ArrayList<>();

			if (tatCa != null) {
				for (HoaDon hd : tatCa) {
					boolean khopMa = true;
					boolean khopNgay = true;

					if (!maHDCanTim.isEmpty()) {
						khopMa = hd.getMaHD() != null && hd.getMaHD().toLowerCase().contains(maHDCanTim);
					}

					if (hd.getNgayLap() != null) {
						LocalDate ngay = hd.getNgayLap().toLocalDate();
						if (tuNgay != null && ngay.isBefore(tuNgay)) {
							khopNgay = false;
						}
						if (denNgay != null && ngay.isAfter(denNgay)) {
							khopNgay = false;
						}
					}

					if (khopMa && khopNgay) {
						ketQua.add(hd);
					}
				}
			}
			hienThiDanhSachHoaDon(ketQua);

		} else if (o.equals(btnLamMoi)) {
			txtTimMaHD.setText("");
			txtTuNgay.setText("");
			txtDenNgay.setText("");
			docDuLieuHoaDon();

		} else if (o.equals(btnXemChiTiet)) {
			int row = tblHoaDon.getSelectedRow();
			moDialogChiTiet(row);
		}
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
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}

