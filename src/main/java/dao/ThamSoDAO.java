package dao;

import model.ThamSo;
import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThamSoDAO {

    /**
     * Retrieves a system parameter by its name.
     * @param tenThamSo The name of the parameter (e.g., "SoXeToiDaMoiNgay", "MucTonKhoToiThieuMacDinh").
     * @return The ThamSo object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public ThamSo getThamSoByName(String tenThamSo) throws SQLException {
        String sql = "SELECT TenThamSo, GiaTri FROM ThamSo WHERE TenThamSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tenThamSo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ThamSo thamSo = new ThamSo();
                    thamSo.setTenThamSo(rs.getString("TenThamSo"));
                    thamSo.setGiaTri(rs.getInt("GiaTri"));
                    return thamSo;
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing system parameter.
     * @param thamSo The ThamSo object with updated information.
     * @throws SQLException if a database access error occurs.
     */
    public void updateThamSo(ThamSo thamSo) throws SQLException {
        String sql = "UPDATE ThamSo SET GiaTri = ? WHERE TenThamSo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, thamSo.getGiaTri());
            pstmt.setString(2, thamSo.getTenThamSo());
            pstmt.executeUpdate();
        }
    }

    /**
     * Retrieves all system parameters.
     * @return A list of ThamSo objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<ThamSo> getAllThamSo() throws SQLException {
        List<ThamSo> thamSoList = new ArrayList<>();
        String sql = "SELECT TenThamSo, GiaTri FROM ThamSo";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ThamSo thamSo = new ThamSo();
                thamSo.setTenThamSo(rs.getString("TenThamSo"));
                thamSo.setGiaTri(rs.getInt("GiaTri"));
                thamSoList.add(thamSo);
            }
        }
        return thamSoList;
    }
}
