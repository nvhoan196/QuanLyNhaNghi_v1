/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nghiepvu.dangky;

import database.DB;
import entities.phieudangky.*;
import entities.dichvu.DichVu;
import entities.khachhang.KhachHang;
import entities.loaiphong.LoaiPhong;
import entities.phong.Phong;
import entities.sudungdv.SuDungDv;
import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Admin
 */
public class FXMLdangkyController implements Initializable {

    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO 
        
        init_icons();
        tbPdk_initTable();
        tbKh_initTable();
        tbPhong_init();
        tbDichVu_init();
        
        init_thongtinkhachhang();
        init_tabphong();
        init_tabphieudangky();
        tbSddv_initialize();
        
        
    }    
    
    
    @FXML
    private TableView tvPdk;
    private ObservableList<PhieuDangKy> items;
    
    ArrayList<KhachHang> listKh;
    ArrayList<Integer> listMaKh;
    ArrayList<String> listStringMaKh;
    ArrayList<String> listSoPhong;
    
    TableColumn<PhieuDangKy, String> maPdkColumn;
    TableColumn<PhieuDangKy, String> maKhColumn;
    TableColumn<PhieuDangKy, String> ngayDkColumn;
    TableColumn<PhieuDangKy, String> ngayDenColumn;
    TableColumn<PhieuDangKy, String> thoiGianOColumn;
    TableColumn<PhieuDangKy, String> treemColumn;
    TableColumn<PhieuDangKy, String> nguoiLonColumn;
    TableColumn<PhieuDangKy, String> soPhongColumn;
    TableColumn<PhieuDangKy, String> traTruocColumn;
    TableColumn<PhieuDangKy, String> chuThichColumn;
    TableColumn<PhieuDangKy, String> daNhanPhongColumn;
    TableColumn<PhieuDangKy, String> daThanhToanColumn;

    public void tbPdk_initTable() {
        Platform.runLater(() -> {
            initListKh();
            initListSoPhong();
            addEvents();
            initColumns();
            initItems();
        });
    }

    private void initListKh() {
        listKh = KhachHang.querySelectAll();
        listMaKh = new ArrayList<>();
        listStringMaKh = new ArrayList<>();
        for (KhachHang kh : listKh) {
            listMaKh.add(new Integer(kh.getMaKh()));
            listStringMaKh.add(kh.toString());
        }
    }
    
    private void initListSoPhong() {
        listSoPhong = new ArrayList<>();
        ArrayList<Phong> tmpList = Phong.querySelectAll();
        for (Phong p : tmpList) {
            String sop = String.valueOf(p.getSoPhong());
            listSoPhong.add(sop);
        }
    }
    
    private void addEvents() {
        
    }

    private void initColumns() {
        initMaPdkColumn();
        initMaKhColumn();
        initNgayDkColumn();
        initNgayDenColumn();
        initThoiGianOColumn();
        initTreemColumn();
        initNguoiLonColumn();
        initSoPhongColumn();
        initTraTruocColumn();
        initChuThichColumn();
        initDaNhanPhongColumn();
        initDaThanhToanColumn();
    }

    private void initMaPdkColumn() {
        maPdkColumn = new TableColumn<>("Mã PĐK");
        maPdkColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int maPdk = pdk.getMaPdk();
            return new SimpleStringProperty(String.valueOf(maPdk));
        });
//        maPdkColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            int newValue = Integer.parseInt(event.getOldValue());
//            try {
//                newValue = Integer.parseInt(event.getNewValue());
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//
//            String sql = "update phieudangky set mapdk = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setMaPdk(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(maPdkColumn);
    }
    
    private void initMaKhColumn() {
        maKhColumn = new TableColumn<>("Mã Khách Hàng");
        maKhColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int maPdk = pdk.getMaPdk();
            for (Integer a : listMaKh) {
                if (a.intValue() == pdk.getMaKh()) {
                    int index = listMaKh.indexOf(a);
                    return new SimpleStringProperty(listStringMaKh.get(index));
                }
            }
            
            return new SimpleStringProperty();
        });
//        ObservableList<String> olKh = FXCollections.observableList(listStringMaKh);
//        maKhColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olKh));
//        maKhColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//            int index = listStringMaKh.indexOf(newValue);
//            
//            int maKh = listMaKh.get(index).intValue();
//
//            String sql = "update phieudangky set makh = '"
//                    + String.valueOf(maKh)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setMaPdk(maKh);
//            } else {
//                tvPdk.refresh();
//            }
//        });
//        maKhColumn.setPrefWidth(250);
        maKhColumn.setPrefWidth(200);
        tvPdk.getColumns().add(maKhColumn);
    }
    
    
    Date checkDateFormat(String date) {
        String[] parts = date.split("/");
        if (parts.length != 3) return null;
        int ngay, thang, nam;
        Date d;
        try {
            ngay = Integer.parseInt(parts[0]);
            thang = Integer.parseInt(parts[1]);
            nam = Integer.parseInt(parts[2]);
            d = new Date(nam-1900, thang-1, ngay);
        } catch (Exception e) {
            return null;
        }
        return d;
    }
    
    private void initNgayDkColumn() {
        ngayDkColumn = new TableColumn<>("Ngày ĐK");
        ngayDkColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            Date ngaydk = pdk.getNgayDk();
            if (ngaydk == null) {
                return new SimpleStringProperty("");
            }
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            String date = f.format(ngaydk);
            
            return new SimpleStringProperty(date);
        });
//        ngayDkColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        ngayDkColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//            Date d = checkDateFormat(newValue);
//            if (d == null) {
//                tvPdk.refresh();
//                return;
//            }
//
//            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//            
//            String sql = "update phieudangky set ngaydk = '"
//                    + f.format(d)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setNgayDk(d);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        ngayDkColumn.setPrefWidth(100);
        tvPdk.getColumns().add(ngayDkColumn);
    }

    private void initNgayDenColumn() {
        ngayDenColumn = new TableColumn<>("Ngày Đến");
        ngayDenColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            Date ngayDen = pdk.getNgayDen();
            if (ngayDen == null) {
                return new SimpleStringProperty("");
            }
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            String date = f.format(ngayDen);
            
            return new SimpleStringProperty(date);
        });
//        ngayDenColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        ngayDenColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//            Date d = checkDateFormat(newValue);
//            if (d == null) {
//                tvPdk.refresh();
//                return;
//            }
//
//            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//            
//            String sql = "update phieudangky set ngayden = '"
//                    + f.format(d)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setNgayDen(d);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        ngayDenColumn.setPrefWidth(100);
        tvPdk.getColumns().add(ngayDenColumn);
    }

    private void initThoiGianOColumn() {
        thoiGianOColumn = new TableColumn<>("Thời Gian Ở");
        thoiGianOColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int thoiGianO = pdk.getThoiGianO();
            return new SimpleStringProperty(String.valueOf(thoiGianO));
        });
//        thoiGianOColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        thoiGianOColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            int newValue = Integer.parseInt(event.getOldValue());
//            try {
//                newValue = Integer.parseInt(event.getNewValue());
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//
//            String sql = "update phieudangky set thoigiano = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setThoiGianO(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(thoiGianOColumn);
    }

    private void initTreemColumn() {
        treemColumn = new TableColumn<>("Trẻ Em");
        treemColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int treem = pdk.getTreem();
            return new SimpleStringProperty(String.valueOf(treem));
        });
//        treemColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        treemColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            int newValue = Integer.parseInt(event.getOldValue());
//            try {
//                newValue = Integer.parseInt(event.getNewValue());
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//
//            String sql = "update phieudangky set treem = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setTreem(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(treemColumn);
    }

    private void initNguoiLonColumn() {
        nguoiLonColumn = new TableColumn<>("Người Lớn");
        nguoiLonColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int nguoilon = pdk.getNguoilon();
            return new SimpleStringProperty(String.valueOf(nguoilon));
        });
//        nguoiLonColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        nguoiLonColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            int newValue = Integer.parseInt(event.getOldValue());
//            try {
//                newValue = Integer.parseInt(event.getNewValue());
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//
//            String sql = "update phieudangky set nguoilon = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setNguoilon(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(nguoiLonColumn);
    }

    private void initSoPhongColumn() {
        soPhongColumn = new TableColumn<>("Số Phòng");
        soPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int soPhong = pdk.getSoPhong();
            return new SimpleStringProperty(String.valueOf(soPhong));
        });
//        ObservableList<String> olSp = FXCollections.observableList(listSoPhong);
//        soPhongColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olSp));
//        soPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//
//            String sql = "update phieudangky set sophong = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setSoPhong(Integer.parseInt(newValue));
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(soPhongColumn);
    }

    private void initTraTruocColumn() {
        traTruocColumn = new TableColumn<>("Trả Trước");
        traTruocColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            float traTruoc = pdk.getTraTruoc();
            return new SimpleStringProperty(String.valueOf(traTruoc));
        });
