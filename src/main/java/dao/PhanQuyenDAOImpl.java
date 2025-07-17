package dao;

import database.DBConnection;
import model.PhanQuyen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PhanQuyenDAOImpl implements PhanQuyenDAO {

    @Override
    public List<PhanQuyen> getAllPhanQuyen() throws SQLException {
        List<PhanQuyen> danhSachPhanQuyen = new ArrayList<>();
        String sql = "SELECT * FROM PhanQuyen";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String maPhanQuyen = rs.getString("MaPhanQuyen");
                String tenPhanQuyen = rs.getString("TenPhanQuyen");
                danhSachPhanQuyen.add(new PhanQuyen(maPhanQuyen, tenPhanQuyen));
            }
        }
        return danhSachPhanQuyen;
    }

    @Override
    public Set<String> getChucNangByMaPhanQuyen(String maPhanQuyen) throws SQLException {
        Set<String> chucNangs = new HashSet<>();
        String sql = "SELECT MaChucNang FROM ChiTietPhanQuyen WHERE MaPhanQuyen = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhanQuyen);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    chucNangs.add(rs.getString("MaChucNang"));
                }
            }
        }
        return chucNangs;
    }

    @Override
    public boolean addPhanQuyen(PhanQuyen phanQuyen, List<String> chucNangs) throws SQLException {
        String sqlPhanQuyen = "INSERT INTO PhanQuyen (MaPhanQuyen, TenPhanQuyen) VALUES (?, ?)";
        String sqlChiTiet = "INSERT INTO ChiTietPhanQuyen (MaPhanQuyen, MaChucNang) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into PhanQuyen table
            try (PreparedStatement pstmtPhanQuyen = conn.prepareStatement(sqlPhanQuyen)) {
                pstmtPhanQuyen.setString(1, phanQuyen.getMaPhanQuyen());
                pstmtPhanQuyen.setString(2, phanQuyen.getTenPhanQuyen());
                pstmtPhanQuyen.executeUpdate();
            }

            // Insert into ChiTietPhanQuyen table
            try (PreparedStatement pstmtChiTiet = conn.prepareStatement(sqlChiTiet)) {
                for (String maChucNang : chucNangs) {
                    pstmtChiTiet.setString(1, phanQuyen.getMaPhanQuyen());
                    pstmtChiTiet.setString(2, maChucNang);
                    pstmtChiTiet.addBatch();
                }
                pstmtChiTiet.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback on error
            }
            throw e; // Re-throw the exception
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean updatePhanQuyen(PhanQuyen phanQuyen, List<String> chucNangs) throws SQLException {
        String sqlUpdatePhanQuyen = "UPDATE PhanQuyen SET TenPhanQuyen = ? WHERE MaPhanQuyen = ?";
        String sqlDeleteChiTiet = "DELETE FROM ChiTietPhanQuyen WHERE MaPhanQuyen = ?";
        String sqlInsertChiTiet = "INSERT INTO ChiTietPhanQuyen (MaPhanQuyen, MaChucNang) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Update TenPhanQuyen
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdatePhanQuyen)) {
                pstmtUpdate.setString(1, phanQuyen.getTenPhanQuyen());
                pstmtUpdate.setString(2, phanQuyen.getMaPhanQuyen());
                pstmtUpdate.executeUpdate();
            }

            // 2. Delete old permissions
            try (PreparedStatement pstmtDelete = conn.prepareStatement(sqlDeleteChiTiet)) {
                pstmtDelete.setString(1, phanQuyen.getMaPhanQuyen());
                pstmtDelete.executeUpdate();
            }

            // 3. Insert new permissions
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsertChiTiet)) {
                for (String maChucNang : chucNangs) {
                    pstmtInsert.setString(1, phanQuyen.getMaPhanQuyen());
                    pstmtInsert.setString(2, maChucNang);
                    pstmtInsert.addBatch();
                }
                pstmtInsert.executeBatch();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Rollback on error
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean deletePhanQuyen(String maPhanQuyen) throws SQLException {
        // Deletion is cascaded by the foreign key constraint ON DELETE CASCADE,
        // so we only need to delete from the parent table.
        String sql = "DELETE FROM PhanQuyen WHERE MaPhanQuyen = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhanQuyen);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isMaPhanQuyenExists(String maPhanQuyen) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PhanQuyen WHERE MaPhanQuyen = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhanQuyen);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPhanQuyenInUse(String maPhanQuyen) throws SQLException {
        String sql = "SELECT COUNT(*) FROM TaiKhoanNguoiDung WHERE MaPhanQuyen = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maPhanQuyen);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
} 