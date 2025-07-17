package model;

import javafx.beans.property.*;

public class ChiTietPhieuSuaChua_VatTu {
    private IntegerProperty maChiTietVatTu;
    private IntegerProperty maPhieuSC;
    private IntegerProperty maVatTu;
    private IntegerProperty soLuong;
    private DoubleProperty donGiaNhap;
    private DoubleProperty thanhTien;

    // For display purposes
    private StringProperty tenVatTu;

    public ChiTietPhieuSuaChua_VatTu() {
        this.maChiTietVatTu = new SimpleIntegerProperty();
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maVatTu = new SimpleIntegerProperty();
        this.soLuong = new SimpleIntegerProperty();
        this.donGiaNhap = new SimpleDoubleProperty();
        this.thanhTien = new SimpleDoubleProperty();
        this.tenVatTu = new SimpleStringProperty();
    }

    // Getters and Setters
    public int getMaChiTietVatTu() {
        return maChiTietVatTu.get();
    }

    public IntegerProperty maChiTietVatTuProperty() {
        return maChiTietVatTu;
    }

    public void setMaChiTietVatTu(int maChiTietVatTu) {
        this.maChiTietVatTu.set(maChiTietVatTu);
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

    public int getMaVatTu() {
        return maVatTu.get();
    }

    public IntegerProperty maVatTuProperty() {
        return maVatTu;
    }

    public void setMaVatTu(int maVatTu) {
        this.maVatTu.set(maVatTu);
    }

    public int getSoLuong() {
        return soLuong.get();
    }

    public IntegerProperty soLuongProperty() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong.set(soLuong);
    }

    public double getDonGiaNhap() {
        return donGiaNhap.get();
    }

    public DoubleProperty donGiaNhapProperty() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap.set(donGiaNhap);
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

    public String getTenVatTu() {
        return tenVatTu.get();
    }

    public StringProperty tenVatTuProperty() {
        return tenVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu.set(tenVatTu);
    }
}
