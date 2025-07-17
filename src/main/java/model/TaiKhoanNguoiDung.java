package model;

import javafx.beans.property.BooleanProperty; // Import for BooleanProperty
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty; // Import for SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaiKhoanNguoiDung {
    private IntegerProperty maTK;
    private StringProperty tenDangNhap;
    private StringProperty matKhauHash; // Store hashed password
    private StringProperty loaiTaiKhoan; // "NhanVien" or "QuanLy"
    private StringProperty hoTen;
    private BooleanProperty trangThai; // New: Status of the account (active/inactive)

    public TaiKhoanNguoiDung() {
        this.maTK = new SimpleIntegerProperty();
        this.tenDangNhap = new SimpleStringProperty();
        this.matKhauHash = new SimpleStringProperty();
        this.loaiTaiKhoan = new SimpleStringProperty();
        this.hoTen = new SimpleStringProperty();
        this.trangThai = new SimpleBooleanProperty(true); // Default to active
    }

    public TaiKhoanNguoiDung(int maTK, String tenDangNhap, String matKhauHash, String loaiTaiKhoan, String hoTen, boolean trangThai) {
        this.maTK = new SimpleIntegerProperty(maTK);
        this.tenDangNhap = new SimpleStringProperty(tenDangNhap);
        this.matKhauHash = new SimpleStringProperty(matKhauHash);
        this.loaiTaiKhoan = new SimpleStringProperty(loaiTaiKhoan);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.trangThai = new SimpleBooleanProperty(trangThai);
    }

    // Getters
    public int getMaTK() { return maTK.get(); }
    public String getTenDangNhap() { return tenDangNhap.get(); }
    public String getMatKhauHash() { return matKhauHash.get(); }
    public String getLoaiTaiKhoan() { return loaiTaiKhoan.get(); }
    public String getHoTen() { return hoTen.get(); }
    public boolean isTrangThai() { return trangThai.get(); } // New Getter

    // Setters
    public void setMaTK(int maTK) { this.maTK.set(maTK); }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap.set(tenDangNhap); }
    public void setMatKhauHash(String matKhauHash) { this.matKhauHash.set(matKhauHash); }
    public void setLoaiTaiKhoan(String loaiTaiKhoan) { this.loaiTaiKhoan.set(loaiTaiKhoan); }
    public void setHoTen(String hoTen) { this.hoTen.set(hoTen); }
    public void setTrangThai(boolean trangThai) { this.trangThai.set(trangThai); } // New Setter

    // Properties for JavaFX binding
    public IntegerProperty maTKProperty() { return maTK; }
    public StringProperty tenDangNhapProperty() { return tenDangNhap; }
    public StringProperty matKhauHashProperty() { return matKhauHash; }
    public StringProperty loaiTaiKhoanProperty() { return loaiTaiKhoan; }
    public StringProperty hoTenProperty() { return hoTen; }
    public BooleanProperty trangThaiProperty() { return trangThai; } // New Property
}
