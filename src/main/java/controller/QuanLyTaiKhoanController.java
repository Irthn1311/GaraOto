package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import model.TaiKhoanNguoiDung;
import dao.TaiKhoanNguoiDungDAO;
import utils.AlertUtils;
import utils.PasswordHasher; // Utility for hashing passwords

import java.sql.SQLException;
import java.util.Optional;
import java.util.Comparator;
import java.util.Base64; // Add this import for Base64 if it's used directly in the file for hashing

public class QuanLyTaiKhoanController {

    @FXML
    private TextField txtTenDangNhap;
    @FXML
    private PasswordField txtMatKhau;
    @FXML
    private TextField txtHoTen;
    @FXML
    private ComboBox<String> cbLoaiTaiKhoan;
    @FXML
    private CheckBox chkTrangThai;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;

    @FXML
    private TableView<TaiKhoanNguoiDung> tblTaiKhoan;
    @FXML
    private TableColumn<TaiKhoanNguoiDung, Integer> colMaTK;
    @FXML
    private TableColumn<TaiKhoanNguoiDung, String> colTenDangNhap;
    @FXML
    private TableColumn<TaiKhoanNguoiDung, String> colHoTen;
    @FXML
    private TableColumn<TaiKhoanNguoiDung, String> colLoaiTaiKhoan;
    @FXML
    private TableColumn<TaiKhoanNguoiDung, Boolean> colTrangThai;

    private TaiKhoanNguoiDungDAO taiKhoanNguoiDungDAO;
    private ObservableList<TaiKhoanNguoiDung> danhSachTaiKhoan;

    private TaiKhoanNguoiDung selectedAccount; // Currently selected account for editing

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        taiKhoanNguoiDungDAO = new TaiKhoanNguoiDungDAO();
        danhSachTaiKhoan = FXCollections.observableArrayList();
        tblTaiKhoan.setItems(danhSachTaiKhoan);

        // Configure TableView columns
        colMaTK.setCellValueFactory(cellData -> cellData.getValue().maTKProperty().asObject());
        colTenDangNhap.setCellValueFactory(cellData -> cellData.getValue().tenDangNhapProperty());
        colHoTen.setCellValueFactory(cellData -> cellData.getValue().hoTenProperty());
        colLoaiTaiKhoan.setCellValueFactory(cellData -> cellData.getValue().loaiTaiKhoanProperty());
        colTrangThai.setCellValueFactory(cellData -> cellData.getValue().trangThaiProperty());

        // Populate ComboBox for LoaiTaiKhoan
        cbLoaiTaiKhoan.setItems(FXCollections.observableArrayList("NhanVien", "QuanLy", "GiamDoc"));

