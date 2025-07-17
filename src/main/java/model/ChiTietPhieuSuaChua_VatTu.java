package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChiTietPhieuSuaChua_VatTu {
    private IntegerProperty maPhieuSC;
    private IntegerProperty maVatTu;
    private IntegerProperty soLuong; // Renamed from soLuongSuDung
    private DoubleProperty donGiaNhap; // Renamed from donGiaBanLucDo

    // Derived properties for UI display (from VatTu table)
    private StringProperty tenVatTu;
    private StringProperty donViTinhVatTu;

    public ChiTietPhieuSuaChua_VatTu() {
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maVatTu = new SimpleIntegerProperty();
        this.soLuong = new SimpleIntegerProperty(); // Renamed
        this.donGiaNhap = new SimpleDoubleProperty(); // Renamed
        this.tenVatTu = new SimpleStringProperty();
        this.donViTinhVatTu = new SimpleStringProperty();
    }

    public ChiTietPhieuSuaChua_VatTu(int maPhieuSC, int maVatTu, int soLuong, double donGiaNhap) {
        this.maPhieuSC = new SimpleIntegerProperty(maPhieuSC);
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.soLuong = new SimpleIntegerProperty(soLuong); // Renamed
        this.donGiaNhap = new SimpleDoubleProperty(donGiaNhap); // Renamed
        this.tenVatTu = new SimpleStringProperty();
        this.donViTinhVatTu = new SimpleStringProperty();
    }

    // Getters
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaVatTu() { return maVatTu.get(); }
    public int getSoLuong() { return soLuong.get(); } // Renamed from getSoLuongSuDung
    public double getDonGiaNhap() { return donGiaNhap.get(); } // Renamed from getDonGiaBanLucDo
    public String getTenVatTu() { return tenVatTu.get(); }
    public String getDonViTinhVatTu() { return donViTinhVatTu.get(); }

    // Setters
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setSoLuong(int soLuong) { this.soLuong.set(soLuong); } // Renamed from setSoLuongSuDung
    public void setDonGiaNhap(double donGiaNhap) { this.donGiaNhap.set(donGiaNhap); } // Renamed from setDonGiaBanLucDo
    public void setTenVatTu(String tenVatTu) { this.tenVatTu.set(tenVatTu); }
    public void setDonViTinhVatTu(String donViTinhVatTu) { this.donViTinhVatTu.set(donViTinhVatTu); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public IntegerProperty soLuongProperty() { return soLuong; } // Renamed from soLuongSuDungProperty
    public DoubleProperty donGiaNhapProperty() { return donGiaNhap; } // Renamed from donGiaBanLucDoProperty
    public StringProperty tenVatTuProperty() { return tenVatTu; }
    public StringProperty donViTinhVatTuProperty() { return donViTinhVatTu; }
}
