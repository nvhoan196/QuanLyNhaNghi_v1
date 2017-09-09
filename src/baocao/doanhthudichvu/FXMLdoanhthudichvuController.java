/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.doanhthudichvu;

import database.DB;
import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
public class FXMLdoanhthudichvuController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initColumns();
        addEventClickBtnBaoCao();
        initData();
    }    
    
    DecimalFormat decimalF = new DecimalFormat("##0.0");
    
    @FXML private TextField tfTuNgay;
    @FXML private TextField tfDenNgay;
    @FXML private Button btnBaoCao;
    @FXML private Label lbTongDoanhThuDv;
    @FXML private Label lbDoanhThuTrungBinhNgay;
    @FXML private Label lbSoNgay;
    
    @FXML private TableView<DoanhThuDichVu> tv;
    ObservableList<DoanhThuDichVu> items;
    
    long soNgay=1;
    @FXML private TableColumn<DoanhThuDichVu, String> maDvColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> dichVuColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> giaDvColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> soLuongColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> soLanColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> tongTienColumn;
    @FXML private TableColumn<DoanhThuDichVu, String> doanhThuTbNgayColumn;
    
    private void initColumns() {
        maDvColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(dtdv.getMaDv()+"");
            }
        });
        
        dichVuColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(dtdv.getDichVu());
            }
        });
        
        giaDvColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(decimalF.format(dtdv.getGiaDichVu()));
            }
        });
        
        soLanColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(dtdv.getSoLanDuocDat()+"");
            }
        });
        
        soLuongColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(dtdv.getSoLuongDuocDat()+"");
            }
        });
        
        tongTienColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(decimalF.format(dtdv.getTongTienThuDuoc()));
            }
        });
        
        doanhThuTbNgayColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DoanhThuDichVu, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DoanhThuDichVu, String> param) {
                DoanhThuDichVu dtdv = param.getValue();
                return new SimpleStringProperty(decimalF.format(dtdv.getTongTienThuDuoc()/soNgay));
            }
        });
    }

    public void addEventClickBtnBaoCao() {
        btnBaoCao.setOnAction((event) -> {
            baoCao();
        });
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
    
    public LocalDate getLDate(String date) {
        String[] parts = date.split("-");
        try {
            if (parts.length == 3) {
                int ngay = Integer.parseInt(parts[2]);
                int thang = Integer.parseInt(parts[1]);
                int nam = Integer.parseInt(parts[0]);
                Date d = new Date(nam - 1900, thang - 1, ngay);
                return d.toLocalDate();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    public void baoCao() {
        if (tfTuNgay.getText().isEmpty() && tfDenNgay.getText().isEmpty()) {
            initData();
            return;
        }
        String begin = checkDateFormat(tfTuNgay.getText());
        String end = checkDateFormat(tfDenNgay.getText());
        if (begin.isEmpty() || end.isEmpty()) {
            utils.Utils.showError("Lỗi định dạng", "Ngày tháng năm phải có dạng: dd/MM/yyyy !");
            return;
        }
        initData(begin, end);
    }
    
    public void initData() {
        LoadingDialog.execute(() -> {
            Date minDate = DoanhThuDichVu.queryMinDate();
            Date maxDate = DoanhThuDichVu.queryMaxDate();
            soNgay = ChronoUnit.DAYS.between(minDate.toLocalDate(), maxDate.toLocalDate()) + 1;
            float tongDoanhThu = DoanhThuDichVu.queryTongDoanhThuDichVu();
            float doanhThuTbNgay = tongDoanhThu / (float) soNgay;
            items = FXCollections.observableList(DoanhThuDichVu.querySelectAll());
            Platform.runLater(() -> {
                lbSoNgay.setText(soNgay + "");
                lbTongDoanhThuDv.setText(decimalF.format(tongDoanhThu));
                lbDoanhThuTrungBinhNgay.setText(decimalF.format(doanhThuTbNgay));
                tv.setItems(items);
            });
        });
    }

    public void initData(String tuNgay, String denNgay) {

        LoadingDialog.execute(() -> {
            LocalDate begin = getLDate(tuNgay);
            LocalDate end = getLDate(denNgay);
            soNgay = ChronoUnit.DAYS.between(begin, end) + 1;
            float tongDoanhThu = DoanhThuDichVu.queryTongDoanhThuDichVu(tuNgay, denNgay);
            float doanhThuTbNgay = tongDoanhThu / (float) soNgay;
            items = FXCollections.observableList(DoanhThuDichVu.querySelectAll(tuNgay, denNgay));
            Platform.runLater(() -> {
                lbSoNgay.setText(soNgay + "");
                lbTongDoanhThuDv.setText(decimalF.format(tongDoanhThu));
                lbDoanhThuTrungBinhNgay.setText(decimalF.format(doanhThuTbNgay));
                tv.setItems(items);
            });
        });
    }
}
