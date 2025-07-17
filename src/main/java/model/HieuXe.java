package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HieuXe {
    private IntegerProperty maHieuXe;
    private StringProperty tenHieuXe;

    public HieuXe() {
        this.maHieuXe = new SimpleIntegerProperty();
        this.tenHieuXe = new SimpleStringProperty();
    }

    public HieuXe(int maHieuXe, String tenHieuXe) {
        this.maHieuXe = new SimpleIntegerProperty(maHieuXe);
        this.tenHieuXe = new SimpleStringProperty(tenHieuXe);
    }

    // Getters
    public int getMaHieuXe() { return maHieuXe.get(); }
    public String getTenHieuXe() { return tenHieuXe.get(); }

    // Setters
    public void setMaHieuXe(int maHieuXe) { this.maHieuXe.set(maHieuXe); }
    public void setTenHieuXe(String tenHieuXe) { this.tenHieuXe.set(tenHieuXe); }

    // Properties for JavaFX binding
    public IntegerProperty maHieuXeProperty() { return maHieuXe; }
    public StringProperty tenHieuXeProperty() { return tenHieuXe; }

    @Override
    public String toString() {
        return tenHieuXe.get();
    }
}
