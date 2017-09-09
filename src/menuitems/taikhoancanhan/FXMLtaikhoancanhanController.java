/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menuitems.taikhoancanhan;

import database.DB;
import entities.loaitk.LoaiTk;
import entities.taikhoan.TaiKhoan;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FXMLtaikhoancanhanController implements Initializable {

    TaiKhoan tk=null;
    
    @FXML
    private Label lbUsername;
    @FXML
    private Label lbLoaiTk;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private ComboBox<String> cbGioiTinh;
    @FXML
    private TextField tfCmnd;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPassCu;
    @FXML
    private PasswordField tfPassMoi;
    @FXML
    private PasswordField tfPassXacNhan;
    @FXML
    private Button btnCapNhatThongTin;
    @FXML
    private Button btnCapNhatMatKhau;
    
    ArrayList<String> alGioiTinh;
    ObservableList<String> olGioiTinh;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alGioiTinh = new ArrayList<>();
        olGioiTinh = FXCollections.observableList(alGioiTinh);
        cbGioiTinh.setItems(olGioiTinh);
        alGioiTinh.add("Nam");
        alGioiTinh.add("Nữ");
        alGioiTinh.add("Không xác định");
        loadInfo();
        
        btnCapNhatThongTin.setOnAction((event) -> {
            tk.setUsername(DB.user);
            tk.setFirstName(tfFirstName.getText());
            tk.setLastName(tfLastName.getText());
            tk.setGioiTinh(cbGioiTinh.getSelectionModel().getSelectedItem());
            tk.setCmnd(tfCmnd.getText());
            tk.setDiaChi(tfDiaChi.getText());
            tk.setEmail(tfEmail.getText());
            if (tk.queryUpdateInfo()) {
                utils.Utils.showSuccess("Thành Công", "Cập nhật thông tin thành công!");
            } else {
                utils.Utils.showError("Thất Bại", "Cập nhật thông tin thất bại!");
            }
        });
        
        btnCapNhatMatKhau.setOnAction((event) -> {
            String passCu = tfPassCu.getText();
            String passMoi = tfPassMoi.getText();
            String passXacNhan = tfPassXacNhan.getText();
            
            if(passCu.isEmpty()) {
                utils.Utils.showError("Thiếu", "Xin mời nhập mật khẩu cũ!");
                return;
            }
            if(passMoi.isEmpty()) {
                utils.Utils.showError("Thiếu", "Xin mời nhập mật khẩu mới!");
                return;
            }
            if(passXacNhan.isEmpty()) {
                utils.Utils.showError("Thiếu", "Xin mời nhập xác nhận mật khẩu mới!");
                return;
            }
            if (!passCu.equals(DB.password)) {
                utils.Utils.showError("Sai mật khẩu", "Mật khẩu cũ không đúng, xin mời nhập lại!");
                tfPassCu.setText("");
                return;
            }
            if (!passMoi.equals(passXacNhan)) {
                utils.Utils.showError("Không trùng khớp", "Xác nhận mật khẩu không trùng khớp, vui lòng nhập lại!");
                tfPassXacNhan.setText("");
                return;
            }
            
            // thuc hien doi mat khau
            String sqlRole = "alter role " + DB.user + " password '" + passXacNhan + "';";
            String sql = "update taikhoan set password = '"
                    + passXacNhan
                    + "' where username = '"
                    + DB.user
                    +"';";
            boolean ok1 = DB.update(sqlRole);
            if (ok1) {
                DB.password = passXacNhan;
            }
            boolean ok2 = DB.update(sql);
            if (ok1 && ok2) {
                tfPassCu.setText("");
                tfPassMoi.setText("");
                tfPassXacNhan.setText("");
                utils.Utils.showSuccess("Thành công", "Đổi mật khẩu thành công!\nBây giờ tài khoản của bạn đã an toàn hơn!");
            } else {
                utils.Utils.showError("Lỗi", "Đã xảy ra lỗi!");
                return;
            }
        });
    }    

    
    
    private void loadInfo() {
        lbUsername.setText(DB.user);
        tk = TaiKhoan.querySelectOne(DB.user);
        if (tk == null) return;
        lbLoaiTk.setText(LoaiTk.querySelectTenByMa(tk.getMaLoaiTk()));
        tfFirstName.setText(tk.getFirstName());
        tfLastName.setText(tk.getLastName());
        cbGioiTinh.getSelectionModel().select(tk.getGioiTinh());
        tfCmnd.setText(tk.getCmnd());
        tfDiaChi.setText(tk.getDiaChi());
        tfEmail.setText(tk.getEmail());
    }
    
}
