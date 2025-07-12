package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DoanhThuEntry; // New model for revenue report
import model.VatTu;
import model.TiepNhan;

import dao.PhieuSuaChuaDAO;
import dao.PhieuThuTienDAO;
import dao.VatTuDAO;
import dao.TiepNhanDAO;
import utils.AlertUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaoCaoController {

    // FXML elements for Doanh thu Tab
    @FXML
    private DatePicker dpDoanhThuFrom;
    @FXML
    private DatePicker dpDoanhThuTo;
    @FXML
    private Button btnXemDoanhThu;
    @FXML
    private TableView<DoanhThuEntry> tblDoanhThu;
    @FXML
    private TableColumn<DoanhThuEntry, LocalDate> colNgayDoanhThu;
    @FXML
    private TableColumn<DoanhThuEntry, Integer> colSoPhieuSuaChua;
    @FXML
    private TableColumn<DoanhThuEntry, Double> colTongTienPhieuSC;
    @FXML
    private TableColumn<DoanhThuEntry, Double> colTongTienThu;
    @FXML
    private TableColumn<DoanhThuEntry, Double> colTienConNoDoanhThu;
    @FXML
    private Label lblTongDoanhThuTrongKy;

    // FXML elements for Tồn kho vật tư Tab
    @FXML
    private Button btnXemTonKho;
    @FXML
    private TableView<VatTu> tblTonKhoVatTu;
    @FXML
    private TableColumn<VatTu, Integer> colMaVatTu;
    @FXML
    private TableColumn<VatTu, String> colTenVatTu;
    @FXML
    private TableColumn<VatTu, Double> colDonGiaTon;
    @FXML
    private TableColumn<VatTu, Integer> colSoLuongTon;
    @FXML
    private TableColumn<VatTu, Double> colTongGiaTriTon;
    @FXML
    private Label lblTongGiaTriTonKho;

    // FXML elements for Công nợ khách hàng Tab
    @FXML
    private Button btnXemCongNo;
    @FXML
    private TableView<TiepNhan> tblCongNoKhachHang;
    @FXML
    private TableColumn<TiepNhan, Integer> colMaTiepNhanCN;
    @FXML
    private TableColumn<TiepNhan, String> colBienSoCN;
    @FXML
    private TableColumn<TiepNhan, String> colTenChuXeCN;
    @FXML
    private TableColumn<TiepNhan, String> colDienThoaiCN;
    @FXML
    private TableColumn<TiepNhan, LocalDate> colNgayTiepNhanCN;
    @FXML
    private TableColumn<TiepNhan, Double> colTongTienNoCN;
    @FXML
    private Label lblTongCongNoKhachHang;

    // DAOs
    private PhieuSuaChuaDAO phieuSuaChuaDAO;
    private PhieuThuTienDAO phieuThuTienDAO;
    private VatTuDAO vatTuDAO;
    private TiepNhanDAO tiepNhanDAO;

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize DAOs
        phieuSuaChuaDAO = new PhieuSuaChuaDAO();
        phieuThuTienDAO = new PhieuThuTienDAO();
        vatTuDAO = new VatTuDAO();
        tiepNhanDAO = new TiepNhanDAO();

        // Set default dates for Doanh thu report (e.g., current month)
        LocalDate today = LocalDate.now();
        dpDoanhThuFrom.setValue(today.withDayOfMonth(1));
        dpDoanhThuTo.setValue(today);

        // Configure TableView columns for Doanh thu
        colNgayDoanhThu.setCellValueFactory(cellData -> cellData.getValue().ngayProperty());
        colSoPhieuSuaChua.setCellValueFactory(cellData -> cellData.getValue().soPhieuSuaChuaProperty().asObject());
        colTongTienPhieuSC.setCellValueFactory(cellData -> cellData.getValue().tongTienPhieuSCProperty().asObject());
        colTongTienThu.setCellValueFactory(cellData -> cellData.getValue().tongTienThuProperty().asObject());
        colTienConNoDoanhThu.setCellValueFactory(cellData -> cellData.getValue().tienConNoProperty().asObject());

        // Configure TableView columns for Tồn kho vật tư
        colMaVatTu.setCellValueFactory(cellData -> cellData.getValue().maVatTuProperty().asObject());
        colTenVatTu.setCellValueFactory(cellData -> cellData.getValue().tenVatTuProperty());
        colDonGiaTon.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().asObject());
        colSoLuongTon.setCellValueFactory(cellData -> cellData.getValue().soLuongTonProperty().asObject());
        colTongGiaTriTon.setCellValueFactory(cellData -> cellData.getValue().donGiaProperty().multiply(cellData.getValue().soLuongTonProperty()).asObject());

        // Configure TableView columns for Công nợ khách hàng
        colMaTiepNhanCN.setCellValueFactory(cellData -> cellData.getValue().maTiepNhanProperty().asObject());
        colBienSoCN.setCellValueFactory(cellData -> cellData.getValue().bienSoProperty());
        colTenChuXeCN.setCellValueFactory(cellData -> cellData.getValue().tenChuXeProperty());
        colDienThoaiCN.setCellValueFactory(cellData -> cellData.getValue().dienThoaiChuXeProperty());
        colNgayTiepNhanCN.setCellValueFactory(cellData -> cellData.getValue().ngayTiepNhanProperty());
        colTongTienNoCN.setCellValueFactory(cellData -> cellData.getValue().tongTienNoProperty().asObject());

        // Initial load for default reports (optional, can be triggered by buttons)
        // handleXemDoanhThu();
        // handleXemTonKho();
        // handleXemCongNo();
    }

    /**
     * Handles the "Xem báo cáo" button action for Doanh thu.
     * Generates and displays the revenue report for the selected date range.
     */
    @FXML
    private void handleXemDoanhThu() {
        LocalDate fromDate = dpDoanhThuFrom.getValue();
        LocalDate toDate = dpDoanhThuTo.getValue();

        if (fromDate == null || toDate == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }
        if (fromDate.isAfter(toDate)) {
            AlertUtils.showWarningAlert("Lỗi ngày", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        try {
            // Get all repair slips and payment receipts within the date range
            List<model.PhieuSuaChua> phieuSuaChuaList = phieuSuaChuaDAO.getPhieuSuaChuaByDateRange(fromDate, toDate);
            List<model.PhieuThuTien> phieuThuTienList = phieuThuTienDAO.getPhieuThuTienByDateRange(fromDate, toDate);
            List<TiepNhan> allTiepNhan = tiepNhanDAO.getAllTiepNhan(); // To get current outstanding debt for each TiepNhan

            // Group repair slips and payments by date
            Map<LocalDate, List<model.PhieuSuaChua>> scByDate = phieuSuaChuaList.stream()
                    .collect(Collectors.groupingBy(model.PhieuSuaChua::getNgaySuaChua));
            Map<LocalDate, List<model.PhieuThuTien>> ttByDate = phieuThuTienList.stream()
                    .collect(Collectors.groupingBy(model.PhieuThuTien::getNgayThu));

            ObservableList<DoanhThuEntry> reportData = FXCollections.observableArrayList();
            double totalRevenueInPeriod = 0.0;

            // Iterate through each day in the range
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                int soPhieuSC = scByDate.getOrDefault(date, List.of()).size();
                double tongTienSC = scByDate.getOrDefault(date, List.of()).stream()
                        .mapToDouble(model.PhieuSuaChua::getTongTien).sum();
                double tongTienThu = ttByDate.getOrDefault(date, List.of()).stream()
                        .mapToDouble(model.PhieuThuTien::getSoTienThu).sum();

                // Calculate outstanding debt for this specific day's records (this is complex for daily cumulative)
                // For simplicity, TienConNoDoanhThu for a day will be the sum of TongTienNo of TiepNhan records
                // that were *active* on that day and still have debt.
                // A more accurate daily "Tien con no" would require tracking cumulative debt changes.
                // For this report, let's consider it as the *new* debt accumulated on that day.
                double tienConNo = tongTienSC - tongTienThu; // This is actually net change for the day

                reportData.add(new DoanhThuEntry(date, soPhieuSC, tongTienSC, tongTienThu, tienConNo));
                totalRevenueInPeriod += tongTienThu; // Total revenue is sum of collected payments
            }

            tblDoanhThu.setItems(reportData);
            lblTongDoanhThuTrongKy.setText(String.format("%.2f VNĐ", totalRevenueInPeriod));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo doanh thu", "Không thể tạo báo cáo doanh thu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xem tồn kho" button action for Tồn kho vật tư.
     * Generates and displays the inventory report.
     */
    @FXML
    private void handleXemTonKho() {
        try {
            ObservableList<VatTu> vatTuList = FXCollections.observableArrayList(vatTuDAO.getAllVatTu());
            tblTonKhoVatTu.setItems(vatTuList);

            double totalInventoryValue = vatTuList.stream()
                    .mapToDouble(vt -> vt.getDonGia() * vt.getSoLuongTon())
                    .sum();
            lblTongGiaTriTonKho.setText(String.format("%.2f VNĐ", totalInventoryValue));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo tồn kho", "Không thể tạo báo cáo tồn kho vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xem công nợ" button action for Công nợ khách hàng.
     * Generates and displays the outstanding debt report.
     */
    @FXML
    private void handleXemCongNo() {
        try {
            // Get all TiepNhan records and filter for outstanding debt
            // Assuming TiepNhanDAO.getAllTiepNhan() fetches all necessary join data
            ObservableList<TiepNhan> allTiepNhanRecords = tiepNhanDAO.getAllTiepNhan();
            ObservableList<TiepNhan> outstandingDebtRecords = FXCollections.observableArrayList();

            double totalOutstandingDebt = 0.0;

            for (TiepNhan tn : allTiepNhanRecords) {
                if (tn.getTongTienNo() > 0) {
                    outstandingDebtRecords.add(tn);
                    totalOutstandingDebt += tn.getTongTienNo();
                }
            }

            tblCongNoKhachHang.setItems(outstandingDebtRecords);
            lblTongCongNoKhachHang.setText(String.format("%.2f VNĐ", totalOutstandingDebt));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo công nợ", "Không thể tạo báo cáo công nợ khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
