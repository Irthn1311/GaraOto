package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import model.VatTu;
import model.NhaCungCap;
import model.PhieuNhapKhoVatTu;
import model.ChiTietPhieuNhapKhoVatTu;
import dao.VatTuDAO;
import dao.NhaCungCapDAO;
import dao.PhieuNhapKhoVatTuDAO;
import dao.ChiTietPhieuNhapKhoVatTuDAO;
import dao.ChiTietPhieuSuaChua_VatTuDAO; // Import the correct DAO
import utils.AlertUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory; // For default cell factories
import java.util.Comparator; // Keep if actually used for sorting

public class QuanLyVatTuController {

    // --- Vật tư Management FXML elements ---
    @FXML private TextField txtMaVatTu;
    @FXML private TextField txtTenVatTu;
    @FXML private TextField txtDonViTinh;
    @FXML private TextField txtDonGiaBan;
    @FXML private TextField txtMucTonKhoToiThieu;
    @FXML private Button btnThemVatTu;
    @FXML private Button btnSuaVatTu;
    @FXML private Button btnXoaVatTu;
    @FXML private Button btnLamMoiVatTu;
    @FXML private TableView<VatTu> tblVatTu;
    @FXML private TableColumn<VatTu, Integer> colMaVatTu;
    @FXML private TableColumn<VatTu, String> colTenVatTu;
    @FXML private TableColumn<VatTu, String> colDonViTinh;
    @FXML private TableColumn<VatTu, Double> colDonGiaBan;
    @FXML private TableColumn<VatTu, Integer> colSoLuongTon;
    @FXML private TableColumn<VatTu, Integer> colMucTonKhoToiThieu;

    // --- Nhập kho Vật tư FXML elements ---
    @FXML private TextField txtMaPhieuNhap;
    @FXML private DatePicker dpNgayNhap;
    @FXML private ComboBox<NhaCungCap> cbNhaCungCap;
    @FXML private ComboBox<VatTu> cbVatTuNhap;
    @FXML private TextField txtSoLuongNhap;
    @FXML private TextField txtDonGiaNhap;
    @FXML private Button btnThemChiTietNhap;
    @FXML private TableView<ChiTietPhieuNhapKhoVatTu> tblChiTietPhieuNhap;
    @FXML private TableColumn<ChiTietPhieuNhapKhoVatTu, String> colCTPN_TenVatTu;
    @FXML private TableColumn<ChiTietPhieuNhapKhoVatTu, Integer> colCTPN_SoLuong;
    @FXML private TableColumn<ChiTietPhieuNhapKhoVatTu, Double> colCTPN_DonGiaNhap;
    @FXML private TableColumn<ChiTietPhieuNhapKhoVatTu, Double> colCTPN_ThanhTien;
    @FXML private Label lblTongTienNhap;
    @FXML private Button btnLapPhieuNhap;
    @FXML private Button btnLamMoiPhieuNhap;

    // --- DAOs ---
    private VatTuDAO vatTuDAO;
    private NhaCungCapDAO nhaCungCapDAO;
    private PhieuNhapKhoVatTuDAO phieuNhapKhoVatTuDAO;
    private ChiTietPhieuNhapKhoVatTuDAO chiTietPhieuNhapKhoVatTuDAO;
    private ChiTietPhieuSuaChua_VatTuDAO chiTietSuaChuaVatTuDAO; // Add the new DAO
    // private ThamSoDAO thamSoDAO; // Removed: Not directly used here

    // --- Observable Lists ---
    private ObservableList<VatTu> vatTuList;
    private ObservableList<NhaCungCap> nhaCungCapList;
    private ObservableList<VatTu> vatTuNhapComboBoxList; // For the ComboBox in import section
    private ObservableList<ChiTietPhieuNhapKhoVatTu> chiTietNhapList;

    private NumberFormat currencyFormat;

    @FXML
    public void initialize() {
        // Initialize DAOs
        vatTuDAO = new VatTuDAO();
        nhaCungCapDAO = new NhaCungCapDAO();
        phieuNhapKhoVatTuDAO = new PhieuNhapKhoVatTuDAO();
        chiTietPhieuNhapKhoVatTuDAO = new ChiTietPhieuNhapKhoVatTuDAO();
        chiTietSuaChuaVatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO(); // Initialize the new DAO

        // Initialize Observable Lists
        vatTuList = FXCollections.observableArrayList();
        nhaCungCapList = FXCollections.observableArrayList();
        vatTuNhapComboBoxList = FXCollections.observableArrayList();
        chiTietNhapList = FXCollections.observableArrayList();

        // Initialize currency formatter
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("vi").setRegion("VN").build());

