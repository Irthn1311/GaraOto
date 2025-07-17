-- Tạo database chính
CREATE DATABASE GaraOtoManagementDB;
GO

-- Sử dụng database
USE GaraOtoManagementDB;
GO

-- Xóa các bảng nếu chúng đã tồn tại để tránh lỗi khi chạy lại script
-- Lưu ý thứ tự xóa bảng để tránh lỗi khóa ngoại (từ bảng con đến bảng cha)
IF OBJECT_ID('dbo.ChiTietPhieuSuaChua_VatTu', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhieuSuaChua_VatTu; -- New: Drop new table
IF OBJECT_ID('dbo.ChiTietPhieuSuaChua_TienCong', 'U') IS NOT NULL DROP TABLE dbo.ChiTietPhieuSuaChua_TienCong; -- New: Drop new table
IF OBJECT_ID('dbo.ChiTietSuaChua', 'U') IS NOT NULL DROP TABLE dbo.ChiTietSuaChua; -- Old: Drop old table
IF OBJECT_ID('dbo.PhieuSuaChua', 'U') IS NOT NULL DROP TABLE dbo.PhieuSuaChua;
IF OBJECT_ID('dbo.PhieuThuTien', 'U') IS NOT NULL DROP TABLE dbo.PhieuThuTien;
IF OBJECT_ID('dbo.TiepNhan', 'U') IS NOT NULL DROP TABLE dbo.TiepNhan;
IF OBJECT_ID('dbo.Xe', 'U') IS NOT NULL DROP TABLE dbo.Xe;
IF OBJECT_ID('dbo.HieuXe', 'U') IS NOT NULL DROP TABLE dbo.HieuXe;
IF OBJECT_ID('dbo.ChuXe', 'U') IS NOT NULL DROP TABLE dbo.ChuXe;
IF OBJECT_ID('dbo.VatTu', 'U') IS NOT NULL DROP TABLE dbo.VatTu;
IF OBJECT_ID('dbo.TienCong', 'U') IS NOT NULL DROP TABLE dbo.TienCong;
IF OBJECT_ID('dbo.ThamSo', 'U') IS NOT NULL DROP TABLE dbo.ThamSo;
IF OBJECT_ID('dbo.TaiKhoanNguoiDung', 'U') IS NOT NULL DROP TABLE dbo.TaiKhoanNguoiDung;
IF OBJECT_ID('dbo.Tho', 'U') IS NOT NULL DROP TABLE dbo.Tho;
IF OBJECT_ID('dbo.PhanCongTho', 'U') IS NOT NULL DROP TABLE dbo.PhanCongTho; -- New: Drop PhanCongTho table if exists
GO

-- Bảng HieuXe (Car Brands)
CREATE TABLE HieuXe (
                        MaHieuXe INT IDENTITY(1,1) PRIMARY KEY,
                        TenHieuXe NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- Bảng ChuXe (Car Owners)
CREATE TABLE ChuXe (
                       MaChuXe INT IDENTITY(1,1) PRIMARY KEY,
                       TenChuXe NVARCHAR(100) NOT NULL,
                       DiaChi NVARCHAR(200),
                       DienThoai NVARCHAR(20),
                       Email NVARCHAR(100)
);
GO

-- Bảng Xe (Cars)
CREATE TABLE Xe (
                    BienSo NVARCHAR(20) PRIMARY KEY,
                    MaHieuXe INT NOT NULL FOREIGN KEY REFERENCES HieuXe(MaHieuXe),
                    MaChuXe INT NOT NULL FOREIGN KEY REFERENCES ChuXe(MaChuXe),
                    DoiXe INT,
                    MauSac NVARCHAR(50),
                    SoKMHienTai INT
);
GO

-- Bảng TiepNhan (Vehicle Acceptance Records)
CREATE TABLE TiepNhan (
                          MaTiepNhan INT IDENTITY(1,1) PRIMARY KEY,
                          BienSo NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES Xe(BienSo),
                          NgayTiepNhan DATE NOT NULL,
                          TongTienNo DECIMAL(18,2) DEFAULT 0.00, -- Total outstanding amount for this acceptance record
                          TrangThaiHoanTat BIT DEFAULT 0 -- 0=In Progress, 1=Completed (fully paid)
);
GO

-- Bảng VatTu (Parts and Materials)
CREATE TABLE VatTu (
                       MaVatTu INT IDENTITY(1,1) PRIMARY KEY,
                       TenVatTu NVARCHAR(100) NOT NULL UNIQUE,
                       DonGiaBan DECIMAL(18,2) NOT NULL CHECK (DonGiaBan >= 0),
                       SoLuongTon INT NOT NULL DEFAULT 0 CHECK (SoLuongTon >= 0),
                       DonViTinh NVARCHAR(50),
                       MucTonKhoToiThieu INT DEFAULT 0 CHECK (MucTonKhoToiThieu >= 0)
);
GO

-- Bảng TienCong (Labor Services)
CREATE TABLE TienCong (
                          MaTienCong INT IDENTITY(1,1) PRIMARY KEY,
                          NoiDung NVARCHAR(100) NOT NULL UNIQUE,
                          DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0)
);
GO

-- Bảng Tho (Mechanics)
CREATE TABLE Tho (
                     MaTho INT IDENTITY(1,1) PRIMARY KEY,
                     TenTho NVARCHAR(100) NOT NULL,
                     SoDienThoai NVARCHAR(20),
                     ChuyenMon NVARCHAR(100)
);
GO

-- Bảng PhieuSuaChua (Repair Slips for a specific acceptance)
CREATE TABLE PhieuSuaChua (
                              MaPhieuSC INT IDENTITY(1,1) PRIMARY KEY,
                              MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan),
                              NgaySuaChua DATE NOT NULL,
                              GhiChu NVARCHAR(255),
                              TongTien DECIMAL(18,2) NOT NULL DEFAULT 0.00 CHECK (TongTien >= 0),
                              MaTho INT NULL FOREIGN KEY REFERENCES Tho(MaTho), -- Mechanic assigned to this repair slip
                              TrangThaiHoanTat BIT DEFAULT 0 -- Status for this specific repair slip (e.g., 0=In Progress, 1=Completed)
);
GO

