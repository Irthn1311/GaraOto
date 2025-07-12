package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Xe {
    private StringProperty bienSo; // Primary Key
    private IntegerProperty maHieuXe; // Foreign key to HieuXe table
    private IntegerProperty maChuXe; // Foreign key to ChuXe table

    // Additional properties for display (derived from joins)
    private StringProperty tenChuXe;
    private StringProperty tenHieuXe;
    private StringProperty dienThoaiChuXe; // For convenience in TiepNhanView
    private StringProperty diaChiChuXe; // Added this property

    // Constructor
    public Xe() {
        this.bienSo = new SimpleStringProperty();
        this.maHieuXe = new SimpleIntegerProperty();
        this.maChuXe = new SimpleIntegerProperty();
        this.tenChuXe = new SimpleStringProperty();
        this.tenHieuXe = new SimpleStringProperty();
        this.dienThoaiChuXe = new SimpleStringProperty();
        this.diaChiChuXe = new SimpleStringProperty(); // Initialize the new property
    }

    // Getters
    public String getBienSo() { return bienSo.get(); }
    public int getMaHieuXe() { return maHieuXe.get(); }
    public int getMaChuXe() { return maChuXe.get(); }

    // Setters
    public void setBienSo(String bienSo) { this.bienSo.set(bienSo); }
    public void setMaHieuXe(int maHieuXe) { this.maHieuXe.set(maHieuXe); }
    public void setMaChuXe(int maChuXe) { this.maChuXe.set(maChuXe); }

    // Properties for JavaFX binding
    public StringProperty bienSoProperty() { return bienSo; }
    public IntegerProperty maHieuXeProperty() { return maHieuXe; }
    public IntegerProperty maChuXeProperty() { return maChuXe; }

    // Getters/Setters for derived properties (for TableView display)
    public String getTenChuXe() { return tenChuXe.get(); }
    public void setTenChuXe(String tenChuXe) { this.tenChuXe.set(tenChuXe); }
    public StringProperty tenChuXeProperty() { return tenChuXe; }

    public String getTenHieuXe() { return tenHieuXe.get(); }
    public void setTenHieuXe(String tenHieuXe) { this.tenHieuXe.set(tenHieuXe); }
    public StringProperty tenHieuXeProperty() { return tenHieuXe; }

    public String getDienThoaiChuXe() { return dienThoaiChuXe.get(); }
    public void setDienThoaiChuXe(String dienThoaiChuXe) { this.dienThoaiChuXe.set(dienThoaiChuXe); }
    public StringProperty dienThoaiChuXeProperty() { return dienThoaiChuXe; }

    // Getter, Setter, and Property for DiaChiChuXe
    public String getDiaChiChuXe() { return diaChiChuXe.get(); }
    public void setDiaChiChuXe(String diaChiChuXe) { this.diaChiChuXe.set(diaChiChuXe); }
    public StringProperty diaChiChuXeProperty() { return diaChiChuXe; }
}