//        traTruocColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        traTruocColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            float newValue = Float.parseFloat(event.getOldValue());
//            try {
//                newValue = Float.parseFloat(event.getNewValue());
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//            String sql = "update phieudangky set tratruoc = '"
//                    + String.valueOf(newValue)
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setTraTruoc(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(traTruocColumn);
    }

    private void initChuThichColumn() {
        chuThichColumn = new TableColumn<>("Chú Thích");
        chuThichColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String chuThich = pdk.getChuThich();
            return new SimpleStringProperty(String.valueOf(chuThich));
        });
//        chuThichColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        chuThichColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getOldValue();
//            try {
//                newValue = event.getNewValue();
//            } catch (NumberFormatException e) {
//                tvPdk.refresh();
//                return;
//            }
//
//            String sql = "update phieudangky set chuthich = '"
//                    + newValue
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setChuThich(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        tvPdk.getColumns().add(chuThichColumn);
    }

    private void initDaNhanPhongColumn() {
        daNhanPhongColumn = new TableColumn<>("Đã Nhận Phòng");
        daNhanPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String daNhanPhong = pdk.getDaNhanPhong();
            
            return new SimpleStringProperty(daNhanPhong);
        });
//        ArrayList<String> alDaNhanPhong = new ArrayList();
//        alDaNhanPhong.add("Chưa nhận phòng");
//        alDaNhanPhong.add("Đã nhận phòng");
//        ObservableList<String> olDaNhanPhong = FXCollections.observableList(alDaNhanPhong);
//        daNhanPhongColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olDaNhanPhong));
//        daNhanPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//
//            String sql = "update phieudangky set danhanphong = '"
//                    + newValue
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setDaNhanPhong(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        daNhanPhongColumn.setPrefWidth(200);
        tvPdk.getColumns().add(daNhanPhongColumn);
    }

    private void initDaThanhToanColumn() {
        daThanhToanColumn = new TableColumn<>("Đã Thanh Toán");
        daThanhToanColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String daThanhToan = pdk.getDaThanhToan();
            
            return new SimpleStringProperty(daThanhToan);
        });
