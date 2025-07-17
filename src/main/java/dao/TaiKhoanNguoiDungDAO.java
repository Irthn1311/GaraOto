package dao;

import model.TaiKhoanNguoiDung;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanNguoiDungDAO {

    /**
     * Authenticates a user by username and hashed password.
     * @param tenDangNhap The username.
     * @param matKhauHash The hashed password.
     * @return The TaiKhoanNguoiDung object if authentication is successful and account is active, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TaiKhoanNguoiDung authenticate(String tenDangNhap, String matKhauHash) throws SQLException {
        String sql = "SELECT MaTK, TenDangNhap, LoaiTaiKhoan, HoTen, TrangThai FROM TaiKhoanNguoiDung WHERE TenDangNhap = ? AND MatKhauHash = ? AND TrangThai = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhauHash);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanNguoiDung user = new TaiKhoanNguoiDung();
                    user.setMaTK(rs.getInt("MaTK"));
                    user.setTenDangNhap(rs.getString("TenDangNhap"));
                    user.setLoaiTaiKhoan(rs.getString("LoaiTaiKhoan"));
                    user.setHoTen(rs.getString("HoTen"));
                    user.setTrangThai(rs.getBoolean("TrangThai"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Adds a new user account to the database.
     * @param user The TaiKhoanNguoiDung object to add.
     * @return The generated MaTK (ID) of the new user, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addTaiKhoanNguoiDung(TaiKhoanNguoiDung user) throws SQLException {
        String sql = "INSERT INTO TaiKhoanNguoiDung (TenDangNhap, MatKhauHash, LoaiTaiKhoan, HoTen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getTenDangNhap());
            pstmt.setString(2, user.getMatKhauHash());
            pstmt.setString(3, user.getLoaiTaiKhoan());
            pstmt.setString(4, user.getHoTen());
            pstmt.setBoolean(5, user.isTrangThai());

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
     * Retrieves all user accounts from the database.
     * @return A list of TaiKhoanNguoiDung objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<TaiKhoanNguoiDung> getAllTaiKhoanNguoiDung() throws SQLException {
        List<TaiKhoanNguoiDung> userList = new ArrayList<>();
        String sql = "SELECT MaTK, TenDangNhap, LoaiTaiKhoan, HoTen, TrangThai FROM TaiKhoanNguoiDung";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TaiKhoanNguoiDung user = new TaiKhoanNguoiDung();
                user.setMaTK(rs.getInt("MaTK"));
                user.setTenDangNhap(rs.getString("TenDangNhap"));
                // user.setMatKhauHash(rs.getString("MatKhauHash")); // Do not retrieve hash for security
                user.setLoaiTaiKhoan(rs.getString("LoaiTaiKhoan"));
                user.setHoTen(rs.getString("HoTen"));
                user.setTrangThai(rs.getBoolean("TrangThai"));
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * Retrieves a user account by its ID.
     * @param maTK The ID of the user account.
     * @return The TaiKhoanNguoiDung object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TaiKhoanNguoiDung getTaiKhoanNguoiDungById(int maTK) throws SQLException {
        String sql = "SELECT MaTK, TenDangNhap, LoaiTaiKhoan, HoTen, TrangThai FROM TaiKhoanNguoiDung WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTK);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanNguoiDung user = new TaiKhoanNguoiDung();
                    user.setMaTK(rs.getInt("MaTK"));
                    user.setTenDangNhap(rs.getString("TenDangNhap"));
                    user.setLoaiTaiKhoan(rs.getString("LoaiTaiKhoan"));
                    user.setHoTen(rs.getString("HoTen"));
                    user.setTrangThai(rs.getBoolean("TrangThai"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a user account by its username.
     * @param tenDangNhap The username of the user account.
     * @return The TaiKhoanNguoiDung object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public TaiKhoanNguoiDung getTaiKhoanNguoiDungByTenDangNhap(String tenDangNhap) throws SQLException {
        String sql = "SELECT MaTK, TenDangNhap, LoaiTaiKhoan, HoTen, TrangThai FROM TaiKhoanNguoiDung WHERE TenDangNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenDangNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TaiKhoanNguoiDung user = new TaiKhoanNguoiDung();
                    user.setMaTK(rs.getInt("MaTK"));
                    user.setTenDangNhap(rs.getString("TenDangNhap"));
                    user.setLoaiTaiKhoan(rs.getString("LoaiTaiKhoan"));
                    user.setHoTen(rs.getString("HoTen"));
                    user.setTrangThai(rs.getBoolean("TrangThai"));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing user account's information in the database.
     * Password hash is updated only if provided (not null/empty).
     * @param user The TaiKhoanNguoiDung object with updated information.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateTaiKhoanNguoiDung(TaiKhoanNguoiDung user) throws SQLException {
        String sql = "UPDATE TaiKhoanNguoiDung SET TenDangNhap = ?, LoaiTaiKhoan = ?, HoTen = ?, TrangThai = ? WHERE MaTK = ?";
        if (user.getMatKhauHash() != null && !user.getMatKhauHash().isEmpty()) {
            sql = "UPDATE TaiKhoanNguoiDung SET TenDangNhap = ?, MatKhauHash = ?, LoaiTaiKhoan = ?, HoTen = ?, TrangThai = ? WHERE MaTK = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getTenDangNhap());
            int paramIndex = 2;
            if (user.getMatKhauHash() != null && !user.getMatKhauHash().isEmpty()) {
                pstmt.setString(paramIndex++, user.getMatKhauHash());
            }
            pstmt.setString(paramIndex++, user.getLoaiTaiKhoan());
            pstmt.setString(paramIndex++, user.getHoTen());
            pstmt.setBoolean(paramIndex++, user.isTrangThai());
            pstmt.setInt(paramIndex, user.getMaTK());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Deletes a user account from the database by its ID.
     * @param maTK The ID of the user account to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteTaiKhoanNguoiDung(int maTK) throws SQLException {
        String sql = "DELETE FROM TaiKhoanNguoiDung WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTK);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    /**
     * Updates the status (active/locked) of a user account.
     * @param maTK The ID of the user account.
     * @param trangThai The new status (true for active, false for locked).
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateTrangThaiTaiKhoan(int maTK, boolean trangThai) throws SQLException {
        String sql = "UPDATE TaiKhoanNguoiDung SET TrangThai = ? WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, trangThai);
            pstmt.setInt(2, maTK);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
