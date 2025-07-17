package dao;

import model.ChiTietPhieuSuaChua_TienCong;
import model.TienCong;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuSuaChua_TienCongDAO {

    /**
     * Adds a new ChiTietPhieuSuaChua_TienCong to the database.
     * @param chiTiet The ChiTietPhieuSuaChua_TienCong object to add.
     * @return The generated MaChiTietTienCong (ID) of the new detail, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChiTietPhieuSuaChua_TienCong(ChiTietPhieuSuaChua_TienCong chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuSuaChua_TienCong (MaPhieuSC, MaTienCong, DonGia, ThanhTien) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaTienCong()); // Changed from getMaLoaiTienCong
            pstmt.setDouble(3, chiTiet.getDonGia());
            pstmt.setDouble(4, chiTiet.getThanhTien());

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
     * Retrieves all ChiTietPhieuSuaChua_TienCong records for a given MaPhieuSC.
     * @param maPhieuSC The ID of the repair slip.
     * @return A list of ChiTietPhieuSuaChua_TienCong objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietPhieuSuaChua_TienCong> getChiTietPhieuSuaChua_TienCongByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietPhieuSuaChua_TienCong> chiTietList = new ArrayList<>();
        String sql = "SELECT ct.MaChiTietTienCong, ct.MaPhieuSC, ct.MaTienCong, ct.DonGia, ct.ThanhTien, tc.NoiDung " +
                     "FROM ChiTietPhieuSuaChua_TienCong ct JOIN TienCong tc ON ct.MaTienCong = tc.MaTienCong " +
                     "WHERE ct.MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuSuaChua_TienCong chiTiet = new ChiTietPhieuSuaChua_TienCong();
                    chiTiet.setMaChiTietTienCong(rs.getInt("MaChiTietTienCong"));
                    chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    chiTiet.setMaTienCong(rs.getInt("MaTienCong")); // Changed from setMaLoaiTienCong
                    chiTiet.setDonGia(rs.getDouble("DonGia"));
                    chiTiet.setThanhTien(rs.getDouble("ThanhTien"));

                    // Populate TienCong object for detailed cost
                    TienCong tienCong = new TienCong();
                    tienCong.setMaTienCong(rs.getInt("MaTienCong"));
                    tienCong.setNoiDung(rs.getString("NoiDung"));
                    tienCong.setDonGia(rs.getDouble("DonGia"));
                    chiTiet.setTienCong(tienCong); // Changed from setLoaiTienCong

                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Updates an existing ChiTietPhieuSuaChua_TienCong record in the database.
     * @param chiTiet The ChiTietPhieuSuaChua_TienCong object with updated information.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateChiTietPhieuSuaChua_TienCong(ChiTietPhieuSuaChua_TienCong chiTiet) throws SQLException {
        String sql = "UPDATE ChiTietPhieuSuaChua_TienCong SET MaPhieuSC = ?, MaTienCong = ?, DonGia = ?, ThanhTien = ? WHERE MaChiTietTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaTienCong()); // Changed from getMaLoaiTienCong
            pstmt.setDouble(3, chiTiet.getDonGia());
            pstmt.setDouble(4, chiTiet.getThanhTien());
            pstmt.setInt(5, chiTiet.getMaChiTietTienCong());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a ChiTietPhieuSuaChua_TienCong record from the database by its ID.
     * @param maChiTietTienCong The ID of the detail to delete.
     * @return True if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteChiTietPhieuSuaChua_TienCong(int maChiTietTienCong) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuSuaChua_TienCong WHERE MaChiTietTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maChiTietTienCong);
            return pstmt.executeUpdate() > 0;
        }
    }
}
