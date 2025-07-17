package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class LoiNhuanEntry {
    private final ObjectProperty<LocalDate> ngay;
    private final DoubleProperty tongDoanhThuBanHang;
    private final DoubleProperty tongChiPhiVatTu;
    private final DoubleProperty tongChiPhiTienCong;
    private final DoubleProperty loiNhuanRong;

    /**
     * Constructor for LoiNhuanEntry.
     * @param ngay The date for the entry.
     * @param tongDoanhThuBanHang Total revenue from sales (e.g., total from repair slips).
     * @param tongChiPhiVatTu Total cost of materials consumed.
     * @param tongChiPhiTienCong Total cost of labor.
     * @param loiNhuanRong Net profit (tongDoanhThuBanHang - tongChiPhiVatTu - tongChiPhiTienCong).
     */
    public LoiNhuanEntry(LocalDate ngay, double tongDoanhThuBanHang, double tongChiPhiVatTu, double tongChiPhiTienCong, double loiNhuanRong) {
        this.ngay = new SimpleObjectProperty<>(ngay);
        this.tongDoanhThuBanHang = new SimpleDoubleProperty(tongDoanhThuBanHang);
        this.tongChiPhiVatTu = new SimpleDoubleProperty(tongChiPhiVatTu);
        this.tongChiPhiTienCong = new SimpleDoubleProperty(tongChiPhiTienCong);
        this.loiNhuanRong = new SimpleDoubleProperty(loiNhuanRong);
    }

    // --- Getters for properties ---
    public ObjectProperty<LocalDate> ngayProperty() {
        return ngay;
    }

    public DoubleProperty tongDoanhThuBanHangProperty() {
        return tongDoanhThuBanHang;
    }

    public DoubleProperty tongChiPhiVatTuProperty() {
        return tongChiPhiVatTu;
    }

    public DoubleProperty tongChiPhiTienCongProperty() {
        return tongChiPhiTienCong;
    }

    public DoubleProperty loiNhuanRongProperty() {
        return loiNhuanRong;
    }

    // --- Getters for values ---
    public LocalDate getNgay() {
        return ngay.get();
    }

    public double getTongDoanhThuBanHang() {
        return tongDoanhThuBanHang.get();
    }

    public double getTongChiPhiVatTu() {
        return tongChiPhiVatTu.get();
    }

    public double getTongChiPhiTienCong() {
        return tongChiPhiTienCong.get();
    }

    public double getLoiNhuanRong() {
        return loiNhuanRong.get();
    }

    // --- Setters for values (especially for aggregation) ---
    public void setTongDoanhThuBanHang(double tongDoanhThuBanHang) {
        this.tongDoanhThuBanHang.set(tongDoanhThuBanHang);
    }

    public void setTongChiPhiVatTu(double tongChiPhiVatTu) {
        this.tongChiPhiVatTu.set(tongChiPhiVatTu);
    }

    public void setTongChiPhiTienCong(double tongChiPhiTienCong) {
        this.tongChiPhiTienCong.set(tongChiPhiTienCong);
    }

    public void setLoiNhuanRong(double loiNhuanRong) {
        this.loiNhuanRong.set(loiNhuanRong);
    }
}
