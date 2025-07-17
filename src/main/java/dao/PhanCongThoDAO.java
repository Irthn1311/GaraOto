package dao;

import model.PhanCongTho;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhanCongThoDAO {

    /**
     * Adds a new mechanic assignment (PhanCongTho) to the database.
     * @param phanCong The PhanCongTho object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addPhanCongTho(PhanCongTho phanCong) throws SQLException {
        String sql = "INSERT INTO PhanCongTho (MaPhieuSC, MaLoaiTienCong, MaTho, NgayPhanCong) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, phanCong.getMaPhieuSC());
            pstmt.setInt(2, phanCong.getMaLoaiTienCong());
            pstmt.setInt(3, phanCong.getMaTho());
            pstmt.setDate(4, Date.valueOf(phanCong.getNgayPhanCong()));
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all mechanic assignments (PhanCongTho) for a given repair slip and labor type.
     * @param maPhieuSC The ID of the repair slip.
     * @param maLoaiTienCong The ID of the labor cost type.
     * @return A list of PhanCongTho objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhanCongTho> getPhanCongTho(int maPhieuSC, int maLoaiTienCong) throws SQLException {
        List<PhanCongTho> phanCongList = new ArrayList<>();
        String sql = "SELECT pct.MaPhieuSC, pct.MaLoaiTienCong, pct.MaTho, pct.NgayPhanCong, " +
                "t.TenTho, ltc.TenLoaiTienCong " +
                "FROM PhanCongTho pct " +
                "JOIN Tho t ON pct.MaTho = t.MaTho " +
                "JOIN LoaiTienCong ltc ON pct.MaLoaiTienCong = ltc.MaLoaiTienCong " +
                "WHERE pct.MaPhieuSC = ? AND pct.MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.setInt(2, maLoaiTienCong);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhanCongTho phanCong = new PhanCongTho();
                    phanCong.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    phanCong.setMaLoaiTienCong(rs.getInt("MaLoaiTienCong"));
                    phanCong.setMaTho(rs.getInt("MaTho"));
                    phanCong.setNgayPhanCong(rs.getDate("NgayPhanCong").toLocalDate());
                    phanCong.setTenTho(rs.getString("TenTho")); // Changed from HoTenTho
                    phanCong.setTenLoaiTienCong(rs.getString("TenLoaiTienCong"));
                    phanCongList.add(phanCong);
                }
            }
        }
        return phanCongList;
    }

    /**
     * Updates an existing mechanic assignment.
     * Note: Since the primary key is composite (MaPhieuSC, MaLoaiTienCong, MaTho),
     * updating MaTho would essentially be a deletion and re-insertion.
     * This method assumes you're updating NgayPhanCong for an existing assignment.
     * If you need to change the assigned mechanic, you should delete the old and add a new one.
     * @param phanCong The PhanCongTho object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updatePhanCongTho(PhanCongTho phanCong) throws SQLException {
        String sql = "UPDATE PhanCongTho SET NgayPhanCong = ? WHERE MaPhieuSC = ? AND MaLoaiTienCong = ? AND MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(phanCong.getNgayPhanCong()));
            pstmt.setInt(2, phanCong.getMaPhieuSC());
            pstmt.setInt(3, phanCong.getMaLoaiTienCong());
            pstmt.setInt(4, phanCong.getMaTho());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a mechanic assignment.
     * @param maPhieuSC The ID of the repair slip.
     * @param maLoaiTienCong The ID of the labor cost type.
     * @param maTho The ID of the mechanic.
     * @throws SQLException if a database access error occurs.
     */
    public void deletePhanCongTho(int maPhieuSC, int maLoaiTienCong, int maTho) throws SQLException {
        String sql = "DELETE FROM PhanCongTho WHERE MaPhieuSC = ? AND MaLoaiTienCong = ? AND MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.setInt(2, maLoaiTienCong);
            pstmt.setInt(3, maTho);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all mechanic assignments for a given repair slip.
     * @param maPhieuSC The ID of the repair slip.
     * @throws SQLException if a database access error occurs.
     */
    public void deletePhanCongThoByMaPhieuSC(int maPhieuSC) throws SQLException {
        String sql = "DELETE FROM PhanCongTho WHERE MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.executeUpdate();
        }
    }
}
