package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.TaiKhoanNguoiDung;
import utils.AlertUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainLayoutController {

    @FXML
    private Label lblLoggedInUser; // In sidebar
    @FXML
    private Label lblCurrentUser;  // In top bar
    @FXML
    private AnchorPane contentArea;

    // Navigation Buttons
    @FXML
    private Button btnTiepNhan;
    @FXML
    private Button btnSuaChua;
    @FXML
    private Button btnThuTien;
    @FXML
    private Button btnBaoCao;
    @FXML
    private Button btnCauHinh;
    @FXML
    private Button btnQuanLyHieuXe; // FXML ID for QuanLyHieuXe button
    @FXML
    private Button btnQuanLyTienCong; // FXML ID for QuanLyTienCong button
    @FXML
    private Button btnQuanLyTho; // FXML ID for QuanLyTho button
    @FXML
    private Button btnQuanLyNhaCungCap; // FXML ID for QuanLyNhaCungCap button
    @FXML
    private Button btnQuanLyVatTu; // New FXML ID for QuanLyVatTu button
    @FXML
    private Button btnDangXuat;

    private TaiKhoanNguoiDung loggedInUser; // To store logged-in user info

    // --- Định nghĩa các hằng số Vai trò ---
    public static final String ROLE_GIAM_DOC = "GiamDoc";
    public static final String ROLE_QUAN_LY = "QuanLy";
    public static final String ROLE_NHAN_VIEN_TIEP_NHAN = "NhanVienTiepNhan";
    public static final String ROLE_THO_SUA_CHUA = "ThoSuaChua";
    public static final String ROLE_NHAN_VIEN_KHO = "NhanVienKho";
    public static final String ROLE_KE_TOAN = "KeToan";
    // --- Kết thúc định nghĩa Vai trò ---

    @FXML
    public void initialize() {
        // The initial view loading will now be handled by setLoggedInUser
        // after the user successfully logs in and their role is known.
        // So, we remove the direct loadView call here.
    }

    /**
     * Sets the logged-in user information and updates UI based on their role.
     * This method is called from LoginController after successful login.
     * @param user The TaiKhoanNguoiDung object of the logged-in user.
     */
    public void setLoggedInUser(TaiKhoanNguoiDung user) {
        this.loggedInUser = user;
        if (loggedInUser != null) {
            lblLoggedInUser.setText("Xin chào, " + loggedInUser.getHoTen() + "!");
            lblCurrentUser.setText("Chào mừng, " + loggedInUser.getHoTen() + "!");
            updateButtonVisibilityByRole(); // Update button visibility based on role
            loadViewBasedOnRole(); // Load default view after user is set and buttons are configured
        }
    }

    /**
     * Updates the visibility and disable status of navigation buttons based on the logged-in user's role.
     */
    private void updateButtonVisibilityByRole() {
        String userRole = loggedInUser.getLoaiTaiKhoan();

        // Default to invisible/disabled for all buttons, then enable based on role
        List<Button> allButtons = Arrays.asList(btnTiepNhan, btnSuaChua, btnThuTien, btnBaoCao, btnCauHinh, btnQuanLyHieuXe, btnQuanLyTienCong, btnQuanLyTho, btnQuanLyNhaCungCap, btnQuanLyVatTu);
        for (Button button : allButtons) {
            button.setVisible(false);
            button.setManaged(false); // Remove from layout flow when invisible
            button.setDisable(true);
        }

        switch (userRole) {
            case ROLE_GIAM_DOC:
            case ROLE_QUAN_LY:
                // Giám Đốc và Quản Lý có toàn quyền truy cập
                for (Button button : allButtons) {
                    button.setVisible(true);
                    button.setManaged(true);
                    button.setDisable(false);
                }
                break;
            case ROLE_NHAN_VIEN_TIEP_NHAN:
                btnTiepNhan.setVisible(true); btnTiepNhan.setManaged(true); btnTiepNhan.setDisable(false);
                break;
            case ROLE_THO_SUA_CHUA:
                btnSuaChua.setVisible(true); btnSuaChua.setManaged(true); btnSuaChua.setDisable(false);
                btnQuanLyTho.setVisible(true); btnQuanLyTho.setManaged(true); btnQuanLyTho.setDisable(false);
                break;
            case ROLE_NHAN_VIEN_KHO:
                btnQuanLyVatTu.setVisible(true); btnQuanLyVatTu.setManaged(true); btnQuanLyVatTu.setDisable(false);
                btnQuanLyNhaCungCap.setVisible(true); btnQuanLyNhaCungCap.setManaged(true); btnQuanLyNhaCungCap.setDisable(false);
                break;
            case ROLE_KE_TOAN:
                btnThuTien.setVisible(true); btnThuTien.setManaged(true); btnThuTien.setDisable(false);
                btnBaoCao.setVisible(true); btnBaoCao.setManaged(true); btnBaoCao.setDisable(false);
                break;
            default:
                AlertUtils.showErrorAlert("Lỗi phân quyền", "Vai trò người dùng không hợp lệ: " + userRole);
                break;
        }
    }

    /**
     * Loads a default view based on the user's role after successful login.
     */
    private void loadViewBasedOnRole() {
        String userRole = loggedInUser.getLoaiTaiKhoan();
        String defaultViewPath = null;

        switch (userRole) {
            case ROLE_GIAM_DOC:
            case ROLE_QUAN_LY:
                defaultViewPath = "/view/QuanLyVatTuView.fxml"; // Default to QuanLyVatTuView for testing inventory
                break;
            case ROLE_NHAN_VIEN_TIEP_NHAN:
                defaultViewPath = "/view/TiepNhanView.fxml";
                break;
            case ROLE_THO_SUA_CHUA:
                defaultViewPath = "/view/SuaChuaView.fxml";
                break;
            case ROLE_NHAN_VIEN_KHO:
                defaultViewPath = "/view/QuanLyVatTuView.fxml";
                break;
            case ROLE_KE_TOAN:
                defaultViewPath = "/view/ThuTienView.fxml";
                break;
            default:
                AlertUtils.showErrorAlert("Lỗi", "Không thể xác định giao diện mặc định cho vai trò này.");
                break;
        }

        if (defaultViewPath != null) {
            loadView(defaultViewPath);
        } else {
            contentArea.getChildren().clear();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTiepNhan() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_NHAN_VIEN_TIEP_NHAN)) {
            loadView("/view/TiepNhanView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleSuaChua() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_THO_SUA_CHUA)) {
            loadView("/view/SuaChuaView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleThuTien() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_KE_TOAN)) {
            loadView("/view/ThuTienView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleBaoCao() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_KE_TOAN)) {
            loadView("/view/BaoCaoView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleCauHinh() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY)) {
            loadView("/view/CauHinhView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleQuanLyHieuXe() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY)) {
            loadView("/view/QuanLyHieuXeView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleQuanLyTienCong() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY)) {
            loadView("/view/QuanLyTienCongView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleQuanLyTho() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_THO_SUA_CHUA)) {
            loadView("/view/QuanLyTho.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleQuanLyNhaCungCap() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_NHAN_VIEN_KHO)) {
            loadView("/view/QuanLyNhaCungCapView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleQuanLyVatTu() {
        if (loggedInUserHasRole(ROLE_GIAM_DOC, ROLE_QUAN_LY, ROLE_NHAN_VIEN_KHO)) {
            loadView("/view/QuanLyVatTuView.fxml");
        } else {
            AlertUtils.showWarningAlert("Không có quyền", "Bạn không có quyền truy cập chức năng này.");
        }
    }

    @FXML
    private void handleDangXuat() {
        AlertUtils.showInformationAlert("Đăng xuất", "Bạn đã đăng xuất thành công!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent loginRoot = loader.load();
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) btnDangXuat.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Đăng nhập Gara Ô tô");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi", "Không thể quay lại màn hình đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to check if the logged-in user has any of the specified roles.
     * @param roles A variable number of role strings to check against.
     * @return true if the logged-in user's role matches any of the provided roles, false otherwise.
     */
    private boolean loggedInUserHasRole(String... roles) {
        if (loggedInUser == null || loggedInUser.getLoaiTaiKhoan() == null) {
            return false;
        }
        String userRole = loggedInUser.getLoaiTaiKhoan();
        for (String role : roles) {
            if (userRole.equals(role)) {
                return true;
            }
        }
        return false;
    }
}
