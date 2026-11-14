package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entity.SanPham;

public class SanPhamGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	// Màu sắc giao diện đồng bộ theo toàn bộ project
	private Color primaryColor = new Color(21, 94, 239);
	private Color textColor = new Color(23, 43, 77);
	private Color backgroundColor = new Color(243, 246, 252);
	private Color dangerColor = new Color(220, 38, 38);
	private Color successColor = new Color(16, 185, 129);

	// Định dạng tiền tệ
	private DecimalFormat df = new DecimalFormat("#,###");

	// Danh sách Collection lưu trữ trên ứng dụng (dùng chung với BanHangGUI)
	public static ArrayList<SanPham> dsSanPham = BanHangGUI.dsSanPham;

	// 1. Các thành phần form nhập thông tin sản phẩm
	private JLabel lblMaSP, lblTenSP, lblLoaiSP, lblDonGia, lblSoLuongTon, lblTrangThai;
	private JTextField txtMaSP, txtTenSP, txtDonGia, txtSoLuongTon;
	private JComboBox<String> cboLoaiSP;
	private JRadioButton radDangKinhDoanh, radNgungKinhDoanh;
	private ButtonGroup grpTrangThai;
	private JTextField txtMess;

	// 2. Các nút chức năng
	private JButton btnThem, btnXoaTrang, btnSua, btnXoa, btnNgungKD;

	// 3. Khu vực tìm kiếm và lọc
	private JLabel lblTimKiem, lblLocLoai;
	private JTextField txtTimKiem;
	private JComboBox<String> cboLocLoai;
	private JButton btnTim, btnTatCa;

	// 4. Bảng danh sách sản phẩm
	private JTable table;
	private DefaultTableModel tableModel;

	public SanPhamGUI() {
		// Thiết lập layout chính
		setLayout(new BorderLayout(10, 10));
		setBackground(backgroundColor);
		setBorder(new EmptyBorder(10, 15, 10, 15));

		// Tiêu đề phía trên
		JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pNorth.setBackground(backgroundColor);
		JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setForeground(textColor);
		pNorth.add(lblTitle);
		add(pNorth, BorderLayout.NORTH);

		// Khu vực nội dung: Phần trên là Form nhập + Nút chức năng, Phần dưới là Bảng
		JPanel pContent = new JPanel(new BorderLayout(10, 10));
		pContent.setBackground(backgroundColor);

		// 1. Panel thông tin sản phẩm (Form editor)
		JPanel pEditor = taoPanelEditor();
		pContent.add(pEditor, BorderLayout.NORTH);

		// 2. Panel bảng danh sách sản phẩm
		JPanel pTable = taoPanelBangSanPham();
		pContent.add(pTable, BorderLayout.CENTER);

		add(pContent, BorderLayout.CENTER);

		// Đăng ký sự kiện
		dangKySuKien();

		// Cập nhật hiển thị bảng từ Collection
		capNhatDuLieu();
	}

	/**
	 * Tạo Panel Form nhập thông tin sản phẩm và các nút thao tác
	 */
	private JPanel taoPanelEditor() {
		JPanel pEditor = new JPanel(new BorderLayout(5, 5));
		pEditor.setBackground(Color.WHITE);
		pEditor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Thông tin sản phẩm", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 13),
				textColor));

		Box b, b1, b2, b3, b4, b5;
		b = Box.createVerticalBox();
		b.setBorder(new EmptyBorder(8, 12, 8, 12));

		b1 = Box.createHorizontalBox();
		b2 = Box.createHorizontalBox();
		b3 = Box.createHorizontalBox();
		b4 = Box.createHorizontalBox();
		b5 = Box.createHorizontalBox();

		// Hàng 1: Mã SP, Tên SP, Loại SP
		lblMaSP = new JLabel("Mã sản phẩm:");
		txtMaSP = new JTextField();

		lblTenSP = new JLabel("Tên sản phẩm:");
		txtTenSP = new JTextField();

		lblLoaiSP = new JLabel("Loại sản phẩm:");
		String[] loaiSPList = { "Thực phẩm", "Nước giải khát", "Bánh kẹo", "Sữa các loại", "Đồ gia dụng", "Khác" };
		cboLoaiSP = new JComboBox<>(loaiSPList);
		cboLoaiSP.setEditable(true);

		b1.add(lblMaSP);
		b1.add(txtMaSP);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblTenSP);
		b1.add(txtTenSP);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblLoaiSP);
		b1.add(cboLoaiSP);

		// Hàng 2: Đơn giá, Số lượng tồn, Trạng thái
		lblDonGia = new JLabel("Đơn giá (VNĐ):");
		txtDonGia = new JTextField();

		lblSoLuongTon = new JLabel("Số lượng tồn:");
		txtSoLuongTon = new JTextField();

		lblTrangThai = new JLabel("Trạng thái:");
		radDangKinhDoanh = new JRadioButton("Đang kinh doanh", true);
		radNgungKinhDoanh = new JRadioButton("Ngừng kinh doanh");
		radDangKinhDoanh.setBackground(Color.WHITE);
		radNgungKinhDoanh.setBackground(Color.WHITE);

		grpTrangThai = new ButtonGroup();
		grpTrangThai.add(radDangKinhDoanh);
		grpTrangThai.add(radNgungKinhDoanh);

		b2.add(lblDonGia);
		b2.add(txtDonGia);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(lblSoLuongTon);
		b2.add(txtSoLuongTon);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(lblTrangThai);
		b2.add(radDangKinhDoanh);
		b2.add(radNgungKinhDoanh);

		// Hàng 3: Dòng hiển thị thông báo lỗi Regex
		txtMess = new JTextField();
		txtMess.setEditable(false);
		txtMess.setBorder(null);
		txtMess.setForeground(dangerColor);
		txtMess.setFont(new Font("Arial", Font.ITALIC, 12));
		b3.add(txtMess);

		// Hàng 4: Các nút chức năng thêm, xóa trắng, sửa, xóa
		btnThem = new JButton("Thêm sản phẩm");
		btnThem.setBackground(primaryColor);
		btnThem.setForeground(Color.WHITE);
		btnThem.setFont(new Font("Arial", Font.BOLD, 12));

		btnXoaTrang = new JButton("Xóa trắng");
		btnSua = new JButton("Cập nhật");
		btnXoa = new JButton("Xóa");
		btnXoa.setBackground(dangerColor);
		btnXoa.setForeground(Color.WHITE);

		btnNgungKD = new JButton("Ngừng kinh doanh");

		b4.add(btnThem);
		b4.add(Box.createRigidArea(new Dimension(8, 0)));
		b4.add(btnXoaTrang);
		b4.add(Box.createRigidArea(new Dimension(8, 0)));
		b4.add(btnSua);
		b4.add(Box.createRigidArea(new Dimension(8, 0)));
		b4.add(btnXoa);
		b4.add(Box.createRigidArea(new Dimension(8, 0)));
		b4.add(btnNgungKD);
		b4.add(Box.createHorizontalGlue());

		// Hàng 5: Tìm kiếm và bộ lọc
		lblTimKiem = new JLabel("Tìm theo mã/tên:");
		txtTimKiem = new JTextField(15);
		btnTim = new JButton("Tìm");
		btnTim.setBackground(primaryColor);
		btnTim.setForeground(Color.WHITE);

		lblLocLoai = new JLabel("Lọc theo loại:");
		String[] locLoaiList = { "Tất cả", "Thực phẩm", "Nước giải khát", "Bánh kẹo", "Sữa các loại", "Đồ gia dụng",
				"Khác" };
		cboLocLoai = new JComboBox<>(locLoaiList);

		btnTatCa = new JButton("Tất cả");

		b5.add(lblTimKiem);
		b5.add(Box.createRigidArea(new Dimension(5, 0)));
		b5.add(txtTimKiem);
		b5.add(Box.createRigidArea(new Dimension(5, 0)));
		b5.add(btnTim);
		b5.add(Box.createRigidArea(new Dimension(20, 0)));
		b5.add(lblLocLoai);
		b5.add(Box.createRigidArea(new Dimension(5, 0)));
		b5.add(cboLocLoai);
		b5.add(Box.createRigidArea(new Dimension(5, 0)));
		b5.add(btnTatCa);
		b5.add(Box.createHorizontalGlue());

		// Gióng hàng đều đặn các nhãn
		lblMaSP.setPreferredSize(new Dimension(95, 22));
		lblDonGia.setPreferredSize(lblMaSP.getPreferredSize());
		lblTenSP.setPreferredSize(new Dimension(95, 22));
		lblSoLuongTon.setPreferredSize(lblTenSP.getPreferredSize());
		lblLoaiSP.setPreferredSize(new Dimension(90, 22));
		lblTrangThai.setPreferredSize(lblLoaiSP.getPreferredSize());

		b.add(b1);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b2);
		b.add(Box.createRigidArea(new Dimension(0, 4)));
		b.add(b3);
		b.add(Box.createRigidArea(new Dimension(0, 8)));
		b.add(b4);
		b.add(Box.createRigidArea(new Dimension(0, 10)));
		b.add(b5);

		pEditor.add(b, BorderLayout.CENTER);
		return pEditor;
	}

	/**
	 * Tạo Panel bảng danh sách sản phẩm
	 */
	private JPanel taoPanelBangSanPham() {
		JPanel pPanel = new JPanel(new BorderLayout(5, 5));
		pPanel.setBackground(Color.WHITE);
		pPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Danh sách sản phẩm", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 13),
				textColor));

		String[] cols = { "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Đơn giá", "Số lượng tồn", "Trạng thái" };
		tableModel = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		dinhDangTable(table);

		JScrollPane scr = new JScrollPane(table);
		pPanel.add(scr, BorderLayout.CENTER);

		return pPanel;
	}

	/**
	 * Định dạng bảng JTable
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
	 * Đăng ký sự kiện cho các nút và bảng
	 */
	private void dangKySuKien() {
		btnThem.addActionListener(this);
		btnXoaTrang.addActionListener(this);
		btnSua.addActionListener(this);
		btnXoa.addActionListener(this);
		btnNgungKD.addActionListener(this);
		btnTim.addActionListener(this);
		btnTatCa.addActionListener(this);
		cboLocLoai.addActionListener(this);

		table.addMouseListener(this);

		txtTimKiem.addActionListener(e -> xuLyTimKiem());
	}

	/**
	 * Cập nhật dữ liệu hiển thị trên bảng từ Collection
	 */
	public void capNhatDuLieu() {
		tableModel.setRowCount(0);
		for (SanPham sp : dsSanPham) {
			tableModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(), df.format(sp.getDonGia()),
					sp.getSoLuongTon(), sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh" });
		}
	}

	/**
	 * Đổ dữ liệu từ dòng được chọn lên form
	 */
	private void fillForm(int row) {
		if (row != -1) {
			txtMaSP.setText(table.getValueAt(row, 0).toString());
			txtTenSP.setText(table.getValueAt(row, 1).toString());
			cboLoaiSP.setSelectedItem(table.getValueAt(row, 2).toString());
			txtDonGia.setText(table.getValueAt(row, 3).toString().replace(",", ""));
			txtSoLuongTon.setText(table.getValueAt(row, 4).toString());

			String tt = table.getValueAt(row, 5).toString();
			if (tt.equalsIgnoreCase("Đang kinh doanh")) {
				radDangKinhDoanh.setSelected(true);
			} else {
				radNgungKinhDoanh.setSelected(true);
			}

			txtMaSP.setEditable(false);
			txtMess.setText("");
		}
	}

	/**
	 * Xóa trắng form nhập liệu
	 */
	private void clearTextFields() {
		txtMaSP.setText("");
		txtTenSP.setText("");
		cboLoaiSP.setSelectedIndex(0);
		txtDonGia.setText("");
		txtSoLuongTon.setText("");
		radDangKinhDoanh.setSelected(true);
		txtMess.setText("");
		txtMaSP.setEditable(true);
		txtMaSP.requestFocus();
	}

	/**
	 * Kiểm tra tính hợp lệ của dữ liệu bằng REGEX
	 */
	private boolean validData() {
		String maSP = txtMaSP.getText().trim();
		String tenSP = txtTenSP.getText().trim();
		String giaStr = txtDonGia.getText().trim();
		String tonStr = txtSoLuongTon.getText().trim();

		// 1. Kiểm tra Mã sản phẩm: SP theo sau là 3 chữ số (SPxxx)
		if (maSP.length() == 0 || !maSP.matches("^SP\\d{3}$")) {
			txtMess.setText("Mã sản phẩm phải có dạng SPxxx (ví dụ: SP001)");
			txtMaSP.requestFocus();
			return false;
		}

		// 2. Kiểm tra Tên sản phẩm: không được rỗng
		if (tenSP.length() == 0) {
			txtMess.setText("Tên sản phẩm không được để trống");
			txtTenSP.requestFocus();
			return false;
		}

		// 3. Kiểm tra Loại sản phẩm: không được rỗng
		Object loaiObj = cboLoaiSP.getSelectedItem();
		if (loaiObj == null || loaiObj.toString().trim().length() == 0) {
			txtMess.setText("Loại sản phẩm không được để trống");
			cboLoaiSP.requestFocus();
			return false;
		}

		// 4. Kiểm tra Đơn giá: phải là số > 0
		if (giaStr.length() == 0) {
			txtMess.setText("Đơn giá không được để trống");
			txtDonGia.requestFocus();
			return false;
		}
		try {
			double gia = Double.parseDouble(giaStr.replace(",", ""));
			if (gia <= 0) {
				txtMess.setText("Đơn giá phải lớn hơn 0");
				txtDonGia.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			txtMess.setText("Đơn giá phải nhập số hợp lệ");
			txtDonGia.requestFocus();
			return false;
		}

		// 5. Kiểm tra Số lượng tồn: phải là số nguyên >= 0
		if (tonStr.length() == 0) {
			txtMess.setText("Số lượng tồn không được để trống");
			txtSoLuongTon.requestFocus();
			return false;
		}
		try {
			int ton = Integer.parseInt(tonStr);
			if (ton < 0) {
				txtMess.setText("Số lượng tồn phải >= 0");
				txtSoLuongTon.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			txtMess.setText("Số lượng tồn phải nhập số nguyên");
			txtSoLuongTon.requestFocus();
			return false;
		}

		txtMess.setText("");
		return true;
	}

	/**
	 * Tạo đối tượng SanPham từ các trường nhập trên form
	 */
	private SanPham revertSanPhamFromTextFields() {
		String ma = txtMaSP.getText().trim();
		String ten = txtTenSP.getText().trim();
		String loai = cboLoaiSP.getSelectedItem().toString().trim();
		double gia = Double.parseDouble(txtDonGia.getText().trim().replace(",", ""));
		int ton = Integer.parseInt(txtSoLuongTon.getText().trim());
		boolean tt = radDangKinhDoanh.isSelected();

		return new SanPham(ma, ten, loai, gia, ton, tt);
	}

	/**
	 * Tìm sản phẩm theo mã trong Collection
	 */
	private SanPham timSanPhamTheoMa(String maSP) {
		for (SanPham sp : dsSanPham) {
			if (sp.getMaSP().equalsIgnoreCase(maSP)) {
				return sp;
			}
		}
		return null;
	}

	/**
	 * Thêm sản phẩm mới vào Collection
	 */
	private void xuLyThem() {
		if (!validData()) {
			return;
		}

		SanPham sp = revertSanPhamFromTextFields();
		if (timSanPhamTheoMa(sp.getMaSP()) != null) {
			txtMess.setText("Trùng mã sản phẩm: " + sp.getMaSP());
			txtMaSP.requestFocus();
			return;
		}

		dsSanPham.add(sp);
		tableModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(), df.format(sp.getDonGia()),
				sp.getSoLuongTon(), sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh" });

		txtMess.setText("Thêm thành công sản phẩm: " + sp.getMaSP());
		clearTextFields();
	}

	/**
	 * Cập nhật thông tin sản phẩm
	 */
	private void xuLySua() {
		int row = table.getSelectedRow();
		if (row == -1) {
			txtMess.setText("Phải chọn dòng cần cập nhật!");
			return;
		}

		if (validData()) {
			String maSP = table.getValueAt(row, 0).toString();
			SanPham sp = timSanPhamTheoMa(maSP);
			if (sp != null) {
				sp.setTenSP(txtTenSP.getText().trim());
				sp.setLoaiSP(cboLoaiSP.getSelectedItem().toString().trim());
				sp.setDonGia(Double.parseDouble(txtDonGia.getText().trim().replace(",", "")));
				sp.setSoLuongTon(Integer.parseInt(txtSoLuongTon.getText().trim()));
				sp.setTrangThai(radDangKinhDoanh.isSelected());

				tableModel.setValueAt(sp.getTenSP(), row, 1);
				tableModel.setValueAt(sp.getLoaiSP(), row, 2);
				tableModel.setValueAt(df.format(sp.getDonGia()), row, 3);
				tableModel.setValueAt(sp.getSoLuongTon(), row, 4);
				tableModel.setValueAt(sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh", row, 5);

				txtMess.setText("Cập nhật thành công sản phẩm: " + maSP);
			}
		}
	}

	/**
	 * Xóa sản phẩm khỏi Collection
	 */
	private void xuLyXoa() {
		int row = table.getSelectedRow();
		if (row == -1) {
			txtMess.setText("Phải chọn dòng cần xóa!");
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Cảnh báo",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			String maSP = table.getValueAt(row, 0).toString();
			SanPham sp = timSanPhamTheoMa(maSP);
			if (sp != null) {
				dsSanPham.remove(sp);
			}
			tableModel.removeRow(row);
			txtMess.setText("Đã xóa sản phẩm: " + maSP);
			clearTextFields();
		}
	}

	/**
	 * Đổi trạng thái sang ngừng kinh doanh
	 */
	private void xuLyNgungKinhDoanh() {
		int row = table.getSelectedRow();
		if (row == -1) {
			txtMess.setText("Phải chọn dòng cần đổi trạng thái!");
			return;
		}

		String maSP = table.getValueAt(row, 0).toString();
		SanPham sp = timSanPhamTheoMa(maSP);
		if (sp != null) {
			sp.setTrangThai(false);
			tableModel.setValueAt("Ngừng kinh doanh", row, 5);
			radNgungKinhDoanh.setSelected(true);
			txtMess.setText("Sản phẩm [" + maSP + "] đã chuyển sang Ngừng kinh doanh.");
		}
	}

	/**
	 * Tìm kiếm sản phẩm theo Mã hoặc Tên và Loại
	 */
	private void xuLyTimKiem() {
		String tuKhoa = txtTimKiem.getText().trim().toLowerCase();
		String loaiLoc = cboLocLoai.getSelectedItem().toString();

		tableModel.setRowCount(0);
		int dem = 0;

		for (SanPham sp : dsSanPham) {
			boolean khopTuKhoa = tuKhoa.isEmpty() || sp.getMaSP().toLowerCase().contains(tuKhoa)
					|| sp.getTenSP().toLowerCase().contains(tuKhoa);

			boolean khopLoai = loaiLoc.equals("Tất cả") || sp.getLoaiSP().equalsIgnoreCase(loaiLoc);

			if (khopTuKhoa && khopLoai) {
				tableModel.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(), df.format(sp.getDonGia()),
						sp.getSoLuongTon(), sp.isTrangThai() ? "Đang kinh doanh" : "Ngừng kinh doanh" });
				dem++;
			}
		}

		txtMess.setText("Tìm thấy " + dem + " sản phẩm.");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		fillForm(row);
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

		if (o.equals(btnThem)) {
			xuLyThem();
		} else if (o.equals(btnXoaTrang)) {
			clearTextFields();
		} else if (o.equals(btnSua)) {
			xuLySua();
		} else if (o.equals(btnXoa)) {
			xuLyXoa();
		} else if (o.equals(btnNgungKD)) {
			xuLyNgungKinhDoanh();
		} else if (o.equals(btnTim)) {
			xuLyTimKiem();
		} else if (o.equals(btnTatCa)) {
			txtTimKiem.setText("");
			cboLocLoai.setSelectedIndex(0);
			capNhatDuLieu();
			txtMess.setText("Hiển thị toàn bộ sản phẩm.");
		} else if (o.equals(cboLocLoai)) {
			xuLyTimKiem();
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("SanPhamGUI Test");
		f.setSize(1100, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(new SanPhamGUI());
		f.setVisible(true);
	}
}
