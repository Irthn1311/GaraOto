package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChuXe {
    private IntegerProperty maChuXe;
    private StringProperty tenChuXe;
    private StringProperty dienThoai;
    private StringProperty diaChi;
    private StringProperty email;

    public ChuXe() {
        this.maChuXe = new SimpleIntegerProperty();
        this.tenChuXe = new SimpleStringProperty();
        this.dienThoai = new SimpleStringProperty();
        this.diaChi = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
    }

    public ChuXe(int maChuXe, String tenChuXe, String dienThoai, String diaChi, String email) {
        this.maChuXe = new SimpleIntegerProperty(maChuXe);
        this.tenChuXe = new SimpleStringProperty(tenChuXe);
        this.dienThoai = new SimpleStringProperty(dienThoai);
        this.diaChi = new SimpleStringProperty(diaChi);
        this.email = new SimpleStringProperty(email);
    }

    // Getters
    public int getMaChuXe() { return maChuXe.get(); }
    public String getTenChuXe() { return tenChuXe.get(); }
    public String getDienThoai() { return dienThoai.get(); }
    public String getDiaChi() { return diaChi.get(); }
    public String getEmail() { return email.get(); }

    // Setters
    public void setMaChuXe(int maChuXe) { this.maChuXe.set(maChuXe); }
    public void setTenChuXe(String tenChuXe) { this.tenChuXe.set(tenChuXe); }
    public void setDienThoai(String dienThoai) { this.dienThoai.set(dienThoai); }
    public void setDiaChi(String diaChi) { this.diaChi.set(diaChi); }
    public void setEmail(String email) { this.email.set(email); }

    // Properties for JavaFX binding
    public IntegerProperty maChuXeProperty() { return maChuXe; }
    public StringProperty tenChuXeProperty() { return tenChuXe; }
    public StringProperty dienThoaiProperty() { return dienThoai; }
    public StringProperty diaChiProperty() { return diaChi; }
    public StringProperty emailProperty() { return email; }

    @Override
    public String toString() {
        return tenChuXe.get();
    }
}
