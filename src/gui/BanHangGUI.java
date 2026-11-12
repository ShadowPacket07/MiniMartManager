package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BanHangGUI extends JPanel {

	private JPanel pDanhSachSP, pGioHang;
	private JLabel lblTongTien, lblSoSanPham;
	private JTextField txtTimKiem, txtMaKhach;
	private JRadioButton rdoVangLai, rdoKhachHang;

	private double tongTien = 0;
	private int tongSoLuong = 0;

	// mã sản phẩm -> nút "+" của dòng đó trong giỏ
	private Map<String, JButton> dsNutCong = new HashMap<>();

	private Color nenColor = new Color(243, 246, 252);
	private Color theColor = Color.WHITE;
	private Color primaryColor = new Color(37, 99, 235);
	private Color textColor = new Color(23, 43, 77);
	private Color secondaryColor = new Color(102, 112, 133);
	private Color vienColor = new Color(208, 213, 221);
	private Color xanhLaColor = new Color(22, 163, 74);
	private Color doColor = new Color(220, 38, 38);

	private Object[][] dsSanPham = {
			{ "SP001", "Nước khoáng Lavie 500ml", 6000.0, 45 },
			{ "SP002", "Mì ly Hảo Hảo Tôm Chua Cay", 9500.0, 60 },
			{ "SP003", "Sữa tươi Vinamilk 180ml", 8500.0, 32 },
			{ "SP004", "Snack Khoai Tây Lay's", 14000.0, 25 },
			{ "SP005", "Trà Ô Long Tea+ Plus", 11000.0, 40 },
			{ "SP006", "Bánh Mì Tươi Kinh Đô", 12000.0, 18 },
			{ "SP007", "Cà phê lon Highlands", 16000.0, 30 },
			{ "SP008", "Xúc xích tiệt trùng Vissan", 10000.0, 50 },
			{ "SP009", "Nước tăng lực RedBull", 15000.0, 80 } };

	public BanHangGUI() {
		setLayout(new BorderLayout(15, 0));
		setBackground(nenColor);
		setBorder(new EmptyBorder(15, 15, 15, 15));

		taoKhuVucSanPham();
		taoKhuVucGioHang();
		hienThiSanPham("");
	}

	private void taoKhuVucSanPham() {
		JPanel pTrai = new JPanel(new BorderLayout(0, 12));
		pTrai.setBackground(nenColor);

		JPanel pTim = new JPanel(new BorderLayout(10, 0));
		pTim.setBackground(theColor);
		pTim.setBorder(taoVien(12, 15, 12, 15));

		txtTimKiem = new JTextField();
		txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
		txtTimKiem.setBorder(taoVien(6, 10, 6, 10));

		JButton btnTimKiem = taoNut("Tìm kiếm", primaryColor, Color.WHITE, 100, 34);
		JButton btnLamMoi = taoNut("Làm mới", new Color(241, 245, 249), textColor, 85, 34);
		btnLamMoi.setBorder(new LineBorder(vienColor, 1, true));

		ActionListener timKiem = e -> hienThiSanPham(txtTimKiem.getText());
		btnTimKiem.addActionListener(timKiem);
		txtTimKiem.addActionListener(timKiem);
		btnLamMoi.addActionListener(e -> {
			txtTimKiem.setText("");
			hienThiSanPham("");
		});

		JPanel pNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		pNut.setBackground(theColor);
		pNut.add(btnTimKiem);
		pNut.add(btnLamMoi);

		pTim.add(taoLabel("Mã sản phẩm:", 14, true, textColor), BorderLayout.WEST);
		pTim.add(txtTimKiem, BorderLayout.CENTER);
		pTim.add(pNut, BorderLayout.EAST);

		pDanhSachSP = new JPanel(new GridLayout(0, 3, 12, 12));
		pDanhSachSP.setBackground(nenColor);
		pDanhSachSP.setBorder(new EmptyBorder(0, 0, 0, 5));

		// bọc thêm 1 panel để các thẻ không bị kéo dãn khi có ít sản phẩm
		JPanel pBoc = new JPanel(new BorderLayout());
		pBoc.setBackground(nenColor);
		pBoc.add(pDanhSachSP, BorderLayout.NORTH);

		JScrollPane scroll = new JScrollPane(pBoc);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);
		scroll.getViewport().setBackground(nenColor);

		pTrai.add(pTim, BorderLayout.NORTH);
		pTrai.add(scroll, BorderLayout.CENTER);
		add(pTrai, BorderLayout.CENTER);
	}

	private void hienThiSanPham(String tuKhoa) {
		pDanhSachSP.removeAll();
		String ma = tuKhoa.trim().toUpperCase();

		if (ma.matches("SP\\d{3}")) {
			boolean timThay = false;
			for (Object[] sp : dsSanPham) {
				if (sp[0].equals(ma)) {
					taoTheSanPham((String) sp[0], (String) sp[1], (double) sp[2], (int) sp[3]);
					timThay = true;
					break;
				}
			}
			if (!timThay) {
				JOptionPane.showMessageDialog(this, "Không tìm thấy mã: " + ma);
				hienTatCaSanPham();
			}
		} else {
			hienTatCaSanPham();
		}

		pDanhSachSP.revalidate();
		pDanhSachSP.repaint();
	}

	private void hienTatCaSanPham() {
		for (Object[] sp : dsSanPham) {
			taoTheSanPham((String) sp[0], (String) sp[1], (double) sp[2], (int) sp[3]);
		}
	}

	private void taoTheSanPham(String ma, String ten, double gia, int ton) {
		JPanel the = new JPanel(new BorderLayout(0, 8));
		the.setBackground(theColor);
		the.setBorder(taoVien(10, 10, 10, 10));

		JPanel pHinh = new JPanel(new BorderLayout());
		pHinh.setBackground(new Color(241, 245, 249));
		pHinh.setPreferredSize(new Dimension(100, 110));
		pHinh.setBorder(new EmptyBorder(6, 8, 6, 8));
		JLabel lblHinh = taoLabel("HÌNH ẢNH", 12, true, new Color(148, 163, 184));
		lblHinh.setHorizontalAlignment(SwingConstants.CENTER);
		pHinh.add(taoLabel(ma, 11, true, primaryColor), BorderLayout.NORTH);
		pHinh.add(lblHinh, BorderLayout.CENTER);

		JPanel pGiaTon = new JPanel(new BorderLayout());
		pGiaTon.setBackground(theColor);
		pGiaTon.add(taoLabel(String.format("%,.0f đ", gia), 14, true, primaryColor), BorderLayout.WEST);
		pGiaTon.add(taoLabel("Tồn: " + ton, 12, false, secondaryColor), BorderLayout.EAST);

		JButton btnThem = taoNut("+ Thêm vào giỏ", primaryColor, Color.WHITE, 0, 32);
		btnThem.addActionListener(e -> themVaoGio(ma, ten, gia));

		JPanel pInfo = new JPanel(new BorderLayout(0, 6));
		pInfo.setBackground(theColor);
		pInfo.add(taoLabel(ten, 13, true, textColor), BorderLayout.NORTH);
		pInfo.add(pGiaTon, BorderLayout.CENTER);
		pInfo.add(btnThem, BorderLayout.SOUTH);

		the.add(pHinh, BorderLayout.NORTH);
		the.add(pInfo, BorderLayout.SOUTH);
		pDanhSachSP.add(the);
	}

	private void taoKhuVucGioHang() {
		JPanel pPhai = new JPanel(new BorderLayout());
		pPhai.setBackground(theColor);
		pPhai.setPreferredSize(new Dimension(340, 0));
		pPhai.setBorder(new LineBorder(vienColor, 1, true));

		JPanel pHead = new JPanel(new BorderLayout());
		pHead.setBackground(theColor);
		pHead.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, vienColor), new EmptyBorder(14, 15, 14, 15)));
		lblSoSanPham = taoLabel("0 sản phẩm", 13, false, secondaryColor);
		pHead.add(taoLabel("Giỏ hàng", 16, true, textColor), BorderLayout.WEST);
		pHead.add(lblSoSanPham, BorderLayout.EAST);

		pGioHang = new JPanel();
		pGioHang.setLayout(new BoxLayout(pGioHang, BoxLayout.Y_AXIS));
		pGioHang.setBackground(theColor);
		pGioHang.setBorder(new EmptyBorder(10, 10, 10, 10));

		JScrollPane scroll = new JScrollPane(pGioHang);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(null);

		// chọn khách hàng
		rdoVangLai = new JRadioButton("Khách vãng lai", true);
		rdoKhachHang = new JRadioButton("Khách hàng");
		for (JRadioButton rdo : new JRadioButton[] { rdoVangLai, rdoKhachHang }) {
			rdo.setBackground(theColor);
			rdo.setForeground(textColor);
			rdo.setFont(new Font("Arial", Font.PLAIN, 13));
		}
		ButtonGroup nhomKhach = new ButtonGroup();
		nhomKhach.add(rdoVangLai);
		nhomKhach.add(rdoKhachHang);

		txtMaKhach = new JTextField();
		txtMaKhach.setFont(new Font("Arial", Font.PLAIN, 13));
		txtMaKhach.setBorder(taoVien(4, 8, 4, 8));
		txtMaKhach.setEnabled(false);

		rdoVangLai.addActionListener(e -> {
			txtMaKhach.setText("");
			txtMaKhach.setEnabled(false);
		});
		rdoKhachHang.addActionListener(e -> {
			txtMaKhach.setEnabled(true);
			txtMaKhach.requestFocus();
		});

		JPanel pRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pRadio.setBackground(theColor);
		pRadio.add(rdoVangLai);
		pRadio.add(rdoKhachHang);

		JPanel pKhach = new JPanel(new GridLayout(3, 1, 0, 4));
		pKhach.setBackground(theColor);
		pKhach.add(taoLabel("Khách hàng", 13, true, textColor));
		pKhach.add(pRadio);
		pKhach.add(txtMaKhach);

		JPanel pTong = new JPanel(new BorderLayout());
		pTong.setBackground(new Color(240, 253, 244));
		pTong.setBorder(new EmptyBorder(10, 12, 10, 12));
		lblTongTien = taoLabel("0 đ", 20, true, xanhLaColor);
		pTong.add(taoLabel("TỔNG CỘNG", 13, true, textColor), BorderLayout.WEST);
		pTong.add(lblTongTien, BorderLayout.EAST);

		JButton btnThanhToan = taoNut("XÁC NHẬN THANH TOÁN", xanhLaColor, Color.WHITE, 0, 42);
		JButton btnLamMoiGio = taoNut("Làm mới giỏ hàng", new Color(254, 242, 242), doColor, 0, 32);
		btnLamMoiGio.setBorder(new LineBorder(new Color(254, 202, 202), 1, true));

		btnThanhToan.addActionListener(e -> {
			if (dsNutCong.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
			} else if (rdoKhachHang.isSelected() && txtMaKhach.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hoặc SĐT khách hàng!");
			} else {
				String khach = "Khách vãng lai";
				if (rdoKhachHang.isSelected()) {
					khach = "Khách hàng: " + txtMaKhach.getText().trim();
				}
				JOptionPane.showMessageDialog(this,
						"Thanh toán thành công!\n" + khach + "\nTổng thu: " + lblTongTien.getText());
				xoaGioHang();
			}
		});
		btnLamMoiGio.addActionListener(e -> xoaGioHang());

		JPanel pNut = new JPanel(new BorderLayout(0, 8));
		pNut.setBackground(theColor);
		pNut.add(btnThanhToan, BorderLayout.NORTH);
		pNut.add(btnLamMoiGio, BorderLayout.SOUTH);

		JPanel pFoot = new JPanel(new BorderLayout(0, 12));
		pFoot.setBackground(theColor);
		pFoot.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, vienColor), new EmptyBorder(12, 15, 12, 15)));
		pFoot.add(pKhach, BorderLayout.NORTH);
		pFoot.add(pTong, BorderLayout.CENTER);
		pFoot.add(pNut, BorderLayout.SOUTH);

		pPhai.add(pHead, BorderLayout.NORTH);
		pPhai.add(scroll, BorderLayout.CENTER);
		pPhai.add(pFoot, BorderLayout.SOUTH);
		add(pPhai, BorderLayout.EAST);
	}

	private void themVaoGio(String ma, String ten, double gia) {
		// đã có trong giỏ thì bấm nút + của dòng đó
		if (dsNutCong.containsKey(ma)) {
			dsNutCong.get(ma).doClick();
			return;
		}

		JPanel dong = new JPanel(new BorderLayout(0, 8));
		dong.setBackground(nenColor);
		dong.setBorder(taoVien(8, 10, 8, 10));
		dong.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

		JButton btnXoa = taoNut("×", nenColor, doColor, 26, 22);
		btnXoa.setFont(new Font("Arial", Font.BOLD, 16));

		JPanel pTren = new JPanel(new BorderLayout());
		pTren.setBackground(nenColor);
		pTren.add(taoLabel(ten, 13, true, textColor), BorderLayout.CENTER);
		pTren.add(btnXoa, BorderLayout.EAST);

		JButton btnTru = taoNut("-", theColor, textColor, 30, 26);
		JButton btnCong = taoNut("+", theColor, textColor, 30, 26);
		JLabel lblSL = taoLabel("1", 12, true, textColor);
		lblSL.setHorizontalAlignment(SwingConstants.CENTER);
		lblSL.setPreferredSize(new Dimension(34, 26));
		lblSL.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, vienColor));

		JPanel pSoLuong = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		pSoLuong.setBackground(theColor);
		pSoLuong.setBorder(new LineBorder(vienColor, 1));
		pSoLuong.add(btnTru);
		pSoLuong.add(lblSL);
		pSoLuong.add(btnCong);

		JLabel lblDonGia = taoLabel(String.format("%,.0f đ x", gia), 12, false, secondaryColor);
		JPanel pTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		pTrai.setBackground(nenColor);
		pTrai.add(lblDonGia);
		pTrai.add(pSoLuong);

		JLabel lblThanhTien = taoLabel(String.format("%,.0f đ", gia), 13, true, doColor);

		JPanel pDuoi = new JPanel(new BorderLayout());
		pDuoi.setBackground(nenColor);
		pDuoi.add(pTrai, BorderLayout.WEST);
		pDuoi.add(lblThanhTien, BorderLayout.EAST);

		dong.add(pTren, BorderLayout.NORTH);
		dong.add(pDuoi, BorderLayout.SOUTH);

		// dùng mảng 1 phần tử để lambda sửa được số lượng
		int[] soLuong = { 1 };
		Component khoangCach = Box.createVerticalStrut(8);

		btnCong.addActionListener(e -> {
			soLuong[0]++;
			lblSL.setText(String.valueOf(soLuong[0]));
			lblThanhTien.setText(String.format("%,.0f đ", soLuong[0] * gia));
			capNhatTong(gia, 1);
		});

		btnTru.addActionListener(e -> {
			if (soLuong[0] > 1) {
				soLuong[0]--;
				lblSL.setText(String.valueOf(soLuong[0]));
				lblThanhTien.setText(String.format("%,.0f đ", soLuong[0] * gia));
				capNhatTong(-gia, -1);
			}
		});

		btnXoa.addActionListener(e -> {
			dsNutCong.remove(ma);
			pGioHang.remove(dong);
			pGioHang.remove(khoangCach);
			pGioHang.revalidate();
			pGioHang.repaint();
			capNhatTong(-soLuong[0] * gia, -soLuong[0]);
		});

		dsNutCong.put(ma, btnCong);
		pGioHang.add(dong);
		pGioHang.add(khoangCach);
		pGioHang.revalidate();
		capNhatTong(gia, 1);
	}

	private void xoaGioHang() {
		dsNutCong.clear();
		pGioHang.removeAll();
		pGioHang.revalidate();
		pGioHang.repaint();
		tongTien = 0;
		tongSoLuong = 0;
		capNhatTong(0, 0);

		rdoVangLai.setSelected(true);
		txtMaKhach.setText("");
		txtMaKhach.setEnabled(false);
	}

	private void capNhatTong(double tien, int soLuong) {
		tongTien += tien;
		tongSoLuong += soLuong;
		lblTongTien.setText(String.format("%,.0f đ", tongTien));
		lblSoSanPham.setText(tongSoLuong + " sản phẩm");
	}

	private JLabel taoLabel(String text, int size, boolean dam, Color color) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(new Font("Arial", dam ? Font.BOLD : Font.PLAIN, size));
		lbl.setForeground(color);
		return lbl;
	}

	private Border taoVien(int top, int left, int bottom, int right) {
		return BorderFactory.createCompoundBorder(new LineBorder(vienColor, 1, true),
				new EmptyBorder(top, left, bottom, right));
	}

	private JButton taoNut(String text, Color nen, Color chu, int rong, int cao) {
		JButton btn = new JButton(text);
		btn.setBackground(nen);
		btn.setForeground(chu);
		btn.setFont(new Font("Arial", Font.BOLD, 13));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (rong > 0) {
			btn.setPreferredSize(new Dimension(rong, cao));
		} else {
			btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, cao));
		}
		return btn;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Bán hàng - MiniMart");
		frame.setSize(1020, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(new BanHangGUI());
		frame.setVisible(true);
	}
}