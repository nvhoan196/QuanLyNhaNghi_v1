/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.loaiphong;

import database.DB;
import fxmls.LoadingDialog;
import java.net.URL;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Admin
 */
public class FXMLloaiphongController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<LoaiPhong> items;
    
    TableColumn<LoaiPhong, String> maLoaiPColumn;
    TableColumn<LoaiPhong, String> tenLoaiPColumn;
    
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
                boolean ok = LoaiPhong.queryInsertOne();
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
                LoaiPhong lp = (LoaiPhong) tableView.getItems().get(index);
                int maLoaiP = lp.getMaLoaiP();
                boolean ok = LoaiPhong.queryDeleteOne(maLoaiP);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initMaLoaiPColumn();
        initTenLoaiPColumn();
    }

    private void initMaLoaiPColumn() {
        maLoaiPColumn = new TableColumn<>("Mã Loại Phòng");
        maLoaiPColumn.setCellValueFactory((TableColumn.CellDataFeatures<LoaiPhong, String> param) -> {
            LoaiPhong lp = param.getValue();
            int maLoaiP = lp.getMaLoaiP();
            return new SimpleStringProperty(String.valueOf(maLoaiP));
        });
        maLoaiPColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maLoaiPColumn.setOnEditCommit((TableColumn.CellEditEvent<LoaiPhong, String> event) -> {
            int row = event.getTablePosition().getRow();
            LoaiPhong lp = items.get(row);
            String newValue = event.getNewValue();
            
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update loaiphong set maloaip = '"
                    + value
                    + "' where maloaip = '"
                    + String.valueOf(lp.getMaLoaiP())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                lp.setMaLoaiP(value);
            } else {
                tableView.refresh();
            }
        });
        maLoaiPColumn.setPrefWidth(200);
        tableView.getColumns().add(maLoaiPColumn);
    }
    
    private void initTenLoaiPColumn() {
        tenLoaiPColumn = new TableColumn<>("Tên Loại Phòng");
        tenLoaiPColumn.setCellValueFactory((TableColumn.CellDataFeatures<LoaiPhong, String> param) -> {
            LoaiPhong lp = param.getValue();
            String tenLoaiP = lp.getTenLoaiP();
            return new SimpleStringProperty(tenLoaiP);
        });
        tenLoaiPColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tenLoaiPColumn.setOnEditCommit((TableColumn.CellEditEvent<LoaiPhong, String> event) -> {
            int row = event.getTablePosition().getRow();
            LoaiPhong lp = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update loaiphong set tenloaip = '"
                    + newValue
                    + "' where maloaip = '"
                    + String.valueOf(lp.getMaLoaiP())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                lp.tenLoaiP = newValue;
            } else {
                tableView.refresh();
            }
        });
        tenLoaiPColumn.setPrefWidth(200);
        tableView.getColumns().add(tenLoaiPColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(LoaiPhong.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
