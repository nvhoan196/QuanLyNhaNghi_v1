/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmls;

import entities.loaitk.LoaiTk;
import entities.taikhoan.TaiKhoan;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FXMLMainFormController implements Initializable {

    public TaiKhoan taiKhoan = new TaiKhoan();
    
    @FXML
    private Label lblHoTen;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblLoaiTk;
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane danhMucTitlePane;
    @FXML
    private Accordion accordionNghiepVu;
    @FXML
    private TitledPane nghiepVuTitlePane;
    
    @FXML
    private Hyperlink hlTaiKhoan;
    @FXML
    private Hyperlink hlLoaiTaiKhoan;
    @FXML
    private Hyperlink hlLoaiPhong;
    @FXML
    private Hyperlink hlPhong;
    @FXML
    private Hyperlink hlThietBi;
    @FXML
    private Hyperlink hlDichVu;
    @FXML
    private Hyperlink hlKhachHang;
    @FXML
    private Hyperlink hlPhieuDangKy;
    @FXML
    private Hyperlink hlDanhSachTb;
    @FXML
    private Hyperlink hlSuDungDv;
    @FXML
    private Hyperlink hlPhieuThanhToan;
    @FXML
    private Hyperlink hlDatPhong;
    
    //////////// MENU
    @FXML private Menu mnDanhMuc;
    @FXML private Menu mnBaoCao;
    
    //////////// MENU ITEM
    @FXML
    private MenuItem miTaiKhoan;
    @FXML
    private MenuItem miLoaiTaiKhoan;
    @FXML
    private MenuItem miLoaiPhong;
    @FXML
    private MenuItem miPhong;
    @FXML
    private MenuItem miThietBi;
    @FXML
    private MenuItem miDichVu;
    @FXML
    private MenuItem miKhachHang;
    @FXML
    private MenuItem miPhieuDangKy;
    @FXML
    private MenuItem miDanhSachTb;
    @FXML
    private MenuItem miSuDungDv;
    @FXML
    private MenuItem miPhieuThanhToan;
    @FXML
    private MenuItem miTacGia;
    @FXML
    private MenuItem miTaiKhoanCaNhan;
    @FXML
    private MenuItem miGiaoDichKhachHang;
    @FXML
    private MenuItem miBaoCaoPhong;
    @FXML 
    private MenuItem miBaoCaoDichVu;
    @FXML 
    private MenuItem miBaoCaoTongHop;
    
    /**
     * Initializes the controller class.
     */
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(() -> {
            initThongTinNhanVien();
        });
        initUIs();
        addEvent();
        disableForLeTan();
    }    
    
    private void initThongTinNhanVien() {
        if (taiKhoan.getLastName() != null && taiKhoan.getFirstName() != null)
            lblHoTen.setText(taiKhoan.getLastName()+ " " + taiKhoan.getFirstName());
        if (taiKhoan.getUsername() != null) {
            lblUsername.setText(taiKhoan.getUsername());
        }
        if (taiKhoan.getEmail() != null) {
            lblEmail.setText(taiKhoan.getEmail());
        }
        lblLoaiTk.setText(LoaiTk.querySelectTenByMa(taiKhoan.getMaLoaiTk()));
        disableForLeTan();
    }
    
    private void disableForLeTan() {
        if (taiKhoan.getMaLoaiTk() == 2) {
            accordion.setDisable(true);
            mnDanhMuc.setDisable(true);
            mnBaoCao.setDisable(true);
        }
    }
    
    private void addEvent() {
        hlLoaiTaiKhoan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Loại Tài Khoản", "/entities/loaitk/FXMLloaitk.fxml");
            });
        });  
        hlTaiKhoan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Tài Khoản", "/entities/taikhoan/FXMLtaikhoan.fxml");
            });
        });
        hlLoaiPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Loại Phòng", "/entities/loaiphong/FXMLloaiphong.fxml");
            });
        });
        hlPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phòng", "/entities/phong/FXMLphong.fxml");
            });
        });
        hlThietBi.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Thiết Bị", "/entities/thietbi/FXMLthietbi.fxml");
            });
        });
        hlDichVu.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Dịch Vụ", "/entities/dichvu/FXMLdichvu.fxml");
            });
        });
        hlKhachHang.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Khách Hàng", "/entities/khachhang/FXMLkhachhang.fxml");
            });
        });
        hlPhieuDangKy.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phiếu Đăng Ký", "/entities/phieudangky/FXMLphieudangky.fxml");
            });
        });
        hlDanhSachTb.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Danh Sách Thiết Bị", "/entities/danhsachtb/FXMLdanhsachtb.fxml");
            });
        });
        hlSuDungDv.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Sử Dụng Dịch Vụ", "/entities/sudungdv/FXMLsudungdv.fxml");
            });
        });
        hlPhieuThanhToan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phiếu Thanh Toán", "/entities/phieuthanhtoan/FXMLphieuthanhtoan.fxml");
            });
        });
        hlDatPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Giao Dịch Khách Hàng", "/nghiepvu/dangky/FXMLdangky.fxml");
            });
        });
        
        //////// EVENTS MENU ITEMS
        miLoaiTaiKhoan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Loại Tài Khoản", "/entities/loaitk/FXMLloaitk.fxml");
            });
        });  
        miTaiKhoan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Tài Khoản", "/entities/taikhoan/FXMLtaikhoan.fxml");
            });
        });
        miLoaiPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Loại Phòng", "/entities/loaiphong/FXMLloaiphong.fxml");
            });
        });
        miPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phòng", "/entities/phong/FXMLphong.fxml");
            });
        });
        miThietBi.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Thiết Bị", "/entities/thietbi/FXMLthietbi.fxml");
            });
        });
        miDichVu.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Dịch Vụ", "/entities/dichvu/FXMLdichvu.fxml");
            });
        });
        miKhachHang.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Khách Hàng", "/entities/khachhang/FXMLkhachhang.fxml");
            });
        });
        miPhieuDangKy.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phiếu Đăng Ký", "/entities/phieudangky/FXMLphieudangky.fxml");
            });
        });
        miDanhSachTb.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Danh Sách Thiết Bị", "/entities/danhsachtb/FXMLdanhsachtb.fxml");
            });
        });
        miSuDungDv.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Sử Dụng Dịch Vụ", "/entities/sudungdv/FXMLsudungdv.fxml");
            });
        });
        miPhieuThanhToan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Phiếu Thanh Toán", "/entities/phieuthanhtoan/FXMLphieuthanhtoan.fxml");
            });
        });
        miTacGia.setOnAction((event) -> {
            showTacGia();
        });
        miTaiKhoanCaNhan.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Tài Khoản Cá Nhân", "/menuitems/taikhoancanhan/FXMLtaikhoancanhan.fxml");
            });
        });
        miGiaoDichKhachHang.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Giao Dịch Khách Hàng", "/nghiepvu/dangky/FXMLdangky.fxml");
            });
        });
        miBaoCaoPhong.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Báo cáo doanh thu theo phòng", "/baocao/doanhthuphong/FXMLdoanhthuphong.fxml");
            });
        });
        miBaoCaoDichVu.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Báo cáo doanh thu theo dịch vụ", "/baocao/doanhthudichvu/FXMLdoanhthudichvu.fxml");
            });
        });
        miBaoCaoTongHop.setOnAction((event) -> {
            LoadingDialog.executeNonModal(() -> {
                addTab("Báo cáo tổng hợp", "/baocao/tonghop/FXMLtonghop.fxml");
            });
        });
    }
    
    private void addTab(String title, String urlFxmlFile) {
        
        Platform.runLater(() -> {
            try {
            
                Tab newTab = new Tab(title);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(urlFxmlFile));
                BorderPane root = loader.load();
                newTab.setContent(root);
                tabPane.getTabs().add(newTab);
                tabPane.getSelectionModel().select(tabPane.getTabs().size()-1);
            
            } catch (IOException ex) {
                Logger.getLogger(FXMLMainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
  
    }

    private void showTacGia() {
        try {
            Stage newStage = new Stage();
            newStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/menuitems/tacgia/FXMLtacgia.fxml"));
            BorderPane root = loader.load();
            newStage.setScene(new Scene(root));
            newStage.show();
            newStage.setTitle("Thông tin tác giả");
        } catch (IOException ex) {
            Logger.getLogger(FXMLMainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initUIs() {
        //accordion.setExpandedPane(danhMucTitlePane);
        accordionNghiepVu.setExpandedPane(nghiepVuTitlePane);
    }
    
}
