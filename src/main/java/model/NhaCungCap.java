package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NhaCungCap {
    private IntegerProperty maNhaCungCap;
    private StringProperty tenNhaCungCap;
    private StringProperty dienThoai;
    private StringProperty diaChi;
    private StringProperty email;

    public NhaCungCap() {
        this.maNhaCungCap = new SimpleIntegerProperty();
        this.tenNhaCungCap = new SimpleStringProperty();
        this.dienThoai = new SimpleStringProperty();
        this.diaChi = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public NhaCungCap(int maNhaCungCap, String tenNhaCungCap, String dienThoai, String diaChi, String email) {
        this.maNhaCungCap = new SimpleIntegerProperty(maNhaCungCap);
        this.tenNhaCungCap = new SimpleStringProperty(tenNhaCungCap);
        this.dienThoai = new SimpleStringProperty(dienThoai);
        this.diaChi = new SimpleStringProperty(diaChi);
        this.email = new SimpleStringProperty(email);
    }

    // Getters
    public int getMaNhaCungCap() { return maNhaCungCap.get(); }
    public String getTenNhaCungCap() { return tenNhaCungCap.get(); }
    public String getDienThoai() { return dienThoai.get(); }
    public String getDiaChi() { return diaChi.get(); }
    public String getEmail() { return email.get(); }

    // Setters
    public void setMaNhaCungCap(int maNhaCungCap) { this.maNhaCungCap.set(maNhaCungCap); }
    public void setTenNhaCungCap(String tenNhaCungCap) { this.tenNhaCungCap.set(tenNhaCungCap); }
    public void setDienThoai(String dienThoai) { this.dienThoai.set(dienThoai); }
    public void setDiaChi(String diaChi) { this.diaChi.set(diaChi); }
    public void setEmail(String email) { this.email.set(email); }

    // Properties for JavaFX binding
    public IntegerProperty maNhaCungCapProperty() { return maNhaCungCap; }
    public StringProperty tenNhaCungCapProperty() { return tenNhaCungCap; }
    public StringProperty dienThoaiProperty() { return dienThoai; }
    public StringProperty diaChiProperty() { return diaChi; }
    public StringProperty emailProperty() { return email; }

    @Override
    public String toString() {
        return tenNhaCungCap.get();
    }
}
