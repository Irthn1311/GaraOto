package dao; // Updated package

import model.HieuXe; // Updated import
import database.DBConnection; // Updated import
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HieuXeDAO {

    /**
     * Retrieves all car brands (HieuXe) from the database.
     * @return A list of HieuXe objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<HieuXe> getAllHieuXe() throws SQLException {
        List<HieuXe> hieuXeList = new ArrayList<>();
        String sql = "SELECT MaHieuXe, TenHieuXe FROM HieuXe";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                HieuXe hieuXe = new HieuXe();
                hieuXe.setMaHieuXe(rs.getInt("MaHieuXe"));
                hieuXe.setTenHieuXe(rs.getString("TenHieuXe"));
                hieuXeList.add(hieuXe);
            }
        }
        return hieuXeList;
    }

    /**
     * Retrieves a car brand (HieuXe) by its name.
     * @param tenHieuXe The name of the car brand.
     * @return The HieuXe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public HieuXe getHieuXeByName(String tenHieuXe) throws SQLException {
        String sql = "SELECT MaHieuXe, TenHieuXe FROM HieuXe WHERE TenHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenHieuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HieuXe hieuXe = new HieuXe();
                    hieuXe.setMaHieuXe(rs.getInt("MaHieuXe"));
                    hieuXe.setTenHieuXe(rs.getString("TenHieuXe"));
                    return hieuXe;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a system parameter value by its name from ThamSo table.
     * @param paramName The name of the parameter (e.g., "SoXeToiDaMoiNgay").
     * @return The value of the parameter as a String, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    public String getThamSoHeThong(String paramName) throws SQLException {
        String sql = "SELECT GiaTri FROM ThamSo WHERE TenThamSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, paramName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return String.valueOf(rs.getInt("GiaTri")); // ThamSo.GiaTri is INT
                }
            }
        }
        return null;
    }
}
