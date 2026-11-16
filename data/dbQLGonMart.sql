USE master
GO

IF DB_ID('GonMart_DB') IS NOT NULL
	DROP DATABASE GonMart_DB
GO

CREATE DATABASE GonMart_DB
GO

USE GonMart_DB
GO

-- 1. Bảng Nhân Viên
CREATE TABLE NhanVien
(
	MaNV VARCHAR(5) NOT NULL,
	HoTen NVARCHAR(100) NOT NULL,
	GioiTinh NVARCHAR(10) NOT NULL,
	NgaySinh DATE NOT NULL,
	SoDienThoai VARCHAR(10) NOT NULL,
	DiaChi NVARCHAR(255),
	VaiTro NVARCHAR(30) NOT NULL,
	TrangThai BIT NOT NULL DEFAULT 1,

	CONSTRAINT PK_NhanVien PRIMARY KEY (MaNV),
	CONSTRAINT CK_NhanVien_MaNV CHECK (MaNV LIKE 'NV[0-9][0-9][0-9]' OR MaNV LIKE 'QL[0-9][0-9][0-9]'),
	CONSTRAINT CK_NhanVien_GioiTinh CHECK (GioiTinh IN (N'Nam', N'Nữ', N'Khác')),
	CONSTRAINT CK_NhanVien_NgaySinh CHECK (NgaySinh <= DATEADD(YEAR, -18, GETDATE()) AND NgaySinh >= DATEADD(YEAR, -65, GETDATE())),
	CONSTRAINT CK_NhanVien_SoDienThoai CHECK (LEN(SoDienThoai) = 10 AND SoDienThoai LIKE '0[35789]%' AND SoDienThoai NOT LIKE '%[^0-9]%'),
	CONSTRAINT CK_NhanVien_VaiTro CHECK (VaiTro IN (N'Quản lý', N'Nhân viên bán hàng'))
)
GO

-- 2. Bảng Tài Khoản
CREATE TABLE TaiKhoan
(
	TenDangNhap VARCHAR(5) NOT NULL,
	MatKhau VARCHAR(64) NOT NULL,
	Quyen VARCHAR(10) NOT NULL,
	TrangThai BIT NOT NULL DEFAULT 1,
	MaNV VARCHAR(5) NOT NULL,

	CONSTRAINT PK_TaiKhoan PRIMARY KEY (TenDangNhap),
	CONSTRAINT CK_TaiKhoan_MatKhau CHECK (LEN(MatKhau) >= 8),
	CONSTRAINT CK_TaiKhoan_Quyen CHECK (Quyen IN ('QUAN_LY', 'BAN_HANG')),
	CONSTRAINT UQ_TaiKhoan_MaNV UNIQUE (MaNV),
	CONSTRAINT FK_TaiKhoan_NhanVien FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV) ON DELETE CASCADE
)
GO

-- 3. Bảng Sản Phẩm
CREATE TABLE SanPham
(
	MaSP VARCHAR(5) NOT NULL,
	TenSP NVARCHAR(150) NOT NULL,
	LoaiSP NVARCHAR(50) NOT NULL,
	DonGia DECIMAL(18,2) NOT NULL,
	SoLuongTon INT NOT NULL DEFAULT 0,
	TrangThai BIT NOT NULL DEFAULT 1,
	HinhAnh NVARCHAR(255),

	CONSTRAINT PK_SanPham PRIMARY KEY (MaSP),
	CONSTRAINT CK_SanPham_MaSP CHECK (MaSP LIKE 'SP[0-9][0-9][0-9]'),
	CONSTRAINT CK_SanPham_DonGia CHECK (DonGia > 0),
	CONSTRAINT CK_SanPham_SoLuongTon CHECK (SoLuongTon >= 0)
)
GO

-- 4. Bảng Hóa Đơn
CREATE TABLE HoaDon
(
	MaHD VARCHAR(5) NOT NULL,
	NgayLap DATETIME NOT NULL DEFAULT GETDATE(),
	MaNV VARCHAR(5) NOT NULL,
	TienKhachDua DECIMAL(18,2) NOT NULL,
	TongTien DECIMAL(18,2) NOT NULL DEFAULT 0,

	CONSTRAINT PK_HoaDon PRIMARY KEY (MaHD),
	CONSTRAINT CK_HoaDon_MaHD CHECK (MaHD LIKE 'HD[0-9][0-9][0-9]'),
	CONSTRAINT CK_HoaDon_NgayLap CHECK (NgayLap <= GETDATE()),
	CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
	CONSTRAINT CK_HoaDon_TongTien CHECK (TongTien >= 0),
	CONSTRAINT CK_HoaDon_TienKhachDua CHECK (TienKhachDua >= 0 AND TienKhachDua >= TongTien)
)
GO

