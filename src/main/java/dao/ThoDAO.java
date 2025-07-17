package dao;

import model.Tho;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThoDAO {

    /**
     * Thêm một thợ mới vào cơ sở dữ liệu.
     * @param tho Đối tượng Tho cần thêm.
     * @return ID được tạo của thợ mới, hoặc -1 nếu thất bại.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public int addTho(Tho tho) throws SQLException {
        String sql = "INSERT INTO Tho (TenTho, SoDienThoai, ChuyenMon) VALUES (?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tho.getTenTho());
            pstmt.setString(2, tho.getSoDienThoai());
            pstmt.setString(3, tho.getChuyenMon());

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
     * Lấy tất cả thợ từ cơ sở dữ liệu.
     * @return Danh sách các đối tượng Tho.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public List<Tho> getAllTho() throws SQLException {
        List<Tho> thoList = new ArrayList<>();
        String sql = "SELECT MaTho, TenTho, SoDienThoai, ChuyenMon FROM Tho";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tho tho = new Tho();
                tho.setMaTho(rs.getInt("MaTho"));
                tho.setTenTho(rs.getString("TenTho"));
                tho.setSoDienThoai(rs.getString("SoDienThoai"));
                tho.setChuyenMon(rs.getString("ChuyenMon"));
                thoList.add(tho);
            }
        }
        return thoList;
    }

    /**
     * Lấy một thợ theo ID.
     * @param maTho ID của thợ.
     * @return Đối tượng Tho nếu tìm thấy, ngược lại là null.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public Tho getThoById(int maTho) throws SQLException {
        String sql = "SELECT MaTho, TenTho, SoDienThoai, ChuyenMon FROM Tho WHERE MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTho);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tho tho = new Tho();
                    tho.setMaTho(rs.getInt("MaTho"));
                    tho.setTenTho(rs.getString("TenTho"));
                    tho.setSoDienThoai(rs.getString("SoDienThoai"));
                    tho.setChuyenMon(rs.getString("ChuyenMon"));
                    return tho;
                }
            }
        }
        return null;
    }

    /**
     * Lấy một thợ theo tên.
     * @param tenTho Tên của thợ.
     * @return Đối tượng Tho nếu tìm thấy, ngược lại là null.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public Tho getThoByName(String tenTho) throws SQLException {
        String sql = "SELECT MaTho, TenTho, SoDienThoai, ChuyenMon FROM Tho WHERE TenTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenTho);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tho tho = new Tho();
                    tho.setMaTho(rs.getInt("MaTho"));
                    tho.setTenTho(rs.getString("TenTho"));
                    tho.setSoDienThoai(rs.getString("SoDienThoai"));
                    tho.setChuyenMon(rs.getString("ChuyenMon"));
                    return tho;
                }
            }
        }
        return null;
    }

    /**
     * Lấy một thợ theo số điện thoại.
     * @param dienThoai Số điện thoại của thợ.
     * @return Đối tượng Tho nếu tìm thấy, ngược lại là null.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public Tho getThoByDienThoai(String dienThoai) throws SQLException {
        String sql = "SELECT MaTho, TenTho, SoDienThoai, ChuyenMon FROM Tho WHERE SoDienThoai = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dienThoai);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Tho tho = new Tho();
                    tho.setMaTho(rs.getInt("MaTho"));
                    tho.setTenTho(rs.getString("TenTho"));
                    tho.setSoDienThoai(rs.getString("SoDienThoai"));
                    tho.setChuyenMon(rs.getString("ChuyenMon"));
                    return tho;
                }
            }
        }
        return null;
    }

    /**
     * Cập nhật thông tin thợ trong cơ sở dữ liệu.
     * @param tho Đối tượng Tho với thông tin đã cập nhật.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public void updateTho(Tho tho) throws SQLException {
        String sql = "UPDATE Tho SET TenTho = ?, SoDienThoai = ?, ChuyenMon = ? WHERE MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tho.getTenTho());
            pstmt.setString(2, tho.getSoDienThoai());
            pstmt.setString(3, tho.getChuyenMon());
            pstmt.setInt(4, tho.getMaTho());
            pstmt.executeUpdate();
        }
    }

    /**
     * Xóa một thợ khỏi cơ sở dữ liệu theo ID.
     * @param maTho ID của thợ cần xóa.
     * @throws SQLException nếu có lỗi truy cập cơ sở dữ liệu.
     */
    public void deleteTho(int maTho) throws SQLException {
        String sql = "DELETE FROM Tho WHERE MaTho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTho);
            pstmt.executeUpdate();
        }
    }
}
