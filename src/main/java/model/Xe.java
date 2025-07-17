package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Xe {
    private StringProperty bienSo;
    private IntegerProperty maHieuXe;
    private IntegerProperty maChuXe;
    private IntegerProperty doiXe;
    private StringProperty mauSac;
    private IntegerProperty soKMHienTai;

    // Derived properties for UI display
    private StringProperty tenHieuXe;
    private StringProperty tenChuXe;
    private StringProperty dienThoaiChuXe;

    public Xe() {
        this.bienSo = new SimpleStringProperty();
        this.maHieuXe = new SimpleIntegerProperty();
        this.maChuXe = new SimpleIntegerProperty();
        this.doiXe = new SimpleIntegerProperty();
        this.mauSac = new SimpleStringProperty();
        this.soKMHienTai = new SimpleIntegerProperty();

        this.tenHieuXe = new SimpleStringProperty();
        this.tenChuXe = new SimpleStringProperty();
        this.dienThoaiChuXe = new SimpleStringProperty();
    }

    public Xe(String bienSo, int maHieuXe, int maChuXe, int doiXe, String mauSac, int soKMHienTai) {
        this.bienSo = new SimpleStringProperty(bienSo);
        this.maHieuXe = new SimpleIntegerProperty(maHieuXe);
        this.maChuXe = new SimpleIntegerProperty(maChuXe);
        this.doiXe = new SimpleIntegerProperty(doiXe);
        this.mauSac = new SimpleStringProperty(mauSac);
        this.soKMHienTai = new SimpleIntegerProperty(soKMHienTai);

        this.tenHieuXe = new SimpleStringProperty(); // Initialize derived properties
        this.tenChuXe = new SimpleStringProperty();
        this.dienThoaiChuXe = new SimpleStringProperty();
    }

    // Getters
    public String getBienSo() { return bienSo.get(); }
    public int getMaHieuXe() { return maHieuXe.get(); }
    public int getMaChuXe() { return maChuXe.get(); }
    public int getDoiXe() { return doiXe.get(); }
    public String getMauSac() { return mauSac.get(); }
    public int getSoKMHienTai() { return soKMHienTai.get(); }

    public String getTenHieuXe() { return tenHieuXe.get(); }
    public String getTenChuXe() { return tenChuXe.get(); }
    public String getDienThoaiChuXe() { return dienThoaiChuXe.get(); }

    // Setters
    public void setBienSo(String bienSo) { this.bienSo.set(bienSo); }
    public void setMaHieuXe(int maHieuXe) { this.maHieuXe.set(maHieuXe); }
    public void setMaChuXe(int maChuXe) { this.maChuXe.set(maChuXe); }
    public void setDoiXe(int doiXe) { this.doiXe.set(doiXe); }
    public void setMauSac(String mauSac) { this.mauSac.set(mauSac); }
    public void setSoKMHienTai(int soKMHienTai) { this.soKMHienTai.set(soKMHienTai); }

    public void setTenHieuXe(String tenHieuXe) { this.tenHieuXe.set(tenHieuXe); }
    public void setTenChuXe(String tenChuXe) { this.tenChuXe.set(tenChuXe); }
    public void setDienThoaiChuXe(String dienThoaiChuXe) { this.dienThoaiChuXe.set(dienThoaiChuXe); }

    // Properties for JavaFX binding
    public StringProperty bienSoProperty() { return bienSo; }
    public IntegerProperty maHieuXeProperty() { return maHieuXe; }
    public IntegerProperty maChuXeProperty() { return maChuXe; }
    public IntegerProperty doiXeProperty() { return doiXe; }
    public StringProperty mauSacProperty() { return mauSac; }
    public IntegerProperty soKMHienTaiProperty() { return soKMHienTai; }

    public StringProperty tenHieuXeProperty() { return tenHieuXe; }
    public StringProperty tenChuXeProperty() { return tenChuXe; }
    public StringProperty dienThoaiChuXeProperty() { return dienThoaiChuXe; }

    @Override
    public String toString() {
        return bienSo.get();
    }
}
