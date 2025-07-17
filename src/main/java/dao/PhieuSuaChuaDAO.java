package dao;

import model.PhieuSuaChua;
import model.ChiTietPhieuSuaChua_VatTu;
import model.ChiTietPhieuSuaChua_TienCong;
import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuSuaChuaDAO {

    /**
     * Adds a new repair slip (PhieuSuaChua) to the database.
     * @param phieuSuaChua The PhieuSuaChua object to add.
     * @return The generated MaPhieuSC (ID) of the new slip, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addPhieuSuaChua(PhieuSuaChua phieuSuaChua) throws SQLException {
        String sql = "INSERT INTO PhieuSuaChua (MaTiepNhan, NgaySuaChua, GhiChu, TongTien, MaTho, TrangThaiHoanTat) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, phieuSuaChua.getMaTiepNhan());
            pstmt.setDate(2, Date.valueOf(phieuSuaChua.getNgaySuaChua()));
            pstmt.setString(3, phieuSuaChua.getGhiChu());
            pstmt.setDouble(4, phieuSuaChua.getTongTien());
            // Handle nullable MaTho
            if (phieuSuaChua.getMaTho() > 0) { // Assuming 0 means no mechanic selected
                pstmt.setInt(5, phieuSuaChua.getMaTho());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setBoolean(6, phieuSuaChua.isTrangThaiHoanTat());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }
        return generatedId;
    }

    /**
     * Retrieves repair slips within a specified date range.
     * This method fetches basic PhieuSuaChua information without details.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuSuaChua objects within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuSuaChua> getPhieuSuaChuaByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuSuaChua> phieuSuaChuaList = new ArrayList<>();
        String sql = "SELECT MaPhieuSC, MaTiepNhan, NgaySuaChua, GhiChu, TongTien, MaTho, TrangThaiHoanTat FROM PhieuSuaChua WHERE NgaySuaChua BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuSuaChua phieuSuaChua = new PhieuSuaChua();
                    phieuSuaChua.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    phieuSuaChua.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    phieuSuaChua.setNgaySuaChua(rs.getDate("NgaySuaChua").toLocalDate());
                    phieuSuaChua.setGhiChu(rs.getString("GhiChu"));
                    phieuSuaChua.setTongTien(rs.getDouble("TongTien"));
                    phieuSuaChua.setMaTho(rs.getInt("MaTho")); // Set MaTho
                    phieuSuaChua.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat")); // Set TrangThaiHoanTat
                    phieuSuaChuaList.add(phieuSuaChua);
                }
            }
        }
        return phieuSuaChuaList;
    }

    /**
     * Retrieves repair slips within a specified date range, including all associated
     * ChiTietSuaChua (VatTu and TienCong details). This is crucial for profit calculation.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuSuaChua objects, each populated with its ChiTietSuaChuaList.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuSuaChua> getPhieuSuaChuaDetailsByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuSuaChua> phieuSuaChuaList = getPhieuSuaChuaByDateRange(fromDate, toDate);
        
        ChiTietPhieuSuaChua_VatTuDAO vatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO();
        ChiTietPhieuSuaChua_TienCongDAO tienCongDAO = new ChiTietPhieuSuaChua_TienCongDAO();

        for (PhieuSuaChua psc : phieuSuaChuaList) {
            List<ChiTietPhieuSuaChua_VatTu> vatTuList = vatTuDAO.getChiTietVatTuByMaPhieuSC(psc.getMaPhieuSC());
            psc.setChiTietVatTuList(vatTuList);

            List<ChiTietPhieuSuaChua_TienCong> tienCongList = tienCongDAO.getChiTietTienCongByMaPhieuSC(psc.getMaPhieuSC());
            psc.setChiTietTienCongList(tienCongList);
        }
        
        return phieuSuaChuaList;
    }
    
    public boolean isThoUsed(int maTho) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuSuaChua WHERE MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTho);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
