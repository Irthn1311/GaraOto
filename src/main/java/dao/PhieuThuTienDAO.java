package dao;

import model.PhieuThuTien;
import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuThuTienDAO {

    /**
     * Adds a new payment receipt (PhieuThuTien) to the database.
     * @param phieuThuTien The PhieuThuTien object to add.
     * @return The generated MaPhieuThu (ID) of the new receipt, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addPhieuThuTien(PhieuThuTien phieuThuTien) throws SQLException {
        String sql = "INSERT INTO PhieuThuTien (MaTiepNhan, NgayThu, SoTienThu) VALUES (?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, phieuThuTien.getMaTiepNhan());
            pstmt.setDate(2, Date.valueOf(phieuThuTien.getNgayThu()));
            pstmt.setDouble(3, phieuThuTien.getSoTienThu());

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
     * Retrieves payment receipts within a specified date range.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuThuTien objects within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuThuTien> getPhieuThuTienByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuThuTien> phieuThuTienList = new ArrayList<>();
        String sql = "SELECT MaPhieuThu, MaTiepNhan, NgayThu, SoTienThu FROM PhieuThuTien WHERE NgayThu BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuThuTien phieuThuTien = new PhieuThuTien();
                    phieuThuTien.setMaPhieuThu(rs.getInt("MaPhieuThu"));
                    phieuThuTien.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    phieuThuTien.setNgayThu(rs.getDate("NgayThu").toLocalDate());
                    phieuThuTien.setSoTienThu(rs.getDouble("SoTienThu"));
                    phieuThuTienList.add(phieuThuTien);
                }
            }
        }
        return phieuThuTienList;
    }
}
