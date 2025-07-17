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
import utils.PermissionManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainLayoutController {

    @FXML private Label lblLoggedInUser;
    @FXML private Label lblCurrentUser;
    @FXML private AnchorPane contentArea;

    // Navigation Buttons
    @FXML private Button btnTiepNhan;
    @FXML private Button btnSuaChua;
    @FXML private Button btnThuTien;
    @FXML private Button btnBaoCao;
    @FXML private Button btnQuanLyHieuXe;
    @FXML private Button btnQuanLyTienCong;
    @FXML private Button btnQuanLyTho;
    @FXML private Button btnQuanLyNhaCungCap;
    @FXML private Button btnQuanLyVatTu;
    @FXML private Button btnQuanLyTaiKhoan;
    @FXML private Button btnQuanLyPhanQuyen;
    @FXML private Button btnCauHinh;
    @FXML private Button btnDangXuat;

    private final Map<Button, String> buttonPermissions = new HashMap<>();

    @FXML
    public void initialize() {
        mapButtonsToPermissions();
    }
    
    private void mapButtonsToPermissions() {
        buttonPermissions.put(btnTiepNhan, "Q1");
        buttonPermissions.put(btnSuaChua, "Q2");
        buttonPermissions.put(btnThuTien, "Q3");
        buttonPermissions.put(btnBaoCao, "Q4");
        buttonPermissions.put(btnCauHinh, "Q5");
        buttonPermissions.put(btnQuanLyHieuXe, "Q6");
        buttonPermissions.put(btnQuanLyTienCong, "Q7");
        buttonPermissions.put(btnQuanLyTho, "Q8");
        buttonPermissions.put(btnQuanLyNhaCungCap, "Q9");
        buttonPermissions.put(btnQuanLyVatTu, "Q10");
        buttonPermissions.put(btnQuanLyTaiKhoan, "Q11");
        buttonPermissions.put(btnQuanLyPhanQuyen, "Q12");
    }

    public void setLoggedInUser(TaiKhoanNguoiDung user) {
        PermissionManager.getInstance().setCurrentUser(user);
        
        if (user != null) {
            lblLoggedInUser.setText("Xin chào, " + user.getHoTen() + "!");
            lblCurrentUser.setText("Chào mừng, " + user.getHoTen());
            updateUIVisibility();
            loadDefaultView();
        }
    }

    private void updateUIVisibility() {
        PermissionManager pm = PermissionManager.getInstance();
        buttonPermissions.forEach((button, permission) -> {
            boolean hasPerm = pm.hasPermission(permission);
            button.setVisible(hasPerm);
            button.setManaged(hasPerm);
        });
    }

    private void loadDefaultView() {
        PermissionManager pm = PermissionManager.getInstance();
        String defaultView = null;
        
        if (pm.hasPermission("Q4")) defaultView = "/view/BaoCaoView.fxml";
        else if (pm.hasPermission("Q1")) defaultView = "/view/TiepNhanView.fxml";
        else if (pm.hasPermission("Q2")) defaultView = "/view/SuaChuaView.fxml";
        else if (pm.hasPermission("Q10")) defaultView = "/view/QuanLyVatTuView.fxml";
        else if (pm.hasPermission("Q3")) defaultView = "/view/ThuTienView.fxml";
        
        if (defaultView != null) {
            loadView(defaultView);
        } else {
            contentArea.getChildren().clear();
            AlertUtils.showWarningAlert("Không có quyền truy cập", "Bạn không có chức năng mặc định nào.");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML private void handleTiepNhan() { loadView("/view/TiepNhanView.fxml"); }
    @FXML private void handleSuaChua() { loadView("/view/SuaChuaView.fxml"); }
    @FXML private void handleThuTien() { loadView("/view/ThuTienView.fxml"); }
    @FXML private void handleBaoCao() { loadView("/view/BaoCaoView.fxml"); }
    @FXML private void handleCauHinh() { loadView("/view/CauHinhView.fxml"); }
    @FXML private void handleQuanLyHieuXe() { loadView("/view/QuanLyHieuXeView.fxml"); }
    @FXML private void handleQuanLyTienCong() { loadView("/view/QuanLyTienCongView.fxml"); }
    @FXML private void handleQuanLyTho() { loadView("/view/QuanLyTho.fxml"); }
    @FXML private void handleQuanLyNhaCungCap() { loadView("/view/QuanLyNhaCungCapView.fxml"); }
    @FXML private void handleQuanLyVatTu() { loadView("/view/QuanLyVatTuView.fxml"); }
    @FXML private void handleQuanLyTaiKhoan() { loadView("/view/QuanLyTaiKhoanView.fxml"); }
    @FXML private void handleQuanLyPhanQuyen() { loadView("/view/QuanLyPhanQuyenView.fxml"); }

    @FXML
    private void handleDangXuat() {
        PermissionManager.getInstance().clearSession();
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
            AlertUtils.showErrorAlert("Lỗi", "Không thể quay lại màn hình đăng nhập.");
            e.printStackTrace();
        }
    }
}
