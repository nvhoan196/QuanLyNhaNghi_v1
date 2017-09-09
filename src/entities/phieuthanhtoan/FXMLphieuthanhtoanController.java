/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phieuthanhtoan;

import entities.phieudangky.*;
import database.DB;
import entities.khachhang.KhachHang;
import entities.phong.Phong;
import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
public class FXMLphieuthanhtoanController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<PhieuThanhToan> items;
    
    ArrayList<PhieuDangKy> listPdk;
    
    DecimalFormat dformat = new DecimalFormat("##0.0");
    
    TableColumn<PhieuThanhToan, String> maPttColumn;
    TableColumn<PhieuThanhToan, String> ngayTtColumn;
    TableColumn<PhieuThanhToan, String> maPdkColumn;
    TableColumn<PhieuThanhToan, String> tienPhongColumn;
    TableColumn<PhieuThanhToan, String> tienDichVuColumn;
    TableColumn<PhieuThanhToan, String> tongTienColumn;
    
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
            initListPdk();
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
                ResultSet rs = DB.select("select max(maptt) as max from phieuthanhtoan;");
                int maxPtt = 0;
                try {
                    if (rs.next()) {
                        maxPtt = rs.getInt("max");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLphieuthanhtoanController.class.getName()).log(Level.SEVERE, null, ex);
                }
                maxPtt++;
                boolean ok = DB.insert("insert into phieuthanhtoan(maptt) values ('" + String.valueOf(maxPtt) + "');");
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
                PhieuThanhToan ptt = (PhieuThanhToan) tableView.getItems().get(index);
                int maPtt = ptt.getMaPtt();
                boolean ok = PhieuThanhToan.queryDeleteOne(maPtt);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initMaPttColumn();
        initNgayTtColumn();
        initMaPdkColumn();
        initTienPhongColumn();
        initTienDichVuColumn();
        initTongTienColumn();
    }

    void initMaPttColumn() {
        maPttColumn = new TableColumn<>("Mã PTT");
        maPttColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            int maPtt = ptt.getMaPtt();
            return new SimpleStringProperty(String.valueOf(maPtt));
        });
        maPttColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maPttColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieuthanhtoan set maptt = '"
                    + String.valueOf(newValue)
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setMaPdk(newValue);
            } else {
                tableView.refresh();
            }
        });
        maPttColumn.setPrefWidth(100);
        tableView.getColumns().add(maPttColumn);
    }
    
    Date checkDateFormat(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) return null;
        int ngay, thang, nam;
        Date d;
        try {
            ngay = Integer.parseInt(parts[0]);
            thang = Integer.parseInt(parts[1]);
            nam = Integer.parseInt(parts[2]);
            d = new Date(nam-1900, thang-1, ngay);
        } catch (Exception e) {
            return null;
        }
        return d;
    }
    
    void initNgayTtColumn() {
        ngayTtColumn = new TableColumn<>("Ngày Thanh Toán");
        ngayTtColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            Date ngayTt = ptt.getNgayTt();
            if (ngayTt == null) {
                return new SimpleStringProperty("");
            }
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            String date = f.format(ngayTt);
            
            return new SimpleStringProperty(date);
        });
        ngayTtColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ngayTtColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            String newValue = event.getNewValue();
            Date d = checkDateFormat(newValue);
            if (d == null) {
                tableView.refresh();
                return;
            }

            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            
            String sql = "update phieuthanhtoan set ngaytt = '"
                    + f.format(d)
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setNgayTt(d);
            } else {
                tableView.refresh();
            }
        });
        ngayTtColumn.setPrefWidth(200);
        tableView.getColumns().add(ngayTtColumn);
    }
    void initMaPdkColumn() {
        maPdkColumn = new TableColumn<>("Mã PĐK");
        maPdkColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            int maPdk = ptt.getMaPdk();
            for (PhieuDangKy pdk : listPdk) {
                if (pdk.getMaPdk() == maPdk) {
                    return new SimpleStringProperty(pdk.toString());
                }
            }
            return new SimpleStringProperty(String.valueOf(""));
        });
        
        ArrayList<String> alPdk = new ArrayList<>();
        for (PhieuDangKy pdk : listPdk) {
            alPdk.add(String.valueOf(pdk.toString()));
        }
        ObservableList<String> olPdk = FXCollections.observableList(alPdk);
        maPdkColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(olPdk)));
        maPdkColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            String newValue = event.getNewValue();
            
            int value = 0;
            for (PhieuDangKy pdk : listPdk) {
                if (pdk.toString().equals(newValue)) {
                    value = pdk.getMaPdk();
                }
            }
            
            
            String sql = "update phieuthanhtoan set mapdk = '"
                    + value
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setMaPdk(value);
            } else {
                tableView.refresh();
            }
        });
        maPdkColumn.setPrefWidth(100);
        tableView.getColumns().add(maPdkColumn);
    }

    void initTienPhongColumn() {
        tienPhongColumn = new TableColumn<>("Tiền Phòng (VNĐ)");
        tienPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            float tienPhong = ptt.getTienPhong();
            return new SimpleStringProperty(dformat.format(tienPhong));
        });
        tienPhongColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tienPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            String newValue = event.getNewValue();
            
            float value = Float.parseFloat(event.getOldValue());
            try {
                value = Float.parseFloat(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update phieuthanhtoan set tienphong = '"
                    + value
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setTienPhong(value);
            } else {
                tableView.refresh();
            }
        });
        tienPhongColumn.setPrefWidth(200);
        tableView.getColumns().add(tienPhongColumn);
    }

    void initTienDichVuColumn() {
        tienDichVuColumn = new TableColumn<>("Tiền Dịch Vụ (VNĐ)");
        tienDichVuColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            float tienDichVu = ptt.getTienDichVu();
            return new SimpleStringProperty(dformat.format(tienDichVu));
        });
        tienDichVuColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tienDichVuColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            String newValue = event.getNewValue();
            
            float value = Float.parseFloat(event.getOldValue());
            try {
                value = Float.parseFloat(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update phieuthanhtoan set tiendichvu = '"
                    + value
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setTienDichVu(value);
            } else {
                tableView.refresh();
            }
        });
        tienDichVuColumn.setPrefWidth(200);
        tableView.getColumns().add(tienDichVuColumn);
    }

    void initTongTienColumn() {
        tongTienColumn = new TableColumn<>("Tổng Tiền (VNĐ)");
        tongTienColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuThanhToan, String> param) -> {
            PhieuThanhToan ptt = param.getValue();
            float tongTien = ptt.getTongTien();
            return new SimpleStringProperty(dformat.format(tongTien));
        });
        tongTienColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tongTienColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuThanhToan, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuThanhToan ptt = items.get(row);
            String newValue = event.getNewValue();
            
            float value = Float.parseFloat(event.getOldValue());
            try {
                value = Float.parseFloat(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update phieuthanhtoan set tongtien = '"
                    + value
                    + "' where maptt = '"
                    + String.valueOf(ptt.getMaPtt())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                ptt.setTongTien(value);
            } else {
                tableView.refresh();
            }
        });
        tongTienColumn.setPrefWidth(200);
        tableView.getColumns().add(tongTienColumn);
    }
    
    void initListPdk() {
        listPdk = PhieuDangKy.querySelectAll();
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(PhieuThanhToan.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }

}
