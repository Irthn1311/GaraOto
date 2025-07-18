package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dao.HieuXeDAO;
import dao.XeDAO;
import dao.ChuXeDAO; // Import ChuXeDAO
import dao.TiepNhanDAO; // Import TiepNhanDAO (formerly HoSoSuaChuaDAO)
import dao.ThamSoDAO; // Import ThamSoDAO
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import model.HieuXe;
import model.Xe;
import model.ChuXe; // Import ChuXe entity
import model.TiepNhan; // Import TiepNhan entity (formerly HoSoSuaChua)
import utils.AlertUtils; // Utility class for showing alerts
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.function.Predicate;

public class TiepNhanController {

    // FXML elements from TiepNhanView.fxml
    @FXML
    private TextField txtTenChuXe;
    @FXML
    private TextField txtBienSoXe;
    @FXML
    private TextField txtDienThoai;
    @FXML
    private TextField txtDiaChi;
    @FXML
    private ComboBox<String> cbHieuXe; // ComboBox for car brands
    @FXML
    private DatePicker dpNgayTiepNhan;
    @FXML
    private TextField txtTinhTrangXe; // New field for car condition
    @FXML
    private Label lblSoXeTrongNgay;
    @FXML
    private TableView<TiepNhan> tblXeTiepNhanTrongNgay; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colBienSo; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colTenChuXe; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colHieuXe; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colDienThoai; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colDiaChi; // Changed to TiepNhan
    @FXML
    private TableColumn<TiepNhan, String> colTinhTrangXe; // New column for car condition
    @FXML
    private TableColumn<TiepNhan, String> colTrangThai; // New column for status
    @FXML
    private Button btnSuaXe; // Button for editing selected record
    @FXML
    private Button btnXoaXe;  // Button for deleting selected record
    @FXML
    private Button btnTienTrangThai; // Button for advancing status
    @FXML
    private ComboBox<String> cbTieuChiTimKiem;
    @FXML
    private TextField txtTimKiem;

    // Store currently selected record
    private TiepNhan selectedTiepNhan;

    // Data Access Objects
    private HieuXeDAO hieuXeDAO;
    private XeDAO xeDAO;
    private ChuXeDAO chuXeDAO; // New DAO for ChuXe
    private TiepNhanDAO tiepNhanDAO; // Renamed from HoSoSuaChuaDAO
    private ThamSoDAO thamSoDAO; // New DAO for ThamSo

    // ObservableList for the TableView
    private ObservableList<TiepNhan> danhSachXeTiepNhanTrongNgay; // Changed to TiepNhan
    private FilteredList<TiepNhan> filteredData;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize DAOs
        hieuXeDAO = new HieuXeDAO();
        xeDAO = new XeDAO();
        chuXeDAO = new ChuXeDAO(); // Initialize new DAO
        tiepNhanDAO = new TiepNhanDAO(); // Initialize renamed DAO
        thamSoDAO = new ThamSoDAO(); // Initialize new DAO

        // Set default date for DatePicker to today
        dpNgayTiepNhan.setValue(LocalDate.now());

        // Initialize ObservableList and FilteredList
        danhSachXeTiepNhanTrongNgay = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(danhSachXeTiepNhanTrongNgay, p -> true);

        // Setup search
        cbTieuChiTimKiem.setItems(FXCollections.observableArrayList("Biển số xe", "Tên chủ xe", "Điện thoại", "Địa chỉ"));
        cbTieuChiTimKiem.setValue("Biển số xe"); // Default search criteria

