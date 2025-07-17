package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ChiTietPhieuSuaChua_TienCong {
    private IntegerProperty maChiTietTienCong;
    private IntegerProperty maPhieuSC;
    private IntegerProperty maTienCong;
    private DoubleProperty donGia;
    private DoubleProperty thanhTien;

    private TienCong tienCong; // Renamed from loaiTienCong to tienCong for consistency

    public ChiTietPhieuSuaChua_TienCong() {
        this.maChiTietTienCong = new SimpleIntegerProperty();
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maTienCong = new SimpleIntegerProperty();
        this.donGia = new SimpleDoubleProperty();
        this.thanhTien = new SimpleDoubleProperty();
    }

    public ChiTietPhieuSuaChua_TienCong(int maChiTietTienCong, int maPhieuSC, int maTienCong, double donGia, double thanhTien) {
        this.maChiTietTienCong = new SimpleIntegerProperty(maChiTietTienCong);
        this.maPhieuSC = new SimpleIntegerProperty(maPhieuSC);
        this.maTienCong = new SimpleIntegerProperty(maTienCong);
        this.donGia = new SimpleDoubleProperty(donGia);
        this.thanhTien = new SimpleDoubleProperty(thanhTien);
    }

    // Getters
    public int getMaChiTietTienCong() { return maChiTietTienCong.get(); }
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaTienCong() { return maTienCong.get(); }
    public double getDonGia() { return donGia.get(); }
    public double getThanhTien() { return thanhTien.get(); }

    // Setters
    public void setMaChiTietTienCong(int maChiTietTienCong) { this.maChiTietTienCong.set(maChiTietTienCong); }
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaTienCong(int maTienCong) { this.maTienCong.set(maTienCong); }
    public void setDonGia(double donGia) { this.donGia.set(donGia); }
    public void setThanhTien(double thanhTien) { this.thanhTien.set(thanhTien); }

    // Properties for JavaFX binding
    public IntegerProperty maChiTietTienCongProperty() { return maChiTietTienCong; }
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maTienCongProperty() { return maTienCong; }
    public DoubleProperty donGiaProperty() { return donGia; }
    public DoubleProperty thanhTienProperty() { return thanhTien; }

    // Getter and Setter for TienCong object
    public TienCong getTienCong() {
        return tienCong;
    }

    public void setTienCong(TienCong tienCong) {
        this.tienCong = tienCong;
    }
}
