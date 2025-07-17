package dao;

import model.ChiTietPhieuSuaChua_TienCong;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuSuaChua_TienCongDAO {

    public void addChiTietTienCong(ChiTietPhieuSuaChua_TienCong chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuSuaChua_TienCong (MaPhieuSC, MaTienCong, DonGia, ThanhTien) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaTienCong());
            pstmt.setDouble(3, chiTiet.getDonGia());
            pstmt.setDouble(4, chiTiet.getThanhTien());
            pstmt.executeUpdate();
        }
    }

    public List<ChiTietPhieuSuaChua_TienCong> getChiTietTienCongByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietPhieuSuaChua_TienCong> danhSach = new ArrayList<>();
        String sql = "SELECT ct.MaChiTietTienCong, ct.MaPhieuSC, ct.MaTienCong, tc.NoiDung, ct.DonGia, ct.ThanhTien " +
                     "FROM ChiTietPhieuSuaChua_TienCong ct " +
                     "JOIN TienCong tc ON ct.MaTienCong = tc.MaTienCong " +
                     "WHERE ct.MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ChiTietPhieuSuaChua_TienCong ct = new ChiTietPhieuSuaChua_TienCong();
                ct.setMaChiTietTienCong(rs.getInt("MaChiTietTienCong"));
                ct.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                ct.setMaTienCong(rs.getInt("MaTienCong"));
                ct.setNoiDungTienCong(rs.getString("NoiDung"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                danhSach.add(ct);
            }
        }
        return danhSach;
    }
    
    public boolean isTienCongUsed(int maTienCong) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietPhieuSuaChua_TienCong WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTienCong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
