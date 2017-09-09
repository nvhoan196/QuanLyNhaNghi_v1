/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.thietbi;

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
public class FXMLthietbiController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<ThietBi> items;
    
    TableColumn<ThietBi, String> maTbColumn;
    TableColumn<ThietBi, String> tenTbColumn;
    
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
                boolean ok = ThietBi.queryInsertOne();
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
                ThietBi tb = (ThietBi) tableView.getItems().get(index);
                int maTb = tb.getMaTb();
                boolean ok = ThietBi.queryDeleteOne(maTb);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
    }

    private void initColumns() {
        initMaTbColumn();
        initTenTbColumn();
    }

    private void initMaTbColumn() {
        maTbColumn = new TableColumn<>("Mã Thiết Bị");
        maTbColumn.setCellValueFactory((TableColumn.CellDataFeatures<ThietBi, String> param) -> {
            ThietBi tb = param.getValue();
            int maTb = tb.getMaTb();
            return new SimpleStringProperty(String.valueOf(maTb));
        });
        maTbColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maTbColumn.setOnEditCommit((TableColumn.CellEditEvent<ThietBi, String> event) -> {
            int row = event.getTablePosition().getRow();
            ThietBi tb = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch(NumberFormatException e) {
                tableView.refresh();
                return;
            }
            
            String sql = "update thietbi set matb = '"
                    + String.valueOf(newValue)
                    + "' where matb = '"
                    + String.valueOf(tb.getMaTb())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                tb.maTb = newValue;
            } else {
                tableView.refresh();
            }
        });
        maTbColumn.setPrefWidth(200);
        tableView.getColumns().add(maTbColumn);
    }
    
    private void initTenTbColumn() {
        tenTbColumn = new TableColumn<>("Tên Thiết Bị");
        tenTbColumn.setCellValueFactory((TableColumn.CellDataFeatures<ThietBi, String> param) -> {
            ThietBi tb = param.getValue();
            String tenTb = tb.getTenTb();
            return new SimpleStringProperty(tenTb);
        });
        tenTbColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tenTbColumn.setOnEditCommit((TableColumn.CellEditEvent<ThietBi, String> event) -> {
            int row = event.getTablePosition().getRow();
            ThietBi tb = items.get(row);
            String newValue = event.getNewValue();
            String sql = "update thietbi set tentb = '"
                    + newValue
                    + "' where matb = '"
                    + String.valueOf(tb.maTb)
                    +"';";
            boolean ok = DB.update(sql);
            if (ok) {
                tb.setTenTb(newValue);
            } else {
                tableView.refresh();
            }
        });
        tenTbColumn.setPrefWidth(200);
        tableView.getColumns().add(tenTbColumn);
    }
   
    private void initItems() {
        LoadingDialog.execute(() -> {
            items = FXCollections.observableArrayList(ThietBi.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
}
