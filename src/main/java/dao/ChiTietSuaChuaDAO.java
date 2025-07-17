package dao;

import model.ChiTietSuaChua;
import model.VatTu; // Import VatTu to get DonGiaNhap
import model.LoaiTienCong; // Import TienCong
import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChiTietSuaChuaDAO {

    /**
     * Adds a new repair detail (ChiTietSuaChua) to the database.
     * This method now handles insertion into two separate tables: ChiTietPhieuSuaChua_VatTu
     * and ChiTietPhieuSuaChua_TienCong based on whether it's a material or labor detail.
     *
     * @param chiTiet The ChiTietSuaChua object to add.
     * @return The generated ID of the new detail, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addChiTietSuaChua(ChiTietSuaChua chiTiet) throws SQLException {
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection()) {
            if (chiTiet.getMaVatTu() != 0) { // It's a material detail
                String sql = "INSERT INTO ChiTietPhieuSuaChua_VatTu (MaPhieuSC, MaVatTu, SoLuong, DonGiaNhap, ThanhTien) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, chiTiet.getMaPhieuSC());
                    pstmt.setInt(2, chiTiet.getMaVatTu());
                    pstmt.setInt(3, chiTiet.getSoLuong());

                    // Fetch DonGiaNhap from VatTu for consistency or use a value from ChiTiet if available
                    // Assuming VatTuDAO.getVatTuById is reliable for getting the current DonGiaNhap
                    VatTuDAO vatTuDAO = new VatTuDAO();
                    VatTu vatTu = vatTuDAO.getVatTuById(chiTiet.getMaVatTu());
                    if (vatTu != null) {
                        pstmt.setDouble(4, vatTu.getDonGiaBan()); // Use DonGiaNhap from VatTu entity
                    } else {
                        // Fallback or error if VatTu not found
                        pstmt.setDouble(4, 0.0); // Or throw an error
                    }
                    pstmt.setDouble(5, chiTiet.getThanhTien());

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet rs = pstmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                generatedId = rs.getInt(1);
                            }
                        }
                    }
                }
            }

            if (chiTiet.getMaLoaiTienCong() != 0) { // It's a labor detail
                String sql = "INSERT INTO ChiTietPhieuSuaChua_TienCong (MaPhieuSC, MaTienCong, DonGia, ThanhTien) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, chiTiet.getMaPhieuSC());
                    pstmt.setInt(2, chiTiet.getMaLoaiTienCong());

                    // Fetch DonGia from TienCong for consistency
                    LoaiTienCongDAO tienCongDAO = new LoaiTienCongDAO();
                    LoaiTienCong tienCong = tienCongDAO.getLoaiTienCongById(chiTiet.getMaLoaiTienCong());
                    if (tienCong != null) {
                        pstmt.setDouble(3, tienCong.getDonGiaTienCong());
                    } else {
                        pstmt.setDouble(3, 0.0);
                    }
                    pstmt.setDouble(4, chiTiet.getThanhTien());

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        // If both material and labor are added in one ChiTietSuaChua object,
                        // this will overwrite the generatedId from material.
                        // In a real scenario, you might want separate IDs or a more complex join.
                        // For now, we'll return the last generated ID.
                        try (ResultSet rs = pstmt.getGeneratedKeys()) {
                            if (rs.next()) {
                                generatedId = rs.getInt(1);
                            }
                        }
                    }
                }
            }
        }
        return generatedId;
    }


    /**
     * Retrieves repair details (ChiTietSuaChua) within a specified date range,
     * including associated VatTu and TienCong information.
     * This is crucial for reports like material consumption and profit.
     *
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of ChiTietSuaChua objects with populated VatTu and TienCong details.
     * @throws SQLException if a database access error occurs.
     */
    public List<ChiTietSuaChua> getChiTietSuaChuaByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<ChiTietSuaChua> chiTietList = new ArrayList<>();

        // SQL to fetch material details (VatTu)
        String sqlVatTu = "SELECT ctv.MaChiTietVatTu, ctv.MaPhieuSC, ctv.MaVatTu, ctv.SoLuong, ctv.DonGiaNhap, ctv.ThanhTien, " +
                "vt.TenVatTu, vt.DonViTinh, vt.SoLuongTon, vt.DonGiaBan " +
                "FROM ChiTietPhieuSuaChua_VatTu ctv " +
                "JOIN PhieuSuaChua ps ON ctv.MaPhieuSC = ps.MaPhieuSC " +
                "JOIN VatTu vt ON ctv.MaVatTu = vt.MaVatTu " +
                "WHERE ps.NgaySuaChua BETWEEN ? AND ?";

        // SQL to fetch labor details (TienCong)
        String sqlTienCong = "SELECT ctc.MaChiTietTienCong, ctc.MaPhieuSC, ctc.MaTienCong, ctc.DonGia, ctc.ThanhTien, " +
                "tc.NoiDung " +
                "FROM ChiTietPhieuSuaChua_TienCong ctc " +
                "JOIN PhieuSuaChua ps ON ctc.MaPhieuSC = ps.MaPhieuSC " +
                "JOIN TienCong tc ON ctc.MaTienCong = tc.MaTienCong " +
                "WHERE ps.NgaySuaChua BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch material details
            try (PreparedStatement pstmtVatTu = conn.prepareStatement(sqlVatTu)) {
                pstmtVatTu.setDate(1, Date.valueOf(fromDate));
                pstmtVatTu.setDate(2, Date.valueOf(toDate));
                try (ResultSet rs = pstmtVatTu.executeQuery()) {
                    while (rs.next()) {
                        ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                        chiTiet.setMaChiTiet(rs.getInt("MaChiTietVatTu"));
                        chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                        chiTiet.setMaVatTu(rs.getInt("MaVatTu"));
                        chiTiet.setSoLuong(rs.getInt("SoLuong"));
                        chiTiet.setThanhTien(rs.getDouble("ThanhTien"));
                        chiTiet.setNoiDung(rs.getString("TenVatTu"));
                        chiTiet.setLoai("Vật tư");
                        chiTiet.setDonGia(rs.getDouble("DonGiaNhap")); // For display, use DonGiaNhap for cost

                        // Set material details
                        VatTu vatTu = new VatTu();
                        vatTu.setMaVatTu(rs.getInt("MaVatTu"));
                        vatTu.setTenVatTu(rs.getString("TenVatTu"));
                        vatTu.setDonViTinh(rs.getString("DonViTinh"));
                        vatTu.setSoLuongTon(rs.getInt("SoLuongTon"));
                        vatTu.setDonGiaBan(rs.getDouble("DonGiaBan")); // Assuming DonGiaBan is the correct field for DonGiaNhap for consistency

                        // Removed: Attempting to set LoaiTienCong properties for a material detail is incorrect.
                        // LoaiTienCong loaiTienCong = new LoaiTienCong();
                        // loaiTienCong.setMaLoaiTienCong(rs.getInt("MaTienCong"));
                        // loaiTienCong.setTenLoaiTienCong(rs.getString("NoiDung"));
                        // loaiTienCong.setDonGiaTienCong(rs.getDouble("DonGia"));

                        chiTiet.setVatTu(vatTu);
                        // chiTiet.setLoaiTienCong(loaiTienCong); // Removed: Setting LoaiTienCong for a material detail

                        chiTietList.add(chiTiet);
                    }
                }
            }

            // Fetch labor details
            try (PreparedStatement pstmtTienCong = conn.prepareStatement(sqlTienCong)) {
                pstmtTienCong.setDate(1, Date.valueOf(fromDate));
                pstmtTienCong.setDate(2, Date.valueOf(toDate));
                try (ResultSet rs = pstmtTienCong.executeQuery()) {
                    while (rs.next()) {
                        ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                        chiTiet.setMaChiTiet(rs.getInt("MaChiTietTienCong"));
                        chiTiet.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                        chiTiet.setMaLoaiTienCong(rs.getInt("MaTienCong"));
                        chiTiet.setThanhTien(rs.getDouble("ThanhTien"));
                        chiTiet.setNoiDung(rs.getString("NoiDung"));
                        chiTiet.setLoai("Tiền công");
                        chiTiet.setSoLuong(1); // Labor is typically 1 unit
                        chiTiet.setDonGia(rs.getDouble("DonGia")); // For display, use DonGia for cost

                        LoaiTienCong tienCong = new LoaiTienCong();
                        tienCong.setMaLoaiTienCong(rs.getInt("MaTienCong"));
                        tienCong.setTenLoaiTienCong(rs.getString("NoiDung"));
                        tienCong.setDonGiaTienCong(rs.getDouble("DonGia")); // Crucial for profit calculation
                        chiTiet.setLoaiTienCong(tienCong);

                        chiTietList.add(chiTiet);
                    }
                }
            }
        }
        return chiTietList;
    }
}
