package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.cell.PropertyValueFactory;

import model.TiepNhan; // Import TiepNhan entity
import model.VatTu;    // New entity for VatTu
import model.TienCong; // Import TienCong
import model.PhieuSuaChua; // New entity for PhieuSuaChua
import model.ChiTietPhieuSuaChua_VatTu; // New model for part details
import model.ChiTietPhieuSuaChua_TienCong; // New model for labor details
import model.Tho; // Import Tho entity

import dao.TiepNhanDAO; // DAO for TiepNhan
import dao.VatTuDAO;    // New DAO for VatTu
import dao.TienCongDAO; // New DAO for TienCong
import dao.PhieuSuaChuaDAO; // New DAO for PhieuSuaChua
import dao.ChiTietPhieuSuaChua_VatTuDAO; // New DAO for part details
import dao.ChiTietPhieuSuaChua_TienCongDAO; // New DAO for labor details
import dao.ThoDAO; // New DAO for Tho
import dao.ChiTietPhieuNhapKhoVatTuDAO; // New DAO for getting DonGiaNhap

import utils.AlertUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional; // For confirmation dialogs

public class SuaChuaController {
    
    // A simple class to display repair details in a unified TableView
    public static class ChiTietSuaChuaDisplay {
        private final IntegerProperty stt;
        private final StringProperty noiDung;
        private final StringProperty loai;
        private final IntegerProperty soLuong;
        private final DoubleProperty donGia;
        private final DoubleProperty thanhTien;
        private final Object originalObject; // To hold the original VatTu or TienCong detail object

        public ChiTietSuaChuaDisplay(int stt, String noiDung, String loai, int soLuong, double donGia, double thanhTien, Object originalObject) {
            this.stt = new SimpleIntegerProperty(stt);
            this.noiDung = new SimpleStringProperty(noiDung);
            this.loai = new SimpleStringProperty(loai);
            this.soLuong = new SimpleIntegerProperty(soLuong);
            this.donGia = new SimpleDoubleProperty(donGia);
            this.thanhTien = new SimpleDoubleProperty(thanhTien);
            this.originalObject = originalObject;
        }

        public IntegerProperty sttProperty() { return stt; }
        public StringProperty noiDungProperty() { return noiDung; }
        public StringProperty loaiProperty() { return loai; }
        public IntegerProperty soLuongProperty() { return soLuong; }
        public DoubleProperty donGiaProperty() { return donGia; }
        public DoubleProperty thanhTienProperty() { return thanhTien; }
        public Object getOriginalObject() { return originalObject; }
    }


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
    private ComboBox<TienCong> cbTienCong; // Changed from ComboBox<String>
    @FXML
    private Button btnThemChiTiet;
    @FXML
    private TableView<ChiTietSuaChuaDisplay> tblChiTietSuaChua;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, Integer> colSTT;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, String> colNoiDung;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, String> colLoai;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, Integer> colSoLuong;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, Double> colDonGia;
    @FXML
    private TableColumn<ChiTietSuaChuaDisplay, Double> colThanhTienChiTiet;
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
    private TienCongDAO tienCongDAO; // Changed to TienCongDAO
    private PhieuSuaChuaDAO phieuSuaChuaDAO;
    private ChiTietPhieuSuaChua_VatTuDAO chiTietVatTuDAO; // New DAO
    private ChiTietPhieuSuaChua_TienCongDAO chiTietTienCongDAO; // New DAO
    private ThoDAO thoDAO; // New DAO for Tho
    private ChiTietPhieuNhapKhoVatTuDAO chiTietPhieuNhapKhoVatTuDAO; // New DAO for getting DonGiaNhap

    // Data lists for the repair slip
    private ObservableList<ChiTietPhieuSuaChua_VatTu> danhSachVatTu;
    private ObservableList<ChiTietPhieuSuaChua_TienCong> danhSachTienCong;
    private ObservableList<ChiTietSuaChuaDisplay> danhSachChiTietDisplay;


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
        tienCongDAO = new TienCongDAO(); // Changed to TienCongDAO
        phieuSuaChuaDAO = new PhieuSuaChuaDAO();
        chiTietVatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO(); // Initialize new DAO
        chiTietTienCongDAO = new ChiTietPhieuSuaChua_TienCongDAO(); // Initialize new DAO
        thoDAO = new ThoDAO(); // Initialize ThoDAO
        chiTietPhieuNhapKhoVatTuDAO = new ChiTietPhieuNhapKhoVatTuDAO(); // Initialize new DAO

