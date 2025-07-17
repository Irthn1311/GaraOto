package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.TiepNhan;
import model.PhieuThuTien;
import dao.TiepNhanDAO;
import dao.PhieuThuTienDAO;
import utils.AlertUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.text.NumberFormat;

public class ThuTienController {

    // FXML elements from ThuTienView.fxml
    @FXML
    private TextField txtBienSoXeSearch;
    @FXML
    private Button btnTimKiemHoSo;

    @FXML
    private TableView<TiepNhan> tblHoSoCanThanhToan;
    @FXML
    private TableColumn<TiepNhan, Integer> colMaTiepNhan;
    @FXML
    private TableColumn<TiepNhan, String> colBienSo;
    @FXML
    private TableColumn<TiepNhan, String> colTenChuXe;
    @FXML
    private TableColumn<TiepNhan, LocalDate> colNgayTiepNhan;
    @FXML
    private TableColumn<TiepNhan, Double> colTongTienNo;
    @FXML
    private TableColumn<TiepNhan, Boolean> colTrangThai; // For completion status

    @FXML
    private Label lblMaTiepNhan;
    @FXML
    private Label lblBienSoXe;
    @FXML
    private Label lblTenChuXeSelected;
    @FXML
    private Label lblDienThoaiSelected;
    @FXML
    private Label lblTongTienNoSelected;
    @FXML
    private DatePicker dpNgayThu;
    @FXML
    private TextField txtSoTienThu;
    @FXML
    private Label lblSoTienConNo;

    @FXML
    private Button btnLapPhieuThu;
    @FXML
    private Button btnLamMoi;

    // DAOs
    private TiepNhanDAO tiepNhanDAO;
    private PhieuThuTienDAO phieuThuTienDAO;

    // Data for the TableView
    private ObservableList<TiepNhan> danhSachHoSoCanThanhToan;

    // Currently selected TiepNhan record
    private TiepNhan selectedTiepNhan;

    // Currency formatting
    private NumberFormat currencyFormat;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize DAOs
        tiepNhanDAO = new TiepNhanDAO();
        phieuThuTienDAO = new PhieuThuTienDAO();

        // Set default date for DatePicker
        dpNgayThu.setValue(LocalDate.now());

        // Initialize ObservableList for table
        danhSachHoSoCanThanhToan = FXCollections.observableArrayList();
        tblHoSoCanThanhToan.setItems(danhSachHoSoCanThanhToan);

        // Configure TableView columns
        colMaTiepNhan.setCellValueFactory(cellData -> cellData.getValue().maTiepNhanProperty().asObject());
        colBienSo.setCellValueFactory(cellData -> cellData.getValue().bienSoProperty());
        colTenChuXe.setCellValueFactory(cellData -> cellData.getValue().tenChuXeProperty());
        colNgayTiepNhan.setCellValueFactory(cellData -> cellData.getValue().ngayTiepNhanProperty());
        colTongTienNo.setCellValueFactory(cellData -> cellData.getValue().tongTienNoProperty().asObject());
        colTrangThai.setCellValueFactory(cellData -> cellData.getValue().trangThaiHoanTatProperty().asObject());

