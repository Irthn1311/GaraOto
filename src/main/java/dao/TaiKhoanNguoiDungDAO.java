package dao;

import model.TaiKhoanNguoiDung;
import database.DBConnection;
import java.sql.*;

public class TaiKhoanNguoiDungDAO {

    /**
     * Retrieves a user account by username.
     * @param username The username to search for.
     * @return The TaiKhoanNguoiDung object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TaiKhoanNguoiDung getTaiKhoanByUsername(String username) throws SQLException {
        String sql = "SELECT MaTK, TenDangNhap, MatKhauHash, LoaiTaiKhoan, HoTen FROM TaiKhoanNguoiDung WHERE TenDangNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanNguoiDung taiKhoan = new TaiKhoanNguoiDung();
                    taiKhoan.setMaTK(rs.getInt("MaTK"));
                    taiKhoan.setTenDangNhap(rs.getString("TenDangNhap"));
                    taiKhoan.setMatKhauHash(rs.getString("MatKhauHash"));
                    taiKhoan.setLoaiTaiKhoan(rs.getString("LoaiTaiKhoan"));
                    taiKhoan.setHoTen(rs.getString("HoTen"));
                    return taiKhoan;
                }
            }
        }
        return null;
    }

    /**
     * Adds a new user account to the database.
     * Note: The password should be hashed *before* calling this method.
     * @param taiKhoan The TaiKhoanNguoiDung object to add.
     * @return The generated MaTK (ID) of the new account, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addTaiKhoan(TaiKhoanNguoiDung taiKhoan) throws SQLException {
        String sql = "INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, LoaiTaiKhoan, HoTen) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, taiKhoan.getTenDangNhap());
            pstmt.setString(2, taiKhoan.getMatKhauHash());
            pstmt.setString(3, taiKhoan.getLoaiTaiKhoan());
            pstmt.setString(4, taiKhoan.getHoTen());

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
}
    