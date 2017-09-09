/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmls;

import database.DB;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import entities.taikhoan.TaiKhoan;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FXMLDangNhapController implements Initializable {

    public Scene scene = null;
    public Stage stage = null;
    
    @FXML
    private VBox root;
    
    @FXML
    private VBox vboxNangCao;
    
    @FXML
    private CheckBox cbNangCao;
    
    @FXML
    private TextField tfUsername;
    
    @FXML
    private PasswordField tfPassword;
    
    @FXML
    private TextField tfIp;
    
    @FXML
    private TextField tfPort;
    
    @FXML
    private TextField tfDbName;
    
    @FXML
    private Button btnDangNhap;
    
    @FXML
    private Button btnHuyBo;
    
    public Model model = new Model();
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        bindings();
        setupUIs();
    }

    private void setupUIs() {
        vboxNangCao.setVisible(false);
        cbNangCao.selectedProperty().bindBidirectional(vboxNangCao.visibleProperty());
        cbNangCao.setSelected(false);
        
        Platform.runLater(() -> {
            //tfPassword.requestFocus();
            if (stage != null) {
                stage.setMaximized(true);
            }
        });
    }
    
    class Model {
        public StringProperty userProperty = new SimpleStringProperty();
        public StringProperty passProperty = new SimpleStringProperty();
        public BooleanProperty isChecked = new SimpleBooleanProperty();
        
        public StringProperty ipProperty = new SimpleStringProperty();
        public StringProperty portProperty = new SimpleStringProperty();
        public StringProperty dbNameProperty = new SimpleStringProperty();
    }
    
    void bindings() {
        model.userProperty.bindBidirectional(tfUsername.textProperty());
        model.passProperty.bindBidirectional(tfPassword.textProperty());
        model.isChecked.bindBidirectional(cbNangCao.selectedProperty());
        model.ipProperty.bindBidirectional(tfIp.textProperty());
        model.portProperty.bindBidirectional(tfPort.textProperty());
        model.dbNameProperty.bindBidirectional(tfDbName.textProperty());
    }
    
    @FXML
    public void dangNhap(ActionEvent e) throws IOException {
        LoadingDialog lf = new LoadingDialog();
        DB.setInfo(model.dbNameProperty.get(),
                model.userProperty.get(),
                model.passProperty.get(),
                model.ipProperty.get(),
                model.portProperty.get());
        lf.addRunnable(() -> {
            DB.connect();
        });
        lf.run(true);
        
        model.passProperty.set("");
        if (DB.isConnected()) {
            showSuccess();
            lf = new LoadingDialog();
            Runnable run = () -> {
                Platform.runLater(() -> {
                    stage.hide();
                    try {
                        showMainForm();
                    } catch (IOException ex) {}
                });
            };
            
            lf.addRunnable(run);
            lf.run(true);
            
              
        } else {
            showError();
        }
        DB.closeConnect();
    }
    
    private void showMainForm() throws IOException{
        Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/fxmls/FXMLMainForm.fxml"));
            BorderPane root = loader.load();
            newStage.setScene(new Scene(root));
            FXMLMainFormController controller = loader.getController();
            TaiKhoan tk = TaiKhoan.querySelectOne(model.userProperty.get());
            tk.toString();
            if (tk != null) {
                controller.taiKhoan = tk;
            } else {
                controller.taiKhoan = new TaiKhoan("", "", 2, "Chưa rõ", "Chưa rõ", "Chưa rõ", "Chưa rõ", "Chưa rõ", "Chưa rõ");
            }
            
            newStage.setMaximized(true);
            newStage.show();
            newStage.setTitle("Quản Lý Khách Sạn - Professional");
            stage.close();
    }
    
    @FXML
    public void huyBo(ActionEvent e) {
        stage.close();
    }
    
    void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText("Đăng nhập thất bại!\nXin hãy thử lại!");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setContentText("Đăng nhập thành công!\nXin chúc mừng!");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
}
