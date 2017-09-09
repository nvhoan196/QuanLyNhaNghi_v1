/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dichvu;

import database.DB;
import entities.thietbi.ThietBi;
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
public class FXMLdichvuController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<DichVu> items;
    
    TableColumn<DichVu, String> maDvColumn;
    TableColumn<DichVu, String> tenDvColumn;
    TableColumn<DichVu, String> giaDvColumn;
    
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
                boolean ok = DichVu.queryInsertOne();
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
                DichVu dv = (DichVu) tableView.getItems().get(index);
                int maDv = dv.getMaDv();
                boolean ok = DichVu.queryDeleteOne(maDv);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initMaTbColumn();
        initTenTbColumn();
        initGiaDvColumn();
    }

    private void initMaTbColumn() {
        maDvColumn = new TableColumn<>("Mã Dịch Vụ (VNĐ)");
        maDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            int maDv = dv.getMaDv();
            return new SimpleStringProperty(String.valueOf(maDv));
        });
        maDvColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maDvColumn.setOnEditCommit((TableColumn.CellEditEvent<DichVu, String> event) -> {
            int row = event.getTablePosition().getRow();
            DichVu dv = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch(NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update dichvu set madv = '"
                    + String.valueOf(newValue)
                    + "' where madv = '"
                    + String.valueOf(dv.getMaDv())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                dv.setMaDv(newValue);
            } else {
                tableView.refresh();
            }
        });
        maDvColumn.setPrefWidth(200);
        tableView.getColumns().add(maDvColumn);
    }
    
    private void initTenTbColumn() {
        tenDvColumn = new TableColumn<>("Tên Dịch Vụ");
        tenDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            String tenDv = dv.getTenDv();
            return new SimpleStringProperty(tenDv);
        });
        tenDvColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tenDvColumn.setOnEditCommit((TableColumn.CellEditEvent<DichVu, String> event) -> {
            int row = event.getTablePosition().getRow();
            DichVu dv = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update dichvu set tendv = '"
                    + newValue
                    + "' where madv = '"
                    + String.valueOf(dv.getMaDv())
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                dv.setTenDv(newValue);
            } else {
                tableView.refresh();
            }
        });
        tenDvColumn.setPrefWidth(200);
        tableView.getColumns().add(tenDvColumn);
    }
   
    private void initGiaDvColumn() {
        giaDvColumn = new TableColumn<>("Giá Dịch Vụ");
        giaDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            float giaDv = dv.getGiaDv();
            return new SimpleStringProperty(String.valueOf(giaDv));
        });
        giaDvColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        giaDvColumn.setOnEditCommit((TableColumn.CellEditEvent<DichVu, String> event) -> {
            int row = event.getTablePosition().getRow();
            DichVu dv = items.get(row);
            float newValue = Float.parseFloat(event.getOldValue());
            try {
                newValue = Float.parseFloat(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update dichvu set giadv = '"
                    + String.valueOf(newValue)
                    + "' where madv = '"
                    + String.valueOf(dv.getMaDv())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                dv.setGiaDv(newValue);
            } else {
                tableView.refresh();
            }
        });
        giaDvColumn.setPrefWidth(200);
        tableView.getColumns().add(giaDvColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(DichVu.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
