package dao;

import model.LoaiTienCong;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiTienCongDAO {

    /**
     * Adds a new labor cost type (LoaiTienCong) to the database.
     * @param loaiTienCong The LoaiTienCong object to add.
     * @return The generated MaLoaiTienCong (ID) of the new type, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addLoaiTienCong(LoaiTienCong loaiTienCong) throws SQLException {
        String sql = "INSERT INTO LoaiTienCong (TenLoaiTienCong, DonGiaTienCong) VALUES (?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, loaiTienCong.getTenLoaiTienCong());
            pstmt.setDouble(2, loaiTienCong.getDonGiaTienCong());

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
     * Retrieves all labor cost types (LoaiTienCong) from the database.
     * @return A list of LoaiTienCong objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<LoaiTienCong> getAllLoaiTienCong() throws SQLException {
        List<LoaiTienCong> loaiTienCongList = new ArrayList<>();
        String sql = "SELECT MaLoaiTienCong, TenLoaiTienCong, DonGiaTienCong FROM LoaiTienCong";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LoaiTienCong loaiTienCong = new LoaiTienCong();
                loaiTienCong.setMaLoaiTienCong(rs.getInt("MaLoaiTienCong"));
                loaiTienCong.setTenLoaiTienCong(rs.getString("TenLoaiTienCong"));
                loaiTienCong.setDonGiaTienCong(rs.getDouble("DonGiaTienCong"));
                loaiTienCongList.add(loaiTienCong);
            }
        }
        return loaiTienCongList;
    }

    /**
     * Retrieves a labor cost type (LoaiTienCong) by its ID.
     * @param maLoaiTienCong The ID of the labor cost type.
     * @return The LoaiTienCong object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public LoaiTienCong getLoaiTienCongById(int maLoaiTienCong) throws SQLException {
        String sql = "SELECT MaLoaiTienCong, TenLoaiTienCong, DonGiaTienCong FROM LoaiTienCong WHERE MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maLoaiTienCong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LoaiTienCong loaiTienCong = new LoaiTienCong();
                    loaiTienCong.setMaLoaiTienCong(rs.getInt("MaLoaiTienCong"));
                    loaiTienCong.setTenLoaiTienCong(rs.getString("TenLoaiTienCong"));
                    loaiTienCong.setDonGiaTienCong(rs.getDouble("DonGiaTienCong"));
                    return loaiTienCong;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a labor cost type (LoaiTienCong) by its name.
     * @param tenLoaiTienCong The name of the labor cost type.
     * @return The LoaiTienCong object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public LoaiTienCong getLoaiTienCongByName(String tenLoaiTienCong) throws SQLException {
        String sql = "SELECT MaLoaiTienCong, TenLoaiTienCong, DonGiaTienCong FROM LoaiTienCong WHERE TenLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenLoaiTienCong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LoaiTienCong loaiTienCong = new LoaiTienCong();
                    loaiTienCong.setMaLoaiTienCong(rs.getInt("MaLoaiTienCong"));
                    loaiTienCong.setTenLoaiTienCong(rs.getString("TenLoaiTienCong"));
                    loaiTienCong.setDonGiaTienCong(rs.getDouble("DonGiaTienCong"));
                    return loaiTienCong;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing labor cost type (LoaiTienCong) in the database.
     * @param loaiTienCong The LoaiTienCong object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateLoaiTienCong(LoaiTienCong loaiTienCong) throws SQLException {
        String sql = "UPDATE LoaiTienCong SET TenLoaiTienCong = ?, DonGiaTienCong = ? WHERE MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loaiTienCong.getTenLoaiTienCong());
            pstmt.setDouble(2, loaiTienCong.getDonGiaTienCong());
            pstmt.setInt(3, loaiTienCong.getMaLoaiTienCong());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a labor cost type (LoaiTienCong) from the database by its ID.
     * @param maLoaiTienCong The ID of the labor cost type to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteLoaiTienCong(int maLoaiTienCong) throws SQLException {
        String sql = "DELETE FROM LoaiTienCong WHERE MaLoaiTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maLoaiTienCong);
            pstmt.executeUpdate();
        }
    }
}
