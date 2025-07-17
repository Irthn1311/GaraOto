package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TieuHaoVatTuEntry {
    private final IntegerProperty maVatTu;
    private final StringProperty tenVatTu;
    private final StringProperty donViTinh;
    private final IntegerProperty soLuongTieuHao;
    private final DoubleProperty tongGiaTriTieuHao;

    /**
     * Constructor for TieuHaoVatTuEntry.
     * @param maVatTu The ID of the material.
     * @param tenVatTu The name of the material.
     * @param donViTinh The unit of the material.
     * @param soLuongTieuHao The quantity of material consumed.
     * @param tongGiaTriTieuHao The total cost value of the consumed material.
     */
    public TieuHaoVatTuEntry(int maVatTu, String tenVatTu, String donViTinh, int soLuongTieuHao, double tongGiaTriTieuHao) {
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.tenVatTu = new SimpleStringProperty(tenVatTu);
        this.donViTinh = new SimpleStringProperty(donViTinh);
        this.soLuongTieuHao = new SimpleIntegerProperty(soLuongTieuHao);
        this.tongGiaTriTieuHao = new SimpleDoubleProperty(tongGiaTriTieuHao);
    }

    // --- Getters for properties ---
    public IntegerProperty maVatTuProperty() {
        return maVatTu;
    }

    public StringProperty tenVatTuProperty() {
        return tenVatTu;
    }

    public StringProperty donViTinhProperty() {
        return donViTinh;
    }

    public IntegerProperty soLuongTieuHaoProperty() {
        return soLuongTieuHao;
    }

    public DoubleProperty tongGiaTriTieuHaoProperty() {
        return tongGiaTriTieuHao;
    }

    // --- Getters for values ---
    public int getMaVatTu() {
        return maVatTu.get();
    }

    public String getTenVatTu() {
        return tenVatTu.get();
    }

    public String getDonViTinh() {
        return donViTinh.get();
    }

    public int getSoLuongTieuHao() {
        return soLuongTieuHao.get();
    }

    public double getTongGiaTriTieuHao() {
        return tongGiaTriTieuHao.get();
    }

    // --- Setters for values (especially for aggregation) ---
    public void setSoLuongTieuHao(int soLuongTieuHao) {
        this.soLuongTieuHao.set(soLuongTieuHao);
    }

    public void setTongGiaTriTieuHao(double tongGiaTriTieuHao) {
        this.tongGiaTriTieuHao.set(tongGiaTriTieuHao);
    }
}
