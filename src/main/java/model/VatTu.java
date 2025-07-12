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
    private DoubleProperty donGia;
    private IntegerProperty soLuongTon;

    public VatTu() {
        this.maVatTu = new SimpleIntegerProperty();
        this.tenVatTu = new SimpleStringProperty();
        this.donGia = new SimpleDoubleProperty();
        this.soLuongTon = new SimpleIntegerProperty();
    }

    public VatTu(int maVatTu, String tenVatTu, double donGia, int soLuongTon) {
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.tenVatTu = new SimpleStringProperty(tenVatTu);
        this.donGia = new SimpleDoubleProperty(donGia);
        this.soLuongTon = new SimpleIntegerProperty(soLuongTon);
    }

    // Getters
    public int getMaVatTu() { return maVatTu.get(); }
    public String getTenVatTu() { return tenVatTu.get(); }
    public double getDonGia() { return donGia.get(); }
    public int getSoLuongTon() { return soLuongTon.get(); }

    // Setters
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setTenVatTu(String tenVatTu) { this.tenVatTu.set(tenVatTu); }
    public void setDonGia(double donGia) { this.donGia.set(donGia); }
    public void setSoLuongTon(int soLuongTon) { this.soLuongTon.set(soLuongTon); }

    // Properties for JavaFX binding
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public StringProperty tenVatTuProperty() { return tenVatTu; }
    public DoubleProperty donGiaProperty() { return donGia; }
    public IntegerProperty soLuongTonProperty() { return soLuongTon; }

    @Override
    public String toString() {
        return tenVatTu.get();
    }
}
