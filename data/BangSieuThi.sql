
-- =====================================================
-- DATABASE: QLSieuThi
-- PROJECT: GonStore
-- DBMS: SQL Server
-- =====================================================

-- 1. TAO DATABASE
IF DB_ID(N'QLSieuThi') IS NULL
BEGIN
    CREATE DATABASE QLSieuThi;
END
GO

USE QLSieuThi;
GO

-- =====================================================
-- 2. TAO BANG SAN PHAM
-- =====================================================

CREATE TABLE SanPham
(
    maSanPham NVARCHAR(100) PRIMARY KEY,
    tenSanPham NVARCHAR(150) NOT NULL,
    giaTien FLOAT NOT NULL CHECK (giaTien > 0),
    trangThai BIT NOT NULL DEFAULT 1,
    anhSanPham NVARCHAR(255)
);
GO

-- =====================================================
-- 3. TAO BANG NHAN VIEN
-- =====================================================

CREATE TABLE NhanVien
(
    maNhanVien NVARCHAR(100) PRIMARY KEY,
    hoTen NVARCHAR(100) NOT NULL,
    soDienThoai NVARCHAR(15),
    diaChi NVARCHAR(150),
    email NVARCHAR(100),
    tienLuong FLOAT,
    ngaySinh DATE,
    gioiTinh NVARCHAR(10)
);
GO

-- =====================================================
-- 4. TAO BANG TAI KHOAN
-- =====================================================

CREATE TABLE TaiKhoan
(
    username NVARCHAR(100) PRIMARY KEY,
    password NVARCHAR(100) NOT NULL,
    role NVARCHAR(20) NOT NULL DEFAULT N'Nhân viên',
    maNhanVien NVARCHAR(100) UNIQUE NOT NULL,

    CONSTRAINT FK_TaiKhoan_NhanVien
        FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maNhanVien)
);
GO

-- =====================================================
-- 5. TAO BANG HOA DON
-- =====================================================

CREATE TABLE HoaDon
(
    maHoaDon NVARCHAR(100) PRIMARY KEY,
    ngayTao DATETIME NOT NULL DEFAULT GETDATE(),
    maNhanVien NVARCHAR(100) NOT NULL,
    tongTien FLOAT NOT NULL CHECK (tongTien >= 0),

    CONSTRAINT FK_HoaDon_NhanVien
        FOREIGN KEY (maNhanVien)
        REFERENCES NhanVien(maNhanVien)
);
GO

-- =====================================================
-- 6. TAO BANG CHI TIET HOA DON
-- =====================================================

CREATE TABLE ChiTietHoaDon
(
    maHoaDon NVARCHAR(100) NOT NULL,
    maSanPham NVARCHAR(100) NOT NULL,
    soLuong INT NOT NULL CHECK (soLuong > 0),
    giaTien FLOAT NOT NULL CHECK (giaTien > 0),

    CONSTRAINT PK_ChiTietHoaDon
        PRIMARY KEY (maHoaDon, maSanPham),

    CONSTRAINT FK_ChiTietHoaDon_HoaDon
        FOREIGN KEY (maHoaDon)
        REFERENCES HoaDon(maHoaDon),

    CONSTRAINT FK_ChiTietHoaDon_SanPham
        FOREIGN KEY (maSanPham)
        REFERENCES SanPham(maSanPham)
);
GO

-- =====================================================
-- 7. THEM DU LIEU SAN PHAM
-- =====================================================

INSERT INTO SanPham
    (maSanPham, tenSanPham, giaTien, trangThai, anhSanPham)