-- 5. Bảng Chi Tiết Hóa Đơn
CREATE TABLE ChiTietHoaDon
(
	MaHD VARCHAR(5) NOT NULL,
	MaSP VARCHAR(5) NOT NULL,
	SoLuong INT NOT NULL,
	DonGia DECIMAL(18,2) NOT NULL,

	CONSTRAINT PK_ChiTietHoaDon PRIMARY KEY (MaHD, MaSP),
	CONSTRAINT FK_ChiTietHoaDon_HoaDon FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD) ON DELETE CASCADE,
	CONSTRAINT FK_ChiTietHoaDon_SanPham FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP),
	CONSTRAINT CK_ChiTietHoaDon_SoLuong CHECK (SoLuong > 0),
	CONSTRAINT CK_ChiTietHoaDon_DonGia CHECK (DonGia > 0)
)
GO

-- =============================================
-- NHẬP DỮ LIỆU MẪU
-- =============================================

-- Bảng Nhân Viên (1 Quản lý, 5 Nhân viên)
INSERT INTO NhanVien VALUES ('QL001', N'Nguyễn Tất Thành Toàn', N'Nam', '1990-01-15', '0901234567', N'Gò Vấp, TP.HCM', N'Quản lý', 1)
INSERT INTO NhanVien VALUES ('NV001', N'Trần Thị Linh Chi', N'Nữ', '2000-05-15', '0912345678', N'Củ Chi, TP.HCM', N'Nhân viên bán hàng', 1)
INSERT INTO NhanVien VALUES ('NV002', N'Phan Hoàng Nam', N'Nam', '1998-10-20', '0987654321', N'Quận 12, TP.HCM', N'Nhân viên bán hàng', 1)
INSERT INTO NhanVien VALUES ('NV003', N'Đặng Thị Ngọc Lan', N'Nữ', '2002-02-12', '0356789123', N'Hóc Môn, TP.HCM', N'Nhân viên bán hàng', 1)
INSERT INTO NhanVien VALUES ('NV004', N'Bùi Xuân Hùng', N'Nam', '1995-12-30', '0789123456', N'Gò Vấp, TP.HCM', N'Nhân viên bán hàng', 1)
INSERT INTO NhanVien VALUES ('NV005', N'Lê Thị Mai Trang', N'Nữ', '2001-07-22', '0898765432', N'Bình Thạnh, TP.HCM', N'Nhân viên bán hàng', 1)
GO

-- Bảng Tài Khoản (Mật khẩu tối thiểu 8 ký tự, tên đăng nhập trùng MaNV)
INSERT INTO TaiKhoan VALUES ('QL001', 'admin123', 'QUAN_LY', 1, 'QL001')
INSERT INTO TaiKhoan VALUES ('NV001', '12345678', 'BAN_HANG', 1, 'NV001')
INSERT INTO TaiKhoan VALUES ('NV002', '12345678', 'BAN_HANG', 1, 'NV002')
INSERT INTO TaiKhoan VALUES ('NV003', '12345678', 'BAN_HANG', 1, 'NV003')
INSERT INTO TaiKhoan VALUES ('NV004', '12345678', 'BAN_HANG', 1, 'NV004')
INSERT INTO TaiKhoan VALUES ('NV005', '12345678', 'BAN_HANG', 1, 'NV005')
GO

-- Bảng Sản Phẩm (15 sản phẩm, có tên file ảnh trong thư mục imgs)
INSERT INTO SanPham VALUES ('SP001', N'Mì trộn Omachi xốt bò hầm', N'Thực phẩm', 25000, 100, 1, 'omachi.jpg')
INSERT INTO SanPham VALUES ('SP002', N'Bánh bao nhân thịt trứng cút', N'Thực phẩm', 15000, 80, 1, 'banhbaonhanthit.jpg')
INSERT INTO SanPham VALUES ('SP003', N'Xúc xích nướng CP', N'Thực phẩm', 12000, 120, 1, 'xucxichnuong.jpg')
INSERT INTO SanPham VALUES ('SP004', N'Cơm nắm cá ngừ Mayonnaise', N'Thực phẩm', 25000, 60, 1, 'comnamnamngu.jpg')
INSERT INTO SanPham VALUES ('SP005', N'Sandwich gà Teriyaki', N'Thực phẩm', 29000, 50, 1, 'sanwichga.jpg')
INSERT INTO SanPham VALUES ('SP006', N'Nước khoáng Aquafina 500ml', N'Nước giải khát', 8000, 200, 1, 'nuoc-tinh-khiet-aquafina-500ml-202407121618240191.jpg')
INSERT INTO SanPham VALUES ('SP007', N'Coca Cola lon 330ml', N'Nước giải khát', 12000, 180, 1, 'cocacola330.jpg')
INSERT INTO SanPham VALUES ('SP008', N'Trà xanh C2 hương chanh 455ml', N'Nước giải khát', 10000, 150, 1, 'c2.jpg')
INSERT INTO SanPham VALUES ('SP009', N'Cà phê hòa tan G7 hộp 18 gói', N'Nước giải khát', 35000, 70, 1, 'g7.jpg')
INSERT INTO SanPham VALUES ('SP010', N'Kem ốc quế Merino socola', N'Bánh kẹo', 15000, 90, 1, 'kem_oc_que_merino.jpg')
INSERT INTO SanPham VALUES ('SP011', N'Snack Oishi tôm cay gói 40g', N'Bánh kẹo', 10000, 130, 1, 'snack_oishi_tom_cay.jpg')
INSERT INTO SanPham VALUES ('SP012', N'Bánh mì que pate đặc biệt', N'Thực phẩm', 18000, 65, 1, 'banhmi_que_pate.jpg')
INSERT INTO SanPham VALUES ('SP013', N'Sữa tươi tiệt trùng TH True Milk', N'Nước giải khát', 10000, 140, 1, 'sua_tuoi_th_true_milk_180ml.jpg')
INSERT INTO SanPham VALUES ('SP014', N'Khăn giấy Pulppy lụa gói', N'Đồ dùng sinh hoạt', 8000, 110, 1, 'khan_giay_pulppy_goi.jpg')
INSERT INTO SanPham VALUES ('SP015', N'Mì ly Modern lẩu Thái', N'Thực phẩm', 15000, 95, 1, 'mi_ly_modern_lau_thai.jpg')
GO

