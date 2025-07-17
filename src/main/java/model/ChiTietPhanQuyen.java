package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChiTietPhanQuyen {
    private final StringProperty maPhanQuyen;
    private final StringProperty maChucNang;

    public ChiTietPhanQuyen() {
        this.maPhanQuyen = new SimpleStringProperty();
        this.maChucNang = new SimpleStringProperty();
    }

    public ChiTietPhanQuyen(String maPhanQuyen, String maChucNang) {
        this.maPhanQuyen = new SimpleStringProperty(maPhanQuyen);
        this.maChucNang = new SimpleStringProperty(maChucNang);
    }

    // Getters
    public String getMaPhanQuyen() {
        return maPhanQuyen.get();
    }

    public String getMaChucNang() {
        return maChucNang.get();
    }

    // Setters
    public void setMaPhanQuyen(String maPhanQuyen) {
        this.maPhanQuyen.set(maPhanQuyen);
    }

    public void setMaChucNang(String maChucNang) {
        this.maChucNang.set(maChucNang);
    }

    // Properties
    public StringProperty maPhanQuyenProperty() {
        return maPhanQuyen;
    }

    public StringProperty maChucNangProperty() {
        return maChucNang;
    }
}
