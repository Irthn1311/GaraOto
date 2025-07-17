package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChiTietPhieuNhapKhoVatTu {
    private IntegerProperty maPhieuNhap;
    private IntegerProperty maVatTu;
    private IntegerProperty soLuongNhap;
    private DoubleProperty donGiaNhap;

    // Derived property for UI display (from VatTu table)
    private StringProperty tenVatTu;

    public ChiTietPhieuNhapKhoVatTu() {
        this.maPhieuNhap = new SimpleIntegerProperty();
        this.maVatTu = new SimpleIntegerProperty();
        this.soLuongNhap = new SimpleIntegerProperty();
        this.donGiaNhap = new SimpleDoubleProperty();
        this.tenVatTu = new SimpleStringProperty(); // Initialize derived property
    }

    public ChiTietPhieuNhapKhoVatTu(int maPhieuNhap, int maVatTu, int soLuongNhap, double donGiaNhap) {
        this.maPhieuNhap = new SimpleIntegerProperty(maPhieuNhap);
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.soLuongNhap = new SimpleIntegerProperty(soLuongNhap);
        this.donGiaNhap = new SimpleDoubleProperty(donGiaNhap);
        this.tenVatTu = new SimpleStringProperty(); // Initialize derived property
    }

    // Getters
    public int getMaPhieuNhap() { return maPhieuNhap.get(); }
    public int getMaVatTu() { return maVatTu.get(); }
    public int getSoLuongNhap() { return soLuongNhap.get(); }
    public double getDonGiaNhap() { return donGiaNhap.get(); }
    public String getTenVatTu() { return tenVatTu.get(); }

    // Setters
    public void setMaPhieuNhap(int maPhieuNhap) { this.maPhieuNhap.set(maPhieuNhap); }
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setSoLuongNhap(int soLuongNhap) { this.soLuongNhap.set(soLuongNhap); }
    public void setDonGiaNhap(double donGiaNhap) { this.donGiaNhap.set(donGiaNhap); }
    public void setTenVatTu(String tenVatTu) { this.tenVatTu.set(tenVatTu); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuNhapProperty() { return maPhieuNhap; }
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public IntegerProperty soLuongNhapProperty() { return soLuongNhap; }
    public DoubleProperty donGiaNhapProperty() { return donGiaNhap; }
    public StringProperty tenVatTuProperty() { return tenVatTu; }
}
