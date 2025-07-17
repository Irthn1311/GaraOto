package dao;

import model.NhaCungCap;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    /**
     * Adds a new supplier (NhaCungCap) to the database.
     * @param nhaCungCap The NhaCungCap object to add.
     * @return The generated MaNhaCungCap (ID) of the new supplier, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addNhaCungCap(NhaCungCap nhaCungCap) throws SQLException {
        String sql = "INSERT INTO NhaCungCap (TenNhaCungCap, DienThoai, DiaChi, Email) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nhaCungCap.getTenNhaCungCap());
            pstmt.setString(2, nhaCungCap.getDienThoai());
            pstmt.setString(3, nhaCungCap.getDiaChi());
            pstmt.setString(4, nhaCungCap.getEmail());

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
     * Retrieves all suppliers (NhaCungCap) from the database.
     * @return A list of NhaCungCap objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<NhaCungCap> getAllNhaCungCap() throws SQLException {
        List<NhaCungCap> nhaCungCapList = new ArrayList<>();
        String sql = "SELECT MaNhaCungCap, TenNhaCungCap, DienThoai, DiaChi, Email FROM NhaCungCap";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                NhaCungCap nhaCungCap = new NhaCungCap();
                nhaCungCap.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
                nhaCungCap.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                nhaCungCap.setDienThoai(rs.getString("DienThoai"));
                nhaCungCap.setDiaChi(rs.getString("DiaChi"));
                nhaCungCap.setEmail(rs.getString("Email"));
                nhaCungCapList.add(nhaCungCap);
            }
        }
        return nhaCungCapList;
    }

    /**
     * Retrieves a supplier (NhaCungCap) by its ID.
     * @param maNhaCungCap The ID of the supplier.
     * @return The NhaCungCap object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public NhaCungCap getNhaCungCapById(int maNhaCungCap) throws SQLException {
        String sql = "SELECT MaNhaCungCap, TenNhaCungCap, DienThoai, DiaChi, Email FROM NhaCungCap WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNhaCungCap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    NhaCungCap nhaCungCap = new NhaCungCap();
                    nhaCungCap.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
                    nhaCungCap.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                    nhaCungCap.setDienThoai(rs.getString("DienThoai"));
                    nhaCungCap.setDiaChi(rs.getString("DiaChi"));
                    nhaCungCap.setEmail(rs.getString("Email"));
                    return nhaCungCap;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing supplier (NhaCungCap) in the database.
     * @param nhaCungCap The NhaCungCap object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateNhaCungCap(NhaCungCap nhaCungCap) throws SQLException {
        String sql = "UPDATE NhaCungCap SET TenNhaCungCap = ?, DienThoai = ?, DiaChi = ?, Email = ? WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nhaCungCap.getTenNhaCungCap());
            pstmt.setString(2, nhaCungCap.getDienThoai());
            pstmt.setString(3, nhaCungCap.getDiaChi());
            pstmt.setString(4, nhaCungCap.getEmail());
            pstmt.setInt(5, nhaCungCap.getMaNhaCungCap());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a supplier (NhaCungCap) from the database by its ID.
     * @param maNhaCungCap The ID of the supplier to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteNhaCungCap(int maNhaCungCap) throws SQLException {
        String sql = "DELETE FROM NhaCungCap WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNhaCungCap);
            pstmt.executeUpdate();
        }
    }
}
