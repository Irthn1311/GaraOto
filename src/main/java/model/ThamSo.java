package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ThamSo {
    private StringProperty tenThamSo;
    private IntegerProperty giaTri;

    public ThamSo() {
        this.tenThamSo = new SimpleStringProperty();
        this.giaTri = new SimpleIntegerProperty();
    }

    public ThamSo(String tenThamSo, int giaTri) {
        this.tenThamSo = new SimpleStringProperty(tenThamSo);
        this.giaTri = new SimpleIntegerProperty(giaTri);
    }

    // Getters
    public String getTenThamSo() { return tenThamSo.get(); }
    public int getGiaTri() { return giaTri.get(); }

    // Setters
    public void setTenThamSo(String tenThamSo) { this.tenThamSo.set(tenThamSo); }
    public void setGiaTri(int giaTri) { this.giaTri.set(giaTri); }

    // Properties for JavaFX binding
    public StringProperty tenThamSoProperty() { return tenThamSo; }
    public IntegerProperty giaTriProperty() { return giaTri; }

    @Override
    public String toString() {
        return tenThamSo.get() + ": " + giaTri.get();
    }
}
