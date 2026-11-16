package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.SanPhamBUS;
import entity.SanPham;

public class SanPhamGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private JLabel lblTieuDe;
	private JLabel lblMaSP, lblTenSP, lblLoaiSP, lblDonGia, lblSoLuongTon, lblTrangThai, lblTim;
	private JTextField txtMaSP, txtTenSP, txtDonGia, txtSoLuongTon, txtTim;
	private JComboBox<String> cboLoaiSP;
	private JRadioButton radDangKD, radNgungKD;
	private ButtonGroup grpTrangThai;

	private JButton btnTim, btnThem, btnXoaTrang, btnSua, btnNgungKD;
	private JTable table;
	private DefaultTableModel tableModel;

	private SanPhamBUS sanPhamBUS = new SanPhamBUS();
	private DecimalFormat df = new DecimalFormat("#,###", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US));

	public SanPhamGUI() {
		setLayout(new BorderLayout());

		// Tiêu đề phía Bắc
		JPanel pN = new JPanel();
		lblTieuDe = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(new Color(21, 94, 239));
		pN.add(lblTieuDe);
		add(pN, BorderLayout.NORTH);

		// Khu vực giữa
		JPanel pC = new JPanel(new BorderLayout());
		pC.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

		// Form nhập thông tin
		Box b = Box.createVerticalBox();
		b.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();

		lblMaSP = new JLabel("Mã sản phẩm:");
		txtMaSP = new JTextField();
		lblTenSP = new JLabel("Tên sản phẩm:");
		txtTenSP = new JTextField();
		b1.add(lblMaSP);
		b1.add(txtMaSP);
		b1.add(Box.createRigidArea(new Dimension(20, 0)));
		b1.add(lblTenSP);
		b1.add(txtTenSP);

		lblLoaiSP = new JLabel("Loại sản phẩm:");
		cboLoaiSP = new JComboBox<String>();
		cboLoaiSP.addItem("Thực phẩm");
		cboLoaiSP.addItem("Nước giải khát");
		cboLoaiSP.addItem("Bánh kẹo");
		cboLoaiSP.addItem("Hóa mỹ phẩm");
		cboLoaiSP.addItem("Đồ dùng sinh hoạt");
		cboLoaiSP.addItem("Khác");
		cboLoaiSP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

		lblDonGia = new JLabel("Đơn giá (VNĐ):");
		txtDonGia = new JTextField();
		b2.add(lblLoaiSP);
		b2.add(cboLoaiSP);
		b2.add(Box.createRigidArea(new Dimension(20, 0)));
		b2.add(lblDonGia);
		b2.add(txtDonGia);

		lblSoLuongTon = new JLabel("Số lượng tồn:");
		txtSoLuongTon = new JTextField();

		lblTrangThai = new JLabel("Trạng thái:");
		radDangKD = new JRadioButton("Đang kinh doanh", true);
		radNgungKD = new JRadioButton("Ngừng kinh doanh");
		grpTrangThai = new ButtonGroup();
		grpTrangThai.add(radDangKD);
		grpTrangThai.add(radNgungKD);

		JPanel pRad = new JPanel();
		pRad.add(radDangKD);
		pRad.add(radNgungKD);

		b3.add(lblSoLuongTon);
		b3.add(txtSoLuongTon);
		b3.add(Box.createRigidArea(new Dimension(20, 0)));
		b3.add(lblTrangThai);
		b3.add(pRad);

		Dimension dLbl = new Dimension(100, 22);
		lblMaSP.setPreferredSize(dLbl);
		lblLoaiSP.setPreferredSize(dLbl);
		lblSoLuongTon.setPreferredSize(dLbl);
		lblTenSP.setPreferredSize(dLbl);
		lblDonGia.setPreferredSize(dLbl);
		lblTrangThai.setPreferredSize(dLbl);

		b.add(b1);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b2);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b3);

		pC.add(b, BorderLayout.NORTH);

		// Bảng hiển thị
		String[] cols = { "Mã SP", "Tên Sản Phẩm", "Loại Sản Phẩm", "Đơn Giá", "Số Lượng Tồn", "Trạng Thái" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(220);
		table.getColumnModel().getColumn(2).setPreferredWidth(130);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(90);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
		pC.add(scroll, BorderLayout.CENTER);

		add(pC, BorderLayout.CENTER);

		// Thanh chức năng phía Nam
		JPanel pS = new JPanel();
		pS.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

		lblTim = new JLabel("Nhập mã/tên cần tìm:");
		txtTim = new JTextField(15);
		btnTim = new JButton("Tìm");
		btnTim.setBackground(new Color(21, 94, 239));
		btnTim.setForeground(Color.WHITE);
		btnTim.setFocusPainted(false);

		btnThem = new JButton("Thêm");
		btnThem.setBackground(new Color(21, 94, 239));
		btnThem.setForeground(Color.WHITE);
		btnThem.setFocusPainted(false);

		btnXoaTrang = new JButton("Xóa trắng");
		btnXoaTrang.setBackground(Color.WHITE);
		btnXoaTrang.setFocusPainted(false);

		btnSua = new JButton("Sửa");
		btnSua.setBackground(new Color(13, 148, 136));
		btnSua.setForeground(Color.WHITE);
		btnSua.setFocusPainted(false);

		btnNgungKD = new JButton("Ngừng kinh doanh");
		btnNgungKD.setBackground(new Color(220, 38, 38));
		btnNgungKD.setForeground(Color.WHITE);
		btnNgungKD.setFocusPainted(false);

		pS.add(lblTim);
		pS.add(txtTim);
		pS.add(btnTim);
		pS.add(Box.createRigidArea(new Dimension(15, 0)));
		pS.add(btnThem);
		pS.add(btnXoaTrang);
		pS.add(btnSua);
		pS.add(btnNgungKD);

		add(pS, BorderLayout.SOUTH);

		// Đăng ký sự kiện
		table.addMouseListener(this);
		btnTim.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoaTrang.addActionListener(this);
		btnSua.addActionListener(this);
		btnNgungKD.addActionListener(this);

		docDuLieuVaoTable();
	}

	public void docDuLieuVaoTable() {
		tableModel.setRowCount(0);
		List<SanPham> ds = sanPhamBUS.getAllSanPham();
		if (ds != null) {
			for (SanPham sp : ds) {
				tableModel.addRow(new Object[] {
						sp.getMaSP(),
						sp.getTenSP(),
						sp.getLoaiSP(),
						df.format(sp.getDonGia()),
						sp.getSoLuongTon(),
						sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh"
				});
			}
		}
	}

	private void clearTextFields() {
		txtMaSP.setText("");
		txtTenSP.setText("");
		txtDonGia.setText("");
		txtSoLuongTon.setText("");
		cboLoaiSP.setSelectedIndex(0);
		radDangKD.setSelected(true);
		txtTim.setText("");
		txtMaSP.setEditable(true);
		txtMaSP.requestFocus();
	}

	private boolean validData() {
		String maSP = txtMaSP.getText().trim();
		String tenSP = txtTenSP.getText().trim();
		String giaStr = txtDonGia.getText().trim().replace(",", "").replace(".", "");
		String tonStr = txtSoLuongTon.getText().trim();

		// 1. Mã sản phẩm: SPxxx
		if (maSP.length() == 0 || !maSP.matches("^SP\\d{3}$")) {
			JOptionPane.showMessageDialog(this, "Mã sản phẩm phải có dạng SPxxx (ví dụ: SP001)!");
			txtMaSP.selectAll();
			txtMaSP.requestFocus();
			return false;
		}

		// 2. Tên sản phẩm: không rỗng
		if (tenSP.length() == 0) {
			JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
			txtTenSP.selectAll();
			txtTenSP.requestFocus();
			return false;
		}

		// 3. Đơn giá: số > 0
		if (giaStr.length() == 0) {
			JOptionPane.showMessageDialog(this, "Đơn giá không được để trống!");
			txtDonGia.selectAll();
			txtDonGia.requestFocus();
			return false;
		}
		try {
			double gia = Double.parseDouble(giaStr);
			if (gia <= 0) {
				JOptionPane.showMessageDialog(this, "Đơn giá phải lớn hơn 0!");
				txtDonGia.selectAll();
				txtDonGia.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Đơn giá phải là số hợp lệ!");
			txtDonGia.selectAll();
			txtDonGia.requestFocus();
			return false;
		}

		// 4. Số lượng tồn: số nguyên >= 0
		if (tonStr.length() == 0) {
			JOptionPane.showMessageDialog(this, "Số lượng tồn không được để trống!");
			txtSoLuongTon.selectAll();
			txtSoLuongTon.requestFocus();
			return false;
		}
		try {
			int ton = Integer.parseInt(tonStr);
			if (ton < 0) {
				JOptionPane.showMessageDialog(this, "Số lượng tồn không được âm!");
				txtSoLuongTon.selectAll();
				txtSoLuongTon.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Số lượng tồn phải là số nguyên!");
			txtSoLuongTon.selectAll();
			txtSoLuongTon.requestFocus();
			return false;
		}

		return true;
	}

	private SanPham revertSanPhamFromTextFields() {
		String ma = txtMaSP.getText().trim();
		String ten = txtTenSP.getText().trim();
		String loai = (String) cboLoaiSP.getSelectedItem();
		double gia = Double.parseDouble(txtDonGia.getText().trim().replace(",", "").replace(".", ""));
		int ton = Integer.parseInt(txtSoLuongTon.getText().trim());
		boolean tt = radDangKD.isSelected();

		SanPham spCu = sanPhamBUS.getSanPhamTheoMa(ma);
		String hinhAnh = (spCu != null) ? spCu.getHinhAnh() : null;

		return new SanPham(ma, ten, loai, gia, ton, tt, hinhAnh);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnThem)) {
			if (!validData()) {
				return;
			}
			String ma = txtMaSP.getText().trim();
			if (sanPhamBUS.getSanPhamTheoMa(ma) != null) {
				JOptionPane.showMessageDialog(this, "Trùng mã sản phẩm: " + ma);
				txtMaSP.selectAll();
				txtMaSP.requestFocus();
				return;
			}

			SanPham sp = revertSanPhamFromTextFields();
			try {
				if (sanPhamBUS.themSanPham(sp)) {
					JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
					docDuLieuVaoTable();
					clearTextFields();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
			}

		} else if (o.equals(btnXoaTrang)) {
			clearTextFields();
			docDuLieuVaoTable();

		} else if (o.equals(btnSua)) {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Phải chọn dòng cần sửa!");
				return;
			}
			if (!validData()) {
				return;
			}

			SanPham sp = revertSanPhamFromTextFields();
			try {
				if (sanPhamBUS.capNhatSanPham(sp)) {
					JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
					docDuLieuVaoTable();
					clearTextFields();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
			}

		} else if (o.equals(btnNgungKD)) {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Phải chọn sản phẩm cần thao tác!");
				return;
			}
			String maSP = table.getValueAt(row, 0).toString();
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có chắc muốn chuyển trạng thái ngừng kinh doanh cho sản phẩm [" + maSP + "]?",
					"Xác nhận", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					if (sanPhamBUS.ngungKinhDoanh(maSP)) {
						JOptionPane.showMessageDialog(this, "Đã ngừng kinh doanh sản phẩm [" + maSP + "]!");
						docDuLieuVaoTable();
						clearTextFields();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
				}
			}

		} else if (o.equals(btnTim)) {
			String tuKhoa = txtTim.getText().trim();
			if (tuKhoa.isEmpty()) {
				docDuLieuVaoTable();
				return;
			}

			List<SanPham> ds = sanPhamBUS.timKiemSanPham(tuKhoa);
			tableModel.setRowCount(0);
			if (ds != null && !ds.isEmpty()) {
				for (SanPham sp : ds) {
					tableModel.addRow(new Object[] {
							sp.getMaSP(),
							sp.getTenSP(),
							sp.getLoaiSP(),
							df.format(sp.getDonGia()),
							sp.getSoLuongTon(),
							sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh"
					});
				}
			} else {
				JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào!");
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row != -1) {
			txtMaSP.setText(table.getValueAt(row, 0).toString());
			txtTenSP.setText(table.getValueAt(row, 1).toString());
			cboLoaiSP.setSelectedItem(table.getValueAt(row, 2).toString());
			String gia = table.getValueAt(row, 3).toString().replace(",", "").replace(".", "");
			txtDonGia.setText(gia);
			txtSoLuongTon.setText(table.getValueAt(row, 4).toString());

			String tt = table.getValueAt(row, 5).toString();
			if (tt.equalsIgnoreCase("Đang kinh doanh")) {
				radDangKD.setSelected(true);
			} else {
				radNgungKD.setSelected(true);
			}

			txtMaSP.setEditable(false);
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

