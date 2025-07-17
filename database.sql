/*
================================================================
 GaraOtoManagementDB - SQL Server Database Script
================================================================
*/

-- Step 1: Create and use the database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'GaraOtoManagementDB')
BEGIN
    CREATE DATABASE GaraOtoManagementDB;
END
GO

USE GaraOtoManagementDB;
GO

-- Step 2: Drop existing tables in the correct order to avoid foreign key constraints
IF OBJECT_ID('dbo.ChiTietPhieuSuaChua_VatTu', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhieuSuaChua_VatTu;
IF OBJECT_ID('dbo.ChiTietPhieuSuaChua_TienCong', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhieuSuaChua_TienCong;
IF OBJECT_ID('dbo.PhieuSuaChua', 'U') IS NOT NULL DROP TABLE dbo.PhieuSuaChua;
IF OBJECT_ID('dbo.PhieuThuTien', 'U') IS NOT NULL DROP TABLE dbo.PhieuThuTien;
IF OBJECT_ID('dbo.TiepNhan', 'U') IS NOT NULL DROP TABLE dbo.TiepNhan;
IF OBJECT_ID('dbo.Xe', 'U') IS NOT NULL DROP TABLE dbo.Xe;
IF OBJECT_ID('dbo.HieuXe', 'U') IS NOT NULL DROP TABLE dbo.HieuXe;
IF OBJECT_ID('dbo.ChuXe', 'U') IS NOT NULL DROP TABLE dbo.ChuXe;
IF OBJECT_ID('dbo.VatTu', 'U') IS NOT NULL DROP TABLE dbo.VatTu;
IF OBJECT_ID('dbo.TienCong', 'U') IS NOT NULL DROP TABLE dbo.TienCong;
IF OBJECT_ID('dbo.ThamSo', 'U') IS NOT NULL DROP TABLE dbo.ThamSo;
IF OBJECT_ID('dbo.Tho', 'U') IS NOT NULL DROP TABLE dbo.Tho;
IF OBJECT_ID('dbo.ChiTietPhanQuyen', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhanQuyen;
IF OBJECT_ID('dbo.TaiKhoanNguoiDung', 'U') IS NOT NULL DROP TABLE dbo.TaiKhoanNguoiDung;
IF OBJECT_ID('dbo.PhanQuyen', 'U') IS NOT NULL DROP TABLE dbo.PhanQuyen;
IF OBJECT_ID('dbo.NhaCungCap', 'U') IS NOT NULL DROP TABLE dbo.NhaCungCap;
GO

-- Step 3: Create Tables

-- Permission System
CREATE TABLE PhanQuyen (
    MaPhanQuyen NVARCHAR(20) PRIMARY KEY,
    TenPhanQuyen NVARCHAR(100) NOT NULL
);
GO

CREATE TABLE TaiKhoanNguoiDung (
    MaTK INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap NVARCHAR(50) UNIQUE NOT NULL,
    MatKhauHash NVARCHAR(255) NOT NULL,
    MaPhanQuyen NVARCHAR(20) FOREIGN KEY REFERENCES PhanQuyen(MaPhanQuyen),
    HoTen NVARCHAR(100) NOT NULL,
    TrangThai BIT DEFAULT 1 -- 1 for Active, 0 for Inactive
);
GO

CREATE TABLE ChiTietPhanQuyen (
    MaPhanQuyen NVARCHAR(20) FOREIGN KEY REFERENCES PhanQuyen(MaPhanQuyen) ON DELETE CASCADE,
    MaChucNang NVARCHAR(20),
    PRIMARY KEY (MaPhanQuyen, MaChucNang)
);
GO

-- Core Garage Tables
CREATE TABLE HieuXe (
    MaHieuXe INT IDENTITY(1,1) PRIMARY KEY,
    TenHieuXe NVARCHAR(50) NOT NULL UNIQUE
);
GO

CREATE TABLE ChuXe (
    MaChuXe INT IDENTITY(1,1) PRIMARY KEY,
    TenChuXe NVARCHAR(100) NOT NULL,
    DiaChi NVARCHAR(200),
    DienThoai NVARCHAR(20),
    Email NVARCHAR(100)
);
GO

CREATE TABLE Xe (
    BienSo NVARCHAR(20) PRIMARY KEY,
    MaHieuXe INT NOT NULL FOREIGN KEY REFERENCES HieuXe(MaHieuXe),
    MaChuXe INT NOT NULL FOREIGN KEY REFERENCES ChuXe(MaChuXe),
    DoiXe INT,
    MauSac NVARCHAR(50),
    SoKMHienTai INT
);
GO

CREATE TABLE TiepNhan (
    MaTiepNhan INT IDENTITY(1,1) PRIMARY KEY,
    BienSo NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES Xe(BienSo),
    NgayTiepNhan DATE NOT NULL,
    TinhTrangXe NVARCHAR(500) NULL,
    TrangThai NVARCHAR(50) NOT NULL DEFAULT N'Chờ sửa',
    TongTienNo DECIMAL(18,2) DEFAULT 0.00,
    TrangThaiHoanTat BIT DEFAULT 0
);
GO

CREATE TABLE VatTu (
    MaVatTu INT IDENTITY(1,1) PRIMARY KEY,
    TenVatTu NVARCHAR(100) NOT NULL UNIQUE,
    DonGiaBan DECIMAL(18,2) NOT NULL CHECK (DonGiaBan >= 0),
    SoLuongTon INT NOT NULL DEFAULT 0 CHECK (SoLuongTon >= 0),
    DonViTinh NVARCHAR(50),
    MucTonKhoToiThieu INT DEFAULT 0 CHECK (MucTonKhoToiThieu >= 0)
);
GO

CREATE TABLE NhaCungCap (
    MaNhaCungCap INT IDENTITY(1,1) PRIMARY KEY,
    TenNhaCungCap NVARCHAR(200) NOT NULL UNIQUE,
    DienThoai NVARCHAR(20),
    DiaChi NVARCHAR(200),
    Email NVARCHAR(100)
);
GO

CREATE TABLE TienCong (
    MaTienCong INT IDENTITY(1,1) PRIMARY KEY,
    NoiDung NVARCHAR(100) NOT NULL UNIQUE,
    DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0)
);
GO

CREATE TABLE Tho (
    MaTho INT IDENTITY(1,1) PRIMARY KEY,
    TenTho NVARCHAR(100) NOT NULL,
    SoDienThoai NVARCHAR(20),
    ChuyenMon NVARCHAR(100)
);
GO

CREATE TABLE PhieuSuaChua (
    MaPhieuSC INT IDENTITY(1,1) PRIMARY KEY,
    MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan),
    NgaySuaChua DATE NOT NULL,
    GhiChu NVARCHAR(255),
    TongTien DECIMAL(18,2) NOT NULL DEFAULT 0.00 CHECK (TongTien >= 0),
    MaTho INT NULL FOREIGN KEY REFERENCES Tho(MaTho),
    TrangThaiHoanTat BIT DEFAULT 0
);
GO

CREATE TABLE ChiTietPhieuSuaChua_VatTu (
    MaChiTietVatTu INT IDENTITY(1,1) PRIMARY KEY,
    MaPhieuSC INT NOT NULL FOREIGN KEY REFERENCES PhieuSuaChua(MaPhieuSC),
    MaVatTu INT NOT NULL FOREIGN KEY REFERENCES VatTu(MaVatTu),
    SoLuong INT NOT NULL CHECK (SoLuong > 0),
    DonGiaNhap DECIMAL(18,2) NOT NULL CHECK (DonGiaNhap >= 0),
    ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0)
);
GO