VALUES
('SP001', N'Mì trộn Omachi xốt bò hầm', 25000, 1, 'imgs/omachi.jpg'),
('SP002', N'Bánh bao nhân thịt', 15000, 1, 'imgs/banhbaonhanthit.jpg'),
('SP003', N'Xúc xích nướng', 12000, 1, 'imgs/xucxichnuong.jpg'),
('SP004', N'Cơm nắm cá ngừ', 25000, 1, 'imgs/comnamnamngu.jpg'),
('SP005', N'Sandwich gà teriyaki', 29000, 1, 'imgs/sanwichga.jpg'),
('SP006', N'Nước suối Aquafina 500ml', 8000, 1, 'imgs/nuoc-tinh-khiet-aquafina-500ml-202407121618240191.jpg'),
('SP007', N'Coca Cola lon 330ml', 12000, 1, 'imgs/cocacola330.jpg'),
('SP008', N'Trà xanh C2 455ml', 10000, 1, 'imgs/c2.jpg'),
('SP009', N'Cà phê G7 hộp', 35000, 1, 'imgs/g7.jpg'),
('SP010', N'Kem ốc quế Merino', 15000, 1, 'imgs/kem_oc_que_merino.jpg'),
('SP011', N'Snack Oishi tôm cay', 10000, 1, 'imgs/snack_oishi_tom_cay.jpg'),
('SP012', N'Bánh mì que pate', 18000, 1, 'imgs/banhmi_que_pate.jpg'),
('SP013', N'Sữa tươi TH True Milk 180ml', 9000, 1, 'imgs/sua_tuoi_th_true_milk_180ml.jpg'),
('SP014', N'Khăn giấy Pulppy gói', 5000, 1, 'imgs/khan_giay_pulppy_goi.jpg'),
('SP015', N'Mì ly Modern lẩu Thái', 15000, 1, 'imgs/mi_ly_modern_lau_thai.jpg');
GO

-- =====================================================
-- 8. THEM DU LIEU NHAN VIEN
-- =====================================================

INSERT INTO NhanVien
    (maNhanVien, hoTen, soDienThoai, diaChi, email,
     tienLuong, ngaySinh, gioiTinh)
VALUES
('NV001', N'Nguyễn Tất Thành Toàn', '0900000001',
 N'Gò Vấp, HCM', 'toan@gmail.com', 15000000, '1990-01-01', N'Nam'),

('NV002', N'Trần Thị Linh Chi', '0900000002',
 N'Củ Chi, HCM', 'chi@gmail.com', 7000000, '2000-05-15', N'Nữ'),

('NV003', N'Phan Hoàng Nam', '0900000003',
 N'Quận 12, HCM', 'nam@gmail.com', 7000000, '1998-10-20', N'Nam'),

('NV004', N'Đặng Thị Ngọc Lan', '0900000004',
 N'Hóc Môn, HCM', 'lan@gmail.com', 7000000, '2002-02-12', N'Nữ'),

('NV005', N'Bùi Xuân Hùng', '0900000005',
 N'Gò Vấp, HCM', 'hung@gmail.com', 7200000, '1995-12-30', N'Nam');
GO

-- =====================================================
-- 9. THEM DU LIEU TAI KHOAN
-- =====================================================

INSERT INTO TaiKhoan
    (username, password, role, maNhanVien)
VALUES
('admin', '123', N'Quản lý', 'NV001'),
('chi123', '123', N'Nhân viên', 'NV002'),
('nam456', '123', N'Nhân viên', 'NV003'),
('lan789', '123', N'Nhân viên', 'NV004'),
('hung12', '123', N'Nhân viên', 'NV005');
GO

-- =====================================================
-- 10. THEM DU LIEU HOA DON
-- =====================================================

INSERT INTO HoaDon
    (maHoaDon, ngayTao, maNhanVien, tongTien)
