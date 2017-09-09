/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.tonghop;

import fxmls.LoadingDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FXMLtonghopController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       init_tonghop();
       initColumns_phanPhoiTb();
       initColumns_chiTietTb();
       init_items_cbPhong();
       
    }  
    
    @FXML private Label lbTongThietBi;
    @FXML private Label lbTongKhachHang;
    @FXML private Label lbTongSoPhong;
    @FXML private Label lbTongSoPhongTrong;
    @FXML private Label lbTongSoPhongDangDuocDat;
    @FXML private Label lbTongSoPhongDangCoKhach;
    @FXML private Label lbPhongDatNhat;
    @FXML private Label lbKhachHangThanhToanNhieuNhat;
    @FXML private Label lbDichVuUaChuongNhat;
    @FXML private Label lbPhieuThanhToanTongTienCaoNhat;
    
    @FXML TableView<PhanPhoiTb> tv_phanPhoiTb;
    ObservableList<PhanPhoiTb> items_phanPhoiTb;
    
    @FXML TableColumn<PhanPhoiTb, String> soPhongColumn;
    @FXML TableColumn<PhanPhoiTb, String> soThietBiHienTaiColumn;
    
    public void initColumns_phanPhoiTb() {
        soPhongColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PhanPhoiTb, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PhanPhoiTb, String> param) {
                PhanPhoiTb pp = param.getValue();
                if (pp.getSoPhong() == 0) {
                    return new SimpleStringProperty("Tồn kho");
                }
                return new SimpleStringProperty(pp.getSoPhong()+"");
            }
        });
        soThietBiHienTaiColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PhanPhoiTb, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PhanPhoiTb, String> param) {
                PhanPhoiTb pp = param.getValue();
                return new SimpleStringProperty(pp.getSoThietBi()+"");
            }
        });
        
        init_items_phanphoitb();
    }
    
    public void init_items_phanphoitb() {
        LoadingDialog.execute(() -> {
            items_phanPhoiTb = FXCollections.observableList(PhanPhoiTb.querySelectAll());
            Platform.runLater(() -> {
                tv_phanPhoiTb.setItems(items_phanPhoiTb);
            });
        });
    }
    
    /////////////////// table chi tiet thiet bi
    @FXML TableView<ChiTietTb> tv_chiTietTb;
    ObservableList<ChiTietTb> items_chiTietTb;
    
    @FXML private ComboBox<String> cbPhong;
    ObservableList<String> items_cbPhong;
    
    @FXML TableColumn<ChiTietTb, String> idtbColumn;
    @FXML TableColumn<ChiTietTb, String> tentbColumn;
    
    public void init_items_cbPhong() {
        
        LoadingDialog.execute(() -> {
            items_cbPhong = FXCollections.observableList(ChiTietTb.querySelectAllPhong());
            Platform.runLater(() -> {
                cbPhong.setItems(items_cbPhong);
            });
        });
        
        cbPhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
            LoadingDialog.execute(() -> {
                items_chiTietTb = FXCollections.observableList(ChiTietTb.querySelectAll(Integer.parseInt(newValue)));
                Platform.runLater(() -> {
                    tv_chiTietTb.setItems(items_chiTietTb);
                });
            });
        });
    }
    
    public void initColumns_chiTietTb() {
        idtbColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ChiTietTb, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ChiTietTb, String> param) {
                ChiTietTb ct = param.getValue();
                return new SimpleStringProperty(ct.getIdTb()+"");
            }
        });
        tentbColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ChiTietTb, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ChiTietTb, String> param) {
                ChiTietTb ct = param.getValue();
                return new SimpleStringProperty(ct.getTenTb());
            }
        });
    }
    
    ///////////// tong hop
    public void init_tonghop() {
        
        LoadingDialog.execute(() -> {
            Platform.runLater(() -> {
                lbTongThietBi.setText(BaoCaoTongHop.tongSoThietBi() + "");
                lbTongKhachHang.setText(BaoCaoTongHop.tongKhachHang() + "");
                lbTongSoPhong.setText(BaoCaoTongHop.tongSoPhong() + "");
                lbTongSoPhongTrong.setText(BaoCaoTongHop.tongSoPhongTrong() + "");
                lbTongSoPhongDangDuocDat.setText(BaoCaoTongHop.tongSoPhongDaDat() + "");
                lbTongSoPhongDangCoKhach.setText(BaoCaoTongHop.tongSoPhongDaNhan() + "");
                lbPhongDatNhat.setText(BaoCaoTongHop.phongDatNhat());
                lbPhieuThanhToanTongTienCaoNhat.setText(BaoCaoTongHop.phieuTtCaoNhat());
                lbDichVuUaChuongNhat.setText(BaoCaoTongHop.dichVuUaChuongNhat());
                lbKhachHangThanhToanNhieuNhat.setText(BaoCaoTongHop.khachHangThanhToanNhieuNhat());
            });
        });
    }
    
}