CREATE TABLE ChiTietPhieuSuaChua_TienCong (
    MaChiTietTienCong INT IDENTITY(1,1) PRIMARY KEY,
    MaPhieuSC INT NOT NULL FOREIGN KEY REFERENCES PhieuSuaChua(MaPhieuSC),
    MaTienCong INT NOT NULL FOREIGN KEY REFERENCES TienCong(MaTienCong),
    DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0),
    ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0)
);
GO

CREATE TABLE PhieuThuTien (
    MaPhieuThu INT IDENTITY(1,1) PRIMARY KEY,
    MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan),
    NgayThu DATE NOT NULL,
    SoTienThu DECIMAL(18,2) NOT NULL CHECK (SoTienThu > 0)
);
GO

CREATE TABLE ThamSo (
    TenThamSo NVARCHAR(100) PRIMARY KEY,
    GiaTri INT NOT NULL
);
GO


-- Step 4: Insert Sample Data

-- Permission System Data
INSERT INTO PhanQuyen (MaPhanQuyen, TenPhanQuyen) VALUES
('GIAMDOC', N'Giám đốc'),
('NVTIEPNHAN', N'Nhân viên tiếp nhận'),
('THOSUACHUA', N'Thợ sửa chữa'),
('KETOAN', N'Kế toán'),
('NVKHO', N'Nhân viên kho');
GO

