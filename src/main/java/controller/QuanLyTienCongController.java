package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.LoaiTienCong;
import dao.LoaiTienCongDAO;
import utils.AlertUtils;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javafx.scene.control.TableCell;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;

public class QuanLyTienCongController {

    @FXML
    private TextField txtMaLoaiTienCong;
    @FXML
    private TextField txtTenLoaiTienCong;
    @FXML
    private TextField txtDonGiaTienCong;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;
    @FXML
    private TableView<LoaiTienCong> tblLoaiTienCong;
    @FXML
    private TableColumn<LoaiTienCong, Integer> colMaLoaiTienCong;
    @FXML
    private TableColumn<LoaiTienCong, String> colTenLoaiTienCong;
    @FXML
    private TableColumn<LoaiTienCong, Double> colDonGiaTienCong;

    private LoaiTienCongDAO loaiTienCongDAO;
    private ObservableList<LoaiTienCong> loaiTienCongList;
    private NumberFormat currencyFormat;

    @FXML
    public void initialize() {
        loaiTienCongDAO = new LoaiTienCongDAO();
        loaiTienCongList = FXCollections.observableArrayList();

        // Initialize currency formatter (using Locale.Builder for modern Java)
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("vi").setRegion("VN").build());

        // Configure TableView columns
        colMaLoaiTienCong.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMaLoaiTienCong()));
        colTenLoaiTienCong.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTenLoaiTienCong()));
        colDonGiaTienCong.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDonGiaTienCong()));
        // Custom cell factory for currency formatting
        colDonGiaTienCong.setCellFactory(tc -> new TableCell<LoaiTienCong, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });

        // Listen for selection changes in the table and show details
        tblLoaiTienCong.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showLoaiTienCongDetails(newValue));

        loadLoaiTienCongData();
    }

    /**
     * Loads labor cost type data from the database into the TableView.
     */
    private void loadLoaiTienCongData() {
        try {
            loaiTienCongList.setAll(loaiTienCongDAO.getAllLoaiTienCong());
            tblLoaiTienCong.setItems(loaiTienCongList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách loại tiền công: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fills all text fields with details of the selected LoaiTienCong.
     * @param loaiTienCong The LoaiTienCong object or null if no selection.
     */
    private void showLoaiTienCongDetails(LoaiTienCong loaiTienCong) {
        if (loaiTienCong != null) {
            txtMaLoaiTienCong.setText(String.valueOf(loaiTienCong.getMaLoaiTienCong()));
            txtTenLoaiTienCong.setText(loaiTienCong.getTenLoaiTienCong());
            txtDonGiaTienCong.setText(String.valueOf(loaiTienCong.getDonGiaTienCong()));
        } else {
            clearFields();
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        txtMaLoaiTienCong.setText("");
        txtTenLoaiTienCong.setText("");
        txtDonGiaTienCong.setText("");
        tblLoaiTienCong.getSelectionModel().clearSelection(); // Clear table selection
    }

    /**
     * Handles the "Thêm" button action. Adds a new labor cost type.
     */
    @FXML
    private void handleThemLoaiTienCong() {
        String tenLoaiTienCong = txtTenLoaiTienCong.getText().trim();
        String donGiaText = txtDonGiaTienCong.getText().trim();

        if (tenLoaiTienCong.isEmpty() || donGiaText.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ tên loại tiền công và đơn giá.");
            return;
        }

        double donGia;
        try {
            donGia = Double.parseDouble(donGiaText);
            if (donGia < 0) {
                AlertUtils.showWarningAlert("Lỗi dữ liệu", "Đơn giá không được âm.");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Đơn giá phải là một số hợp lệ.");
            return;
        }

        try {
            // Check for duplicate name
            if (loaiTienCongDAO.getLoaiTienCongByName(tenLoaiTienCong) != null) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên loại tiền công này đã tồn tại.");
                return;
            }

            LoaiTienCong newLoaiTienCong = new LoaiTienCong();
            newLoaiTienCong.setTenLoaiTienCong(tenLoaiTienCong);
            newLoaiTienCong.setDonGiaTienCong(donGia);

            int generatedId = loaiTienCongDAO.addLoaiTienCong(newLoaiTienCong);
            if (generatedId != -1) {
                AlertUtils.showInformationAlert("Thành công", "Thêm loại tiền công thành công!");
                loadLoaiTienCongData(); // Reload data to show the new item
                clearFields();
            } else {
                AlertUtils.showErrorAlert("Lỗi", "Không thể thêm loại tiền công.");
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm loại tiền công: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Sửa" button action. Updates an existing labor cost type.
     */
    @FXML
    private void handleSuaLoaiTienCong() {
        LoaiTienCong selectedLoaiTienCong = tblLoaiTienCong.getSelectionModel().getSelectedItem();
        if (selectedLoaiTienCong == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một loại tiền công để sửa.");
            return;
        }

        String tenLoaiTienCongMoi = txtTenLoaiTienCong.getText().trim();
        String donGiaTextMoi = txtDonGiaTienCong.getText().trim();

        if (tenLoaiTienCongMoi.isEmpty() || donGiaTextMoi.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ tên loại tiền công và đơn giá.");
            return;
        }

        double donGiaMoi;
        try {
            donGiaMoi = Double.parseDouble(donGiaTextMoi);
            if (donGiaMoi < 0) {
                AlertUtils.showWarningAlert("Lỗi dữ liệu", "Đơn giá không được âm.");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Đơn giá phải là một số hợp lệ.");
            return;
        }

        try {
            // Check for duplicate name, but allow update if it's the same item
            LoaiTienCong existingLoaiTienCong = loaiTienCongDAO.getLoaiTienCongByName(tenLoaiTienCongMoi);
            if (existingLoaiTienCong != null && existingLoaiTienCong.getMaLoaiTienCong() != selectedLoaiTienCong.getMaLoaiTienCong()) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên loại tiền công này đã tồn tại cho loại tiền công khác.");
                return;
            }

            selectedLoaiTienCong.setTenLoaiTienCong(tenLoaiTienCongMoi);
            selectedLoaiTienCong.setDonGiaTienCong(donGiaMoi);
            loaiTienCongDAO.updateLoaiTienCong(selectedLoaiTienCong);
            AlertUtils.showInformationAlert("Thành công", "Cập nhật loại tiền công thành công!");
            loadLoaiTienCongData();
            clearFields();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật loại tiền công: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa" button action. Deletes a selected labor cost type.
     */
    @FXML
    private void handleXoaLoaiTienCong() {
        LoaiTienCong selectedLoaiTienCong = tblLoaiTienCong.getSelectionModel().getSelectedItem();
        if (selectedLoaiTienCong == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một loại tiền công để xóa.");
            return;
        }

        if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa loại tiền công '" + selectedLoaiTienCong.getTenLoaiTienCong() + "' không?")) {
            try {
                loaiTienCongDAO.deleteLoaiTienCong(selectedLoaiTienCong.getMaLoaiTienCong());
                AlertUtils.showInformationAlert("Thành công", "Xóa loại tiền công thành công!");
                loadLoaiTienCongData();
                clearFields();
            } catch (SQLException e) {
                // Handle foreign key constraint violation (e.g., if there are repair details using this labor type)
                if (e.getSQLState().startsWith("23")) { // SQLSTATE for integrity constraint violation
                    AlertUtils.showErrorAlert("Lỗi ràng buộc", "Không thể xóa loại tiền công này vì có phiếu sửa chữa đang sử dụng. Vui lòng xóa các chi tiết sửa chữa liên quan trước.");
                } else {
                    AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi xóa loại tiền công: " + e.getMessage());
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
        loadLoaiTienCongData();
        AlertUtils.showInformationAlert("Làm mới", "Dữ liệu loại tiền công đã được làm mới.");
    }
}
