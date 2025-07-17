package dao;

import model.PhanQuyen;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface PhanQuyenDAO {
    /**
     * Lấy danh sách tất cả các nhóm quyền.
     * @return Danh sách các đối tượng PhanQuyen.
     * @throws SQLException
     */
    List<PhanQuyen> getAllPhanQuyen() throws SQLException;

    /**
     * Lấy danh sách các mã chức năng của một nhóm quyền cụ thể.
     * @param maPhanQuyen Mã của nhóm quyền.
     * @return Một Set chứa các mã chức năng.
     * @throws SQLException
     */
    Set<String> getChucNangByMaPhanQuyen(String maPhanQuyen) throws SQLException;

    /**
     * Thêm một nhóm quyền mới cùng với các chức năng của nó.
     * @param phanQuyen Đối tượng PhanQuyen chứa thông tin nhóm quyền.
     * @param chucNangs Một List chứa các mã chức năng.
     * @return true nếu thêm thành công, false nếu thất bại.
     * @throws SQLException
     */
    boolean addPhanQuyen(PhanQuyen phanQuyen, List<String> chucNangs) throws SQLException;

    /**
     * Cập nhật thông tin một nhóm quyền (tên và danh sách chức năng).
     * @param phanQuyen Đối tượng PhanQuyen chứa thông tin cập nhật.
     * @param chucNangs Danh sách chức năng mới.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     * @throws SQLException
     */
    boolean updatePhanQuyen(PhanQuyen phanQuyen, List<String> chucNangs) throws SQLException;

    /**
     * Xóa một nhóm quyền.
     * @param maPhanQuyen Mã của nhóm quyền cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại.
     * @throws SQLException
     */
    boolean deletePhanQuyen(String maPhanQuyen) throws SQLException;

    /**
     * Kiểm tra xem một mã phân quyền đã tồn tại hay chưa.
     * @param maPhanQuyen Mã phân quyền cần kiểm tra.
     * @return true nếu mã đã tồn tại, false nếu chưa.
     * @throws SQLException
     */
    boolean isMaPhanQuyenExists(String maPhanQuyen) throws SQLException;
    
    /**
     * Kiểm tra xem một nhóm quyền có đang được tài khoản nào sử dụng không.
     * @param maPhanQuyen Mã phân quyền cần kiểm tra.
     * @return true nếu đang được sử dụng, false nếu không.
     * @throws SQLException
     */
    boolean isPhanQuyenInUse(String maPhanQuyen) throws SQLException;
} 