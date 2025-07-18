package controller;

import dao.ChiTietPhieuSuaChua_TienCongDAO;
import dao.ChiTietPhieuSuaChua_VatTuDAO;
import dao.PhieuSuaChuaDAO;
import dao.TiepNhanDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;
import utils.AlertUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TrangThaiXuongXeController {

    // FXML fields for table and details
    @FXML private TableView<TiepNhan> tvXeDangXuLy;
    @FXML private TableColumn<TiepNhan, String> colBienSo;
    @FXML private TableColumn<TiepNhan, String> colTenChuXe;
    @FXML private TableColumn<TiepNhan, LocalDate> colNgayTiepNhan;
    @FXML private TableColumn<TiepNhan, String> colTrangThai;
    @FXML private Label lblBienSoChiTiet;
    @FXML private TabPane tabPaneChiTiet;
    @FXML private Label lblThongTinSuaChua;
    @FXML private TableView<ChiTietPhieuSuaChua_VatTu> tvVatTu;
    @FXML private TableColumn<ChiTietPhieuSuaChua_VatTu, String> colTenVatTu;
    @FXML private TableColumn<ChiTietPhieuSuaChua_VatTu, Integer> colSoLuongVatTu;
    @FXML private TableColumn<ChiTietPhieuSuaChua_VatTu, BigDecimal> colThanhTienVatTu;
    @FXML private TableView<ChiTietPhieuSuaChua_TienCong> tvTienCong;
    @FXML private TableColumn<ChiTietPhieuSuaChua_TienCong, String> colNoiDungTienCong;
    @FXML private TableColumn<ChiTietPhieuSuaChua_TienCong, BigDecimal> colThanhTienCong;

    // FXML fields for filtering
    @FXML private DatePicker dpTuNgay;
    @FXML private DatePicker dpDenNgay;
    @FXML private Button btnLoc;
    @FXML private Button btnLamMoiLoc;

    // DAOs
    private final TiepNhanDAO tiepNhanDAO = new TiepNhanDAO();
    private final PhieuSuaChuaDAO phieuSuaChuaDAO = new PhieuSuaChuaDAO();
    private final ChiTietPhieuSuaChua_VatTuDAO vatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO();
    private final ChiTietPhieuSuaChua_TienCongDAO tienCongDAO = new ChiTietPhieuSuaChua_TienCongDAO();

    @FXML
    public void initialize() {
        setupTables();
        loadXeDangXuLy(null, null); // Load all on initial view

        tvXeDangXuLy.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadChiTietXe(newSelection);
            } else {
                clearChiTiet();
            }
        });
        clearChiTiet();
    }

    private void setupTables() {
        colBienSo.setCellValueFactory(new PropertyValueFactory<>("bienSo"));
        colTenChuXe.setCellValueFactory(new PropertyValueFactory<>("tenChuXe"));
        colNgayTiepNhan.setCellValueFactory(new PropertyValueFactory<>("ngayTiepNhan"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        colTenVatTu.setCellValueFactory(new PropertyValueFactory<>("tenVatTu"));
        colSoLuongVatTu.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        colThanhTienVatTu.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        colNoiDungTienCong.setCellValueFactory(new PropertyValueFactory<>("noiDungTienCong"));
        colThanhTienCong.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));
    }

    private void loadXeDangXuLy(LocalDate fromDate, LocalDate toDate) {
        List<TiepNhan> listXe;
        if (fromDate != null && toDate != null) {
            listXe = tiepNhanDAO.getXeChuaHoanTatByDateRange(fromDate, toDate);
        } else {
            listXe = tiepNhanDAO.getTatCaXeChuaHoanTat();
        }
        ObservableList<TiepNhan> observableList = FXCollections.observableArrayList(listXe);
        tvXeDangXuLy.setItems(observableList);
        clearChiTiet();
    }

    private void loadChiTietXe(TiepNhan tiepNhan) {
        lblBienSoChiTiet.setText("Chi tiết cho xe: " + tiepNhan.getBienSo());
        tabPaneChiTiet.setVisible(true);

        PhieuSuaChua psc = phieuSuaChuaDAO.getPhieuSuaChuaByMaTiepNhan(tiepNhan.getMaTiepNhan());

        if (psc != null) {
            String thongTinTho = "Chưa phân công";
            if (psc.getMaTho() > 0) {
                 thongTinTho = "Thợ: " + psc.getTenTho();
            }
            lblThongTinSuaChua.setText(
                "Ngày sửa chữa: " + psc.getNgaySuaChua() + "\n" +
                thongTinTho + "\n" +
                "Tổng tiền (tạm tính): " + psc.getTongTien() + " VNĐ" + "\n" +
                "Ghi chú: " + psc.getGhiChu()
            );

            try {
                List<ChiTietPhieuSuaChua_VatTu> vatTuList = vatTuDAO.getChiTietVatTuByMaPhieuSC(psc.getMaPhieuSC());
                tvVatTu.setItems(FXCollections.observableArrayList(vatTuList));

                List<ChiTietPhieuSuaChua_TienCong> tienCongList = tienCongDAO.getChiTietTienCongByMaPhieuSC(psc.getMaPhieuSC());
                tvTienCong.setItems(FXCollections.observableArrayList(tienCongList));
            } catch (SQLException e) {
                e.printStackTrace();
                AlertUtils.showErrorAlert("Lỗi", "Không thể tải chi tiết sửa chữa.");
            }

        } else {
            lblThongTinSuaChua.setText("Chưa có phiếu sửa chữa cho xe này.");
            tvVatTu.getItems().clear();
            tvTienCong.getItems().clear();
        }
    }

    private void clearChiTiet() {
        lblBienSoChiTiet.setText("Vui lòng chọn một xe để xem chi tiết");
        tabPaneChiTiet.setVisible(false);
        lblThongTinSuaChua.setText("");
        tvVatTu.getItems().clear();
        tvTienCong.getItems().clear();
    }

    @FXML
    private void handleLoc() {
        LocalDate fromDate = dpTuNgay.getValue();
        LocalDate toDate = dpDenNgay.getValue();

        if (fromDate == null || toDate == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn cả ngày bắt đầu và ngày kết thúc.");
            return;
        }

        if (fromDate.isAfter(toDate)) {
            AlertUtils.showWarningAlert("Ngày không hợp lệ", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        loadXeDangXuLy(fromDate, toDate);
    }

    @FXML
    private void handleLamMoiLoc() {
        dpTuNgay.setValue(null);
        dpDenNgay.setValue(null);
        loadXeDangXuLy(null, null);
    }
} 