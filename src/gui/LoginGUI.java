
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// ===== KHAI BÁO THUỘC TÍNH =====

	private JPanel pLeft, pRight;

	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private JCheckBox chkShowPassword;

	private JButton btnLogin;

	private JLabel lblMessage;

	private Color primaryColor = new Color(37, 99, 235);
	private Color sidebarColor = new Color(21, 94, 239);
	private Color textColor = new Color(23, 43, 77);
	private Color secondaryColor = new Color(102, 112, 133);

	// ===== CONSTRUCTOR =====

	public LoginGUI() {

		setTitle("MiniMart Manager - Đăng nhập");
		setSize(1200, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		setLayout(new GridLayout(1, 2));

		// ===== PANEL BÊN TRÁI =====

		pLeft = new JPanel();
		pLeft.setBackground(sidebarColor);
		pLeft.setLayout(new BoxLayout(pLeft, BoxLayout.Y_AXIS));

		JLabel lblLogo = new JLabel("MINIMART");
		lblLogo.setFont(new Font("Arial", Font.BOLD, 36));
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSlogan = new JLabel("MANAGEMENT SYSTEM");
		lblSlogan.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSlogan.setForeground(Color.WHITE);
		lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblDescription = new JLabel(
				"<html><div style='text-align:center;'>" + "Hệ thống quản lý cửa hàng tiện lợi" + "</div></html>");
		lblDescription.setFont(new Font("Arial", Font.PLAIN, 14));
		lblDescription.setForeground(Color.WHITE);
		lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);

		pLeft.add(Box.createVerticalGlue());
		pLeft.add(lblLogo);
		pLeft.add(Box.createVerticalStrut(15));
		pLeft.add(lblSlogan);
		pLeft.add(Box.createVerticalStrut(20));
		pLeft.add(lblDescription);
		pLeft.add(Box.createVerticalGlue());

		// ===== PANEL BÊN PHẢI =====

		pRight = new JPanel();
		pRight.setBackground(Color.WHITE);
		pRight.setLayout(new BoxLayout(pRight, BoxLayout.Y_AXIS));
		pRight.setBorder(new EmptyBorder(70, 65, 70, 65));

		JLabel lblTitle = new JLabel("Đăng nhập");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
		lblTitle.setForeground(textColor);
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblWelcome = new JLabel("Chào mừng bạn quay trở lại!");
		lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));
		lblWelcome.setForeground(secondaryColor);
		lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

		// ===== Ô TÊN ĐĂNG NHẬP =====

		JLabel lblUsername = new JLabel("Tên đăng nhập");
		lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
		lblUsername.setForeground(textColor);
		lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 15));
		txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

		// ===== Ô MẬT KHẨU =====

		JLabel lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
		lblPassword.setForeground(textColor);
		lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 15));
		txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

		// ===== CHECKBOX HIỆN MẬT KHẨU =====

		chkShowPassword = new JCheckBox("Hiển thị mật khẩu");
		chkShowPassword.setBackground(Color.WHITE);
		chkShowPassword.setForeground(secondaryColor);
		chkShowPassword.setFont(new Font("Arial", Font.PLAIN, 12));
		chkShowPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

		chkShowPassword.addActionListener(this);

		// ===== THÔNG BÁO =====

		lblMessage = new JLabel(" ");
		lblMessage.setFont(new Font("Arial", Font.PLAIN, 12));
		lblMessage.setForeground(Color.RED);
		lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

		// ===== NÚT ĐĂNG NHẬP =====

		btnLogin = new JButton("ĐĂNG NHẬP");
		btnLogin.setFont(new Font("Arial", Font.BOLD, 15));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(primaryColor);
		btnLogin.setFocusPainted(false);
		btnLogin.setBorderPainted(false);
		btnLogin.setOpaque(true);
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

		btnLogin.addActionListener(this);

		// ===== THÊM CÁC THÀNH PHẦN VÀO PANEL =====

		pRight.add(lblTitle);
		pRight.add(Box.createVerticalStrut(10));
		pRight.add(lblWelcome);

		pRight.add(Box.createVerticalStrut(45));
		pRight.add(lblUsername);
		pRight.add(Box.createVerticalStrut(8));
		pRight.add(txtUsername);

		pRight.add(Box.createVerticalStrut(22));
		pRight.add(lblPassword);
		pRight.add(Box.createVerticalStrut(8));
		pRight.add(txtPassword);

		pRight.add(Box.createVerticalStrut(10));
		pRight.add(chkShowPassword);

		pRight.add(Box.createVerticalStrut(15));
		pRight.add(lblMessage);

		pRight.add(Box.createVerticalStrut(20));
		pRight.add(btnLogin);

		// ===== THÊM HAI PANEL VÀO FRAME =====

		add(pLeft);
		add(pRight);
	}

	// ===== XỬ LÝ SỰ KIỆN =====

	@Override
	public void actionPerformed(ActionEvent e) {

		// Hiện hoặc ẩn mật khẩu
		if (e.getSource() == chkShowPassword) {

			if (chkShowPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar('•');
			}
		}

		// Xử lý nút đăng nhập
		else if (e.getSource() == btnLogin) {

			String username = txtUsername.getText();

			String password = new String(txtPassword.getPassword());

			if (username.trim().isEmpty() || password.isEmpty()) {

				lblMessage.setText("Vui lòng nhập đầy đủ tài khoản và mật khẩu!");

			} else {

				lblMessage.setText("Thông tin đã được nhập. Chưa xác thực tài khoản!");
			}
		}
	}
}
