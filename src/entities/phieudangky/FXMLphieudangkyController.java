/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phieudangky;

import database.DB;
import entities.khachhang.KhachHang;
import entities.phong.Phong;
import fxmls.LoadingDialog;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class FXMLphieudangkyController implements Initializable {

    @FXML
    private TableView tableView;
    private ObservableList<PhieuDangKy> items;
    
    ArrayList<KhachHang> listKh;
    ArrayList<Integer> listMaKh;
    ArrayList<String> listStringMaKh;
    ArrayList<String> listSoPhong;
    
    DecimalFormat dformat = new DecimalFormat("##0.0");
    
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
            initListKh();
            initListSoPhong();
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
        
        themButton.setOnAction((event) -> {
            LoadingDialog.execute(() -> { 
                ResultSet rs = DB.select("select max(mapdk) as max from phieudangky;");
                int maxPdk = 0;
                try {
                    if (rs.next()) {
                        maxPdk = rs.getInt("max");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLphieudangkyController.class.getName()).log(Level.SEVERE, null, ex);
                }
                maxPdk++;
                boolean ok = DB.insert("insert into phieudangky(mapdk) values ('" + String.valueOf(maxPdk) + "');");
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
                PhieuDangKy pdk = (PhieuDangKy) tableView.getItems().get(index);
                int maPdk = pdk.getMaPdk();
                boolean ok = PhieuDangKy.queryDeleteOne(maPdk);
                if (ok) {
                    items.remove(index);
                }
            }
        });
        
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
        maPdkColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        maPdkColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieudangky set mapdk = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setMaPdk(newValue);
            } else {
                tableView.refresh();
            }
        });
        tableView.getColumns().add(maPdkColumn);
    }
    
    private void initMaKhColumn() {
        maKhColumn = new TableColumn<>("Mã Khách Hàng");
        maKhColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int maPdk = pdk.getMaPdk();
            for (Integer a : listMaKh) {
                if (a.intValue() == pdk.maKh) {
                    int index = listMaKh.indexOf(a);
                    return new SimpleStringProperty(listStringMaKh.get(index));
                }
            }
            
            return new SimpleStringProperty();
        });
        ObservableList<String> olKh = FXCollections.observableList(listStringMaKh);
        maKhColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olKh));
        maKhColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();
            int index = listStringMaKh.indexOf(newValue);
            
            int maKh = listMaKh.get(index).intValue();

            String sql = "update phieudangky set makh = '"
                    + String.valueOf(maKh)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setMaPdk(maKh);
            } else {
                tableView.refresh();
            }
        });
        maKhColumn.setPrefWidth(250);
        tableView.getColumns().add(maKhColumn);
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
        ngayDkColumn = new TableColumn<>("Ngày Đăng Ký");
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
        ngayDkColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ngayDkColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();
            Date d = checkDateFormat(newValue);
            if (d == null) {
                tableView.refresh();
                return;
            }

            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            
            String sql = "update phieudangky set ngaydk = '"
                    + f.format(d)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setNgayDk(d);
            } else {
                tableView.refresh();
            }
        });
        ngayDkColumn.setPrefWidth(130);
        tableView.getColumns().add(ngayDkColumn);
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
        ngayDenColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        ngayDenColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();
            Date d = checkDateFormat(newValue);
            if (d == null) {
                tableView.refresh();
                return;
            }

            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            
            String sql = "update phieudangky set ngayden = '"
                    + f.format(d)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setNgayDen(d);
            } else {
                tableView.refresh();
            }
        });
        ngayDenColumn.setPrefWidth(130);
        tableView.getColumns().add(ngayDenColumn);
    }

    private void initThoiGianOColumn() {
        thoiGianOColumn = new TableColumn<>("Thời Gian Ở");
        thoiGianOColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int thoiGianO = pdk.getThoiGianO();
            return new SimpleStringProperty(String.valueOf(thoiGianO));
        });
        thoiGianOColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        thoiGianOColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieudangky set thoigiano = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setThoiGianO(newValue);
            } else {
                tableView.refresh();
            }
        });
        thoiGianOColumn.setPrefWidth(130);
        tableView.getColumns().add(thoiGianOColumn);
    }

    private void initTreemColumn() {
        treemColumn = new TableColumn<>("Trẻ Em");
        treemColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int treem = pdk.getTreem();
            return new SimpleStringProperty(String.valueOf(treem));
        });
        treemColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        treemColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieudangky set treem = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setTreem(newValue);
            } else {
                tableView.refresh();
            }
        });
        treemColumn.setPrefWidth(130);
        tableView.getColumns().add(treemColumn);
    }

    private void initNguoiLonColumn() {
        nguoiLonColumn = new TableColumn<>("Người Lớn");
        nguoiLonColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int nguoilon = pdk.getNguoilon();
            return new SimpleStringProperty(String.valueOf(nguoilon));
        });
        nguoiLonColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nguoiLonColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            int newValue = Integer.parseInt(event.getOldValue());
            try {
                newValue = Integer.parseInt(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieudangky set nguoilon = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setNguoilon(newValue);
            } else {
                tableView.refresh();
            }
        });
        nguoiLonColumn.setPrefWidth(130);
        tableView.getColumns().add(nguoiLonColumn);
    }

    private void initSoPhongColumn() {
        soPhongColumn = new TableColumn<>("Số Phòng");
        soPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            int soPhong = pdk.getSoPhong();
            return new SimpleStringProperty(String.valueOf(soPhong));
        });
        ObservableList<String> olSp = FXCollections.observableList(listSoPhong);
        soPhongColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olSp));
        soPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();

            String sql = "update phieudangky set sophong = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setSoPhong(Integer.parseInt(newValue));
            } else {
                tableView.refresh();
            }
        });
        tableView.getColumns().add(soPhongColumn);
    }

    private void initTraTruocColumn() {
        traTruocColumn = new TableColumn<>("Trả Trước");
        traTruocColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            float traTruoc = pdk.getTraTruoc();
            return new SimpleStringProperty(dformat.format(traTruoc));
        });
        traTruocColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        traTruocColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            float newValue = Float.parseFloat(event.getOldValue());
            try {
                newValue = Float.parseFloat(event.getNewValue());
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }
            String sql = "update phieudangky set tratruoc = '"
                    + String.valueOf(newValue)
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setTraTruoc(newValue);
            } else {
                tableView.refresh();
            }
        });
        traTruocColumn.setPrefWidth(170);
        tableView.getColumns().add(traTruocColumn);
    }

    private void initChuThichColumn() {
        chuThichColumn = new TableColumn<>("Chú Thích");
        chuThichColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String chuThich = pdk.getChuThich();
            return new SimpleStringProperty(String.valueOf(chuThich));
        });
        chuThichColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        chuThichColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getOldValue();
            try {
                newValue = event.getNewValue();
            } catch (NumberFormatException e) {
                tableView.refresh();
                return;
            }

            String sql = "update phieudangky set chuthich = '"
                    + newValue
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setChuThich(newValue);
            } else {
                tableView.refresh();
            }
        });
        tableView.getColumns().add(chuThichColumn);
    }

    private void initDaNhanPhongColumn() {
        daNhanPhongColumn = new TableColumn<>("Đã Nhận Phòng");
        daNhanPhongColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String daNhanPhong = pdk.getDaNhanPhong();
            
            return new SimpleStringProperty(daNhanPhong);
        });
        ArrayList<String> alDaNhanPhong = new ArrayList();
        alDaNhanPhong.add("Chưa nhận phòng");
        alDaNhanPhong.add("Đã nhận phòng");
        ObservableList<String> olDaNhanPhong = FXCollections.observableList(alDaNhanPhong);
        daNhanPhongColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olDaNhanPhong));
        daNhanPhongColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();

            String sql = "update phieudangky set danhanphong = '"
                    + newValue
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setDaNhanPhong(newValue);
            } else {
                tableView.refresh();
            }
        });
        daNhanPhongColumn.setPrefWidth(150);
        tableView.getColumns().add(daNhanPhongColumn);
    }

    private void initDaThanhToanColumn() {
        daThanhToanColumn = new TableColumn<>("Đã Thanh Toán");
        daThanhToanColumn.setCellValueFactory((TableColumn.CellDataFeatures<PhieuDangKy, String> param) -> {
            PhieuDangKy pdk = param.getValue();
            String daThanhToan = pdk.getDaThanhToan();
            
            return new SimpleStringProperty(daThanhToan);
        });
        ArrayList<String> alDaThanhToan = new ArrayList();
        alDaThanhToan.add("Chưa thanh toán");
        alDaThanhToan.add("Đã thanh toán");
        ObservableList<String> olDaThanhToan = FXCollections.observableList(alDaThanhToan);
        daThanhToanColumn.setCellFactory(ComboBoxTableCell.forTableColumn(olDaThanhToan));
        daThanhToanColumn.setOnEditCommit((TableColumn.CellEditEvent<PhieuDangKy, String> event) -> {
            int row = event.getTablePosition().getRow();
            PhieuDangKy pdk = items.get(row);
            String newValue = event.getNewValue();

            String sql = "update phieudangky set dathanhtoan = '"
                    + newValue
                    + "' where mapdk = '"
                    + String.valueOf(pdk.getMaPdk())
                    + "';";
            boolean ok = DB.update(sql);
            if (ok) {
                pdk.setDaThanhToan(newValue);
            } else {
                tableView.refresh();
            }
        });
        daThanhToanColumn.setPrefWidth(150);
        tableView.getColumns().add(daThanhToanColumn);
    }
    
    private void initItems() {
        LoadingDialog.execute(() -> {
            initListKh();
            initListSoPhong();
            items = FXCollections.observableArrayList(PhieuDangKy.querySelectAll());
            Platform.runLater(() -> {
                tableView.setItems(items);
            });
        });
    }

}