VALUES
('HD001', '2026-04-28 08:00:00', 'NV002', 75000),
('HD002', '2026-04-28 08:15:00', 'NV003', 45000),
('HD003', '2026-04-28 08:30:00', 'NV002', 29000),
('HD004', '2026-04-28 09:00:00', 'NV004', 96000),
('HD005', '2026-04-28 09:30:00', 'NV005', 40000),
('HD006', '2026-04-28 10:00:00', 'NV002', 70000),
('HD007', '2026-04-28 10:15:00', 'NV003', 38000),
('HD008', '2026-04-28 10:45:00', 'NV003', 48000),
('HD009', '2026-04-28 11:00:00', 'NV004', 58000),
('HD010', '2026-04-28 11:30:00', 'NV005', 40000),
('HD011', '2026-04-28 12:00:00', 'NV002', 35000),
('HD012', '2026-04-28 13:00:00', 'NV003', 30000),
('HD013', '2026-04-28 14:00:00', 'NV004', 45000),
('HD014', '2026-04-28 15:00:00', 'NV005', 50000),
('HD015', '2026-04-28 16:00:00', 'NV002', 35000),
('HD016', '2026-04-28 17:00:00', 'NV003', 90000),
('HD017', '2026-04-28 18:00:00', 'NV004', 35000),
('HD018', '2026-04-28 19:00:00', 'NV005', 90000),
('HD019', '2026-04-28 20:00:00', 'NV002', 32000),
('HD020', '2026-04-28 21:00:00', 'NV003', 29000),
('HD100', '2026-05-01 08:00:00', 'NV002', 75000),
('HD102', '2026-05-03 08:15:00', 'NV003', 45000),
('HD103', '2026-05-02 08:30:00', 'NV002', 29000);
GO

-- =====================================================
-- 11. THEM DU LIEU CHI TIET HOA DON
-- =====================================================

INSERT INTO ChiTietHoaDon
    (maHoaDon, maSanPham, soLuong, giaTien)
VALUES
('HD001', 'SP001', 2, 25000),
('HD001', 'SP008', 1, 25000),
('HD002', 'SP004', 1, 45000),
('HD003', 'SP002', 1, 29000),
('HD004', 'SP003', 3, 32000),
('HD005', 'SP006', 1, 40000),
('HD006', 'SP005', 2, 35000),
('HD007', 'SP011', 1, 38000),
('HD008', 'SP012', 1, 48000),
('HD009', 'SP002', 2, 29000),
('HD010', 'SP015', 2, 20000),
('HD011', 'SP014', 1, 35000),
('HD012', 'SP013', 1, 30000),
('HD013', 'SP007', 1, 45000),
('HD014', 'SP001', 2, 25000),
('HD015', 'SP010', 1, 35000),
('HD016', 'SP004', 2, 45000),
('HD017', 'SP005', 1, 35000),
('HD018', 'SP009', 3, 30000),
('HD019', 'SP003', 1, 32000),
('HD020', 'SP002', 1, 29000),
('HD100', 'SP001', 2, 25000),
('HD100', 'SP008', 1, 25000),
('HD102', 'SP004', 1, 45000),
('HD103', 'SP002', 1, 29000);
GO

-- =====================================================
-- 12. KIEM TRA DU LIEU
-- =====================================================

SELECT * FROM SanPham;
SELECT * FROM NhanVien;
SELECT * FROM TaiKhoan;
SELECT * FROM HoaDon;
SELECT * FROM ChiTietHoaDon;
GO

-- =====================================================
-- 13. KIEM TRA QUAN HE NHAN VIEN - TAI KHOAN
-- =====================================================

SELECT
    NV.maNhanVien,
    NV.hoTen,
    TK.username,
    TK.role
FROM NhanVien NV
LEFT JOIN TaiKhoan TK
    ON NV.maNhanVien = TK.maNhanVien;
GO

-- =====================================================
-- 14. KIEM TRA CHI TIET HOA DON
-- =====================================================

SELECT
    HD.maHoaDon,
    HD.ngayTao,
    NV.hoTen AS tenNhanVien,
    SP.tenSanPham,
    CTHD.soLuong,
    CTHD.giaTien,
    CTHD.soLuong * CTHD.giaTien AS thanhTien,
    HD.tongTien
FROM HoaDon HD
JOIN NhanVien NV
    ON HD.maNhanVien = NV.maNhanVien
JOIN ChiTietHoaDon CTHD
    ON HD.maHoaDon = CTHD.maHoaDon
JOIN SanPham SP
    ON CTHD.maSanPham = SP.maSanPham
ORDER BY HD.ngayTao, HD.maHoaDon;
GO
