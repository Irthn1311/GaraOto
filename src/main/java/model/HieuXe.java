package model; // Updated package

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

    // Getter for MaHieuXe
    public int getMaHieuXe() {
        return maHieuXe.get();
    }

    // Setter for MaHieuXe
    public void setMaHieuXe(int maHieuXe) {
        this.maHieuXe.set(maHieuXe);
    }

    // Property for MaHieuXe (for JavaFX binding)
    public IntegerProperty maHieuXeProperty() {
        return maHieuXe;
    }

    // Getter for TenHieuXe
    public String getTenHieuXe() {
        return tenHieuXe.get();
    }

    // Setter for TenHieuXe
    public void setTenHieuXe(String tenHieuXe) {
        this.tenHieuXe.set(tenHieuXe);
    }

    // Property for TenHieuXe (for JavaFX binding)
    public StringProperty tenHieuXeProperty() {
        return tenHieuXe;
    }

    @Override
    public String toString() {
        return tenHieuXe.get(); // Useful for ComboBox display
    }
}
