package model;

import javafx.beans.property.*;

public class TonKhoEntry {
    private final IntegerProperty maVatTu;
    private final StringProperty tenVatTu;
    private final DoubleProperty donGiaVon; // Cost price
    private final IntegerProperty soLuongTon;
    private final DoubleProperty tongGiaTriTon;

    public TonKhoEntry(int maVatTu, String tenVatTu, double donGiaVon, int soLuongTon) {
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.tenVatTu = new SimpleStringProperty(tenVatTu);
        this.donGiaVon = new SimpleDoubleProperty(donGiaVon);
        this.soLuongTon = new SimpleIntegerProperty(soLuongTon);
        this.tongGiaTriTon = new SimpleDoubleProperty(donGiaVon * soLuongTon);
    }

    // Getters
    public int getMaVatTu() { return maVatTu.get(); }
    public String getTenVatTu() { return tenVatTu.get(); }
    public double getDonGiaVon() { return donGiaVon.get(); }
    public int getSoLuongTon() { return soLuongTon.get(); }
    public double getTongGiaTriTon() { return tongGiaTriTon.get(); }

    // Properties
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public StringProperty tenVatTuProperty() { return tenVatTu; }
    public DoubleProperty donGiaVonProperty() { return donGiaVon; }
    public IntegerProperty soLuongTonProperty() { return soLuongTon; }
    public DoubleProperty tongGiaTriTonProperty() { return tongGiaTriTon; }
} 