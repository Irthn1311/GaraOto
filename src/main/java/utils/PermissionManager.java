package utils;

import dao.PhanQuyenDAO;
import dao.PhanQuyenDAOImpl;
import model.TaiKhoanNguoiDung;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Singleton class to manage the current user's session and permissions.
 */
public class PermissionManager {

    private static PermissionManager instance;
    private TaiKhoanNguoiDung currentUser;
    private Set<String> userPermissions;

    private final PhanQuyenDAO phanQuyenDAO;

    private PermissionManager() {
        // Initialize with a default set of empty permissions.
        this.userPermissions = new HashSet<>();
        // In a real application, you might use dependency injection here.
        this.phanQuyenDAO = new PhanQuyenDAOImpl();
    }

    /**
     * Gets the single instance of the PermissionManager.
     * @return The singleton instance.
     */
    public static synchronized PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    /**
     * Sets the current logged-in user and loads their permissions.
     * @param user The user who has logged in.
     */
    public void setCurrentUser(TaiKhoanNguoiDung user) {
        this.currentUser = user;
        if (user != null && user.getMaPhanQuyen() != null) {
            loadUserPermissions(user.getMaPhanQuyen());
        } else {
            // If user is null (logged out) or has no role, clear permissions.
            this.userPermissions.clear();
        }
    }

    /**
     * Retrieves the currently logged-in user.
     * @return The current TaiKhoanNguoiDung, or null if no one is logged in.
     */
    public TaiKhoanNguoiDung getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if the current user has a specific permission.
     * @param maChucNang The permission code to check (e.g., "Q1", "Q12").
     * @return true if the user has the permission, false otherwise.
     */
    public boolean hasPermission(String maChucNang) {
        if (currentUser == null) {
            return false; // No user, no permissions.
        }
        // A special case for "GIAMDOC" (Director) who has all permissions.
        if ("GIAMDOC".equals(currentUser.getMaPhanQuyen())) {
            return true;
        }
        return userPermissions.contains(maChucNang);
    }

    /**
     * Clears the current user session and permissions.
     */
    public void clearSession() {
        this.currentUser = null;
        this.userPermissions.clear();
    }

    /**
     * Loads the permissions for a given role from the database.
     * @param maPhanQuyen The role ID to load permissions for.
     */
    private void loadUserPermissions(String maPhanQuyen) {
        try {
            this.userPermissions = phanQuyenDAO.getChucNangByMaPhanQuyen(maPhanQuyen);
        } catch (SQLException e) {
            // In a real app, show an error dialog to the user.
            System.err.println("Error loading user permissions for role: " + maPhanQuyen);
            e.printStackTrace();
            // Clear permissions as a safety measure.
            this.userPermissions = Collections.emptySet();
        }
    }
} 