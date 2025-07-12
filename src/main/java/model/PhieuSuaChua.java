package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class PhieuSuaChua {
    private IntegerProperty maPhieuSC;
    private IntegerProperty maTiepNhan; // FK to TiepNhan
    private ObjectProperty<LocalDate> ngaySuaChua;
    private StringProperty ghiChu;
    private DoubleProperty tongTien;

    public PhieuSuaChua() {
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maTiepNhan = new SimpleIntegerProperty();
        this.ngaySuaChua = new SimpleObjectProperty<>();
        this.ghiChu = new SimpleStringProperty();
        this.tongTien = new SimpleDoubleProperty();
    }

    // Getters
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaTiepNhan() { return maTiepNhan.get(); }
    public LocalDate getNgaySuaChua() { return ngaySuaChua.get(); }
    public String getGhiChu() { return ghiChu.get(); }
    public double getTongTien() { return tongTien.get(); }

    // Setters
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaTiepNhan(int maTiepNhan) { this.maTiepNhan.set(maTiepNhan); }
    public void setNgaySuaChua(LocalDate ngaySuaChua) { this.ngaySuaChua.set(ngaySuaChua); }
    public void setGhiChu(String ghiChu) { this.ghiChu.set(ghiChu); }
    public void setTongTien(double tongTien) { this.tongTien.set(tongTien); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maTiepNhanProperty() { return maTiepNhan; }
    public ObjectProperty<LocalDate> ngaySuaChuaProperty() { return ngaySuaChua; }
    public StringProperty ghiChuProperty() { return ghiChu; }
    public DoubleProperty tongTienProperty() { return tongTien; }
}
