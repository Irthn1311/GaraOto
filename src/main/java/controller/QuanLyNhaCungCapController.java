package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.NhaCungCap;
import dao.NhaCungCapDAO;
import dao.PhieuNhapKhoVatTuDAO; // Import the DAO
import utils.AlertUtils;

import java.sql.SQLException;
import java.util.Optional;

public class QuanLyNhaCungCapController {

    @FXML
    private TextField txtMaNhaCungCap;
    @FXML
    private TextField txtTenNhaCungCap;
    @FXML
    private TextField txtDienThoai;
    @FXML
    private TextField txtDiaChi;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;
    @FXML
    private TableView<NhaCungCap> tblNhaCungCap;
    @FXML
    private TableColumn<NhaCungCap, Integer> colMaNhaCungCap;
    @FXML
    private TableColumn<NhaCungCap, String> colTenNhaCungCap;
    @FXML
    private TableColumn<NhaCungCap, String> colDienThoai;
    @FXML
    private TableColumn<NhaCungCap, String> colDiaChi;
    @FXML
    private TableColumn<NhaCungCap, String> colEmail;

    private NhaCungCapDAO nhaCungCapDAO;
    private PhieuNhapKhoVatTuDAO phieuNhapKhoVatTuDAO; // Add the DAO
    private ObservableList<NhaCungCap> nhaCungCapList;

    @FXML
    public void initialize() {
        nhaCungCapDAO = new NhaCungCapDAO();
        phieuNhapKhoVatTuDAO = new PhieuNhapKhoVatTuDAO(); // Initialize the DAO
        nhaCungCapList = FXCollections.observableArrayList();

        // Configure TableView columns
        colMaNhaCungCap.setCellValueFactory(cellData -> cellData.getValue().maNhaCungCapProperty().asObject());
        colTenNhaCungCap.setCellValueFactory(cellData -> cellData.getValue().tenNhaCungCapProperty());
        colDienThoai.setCellValueFactory(cellData -> cellData.getValue().dienThoaiProperty());
        colDiaChi.setCellValueFactory(cellData -> cellData.getValue().diaChiProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        // Listener for table selection to populate text fields for editing
        tblNhaCungCap.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showNhaCungCapDetails(newValue));

        loadNhaCungCapData();
    }

