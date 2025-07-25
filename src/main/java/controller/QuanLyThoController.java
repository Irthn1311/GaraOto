package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Tho;
import dao.ThoDAO;
import dao.PhieuSuaChuaDAO; // Import the DAO
import utils.AlertUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.sql.SQLException;
import java.util.Optional;

public class QuanLyThoController {

    @FXML
    private TextField txtMaTho;
    @FXML
    private TextField txtHoTenTho;
    @FXML
    private TextField txtDienThoai;
    @FXML
    private TextField txtChuyenMon;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;
    @FXML
    private TableView<Tho> tblTho;
    @FXML
    private TableColumn<Tho, Integer> colMaTho;
    @FXML
    private TableColumn<Tho, String> colHoTenTho;
    @FXML
    private TableColumn<Tho, String> colDienThoai;
    @FXML
    private TableColumn<Tho, String> colChuyenMon;

    private ThoDAO thoDAO;
    private PhieuSuaChuaDAO phieuSuaChuaDAO; // Add the DAO
    private ObservableList<Tho> thoList;

    @FXML
    public void initialize() {
        thoDAO = new ThoDAO();
        phieuSuaChuaDAO = new PhieuSuaChuaDAO(); // Initialize the DAO
        thoList = FXCollections.observableArrayList();

        // Configure TableView columns
        colMaTho.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMaTho()));
        colHoTenTho.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTenTho()));
        colDienThoai.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getSoDienThoai()));
        colChuyenMon.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getChuyenMon()));

        // Listener for table selection to populate text fields for editing
        tblTho.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showThoDetails(newValue));

        loadThoData();
    }

    /**
     * Loads mechanic data from the database into the TableView.
     */
    private void loadThoData() {
        try {
            thoList.setAll(thoDAO.getAllTho());
            tblTho.setItems(thoList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách thợ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fills all text fields with details of the selected Tho.
     * @param tho The Tho object or null if no selection.
     */
    private void showThoDetails(Tho tho) {
        if (tho != null) {
            txtMaTho.setText(String.valueOf(tho.getMaTho()));
            txtHoTenTho.setText(tho.getTenTho());
            txtDienThoai.setText(tho.getSoDienThoai());
            txtChuyenMon.setText(tho.getChuyenMon());
        } else {
            clearFields();
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtMaTho.setText("");
        txtHoTenTho.setText("");
        txtDienThoai.setText("");
        txtChuyenMon.setText("");
        tblTho.getSelectionModel().clearSelection(); // Clear table selection
    }

    /**
     * Handles the "Thêm" button action. Adds a new mechanic.
     */
    @FXML
    private void handleThemTho() {
        String hoTen = txtHoTenTho.getText().trim();
        String dienThoai = txtDienThoai.getText().trim();
        String chuyenMon = txtChuyenMon.getText().trim();

        if (hoTen.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập họ tên thợ.");
            return;
        }
        if (!dienThoai.isEmpty() && !dienThoai.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }

        try {
            // Check for duplicate phone number
            if (thoDAO.getThoByDienThoai(dienThoai) != null) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Số điện thoại này đã được sử dụng cho một thợ khác.");
                return;
            }

            Tho newTho = new Tho();
            newTho.setTenTho(hoTen);
            newTho.setSoDienThoai(dienThoai);
            newTho.setChuyenMon(chuyenMon);

            int generatedId = thoDAO.addTho(newTho);
            if (generatedId != -1) {
                AlertUtils.showInformationAlert("Thành công", "Thêm thợ thành công!");
                loadThoData(); // Reload data to show the new item
                clearFields();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể thêm thợ.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm thợ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Sửa" button action. Updates an existing mechanic.
     */
    @FXML
    private void handleSuaTho() {
        Tho selectedTho = tblTho.getSelectionModel().getSelectedItem();
        if (selectedTho == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một thợ để sửa.");
            return;
        }

        String hoTenMoi = txtHoTenTho.getText().trim();
        String dienThoaiMoi = txtDienThoai.getText().trim();
        String chuyenMonMoi = txtChuyenMon.getText().trim();

        if (hoTenMoi.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập họ tên thợ.");
            return;
        }
        if (!dienThoaiMoi.isEmpty() && !dienThoaiMoi.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }

        try {
            // Check if the new phone number duplicates another existing entry
            Tho existingTho = thoDAO.getThoByDienThoai(dienThoaiMoi);
            if (existingTho != null && existingTho.getMaTho() != selectedTho.getMaTho()) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Số điện thoại này đã được sử dụng cho một thợ khác.");
                return;
            }

            selectedTho.setTenTho(hoTenMoi);
            selectedTho.setSoDienThoai(dienThoaiMoi);
            selectedTho.setChuyenMon(chuyenMonMoi);
            thoDAO.updateTho(selectedTho);
            AlertUtils.showInformationAlert("Thành công", "Cập nhật thông tin thợ thành công!");
            loadThoData();
            clearFields();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật thông tin thợ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa" button action. Deletes a selected mechanic.
     */
    @FXML
    private void handleXoaTho() {
        Tho selectedTho = tblTho.getSelectionModel().getSelectedItem();
        if (selectedTho == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một thợ để xóa.");
            return;
        }

        try {
            // Check if the mechanic is used in any repair slips
            boolean isUsed = phieuSuaChuaDAO.isThoUsed(selectedTho.getMaTho());
            if (isUsed) {
                AlertUtils.showErrorAlert("Không thể xóa", "Thợ này đã được phân công trong các phiếu sửa chữa và không thể xóa.");
                return;
            }

            if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa thợ '" + selectedTho.getTenTho() + "' không?")) {
                thoDAO.deleteTho(selectedTho.getMaTho());
                AlertUtils.showInformationAlert("Thành công", "Xóa thợ thành công!");
                loadThoData();
                clearFields();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi xóa thợ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLamMoi() {
        clearFields();
        loadThoData();
    }
}
