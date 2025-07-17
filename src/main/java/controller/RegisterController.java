package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.TaiKhoanNguoiDung;
import dao.TaiKhoanNguoiDungDAO;
import utils.AlertUtils;
import utils.PasswordHasher;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField txtHoTen;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmPassword;
    @FXML
    private Button btnRegister;
    @FXML
    private Hyperlink linkBackToLogin;

    private TaiKhoanNguoiDungDAO taiKhoanNguoiDungDAO;

    @FXML
    public void initialize() {
        taiKhoanNguoiDungDAO = new TaiKhoanNguoiDungDAO();
    }

    @FXML
    private void handleRegister() {
        String hoTen = txtHoTen.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // 1. Validate inputs
        if (hoTen.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ các trường.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            AlertUtils.showWarningAlert("Lỗi mật khẩu", "Mật khẩu xác nhận không khớp.");
            return;
        }
        if (password.length() < 6) {
            AlertUtils.showWarningAlert("Mật khẩu yếu", "Mật khẩu phải có ít nhất 6 ký tự.");
            return;
        }

        try {
            // 2. Check if username already exists
            if (taiKhoanNguoiDungDAO.checkUsernameExists(username)) {
                AlertUtils.showErrorAlert("Lỗi đăng ký", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác.");
                return;
            }

            // 3. Hash password
            String hashedPassword = PasswordHasher.hashPassword(password);

            // 4. Create new user account. By default, they have no role (MaPhanQuyen is null).
            // An admin must assign a role later.
            TaiKhoanNguoiDung newUser = new TaiKhoanNguoiDung();
            newUser.setHoTen(hoTen);
            newUser.setTenDangNhap(username);
            newUser.setMatKhauHash(hashedPassword);
            newUser.setMaPhanQuyen(null); // No role assigned upon registration
            newUser.setTrangThai(true); 

            boolean success = taiKhoanNguoiDungDAO.addTaiKhoanNguoiDung(newUser);

            if (success) {
                AlertUtils.showSuccessAlert("Thành công", "Đăng ký tài khoản thành công! Vui lòng chờ quản trị viên cấp quyền để đăng nhập.");
                handleBackToLogin();
            } else {
                AlertUtils.showErrorAlert("Lỗi đăng ký", "Không thể đăng ký tài khoản. Vui lòng thử lại.");
            }

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent loginRoot = loader.load();
            Scene scene = new Scene(loginRoot);
            Stage stage = (Stage) btnRegister.getScene().getWindow(); // Get current stage
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
