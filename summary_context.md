Giai đoạn 1: Xác lập phạm vi đề tài & Chuẩn hóa cấu trúc dữ liệu nền tảng
Bối cảnh & Phạm vi: Dự án môn Lập trình Hướng sự kiện (IUH) với đề tài Siêu thị tiện lợi GọnMart (loại bỏ từ "MiniMart"). Định hướng phục vụ nội bộ (Quản lý và Thu ngân), xác định mô hình bán lẻ vãng lai nên bỏ hoàn toàn bảng/đối tượng KhachHang, tinh gọn bảng bỏ email, tienLuong.
Cơ sở dữ liệu: Chốt chuẩn 5 bảng trong GonMart_DB (NhanVien, TaiKhoan, SanPham, HoaDon, ChiTietHoaDon), ràng buộc toàn vẹn bằng CHECK và nạp sẵn dữ liệu mẫu.
Kết nối & Entity: Cài đặt ConnectDB theo mẫu Singleton Thread-Safe (chỉ dùng sqljdbc42.jar). Xây dựng 5 lớp Entity đóng gói chặt chẽ: nạp chồng constructor, bắt lỗi định dạng (regex, tuổi 18-65, số dương) ngay tại từng setter và ném IllegalArgumentException.
Giai đoạn 2: Hiện thực hóa Nghiệp vụ (BUS), DAO & Khắc phục 8 "tử huyệt" kỹ thuật ban đầu
Tầng DAO & Transaction: Xóa sạch việc lưu RAM ảo (static ArrayList), chuyển sang JDBC thuần (PreparedStatement). Hiện thực Database Transaction trong HoaDonDAO.luuHoaDonVaChiTiet() (setAutoCommit(false) 
→
→ thêm hóa đơn 
→
→ thêm chi tiết 
→
→ trừ tồn kho 
→
→ commit/rollback).
Tầng BUS & Session: Tạo Session.java quản trị tài khoản, phiên trực ca và hàm phân quyền isQuanLy().
Khắc phục 8 tử huyệt đồ án:
Chuyển đường dẫn nạp ảnh sang dạng tương đối (/imgs/logo.png).
Bỏ RAM ảo, kết nối 100% CSDL thật.
Chuẩn hóa ConnectDB Singleton.
Đồng bộ thuộc tính Entity khớp với CSDL.
Bắt lỗi form nhập liệu theo chuẩn 4 bước IUH (JOptionPane 
→
→ selectAll() 
→
→ requestFocus() 
→
→ return false).
Khóa sửa trực tiếp trên ô giỏ hàng, chuyển sang nút "Sửa số lượng".
Bọc Transaction khi thanh toán POS.
Khóa ô nhập mã khóa chính (txtMa.setEditable(false)) khi chọn dòng sửa.
Giai đoạn 3: Phản biện mã nguồn mẫu bên ngoài & Quyết định bảo toàn kiến trúc
Bối cảnh: Xuất hiện đề xuất đối chiếu và chuyển mã nguồn theo một bộ code mẫu trôi nổi (đổi sang mô hình 2 lớp, đổi tên trường CSDL, thêm lại KhachHang, email, tienLuong).
Quyết định kỹ thuật: Bảo toàn 100% mã nguồn hiện tại, không sửa theo code mẫu.
Lý do bảo toàn: Giữ vững kiến trúc 3 lớp chuẩn mực (GUI -> BUS -> DAO -> ConnectDB + Entity + Util); tránh xung đột tên cột CSDL (maSP vs maSanPham); tiếp tục duy trì kỹ thuật try-with-resources hiện đại; và bảo đảm mã nguồn đồng nhất tuyệt đối với Báo cáo Word (HSK.docx).
Giai đoạn 4: Chuẩn hóa Khởi động an toàn & Nhận diện thương hiệu công nghiệp
Khởi động ứng dụng (Main.java): Thiết lập Main.java là Entry Point duy nhất. Thêm khối kiểm tra ConnectDB ngay khi khởi chạy; nếu SQL Server chưa bật hoặc sai cổng, hiển thị JOptionPane cảnh báo trực quan thay vì để ứng dụng sập im lặng.
Đồng bộ Font chữ & Giao diện: Thiết lập UIManager.put toàn hệ thống sang font Times New Roman học thuật.
Bộ nhận diện thương hiệu: Chuyển đổi toàn bộ 8 màn hình Swing sang tone màu chủ đạo Xanh dương Doanh nghiệp #155EEF. Phân cấp màu nút bấm rõ ràng (Thêm/Tìm: Xanh dương, Giỏ hàng: Xanh lá, Sửa: Teal, Xóa/Ngừng bán: Đỏ). Sử dụng CardLayout trên MainGUI để chuyển đổi phân hệ mượt mà.
Giai đoạn 5: Đối soát 1-1 với Báo cáo Word & Vá 6 lỗi tiềm ẩn chuyên sâu
Đối soát tài liệu: Kiểm tra khớp từng trường thuộc tính giữa bảng 2.6 – 2.10 trong file Word HSK.docx với mã nguồn và SQL Server.
Xử lý 6 lỗi tiềm ẩn vừa phát hiện:
Hóa đơn hiển thị 0đ: Bổ sung biến và hàm đọc tongTien trong HoaDon và HoaDonDAO.mapRow.
Thêm nhân viên thiếu tài khoản: Tự động kích hoạt tạo TaiKhoan (mật khẩu mặc định 12345678) khi thêm mới nhân viên qua NhanVienBUS.
Mất ảnh sản phẩm: Lưu lại tên ảnh cũ (spCu.getHinhAnh()) khi cập nhật thông tin sản phẩm.
Crash số tiền trên Windows tiếng Việt: Cố định DecimalFormatSymbols theo Locale.US để xử lý triệt để dấu chấm/phẩy hàng nghìn.
Lỗi đọc lịch sử đơn hàng: Đảo thứ tự setSoLuong trước setSanPham trong ChiTietHoaDon để không bị chặn bởi tồn kho hiện tại.
Lệch chuỗi vai trò: Chuẩn hóa chuỗi "Bán hàng" thành "Nhân viên bán hàng" tại setter để khớp CHECK constraint của CSDL.
Giai đoạn 6: Kiểm thử phân quyền & Đánh giá chất lượng mã nguồn
Cơ chế phân quyền (Session):
Quản lý (admin / QL001): Hiển thị toàn bộ 6 chức năng (Trang chủ, Nhân viên, Sản phẩm, Bán hàng, Quản lý hóa đơn, Thống kê doanh thu).
Thu ngân (nv002 / NV002): Chỉ hiển thị 3 chức năng thu gọn (Trang chủ, POS Bán hàng, Tra cứu hóa đơn); hóa đơn lập tự động gắn đúng mã nhân viên trực ca.
Chất lượng mã nguồn: Biên dịch bằng javac thành công tuyệt đối (Exit Code 0), sinh đầy đủ các file .class trong thư mục bin/. Toàn bộ 5/5 test case tự động kiểm thử nghiệp vụ độc lập đều đạt chuẩn PASSED 100%.
Giai đoạn 7: Hiện trạng & Kế hoạch hoàn tất đóng gói bảo vệ
Trạng thái: Tầng Logic, CSDL và Nghiệp vụ đạt mức xuất sắc (10/10).
Nhiệm vụ bàn giao cuối cùng:
Chạy nghiệm thu trực tiếp giao diện từ Main.java trên cả hai tài khoản mẫu (admin và nv002).
Chụp ảnh màn hình giao diện ứng dụng thực tế (thay thế các hình mockup cũ) đưa vào Chương 3 của file Báo cáo Word HSK.docx.
Đóng gói mã nguồn sạch, file kịch bản CSDL GonMart_DB, thư viện sqljdbc42.jar và file báo cáo hoàn chỉnh để nộp đồ án.Giai đoạn 1: Xác lập phạm vi đề tài & Chuẩn hóa cấu trúc dữ liệu nền tảng
Bối cảnh & Phạm vi: Dự án môn Lập trình Hướng sự kiện (IUH) với đề tài Siêu thị tiện lợi GọnMart (loại bỏ từ "MiniMart"). Định hướng phục vụ nội bộ (Quản lý và Thu ngân), xác định mô hình bán lẻ vãng lai nên bỏ hoàn toàn bảng/đối tượng KhachHang, tinh gọn bảng bỏ email, tienLuong.
Cơ sở dữ liệu: Chốt chuẩn 5 bảng trong GonMart_DB (NhanVien, TaiKhoan, SanPham, HoaDon, ChiTietHoaDon), ràng buộc toàn vẹn bằng CHECK và nạp sẵn dữ liệu mẫu.
Kết nối & Entity: Cài đặt ConnectDB theo mẫu Singleton Thread-Safe (chỉ dùng sqljdbc42.jar). Xây dựng 5 lớp Entity đóng gói chặt chẽ: nạp chồng constructor, bắt lỗi định dạng (regex, tuổi 18-65, số dương) ngay tại từng setter và ném IllegalArgumentException.
Giai đoạn 2: Hiện thực hóa Nghiệp vụ (BUS), DAO & Khắc phục 8 "tử huyệt" kỹ thuật ban đầu
Tầng DAO & Transaction: Xóa sạch việc lưu RAM ảo (static ArrayList), chuyển sang JDBC thuần (PreparedStatement). Hiện thực Database Transaction trong HoaDonDAO.luuHoaDonVaChiTiet() (setAutoCommit(false) 
→
→ thêm hóa đơn 
→
→ thêm chi tiết 
→
→ trừ tồn kho 
→
→ commit/rollback).
Tầng BUS & Session: Tạo Session.java quản trị tài khoản, phiên trực ca và hàm phân quyền isQuanLy().
Khắc phục 8 tử huyệt đồ án:
Chuyển đường dẫn nạp ảnh sang dạng tương đối (/imgs/logo.png).
Bỏ RAM ảo, kết nối 100% CSDL thật.
Chuẩn hóa ConnectDB Singleton.
Đồng bộ thuộc tính Entity khớp với CSDL.
Bắt lỗi form nhập liệu theo chuẩn 4 bước IUH (JOptionPane 
→
→ selectAll() 
→
→ requestFocus() 
→
→ return false).
Khóa sửa trực tiếp trên ô giỏ hàng, chuyển sang nút "Sửa số lượng".
Bọc Transaction khi thanh toán POS.
Khóa ô nhập mã khóa chính (txtMa.setEditable(false)) khi chọn dòng sửa.
Giai đoạn 3: Phản biện mã nguồn mẫu bên ngoài & Quyết định bảo toàn kiến trúc
Bối cảnh: Xuất hiện đề xuất đối chiếu và chuyển mã nguồn theo một bộ code mẫu trôi nổi (đổi sang mô hình 2 lớp, đổi tên trường CSDL, thêm lại KhachHang, email, tienLuong).
Quyết định kỹ thuật: Bảo toàn 100% mã nguồn hiện tại, không sửa theo code mẫu.
Lý do bảo toàn: Giữ vững kiến trúc 3 lớp chuẩn mực (GUI -> BUS -> DAO -> ConnectDB + Entity + Util); tránh xung đột tên cột CSDL (maSP vs maSanPham); tiếp tục duy trì kỹ thuật try-with-resources hiện đại; và bảo đảm mã nguồn đồng nhất tuyệt đối với Báo cáo Word (HSK.docx).
Giai đoạn 4: Chuẩn hóa Khởi động an toàn & Nhận diện thương hiệu công nghiệp
Khởi động ứng dụng (Main.java): Thiết lập Main.java là Entry Point duy nhất. Thêm khối kiểm tra ConnectDB ngay khi khởi chạy; nếu SQL Server chưa bật hoặc sai cổng, hiển thị JOptionPane cảnh báo trực quan thay vì để ứng dụng sập im lặng.
Đồng bộ Font chữ & Giao diện: Thiết lập UIManager.put toàn hệ thống sang font Times New Roman học thuật.
Bộ nhận diện thương hiệu: Chuyển đổi toàn bộ 8 màn hình Swing sang tone màu chủ đạo Xanh dương Doanh nghiệp #155EEF. Phân cấp màu nút bấm rõ ràng (Thêm/Tìm: Xanh dương, Giỏ hàng: Xanh lá, Sửa: Teal, Xóa/Ngừng bán: Đỏ). Sử dụng CardLayout trên MainGUI để chuyển đổi phân hệ mượt mà.
Giai đoạn 5: Đối soát 1-1 với Báo cáo Word & Vá 6 lỗi tiềm ẩn chuyên sâu
Đối soát tài liệu: Kiểm tra khớp từng trường thuộc tính giữa bảng 2.6 – 2.10 trong file Word HSK.docx với mã nguồn và SQL Server.
Xử lý 6 lỗi tiềm ẩn vừa phát hiện:
Hóa đơn hiển thị 0đ: Bổ sung biến và hàm đọc tongTien trong HoaDon và HoaDonDAO.mapRow.
Thêm nhân viên thiếu tài khoản: Tự động kích hoạt tạo TaiKhoan (mật khẩu mặc định 12345678) khi thêm mới nhân viên qua NhanVienBUS.
Mất ảnh sản phẩm: Lưu lại tên ảnh cũ (spCu.getHinhAnh()) khi cập nhật thông tin sản phẩm.
Crash số tiền trên Windows tiếng Việt: Cố định DecimalFormatSymbols theo Locale.US để xử lý triệt để dấu chấm/phẩy hàng nghìn.
Lỗi đọc lịch sử đơn hàng: Đảo thứ tự setSoLuong trước setSanPham trong ChiTietHoaDon để không bị chặn bởi tồn kho hiện tại.
Lệch chuỗi vai trò: Chuẩn hóa chuỗi "Bán hàng" thành "Nhân viên bán hàng" tại setter để khớp CHECK constraint của CSDL.
Giai đoạn 6: Kiểm thử phân quyền & Đánh giá chất lượng mã nguồn
Cơ chế phân quyền (Session):
Quản lý (admin / QL001): Hiển thị toàn bộ 6 chức năng (Trang chủ, Nhân viên, Sản phẩm, Bán hàng, Quản lý hóa đơn, Thống kê doanh thu).
Thu ngân (nv002 / NV002): Chỉ hiển thị 3 chức năng thu gọn (Trang chủ, POS Bán hàng, Tra cứu hóa đơn); hóa đơn lập tự động gắn đúng mã nhân viên trực ca.
Chất lượng mã nguồn: Biên dịch bằng javac thành công tuyệt đối (Exit Code 0), sinh đầy đủ các file .class trong thư mục bin/. Toàn bộ 5/5 test case tự động kiểm thử nghiệp vụ độc lập đều đạt chuẩn PASSED 100%.
Giai đoạn 7: Hiện trạng & Kế hoạch hoàn tất đóng gói bảo vệ
Trạng thái: Tầng Logic, CSDL và Nghiệp vụ đạt mức xuất sắc (10/10).
Nhiệm vụ bàn giao cuối cùng:
Chạy nghiệm thu trực tiếp giao diện từ Main.java trên cả hai tài khoản mẫu (admin và nv002).
Chụp ảnh màn hình giao diện ứng dụng thực tế (thay thế các hình mockup cũ) đưa vào Chương 3 của file Báo cáo Word HSK.docx.
Đóng gói mã nguồn sạch, file kịch bản CSDL GonMart_DB, thư viện sqljdbc42.jar và file báo cáo hoàn chỉnh để nộp đồ án.