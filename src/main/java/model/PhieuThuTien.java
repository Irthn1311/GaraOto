package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class PhieuThuTien {
    private IntegerProperty maPhieuThu;
    private IntegerProperty maTiepNhan; // Foreign key to TiepNhan table
    private ObjectProperty<LocalDate> ngayThu;
    private DoubleProperty soTienThu;

    public PhieuThuTien() {
        this.maPhieuThu = new SimpleIntegerProperty();
        this.maTiepNhan = new SimpleIntegerProperty();
        this.ngayThu = new SimpleObjectProperty<>();
        this.soTienThu = new SimpleDoubleProperty();
    }

    // Getters
    public int getMaPhieuThu() { return maPhieuThu.get(); }
    public int getMaTiepNhan() { return maTiepNhan.get(); }
    public LocalDate getNgayThu() { return ngayThu.get(); }
    public double getSoTienThu() { return soTienThu.get(); }

    // Setters
    public void setMaPhieuThu(int maPhieuThu) { this.maPhieuThu.set(maPhieuThu); }
    public void setMaTiepNhan(int maTiepNhan) { this.maTiepNhan.set(maTiepNhan); }
    public void setNgayThu(LocalDate ngayThu) { this.ngayThu.set(ngayThu); }
    public void setSoTienThu(double soTienThu) { this.soTienThu.set(soTienThu); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuThuProperty() { return maPhieuThu; }
    public IntegerProperty maTiepNhanProperty() { return maTiepNhan; }
    public ObjectProperty<LocalDate> ngayThuProperty() { return ngayThu; }
    public DoubleProperty soTienThuProperty() { return soTienThu; }
}
