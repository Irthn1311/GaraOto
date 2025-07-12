-- Tạo database chính
CREATE DATABASE GaraOtoDB;
GO

-- Sử dụng database
USE GaraOtoDB;
GO

-- Xóa các bảng nếu chúng đã tồn tại để tránh lỗi khi chạy lại script
-- Lưu ý thứ tự xóa bảng để tránh lỗi khóa ngoại
IF OBJECT_ID('dbo.ChiTietSuaChua', 'U') IS NOT NULL DROP TABLE dbo.ChiTietSuaChua;
IF OBJECT_ID('dbo.PhieuSuaChua', 'U') IS NOT NULL DROP TABLE dbo.PhieuSuaChua;
IF OBJECT_ID('dbo.PhieuThuTien', 'U') IS NOT NULL DROP TABLE dbo.PhieuThuTien;
IF OBJECT_ID('dbo.TiepNhan', 'U') IS NOT NULL DROP TABLE dbo.TiepNhan;
IF OBJECT_ID('dbo.Xe', 'U') IS NOT NULL DROP TABLE dbo.Xe;
IF OBJECT_ID('dbo.HieuXe', 'U') IS NOT NULL DROP TABLE dbo.HieuXe;
IF OBJECT_ID('dbo.ChuXe', 'U') IS NOT NULL DROP TABLE dbo.ChuXe;
IF OBJECT_ID('dbo.VatTu', 'U') IS NOT NULL DROP TABLE dbo.VatTu;
IF OBJECT_ID('dbo.TienCong', 'U') IS NOT NULL DROP TABLE dbo.TienCong;
IF OBJECT_ID('dbo.ThamSo', 'U') IS NOT NULL DROP TABLE dbo.ThamSo;
IF OBJECT_ID('dbo.TaiKhoanNguoiDung', 'U') IS NOT NULL DROP TABLE dbo.TaiKhoanNguoiDung; -- Thêm bảng này nếu bạn có

GO

-- Bảng HieuXe
CREATE TABLE HieuXe (
                        MaHieuXe INT IDENTITY(1,1) PRIMARY KEY,
                        TenHieuXe NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- Bảng ChuXe
CREATE TABLE ChuXe (
                       MaChuXe INT IDENTITY(1,1) PRIMARY KEY,
                       TenChuXe NVARCHAR(100) NOT NULL,
                       DiaChi NVARCHAR(200),
                       DienThoai NVARCHAR(20),
                       Email NVARCHAR(100)
);
GO

-- Bảng Xe
CREATE TABLE Xe (
                    BienSo NVARCHAR(20) PRIMARY KEY,
                    MaHieuXe INT NOT NULL FOREIGN KEY REFERENCES HieuXe(MaHieuXe),
                    MaChuXe INT NOT NULL FOREIGN KEY REFERENCES ChuXe(MaChuXe)
);
GO

-- Bảng TiepNhan (Hồ sơ tiếp nhận xe vào gara)
CREATE TABLE TiepNhan (
                          MaTiepNhan INT IDENTITY(1,1) PRIMARY KEY,
                          BienSo NVARCHAR(20) NOT NULL FOREIGN KEY REFERENCES Xe(BienSo),
                          NgayTiepNhan DATE NOT NULL,
    -- Thêm trường để theo dõi tổng tiền nợ cho lần tiếp nhận này
                          TongTienNo DECIMAL(18,2) DEFAULT 0.00,
    -- Thêm trạng thái hồ sơ: 0=Đang xử lý, 1=Đã hoàn tất (đã thanh toán hết)
                          TrangThaiHoanTat BIT DEFAULT 0
);
GO

-- Bảng VatTu
CREATE TABLE VatTu (
                       MaVatTu INT IDENTITY(1,1) PRIMARY Posted,
                       TenVatTu NVARCHAR(100) NOT NULL UNIQUE,
                       DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0),
                       SoLuongTon INT NOT NULL DEFAULT 0 CHECK (SoLuongTon >= 0) -- Thêm trường tồn kho
);
GO

-- Bảng TienCong
CREATE TABLE TienCong (
                          MaTienCong INT IDENTITY(1,1) PRIMARY KEY,
                          NoiDung NVARCHAR(100) NOT NULL UNIQUE,
                          DonGia DECIMAL(18,2) NOT NULL CHECK (DonGia >= 0)
);
GO

-- Bảng PhieuSuaChua (Phiếu sửa chữa cho một lần tiếp nhận)
CREATE TABLE PhieuSuaChua (
                              MaPhieuSC INT IDENTITY(1,1) PRIMARY KEY,
                              MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan),
                              NgaySuaChua DATE NOT NULL,
                              GhiChu NVARCHAR(255),
                              TongTien DECIMAL(18,2) NOT NULL DEFAULT 0.00 CHECK (TongTien >= 0) -- Tổng tiền của phiếu sửa chữa này
);
GO

