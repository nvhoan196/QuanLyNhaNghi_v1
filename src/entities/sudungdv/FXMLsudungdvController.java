/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.sudungdv;

import entities.danhsachtb.*;
import entities.phong.*;
import database.DB;
import entities.dichvu.DichVu;
import entities.phieudangky.PhieuDangKy;
import entities.thietbi.ThietBi;
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
public class FXMLsudungdvController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<SuDungDv> items;
    private ArrayList<PhieuDangKy> listPdk;
    private ArrayList<DichVu> listDv;
    
    TableColumn<SuDungDv, String> maPdkColumn;
    TableColumn<SuDungDv, String> maDvColumn;
    TableColumn<SuDungDv, String> idDvColumn;
    TableColumn<SuDungDv, String> soLuongColumn;
    
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
            initListDv();
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
                boolean ok = SuDungDv.queryInsertOne();
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
                SuDungDv sddv = (SuDungDv) tableView.getItems().get(index);
                int iddv = sddv.getIdDv();
                boolean ok = SuDungDv.queryDeleteOne(iddv);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initIdDvColumn();
        initMaPdkColumn();
        initMaDvPColumn();
        initSoLuongColumn();
    }

    private void initMaPdkColumn() {
        maPdkColumn = new TableColumn<>("Mã PĐK");
        maPdkColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int maPdk = sddv.getMaPdk();
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
        maPdkColumn.setOnEditCommit((TableColumn.CellEditEvent<SuDungDv, String> event) -> {
            int row = event.getTablePosition().getRow();
            SuDungDv sddv = items.get(row);
            String newValue = event.getNewValue();
            
            int value = 0;
            for (PhieuDangKy pdk : listPdk) {
                if (pdk.toString().equals(newValue)) {
                    value = pdk.getMaPdk();
                }
            }
            
            
            String sql = "update sudungdv set mapdk = '"
                    + value
                    + "' where iddv = '"
                    + String.valueOf(sddv.getIdDv())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                sddv.setMaPdk(value);
            } else {
                tableView.refresh();
            }
        });
        maPdkColumn.setPrefWidth(100);
        tableView.getColumns().add(maPdkColumn);
    }
    
    private void initMaDvPColumn() {
        maDvColumn = new TableColumn<>("Mã thiết bị");
        maDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int maDv = sddv.getMaDv();
            for (DichVu dv : listDv) {
                if (dv.getMaDv() == maDv) {
                    return new SimpleStringProperty(dv.toString());
                }
            }
            return new SimpleStringProperty("");
        });
        ArrayList<String> alDv = new ArrayList<>();
        for (DichVu dv : listDv) {
            alDv.add(dv.toString());
        }
        ObservableList<String> olDv = FXCollections.observableList(alDv);
        maDvColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(olDv)));
        maDvColumn.setOnEditCommit((TableColumn.CellEditEvent<SuDungDv, String> event) -> {
            int row = event.getTablePosition().getRow();
            SuDungDv sddv = items.get(row);
            String newValue = event.getNewValue();
            int maDv = 0;
            for (DichVu dv : listDv) {
                if (dv.toString().equals(newValue)) {
                    maDv = dv.getMaDv();
                }
            }
            String sql = "update sudungdv set madv = '"
                    + String.valueOf(maDv)
                    + "' where iddv = '"
                    + String.valueOf(sddv.getIdDv())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                sddv.setMaDv(maDv);
            } else {
                tableView.refresh();
            }
        });
        maDvColumn.setPrefWidth(200);
        tableView.getColumns().add(maDvColumn);
    }
    
    private void initIdDvColumn() {
        idDvColumn = new TableColumn<>("ID Dịch Vụ");
        idDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int iddv = sddv.getIdDv();
            return new SimpleStringProperty(String.valueOf(iddv));
        });
        idDvColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idDvColumn.setOnEditCommit((TableColumn.CellEditEvent<SuDungDv, String> event) -> {
            int row = event.getTablePosition().getRow();
            SuDungDv sddv = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update sudungdv set iddv = '"
                    + value
                    + "' where iddv = '"
                    + String.valueOf(sddv.getIdDv())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                sddv.setIdDv(value);
            } else {
                tableView.refresh();
            }
        });
        idDvColumn.setPrefWidth(100);
        tableView.getColumns().add(idDvColumn);
    }
    
    private void initSoLuongColumn() {
        soLuongColumn = new TableColumn<>("Số lượng");
        soLuongColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int soLuong = sddv.getSoLuong();
            return new SimpleStringProperty(String.valueOf(soLuong));
        });
        soLuongColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        soLuongColumn.setOnEditCommit((TableColumn.CellEditEvent<SuDungDv, String> event) -> {
            int row = event.getTablePosition().getRow();
            SuDungDv sddv = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update sudungdv set soluong = '"
                    + value
                    + "' where iddv = '"
                    + String.valueOf(sddv.getIdDv())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                sddv.setSoLuong(value);
            } else {
                tableView.refresh();
            }
        });
        soLuongColumn.setPrefWidth(100);
        tableView.getColumns().add(soLuongColumn);
    }
    
    private void initListPdk() {
        listPdk = PhieuDangKy.querySelectAll();
    }
    
    private void initListDv() {
        listDv = DichVu.querySelectAll();
    }
    
    
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(SuDungDv.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