-- NEW TABLE: ChiTietPhieuSuaChua_VatTu (Details of parts in a repair slip)
CREATE TABLE ChiTietPhieuSuaChua_VatTu (
                                           MaChiTietVatTu INT IDENTITY(1,1) PRIMARY KEY,
                                           MaPhieuSC INT NOT NULL FOREIGN KEY REFERENCES PhieuSuaChua(MaPhieuSC),
                                           MaVatTu INT NOT NULL FOREIGN KEY REFERENCES VatTu(MaVatTu),
                                           SoLuong INT NOT NULL CHECK (SoLuong > 0),
                                           DonGiaNhap DECIMAL(18,2) NOT NULL CHECK (DonGiaNhap >= 0), -- Price at the time of repair
                                           ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0)
);
GO

-- NEW TABLE: ChiTietPhieuSuaChua_TienCong (Details of labor in a repair slip)
CREATE TABLE ChiTietPhieuSuaChua_TienCong (
                                              MaChiTietTienCong INT IDENTITY(1,1) PRIMARY KEY,
                                              MaPhieuSC INT NOT NULL FOREIGN KEY REFERENCES PhieuSuaChua(MaPhieuSC),
                                              MaTienCong INT NOT NULL FOREIGN KEY REFERENCES TienCong(MaTienCong),
                                              DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0), -- Labor rate at the time of repair
                                              ThanhTien DECIMAL(18,2) NOT NULL CHECK (ThanhTien >= 0)
);
GO

-- Bảng PhieuThuTien (Payment Receipts for a specific acceptance)
CREATE TABLE PhieuThuTien (
                              MaPhieuThu INT IDENTITY(1,1) PRIMARY KEY,
                              MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan),
                              NgayThu DATE NOT NULL,
                              SoTienThu DECIMAL(18,2) NOT NULL CHECK (SoTienThu > 0)
);
GO

-- Bảng ThamSo (System Parameters)
CREATE TABLE ThamSo (
                        TenThamSo NVARCHAR(100) PRIMARY KEY,
                        GiaTri INT NOT NULL
);
GO

-- Bảng TaiKhoanNguoiDung (User Accounts for staff/management)
CREATE TABLE TaiKhoanNguoiDung (
                                   MaTK INT IDENTITY(1,1) PRIMARY KEY,
                                   TenDangNhap VARCHAR(50) NOT NULL UNIQUE,
                                   MatKhauHash VARCHAR(255) NOT NULL,
                                   LoaiTaiKhoan VARCHAR(20) NOT NULL CHECK (LoaiTaiKhoan IN ('GiamDoc', 'QuanLy', 'NhanVienTiepNhan', 'ThoSuaChua', 'NhanVienKho', 'KeToan')),
                                   HoTen NVARCHAR(100),
                                   TrangThai BIT DEFAULT 1 -- New column for account status
);
GO

