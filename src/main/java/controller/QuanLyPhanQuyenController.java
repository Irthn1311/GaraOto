package controller;

import dao.PhanQuyenDAO;
import dao.PhanQuyenDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.PhanQuyen;
import utils.AlertUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuanLyPhanQuyenController {

    @FXML private TableView<PhanQuyen> tvPhanQuyen;
    @FXML private TableColumn<PhanQuyen, String> colMaPhanQuyen;
    @FXML private TableColumn<PhanQuyen, String> colTenPhanQuyen;
    @FXML private TextField txtMaPhanQuyen;
    @FXML private TextField txtTenPhanQuyen;
    @FXML private GridPane gridChucNang;
    @FXML private Button btnThem;
    @FXML private Button btnSua;
    @FXML private Button btnXoa;

    private final PhanQuyenDAO phanQuyenDAO = new PhanQuyenDAOImpl();
    private final ObservableList<PhanQuyen> phanQuyenList = FXCollections.observableArrayList();
    private final Map<String, CheckBox> checkBoxes = new LinkedHashMap<>();

    // Định nghĩa các chức năng
    private static final Map<String, String> CHUC_NANG_MAP = new LinkedHashMap<>();
    static {
        CHUC_NANG_MAP.put("Q1", "Tiếp Nhận Xe");
        CHUC_NANG_MAP.put("Q2", "Lập Phiếu Sửa Chữa");
        CHUC_NANG_MAP.put("Q3", "Lập Phiếu Thu Tiền");
        CHUC_NANG_MAP.put("Q4", "Xem Báo Cáo");
        CHUC_NANG_MAP.put("Q5", "Thay Đổi Cấu Hình");
        CHUC_NANG_MAP.put("Q6", "Quản Lý Hiệu Xe");
        CHUC_NANG_MAP.put("Q7", "Quản Lý Tiền Công");
        CHUC_NANG_MAP.put("Q8", "Quản Lý Thợ");
        CHUC_NANG_MAP.put("Q9", "Quản Lý Nhà Cung Cấp");
        CHUC_NANG_MAP.put("Q10", "Quản Lý Vật Tư");
        CHUC_NANG_MAP.put("Q11", "Quản Lý Tài Khoản");
        CHUC_NANG_MAP.put("Q12", "Quản Lý Phân Quyền");
    }

    @FXML
    public void initialize() {
        setupUI();
        loadPhanQuyen();
    }

    private void setupUI() {
        // Cấu hình bảng
        colMaPhanQuyen.setCellValueFactory(cellData -> cellData.getValue().maPhanQuyenProperty());
        colTenPhanQuyen.setCellValueFactory(cellData -> cellData.getValue().tenPhanQuyenProperty());
        tvPhanQuyen.setItems(phanQuyenList);

        // Tạo checkboxes cho các chức năng
        int row = 0, col = 0;
        for (Map.Entry<String, String> entry : CHUC_NANG_MAP.entrySet()) {
            CheckBox cb = new CheckBox(entry.getValue());
            cb.setUserData(entry.getKey());
            checkBoxes.put(entry.getKey(), cb);
            gridChucNang.add(cb, col, row);
            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        // Thêm listener để hiển thị chi tiết khi chọn một dòng
        tvPhanQuyen.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPhanQuyenDetails(newValue));
    }

    private void loadPhanQuyen() {
        try {
            phanQuyenList.setAll(phanQuyenDAO.getAllPhanQuyen());
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải dữ liệu", "Không thể tải danh sách phân quyền.");
            e.printStackTrace();
        }
    }

    private void showPhanQuyenDetails(PhanQuyen phanQuyen) {
        if (phanQuyen != null) {
            txtMaPhanQuyen.setText(phanQuyen.getMaPhanQuyen());
            txtTenPhanQuyen.setText(phanQuyen.getTenPhanQuyen());
            txtMaPhanQuyen.setDisable(true);
            btnThem.setDisable(true);
            btnSua.setDisable(false);
            btnXoa.setDisable(false);

            try {
                // Reset tất cả checkbox
                checkBoxes.values().forEach(cb -> cb.setSelected(false));
                // Lấy và tick các checkbox tương ứng
                phanQuyenDAO.getChucNangByMaPhanQuyen(phanQuyen.getMaPhanQuyen())
                        .forEach(maChucNang -> {
                            if (checkBoxes.containsKey(maChucNang)) {
                                checkBoxes.get(maChucNang).setSelected(true);
                            }
                        });
            } catch (SQLException e) {
                AlertUtils.showErrorAlert("Lỗi tải chi tiết", "Không thể tải chi tiết quyền.");
                e.printStackTrace();
            }
        } else {
            handleLamMoi();
        }
    }

    @FXML
    void handleLamMoi() {
        tvPhanQuyen.getSelectionModel().clearSelection();
        txtMaPhanQuyen.clear();
        txtTenPhanQuyen.clear();
        checkBoxes.values().forEach(cb -> cb.setSelected(false));
        txtMaPhanQuyen.setDisable(false);
        btnThem.setDisable(false);
        btnSua.setDisable(true);
        btnXoa.setDisable(true);
    }

    @FXML
    void handleThem() {
        String maPhanQuyen = txtMaPhanQuyen.getText().trim().toUpperCase();
        String tenPhanQuyen = txtTenPhanQuyen.getText().trim();
        List<String> selectedChucNang = getSelectedChucNang();

        if (maPhanQuyen.isEmpty() || tenPhanQuyen.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Mã và Tên phân quyền không được để trống.");
            return;
        }

        try {
            if (phanQuyenDAO.isMaPhanQuyenExists(maPhanQuyen)) {
                AlertUtils.showWarningAlert("Trùng lặp", "Mã phân quyền đã tồn tại.");
                return;
            }

            PhanQuyen newPhanQuyen = new PhanQuyen(maPhanQuyen, tenPhanQuyen);
            if (phanQuyenDAO.addPhanQuyen(newPhanQuyen, selectedChucNang)) {
                AlertUtils.showSuccessAlert("Thành công", "Thêm nhóm quyền thành công.");
                loadPhanQuyen();
                handleLamMoi();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi SQL", "Không thể thêm nhóm quyền.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleSua() {
        PhanQuyen selected = tvPhanQuyen.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một nhóm quyền để sửa.");
            return;
        }

        String tenPhanQuyen = txtTenPhanQuyen.getText().trim();
        List<String> selectedChucNang = getSelectedChucNang();

        if (tenPhanQuyen.isEmpty()) {
            AlertUtils.showWarningAlert("Thiếu thông tin", "Tên phân quyền không được để trống.");
            return;
        }

        // Cannot edit "GIAMDOC" role
        if ("GIAMDOC".equals(selected.getMaPhanQuyen())) {
            AlertUtils.showWarningAlert("Không được phép", "Không thể chỉnh sửa quyền của Giám đốc.");
            return;
        }

        try {
            PhanQuyen updatedPhanQuyen = new PhanQuyen(selected.getMaPhanQuyen(), tenPhanQuyen);
            if (phanQuyenDAO.updatePhanQuyen(updatedPhanQuyen, selectedChucNang)) {
                AlertUtils.showSuccessAlert("Thành công", "Cập nhật nhóm quyền thành công.");
                loadPhanQuyen();
                handleLamMoi();
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi SQL", "Không thể cập nhật nhóm quyền.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleXoa() {
        PhanQuyen selected = tvPhanQuyen.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarningAlert("Chưa chọn", "Vui lòng chọn một nhóm quyền để xóa.");
            return;
        }

        // Cannot delete "GIAMDOC" role
        if ("GIAMDOC".equals(selected.getMaPhanQuyen())) {
            AlertUtils.showWarningAlert("Không được phép", "Không thể xóa quyền của Giám đốc.");
            return;
        }
        
        try {
            if (phanQuyenDAO.isPhanQuyenInUse(selected.getMaPhanQuyen())) {
                AlertUtils.showErrorAlert("Không thể xóa", "Nhóm quyền đang được sử dụng bởi một hoặc nhiều tài khoản.");
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa nhóm quyền '" + selected.getTenPhanQuyen() + "' không?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        if (phanQuyenDAO.deletePhanQuyen(selected.getMaPhanQuyen())) {
                            AlertUtils.showSuccessAlert("Thành công", "Xóa nhóm quyền thành công.");
                            loadPhanQuyen();
                            handleLamMoi();
                        }
                    } catch (SQLException e) {
                        AlertUtils.showErrorAlert("Lỗi SQL", "Không thể xóa nhóm quyền.");
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi SQL", "Không thể kiểm tra việc sử dụng nhóm quyền.");
            e.printStackTrace();
        }
    }

    private List<String> getSelectedChucNang() {
        return checkBoxes.entrySet().stream()
                .filter(entry -> entry.getValue().isSelected())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
} 