package model;

import javafx.beans.property.*;

public class TieuHaoVatTuEntry {
    private final IntegerProperty maVatTu;
    private final StringProperty tenVatTu;
    private final StringProperty donViTinh;
    private final IntegerProperty soLuongTieuHao;
    private final DoubleProperty tongGiaTriTieuHao;

    public TieuHaoVatTuEntry(int maVatTu, String tenVatTu, String donViTinh, int soLuongTieuHao, double tongGiaTriTieuHao) {
        this.maVatTu = new SimpleIntegerProperty(maVatTu);
        this.tenVatTu = new SimpleStringProperty(tenVatTu);
        this.donViTinh = new SimpleStringProperty(donViTinh);
        this.soLuongTieuHao = new SimpleIntegerProperty(soLuongTieuHao);
        this.tongGiaTriTieuHao = new SimpleDoubleProperty(tongGiaTriTieuHao);
    }

    // --- Property Getters ---

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

    // --- Standard Getters ---

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
}
