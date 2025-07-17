package dao;

import model.HieuXe;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HieuXeDAO {

    /**
     * Adds a new car brand (HieuXe) to the database.
     * @param hieuXe The HieuXe object to add.
     * @return The generated MaHieuXe (ID) of the new brand, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addHieuXe(HieuXe hieuXe) throws SQLException {
        String sql = "INSERT INTO HieuXe (TenHieuXe) VALUES (?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, hieuXe.getTenHieuXe());

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
     * Retrieves all car brands (HieuXe) from the database.
     * @return A list of HieuXe objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<HieuXe> getAllHieuXe() throws SQLException {
        List<HieuXe> hieuXeList = new ArrayList<>();
        String sql = "SELECT MaHieuXe, TenHieuXe FROM HieuXe";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HieuXe hieuXe = new HieuXe();
                hieuXe.setMaHieuXe(rs.getInt("MaHieuXe"));
                hieuXe.setTenHieuXe(rs.getString("TenHieuXe"));
                hieuXeList.add(hieuXe);
            }
        }
        return hieuXeList;
    }

    /**
     * Retrieves a car brand (HieuXe) by its ID.
     * @param maHieuXe The ID of the car brand.
     * @return The HieuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public HieuXe getHieuXeById(int maHieuXe) throws SQLException {
        String sql = "SELECT MaHieuXe, TenHieuXe FROM HieuXe WHERE MaHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maHieuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HieuXe hieuXe = new HieuXe();
                    hieuXe.setMaHieuXe(rs.getInt("MaHieuXe"));
                    hieuXe.setTenHieuXe(rs.getString("TenHieuXe"));
                    return hieuXe;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a car brand (HieuXe) by its name.
     * @param tenHieuXe The name of the car brand.
     * @return The HieuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public HieuXe getHieuXeByName(String tenHieuXe) throws SQLException {
        String sql = "SELECT MaHieuXe, TenHieuXe FROM HieuXe WHERE TenHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenHieuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HieuXe hieuXe = new HieuXe();
                    hieuXe.setMaHieuXe(rs.getInt("MaHieuXe"));
                    hieuXe.setTenHieuXe(rs.getString("TenHieuXe"));
                    return hieuXe;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing car brand (HieuXe) in the database.
     * @param hieuXe The HieuXe object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateHieuXe(HieuXe hieuXe) throws SQLException {
        String sql = "UPDATE HieuXe SET TenHieuXe = ? WHERE MaHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hieuXe.getTenHieuXe());
            pstmt.setInt(2, hieuXe.getMaHieuXe());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a car brand (HieuXe) from the database by its ID.
     * @param maHieuXe The ID of the car brand to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteHieuXe(int maHieuXe) throws SQLException {
        String sql = "DELETE FROM HieuXe WHERE MaHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maHieuXe);
            pstmt.executeUpdate();
        }
    }
}
