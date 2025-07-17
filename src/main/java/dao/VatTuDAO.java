package dao;

import model.VatTu;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VatTuDAO {

    /**
     * Adds a new material (VatTu) to the database.
     * @param vatTu The VatTu object to add.
     * @return The generated MaVatTu (ID) of the new material, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addVatTu(VatTu vatTu) throws SQLException {
        String sql = "INSERT INTO VatTu (TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, vatTu.getTenVatTu());
            pstmt.setDouble(2, vatTu.getDonGiaBan());
            pstmt.setInt(3, vatTu.getSoLuongTon());
            pstmt.setString(4, vatTu.getDonViTinh());
            pstmt.setInt(5, vatTu.getMucTonKhoToiThieu());

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
     * Retrieves all materials (VatTu) from the database.
     * @return A list of VatTu objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<VatTu> getAllVatTu() throws SQLException {
        List<VatTu> vatTuList = new ArrayList<>();
        String sql = "SELECT MaVatTu, TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu FROM VatTu";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                VatTu vatTu = new VatTu();
                vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                vatTu.setTenVatTu(rs.getString("TenVatTu"));
                vatTu.setDonGiaBan(rs.getDouble("DonGiaBan"));
                vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                vatTu.setDonViTinh(rs.getString("DonViTinh"));
                vatTu.setMucTonKhoToiThieu(rs.getInt("MucTonKhoToiThieu"));
                vatTuList.add(vatTu);
            }
        }
        return vatTuList;
    }

    /**
     * Retrieves a material (VatTu) by its ID.
     * @param maVatTu The ID of the material.
     * @return The VatTu object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public VatTu getVatTuById(int maVatTu) throws SQLException {
        String sql = "SELECT MaVatTu, TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu FROM VatTu WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maVatTu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    VatTu vatTu = new VatTu();
                    vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                    vatTu.setTenVatTu(rs.getString("TenVatTu"));
                    vatTu.setDonGiaBan(rs.getDouble("DonGiaBan"));
                    vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                    vatTu.setDonViTinh(rs.getString("DonViTinh"));
                    vatTu.setMucTonKhoToiThieu(rs.getInt("MucTonKhoToiThieu"));
                    return vatTu;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a material (VatTu) by its name.
     * @param tenVatTu The name of the material.
     * @return The VatTu object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public VatTu getVatTuByName(String tenVatTu) throws SQLException {
        String sql = "SELECT MaVatTu, TenVatTu, DonGiaBan, SoLuongTon, DonViTinh, MucTonKhoToiThieu FROM VatTu WHERE TenVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenVatTu);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    VatTu vatTu = new VatTu();
                    vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                    vatTu.setTenVatTu(rs.getString("TenVatTu"));
                    vatTu.setDonGiaBan(rs.getDouble("DonGiaBan"));
                    vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                    vatTu.setDonViTinh(rs.getString("DonViTinh"));
                    vatTu.setMucTonKhoToiThieu(rs.getInt("MucTonKhoToiThieu"));
                    return vatTu;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing material (VatTu) in the database.
     * @param vatTu The VatTu object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateVatTu(VatTu vatTu) throws SQLException {
        String sql = "UPDATE VatTu SET TenVatTu = ?, DonGiaBan = ?, SoLuongTon = ?, DonViTinh = ?, MucTonKhoToiThieu = ? WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vatTu.getTenVatTu());
            pstmt.setDouble(2, vatTu.getDonGiaBan());
            pstmt.setInt(3, vatTu.getSoLuongTon());
            pstmt.setString(4, vatTu.getDonViTinh());
            pstmt.setInt(5, vatTu.getMucTonKhoToiThieu());
            pstmt.setInt(6, vatTu.getMaVatTu());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a material (VatTu) from the database by its ID.
     * @param maVatTu The ID of the material to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteVatTu(int maVatTu) throws SQLException {
        String sql = "DELETE FROM VatTu WHERE MaVatTu = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maVatTu);
            pstmt.executeUpdate();
        }
    }
}
