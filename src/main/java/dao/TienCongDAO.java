package dao;

import model.TienCong;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TienCongDAO {

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
     * Retrieves a labor service (TienCong) by its content/description.
     * @param noiDung The content/description of the labor service.
     * @return The TienCong object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TienCong getTienCongByNoiDung(String noiDung) throws SQLException {
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
}
