package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class PhieuNhapKhoVatTu {
    private IntegerProperty maPhieuNhap;
    private ObjectProperty<LocalDate> ngayNhap;
    private IntegerProperty maNhaCungCap;
    private DoubleProperty tongTienNhap;

    // Derived property for UI display (from NhaCungCap table)
    private StringProperty tenNhaCungCap;

    public PhieuNhapKhoVatTu() {
        this.maPhieuNhap = new SimpleIntegerProperty();
        this.ngayNhap = new SimpleObjectProperty<>();
        this.maNhaCungCap = new SimpleIntegerProperty();
        this.tongTienNhap = new SimpleDoubleProperty();
        this.tenNhaCungCap = new SimpleStringProperty(); // Initialize derived property
    }

    public PhieuNhapKhoVatTu(int maPhieuNhap, LocalDate ngayNhap, int maNhaCungCap, double tongTienNhap) {
        this.maPhieuNhap = new SimpleIntegerProperty(maPhieuNhap);
        this.ngayNhap = new SimpleObjectProperty<>(ngayNhap);
        this.maNhaCungCap = new SimpleIntegerProperty(maNhaCungCap);
        this.tongTienNhap = new SimpleDoubleProperty(tongTienNhap);
        this.tenNhaCungCap = new SimpleStringProperty(); // Initialize derived property
    }

    // Getters
    public int getMaPhieuNhap() { return maPhieuNhap.get(); }
    public LocalDate getNgayNhap() { return ngayNhap.get(); }
    public int getMaNhaCungCap() { return maNhaCungCap.get(); }
    public double getTongTienNhap() { return tongTienNhap.get(); }
    public String getTenNhaCungCap() { return tenNhaCungCap.get(); }

    // Setters
    public void setMaPhieuNhap(int maPhieuNhap) { this.maPhieuNhap.set(maPhieuNhap); }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap.set(ngayNhap); }
    public void setMaNhaCungCap(int maNhaCungCap) { this.maNhaCungCap.set(maNhaCungCap); }
    public void setTongTienNhap(double tongTienNhap) { this.tongTienNhap.set(tongTienNhap); }
    public void setTenNhaCungCap(String tenNhaCungCap) { this.tenNhaCungCap.set(tenNhaCungCap); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuNhapProperty() { return maPhieuNhap; }
    public ObjectProperty<LocalDate> ngayNhapProperty() { return ngayNhap; }
    public IntegerProperty maNhaCungCapProperty() { return maNhaCungCap; }
    public DoubleProperty tongTienNhapProperty() { return tongTienNhap; }
    public StringProperty tenNhaCungCapProperty() { return tenNhaCungCap; }
}
