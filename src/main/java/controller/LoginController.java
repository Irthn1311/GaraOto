package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Import for Scene
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage; // Import for Stage
import model.TaiKhoanNguoiDung;
import dao.TaiKhoanNguoiDungDAO;
import utils.AlertUtils; // Corrected import from 'utils.AlertUtils' to 'util.AlertUtils'
import utils.PasswordHasher; // Utility for password hashing

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink linkRegister;

    private TaiKhoanNguoiDungDAO taiKhoanNguoiDungDAO;

    @FXML
    public void initialize() {
        taiKhoanNguoiDungDAO = new TaiKhoanNguoiDungDAO();
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText(); // Get raw password

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }

        try {
            TaiKhoanNguoiDung user = taiKhoanNguoiDungDAO.getTaiKhoanByUsername(username);

            if (user != null) {
                // Hash the input password and compare with the stored hash
                String hashedPassword = PasswordHasher.hashPassword(password);
                if (hashedPassword.equals(user.getMatKhauHash())) {
                    AlertUtils.showInformationAlert("Thành công", "Đăng nhập thành công!");

                    // Load MainLayout.fxml
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainLayout.fxml"));
                    Parent mainLayoutRoot = loader.load();

                    // Pass logged-in user information to MainLayoutController
                    MainLayoutController mainLayoutController = loader.getController();
                    mainLayoutController.setLoggedInUser(user);

                    Scene scene = new Scene(mainLayoutRoot);
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Quản lý Gara Ô tô");
                    stage.setMaximized(true); // Maximize the main window
                    stage.show();

                } else {
                    AlertUtils.showErrorAlert("Lỗi đăng nhập", "Tên đăng nhập hoặc mật khẩu không đúng.");
                }
            } else {
                AlertUtils.showErrorAlert("Lỗi đăng nhập", "Tên đăng nhập hoặc mật khẩu không đúng.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi kiểm tra thông tin đăng nhập: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện chính: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml"));
            Parent registerRoot = loader.load();
            Scene scene = new Scene(registerRoot);
            Stage stage = (Stage) linkRegister.getScene().getWindow(); // Get current stage
            stage.setScene(scene);
            stage.setTitle("Đăng ký tài khoản");
            stage.setResizable(false); // Registration window usually not resizable
            stage.show();
        } catch (IOException e) {
            AlertUtils.showErrorAlert("Lỗi tải giao diện", "Không thể tải giao diện đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
