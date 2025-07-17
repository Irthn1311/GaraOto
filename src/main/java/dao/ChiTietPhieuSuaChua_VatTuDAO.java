package dao;

import model.ChiTietPhieuSuaChua_VatTu;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuSuaChua_VatTuDAO {

    /**
     * Adds a new repair slip detail for parts (ChiTietPhieuSuaChua_VatTu) to the database.
     * @param chiTiet The ChiTietPhieuSuaChua_VatTu object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addChiTietVatTu(ChiTietPhieuSuaChua_VatTu chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuSuaChua_VatTu (MaPhieuSC, MaVatTu, SoLuongSuDung, DonGiaBanLucDo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setInt(2, chiTiet.getMaVatTu());
            pstmt.setInt(3, chiTiet.getSoLuongSuDung());
            pstmt.setDouble(4, chiTiet.getDonGiaBanLucDo());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all repair slip details for parts (ChiTietPhieuSuaChua_VatTu) for a given repair slip ID.
     * @param maPhieuSC The ID of the repair slip.
     * @return A list of ChiTietPhieuSuaChua_VatTu objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietPhieuSuaChua_VatTu> getChiTietVatTuByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietPhieuSuaChua_VatTu> chiTietList = new ArrayList<>();
        String sql = "SELECT ctsv.MaPhieuSC, ctsv.MaVatTu, ctsv.SoLuongSuDung, ctsv.DonGiaBanLucDo, " +
                "vt.TenVatTu, vt.DonViTinh " +
                "FROM ChiTietPhieuSuaChua_VatTu ctsv " +
                "JOIN VatTu vt ON ctsv.MaVatTu = vt.MaVatTu " +
                "WHERE ctsv.MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuSuaChua_VatTu chiTiet = new ChiTietPhieuSuaChua_VatTu();
                    chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    chiTiet.setMaVatTu(rs.getInt("MaVatTu"));
                    chiTiet.setSoLuongSuDung(rs.getInt("SoLuongSuDung"));
                    chiTiet.setDonGiaBanLucDo(rs.getDouble("DonGiaBanLucDo"));
                    chiTiet.setTenVatTu(rs.getString("TenVatTu"));
                    chiTiet.setDonViTinhVatTu(rs.getString("DonViTinh"));
                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Updates an existing repair slip detail for parts.
     * @param chiTiet The ChiTietPhieuSuaChua_VatTu object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateChiTietVatTu(ChiTietPhieuSuaChua_VatTu chiTiet) throws SQLException {
        String sql = "UPDATE ChiTietPhieuSuaChua_VatTu SET SoLuongSuDung = ?, DonGiaBanLucDo = ? WHERE MaPhieuSC = ? AND MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getSoLuongSuDung());
            pstmt.setDouble(2, chiTiet.getDonGiaBanLucDo());
            pstmt.setInt(3, chiTiet.getMaPhieuSC());
            pstmt.setInt(4, chiTiet.getMaVatTu());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a repair slip detail for parts.
     * @param maPhieuSC The ID of the repair slip.
     * @param maVatTu The ID of the part.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietVatTu(int maPhieuSC, int maVatTu) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuSuaChua_VatTu WHERE MaPhieuSC = ? AND MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.setInt(2, maVatTu);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all repair slip details for parts for a given repair slip ID.
     * @param maPhieuSC The ID of the repair slip.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietVatTuByMaPhieuSC(int maPhieuSC) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuSuaChua_VatTu WHERE MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            pstmt.executeUpdate();
        }
    }
}
