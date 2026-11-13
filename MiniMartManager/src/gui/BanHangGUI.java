package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.NhanVien;
import entity.SanPham;

public class BanHangGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	// Màu sắc giao diện đồng bộ theo MainGUI
	private Color primaryColor = new Color(21, 94, 239);
	private Color textColor = new Color(23, 43, 77);
	private Color backgroundColor = new Color(243, 246, 252);
	private Color successColor = new Color(16, 185, 129);
	private Color dangerColor = new Color(220, 38, 38);

	// Định dạng tiền tệ và ngày giờ
	private DecimalFormat df = new DecimalFormat("#,###");
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	// Danh sách Collection lưu trữ trên ứng dụng (không có CSDL)
	public static ArrayList<SanPham> dsSanPham = new ArrayList<>();
	private ArrayList<ChiTietHoaDon> dsGioHang = new ArrayList<>();

	// 1. Thành phần Quản lý / Nhập sản phẩm mới
	private JLabel lblMaSP, lblTenSP, lblLoaiSP, lblDonGia, lblSoLuongTon;
	private JTextField txtMaSP, txtTenSP, txtLoaiSP, txtDonGia, txtSoLuongTon;
	private JButton btnThemSP, btnXoaTrangSP;
	private JTextField txtMessSP;

	// 2. Thành phần Bảng sản phẩm & Chọn vào giỏ
	private JLabel lblTimSP, lblSLMua;
	private JTextField txtTimSP, txtSLMua;
	private JButton btnTimSP, btnTatCaSP, btnThemGio;
	private JTable tblSanPham;
	private DefaultTableModel modelSanPham;

	// 3. Thành phần Bảng giỏ hàng
	private JTable tblGioHang;
	private DefaultTableModel modelGioHang;
	private JButton btnSuaSL, btnXoaMon, btnXoaRongGio;

	// 4. Thành phần Tính tiền & Thanh toán
	private JLabel lblMaHD, lblNgayLap, lblNhanVien, lblTongTien, lblTienKhach, lblTienThua;
	private JTextField txtMaHD, txtNgayLap, txtNhanVien, txtTongTien, txtTienKhach, txtTienThua;
	private JButton btnThanhToan, btnHuyDon;
	private JTextField txtMessThanhToan;

	private double tongTienHoaDon = 0;
	private int demMaHD = 1;

	public BanHangGUI() {
		// Thiết lập layout chính cho panel
		setLayout(new BorderLayout(10, 10));
		setBackground(backgroundColor);
		setBorder(new EmptyBorder(10, 15, 10, 15));

		// Tiêu đề phía trên
		JPanel pNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pNorth.setBackground(backgroundColor);
		JLabel lblTitle = new JLabel("MÀN HÌNH BÁN HÀNG (POS)");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setForeground(textColor);
		pNorth.add(lblTitle);
		add(pNorth, BorderLayout.NORTH);

		// Khu vực nội dung chia làm 2 phần: Trái (Sản phẩm & Giỏ hàng), Phải (Thanh toán)
		JPanel pCenter = new JPanel(new BorderLayout(10, 10));
		pCenter.setBackground(backgroundColor);

		JPanel pLeft = taoPanelTrai();
		pCenter.add(pLeft, BorderLayout.CENTER);

		JPanel pRight = taoPanelPhai();
		pCenter.add(pRight, BorderLayout.EAST);

		add(pCenter, BorderLayout.CENTER);

		// Đăng ký sự kiện
		dangKySuKien();

		// Khởi tạo trạng thái ban đầu cho hóa đơn
		taoHoaDonMoi();
	}

	/**
	 * Tạo Panel bên trái: Nhập SP, Bảng SP và Giỏ hàng
	 */
	private JPanel taoPanelTrai() {
		JPanel pLeft = new JPanel();
		pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));
		pLeft.setBackground(backgroundColor);

		// ===== PHẦN 1: KHU VỰC SẢN PHẨM =====
		JPanel pSP = new JPanel(new BorderLayout(5, 5));
		pSP.setBackground(Color.WHITE);
		pSP.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Quản lý & Chọn sản phẩm",
				TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 13), textColor));

		// Box nhập thông tin sản phẩm và regex
		Box bSP, b1, b2, b3, b4;
		bSP = Box.createVerticalBox();
		bSP.setBorder(new EmptyBorder(5, 10, 5, 10));

		b1 = Box.createHorizontalBox();
		b2 = Box.createHorizontalBox();
		b3 = Box.createHorizontalBox();
		b4 = Box.createHorizontalBox();

		lblMaSP = new JLabel("Mã SP:");
		txtMaSP = new JTextField();
		lblTenSP = new JLabel("Tên SP:");
		txtTenSP = new JTextField();
		lblLoaiSP = new JLabel("Loại SP:");
		txtLoaiSP = new JTextField();

		b1.add(lblMaSP);
		b1.add(txtMaSP);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblTenSP);
		b1.add(txtTenSP);
		b1.add(Box.createRigidArea(new Dimension(15, 0)));
		b1.add(lblLoaiSP);
		b1.add(txtLoaiSP);

		lblDonGia = new JLabel("Đơn giá:");
		txtDonGia = new JTextField();
		lblSoLuongTon = new JLabel("Tồn kho:");
		txtSoLuongTon = new JTextField();
		btnThemSP = new JButton("Thêm SP");
		btnXoaTrangSP = new JButton("Xóa trắng");

		btnThemSP.setBackground(primaryColor);
		btnThemSP.setForeground(Color.WHITE);
		btnThemSP.setFont(new Font("Arial", Font.BOLD, 12));

		b2.add(lblDonGia);
		b2.add(txtDonGia);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(lblSoLuongTon);
		b2.add(txtSoLuongTon);
		b2.add(Box.createRigidArea(new Dimension(15, 0)));
		b2.add(btnThemSP);
		b2.add(Box.createRigidArea(new Dimension(5, 0)));
		b2.add(btnXoaTrangSP);

		// Dòng hiển thị thông báo lỗi Regex sản phẩm
		txtMessSP = new JTextField();
		txtMessSP.setEditable(false);
		txtMessSP.setBorder(null);
		txtMessSP.setForeground(dangerColor);
		txtMessSP.setFont(new Font("Arial", Font.ITALIC, 12));
		b3.add(txtMessSP);

		// Dòng tìm kiếm và nút Thêm vào giỏ
		lblTimSP = new JLabel("Tìm SP:");
		txtTimSP = new JTextField();
		btnTimSP = new JButton("Tìm");
		btnTatCaSP = new JButton("Tất cả");

		lblSLMua = new JLabel("SL mua:");
		txtSLMua = new JTextField("1", 4);
		btnThemGio = new JButton("+ Thêm vào giỏ");
		btnThemGio.setBackground(primaryColor);
		btnThemGio.setForeground(Color.WHITE);
		btnThemGio.setFont(new Font("Arial", Font.BOLD, 12));

		b4.add(lblTimSP);
		b4.add(txtTimSP);
		b4.add(Box.createRigidArea(new Dimension(5, 0)));
		b4.add(btnTimSP);
		b4.add(Box.createRigidArea(new Dimension(5, 0)));
		b4.add(btnTatCaSP);
		b4.add(Box.createRigidArea(new Dimension(20, 0)));
		b4.add(lblSLMua);
		b4.add(txtSLMua);
		b4.add(Box.createRigidArea(new Dimension(5, 0)));
		b4.add(btnThemGio);

		// Căn chỉnh kích thước nhãn để thẳng hàng
		lblMaSP.setPreferredSize(new Dimension(55, 22));
		lblDonGia.setPreferredSize(lblMaSP.getPreferredSize());
		lblTimSP.setPreferredSize(lblMaSP.getPreferredSize());
		lblTenSP.setPreferredSize(new Dimension(55, 22));
		lblSoLuongTon.setPreferredSize(lblTenSP.getPreferredSize());
		lblLoaiSP.setPreferredSize(new Dimension(55, 22));

		bSP.add(b1);
		bSP.add(Box.createRigidArea(new Dimension(0, 6)));
		bSP.add(b2);
		bSP.add(Box.createRigidArea(new Dimension(0, 4)));
		bSP.add(b3);
		bSP.add(Box.createRigidArea(new Dimension(0, 6)));
		bSP.add(b4);

		pSP.add(bSP, BorderLayout.NORTH);

		// Bảng danh sách sản phẩm
		String[] colsSP = { "Mã SP", "Tên sản phẩm", "Loại", "Đơn giá", "Tồn kho" };
		modelSanPham = new DefaultTableModel(colsSP, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblSanPham = new JTable(modelSanPham);
		dinhDangTable(tblSanPham);

		JScrollPane scrSP = new JScrollPane(tblSanPham);
		scrSP.setPreferredSize(new Dimension(0, 140));
		pSP.add(scrSP, BorderLayout.CENTER);

		// ===== PHẦN 2: KHU VỰC GIỎ HÀNG =====
		JPanel pGH = new JPanel(new BorderLayout(5, 5));
		pGH.setBackground(Color.WHITE);
		pGH.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Giỏ hàng của khách",
				TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 13), textColor));

		String[] colsGH = { "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền" };
		modelGioHang = new DefaultTableModel(colsGH, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3; // Cho phép sửa trực tiếp cột Số lượng
			}
		};
		tblGioHang = new JTable(modelGioHang);
		dinhDangTable(tblGioHang);

		JScrollPane scrGH = new JScrollPane(tblGioHang);
		scrGH.setPreferredSize(new Dimension(0, 150));
		pGH.add(scrGH, BorderLayout.CENTER);

		// Nút chức năng giỏ hàng
		JPanel pNutGH = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		pNutGH.setBackground(Color.WHITE);

		btnSuaSL = new JButton("Sửa số lượng");
		btnXoaMon = new JButton("Xóa món");
		btnXoaMon.setBackground(dangerColor);
		btnXoaMon.setForeground(Color.WHITE);

		btnXoaRongGio = new JButton("Xóa tất cả");

		pNutGH.add(btnSuaSL);
		pNutGH.add(btnXoaMon);
		pNutGH.add(btnXoaRongGio);
		pGH.add(pNutGH, BorderLayout.SOUTH);

		pLeft.add(pSP);
		pLeft.add(Box.createVerticalStrut(10));
		pLeft.add(pGH);

		return pLeft;
	}

	/**
	 * Tạo Panel bên phải: Thông tin tính tiền và thanh toán
	 */
	private JPanel taoPanelPhai() {
		JPanel pRight = new JPanel(new BorderLayout());
		pRight.setPreferredSize(new Dimension(330, 0));
		pRight.setBackground(Color.WHITE);
		pRight.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225)),
				"Thông tin thanh toán",
				TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 14), textColor));

		Box bTT = Box.createVerticalBox();
		bTT.setBorder(new EmptyBorder(10, 12, 10, 12));

		// Mã HĐ
		Box bMaHD = Box.createHorizontalBox();
		lblMaHD = new JLabel("Mã hóa đơn:");
		txtMaHD = new JTextField();
		txtMaHD.setEditable(false);
		txtMaHD.setFont(new Font("Arial", Font.BOLD, 13));
		bMaHD.add(lblMaHD);
		bMaHD.add(txtMaHD);

		// Ngày lập
		Box bNgay = Box.createHorizontalBox();
		lblNgayLap = new JLabel("Ngày lập:");
		txtNgayLap = new JTextField();
		txtNgayLap.setEditable(false);
		bNgay.add(lblNgayLap);
		bNgay.add(txtNgayLap);

		// Nhân viên
		Box bNV = Box.createHorizontalBox();
		lblNhanVien = new JLabel("Nhân viên:");
		txtNhanVien = new JTextField("NV001");
		txtNhanVien.setEditable(false);
		bNV.add(lblNhanVien);
		bNV.add(txtNhanVien);

		// Tổng tiền
		Box bTong = Box.createHorizontalBox();
		lblTongTien = new JLabel("Tổng tiền:");
		lblTongTien.setForeground(dangerColor);
		lblTongTien.setFont(new Font("Arial", Font.BOLD, 13));
		txtTongTien = new JTextField("0");
		txtTongTien.setEditable(false);
		txtTongTien.setFont(new Font("Arial", Font.BOLD, 18));
		txtTongTien.setForeground(dangerColor);
		txtTongTien.setHorizontalAlignment(JTextField.RIGHT);
		bTong.add(lblTongTien);
		bTong.add(txtTongTien);

		// Tiền khách đưa
		Box bKhach = Box.createHorizontalBox();
		lblTienKhach = new JLabel("Khách đưa:");
		lblTienKhach.setFont(new Font("Arial", Font.BOLD, 13));
		txtTienKhach = new JTextField();
		txtTienKhach.setFont(new Font("Arial", Font.BOLD, 16));
		txtTienKhach.setHorizontalAlignment(JTextField.RIGHT);
		bKhach.add(lblTienKhach);
		bKhach.add(txtTienKhach);

		// Tiền thừa
		Box bThua = Box.createHorizontalBox();
		lblTienThua = new JLabel("Tiền thừa:");
		lblTienThua.setFont(new Font("Arial", Font.BOLD, 13));
		txtTienThua = new JTextField("0");
		txtTienThua.setEditable(false);
		txtTienThua.setFont(new Font("Arial", Font.BOLD, 16));
		txtTienThua.setForeground(successColor);
		txtTienThua.setHorizontalAlignment(JTextField.RIGHT);
		bThua.add(lblTienThua);
		bThua.add(txtTienThua);

		// Thông báo lỗi thanh toán
		txtMessThanhToan = new JTextField();
		txtMessThanhToan.setEditable(false);
		txtMessThanhToan.setBorder(null);
		txtMessThanhToan.setForeground(dangerColor);
		txtMessThanhToan.setFont(new Font("Arial", Font.ITALIC, 12));

		// Canh kích thước nhãn thanh toán
		lblMaHD.setPreferredSize(new Dimension(85, 22));
		lblNgayLap.setPreferredSize(lblMaHD.getPreferredSize());
		lblNhanVien.setPreferredSize(lblMaHD.getPreferredSize());
		lblTongTien.setPreferredSize(lblMaHD.getPreferredSize());
		lblTienKhach.setPreferredSize(lblMaHD.getPreferredSize());
		lblTienThua.setPreferredSize(lblMaHD.getPreferredSize());

		bTT.add(bMaHD);
		bTT.add(Box.createRigidArea(new Dimension(0, 10)));
		bTT.add(bNgay);
		bTT.add(Box.createRigidArea(new Dimension(0, 10)));
		bTT.add(bNV);
		bTT.add(Box.createRigidArea(new Dimension(0, 15)));
		bTT.add(bTong);
		bTT.add(Box.createRigidArea(new Dimension(0, 10)));
		bTT.add(bKhach);
		bTT.add(Box.createRigidArea(new Dimension(0, 10)));
		bTT.add(bThua);
		bTT.add(Box.createRigidArea(new Dimension(0, 8)));
		bTT.add(txtMessThanhToan);
		bTT.add(Box.createVerticalGlue());

		pRight.add(bTT, BorderLayout.CENTER);

		// Nút thanh toán và nút hủy ở dưới
		JPanel pNutTT = new JPanel();
		pNutTT.setLayout(new BoxLayout(pNutTT, BoxLayout.Y_AXIS));
		pNutTT.setBackground(Color.WHITE);
		pNutTT.setBorder(new EmptyBorder(10, 15, 15, 15));

		btnThanhToan = new JButton("XÁC NHẬN THANH TOÁN");
		btnThanhToan.setBackground(successColor);
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setFont(new Font("Arial", Font.BOLD, 15));
		btnThanhToan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		btnThanhToan.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnHuyDon = new JButton("Hủy / Tạo đơn mới");
		btnHuyDon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		btnHuyDon.setAlignmentX(Component.CENTER_ALIGNMENT);

		pNutTT.add(btnThanhToan);
		pNutTT.add(Box.createVerticalStrut(8));
		pNutTT.add(btnHuyDon);

		pRight.add(pNutTT, BorderLayout.SOUTH);

		return pRight;
	}

	/**
	 * Định dạng chuẩn cho bảng JTable
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
		btnThemSP.addActionListener(this);
		btnXoaTrangSP.addActionListener(this);
		btnTimSP.addActionListener(this);
		btnTatCaSP.addActionListener(this);
		btnThemGio.addActionListener(this);
		btnSuaSL.addActionListener(this);
		btnXoaMon.addActionListener(this);
		btnXoaRongGio.addActionListener(this);
		btnThanhToan.addActionListener(this);
		btnHuyDon.addActionListener(this);

		tblSanPham.addMouseListener(this);

		// Tự động tính tiền thừa khi gõ ô tiền khách đưa
		txtTienKhach.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				tinhTienThua();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				tinhTienThua();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				tinhTienThua();
			}
		});

		// Bắt sự kiện sửa số lượng trực tiếp trong giỏ hàng
		modelGioHang.addTableModelListener(e -> {
			if (e.getType() == javax.swing.event.TableModelEvent.UPDATE) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				if (col == 3 && row >= 0 && row < modelGioHang.getRowCount()) {
					xuLySuaSLTrucTiep(row);
				}
			}
		});
	}

	/**
	 * Tạo mã và ngày cho hóa đơn mới
	 */
	private void taoHoaDonMoi() {
		txtMaHD.setText(String.format("HD%03d", demMaHD));
		txtNgayLap.setText(LocalDateTime.now().format(dtf));
		txtNhanVien.setText("NV001");

		dsGioHang.clear();
		modelGioHang.setRowCount(0);
		txtTongTien.setText("0");
		txtTienKhach.setText("");
		txtTienThua.setText("0");
		tongTienHoaDon = 0;
		txtMessThanhToan.setText("");
	}

	/**
	 * Kiểm tra REGEX dữ liệu nhập của Sản phẩm
	 */
	private boolean validDataSanPham() {
		String maSP = txtMaSP.getText().trim();
		String tenSP = txtTenSP.getText().trim();
		String loaiSP = txtLoaiSP.getText().trim();
		String giaStr = txtDonGia.getText().trim();
		String tonStr = txtSoLuongTon.getText().trim();

		// 1. Kiểm tra Mã sản phẩm: bắt đầu bằng SP theo sau là 3 chữ số
		if (maSP.length() == 0 || !maSP.matches("^SP\\d{3}$")) {
			txtMessSP.setText("Mã SP phải có dạng SPxxx (ví dụ: SP001)");
			txtMaSP.requestFocus();
			return false;
		}

		// Kiểm tra trùng mã sản phẩm trong collection
		for (SanPham sp : dsSanPham) {
			if (sp.getMaSP().equalsIgnoreCase(maSP)) {
				txtMessSP.setText("Trùng mã sản phẩm: " + maSP);
				txtMaSP.requestFocus();
				return false;
			}
		}

		// 2. Kiểm tra Tên sản phẩm: không được để trống
		if (tenSP.length() == 0) {
			txtMessSP.setText("Tên sản phẩm không được để trống");
			txtTenSP.requestFocus();
			return false;
		}

		// 3. Kiểm tra Loại sản phẩm: không được để trống
		if (loaiSP.length() == 0) {
			txtMessSP.setText("Loại sản phẩm không được để trống");
			txtLoaiSP.requestFocus();
			return false;
		}

		// 4. Kiểm tra Đơn giá: phải là số thực > 0
		if (giaStr.length() == 0) {
			txtMessSP.setText("Đơn giá không được để trống");
			txtDonGia.requestFocus();
			return false;
		}
		try {
			double gia = Double.parseDouble(giaStr.replace(",", ""));
			if (gia <= 0) {
				txtMessSP.setText("Đơn giá phải lớn hơn 0");
				txtDonGia.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			txtMessSP.setText("Đơn giá phải nhập số hợp lệ");
			txtDonGia.requestFocus();
			return false;
		}

		// 5. Kiểm tra Số lượng tồn kho: phải là số nguyên >= 0
		if (tonStr.length() == 0) {
			txtMessSP.setText("Số lượng tồn kho không được để trống");
			txtSoLuongTon.requestFocus();
			return false;
		}
		try {
			int ton = Integer.parseInt(tonStr);
			if (ton < 0) {
				txtMessSP.setText("Số lượng tồn không được âm");
				txtSoLuongTon.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			txtMessSP.setText("Số lượng tồn phải nhập số nguyên");
			txtSoLuongTon.requestFocus();
			return false;
		}

		txtMessSP.setText("");
		return true;
	}

	/**
	 * Thêm sản phẩm mới vào Collection và cập nhật bảng
	 */
	private void xuLyThemSanPham() {
		if (!validDataSanPham()) {
			return;
		}

		String maSP = txtMaSP.getText().trim();
		String tenSP = txtTenSP.getText().trim();
		String loaiSP = txtLoaiSP.getText().trim();
		double donGia = Double.parseDouble(txtDonGia.getText().trim().replace(",", ""));
		int soLuongTon = Integer.parseInt(txtSoLuongTon.getText().trim());

		SanPham sp = new SanPham(maSP, tenSP, loaiSP, donGia, soLuongTon, true);
		dsSanPham.add(sp);

		modelSanPham.addRow(new Object[] {
				sp.getMaSP(),
				sp.getTenSP(),
				sp.getLoaiSP(),
				df.format(sp.getDonGia()),
				sp.getSoLuongTon()
		});

		txtMessSP.setText("Đã thêm thành công sản phẩm: " + maSP);
		xoaTrangSanPham();
	}

	/**
	 * Xóa trắng form nhập sản phẩm
	 */
	private void xoaTrangSanPham() {
		txtMaSP.setText("");
		txtTenSP.setText("");
		txtLoaiSP.setText("");
		txtDonGia.setText("");
		txtSoLuongTon.setText("");
		txtMaSP.requestFocus();
	}

	/**
	 * Tìm kiếm sản phẩm theo mã hoặc tên trong Collection
	 */
	private void xuLyTimSanPham() {
		String tuKhoa = txtTimSP.getText().trim().toLowerCase();
		modelSanPham.setRowCount(0);

		for (SanPham sp : dsSanPham) {
			if (tuKhoa.isEmpty() || sp.getMaSP().toLowerCase().contains(tuKhoa)
					|| sp.getTenSP().toLowerCase().contains(tuKhoa)) {
				modelSanPham.addRow(new Object[] {
						sp.getMaSP(),
						sp.getTenSP(),
						sp.getLoaiSP(),
						df.format(sp.getDonGia()),
						sp.getSoLuongTon()
				});
			}
		}
	}

	/**
	 * Thêm sản phẩm được chọn vào giỏ hàng
	 */
	private void xuLyThemGio() {
		int row = tblSanPham.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 sản phẩm trong bảng sản phẩm!");
			return;
		}

		// Kiểm tra số lượng mua regex
		String slMuaStr = txtSLMua.getText().trim();
		if (!slMuaStr.matches("^[1-9]\\d*$")) {
			JOptionPane.showMessageDialog(this, "Số lượng mua phải là số nguyên dương > 0!");
			txtSLMua.requestFocus();
			return;
		}

		int slMua = Integer.parseInt(slMuaStr);
		String maSP = modelSanPham.getValueAt(row, 0).toString();
		SanPham sp = timSanPhamTheoMa(maSP);

		if (sp == null) {
			return;
		}

		// Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
		int dongTrung = -1;
		for (int i = 0; i < modelGioHang.getRowCount(); i++) {
			if (modelGioHang.getValueAt(i, 0).toString().equalsIgnoreCase(maSP)) {
				dongTrung = i;
				break;
			}
		}

		if (dongTrung != -1) {
			int slCu = Integer.parseInt(modelGioHang.getValueAt(dongTrung, 3).toString());
			int tongSL = slCu + slMua;
			if (tongSL > sp.getSoLuongTon()) {
				JOptionPane.showMessageDialog(this, "Tổng số lượng trong giỏ (" + tongSL + ") vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
				return;
			}
			modelGioHang.setValueAt(tongSL, dongTrung, 3);
			modelGioHang.setValueAt(df.format(tongSL * sp.getDonGia()), dongTrung, 4);
		} else {
			if (slMua > sp.getSoLuongTon()) {
				JOptionPane.showMessageDialog(this, "Số lượng mua (" + slMua + ") vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
				return;
			}
			modelGioHang.addRow(new Object[] {
					sp.getMaSP(),
					sp.getTenSP(),
					df.format(sp.getDonGia()),
					slMua,
					df.format(slMua * sp.getDonGia())
			});
		}

		txtSLMua.setText("1");
		tinhTongTien();
	}

	/**
	 * Sửa số lượng trực tiếp trong bảng giỏ hàng
	 */
	private void xuLySuaSLTrucTiep(int row) {
		try {
			int slMoi = Integer.parseInt(modelGioHang.getValueAt(row, 3).toString().trim());
			String maSP = modelGioHang.getValueAt(row, 0).toString();
			SanPham sp = timSanPhamTheoMa(maSP);

			if (slMoi <= 0) {
				JOptionPane.showMessageDialog(this, "Số lượng phải > 0! Để xóa vui lòng chọn nút Xóa món.");
				modelGioHang.setValueAt(1, row, 3);
				slMoi = 1;
			} else if (sp != null && slMoi > sp.getSoLuongTon()) {
				JOptionPane.showMessageDialog(this, "Số lượng (" + slMoi + ") vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
				modelGioHang.setValueAt(sp.getSoLuongTon(), row, 3);
				slMoi = sp.getSoLuongTon();
			}

			if (sp != null) {
				modelGioHang.setValueAt(df.format(slMoi * sp.getDonGia()), row, 4);
			}
			tinhTongTien();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
			modelGioHang.setValueAt(1, row, 3);
			tinhTongTien();
		}
	}

	/**
	 * Sửa số lượng bằng nút
	 */
	private void xuLySuaSLDialog() {
		int row = tblGioHang.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn món trong giỏ cần sửa số lượng!");
			return;
		}

		String maSP = modelGioHang.getValueAt(row, 0).toString();
		SanPham sp = timSanPhamTheoMa(maSP);
		String nhap = JOptionPane.showInputDialog(this, "Nhập số lượng mới:", modelGioHang.getValueAt(row, 3));

		if (nhap != null && !nhap.trim().isEmpty()) {
			try {
				int slMoi = Integer.parseInt(nhap.trim());
				if (slMoi <= 0) {
					JOptionPane.showMessageDialog(this, "Số lượng phải > 0!");
					return;
				}
				if (sp != null && slMoi > sp.getSoLuongTon()) {
					JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
					return;
				}
				modelGioHang.setValueAt(slMoi, row, 3);
				if (sp != null) {
					modelGioHang.setValueAt(df.format(slMoi * sp.getDonGia()), row, 4);
				}
				tinhTongTien();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
			}
		}
	}

	/**
	 * Xóa món khỏi giỏ hàng
	 */
	private void xuLyXoaMon() {
		int row = tblGioHang.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa khỏi giỏ!");
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa món này khỏi giỏ hàng?", "Xác nhận",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			modelGioHang.removeRow(row);
			tinhTongTien();
		}
	}

	/**
	 * Tính tổng tiền giỏ hàng
	 */
	private void tinhTongTien() {
		tongTienHoaDon = 0;
		for (int i = 0; i < modelGioHang.getRowCount(); i++) {
			double thanhTien = Double.parseDouble(modelGioHang.getValueAt(i, 4).toString().replace(",", ""));
			tongTienHoaDon += thanhTien;
		}

		txtTongTien.setText(df.format(tongTienHoaDon));
		tinhTienThua();
	}

	/**
	 * Tính tiền thừa trả khách
	 */
	private void tinhTienThua() {
		String tienKhachStr = txtTienKhach.getText().trim().replace(",", "");
		if (tienKhachStr.isEmpty()) {
			txtTienThua.setText("0");
			txtTienThua.setForeground(successColor);
			return;
		}

		try {
			double tienKhach = Double.parseDouble(tienKhachStr);
			double tienThua = tienKhach - tongTienHoaDon;
			if (tienThua >= 0) {
				txtTienThua.setText(df.format(tienThua));
				txtTienThua.setForeground(successColor);
				txtMessThanhToan.setText("");
			} else {
				txtTienThua.setText("-" + df.format(Math.abs(tienThua)));
				txtTienThua.setForeground(dangerColor);
				txtMessThanhToan.setText("Khách đưa chưa đủ tiền thanh toán!");
			}
		} catch (NumberFormatException e) {
			txtTienThua.setText("Lỗi số tiền");
			txtTienThua.setForeground(dangerColor);
		}
	}

	/**
	 * Xử lý xác nhận thanh toán và lưu vào Collection
	 */
	private void xuLyThanhToan() {
		// 1. Kiểm tra giỏ hàng
		if (modelGioHang.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống! Vui lòng chọn sản phẩm.");
			return;
		}

		// 2. Kiểm tra tiền khách đưa regex và số học
		String tienKhachStr = txtTienKhach.getText().trim().replace(",", "");
		if (tienKhachStr.length() == 0) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tiền khách đưa!");
			txtTienKhach.requestFocus();
			return;
		}

		double tienKhach = 0;
		try {
			tienKhach = Double.parseDouble(tienKhachStr);
			if (tienKhach < tongTienHoaDon) {
				JOptionPane.showMessageDialog(this, "Tiền khách đưa chưa đủ để thanh toán!");
				txtTienKhach.requestFocus();
				return;
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Tiền khách đưa phải là số hợp lệ!");
			txtTienKhach.requestFocus();
			return;
		}

		// 3. Tạo đối tượng Hóa đơn và Chi tiết hóa đơn
		HoaDon hd = new HoaDon(txtMaHD.getText().trim(), LocalDateTime.now(), new NhanVien(), tienKhach);
		ArrayList<ChiTietHoaDon> dsCT = new ArrayList<>();

		for (int i = 0; i < modelGioHang.getRowCount(); i++) {
			String maSP = modelGioHang.getValueAt(i, 0).toString();
			SanPham sp = timSanPhamTheoMa(maSP);
			int sl = Integer.parseInt(modelGioHang.getValueAt(i, 3).toString());
			double donGia = Double.parseDouble(modelGioHang.getValueAt(i, 2).toString().replace(",", ""));

			ChiTietHoaDon ct = new ChiTietHoaDon(hd, sp, sl, donGia);
			dsCT.add(ct);

			// Trừ số lượng tồn kho của sản phẩm trong Collection
			if (sp != null) {
				sp.setSoLuongTon(sp.getSoLuongTon() - sl);
			}
		}
		hd.setDsChiTiet(dsCT);

		// 4. Lưu hóa đơn vào Collection dùng chung
		HoaDonGUI.dsHoaDon.add(hd);

		// Cập nhật lại tồn kho hiển thị trên bảng sản phẩm
		capNhatBangSanPham();

		// 5. Thông báo thanh toán thành công
		JOptionPane.showMessageDialog(this,
				"Thanh toán thành công hóa đơn " + hd.getMaHD() + "!\n"
						+ "Tổng tiền: " + df.format(tongTienHoaDon) + " VNĐ\n"
						+ "Tiền thừa trả khách: " + txtTienThua.getText() + " VNĐ",
				"Thành công", JOptionPane.INFORMATION_MESSAGE);

		// Tăng số mã hóa đơn cho lượt tiếp theo
		demMaHD++;
		taoHoaDonMoi();
	}

	/**
	 * Cập nhật lại toàn bộ bảng sản phẩm từ Collection
	 */
	private void capNhatBangSanPham() {
		modelSanPham.setRowCount(0);
		for (SanPham sp : dsSanPham) {
			modelSanPham.addRow(new Object[] {
					sp.getMaSP(),
					sp.getTenSP(),
					sp.getLoaiSP(),
					df.format(sp.getDonGia()),
					sp.getSoLuongTon()
			});
		}
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

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblSanPham.getSelectedRow();
		if (row != -1) {
			txtMaSP.setText(modelSanPham.getValueAt(row, 0).toString());
			txtTenSP.setText(modelSanPham.getValueAt(row, 1).toString());
			txtLoaiSP.setText(modelSanPham.getValueAt(row, 2).toString());
			txtDonGia.setText(modelSanPham.getValueAt(row, 3).toString().replace(",", ""));
			txtSoLuongTon.setText(modelSanPham.getValueAt(row, 4).toString());
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

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnThemSP)) {
			xuLyThemSanPham();
		} else if (o.equals(btnXoaTrangSP)) {
			xoaTrangSanPham();
			txtMessSP.setText("");
		} else if (o.equals(btnTimSP)) {
			xuLyTimSanPham();
		} else if (o.equals(btnTatCaSP)) {
			txtTimSP.setText("");
			capNhatBangSanPham();
		} else if (o.equals(btnThemGio)) {
			xuLyThemGio();
		} else if (o.equals(btnSuaSL)) {
			xuLySuaSLDialog();
		} else if (o.equals(btnXoaMon)) {
			xuLyXoaMon();
		} else if (o.equals(btnXoaRongGio)) {
			if (modelGioHang.getRowCount() > 0 && JOptionPane.showConfirmDialog(this,
					"Bạn có muốn xóa toàn bộ giỏ hàng?", "Cảnh báo",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				modelGioHang.setRowCount(0);
				tinhTongTien();
			}
		} else if (o.equals(btnThanhToan)) {
			xuLyThanhToan();
		} else if (o.equals(btnHuyDon)) {
			taoHoaDonMoi();
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("BanHangGUI Test");
		f.setSize(1100, 700);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.add(new BanHangGUI());
		f.setVisible(true);
	}
}
