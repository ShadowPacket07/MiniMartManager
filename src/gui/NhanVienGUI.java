package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.NhanVienBUS;
import entity.NhanVien;

public class NhanVienGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private NhanVienBUS nhanVienBUS = new NhanVienBUS();

	private JTextField txtMaNV, txtHoTen, txtSDT, txtEmail, txtDiaChi, txtTienLuong;
	private JComboBox<String> cmbGioiTinh;
	private JComboBox<Integer> cmbNgay, cmbThang, cmbNam;

	private JTextField txtTimKiem;
	private JButton btnTimKiem;

	private DefaultTableModel tableModel;
	private JTable table;
	private JButton btnThem, btnSua, btnXoa, btnLamMoi;

	public NhanVienGUI() {
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(243, 246, 252));
		setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

		JPanel pnlForm = new JPanel(new BorderLayout());
		pnlForm.setBackground(Color.WHITE);
		pnlForm.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
						BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15),
								"THÔNG TIN NHÂN VIÊN", 0, 0, new Font("Arial", Font.BOLD, 15),
								new Color(21, 94, 239))));

		Box b = Box.createVerticalBox();
		b.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		Box b1 = Box.createHorizontalBox();
		b1.add(new JLabel("Mã NV:"));
		b1.add(Box.createHorizontalStrut(10));
		b1.add(txtMaNV = new JTextField());
		b1.add(Box.createHorizontalStrut(20));
		b1.add(new JLabel("Họ tên:"));
		b1.add(Box.createHorizontalStrut(10));
		b1.add(txtHoTen = new JTextField());
		b1.add(Box.createHorizontalStrut(20));
		b1.add(new JLabel("Giới tính:"));
		b1.add(Box.createHorizontalStrut(10));
		cmbGioiTinh = new JComboBox<>(new String[] { "Nam", "Nữ", "Khác" });
		b1.add(cmbGioiTinh);

		Box b2 = Box.createHorizontalBox();
		b2.add(new JLabel("SĐT:"));
		b2.add(Box.createHorizontalStrut(10));
		b2.add(txtSDT = new JTextField());
		b2.add(Box.createHorizontalStrut(20));
		b2.add(new JLabel("Email:"));
		b2.add(Box.createHorizontalStrut(10));
		b2.add(txtEmail = new JTextField());
		b2.add(Box.createHorizontalStrut(20));
		b2.add(new JLabel("Ngày sinh:"));
		b2.add(Box.createHorizontalStrut(10));

		JPanel pnlNgaySinh = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		pnlNgaySinh.setOpaque(false);
		pnlNgaySinh.add(new JLabel("Ngày:"));
		cmbNgay = new JComboBox<>();
		for (int i = 1; i <= 31; i++) {
			cmbNgay.addItem(i);
		}
		pnlNgaySinh.add(cmbNgay);

		pnlNgaySinh.add(new JLabel("Tháng:"));
		cmbThang = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			cmbThang.addItem(i);
		}
		pnlNgaySinh.add(cmbThang);

		pnlNgaySinh.add(new JLabel("Năm:"));
		cmbNam = new JComboBox<>();
		int currentYear = LocalDate.now().getYear();
		for (int i = currentYear; i >= 1950; i--) {
			cmbNam.addItem(i);
		}
		cmbNam.setSelectedItem(2000);
		pnlNgaySinh.add(cmbNam);

		b2.add(pnlNgaySinh);

		Box b3 = Box.createHorizontalBox();
		b3.add(new JLabel("Địa chỉ:"));
		b3.add(Box.createHorizontalStrut(10));
		b3.add(txtDiaChi = new JTextField());
		b3.add(Box.createHorizontalStrut(20));
		b3.add(new JLabel("Tiền lương:"));
		b3.add(Box.createHorizontalStrut(10));
		b3.add(txtTienLuong = new JTextField());

		Dimension fieldHeight = new Dimension(Integer.MAX_VALUE, 30);
		txtMaNV.setMaximumSize(fieldHeight);
		txtHoTen.setMaximumSize(fieldHeight);
		txtSDT.setMaximumSize(fieldHeight);
		txtEmail.setMaximumSize(fieldHeight);
		txtDiaChi.setMaximumSize(fieldHeight);
		txtTienLuong.setMaximumSize(fieldHeight);
		cmbGioiTinh.setMaximumSize(fieldHeight);

		b1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		b2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		b3.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

		Dimension lblSize = new Dimension(75, 25);
		((JLabel) b1.getComponent(0)).setPreferredSize(lblSize);
		((JLabel) b2.getComponent(0)).setPreferredSize(lblSize);
		((JLabel) b3.getComponent(0)).setPreferredSize(lblSize);

		Box b4 = Box.createHorizontalBox();
		b4.add(Box.createHorizontalGlue());
		b4.add(btnThem = taoNut("Thêm", new Color(21, 94, 239)));
		b4.add(Box.createHorizontalStrut(20));
		b4.add(btnSua = taoNut("Sửa", new Color(40, 167, 69)));
		b4.add(Box.createHorizontalStrut(20));
		b4.add(btnXoa = taoNut("Xóa", new Color(220, 53, 69)));
		b4.add(Box.createHorizontalStrut(20));
		b4.add(btnLamMoi = taoNut("Làm Mới", new Color(108, 117, 125)));
		b4.add(Box.createHorizontalGlue());

		b.add(b1);
		b.add(Box.createVerticalStrut(10));
		b.add(b2);
		b.add(Box.createVerticalStrut(10));
		b.add(b3);
		b.add(Box.createVerticalStrut(15));
		b.add(b4);
		b.add(Box.createVerticalStrut(5));

		pnlForm.add(b, BorderLayout.CENTER);
		add(pnlForm, BorderLayout.NORTH);

		JPanel pnlCenter = new JPanel(new BorderLayout(5, 10));
		pnlCenter.setOpaque(false);

		JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		pnlSearch.setBackground(Color.WHITE);
		pnlSearch.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		JLabel lblTim = new JLabel("Tìm kiếm (Mã / Tên):");
		lblTim.setFont(new Font("Arial", Font.BOLD, 13));
		pnlSearch.add(lblTim);

		txtTimKiem = new JTextField();
		txtTimKiem.setPreferredSize(new Dimension(250, 32));
		pnlSearch.add(txtTimKiem);

		btnTimKiem = taoNut("Tìm Kiếm", new Color(255, 153, 0));
		btnTimKiem.setPreferredSize(new Dimension(100, 32));
		pnlSearch.add(btnTimKiem);

		pnlCenter.add(pnlSearch, BorderLayout.NORTH);

		String[] headers = { "Mã NV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Địa Chỉ", "Tiền Lương" };
		tableModel = new DefaultTableModel(headers, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setRowHeight(30);
		table.getTableHeader().setBackground(new Color(21, 94, 239));
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
				if (isSelected) {
					c.setBackground(new Color(204, 229, 255));
					c.setForeground(Color.BLACK);
				} else if (row % 2 == 0) {
					c.setBackground(Color.WHITE);
					c.setForeground(Color.BLACK);
				} else {
					c.setBackground(new Color(245, 248, 255));
					c.setForeground(Color.BLACK);
				}
				return c;
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
		pnlCenter.add(scroll, BorderLayout.CENTER);

		add(pnlCenter, BorderLayout.CENTER);

		addTableListener();
		loadData();
	}

	private JButton taoNut(String ten, Color bg) {
		JButton btn = new JButton(ten);
		btn.setFont(new Font("Arial", Font.BOLD, 13));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setPreferredSize(new Dimension(110, 35));
		btn.setMaximumSize(new Dimension(110, 35));
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.addActionListener(this);
		return btn;
	}

	private void loadData() {
		try {
			loadTable(nhanVienBUS.getAllNhanVien());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadTable(List<NhanVien> list) {
		tableModel.setRowCount(0);
		for (NhanVien nv : list) {
			tableModel.addRow(new Object[] { nv.getMaNhanVien(), nv.getHoTen(), nv.getGioiTinh(),
					nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "", nv.getSoDienThoai(), nv.getEmail(),
					nv.getDiaChi(), String.format("%,.0f đ", nv.getTienLuong()) });
		}
	}

	private void addTableListener() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row < 0) {
					return;
				}

				String ma = tableModel.getValueAt(row, 0).toString();
				try {
					NhanVien nv = nhanVienBUS.timTheoMa(ma);
					if (nv == null) {
						return;
					}

					txtMaNV.setText(nv.getMaNhanVien());
					txtHoTen.setText(nv.getHoTen());
					cmbGioiTinh.setSelectedItem(nv.getGioiTinh());
					if (nv.getNgaySinh() != null) {
						cmbNgay.setSelectedItem(nv.getNgaySinh().getDayOfMonth());
						cmbThang.setSelectedItem(nv.getNgaySinh().getMonthValue());
						cmbNam.setSelectedItem(nv.getNgaySinh().getYear());
					}
					txtSDT.setText(nv.getSoDienThoai());
					txtEmail.setText(nv.getEmail());
					txtDiaChi.setText(nv.getDiaChi());
					txtTienLuong.setText(String.format("%.0f", nv.getTienLuong()));
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o == btnThem) {
			NhanVien nv = layDuLieu();
			if (nv == null) {
				return;
			}
			try {
				if (nhanVienBUS.themNhanVien(nv)) {
					JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
					xoaForm();
					loadData();
				} else {
					JOptionPane.showMessageDialog(this, "Thêm thất bại (Mã có thể đã tồn tại)!");
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "Lỗi SQL: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} else if (o == btnSua) {
			if (txtMaNV.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
				return;
			}
			NhanVien nv = layDuLieu();
			if (nv == null) {
				return;
			}
			try {
				if (nhanVienBUS.chinhSuaNhanVien(nv)) {
					JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
					xoaForm();
					loadData();
				} else {
					JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "Lỗi SQL: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			}

		} else if (o == btnXoa) {
			String ma = txtMaNV.getText().trim();
			if (ma.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
				return;
			}
			int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên [" + ma + "]?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (cf == JOptionPane.YES_OPTION) {
				try {
					if (nhanVienBUS.xoaNhanVien(ma)) {
						JOptionPane.showMessageDialog(this, "Xóa thành công!");
						xoaForm();
						loadData();
					} else {
						JOptionPane.showMessageDialog(this,
								"Không thể xóa (nhân viên có tài khoản hoặc đang liên kết hóa đơn)!");
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e1.getMessage(), "Lỗi",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (o == btnLamMoi) {
			xoaForm();
			loadData();

		} else if (o == btnTimKiem) {
			String tuKhoa = txtTimKiem.getText().trim();
			if (tuKhoa.isEmpty()) {
				loadData();
				return;
			}
			try {
				NhanVien nv = nhanVienBUS.timTheoMa(tuKhoa);
				if (nv != null) {
					List<NhanVien> list = new ArrayList<>();
					list.add(nv);
					loadTable(list);
				} else {
					List<NhanVien> list = nhanVienBUS.timKiemTheoTen(tuKhoa);
					if (list.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào phù hợp!");
					}
					loadTable(list);
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e1.getMessage(), "Lỗi",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private void xoaForm() {
		txtMaNV.setText("");
		txtHoTen.setText("");
		txtSDT.setText("");
		txtEmail.setText("");
		txtDiaChi.setText("");
		txtTienLuong.setText("");
		cmbGioiTinh.setSelectedIndex(0);
		cmbNgay.setSelectedItem(1);
		cmbThang.setSelectedItem(1);
		cmbNam.setSelectedItem(2000);
		txtTimKiem.setText("");
		table.clearSelection();
		txtMaNV.requestFocus();
	}

	private NhanVien layDuLieu() {
		String ma = txtMaNV.getText().trim();
		String ten = txtHoTen.getText().trim();
		String sdt = txtSDT.getText().trim();
		String email = txtEmail.getText().trim();
		String diaChi = txtDiaChi.getText().trim();
		String gioiTinh = cmbGioiTinh.getSelectedItem().toString();

		if (ma.isEmpty() || ten.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Mã và Tên nhân viên không được để trống!");
			return null;
		}

		double luong = 0;
		try {
			luong = Double.parseDouble(txtTienLuong.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Tiền lương phải là số hợp lệ!");
			return null;
		}

		int ngay = (int) cmbNgay.getSelectedItem();
		int thang = (int) cmbThang.getSelectedItem();
		int nam = (int) cmbNam.getSelectedItem();

		LocalDate ngaySinh;
		try {
			ngaySinh = LocalDate.of(nam, thang, ngay);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this,
					"Ngày tháng năm sinh không hợp lệ (Ví dụ: tháng 2 không có 30, 31 ngày)!");
			return null;
		}

		if (!sdt.isEmpty() && !sdt.matches("0\\d{9}")) {
			JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 chữ số và bắt đầu bằng số 0!");
			return null;
		}

		return new NhanVien(ma, ten, sdt, diaChi, email, luong, ngaySinh, gioiTinh);
	}
}