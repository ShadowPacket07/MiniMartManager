
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
import java.sql.SQLException;

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

public class LoginGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private Color primaryColor = new Color(255, 131, 40);
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
		setTitle("GonStore - LoginGUI");
		setSize(1200, 750);
		setMinimumSize(new Dimension(800, 500));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);

		// Khởi tạo hai JPanel
		pnlLeft = new JPanel();
		pnlRight = new JPanel();
		pnlLeft.setBackground(primaryColor);
		pnlRight.setBackground(backgroundColor);
		pnlLeft.setPreferredSize(new Dimension(380, 550));
		add(pnlLeft, BorderLayout.WEST);
		add(pnlRight, BorderLayout.CENTER);

		// Thiết lập bố cục cho khu vực bên trái
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBorder(new EmptyBorder(50, 30, 50, 30));

		// Hiển thị logo
		ImageIcon icon = new ImageIcon("imgs/logo.png");
		Image img = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
		lblLogo = new JLabel(new ImageIcon(img));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Tên cửa hàng
		lblStoreName = new JLabel("Nhóm 3");
		lblStoreName.setFont(new Font("Arial", Font.BOLD, 32));
		lblStoreName.setForeground(Color.WHITE);
		lblStoreName.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Slogan
		lblSlogan = new JLabel("QUẢN LÝ CỬA HÀNG TIỆN LỢI");
		lblSlogan.setFont(new Font("Arial", Font.BOLD, 16));
		lblSlogan.setForeground(Color.WHITE);
		lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm các thành phần vào pnlLeft
		pnlLeft.add(Box.createVerticalGlue());
		pnlLeft.add(lblLogo);
		pnlLeft.add(Box.createVerticalStrut(20));
		pnlLeft.add(lblStoreName);
		pnlLeft.add(Box.createVerticalStrut(15));
		pnlLeft.add(lblSlogan);
		pnlLeft.add(Box.createVerticalStrut(15));
		pnlLeft.add(Box.createVerticalGlue());

		// Thiết lập bố cục khu vực bên phải
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBorder(new EmptyBorder(80, 100, 80, 100));

		// Tiêu đề đăng nhập và mô tả
		lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitle.setForeground(new Color(40, 40, 40));
		lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblDescription = new JLabel("Chào mừng bạn quay trở lại GonStore!");
		lblDescription.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDescription.setForeground(Color.GRAY);
		lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Ô nhập liệu
		lblUsername = new JLabel("Tên đăng nhập");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
		lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 15));
		txtUsername.setPreferredSize(new Dimension(400, 40));
		txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

		lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
		lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		txtPassword.setPreferredSize(new Dimension(400, 40));
		txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		defaultEchoChar = txtPassword.getEchoChar();
		// Hiện mật khẩu, quên mật khẩu, đăng nhập, thoát
		chkShowPassword = new JCheckBox("Hiện mật khẩu");
		chkShowPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		chkShowPassword.setBackground(backgroundColor);

		btnForgotPassword = new JButton("Quên mật khẩu?");
		btnForgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		btnForgotPassword.setForeground(new Color(255, 131, 40));
		btnForgotPassword.setContentAreaFilled(false);
		btnForgotPassword.setFocusPainted(false);

		btnLogin = new JButton("ĐĂNG NHẬP");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 15));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(primaryColor);
		btnLogin.setFocusPainted(false);
		btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

		btnExit = new JButton("THOÁT");
		btnExit.setFont(new Font("Arial", Font.BOLD, 14));
		btnExit.setContentAreaFilled(false);
		btnExit.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnExit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

		// Thêm các thành phần vào pnlRight
		pnlRight.add(lblTitle);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(lblDescription);
		pnlRight.add(Box.createVerticalStrut(40));

		pnlRight.add(lblUsername);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(txtUsername);
		pnlRight.add(Box.createVerticalStrut(25));

		pnlRight.add(lblPassword);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(txtPassword);
		pnlRight.add(Box.createVerticalStrut(10));

		JPanel showAndForgot = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		showAndForgot.setBackground(backgroundColor);
		showAndForgot.add(chkShowPassword);
		showAndForgot.add(btnForgotPassword);
		// Giới hạn chiều cao, cho phép panel sử dụng chiều rộng của pnlRight
		showAndForgot.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		showAndForgot.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlRight.add(showAndForgot);
		pnlRight.add(Box.createVerticalStrut(10));

		pnlRight.add(btnLogin);
		pnlRight.add(Box.createVerticalStrut(12));
		pnlRight.add(btnExit);

		btnLogin.addActionListener(this);
		btnExit.addActionListener(this);
		btnForgotPassword.addActionListener(this);
		chkShowPassword.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object o = e.getSource();
		if (o == chkShowPassword) {
			if (chkShowPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar(defaultEchoChar);
			}
		} else if (o == btnForgotPassword) {
			JOptionPane.showMessageDialog(this,
					"Vui lòng liên hệ quản trị viên hệ thống để được hỗ trợ cấp lại mật khẩu!", "Quên mật khẩu",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (o == btnExit) {
			int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát chương trình?",
					"Xác nhận thoát", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (choice == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		} else if (o == btnLogin) {
			String un = txtUsername.getText().trim();
			String pw = new String(txtPassword.getPassword());

			if (un.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Thông Báo",
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
				TaiKhoan tk = taiKhoanBUS.timTheoUsername(un);
				if (tk == null) {
					JOptionPane.showMessageDialog(this, "Tên đăng nhập không tồn tại", "Đăng nhập thất bại",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!pw.equals(tk.getPassword())) {
					JOptionPane.showMessageDialog(this, "Mật khẩu không chính xác!", "Đăng nhập thất bại",
							JOptionPane.ERROR_MESSAGE);
					txtPassword.setText("");
					txtPassword.requestFocus();
					return;
				}
				JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				String role = tk.getRole();
				if (!"Quản lý".equalsIgnoreCase(role) && !"Nhân viên".equalsIgnoreCase(role)) {
					JOptionPane.showMessageDialog(this, "Vai trò tài khoản không hợp lệ", "Thông báo",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				MainGUI mainGUI = new MainGUI(tk);
				mainGUI.setVisible(true);
				this.dispose();

			} catch (SQLException ex) {
				// TODO: handle exception
				JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi hệ thống",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

}
