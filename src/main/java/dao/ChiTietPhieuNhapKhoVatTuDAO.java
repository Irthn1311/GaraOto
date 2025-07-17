package dao;

import model.ChiTietPhieuNhapKhoVatTu;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuNhapKhoVatTuDAO {

    /**
     * Adds a new detail for a parts receipt (ChiTietPhieuNhapKhoVatTu) to the database.
     * @param chiTiet The ChiTietPhieuNhapKhoVatTu object to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addChiTietPhieuNhap(ChiTietPhieuNhapKhoVatTu chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietPhieuNhapKhoVatTu (MaPhieuNhap, MaVatTu, SoLuongNhap, DonGiaNhap) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getMaPhieuNhap());
            pstmt.setInt(2, chiTiet.getMaVatTu());
            pstmt.setInt(3, chiTiet.getSoLuongNhap());
            pstmt.setDouble(4, chiTiet.getDonGiaNhap());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all details for a parts receipt (ChiTietPhieuNhapKhoVatTu) for a given receipt ID.
     * @param maPhieuNhap The ID of the parts receipt.
     * @return A list of ChiTietPhieuNhapKhoVatTu objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietPhieuNhapKhoVatTu> getChiTietPhieuNhapByMaPhieuNhap(int maPhieuNhap) throws SQLException {
        List<ChiTietPhieuNhapKhoVatTu> chiTietList = new ArrayList<>();
        String sql = "SELECT ctpn.MaPhieuNhap, ctpn.MaVatTu, ctpn.SoLuongNhap, ctpn.DonGiaNhap, vt.TenVatTu " +
                "FROM ChiTietPhieuNhapKhoVatTu ctpn " +
                "JOIN VatTu vt ON ctpn.MaVatTu = vt.MaVatTu " +
                "WHERE ctpn.MaPhieuNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhapKhoVatTu chiTiet = new ChiTietPhieuNhapKhoVatTu();
                    chiTiet.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                    chiTiet.setMaVatTu(rs.getInt("MaVatTu"));
                    chiTiet.setSoLuongNhap(rs.getInt("SoLuongNhap"));
                    chiTiet.setDonGiaNhap(rs.getDouble("DonGiaNhap"));
                    chiTiet.setTenVatTu(rs.getString("TenVatTu"));
                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Updates an existing detail for a parts receipt.
     * @param chiTiet The ChiTietPhieuNhapKhoVatTu object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateChiTietPhieuNhap(ChiTietPhieuNhapKhoVatTu chiTiet) throws SQLException {
        String sql = "UPDATE ChiTietPhieuNhapKhoVatTu SET SoLuongNhap = ?, DonGiaNhap = ? WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chiTiet.getSoLuongNhap());
            pstmt.setDouble(2, chiTiet.getDonGiaNhap());
            pstmt.setInt(3, chiTiet.getMaPhieuNhap());
            pstmt.setInt(4, chiTiet.getMaVatTu());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a detail for a parts receipt.
     * @param maPhieuNhap The ID of the parts receipt.
     * @param maVatTu The ID of the part.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietPhieuNhap(int maPhieuNhap, int maVatTu) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuNhapKhoVatTu WHERE MaPhieuNhap = ? AND MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            pstmt.setInt(2, maVatTu);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes all details for a parts receipt for a given receipt ID.
     * @param maPhieuNhap The ID of the parts receipt.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteChiTietByMaPhieuNhap(int maPhieuNhap) throws SQLException {
        String sql = "DELETE FROM ChiTietPhieuNhapKhoVatTu WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            pstmt.executeUpdate();
        }
    }
}
