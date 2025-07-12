package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Lớp MainController điều khiển logic cho giao diện chính của ứng dụng.
 */
public class MainController {

    @FXML
    private Label welcomeLabel;

    /**
     * Phương thức khởi tạo (được gọi tự động sau khi FXML được tải).
     */
    public void initialize() {
        welcomeLabel.setText("Chào mừng đến với ứng dụng quản lý Gara Ô tô!");
    }

    // Thêm các phương thức xử lý sự kiện (ví dụ: handleTiepNhanXe, handleSuaChuaXe...)
} 