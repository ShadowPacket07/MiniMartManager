package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.HoaDonBUS;
import bus.SanPhamBUS;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.NhanVien;
import entity.SanPham;
import util.Session;

public class BanHangGUI extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private JLabel lblTieuDe;
	private JLabel lblTim, lblSLMua;
	private JTextField txtTim, txtSLMua;
	private JButton btnTim, btnTatCa, btnThemGio;

	private JTable tblSanPham;
	private DefaultTableModel modelSanPham;

	private JTable tblGioHang;
	private DefaultTableModel modelGioHang;
	private JButton btnSuaSL, btnXoaMon, btnXoaRongGio;

	private JLabel lblMaHD, lblNgayLap, lblNhanVien, lblTongTien, lblTienKhach, lblTienThua;
	private JTextField txtMaHD, txtNgayLap, txtNhanVien, txtTongTien, txtTienKhach, txtTienThua;
	private JButton btnThanhToan, btnHuyDon;

	private SanPhamBUS sanPhamBUS = new SanPhamBUS();
	private HoaDonBUS hoaDonBUS = new HoaDonBUS();

	private DecimalFormat df = new DecimalFormat("#,###",
			java.text.DecimalFormatSymbols.getInstance(java.util.Locale.US));
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private double tongTienHoaDon = 0;

	public BanHangGUI() {
		setLayout(new BorderLayout());

		// Tiêu đề phía Bắc
		JPanel pN = new JPanel();
		lblTieuDe = new JLabel("BÁN HÀNG (POS)", JLabel.CENTER);
		lblTieuDe.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblTieuDe.setForeground(new Color(21, 94, 239));
		pN.add(lblTieuDe);
		add(pN, BorderLayout.NORTH);

		// Khu vực giữa: Panel Trái (Sản phẩm & Giỏ hàng), Panel Phải (Thanh toán)
		JPanel pC = new JPanel(new BorderLayout(10, 0));
		pC.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		// === PANEL TRÁI ===
		JPanel pTrai = new JPanel(new BorderLayout(0, 10));

		// Phần trên: Bảng sản phẩm
		JPanel pSP = new JPanel(new BorderLayout(0, 5));
		pSP.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm đang bán"));

		JPanel pTimSP = new JPanel();
		lblTim = new JLabel("Tìm sản phẩm:");
		txtTim = new JTextField(12);
		btnTim = new JButton("Tìm");
		btnTim.setBackground(new Color(21, 94, 239));
		btnTim.setForeground(Color.WHITE);
		btnTim.setFocusPainted(false);

		btnTatCa = new JButton("Tất cả");
		btnTatCa.setBackground(Color.WHITE);
		btnTatCa.setFocusPainted(false);

		lblSLMua = new JLabel("Số lượng mua:");
		txtSLMua = new JTextField("1", 4);

		btnThemGio = new JButton("+ Thêm vào giỏ");
		btnThemGio.setBackground(new Color(22, 163, 74));
		btnThemGio.setForeground(Color.WHITE);
		btnThemGio.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnThemGio.setFocusPainted(false);

		pTimSP.add(lblTim);
		pTimSP.add(txtTim);
		pTimSP.add(btnTim);
		pTimSP.add(btnTatCa);
		pTimSP.add(Box.createRigidArea(new Dimension(15, 0)));
		pTimSP.add(lblSLMua);
		pTimSP.add(txtSLMua);
		pTimSP.add(btnThemGio);
		pSP.add(pTimSP, BorderLayout.NORTH);

		String[] colsSP = { "Ảnh", "Mã SP", "Tên Sản Phẩm", "Loại", "Đơn Giá", "Tồn Kho", "Thêm" };
		modelSanPham = new DefaultTableModel(colsSP, 0) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnIndex == 0 ? Icon.class : Object.class; // cột 0 hiển thị ảnh
			}
		};
		tblSanPham = new JTable(modelSanPham);
		tblSanPham.setRowHeight(56); // hàng cao hơn để vừa ảnh
		dinhDangCotSo(tblSanPham, 4, 5); // Đơn giá, Tồn kho (dịch 1 cột vì thêm cột Ảnh)

		// Cột "Thêm": chữ + to, màu xanh lá, căn giữa
		DefaultTableCellRenderer rCong = new DefaultTableCellRenderer();
		rCong.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		rCong.setForeground(new Color(22, 163, 74));
		rCong.setFont(new Font("Times New Roman", Font.BOLD, 24));
		tblSanPham.getColumnModel().getColumn(6).setCellRenderer(rCong);
		tblSanPham.getColumnModel().getColumn(6).setMaxWidth(50);

		JScrollPane scrSP = new JScrollPane(tblSanPham);
		scrSP.setPreferredSize(new Dimension(180, 240));
		pSP.add(scrSP, BorderLayout.CENTER);
		pTrai.add(pSP, BorderLayout.NORTH);

		// Phần dưới: Giỏ hàng
		JPanel pGH = new JPanel(new BorderLayout(0, 5));
		pGH.setBorder(BorderFactory.createTitledBorder("Giỏ hàng của khách"));

		String[] colsGH = { "Mã SP", "Tên Sản Phẩm", "Đơn Giá", "Số Lượng", "Thành Tiền" };
		modelGioHang = new DefaultTableModel(colsGH, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblGioHang = new JTable(modelGioHang);
		dinhDangCotSo(tblGioHang, 2, 3, 4);

		JScrollPane scrGH = new JScrollPane(tblGioHang);
		scrGH.setPreferredSize(new Dimension(650, 180));
		pGH.add(scrGH, BorderLayout.CENTER);

		JPanel pNutGH = new JPanel();
		btnSuaSL = new JButton("Sửa số lượng");
		btnSuaSL.setBackground(new Color(13, 148, 136));
		btnSuaSL.setForeground(Color.WHITE);
		btnSuaSL.setFocusPainted(false);

		btnXoaMon = new JButton("Xóa món");
		btnXoaMon.setBackground(Color.WHITE);
		btnXoaMon.setFocusPainted(false);

		btnXoaRongGio = new JButton("Xóa tất cả");
		btnXoaRongGio.setBackground(new Color(220, 38, 38));
		btnXoaRongGio.setForeground(Color.WHITE);
		btnXoaRongGio.setFocusPainted(false);
		pNutGH.add(btnSuaSL);
		pNutGH.add(btnXoaMon);
		pNutGH.add(btnXoaRongGio);
		pGH.add(pNutGH, BorderLayout.SOUTH);

		pTrai.add(pGH, BorderLayout.CENTER);
		pC.add(pTrai, BorderLayout.CENTER);

		// === PANEL PHẢI (THANH TOÁN) ===
		JPanel pPhai = new JPanel(new BorderLayout());
		pPhai.setPreferredSize(new Dimension(320, 0));
		pPhai.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));

		Box b = Box.createVerticalBox();
		b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Box b1 = Box.createHorizontalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();
		Box b4 = Box.createHorizontalBox();
		Box b5 = Box.createHorizontalBox();
		Box b6 = Box.createHorizontalBox();

		lblMaHD = new JLabel("Mã HĐ:");
		txtMaHD = new JTextField();
		txtMaHD.setEditable(false);
		b1.add(lblMaHD);
		b1.add(txtMaHD);

		lblNgayLap = new JLabel("Ngày lập:");
		txtNgayLap = new JTextField();
		txtNgayLap.setEditable(false);
		b2.add(lblNgayLap);
		b2.add(txtNgayLap);

		lblNhanVien = new JLabel("Nhân viên:");
		txtNhanVien = new JTextField();
		txtNhanVien.setEditable(false);
		b3.add(lblNhanVien);
		b3.add(txtNhanVien);

		lblTongTien = new JLabel("Tổng tiền:");
		txtTongTien = new JTextField("0");
		txtTongTien.setEditable(false);
		txtTongTien.setFont(new Font("Times New Roman", Font.BOLD, 16));
		txtTongTien.setForeground(Color.RED);
		b4.add(lblTongTien);
		b4.add(txtTongTien);

		lblTienKhach = new JLabel("Tiền khách:");
		txtTienKhach = new JTextField();
		txtTienKhach.setFont(new Font("Times New Roman", Font.BOLD, 15));
		b5.add(lblTienKhach);
		b5.add(txtTienKhach);

		lblTienThua = new JLabel("Tiền thừa:");
		txtTienThua = new JTextField("0");
		txtTienThua.setEditable(false);
		txtTienThua.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtTienThua.setForeground(new Color(0, 128, 0));
		b6.add(lblTienThua);
		b6.add(txtTienThua);

		Dimension dLbl = new Dimension(80, 24);
		lblMaHD.setPreferredSize(dLbl);
		lblNgayLap.setPreferredSize(dLbl);
		lblNhanVien.setPreferredSize(dLbl);
		lblTongTien.setPreferredSize(dLbl);
		lblTienKhach.setPreferredSize(dLbl);
		lblTienThua.setPreferredSize(dLbl);

		b.add(b1);
		b.add(Box.createRigidArea(new Dimension(0, 10)));
		b.add(b2);
		b.add(Box.createRigidArea(new Dimension(0, 10)));
		b.add(b3);
		b.add(Box.createRigidArea(new Dimension(0, 15)));
		b.add(b4);
		b.add(Box.createRigidArea(new Dimension(0, 10)));
		b.add(b5);
		b.add(Box.createRigidArea(new Dimension(0, 10)));
		b.add(b6);
		b.add(Box.createVerticalGlue());

		pPhai.add(b, BorderLayout.CENTER);

		JPanel pNutTT = new JPanel();
		btnThanhToan = new JButton("THANH TOÁN");
		btnThanhToan.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnThanhToan.setBackground(new Color(21, 94, 239));
		btnThanhToan.setForeground(Color.WHITE);
		btnThanhToan.setFocusPainted(false);
		btnThanhToan.setPreferredSize(new Dimension(150, 42));
		btnHuyDon = new JButton("Hủy / Tạo đơn mới");
		btnHuyDon.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		btnHuyDon.setPreferredSize(new Dimension(140, 42));

		pNutTT.add(btnThanhToan);
		pNutTT.add(btnHuyDon);
		pPhai.add(pNutTT, BorderLayout.SOUTH);

		pC.add(pPhai, BorderLayout.EAST);
		add(pC, BorderLayout.CENTER);

		// Đăng ký sự kiện
		tblSanPham.addMouseListener(this);
		btnTim.addActionListener(this);
		btnTatCa.addActionListener(this);
		btnThemGio.addActionListener(this);
		btnSuaSL.addActionListener(this);
		btnXoaMon.addActionListener(this);
		btnXoaRongGio.addActionListener(this);
		btnThanhToan.addActionListener(this);
		btnHuyDon.addActionListener(this);

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

		capNhatBangSanPham();
		taoHoaDonMoi();
	}

	private void themDongSanPham(SanPham sp) {
		ImageIcon icon = taiAnh(sp.getHinhAnh());

		Object[] row = { icon, sp.getMaSP(), sp.getTenSP(), sp.getLoaiSP(), sp.getDonGia(), sp.getSoLuongTon(), "+" // Cột
																													// nút
																													// thêm
		};

		modelSanPham.addRow(row);
	}
	// Đọc ảnh từ đường dẫn trong CSDL (vd: imgs/omachi.jpg) và thu nhỏ 50x50

	private ImageIcon taiAnh(String tenFile) {
		if (tenFile == null || tenFile.trim().isEmpty()) {
			return null;
		}

		// Lấy đường dẫn thư mục gốc của project
		String duongDan = System.getProperty("user.dir") + "/imgs/" + tenFile;

		ImageIcon icon = new ImageIcon(duongDan);

		// Kiểm tra ảnh có tồn tại hay không
		if (icon.getIconWidth() <= 0) {
			System.out.println("Không tìm thấy ảnh: " + duongDan);
			return null;
		}

		// Thu nhỏ ảnh về kích thước 50x50
		Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		return new ImageIcon(img);
	}

	private void dinhDangCotSo(JTable t, int... cols) {
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
		for (int col : cols) {
			t.getColumnModel().getColumn(col).setCellRenderer(r);
		}
	}

	public void taoHoaDonMoi() {
		txtMaHD.setText(hoaDonBUS.taoMaHDTuDong());
		txtNgayLap.setText(LocalDateTime.now().format(dtf));

		String maNV = Session.getMaNVHienTai();
		String tenNV = Session.getTenNguoiDung();
		if (maNV != null && !maNV.isEmpty()) {
			txtNhanVien.setText(maNV + " - " + tenNV);
		} else {
			txtNhanVien.setText("NV001");
		}

		modelGioHang.setRowCount(0);
		txtTongTien.setText("0");
		txtTienKhach.setText("");
		txtTienThua.setText("0");
		tongTienHoaDon = 0;
	}

	public void capNhatBangSanPham() {
		modelSanPham.setRowCount(0);
		List<SanPham> ds = sanPhamBUS.getSanPhamDangKinhDoanh();
		if (ds != null) {
			for (SanPham sp : ds) {
				themDongSanPham(sp);
			}
		}
	}

	private void tinhTongTien() {
		tongTienHoaDon = 0;
		for (int i = 0; i < modelGioHang.getRowCount(); i++) {
			double tt = Double.parseDouble(modelGioHang.getValueAt(i, 4).toString().replace(",", "").replace(".", ""));
			tongTienHoaDon += tt;
		}
		txtTongTien.setText(df.format(tongTienHoaDon));
		tinhTienThua();
	}

	private void tinhTienThua() {
		String tienKhachStr = txtTienKhach.getText().trim().replace(",", "").replace(".", "");
		if (tienKhachStr.isEmpty()) {
			txtTienThua.setText("0");
			return;
		}
		try {
			double tienKhach = Double.parseDouble(tienKhachStr);
			double tienThua = tienKhach - tongTienHoaDon;
			if (tienThua >= 0) {
				txtTienThua.setText(df.format(tienThua));
			} else {
				txtTienThua.setText("-" + df.format(Math.abs(tienThua)));
			}
		} catch (NumberFormatException e) {
			txtTienThua.setText("Lỗi số");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if (o.equals(btnTim)) {
			String tuKhoa = txtTim.getText().trim();
			modelSanPham.setRowCount(0);
			List<SanPham> ds = sanPhamBUS.timKiemSanPham(tuKhoa);
			if (ds != null) {
				for (SanPham sp : ds) {
					if (sp.isTrangThai()) {
						themDongSanPham(sp);
					}
				}
			}

		} else if (o.equals(btnTatCa)) {
			txtTim.setText("");
			capNhatBangSanPham();

		} else if (o.equals(btnThemGio)) {
			int row = tblSanPham.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 sản phẩm trong bảng sản phẩm!");
				return;
			}

			String slStr = txtSLMua.getText().trim();
			if (!slStr.matches("^[1-9]\\d*$")) {
				JOptionPane.showMessageDialog(this, "Số lượng mua phải là số nguyên > 0!");
				txtSLMua.selectAll();
				txtSLMua.requestFocus();
				return;
			}

			int slMua = Integer.parseInt(slStr);
			String maSP = modelSanPham.getValueAt(row, 1).toString();
			SanPham sp = sanPhamBUS.getSanPhamTheoMa(maSP);
			if (sp == null) {
				return;
			}

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
					JOptionPane.showMessageDialog(this,
							"Số lượng mua (" + tongSL + ") vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
					return;
				}
				modelGioHang.setValueAt(tongSL, dongTrung, 3);
				modelGioHang.setValueAt(df.format(tongSL * sp.getDonGia()), dongTrung, 4);
			} else {
				if (slMua > sp.getSoLuongTon()) {
					JOptionPane.showMessageDialog(this,
							"Số lượng mua (" + slMua + ") vượt quá tồn kho (" + sp.getSoLuongTon() + ")!");
					return;
				}
				modelGioHang.addRow(new Object[] { sp.getMaSP(), sp.getTenSP(), df.format(sp.getDonGia()), slMua,
						df.format(slMua * sp.getDonGia()) });
			}

			txtSLMua.setText("1");
			tinhTongTien();

		} else if (o.equals(btnSuaSL)) {
			int row = tblGioHang.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn món trong giỏ hàng cần sửa số lượng!");
				return;
			}
			String maSP = modelGioHang.getValueAt(row, 0).toString();
			SanPham sp = sanPhamBUS.getSanPhamTheoMa(maSP);
			if (sp == null) {
				return;
			}
			int slCu = Integer.parseInt(modelGioHang.getValueAt(row, 3).toString());
			String input = JOptionPane.showInputDialog(this, "Nhập số lượng mới cho sản phẩm [" + sp.getTenSP() + "]:",
					String.valueOf(slCu));
			if (input == null) {
				return;
			}
			input = input.trim();
			if (!input.matches("^[1-9]\\d*$")) {
				JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên > 0!");
				return;
			}
			int slMoi = Integer.parseInt(input);
			if (slMoi > sp.getSoLuongTon()) {
				JOptionPane.showMessageDialog(this,
						"Số lượng yêu cầu (" + slMoi + ") vượt quá tồn kho hiện có (" + sp.getSoLuongTon() + ")!");
				return;
			}
			modelGioHang.setValueAt(slMoi, row, 3);
			modelGioHang.setValueAt(df.format(slMoi * sp.getDonGia()), row, 4);
			tinhTongTien();

		} else if (o.equals(btnXoaMon)) {
			int row = tblGioHang.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa khỏi giỏ!");
				return;
			}
			modelGioHang.removeRow(row);
			tinhTongTien();

		} else if (o.equals(btnXoaRongGio)) {
			if (modelGioHang.getRowCount() > 0
					&& JOptionPane.showConfirmDialog(this, "Bạn có muốn xóa toàn bộ giỏ hàng?", "Xác nhận",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				modelGioHang.setRowCount(0);
				tinhTongTien();
			}

		} else if (o.equals(btnThanhToan)) {
			if (modelGioHang.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
				return;
			}

			String tienKhachStr = txtTienKhach.getText().trim().replace(",", "").replace(".", "");
			if (tienKhachStr.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tiền khách đưa!");
				txtTienKhach.selectAll();
				txtTienKhach.requestFocus();
				return;
			}

			double tienKhach = 0;
			try {
				tienKhach = Double.parseDouble(tienKhachStr);
				if (tienKhach < tongTienHoaDon) {
					JOptionPane.showMessageDialog(this, "Tiền khách đưa chưa đủ để thanh toán!");
					txtTienKhach.selectAll();
					txtTienKhach.requestFocus();
					return;
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Tiền khách đưa phải là số hợp lệ!");
				txtTienKhach.selectAll();
				txtTienKhach.requestFocus();
				return;
			}

			NhanVien nv = Session.getNhanVienHienTai();
			if (nv == null) {
				nv = new bus.NhanVienBUS().timTheoMa("NV001");
			}

			HoaDon hd = new HoaDon(txtMaHD.getText().trim(), LocalDateTime.now(), nv, tienKhach);
			List<ChiTietHoaDon> dsCT = new ArrayList<>();

			for (int i = 0; i < modelGioHang.getRowCount(); i++) {
				String maSP = modelGioHang.getValueAt(i, 0).toString();
				SanPham sp = sanPhamBUS.getSanPhamTheoMa(maSP);
				int sl = Integer.parseInt(modelGioHang.getValueAt(i, 3).toString());
				double donGia = Double
						.parseDouble(modelGioHang.getValueAt(i, 2).toString().replace(",", "").replace(".", ""));

				ChiTietHoaDon ct = new ChiTietHoaDon(hd, sp, sl, donGia);
				dsCT.add(ct);
			}
			hd.setDsChiTiet(dsCT);

			try {
				if (hoaDonBUS.thanhToan(hd)) {
					JOptionPane.showMessageDialog(this,
							"Thanh toán thành công hóa đơn " + hd.getMaHD() + "!\n" + "Tổng tiền: "
									+ df.format(tongTienHoaDon) + " VNĐ\n" + "Tiền thừa: " + txtTienThua.getText()
									+ " VNĐ");
					capNhatBangSanPham();
					taoHoaDonMoi();
				} else {
					JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
			}

		} else if (o.equals(btnHuyDon)) {
			taoHoaDonMoi();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tblSanPham.rowAtPoint(e.getPoint());
		int col = tblSanPham.columnAtPoint(e.getPoint());
		if (row >= 0 && col == 6) { // cột "Thêm"
			tblSanPham.setRowSelectionInterval(row, row);
			btnThemGio.doClick(); // dùng lại logic có sẵn của nút "+ Thêm vào giỏ"
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
}
