package controller;

import dao.PhanQuyenDAO;
import dao.PhanQuyenDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import model.PhanQuyen;
import model.TaiKhoanNguoiDung;
import dao.TaiKhoanNguoiDungDAO;
import utils.AlertUtils;
import utils.PasswordHasher;

import java.sql.SQLException;
import java.util.Optional;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextInputDialog;

public class QuanLyTaiKhoanController {

    @FXML private TextField txtTenDangNhap;
    @FXML private PasswordField txtMatKhau;
    @FXML private ComboBox<PhanQuyen> cmbPhanQuyen;
    @FXML private TextField txtHoTen;
    @FXML private CheckBox chkTrangThai;
    @FXML private TableView<TaiKhoanNguoiDung> tblTaiKhoan;
    @FXML private TableColumn<TaiKhoanNguoiDung, Integer> colMaTK;
    @FXML private TableColumn<TaiKhoanNguoiDung, String> colTenDangNhap;
    @FXML private TableColumn<TaiKhoanNguoiDung, String> colPhanQuyen;
    @FXML private TableColumn<TaiKhoanNguoiDung, String> colHoTen;
    @FXML private TableColumn<TaiKhoanNguoiDung, Boolean> colTrangThai;
    
    private TaiKhoanNguoiDungDAO taiKhoanDAO;
    private PhanQuyenDAO phanQuyenDAO;
    private ObservableList<PhanQuyen> phanQuyenList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        taiKhoanDAO = new TaiKhoanNguoiDungDAO();
        phanQuyenDAO = new PhanQuyenDAOImpl();
        
        setupTableColumns();
        loadPhanQuyen();
        loadTaiKhoan();
        
