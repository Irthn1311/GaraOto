package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.*;
import javafx.scene.control.cell.PropertyValueFactory;

import model.*;
import dao.*;
import utils.AlertUtils;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class SuaChuaController {

    public static class ChiTietSuaChuaDisplay {
        private final IntegerProperty stt;
        private final StringProperty noiDung;
        private final StringProperty vatTu;
        private final IntegerProperty soLuong;
        private final DoubleProperty donGia;
        private final DoubleProperty tienCong;
        private final DoubleProperty thanhTien;
        private final VatTu originalVatTu;
        private final TienCong originalTienCong;

        public ChiTietSuaChuaDisplay(int stt, String noiDung, VatTu vatTu, int soLuong, TienCong tienCong) {
            this.stt = new SimpleIntegerProperty(stt);
            this.noiDung = new SimpleStringProperty(noiDung);
            this.vatTu = new SimpleStringProperty(vatTu != null ? vatTu.getTenVatTu() : "");
            this.soLuong = new SimpleIntegerProperty(soLuong);
            this.donGia = new SimpleDoubleProperty(vatTu != null ? vatTu.getDonGiaBan() : 0);
            this.tienCong = new SimpleDoubleProperty(tienCong != null ? tienCong.getDonGia() : 0);
            double donGiaValue = (vatTu != null) ? vatTu.getDonGiaBan() : 0;
            double tienCongValue = (tienCong != null) ? tienCong.getDonGia() : 0;
            this.thanhTien = new SimpleDoubleProperty((soLuong * donGiaValue) + tienCongValue);
            this.originalVatTu = vatTu;
            this.originalTienCong = tienCong;
        }

        public IntegerProperty sttProperty() { return stt; }
        public StringProperty noiDungProperty() { return noiDung; }
        public StringProperty vatTuProperty() { return vatTu; }
        public IntegerProperty soLuongProperty() { return soLuong; }
        public DoubleProperty donGiaProperty() { return donGia; }
        public DoubleProperty tienCongProperty() { return tienCong; }
        public DoubleProperty thanhTienProperty() { return thanhTien; }
        public VatTu getOriginalVatTu() { return originalVatTu; }
        public TienCong getOriginalTienCong() { return originalTienCong; }
    }

    @FXML private ComboBox<String> cbBienSoXeSearch;
    @FXML private Button btnTimKiemXe;
    @FXML private Button btnChonHoSo;
    @FXML private Label lblTenChuXe;
    @FXML private Label lblHieuXe;
    @FXML private Label lblDienThoai;
    @FXML private Label lblDiaChi;
    @FXML private Label lblNgayTiepNhan;
    @FXML private Label lblTienNoHienTai;
    @FXML private TextField txtNoiDungSuaChua;
    @FXML private ComboBox<VatTu> cbVatTu;
    @FXML private TextField txtSoLuongVatTu;
    @FXML private ComboBox<TienCong> cbTienCong;
    @FXML private Button btnThemChiTiet;
    @FXML private TableView<ChiTietSuaChuaDisplay> tblChiTietSuaChua;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, Integer> colSTT;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, String> colNoiDung;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, String> colVatTu;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, Integer> colSoLuong;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, Double> colDonGia;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, Double> colTienCong;
    @FXML private TableColumn<ChiTietSuaChuaDisplay, Double> colThanhTienChiTiet;
    @FXML private Button btnXoaChiTiet;
    @FXML private Label lblTongTienPhieuSC;
    @FXML private DatePicker dpNgaySuaChua;
    @FXML private TextArea txtGhiChu;
    @FXML private ComboBox<Tho> cbThoPhanCong;
    @FXML private Button btnLapPhieuSuaChua;
    @FXML private Button btnLamMoiPhieu;

    private TiepNhanDAO tiepNhanDAO;
    private VatTuDAO vatTuDAO;
    private TienCongDAO tienCongDAO;
    private PhieuSuaChuaDAO phieuSuaChuaDAO;
    private ChiTietPhieuSuaChua_VatTuDAO chiTietVatTuDAO;
    private ChiTietPhieuSuaChua_TienCongDAO chiTietTienCongDAO;
    private ThoDAO thoDAO;

    private ObservableList<ChiTietSuaChuaDisplay> danhSachChiTietDisplay;
    private TiepNhan selectedTiepNhan;

    @FXML
    public void initialize() {
        tiepNhanDAO = new TiepNhanDAO();
        vatTuDAO = new VatTuDAO();
        tienCongDAO = new TienCongDAO();
        phieuSuaChuaDAO = new PhieuSuaChuaDAO();
        chiTietVatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO();
        chiTietTienCongDAO = new ChiTietPhieuSuaChua_TienCongDAO();
        thoDAO = new ThoDAO();

        dpNgaySuaChua.setValue(LocalDate.now());
        danhSachChiTietDisplay = FXCollections.observableArrayList();
        tblChiTietSuaChua.setItems(danhSachChiTietDisplay);

        colSTT.setCellValueFactory(cellData -> cellData.getValue().sttProperty().asObject());
        colNoiDung.setCellValueFactory(cellData -> cellData.getValue().noiDungProperty());
        colVatTu.setCellValueFactory(cellData -> cellData.getValue().vatTuProperty());
        colSoLuong.setCellValueFactory(cellData -> cellData.getValue().soLuongProperty().asObject());
        colDonGia.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());
        colTienCong.setCellValueFactory(cellData -> cellData.getValue().tienCongProperty().asObject());
        colThanhTienChiTiet.setCellValueFactory(cellData -> cellData.getValue().thanhTienProperty().asObject());

        danhSachChiTietDisplay.addListener((javafx.collections.ListChangeListener.Change<? extends ChiTietSuaChuaDisplay> c) -> {
            calculateAndDisplayTotal();
            updateSTT();
        });

        loadAllBienSoXe();
        loadVatTuData();
        loadTienCongData();
        loadThoData();
        setFormEnabled(false);
    }

    private void setFormEnabled(boolean enable) {
        txtNoiDungSuaChua.setDisable(!enable);
        cbVatTu.setDisable(!enable);
        txtSoLuongVatTu.setDisable(!enable);
        cbTienCong.setDisable(!enable);
        btnThemChiTiet.setDisable(!enable);
        tblChiTietSuaChua.setDisable(!enable);
        btnXoaChiTiet.setDisable(!enable);
        dpNgaySuaChua.setDisable(!enable);
        txtGhiChu.setDisable(!enable);
        cbThoPhanCong.setDisable(!enable);
        btnLapPhieuSuaChua.setDisable(!enable);
        btnLamMoiPhieu.setDisable(!enable);
    }

    private void loadAllBienSoXe() {
        try {
            cbBienSoXeSearch.setItems(tiepNhanDAO.getAllBienSoXe());
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách biển số xe.");
        }
    }

    private void loadVatTuData() {
        try {
            cbVatTu.setItems(FXCollections.observableArrayList(vatTuDAO.getAllVatTu()));
            cbVatTu.setConverter(new javafx.util.StringConverter<VatTu>() {
                @Override
                public String toString(VatTu vatTu) { return vatTu != null ? vatTu.getTenVatTu() : ""; }
                @Override
                public VatTu fromString(String string) { return null; }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách vật tư.");
        }
    }

    private void loadTienCongData() {
        try {
            cbTienCong.setItems(FXCollections.observableArrayList(tienCongDAO.getAllTienCong()));
             cbTienCong.setConverter(new javafx.util.StringConverter<TienCong>() {
                @Override
                public String toString(TienCong tienCong) { return tienCong != null ? tienCong.getNoiDung() : ""; }
                @Override
                public TienCong fromString(String string) { return null; }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách tiền công.");
        }
    }

    private void loadThoData() {
        try {
            cbThoPhanCong.setItems(FXCollections.observableArrayList(thoDAO.getAllTho()));
            cbThoPhanCong.setConverter(new javafx.util.StringConverter<Tho>() {
                @Override
                public String toString(Tho tho) { return tho != null ? tho.getTenTho() : ""; }
                @Override
                public Tho fromString(String string) { return null; }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách thợ.");
        }
    }

    @FXML
    private void handleTimKiemXe() {
        String bienSoXe = cbBienSoXeSearch.getValue();
        if (bienSoXe == null || bienSoXe.trim().isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập hoặc chọn biển số xe.");
            return;
        }
        try {
            ObservableList<TiepNhan> records = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            if (records.isEmpty()) {
                AlertUtils.showInformationAlert("Không tìm thấy", "Không có hồ sơ cho biển số xe: " + bienSoXe);
                clearVehicleInfoLabels();
                setFormEnabled(false);
            } else if (records.size() == 1) {
                selectedTiepNhan = records.get(0);
                displayTiepNhanInfo(selectedTiepNhan);
                setFormEnabled(true);
            } else {
                showTiepNhanSelectionDialog(records);
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tìm kiếm", "Lỗi khi tìm hồ sơ tiếp nhận.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChonHoSo() {
         String bienSoXe = cbBienSoXeSearch.getValue();
        if (bienSoXe == null || bienSoXe.trim().isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập hoặc chọn biển số xe.");
            return;
        }
        try {
            ObservableList<TiepNhan> records = tiepNhanDAO.getTiepNhanByBienSo(bienSoXe);
            if (!records.isEmpty()) {
                showTiepNhanSelectionDialog(records);
            } else {
                AlertUtils.showInformationAlert("Không tìm thấy", "Không có hồ sơ cho biển số xe: " + bienSoXe);
            }
        } catch (SQLException e) {
             AlertUtils.showErrorAlert("Lỗi tải hồ sơ", "Lỗi khi tải danh sách hồ sơ.");
             e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void showTiepNhanSelectionDialog(ObservableList<TiepNhan> records) {
        Dialog<TiepNhan> dialog = new Dialog<>();
        dialog.setTitle("Chọn Hồ Sơ Tiếp Nhận");
        dialog.setHeaderText("Có nhiều hồ sơ, vui lòng chọn một:");
        ButtonType selectButtonType = new ButtonType("Chọn", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);

        TableView<TiepNhan> selectionTable = new TableView<>(records);
        TableColumn<TiepNhan, Integer> colMaTiepNhan = new TableColumn<>("Mã TN");
        colMaTiepNhan.setCellValueFactory(p -> p.getValue().maTiepNhanProperty().asObject());
        TableColumn<TiepNhan, String> colBienSoDialog = new TableColumn<>("Biển số");
        colBienSoDialog.setCellValueFactory(p -> p.getValue().bienSoProperty());
        TableColumn<TiepNhan, String> colTenChuXeDialog = new TableColumn<>("Chủ xe");
        colTenChuXeDialog.setCellValueFactory(p -> p.getValue().tenChuXeProperty());
        TableColumn<TiepNhan, LocalDate> colNgayTiepNhanDialog = new TableColumn<>("Ngày TN");
        colNgayTiepNhanDialog.setCellValueFactory(p -> p.getValue().ngayTiepNhanProperty());
        selectionTable.getColumns().setAll(colMaTiepNhan, colBienSoDialog, colTenChuXeDialog, colNgayTiepNhanDialog);
        dialog.getDialogPane().setContent(selectionTable);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return selectionTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(tiepNhan -> {
            selectedTiepNhan = tiepNhan;
            displayTiepNhanInfo(selectedTiepNhan);
            setFormEnabled(true);
            danhSachChiTietDisplay.clear();
        });
    }

    private void displayTiepNhanInfo(TiepNhan tiepNhan) {
        lblTenChuXe.setText(tiepNhan.getTenChuXe());
        lblHieuXe.setText(tiepNhan.getTenHieuXe());
        lblDienThoai.setText(tiepNhan.getDienThoaiChuXe());
        lblDiaChi.setText(tiepNhan.getDiaChiChuXe());
        lblNgayTiepNhan.setText(tiepNhan.getNgayTiepNhan().toString());
        lblTienNoHienTai.setText(String.format("%,.2f VNĐ", tiepNhan.getTongTienNo()));
    }

    private void clearVehicleInfoLabels() {
        lblTenChuXe.setText("[Chưa chọn]");
        lblHieuXe.setText("[Chưa chọn]");
        lblDienThoai.setText("[Chưa chọn]");
        lblDiaChi.setText("[Chưa chọn]");
        lblNgayTiepNhan.setText("[Chưa chọn]");
        lblTienNoHienTai.setText("0.00 VNĐ");
    }
    
    @FXML
    private void handleThemChiTiet() {
        String noiDung = txtNoiDungSuaChua.getText().trim();
        VatTu selectedVatTu = cbVatTu.getValue();
        TienCong selectedTienCong = cbTienCong.getValue();
        String soLuongStr = txtSoLuongVatTu.getText().trim();

        if (noiDung.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập nội dung sửa chữa.");
            return;
        }

        int soLuong = 0;
        if (selectedVatTu != null) {
            if (soLuongStr.isEmpty() || !soLuongStr.matches("\\d+") || Integer.parseInt(soLuongStr) <= 0) {
                AlertUtils.showWarningAlert("Số lượng không hợp lệ", "Vui lòng nhập số lượng nguyên dương cho vật tư.");
                return;
            }
            soLuong = Integer.parseInt(soLuongStr);

            try {
                VatTu currentVatTu = vatTuDAO.getVatTuById(selectedVatTu.getMaVatTu());
                if (currentVatTu.getSoLuongTon() < soLuong) {
                    AlertUtils.showErrorAlert("Không đủ tồn kho", "Số lượng tồn kho của " + currentVatTu.getTenVatTu() + " không đủ (còn " + currentVatTu.getSoLuongTon() + ").");
                    return;
                }
            } catch (SQLException e) {
                 AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Lỗi khi kiểm tra tồn kho: " + e.getMessage());
                 e.printStackTrace();
                 return;
            }
        }
        
        danhSachChiTietDisplay.add(new ChiTietSuaChuaDisplay(
            danhSachChiTietDisplay.size() + 1,
            noiDung,
            selectedVatTu,
            soLuong,
            selectedTienCong
        ));

        txtNoiDungSuaChua.clear();
        cbVatTu.getSelectionModel().clearSelection();
        txtSoLuongVatTu.clear();
        cbTienCong.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void handleXoaChiTiet() {
        ChiTietSuaChuaDisplay selectedItem = tblChiTietSuaChua.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            danhSachChiTietDisplay.remove(selectedItem);
        } else {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một mục để xóa.");
        }
    }

    private void updateSTT() {
        for (int i = 0; i < danhSachChiTietDisplay.size(); i++) {
            danhSachChiTietDisplay.get(i).stt.set(i + 1);
        }
    }

    private void calculateAndDisplayTotal() {
        double total = danhSachChiTietDisplay.stream().mapToDouble(item -> item.thanhTien.get()).sum();
        lblTongTienPhieuSC.setText(String.format("%,.0f VNĐ", total));
    }

    @FXML
    private void handleLapPhieuSuaChua() {
        if (selectedTiepNhan == null || danhSachChiTietDisplay.isEmpty() || cbThoPhanCong.getValue() == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng hoàn tất thông tin phiếu sửa chữa.");
            return;
        }

        try {
            PhieuSuaChua newPhieu = new PhieuSuaChua();
            newPhieu.setMaTiepNhan(selectedTiepNhan.getMaTiepNhan());
            newPhieu.setNgaySuaChua(dpNgaySuaChua.getValue());
            newPhieu.setGhiChu(txtGhiChu.getText());
            newPhieu.setMaTho(cbThoPhanCong.getValue().getMaTho());
            newPhieu.setTongTien(Double.parseDouble(lblTongTienPhieuSC.getText().replaceAll("[^\\d]", "")));
            newPhieu.setTrangThaiHoanTat(false);

            int maPhieuSC = phieuSuaChuaDAO.addPhieuSuaChua(newPhieu);

            for (ChiTietSuaChuaDisplay item : danhSachChiTietDisplay) {
                if (item.getOriginalVatTu() != null) {
                    ChiTietPhieuSuaChua_VatTu vtDetail = new ChiTietPhieuSuaChua_VatTu();
                    vtDetail.setMaPhieuSC(maPhieuSC);
                    vtDetail.setMaVatTu(item.getOriginalVatTu().getMaVatTu());
                    vtDetail.setSoLuong(item.soLuong.get());
                    vtDetail.setDonGiaNhap(item.donGia.get());
                    vtDetail.setThanhTien(item.soLuong.get() * item.donGia.get());
                    chiTietVatTuDAO.addChiTietVatTu(vtDetail);

                    VatTu vatTuToUpdate = vatTuDAO.getVatTuById(vtDetail.getMaVatTu());
                    vatTuDAO.updateSoLuongTon(vtDetail.getMaVatTu(), vatTuToUpdate.getSoLuongTon() - vtDetail.getSoLuong());
                }

                if (item.getOriginalTienCong() != null) {
                    ChiTietPhieuSuaChua_TienCong tcDetail = new ChiTietPhieuSuaChua_TienCong();
                    tcDetail.setMaPhieuSC(maPhieuSC);
                    tcDetail.setMaTienCong(item.getOriginalTienCong().getMaTienCong());
                    // We need to associate the content with the labor cost.
                    // For now, let's assume the MaTienCong is enough.
                    // A better approach would be to save the `noiDung` from `ChiTietSuaChuaDisplay`
                    // in a new field in the `CHITIETPHIEUSUACHUA_TIENCONG` table.
                    tcDetail.setDonGia(item.tienCong.get());
                    tcDetail.setThanhTien(item.tienCong.get());
                    chiTietTienCongDAO.addChiTietTienCong(tcDetail);
                }
            }
            
            tiepNhanDAO.updateTienNoAndTrangThaiXe(selectedTiepNhan.getMaTiepNhan(), newPhieu.getTongTien(), "Đang sửa");
            AlertUtils.showInformationAlert("Thành công", "Lập phiếu sửa chữa thành công!");
            handleLamMoiPhieu();

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi cơ sở dữ liệu", "Không thể lưu phiếu sửa chữa: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            AlertUtils.showErrorAlert("Lỗi định dạng", "Lỗi khi đọc tổng tiền. Vui lòng kiểm tra lại.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLamMoiPhieu() {
        cbBienSoXeSearch.getSelectionModel().clearSelection();
        cbBienSoXeSearch.setValue(null);
        clearVehicleInfoLabels();
        selectedTiepNhan = null;
        danhSachChiTietDisplay.clear();
        txtNoiDungSuaChua.clear();
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