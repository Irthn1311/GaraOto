package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tho {
    private IntegerProperty maTho;
    private StringProperty tenTho;
    private StringProperty soDienThoai; // Changed from dienThoai to soDienThoai
    private StringProperty chuyenMon; // Changed from diaChi to chuyenMon

    public Tho() {
        this.maTho = new SimpleIntegerProperty();
        this.tenTho = new SimpleStringProperty();
        this.soDienThoai = new SimpleStringProperty();
        this.chuyenMon = new SimpleStringProperty();
    }

    public Tho(int maTho, String tenTho, String soDienThoai, String chuyenMon) {
        this.maTho = new SimpleIntegerProperty(maTho);
        this.tenTho = new SimpleStringProperty(tenTho);
        this.soDienThoai = new SimpleStringProperty(soDienThoai);
        this.chuyenMon = new SimpleStringProperty(chuyenMon);
    }

    // Getters
    public int getMaTho() { return maTho.get(); }
    public String getTenTho() { return tenTho.get(); }
    public String getSoDienThoai() { return soDienThoai.get(); }
    public String getChuyenMon() { return chuyenMon.get(); }

    // Setters
    public void setMaTho(int maTho) { this.maTho.set(maTho); }
    public void setTenTho(String tenTho) { this.tenTho.set(tenTho); }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai.set(soDienThoai); }
    public void setChuyenMon(String chuyenMon) { this.chuyenMon.set(chuyenMon); }

    // Properties for JavaFX binding
    public IntegerProperty maThoProperty() { return maTho; }
    public StringProperty tenThoProperty() { return tenTho; }
    public StringProperty soDienThoaiProperty() { return soDienThoai; }
    public StringProperty chuyenMonProperty() { return chuyenMon; }

    @Override
    public String toString() {
        return tenTho.get();
    }
}
