package main;

import java.awt.Font;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import connectDB.ConnectDB;
import gui.LoginGUI;

public class Main {
	public static void main(String[] args) {
		// Thiet lap font mac dinh Times New Roman 14 cho toan bo he thong Swing
		FontUIResource fontDefault = new FontUIResource(new Font("Times New Roman", Font.PLAIN, 14));
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource || (key != null && key.toString().endsWith(".font"))) {
				UIManager.put(key, fontDefault);
			}
		}

		// Kiem tra ket noi CSDL truoc khi khoi chay ung dung (Chuan mau IUH)
		try {
			ConnectDB.getInstance().connect();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Không thể kết nối đến cơ sở dữ liệu GonMart_DB!\n"
					+ "Vui lòng kiểm tra lại dịch vụ SQL Server (localhost:1433).\n"
					+ "Chi tiết lỗi: " + e.getMessage(),
					"Lỗi kết nối CSDL", JOptionPane.ERROR_MESSAGE);
		}

		LoginGUI loginGUI = new LoginGUI();
		loginGUI.setVisible(true);
	}
}
