package dao;

import model.ChiTietSuaChua;
import model.VatTu;
import model.TienCong;
import database.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSuaChuaDAO {

    /**
     * Adds a new ChiTietSuaChua to the database.
     * This method handles both material and labor details.
     * @param chiTiet The ChiTietSuaChua object to add.
     * @return The generated MaChiTietSuaChua (ID) of the new detail, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChiTietSuaChua(ChiTietSuaChua chiTiet) throws SQLException {
        String sql = "INSERT INTO ChiTietSuaChua (MaPhieuSC, Loai, SoLuong, NoiDung, DonGiaNhapThoiDiemSuaChua, MaVatTu, MaTienCong) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, chiTiet.getMaPhieuSC());
            pstmt.setString(2, chiTiet.getLoai());
            pstmt.setInt(3, chiTiet.getSoLuong());
            pstmt.setString(4, chiTiet.getNoiDung());
            pstmt.setDouble(5, chiTiet.getDonGiaNhapThoiDiemSuaChua());

            // Handle MaVatTu (can be null for labor)
            if (chiTiet.getMaVatTu() > 0) {
                pstmt.setInt(6, chiTiet.getMaVatTu());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            // Handle MaTienCong (can be null for materials)
            if (chiTiet.getMaTienCong() > 0) {
                pstmt.setInt(7, chiTiet.getMaTienCong());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

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
     * Retrieves all ChiTietSuaChua records for a given MaPhieuSC.
     * This method also populates associated VatTu or TienCong objects.
     * @param maPhieuSC The ID of the repair slip.
     * @return A list of ChiTietSuaChua objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietSuaChua> getChiTietSuaChuaByMaPhieuSC(int maPhieuSC) throws SQLException {
        List<ChiTietSuaChua> chiTietList = new ArrayList<>();
        String sql = "SELECT cs.*, vt.TenVatTu, vt.DonGiaBan, vt.DonViTinh, tc.NoiDung, tc.DonGia " +
                     "FROM ChiTietSuaChua cs " +
                     "LEFT JOIN VatTu vt ON cs.MaVatTu = vt.MaVatTu " +
                     "LEFT JOIN TienCong tc ON cs.MaTienCong = tc.MaTienCong " +
                     "WHERE cs.MaPhieuSC = ? ORDER BY cs.MaChiTietSuaChua";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                    chiTiet.setMaChiTiet(rs.getInt("MaChiTietSuaChua")); // Changed from setMaChiTietSuaChua
                    chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    chiTiet.setLoai(rs.getString("Loai"));
                    chiTiet.setSoLuong(rs.getInt("SoLuong"));
                    chiTiet.setNoiDung(rs.getString("NoiDung"));
                    chiTiet.setDonGiaNhapThoiDiemSuaChua(rs.getDouble("DonGiaNhapThoiDiemSuaChua"));

                    int maVatTu = rs.getInt("MaVatTu");
                    if (!rs.wasNull()) {
                        chiTiet.setMaVatTu(maVatTu);
                        VatTu vatTu = new VatTu();
                        vatTu.setMaVatTu(maVatTu);
                        vatTu.setTenVatTu(rs.getString("TenVatTu"));
                        vatTu.setDonViTinh(rs.getString("DonViTinh"));
                        // DonGiaBan in VatTu is for selling, use DonGiaNhapThoiDiemSuaChua for cost here
                        vatTu.setDonGiaBan(rs.getDouble("DonGiaNhapThoiDiemSuaChua")); // Using DonGiaNhapThoiDiemSuaChua as DonGiaBan for cost
                        chiTiet.setVatTu(vatTu);
                    }

                    int maTienCong = rs.getInt("MaTienCong");
                    if (!rs.wasNull()) {
                        chiTiet.setMaTienCong(maTienCong);
                        TienCong tienCong = new TienCong();
                        tienCong.setMaTienCong(maTienCong);
                        tienCong.setNoiDung(rs.getString("NoiDung"));
                        tienCong.setDonGia(rs.getDouble("DonGia"));
                        chiTiet.setTienCong(tienCong);
                    }
                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Retrieves ChiTietSuaChua records within a specified date range.
     * This method is used for reports that need aggregated material/labor costs.
     * It primarily fetches material details.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of ChiTietSuaChua objects for materials within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietSuaChua> getChiTietSuaChuaByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<ChiTietSuaChua> chiTietList = new ArrayList<>();
        String sql = "SELECT cts.MaChiTietSuaChua, cts.MaPhieuSC, cts.Loai, cts.SoLuong, cts.NoiDung, cts.DonGiaNhapThoiDiemSuaChua, cts.MaVatTu, cts.MaTienCong, " +
                     "vt.TenVatTu, vt.DonViTinh, tc.NoiDung AS TienCongNoiDung, tc.DonGia AS TienCongDonGia " +
                     "FROM ChiTietSuaChua cts " +
                     "JOIN PhieuSuaChua ps ON cts.MaPhieuSC = ps.MaPhieuSC " +
                     "LEFT JOIN VatTu vt ON cts.MaVatTu = vt.MaVatTu " +
                     "LEFT JOIN TienCong tc ON cts.MaTienCong = tc.MaTienCong " +
                     "WHERE ps.NgaySuaChua BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                    chiTiet.setMaChiTiet(rs.getInt("MaChiTietSuaChua")); // Changed from setMaChiTietSuaChua
                    chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    chiTiet.setLoai(rs.getString("Loai"));
                    chiTiet.setSoLuong(rs.getInt("SoLuong"));
                    chiTiet.setNoiDung(rs.getString("NoiDung"));
                    chiTiet.setDonGiaNhapThoiDiemSuaChua(rs.getDouble("DonGiaNhapThoiDiemSuaChua"));

                    int maVatTu = rs.getInt("MaVatTu");
                    if (!rs.wasNull()) {
                        chiTiet.setMaVatTu(maVatTu);
                        VatTu vatTu = new VatTu();
                        vatTu.setMaVatTu(maVatTu);
                        vatTu.setTenVatTu(rs.getString("TenVatTu"));
                        vatTu.setDonViTinh(rs.getString("DonViTinh"));
                        // DonGiaBan in VatTu is for selling, use DonGiaNhapThoiDiemSuaChua for cost here
                        vatTu.setDonGiaBan(rs.getDouble("DonGiaNhapThoiDiemSuaChua")); // Using DonGiaNhapThoiDiemSuaChua as DonGiaBan for cost
                        chiTiet.setVatTu(vatTu);
                    }

                    int maTienCong = rs.getInt("MaTienCong");
                    if (!rs.wasNull()) {
                        chiTiet.setMaTienCong(maTienCong);
                        TienCong tienCong = new TienCong();
                        tienCong.setMaTienCong(maTienCong);
                        tienCong.setNoiDung(rs.getString("TienCongNoiDung"));
                        tienCong.setDonGia(rs.getDouble("TienCongDonGia"));
                        chiTiet.setTienCong(tienCong);
                    }
                    chiTietList.add(chiTiet);
                }
            }
        }
        return chiTietList;
    }

    /**
     * Updates an existing ChiTietSuaChua record in the database.
     * @param chiTiet The ChiTietSuaChua object with updated information.
     * @return True if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateChiTietSuaChua(ChiTietSuaChua chiTiet) throws SQLException {
        String sql = "UPDATE ChiTietSuaChua SET Loai = ?, SoLuong = ?, NoiDung = ?, DonGiaNhapThoiDiemSuaChua = ?, MaVatTu = ?, MaTienCong = ? WHERE MaChiTietSuaChua = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, chiTiet.getLoai());
            pstmt.setInt(2, chiTiet.getSoLuong());
            pstmt.setString(3, chiTiet.getNoiDung());
            pstmt.setDouble(4, chiTiet.getDonGiaNhapThoiDiemSuaChua());

            // Handle nullable MaVatTu
            if (chiTiet.getMaVatTu() > 0) {
                pstmt.setInt(5, chiTiet.getMaVatTu());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            // Handle nullable MaTienCong
            if (chiTiet.getMaTienCong() > 0) {
                pstmt.setInt(6, chiTiet.getMaTienCong());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.setInt(7, chiTiet.getMaChiTiet()); // Changed from getMaChiTietSuaChua
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a ChiTietSuaChua record from the database by its ID.
     * @param maChiTietSuaChua The ID of the detail to delete.
     * @return True if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteChiTietSuaChua(int maChiTietSuaChua) throws SQLException {
        String sql = "DELETE FROM ChiTietSuaChua WHERE MaChiTietSuaChua = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maChiTietSuaChua);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes all ChiTietSuaChua records associated with a given MaPhieuSC.
     * @param maPhieuSC The ID of the repair slip.
     * @return True if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteChiTietSuaChuaByMaPhieuSC(int maPhieuSC) throws SQLException {
        String sql = "DELETE FROM ChiTietSuaChua WHERE MaPhieuSC = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maPhieuSC);
            return pstmt.executeUpdate() > 0;
        }
    }
}
