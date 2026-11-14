package main;

import entity.TaiKhoan;
import gui.MainGUI;

public class Main {
	public static void main(String[] args) {
		TaiKhoan tk = new TaiKhoan("admin", "123", "Quản lý", "NV001");
		MainGUI mainGUI = new MainGUI(tk);
		mainGUI.setVisible(true);
	}
}