        // Listener for table selection to populate text fields for editing
        tblTaiKhoan.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaiKhoanDetails(newValue));

        loadTaiKhoanData();
        btnSua.setDisable(true); // Disable Edit/Delete initially
        btnXoa.setDisable(true);
    }

    /**
     * Loads account data from the database into the TableView.
     */
    private void loadTaiKhoanData() {
        try {
            danhSachTaiKhoan.clear();
            danhSachTaiKhoan.addAll(taiKhoanNguoiDungDAO.getAllTaiKhoanNguoiDung());
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the details of a selected account in the input fields.
     * @param taiKhoan The TaiKhoanNguoiDung object to display.
     */
    private void showTaiKhoanDetails(TaiKhoanNguoiDung taiKhoan) {
        if (taiKhoan != null) {
            selectedAccount = taiKhoan;
            txtTenDangNhap.setText(taiKhoan.getTenDangNhap());
            txtMatKhau.setText(""); // Never display password
            txtHoTen.setText(taiKhoan.getHoTen());
            cbLoaiTaiKhoan.getSelectionModel().select(taiKhoan.getLoaiTaiKhoan());
            chkTrangThai.setSelected(taiKhoan.isTrangThai());

            btnThem.setDisable(true); // Disable Add when an item is selected for editing
            btnSua.setDisable(false);
            btnXoa.setDisable(false);
        } else {
            selectedAccount = null;
            clearForm();
        }
    }

    /**
     * Clears all input fields in the form.
     */
    private void clearForm() {
        txtTenDangNhap.clear();
        txtMatKhau.clear();
        txtHoTen.clear();
        cbLoaiTaiKhoan.getSelectionModel().clearSelection();
        chkTrangThai.setSelected(true); // Default to active
        tblTaiKhoan.getSelectionModel().clearSelection();
    }

    /**
     * Handles the "Thêm" button action. Adds a new user account.
     */
    @FXML
    private void handleThemTaiKhoan() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim();
        String hoTen = txtHoTen.getText().trim();
        String loaiTaiKhoan = cbLoaiTaiKhoan.getSelectionModel().getSelectedItem();
        boolean trangThai = chkTrangThai.isSelected();

        if (tenDangNhap.isEmpty() || matKhau.isEmpty() || hoTen.isEmpty() || loaiTaiKhoan == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ các trường bắt buộc (Tên đăng nhập, Mật khẩu, Họ tên, Loại tài khoản).");
            return;
        }

        try {
            // Check if username already exists
            if (taiKhoanNguoiDungDAO.getTaiKhoanNguoiDungByTenDangNhap(tenDangNhap) != null) {
                AlertUtils.showWarningAlert("Tên đăng nhập đã tồn tại", "Tên đăng nhập này đã được sử dụng. Vui lòng chọn tên khác.");
                return;
            }

            String matKhauHash = PasswordHasher.hashPassword(matKhau);
            TaiKhoanNguoiDung newAccount = new TaiKhoanNguoiDung(0, tenDangNhap, matKhauHash, loaiTaiKhoan, hoTen, trangThai);
            int generatedId = taiKhoanNguoiDungDAO.addTaiKhoanNguoiDung(newAccount);

            if (generatedId != -1) {
                AlertUtils.showInformationAlert("Thành công", "Thêm tài khoản thành công!");
                loadTaiKhoanData();
                clearForm();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể thêm tài khoản. Vui lòng thử lại.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi thêm tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Sửa" button action. Updates an existing user account.
     */
    @FXML
    private void handleSuaTaiKhoan() {
        if (selectedAccount == null) {
            AlertUtils.showWarningAlert("Chưa chọn tài khoản", "Vui lòng chọn một tài khoản để sửa.");
            return;
        }

        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText().trim(); // New password (can be empty)
        String hoTen = txtHoTen.getText().trim();
        String loaiTaiKhoan = cbLoaiTaiKhoan.getSelectionModel().getSelectedItem();
        boolean trangThai = chkTrangThai.isSelected();

        if (tenDangNhap.isEmpty() || hoTen.isEmpty() || loaiTaiKhoan == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ các trường bắt buộc (Tên đăng nhập, Họ tên, Loại tài khoản).");
            return;
        }

        try {
            // Check if username changed and if new username already exists
            if (!tenDangNhap.equals(selectedAccount.getTenDangNhap())) {
                if (taiKhoanNguoiDungDAO.getTaiKhoanNguoiDungByTenDangNhap(tenDangNhap) != null) {
                    AlertUtils.showWarningAlert("Tên đăng nhập đã tồn tại", "Tên đăng nhập này đã được sử dụng bởi tài khoản khác.");
                    return;
                }
            }

            selectedAccount.setTenDangNhap(tenDangNhap);
            selectedAccount.setHoTen(hoTen);
            selectedAccount.setLoaiTaiKhoan(loaiTaiKhoan);
            selectedAccount.setTrangThai(trangThai);

            // Only update password if a new password is provided
            if (!matKhau.isEmpty()) {
                selectedAccount.setMatKhauHash(PasswordHasher.hashPassword(matKhau));
            } else {
                // If password is empty, ensure the hash is not accidentally cleared
                // (TaiKhoanNguoiDungDAO.updateTaiKhoanNguoiDung handles this by checking for null/empty hash)
                selectedAccount.setMatKhauHash(null);
            }

            boolean success = taiKhoanNguoiDungDAO.updateTaiKhoanNguoiDung(selectedAccount);

            if (success) {
                AlertUtils.showInformationAlert("Thành công", "Cập nhật tài khoản thành công!");
                loadTaiKhoanData();
                clearForm();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể cập nhật tài khoản. Vui lòng thử lại.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi cập nhật tài khoản: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa" button action. Deletes the selected user account.
     */
    @FXML
    private void handleXoaTaiKhoan() {
        if (selectedAccount == null) {
            AlertUtils.showWarningAlert("Chưa chọn tài khoản", "Vui lòng chọn một tài khoản để xóa.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận xóa");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Bạn có chắc chắn muốn xóa tài khoản '" + selectedAccount.getTenDangNhap() + "' không?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = taiKhoanNguoiDungDAO.deleteTaiKhoanNguoiDung(selectedAccount.getMaTK());
                if (success) {
                    AlertUtils.showInformationAlert("Thành công", "Xóa tài khoản thành công!");
                    loadTaiKhoanData();
                    clearForm();
                } else {
                    AlertUtils.showErrorAlert("Lỗi", "Không thể xóa tài khoản. Vui lòng thử lại.");
                }
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi xóa tài khoản: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears the form and table selection.
     */
    @FXML
    private void handleLamMoi() {
        clearForm();
        tblTaiKhoan.getSelectionModel().clearSelection();
        btnThem.setDisable(false);
        btnSua.setDisable(true);
        btnXoa.setDisable(true);
    }
}