        // Set default date for DatePicker
        dpNgaySuaChua.setValue(LocalDate.now());

        // Initialize ObservableLists for data
        danhSachVatTu = FXCollections.observableArrayList();
        danhSachTienCong = FXCollections.observableArrayList();
        danhSachChiTietDisplay = FXCollections.observableArrayList();
        tblChiTietSuaChua.setItems(danhSachChiTietDisplay);

        // Configure TableView columns for the display class
        colSTT.setCellValueFactory(cellData -> cellData.getValue().sttProperty().asObject());
        colNoiDung.setCellValueFactory(cellData -> cellData.getValue().noiDungProperty());
        colLoai.setCellValueFactory(cellData -> cellData.getValue().loaiProperty());
        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());
        colThanhTienChiTiet.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());

        // Add listener to update total when details change
        danhSachChiTietDisplay.addListener((javafx.collections.ListChangeListener.Change<? extends ChiTietSuaChuaDisplay> c) -> {
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
            ObservableList<TienCong> tienCongList = FXCollections.observableArrayList(tienCongDAO.getAllTienCong());
            cbTienCong.setItems(tienCongList);
            cbTienCong.setConverter(new javafx.util.StringConverter<TienCong>() {
                @Override
                public String toString(TienCong tienCong) {
                    return (tienCong != null) ? tienCong.getNoiDung() : "";
                }

                @Override
                public TienCong fromString(String string) {
                    // Not used for this ComboBox
                    return null;
                }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu tiền công", "Không thể tải dữ liệu tiền công: " + e.getMessage());
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
     * Displays a dialog for the user to select a TiepNhan record from a list.
     * @param records The list of TiepNhan records to display.
     */
    @SuppressWarnings("unchecked") // Suppress warning about generic array creation for varargs
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
            danhSachChiTietDisplay.clear(); // Clear previous details when new record is selected
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
        lblTenChuXe.setText("");
        lblHieuXe.setText("");
        lblDienThoai.setText("");
        lblDiaChi.setText("");
        lblNgayTiepNhan.setText("");
        lblTienNoHienTai.setText("");
    }

    /**
     * Handles the "Thêm chi tiết" button action.
     * Adds either a part (VatTu) or a labor service (TienCong) to the repair details list.
     */
    @FXML
    private void handleThemChiTiet() {
        String selectedVatTuName = cbVatTu.getValue();
        TienCong selectedTienCong = cbTienCong.getValue();
        String soLuongStr = txtSoLuongVatTu.getText().trim();

        if (selectedVatTuName == null && selectedTienCong == null) {
            AlertUtils.showWarningAlert("Chưa chọn mục", "Vui lòng chọn một vật tư hoặc một loại tiền công để thêm.");
            return;
        }

        if (selectedVatTuName != null && selectedTienCong != null) {
            AlertUtils.showWarningAlert("Chọn quá nhiều", "Vui lòng chỉ chọn vật tư hoặc tiền công, không chọn cả hai.");
            return;
        }

        try {
            if (selectedVatTuName != null) {
                // Handle adding VatTu
                if (soLuongStr.isEmpty() || !soLuongStr.matches("\\d+") || Integer.parseInt(soLuongStr) <= 0) {
                    AlertUtils.showWarningAlert("Số lượng không hợp lệ", "Vui lòng nhập số lượng là một số nguyên dương.");
                    return;
                }
                int soLuong = Integer.parseInt(soLuongStr);
                VatTu vatTu = vatTuDAO.getVatTuByName(selectedVatTuName);

                if (vatTu.getSoLuongTon() < soLuong) {
                    AlertUtils.showErrorAlert("Không đủ tồn kho", "Số lượng tồn kho của " + vatTu.getTenVatTu() + " không đủ (còn " + vatTu.getSoLuongTon() + ").");
                    return;
                }
                
                // For simplicity, using DonGiaBan. A better approach would be to get DonGiaNhap from PhieuNhapKho.
                double donGia = vatTu.getDonGiaBan(); 
                double thanhTien = soLuong * donGia;

                ChiTietPhieuSuaChua_VatTu chiTietVatTu = new ChiTietPhieuSuaChua_VatTu();
                chiTietVatTu.setMaVatTu(vatTu.getMaVatTu());
                chiTietVatTu.setTenVatTu(vatTu.getTenVatTu());
                chiTietVatTu.setSoLuong(soLuong);
                chiTietVatTu.setDonGiaNhap(donGia); // Using selling price as cost price for now
                chiTietVatTu.setThanhTien(thanhTien);
                
                danhSachVatTu.add(chiTietVatTu);
                updateDisplayList();


            } else { // Handle adding TienCong
                double donGia = selectedTienCong.getDonGia();
                double thanhTien = donGia; // Quantity is always 1 for labor

                ChiTietPhieuSuaChua_TienCong chiTietTienCong = new ChiTietPhieuSuaChua_TienCong();
                chiTietTienCong.setMaTienCong(selectedTienCong.getMaTienCong());
                chiTietTienCong.setNoiDungTienCong(selectedTienCong.getNoiDung());
                chiTietTienCong.setDonGia(donGia);
                chiTietTienCong.setThanhTien(thanhTien);

                danhSachTienCong.add(chiTietTienCong);
                updateDisplayList();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi truy xuất dữ liệu: " + e.getMessage());
        }

        // Clear inputs after adding
        cbVatTu.getSelectionModel().clearSelection();
        cbTienCong.getSelectionModel().clearSelection();
        txtSoLuongVatTu.clear();
    }


    /**
     * Refreshes the display TableView from the underlying data lists.
     */
    private void updateDisplayList() {
        danhSachChiTietDisplay.clear();
        int stt = 1;
        for (ChiTietPhieuSuaChua_VatTu vt : danhSachVatTu) {
            danhSachChiTietDisplay.add(new ChiTietSuaChuaDisplay(
                    stt++,
                    vt.getTenVatTu(),
                    "Vật tư",
                    vt.getSoLuong(),
                    vt.getDonGiaNhap(),
                    vt.getThanhTien(),
                    vt
            ));
        }
        for (ChiTietPhieuSuaChua_TienCong tc : danhSachTienCong) {
            danhSachChiTietDisplay.add(new ChiTietSuaChuaDisplay(
                    stt++,
                    tc.getNoiDungTienCong(),
                    "Tiền công",
                    1,
                    tc.getDonGia(),
                    tc.getThanhTien(),
                    tc
            ));
        }
    }


    /**
     * Handles the "Xóa chi tiết" button action.
     * Removes the selected item from the details list.
     */
    @FXML
    private void handleXoaChiTiet() {
        ChiTietSuaChuaDisplay selectedItem = tblChiTietSuaChua.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertUtils.showWarningAlert("Chưa chọn mục", "Vui lòng chọn một mục trong bảng để xóa.");
            return;
        }

        Object originalObject = selectedItem.getOriginalObject();
        if (originalObject instanceof ChiTietPhieuSuaChua_VatTu) {
            danhSachVatTu.remove((ChiTietPhieuSuaChua_VatTu) originalObject);
        } else if (originalObject instanceof ChiTietPhieuSuaChua_TienCong) {
            danhSachTienCong.remove((ChiTietPhieuSuaChua_TienCong) originalObject);
        }
        
        updateDisplayList();
    }

    /**
     * Calculates the total amount for the repair slip and displays it.
     */
    private void calculateAndDisplayTotal() {
        double total = 0;
        for (ChiTietSuaChuaDisplay item : danhSachChiTietDisplay) {
            total += item.thanhTien.get();
        }
        lblTongTienPhieuSC.setText(String.format("%,.0f VNĐ", total));
    }

    /**
     * Handles the "Lập phiếu sửa chữa" button action.
     * Saves the repair slip and its details to the database.
     */
    @FXML
    private void handleLapPhieuSuaChua() {
        // 1. Validate inputs
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Chưa chọn xe", "Vui lòng tìm và chọn một hồ sơ xe để lập phiếu.");
            return;
        }
        if (danhSachChiTietDisplay.isEmpty()) {
            AlertUtils.showWarningAlert("Phiếu trống", "Vui lòng thêm ít nhất một vật tư hoặc tiền công vào phiếu.");
            return;
        }
        LocalDate ngaySuaChua = dpNgaySuaChua.getValue();
        if (ngaySuaChua == null) {
            AlertUtils.showWarningAlert("Thiếu ngày sửa chữa", "Vui lòng chọn ngày sửa chữa.");
            return;
        }
        String tenTho = cbThoPhanCong.getValue();
        if (tenTho == null || tenTho.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thợ", "Vui lòng chọn một thợ để phân công.");
            return;
        }

        try {
            // 2. Get the assigned mechanic's ID
            Tho tho = thoDAO.getThoByName(tenTho);
            if (tho == null) {
                AlertUtils.showErrorAlert("Lỗi dữ liệu", "Không tìm thấy thợ được chọn trong cơ sở dữ liệu.");
                return;
            }

            // 3. Create and save the PhieuSuaChua
            PhieuSuaChua newPhieu = new PhieuSuaChua();
            newPhieu.setMaTiepNhan(selectedTiepNhan.getMaTiepNhan());
            newPhieu.setNgaySuaChua(ngaySuaChua);
            newPhieu.setGhiChu(txtGhiChu.getText());
            newPhieu.setMaTho(tho.getMaTho());
            newPhieu.setTongTien(Double.parseDouble(lblTongTienPhieuSC.getText().replaceAll("[^\\d]", "")));
            newPhieu.setTrangThaiHoanTat(false); // Default status

            int maPhieuSC = phieuSuaChuaDAO.addPhieuSuaChua(newPhieu);

            // 4. Save the details (VatTu and TienCong)
            for (ChiTietPhieuSuaChua_VatTu vtDetail : danhSachVatTu) {
                vtDetail.setMaPhieuSC(maPhieuSC);
                chiTietVatTuDAO.addChiTietVatTu(vtDetail);
                
                // Update stock quantity
                VatTu vatTuToUpdate = vatTuDAO.getVatTuById(vtDetail.getMaVatTu());
                int newQuantity = vatTuToUpdate.getSoLuongTon() - vtDetail.getSoLuong();
                vatTuDAO.updateSoLuongTon(vatTuToUpdate.getMaVatTu(), newQuantity);
            }

            for (ChiTietPhieuSuaChua_TienCong tcDetail : danhSachTienCong) {
                tcDetail.setMaPhieuSC(maPhieuSC);
                chiTietTienCongDAO.addChiTietTienCong(tcDetail);
            }
            
            // 5. Update the car's debt and status in TiepNhan table
            double themTienNo = newPhieu.getTongTien();
            tiepNhanDAO.updateTienNoAndTrangThaiXe(selectedTiepNhan.getMaTiepNhan(), themTienNo, "Đang sửa");


            AlertUtils.showInformationAlert("Thành công", "Lập phiếu sửa chữa thành công!");
            handleLamMoiPhieu(); // Clear the form for the next entry

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể lưu phiếu sửa chữa: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Lỗi khi đọc tổng tiền. Vui lòng kiểm tra lại.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears all inputs and selections.
     */
    @FXML
    private void handleLamMoiPhieu() {
        txtBienSoXeSearch.clear();
        clearVehicleInfoLabels();
        selectedTiepNhan = null;

        danhSachVatTu.clear();
        danhSachTienCong.clear();
        danhSachChiTietDisplay.clear();

        cbVatTu.getSelectionModel().clearSelection();
        txtSoLuongVatTu.clear();
        cbTienCong.getSelectionModel().clearSelection();
        tblChiTietSuaChua.getSelectionModel().clearSelection();
        lblTongTienPhieuSC.setText("0 VNĐ");
        dpNgaySuaChua.setValue(LocalDate.now());
        txtGhiChu.clear();
        cbThoPhanCong.getSelectionModel().clearSelection();
        
        setFormEnabled(false);
    }
}

