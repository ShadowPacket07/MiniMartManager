# MiniMartManager
Convenience Store Management System using Java Swing and SQL Server
## Yêu cầu môi trường phát triển

Để chạy dự án GonStore, các thành viên cần chuẩn bị:

- JDK 17 (Java Development Kit).
- Eclipse IDE hỗ trợ Java 17.
- Microsoft SQL Server.
- Database `QLSieuThi`.
- JDBC Driver `sqljdbc42.jar` (đã được cung cấp trong thư mục `lib`).

### Cấu hình sau khi Clone/Pull

1. Cài đặt JDK 17.
2. Mở project bằng Eclipse.
3. Kiểm tra `Java Build Path` sử dụng `JavaSE-17`.
4. Kiểm tra thư viện `lib/sqljdbc42.jar` đã được nhận diện.
5. Cấu hình kết nối SQL Server trong `ConnectDB.java` theo môi trường máy cá nhân.
6. Chạy script `data/BangSieuThi.sql` để tạo database nếu chưa có.

**Lưu ý:** Không commit mật khẩu SQL Server cá nhân lên GitHub.