-- Initial default values for ThamSo
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoXeToiDaMoiNgay', 30);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoHieuXeToiDa', 10);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoLoaiVatTuToiDa', 200);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoLoaiTienCongToiDa', 100);
GO

-- Sample data for HieuXe
INSERT INTO HieuXe (TenHieuXe) VALUES
('Toyota'), ('Honda'), ('Ford'), ('BMW'), ('Mercedes-Benz'),
('Hyundai'), ('Kia'), ('Mazda'), ('Chevrolet'), ('Nissan');
GO

-- Sample data for ChuXe
INSERT INTO ChuXe (TenChuXe, DiaChi, DienThoai, Email) VALUES
(N'Nguyễn Văn A', N'123 Đường ABC, Quận 1', '0901234567', 'nguyenvana@example.com'),
(N'Trần Thị B', N'456 Đường XYZ, Quận 2', '0912345678', 'tranb@example.com'),
(N'Lê Văn C', N'789 Đường DEF, Quận 3', '0987654321', 'lec@example.com');
GO

-- Sample data for Xe (requires ChuXe and HieuXe to exist first)
INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe) VALUES
('51A-123.45', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Toyota'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Nguyễn Văn A')),
('51B-678.90', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Honda'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Trần Thị B')),
('51C-111.22', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Ford'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Lê Văn C'));
GO

-- Sample data for VatTu
INSERT INTO VatTu (TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu) VALUES
(N'Lốp xe Michelin 205/55R16', 1500000.00, 50, N'cái', 10),
(N'Dầu nhớt Castrol GTX 4L', 450000.00, 100, N'chai', 20),
(N'Lọc dầu động cơ', 120000.00, 80, N'cái', 15),
(N'Bugia Denso', 80000.00, 120, N'cái', 30);
GO

-- Sample data for TienCong
INSERT INTO TienCong (NoiDung, DonGia) VALUES
(N'Kiểm tra tổng quát', 200000.00),
(N'Thay dầu nhớt', 150000.00),
(N'Thay lốp xe', 100000.00),
(N'Cân chỉnh thước lái', 300000.00);
GO

-- Sample data for Tho
INSERT INTO Tho (TenTho, SoDienThoai, ChuyenMon) VALUES
(N'Nguyễn Văn Thợ', '0900111222', N'Động cơ'),
(N'Phạm Thị Sửa', '0900333444', N'Điện - Điện lạnh'),
(N'Lê Minh Kỹ', '0900555666', N'Gầm - Lốp');
GO

-- Sample data for TaiKhoanNguoiDung
INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, LoaiTaiKhoan, HoTen) VALUES
('giamdoc', 'e10adc3949ba59abbe56e057f20f883e', 'GiamDoc', N'Trần Văn Giám Đốc'), -- password123
('quanly', 'e10adc3949ba59abbe56e057f20f883e', 'QuanLy', N'Lý Thị Quản Lý'),     -- password123
('tiepnhan', 'e10adc3949ba59abbe56e057f20f883e', 'NhanVienTiepNhan', N'Hoàng Văn Tiếp Nhận'), -- password123
('thosuachua', 'e10adc3949ba59abbe56e057f20f883e', 'ThoSuaChua', N'Nguyễn Văn Thợ A'), -- password123
('nhanvienkho', 'e10adc3949ba59abbe56e057f20f883e', 'NhanVienKho', N'Phạm Thị Kho'), -- password123
('ketoan', 'e10adc3949ba59abbe56e057f20f883e', 'KeToan', N'Đỗ Thị Kế Toán'); -- password123
GO

-- Sample data for TiepNhan (for testing)
INSERT INTO TiepNhan (BienSo, NgayTiepNhan, TongTienNo, TrangThaiHoanTat) VALUES
('51A-123.45', GETDATE(), 0.00, 0), -- Xe A được tiếp nhận hôm nay
('51B-678.90', DATEADD(day, -5, GETDATE()), 0.00, 0); -- Xe B được tiếp nhận 5 ngày trước
GO
