package model; // Updated package

import javafx.beans.property.*;
import java.time.LocalDate;

public class TiepNhan {
    private IntegerProperty maTiepNhan;
    private StringProperty bienSo; // Foreign key to Xe table
    private ObjectProperty<LocalDate> ngayTiepNhan;
    private DoubleProperty tongTienNo;
    private BooleanProperty trangThaiHoanTat;

    // Additional properties for TableView display (derived from joins)
    private StringProperty tenChuXe;
    private StringProperty tenHieuXe;
    private StringProperty dienThoaiChuXe; // For convenience in TiepNhanView
    private StringProperty diaChiChuXe; // For convenience in TiepNhanView


    public TiepNhan() {
        this.maTiepNhan = new SimpleIntegerProperty();
        this.bienSo = new SimpleStringProperty();
        this.ngayTiepNhan = new SimpleObjectProperty<>();
        this.tongTienNo = new SimpleDoubleProperty();
        this.trangThaiHoanTat = new SimpleBooleanProperty();

        // Initialize derived properties
        this.tenChuXe = new SimpleStringProperty();
        this.tenHieuXe = new SimpleStringProperty();
        this.dienThoaiChuXe = new SimpleStringProperty();
        this.diaChiChuXe = new SimpleStringProperty();
    }

    // Getters
    public int getMaTiepNhan() { return maTiepNhan.get(); }
    public String getBienSo() { return bienSo.get(); }
    public LocalDate getNgayTiepNhan() { return ngayTiepNhan.get(); }
    public double getTongTienNo() { return tongTienNo.get(); }
    public boolean isTrangThaiHoanTat() { return trangThaiHoanTat.get(); }

    // Setters
    public void setMaTiepNhan(int maTiepNhan) { this.maTiepNhan.set(maTiepNhan); }
    public void setBienSo(String bienSo) { this.bienSo.set(bienSo); }
    public void setNgayTiepNhan(LocalDate ngayTiepNhan) { this.ngayTiepNhan.set(ngayTiepNhan); }
    public void setTongTienNo(double tongTienNo) { this.tongTienNo.set(tongTienNo); }
    public void setTrangThaiHoanTat(boolean trangThaiHoanTat) { this.trangThaiHoanTat.set(trangThaiHoanTat); }

    // Properties for JavaFX binding
    public IntegerProperty maTiepNhanProperty() { return maTiepNhan; }
    public StringProperty bienSoProperty() { return bienSo; }
    public ObjectProperty<LocalDate> ngayTiepNhanProperty() { return ngayTiepNhan; }
    public DoubleProperty tongTienNoProperty() { return tongTienNo; }
    public BooleanProperty trangThaiHoanTatProperty() { return trangThaiHoanTat; }

    // Getters and Setters for derived properties (for TableView display)
    public String getTenChuXe() { return tenChuXe.get(); }
    public void setTenChuXe(String tenChuXe) { this.tenChuXe.set(tenChuXe); }
    public StringProperty tenChuXeProperty() { return tenChuXe; }

    public String getTenHieuXe() { return tenHieuXe.get(); }
    public void setTenHieuXe(String tenHieuXe) { this.tenHieuXe.set(tenHieuXe); }
    public StringProperty tenHieuXeProperty() { return tenHieuXe; }

    public String getDienThoaiChuXe() { return dienThoaiChuXe.get(); }
    public void setDienThoaiChuXe(String dienThoaiChuXe) { this.dienThoaiChuXe.set(dienThoaiChuXe); }
    public StringProperty dienThoaiChuXeProperty() { return dienThoaiChuXe; }

    public String getDiaChiChuXe() { return diaChiChuXe.get(); }
    public void setDiaChiChuXe(String diaChiChuXe) { this.diaChiChuXe.set(diaChiChuXe); }
    public StringProperty diaChiChuXeProperty() { return diaChiChuXe; }
}
