package dao;

import model.ChiTietPhieuSuaChua_VatTu;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuSuaChua_VatTuDAO {

    public void addChiTietVatTu(ChiTietPhieuSuaChua_VatTu chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuSuaChua_VatTu (MaPhieuSC, MaVatTu, SoLuong, DonGiaNhap, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaVatTu());
            pstmt.setInt(3, chiTiet.getSoLuong());
            pstmt.setDouble(4, chiTiet.getDonGiaNhap());
            pstmt.setDouble(5, chiTiet.getThanhTien());
            pstmt.executeUpdate();
        }
    }
    
    public List<ChiTietPhieuSuaChua_VatTu> getChiTietVatTuByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietPhieuSuaChua_VatTu> danhSach = new ArrayList<>();
        String sql = "SELECT ct.MaChiTietVatTu, ct.MaPhieuSC, ct.MaVatTu, v.TenVatTu, ct.SoLuong, ct.DonGiaNhap, ct.ThanhTien " +
                     "FROM ChiTietPhieuSuaChua_VatTu ct " +
                     "JOIN VatTu v ON ct.MaVatTu = v.MaVatTu " +
                     "WHERE ct.MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ChiTietPhieuSuaChua_VatTu ct = new ChiTietPhieuSuaChua_VatTu();
                ct.setMaChiTietVatTu(rs.getInt("MaChiTietVatTu"));
                ct.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                ct.setMaVatTu(rs.getInt("MaVatTu"));
                ct.setTenVatTu(rs.getString("TenVatTu"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGiaNhap(rs.getDouble("DonGiaNhap"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                danhSach.add(ct);
            }
        }
        return danhSach;
    }
    
    public boolean isVatTuUsedInAnyPhieuSuaChua(int maVatTu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietPhieuSuaChua_VatTu WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maVatTu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