        // --- Configure VatTu TableView ---
        colMaVatTu.setCellValueFactory(new PropertyValueFactory<>("maVatTu"));
        colTenVatTu.setCellValueFactory(new PropertyValueFactory<>("tenVatTu"));
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        colDonGiaBan.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        // Custom cell factory for currency formatting for DonGiaBan
        colDonGiaBan.setCellFactory(tc -> new TableCell<VatTu, Double>() {
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
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        colMucTonKhoToiThieu.setCellValueFactory(new PropertyValueFactory<>("mucTonKhoToiThieu"));

        // Custom cell factory for SoLuongTon to highlight low stock (Bước 3.3)
        colSoLuongTon.setCellFactory(tc -> new TableCell<VatTu, Integer>() {
            @Override
            protected void updateItem(Integer soLuongTon, boolean empty) {
                super.updateItem(soLuongTon, empty);
                if (empty || soLuongTon == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(soLuongTon));
                    VatTu vatTu = getTableView().getItems().get(getIndex());
                    if (vatTu != null && soLuongTon < vatTu.getMucTonKhoToiThieu()) {
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red; -fx-font-weight: bold;"); // Light red background for low stock
                    } else {
                        setStyle(""); // Reset style
                    }
                }
            }
        });

        // Listener for table selection to populate text fields for editing
        tblVatTu.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showVatTuDetails(newValue));

        // Listener for ComboBox to update DonViTinh when NhaCungCap changes (if applicable, remove if not)
        cbNhaCungCap.valueProperty().addListener((
                _observable, _oldValue, newValue) -> {
            // Implement logic if needed, otherwise remove this listener
        });

