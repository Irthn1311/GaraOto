package dao;

import model.PhieuSuaChua;
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
        String sql = "INSERT INTO PhieuSuaChua (MaTiepNhan, NgaySuaChua, GhiChu, TongTien) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, phieuSuaChua.getMaTiepNhan());
            pstmt.setDate(2, Date.valueOf(phieuSuaChua.getNgaySuaChua()));
            pstmt.setString(3, phieuSuaChua.getGhiChu());
            pstmt.setDouble(4, phieuSuaChua.getTongTien());

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
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuSuaChua objects within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuSuaChua> getPhieuSuaChuaByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuSuaChua> phieuSuaChuaList = new ArrayList<>();
        String sql = "SELECT MaPhieuSC, MaTiepNhan, NgaySuaChua, GhiChu, TongTien FROM PhieuSuaChua WHERE NgaySuaChua BETWEEN ? AND ?";
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
                    phieuSuaChuaList.add(phieuSuaChua);
                }
            }
        }
        return phieuSuaChuaList;
    }
}
