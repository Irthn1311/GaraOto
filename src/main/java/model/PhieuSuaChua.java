package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty; // Import for BooleanProperty
import javafx.beans.property.SimpleBooleanProperty; // Import for SimpleBooleanProperty
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuSuaChua {
    private IntegerProperty maPhieuSC;
    private IntegerProperty maTiepNhan; // FK to TiepNhan
    private ObjectProperty<LocalDate> ngaySuaChua;
    private StringProperty ghiChu;
    private DoubleProperty tongTien;
    private IntegerProperty maTho; // New: FK to Tho
    private BooleanProperty trangThaiHoanTat; // New: Status for this specific repair slip

    private List<ChiTietPhieuSuaChua_VatTu> chiTietVatTuList;
    private List<ChiTietPhieuSuaChua_TienCong> chiTietTienCongList;

    public PhieuSuaChua() {
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maTiepNhan = new SimpleIntegerProperty();
        this.ngaySuaChua = new SimpleObjectProperty<>();
        this.ghiChu = new SimpleStringProperty();
        this.tongTien = new SimpleDoubleProperty();
        this.maTho = new SimpleIntegerProperty(0); // Initialize with default value
        this.trangThaiHoanTat = new SimpleBooleanProperty(false); // Initialize with default value
        this.chiTietVatTuList = new ArrayList<>();
        this.chiTietTienCongList = new ArrayList<>();
    }

    // Getters
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaTiepNhan() { return maTiepNhan.get(); }
    public LocalDate getNgaySuaChua() { return ngaySuaChua.get(); }
    public String getGhiChu() { return ghiChu.get(); }
    public double getTongTien() { return tongTien.get(); }
    public int getMaTho() { return maTho.get(); } // New Getter
    public boolean isTrangThaiHoanTat() { return trangThaiHoanTat.get(); } // New Getter
    public List<ChiTietPhieuSuaChua_VatTu> getChiTietVatTuList() { return chiTietVatTuList; }
    public List<ChiTietPhieuSuaChua_TienCong> getChiTietTienCongList() { return chiTietTienCongList; }


    // Setters
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaTiepNhan(int maTiepNhan) { this.maTiepNhan.set(maTiepNhan); }
    public void setNgaySuaChua(LocalDate ngaySuaChua) { this.ngaySuaChua.set(ngaySuaChua); }
    public void setGhiChu(String ghiChu) { this.ghiChu.set(ghiChu); }
    public void setTongTien(double tongTien) { this.tongTien.set(tongTien); }
    public void setMaTho(int maTho) { this.maTho.set(maTho); } // New Setter
    public void setTrangThaiHoanTat(boolean trangThaiHoanTat) { this.trangThaiHoanTat.set(trangThaiHoanTat); } // New Setter
    public void setChiTietVatTuList(List<ChiTietPhieuSuaChua_VatTu> chiTietVatTuList) { this.chiTietVatTuList = chiTietVatTuList; }
    public void setChiTietTienCongList(List<ChiTietPhieuSuaChua_TienCong> chiTietTienCongList) { this.chiTietTienCongList = chiTietTienCongList; }



    // Properties for JavaFX binding
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maTiepNhanProperty() { return maTiepNhan; }
    public ObjectProperty<LocalDate> ngaySuaChuaProperty() { return ngaySuaChua; }
    public StringProperty ghiChuProperty() { return ghiChu; }
    public DoubleProperty tongTienProperty() { return tongTien; }
    public IntegerProperty maThoProperty() { return maTho; } // New Property
    public BooleanProperty trangThaiHoanTatProperty() { return trangThaiHoanTat; } // New Property
}
