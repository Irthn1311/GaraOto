package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Added import for Scene
import javafx.stage.Stage; // Added import for Stage
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.TaiKhoanNguoiDung; // Import the user model
import utils.AlertUtils; // Assuming you have an AlertUtils class

import java.io.IOException;

public class MainLayoutController {

    @FXML
    private Label lblLoggedInUser; // In sidebar
    @FXML
    private Label lblCurrentUser;  // In top bar
    @FXML
    private AnchorPane contentArea;

    private TaiKhoanNguoiDung loggedInUser; // To store logged-in user info

    @FXML
    public void initialize() {
        // Load default view (e.g., TiepNhanView) when MainLayout is initialized
        loadView("/view/TiepNhanView.fxml");
    }

    // Method to set the logged-in user information
    public void setLoggedInUser(TaiKhoanNguoiDung user) {
        this.loggedInUser = user;
        if (loggedInUser != null) {
            lblLoggedInUser.setText("Xin chào, " + loggedInUser.getHoTen() + "!");
            lblCurrentUser.setText("Chào mừng, " + loggedInUser.getHoTen() + "!");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
            // Optionally, pass data to the loaded controller if needed
            // Example: if (loader.getController() instanceof SomeController) {
            //     ((SomeController) loader.getController()).setData(someData);
            // }
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTiepNhan() {
        loadView("/view/TiepNhanView.fxml");
    }

    @FXML
    private void handleSuaChua() {
        loadView("/view/SuaChuaView.fxml");
    }

    @FXML
    private void handleThuTien() {
        loadView("/view/ThuTienView.fxml");
    }

    @FXML
    private void handleBaoCao() {
        loadView("/view/BaoCaoView.fxml");
    }

    @FXML
    private void handleCauHinh() {
        loadView("/view/CauHinhView.fxml");
    }

    @FXML
    private void handleDangXuat() {
        AlertUtils.showInformationAlert("Đăng xuất", "Bạn đã đăng xuất thành công!");
        // Implement logic to return to login screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent loginRoot = loader.load();
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) lblLoggedInUser.getScene().getWindow(); // Get current stage
            stage.setScene(scene);
            stage.setTitle("Đăng nhập Gara Ô tô");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi", "Không thể quay lại màn hình đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