        txtTimKiem.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(createPredicate(newValue))
        );
        cbTieuChiTimKiem.valueProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(createPredicate(txtTimKiem.getText()))
        );

        // Wrap the FilteredList in a SortedList.
        SortedList<TiepNhan> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tblXeTiepNhanTrongNgay.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        tblXeTiepNhanTrongNgay.setItems(sortedData);

        // Listen for row selection to enable editing/deleting
        tblXeTiepNhanTrongNgay.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedTiepNhan = newSel;
            if (newSel != null) {
                populateForm(newSel);
            }
        });

        // Configure TableView columns
        // These PropertyValueFactory mappings now correctly use TiepNhan properties
        colBienSo.setCellValueFactory(cellData -> cellData.getValue().bienSoProperty());
        colTenChuXe.setCellValueFactory(cellData -> cellData.getValue().tenChuXeProperty());
        colHieuXe.setCellValueFactory(cellData -> cellData.getValue().tenHieuXeProperty());
        colDienThoai.setCellValueFactory(cellData -> cellData.getValue().dienThoaiChuXeProperty());
        colDiaChi.setCellValueFactory(cellData -> cellData.getValue().diaChiChuXeProperty());
        colTinhTrangXe.setCellValueFactory(cellData -> cellData.getValue().tinhTrangXeProperty());
        colTrangThai.setCellValueFactory(cellData -> cellData.getValue().trangThaiProperty());

        // Load initial data
        loadHieuXeData();
        loadXeTiepNhanTrongNgay(); // Corrected method name
    }

    private Predicate<TiepNhan> createPredicate(String filterText) {
        return tiepNhan -> {
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = filterText.toLowerCase();
            String searchCriteria = cbTieuChiTimKiem.getValue();

            if (searchCriteria == null) {
                return true;
            }

            if ("Biển số xe".equals(searchCriteria)) {
                return tiepNhan.getBienSo().toLowerCase().contains(lowerCaseFilter);
            } else if ("Tên chủ xe".equals(searchCriteria)) {
                return tiepNhan.getTenChuXe().toLowerCase().contains(lowerCaseFilter);
            } else if ("Điện thoại".equals(searchCriteria)) {
                return tiepNhan.getDienThoaiChuXe().toLowerCase().contains(lowerCaseFilter);
            } else if ("Địa chỉ".equals(searchCriteria)) {
                return tiepNhan.getDiaChiChuXe().toLowerCase().contains(lowerCaseFilter);
            }

            return false; // No match
        };
    }

    /**
     * Loads car brands (HieuXe) from the database into the ComboBox.
     */
    private void loadHieuXeData() {
        try {
            ObservableList<String> hieuXeNames = FXCollections.observableArrayList();
            for (HieuXe hieuXe : hieuXeDAO.getAllHieuXe()) {
                hieuXeNames.add(hieuXe.getTenHieuXe());
            }
            cbHieuXe.setItems(hieuXeNames);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách hiệu xe từ cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    /**
     * Loads cars accepted for the current day into the TableView and updates the count label.
     */
    private void loadXeTiepNhanTrongNgay() { // Corrected method name
        try {
            LocalDate ngayHienTai = dpNgayTiepNhan.getValue(); // Use the selected date in DatePicker
            if (ngayHienTai == null) {
                ngayHienTai = LocalDate.now(); // Fallback to current date if not set
                dpNgayTiepNhan.setValue(ngayHienTai);
            }
            ObservableList<TiepNhan> xeTrongNgay = tiepNhanDAO.getTiepNhanByNgayTiepNhan(ngayHienTai); // Changed to tiepNhanDAO and TiepNhan
            danhSachXeTiepNhanTrongNgay.clear();
            danhSachXeTiepNhanTrongNgay.addAll(xeTrongNgay);

            // Update the count label
            try {
                // Use the correct parameter name from your updated SQL script
                int maxXeTrongNgay = thamSoDAO.getThamSoByName("SoXeToiDaMoiNgay").getGiaTri();
                lblSoXeTrongNgay.setText(danhSachXeTiepNhanTrongNgay.size() + "/" + maxXeTrongNgay);
                if (danhSachXeTiepNhanTrongNgay.size() >= maxXeTrongNgay) {
                    lblSoXeTrongNgay.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    lblSoXeTrongNgay.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;"); // Default color
                }
            } catch (NumberFormatException e) {
                lblSoXeTrongNgay.setText(danhSachXeTiepNhanTrongNgay.size() + "/Giới hạn không xác định");
                AlertUtils.showWarningAlert("Cảnh báo", "Không thể đọc giới hạn số xe tối đa trong ngày từ cấu hình.");
            }

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách xe tiếp nhận trong ngày.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Tiếp nhận xe" button action.
     * This method will validate inputs, save car info, and create a new repair record.
     */
    @FXML
    private void handleTiepNhanXe() {
        // 1. Get data from input fields
        String tenChuXe = txtTenChuXe.getText().trim();
        String bienSoXe = txtBienSoXe.getText().trim();
        String dienThoai = txtDienThoai.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String tenHieuXe = cbHieuXe.getValue();
        LocalDate ngayTiepNhan = dpNgayTiepNhan.getValue();
        String tinhTrangXe = txtTinhTrangXe.getText().trim();

        // 2. Validate inputs
        if (tenChuXe.isEmpty() || bienSoXe.isEmpty() || tenHieuXe == null || ngayTiepNhan == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Tên chủ xe, Biển số xe, Hiệu xe và Ngày tiếp nhận.");
            return;
        }

        // Basic phone number validation (optional, can be more robust)
        if (!dienThoai.isEmpty() && !dienThoai.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }

        try {
            // Check if the car is already being repaired
            ObservableList<TiepNhan> existingRecords = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            for (TiepNhan record : existingRecords) {
                if (!record.isTrangThaiHoanTat()) {
                    AlertUtils.showErrorAlert("Xe đang sửa chữa", "Xe này đã được tiếp nhận và đang trong quá trình sửa chữa.");
                    return;
                }
            }

            // Check daily acceptance limit (QĐ1)
            int maxXeTrongNgay = thamSoDAO.getThamSoByName("SoXeToiDaMoiNgay").getGiaTri(); // Use new param name
            if (danhSachXeTiepNhanTrongNgay.size() >= maxXeTrongNgay) {
                AlertUtils.showErrorAlert("Vượt quá giới hạn", "Số lượng xe tiếp nhận trong ngày đã đạt giới hạn tối đa (" + maxXeTrongNgay + " xe).");
                return;
            }

            // Handle ChuXe (Owner)
            ChuXe chuXe = chuXeDAO.getChuXeByDienThoai(dienThoai);
            int maChuXe;
            if (chuXe == null) {
                // Create new ChuXe if not exists
                chuXe = new ChuXe();
                chuXe.setTenChuXe(tenChuXe);
                chuXe.setDienThoai(dienThoai);
                chuXe.setDiaChi(diaChi);
                // Email is optional, can be set if available in future
                maChuXe = chuXeDAO.addChuXe(chuXe); // addChuXe returns int for generated ID
            } else {
                // Use existing ChuXe's ID, ask before updating info
                maChuXe = chuXe.getMaChuXe();
                if (!chuXe.getTenChuXe().equals(tenChuXe) || !chuXe.getDiaChi().equals(diaChi) || !chuXe.getDienThoai().equals(dienThoai)) {
                    boolean confirmUpdate = AlertUtils.showConfirmationAlert("Xác nhận cập nhật",
                            "Thông tin chủ xe đã tồn tại nhưng có một vài điểm khác biệt. Bạn có muốn cập nhật không?");
                    if (confirmUpdate) {
                        chuXe.setTenChuXe(tenChuXe);
                        chuXe.setDiaChi(diaChi);
                        chuXe.setDienThoai(dienThoai);
                        chuXeDAO.updateChuXe(chuXe);
                    }
                }
            }

            // Get MaHieuXe
            HieuXe hieuXe = hieuXeDAO.getHieuXeByName(tenHieuXe);
            if (hieuXe == null) {
                AlertUtils.showErrorAlert("Lỗi", "Hiệu xe không tồn tại trong hệ thống.");
                return;
            }
            int maHieuXe = hieuXe.getMaHieuXe();

            // Handle Xe (Car)
            Xe xe = xeDAO.getXeByBienSo(bienSoXe);
            if (xe == null) {
                // Create new Xe if not exists
                xe = new Xe();
                xe.setBienSo(bienSoXe);
                xe.setMaHieuXe(maHieuXe);
                xe.setMaChuXe(maChuXe);
                xeDAO.addXe(xe); // addXe now returns void, just executes
            } else {
                // Update existing Xe's MaHieuXe and MaChuXe if necessary
                // This assumes BienSo is immutable (PK)
                if (xe.getMaHieuXe() != maHieuXe || xe.getMaChuXe() != maChuXe) {
                    xe.setMaHieuXe(maHieuXe);
                    xe.setMaChuXe(maChuXe);
                    xeDAO.updateXe(xe);
                }
            }

            // Create new TiepNhan (acceptance record)
            TiepNhan newTiepNhan = new TiepNhan();
            newTiepNhan.setBienSo(bienSoXe);
            newTiepNhan.setNgayTiepNhan(ngayTiepNhan);
            newTiepNhan.setTongTienNo(0.0); // Initially no debt
            newTiepNhan.setTrangThaiHoanTat(false); // Not completed yet
            newTiepNhan.setTinhTrangXe(tinhTrangXe); // Set car condition
            newTiepNhan.setTrangThai("Chờ sửa"); // Set initial status

            tiepNhanDAO.addTiepNhan(newTiepNhan); // Changed to tiepNhanDAO

            AlertUtils.showInformationAlert("Thành công", "Tiếp nhận xe và tạo hồ sơ tiếp nhận thành công!");

            // Refresh the table and clear inputs
            loadXeTiepNhanTrongNgay();
            handleLamMoi();

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Đã xảy ra lỗi khi lưu thông tin: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi cấu hình", "Giá trị giới hạn xe trong ngày không hợp lệ. Vui lòng kiểm tra cấu hình hệ thống.");
            e.printStackTrace();
        }
    }

    private void populateForm(TiepNhan record) {
        txtTenChuXe.setText(record.getTenChuXe());
        txtBienSoXe.setText(record.getBienSo());
        txtDienThoai.setText(record.getDienThoaiChuXe());
        txtDiaChi.setText(record.getDiaChiChuXe());
        cbHieuXe.setValue(record.getTenHieuXe());
        dpNgayTiepNhan.setValue(record.getNgayTiepNhan());
        txtTinhTrangXe.setText(record.getTinhTrangXe());
    }

    @FXML
    private void handleSuaXe() {
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn xe cần sửa trong bảng.");
            return;
        }
        // Reuse validation from handleTiepNhanXe
        String tenChuXe = txtTenChuXe.getText().trim();
        String bienSoXe = txtBienSoXe.getText().trim();
        String dienThoai = txtDienThoai.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String tenHieuXe = cbHieuXe.getValue();
        String tinhTrangXe = txtTinhTrangXe.getText().trim();

        if (tenChuXe.isEmpty() || bienSoXe.isEmpty() || tenHieuXe == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ Tên chủ xe, Biển số xe và Hiệu xe.");
            return;
        }
        if (!dienThoai.isEmpty() && !dienThoai.matches("\\d{10,15}")) {
            AlertUtils.showWarningAlert("Lỗi định dạng", "Số điện thoại không hợp lệ. Vui lòng nhập từ 10 đến 15 chữ số.");
            return;
        }
        try {
            // Update owner
            ChuXe chuXe = chuXeDAO.getChuXeByDienThoai(dienThoai);
            int maChuXe;
            if (chuXe == null) {
                chuXe = new ChuXe();
                chuXe.setTenChuXe(tenChuXe);
                chuXe.setDienThoai(dienThoai);
                chuXe.setDiaChi(diaChi);
                maChuXe = chuXeDAO.addChuXe(chuXe);
            } else {
                maChuXe = chuXe.getMaChuXe();
                chuXe.setTenChuXe(tenChuXe);
                chuXe.setDiaChi(diaChi);
                chuXeDAO.updateChuXe(chuXe);
            }

            // Update vehicle info
            HieuXe hieuXe = hieuXeDAO.getHieuXeByName(tenHieuXe);
            if (hieuXe == null) {
                AlertUtils.showErrorAlert("Lỗi", "Hiệu xe không tồn tại trong hệ thống.");
                return;
            }
            Xe xe = xeDAO.getXeByBienSo(bienSoXe);
            if (xe != null) {
                xe.setMaHieuXe(hieuXe.getMaHieuXe());
                xe.setMaChuXe(maChuXe);
                xeDAO.updateXe(xe);
            }

            // Update acceptance record condition
            tiepNhanDAO.updateTinhTrangXe(selectedTiepNhan.getMaTiepNhan(), tinhTrangXe);

            AlertUtils.showInformationAlert("Thành công", "Cập nhật thông tin xe thành công.");
            loadXeTiepNhanTrongNgay();
            handleLamMoi();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleXoaXe() {
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn xe cần xóa trong bảng.");
            return;
        }
        if (!AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa hồ sơ tiếp nhận xe này?")) {
            return;
        }
        try {
            tiepNhanDAO.deleteTiepNhan(selectedTiepNhan.getMaTiepNhan());
            AlertUtils.showInformationAlert("Đã xóa", "Xóa hồ sơ tiếp nhận thành công.");
            loadXeTiepNhanTrongNgay();
            handleLamMoi();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTienTrangThai() {
        if (selectedTiepNhan == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn xe trong bảng để thay đổi trạng thái.");
            return;
        }
        String current = selectedTiepNhan.getTrangThai();
        String next;
        switch (current) {
            case "Chờ sửa":
                next = "Đang sửa";
                break;
            case "Đang sửa":
                next = "Đã xong";
                break;
            default:
                AlertUtils.showInformationAlert("Trạng thái cuối", "Xe đã hoàn tất sửa chữa.");
                return;
        }
        if (!AlertUtils.showConfirmationAlert("Xác nhận", "Chuyển trạng thái xe từ '" + current + "' sang '" + next + "'?")) {
            return;
        }
        try {
            tiepNhanDAO.updateTrangThai(selectedTiepNhan.getMaTiepNhan(), next);
            AlertUtils.showSuccessAlert("Thành công", "Cập nhật trạng thái thành công.");
            loadXeTiepNhanTrongNgay();
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Clears all input fields.
     */
    @FXML
    private void handleLamMoi() {
        txtTenChuXe.clear();
        txtBienSoXe.clear();
        txtDienThoai.clear();
        txtDiaChi.clear();
        cbHieuXe.getSelectionModel().clearSelection();
        dpNgayTiepNhan.setValue(LocalDate.now());
        txtTinhTrangXe.clear();
        tblXeTiepNhanTrongNgay.getSelectionModel().clearSelection();
        selectedTiepNhan = null;
        // Clear search
        txtTimKiem.clear();
        cbTieuChiTimKiem.setValue("Biển số xe");
        filteredData.setPredicate(null); // Reset filter
    }
}
