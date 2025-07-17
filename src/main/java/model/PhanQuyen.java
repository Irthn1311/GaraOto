package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PhanQuyen {
    private final StringProperty maPhanQuyen;
    private final StringProperty tenPhanQuyen;

    public PhanQuyen() {
        this.maPhanQuyen = new SimpleStringProperty();
        this.tenPhanQuyen = new SimpleStringProperty();
    }

    public PhanQuyen(String maPhanQuyen, String tenPhanQuyen) {
        this.maPhanQuyen = new SimpleStringProperty(maPhanQuyen);
        this.tenPhanQuyen = new SimpleStringProperty(tenPhanQuyen);
    }

    // Getters
    public String getMaPhanQuyen() {
        return maPhanQuyen.get();
    }

    public String getTenPhanQuyen() {
        return tenPhanQuyen.get();
    }

    // Setters
    public void setMaPhanQuyen(String maPhanQuyen) {
        this.maPhanQuyen.set(maPhanQuyen);
    }

    public void setTenPhanQuyen(String tenPhanQuyen) {
        this.tenPhanQuyen.set(tenPhanQuyen);
    }

    // Properties
    public StringProperty maPhanQuyenProperty() {
        return maPhanQuyen;
    }

    public StringProperty tenPhanQuyenProperty() {
        return tenPhanQuyen;
    }

    @Override
    public String toString() {
        return this.getTenPhanQuyen(); // Useful for ComboBox display
    }
} 