-- Function codes for reference in the application:
-- Q1: TiepNhan, Q2: SuaChua, Q3: ThuTien, Q4: BaoCao, Q5: CauHinh
-- Q6: QLHieuXe, Q7: QLTienCong, Q8: QLTho, Q9: QLNhaCungCap, Q10: QLVatTu, Q11: QLTaiKhoan, Q12: QLPhanQuyen
INSERT INTO ChiTietPhanQuyen (MaPhanQuyen, MaChucNang) VALUES
('GIAMDOC', 'Q1'), ('GIAMDOC', 'Q2'), ('GIAMDOC', 'Q3'), ('GIAMDOC', 'Q4'),
('GIAMDOC', 'Q5'), ('GIAMDOC', 'Q6'), ('GIAMDOC', 'Q7'), ('GIAMDOC', 'Q8'),
('GIAMDOC', 'Q9'), ('GIAMDOC', 'Q10'), ('GIAMDOC', 'Q11'), ('GIAMDOC', 'Q12'),
('NVTIEPNHAN', 'Q1'),
('THOSUACHUA', 'Q2'),
('KETOAN', 'Q3'), ('KETOAN', 'Q4'),
('NVKHO', 'Q9'), ('NVKHO', 'Q10');
GO

-- Default password for all sample accounts is '123'
-- MD5 hash for '123': 202cb962ac59075b964b07152d234b70
INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, MaPhanQuyen, HoTen, TrangThai) VALUES
('admin', '202cb962ac59075b964b07152d234b70', 'GIAMDOC', N'Admin', 1),
('tiepnhan', '202cb962ac59075b964b07152d234b70', 'NVTIEPNHAN', N'Nguyễn Thị Tiếp Nhận', 1),
('suachua', '202cb962ac59075b964b07152d234b70', 'THOSUACHUA', N'Trần Văn Thợ', 1),
('ketoan', '202cb962ac59075b964b07152d234b70', 'KETOAN', N'Lê Thị Kế Toán', 1),
('kho', '202cb962ac59075b964b07152d234b70', 'NVKHO', N'Phạm Văn Kho', 1);
GO

-- Core Data
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES
(N'SoXeToiDaMoiNgay', 30),
(N'SoHieuXeToiDa', 10),
(N'SoLoaiVatTuToiDa', 200),
(N'SoLoaiTienCongToiDa', 100);
GO

INSERT INTO HieuXe (TenHieuXe) VALUES
('Toyota'), ('Honda'), ('Ford'), ('BMW'), ('Mercedes-Benz'),
('Hyundai'), ('Kia'), ('Mazda'), ('Chevrolet'), ('Nissan');
GO

INSERT INTO ChuXe (TenChuXe, DiaChi, DienThoai, Email) VALUES
(N'Nguyễn Văn A', N'123 Đường ABC, Quận 1', '0901234567', 'nguyenvana@example.com'),
(N'Trần Thị B', N'456 Đường XYZ, Quận 2', '0912345678', 'tranb@example.com'),
(N'Lê Văn C', N'789 Đường DEF, Quận 3', '0987654321', 'lec@example.com');
GO

INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe) VALUES
('51A-123.45', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Toyota'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Nguyễn Văn A')),
('51B-678.90', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Honda'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Trần Thị B')),
('51C-111.22', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Ford'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Lê Văn C'));
GO

INSERT INTO VatTu (TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu) VALUES
(N'Lốp xe Michelin 205/55R16', 1500000.00, 50, N'cái', 10),
(N'Dầu nhớt Castrol GTX 4L', 450000.00, 100, N'chai', 20),
(N'Lọc dầu động cơ', 120000.00, 80, N'cái', 15),
(N'Bugia Denso', 80000.00, 120, N'cái', 30);
GO

INSERT INTO NhaCungCap (TenNhaCungCap, DienThoai, DiaChi, Email) VALUES
(N'Công ty TNHH Phụ tùng ô tô ABC', '0281234567', N'123 Đường XYZ, Quận 10', 'info@abcphutung.com'),
(N'Công ty Cổ phần Vật tư Sửa chữa Xe', '0249876543', N'456 Đường DEF, Quận Hoàn Kiếm', 'contact@vtscx.com');
GO

INSERT INTO TienCong (NoiDung, DonGia) VALUES
(N'Kiểm tra tổng quát', 200000.00),
(N'Thay dầu nhớt', 150000.00),
(N'Thay lốp xe', 100000.00),
(N'Cân chỉnh thước lái', 300000.00);
GO

INSERT INTO Tho (TenTho, SoDienThoai, ChuyenMon) VALUES
(N'Nguyễn Văn Thợ', '0900111222', N'Động cơ'),
(N'Phạm Thị Sửa', '0900333444', N'Điện - Điện lạnh'),
(N'Lê Minh Kỹ', '0900555666', N'Gầm - Lốp');
GO

INSERT INTO TiepNhan (BienSo, NgayTiepNhan, TinhTrangXe, TrangThai, TongTienNo, TrangThaiHoanTat) VALUES
('51A-123.45', GETDATE(), N'Trầy xước nhẹ cản trước', N'Chờ sửa', 0.00, 0),
('51B-678.90', DATEADD(day, -5, GETDATE()), N'Bể đèn hậu phải', N'Đang sửa', 0.00, 0);
GO

PRINT 'Database script completed successfully.';
GO