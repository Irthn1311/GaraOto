package dao;

import model.Xe;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XeDAO {

    /**
     * Adds a new vehicle (Xe) to the database.
     * @param xe The Xe object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addXe(Xe xe) throws SQLException {
        String sql = "INSERT INTO Xe (BienSo, MaHieuXe, MaChuXe, DoiXe, MauSac, SoKMHienTai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, xe.getBienSo());
            pstmt.setInt(2, xe.getMaHieuXe());
            pstmt.setInt(3, xe.getMaChuXe());
            pstmt.setInt(4, xe.getDoiXe());
            pstmt.setString(5, xe.getMauSac());
            pstmt.setInt(6, xe.getSoKMHienTai());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all vehicles (Xe) from the database, including joined data for HieuXe and ChuXe.
     * @return A list of Xe objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<Xe> getAllXe() throws SQLException {
        List<Xe> xeList = new ArrayList<>();
        String sql = "SELECT x.BienSo, x.MaHieuXe, x.MaChuXe, x.DoiXe, x.MauSac, x.SoKMHienTai, " +
                "hx.TenHieuXe, cx.TenChuXe, cx.DienThoai " +
                "FROM Xe x " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Xe xe = new Xe();
                xe.setBienSo(rs.getString("BienSo"));
                xe.setMaHieuXe(rs.getInt("MaHieuXe"));
                xe.setMaChuXe(rs.getInt("MaChuXe"));
                xe.setDoiXe(rs.getInt("DoiXe"));
                xe.setMauSac(rs.getString("MauSac"));
                xe.setSoKMHienTai(rs.getInt("SoKMHienTai"));
                xe.setTenHieuXe(rs.getString("TenHieuXe"));
                xe.setTenChuXe(rs.getString("TenChuXe"));
                xe.setDienThoaiChuXe(rs.getString("DienThoai"));
                xeList.add(xe);
            }
        }
        return xeList;
    }

    /**
     * Retrieves a vehicle (Xe) by its license plate (BienSo), including joined data.
     * @param bienSo The license plate of the vehicle.
     * @return The Xe object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public Xe getXeByBienSo(String bienSo) throws SQLException {
        String sql = "SELECT x.BienSo, x.MaHieuXe, x.MaChuXe, x.DoiXe, x.MauSac, x.SoKMHienTai, " +
                "hx.TenHieuXe, cx.TenChuXe, cx.DienThoai " +
                "FROM Xe x " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
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
                    xe.setDoiXe(rs.getInt("DoiXe"));
                    xe.setMauSac(rs.getString("MauSac"));
                    xe.setSoKMHienTai(rs.getInt("SoKMHienTai"));
                    xe.setTenHieuXe(rs.getString("TenHieuXe"));
                    xe.setTenChuXe(rs.getString("TenChuXe"));
                    xe.setDienThoaiChuXe(rs.getString("DienThoai"));
                    return xe;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing vehicle (Xe) in the database.
     * @param xe The Xe object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateXe(Xe xe) throws SQLException {
        String sql = "UPDATE Xe SET MaHieuXe = ?, MaChuXe = ?, DoiXe = ?, MauSac = ?, SoKMHienTai = ? WHERE BienSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, xe.getMaHieuXe());
            pstmt.setInt(2, xe.getMaChuXe());
            pstmt.setInt(3, xe.getDoiXe());
            pstmt.setString(4, xe.getMauSac());
            pstmt.setInt(5, xe.getSoKMHienTai());
            pstmt.setString(6, xe.getBienSo());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a vehicle (Xe) from the database by its license plate.
     * @param bienSo The license plate of the vehicle to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteXe(String bienSo) throws SQLException {
        String sql = "DELETE FROM Xe WHERE BienSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bienSo);
            pstmt.executeUpdate();
        }
    }
    
    public boolean isHieuXeUsed(int maHieuXe) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Xe WHERE MaHieuXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maHieuXe);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
