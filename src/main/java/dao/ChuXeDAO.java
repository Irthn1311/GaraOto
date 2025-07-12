package dao; // Updated package

import model.ChuXe; // Updated import
import database.DBConnection; // Updated import
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuXeDAO {

    /**
     * Adds a new ChuXe to the database.
     * @param chuXe The ChuXe object to add.
     * @return The generated MaChuXe (ID) of the new ChuXe, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChuXe(ChuXe chuXe) throws SQLException {
        String sql = "INSERT INTO ChuXe (TenChuXe, DiaChi, DienThoai, Email) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, chuXe.getTenChuXe());
            pstmt.setString(2, chuXe.getDiaChi());
            pstmt.setString(3, chuXe.getDienThoai());
            pstmt.setString(4, chuXe.getEmail());

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
     * Retrieves a ChuXe from the database by phone number.
     * @param dienThoai The phone number of the ChuXe to retrieve.
     * @return The ChuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public ChuXe getChuXeByDienThoai(String dienThoai) throws SQLException {
        String sql = "SELECT MaChuXe, TenChuXe, DiaChi, DienThoai, Email FROM ChuXe WHERE DienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dienThoai);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ChuXe chuXe = new ChuXe();
                    chuXe.setMaChuXe(rs.getInt("MaChuXe"));
                    chuXe.setTenChuXe(rs.getString("TenChuXe"));
                    chuXe.setDiaChi(rs.getString("DiaChi"));
                    chuXe.setDienThoai(rs.getString("DienThoai"));
                    chuXe.setEmail(rs.getString("Email"));
                    return chuXe;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a ChuXe from the database by MaChuXe.
     * @param maChuXe The ID of the ChuXe to retrieve.
     * @return The ChuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public ChuXe getChuXeById(int maChuXe) throws SQLException {
        String sql = "SELECT MaChuXe, TenChuXe, DiaChi, DienThoai, Email FROM ChuXe WHERE MaChuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maChuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ChuXe chuXe = new ChuXe();
                    chuXe.setMaChuXe(rs.getInt("MaChuXe"));
                    chuXe.setTenChuXe(rs.getString("TenChuXe"));
                    chuXe.setDiaChi(rs.getString("DiaChi"));
                    chuXe.setDienThoai(rs.getString("DienThoai"));
                    chuXe.setEmail(rs.getString("Email"));
                    return chuXe;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing ChuXe's information in the database.
     * @param chuXe The ChuXe object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateChuXe(ChuXe chuXe) throws SQLException {
        String sql = "UPDATE ChuXe SET TenChuXe = ?, DiaChi = ?, DienThoai = ?, Email = ? WHERE MaChuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chuXe.getTenChuXe());
            pstmt.setString(2, chuXe.getDiaChi());
            pstmt.setString(3, chuXe.getDienThoai());
            pstmt.setString(4, chuXe.getEmail());
            pstmt.setInt(5, chuXe.getMaChuXe());
            pstmt.executeUpdate();
        }
    }
}
