package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChiTietSuaChua {
    private IntegerProperty maChiTiet;
    private IntegerProperty maPhieuSC; // FK to PhieuSuaChua
    private IntegerProperty maVatTu; // FK to VatTu (can be NULL in DB, 0 in Java if not used)
    private IntegerProperty soLuong; // Can be NULL in DB, 0 in Java if not used
    private IntegerProperty maLoaiTienCong; // Changed from maTienCong to maLoaiTienCong
    private DoubleProperty thanhTien;

    // Properties for TableView display (not directly from DB, but derived/combined)
    private IntegerProperty stt; // For sequential numbering in TableView
    private StringProperty noiDung; // Name of VatTu or TienCong description
    private StringProperty loai; // "Vật tư" or "Tiền công" or "Vật tư & Công"
    private DoubleProperty donGia; // Unit price of VatTu or TienCong (used for display, typically DonGiaBan for VatTu)

    // References to actual objects for detailed calculations (e.g., DonGiaNhap for VatTu)
    private VatTu vatTu;
    private LoaiTienCong loaiTienCong;

    public ChiTietSuaChua() {
        this.maChiTiet = new SimpleIntegerProperty();
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maVatTu = new SimpleIntegerProperty(0); // Default to 0 for no selection
        this.soLuong = new SimpleIntegerProperty(0); // Default to 0
        this.maLoaiTienCong = new SimpleIntegerProperty(0); // Changed from maTienCong to maLoaiTienCong, Default to 0 for no selection
        this.thanhTien = new SimpleDoubleProperty();

        this.stt = new SimpleIntegerProperty();
        this.noiDung = new SimpleStringProperty();
        this.loai = new SimpleStringProperty();
        this.donGia = new SimpleDoubleProperty();

        this.vatTu = null;
        this.loaiTienCong = null;
    }

    // Getters
    public int getMaChiTiet() { return maChiTiet.get(); }
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaVatTu() { return maVatTu.get(); }
    public int getSoLuong() { return soLuong.get(); }
    public int getMaLoaiTienCong() { return maLoaiTienCong.get(); }
    public double getThanhTien() { return thanhTien.get(); }

    // Setters
    public void setMaChiTiet(int maChiTiet) { this.maChiTiet.set(maChiTiet); }
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setSoLuong(int soLuong) { this.soLuong.set(soLuong); }
    public void setMaLoaiTienCong(int maLoaiTienCong) { this.maLoaiTienCong.set(maLoaiTienCong); }
    public void setThanhTien(double thanhTien) { this.thanhTien.set(thanhTien); }

    // Properties for JavaFX binding
    public IntegerProperty maChiTietProperty() { return maChiTiet; }
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public IntegerProperty soLuongProperty() { return soLuong; }
    public IntegerProperty maLoaiTienCongProperty() { return maLoaiTienCong; }
    public DoubleProperty thanhTienProperty() { return thanhTien; }

    // Getters/Setters/Properties for TableView display
    public int getStt() { return stt.get(); }
    public void setStt(int stt) { this.stt.set(stt); }
    public IntegerProperty sttProperty() { return stt; }

    public String getNoiDung() { return noiDung.get(); }
    public void setNoiDung(String noiDung) { this.noiDung.set(noiDung); }
    public StringProperty noiDungProperty() { return noiDung; }

    public String getLoai() { return loai.get(); }
    public void setLoai(String loai) { this.loai.set(loai); }
    public StringProperty loaiProperty() { return loai; }

    public double getDonGia() { return donGia.get(); }
    public void setDonGia(double donGia) { this.donGia.set(donGia); }
    public DoubleProperty donGiaProperty() { return donGia; }

    // Getters and Setters for VatTu and LoaiTienCong objects
    public VatTu getVatTu() {
        return vatTu;
    }

    public void setVatTu(VatTu vatTu) {
        this.vatTu = vatTu;
    }

    public LoaiTienCong getLoaiTienCong() {
        return loaiTienCong;
    }

    public void setLoaiTienCong(LoaiTienCong loaiTienCong) {
        this.loaiTienCong = loaiTienCong;
    }
}
