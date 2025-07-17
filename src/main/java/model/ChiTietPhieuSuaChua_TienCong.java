package model;

import javafx.beans.property.*;

public class ChiTietPhieuSuaChua_TienCong {
    private IntegerProperty maChiTietTienCong;
    private IntegerProperty maPhieuSC;
    private IntegerProperty maTienCong;
    private DoubleProperty donGia;
    private DoubleProperty thanhTien;

    // For display purposes
    private StringProperty noiDungTienCong;

    public ChiTietPhieuSuaChua_TienCong() {
        this.maChiTietTienCong = new SimpleIntegerProperty();
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maTienCong = new SimpleIntegerProperty();
        this.donGia = new SimpleDoubleProperty();
        this.thanhTien = new SimpleDoubleProperty();
        this.noiDungTienCong = new SimpleStringProperty();
    }

    // Getters and Setters
    public int getMaChiTietTienCong() {
        return maChiTietTienCong.get();
    }

    public IntegerProperty maChiTietTienCongProperty() {
        return maChiTietTienCong;
    }

    public void setMaChiTietTienCong(int maChiTietTienCong) {
        this.maChiTietTienCong.set(maChiTietTienCong);
    }

    public int getMaPhieuSC() {
        return maPhieuSC.get();
    }

    public IntegerProperty maPhieuSCProperty() {
        return maPhieuSC;
    }

    public void setMaPhieuSC(int maPhieuSC) {
        this.maPhieuSC.set(maPhieuSC);
    }

    public int getMaTienCong() {
        return maTienCong.get();
    }

    public IntegerProperty maTienCongProperty() {
        return maTienCong;
    }

    public void setMaTienCong(int maTienCong) {
        this.maTienCong.set(maTienCong);
    }

    public double getDonGia() {
        return donGia.get();
    }

    public DoubleProperty donGiaProperty() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia.set(donGia);
    }

    public double getThanhTien() {
        return thanhTien.get();
    }

    public DoubleProperty thanhTienProperty() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien.set(thanhTien);
    }

    public String getNoiDungTienCong() {
        return noiDungTienCong.get();
    }

    public StringProperty noiDungTienCongProperty() {
        return noiDungTienCong;
    }

    public void setNoiDungTienCong(String noiDungTienCong) {
        this.noiDungTienCong.set(noiDungTienCong);
    }
}
