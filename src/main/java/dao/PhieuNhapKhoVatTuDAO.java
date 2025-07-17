package dao;

import model.PhieuNhapKhoVatTu;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapKhoVatTuDAO {

    /**
     * Adds a new parts receipt (PhieuNhapKhoVatTu) to the database.
     * @param phieuNhap The PhieuNhapKhoVatTu object to add.
     * @return The generated MaPhieuNhap (ID) of the new receipt, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addPhieuNhapKhoVatTu(PhieuNhapKhoVatTu phieuNhap) throws SQLException {
        String sql = "INSERT INTO PhieuNhapKhoVatTu (NgayNhap, MaNhaCungCap, TongTienNhap) VALUES (?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setDate(1, Date.valueOf(phieuNhap.getNgayNhap()));
            pstmt.setInt(2, phieuNhap.getMaNhaCungCap());
            pstmt.setDouble(3, phieuNhap.getTongTienNhap());

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
     * Retrieves all parts receipts (PhieuNhapKhoVatTu) from the database, including supplier name.
     * @return A list of PhieuNhapKhoVatTu objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuNhapKhoVatTu> getAllPhieuNhapKhoVatTu() throws SQLException {
        List<PhieuNhapKhoVatTu> phieuNhapList = new ArrayList<>();
        String sql = "SELECT pn.MaPhieuNhap, pn.NgayNhap, pn.MaNhaCungCap, pn.TongTienNhap, ncc.TenNhaCungCap " +
                "FROM PhieuNhapKhoVatTu pn JOIN NhaCungCap ncc ON pn.MaNhaCungCap = ncc.MaNhaCungCap";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PhieuNhapKhoVatTu phieuNhap = new PhieuNhapKhoVatTu();
                phieuNhap.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                phieuNhap.setNgayNhap(rs.getDate("NgayNhap").toLocalDate());
                phieuNhap.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
                phieuNhap.setTongTienNhap(rs.getDouble("TongTienNhap"));
                phieuNhap.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                phieuNhapList.add(phieuNhap);
            }
        }
        return phieuNhapList;
    }

    /**
     * Retrieves a parts receipt (PhieuNhapKhoVatTu) by its ID, including supplier name.
     * @param maPhieuNhap The ID of the parts receipt.
     * @return The PhieuNhapKhoVatTu object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public PhieuNhapKhoVatTu getPhieuNhapKhoVatTuById(int maPhieuNhap) throws SQLException {
        String sql = "SELECT pn.MaPhieuNhap, pn.NgayNhap, pn.MaNhaCungCap, pn.TongTienNhap, ncc.TenNhaCungCap " +
                "FROM PhieuNhapKhoVatTu pn JOIN NhaCungCap ncc ON pn.MaNhaCungCap = ncc.MaNhaCungCap " +
                "WHERE pn.MaPhieuNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PhieuNhapKhoVatTu phieuNhap = new PhieuNhapKhoVatTu();
                    phieuNhap.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                    phieuNhap.setNgayNhap(rs.getDate("NgayNhap").toLocalDate());
                    phieuNhap.setMaNhaCungCap(rs.getInt("MaNhaCungCap"));
                    phieuNhap.setTongTienNhap(rs.getDouble("TongTienNhap"));
                    phieuNhap.setTenNhaCungCap(rs.getString("TenNhaCungCap"));
                    return phieuNhap;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing parts receipt (PhieuNhapKhoVatTu) in the database.
     * @param phieuNhap The PhieuNhapKhoVatTu object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updatePhieuNhapKhoVatTu(PhieuNhapKhoVatTu phieuNhap) throws SQLException {
        String sql = "UPDATE PhieuNhapKhoVatTu SET NgayNhap = ?, MaNhaCungCap = ?, TongTienNhap = ? WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(phieuNhap.getNgayNhap()));
            pstmt.setInt(2, phieuNhap.getMaNhaCungCap());
            pstmt.setDouble(3, phieuNhap.getTongTienNhap());
            pstmt.setInt(4, phieuNhap.getMaPhieuNhap());
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a parts receipt (PhieuNhapKhoVatTu) from the database by its ID.
     * Note: This will likely require deleting associated ChiTietPhieuNhapKhoVatTu records first.
     * @param maPhieuNhap The ID of the parts receipt to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deletePhieuNhapKhoVatTu(int maPhieuNhap) throws SQLException {
        // First, delete child records in ChiTietPhieuNhapKhoVatTu
        ChiTietPhieuNhapKhoVatTuDAO chiTietDAO = new ChiTietPhieuNhapKhoVatTuDAO();
        chiTietDAO.deleteChiTietByMaPhieuNhap(maPhieuNhap);

        String sql = "DELETE FROM PhieuNhapKhoVatTu WHERE MaPhieuNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuNhap);
            pstmt.executeUpdate();
        }
    }
    
    public boolean isNhaCungCapUsed(int maNhaCungCap) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhieuNhapKhoVatTu WHERE MaNhaCungCap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maNhaCungCap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
