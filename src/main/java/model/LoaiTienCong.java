package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoaiTienCong {
    private IntegerProperty maLoaiTienCong;
    private StringProperty tenLoaiTienCong;
    private DoubleProperty donGiaTienCong;

    public LoaiTienCong() {
        this.maLoaiTienCong = new SimpleIntegerProperty();
        this.tenLoaiTienCong = new SimpleStringProperty();
        this.donGiaTienCong = new SimpleDoubleProperty();
    }

    public LoaiTienCong(int maLoaiTienCong, String tenLoaiTienCong, double donGiaTienCong) {
        this.maLoaiTienCong = new SimpleIntegerProperty(maLoaiTienCong);
        this.tenLoaiTienCong = new SimpleStringProperty(tenLoaiTienCong);
        this.donGiaTienCong = new SimpleDoubleProperty(donGiaTienCong);
    }

    // Getters
    public int getMaLoaiTienCong() { return maLoaiTienCong.get(); }
    public String getTenLoaiTienCong() { return tenLoaiTienCong.get(); }
    public double getDonGiaTienCong() { return donGiaTienCong.get(); }

    // Setters
    public void setMaLoaiTienCong(int maLoaiTienCong) { this.maLoaiTienCong.set(maLoaiTienCong); }
    public void setTenLoaiTienCong(String tenLoaiTienCong) { this.tenLoaiTienCong.set(tenLoaiTienCong); }
    public void setDonGiaTienCong(double donGiaTienCong) { this.donGiaTienCong.set(donGiaTienCong); }

    // Properties for JavaFX binding
    public IntegerProperty maLoaiTienCongProperty() { return maLoaiTienCong; }
    public StringProperty tenLoaiTienCongProperty() { return tenLoaiTienCong; }
    public DoubleProperty donGiaTienCongProperty() { return donGiaTienCong; }

    @Override
    public String toString() {
        return tenLoaiTienCong.get();
    }
}
