package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.TiepNhan; // Import TiepNhan entity
import model.VatTu;    // New entity for VatTu
import model.LoaiTienCong; // New entity for TienCong
import model.PhieuSuaChua; // New entity for PhieuSuaChua
import model.ChiTietSuaChua; // New entity for ChiTietSuaChua
import model.Tho; // Import Tho entity

import dao.TiepNhanDAO; // DAO for TiepNhan
import dao.VatTuDAO;    // New DAO for VatTu
import dao.LoaiTienCongDAO; // New DAO for TienCong
import dao.PhieuSuaChuaDAO; // New DAO for PhieuSuaChua
import dao.ChiTietSuaChuaDAO; // New DAO for ChiTietSuaChua
import dao.ThoDAO; // New DAO for Tho

import utils.AlertUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional; // For confirmation dialogs

public class SuaChuaController {

    // FXML elements from SuaChuaView.fxml
    @FXML
    private TextField txtBienSoXeSearch;
    @FXML
    private Button btnTimKiemXe;
    @FXML
    private Button btnChonHoSo; // For selecting a specific TiepNhan record if multiple exist

    @FXML
    private Label lblTenChuXe;
    @FXML
    private Label lblHieuXe;
    @FXML
    private Label lblDienThoai;
    @FXML
    private Label lblDiaChi;
    @FXML
    private Label lblNgayTiepNhan;
    @FXML
    private Label lblTienNoHienTai;

    @FXML
    private ComboBox<String> cbVatTu;
    @FXML
    private TextField txtSoLuongVatTu;
    @FXML
    private ComboBox<String> cbTienCong;
    @FXML
    private Button btnThemChiTiet;
    @FXML
    private TableView<ChiTietSuaChua> tblChiTietSuaChua;
    @FXML
    private TableColumn<ChiTietSuaChua, Integer> colSTT;
    @FXML
    private TableColumn<ChiTietSuaChua, String> colNoiDung;
    @FXML
    private TableColumn<ChiTietSuaChua, String> colLoai;
    @FXML
    private TableColumn<ChiTietSuaChua, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietSuaChua, Double> colDonGia;
    @FXML
    private TableColumn<ChiTietSuaChua, Double> colThanhTienChiTiet;
    @FXML
    private Button btnXoaChiTiet;
    @FXML
    private Label lblTongTienPhieuSC;

    @FXML
    private DatePicker dpNgaySuaChua;
    @FXML
    private TextArea txtGhiChu;
    @FXML
    private ComboBox<String> cbThoPhanCong; // New: ComboBox for assigning mechanic
    @FXML
    private Button btnLapPhieuSuaChua;
    @FXML
    private Button btnLamMoiPhieu;

    // DAOs
    private TiepNhanDAO tiepNhanDAO;
    private VatTuDAO vatTuDAO;
    private LoaiTienCongDAO tienCongDAO;
    private PhieuSuaChuaDAO phieuSuaChuaDAO;
    private ChiTietSuaChuaDAO chiTietSuaChuaDAO;
    private ThoDAO thoDAO; // New DAO for Tho

    // Data for the TableView
    private ObservableList<ChiTietSuaChua> danhSachChiTietSuaChua;

    // Currently selected TiepNhan record
    private TiepNhan selectedTiepNhan;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize DAOs
        tiepNhanDAO = new TiepNhanDAO();
        vatTuDAO = new VatTuDAO();
        tienCongDAO = new LoaiTienCongDAO();
        phieuSuaChuaDAO = new PhieuSuaChuaDAO();
        chiTietSuaChuaDAO = new ChiTietSuaChuaDAO();
        thoDAO = new ThoDAO(); // Initialize ThoDAO

        // Set default date for DatePicker
        dpNgaySuaChua.setValue(LocalDate.now());

        // Initialize ObservableList for table
        danhSachChiTietSuaChua = FXCollections.observableArrayList();
        tblChiTietSuaChua.setItems(danhSachChiTietSuaChua);

