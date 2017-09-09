/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.danhsachtb;

import entities.phong.*;
import database.DB;
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
public class FXMLdanhsachtbController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<DanhSachTb> items;
    private ArrayList<Phong> listPhong;
    private ArrayList<ThietBi> listThietBi;
    private ArrayList<String> listStringTb;
    
    TableColumn<DanhSachTb, String> soPhongColumn;
    TableColumn<DanhSachTb, String> matbColumn;
    TableColumn<DanhSachTb, String> idtbColumn;
    
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
            initListPhong();
            initListThietBi();
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
                boolean ok = DanhSachTb.queryInsertOne();
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
                DanhSachTb p = (DanhSachTb) tableView.getItems().get(index);
                int idtb = p.getIdTb();
                boolean ok = DanhSachTb.queryDeleteOne(idtb);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initIdtbColumn();
        initSoPhongColumn();
        initMatbPColumn();
        
    }

    private void initSoPhongColumn() {
        soPhongColumn = new TableColumn<>("Số Phòng");
        soPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<DanhSachTb, String> param) -> {
            DanhSachTb dstb = param.getValue();
            int soPhong = dstb.getSoPhong();
            return new SimpleStringProperty(String.valueOf(soPhong));
        });
        
        ArrayList<String> alSoPhong = new ArrayList<>();
        for (Phong p : listPhong) {
            alSoPhong.add(String.valueOf(p.getSoPhong()));
        }
        ObservableList<String> olSoPhong = FXCollections.observableList(alSoPhong);
        soPhongColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(olSoPhong)));
        soPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<DanhSachTb, String> event) -> {
            int row = event.getTablePosition().getRow();
            DanhSachTb dstb = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update danhsachtb set sophong = '"
                    + value
                    + "' where idtb = '"
                    + String.valueOf(dstb.getIdTb())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                dstb.setSoPhong(value);
            } else {
                tableView.refresh();
            }
        });
        soPhongColumn.setPrefWidth(100);
        tableView.getColumns().add(soPhongColumn);
    }
    
    private void initMatbPColumn() {
        matbColumn = new TableColumn<>("Mã thiết bị");
        matbColumn.setCellValueFactory((TableColumn.CellDataFeatures<DanhSachTb, String> param) -> {
            DanhSachTb dstb = param.getValue();
            int matb = dstb.getMaTb();
            for (ThietBi tb : listThietBi) {
                if (tb.getMaTb() == matb) {
                    return new SimpleStringProperty(tb.toString());
                }
            }
            return new SimpleStringProperty("");
        });
        ArrayList<String> alStringTb = new ArrayList<>();
        for (ThietBi tb : listThietBi) {
            alStringTb.add(tb.toString());
        }
        ObservableList<String> olStringTb = FXCollections.observableList(alStringTb);
        matbColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(olStringTb)));
        matbColumn.setOnEditCommit((TableColumn.CellEditEvent<DanhSachTb, String> event) -> {
            int row = event.getTablePosition().getRow();
            DanhSachTb dstb = items.get(row);
            String newValue = event.getNewValue();
            int matb = 0;
            for (ThietBi tb : listThietBi) {
                if (tb.toString().equals(newValue)) {
                    matb = tb.getMaTb();
                    System.out.println("OK FINE !");
                }
            }
            String sql = "update danhsachtb set matb = '"
                    + String.valueOf(matb)
                    + "' where idtb = '"
                    + String.valueOf(dstb.getIdTb())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                dstb.setMaTb(matb);
            } else {
                tableView.refresh();
            }
        });
        matbColumn.setPrefWidth(200);
        tableView.getColumns().add(matbColumn);
    }
    
    private void initIdtbColumn() {
        idtbColumn = new TableColumn<>("ID Thiết Bị");
        idtbColumn.setCellValueFactory((TableColumn.CellDataFeatures<DanhSachTb, String> param) -> {
            DanhSachTb dstb = param.getValue();
            int idtb = dstb.getIdTb();
            return new SimpleStringProperty(String.valueOf(idtb));
        });
        idtbColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idtbColumn.setOnEditCommit((TableColumn.CellEditEvent<DanhSachTb, String> event) -> {
            int row = event.getTablePosition().getRow();
            DanhSachTb dstb = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update danhsachtb set idtb = '"
                    + value
                    + "' where idtb = '"
                    + String.valueOf(dstb.getIdTb())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                dstb.setIdTb(value);
            } else {
                tableView.refresh();
            }
        });
        idtbColumn.setPrefWidth(100);
        tableView.getColumns().add(idtbColumn);
    }
    
    private void initListPhong() {
        listPhong = Phong.querySelectAll();
    }
    
    private void initListThietBi() {
        listThietBi = ThietBi.querySelectAll();
    }
    
    
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(DanhSachTb.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
