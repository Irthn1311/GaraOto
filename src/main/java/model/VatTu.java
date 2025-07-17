package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VatTu {
    private IntegerProperty maVatTu;
    private StringProperty tenVatTu;
    private DoubleProperty donGiaBan; // Changed from donGia to donGiaBan
    private IntegerProperty soLuongTon;
    private StringProperty donViTinh; // New property
    private IntegerProperty mucTonKhoToiThieu; // New property

    public VatTu() {
        this.maVatTu = new SimpleIntegerProperty();
        this.tenVatTu = new SimpleStringProperty();
        this.donGiaBan = new SimpleDoubleProperty();
        this.soLuongTon = new SimpleIntegerProperty();
        this.donViTinh = new SimpleStringProperty(); // Initialize new property
        this.mucTonKhoToiThieu = new SimpleIntegerProperty(); // Initialize new property
    }

    public VatTu(int maVatTu, String tenVatTu, double donGiaBan, int soLuongTon, String donViTinh, int mucTonKhoToiThieu) {
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.tenVatTu = new SimpleStringProperty(tenVatTu);
        this.donGiaBan = new SimpleDoubleProperty(donGiaBan);
        this.soLuongTon = new SimpleIntegerProperty(soLuongTon);
        this.donViTinh = new SimpleStringProperty(donViTinh);
        this.mucTonKhoToiThieu = new SimpleIntegerProperty(mucTonKhoToiThieu);
    }

    // Getters
    public int getMaVatTu() { return maVatTu.get(); }
    public String getTenVatTu() { return tenVatTu.get(); }
    public double getDonGiaBan() { return donGiaBan.get(); } // Getter for donGiaBan
    public int getSoLuongTon() { return soLuongTon.get(); }
    public String getDonViTinh() { return donViTinh.get(); } // Getter for DonViTinh
    public int getMucTonKhoToiThieu() { return mucTonKhoToiThieu.get(); } // Getter for MucTonKhoToiThieu

    // Setters
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setTenVatTu(String tenVatTu) { this.tenVatTu.set(tenVatTu); }
    public void setDonGiaBan(double donGiaBan) { this.donGiaBan.set(donGiaBan); } // Setter for donGiaBan
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon.set(soLuongTon); }
    public void setDonViTinh(String donViTinh) { this.donViTinh.set(donViTinh); } // Setter for DonViTinh
    public void setMucTonKhoToiThieu(int mucTonKhoToiThieu) { this.mucTonKhoToiThieu.set(mucTonKhoToiThieu); } // Setter for MucTonKhoToiThieu

    // Properties for JavaFX binding
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public StringProperty tenVatTuProperty() { return tenVatTu; }
    public DoubleProperty donGiaBanProperty() { return donGiaBan; } // Property for donGiaBan
    public IntegerProperty soLuongTonProperty() { return soLuongTon; }
    public StringProperty donViTinhProperty() { return donViTinh; } // Property for DonViTinh
    public IntegerProperty mucTonKhoToiThieuProperty() { return mucTonKhoToiThieu; } // Property for MucTonKhoToiThieu

    @Override
    public String toString() {
        return tenVatTu.get();
    }
}