        tblTaiKhoan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void setupTableColumns() {
        colMaTK.setCellValueFactory(new PropertyValueFactory<>("maTK"));
        colTenDangNhap.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        // Add custom cell factory to display readable status instead of boolean values
        colTrangThai.setCellFactory(column -> new TableCell<TaiKhoanNguoiDung, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Hoạt động" : "Bị Khóa");
                }
            }
        });
        colPhanQuyen.setCellValueFactory(new PropertyValueFactory<>("maPhanQuyen"));
    }
    
    private void loadPhanQuyen() {
        try {
            phanQuyenList.setAll(phanQuyenDAO.getAllPhanQuyen());
            cmbPhanQuyen.setItems(phanQuyenList);
            cmbPhanQuyen.setConverter(new StringConverter<PhanQuyen>() {
                @Override
                public String toString(PhanQuyen object) {
                    return object == null ? "" : object.getTenPhanQuyen();
                }

                @Override
                public PhanQuyen fromString(String string) {
                    return cmbPhanQuyen.getItems().stream().filter(pq -> 
                        pq.getTenPhanQuyen().equals(string)).findFirst().orElse(null);
                }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách phân quyền.");
            e.printStackTrace();
        }
    }

    private void loadTaiKhoan() {
        try {
            tblTaiKhoan.getItems().setAll(taiKhoanDAO.getAllTaiKhoanNguoiDung());
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách tài khoản.");
            e.printStackTrace();
        }
    }

    private void populateFields(TaiKhoanNguoiDung user) {
        txtTenDangNhap.setText(user.getTenDangNhap());
        txtHoTen.setText(user.getHoTen());
        chkTrangThai.setSelected(user.isTrangThai());
        txtMatKhau.clear();

        // Find and set the correct PhanQuyen object in the ComboBox
        phanQuyenList.stream()
            .filter(pq -> pq.getMaPhanQuyen().equals(user.getMaPhanQuyen()))
            .findFirst()
            .ifPresent(cmbPhanQuyen::setValue);
    }

    @FXML
    private void handleThemTaiKhoan() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = txtMatKhau.getText();
        PhanQuyen phanQuyen = cmbPhanQuyen.getValue();
        String hoTen = txtHoTen.getText().trim();

        if (tenDangNhap.isEmpty() || matKhau.isEmpty() || phanQuyen == null || hoTen.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        try {
            if (taiKhoanDAO.checkUsernameExists(tenDangNhap)) {
                AlertUtils.showErrorAlert("Lỗi", "Tên đăng nhập đã tồn tại.");
                return;
            }

            String matKhauHash = PasswordHasher.hashPassword(matKhau);
            TaiKhoanNguoiDung newUser = new TaiKhoanNguoiDung(0, tenDangNhap, matKhauHash, phanQuyen.getMaPhanQuyen(), hoTen, chkTrangThai.isSelected());
            if (taiKhoanDAO.addTaiKhoanNguoiDung(newUser)) {
                AlertUtils.showSuccessAlert("Thành công", "Thêm tài khoản thành công.");
                loadTaiKhoan();
                handleLamMoi();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể thêm tài khoản.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSuaTaiKhoan() {
        TaiKhoanNguoiDung selectedUser = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showWarningAlert("Chưa chọn tài khoản", "Vui lòng chọn một tài khoản để sửa.");
            return;
        }

        if ("admin".equalsIgnoreCase(selectedUser.getTenDangNhap())) {
            AlertUtils.showWarningAlert("Không được phép", "Không thể chỉnh sửa tài khoản 'admin'.");
            return;
        }

        String tenDangNhap = txtTenDangNhap.getText().trim();
        PhanQuyen phanQuyen = cmbPhanQuyen.getValue();
        String hoTen = txtHoTen.getText().trim();

        if (tenDangNhap.isEmpty() || phanQuyen == null || hoTen.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        try {
            TaiKhoanNguoiDung existingUser = taiKhoanDAO.getTaiKhoanByTenDangNhap(tenDangNhap);
            if (existingUser != null && existingUser.getMaTK() != selectedUser.getMaTK()) {
                AlertUtils.showErrorAlert("Lỗi", "Tên đăng nhập đã tồn tại.");
                return;
            }

            selectedUser.setTenDangNhap(tenDangNhap);
            selectedUser.setHoTen(hoTen);
            selectedUser.setMaPhanQuyen(phanQuyen.getMaPhanQuyen());
            selectedUser.setTrangThai(chkTrangThai.isSelected());

            if (taiKhoanDAO.updateTaiKhoanNguoiDung(selectedUser)) {
                AlertUtils.showSuccessAlert("Thành công", "Cập nhật tài khoản thành công.");
                loadTaiKhoan();
                handleLamMoi();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể cập nhật tài khoản.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleXoaTaiKhoan() {
        TaiKhoanNguoiDung selectedUser = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showWarningAlert("Chưa chọn tài khoản", "Vui lòng chọn một tài khoản để xóa.");
            return;
        }

        if ("admin".equalsIgnoreCase(selectedUser.getTenDangNhap())) {
            AlertUtils.showErrorAlert("Không thể xóa", "Không được phép xóa tài khoản 'admin'.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa tài khoản '" + selectedUser.getTenDangNhap() + "' không?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    if(taiKhoanDAO.deleteTaiKhoanNguoiDung(selectedUser.getMaTK())) {
                        AlertUtils.showSuccessAlert("Thành công", "Xóa tài khoản thành công.");
                        loadTaiKhoan();
                        handleLamMoi();
                    }
                } catch (SQLException e) {
                    AlertUtils.showErrorAlert("Lỗi SQL", "Không thể xóa tài khoản.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleLamMoi() {
        txtTenDangNhap.clear();
        txtMatKhau.clear();
        txtHoTen.clear();
        cmbPhanQuyen.setValue(null);
        chkTrangThai.setSelected(true);
        tblTaiKhoan.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void handleResetPassword() {
        TaiKhoanNguoiDung selectedUser = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showWarningAlert("Chưa chọn tài khoản", "Vui lòng chọn tài khoản cần reset mật khẩu.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset Mật khẩu");
        dialog.setHeaderText("Reset mật khẩu cho: " + selectedUser.getTenDangNhap());
        dialog.setContentText("Nhập mật khẩu mới:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newPassword -> {
            if (newPassword.trim().isEmpty()) {
                AlertUtils.showWarningAlert("Lỗi", "Mật khẩu không được để trống.");
                return;
            }
            try {
                String newHashedPassword = PasswordHasher.hashPassword(newPassword);
                if(taiKhoanDAO.updateUserPassword(selectedUser.getMaTK(), newHashedPassword)) {
                    AlertUtils.showSuccessAlert("Thành công", "Reset mật khẩu thành công.");
                }
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể cập nhật mật khẩu.");
                e.printStackTrace();
            }
        });
    }
}