        // Custom cell factory for currency formatting
        colTongTienNo.setCellFactory(tc -> new TableCell<TiepNhan, Double>() {
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

        // Add listener to table selection to display selected TiepNhan info
        tblHoSoCanThanhToan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTiepNhan = newSelection;
                displaySelectedTiepNhanInfo(newSelection);
                setPaymentFormEnabled(true);
            } else {
                selectedTiepNhan = null;
                clearSelectedTiepNhanInfo();
                setPaymentFormEnabled(false);
            }
        });

        // Add listener to SoTienThu TextField to calculate SoTienConNo
        txtSoTienThu.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                calculateSoTienConNo();
            }
        });

        // Disable payment form elements initially
        setPaymentFormEnabled(false);

        // Initialize currency format
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));
    }

    /**
     * Enables or disables input fields and buttons related to payment.
     * @param enable true to enable, false to disable.
     */
    private void setPaymentFormEnabled(boolean enable) {
        dpNgayThu.setDisable(!enable);
        txtSoTienThu.setDisable(!enable);
        btnLapPhieuThu.setDisable(!enable);
    }

    /**
     * Handles the "Tìm kiếm" button action. Searches for TiepNhan records by BienSoXe.
     */
    @FXML
    private void handleTimKiemHoSo() {
        String bienSoXe = txtBienSoXeSearch.getText().trim();
        if (bienSoXe.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập biển số xe để tìm kiếm.");
            return;
        }

        try {
            // Get all TiepNhan records for the given BienSo
            // Note: TiepNhanDAO.getTiepNhanByBienSo should return all records,
            // then we filter for outstanding ones.
            ObservableList<TiepNhan> allTiepNhanRecords = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            ObservableList<TiepNhan> outstandingTiepNhanRecords = FXCollections.observableArrayList();

            for (TiepNhan tn : allTiepNhanRecords) {
                if (tn.getTongTienNo() > 0) { // Filter for records with outstanding debt
                    outstandingTiepNhanRecords.add(tn);
                }
            }

            if (outstandingTiepNhanRecords.isEmpty()) {
                AlertUtils.showInformationAlert("Không tìm thấy", "Không tìm thấy hồ sơ tiếp nhận nào còn nợ cho biển số xe: " + bienSoXe);
                danhSachHoSoCanThanhToan.clear();
                clearSelectedTiepNhanInfo();
                setPaymentFormEnabled(false);
            } else {
                danhSachHoSoCanThanhToan.clear();
                danhSachHoSoCanThanhToan.addAll(outstandingTiepNhanRecords);
                // Automatically select the first item if only one, or for convenience
                if (!outstandingTiepNhanRecords.isEmpty()) {
                    tblHoSoCanThanhToan.getSelectionModel().selectFirst();
                }
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tìm kiếm", "Đã xảy ra lỗi khi tìm kiếm hồ sơ tiếp nhận: " + e.getMessage());
            e.printStackTrace();
            danhSachHoSoCanThanhToan.clear();
            clearSelectedTiepNhanInfo();
            setPaymentFormEnabled(false);
        }
    }

    /**
     * Displays the information of the selected TiepNhan record on the labels.
     * @param tiepNhan The TiepNhan object to display.
     */
    private void displaySelectedTiepNhanInfo(TiepNhan tiepNhan) {
        lblMaTiepNhan.setText(String.valueOf(tiepNhan.getMaTiepNhan()));
        lblBienSoXe.setText(tiepNhan.getBienSo());
        lblTenChuXeSelected.setText(tiepNhan.getTenChuXe());
        lblDienThoaiSelected.setText(tiepNhan.getDienThoaiChuXe());
        lblTongTienNoSelected.setText(currencyFormat.format(tiepNhan.getTongTienNo()));
        calculateSoTienConNo(); // Recalculate based on current input
    }

    /**
     * Clears all selected TiepNhan information labels.
     */
    private void clearSelectedTiepNhanInfo() {
        lblMaTiepNhan.setText("[Chưa chọn]");
        lblBienSoXe.setText("[Chưa chọn]");
        lblTenChuXeSelected.setText("[Chưa chọn]");
        lblDienThoaiSelected.setText("[Chưa chọn]");
        lblTongTienNoSelected.setText(currencyFormat.format(0.0));
        lblSoTienConNo.setText(currencyFormat.format(0.0));
        txtSoTienThu.clear();
    }

    /**
     * Calculates and displays the remaining debt amount based on the entered payment.
     */
    private void calculateSoTienConNo() {
        if (selectedTiepNhan == null) {
            lblSoTienConNo.setText(currencyFormat.format(0.0));
            return;
        }

        double tongTienNo = selectedTiepNhan.getTongTienNo();
        double soTienThu = 0.0;
        try {
            if (!txtSoTienThu.getText().trim().isEmpty()) {
                soTienThu = Double.parseDouble(txtSoTienThu.getText().trim());
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input for calculation, will be validated on form submission
            soTienThu = 0.0;
        }

        double conNo = tongTienNo - soTienThu;
        lblSoTienConNo.setText(currencyFormat.format(conNo));

        // Highlight if remaining debt is negative (overpayment) or exactly zero
        if (conNo < 0) {
            lblSoTienConNo.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;"); // Overpayment
        } else if (conNo == 0) {
            lblSoTienConNo.setStyle("-fx-text-fill: green; -fx-font-weight: bold;"); // Fully paid
        } else {
            lblSoTienConNo.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Still debt
        }
    }

    /**
     * Handles the "Lập phiếu thu" button action. Creates and saves a new payment receipt.
     */
    @FXML
    private void handleLapPhieuThu() {
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn một hồ sơ tiếp nhận để lập phiếu thu.");
            return;
        }

        LocalDate ngayThu = dpNgayThu.getValue();
        if (ngayThu == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn ngày thu tiền.");
            return;
        }

        double soTienThu;
        try {
            soTienThu = Double.parseDouble(txtSoTienThu.getText().trim());
            if (soTienThu <= 0) {
                AlertUtils.showWarningAlert("Lỗi nhập liệu", "Số tiền thu phải lớn hơn 0.");
                return;
            }
        } catch (NumberFormatException e) {
            AlertUtils.showWarningAlert("Lỗi nhập liệu", "Số tiền thu không hợp lệ. Vui lòng nhập một số.");
            return;
        }

        double tongTienNoHienTai = selectedTiepNhan.getTongTienNo();
        if (soTienThu > tongTienNoHienTai && tongTienNoHienTai > 0) {
            AlertUtils.showWarningAlert("Cảnh báo", "Số tiền thu vượt quá số tiền nợ hiện tại. Vui lòng kiểm tra lại.");
            // Optionally, you could ask for confirmation to accept overpayment
            // For now, we'll just warn and let them proceed if they insist
        }

        if (tongTienNoHienTai == 0 && soTienThu > 0) {
            AlertUtils.showWarningAlert("Cảnh báo", "Hồ sơ này không còn nợ. Không thể lập phiếu thu tiền.");
            return;
        }


        try {
            // 1. Create PhieuThuTien
            PhieuThuTien phieuThuTien = new PhieuThuTien();
            phieuThuTien.setMaTiepNhan(selectedTiepNhan.getMaTiepNhan());
            phieuThuTien.setNgayThu(ngayThu);
            phieuThuTien.setSoTienThu(soTienThu);

            phieuThuTienDAO.addPhieuThuTien(phieuThuTien);

            // 2. Update TongTienNo for the selected TiepNhan record
            double newTongTienNo = tongTienNoHienTai - soTienThu;
            boolean trangThaiHoanTat = (newTongTienNo <= 0); // Mark as complete if debt is zero or less

            selectedTiepNhan.setTongTienNo(newTongTienNo); // Update local object
            selectedTiepNhan.setTrangThaiHoanTat(trangThaiHoanTat); // Update local object

            tiepNhanDAO.updateTongTienNoAndTrangThai(selectedTiepNhan.getMaTiepNhan(), newTongTienNo, trangThaiHoanTat);

            AlertUtils.showInformationAlert("Thành công", "Lập phiếu thu tiền thành công!");
            handleLamMoi(); // Clear form and refresh table

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi lập phiếu thu tiền: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears all input fields and table.
     */
    @FXML
    private void handleLamMoi() {
        txtBienSoXeSearch.clear();
        danhSachHoSoCanThanhToan.clear();
        clearSelectedTiepNhanInfo();
        dpNgayThu.setValue(LocalDate.now());
        setPaymentFormEnabled(false);
        selectedTiepNhan = null;
    }
}