-- Bảng Hóa Đơn (8 hóa đơn)
INSERT INTO HoaDon VALUES ('HD001', '2024-10-20 08:30:00', 'NV001', 100000, 84000)
INSERT INTO HoaDon VALUES ('HD002', '2024-10-20 09:15:00', 'NV002', 50000, 46000)
INSERT INTO HoaDon VALUES ('HD003', '2024-10-21 10:00:00', 'NV001', 50000, 37000)
INSERT INTO HoaDon VALUES ('HD004', '2024-10-21 11:30:00', 'NV003', 100000, 76000)
INSERT INTO HoaDon VALUES ('HD005', '2024-10-22 14:00:00', 'NV004', 100000, 78000)
INSERT INTO HoaDon VALUES ('HD006', '2024-10-22 16:45:00', 'NV002', 80000, 65000)
INSERT INTO HoaDon VALUES ('HD007', '2024-10-23 18:20:00', 'NV005', 80000, 66000)
INSERT INTO HoaDon VALUES ('HD008', '2024-10-23 19:10:00', 'NV001', 50000, 41000)
GO

-- Bảng Chi Tiết Hóa Đơn (18 chi tiết hóa đơn)
-- HD001
INSERT INTO ChiTietHoaDon VALUES ('HD001', 'SP001', 2, 25000)
INSERT INTO ChiTietHoaDon VALUES ('HD001', 'SP007', 2, 12000)
INSERT INTO ChiTietHoaDon VALUES ('HD001', 'SP008', 1, 10000)

-- HD002
INSERT INTO ChiTietHoaDon VALUES ('HD002', 'SP002', 2, 15000)
INSERT INTO ChiTietHoaDon VALUES ('HD002', 'SP006', 2, 8000)

-- HD003
INSERT INTO ChiTietHoaDon VALUES ('HD003', 'SP004', 1, 25000)
INSERT INTO ChiTietHoaDon VALUES ('HD003', 'SP007', 1, 12000)

-- HD004
INSERT INTO ChiTietHoaDon VALUES ('HD004', 'SP003', 3, 12000)
INSERT INTO ChiTietHoaDon VALUES ('HD004', 'SP008', 2, 10000)
INSERT INTO ChiTietHoaDon VALUES ('HD004', 'SP011', 2, 10000)

-- HD005
INSERT INTO ChiTietHoaDon VALUES ('HD005', 'SP005', 2, 29000)
INSERT INTO ChiTietHoaDon VALUES ('HD005', 'SP013', 2, 10000)

-- HD006
INSERT INTO ChiTietHoaDon VALUES ('HD006', 'SP009', 1, 35000)
INSERT INTO ChiTietHoaDon VALUES ('HD006', 'SP010', 2, 15000)

-- HD007
INSERT INTO ChiTietHoaDon VALUES ('HD007', 'SP012', 2, 18000)
INSERT INTO ChiTietHoaDon VALUES ('HD007', 'SP015', 2, 15000)

-- HD008
INSERT INTO ChiTietHoaDon VALUES ('HD008', 'SP014', 2, 8000)
INSERT INTO ChiTietHoaDon VALUES ('HD008', 'SP001', 1, 25000)
GO

-- =============================================
-- KIỂM TRA DỮ LIỆU
-- =============================================
SELECT * FROM NhanVien
SELECT * FROM TaiKhoan
SELECT * FROM SanPham
SELECT * FROM HoaDon
SELECT * FROM ChiTietHoaDon
GO