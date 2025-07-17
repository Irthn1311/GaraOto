package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChiTietPhieuSuaChua_TienCong {
    private IntegerProperty maPhieuSC;
    private IntegerProperty maLoaiTienCong;
    // private IntegerProperty soLuong; // Removed: Not present in database.sql
    private DoubleProperty donGia; // Changed from donGiaTienCongLucDo to donGia

    // Derived property for UI display (from LoaiTienCong table)
    private StringProperty tenLoaiTienCong;

    public ChiTietPhieuSuaChua_TienCong() {
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maLoaiTienCong = new SimpleIntegerProperty();
        // this.soLuong = new SimpleIntegerProperty(); // Removed
        this.donGia = new SimpleDoubleProperty(); // Changed
        this.tenLoaiTienCong = new SimpleStringProperty();
    }

    public ChiTietPhieuSuaChua_TienCong(int maPhieuSC, int maLoaiTienCong, /* int soLuong, */ double donGia) {
        this.maPhieuSC = new SimpleIntegerProperty(maPhieuSC);
        this.maLoaiTienCong = new SimpleIntegerProperty(maLoaiTienCong);
        // this.soLuong = new SimpleIntegerProperty(soLuong); // Removed
        this.donGia = new SimpleDoubleProperty(donGia); // Changed
        this.tenLoaiTienCong = new SimpleStringProperty();
    }

    // Getters
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaLoaiTienCong() { return maLoaiTienCong.get(); }
    // public int getSoLuong() { return soLuong.get(); } // Removed
    public double getDonGia() { return donGia.get(); } // Changed from getDonGiaTienCongLucDo
    public String getTenLoaiTienCong() { return tenLoaiTienCong.get(); }

    // Setters
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaLoaiTienCong(int maLoaiTienCong) { this.maLoaiTienCong.set(maLoaiTienCong); }
    // public void setSoLuong(int soLuong) { this.soLuong.set(soLuong); } // Removed
    public void setDonGia(double donGia) { this.donGia.set(donGia); } // Changed from setDonGiaTienCongLucDo
    public void setTenLoaiTienCong(String tenLoaiTienCong) { this.tenLoaiTienCong.set(tenLoaiTienCong); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maLoaiTienCongProperty() { return maLoaiTienCong; }
    // public IntegerProperty soLuongProperty() { return soLuong; } // Removed
    public DoubleProperty donGiaProperty() { return donGia; } // Changed from donGiaTienCongLucDoProperty
    public StringProperty tenLoaiTienCongProperty() { return tenLoaiTienCong; }
}
