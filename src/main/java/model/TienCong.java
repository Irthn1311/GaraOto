package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TienCong {
    private IntegerProperty maTienCong;
    private StringProperty noiDung;
    private DoubleProperty donGia;

    public TienCong() {
        this.maTienCong = new SimpleIntegerProperty();
        this.noiDung = new SimpleStringProperty();
        this.donGia = new SimpleDoubleProperty();
    }

    public TienCong(int maTienCong, String noiDung, double donGia) {
        this.maTienCong = new SimpleIntegerProperty(maTienCong);
        this.noiDung = new SimpleStringProperty(noiDung);
        this.donGia = new SimpleDoubleProperty(donGia);
    }

    // Getters
    public int getMaTienCong() { return maTienCong.get(); }
    public String getNoiDung() { return noiDung.get(); }
    public double getDonGia() { return donGia.get(); }

    // Setters
    public void setMaTienCong(int maTienCong) { this.maTienCong.set(maTienCong); }
    public void setNoiDung(String noiDung) { this.noiDung.set(noiDung); }
    public void setDonGia(double donGia) { this.donGia.set(donGia); }

    // Properties for JavaFX binding
    public IntegerProperty maTienCongProperty() { return maTienCong; }
    public StringProperty noiDungProperty() { return noiDung; }
    public DoubleProperty donGiaProperty() { return donGia; }

    @Override
    public String toString() {
        return noiDung.get();
    }
}