        // Configure TableView columns
        colSTT.setCellValueFactory(cellData -> cellData.getValue().sttProperty().asObject());
        colNoiDung.setCellValueFactory(cellData -> cellData.getValue().noiDungProperty());
        colLoai.setCellValueFactory(cellData -> cellData.getValue().loaiProperty());
        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());
        colThanhTienChiTiet.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());

        // Add listener to update total when details change
        danhSachChiTietSuaChua.addListener((javafx.collections.ListChangeListener.Change<? extends ChiTietSuaChua> c) -> {
            calculateAndDisplayTotal();
        });

        // Load initial data for ComboBoxes
        loadVatTuData();
        loadTienCongData();
        loadThoData(); // Load mechanic data

        // Disable elements until a TiepNhan record is selected
        setFormEnabled(false);
    }

    /**
     * Enables or disables input fields and buttons related to repair details and slip creation.
     * @param enable true to enable, false to disable.
     */
    private void setFormEnabled(boolean enable) {
        cbVatTu.setDisable(!enable);
        txtSoLuongVatTu.setDisable(!enable);
        cbTienCong.setDisable(!enable);
        btnThemChiTiet.setDisable(!enable);
        tblChiTietSuaChua.setDisable(!enable);
        btnXoaChiTiet.setDisable(!enable);
        dpNgaySuaChua.setDisable(!enable);
        txtGhiChu.setDisable(!enable);
        cbThoPhanCong.setDisable(!enable); // Enable/disable mechanic combobox
        btnLapPhieuSuaChua.setDisable(!enable);
        btnLamMoiPhieu.setDisable(!enable);
    }

    /**
     * Loads VatTu (parts) from the database into the ComboBox.
     */
    private void loadVatTuData() {
        try {
            ObservableList<String> vatTuNames = FXCollections.observableArrayList();
            for (VatTu vt : vatTuDAO.getAllVatTu()) {
                vatTuNames.add(vt.getTenVatTu());
            }
            cbVatTu.setItems(vatTuNames);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách vật tư từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    /**
     * Loads TienCong (labor services) from the database into the ComboBox.
     */
    private void loadTienCongData() {
        try {
            ObservableList<String> tienCongContents = FXCollections.observableArrayList();
            for (LoaiTienCong tc : tienCongDAO.getAllLoaiTienCong()) {
                tienCongContents.add(tc.getTenLoaiTienCong());
            }
            cbTienCong.setItems(tienCongContents);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách tiền công từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    /**
     * Loads Tho (mechanics) from the database into the ComboBox.
     */
    private void loadThoData() {
        try {
            ObservableList<String> thoNames = FXCollections.observableArrayList();
            for (Tho tho : thoDAO.getAllTho()) {
                thoNames.add(tho.getTenTho());
            }
            cbThoPhanCong.setItems(thoNames);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách thợ từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Tìm kiếm" button action. Searches for TiepNhan records by BienSoXe.
     */
    @FXML
    private void handleTimKiemXe() {
        String bienSoXe = txtBienSoXeSearch.getText().trim();
        if (bienSoXe.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập biển số xe để tìm kiếm.");
            return;
        }

        try {
            ObservableList<TiepNhan> tiepNhanRecords = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            if (tiepNhanRecords.isEmpty()) {
                AlertUtils.showInformationAlert("Không tìm thấy", "Không tìm thấy hồ sơ tiếp nhận nào cho biển số xe: " + bienSoXe);
                clearVehicleInfoLabels();
                setFormEnabled(false);
            } else if (tiepNhanRecords.size() == 1) {
                selectedTiepNhan = tiepNhanRecords.get(0);
                displayTiepNhanInfo(selectedTiepNhan);
                setFormEnabled(true);
            } else {
                // Multiple records found, prompt user to choose
                showTiepNhanSelectionDialog(tiepNhanRecords);
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tìm kiếm", "Đã xảy ra lỗi khi tìm kiếm hồ sơ tiếp nhận: " + e.getMessage());
            e.printStackTrace();
            clearVehicleInfoLabels();
            setFormEnabled(false);
        }
    }

    /**
     * Handles the "Chọn hồ sơ" button action. (This button is primarily for when multiple records are found)
     * For now, it can be used to re-trigger the selection dialog if needed.
     */
    @FXML
    private void handleChonHoSo() {
        String bienSoXe = txtBienSoXeSearch.getText().trim();
        if (bienSoXe.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập biển số xe để chọn hồ sơ.");
            return;
        }
        try {
            ObservableList<TiepNhan> tiepNhanRecords = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            if (tiepNhanRecords.isEmpty()) {
                AlertUtils.showInformationAlert("Không tìm thấy", "Không tìm thấy hồ sơ tiếp nhận nào cho biển số xe: " + bienSoXe);
            } else {
                showTiepNhanSelectionDialog(tiepNhanRecords);
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi chọn hồ sơ", "Đã xảy ra lỗi khi tải danh sách hồ sơ: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays a dialog for the user to select a specific TiepNhan record.
     * @param records The list of TiepNhan records to choose from.
     */
    private void showTiepNhanSelectionDialog(ObservableList<TiepNhan> records) {
        Dialog<TiepNhan> dialog = new Dialog<>();
        dialog.setTitle("Chọn Hồ Sơ Tiếp Nhận");
        dialog.setHeaderText("Có nhiều hồ sơ tiếp nhận cho biển số này. Vui lòng chọn một:");

        // Set the button types.
        ButtonType selectButtonType = new ButtonType("Chọn", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        TableView<TiepNhan> selectionTable = new TableView<>();
        selectionTable.setItems(records);

        TableColumn<TiepNhan, Integer> colMaTiepNhan = new TableColumn<>("Mã TN");
        colMaTiepNhan.setCellValueFactory(cellData -> cellData.getValue().maTiepNhanProperty().asObject());
        colMaTiepNhan.setPrefWidth(80);

        TableColumn<TiepNhan, String> colBienSoDialog = new TableColumn<>("Biển số");
        colBienSoDialog.setCellValueFactory(cellData -> cellData.getValue().bienSoProperty());
        colBienSoDialog.setPrefWidth(120);

        TableColumn<TiepNhan, String> colTenChuXeDialog = new TableColumn<>("Chủ xe");
        colTenChuXeDialog.setCellValueFactory(cellData -> cellData.getValue().tenChuXeProperty());
        colTenChuXeDialog.setPrefWidth(150);

        TableColumn<TiepNhan, LocalDate> colNgayTiepNhanDialog = new TableColumn<>("Ngày TN");
        colNgayTiepNhanDialog.setCellValueFactory(cellData -> cellData.getValue().ngayTiepNhanProperty());
        colNgayTiepNhanDialog.setPrefWidth(100);

        TableColumn<TiepNhan, Double> colTongTienNoDialog = new TableColumn<>("Tiền nợ");
        colTongTienNoDialog.setCellValueFactory(cellData -> cellData.getValue().tongTienNoProperty().asObject());
        colTongTienNoDialog.setPrefWidth(100);

        selectionTable.getColumns().addAll(colMaTiepNhan, colBienSoDialog, colTenChuXeDialog, colNgayTiepNhanDialog, colTongTienNoDialog);

        dialog.getDialogPane().setContent(selectionTable);

        // Convert the result to a TiepNhan object when the select button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return selectionTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<TiepNhan> result = dialog.showAndWait();

        result.ifPresent(tiepNhan -> {
            selectedTiepNhan = tiepNhan;
            displayTiepNhanInfo(selectedTiepNhan);
            setFormEnabled(true);
            danhSachChiTietSuaChua.clear(); // Clear previous details when new record is selected
            calculateAndDisplayTotal(); // Recalculate total
        });
    }


    /**
     * Displays the information of the selected TiepNhan record on the labels.
     * @param tiepNhan The TiepNhan object to display.
     */
    private void displayTiepNhanInfo(TiepNhan tiepNhan) {
        lblTenChuXe.setText(tiepNhan.getTenChuXe());
        lblHieuXe.setText(tiepNhan.getTenHieuXe());
        lblDienThoai.setText(tiepNhan.getDienThoaiChuXe());
        lblDiaChi.setText(tiepNhan.getDiaChiChuXe());
        lblNgayTiepNhan.setText(tiepNhan.getNgayTiepNhan().toString());
        lblTienNoHienTai.setText(String.format("%.2f VNĐ", tiepNhan.getTongTienNo()));
    }

    /**
     * Clears all vehicle information labels.
     */
    private void clearVehicleInfoLabels() {
        lblTenChuXe.setText("[Chưa chọn]");
        lblHieuXe.setText("[Chưa chọn]");
        lblDienThoai.setText("[Chưa chọn]");
        lblDiaChi.setText("[Chưa chọn]");
        lblNgayTiepNhan.setText("[Chưa chọn]");
        lblTienNoHienTai.setText("0.00 VNĐ");
    }

    /**
     * Handles the "Thêm" button action. Adds a new repair detail to the table.
     */
    @FXML
    private void handleThemChiTiet() {
        String selectedVatTu = cbVatTu.getValue();
        String soLuongText = txtSoLuongVatTu.getText().trim();
        String selectedTienCong = cbTienCong.getValue();

        // Validate inputs
        if (selectedVatTu == null && selectedTienCong == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn Vật tư hoặc Tiền công.");
            return;
        }

        int soLuong = 0;
        if (selectedVatTu != null) {
            if (soLuongText.isEmpty() || !soLuongText.matches("\\d+")) {
                AlertUtils.showWarningAlert("Lỗi số lượng", "Vui lòng nhập số lượng vật tư hợp lệ.");
                return;
            }
            soLuong = Integer.parseInt(soLuongText);
            if (soLuong <= 0) {
                AlertUtils.showWarningAlert("Lỗi số lượng", "Số lượng vật tư phải lớn hơn 0.");
                return;
            }
        }

        try {
            ChiTietSuaChua chiTiet = new ChiTietSuaChua();
            double donGia = 0.0;
            double thanhTien = 0.0;

            if (selectedVatTu != null) {
                VatTu vatTu = vatTuDAO.getVatTuByName(selectedVatTu);
                if (vatTu == null) {
                    AlertUtils.showErrorAlert("Lỗi", "Vật tư không tồn tại.");
                    return;
                }
                chiTiet.setMaVatTu(vatTu.getMaVatTu());
                chiTiet.setNoiDung(vatTu.getTenVatTu());
                chiTiet.setLoai("Vật tư");
                chiTiet.setSoLuong(soLuong);
                chiTiet.setDonGia(vatTu.getDonGiaBan()); // Use DonGiaBan from VatTu
                thanhTien += vatTu.getDonGiaBan() * soLuong; // Use DonGiaBan for calculation
                donGia = vatTu.getDonGiaBan(); // Set for display
            }

            if (selectedTienCong != null) {
                LoaiTienCong tienCong = tienCongDAO.getLoaiTienCongByName(selectedTienCong);
                if (tienCong == null) {
                    AlertUtils.showErrorAlert("Lỗi", "Tiền công không tồn tại.");
                    return;
                }
                // If both vatTu and tienCong are selected, combine them into one detail line
                if (selectedVatTu != null) {
                    chiTiet.setMaLoaiTienCong(tienCong.getMaLoaiTienCong()); // Use MaLoaiTienCong
                    chiTiet.setNoiDung(chiTiet.getNoiDung() + " + " + tienCong.getTenLoaiTienCong());
                    chiTiet.setLoai("Vật tư & Công");
                    thanhTien += tienCong.getDonGiaTienCong(); // Add labor cost
                    // DonGia for display in this case is tricky, might show 0 or average or sum
                    // For simplicity, let's just show the total for this detail line
                } else {
                    chiTiet.setMaLoaiTienCong(tienCong.getMaLoaiTienCong()); // Use MaLoaiTienCong
                    chiTiet.setNoiDung(tienCong.getTenLoaiTienCong());
                    chiTiet.setLoai("Tiền công");
                    chiTiet.setSoLuong(1); // Labor is usually 1 unit
                    chiTiet.setDonGia(tienCong.getDonGiaTienCong());
                    thanhTien += tienCong.getDonGiaTienCong();
                    donGia = tienCong.getDonGiaTienCong(); // Set for display
                }
            }

            // Ensure DonGia is set for display even if it's a combined item or only labor
            if (selectedVatTu == null && selectedTienCong != null) {
                chiTiet.setDonGia(donGia); // Set the labor cost as DonGia
            } else if (selectedVatTu != null && selectedTienCong == null) {
                chiTiet.setDonGia(donGia); // Set the material cost as DonGia
            } else if (selectedVatTu != null && selectedTienCong != null) {
                // For combined, DonGia might not be meaningful as a single value
                // We can set it to 0 or leave it as the material's price.
                // For now, let's set it to the material's price if material exists, else labor's price
                chiTiet.setDonGia(donGia); // This will be material's price if selectedVatTu != null
            }


            chiTiet.setThanhTien(thanhTien);
            chiTiet.setStt(danhSachChiTietSuaChua.size() + 1); // Set STT

            danhSachChiTietSuaChua.add(chiTiet);

            // Clear inputs after adding
            cbVatTu.getSelectionModel().clearSelection();
            txtSoLuongVatTu.clear();
            cbTienCong.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi thêm chi tiết", "Đã xảy ra lỗi khi thêm chi tiết sửa chữa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xóa mục" button action. Removes selected repair detail from the table.
     */
    @FXML
    private void handleXoaChiTiet() {
        ChiTietSuaChua selectedItem = tblChiTietSuaChua.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Xác nhận xóa");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Bạn có chắc chắn muốn xóa mục này?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                danhSachChiTietSuaChua.remove(selectedItem);
                // Re-index STT after removal
                for (int i = 0; i < danhSachChiTietSuaChua.size(); i++) {
                    danhSachChiTietSuaChua.get(i).setStt(i + 1);
                }
            }
        } else {
            AlertUtils.showWarningAlert("Chưa chọn mục", "Vui lòng chọn một mục để xóa.");
        }
    }

    /**
     * Calculates the total amount of the repair slip and updates the label.
     */
    private void calculateAndDisplayTotal() {
        double total = 0.0;
        for (ChiTietSuaChua chiTiet : danhSachChiTietSuaChua) {
            total += chiTiet.getThanhTien();
        }
        lblTongTienPhieuSC.setText(String.format("%.2f VNĐ", total));
    }

    /**
     * Handles the "Lập phiếu sửa chữa" button action. Saves the repair slip and its details to the database.
     */
    @FXML
    private void handleLapPhieuSuaChua() {
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn một hồ sơ tiếp nhận trước khi lập phiếu sửa chữa.");
            return;
        }
        if (danhSachChiTietSuaChua.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu chi tiết", "Vui lòng thêm ít nhất một chi tiết sửa chữa.");
            return;
        }
        LocalDate ngaySuaChua = dpNgaySuaChua.getValue();
        if (ngaySuaChua == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn ngày sửa chữa.");
            return;
        }

        String selectedThoName = cbThoPhanCong.getValue();
        Integer maTho = null;
        if (selectedThoName != null && !selectedThoName.isEmpty()) {
            try {
                Tho tho = thoDAO.getThoByName(selectedThoName); // Assuming you have getThoByName in ThoDAO
                if (tho != null) {
                    maTho = tho.getMaTho();
                }
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi thợ", "Không thể lấy thông tin thợ: " + e.getMessage());
                e.printStackTrace();
                return;
            }
        }

        try {
            // 1. Create PhieuSuaChua
            PhieuSuaChua phieuSuaChua = new PhieuSuaChua();
            phieuSuaChua.setMaTiepNhan(selectedTiepNhan.getMaTiepNhan());
            phieuSuaChua.setNgaySuaChua(ngaySuaChua);
            phieuSuaChua.setGhiChu(txtGhiChu.getText().trim());
            phieuSuaChua.setTongTien(Double.parseDouble(lblTongTienPhieuSC.getText().replace(" VNĐ", "").replace(",", ""))); // Get total from label
            if (maTho != null) {
                phieuSuaChua.setMaTho(maTho);
            }
            phieuSuaChua.setTrangThaiHoanTat(false); // Initially not completed

            int maPhieuSC = phieuSuaChuaDAO.addPhieuSuaChua(phieuSuaChua);

            // 2. Add ChiTietSuaChua
            for (ChiTietSuaChua chiTiet : danhSachChiTietSuaChua) {
                chiTiet.setMaPhieuSC(maPhieuSC);
                chiTietSuaChuaDAO.addChiTietSuaChua(chiTiet);

                // 3. Update SoLuongTon for VatTu
                if (chiTiet.getMaVatTu() != 0) { // Assuming 0 means no MaVatTu
                    VatTu vatTu = vatTuDAO.getVatTuById(chiTiet.getMaVatTu());
                    if (vatTu != null) {
                        vatTu.setSoLuongTon(vatTu.getSoLuongTon() - chiTiet.getSoLuong());
                        vatTuDAO.updateVatTu(vatTu);
                    }
                }
            }

            // 4. Update TongTienNo for the selected TiepNhan record
            double currentTongTienNo = selectedTiepNhan.getTongTienNo();
            double newTongTienNo = currentTongTienNo + phieuSuaChua.getTongTien();
            selectedTiepNhan.setTongTienNo(newTongTienNo);
            // TrangThaiHoanTat for TiepNhan should only be true if TongTienNo is 0
            // We don't set it to true here, only when payment is received and debt becomes 0
            tiepNhanDAO.updateTongTienNoAndTrangThai(selectedTiepNhan.getMaTiepNhan(), newTongTienNo, selectedTiepNhan.isTrangThaiHoanTat());

            AlertUtils.showInformationAlert("Thành công", "Lập phiếu sửa chữa thành công!");
            handleLamMoiPhieu(); // Clear form after successful submission

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi lập phiếu sửa chữa: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi dữ liệu", "Tổng tiền không hợp lệ. Vui lòng kiểm tra lại.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears all input fields and table.
     */
    @FXML
    private void handleLamMoiPhieu() {
        txtBienSoXeSearch.clear();
        clearVehicleInfoLabels();
        danhSachChiTietSuaChua.clear();
        calculateAndDisplayTotal();
        dpNgaySuaChua.setValue(LocalDate.now());
        txtGhiChu.clear();
        cbVatTu.getSelectionModel().clearSelection();
        txtSoLuongVatTu.clear();
        cbTienCong.getSelectionModel().clearSelection();
        cbThoPhanCong.getSelectionModel().clearSelection(); // Clear mechanic selection
        selectedTiepNhan = null; // Clear selected TiepNhan
        setFormEnabled(false);
    }
}
