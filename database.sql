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
-- Drop new inventory receipt tables (child first)
IF OBJECT_ID('dbo.ChiTietPhieuNhapKhoVatTu', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhieuNhapKhoVatTu;
IF OBJECT_ID('dbo.PhieuNhapKhoVatTu', 'U') IS NOT NULL DROP TABLE dbo.PhieuNhapKhoVatTu;
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

-- =========================
-- Parts Receipt (Nhập kho vật tư)
-- =========================

-- Master receipt table
CREATE TABLE PhieuNhapKhoVatTu (
    MaPhieuNhap INT IDENTITY(1,1) PRIMARY KEY,
    NgayNhap DATE NOT NULL,
    MaNhaCungCap INT NOT NULL FOREIGN KEY REFERENCES NhaCungCap(MaNhaCungCap),
    TongTienNhap DECIMAL(18,2) NOT NULL DEFAULT 0.00 CHECK (TongTienNhap >= 0)
);
GO

-- Receipt detail table
CREATE TABLE ChiTietPhieuNhapKhoVatTu (
    MaChiTietPhieuNhap INT IDENTITY(1,1) PRIMARY KEY,
    MaPhieuNhap INT NOT NULL FOREIGN KEY REFERENCES PhieuNhapKhoVatTu(MaPhieuNhap) ON DELETE CASCADE,
    MaVatTu INT NOT NULL FOREIGN KEY REFERENCES VatTu(MaVatTu),
    SoLuongNhap INT NOT NULL CHECK (SoLuongNhap > 0),
    DonGiaNhap DECIMAL(18,2) NOT NULL CHECK (DonGiaNhap >= 0)
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
-- Q6: QLHieuXe, Q7: QLTienCong, Q8: QLTho, Q9: QLNhaCungCap, Q10: QLVatTu, Q11: QLTaiKhoan, Q12: QLPhanQuyen, Q13: TrangThaiXuongXe
INSERT INTO ChiTietPhanQuyen (MaPhanQuyen, MaChucNang) VALUES
('GIAMDOC', 'Q1'), ('GIAMDOC', 'Q2'), ('GIAMDOC', 'Q3'), ('GIAMDOC', 'Q4'),
('GIAMDOC', 'Q5'), ('GIAMDOC', 'Q6'), ('GIAMDOC', 'Q7'), ('GIAMDOC', 'Q8'),
('GIAMDOC', 'Q9'), ('GIAMDOC', 'Q10'), ('GIAMDOC', 'Q11'), ('GIAMDOC', 'Q12'), ('GIAMDOC', 'Q13'),
('NVTIEPNHAN', 'Q1'), ('NVTIEPNHAN', 'Q13'),
('THOSUACHUA', 'Q2'),
('KETOAN', 'Q3'), ('KETOAN', 'Q4'),
('NVKHO', 'Q9'), ('NVKHO', 'Q10');
GO

-- Default password for all sample accounts is '123'
-- MD5 hash for '123': 202cb962ac59075b964b07152d234b70
INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, MaPhanQuyen, HoTen, TrangThai) VALUES
('admin', '202cb962ac59075b964b07152d234b70', 'GIAMDOC', N'Admin Gara', 1),
('tiepn001', '202cb962ac59075b964b07152d234b70', 'NVTIEPNHAN', N'Nguyễn Tiếp Nhận', 1),
('tiepn002', '202cb962ac59075b964b07152d234b70', 'NVTIEPNHAN', N'Trần Tiếp Nhận', 1),
('suachu001', '202cb962ac59075b964b07152d234b70', 'THOSUACHUA', N'Võ Sửa Chữa', 1),
('suachu002', '202cb962ac59075b964b07152d234b70', 'THOSUACHUA', N'Lê Sửa Chữa', 1),
('kho001', '202cb962ac59075b964b07152d234b70', 'NVKHO', N'Phạm Kho', 1),
('kho002', '202cb962ac59075b964b07152d234b70', 'NVKHO', N'Huỳnh Kho', 1),
('ketoan001', '202cb962ac59075b964b07152d234b70', 'KETOAN', N'Lê Kế Toán', 1),
('ketoan002', '202cb962ac59075b964b07152d234b70', 'KETOAN', N'Trịnh Kế Toán', 1),
('quanly001', '202cb962ac59075b964b07152d234b70', 'GIAMDOC', N'Nguyễn Quản Lý', 1);
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
('Hyundai'), ('VinFast'), ('Mazda'), ('Chevrolet'), ('Mitsubishi');
GO

INSERT INTO ChuXe (TenChuXe, DiaChi, DienThoai, Email) VALUES
(N'Nguyễn Văn A', N'123 Đường ABC, Quận 1', '0901234567', 'nguyenvana@example.com'),
(N'Trần Thị B', N'456 Đường XYZ, Quận 2', '0912345678', 'tranb@example.com'),
(N'Lê Văn C', N'789 Đường DEF, Quận 3', '0987654321', 'lec@example.com'),
(N'Phạm Minh D', N'101 Đường LMN, Quận 4', '0934567890', 'phammd@example.com'),
(N'Đinh Thị E', N'202 Đường OPQ, Quận 5', '0943219876', 'dinhthe@example.com'),
(N'Võ Thành F', N'303 Đường RST, Quận 6', '0956784321', 'vothanhf@example.com'),
(N'Phan Thị G', N'404 Đường UVW, Quận 7', '0967896543', 'phanthig@example.com'),
(N'Lưu Văn H', N'505 Đường XYZ, Quận 8', '0978945612', 'luuvanh@example.com'),
(N'Ngô Thị I', N'606 Đường ABC, Quận 9', '0983214567', 'ngothii@example.com'),
(N'Hồ Văn J', N'707 Đường DEF, Quận 10', '0998761234', 'hovanj@example.com');

GO

INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe) VALUES
('51A-123.45', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Toyota'), 1),
('51B-678.90', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Honda'), 2),
('51C-111.22', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Ford'), 3),
('52A-987.65', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'BMW'), 4),
('52B-555.55', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Hyundai'), 5),
('53C-333.33', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Mazda'), 6),
('53A-777.77', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'VinFast'), 7),
('54B-888.88', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Chevrolet'), 8),
('54A-222.22', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Mercedes-Benz'), 9),
('55C-666.66', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Mitsubishi'), 10);
GO

INSERT INTO VatTu (TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu)
VALUES
(N'Lốp Michelin 205/55R16', 1800000, 25, N'Cái', 5),
(N'Lốp Bridgestone 195/65R15', 1500000, 30, N'Cái', 5),
(N'Dầu nhớt Castrol GTX 4L', 450000, 50, N'Chai', 10),
(N'Dầu nhớt Mobil 1 5W30 4L', 620000, 40, N'Chai', 10),
(N'Lọc dầu động cơ Toyota', 120000, 60, N'Cái', 15),
(N'Lọc gió động cơ Honda', 180000, 45, N'Cái', 15),
(N'Lọc điều hòa xe Kia', 150000, 40, N'Cái', 10),
(N'Bugi NGK Laser Iridium', 320000, 55, N'Cái', 15),
(N'Bugi Denso Iridium', 290000, 50, N'Cái', 15),
(N'Ắc quy GS 12V-45Ah', 1400000, 20, N'Bình', 5),
(N'Ắc quy Đồng Nai 12V-70Ah', 2300000, 15, N'Bình', 5),
(N'Đèn pha LED VinFast Fadil', 1200000, 10, N'Cái', 3),
(N'Đèn hậu Ford Ranger', 1400000, 10, N'Cái', 3),
(N'Bộ má phanh trước Hyundai Accent', 850000, 25, N'Bộ', 5),
(N'Đĩa phanh trước Kia Morning', 780000, 20, N'Cái', 5),
(N'Gioăng nắp máy Mazda CX-5', 380000, 18, N'Bộ', 5),
(N'Dây curoa tổng Toyota Vios', 420000, 22, N'Sợi', 5),
(N'Bơm xăng Hyundai i10', 1650000, 12, N'Bộ', 3),
(N'Gạt mưa Bosch Aerotwin 20-24 inch', 480000, 35, N'Bộ', 10),
(N'Dây phanh tay Honda City', 620000, 20, N'Bộ', 5);

DECLARE @i INT = 21;
WHILE @i <= 200
BEGIN
    INSERT INTO VatTu (TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu)
    VALUES (N'Phụ tùng thực tế số ' + CAST(@i AS NVARCHAR), 100000 + (@i * 4000), 10 + (@i % 20), N'Cái', 5);
    SET @i = @i + 1;
END;

INSERT INTO NhaCungCap (TenNhaCungCap, DienThoai, DiaChi, Email) VALUES
(N'Công ty TNHH Phụ tùng ô tô Thành Đạt', '02866882416', N'Quận 10, TP.HCM', 'thanhdatphutung@gmail.com'),
(N'Công ty TNHH MTV Phụ Tùng Ô Tô Tín Nghĩa', '02438512345', N'Cầu Giấy, Hà Nội', 'tinnghia.auto@gmail.com'),
(N'Công ty Cổ Phần Dịch Vụ Phụ Tùng Ô Tô Hà Thành', '02439998877', N'Nam Từ Liêm, Hà Nội', 'hathanh.parts@gmail.com'),
(N'Công ty TNHH Phụ Tùng Ô Tô Hưng Thịnh', '02839565533', N'Quận 5, TP.HCM', 'hungthinh.parts@gmail.com'),
(N'Công ty TNHH Đại Phát Auto', '02253889911', N'Lê Chân, Hải Phòng', 'daiphat.auto@gmail.com'),
(N'Auto Minh Hòa', '02437654321', N'Thanh Xuân, Hà Nội', 'minhhoa.autoparts@gmail.com'),
(N'Công ty TNHH TM DV Phụ Tùng Ô Tô Thành Công', '02838776655', N'Tân Bình, TP.HCM', 'thanhtcong.parts@gmail.com'),
(N'Công ty TNHH TM DV An Phát Auto', '02839887766', N'Bình Thạnh, TP.HCM', 'anphatauto@gmail.com'),
(N'Công ty Cổ phần Phụ Tùng Ô Tô Trường Hải', '02513895555', N'Thủ Đức, TP.HCM', 'truonghai.parts@gmail.com'),
(N'Công ty TNHH Huy Hoàng Auto', '02439997755', N'Hoàng Mai, Hà Nội', 'huyhoang.auto@gmail.com'),
(N'Công ty TNHH Phụ Tùng Ô Tô Hoàng Gia', '02839990066', N'Bình Tân, TP.HCM', 'hoanggia.autoparts@gmail.com'),
(N'Công ty TNHH Auto Sài Gòn Phát', '02838552233', N'Quận 11, TP.HCM', 'saigonphat.auto@gmail.com'),
(N'Công ty TNHH Auto Hồng Phát', '02437778899', N'Long Biên, Hà Nội', 'hongphat.autoparts@gmail.com'),
(N'Phụ Tùng Ô Tô Hải Phát', '02253880077', N'Lê Chân, Hải Phòng', 'haiphat.autoparts@gmail.com'),
(N'Công ty TNHH Ô Tô Hưng Phát', '02438885511', N'Ba Đình, Hà Nội', 'hungphat.autoparts@gmail.com'),
(N'Công ty TNHH TM DV Phụ Tùng Ô Tô Gia Minh', '02835556677', N'Quận 7, TP.HCM', 'giaminh.parts@gmail.com'),
(N'Công ty TNHH Auto Nam Việt', '02438667755', N'Hà Đông, Hà Nội', 'namviet.autoparts@gmail.com'),
(N'Công ty TNHH Linh Kiện Ô Tô Đại Hưng', '02838889977', N'Tân Phú, TP.HCM', 'daihung.parts@gmail.com'),
(N'Công ty TNHH Phụ Tùng Ô Tô Việt Nhật', '02435557733', N'Cầu Giấy, Hà Nội', 'vietnhat.parts@gmail.com'),
(N'Công ty TNHH TM DV Ô Tô Kim Long', '02838883311', N'Gò Vấp, TP.HCM', 'kimlong.autoparts@gmail.com');
GO

INSERT INTO TienCong (NoiDung, DonGia) VALUES
(N'Thay dầu nhớt động cơ', 150000),
(N'Thay lọc dầu động cơ', 80000),
(N'Thay lọc gió động cơ', 100000),
(N'Thay lọc điều hòa', 90000),
(N'Thay bugi', 120000),
(N'Cân chỉnh góc đặt bánh xe', 250000),
(N'Thay lốp xe', 100000),
(N'Thay má phanh trước', 150000),
(N'Thay má phanh sau', 150000),
(N'Thay đĩa phanh trước', 180000),
(N'Thay đĩa phanh sau', 180000),
(N'Thay dây curoa tổng', 250000),
(N'Thay bơm nước làm mát', 300000),
(N'Thay gioăng nắp máy', 200000),
(N'Thay ắc quy', 120000),
(N'Kiểm tra tổng quát ô tô', 300000),
(N'Sửa chữa điều hòa ô tô', 400000),
(N'Xả gas và nạp gas điều hòa', 350000),
(N'Thay đèn pha', 150000),
(N'Thay đèn hậu', 120000);

-- Thêm 80 tiền công tự động
DECLARE @j INT = 21;
WHILE @j <= 100
BEGIN
    INSERT INTO TienCong (NoiDung, DonGia)
    VALUES (N'Dịch vụ sửa chữa số ' + CAST(@j AS NVARCHAR), 100000 + (@j * 15000));
    SET @j = @j + 1;
END;

INSERT INTO Tho (TenTho, SoDienThoai, ChuyenMon) VALUES
(N'Nguyễn Văn An', '0900111001', N'Gầm - Máy'),
(N'Trần Văn Bình', '0900111002', N'Điện - Điện lạnh'),
(N'Phạm Văn Cường', '0900111003', N'Bảo trì nhanh'),
(N'Lê Thị Dung', '0900111004', N'Gầm - Treo'),
(N'Võ Văn Em', '0900111005', N'Máy gầm tổng hợp'),
(N'Phan Thị Giang', '0900111006', N'Điện - Đèn - Camera'),
(N'Nguyễn Quốc Hoàng', '0900111007', N'Sơn - Dặm tút'),
(N'Ngô Văn Hải', '0900111008', N'Điều hòa'),
(N'Bùi Thị Kim', '0900111009', N'Bảo trì nhanh'),
(N'Đinh Thị Lan', '0900111010', N'Động cơ điện'),
(N'Phạm Quang Minh', '0900111011', N'Điện khởi động'),
(N'Lưu Thanh Nam', '0900111012', N'Khung gầm'),
(N'Hoàng Thị Oanh', '0900111013', N'Nội thất ô tô'),
(N'Đặng Văn Phúc', '0900111014', N'Điện - Sửa điều hòa'),
(N'Nguyễn Thị Quỳnh', '0900111015', N'Đồng - Sơn nhanh'),
(N'Vũ Văn Sơn', '0900111016', N'Điện lạnh xe hơi'),
(N'Huỳnh Thị Trang', '0900111017', N'Điện thân xe'),
(N'Phan Văn Út', '0900111018', N'Động cơ diesel'),
(N'Đặng Văn Vũ', '0900111019', N'Động cơ xăng'),
(N'Nguyễn Thị Yến', '0900111020', N'Sửa chữa tổng hợp');
GO

INSERT INTO TiepNhan (BienSo, NgayTiepNhan, TinhTrangXe, TrangThai, TongTienNo, TrangThaiHoanTat) VALUES
('51A-123.45', GETDATE(), N'Trầy xước nhẹ cản trước', N'Chờ sửa', 0.00, 0),
('51B-678.90', GETDATE(), N'Bể đèn hậu', N'Đang sửa', 0.00, 0),
('51C-111.22', GETDATE(), N'Thay lốp xe', N'Chờ sửa', 0.00, 0),
('52A-987.65', GETDATE(), N'Thay dầu định kỳ', N'Đang sửa', 0.00, 0),
('52B-555.55', GETDATE(), N'Lỗi điện nhỏ', N'Chờ sửa', 0.00, 0),
('53C-333.33', GETDATE(), N'Vệ sinh nội thất', N'Chờ sửa', 0.00, 0),
('53A-777.77', GETDATE(), N'Kiểm tra phanh', N'Đang sửa', 0.00, 0),
('54B-888.88', GETDATE(), N'Bảo trì 10000km', N'Chờ sửa', 0.00, 0),
('54A-222.22', GETDATE(), N'Thay bugi', N'Chờ sửa', 0.00, 0),
('55C-666.66', GETDATE(), N'Thay bình ắc quy', N'Đang sửa', 0.00, 0);
GO

PRINT 'Database script completed successfully.';
GO