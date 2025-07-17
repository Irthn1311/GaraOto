package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
// import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.HieuXe;
import dao.HieuXeDAO;
import utils.AlertUtils;

import java.sql.SQLException;

public class QuanLyHieuXeController {

    @FXML
    private TextField txtMaHieuXe;
    @FXML
    private TextField txtTenHieuXe;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;
    @FXML
    private TableView<HieuXe> tblHieuXe;
    @FXML
    private TableColumn<HieuXe, Integer> colMaHieuXe;
    @FXML
    private TableColumn<HieuXe, String> colTenHieuXe;

    private HieuXeDAO hieuXeDAO;
    private ObservableList<HieuXe> hieuXeList;

    @FXML
    public void initialize() {
        hieuXeDAO = new HieuXeDAO();
        hieuXeList = FXCollections.observableArrayList();

        // Configure TableView columns
        colMaHieuXe.setCellValueFactory(cellData -> cellData.getValue().maHieuXeProperty().asObject());
        colTenHieuXe.setCellValueFactory(cellData -> cellData.getValue().tenHieuXeProperty());

        // Listen for selection changes in the table and show details
        tblHieuXe.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showHieuXeDetails(newValue));

        loadHieuXeData();
    }

    /**
     * Loads car brand data from the database into the TableView.
     */
    private void loadHieuXeData() {
        try {
            hieuXeList.setAll(hieuXeDAO.getAllHieuXe());
            tblHieuXe.setItems(hieuXeList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách hiệu xe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fills all text fields with details of the selected HieuXe.
     * @param hieuXe The HieuXe object or null if no selection.
     */
    private void showHieuXeDetails(HieuXe hieuXe) {
        if (hieuXe != null) {
            txtMaHieuXe.setText(String.valueOf(hieuXe.getMaHieuXe()));
            txtTenHieuXe.setText(hieuXe.getTenHieuXe());
        } else {
            clearFields();
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtMaHieuXe.setText("");
        txtTenHieuXe.setText("");
        tblHieuXe.getSelectionModel().clearSelection(); // Clear table selection
    }

    /**
     * Handles the "Thêm" button action. Adds a new car brand.
     */
    @FXML
    private void handleThemHieuXe() {
        String tenHieuXe = txtTenHieuXe.getText().trim();

        if (tenHieuXe.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập tên hiệu xe.");
            return;
        }

        try {
            // Check for duplicate name
            if (hieuXeDAO.getHieuXeByName(tenHieuXe) != null) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên hiệu xe này đã tồn tại.");
                return;
            }

            HieuXe newHieuXe = new HieuXe();
            newHieuXe.setTenHieuXe(tenHieuXe);

            int generatedId = hieuXeDAO.addHieuXe(newHieuXe);
            if (generatedId != -1) {
                AlertUtils.showInformationAlert("Thành công", "Thêm hiệu xe thành công!");
                loadHieuXeData(); // Reload data to show the new item
                clearFields();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể thêm hiệu xe.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm hiệu xe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Sửa" button action. Updates an existing car brand.
     */
    @FXML
    private void handleSuaHieuXe() {
        HieuXe selectedHieuXe = tblHieuXe.getSelectionModel().getSelectedItem();
        if (selectedHieuXe == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một hiệu xe để sửa.");
            return;
        }

        String tenHieuXeMoi = txtTenHieuXe.getText().trim();

        if (tenHieuXeMoi.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập tên hiệu xe.");
            return;
        }

        try {
            // Check for duplicate name, but allow update if it's the same item
            HieuXe existingHieuXe = hieuXeDAO.getHieuXeByName(tenHieuXeMoi);
            if (existingHieuXe != null && existingHieuXe.getMaHieuXe() != selectedHieuXe.getMaHieuXe()) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên hiệu xe này đã tồn tại cho hiệu xe khác.");
                return;
            }

            selectedHieuXe.setTenHieuXe(tenHieuXeMoi);
            hieuXeDAO.updateHieuXe(selectedHieuXe);
            AlertUtils.showInformationAlert("Thành công", "Cập nhật hiệu xe thành công!");
            loadHieuXeData();
            clearFields();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật hiệu xe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa" button action. Deletes a selected car brand.
     */
    @FXML
    private void handleXoaHieuXe() {
        HieuXe selectedHieuXe = tblHieuXe.getSelectionModel().getSelectedItem();
        if (selectedHieuXe == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một hiệu xe để xóa.");
            return;
        }

        if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa hiệu xe '" + selectedHieuXe.getTenHieuXe() + "' không?")) {
            try {
                hieuXeDAO.deleteHieuXe(selectedHieuXe.getMaHieuXe());
                AlertUtils.showInformationAlert("Thành công", "Xóa hiệu xe thành công!");
                loadHieuXeData();
                clearFields();
            } catch (SQLException e) {
                // Handle foreign key constraint violation (e.g., if there are cars with this brand)
                if (e.getSQLState().startsWith("23")) { // SQLSTATE for integrity constraint violation
                    AlertUtils.showErrorAlert("Lỗi ràng buộc", "Không thể xóa hiệu xe này vì có xe đang sử dụng hiệu xe này. Vui lòng xóa các xe liên quan trước.");
                } else {
                    AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi xóa hiệu xe: " + e.getMessage());
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears fields and reloads data.
     */
    @FXML
    private void handleLamMoi() {
        clearFields();
        loadHieuXeData();
        AlertUtils.showInformationAlert("Làm mới", "Dữ liệu hiệu xe đã được làm mới.");
    }
}
