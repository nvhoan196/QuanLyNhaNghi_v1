/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phong;

import database.DB;
import entities.loaiphong.LoaiPhong;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Admin
 */
public class FXMLphongController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<Phong> items;
    private ArrayList<LoaiPhong> listLoaiPhong;
    
    TableColumn<Phong, String> soPhongPColumn;
    TableColumn<Phong, String> maLoaiPColumn;
    TableColumn<Phong, String> giaPhongColumn;
    TableColumn<Phong, String> trangThaiColumn;
    
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
            listLoaiPhong = LoaiPhong.querySelectAll();
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
                boolean ok = Phong.queryInsertOne();
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
                Phong p = (Phong) tableView.getItems().get(index);
                int soPhong = p.getSoPhong();
                boolean ok = Phong.queryDeleteOne(soPhong);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initSoPhongColumn();
        initMaLoaiPColumn();
        initGiaPhongColumn();
        initTrangThaiColumn();
    }

    private void initSoPhongColumn() {
        soPhongPColumn = new TableColumn<>("Số Phòng");
        soPhongPColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            int soPhong = p.getSoPhong();
            return new SimpleStringProperty(String.valueOf(soPhong));
        });
        soPhongPColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        soPhongPColumn.setOnEditCommit((TableColumn.CellEditEvent<Phong, String> event) -> {
            int row = event.getTablePosition().getRow();
            Phong p = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update phong set sophong = '"
                    + value
                    + "' where sophong = '"
                    + String.valueOf(p.getSoPhong())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                p.setSoPhong(value);
            } else {
                tableView.refresh();
            }
        });
        tableView.getColumns().add(soPhongPColumn);
    }
    
    private void initMaLoaiPColumn() {
        maLoaiPColumn = new TableColumn<>("Loại Phòng");
        maLoaiPColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            int maLoaiP = p.getMaLoaiP();
            for (LoaiPhong lp : listLoaiPhong) {
                if (lp.getMaLoaiP() == maLoaiP) {
                    return new SimpleStringProperty(lp.getTenLoaiP());
                }
            }
            return new SimpleStringProperty("");
        });
        ArrayList<String> tenLoaiPList = new ArrayList<>();
        for (LoaiPhong lp : listLoaiPhong) {
            tenLoaiPList.add(lp.getTenLoaiP());
        }
        maLoaiPColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(tenLoaiPList)));
        maLoaiPColumn.setOnEditCommit((TableColumn.CellEditEvent<Phong, String> event) -> {
            int row = event.getTablePosition().getRow();
            Phong p = items.get(row);
            String newValue = event.getNewValue();
            int maLoaiP = LoaiPhong.queryMaLoaiPByTenLoaiP(newValue);
            String sql = "update phong set maloaip = '"
                    + String.valueOf(maLoaiP)
                    + "' where sophong = '"
                    + String.valueOf(p.getSoPhong())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                p.setMaLoaiP(maLoaiP);
            } else {
                tableView.refresh();
            }
        });
        maLoaiPColumn.setPrefWidth(200);
        tableView.getColumns().add(maLoaiPColumn);
    }
    
    private void initGiaPhongColumn() {
        giaPhongColumn = new TableColumn<>("Giá Phòng (VNĐ)");
        giaPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            float giaPhong = p.getGiaPhong();
            return new SimpleStringProperty(String.valueOf(giaPhong));
        });
        giaPhongColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        giaPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<Phong, String> event) -> {
            int row = event.getTablePosition().getRow();
            Phong p = items.get(row);
            String newValue = event.getNewValue();
            
            float value = Float.parseFloat(event.getOldValue());
            try {
                value = Float.parseFloat(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update phong set giaphong = '"
                    + value
                    + "' where sophong = '"
                    + String.valueOf(p.getSoPhong())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                p.setGiaPhong(value);
            } else {
                tableView.refresh();
            }
        });
        giaPhongColumn.setPrefWidth(200);
        tableView.getColumns().add(giaPhongColumn);
    }
    
    private void initTrangThaiColumn() {
        trangThaiColumn = new TableColumn<>("Trạng Thái");
        trangThaiColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            String trangThai = p.getTrangThai();
            return new SimpleStringProperty(trangThai);
        });
        ArrayList<String> trangThaiList = new ArrayList<>();
        trangThaiList.add("Đang trống");
        trangThaiList.add("Đã đặt phòng");
        trangThaiList.add("Đã nhận phòng");
        trangThaiColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(trangThaiList)));
        trangThaiColumn.setOnEditCommit((TableColumn.CellEditEvent<Phong, String> event) -> {
            int row = event.getTablePosition().getRow();
            Phong p = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update phong set trangthai = '"
                    + newValue
                    + "' where sophong = '"
                    + String.valueOf(p.getSoPhong())
                    +"';";
            System.out.println(sql);
            boolean ok = DB.update(sql);
            if (ok) {
                p.setTrangThai(newValue);
            } else {
                tableView.refresh();
            }
        });
        trangThaiColumn.setPrefWidth(150);
        tableView.getColumns().add(trangThaiColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(Phong.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
