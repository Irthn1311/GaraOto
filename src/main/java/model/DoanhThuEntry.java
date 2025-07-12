package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDate;

public class DoanhThuEntry {
    private ObjectProperty<LocalDate> ngay;
    private IntegerProperty soPhieuSuaChua;
    private DoubleProperty tongTienPhieuSC;
    private DoubleProperty tongTienThu;
    private DoubleProperty tienConNo; // Tiền còn nợ tại cuối ngày đó

    public DoanhThuEntry() {
        this.ngay = new SimpleObjectProperty<>();
        this.soPhieuSuaChua = new SimpleIntegerProperty();
        this.tongTienPhieuSC = new SimpleDoubleProperty();
        this.tongTienThu = new SimpleDoubleProperty();
        this.tienConNo = new SimpleDoubleProperty();
    }

    public DoanhThuEntry(LocalDate ngay, int soPhieuSuaChua, double tongTienPhieuSC, double tongTienThu, double tienConNo) {
        this.ngay = new SimpleObjectProperty<>(ngay);
        this.soPhieuSuaChua = new SimpleIntegerProperty(soPhieuSuaChua);
        this.tongTienPhieuSC = new SimpleDoubleProperty(tongTienPhieuSC);
        this.tongTienThu = new SimpleDoubleProperty(tongTienThu);
        this.tienConNo = new SimpleDoubleProperty(tienConNo);
    }

    // Getters
    public LocalDate getNgay() { return ngay.get(); }
    public int getSoPhieuSuaChua() { return soPhieuSuaChua.get(); }
    public double getTongTienPhieuSC() { return tongTienPhieuSC.get(); }
    public double getTongTienThu() { return tongTienThu.get(); }
    public double getTienConNo() { return tienConNo.get(); }

    // Setters
    public void setNgay(LocalDate ngay) { this.ngay.set(ngay); }
    public void setSoPhieuSuaChua(int soPhieuSuaChua) { this.soPhieuSuaChua.set(soPhieuSuaChua); }
    public void setTongTienPhieuSC(double tongTienPhieuSC) { this.tongTienPhieuSC.set(tongTienPhieuSC); }
    public void setTongTienThu(double tongTienThu) { this.tongTienThu.set(tongTienThu); }
    public void setTienConNo(double tienConNo) { this.tienConNo.set(tienConNo); }

    // Properties for JavaFX binding
    public ObjectProperty<LocalDate> ngayProperty() { return ngay; }
    public IntegerProperty soPhieuSuaChuaProperty() { return soPhieuSuaChua; }
    public DoubleProperty tongTienPhieuSCProperty() { return tongTienPhieuSC; }
    public DoubleProperty tongTienThuProperty() { return tongTienThu; }
    public DoubleProperty tienConNoProperty() { return tienConNo; }
}
