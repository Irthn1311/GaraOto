package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.TienCong;
import dao.TienCongDAO;
import utils.AlertUtils;

import java.sql.SQLException;

public class QuanLyTienCongController {

    @FXML
    private TableView<TienCong> tblLoaiTienCong;
    @FXML
    private TableColumn<TienCong, Integer> colMaLoaiTienCong;
    @FXML
    private TableColumn<TienCong, String> colNoiDungLoaiTienCong;
    @FXML
    private TableColumn<TienCong, Double> colDonGiaLoaiTienCong;
    @FXML
    private TextField txtMaLoaiTienCong;
    @FXML
    private TextField txtNoiDungLoaiTienCong;
    @FXML
    private TextField txtDonGiaLoaiTienCong;
    @FXML
    private Button btnThem;
    @FXML
    private Button btnSua;
    @FXML
    private Button btnXoa;
    @FXML
    private Button btnLamMoi;

    private TienCongDAO tienCongDAO;
    private ObservableList<TienCong> tienCongList;

    @FXML
    public void initialize() {
        tienCongDAO = new TienCongDAO();
        tienCongList = FXCollections.observableArrayList();

        // Configure table columns
        colMaLoaiTienCong.setCellValueFactory(cellData -> cellData.getValue().maTienCongProperty().asObject());
        colNoiDungLoaiTienCong.setCellValueFactory(cellData -> cellData.getValue().noiDungProperty());
        colDonGiaLoaiTienCong.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());

        loadLoaiTienCongData();

        // Listener for table selection to populate text fields for editing
        tblLoaiTienCong.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showLoaiTienCongDetails(newValue));
    }

    private void loadLoaiTienCongData() {
        try {
            tienCongList.setAll(tienCongDAO.getAllTienCong());
            tblLoaiTienCong.setItems(tienCongList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách tiền công: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showLoaiTienCongDetails(TienCong tienCong) {
        if (tienCong != null) {
            txtMaLoaiTienCong.setText(String.valueOf(tienCong.getMaTienCong()));
            txtNoiDungLoaiTienCong.setText(tienCong.getNoiDung());
            txtDonGiaLoaiTienCong.setText(String.valueOf(tienCong.getDonGia()));
        } else {
            clearFields();
        }
    }

    @FXML
    private void handleThemLoaiTienCong() {
        if (isInputValid()) {
            String noiDung = txtNoiDungLoaiTienCong.getText();
            double donGia = Double.parseDouble(txtDonGiaLoaiTienCong.getText());

            TienCong newTienCong = new TienCong(0, noiDung, donGia);
            try {
                int id = tienCongDAO.addTienCong(newTienCong);
                if (id != -1) {
                    newTienCong.setMaTienCong(id);
                    tienCongList.add(newTienCong);
                    AlertUtils.showInformationAlert("Thành công", "Thêm loại tiền công thành công!");
                    clearFields();
                } else {
                    AlertUtils.showErrorAlert("Lỗi", "Không thể thêm loại tiền công.");
                }
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm loại tiền công: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSuaLoaiTienCong() {
        TienCong selectedTienCong = tblLoaiTienCong.getSelectionModel().getSelectedItem();
        if (selectedTienCong != null) {
            if (isInputValid()) {
                String noiDung = txtNoiDungLoaiTienCong.getText();
                double donGia = Double.parseDouble(txtDonGiaLoaiTienCong.getText());

                selectedTienCong.setNoiDung(noiDung);
                selectedTienCong.setDonGia(donGia);

                try {
                    if (tienCongDAO.updateTienCong(selectedTienCong)) {
                        tblLoaiTienCong.refresh();
                        AlertUtils.showInformationAlert("Thành công", "Cập nhật loại tiền công thành công!");
                        clearFields();
                    } else {
                        AlertUtils.showErrorAlert("Lỗi", "Không thể cập nhật loại tiền công.");
                    }
                } catch (SQLException e) {
                    AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật loại tiền công: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn loại tiền công để sửa.");
        }
    }

    @FXML
    private void handleXoaLoaiTienCong() {
        TienCong selectedTienCong = tblLoaiTienCong.getSelectionModel().getSelectedItem();
        if (selectedTienCong != null) {
            if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa loại tiền công này không?")) {
                try {
                    if (tienCongDAO.deleteTienCong(selectedTienCong.getMaTienCong())) {
                        tienCongList.remove(selectedTienCong);
                        AlertUtils.showInformationAlert("Thành công", "Xóa loại tiền công thành công!");
                        clearFields();
                    } else {
                        AlertUtils.showErrorAlert("Lỗi", "Không thể xóa loại tiền công.");
                    }
                } catch (SQLException e) {
                    AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi xóa loại tiền công: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn loại tiền công để xóa.");
        }
    }

    @FXML
    private void handleLamMoi() {
        clearFields();
        loadLoaiTienCongData();
    }

    private void clearFields() {
        txtMaLoaiTienCong.setText("");
        txtNoiDungLoaiTienCong.setText("");
        txtDonGiaLoaiTienCong.setText("");
        tblLoaiTienCong.getSelectionModel().clearSelection();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (txtNoiDungLoaiTienCong.getText() == null || txtNoiDungLoaiTienCong.getText().isEmpty()) {
            errorMessage += "Vui lòng nhập nội dung tiền công!\n";
        }
        if (txtDonGiaLoaiTienCong.getText() == null || txtDonGiaLoaiTienCong.getText().isEmpty()) {
            errorMessage += "Vui lòng nhập đơn giá tiền công!\n";
        } else {
            try {
                double donGia = Double.parseDouble(txtDonGiaLoaiTienCong.getText());
                if (donGia < 0) {
                    errorMessage += "Đơn giá tiền công không thể âm!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Đơn giá tiền công phải là số!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            AlertUtils.showWarningAlert("Thông tin không hợp lệ", errorMessage);
            return false;
        }
    }
}
