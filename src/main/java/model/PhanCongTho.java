package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class PhanCongTho {
    private IntegerProperty maPhieuSC;
    private IntegerProperty maLoaiTienCong;
    private IntegerProperty maTho;
    private ObjectProperty<LocalDate> ngayPhanCong;

    // Derived properties for UI display (from Tho and LoaiTienCong tables)
    private StringProperty tenTho; // Changed from hoTenTho
    private StringProperty tenLoaiTienCong;

    public PhanCongTho() {
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maLoaiTienCong = new SimpleIntegerProperty();
        this.maTho = new SimpleIntegerProperty();
        this.ngayPhanCong = new SimpleObjectProperty<>();
        this.tenTho = new SimpleStringProperty(); // Changed
        this.tenLoaiTienCong = new SimpleStringProperty();
    }

    public PhanCongTho(int maPhieuSC, int maLoaiTienCong, int maTho, LocalDate ngayPhanCong) {
        this.maPhieuSC = new SimpleIntegerProperty(maPhieuSC);
        this.maLoaiTienCong = new SimpleIntegerProperty(maLoaiTienCong);
        this.maTho = new SimpleIntegerProperty(maTho);
        this.ngayPhanCong = new SimpleObjectProperty<>(ngayPhanCong);
        this.tenTho = new SimpleStringProperty(); // Changed
        this.tenLoaiTienCong = new SimpleStringProperty();
    }

    // Getters
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaLoaiTienCong() { return maLoaiTienCong.get(); }
    public int getMaTho() { return maTho.get(); }
    public LocalDate getNgayPhanCong() { return ngayPhanCong.get(); }
    public String getTenTho() { return tenTho.get(); } // Changed from getHoTenTho
    public String getTenLoaiTienCong() { return tenLoaiTienCong.get(); }

    // Setters
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaLoaiTienCong(int maLoaiTienCong) { this.maLoaiTienCong.set(maLoaiTienCong); }
    public void setMaTho(int maTho) { this.maTho.set(maTho); }
    public void setNgayPhanCong(LocalDate ngayPhanCong) { this.ngayPhanCong.set(ngayPhanCong); }
    public void setTenTho(String tenTho) { this.tenTho.set(tenTho); } // Changed from setHoTenTho
    public void setTenLoaiTienCong(String tenLoaiTienCong) { this.tenLoaiTienCong.set(tenLoaiTienCong); }

    // Properties for JavaFX binding
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maLoaiTienCongProperty() { return maLoaiTienCong; }
    public IntegerProperty maThoProperty() { return maTho; }
    public ObjectProperty<LocalDate> ngayPhanCongProperty() { return ngayPhanCong; }
    public StringProperty tenThoProperty() { return tenTho; } // Changed from hoTenThoProperty
    public StringProperty tenLoaiTienCongProperty() { return tenLoaiTienCong; }
}
