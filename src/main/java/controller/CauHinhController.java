package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.ThamSo;
import dao.ThamSoDAO;
import utils.AlertUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CauHinhController {

    @FXML
    private TextField txtSoXeToiDaMoiNgay;
    @FXML
    private TextField txtSoHieuXeToiDa;
    @FXML
    private TextField txtSoLoaiVatTuToiDa;
    @FXML
    private TextField txtSoLoaiTienCongToiDa;
    @FXML
    private Button btnLuuCauHinh;
    @FXML
    private Button btnLamMoi;

    private ThamSoDAO thamSoDAO;
    private Map<String, TextField> paramTextFieldMap; // Map to easily manage text fields by parameter name

    /**
     * Initializes the controller. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        thamSoDAO = new ThamSoDAO();
        paramTextFieldMap = new HashMap<>();
        paramTextFieldMap.put("SoXeToiDaMoiNgay", txtSoXeToiDaMoiNgay);
        paramTextFieldMap.put("SoHieuXeToiDa", txtSoHieuXeToiDa);
        paramTextFieldMap.put("SoLoaiVatTuToiDa", txtSoLoaiVatTuToiDa);
        paramTextFieldMap.put("SoLoaiTienCongToiDa", txtSoLoaiTienCongToiDa);

        loadParameters();
    }

    /**
     * Loads system parameters from the database and displays them in the text fields.
     */
    private void loadParameters() {
        try {
            List<ThamSo> thamSoList = thamSoDAO.getAllThamSo();
            for (ThamSo ts : thamSoList) {
                TextField textField = paramTextFieldMap.get(ts.getTenThamSo());
                if (textField != null) {
                    textField.setText(String.valueOf(ts.getGiaTri()));
                }
            }
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi tải cấu hình", "Không thể tải các tham số cấu hình từ cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Lưu cấu hình" button action. Saves the updated parameters to the database.
     */
    @FXML
    private void handleLuuCauHinh() {
        try {
            for (Map.Entry<String, TextField> entry : paramTextFieldMap.entrySet()) {
                String paramName = entry.getKey();
                TextField textField = entry.getValue();
                String valueText = textField.getText().trim();

                if (valueText.isEmpty()) {
                    AlertUtils.showWarningAlert("Thiếu thông tin", "Vui lòng nhập giá trị cho tất cả các tham số.");
                    return;
                }

                int giaTri;
                try {
                    giaTri = Integer.parseInt(valueText);
                    if (giaTri < 0) {
                        AlertUtils.showWarningAlert("Lỗi giá trị", "Giá trị của '" + paramName + "' phải là số nguyên không âm.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    AlertUtils.showWarningAlert("Lỗi định dạng", "Giá trị của '" + paramName + "' phải là một số nguyên hợp lệ.");
                    return;
                }

                ThamSo thamSo = new ThamSo(paramName, giaTri);
                thamSoDAO.updateThamSo(thamSo);
            }
            AlertUtils.showInformationAlert("Thành công", "Cấu hình đã được lưu thành công!");
        } catch (SQLException e) {
            AlertUtils.showErrorAlert("Lỗi lưu cấu hình", "Đã xảy ra lỗi khi lưu cấu hình: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Làm mới" button action. Reloads parameters from the database.
     */
    @FXML
    private void handleLamMoi() {
        loadParameters();
        AlertUtils.showInformationAlert("Làm mới", "Các tham số cấu hình đã được làm mới từ cơ sở dữ liệu.");
    }
}
