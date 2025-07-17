package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp DBConnection quản lý kết nối đến cơ sở dữ liệu SQL Server.
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=GaraOtoManagementDB;encrypt=true;trustServerCertificate=true;";
    private static final String DB_USER = "sa"; // Thay đổi username của bạn
    private static final String DB_PASSWORD = "12345678";; // Thay đổi password của bạn

    /**
     * Phương thức thiết lập và trả về kết nối đến cơ sở dữ liệu.
     * @return Đối tượng Connection đã được kết nối.
     * @throws SQLException Nếu có lỗi xảy ra trong quá trình kết nối.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found!");
            e.printStackTrace();
            throw new SQLException("SQL Server JDBC Driver not found.", e);
        }
        // Establish the connection
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Phương thức đóng kết nối cơ sở dữ liệu.
     * @param connection Đối tượng Connection cần đóng.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
} 