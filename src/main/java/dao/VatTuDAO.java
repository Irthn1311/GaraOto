package dao;

import model.VatTu;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VatTuDAO {

    /**
     * Retrieves all parts (VatTu) from the database.
     * @return A list of VatTu objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<VatTu> getAllVatTu() throws SQLException {
        List<VatTu> vatTuList = new ArrayList<>();
        String sql = "SELECT MaVatTu, TenVatTu, DonGia, SoLuongTon FROM VatTu";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VatTu vatTu = new VatTu();
                vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                vatTu.setTenVatTu(rs.getString("TenVatTu"));
                vatTu.setDonGia(rs.getDouble("DonGia"));
                vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                vatTuList.add(vatTu);
            }
        }
        return vatTuList;
    }

    /**
     * Retrieves a part (VatTu) by its name.
     * @param tenVatTu The name of the part.
     * @return The VatTu object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public VatTu getVatTuByName(String tenVatTu) throws SQLException {
        String sql = "SELECT MaVatTu, TenVatTu, DonGia, SoLuongTon FROM VatTu WHERE TenVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenVatTu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    VatTu vatTu = new VatTu();
                    vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                    vatTu.setTenVatTu(rs.getString("TenVatTu"));
                    vatTu.setDonGia(rs.getDouble("DonGia"));
                    vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                    return vatTu;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a part (VatTu) by its ID.
     * @param maVatTu The ID of the part.
     * @return The VatTu object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public VatTu getVatTuById(int maVatTu) throws SQLException {
        String sql = "SELECT MaVatTu, TenVatTu, DonGia, SoLuongTon FROM VatTu WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maVatTu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    VatTu vatTu = new VatTu();
                    vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                    vatTu.setTenVatTu(rs.getString("TenVatTu"));
                    vatTu.setDonGia(rs.getDouble("DonGia"));
                    vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                    return vatTu;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing part's information in the database.
     * @param vatTu The VatTu object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateVatTu(VatTu vatTu) throws SQLException {
        String sql = "UPDATE VatTu SET TenVatTu = ?, DonGia = ?, SoLuongTon = ? WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vatTu.getTenVatTu());
            pstmt.setDouble(2, vatTu.getDonGia());
            pstmt.setInt(3, vatTu.getSoLuongTon());
            pstmt.setInt(4, vatTu.getMaVatTu());
            pstmt.executeUpdate();
        }
    }
}
