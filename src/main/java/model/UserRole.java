package model;

/**
 * Enum to represent user roles in the system.
 * Using an enum provides type safety and makes the code more robust and readable.
 */
public enum UserRole {
    GIAM_DOC("GiamDoc", "Giám Đốc"),
    QUAN_LY("QuanLy", "Quản Lý"),
    NHAN_VIEN_TIEP_NHAN("NhanVienTiepNhan", "Nhân viên tiếp nhận"),
    THO_SUA_CHUA("ThoSuaChua", "Thợ sửa chữa"),
    NHAN_VIEN_KHO("NhanVienKho", "Nhân viên kho"),
    KE_TOAN("KeToan", "Kế toán");


    private final String roleName;
    private final String displayName;

    UserRole(String roleName, String displayName) {
        this.roleName = roleName;
        this.displayName = displayName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDisplayName() {
        return displayName;
    }


    /**
     * Finds a UserRole enum by its roleName (the string stored in the database).
     * This is crucial for converting database strings back to enum types.
     *
     * @param roleName The name of the role as a string.
     * @return The corresponding UserRole, or null if no match is found.
     */
    public static UserRole fromRoleName(String roleName) {
        if (roleName == null) {
            return null;
        }
        for (UserRole role : values()) {
            if (role.getRoleName().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        // Handle legacy roles for backward compatibility during transition
        switch (roleName.toLowerCase()) {
            case "quanly":
                return QUAN_LY;
            case "nhanvien":
                // This is ambiguous. Defaulting to a sensible role, but should be updated.
                // For now, let's map "NhanVien" to a role with basic permissions.
                // A better approach would be to force an update in the database.
                return NHAN_VIEN_TIEP_NHAN;
            default:
                return null; // Or throw an IllegalArgumentException
        }
    }

    /**
     * Provides the display name for use in UI components like ComboBoxes.
     *
     * @return The user-friendly display name.
     */
    @Override
    public String toString() {
        return this.displayName;
    }
} 