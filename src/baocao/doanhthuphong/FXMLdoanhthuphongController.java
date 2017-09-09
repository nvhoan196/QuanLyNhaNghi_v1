/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.doanhthuphong;

import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FXMLdoanhthuphongController implements Initializable {

    DecimalFormat decimalF = new DecimalFormat("##0.0");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initColumns();
        LoadingDialog.execute(() -> {
            items = FXCollections.observableList(DoanhThuPhong.queryDoanhThuPhong());
            Platform.runLater(() -> {
                
                tv.setItems(items);
                updateTong();
            });
        });
        
        btnBaoCaoPhong.setOnAction((event) -> {
            if (tfTuNgay.getText().isEmpty() && tfDenNgay.getText().isEmpty()) {
                items = FXCollections.observableList(DoanhThuPhong.queryDoanhThuPhong());
                tv.setItems(items);
                updateTong();
                LoadingDialog.execute(() -> {
                    items = FXCollections.observableList(DoanhThuPhong.queryDoanhThuPhong());
                    Platform.runLater(() -> {
                        
                        tv.setItems(items);
                        updateTong();
                    });
                });
            } else {
                String begin = checkDateFormat(tfTuNgay.getText());
                String end = checkDateFormat(tfDenNgay.getText());
                if (begin.isEmpty() || end.isEmpty()) {
                    utils.Utils.showError("Lỗi định dạng", "Ngày tháng năm phải có dạng: dd/MM/yyyy !");
                    return;
                }
                LoadingDialog.execute(() -> {
                    items = FXCollections.observableList(DoanhThuPhong.queryDoanhThuPhong(begin, end));
                    Platform.runLater(() -> {
                        
                        tv.setItems(items);
                        updateTong(begin, end);
                    });
                });
            }
        });
    }  
    
    public void updateTong() {
        lbTongTienPhong.setText(decimalF.format(DoanhThuPhong.queryTongTienPhong()));
        lbTongTienDichVu.setText(decimalF.format(DoanhThuPhong.queryTongTienDichVu()));
        lbTongDoanhThu.setText(decimalF.format(DoanhThuPhong.queryTongTienPhong()+DoanhThuPhong.queryTongTienDichVu()));
    }
    
    public void updateTong(String tuNgay, String denNgay) {
        lbTongTienPhong.setText(decimalF.format(DoanhThuPhong.queryTongTienPhong(tuNgay, denNgay)));
        lbTongTienDichVu.setText(decimalF.format(DoanhThuPhong.queryTongTienDichVu(tuNgay, denNgay)));
        lbTongDoanhThu.setText(decimalF.format(DoanhThuPhong.queryTongTienPhong(tuNgay, denNgay)+DoanhThuPhong.queryTongTienDichVu(tuNgay, denNgay)));
    }
    
    public String checkDateFormat(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) return "";
        try {
            int ngay = Integer.parseInt(parts[0]);
            int thang = Integer.parseInt(parts[1]);
            int nam = Integer.parseInt(parts[2]);
            return nam+"-"+thang+"-"+ngay;
        } catch (Exception e) {
            return "";
        }
    }
    
    public LocalDate getLocalDate(String date) {
        String[] parts = date.split("/");
        try {
            int ngay = Integer.parseInt(parts[0]);
            int thang = Integer.parseInt(parts[1]);
            int nam = Integer.parseInt(parts[2]);
            Date  d = new Date(nam-1900, thang-1, ngay);
            return d.toLocalDate();
        } catch (Exception e) {
            return null;
        }
    }
    
    ////////////// CAC CONTROLS
    @FXML private TextField tfTuNgay;
    @FXML private TextField tfDenNgay;
    @FXML private Button btnBaoCaoPhong;
    
    @FXML Label lbTongTienPhong;
    @FXML Label lbTongTienDichVu;
    @FXML Label lbTongDoanhThu;
    
    @FXML TableView<DoanhThuPhong> tv;
    ObservableList<DoanhThuPhong> items;
    
    @FXML TableColumn<DoanhThuPhong, String> soPhongColumn;
    @FXML TableColumn<DoanhThuPhong, String> tongPdkColumn;
    @FXML TableColumn<DoanhThuPhong, String> tongPttColumn;
    @FXML TableColumn<DoanhThuPhong, String> tongTienPhongColumn;
    @FXML TableColumn<DoanhThuPhong, String> tongTienDvColumn;
    @FXML TableColumn<DoanhThuPhong, String> tongDoanhThuColumn;

    private void initColumns() {
        soPhongColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                return new SimpleStringProperty(p.getSoPhong()+"");
            }
        });
        
        tongPdkColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                    return new SimpleStringProperty(p.getTongPdk()+"");
                
            }
        });
        
        tongPttColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                return new SimpleStringProperty(p.getTongPtt()+"");
            }
        });
        tongTienPhongColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                return new SimpleStringProperty(decimalF.format(p.getTongTienPhong()));
            }
        });
        tongTienDvColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                return new SimpleStringProperty(decimalF.format(p.getTongTienDv()));
            }
        });
        tongDoanhThuColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuPhong, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuPhong, String> param) {
                DoanhThuPhong p = param.getValue();
                return new SimpleStringProperty(decimalF.format(p.getTongDoanhThuPhong()));
            }
        });
    }
    
    
    
}
