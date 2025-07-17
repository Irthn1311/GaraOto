package dao;

import model.TaiKhoanNguoiDung;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanNguoiDungDAO {

    /**
     * Authenticates a user by username.
     * @param tenDangNhap The username.
     * @return The TaiKhoanNguoiDung object if user exists and is active, null otherwise.
     */
    public TaiKhoanNguoiDung getTaiKhoanByTenDangNhap(String tenDangNhap) throws SQLException {
        String sql = "SELECT * FROM TaiKhoanNguoiDung WHERE TenDangNhap = ? AND TrangThai = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanNguoiDung(
                        rs.getInt("MaTK"),
                        rs.getString("TenDangNhap"),
                        rs.getString("MatKhauHash"),
                        rs.getString("MaPhanQuyen"),
                        rs.getString("HoTen"),
                        rs.getBoolean("TrangThai")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Adds a new user account to the database.
     * @param user The TaiKhoanNguoiDung object to add.
     * @return true if insertion is successful, false otherwise.
     */
    public boolean addTaiKhoanNguoiDung(TaiKhoanNguoiDung user) throws SQLException {
        String sql = "INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, MaPhanQuyen, HoTen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getTenDangNhap());
            pstmt.setString(2, user.getMatKhauHash());
            pstmt.setString(3, user.getMaPhanQuyen());
            pstmt.setString(4, user.getHoTen());
            pstmt.setBoolean(5, user.isTrangThai());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all user accounts from the database.
     * @return A list of TaiKhoanNguoiDung objects.
     */
    public List<TaiKhoanNguoiDung> getAllTaiKhoanNguoiDung() throws SQLException {
        List<TaiKhoanNguoiDung> danhSachTaiKhoan = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoanNguoiDung";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                danhSachTaiKhoan.add(new TaiKhoanNguoiDung(
                    rs.getInt("MaTK"),
                    rs.getString("TenDangNhap"),
                    rs.getString("MatKhauHash"),
                    rs.getString("MaPhanQuyen"),
                    rs.getString("HoTen"),
                    rs.getBoolean("TrangThai")
                ));
            }
        }
        return danhSachTaiKhoan;
    }

    /**
     * Updates an existing user account's information in the database.
     * Note: This method does not update the password.
     * @param user The TaiKhoanNguoiDung object with updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateTaiKhoanNguoiDung(TaiKhoanNguoiDung user) throws SQLException {
        String sql = "UPDATE TaiKhoanNguoiDung SET TenDangNhap = ?, MaPhanQuyen = ?, HoTen = ?, TrangThai = ? WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getTenDangNhap());
            pstmt.setString(2, user.getMaPhanQuyen());
            pstmt.setString(3, user.getHoTen());
            pstmt.setBoolean(4, user.isTrangThai());
            pstmt.setInt(5, user.getMaTK());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a user account from the database by its ID.
     * @param maTK The ID of the user account to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteTaiKhoanNguoiDung(int maTK) throws SQLException {
        String sql = "DELETE FROM TaiKhoanNguoiDung WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTK);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates only the user's password hash.
     * @param maTK The user's ID.
     * @param newMatKhauHash The new hashed password.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateUserPassword(int maTK, String newMatKhauHash) throws SQLException {
        String sql = "UPDATE TaiKhoanNguoiDung SET MatKhauHash = ? WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newMatKhauHash);
            pstmt.setInt(2, maTK);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Checks if a username already exists.
     * @param tenDangNhap The username to check.
     * @return true if the username exists, false otherwise.
     */
    public boolean checkUsernameExists(String tenDangNhap) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TaiKhoanNguoiDung WHERE TenDangNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
