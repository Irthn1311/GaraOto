package dao; // Updated package

import model.Xe; // Updated import
import database.DBConnection; // Updated import
import java.sql.*;

public class XeDAO {

    /**
     * Adds a new car to the database.
     * @param xe The Xe object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addXe(Xe xe) throws SQLException {
        String sql = "INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, xe.getBienSo());
            pstmt.setInt(2, xe.getMaHieuXe());
            pstmt.setInt(3, xe.getMaChuXe());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves a car from the database by its license plate (BienSo).
     * This method now joins with ChuXe and HieuXe to get full details.
     * @param bienSo The license plate of the car to retrieve.
     * @return The Xe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public Xe getXeByBienSo(String bienSo) throws SQLException {
        String sql = "SELECT x.BienSo, x.MaHieuXe, x.MaChuXe, cx.TenChuXe, cx.DienThoai, cx.DiaChi, hx.TenHieuXe " +
                "FROM Xe x " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "WHERE x.BienSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bienSo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Xe xe = new Xe();
                    xe.setBienSo(rs.getString("BienSo"));
                    xe.setMaHieuXe(rs.getInt("MaHieuXe"));
                    xe.setMaChuXe(rs.getInt("MaChuXe"));
                    // Set derived properties for display
                    xe.setTenChuXe(rs.getString("TenChuXe"));
                    xe.setDienThoaiChuXe(rs.getString("DienThoai"));
                    xe.setDiaChiChuXe(rs.getString("DiaChi")); // Assuming you add DiaChiChuXe property to Xe entity
                    xe.setTenHieuXe(rs.getString("TenHieuXe"));
                    return xe;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing car's information in the database.
     * Note: BienSo is PK, so it's not updated. Only FKs.
     * @param xe The Xe object with updated information (MaHieuXe, MaChuXe).
     * @throws SQLException if a database access error occurs.
     */
    public void updateXe(Xe xe) throws SQLException {
        String sql = "UPDATE Xe SET MaHieuXe = ?, MaChuXe = ? WHERE BienSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, xe.getMaHieuXe());
            pstmt.setInt(2, xe.getMaChuXe());
            pstmt.setString(3, xe.getBienSo());
            pstmt.executeUpdate();
        }
    }
}
