package dao;

import model.ChiTietPhieuSuaChua_TienCong;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuSuaChua_TienCongDAO {

    /**
     * Adds a new repair slip detail for labor (ChiTietPhieuSuaChua_TienCong) to the database.
     * @param chiTiet The ChiTietPhieuSuaChua_TienCong object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addChiTietTienCong(ChiTietPhieuSuaChua_TienCong chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuSuaChua_TienCong (MaPhieuSC, MaLoaiTienCong, SoLuong, DonGiaTienCongLucDo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaLoaiTienCong());
            pstmt.setInt(3, chiTiet.getSoLuong());
            pstmt.setDouble(4, chiTiet.getDonGiaTienCongLucDo());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all repair slip details for labor (ChiTietPhieuSuaChua_TienCong) for a given repair slip ID.
     * @param maPhieuSC The ID of the repair slip.
     * @return A list of ChiTietPhieuSuaChua_TienCong objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietPhieuSuaChua_TienCong> getChiTietTienCongByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietPhieuSuaChua_TienCong> chiTietList = new ArrayList<>();
        String sql = "SELECT ctsc.MaPhieuSC, ctsc.MaLoaiTienCong, ctsc.SoLuong, ctsc.DonGiaTienCongLucDo, " +
                "ltc.TenLoaiTienCong " +
                "FROM ChiTietPhieuSuaChua_TienCong ctsc " +
                "JOIN LoaiTienCong ltc ON ctsc.MaLoaiTienCong = ltc.MaLoaiTienCong " +
                "WHERE ctsc.MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuSuaChua_TienCong chiTiet = new ChiTietPhieuSuaChua_TienCong();
                    chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    chiTiet.setMaLoaiTienCong(rs.getInt("MaLoaiTienCong"));
                    chiTiet.setSoLuong(rs.getInt("SoLuong"));
                    chiTiet.setDonGiaTienCongLucDo(rs.getDouble("DonGiaTienCongLucDo"));
                    chiTiet.setTenLoaiTienCong(rs.getString("TenLoaiTienCong"));
                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Updates an existing repair slip detail for labor.
     * @param chiTiet The ChiTietPhieuSuaChua_TienCong object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateChiTietTienCong(ChiTietPhieuSuaChua_TienCong chiTiet) throws SQLException {
        String sql = "UPDATE ChiTietPhieuSuaChua_TienCong SET SoLuong = ?, DonGiaTienCongLucDo = ? WHERE MaPhieuSC = ? AND MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getSoLuong());
            pstmt.setDouble(2, chiTiet.getDonGiaTienCongLucDo());
            pstmt.setInt(3, chiTiet.getMaPhieuSC());
            pstmt.setInt(4, chiTiet.getMaLoaiTienCong());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a repair slip detail for labor.
     * @param maPhieuSC The ID of the repair slip.
     * @param maLoaiTienCong The ID of the labor cost type.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietTienCong(int maPhieuSC, int maLoaiTienCong) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuSuaChua_TienCong WHERE MaPhieuSC = ? AND MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.setInt(2, maLoaiTienCong);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all repair slip details for labor for a given repair slip ID.
     * @param maPhieuSC The ID of the repair slip.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietTienCongByMaPhieuSC(int maPhieuSC) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuSuaChua_TienCong WHERE MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.executeUpdate();
        }
    }
}