    /**
     * Loads supplier data from the database into the TableView.
     */
    private void loadNhaCungCapData() {
        try {
            nhaCungCapList.setAll(nhaCungCapDAO.getAllNhaCungCap());
            tblNhaCungCap.setItems(nhaCungCapList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fills all text fields with details of the selected NhaCungCap.
     * @param nhaCungCap The NhaCungCap object or null if no selection.
     */
    private void showNhaCungCapDetails(NhaCungCap nhaCungCap) {
        if (nhaCungCap != null) {
            txtMaNhaCungCap.setText(String.valueOf(nhaCungCap.getMaNhaCungCap()));
            txtTenNhaCungCap.setText(nhaCungCap.getTenNhaCungCap());
            txtDienThoai.setText(nhaCungCap.getDienThoai());
            txtDiaChi.setText(nhaCungCap.getDiaChi());
            txtEmail.setText(nhaCungCap.getEmail());
        } else {
            clearFields();
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtMaNhaCungCap.setText("");
        txtTenNhaCungCap.setText("");
        txtDienThoai.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        tblNhaCungCap.getSelectionModel().clearSelection(); // Clear table selection
    }

    /**
     * Handles the "Thêm" button action. Adds a new supplier.
     */
    @FXML
    private void handleThemNhaCungCap() {
        String tenNhaCungCap = txtTenNhaCungCap.getText().trim();
        String dienThoai = txtDienThoai.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String email = txtEmail.getText().trim();

        if (tenNhaCungCap.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập tên nhà cung cấp.");
            return;
        }
        if (!dienThoai.isEmpty() && !dienThoai.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }
        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Địa chỉ email không hợp lệ.");
            return;
        }

        try {
            // Check for duplicate name
            if (nhaCungCapDAO.getNhaCungCapByTen(tenNhaCungCap) != null) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên nhà cung cấp này đã tồn tại.");
                return;
            }

            NhaCungCap newNhaCungCap = new NhaCungCap();
            newNhaCungCap.setTenNhaCungCap(tenNhaCungCap);
            newNhaCungCap.setDienThoai(dienThoai);
            newNhaCungCap.setDiaChi(diaChi);
            newNhaCungCap.setEmail(email);

            int generatedId = nhaCungCapDAO.addNhaCungCap(newNhaCungCap);
            if (generatedId != -1) {
                AlertUtils.showInformationAlert("Thành công", "Thêm nhà cung cấp thành công!");
                loadNhaCungCapData(); // Reload data to show the new item
                clearFields();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể thêm nhà cung cấp.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Sửa" button action. Updates an existing supplier.
     */
    @FXML
    private void handleSuaNhaCungCap() {
        NhaCungCap selectedNhaCungCap = tblNhaCungCap.getSelectionModel().getSelectedItem();
        if (selectedNhaCungCap == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một nhà cung cấp để sửa.");
            return;
        }

        String tenNhaCungCapMoi = txtTenNhaCungCap.getText().trim();
        String dienThoaiMoi = txtDienThoai.getText().trim();
        String diaChiMoi = txtDiaChi.getText().trim();
        String emailMoi = txtEmail.getText().trim();

        if (tenNhaCungCapMoi.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập tên nhà cung cấp.");
            return;
        }
        if (!dienThoaiMoi.isEmpty() && !dienThoaiMoi.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }
        if (!emailMoi.isEmpty() && !emailMoi.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Địa chỉ email không hợp lệ.");
            return;
        }

        try {
            // Check if the new name duplicates another existing entry
            NhaCungCap existingNhaCungCap = nhaCungCapDAO.getNhaCungCapByTen(tenNhaCungCapMoi);
            if (existingNhaCungCap != null && existingNhaCungCap.getMaNhaCungCap() != selectedNhaCungCap.getMaNhaCungCap()) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên nhà cung cấp này đã tồn tại ở một mục khác.");
                return;
            }

            selectedNhaCungCap.setTenNhaCungCap(tenNhaCungCapMoi);
            selectedNhaCungCap.setDienThoai(dienThoaiMoi);
            selectedNhaCungCap.setDiaChi(diaChiMoi);
            selectedNhaCungCap.setEmail(emailMoi);
            nhaCungCapDAO.updateNhaCungCap(selectedNhaCungCap);
            AlertUtils.showInformationAlert("Thành công", "Cập nhật nhà cung cấp thành công!");
            loadNhaCungCapData();
            clearFields();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa" button action. Deletes a selected supplier.
     */
    @FXML
    private void handleXoaNhaCungCap() {
        NhaCungCap selectedNhaCungCap = tblNhaCungCap.getSelectionModel().getSelectedItem();
        if (selectedNhaCungCap == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một nhà cung cấp để xóa.");
            return;
        }
        
        try {
            // Check if the supplier is used in any import slips
            boolean isUsed = phieuNhapKhoVatTuDAO.isNhaCungCapUsed(selectedNhaCungCap.getMaNhaCungCap());
            if (isUsed) {
                AlertUtils.showErrorAlert("Không thể xóa", "Nhà cung cấp này đã được sử dụng trong các phiếu nhập kho và không thể xóa.");
                return;
            }

            if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa nhà cung cấp '" + selectedNhaCungCap.getTenNhaCungCap() + "' không?")) {
                nhaCungCapDAO.deleteNhaCungCap(selectedNhaCungCap.getMaNhaCungCap());
                AlertUtils.showInformationAlert("Thành công", "Xóa nhà cung cấp thành công!");
                loadNhaCungCapData();
                clearFields();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi xóa nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears fields and reloads data.
     */
    @FXML
    private void handleLamMoi() {
        clearFields();
        loadNhaCungCapData();
        AlertUtils.showInformationAlert("Làm mới", "Dữ liệu nhà cung cấp đã được làm mới.");
    }
}
