package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import javax.swing.table.DefaultTableModel;

import bus.NhanVienBUS;
import entity.NhanVien;

public class NhanVienGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private JLabel lblTieuDe;
	private JLabel lblMaNV, lblHoTen, lblGioiTinh, lblNgaySinh, lblSDT, lblDiaChi, lblVaiTro, lblTrangThai, lblTim;
	private JTextField txtMaNV, txtHoTen, txtNgaySinh, txtSDT, txtDiaChi, txtTim;
	private JComboBox<String> cboGioiTinh, cboVaiTro;
	private JRadioButton radDangLam, radNghiViec;
	private ButtonGroup grpTrangThai;

	private JButton btnTim, btnThem, btnXoaTrang, btnSua, btnXoa;
	private JTable table;
	private DefaultTableModel tableModel;

	private NhanVienBUS nhanVienBUS = new NhanVienBUS();
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public NhanVienGUI() {
		setLayout(new BorderLayout());

		// Tiêu đề phía Bắc
		JPanel pN = new JPanel();
		lblTieuDe = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(new Color(21, 94, 239));
		pN.add(lblTieuDe);
		add(pN, BorderLayout.NORTH);

		// Khu vực giữa
		JPanel pC = new JPanel(new BorderLayout());
		pC.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

		// Form nhập thông tin
		Box b = Box.createVerticalBox();
		b.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();

		lblMaNV = new JLabel("Mã NV:");
		txtMaNV = new JTextField();
		lblHoTen = new JLabel("Họ tên:");
		txtHoTen = new JTextField();
		lblGioiTinh = new JLabel("Giới tính:");
		cboGioiTinh = new JComboBox<String>();
		cboGioiTinh.addItem("Nam");
		cboGioiTinh.addItem("Nữ");
		cboGioiTinh.addItem("Khác");
		cboGioiTinh.setPreferredSize(new Dimension(80, 22));

		b1.add(lblMaNV);
		b1.add(txtMaNV);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblHoTen);
		b1.add(txtHoTen);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblGioiTinh);
		b1.add(cboGioiTinh);

		lblNgaySinh = new JLabel("Ngày sinh:");
		txtNgaySinh = new JTextField();
		lblSDT = new JLabel("Số ĐT:");
		txtSDT = new JTextField();
		lblVaiTro = new JLabel("Vai trò:");
		cboVaiTro = new JComboBox<String>();
		cboVaiTro.addItem("Nhân viên bán hàng");
		cboVaiTro.addItem("Quản lý");
		cboVaiTro.setPreferredSize(new Dimension(140, 22));

		b2.add(lblNgaySinh);
		b2.add(txtNgaySinh);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(lblSDT);
		b2.add(txtSDT);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(lblVaiTro);
		b2.add(cboVaiTro);

		lblDiaChi = new JLabel("Địa chỉ:");
		txtDiaChi = new JTextField();

		lblTrangThai = new JLabel("Trạng thái:");
		radDangLam = new JRadioButton("Đang làm việc", true);
		radNghiViec = new JRadioButton("Đã nghỉ việc");
		grpTrangThai = new ButtonGroup();
		grpTrangThai.add(radDangLam);
		grpTrangThai.add(radNghiViec);

		JPanel pRad = new JPanel();
		pRad.add(radDangLam);
		pRad.add(radNghiViec);

		b3.add(lblDiaChi);
		b3.add(txtDiaChi);
		b3.add(Box.createRigidArea(new Dimension(15, 0)));
		b3.add(lblTrangThai);
		b3.add(pRad);

		Dimension dLbl = new Dimension(80, 22);
		lblMaNV.setPreferredSize(dLbl);
		lblNgaySinh.setPreferredSize(dLbl);
		lblDiaChi.setPreferredSize(dLbl);
		lblHoTen.setPreferredSize(new Dimension(60, 22));
		lblSDT.setPreferredSize(new Dimension(60, 22));
		lblTrangThai.setPreferredSize(new Dimension(70, 22));

		b.add(b1);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b2);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b3);

		pC.add(b, BorderLayout.NORTH);

		// Bảng hiển thị nhân viên
		String[] cols = { "Mã NV", "Họ Tên", "Giới Tính", "Ngày Sinh", "Số ĐT", "Địa Chỉ", "Vai Trò", "Trạng Thái" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);

		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(160);
		table.getColumnModel().getColumn(2).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setPreferredWidth(90);
		table.getColumnModel().getColumn(4).setPreferredWidth(95);
		table.getColumnModel().getColumn(5).setPreferredWidth(160);
		table.getColumnModel().getColumn(6).setPreferredWidth(130);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));
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

		btnXoa = new JButton("Xóa / Nghỉ");
		btnXoa.setBackground(new Color(220, 38, 38));
		btnXoa.setForeground(Color.WHITE);
		btnXoa.setFocusPainted(false);

		pS.add(lblTim);
		pS.add(txtTim);
		pS.add(btnTim);
		pS.add(Box.createRigidArea(new Dimension(15, 0)));
		pS.add(btnThem);
		pS.add(btnXoaTrang);
		pS.add(btnSua);
		pS.add(btnXoa);

		add(pS, BorderLayout.SOUTH);

		// Đăng ký sự kiện
		table.addMouseListener(this);
		btnTim.addActionListener(this);
		btnThem.addActionListener(this);
		btnXoaTrang.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);

		loadData();
	}

	public void loadData() {
		tableModel.setRowCount(0);
		List<NhanVien> ds = nhanVienBUS.getAllNhanVien();
		if (ds != null) {
			for (NhanVien nv : ds) {
				String ns = nv.getNgaySinh() != null ? nv.getNgaySinh().format(df) : "";
				tableModel.addRow(new Object[] {
						nv.getMaNV(),
						nv.getHoTen(),
						nv.getGioiTinh(),
						ns,
						nv.getSoDienThoai(),
						nv.getDiaChi(),
						nv.getVaiTro(),
						nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ việc"
				});
			}
		}
	}

	private void clearTextFields() {
		txtMaNV.setText("");
		txtHoTen.setText("");
		txtNgaySinh.setText("");
		txtSDT.setText("");
		txtDiaChi.setText("");
		cboGioiTinh.setSelectedIndex(0);
		cboVaiTro.setSelectedIndex(0);
		radDangLam.setSelected(true);
		txtTim.setText("");
		txtMaNV.setEditable(true);
		txtMaNV.requestFocus();
	}

	private boolean validData() {
		String ma = txtMaNV.getText().trim();
		String ten = txtHoTen.getText().trim();
		String nsStr = txtNgaySinh.getText().trim();
		String sdt = txtSDT.getText().trim();
		String diaChi = txtDiaChi.getText().trim();

		// 1. Mã nhân viên: NVxxx hoặc QLxxx
		if (ma.length() == 0 || !ma.matches("^(NV|QL)\\d{3}$")) {
			JOptionPane.showMessageDialog(this, "Mã nhân viên phải theo định dạng NVxxx hoặc QLxxx (ví dụ: NV001, QL001)!");
			txtMaNV.selectAll();
			txtMaNV.requestFocus();
			return false;
		}

		// 2. Họ tên
		if (ten.length() == 0 || !ten.matches("^[\\p{L}\\s]+$")) {
			JOptionPane.showMessageDialog(this, "Họ tên chỉ được chứa chữ cái và khoảng trắng, không được để trống!");
			txtHoTen.selectAll();
			txtHoTen.requestFocus();
			return false;
		}

		// 3. Ngày sinh: dd/MM/yyyy, tuổi từ 18 đến 65
		if (nsStr.length() == 0) {
			JOptionPane.showMessageDialog(this, "Ngày sinh không được để trống (định dạng dd/MM/yyyy)!");
			txtNgaySinh.selectAll();
			txtNgaySinh.requestFocus();
			return false;
		}

		LocalDate ngaySinh;
		try {
			if (nsStr.contains("/")) {
				ngaySinh = LocalDate.parse(nsStr, df);
			} else {
				ngaySinh = LocalDate.parse(nsStr);
			}
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng dd/MM/yyyy (ví dụ: 15/08/2000)!");
			txtNgaySinh.selectAll();
			txtNgaySinh.requestFocus();
			return false;
		}

		int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
		if (tuoi < 18 || tuoi > 65) {
			JOptionPane.showMessageDialog(this, "Tuổi nhân viên phải từ 18 đến 65 tuổi (Hiện tại: " + tuoi + " tuổi)!");
			txtNgaySinh.selectAll();
			txtNgaySinh.requestFocus();
			return false;
		}

		// 4. Số điện thoại: 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09
		if (sdt.length() == 0 || !sdt.matches("^0[35789]\\d{8}$")) {
			JOptionPane.showMessageDialog(this, "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09!");
			txtSDT.selectAll();
			txtSDT.requestFocus();
			return false;
		}

		// 5. Địa chỉ
		if (diaChi.length() == 0) {
			JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!");
			txtDiaChi.selectAll();
			txtDiaChi.requestFocus();
			return false;
		}

		return true;
	}

	private NhanVien revertNhanVienFromTextFields() {
		String ma = txtMaNV.getText().trim();
		String ten = txtHoTen.getText().trim();
		String gt = (String) cboGioiTinh.getSelectedItem();

		String nsStr = txtNgaySinh.getText().trim();
		LocalDate ngaySinh;
		if (nsStr.contains("/")) {
			ngaySinh = LocalDate.parse(nsStr, df);
		} else {
			ngaySinh = LocalDate.parse(nsStr);
		}

		String sdt = txtSDT.getText().trim();
		String diaChi = txtDiaChi.getText().trim();
		String vaiTro = (String) cboVaiTro.getSelectedItem();
		boolean trangThai = radDangLam.isSelected();

		return new NhanVien(ma, ten, gt, ngaySinh, sdt, diaChi, vaiTro, trangThai);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnThem)) {
			if (!validData()) {
				return;
			}
			String ma = txtMaNV.getText().trim();
			if (nhanVienBUS.timTheoMa(ma) != null) {
				JOptionPane.showMessageDialog(this, "Trùng mã nhân viên: " + ma);
				txtMaNV.selectAll();
				txtMaNV.requestFocus();
				return;
			}

			NhanVien nv = revertNhanVienFromTextFields();
			try {
				if (nhanVienBUS.themNhanVien(nv)) {
					JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
					loadData();
					clearTextFields();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
			}

		} else if (o.equals(btnXoaTrang)) {
			clearTextFields();
			loadData();

		} else if (o.equals(btnSua)) {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Phải chọn nhân viên cần sửa!");
				return;
			}
			if (!validData()) {
				return;
			}

			NhanVien nv = revertNhanVienFromTextFields();
			try {
				if (nhanVienBUS.chinhSuaNhanVien(nv)) {
					JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
					loadData();
					clearTextFields();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
			}

		} else if (o.equals(btnXoa)) {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Phải chọn dòng cần xóa!");
				return;
			}
			String ma = table.getValueAt(row, 0).toString();
			int confirm = JOptionPane.showConfirmDialog(this,
					"Bạn có chắc muốn xóa hoặc cho nghỉ việc nhân viên [" + ma + "] không?",
					"Cảnh báo", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					if (nhanVienBUS.xoaNhanVien(ma)) {
						JOptionPane.showMessageDialog(this, "Thực hiện thành công!");
						loadData();
						clearTextFields();
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
				}
			}

		} else if (o.equals(btnTim)) {
			String tuKhoa = txtTim.getText().trim();
			if (tuKhoa.isEmpty()) {
				loadData();
				return;
			}

			NhanVien nv = nhanVienBUS.timTheoMa(tuKhoa);
			tableModel.setRowCount(0);
			if (nv != null) {
				tableModel.addRow(new Object[] {
						nv.getMaNV(),
						nv.getHoTen(),
						nv.getGioiTinh(),
						nv.getNgaySinh() != null ? nv.getNgaySinh().format(df) : "",
						nv.getSoDienThoai(),
						nv.getDiaChi(),
						nv.getVaiTro(),
						nv.isTrangThai() ? "Đang làm việc" : "Đã nghỉ việc"
				});
			} else {
				List<NhanVien> list = nhanVienBUS.timKiemTheoTen(tuKhoa);
				if (list != null && !list.isEmpty()) {
					for (NhanVien item : list) {
						tableModel.addRow(new Object[] {
								item.getMaNV(),
								item.getHoTen(),
								item.getGioiTinh(),
								item.getNgaySinh() != null ? item.getNgaySinh().format(df) : "",
								item.getSoDienThoai(),
								item.getDiaChi(),
								item.getVaiTro(),
								item.isTrangThai() ? "Đang làm việc" : "Đã nghỉ việc"
						});
					}
				} else {
					JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp!");
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		if (row != -1) {
			txtMaNV.setText(table.getValueAt(row, 0).toString());
			txtHoTen.setText(table.getValueAt(row, 1).toString());
			cboGioiTinh.setSelectedItem(table.getValueAt(row, 2).toString());
			txtNgaySinh.setText(table.getValueAt(row, 3).toString());
			txtSDT.setText(table.getValueAt(row, 4).toString());
			txtDiaChi.setText(table.getValueAt(row, 5).toString());
			cboVaiTro.setSelectedItem(table.getValueAt(row, 6).toString());

			String tt = table.getValueAt(row, 7).toString();
			if (tt.equalsIgnoreCase("Đang làm việc")) {
				radDangLam.setSelected(true);
			} else {
				radNghiViec.setSelected(true);
			}

			txtMaNV.setEditable(false);
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

