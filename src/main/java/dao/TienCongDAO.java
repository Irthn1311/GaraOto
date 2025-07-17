package dao;

import model.TienCong;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TienCongDAO {

    /**
     * Adds a new labor service (TienCong) to the database.
     * @param tienCong The TienCong object to add.
     * @return The generated MaTienCong (ID) of the new labor service, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addTienCong(TienCong tienCong) throws SQLException {
        String sql = "INSERT INTO TienCong (NoiDung, DonGia) VALUES (?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tienCong.getNoiDung());
            pstmt.setDouble(2, tienCong.getDonGia());

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
     * Retrieves all labor services (TienCong) from the database.
     * @return A list of TienCong objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<TienCong> getAllTienCong() throws SQLException {
        List<TienCong> tienCongList = new ArrayList<>();
        String sql = "SELECT MaTienCong, NoiDung, DonGia FROM TienCong";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TienCong tienCong = new TienCong();
                tienCong.setMaTienCong(rs.getInt("MaTienCong"));
                tienCong.setNoiDung(rs.getString("NoiDung"));
                tienCong.setDonGia(rs.getDouble("DonGia"));
                tienCongList.add(tienCong);
            }
        }
        return tienCongList;
    }

    /**
     * Retrieves a labor service (TienCong) by its ID.
     * @param maTienCong The ID of the labor service.
     * @return The TienCong object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TienCong getTienCongById(int maTienCong) throws SQLException {
        String sql = "SELECT MaTienCong, NoiDung, DonGia FROM TienCong WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTienCong);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TienCong tienCong = new TienCong();
                    tienCong.setMaTienCong(rs.getInt("MaTienCong"));
                    tienCong.setNoiDung(rs.getString("NoiDung"));
                    tienCong.setDonGia(rs.getDouble("DonGia"));
                    return tienCong;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a labor service (TienCong) by its content/name.
     * @param noiDung The content/name of the labor service.
     * @return The TienCong object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TienCong getTienCongByName(String noiDung) throws SQLException {
        String sql = "SELECT MaTienCong, NoiDung, DonGia FROM TienCong WHERE NoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, noiDung);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TienCong tienCong = new TienCong();
                    tienCong.setMaTienCong(rs.getInt("MaTienCong"));
                    tienCong.setNoiDung(rs.getString("NoiDung"));
                    tienCong.setDonGia(rs.getDouble("DonGia"));
                    return tienCong;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing labor service (TienCong) in the database.
     * @param tienCong The TienCong object with updated information.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateTienCong(TienCong tienCong) throws SQLException {
        String sql = "UPDATE TienCong SET NoiDung = ?, DonGia = ? WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tienCong.getNoiDung());
            pstmt.setDouble(2, tienCong.getDonGia());
            pstmt.setInt(3, tienCong.getMaTienCong());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a labor service (TienCong) from the database by its ID.
     * @param maTienCong The ID of the labor service to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteTienCong(int maTienCong) throws SQLException {
        String sql = "DELETE FROM TienCong WHERE MaTienCong = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTienCong);
            return pstmt.executeUpdate() > 0;
        }
    }
}
