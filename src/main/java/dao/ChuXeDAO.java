package dao;

import model.ChuXe;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChuXeDAO {

    /**
     * Adds a new car owner (ChuXe) to the database.
     * @param chuXe The ChuXe object to add.
     * @return The generated MaChuXe (ID) of the new owner, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChuXe(ChuXe chuXe) throws SQLException {
        String sql = "INSERT INTO ChuXe (TenChuXe, DienThoai, DiaChi, Email) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, chuXe.getTenChuXe());
            pstmt.setString(2, chuXe.getDienThoai());
            pstmt.setString(3, chuXe.getDiaChi());
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
     * Retrieves all car owners (ChuXe) from the database.
     * @return A list of ChuXe objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChuXe> getAllChuXe() throws SQLException {
        List<ChuXe> chuXeList = new ArrayList<>();
        String sql = "SELECT MaChuXe, TenChuXe, DienThoai, DiaChi, Email FROM ChuXe";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ChuXe chuXe = new ChuXe();
                chuXe.setMaChuXe(rs.getInt("MaChuXe"));
                chuXe.setTenChuXe(rs.getString("TenChuXe"));
                chuXe.setDienThoai(rs.getString("DienThoai"));
                chuXe.setDiaChi(rs.getString("DiaChi"));
                chuXe.setEmail(rs.getString("Email"));
                chuXeList.add(chuXe);
            }
        }
        return chuXeList;
    }

    /**
     * Retrieves a car owner (ChuXe) by their ID.
     * @param maChuXe The ID of the car owner.
     * @return The ChuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public ChuXe getChuXeById(int maChuXe) throws SQLException {
        String sql = "SELECT MaChuXe, TenChuXe, DienThoai, DiaChi, Email FROM ChuXe WHERE MaChuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maChuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ChuXe chuXe = new ChuXe();
                    chuXe.setMaChuXe(rs.getInt("MaChuXe"));
                    chuXe.setTenChuXe(rs.getString("TenChuXe"));
                    chuXe.setDienThoai(rs.getString("DienThoai"));
                    chuXe.setDiaChi(rs.getString("DiaChi"));
                    chuXe.setEmail(rs.getString("Email"));
                    return chuXe;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a car owner (ChuXe) by their phone number.
     * @param dienThoai The phone number of the car owner.
     * @return The ChuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public ChuXe getChuXeByDienThoai(String dienThoai) throws SQLException {
        String sql = "SELECT MaChuXe, TenChuXe, DienThoai, DiaChi, Email FROM ChuXe WHERE DienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dienThoai);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ChuXe chuXe = new ChuXe();
                    chuXe.setMaChuXe(rs.getInt("MaChuXe"));
                    chuXe.setTenChuXe(rs.getString("TenChuXe"));
                    chuXe.setDienThoai(rs.getString("DienThoai"));
                    chuXe.setDiaChi(rs.getString("DiaChi"));
                    chuXe.setEmail(rs.getString("Email"));
                    return chuXe;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing car owner (ChuXe) in the database.
     * @param chuXe The ChuXe object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateChuXe(ChuXe chuXe) throws SQLException {
        String sql = "UPDATE ChuXe SET TenChuXe = ?, DienThoai = ?, DiaChi = ?, Email = ? WHERE MaChuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chuXe.getTenChuXe());
            pstmt.setString(2, chuXe.getDienThoai());
            pstmt.setString(3, chuXe.getDiaChi());
            pstmt.setString(4, chuXe.getEmail());
            pstmt.setInt(5, chuXe.getMaChuXe());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a car owner (ChuXe) from the database by their ID.
     * @param maChuXe The ID of the car owner to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChuXe(int maChuXe) throws SQLException {
        String sql = "DELETE FROM ChuXe WHERE MaChuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maChuXe);
            pstmt.executeUpdate();
        }
    }
}
