package model;

import javafx.beans.property.BooleanProperty; // Import for BooleanProperty
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty; // Import for SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TaiKhoanNguoiDung {
    private IntegerProperty maTK;
    private StringProperty tenDangNhap;
    private StringProperty matKhauHash; // Store hashed password
    private StringProperty maPhanQuyen; // Thay thế cho loaiTaiKhoan
    private StringProperty hoTen;
    private BooleanProperty trangThai; // New: Status of the account (active/inactive)

    public TaiKhoanNguoiDung() {
        this.maTK = new SimpleIntegerProperty();
        this.tenDangNhap = new SimpleStringProperty();
        this.matKhauHash = new SimpleStringProperty();
        this.maPhanQuyen = new SimpleStringProperty(); // Thay thế
        this.hoTen = new SimpleStringProperty();
        this.trangThai = new SimpleBooleanProperty(true); // Default to active
    }

    public TaiKhoanNguoiDung(int maTK, String tenDangNhap, String matKhauHash, String maPhanQuyen, String hoTen, boolean trangThai) {
        this.maTK = new SimpleIntegerProperty(maTK);
        this.tenDangNhap = new SimpleStringProperty(tenDangNhap);
        this.matKhauHash = new SimpleStringProperty(matKhauHash);
        this.maPhanQuyen = new SimpleStringProperty(maPhanQuyen); // Thay thế
        this.hoTen = new SimpleStringProperty(hoTen);
        this.trangThai = new SimpleBooleanProperty(trangThai);
    }

    // Getters
    public int getMaTK() { return maTK.get(); }
    public String getTenDangNhap() { return tenDangNhap.get(); }
    public String getMatKhauHash() { return matKhauHash.get(); }
    public String getMaPhanQuyen() { return maPhanQuyen.get(); } // Thay thế
    public String getHoTen() { return hoTen.get(); }
    public boolean isTrangThai() { return trangThai.get(); } // New Getter

    // Setters
    public void setMaTK(int maTK) { this.maTK.set(maTK); }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap.set(tenDangNhap); }
    public void setMatKhauHash(String matKhauHash) { this.matKhauHash.set(matKhauHash); }
    public void setMaPhanQuyen(String maPhanQuyen) { this.maPhanQuyen.set(maPhanQuyen); } // Thay thế
    public void setHoTen(String hoTen) { this.hoTen.set(hoTen); }
    public void setTrangThai(boolean trangThai) { this.trangThai.set(trangThai); } // New Setter

    // Properties for JavaFX binding
    public IntegerProperty maTKProperty() { return maTK; }
    public StringProperty tenDangNhapProperty() { return tenDangNhap; }
    public StringProperty matKhauHashProperty() { return matKhauHash; }
    public StringProperty maPhanQuyenProperty() { return maPhanQuyen; } // Thay thế
    public StringProperty hoTenProperty() { return hoTen; }
    public BooleanProperty trangThaiProperty() { return trangThai; } // New Property
}
