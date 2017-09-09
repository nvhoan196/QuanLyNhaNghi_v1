/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.loaitk;

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
 * FXML Controller class
 *
 * @author Admin
 */

public class FXMLloaitkController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<LoaiTk> items;
    
    TableColumn<LoaiTk, String> maLoaiTkColumn;
    TableColumn<LoaiTk, String> tenLoaiTkColumn;
    
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
            initUis();
            addEvents();
            initColumns();
            initItems();
        });
    }    

    private void initUis() {
        themButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/add.png"))));
        xoaButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/delete.png"))));
        refreshButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/refresh.png"))));
    }
    
    private void addEvents() {
        themButton.setOnAction((event) -> {
            LoaiTk tk = new LoaiTk();
            boolean ok = LoaiTk.queryInsertOne();
            if (ok = false) return;
            initItems();
        });
        xoaButton.setOnAction((event) -> {
            LoadingDialog.execute(() -> {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if (index >=0 ) {
                    LoaiTk loaiTk = (LoaiTk) tableView.getItems().get(index);
                    boolean ok = LoaiTk.queryDeleteOne(loaiTk.getMaLoaiTk());
                    if (ok) {
                        items.remove(tableView.getSelectionModel().getSelectedIndex());
                    }  
                }
            });
        });
        refreshButton.setOnAction((event) -> {
            initItems();
        });
    }
    
    private void initColumns() {
        initMaLoaiTkColumn();
        initTenLoaiTkColumn();
    }
    
    private void initMaLoaiTkColumn() {
        maLoaiTkColumn = new TableColumn<>("Mã Loại TK");
        maLoaiTkColumn.setCellValueFactory((TableColumn.CellDataFeatures<LoaiTk, String> param) -> {
            LoaiTk ltk = param.getValue();
            String mltk = String.valueOf(ltk.getMaLoaiTk());
            return new SimpleStringProperty(mltk);
        });
        maLoaiTkColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maLoaiTkColumn.setOnEditCommit((event) -> {
            int row = event.getTablePosition().getRow();
            LoaiTk ltkCu = items.get(row);
            int value = Integer.parseInt(event.getOldValue());
            try {
                value = Integer.parseInt(event.getNewValue());
                LoaiTk tkMoi = new LoaiTk(value, ltkCu.getTenLoaiTk());
                boolean ok = tkMoi.queryUpdate(ltkCu.getMaLoaiTk());
                if (ok) {
                    items.get(row).setMaLoaiTk(value);
                }
                tableView.refresh();
            } catch(NumberFormatException e) {
                //items.get(row).setMaLoaiTk(value);
                tableView.refresh();
            }
        });
        maLoaiTkColumn.setPrefWidth(100);
        tableView.getColumns().add(maLoaiTkColumn);
    }

    private void initTenLoaiTkColumn() {
        tenLoaiTkColumn = new TableColumn<>("Tên Loại TK");
        tenLoaiTkColumn.setCellValueFactory((TableColumn.CellDataFeatures<LoaiTk, String> param) -> {
            LoaiTk ltk = param.getValue();
            String mltk = ltk.getTenLoaiTk();
            return new SimpleStringProperty(mltk);
        });
        tenLoaiTkColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tenLoaiTkColumn.setOnEditCommit((event) -> {
            int row = event.getTablePosition().getRow();
            LoaiTk ltk = items.get(row);
            LoaiTk ltkMoi = new LoaiTk(ltk.getMaLoaiTk(), event.getNewValue());
            boolean ok = ltkMoi.queryUpdate(ltkMoi.getMaLoaiTk());
            if (ok) {
                items.get(row).setTenLoaiTk(event.getNewValue());
            }
        });
        tenLoaiTkColumn.setPrefWidth(100);
        tableView.getColumns().add(tenLoaiTkColumn);
    }

    private void initItems() {
        LoadingDialog.executeNonModal(() -> {
            items = FXCollections.observableArrayList(LoaiTk.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }
 
}
