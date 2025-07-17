package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import java.time.LocalDate;

public class LuotTiepNhanSuaChuaEntry {
    private final ObjectProperty<LocalDate> ngay;
    private final IntegerProperty soLuotTiepNhan;
    private final IntegerProperty soPhieuSuaChua;

    /**
     * Constructor for LuotTiepNhanSuaChuaEntry.
     * @param ngay The date for the entry.
     * @param soLuotTiepNhan The number of acceptance records on this date.
     * @param soPhieuSuaChua The number of repair slips on this date.
     */
    public LuotTiepNhanSuaChuaEntry(LocalDate ngay, int soLuotTiepNhan, int soPhieuSuaChua) {
        this.ngay = new SimpleObjectProperty<>(ngay);
        this.soLuotTiepNhan = new SimpleIntegerProperty(soLuotTiepNhan);
        this.soPhieuSuaChua = new SimpleIntegerProperty(soPhieuSuaChua);
    }

    // --- Getters for properties ---
    public ObjectProperty<LocalDate> ngayProperty() {
        return ngay;
    }

    public IntegerProperty soLuotTiepNhanProperty() {
        return soLuotTiepNhan;
    }

    public IntegerProperty soPhieuSuaChuaProperty() {
        return soPhieuSuaChua;
    }

    // --- Getters for values ---
    public LocalDate getNgay() {
        return ngay.get();
    }

    public int getSoLuotTiepNhan() {
        return soLuotTiepNhan.get();
    }

    public int getSoPhieuSuaChua() {
        return soPhieuSuaChua.get();
    }

    // --- Setters for values (if needed, though reports are usually read-only) ---
    public void setNgay(LocalDate ngay) {
        this.ngay.set(ngay);
    }

    public void setSoLuotTiepNhan(int soLuotTiepNhan) {
        this.soLuotTiepNhan.set(soLuotTiepNhan);
    }

    public void setSoPhieuSuaChua(int soPhieuSuaChua) {
        this.soPhieuSuaChua.set(soPhieuSuaChua);
    }
}
