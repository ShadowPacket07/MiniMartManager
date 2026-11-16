package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import bus.TaiKhoanBUS;
import entity.TaiKhoan;
import util.Session;

public class LoginGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Color primaryColor = new Color(21, 94, 239); // Xanh duong #155EEF chuan dong bo
	private Color backgroundColor = new Color(248, 249, 252);

	private JPanel pnlLeft;
	private JPanel pnlRight;

	private JLabel lblLogo;
	private JLabel lblStoreName;
	private JLabel lblSlogan;

	// Tiêu đề
	private JLabel lblTitle;
	private JLabel lblDescription;

	// Ô nhập liệu
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	// Các chức năng
	private char defaultEchoChar;
	private JCheckBox chkShowPassword;
	private JButton btnForgotPassword;
	private JButton btnLogin;
	private JButton btnExit;

	private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();

	public LoginGUI() {
		setTitle("GọnMart - Đăng nhập hệ thống");
		setSize(1000, 650);
		setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Khởi tạo hai JPanel
		pnlLeft = new JPanel();
		pnlRight = new JPanel();
		pnlLeft.setBackground(primaryColor);
		pnlRight.setBackground(backgroundColor);
		pnlLeft.setPreferredSize(new Dimension(360, 650));
		add(pnlLeft, BorderLayout.WEST);
		add(pnlRight, BorderLayout.CENTER);

		// Khu vực bên trái (Thương hiệu)
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBorder(new EmptyBorder(50, 30, 50, 30));

		// Hiển thị logo tương đối (fallback an toàn đa tầng)
		lblLogo = new JLabel();
		URL logoUrl = getClass().getResource("/imgs/logo.png");
		if (logoUrl == null) {
			logoUrl = getClass().getResource("/logo.png");
		}
		ImageIcon icon = null;
		if (logoUrl != null) {
			icon = new ImageIcon(logoUrl);
		} else if (new java.io.File("imgs/logo.png").exists()) {
			icon = new ImageIcon("imgs/logo.png");
		}
		if (icon != null) {
			Image img = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
			lblLogo.setIcon(new ImageIcon(img));
		}
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblStoreName = new JLabel("GọnMart");
		lblStoreName.setFont(new Font("Times New Roman", Font.BOLD, 32));
		lblStoreName.setForeground(Color.WHITE);
		lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblSlogan = new JLabel("SIÊU THỊ TIỆN LỢI THÔNG MINH");
		lblSlogan.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblSlogan.setForeground(Color.WHITE);
		lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSub = new JLabel("Phục vụ nhanh chóng - Giá cả hợp lý");
		lblSub.setFont(new Font("Times New Roman", Font.ITALIC, 13));
		lblSub.setForeground(new Color(224, 242, 254));
		lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlLeft.add(Box.createVerticalGlue());
		pnlLeft.add(lblLogo);
		pnlLeft.add(Box.createVerticalStrut(20));
		pnlLeft.add(lblStoreName);
		pnlLeft.add(Box.createVerticalStrut(10));
		pnlLeft.add(lblSlogan);
		pnlLeft.add(Box.createVerticalStrut(8));
		pnlLeft.add(lblSub);
		pnlLeft.add(Box.createVerticalGlue());

		// Khu vực bên phải (Form đăng nhập)
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBorder(new EmptyBorder(60, 70, 60, 70));

		lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblTitle.setForeground(new Color(40, 40, 40));
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblDescription = new JLabel("Chào mừng trở lại! Vui lòng đăng nhập để tiếp tục.");
		lblDescription.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		lblDescription.setForeground(Color.GRAY);
		lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblUsername = new JLabel("Tên đăng nhập (Mã nhân viên)");
		lblUsername.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtUsername.setPreferredSize(new Dimension(380, 38));
		txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
		txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		txtPassword.setPreferredSize(new Dimension(380, 38));
		txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
		txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		defaultEchoChar = txtPassword.getEchoChar();

		chkShowPassword = new JCheckBox("Hiện mật khẩu");
		chkShowPassword.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		chkShowPassword.setBackground(backgroundColor);

		btnForgotPassword = new JButton("Quên mật khẩu?");
		btnForgotPassword.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnForgotPassword.setForeground(primaryColor);
		btnForgotPassword.setContentAreaFilled(false);
		btnForgotPassword.setBorderPainted(false);
		btnForgotPassword.setFocusPainted(false);

		btnLogin = new JButton("ĐĂNG NHẬP");
		btnLogin.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(primaryColor);
		btnLogin.setFocusPainted(false);
		btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

		btnExit = new JButton("THOÁT");
		btnExit.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnExit.setContentAreaFilled(false);
		btnExit.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnExit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

		pnlRight.add(lblTitle);
		pnlRight.add(Box.createVerticalStrut(6));
		pnlRight.add(lblDescription);
		pnlRight.add(Box.createVerticalStrut(30));

		pnlRight.add(lblUsername);
		pnlRight.add(Box.createVerticalStrut(8));
		pnlRight.add(txtUsername);
		pnlRight.add(Box.createVerticalStrut(20));

		pnlRight.add(lblPassword);
		pnlRight.add(Box.createVerticalStrut(8));
		pnlRight.add(txtPassword);
		pnlRight.add(Box.createVerticalStrut(8));

		JPanel showAndForgot = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		showAndForgot.setBackground(backgroundColor);
		showAndForgot.add(chkShowPassword);
		showAndForgot.add(btnForgotPassword);
		showAndForgot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
		showAndForgot.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlRight.add(showAndForgot);
		pnlRight.add(Box.createVerticalStrut(15));

		pnlRight.add(btnLogin);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(btnExit);

		btnLogin.addActionListener(this);
		btnExit.addActionListener(this);
		btnForgotPassword.addActionListener(this);
		chkShowPassword.addActionListener(this);
		
		// Enter trên ô mật khẩu để đăng nhập luôn
		txtPassword.addActionListener(this::actionPerformed);
		txtUsername.addActionListener(e -> txtPassword.requestFocus());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == chkShowPassword) {
			if (chkShowPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar(defaultEchoChar);
			}
		} else if (o == btnForgotPassword) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng liên hệ Người quản lý để được cấp lại mật khẩu!", "Quên mật khẩu",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (o == btnExit) {
			int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát ứng dụng?",
					"Xác nhận thoát", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		} else if (o == btnLogin || o == txtPassword) {
			String un = txtUsername.getText().trim();
			String pw = new String(txtPassword.getPassword());

			if (un.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				txtUsername.requestFocus();
				return;
			}
			if (pw.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Thông báo",
						JOptionPane.WARNING_MESSAGE);
				txtPassword.requestFocus();
				return;
			}

			try {
				TaiKhoan tk = taiKhoanBUS.dangNhap(un, pw);
				if (tk != null) {
					Session.setTaiKhoan(tk);
					JOptionPane.showMessageDialog(this,
							"Đăng nhập thành công!\nXin chào: " + Session.getTenNguoiDung() + " (" + (Session.isQuanLy() ? "Quản lý" : "Nhân viên bán hàng") + ")",
							"Thành công", JOptionPane.INFORMATION_MESSAGE);
					
					MainGUI mainGUI = new MainGUI(tk);
					mainGUI.setVisible(true);
					this.dispose();
				}
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Đăng nhập thất bại",
						JOptionPane.ERROR_MESSAGE);
				txtPassword.setText("");
				txtPassword.requestFocus();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(),
						"Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}

