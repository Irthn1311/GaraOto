package dao;

import model.ChiTietSuaChua;
import database.DBConnection;
import java.sql.*;

public class ChiTietSuaChuaDAO {

    /**
     * Adds a new repair detail (ChiTietSuaChua) to the database.
     * @param chiTiet The ChiTietSuaChua object to add.
     * @return The generated MaChiTiet (ID) of the new detail, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChiTietSuaChua(ChiTietSuaChua chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietSuaChua (MaPhieuSC, MaVatTu, SoLuong, MaTienCong, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            // Set MaVatTu and SoLuong to NULL if not used (0 in Java entity)
            if (chiTiet.getMaVatTu() == 0) {
                pstmt.setNull(2, Types.INTEGER);
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(2, chiTiet.getMaVatTu());
                pstmt.setInt(3, chiTiet.getSoLuong());
            }
            // Set MaTienCong to NULL if not used (0 in Java entity)
            if (chiTiet.getMaTienCong() == 0) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, chiTiet.getMaTienCong());
            }
            pstmt.setDouble(5, chiTiet.getThanhTien());

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

    // You might need other methods like getChiTietSuaChuaByPhieuSC later
}