-- Bảng ChiTietSuaChua (Chi tiết vật tư và tiền công trong một phiếu sửa chữa)
CREATE TABLE ChiTietSuaChua (
                                MaChiTiet INT IDENTITY(1,1) PRIMARY KEY,
                                MaPhieuSC INT NOT NULL FOREIGN KEY REFERENCES PhieuSuaChua(MaPhieuSC),
                                MaVatTu INT NULL FOREIGN KEY REFERENCES VatTu(MaVatTu), -- Cho phép NULL
                                SoLuong INT NULL CHECK (SoLuong >= 0), -- Cho phép NULL nếu không phải vật tư
                                MaTienCong INT NULL FOREIGN KEY REFERENCES TienCong(MaTienCong), -- Cho phép NULL
                                ThanhTien DECIMAL(18,2) NOT NULL, -- Tính bằng code Java hoặc trigger
    -- Ràng buộc: phải có ít nhất MaVatTu hoặc MaTienCong
                                CONSTRAINT CK_ChiTietSuaChua_Type CHECK (MaVatTu IS NOT NULL OR MaTienCong IS NOT NULL)
);
GO

-- Bảng PhieuThuTien (Phiếu thu tiền cho một lần tiếp nhận cụ thể)
CREATE TABLE PhieuThuTien (
                              MaPhieuThu INT IDENTITY(1,1) PRIMARY KEY,
                              MaTiepNhan INT NOT NULL FOREIGN KEY REFERENCES TiepNhan(MaTiepNhan), -- Liên kết với TiepNhan
                              NgayThu DATE NOT NULL,
                              SoTienThu DECIMAL(18,2) NOT NULL CHECK (SoTienThu > 0)
    -- Email không cần thiết ở đây, đã có trong ChuXe
);
GO

-- Bảng ThamSo (Tham số hệ thống)
CREATE TABLE ThamSo (
                        TenThamSo NVARCHAR(100) PRIMARY KEY,
                        GiaTri INT NOT NULL -- Giữ kiểu INT vì các tham số hiện tại là số
);
GO

-- Bảng TaiKhoanNguoiDung (Để quản lý đăng nhập nhân viên/quản lý)
CREATE TABLE TaiKhoanNguoiDung (
                                   MaTK INT IDENTITY(1,1) PRIMARY KEY,
                                   TenDangNhap VARCHAR(50) NOT NULL UNIQUE,
                                   MatKhauHash VARCHAR(255) NOT NULL, -- Lưu mật khẩu đã hash
                                   LoaiTaiKhoan VARCHAR(20) NOT NULL CHECK (LoaiTaiKhoan IN ('NhanVien', 'QuanLy')),
                                   HoTen NVARCHAR(100)
);
GO

-- Giá trị mặc định đề xuất cho ThamSo
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoXeToiDaMoiNgay', 30);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoHieuXeToiDa', 10);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoLoaiVatTuToiDa', 200);
INSERT INTO ThamSo (TenThamSo, GiaTri) VALUES ('SoLoaiTienCongToiDa', 100);
GO

-- Dữ liệu mẫu cho HieuXe
INSERT INTO HieuXe (TenHieuXe) VALUES
('Toyota'), ('Honda'), ('Ford'), ('BMW'), ('Mercedes-Benz'),
('Hyundai'), ('Kia'), ('Mazda'), ('Chevrolet'), ('Nissan');
GO

-- Dữ liệu mẫu cho ChuXe
INSERT INTO ChuXe (TenChuXe, DiaChi, DienThoai, Email) VALUES
(N'Nguyễn Văn A', N'123 Đường ABC, Quận 1', '0901234567', 'nguyenvana@example.com'),
(N'Trần Thị B', N'456 Đường XYZ, Quận 2', '0912345678', 'tranb@example.com');
GO

-- Dữ liệu mẫu cho Xe (phải có ChuXe và HieuXe trước)
INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe) VALUES
('51A-123.45', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Toyota'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Nguyễn Văn A')),
('51B-678.90', (SELECT MaHieuXe FROM HieuXe WHERE TenHieuXe = 'Honda'), (SELECT MaChuXe FROM ChuXe WHERE TenChuXe = N'Trần Thị B'));
GO

-- Dữ liệu mẫu cho VatTu
INSERT INTO VatTu (TenVatTu, DonGia, SoLuongTon) VALUES
(N'Lốp xe Michelin 205/55R16', 1500000.00, 50),
(N'Dầu nhớt Castrol GTX 4L', 450000.00, 100),
(N'Lọc dầu động cơ', 120000.00, 80);
GO

-- Dữ liệu mẫu cho TienCong
INSERT INTO TienCong (NoiDung, DonGia) VALUES
(N'Kiểm tra tổng quát', 200000.00),
(N'Thay dầu nhớt', 150000.00),
(N'Thay lốp xe', 100000.00);
GO

-- Dữ liệu mẫu cho TaiKhoanNguoiDung
INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, LoaiTaiKhoan, HoTen) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'QuanLy', N'Nguyễn Văn Quản Lý'), -- password123
('nhanvien1', 'e10adc3949ba59abbe56e057f20f883e', 'NhanVien', N'Trần Thị Nhân Viên A');
GO
