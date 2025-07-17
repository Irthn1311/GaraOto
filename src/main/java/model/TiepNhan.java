package model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class TiepNhan {
    private IntegerProperty maTiepNhan;
    private StringProperty bienSo;
    private ObjectProperty<LocalDate> ngayTiepNhan;
    private DoubleProperty tongTienNo;
    private BooleanProperty trangThaiHoanTat;

    // Derived properties for UI display (from Xe, ChuXe, HieuXe tables)
    private StringProperty tenChuXe;
    private StringProperty dienThoaiChuXe;
    private StringProperty diaChiChuXe;
    private StringProperty tenHieuXe;

    public TiepNhan() {
        this.maTiepNhan = new SimpleIntegerProperty();
        this.bienSo = new SimpleStringProperty();
        this.ngayTiepNhan = new SimpleObjectProperty<>();
        this.tongTienNo = new SimpleDoubleProperty();
        this.trangThaiHoanTat = new SimpleBooleanProperty();

        this.tenChuXe = new SimpleStringProperty();
        this.dienThoaiChuXe = new SimpleStringProperty();
        this.diaChiChuXe = new SimpleStringProperty();
        this.tenHieuXe = new SimpleStringProperty();
    }

    public TiepNhan(int maTiepNhan, String bienSo, LocalDate ngayTiepNhan, double tongTienNo, boolean trangThaiHoanTat) {
        this.maTiepNhan = new SimpleIntegerProperty(maTiepNhan);
        this.bienSo = new SimpleStringProperty(bienSo);
        this.ngayTiepNhan = new SimpleObjectProperty<>(ngayTiepNhan);
        this.tongTienNo = new SimpleDoubleProperty(tongTienNo);
        this.trangThaiHoanTat = new SimpleBooleanProperty(trangThaiHoanTat);

        this.tenChuXe = new SimpleStringProperty(); // Initialize derived properties
        this.dienThoaiChuXe = new SimpleStringProperty();
        this.diaChiChuXe = new SimpleStringProperty();
        this.tenHieuXe = new SimpleStringProperty();
    }

    // Getters
    public int getMaTiepNhan() { return maTiepNhan.get(); }
    public String getBienSo() { return bienSo.get(); }
    public LocalDate getNgayTiepNhan() { return ngayTiepNhan.get(); }
    public double getTongTienNo() { return tongTienNo.get(); }
    public boolean isTrangThaiHoanTat() { return trangThaiHoanTat.get(); }

    public String getTenChuXe() { return tenChuXe.get(); }
    public String getDienThoaiChuXe() { return dienThoaiChuXe.get(); }
    public String getDiaChiChuXe() { return diaChiChuXe.get(); }
    public String getTenHieuXe() { return tenHieuXe.get(); }

    // Setters
    public void setMaTiepNhan(int maTiepNhan) { this.maTiepNhan.set(maTiepNhan); }
    public void setBienSo(String bienSo) { this.bienSo.set(bienSo); }
    public void setNgayTiepNhan(LocalDate ngayTiepNhan) { this.ngayTiepNhan.set(ngayTiepNhan); }
    public void setTongTienNo(double tongTienNo) { this.tongTienNo.set(tongTienNo); }
    public void setTrangThaiHoanTat(boolean trangThaiHoanTat) { this.trangThaiHoanTat.set(trangThaiHoanTat); }

    public void setTenChuXe(String tenChuXe) { this.tenChuXe.set(tenChuXe); }
    public void setDienThoaiChuXe(String dienThoaiChuXe) { this.dienThoaiChuXe.set(dienThoaiChuXe); }
    public void setDiaChiChuXe(String diaChiChuXe) { this.diaChiChuXe.set(diaChiChuXe); }
    public void setTenHieuXe(String tenHieuXe) { this.tenHieuXe.set(tenHieuXe); }

    // Properties for JavaFX binding
    public IntegerProperty maTiepNhanProperty() { return maTiepNhan; }
    public StringProperty bienSoProperty() { return bienSo; }
    public ObjectProperty<LocalDate> ngayTiepNhanProperty() { return ngayTiepNhan; }
    public DoubleProperty tongTienNoProperty() { return tongTienNo; }
    public BooleanProperty trangThaiHoanTatProperty() { return trangThaiHoanTat; }

    public StringProperty tenChuXeProperty() { return tenChuXe; }
    public StringProperty dienThoaiChuXeProperty() { return dienThoaiChuXe; }
    public StringProperty diaChiChuXeProperty() { return diaChiChuXe; }
    public StringProperty tenHieuXeProperty() { return tenHieuXe; }
}
