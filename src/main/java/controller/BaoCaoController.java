package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DoanhThuEntry; // Model for revenue report
import model.VatTu;
import model.TiepNhan;
import model.PhieuSuaChua; // Import PhieuSuaChua
import model.ChiTietPhieuSuaChua_VatTu;
import model.ChiTietPhieuSuaChua_TienCong;
import model.LuotTiepNhanSuaChuaEntry; // Model for acceptance/repair count report
import model.TieuHaoVatTuEntry; // Model for material consumption report
import model.LoiNhuanEntry; // Model for overall profit report
import model.TonKhoEntry; // Model for inventory report
import model.TienCong;

import dao.PhieuSuaChuaDAO;
import dao.PhieuThuTienDAO;
import dao.VatTuDAO;
import dao.TiepNhanDAO;
import dao.ChiTietPhieuSuaChua_VatTuDAO;
import dao.ChiTietPhieuSuaChua_TienCongDAO;
import dao.ChiTietPhieuNhapKhoVatTuDAO; // DAO for inventory report
import utils.AlertUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.text.NumberFormat; // For currency formatting
import java.util.Locale; // For currency formatting
import java.util.Comparator; // For sorting

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
    private TableView<TonKhoEntry> tblTonKhoVatTu;
    @FXML
    private TableColumn<TonKhoEntry, Integer> colMaVatTu;
    @FXML
    private TableColumn<TonKhoEntry, String> colTenVatTu;
    @FXML
    private TableColumn<TonKhoEntry, Double> colDonGiaTon;
    @FXML
    private TableColumn<TonKhoEntry, Integer> colSoLuongTon;
    @FXML
    private TableColumn<TonKhoEntry, Double> colTongGiaTriTon;
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

    // FXML elements for Lượt tiếp nhận và sửa chữa Tab (NEW)
    @FXML
    private DatePicker dpLuotFrom;
    @FXML
    private DatePicker dpLuotTo;
    @FXML
    private Button btnXemLuot;
    @FXML
    private TableView<LuotTiepNhanSuaChuaEntry> tblLuotTiepNhanSuaChua;
    @FXML
    private TableColumn<LuotTiepNhanSuaChuaEntry, LocalDate> colNgayLuot;
    @FXML
    private TableColumn<LuotTiepNhanSuaChuaEntry, Integer> colSoLuotTiepNhan;
    @FXML
    private TableColumn<LuotTiepNhanSuaChuaEntry, Integer> colSoPhieuSuaChuaLuot;

    // FXML elements for Tiêu hao vật tư Tab (NEW)
    @FXML
    private DatePicker dpTieuHaoFrom;
    @FXML
    private DatePicker dpTieuHaoTo;
    @FXML
    private Button btnXemTieuHao;
    @FXML
    private TableView<TieuHaoVatTuEntry> tblTieuHaoVatTu;
    @FXML
    private TableColumn<TieuHaoVatTuEntry, Integer> colMaVatTuTH;
    @FXML
    private TableColumn<TieuHaoVatTuEntry, String> colTenVatTuTH;
    @FXML
    private TableColumn<TieuHaoVatTuEntry, String> colDonViTinhTH;
    @FXML
    private TableColumn<TieuHaoVatTuEntry, Integer> colSoLuongTieuHaoTH;
    @FXML
    private TableColumn<TieuHaoVatTuEntry, Double> colTongGiaTriTieuHaoTH;
    @FXML
    private Label lblTongGiaTriTieuHao;

    // FXML elements for Lợi nhuận tổng thể Tab (NEW)
    @FXML
    private DatePicker dpLoiNhuanFrom;
    @FXML
    private DatePicker dpLoiNhuanTo;
    @FXML
    private Button btnXemLoiNhuan;
    @FXML
    private TableView<LoiNhuanEntry> tblLoiNhuan;
    @FXML
    private TableColumn<LoiNhuanEntry, LocalDate> colNgayLoiNhuan;
    @FXML
    private TableColumn<LoiNhuanEntry, Double> colDoanhThuLoiNhuan;
    @FXML
    private TableColumn<LoiNhuanEntry, Double> colTongChiPhiVatTuLN;
    @FXML
    private TableColumn<LoiNhuanEntry, Double> colTongChiPhiTienCongLN;
    @FXML
    private TableColumn<LoiNhuanEntry, Double> colLoiNhuanRongLN;
    @FXML
    private Label lblTongLoiNhuanRong;


    // DAOs
    private PhieuSuaChuaDAO phieuSuaChuaDAO;
    private PhieuThuTienDAO phieuThuTienDAO;
    private VatTuDAO vatTuDAO;
    private TiepNhanDAO tiepNhanDAO;
    private ChiTietPhieuSuaChua_VatTuDAO chiTietVatTuDAO;
    private ChiTietPhieuSuaChua_TienCongDAO chiTietTienCongDAO;
    private ChiTietPhieuNhapKhoVatTuDAO chiTietNhapKhoDAO; // DAO for inventory report


    private NumberFormat currencyFormat; // For currency formatting

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
        chiTietVatTuDAO = new ChiTietPhieuSuaChua_VatTuDAO();
        chiTietTienCongDAO = new ChiTietPhieuSuaChua_TienCongDAO();
        chiTietNhapKhoDAO = new ChiTietPhieuNhapKhoVatTuDAO(); // Initialize for inventory report


        // Initialize currency formatter
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("vi", "VN"));

        // Set default dates for all reports (e.g., current month)
        LocalDate today = LocalDate.now();
        dpDoanhThuFrom.setValue(today.withDayOfMonth(1));
        dpDoanhThuTo.setValue(today);
        dpLuotFrom.setValue(today.withDayOfMonth(1));
        dpLuotTo.setValue(today);
        dpTieuHaoFrom.setValue(today.withDayOfMonth(1));
        dpTieuHaoTo.setValue(today);
        dpLoiNhuanFrom.setValue(today.withDayOfMonth(1));
        dpLoiNhuanTo.setValue(today);


        // Configure TableView columns for Doanh thu
        colNgayDoanhThu.setCellValueFactory(cellData -> cellData.getValue().ngayProperty());
        colSoPhieuSuaChua.setCellValueFactory(cellData -> cellData.getValue().soPhieuSuaChuaProperty().asObject());
        colTongTienPhieuSC.setCellValueFactory(cellData -> cellData.getValue().tongTienPhieuSCProperty().asObject());
        colTongTienThu.setCellValueFactory(cellData -> cellData.getValue().tongTienThuProperty().asObject());
        colTienConNoDoanhThu.setCellValueFactory(cellData -> cellData.getValue().tienConNoProperty().asObject());
        // Custom cell factories for currency formatting
        setupCurrencyColumn(colTongTienPhieuSC);
        setupCurrencyColumn(colTongTienThu);
        setupCurrencyColumn(colTienConNoDoanhThu);

        // Configure TableView columns for Tồn kho vật tư
        colMaVatTu.setCellValueFactory(cellData -> cellData.getValue().maVatTuProperty().asObject());
        colTenVatTu.setCellValueFactory(cellData -> cellData.getValue().tenVatTuProperty());
        colDonGiaTon.setCellValueFactory(cellData -> cellData.getValue().donGiaVonProperty().asObject()); // Use DonGiaVon for inventory value
        colSoLuongTon.setCellValueFactory(cellData -> cellData.getValue().soLuongTonProperty().asObject());
        colTongGiaTriTon.setCellValueFactory(cellData -> cellData.getValue().tongGiaTriTonProperty().asObject());
        // Custom cell factories for currency formatting
        setupCurrencyColumn(colDonGiaTon);
        setupCurrencyColumn(colTongGiaTriTon);


        // Configure TableView columns for Công nợ khách hàng
        colMaTiepNhanCN.setCellValueFactory(cellData -> cellData.getValue().maTiepNhanProperty().asObject());
        colBienSoCN.setCellValueFactory(cellData -> cellData.getValue().bienSoProperty());
        colTenChuXeCN.setCellValueFactory(cellData -> cellData.getValue().tenChuXeProperty());
        colDienThoaiCN.setCellValueFactory(cellData -> cellData.getValue().dienThoaiChuXeProperty());
        colNgayTiepNhanCN.setCellValueFactory(cellData -> cellData.getValue().ngayTiepNhanProperty());
        colTongTienNoCN.setCellValueFactory(cellData -> cellData.getValue().tongTienNoProperty().asObject());
        // Custom cell factory for currency formatting
        setupCurrencyColumn(colTongTienNoCN);

        // Configure TableView columns for Lượt tiếp nhận và sửa chữa
        colNgayLuot.setCellValueFactory(cellData -> cellData.getValue().ngayProperty());
        colSoLuotTiepNhan.setCellValueFactory(cellData -> cellData.getValue().soLuotTiepNhanProperty().asObject());
        colSoPhieuSuaChuaLuot.setCellValueFactory(cellData -> cellData.getValue().soPhieuSuaChuaProperty().asObject());

        // Configure TableView columns for Tiêu hao vật tư
        colMaVatTuTH.setCellValueFactory(cellData -> cellData.getValue().maVatTuProperty().asObject());
        colTenVatTuTH.setCellValueFactory(cellData -> cellData.getValue().tenVatTuProperty());
        colDonViTinhTH.setCellValueFactory(cellData -> cellData.getValue().donViTinhProperty());
        colSoLuongTieuHaoTH.setCellValueFactory(cellData -> cellData.getValue().soLuongTieuHaoProperty().asObject());
        colTongGiaTriTieuHaoTH.setCellValueFactory(cellData -> cellData.getValue().tongGiaTriTieuHaoProperty().asObject());
        // Custom cell factory for currency formatting
        setupCurrencyColumn(colTongGiaTriTieuHaoTH);

        // Configure TableView columns for Lợi nhuận tổng thể
        colNgayLoiNhuan.setCellValueFactory(cellData -> cellData.getValue().ngayProperty());
        colDoanhThuLoiNhuan.setCellValueFactory(cellData -> cellData.getValue().tongDoanhThuBanHangProperty().asObject());
        colTongChiPhiVatTuLN.setCellValueFactory(cellData -> cellData.getValue().tongChiPhiVatTuProperty().asObject());
        colTongChiPhiTienCongLN.setCellValueFactory(cellData -> cellData.getValue().tongChiPhiTienCongProperty().asObject());
        colLoiNhuanRongLN.setCellValueFactory(cellData -> cellData.getValue().loiNhuanRongProperty().asObject());
        // Custom cell factories for currency formatting
        setupCurrencyColumn(colDoanhThuLoiNhuan);
        setupCurrencyColumn(colTongChiPhiVatTuLN);
        setupCurrencyColumn(colTongChiPhiTienCongLN);
        setupCurrencyColumn(colLoiNhuanRongLN);
    }

    /**
     * Helper method to set up currency formatting for TableColumns.
     * @param column The TableColumn to apply currency formatting to.
     */
    private <S> void setupCurrencyColumn(TableColumn<S, Double> column) {
        column.setCellFactory(tc -> new TableCell<S, Double>() {
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
            // No need to fetch all TiepNhan here, as we are focusing on daily aggregates of SC and TT.

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

                // TienConNoDoanhThu for a day represents the net change in debt for that specific day,
                // or more accurately, the difference between what was charged and what was collected on that day.
                double tienConNo = tongTienSC - tongTienThu;

                reportData.add(new DoanhThuEntry(date, soPhieuSC, tongTienSC, tongTienThu, tienConNo));
                totalRevenueInPeriod += tongTienThu; // Total revenue is sum of collected payments
            }

            // Sort reportData by date
            reportData.sort(Comparator.comparing(DoanhThuEntry::getNgay));

            tblDoanhThu.setItems(reportData);
            lblTongDoanhThuTrongKy.setText(currencyFormat.format(totalRevenueInPeriod));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo doanh thu", "Không thể tạo báo cáo doanh thu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of viewing the inventory report.
     */
    @FXML
    private void handleXemTonKho() {
        try {
            List<VatTu> allVatTu = vatTuDAO.getAllVatTu();

            if (allVatTu.isEmpty()) {
                tblTonKhoVatTu.setItems(FXCollections.observableArrayList());
                lblTongGiaTriTonKho.setText(currencyFormat.format(0));
                AlertUtils.showInformationAlert("Thông tin", "Không có vật tư nào trong kho.");
                return;
            }

            ObservableList<TonKhoEntry> tonKhoEntries = FXCollections.observableArrayList();
            double totalInventoryValue = 0.0;

            for (VatTu vt : allVatTu) {
                // Lấy đơn giá nhập gần nhất; nếu chưa từng nhập, dùng đơn giá bán làm tham chiếu
                double donGiaVon = chiTietNhapKhoDAO.getLatestDonGiaNhapByMaVatTu(vt.getMaVatTu());
                if (donGiaVon == 0.0) {
                    donGiaVon = vt.getDonGiaBan();
                }

                TonKhoEntry entry = new TonKhoEntry(vt.getMaVatTu(), vt.getTenVatTu(), donGiaVon, vt.getSoLuongTon());
                tonKhoEntries.add(entry);

                totalInventoryValue += entry.getTongGiaTriTon();
            }

            tblTonKhoVatTu.setItems(tonKhoEntries);
            lblTongGiaTriTonKho.setText(currencyFormat.format(totalInventoryValue));

        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtils.showErrorAlert("Lỗi báo cáo tồn kho", "Không thể tạo báo cáo tồn kho: " + e.getMessage());
        }
    }

    /**
     * Handles the "Xem công nợ" button action for Công nợ khách hàng.
     * Generates and displays the outstanding debt report.
     */
    @FXML
    private void handleXemCongNo() {
        try {
            ObservableList<TiepNhan> outstandingDebtRecords = tiepNhanDAO.getAllTiepNhanCoNo();
            
            double totalOutstandingDebt = outstandingDebtRecords.stream()
                                    .mapToDouble(TiepNhan::getTongTienNo)
                                    .sum();
            
            outstandingDebtRecords.sort(Comparator.comparing(TiepNhan::getMaTiepNhan));

            tblCongNoKhachHang.setItems(outstandingDebtRecords);
            lblTongCongNoKhachHang.setText(currencyFormat.format(totalOutstandingDebt));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo công nợ", "Không thể tạo báo cáo công nợ khách hàng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xem báo cáo" button action for Lượt tiếp nhận và sửa chữa.
     * Generates and displays the report for acceptance and repair counts.
     */
    @FXML
    private void handleXemLuot() {
        LocalDate fromDate = dpLuotFrom.getValue();
        LocalDate toDate = dpLuotTo.getValue();

        if (fromDate == null || toDate == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }
        if (fromDate.isAfter(toDate)) {
            AlertUtils.showWarningAlert("Lỗi ngày", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        try {
            // Get all TiepNhan and PhieuSuaChua records within the date range
            List<TiepNhan> tiepNhanList = tiepNhanDAO.getTiepNhanByDateRange(fromDate, toDate);
            List<PhieuSuaChua> phieuSuaChuaList = phieuSuaChuaDAO.getPhieuSuaChuaByDateRange(fromDate, toDate);

            // Group by date
            Map<LocalDate, Long> tiepNhanCountByDate = tiepNhanList.stream()
                    .collect(Collectors.groupingBy(TiepNhan::getNgayTiepNhan, Collectors.counting()));
            Map<LocalDate, Long> phieuSuaChuaCountByDate = phieuSuaChuaList.stream()
                    .collect(Collectors.groupingBy(PhieuSuaChua::getNgaySuaChua, Collectors.counting()));

            ObservableList<LuotTiepNhanSuaChuaEntry> reportData = FXCollections.observableArrayList();

            // Iterate through each day in the range to combine data
            for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
                int soLuotTiepNhan = tiepNhanCountByDate.getOrDefault(date, 0L).intValue();
                int soPhieuSuaChua = phieuSuaChuaCountByDate.getOrDefault(date, 0L).intValue();
                reportData.add(new LuotTiepNhanSuaChuaEntry(date, soLuotTiepNhan, soPhieuSuaChua));
            }

            // Sort reportData by date
            reportData.sort(Comparator.comparing(LuotTiepNhanSuaChuaEntry::getNgay));

            tblLuotTiepNhanSuaChua.setItems(reportData);

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo lượt tiếp nhận và sửa chữa", "Không thể tạo báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xem báo cáo" button action for Tiêu hao vật tư.
     * Generates and displays the material consumption report.
     */
    @FXML
    private void handleXemTieuHao() {
        LocalDate fromDate = dpTieuHaoFrom.getValue();
        LocalDate toDate = dpTieuHaoTo.getValue();

        if (fromDate == null || toDate == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }
        if (fromDate.isAfter(toDate)) {
            AlertUtils.showWarningAlert("Lỗi ngày", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        try {
            List<PhieuSuaChua> phieuSuaChuaList = phieuSuaChuaDAO.getPhieuSuaChuaDetailsByDateRange(fromDate, toDate);
            Map<Integer, VatTu> vatTuMap = vatTuDAO.getAllVatTu().stream()
                    .collect(Collectors.toMap(VatTu::getMaVatTu, vt -> vt));

            // Flatten the list of ChiTietSuaChua from all PhieuSuaChua
            Map<Integer, TieuHaoVatTuEntry> tieuHaoMap = phieuSuaChuaList.stream()
                    .flatMap(psc -> psc.getChiTietVatTuList().stream())
                    .collect(Collectors.groupingBy(
                            ChiTietPhieuSuaChua_VatTu::getMaVatTu,
                            Collectors.collectingAndThen(Collectors.toList(), list -> {
                                ChiTietPhieuSuaChua_VatTu first = list.get(0);
                                int totalSoLuong = list.stream().mapToInt(ChiTietPhieuSuaChua_VatTu::getSoLuong).sum();
                                // Correctly calculate cost based on DonGiaNhap
                                double totalGiaTri = list.stream().mapToDouble(vt -> vt.getSoLuong() * vt.getDonGiaNhap()).sum();
                                // Get DonViTinh from the pre-fetched map
                                VatTu vt = vatTuMap.get(first.getMaVatTu());
                                String donViTinh = (vt != null) ? vt.getDonViTinh() : "";
                                return new TieuHaoVatTuEntry(first.getMaVatTu(), first.getTenVatTu(), donViTinh, totalSoLuong, totalGiaTri);
                            })
                    ));

            ObservableList<TieuHaoVatTuEntry> reportData = FXCollections.observableArrayList(tieuHaoMap.values());
            reportData.sort(Comparator.comparingInt(TieuHaoVatTuEntry::getSoLuongTieuHao).reversed());

            tblTieuHaoVatTu.setItems(reportData);

            double totalGiaTriTieuHao = reportData.stream()
                    .mapToDouble(TieuHaoVatTuEntry::getTongGiaTriTieuHao)
                    .sum();
            lblTongGiaTriTieuHao.setText(currencyFormat.format(totalGiaTriTieuHao));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo tiêu hao vật tư", "Không thể tạo báo cáo tiêu hao vật tư: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Xem báo cáo" button action for Lợi nhuận tổng thể.
     * Generates and displays the overall profit report.
     */
    @FXML
    private void handleXemLoiNhuan() {
        LocalDate fromDate = dpLoiNhuanFrom.getValue();
        LocalDate toDate = dpLoiNhuanTo.getValue();

        if (fromDate == null || toDate == null) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng chọn đầy đủ ngày bắt đầu và ngày kết thúc.");
            return;
        }
        if (fromDate.isAfter(toDate)) {
            AlertUtils.showWarningAlert("Lỗi ngày", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        try {
            List<PhieuSuaChua> phieuSuaChuaList = phieuSuaChuaDAO.getPhieuSuaChuaDetailsByDateRange(fromDate, toDate);

            Map<LocalDate, List<PhieuSuaChua>> groupedByDate = phieuSuaChuaList.stream()
                    .collect(Collectors.groupingBy(PhieuSuaChua::getNgaySuaChua));

            ObservableList<LoiNhuanEntry> reportData = FXCollections.observableArrayList();
            for (Map.Entry<LocalDate, List<PhieuSuaChua>> entry : groupedByDate.entrySet()) {
                LocalDate ngay = entry.getKey();
                List<PhieuSuaChua> phieuTrongNgay = entry.getValue();

                double tongDoanhThu = phieuTrongNgay.stream().mapToDouble(PhieuSuaChua::getTongTien).sum();
                
                // Correctly calculate total material cost using DonGiaNhap
                double tongChiPhiVatTu = phieuTrongNgay.stream()
                        .flatMap(psc -> psc.getChiTietVatTuList().stream())
                        .mapToDouble(ct -> ct.getSoLuong() * ct.getDonGiaNhap())
                        .sum();

                double tongChiPhiTienCong = phieuTrongNgay.stream()
                        .flatMap(psc -> psc.getChiTietTienCongList().stream())
                        .mapToDouble(ChiTietPhieuSuaChua_TienCong::getThanhTien)
                        .sum();

                reportData.add(new LoiNhuanEntry(ngay, tongDoanhThu, tongChiPhiVatTu, tongChiPhiTienCong));
            }

            tblLoiNhuan.setItems(reportData);

            double totalLoiNhuanRong = reportData.stream()
                    .mapToDouble(LoiNhuanEntry::getLoiNhuanRong)
                    .sum();
            lblTongLoiNhuanRong.setText(currencyFormat.format(totalLoiNhuanRong));

        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi báo cáo lợi nhuận", "Không thể tạo báo cáo lợi nhuận tổng thể: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
