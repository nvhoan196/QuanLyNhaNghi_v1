/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.taikhoan;

import database.DB;
import entities.loaitk.LoaiTk;
import fxmls.LoadingDialog;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Admin
 */

public class FXMLtaikhoanController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<TaiKhoan> items;
    private ArrayList<LoaiTk> loaiTkItems;
    
    TableColumn<TaiKhoan, String> usernameColumn;
    TableColumn<TaiKhoan, String> passwordColumn;
    TableColumn<TaiKhoan, String> maLoaiTkColumn;
    TableColumn<TaiKhoan, String> firstNameColumn;
    TableColumn<TaiKhoan, String> lastNameColumn;
    TableColumn<TaiKhoan, String> gioiTinhColumn;
    TableColumn<TaiKhoan, String> cmndColumn;
    TableColumn<TaiKhoan, String> diaChiColumn;
    TableColumn<TaiKhoan, String> emailColumn;
    
    @FXML
    private Button themButton;
    @FXML
    private Button xoaButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField tfNewUser;
    @FXML
    private TextField tfNewPass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        Platform.runLater(() -> {
            initUIs();
            loaiTkItems = LoaiTk.querySelectAll();
            addEvents();
            initColumns();
            initItems();
        });
    }    

    private void initUIs() {
        themButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/add.png"))));
        xoaButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/delete.png"))));
        refreshButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/refresh.png"))));
    }

    private void addEvents() {
        
        themButton.setOnAction((event) -> {
            if (tfNewUser.getText().isEmpty() || tfNewPass.getText().isEmpty()) {
                return;
            }
            LoadingDialog.execute(() -> {
                boolean ok = TaiKhoan.queryInsertOne(tfNewUser.getText(), tfNewPass.getText());
                if (ok) {
                    //items.add(new TaiKhoan("\"New User\"", "", -1, "", "", "", "", "", ""));
                    initItems();
                    tfNewUser.setText("");
                    tfNewPass.setText("");
                }
            });
        });
        
        refreshButton.setOnAction((event) -> {
            initItems();
        });
        
        xoaButton.setOnAction((event) -> {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                TaiKhoan tk = (TaiKhoan) tableView.getItems().get(index);
                String username = tk.getUsername();
                if (username.equals("postgres")) return;
                boolean ok = TaiKhoan.queryDeleteOne(username);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initUsernameColumn();
        initPasswordColumn();
        initMaLoaiTkColumn();
        initFirstNameColumn();
        initLastNameColumn();
        initGioiTinhColumn();
        initCmndColumn();
        initDiaChiColumn();
        initEmailColumn();
    }

    private void initUsernameColumn() {
        usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String username = tk.getUsername();
            return new SimpleStringProperty(username);
        });
//        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        usernameColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            TaiKhoan tk = items.get(row);
//            String newValue = event.getNewValue();
//            
//            String sql = "update taikhoan set username = '"
//                    + newValue
//                    + "' where username = '"
//                    + tk.getUsername()
//                    +"';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                tk.username = newValue;
//            } else {
//                tableView.refresh();
//            }
//        });
        usernameColumn.setPrefWidth(150);
        tableView.getColumns().add(usernameColumn);
    }
    
    private void initPasswordColumn() {
        passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String password = tk.getPassword();
            return new SimpleStringProperty(password);
        });
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sqlRole = "alter role " + tk.username + " password '" + newValue + "';";
            String sql = "update taikhoan set password = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok1 = DB.update(sqlRole);
            if (ok1 && DB.user.equals(tk.username)) {
                DB.password = newValue;
            }
            boolean ok2 = DB.update(sql);
            if (ok1 && ok2) {
                tk.password = newValue;
            } else {
                tableView.refresh();
            }
        });
        passwordColumn.setPrefWidth(150);
        tableView.getColumns().add(passwordColumn);
    }
    
    private void initMaLoaiTkColumn() {
        maLoaiTkColumn = new TableColumn<>("Loại Tài Khoản");
        maLoaiTkColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            int maLoaiTk = tk.getMaLoaiTk();
            for (LoaiTk ltk : loaiTkItems) {
                if (ltk.getMaLoaiTk() == maLoaiTk) {
                    return new SimpleStringProperty(ltk.getTenLoaiTk());
                }
            }
            return new SimpleStringProperty("");
        });
        ArrayList<String> tenLoaiTkList = new ArrayList<>();
        for (LoaiTk ltk : loaiTkItems) {
            tenLoaiTkList.add(ltk.getTenLoaiTk());
        }
        ObservableList<String> loaiTkComboBoxItems = FXCollections.observableArrayList(tenLoaiTkList);
        maLoaiTkColumn.setCellFactory(ComboBoxTableCell.forTableColumn(loaiTkComboBoxItems));
        maLoaiTkColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            int maloaiTk = LoaiTk.querySelectMaByTen(newValue);
            String sql = "update taikhoan set maloaitk = '" + maloaiTk + "' where username = '" + tk.getUsername() + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.setMaLoaiTk(maloaiTk);
            } else {
                tableView.refresh();
            }
        });
        maLoaiTkColumn.setPrefWidth(110);
        tableView.getColumns().add(maLoaiTkColumn);
    }
    
    private void initFirstNameColumn() {
        firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String firstName = tk.getFirstName();
            return new SimpleStringProperty(firstName);
        });
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set firstname = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.firstName = newValue;
            } else {
                tableView.refresh();
            }
        });
        firstNameColumn.setPrefWidth(200);
        tableView.getColumns().add(firstNameColumn);
    }
    
    private void initLastNameColumn() {
        lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String lastName = tk.getLastName();
            return new SimpleStringProperty(lastName);
        });
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set lastname = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.lastName = newValue;
            } else {
                tableView.refresh();
            }
        });
        lastNameColumn.setPrefWidth(200);
        tableView.getColumns().add(lastNameColumn);
    }
    
    private void initGioiTinhColumn() {
        gioiTinhColumn = new TableColumn<>("Giới Tính");
        gioiTinhColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String gioiTinh = tk.getGioiTinh();
            return new SimpleStringProperty(gioiTinh);
        });
        ArrayList<String> gioiTinhList = new ArrayList<>();
        gioiTinhList.add("Nam");
        gioiTinhList.add("Nữ");
        gioiTinhList.add("Không xác định");
        gioiTinhColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(gioiTinhList)));
        gioiTinhColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set gioitinh = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.gioiTinh = newValue;
            } else {
                tableView.refresh();
            }
        });
        gioiTinhColumn.setPrefWidth(150);
        tableView.getColumns().add(gioiTinhColumn);
    }
    
    private void initCmndColumn() {
        cmndColumn = new TableColumn<>("Số CMND");
        cmndColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String cmnd = tk.getCmnd();
            return new SimpleStringProperty(cmnd);
        });
        cmndColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cmndColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set cmnd = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.cmnd = newValue;
            } else {
                tableView.refresh();
            }
        });
        cmndColumn.setPrefWidth(100);
        tableView.getColumns().add(cmndColumn);
    }
    
    private void initDiaChiColumn() {
        diaChiColumn = new TableColumn<>("Địa Chỉ");
        diaChiColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String diaChi = tk.getDiaChi();
            return new SimpleStringProperty(diaChi);
        });
        diaChiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        diaChiColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set diachi = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.diaChi = newValue;
            } else {
                tableView.refresh();
            }
        });
        diaChiColumn.setPrefWidth(200);
        tableView.getColumns().add(diaChiColumn);
    }
    
    private void initEmailColumn() {
        emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory((TableColumn.CellDataFeatures<TaiKhoan, String> param) -> {
            TaiKhoan tk = param.getValue();
            String email = tk.getEmail();
            return new SimpleStringProperty(email);
        });
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit((TableColumn.CellEditEvent<TaiKhoan, String> event) -> {
            int row = event.getTablePosition().getRow();
            TaiKhoan tk = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update taikhoan set email = '"
                    + newValue
                    + "' where username = '"
                    + tk.getUsername()
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tk.email = newValue;
            } else {
                tableView.refresh();
            }
        });
        emailColumn.setPrefWidth(200);
        tableView.getColumns().add(emailColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            loaiTkItems = LoaiTk.querySelectAll();
            items = FXCollections.observableArrayList(TaiKhoan.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
