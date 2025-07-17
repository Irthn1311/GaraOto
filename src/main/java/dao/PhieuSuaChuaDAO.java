package dao;

import model.PhieuSuaChua;
import model.ChiTietSuaChua; // Import ChiTietSuaChua
import model.VatTu; // Import VatTu
import model.LoaiTienCong; // Import TienCong
import model.Tho; // Import Tho
import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuSuaChuaDAO {

    /**
     * Adds a new repair slip (PhieuSuaChua) to the database.
     * @param phieuSuaChua The PhieuSuaChua object to add.
     * @return The generated MaPhieuSC (ID) of the new slip, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addPhieuSuaChua(PhieuSuaChua phieuSuaChua) throws SQLException {
        String sql = "INSERT INTO PhieuSuaChua (MaTiepNhan, NgaySuaChua, GhiChu, TongTien, MaTho, TrangThaiHoanTat) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, phieuSuaChua.getMaTiepNhan());
            pstmt.setDate(2, Date.valueOf(phieuSuaChua.getNgaySuaChua()));
            pstmt.setString(3, phieuSuaChua.getGhiChu());
            pstmt.setDouble(4, phieuSuaChua.getTongTien());
            // Handle nullable MaTho
            if (phieuSuaChua.getMaTho() > 0) { // Assuming 0 means no mechanic selected
                pstmt.setInt(5, phieuSuaChua.getMaTho());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setBoolean(6, phieuSuaChua.isTrangThaiHoanTat());

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
     * Retrieves repair slips within a specified date range.
     * This method fetches basic PhieuSuaChua information without details.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuSuaChua objects within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuSuaChua> getPhieuSuaChuaByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuSuaChua> phieuSuaChuaList = new ArrayList<>();
        String sql = "SELECT MaPhieuSC, MaTiepNhan, NgaySuaChua, GhiChu, TongTien, MaTho, TrangThaiHoanTat FROM PhieuSuaChua WHERE NgaySuaChua BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PhieuSuaChua phieuSuaChua = new PhieuSuaChua();
                    phieuSuaChua.setMaPhieuSC(rs.getInt("MaPhieuSC"));
                    phieuSuaChua.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    phieuSuaChua.setNgaySuaChua(rs.getDate("NgaySuaChua").toLocalDate());
                    phieuSuaChua.setGhiChu(rs.getString("GhiChu"));
                    phieuSuaChua.setTongTien(rs.getDouble("TongTien"));
                    phieuSuaChua.setMaTho(rs.getInt("MaTho")); // Set MaTho
                    phieuSuaChua.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat")); // Set TrangThaiHoanTat
                    phieuSuaChuaList.add(phieuSuaChua);
                }
            }
        }
        return phieuSuaChuaList;
    }

    /**
     * Retrieves repair slips within a specified date range, including all associated
     * ChiTietSuaChua (VatTu and TienCong details). This is crucial for profit calculation.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of PhieuSuaChua objects, each populated with its ChiTietSuaChuaList.
     * @throws SQLException if a database access error occurs.
     */
    public List<PhieuSuaChua> getPhieuSuaChuaDetailsByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<PhieuSuaChua> phieuSuaChuaList = new ArrayList<>();
        String sqlPhieuSuaChua = "SELECT MaPhieuSC, MaTiepNhan, NgaySuaChua, GhiChu, TongTien, MaTho, TrangThaiHoanTat FROM PhieuSuaChua WHERE NgaySuaChua BETWEEN ? AND ?";

        // SQL for fetching material details
        String sqlVatTuDetails = "SELECT ct.MaChiTietVatTu, ct.MaVatTu, ct.SoLuong, ct.DonGiaNhap, ct.ThanhTien, vt.TenVatTu, vt.DonViTinh " +
                "FROM ChiTietPhieuSuaChua_VatTu ct " +
                "JOIN VatTu vt ON ct.MaVatTu = vt.MaVatTu " +
                "WHERE ct.MaPhieuSC = ?";

        // SQL for fetching labor details
        String sqlTienCongDetails = "SELECT ct.MaChiTietTienCong, ct.MaTienCong, ct.DonGia, ct.ThanhTien, tc.NoiDung " +
                "FROM ChiTietPhieuSuaChua_TienCong ct " +
                "JOIN TienCong tc ON ct.MaTienCong = tc.MaTienCong " +
                "WHERE ct.MaPhieuSC = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // First, get all PhieuSuaChua records
            try (PreparedStatement pstmtPhieuSC = conn.prepareStatement(sqlPhieuSuaChua)) {
                pstmtPhieuSC.setDate(1, Date.valueOf(fromDate));
                pstmtPhieuSC.setDate(2, Date.valueOf(toDate));

                try (ResultSet rsPhieuSuaSC = pstmtPhieuSC.executeQuery()) {
                    while (rsPhieuSuaSC.next()) {
                        PhieuSuaChua phieuSuaChua = new PhieuSuaChua();
                        phieuSuaChua.setMaPhieuSC(rsPhieuSuaSC.getInt("MaPhieuSC"));
                        phieuSuaChua.setMaTiepNhan(rsPhieuSuaSC.getInt("MaTiepNhan"));
                        phieuSuaChua.setNgaySuaChua(rsPhieuSuaSC.getDate("NgaySuaChua").toLocalDate());
                        phieuSuaChua.setGhiChu(rsPhieuSuaSC.getString("GhiChu"));
                        phieuSuaChua.setTongTien(rsPhieuSuaSC.getDouble("TongTien"));
                        phieuSuaChua.setTrangThaiHoanTat(rsPhieuSuaSC.getBoolean("TrangThaiHoanTat"));
                        // Handle MaTho being NULL
                        int maTho = rsPhieuSuaSC.getInt("MaTho");
                        if (!rsPhieuSuaSC.wasNull()) {
                            phieuSuaChua.setMaTho(maTho);
                        } else {
                            phieuSuaChua.setMaTho(0); // Or some default indicating no mechanic
                        }

                        List<ChiTietSuaChua> chiTietList = new ArrayList<>();

                        // Get material details for this PhieuSuaChua
                        try (PreparedStatement pstmtVatTu = conn.prepareStatement(sqlVatTuDetails)) {
                            pstmtVatTu.setInt(1, phieuSuaChua.getMaPhieuSC());
                            try (ResultSet rsVatTu = pstmtVatTu.executeQuery()) {
                                while (rsVatTu.next()) {
                                    ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                                    chiTiet.setMaPhieuSC(phieuSuaChua.getMaPhieuSC());
                                    chiTiet.setMaVatTu(rsVatTu.getInt("MaVatTu"));
                                    chiTiet.setSoLuong(rsVatTu.getInt("SoLuong"));
                                    chiTiet.setThanhTien(rsVatTu.getDouble("ThanhTien"));

                                    // Populate VatTu object for detailed cost (DonGiaNhap)
                                    VatTu vatTu = new VatTu();
                                    vatTu.setMaVatTu(rsVatTu.getInt("MaVatTu"));
                                    vatTu.setTenVatTu(rsVatTu.getString("TenVatTu"));
                                    vatTu.setDonGiaBan(rsVatTu.getDouble("DonGiaNhap")); // Use DonGiaNhap for cost from ChiTiet table
                                    vatTu.setDonViTinh(rsVatTu.getString("DonViTinh"));
                                    chiTiet.setVatTu(vatTu);

                                    chiTietList.add(chiTiet);
                                }
                            }
                        }

                        // Get labor details for this PhieuSuaChua
                        try (PreparedStatement pstmtTienCong = conn.prepareStatement(sqlTienCongDetails)) {
                            pstmtTienCong.setInt(1, phieuSuaChua.getMaPhieuSC());
                            try (ResultSet rsTienCong = pstmtTienCong.executeQuery()) {
                                while (rsTienCong.next()) {
                                    ChiTietSuaChua chiTiet = new ChiTietSuaChua();
                                    chiTiet.setMaPhieuSC(phieuSuaChua.getMaPhieuSC());
                                    chiTiet.setMaLoaiTienCong(rsTienCong.getInt("MaTienCong"));
                                    chiTiet.setThanhTien(rsTienCong.getDouble("ThanhTien"));

                                    // Populate LoaiTienCong object for detailed cost (DonGia)
                                    LoaiTienCong tienCong = new LoaiTienCong();
                                    tienCong.setMaLoaiTienCong(rsTienCong.getInt("MaTienCong"));
                                    tienCong.setTenLoaiTienCong(rsTienCong.getString("NoiDung"));
                                    tienCong.setDonGiaTienCong(rsTienCong.getDouble("DonGia")); // Use DonGia for cost from TienCong table
                                    chiTiet.setLoaiTienCong(tienCong);

                                    chiTietList.add(chiTiet);
                                }
                            }
                        }
                        phieuSuaChua.setChiTietSuaChuaList(chiTietList);
                        phieuSuaChuaList.add(phieuSuaChua);
                    }
                }
            }
        }
        return phieuSuaChuaList;
    }
}
