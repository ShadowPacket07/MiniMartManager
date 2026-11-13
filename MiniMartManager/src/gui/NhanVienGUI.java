
package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class NhanVienGUI extends JPanel {

	public NhanVienGUI() {

		// 1. Thiết lập Layout
		setLayout(new BorderLayout(10, 10));
		setBackground(new Color(243, 246, 252));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// 2. Tạo tiêu đề
		JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(23, 43, 77));

		// 3. Tạo các nút chức năng
		JButton btnThem = new JButton("Thêm nhân viên");
		JButton btnSua = new JButton("Sửa");
		JButton btnXoa = new JButton("Xóa");

		JPanel pButtons = new JPanel();
		pButtons.setBackground(new Color(243, 246, 252));

		pButtons.add(btnThem);
		pButtons.add(btnSua);
		pButtons.add(btnXoa);

		// 4. Tạo bảng nhân viên
		String[] columns = { "Mã NV", "Họ tên", "Giới tính", "Số điện thoại", "Email" };

		Object[][] data = { { "NV001", "Nguyễn Văn An", "Nam", "0901234567", "an@gmail.com" },
				{ "NV002", "Trần Thị Bình", "Nữ", "0912345678", "binh@gmail.com" } };

		DefaultTableModel model = new DefaultTableModel(data, columns);

		JTable tblNhanVien = new JTable(model);

		JScrollPane scrollPane = new JScrollPane(tblNhanVien);

		// 5. Đưa các thành phần vào giao diện
		add(lblTitle, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(pButtons, BorderLayout.SOUTH);
	}
}
