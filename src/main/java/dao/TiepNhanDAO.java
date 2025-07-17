package dao;

import model.TiepNhan;
import database.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList; // Added import
import java.util.List; // Added import

public class TiepNhanDAO {

    /**
     * Adds a new TiepNhan (acceptance record) to the database.
     * @param tiepNhan The TiepNhan object to add.
     * @return The generated MaTiepNhan (ID) of the new record, or -1 if insertion fails.
     * @throws SQLException if a database access error occurs.
     */
    public int addTiepNhan(TiepNhan tiepNhan) throws SQLException {
        String sql = "INSERT INTO TiepNhan (BienSo, NgayTiepNhan, TongTienNo, TrangThaiHoanTat, TinhTrangXe, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tiepNhan.getBienSo());
            pstmt.setDate(2, Date.valueOf(tiepNhan.getNgayTiepNhan()));
            pstmt.setDouble(3, tiepNhan.getTongTienNo());
            pstmt.setBoolean(4, tiepNhan.isTrangThaiHoanTat());
            pstmt.setString(5, tiepNhan.getTinhTrangXe());
            pstmt.setString(6, tiepNhan.getTrangThai());

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
     * Retrieves TiepNhan records for a specific date,
     * including related car, owner, and car brand information for display.
     * @param ngayTiepNhan The date to filter records by.
     * @return An ObservableList of TiepNhan objects.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<TiepNhan> getTiepNhanByNgayTiepNhan(LocalDate ngayTiepNhan) throws SQLException {
        ObservableList<TiepNhan> tiepNhanList = FXCollections.observableArrayList();
        String sql = "SELECT tn.MaTiepNhan, tn.BienSo, tn.NgayTiepNhan, tn.TongTienNo, tn.TrangThaiHoanTat, tn.TinhTrangXe, tn.TrangThai, " +
                "x.MaHieuXe, x.MaChuXe, " +
                "cx.TenChuXe, cx.DienThoai, cx.DiaChi, " +
                "hx.TenHieuXe " +
                "FROM TiepNhan tn " +
                "JOIN Xe x ON tn.BienSo = x.BienSo " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "WHERE tn.NgayTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(ngayTiepNhan));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TiepNhan tiepNhan = new TiepNhan();
                    tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    tiepNhan.setBienSo(rs.getString("BienSo"));
                    tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                    tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                    tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                    tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                    tiepNhan.setTrangThai(rs.getString("TrangThai"));

                    // Set derived properties for TableView display
                    tiepNhan.setTenChuXe(rs.getString("TenChuXe"));
                    tiepNhan.setDienThoaiChuXe(rs.getString("DienThoai"));
                    tiepNhan.setDiaChiChuXe(rs.getString("DiaChi"));
                    tiepNhan.setTenHieuXe(rs.getString("TenHieuXe"));

                    tiepNhanList.add(tiepNhan);
                }
            }
        }
        return tiepNhanList;
    }

    /**
     * Retrieves TiepNhan records by car's license plate (BienSo),
     * including related car, owner, and car brand information for display.
     * This method is crucial for the SuaChuaController to find existing acceptance records.
     * @param bienSo The license plate to filter records by.
     * @return An ObservableList of TiepNhan objects.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<TiepNhan> getTiepNhanByBienSo(String bienSo) throws SQLException {
        ObservableList<TiepNhan> tiepNhanList = FXCollections.observableArrayList();
        String sql = "SELECT tn.MaTiepNhan, tn.BienSo, tn.NgayTiepNhan, tn.TongTienNo, tn.TrangThaiHoanTat, tn.TinhTrangXe, tn.TrangThai, " +
                "x.MaHieuXe, x.MaChuXe, " +
                "cx.TenChuXe, cx.DienThoai, cx.DiaChi, " +
                "hx.TenHieuXe " +
                "FROM TiepNhan tn " +
                "JOIN Xe x ON tn.BienSo = x.BienSo " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "WHERE tn.BienSo = ? ORDER BY tn.NgayTiepNhan DESC"; // Order by date to get most recent first
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bienSo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TiepNhan tiepNhan = new TiepNhan();
                    tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    tiepNhan.setBienSo(rs.getString("BienSo"));
                    tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                    tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                    tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                    tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                    tiepNhan.setTrangThai(rs.getString("TrangThai"));

                    // Set derived properties for TableView display
                    tiepNhan.setTenChuXe(rs.getString("TenChuXe"));
                    tiepNhan.setDienThoaiChuXe(rs.getString("DienThoai"));
                    tiepNhan.setDiaChiChuXe(rs.getString("DiaChi"));
                    tiepNhan.setTenHieuXe(rs.getString("TenHieuXe"));

                    tiepNhanList.add(tiepNhan);
                }
            }
        }
        return tiepNhanList;
    }

    public ObservableList<TiepNhan> getOustandingTiepNhanByBienSo(String bienSo) throws SQLException {
        ObservableList<TiepNhan> tiepNhanList = FXCollections.observableArrayList();
        String sql = "SELECT tn.MaTiepNhan, tn.BienSo, tn.NgayTiepNhan, tn.TongTienNo, tn.TrangThaiHoanTat, tn.TinhTrangXe, tn.TrangThai, " +
                "x.MaHieuXe, x.MaChuXe, " +
                "cx.TenChuXe, cx.DienThoai, cx.DiaChi, " +
                "hx.TenHieuXe " +
                "FROM TiepNhan tn " +
                "JOIN Xe x ON tn.BienSo = x.BienSo " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "WHERE tn.BienSo = ? AND tn.TongTienNo > 0 ORDER BY tn.NgayTiepNhan DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bienSo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TiepNhan tiepNhan = new TiepNhan();
                    tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    tiepNhan.setBienSo(rs.getString("BienSo"));
                    tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                    tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                    tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                    tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                    tiepNhan.setTrangThai(rs.getString("TrangThai"));

                    // Set derived properties for TableView display
                    tiepNhan.setTenChuXe(rs.getString("TenChuXe"));
                    tiepNhan.setDienThoaiChuXe(rs.getString("DienThoai"));
                    tiepNhan.setDiaChiChuXe(rs.getString("DiaChi"));
                    tiepNhan.setTenHieuXe(rs.getString("TenHieuXe"));

                    tiepNhanList.add(tiepNhan);
                }
            }
        }
        return tiepNhanList;
    }

    /**
     * Retrieves all TiepNhan records, including related car, owner, and car brand information for display.
     * This method is useful for reports like outstanding debt.
     * @return An ObservableList of all TiepNhan objects.
     * @throws SQLException if a database access error occurs.
     */
    public ObservableList<TiepNhan> getAllTiepNhan() throws SQLException {
        ObservableList<TiepNhan> tiepNhanList = FXCollections.observableArrayList();
        String sql = "SELECT tn.MaTiepNhan, tn.BienSo, tn.NgayTiepNhan, tn.TongTienNo, tn.TrangThaiHoanTat, tn.TinhTrangXe, tn.TrangThai, " +
                "x.MaHieuXe, x.MaChuXe, " +
                "cx.TenChuXe, cx.DienThoai, cx.DiaChi, " +
                "hx.TenHieuXe " +
                "FROM TiepNhan tn " +
                "JOIN Xe x ON tn.BienSo = x.BienSo " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TiepNhan tiepNhan = new TiepNhan();
                tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                tiepNhan.setBienSo(rs.getString("BienSo"));
                tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                tiepNhan.setTrangThai(rs.getString("TrangThai"));

                // Set derived properties for TableView display
                tiepNhan.setTenChuXe(rs.getString("TenChuXe"));
                tiepNhan.setDienThoaiChuXe(rs.getString("DienThoai"));
                tiepNhan.setDiaChiChuXe(rs.getString("DiaChi"));
                tiepNhan.setTenHieuXe(rs.getString("TenHieuXe"));

                tiepNhanList.add(tiepNhan);
            }
        }
        return tiepNhanList;
    }

    /**
     * Retrieves TiepNhan records within a specified date range.
     * This method is useful for reports like acceptance counts.
     * @param fromDate The start date of the range (inclusive).
     * @param toDate The end date of the range (inclusive).
     * @return A list of TiepNhan objects within the date range.
     * @throws SQLException if a database access error occurs.
     */
    public List<TiepNhan> getTiepNhanByDateRange(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<TiepNhan> tiepNhanList = new ArrayList<>();
        String sql = "SELECT MaTiepNhan, BienSo, NgayTiepNhan, TongTienNo, TrangThaiHoanTat, TinhTrangXe, TrangThai FROM TiepNhan WHERE NgayTiepNhan BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TiepNhan tiepNhan = new TiepNhan();
                    tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    tiepNhan.setBienSo(rs.getString("BienSo"));
                    tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                    tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                    tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                    tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                    tiepNhan.setTrangThai(rs.getString("TrangThai"));
                    tiepNhanList.add(tiepNhan);
                }
            }
        }
        return tiepNhanList;
    }

    /**
     * Updates the total debt and completion status for a TiepNhan record.
     * @param maTiepNhan The ID of the TiepNhan record to update.
     * @param tongTienNo The new total debt amount.
     * @param trangThaiHoanTat The new completion status.
     * @throws SQLException if a database access error occurs.
     */
    public void updateTongTienNoAndTrangThai(int maTiepNhan, double tongTienNo, boolean trangThaiHoanTat) throws SQLException {
        String sql = "UPDATE TiepNhan SET TongTienNo = ?, TrangThaiHoanTat = ? WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, tongTienNo);
            pstmt.setBoolean(2, trangThaiHoanTat);
            pstmt.setInt(3, maTiepNhan);
            pstmt.executeUpdate();
        }
    }
    
    public void updatePaymentStatus(int maTiepNhan, double tienDaTra) throws SQLException {
        String sql = "UPDATE TiepNhan SET TongTienNo = TongTienNo - ?, TrangThai = CASE WHEN (TongTienNo - ?) <= 0 THEN N'Đã giao' ELSE TrangThai END, TrangThaiHoanTat = CASE WHEN (TongTienNo - ?) <= 0 THEN 1 ELSE 0 END WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, tienDaTra);
            pstmt.setDouble(2, tienDaTra);
            pstmt.setDouble(3, tienDaTra);
            pstmt.setInt(4, maTiepNhan);
            pstmt.executeUpdate();
        }
    }

    public void updateTienNoAndTrangThaiXe(int maTiepNhan, double themTienNo, String trangThaiMoi) throws SQLException {
        String sql = "UPDATE TiepNhan SET TongTienNo = TongTienNo + ?, TrangThai = ? WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, themTienNo);
            pstmt.setString(2, trangThaiMoi);
            pstmt.setInt(3, maTiepNhan);
            pstmt.executeUpdate();
        }
    }
    
    public ObservableList<TiepNhan> getAllTiepNhanCoNo() throws SQLException {
        ObservableList<TiepNhan> tiepNhanList = FXCollections.observableArrayList();
        String sql = "SELECT tn.MaTiepNhan, tn.BienSo, tn.NgayTiepNhan, tn.TongTienNo, tn.TrangThaiHoanTat, tn.TinhTrangXe, tn.TrangThai, " +
                "x.MaHieuXe, x.MaChuXe, " +
                "cx.TenChuXe, cx.DienThoai, cx.DiaChi, " +
                "hx.TenHieuXe " +
                "FROM TiepNhan tn " +
                "JOIN Xe x ON tn.BienSo = x.BienSo " +
                "JOIN ChuXe cx ON x.MaChuXe = cx.MaChuXe " +
                "JOIN HieuXe hx ON x.MaHieuXe = hx.MaHieuXe " +
                "WHERE tn.TongTienNo > 0 ORDER BY tn.NgayTiepNhan DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TiepNhan tiepNhan = new TiepNhan();
                    tiepNhan.setMaTiepNhan(rs.getInt("MaTiepNhan"));
                    tiepNhan.setBienSo(rs.getString("BienSo"));
                    tiepNhan.setNgayTiepNhan(rs.getDate("NgayTiepNhan").toLocalDate());
                    tiepNhan.setTongTienNo(rs.getDouble("TongTienNo"));
                    tiepNhan.setTrangThaiHoanTat(rs.getBoolean("TrangThaiHoanTat"));
                    tiepNhan.setTinhTrangXe(rs.getString("TinhTrangXe"));
                    tiepNhan.setTrangThai(rs.getString("TrangThai"));

                    // Set derived properties for TableView display
                    tiepNhan.setTenChuXe(rs.getString("TenChuXe"));
                    tiepNhan.setDienThoaiChuXe(rs.getString("DienThoai"));
                    tiepNhan.setDiaChiChuXe(rs.getString("DiaChi"));
                    tiepNhan.setTenHieuXe(rs.getString("TenHieuXe"));

                    tiepNhanList.add(tiepNhan);
                }
            }
        }
        return tiepNhanList;
    }

    public ObservableList<String> getAllBienSoXe() throws SQLException {
        ObservableList<String> bienSoList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT BienSo FROM XE"; // Lấy tất cả biển số xe không trùng lặp
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                bienSoList.add(rs.getString("BienSo"));
            }
        }
        return bienSoList;
    }

    // New: Update only car condition (TinhTrangXe) for a TiepNhan record
    public void updateTinhTrangXe(int maTiepNhan, String tinhTrangXe) throws SQLException {
        String sql = "UPDATE TiepNhan SET TinhTrangXe = ? WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tinhTrangXe);
            pstmt.setInt(2, maTiepNhan);
            pstmt.executeUpdate();
        }
    }

    // New: Update status (TrangThai) with forward-only semantics handled at controller level.
    // If TrangThai becomes 'Đã xong' then also mark TrangThaiHoanTat = 1
    public void updateTrangThai(int maTiepNhan, String trangThaiMoi) throws SQLException {
        String sql = "UPDATE TiepNhan SET TrangThai = ?, TrangThaiHoanTat = CASE WHEN ? = N'Đã xong' THEN 1 ELSE TrangThaiHoanTat END WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trangThaiMoi);
            pstmt.setString(2, trangThaiMoi);
            pstmt.setInt(3, maTiepNhan);
            pstmt.executeUpdate();
        }
    }

    // New: Delete a TiepNhan record by its ID
    public void deleteTiepNhan(int maTiepNhan) throws SQLException {
        String sql = "DELETE FROM TiepNhan WHERE MaTiepNhan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTiepNhan);
            pstmt.executeUpdate();
        }
    }
}