        // --- Configure NhapKho TableView ---
        colCTPN_TenVatTu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTenVatTu()));
        colCTPN_SoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuongNhap"));
        colCTPN_DonGiaNhap.setCellValueFactory(new PropertyValueFactory<>("donGiaNhap"));
        // Custom cell factory for currency formatting for DonGiaNhap
        colCTPN_DonGiaNhap.setCellFactory(tc -> new TableCell<ChiTietPhieuNhapKhoVatTu, Double>() {
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
        colCTPN_ThanhTien.setCellValueFactory(cellData -> {
            double soLuong = cellData.getValue().getSoLuongNhap();
            double donGia = cellData.getValue().getDonGiaNhap();
            return new SimpleDoubleProperty(soLuong * donGia).asObject();
        });
        colCTPN_ThanhTien.setCellFactory(tc -> new TableCell<ChiTietPhieuNhapKhoVatTu, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(total));
                }
            }
        });

        tblChiTietPhieuNhap.setItems(chiTietNhapList);

        // Set default date for DatePicker in import section
        dpNgayNhap.setValue(LocalDate.now());

        // Load initial data
        loadVatTuData();
        loadNhaCungCapData();
        loadVatTuNhapComboBox();
        clearPhieuNhapFields(); // Clear import fields on init
    }

    // --- VatTu Management Methods (Bước 3.1) ---

    private void loadVatTuData() {
        try {
            vatTuList.setAll(vatTuDAO.getAllVatTu());
            tblVatTu.setItems(vatTuList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showVatTuDetails(VatTu vatTu) {
        if (vatTu != null) {
            txtMaVatTu.setText(String.valueOf(vatTu.getMaVatTu()));
            txtTenVatTu.setText(vatTu.getTenVatTu());
            txtDonViTinh.setText(vatTu.getDonViTinh());
            txtDonGiaBan.setText(String.valueOf(vatTu.getDonGiaBan()));
            txtMucTonKhoToiThieu.setText(String.valueOf(vatTu.getMucTonKhoToiThieu()));
            // Disable editing of fields that should not be changed directly
            txtTenVatTu.setDisable(false);
            txtDonViTinh.setDisable(false);
            txtDonGiaBan.setDisable(false);
            txtMucTonKhoToiThieu.setDisable(false);
        } else {
            clearVatTuFields();
        }
    }

    private void clearVatTuFields() {
        txtMaVatTu.setText("");
        txtTenVatTu.setText("");
        txtDonViTinh.setText("");
        txtDonGiaBan.setText("");
        txtMucTonKhoToiThieu.setText("");
        tblVatTu.getSelectionModel().clearSelection();
        // Re-enable fields for adding new item
        txtTenVatTu.setDisable(false);
        txtDonViTinh.setDisable(false);
        txtDonGiaBan.setDisable(false);
        txtMucTonKhoToiThieu.setDisable(false);
    }

    @FXML
    private void handleThemVatTu() {
        String tenVatTu = txtTenVatTu.getText().trim();
        String donViTinh = txtDonViTinh.getText().trim();
        String donGiaBanText = txtDonGiaBan.getText().trim();
        String mucTonKhoToiThieuText = txtMucTonKhoToiThieu.getText().trim();

        if (tenVatTu.isEmpty() || donViTinh.isEmpty() || donGiaBanText.isEmpty() || mucTonKhoToiThieuText.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin vật tư.");
            return;
        }

        try {
            double donGiaBan = Double.parseDouble(donGiaBanText);
            int mucTonKhoToiThieu = Integer.parseInt(mucTonKhoToiThieuText);

            if (donGiaBan < 0 || mucTonKhoToiThieu < 0) {
                AlertUtils.showWarningAlert("Lỗi dữ liệu", "Đơn giá bán và mức tồn kho tối thiểu không được âm.");
                return;
            }

            // Check for duplicate name
            if (vatTuDAO.getVatTuByName(tenVatTu) != null) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên vật tư này đã tồn tại.");
                return;
            }

            VatTu newVatTu = new VatTu();
            newVatTu.setTenVatTu(tenVatTu);
            newVatTu.setDonViTinh(donViTinh);
            newVatTu.setDonGiaBan(donGiaBan);
            newVatTu.setSoLuongTon(0); // Initial stock is 0, must be updated via import
            newVatTu.setMucTonKhoToiThieu(mucTonKhoToiThieu);

            vatTuDAO.addVatTu(newVatTu);
            AlertUtils.showInformationAlert("Thành công", "Đã thêm vật tư mới thành công.");
            loadVatTuData(); // Refresh the table
            loadVatTuNhapComboBox(); // Refresh the combo box
            clearVatTuFields();

        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Đơn giá bán và mức tồn kho tối thiểu phải là số hợp lệ.");
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi thêm vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSuaVatTu() {
        VatTu selectedVatTu = tblVatTu.getSelectionModel().getSelectedItem();
        if (selectedVatTu == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một vật tư để sửa.");
            return;
        }

        String tenVatTuMoi = txtTenVatTu.getText().trim();
        String donViTinhMoi = txtDonViTinh.getText().trim();
        String donGiaBanTextMoi = txtDonGiaBan.getText().trim();
        String mucTonKhoToiThieuTextMoi = txtMucTonKhoToiThieu.getText().trim();

        if (tenVatTuMoi.isEmpty() || donViTinhMoi.isEmpty() || donGiaBanTextMoi.isEmpty() || mucTonKhoToiThieuTextMoi.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ thông tin vật tư.");
            return;
        }

        try {
            double donGiaBanMoi = Double.parseDouble(donGiaBanTextMoi);
            int mucTonKhoToiThieuMoi = Integer.parseInt(mucTonKhoToiThieuTextMoi);

            if (donGiaBanMoi < 0 || mucTonKhoToiThieuMoi < 0) {
                AlertUtils.showWarningAlert("Lỗi dữ liệu", "Đơn giá bán và mức tồn kho tối thiểu không được âm.");
                return;
            }

            // Check for duplicate name, but allow update if it's the same item
            VatTu existingVatTu = vatTuDAO.getVatTuByName(tenVatTuMoi);
            if (existingVatTu != null && existingVatTu.getMaVatTu() != selectedVatTu.getMaVatTu()) {
                AlertUtils.showErrorAlert("Lỗi trùng lặp", "Tên vật tư này đã tồn tại cho vật tư khác.");
                return;
            }

            selectedVatTu.setTenVatTu(tenVatTuMoi);
            selectedVatTu.setDonViTinh(donViTinhMoi);
            selectedVatTu.setDonGiaBan(donGiaBanMoi);
            selectedVatTu.setMucTonKhoToiThieu(mucTonKhoToiThieuMoi);
            vatTuDAO.updateVatTu(selectedVatTu);
            AlertUtils.showInformationAlert("Thành công", "Cập nhật vật tư thành công!");
            loadVatTuData();
            loadVatTuNhapComboBox(); // Refresh vật tư list for import
            clearVatTuFields();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Đơn giá bán và mức tồn kho tối thiểu phải là số hợp lệ.");
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi cập nhật vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleXoaVatTu() {
        VatTu selectedVatTu = tblVatTu.getSelectionModel().getSelectedItem();
        if (selectedVatTu == null) {
            AlertUtils.showWarningAlert("Chưa chọn vật tư", "Vui lòng chọn một vật tư để xóa.");
            return;
        }

        try {
            // Check if the part is used in any repair slips
            boolean isInPhieuSuaChua = chiTietSuaChuaVatTuDAO.isVatTuUsedInAnyPhieuSuaChua(selectedVatTu.getMaVatTu());
            if (isInPhieuSuaChua) {
                AlertUtils.showErrorAlert("Không thể xóa", "Vật tư này đã được sử dụng trong các phiếu sửa chữa và không thể xóa.");
                return;
            }

            // Optional: Check if the part exists in any import slips.
            boolean isInPhieuNhap = chiTietPhieuNhapKhoVatTuDAO.isVatTuUsedInAnyPhieuNhap(selectedVatTu.getMaVatTu());
            if (isInPhieuNhap) {
                AlertUtils.showErrorAlert("Không thể xóa", "Vật tư này đã tồn tại trong các phiếu nhập kho và không thể xóa.");
                return;
            }

            if (AlertUtils.showConfirmationAlert("Xác nhận xóa", "Bạn có chắc chắn muốn xóa vật tư '" + selectedVatTu.getTenVatTu() + "'?")) {
                vatTuDAO.deleteVatTu(selectedVatTu.getMaVatTu());
                AlertUtils.showInformationAlert("Thành công", "Đã xóa vật tư thành công.");
                loadVatTuData();
                clearVatTuFields();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể xóa vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLamMoiVatTu() {
        clearVatTuFields();
        loadVatTuData();
        AlertUtils.showInformationAlert("Làm mới", "Dữ liệu vật tư đã được làm mới.");
    }

    // --- Nhập kho Vật tư Methods (Bước 3.2) ---

    private void loadNhaCungCapData() {
        try {
            nhaCungCapList.setAll(nhaCungCapDAO.getAllNhaCungCap());
            cbNhaCungCap.setItems(nhaCungCapList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách nhà cung cấp: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadVatTuNhapComboBox() {
        try {
            vatTuNhapComboBoxList.setAll(vatTuDAO.getAllVatTu());
            cbVatTuNhap.setItems(vatTuNhapComboBoxList);
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách vật tư cho nhập kho: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleThemChiTietNhap() {
        VatTu selectedVatTu = cbVatTuNhap.getSelectionModel().getSelectedItem();
        String soLuongText = txtSoLuongNhap.getText().trim();
        String donGiaNhapText = txtDonGiaNhap.getText().trim();

        if (selectedVatTu == null || soLuongText.isEmpty() || donGiaNhapText.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn vật tư, nhập số lượng và đơn giá nhập.");
            return;
        }

        try {
            int soLuong = Integer.parseInt(soLuongText);
            double donGiaNhap = Double.parseDouble(donGiaNhapText);

            if (soLuong <= 0 || donGiaNhap < 0) {
                AlertUtils.showWarningAlert("Lỗi dữ liệu", "Số lượng phải lớn hơn 0 và đơn giá nhập không được âm.");
                return;
            }

            // Check if item already in current import list
            Optional<ChiTietPhieuNhapKhoVatTu> existingDetail = chiTietNhapList.stream()
                    .filter(ct -> ct.getMaVatTu() == selectedVatTu.getMaVatTu())
                    .findFirst();

            if (existingDetail.isPresent()) {
                AlertUtils.showWarningAlert("Trùng lặp", "Vật tư này đã có trong danh sách nhập kho. Vui lòng sửa hoặc xóa mục đã có.");
                return;
            }

            ChiTietPhieuNhapKhoVatTu chiTiet = new ChiTietPhieuNhapKhoVatTu();
            chiTiet.setMaVatTu(selectedVatTu.getMaVatTu());
            chiTiet.setSoLuongNhap(soLuong);
            chiTiet.setDonGiaNhap(donGiaNhap);
            chiTiet.setTenVatTu(selectedVatTu.getTenVatTu()); // Set derived property for display

            chiTietNhapList.add(chiTiet);
            updateTongTienNhap();
            clearChiTietNhapFields();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Số lượng và đơn giá nhập phải là số hợp lệ.");
        }
    }

    private void updateTongTienNhap() {
        double total = chiTietNhapList.stream()
                .mapToDouble(ct -> ct.getSoLuongNhap() * ct.getDonGiaNhap())
                .sum();
        lblTongTienNhap.setText(currencyFormat.format(total));
    }

    private void clearChiTietNhapFields() {
        cbVatTuNhap.getSelectionModel().clearSelection();
        txtSoLuongNhap.setText("");
        txtDonGiaNhap.setText("");
    }

    private void clearPhieuNhapFields() {
        txtMaPhieuNhap.setText("");
        dpNgayNhap.setValue(LocalDate.now());
        cbNhaCungCap.getSelectionModel().clearSelection();
        chiTietNhapList.clear();
        updateTongTienNhap();
    }

    @FXML
    private void handleLapPhieuNhap() {
        LocalDate ngayNhap = dpNgayNhap.getValue();
        NhaCungCap selectedNhaCungCap = cbNhaCungCap.getSelectionModel().getSelectedItem();

        if (ngayNhap == null || selectedNhaCungCap == null || chiTietNhapList.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn ngày nhập, nhà cung cấp và thêm ít nhất một vật tư vào phiếu nhập.");
            return;
        }

        if (AlertUtils.showConfirmationAlert("Xác nhận lập phiếu nhập", "Bạn có chắc chắn muốn lập phiếu nhập kho này không?")) {
            try {
                double tongTienNhap = chiTietNhapList.stream()
                        .mapToDouble(ct -> ct.getSoLuongNhap() * ct.getDonGiaNhap())
                        .sum();

                PhieuNhapKhoVatTu phieuNhap = new PhieuNhapKhoVatTu();
                phieuNhap.setNgayNhap(ngayNhap);
                phieuNhap.setMaNhaCungCap(selectedNhaCungCap.getMaNhaCungCap());
                phieuNhap.setTongTienNhap(tongTienNhap);

                int maPhieuNhap = phieuNhapKhoVatTuDAO.addPhieuNhapKhoVatTu(phieuNhap);

                if (maPhieuNhap != -1) {
                    for (ChiTietPhieuNhapKhoVatTu chiTiet : chiTietNhapList) {
                        chiTiet.setMaPhieuNhap(maPhieuNhap);
                        chiTietPhieuNhapKhoVatTuDAO.addChiTietPhieuNhap(chiTiet);

                        // Update SoLuongTon in VatTu table (Logic for 3.2)
                        VatTu vatTu = vatTuDAO.getVatTuById(chiTiet.getMaVatTu());
                        if (vatTu != null) {
                            vatTu.setSoLuongTon(vatTu.getSoLuongTon() + chiTiet.getSoLuongNhap());
                            vatTuDAO.updateVatTu(vatTu); // Update only stock, other fields remain
                        }
                    }
                    AlertUtils.showInformationAlert("Thành công", "Lập phiếu nhập kho thành công!");
                    loadVatTuData(); // Refresh vat tu table to reflect new stock
                    loadVatTuNhapComboBox(); // Refresh combobox as well
                    clearPhieuNhapFields();
                } else {
                    AlertUtils.showErrorAlert("Lỗi", "Không thể lập phiếu nhập kho.");
                }
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi lập phiếu nhập kho: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLamMoiPhieuNhap() {
        clearPhieuNhapFields();
        AlertUtils.showInformationAlert("Làm mới", "Phiếu nhập kho đã được làm mới.");
    }

    // --- Bước 3.3: Cảnh báo tồn kho thấp ---
    // Logic được tích hợp trong colSoLuongTon.setCellFactory để tô màu hàng.
    // Mức tồn kho tối thiểu được lấy từ chính đối tượng VatTu.
    // Nếu bạn muốn một cảnh báo chủ động (ví dụ: khi khởi động ứng dụng),
    // bạn có thể thêm một phương thức để kiểm tra tất cả các vật tư tồn kho thấp
    // và hiển thị một cảnh báo tổng hợp. Hiện tại, dấu hiệu trực quan trên bảng là đủ.
}