//        ArrayList<String> alDaThanhToan = new ArrayList();
//        alDaThanhToan.add("Chưa thanh toán");
//        alDaThanhToan.add("Đã thanh toán");
//        ObservableList<String> olDaThanhToan = FXCollections.observableList(alDaThanhToan);
//        daThanhToanColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olDaThanhToan));
//        daThanhToanColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
//            int row = event.getTablePosition().getRow();
//            PhieuDangKy pdk = items.get(row);
//            String newValue = event.getNewValue();
//
//            String sql = "update phieudangky set dathanhtoan = '"
//                    + newValue
//                    + "' where mapdk = '"
//                    + String.valueOf(pdk.getMaPdk())
//                    + "';";
//            boolean ok = DB.update(sql);
//            if (ok) {
//                pdk.setDaThanhToan(newValue);
//            } else {
//                tvPdk.refresh();
//            }
//        });
        daThanhToanColumn.setPrefWidth(200);
        tvPdk.getColumns().add(daThanhToanColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            initListKh();
            initListSoPhong();
            items = FXCollections.observableArrayList(PhieuDangKy.querySelectAll());
            Platform.runLater(() -> {
                tvPdk.setItems(items);
            });
        });
    }
    
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////////////////
    ///////////////// TABLE KHACH HANG /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    @FXML
    private TableView tvKh;
    private ObservableList<KhachHang> itemsKh;
    
    TableColumn<KhachHang, String> tbKh_maKhColumn;
    TableColumn<KhachHang, String> tbKh_firstNameColumn;
    TableColumn<KhachHang, String> tbKh_lastNameColumn;
    TableColumn<KhachHang, String> tbKh_gioiTinhColumn;
    TableColumn<KhachHang, String> tbKh_sdtColumn;
    TableColumn<KhachHang, String> tbKh_cmndColumn;
    TableColumn<KhachHang, String> tbKh_diaChiColumn;
    TableColumn<KhachHang, String> tbKh_quocTichColumn;
    TableColumn<KhachHang, String> tbKh_emailColumn;
    

    /**
     * Initializes the controller class.
     */
    public void tbKh_initTable() {
        // TODO 
        Platform.runLater(() -> {
            tbKh_initColumns();
            tbKh_initItems();
        });
    }    

    private void tbKh_initColumns() {
        tbKh_initMaKhColumn();
        tbKh_initFirstNameColumn();
        tbKh_initLastNameColumn();
        tbKh_initGioiTinhColumn();
        tbKh_initSdtColumn();
        tbKh_initCmndColumn();
        tbKh_initDiaChiColumn();
        tbKh_initQuocTichColumn();
        tbKh_initEmailColumn();
    }

    private void tbKh_initMaKhColumn() {
        tbKh_maKhColumn = new TableColumn<>("Mã KH");
        tbKh_maKhColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            int maKh = kh.getMaKh();
            return new SimpleStringProperty(String.valueOf(maKh));
        });
        tbKh_maKhColumn.setPrefWidth(50);
        tvKh.getColumns().add(tbKh_maKhColumn);
    }
    
    private void tbKh_initFirstNameColumn() {
        tbKh_firstNameColumn = new TableColumn<>("First Name");
        tbKh_firstNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String firstName = kh.getFirstName();
            return new SimpleStringProperty(firstName);
        });
        tbKh_firstNameColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_firstNameColumn);
    }
    
    private void tbKh_initLastNameColumn() {
        tbKh_lastNameColumn = new TableColumn<>("Last Name");
        tbKh_lastNameColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String lastName = kh.getLastName();
            return new SimpleStringProperty(lastName);
        });
        tbKh_lastNameColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_lastNameColumn);
    }
    
    private void tbKh_initGioiTinhColumn() {
        tbKh_gioiTinhColumn = new TableColumn<>("Giới Tính");
        tbKh_gioiTinhColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String gioiTinh = kh.getGioitinh();
            return new SimpleStringProperty(gioiTinh);
        });
        tbKh_gioiTinhColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_gioiTinhColumn);
    }
    
    private void tbKh_initSdtColumn() {
        tbKh_sdtColumn = new TableColumn<>("Số điện thoại");
        tbKh_sdtColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String sdt = kh.getSdt();
            return new SimpleStringProperty(sdt);
        });
        tbKh_sdtColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_sdtColumn);
    }
    
    private void tbKh_initCmndColumn() {
        tbKh_cmndColumn = new TableColumn<>("Số CMND");
        tbKh_cmndColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String cmnd = kh.getCmnd();
            return new SimpleStringProperty(cmnd);
        });
        tbKh_cmndColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_cmndColumn);
    }
    
    private void tbKh_initDiaChiColumn() {
        tbKh_diaChiColumn = new TableColumn<>("Địa Chỉ");
        tbKh_diaChiColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String diaChi = kh.getDiaChi();
            return new SimpleStringProperty(diaChi);
        });
        tbKh_diaChiColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_diaChiColumn);
    }
    
    private void tbKh_initQuocTichColumn() {
        tbKh_quocTichColumn = new TableColumn<>("Quốc Tịch");
        tbKh_quocTichColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String quocTich = kh.getQuocTich();
            return new SimpleStringProperty(quocTich);
        });
        tbKh_quocTichColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_quocTichColumn);
    }
    
    private void tbKh_initEmailColumn() {
        tbKh_emailColumn = new TableColumn<>("Email");
        tbKh_emailColumn.setCellValueFactory((TableColumn.CellDataFeatures<KhachHang, String> param) -> {
            KhachHang kh = param.getValue();
            String email = kh.getEmail();
            return new SimpleStringProperty(email);
        });
        tbKh_emailColumn.setPrefWidth(150);
        tvKh.getColumns().add(tbKh_emailColumn);
    }
    
    private void tbKh_initItems() {
        LoadingDialog.execute(() -> {
            itemsKh = FXCollections.observableArrayList(KhachHang.querySelectAll());
            Platform.runLater(() -> {
                tvKh.setItems(itemsKh);
            });
        });
    }
    
    
    
    ////////////////////////////////////////////
    /////////// TABLE PHONG ///////////////////
    ////////////////////////////////////////////
    
    @FXML
    private TableView tbPhong_tv;
    private ObservableList<Phong> tbPhong_items;
    private ArrayList<LoaiPhong> tbPhong_listLoaiPhong;
    
    TableColumn<Phong, String> tbPhong_soPhongPColumn;
    TableColumn<Phong, String> tbPhong_maLoaiPColumn;
    TableColumn<Phong, String> tbPhong_giaPhongColumn;
    TableColumn<Phong, String> tbPhong_trangThaiColumn;
    
    public void tbPhong_init() {
        // TODO 
        Platform.runLater(() -> {
            tbPhong_listLoaiPhong = LoaiPhong.querySelectAll();
            tbPhong_initColumns();
            tbPhong_initItems();
        });
    }    
    
    private void tbPhong_initColumns() {
        tbPhong_initSoPhongColumn();
        tbPhong_initMaLoaiPColumn();
        tbPhong_initGiaPhongColumn();
        tbPhong_initTrangThaiColumn();
    }

    private void tbPhong_initSoPhongColumn() {
        tbPhong_soPhongPColumn = new TableColumn<>("Số Phòng");
        tbPhong_soPhongPColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            int soPhong = p.getSoPhong();
            return new SimpleStringProperty(String.valueOf(soPhong));
        });
        tbPhong_tv.getColumns().add(tbPhong_soPhongPColumn);
    }
    
    private void tbPhong_initMaLoaiPColumn() {
        tbPhong_maLoaiPColumn = new TableColumn<>("Loại Phòng");
        tbPhong_maLoaiPColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            int maLoaiP = p.getMaLoaiP();
            for (LoaiPhong lp : tbPhong_listLoaiPhong) {
                if (lp.getMaLoaiP() == maLoaiP) {
                    return new SimpleStringProperty(lp.getTenLoaiP());
                }
            }
            return new SimpleStringProperty("");
        });
        tbPhong_maLoaiPColumn.setPrefWidth(200);
        tbPhong_tv.getColumns().add(tbPhong_maLoaiPColumn);
    }
    
    private void tbPhong_initGiaPhongColumn() {
        tbPhong_giaPhongColumn = new TableColumn<>("Giá Phòng (VNĐ)");
        tbPhong_giaPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            float giaPhong = p.getGiaPhong();
            return new SimpleStringProperty(String.valueOf(giaPhong));
        });
        tbPhong_giaPhongColumn.setPrefWidth(200);
        tbPhong_tv.getColumns().add(tbPhong_giaPhongColumn);
    }
    
    private void tbPhong_initTrangThaiColumn() {
        tbPhong_trangThaiColumn = new TableColumn<>("Trạng Thái");
        tbPhong_trangThaiColumn.setCellValueFactory((TableColumn.CellDataFeatures<Phong, String> param) -> {
            Phong p = param.getValue();
            String trangThai = p.getTrangThai();
            return new SimpleStringProperty(trangThai);
        });
        tbPhong_trangThaiColumn.setPrefWidth(200);
        tbPhong_tv.getColumns().add(tbPhong_trangThaiColumn);
    }
    
    private void tbPhong_initItems() {
        LoadingDialog.execute(() -> {
            tbPhong_items = FXCollections.observableArrayList(Phong.querySelectAllEmpty());
            Platform.runLater(() -> {
                tbPhong_tv.setItems(tbPhong_items);
            });
        });
    }
    
    
    ///////////////////////////////////////////////////////////
    ///////// TABLE DICH VU //////////////////////////////////
    //////////////////////////////////////////////////////////
    @FXML
    private TableView tbDichVu_tv;
    private ObservableList<DichVu> tbDichVu_items;
    
    TableColumn<DichVu, String> tbDichVu_maDvColumn;
    TableColumn<DichVu, String> tbDichVu_tenDvColumn;
    TableColumn<DichVu, String> tbDichVu_giaDvColumn;

    public void tbDichVu_init() {
        // TODO 
        Platform.runLater(() -> {
            tbDichVu_initColumns();
            tbDichVu_initItems();
        });
    }    

    private void tbDichVu_initColumns() {
        tbDichVu_initMaDvColumn();
        tbDichVu_initTenDvColumn();
        tbDichVu_initGiaDvColumn();
    }

    private void tbDichVu_initMaDvColumn() {
        tbDichVu_maDvColumn = new TableColumn<>("Mã Dịch Vụ (VNĐ)");
        tbDichVu_maDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            int maDv = dv.getMaDv();
            return new SimpleStringProperty(String.valueOf(maDv));
        });
        tbDichVu_maDvColumn.setPrefWidth(100);
        tbDichVu_tv.getColumns().add(tbDichVu_maDvColumn);
    }
    
    private void tbDichVu_initTenDvColumn() {
        tbDichVu_tenDvColumn = new TableColumn<>("Tên Dịch Vụ");
        tbDichVu_tenDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            String tenDv = dv.getTenDv();
            return new SimpleStringProperty(tenDv);
        });
        tbDichVu_tenDvColumn.setPrefWidth(200);
        tbDichVu_tv.getColumns().add(tbDichVu_tenDvColumn);
    }
   
    private void tbDichVu_initGiaDvColumn() {
        tbDichVu_giaDvColumn = new TableColumn<>("Giá Dịch Vụ");
        tbDichVu_giaDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<DichVu, String> param) -> {
            DichVu dv = param.getValue();
            float giaDv = dv.getGiaDv();
            return new SimpleStringProperty(String.valueOf(giaDv));
        });
        tbDichVu_giaDvColumn.setPrefWidth(200);
        tbDichVu_tv.getColumns().add(tbDichVu_giaDvColumn);
    }
    
    private void tbDichVu_initItems() {
        LoadingDialog.execute(() -> {
            tbDichVu_items = FXCollections.observableArrayList(DichVu.querySelectAll());
            Platform.runLater(() -> {
                tbDichVu_tv.setItems(tbDichVu_items);
            });
        });
    }
    
    ////////////////////////////////
    ////////// LOGIC
    //////////////////////////////////////////////
    //////////////////////////////////////////////
    //////////////////////////////////////////////
    

    ///////////////////////////////////////////////////
    ////// cac Controls cua tab khach hang
    @FXML
    private TextField tfCmnd;
    @FXML
    private TextField tfMaKh;
    @FXML
    private TextField tfTen;
    @FXML
    private TextField tfHoDem;
    @FXML
    private TextField tfSdt;
    @FXML
    private TextField tfDiaChi;
    @FXML
    private TextField tfQuocTich;
    @FXML
    private TextField tfEmail;
    
    public void init_thongtinkhachhang() {
        initCbGioiTinh();
        addEventClickItemTablePdk();
        addEventClickBtnLamMoiKh();
        addEventClickBtnThemKhMoi();
        addEventClickBtnTuDongTaoMakh();
        addEventClickBtnCapNhatKhachHang();
        addEventClickBtnTimKiemKhachHang();
        addEventClickBtnChonKhachHang();
    }
    
    @FXML
    private ComboBox<String> cbGioiTinh;
    private ArrayList<String> alCbGioiTinh;
    private ObservableList<String> olCbGioiTinh;
    public void initCbGioiTinh() {
        alCbGioiTinh = new ArrayList<>();
        alCbGioiTinh.add("Nam");
        alCbGioiTinh.add("Nữ");
        alCbGioiTinh.add("Không xác định");
        olCbGioiTinh = FXCollections.observableList(alCbGioiTinh);
        cbGioiTinh.setItems(olCbGioiTinh);
    }
    
    public void addEventClickItemTablePdk() {
        tvKh.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            KhachHang kh = (KhachHang) tvKh.getSelectionModel().getSelectedItem();
            if (kh != null) {
                tfCmnd.setText(kh.getCmnd());
                tfMaKh.setText(kh.getMaKh()+"");
                tfTen.setText(kh.getFirstName());
                tfHoDem.setText(kh.getLastName());
                tfSdt.setText(kh.getSdt());
                tfDiaChi.setText(kh.getDiaChi());
                tfQuocTich.setText(kh.getQuocTich());
                tfEmail.setText(kh.getEmail());
                cbGioiTinh.getSelectionModel().select(kh.getGioitinh());
            }
        });
    }
    
    @FXML
    private Button btnLamMoiKh;
    public void addEventClickBtnLamMoiKh() {
        btnLamMoiKh.setOnAction((event) -> {
            clearAllTfKh();
        });
    }
    
    @FXML
    private Button btnThemKhMoi;
    public void addEventClickBtnThemKhMoi() {
        btnThemKhMoi.setOnAction((event) -> {
            try {
                String cmnd = tfCmnd.getText();
                String maKh = tfMaKh.getText();
                String ten = tfTen.getText();
                String hoDem = tfHoDem.getText();
                String sdt = tfSdt.getText();
                String diaChi = tfDiaChi.getText();
                String quocTich = tfQuocTich.getText();
                String email = tfEmail.getText();
                String gioiTinh = cbGioiTinh.getSelectionModel().getSelectedItem();
                if (gioiTinh == null) {
                    gioiTinh = "Không xác định";
                }
                if (!cmnd.isEmpty() && !maKh.isEmpty() && !ten.isEmpty() && !hoDem.isEmpty()) {
                    KhachHang kh = new KhachHang(Integer.parseInt(maKh), ten, hoDem, gioiTinh, sdt, cmnd, diaChi, quocTich, email);
                    if (kh.insertToDatabase()) {
                        tbKh_initItems();
                        utils.Utils.showSuccess("Thành công", "Thêm khách hàng thành công!");
                        for (int i=0; i<itemsKh.size(); i++) {
                            KhachHang k = itemsKh.get(i);
                            if (maKh.equals(k.getMaKh()+"")) {
                                tvKh.getSelectionModel().select(k);
                                tvKh.scrollTo(k);
                                break;
                            }
                        }                        
                        return;
                    }
                }
                
            } catch (Exception e) {
                
            }
            utils.Utils.showError("Lỗi", "Thêm khách hàng không thành công!\nKiểm tra lại thông tin nhập!");
        });
    }
    
    @FXML
    private Button btnTuDongTaoMaKh;
    public void addEventClickBtnTuDongTaoMakh() {
        btnTuDongTaoMaKh.setOnAction((event) -> {
            try {
                ResultSet rs = DB.select("select max(makh) as max from khachhang;");
                if (rs.next()) {
                    int max = rs.getInt("max");
                    max++;
                    tfMaKh.setText(max + "");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });   
    }
    
    @FXML
    private Button btnCapNhatKhachHang;
    public void addEventClickBtnCapNhatKhachHang() {
        btnCapNhatKhachHang.setOnAction((event) -> {
            try {
                KhachHang khCu = (KhachHang) tvKh.getSelectionModel().getSelectedItem();
                if (khCu == null) {
                    utils.Utils.showError("Lỗi", "Chọn khách hàng cần thay đổi thông tin!");
                    return;
                }
                String cmnd = tfCmnd.getText();
                String maKh = tfMaKh.getText();
                String ten = tfTen.getText();
                String hoDem = tfHoDem.getText();
                String sdt = tfSdt.getText();
                String diaChi = tfDiaChi.getText();
                String quocTich = tfQuocTich.getText();
                String email = tfEmail.getText();
                String gioiTinh = cbGioiTinh.getSelectionModel().getSelectedItem();
                if (!cmnd.isEmpty() && !maKh.isEmpty() && !ten.isEmpty() && !hoDem.isEmpty()) {
                    KhachHang kh = new KhachHang(Integer.parseInt(maKh), ten, hoDem, gioiTinh, sdt, cmnd, diaChi, quocTich, email);
                    if (kh.updateToDatabase(khCu.getMaKh())) {
                        tbKh_initItems();
                        utils.Utils.showSuccess("Thành công", "Cập nhật thông tin khách hàng thành công!");
                        for (int i = 0; i < itemsKh.size(); i++) {
                            KhachHang k = itemsKh.get(i);
                            if (k.getMaKh() == Integer.parseInt(maKh)) {
                                tvKh.getSelectionModel().select(k);
                                tvKh.scrollTo(k);
                                break;
                            }
                        }
                        return;
                    }
                }

            } catch (Exception e) {
            }
            utils.Utils.showError("Lỗi", "Cập nhật thông tin khách hàng không thành công!\nKiểm tra lại thông tin nhập!");
        });
    }
    
    @FXML
    private Button btnTimKiemKhachHang;
    public void addEventClickBtnTimKiemKhachHang() {
        btnTimKiemKhachHang.setOnAction((event) -> {
            LoadingDialog.execute(() -> {
                String cmnd = tfCmnd.getText();
            String maKh = tfMaKh.getText();
            String ten = tfTen.getText();
            String hoDem = tfHoDem.getText();
            String sdt = tfSdt.getText();
            String diaChi = tfDiaChi.getText();
            String quocTich = tfQuocTich.getText();
            String email = tfEmail.getText();
            
            String gioiTinh = cbGioiTinh.getSelectionModel().getSelectedItem();
            
            if (cmnd == null) {
                cmnd = "";
            }
            if (maKh == null) {
                maKh = "";
            }
            if (ten == null ) {
                ten = "";
            }
            if (hoDem == null) {
                hoDem = "";
            }
            if (sdt == null) {
                sdt = "";
            }
            if (diaChi == null) {
                diaChi = "";
            }
            if (quocTich == null ) {
                quocTich = "";
            }
            if (gioiTinh == null) {
                gioiTinh = "";
            }
            if (email == null) {
                email = "";
            }
            
//            System.out.println(gioiTinh);
                
            if (cmnd.isEmpty()
                    && maKh.isEmpty()
                    && ten.isEmpty()
                    && hoDem.isEmpty()
                    && sdt.isEmpty()
                    && diaChi.isEmpty()
                    && quocTich.isEmpty()
                    && email.isEmpty()
                    && gioiTinh.isEmpty()) {
                System.out.println("Chay den day 1");
                tbKh_initItems();
                
            } else {
                try {
                    String sql = "select * from khachhang where ";
                    if (!maKh.isEmpty()) {
                        sql = sql + "and makh = '" + maKh + "' ";
                    }
                    if (!ten.isEmpty()) {
                        sql = sql + "and firstname like '%" + ten + "%' ";
                    }
                    if (!hoDem.isEmpty()) {
                        sql = sql + "and lastname like '%" + hoDem + "%' ";
                    }
                    if (!sdt.isEmpty()) {
                        sql = sql + "and sdt = '" + sdt + "' ";
                    }
                    if (!diaChi.isEmpty()) {
                        sql = sql + "and diachi = '" + diaChi + "' ";
                    }
                    if (!quocTich.isEmpty()) {
                        sql = sql + "and quoctich = '" + quocTich + "' ";
                    }
                    if (!email.isEmpty()) {
                        sql = sql + "and email = '" + email + "' ";
                    }
                    if (!gioiTinh.isEmpty()) {
                        sql = sql + "and gioitinh = '" + gioiTinh + "' ";
                    }
                    if (!cmnd.isEmpty()) {
                        sql = sql + "and cmnd = '" + cmnd + "' ";
                    }
                    sql = sql + ";";
                    
                    StringBuilder tmpSql = new StringBuilder(sql);
                    int index = sql.indexOf("and");
                    tmpSql.delete(index, index+4);
                    sql = tmpSql.toString();
                    System.out.println("Chay den day 2");
                    System.out.println(sql);
                    ResultSet rs = DB.select(sql);
                    ArrayList<KhachHang> tmpAlKh = new ArrayList<>();
                    while (rs.next()) {
                        tmpAlKh.add(new KhachHang(rs.getInt("makh"), 
                                rs.getString("firstname"), 
                                rs.getString("lastname"), 
                                rs.getString("gioitinh"),
                                rs.getString("sdt"),
                                rs.getString("cmnd"),
                                rs.getString("diachi"), 
                                rs.getString("quoctich"), 
                                rs.getString("email")));
                    }
                    
                    Platform.runLater(() -> {
                        itemsKh = FXCollections.observableList(tmpAlKh);
                        tvKh.setItems(itemsKh);
                    });
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            });
        });
    }
    
    @FXML Button btnChonKhachHang;
    public void addEventClickBtnChonKhachHang() {
        btnChonKhachHang.setOnAction((event) -> {
            KhachHang kh = (KhachHang) tvKh.getSelectionModel().getSelectedItem();
            if (kh == null) {
                utils.Utils.showError("Chưa chọn khách hàng", "Xin vui lòng chọn khách hàng muốn đăng kí phòng");
                return;
            }
            lbhoTen.setText(kh.getLastName() + " " + kh.getFirstName());
            lbMaKh.setText(kh.getMaKh()+"");
            utils.Utils.showSuccess("Chọn thành công", "Chọn khách hàng thành công!\nXin vui lòng qua Tab Phiếu Đăng Ký để xem!");
        });
    }
    
    public void clearAllTfKh() {
        tfCmnd.setText("");
        tfMaKh.setText("");
        tfTen.setText("");
        tfHoDem.setText("");
        tfSdt.setText("");
        tfDiaChi.setText("");
        tfQuocTich.setText("");
        tfEmail.setText("");
        cbGioiTinh.getSelectionModel().select("");
    }
    
    
    /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    ////////////// TAB PHONG //////////////////////////////
    ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    public void init_tabphong() {
        addEventClickBtnLamMoiPhong();
        addEventBtnChonPhong();
    }
    
    @FXML
    private Button btnLamMoiPhong;
    public void addEventClickBtnLamMoiPhong() {
        btnLamMoiPhong.setOnAction((event) -> {
            tbPhong_initItems();
        });
    }
    
    @FXML
    private Button btnChonPhong;
    public void addEventBtnChonPhong() {
        btnChonPhong.setOnAction((event) -> {
            Phong p = (Phong) tbPhong_tv.getSelectionModel().getSelectedItem();
            if (p == null) {
                utils.Utils.showError("Chưa chọn phòng", "Xin chọn phòng trước!");
                return;
            }
            lbPhong.setText(p.getSoPhong()+"");
            String loaip = LoaiPhong.queryTenLoaiPByMaLoaiP(p.getMaLoaiP());
            lbLoaiPhong.setText(loaip);
            utils.Utils.showSuccess("Thành công", "Đã chọn phòng!\nQua Tab Phiếu đăng ký để xem!");
        });
    }
    
    
    /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    ////////////// TAB PHIEU DANG KY //////////////////////////////
    ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    public void init_tabphieudangky() {
        addEventItemClickTablePdk();
        addEventClickBtnTuDongTaoMaPdk();
        addEventClickBtnLayNgayHomNay_NgayDk();
        addEventClickBtnLayNgayHomNay_NgayDen();
        addEventClickBtnTaoPhieuDangKy();
        addEventClickBtnLamMoiPdk();
        addEventClickBtnTinhDenGio();
        addEventClickBtnTimKiemPdk();
        addEventClickBtnCapNhatPdk();
        addEventClickBtnNhanPhong();
        addEventClickBtnHuyPdk();
        addEventClickBtnTinhTien();
        addEventClickBtnThanhToan();
    }
    
    @FXML
    private Label lbhoTen;
    @FXML
    private Label lbMaKh;
    @FXML
    private Label lbPhong;
    @FXML
    private Label lbLoaiPhong;
    @FXML
    private Label lbDaNhanPhong;
    @FXML
    private Label lbDaThanhToan;
    @FXML
    private TextField tfMaPdk;
    @FXML
    private TextField tfNgayDangKy;
    @FXML
    private TextField tfNgayDen;
    @FXML
    private TextField tfSoTreEm;
    @FXML
    private TextField tfSoNguoiLon;
    @FXML
    private TextField tfThoiGianO;
    @FXML
    private TextField tfTraTruoc;
    
    @FXML Label lbTienPhong;
    @FXML Label lbTienDichVu;
    @FXML Label lbTraTruoc;
    @FXML Label lbTongTien;
    
    public void clearThongTinThanhToan() {
        lbTienPhong.setText("0.0");
        lbTienDichVu.setText("0.0");
        lbTraTruoc.setText("0.0");
        lbTongTien.setText("0.0");
    }
    
    @FXML private Button btnTinhTien;
    @FXML private Button btnThanhToan;
    
    public void addEventClickBtnTinhTien() {
        btnTinhTien.setOnAction((event) -> {
            tinhTien();
        });
    }
    
    private void tinhTien() {
        PhieuDangKy pdk = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();;
        if (pdk == null) {
            utils.Utils.showError("Chưa chọn PĐK", "Xin hãy chọn phiếu đăng ký!");
            return;
        }
        DecimalFormat df = new DecimalFormat("##0.0");
        tinhDenGio();
        lbTienPhong.setText(df.format((Phong.queryTienPhong(pdk.getSoPhong()) * Integer.parseInt(tfThoiGianO.getText()))));
        lbTraTruoc.setText(df.format(pdk.getTraTruoc()));
        lbTienDichVu.setText(SuDungDv.queryTienDichVu(pdk.getMaPdk()) + "");
        float tt = Float.parseFloat(lbTienPhong.getText()) + Float.parseFloat(lbTienDichVu.getText()) - Float.parseFloat(lbTraTruoc.getText());
        lbTongTien.setText(df.format(tt));
    }

    public void addEventClickBtnThanhToan() {
        btnThanhToan.setOnAction((event) -> {
            PhieuDangKy pdk = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();;
            if (pdk == null) {
                utils.Utils.showError("Chưa chọn PĐK", "Xin hãy chọn phiếu đăng ký!");
                return;
            }
            tinhTien();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String ngaytt = df.format(Calendar.getInstance().getTime());
            
            float tt = Float.parseFloat(lbTienPhong.getText()) + Float.parseFloat(lbTienDichVu.getText());
            
            String sql1 = "insert into phieuthanhtoan(ngaytt, mapdk, tienphong, tiendichvu, tongtien) values ('"
                    + ngaytt + "', '"
                    + pdk.getMaPdk() + "', '"
                    + lbTienPhong.getText() + "', '"
                    + lbTienDichVu.getText() + "', '"
                    + tt + "');";
            
            String sql2 = "update phieudangky set dathanhtoan = 'Đã thanh toán' where mapdk = '" + pdk.getMaPdk() + "';";
            String sql3 = "update phong set trangthai = 'Đang trống' where sophong = '" + pdk.getSoPhong() + "';";
            System.out.println(sql1);
            System.out.println(sql2);
            System.out.println(sql3);
            int mpdk = pdk.getMaPdk();
            if (DB.insert(sql1) && DB.update(sql2) && DB.update(sql3)) {
                tbPhong_initItems();
                initItems();
                utils.Utils.showSuccess("Thành công", "Thanh toán thành công!");
                for (PhieuDangKy p : items) {
                    if (p.getMaPdk() == mpdk) {
                        tvPdk.getSelectionModel().select(p);
                        tvPdk.scrollTo(p);
                    }
                }
            }
            else {
                utils.Utils.showError("Lỗi", "Thanh toán thất bại!");
            }
        });
    }
    
    public void addEventItemClickTablePdk() {
        tvPdk.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            PhieuDangKy pdk = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();
            if (pdk != null) {
                KhachHang kh = KhachHang.querySelectOne(pdk.getMaKh());
                lbhoTen.setText(kh.getLastName() + " " + kh.getFirstName());
                lbMaKh.setText(pdk.getMaKh()+"");
                lbPhong.setText(pdk.getSoPhong()+"");
                Phong p = Phong.querySelectOne(pdk.getSoPhong());
                lbLoaiPhong.setText(LoaiPhong.queryTenLoaiPByMaLoaiP(p.getMaLoaiP()));
                tfMaPdk.setText(pdk.getMaPdk()+"");
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String valueDate = "";
                if (pdk.getNgayDk() != null) {
                    valueDate = df.format(pdk.getNgayDk());
                }
                tfNgayDangKy.setText(valueDate);
                valueDate = "";
                if (pdk.getNgayDen() != null) {
                    valueDate = df.format(pdk.getNgayDen());
                }
                tfNgayDen.setText(valueDate);
                tfSoTreEm.setText(pdk.getTreem()+"");
                tfSoNguoiLon.setText(pdk.getNguoilon()+"");
                tfThoiGianO.setText(pdk.getThoiGianO()+"");
                tfTraTruoc.setText(pdk.getTraTruoc()+"");
                lbDaNhanPhong.setText(pdk.getDaNhanPhong());
                lbDaThanhToan.setText(pdk.getDaThanhToan());
                
                lbMaPdk_dv.setText(pdk.getMaPdk()+"");
                lbHoTen_dv.setText(lbhoTen.getText());
                lbPhong_dv.setText(pdk.getSoPhong()+"");
                tbSddv_initItems(pdk.getMaPdk());
                clearThongTinThanhToan();
            }
        });
    }
    
    @FXML
    private Button btnTuDongTaoMaPdk;
    public void addEventClickBtnTuDongTaoMaPdk() {
        btnTuDongTaoMaPdk.setOnAction((event) -> {
            try {
                String sql = "select max(mapdk) as max from phieudangky;";
                ResultSet rs = DB.select(sql);
                int max = 0;
                if (rs.next()) {
                    max = rs.getInt("max");
                    max++;
                    tfMaPdk.setText(max+"");
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    private Button btnLayNgayHomNay_NgayDk;
    public void addEventClickBtnLayNgayHomNay_NgayDk() {
        btnLayNgayHomNay_NgayDk.setOnAction((event) -> {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            tfNgayDangKy.setText(df.format(Calendar.getInstance().getTime()));
        });
    }
           
    @FXML
    private Button btnLayNgayHomNay_NgayDen;
    public void addEventClickBtnLayNgayHomNay_NgayDen() {
        btnLayNgayHomNay_NgayDen.setOnAction((event) -> {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            tfNgayDen.setText(df.format(Calendar.getInstance().getTime()));
        });
    }
    
    @FXML Button btnTaoPhieuDangKy;
    public void addEventClickBtnTaoPhieuDangKy() {
        btnTaoPhieuDangKy.setOnAction((event) -> {
            String mapdk = tfMaPdk.getText();
            String makh = lbMaKh.getText();
            String sophong = lbPhong.getText();            
            String ngayden = tfNgayDen.getText();
            
            String ngaydk = tfNgayDangKy.getText();
            String treem = tfSoTreEm.getText();
            String nguoilon = tfSoNguoiLon.getText();
            String thoigiano = tfThoiGianO.getText();
            String tratruoc = tfTraTruoc.getText();
            
            //kiem tra ton tai cac truong bat buoc
            if (mapdk.isEmpty() || makh.isEmpty() || sophong.isEmpty() || ngayden.isEmpty() || ngaydk.isEmpty()) {
                utils.Utils.showError("Thiếu", "Phải nhập đầy đủ các trường yêu cầu!");
                return;
            }
            
            // kiem tra du lieu hop le
            Date ngayDen = null, ngayDk = null;
            try {
                mapdk = Integer.parseInt(mapdk) +"";
                makh = Integer.parseInt(makh) +"";
                sophong = Integer.parseInt(sophong) +"";
                String[] partsNgayDen = ngayden.split("/");
                if (partsNgayDen.length == 3) {
                    int ngay = Integer.parseInt(partsNgayDen[0]);
                    int thang = Integer.parseInt(partsNgayDen[1]);
                    int nam = Integer.parseInt(partsNgayDen[2]);
                    ngayDen = new Date(nam-1900, thang-1, ngay);
                }
                DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
                ngayden = d.format(ngayDen);
                String[] partsNgayDangKy = ngaydk.split("/");
                if (partsNgayDangKy.length == 3) {
                    int ngay = Integer.parseInt(partsNgayDangKy[0]);
                    int thang = Integer.parseInt(partsNgayDangKy[1]);
                    int nam = Integer.parseInt(partsNgayDangKy[2]);
                    ngayDk = new Date(nam-1900, thang-1, ngay);
                }
                ngaydk = d.format(ngayDk);
                if (treem.isEmpty()) treem = "0";
                if (nguoilon.isEmpty()) nguoilon = "0";
                if (thoigiano.isEmpty()) thoigiano = "0";
                if (tratruoc.isEmpty()) tratruoc = "0";
                
                treem = Integer.parseInt(treem) +"";
                nguoilon = Integer.parseInt(nguoilon) +"";
                thoigiano = Integer.parseInt(thoigiano) +"";
                tratruoc= Float.parseFloat(tratruoc) +"";
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String sql = "insert into phieudangky(mapdk, makh, ngaydk, ngayden, thoigiano, treem, nguoilon, sophong, tratruoc, chuthich, danhanphong, dathanhtoan) values("
                        + "'" + mapdk + "', "
                        + "'" + makh + "', "
                        + "'" + ngaydk + "', "
                        + "'" + ngayden + "', "
                        + "'" + thoigiano + "', "
                        + "'" + treem + "', "
                        + "'" + nguoilon + "', "
                        + "'" + sophong + "', "
                        + "'" + tratruoc + "', "
                        + "'" + "" + "', "
                        + "'" + "Chưa nhận phòng" + "', "
                        + "'" + "Chưa thanh toán" + "');";
                System.out.println(sql);
                if (DB.insert(sql)) {
                    DB.update("update phong set trangthai = 'Đã đặt phòng' where sophong = '" + sophong + "';");
                    tbPhong_initItems();
                    initItems();
                    utils.Utils.showSuccess("Thành công", "Thêm phiếu đăng ký thành công!");
                    for (int i=0; i<items.size(); i++) {
                        System.out.println(items.get(i).getMaPdk() + " - " + Integer.parseInt(mapdk));
                        if (items.get(i).getMaPdk() == Integer.parseInt(mapdk)) {
                            tvPdk.getSelectionModel().select(items.get(i));
                            tvPdk.scrollTo(items.get(i));
                        }
                    }
                    
                } else {
                    utils.Utils.showError("Lỗi database", "Không thể thêm phiếu đăng ký vào cơ sở dữ liệu!");
                }
                
            } catch (Exception e) {
                utils.Utils.showError("Lỗi", "Dữ liệu không hợp lệ!\nĐịnh dạng ngày tháng hợp lệ: dd/MM/yyyy\nPhải nhập số nguyên vào các trường là số!");
                return;
            }
        });
    }
    
    @FXML
    private Button btnLamMoiPdk;
    public void addEventClickBtnLamMoiPdk() {
        btnLamMoiPdk.setOnAction((event) -> {
            lbhoTen.setText("");
            lbMaKh.setText("");
            lbPhong.setText("");
            lbLoaiPhong.setText("");
            tfMaPdk.setText("");
            tfNgayDangKy.setText("");
            tfNgayDen.setText("");
            tfSoTreEm.setText("");
            tfSoNguoiLon.setText("");
            tfThoiGianO.setText("");
            tfTraTruoc.setText("");
            lbDaNhanPhong.setText("");
            lbDaThanhToan.setText("");
        });
    }
    
    @FXML
    private Button btnTinhDenGio;
    public void addEventClickBtnTinhDenGio() {
        btnTinhDenGio.setOnAction((event) -> {
            tinhDenGio();
        });
    }
    
    private void tinhDenGio() {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String hientai = df.format(Calendar.getInstance().getTime());
            Date hienTai = null;
            String[] partsHienTai = hientai.split("/");;

            if (partsHienTai.length == 3) {
                int ngay = Integer.parseInt(partsHienTai[0]);
                int thang = Integer.parseInt(partsHienTai[1]);
                int nam = Integer.parseInt(partsHienTai[2]);
                hienTai = new Date(nam - 1900, thang - 1, ngay);
            }
//            System.out.println("CHay den day 1");
            LocalDate dateHienTai = hienTai.toLocalDate();
//            System.out.println("CHay den day 2");
            String ngayden = tfNgayDen.getText();
            if (ngayden.isEmpty()) {
                utils.Utils.showError("Thiếu thông tin", "Thiếu thông tin ngày đến");
                return;
            }

            String[] partsNgayDen = ngayden.split("/");
            Date ngayDen = null;

            if (partsNgayDen.length == 3) {
                int ngay = Integer.parseInt(partsNgayDen[0]);
                int thang = Integer.parseInt(partsNgayDen[1]);
                int nam = Integer.parseInt(partsNgayDen[2]);
                ngayDen = new Date(nam - 1900, thang - 1, ngay);
            }
            LocalDate dateNgayDen = ngayDen.toLocalDate();

            long songay = ChronoUnit.DAYS.between(dateNgayDen, dateHienTai) + 1;
            tfThoiGianO.setText(songay + "");
            return;
        } catch (Exception e) {
            utils.Utils.showError("Lỗi", "Không thể tính toán!");
        }
    }
    
    @FXML Button btnTimKiemPdk;
    public void addEventClickBtnTimKiemPdk() {
        btnTimKiemPdk.setOnAction((event) -> {
            LoadingDialog.execute(() -> {
                String mapdk = tfMaPdk.getText();
                String makh = lbMaKh.getText();
                String sophong = lbPhong.getText();
                String ngayden = tfNgayDen.getText();

                String ngaydk = tfNgayDangKy.getText();
                String treem = tfSoTreEm.getText();
                String nguoilon = tfSoNguoiLon.getText();
                String thoigiano = tfThoiGianO.getText();
                String tratruoc = tfTraTruoc.getText();


                if (mapdk == null) {
                    mapdk = "";
                }
                if (makh == null) {
                    makh = "";
                }
                if (sophong == null) {
                    sophong = "";
                }
                if (ngayden == null) {
                    ngayden = "";
                }
                if (ngaydk == null) {
                    ngaydk = "";
                }
                if (treem == null) {
                    treem = "";
                }
                if (nguoilon == null) {
                    nguoilon = "";
                }
                if (thoigiano == null) {
                    thoigiano = "";
                }
                if (tratruoc == null) {
                    tratruoc = "";
                }

//            System.out.println(gioiTinh);
                if (mapdk.isEmpty()
                        && makh.isEmpty()
                        && sophong.isEmpty()
                        && ngayden.isEmpty()
                        && ngaydk.isEmpty()
                        && treem.isEmpty()
                        && nguoilon.isEmpty()
                        && thoigiano.isEmpty()
                        && tratruoc.isEmpty()) {
                   
                    initItems();

                } else {
                    try {
                        String sql = "select * from phieudangky where ";
                        if (!mapdk.isEmpty()) {
                            sql = sql + "and mapdk = '" + mapdk + "' ";
                        }
                        if (!makh.isEmpty()) {
                            sql = sql + "and makh = '" + makh + "' ";
                        }
                        if (!sophong.isEmpty()) {
                            sql = sql + "and sophong = '" + sophong + "' ";
                        }
                        if (!ngayden.isEmpty()) {
                            String[] partsNgayDen = ngayden.split("/");;

                            if (partsNgayDen.length == 3) {
                                ngayden = partsNgayDen[2] + "-" + partsNgayDen[1] + "-" + partsNgayDen[0];
                            }
                            sql = sql + "and ngayden = '" + ngayden + "' ";
                        }
                        if (!ngaydk.isEmpty()) {
                            String[] partsNgayDk = ngaydk.split("/");;

                            if (partsNgayDk.length == 3) {
                                ngayden = partsNgayDk[2] + "-" + partsNgayDk[1] + "-" + partsNgayDk[0];
                            }
                            sql = sql + "and ngaydk = '" + ngaydk + "' ";
                        }
                        if (!treem.isEmpty()) {
                            sql = sql + "and treem = '" + treem + "' ";
                        }
                        if (!nguoilon.isEmpty()) {
                            sql = sql + "and nguoilon = '" + nguoilon + "' ";
                        }
                        if (!thoigiano.isEmpty()) {
                            sql = sql + "and thoigiano = '" + thoigiano + "' ";
                        }
                        if (!tratruoc.isEmpty()) {
                            sql = sql + "and tratruoc = '" + tratruoc + "' ";
                        }
                        sql = sql + ";";

                        StringBuilder tmpSql = new StringBuilder(sql);
                        int index = sql.indexOf("and");
                        tmpSql.delete(index, index + 4);
                        sql = tmpSql.toString();
                        System.out.println(sql);
                        ResultSet rs = DB.select(sql);
                        ArrayList<PhieuDangKy> tmpAlPdk = new ArrayList<>();
                        while (rs.next()) {
                            tmpAlPdk.add(new PhieuDangKy(rs.getInt("mapdk"),
                                    rs.getInt("makh"), 
                                    rs.getDate("ngaydk"),
                                    rs.getDate("ngayden"), 
                                    rs.getInt("thoigiano"),
                                    rs.getInt("treem"),
                                    rs.getInt("nguoilon"), 
                                    rs.getInt("sophong"), 
                                    rs.getFloat("tratruoc"),
                                    rs.getString("chuthich"),
                                    rs.getString("danhanphong"),
                                    rs.getString("dathanhtoan")));
                        }

                        Platform.runLater(() -> {
                            items = FXCollections.observableList(tmpAlPdk);
                            tvPdk.setItems(items);
                        });
                    } catch (SQLException ex) {
                        Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        });
    }
    
    @FXML Button btnCapNhatPdk;
    public void addEventClickBtnCapNhatPdk() {
        btnCapNhatPdk.setOnAction((event) -> {
            
            PhieuDangKy p = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();
            if (p == null) {
                utils.Utils.showError("Chưa chọn PĐK", "Xin vui lòng chọn PĐK cần cập nhật!");
                return;
            }
            
            String mapdk = tfMaPdk.getText();
            String makh = lbMaKh.getText();
            String sophong = lbPhong.getText();            
            String ngayden = tfNgayDen.getText();
            
            String ngaydk = tfNgayDangKy.getText();
            String treem = tfSoTreEm.getText();
            String nguoilon = tfSoNguoiLon.getText();
            String thoigiano = tfThoiGianO.getText();
            String tratruoc = tfTraTruoc.getText();
            
            //kiem tra ton tai cac truong bat buoc
            if (mapdk.isEmpty() || makh.isEmpty() || sophong.isEmpty() || ngayden.isEmpty() || ngaydk.isEmpty()) {
                utils.Utils.showError("Thiếu", "Phải nhập đầy đủ các trường yêu cầu!");
                return;
            }
            
            // kiem tra du lieu hop le
            Date ngayDen = null, ngayDk = null;
            try {
                mapdk = Integer.parseInt(mapdk) +"";
                makh = Integer.parseInt(makh) +"";
                sophong = Integer.parseInt(sophong) +"";
                String[] partsNgayDen = ngayden.split("/");
                if (partsNgayDen.length == 3) {
                    int ngay = Integer.parseInt(partsNgayDen[0]);
                    int thang = Integer.parseInt(partsNgayDen[1]);
                    int nam = Integer.parseInt(partsNgayDen[2]);
                    ngayDen = new Date(nam-1900, thang-1, ngay);
                }
                DateFormat d = new SimpleDateFormat("yyyy-MM-dd");
                ngayden = d.format(ngayDen);
                String[] partsNgayDangKy = ngaydk.split("/");
                if (partsNgayDangKy.length == 3) {
                    int ngay = Integer.parseInt(partsNgayDangKy[0]);
                    int thang = Integer.parseInt(partsNgayDangKy[1]);
                    int nam = Integer.parseInt(partsNgayDangKy[2]);
                    ngayDk = new Date(nam-1900, thang-1, ngay);
                }
                ngaydk = d.format(ngayDk);
                if (treem.isEmpty()) treem = "0";
                if (nguoilon.isEmpty()) nguoilon = "0";
                if (thoigiano.isEmpty()) thoigiano = "0";
                if (tratruoc.isEmpty()) tratruoc = "0";
                
                treem = Integer.parseInt(treem) +"";
                nguoilon = Integer.parseInt(nguoilon) +"";
                thoigiano = Integer.parseInt(thoigiano) +"";
                tratruoc= Float.parseFloat(tratruoc) +"";
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                
                String sql = "update phieudangky set "
                        + "mapdk = '" + mapdk + "', "
                        + "makh = '" + makh + "', "
                        + "ngaydk = '" + ngaydk + "', "
                        + "ngayden = '" + ngayden + "', "
                        + "thoigiano = '" + thoigiano + "', "
                        + "treem = '" + treem + "', "
                        + "nguoilon = '" + nguoilon + "', "
                        + "sophong = '" + sophong + "', "
                        + "tratruoc = '" + tratruoc + "' "
                        + "where mapdk = '" + p.getMaPdk() + "';";
                System.out.println(sql);
                if (DB.update(sql)) {
                    //DB.update("update phong set trangthai = 'Đã đặt phòng' where sophong = '" + sophong + "';");
                    //tbPhong_initItems();
                    // kiem tra de thay doi trang thai phong
                    if (p.getSoPhong() != Integer.parseInt(sophong)) {
                        ResultSet rs = DB.select("select trangthai from phong where sophong = '" + p.getSoPhong() + "';");
                        String valueTrangThai = "";
                        if (rs.next()) {
                            valueTrangThai = rs.getString("trangthai");
                        }
                        DB.update("update phong set trangthai = 'Đang trống' where sophong = '" + p.getSoPhong() + "';");
                        DB.update("update phong set trangthai = '"+valueTrangThai+"' where sophong = '" + sophong + "';");
                        tbPhong_initItems();
                    }
                    initItems();
                    utils.Utils.showSuccess("Thành công", "Cập nhật phiếu đăng ký thành công!");
                    for (int i=0; i<items.size(); i++) {
                        //System.out.println(items.get(i).getMaPdk() + " - " + Integer.parseInt(mapdk));
                        if (items.get(i).getMaPdk() == Integer.parseInt(mapdk)) {
                            tvPdk.getSelectionModel().select(items.get(i));
                            tvPdk.scrollTo(items.get(i));
                        }
                    }
                    
                } else {
                    utils.Utils.showError("Lỗi database", "Không thể cập nhật phiếu đăng ký vào cơ sở dữ liệu!");
                }
                
            } catch (Exception e) {
                utils.Utils.showError("Lỗi", "Dữ liệu không hợp lệ!\nĐịnh dạng ngày tháng hợp lệ: dd/MM/yyyy\nPhải nhập số nguyên vào các trường là số!");
                return;
            }
        });
    }
    
    @FXML Button btnNhanPhong;
    public void addEventClickBtnNhanPhong() {
        btnNhanPhong.setOnAction((event) -> {
            PhieuDangKy p = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();
            if (p == null) {
                utils.Utils.showError("Chưa chọn PĐK", "Xin vui lòng chọn PĐK cần cập nhật!");
                return;
            }
            int mpdk = p.getMaPdk();
            if (DB.update("update phong set trangthai = 'Đã nhận phòng' where sophong = '" + p.getSoPhong() + "';")
                    && DB.update("update phieudangky set danhanphong = 'Đã nhận phòng' where mapdk = '" + p.getMaPdk() + "';")) {
                initItems();
                utils.Utils.showSuccess("Thành công", "Đã thay đổi trạng thái phòng " + p.getSoPhong());
                for (int i=0; i<items.size(); i++) {
                        if (items.get(i).getMaPdk() == mpdk) {
                            tvPdk.getSelectionModel().select(items.get(i));
                            tvPdk.scrollTo(items.get(i));
                        }
                    }
            } else {
                utils.Utils.showError("Lỗi", "Không thể thay đổi trạng thái phòng " + p.getSoPhong());
            }
        });
    }
    
    @FXML Button btnHuyPdk;
    public void addEventClickBtnHuyPdk() {
        btnHuyPdk.setOnAction((event) -> {
            PhieuDangKy p = (PhieuDangKy) tvPdk.getSelectionModel().getSelectedItem();
            if (p == null) {
                utils.Utils.showError("Chưa chọn PĐK", "Xin vui lòng chọn PĐK cần hủy!");
                return;
            }
            
            String sql = "delete from phieudangky where mapdk = '" + p.getMaPdk() + "';";
            if (DB.delete(sql)) {
                DB.update("update phong set trangthai = 'Đang trống' where sophong = '" + p.getSoPhong() + "';");
                utils.Utils.showSuccess("Thành công", "Đã hủy phiếu đăng ký!");
                initItems();
            } else {
                utils.Utils.showError("Lỗi", "Không thể hủy phiếu đăng ký!");
            }
        });
    }
    
    ////////////////////////////////////////////////////////////////////
    /////////// TAB DANG KY DICH VU /////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    
    
    @FXML private Label lbMaPdk_dv;
    @FXML private Label lbHoTen_dv;
    @FXML private Label lbPhong_dv;
    @FXML private TextField tfSoLuong_dv;
    @FXML private Button btnThemDichVu;
    @FXML private Button btnXoaDichVu;
    
    @FXML
    private TableView tbSddv_tableView;
    private ObservableList<SuDungDv> tbSddv_items;
    private ArrayList<DichVu> tbSddv_listDv;
    
    TableColumn<SuDungDv, String> tbSddv_maPdkColumn;
    TableColumn<SuDungDv, String> tbSddv_maDvColumn;
    TableColumn<SuDungDv, String> tbSddv_idDvColumn;
    TableColumn<SuDungDv, String> tbSddv_soLuongColumn;
    public void tbSddv_initialize() {
        // TODO 
        Platform.runLater(() -> {
            tbSddv_initListDv();
            tbSddv_initColumns();
        });
        addEventClickBtnThemDichVu();
        addEventClickBtnXoaDichVu();
    }    

    private void tbSddv_initColumns() {
        tbSddv_initIdDvColumn();
        tbSddv_initMaPdkColumn();
        tbSddv_initMaDvPColumn();
        tbSddv_initSoLuongColumn();
    }

    private void tbSddv_initMaPdkColumn() {
        tbSddv_maPdkColumn = new TableColumn<>("Mã Phiếu Đăng Ký");
        tbSddv_maPdkColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int maPdk = sddv.getMaPdk();
            return new SimpleStringProperty(String.valueOf(maPdk));
        });
        tbSddv_maPdkColumn.setPrefWidth(150);
        tbSddv_tableView.getColumns().add(tbSddv_maPdkColumn);
    }
    
    private void tbSddv_initMaDvPColumn() {
        tbSddv_maDvColumn = new TableColumn<>("Mã dịch vụ");
        tbSddv_maDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int maDv = sddv.getMaDv();
            for (DichVu dv : tbSddv_listDv) {
                if (dv.getMaDv() == maDv) {
                    return new SimpleStringProperty(dv.toString());
                }
            }
            return new SimpleStringProperty("");
        });
        tbSddv_maDvColumn.setPrefWidth(200);
        tbSddv_tableView.getColumns().add(tbSddv_maDvColumn);
    }
    
    private void tbSddv_initIdDvColumn() {
        tbSddv_idDvColumn = new TableColumn<>("ID Dịch Vụ");
        tbSddv_idDvColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int iddv = sddv.getIdDv();
            return new SimpleStringProperty(String.valueOf(iddv));
        });
        tbSddv_idDvColumn.setPrefWidth(150);
        tbSddv_tableView.getColumns().add(tbSddv_idDvColumn);
    }
    
    private void tbSddv_initSoLuongColumn() {
        tbSddv_soLuongColumn = new TableColumn<>("Số lượng");
        tbSddv_soLuongColumn.setCellValueFactory((TableColumn.CellDataFeatures<SuDungDv, String> param) -> {
            SuDungDv sddv = param.getValue();
            int soLuong = sddv.getSoLuong();
            return new SimpleStringProperty(String.valueOf(soLuong));
        });
        tbSddv_soLuongColumn.setPrefWidth(150);
        tbSddv_tableView.getColumns().add(tbSddv_soLuongColumn);
    }
    
    private void tbSddv_initListDv() {
        tbSddv_listDv = DichVu.querySelectAll();
    }

    private void tbSddv_initItems(int maPdk) {
        LoadingDialog.execute(() -> {
            try {
                ArrayList<SuDungDv> tbSddv_array = new ArrayList<>();
                ResultSet rs = DB.select("select * from sudungdv where mapdk = '" + maPdk +"';");
                while (rs.next()) {
                    tbSddv_array.add(new SuDungDv(rs.getInt("iddv"), rs.getInt("mapdk"), rs.getInt("madv"), rs.getInt("soluong")));
                }
                tbSddv_items = FXCollections.observableArrayList(tbSddv_array);
                Platform.runLater(() -> {
                    tbSddv_tableView.setItems(tbSddv_items);
                });
            } catch (SQLException ex) {
                Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void addEventClickBtnThemDichVu() {
        btnThemDichVu.setOnAction((event) -> {
            try {
                if (lbMaPdk_dv.getText().isEmpty()) {
                    utils.Utils.showError("Thiếu thông tin", "Hãy chọn phiếu đăng ký trước!");
                    return;
                }
                
                int mpdk = 0;
                try {
                    mpdk = Integer.parseInt(lbMaPdk_dv.getText());
                } catch (Exception e) {
                    utils.Utils.showError("Sai định dạng", "Mã phiếu đăng ký phải là số nguyên!");
                    return;
                }
                
                if (tfSoLuong_dv.getText().isEmpty()) {
                    utils.Utils.showError("Thiếu thông tin", "Hãy nhập số lượng dịch vụ!");
                    return;
                }
                DichVu dv = (DichVu) tbDichVu_tv.getSelectionModel().getSelectedItem();
                if (dv == null) {
                    utils.Utils.showError("Chưa chọn dịch vụ", "Hãy chọn dịch vụ có sẵn!");
                    return;
                }
                int sl = 0;
                try {
                    sl = Integer.parseInt(tfSoLuong_dv.getText());
                } catch (Exception e) {
                    utils.Utils.showError("Sai định dạng", "Số lượng dịch vụ phải là số nguyên!");
                    return;
                }
                
                int max = 0;
                ResultSet rs = DB.select("select max(iddv) as max from sudungdv;");
                if (rs.next()) {
                    max = rs.getInt("max");
                    max++;
                }
                
                String sql = "insert into sudungdv(iddv, mapdk, madv, soluong) values('" 
                        + max + "', '"
                        + mpdk + "', '"
                        + dv.getMaDv() + "', '"
                        + sl + "');";
                        
                if (DB.insert(sql)) {
                    utils.Utils.showSuccess("Thành công", "Đăng ký dịch vụ thành công!");
                    tbSddv_initItems(Integer.parseInt(lbMaPdk_dv.getText()));
                    tfSoLuong_dv.setText("");
                } else {
                    utils.Utils.showError("Lỗi", "Đăng ký dịch vụ thất bại!");
                }
                        } catch (SQLException ex) {
                Logger.getLogger(FXMLdangkyController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
    }
    private void addEventClickBtnXoaDichVu() {
        btnXoaDichVu.setOnAction((event) -> {
            SuDungDv sddv = (SuDungDv) tbSddv_tableView.getSelectionModel().getSelectedItem();
            if (sddv == null) {
                utils.Utils.showError("Chưa chọn dịch vụ", "Hãy chọn dịch vụ muốn xóa!");
                return;
            }
            String sql = "delete from sudungdv where iddv = '" + sddv.getIdDv() + "';";
            if (DB.delete(sql)) {
                utils.Utils.showSuccess("Thành công", "Xóa dịch vụ thành công!");
                tbSddv_initItems(Integer.parseInt(lbMaPdk_dv.getText()));
            } else {
                utils.Utils.showError("Lỗi", "Xóa dịch vụ thất bại!");
            }
        });
    }

    private void init_icons() {
        btnTaoPhieuDangKy.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/create.png"))));
        btnThemKhMoi.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/create.png"))));
        btnThemDichVu.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/create.png"))));
        btnTuDongTaoMaKh.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/auto.png"))));
        btnTuDongTaoMaPdk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/auto.png"))));
        btnLamMoiKh.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/clear.png"))));
        btnLamMoiPdk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/clear.png"))));
        btnLamMoiPhong.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/refresh2.png"))));
        btnCapNhatKhachHang.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/update.png"))));
        btnCapNhatPdk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/update.png"))));
        btnChonKhachHang.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/choose.png"))));
        btnChonPhong.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/choose.png"))));
        btnTimKiemKhachHang.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/search.png"))));
        btnTimKiemPdk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/search.png"))));
        btnXoaDichVu.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/delete2.png"))));
        btnNhanPhong.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/getroom.png"))));
        btnTinhTien.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/tinhtien.png"))));
        btnThanhToan.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/thanhtoan.png"))));
        btnHuyPdk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/delete2.png"))));
        btnLayNgayHomNay_NgayDen.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/today.png"))));
        btnLayNgayHomNay_NgayDk.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/today.png"))));
        btnTinhDenGio.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/clock.png"))));
    }
}
