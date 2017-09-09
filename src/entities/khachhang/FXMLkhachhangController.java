/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.khachhang;

import database.DB;
import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Admin
 */
public class FXMLkhachhangController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<KhachHang> items;
    
    TableColumn<KhachHang, String> maKhColumn;
    TableColumn<KhachHang, String> firstNameColumn;
    TableColumn<KhachHang, String> lastNameColumn;
    TableColumn<KhachHang, String> gioiTinhColumn;
    TableColumn<KhachHang, String> sdtColumn;
    TableColumn<KhachHang, String> cmndColumn;
    TableColumn<KhachHang, String> diaChiColumn;
    TableColumn<KhachHang, String> quocTichColumn;
    TableColumn<KhachHang, String> emailColumn;
    
    @FXML
    private Button themButton;
    @FXML
    private Button xoaButton;
    @FXML
    private Button refreshButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        Platform.runLater(() -> {
            initUIs();
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
            LoadingDialog.execute(() -> { 
                ResultSet rs = DB.select("select max(makh) as max from khachhang;");
                int maxMaKh = 0;
                try {
                    if (rs.next()) {
                        maxMaKh = rs.getInt("max");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLkhachhangController.class.getName()).log(Level.SEVERE, null, ex);
                }
                maxMaKh++;
                boolean ok = DB.insert("insert into khachhang(makh) values ('" + String.valueOf(maxMaKh) + "');");
                if (ok) {
                    initItems();
                }
            });
        });
        
        refreshButton.setOnAction((event) -> {
            initItems();
        });
        
        xoaButton.setOnAction((event) -> {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                KhachHang kh = (KhachHang) tableView.getItems().get(index);
                int maKh = kh.getMaKh();
                boolean ok = KhachHang.queryDeleteOne(maKh);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initMaKhColumn();
        initFirstNameColumn();
        initLastNameColumn();
        initGioiTinhColumn();
        initSdtColumn();
        initCmndColumn();
        initDiaChiColumn();
        initQuocTichColumn();
        initEmailColumn();
    }

    private void initMaKhColumn() {
        maKhColumn = new TableColumn<>("Mã KH");
        maKhColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            int maKh = kh.getMaKh();
            return new SimpleStringProperty(String.valueOf(maKh));
        });
        maKhColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maKhColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch(NumberFormatException e) {
                tableView.refresh();
                return;
            }
        
            String sql = "update khachhang set makh = '"
                    + String.valueOf(newValue)
                    + "' where makh = '"
                    + String.valueOf(kh.getMaKh())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setMaKh(newValue);
            } else {
                tableView.refresh();
            }
        });
        tableView.getColumns().add(maKhColumn);
    }
    
    private void initFirstNameColumn() {
        firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String firstName = kh.getFirstName();
            return new SimpleStringProperty(firstName);
        });
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set firstname = '"
                    + newValue
                    + "' where makh = '"
                    + String.valueOf(kh.getMaKh())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setFirstName(newValue);
            } else {
                tableView.refresh();
            }
        });
        firstNameColumn.setPrefWidth(150);
        tableView.getColumns().add(firstNameColumn);
    }
    
    private void initLastNameColumn() {
        lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String lastName = kh.getLastName();
            return new SimpleStringProperty(lastName);
        });
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set lastname = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setLastName(newValue);
            } else {
                tableView.refresh();
            }
        });
        lastNameColumn.setPrefWidth(150);
        tableView.getColumns().add(lastNameColumn);
    }
    
    private void initGioiTinhColumn() {
        gioiTinhColumn = new TableColumn<>("Giới Tính");
        gioiTinhColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String gioiTinh = kh.getGioitinh();
            return new SimpleStringProperty(gioiTinh);
        });
        ArrayList<String> listGioiTinh = new ArrayList<>();
        listGioiTinh.add("Nam");
        listGioiTinh.add("Nữ");
        listGioiTinh.add("Không xác định");
        ObservableList<String> olGioiTinh = FXCollections.observableList(listGioiTinh);
        gioiTinhColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olGioiTinh));
        gioiTinhColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set gioitinh = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setGioitinh(newValue);
            } else {
                tableView.refresh();
            }
        });
        gioiTinhColumn.setPrefWidth(150);
        tableView.getColumns().add(gioiTinhColumn);
    }
    
    private void initSdtColumn() {
        sdtColumn = new TableColumn<>("Số điện thoại");
        sdtColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String sdt = kh.getSdt();
            return new SimpleStringProperty(sdt);
        });
        sdtColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sdtColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set sdt = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setSdt(newValue);
            } else {
                tableView.refresh();
            }
        });
        sdtColumn.setPrefWidth(150);
        tableView.getColumns().add(sdtColumn);
    }
    
    private void initCmndColumn() {
        cmndColumn = new TableColumn<>("Số CMND");
        cmndColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String cmnd = kh.getCmnd();
            return new SimpleStringProperty(cmnd);
        });
        cmndColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        cmndColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set cmnd = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setCmnd(newValue);
            } else {
                tableView.refresh();
            }
        });
        cmndColumn.setPrefWidth(150);
        tableView.getColumns().add(cmndColumn);
    }
    
    private void initDiaChiColumn() {
        diaChiColumn = new TableColumn<>("Địa Chỉ");
        diaChiColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String diaChi = kh.getDiaChi();
            return new SimpleStringProperty(diaChi);
        });
        diaChiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        diaChiColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set diachi = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setDiaChi(newValue);
            } else {
                tableView.refresh();
            }
        });
        diaChiColumn.setPrefWidth(200);
        tableView.getColumns().add(diaChiColumn);
    }
    
    private void initQuocTichColumn() {
        quocTichColumn = new TableColumn<>("Quốc Tịch");
        quocTichColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String quocTich = kh.getQuocTich();
            return new SimpleStringProperty(quocTich);
        });
        quocTichColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quocTichColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set quoctich = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setQuocTich(newValue);
            } else {
                tableView.refresh();
            }
        });
        quocTichColumn.setPrefWidth(200);
        tableView.getColumns().add(quocTichColumn);
    }
    
    private void initEmailColumn() {
        emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String email = kh.getEmail();
            return new SimpleStringProperty(email);
        });
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit((TableColumn.CellEditEvent<KhachHang, String> event) -> {
            int row = event.getTablePosition().getRow();
            KhachHang kh = items.get(row);
            String newValue = event.getNewValue();
        
            String sql = "update khachhang set email = '"
                    + newValue
                    + "' where makh = '"
                    + kh.getMaKh()
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                kh.setEmail(newValue);
            } else {
                tableView.refresh();
            }
        });
        emailColumn.setPrefWidth(200);
        tableView.getColumns().add(emailColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(KhachHang.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
