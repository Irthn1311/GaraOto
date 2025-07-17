package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.TienCong;
import model.VatTu;

public class ChiTietSuaChua {
    private IntegerProperty maChiTiet;
    private IntegerProperty maPhieuSC; // FK to PhieuSuaChua
    private IntegerProperty maVatTu; // FK to VatTu (can be NULL in DB, 0 in Java if not used)
    private IntegerProperty soLuong; // Can be NULL in DB, 0 in Java if not used
    private IntegerProperty maTienCong; // Changed from maLoaiTienCong to maTienCong
    private DoubleProperty thanhTien;

    // Properties for TableView display (not directly from DB, but derived/combined)
    private IntegerProperty stt; // For sequential numbering in TableView
    private StringProperty noiDung; // Name of VatTu or TienCong description
    private StringProperty loai; // "Vật tư" or "Tiền công" or "Vật tư & Công"
    private DoubleProperty donGia; // Unit price of VatTu or TienCong (used for display, typically DonGiaBan for VatTu)

    // New property to specifically hold the cost price of the material at the time of repair
    private DoubleProperty donGiaNhapThoiDiemSuaChua;

    // References to actual objects for detailed calculations (e.g., DonGiaNhap for VatTu)
    private VatTu vatTu;
    private TienCong tienCong;

    public ChiTietSuaChua() {
        this.maChiTiet = new SimpleIntegerProperty();
        this.maPhieuSC = new SimpleIntegerProperty();
        this.maVatTu = new SimpleIntegerProperty(0); // Default to 0 for no selection
        this.soLuong = new SimpleIntegerProperty(0); // Default to 0
        this.maTienCong = new SimpleIntegerProperty(0); // Changed from maLoaiTienCong to maTienCong, Default to 0 for no selection
        this.thanhTien = new SimpleDoubleProperty();

        this.stt = new SimpleIntegerProperty();
        this.noiDung = new SimpleStringProperty();
        this.loai = new SimpleStringProperty();
        this.donGia = new SimpleDoubleProperty();

        this.donGiaNhapThoiDiemSuaChua = new SimpleDoubleProperty(0.0); // Initialize new property

        this.vatTu = null;
        this.tienCong = null;
    }

    // Getters
    public int getMaChiTiet() { return maChiTiet.get(); }
    public int getMaPhieuSC() { return maPhieuSC.get(); }
    public int getMaVatTu() { return maVatTu.get(); }
    public int getSoLuong() { return soLuong.get(); }
    public int getMaTienCong() { return maTienCong.get(); }
    public double getThanhTien() { return thanhTien.get(); }

    // New Getter for donGiaNhapThoiDiemSuaChua
    public double getDonGiaNhapThoiDiemSuaChua() { return donGiaNhapThoiDiemSuaChua.get(); }

    // Setters
    public void setMaChiTiet(int maChiTiet) { this.maChiTiet.set(maChiTiet); }
    public void setMaPhieuSC(int maPhieuSC) { this.maPhieuSC.set(maPhieuSC); }
    public void setMaVatTu(int maVatTu) { this.maVatTu.set(maVatTu); }
    public void setSoLuong(int soLuong) { this.soLuong.set(soLuong); }
    public void setMaTienCong(int maTienCong) { this.maTienCong.set(maTienCong); }
    public void setThanhTien(double thanhTien) { this.thanhTien.set(thanhTien); }

    // New Setter for donGiaNhapThoiDiemSuaChua
    public void setDonGiaNhapThoiDiemSuaChua(double donGiaNhapThoiDiemSuaChua) { this.donGiaNhapThoiDiemSuaChua.set(donGiaNhapThoiDiemSuaChua); }

    // Properties for JavaFX binding
    public IntegerProperty maChiTietProperty() { return maChiTiet; }
    public IntegerProperty maPhieuSCProperty() { return maPhieuSC; }
    public IntegerProperty maVatTuProperty() { return maVatTu; }
    public IntegerProperty soLuongProperty() { return soLuong; }
    public IntegerProperty maTienCongProperty() { return maTienCong; }
    public DoubleProperty thanhTienProperty() { return thanhTien; }

    // New Property for donGiaNhapThoiDiemSuaChua
    public DoubleProperty donGiaNhapThoiDiemSuaChuaProperty() { return donGiaNhapThoiDiemSuaChua; }

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

    // Getters and Setters for VatTu and TienCong objects
    public VatTu getVatTu() {
        return vatTu;
    }

    public void setVatTu(VatTu vatTu) {
        this.vatTu = vatTu;
    }

    public TienCong getTienCong() {
        return tienCong;
    }

    public void setTienCong(TienCong tienCong) {
        this.tienCong = tienCong;
    }
}
