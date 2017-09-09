/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.tonghop;

import database.DB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class BaoCaoTongHop {
    public static int tongSoThietBi() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from danhsachtb;");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    public static int tongKhachHang() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from khachhang;");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    public static int tongSoPhong() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from phong;");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static int tongSoPhongTrong() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from phong where trangthai = 'Đang trống';");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static int tongSoPhongDaDat() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from phong where trangthai = 'Đã đặt phòng';");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static int tongSoPhongDaNhan() {
        int value = 0;
        try {
            
            ResultSet rs = DB.select("select count(*) as tong from phong where trangthai = 'Đã nhận phòng';");
            if (rs.next()) {
                value = rs.getInt("tong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static String phongDatNhat() {
        String value = "";
        try {
            
            ResultSet rs = DB.select("select sophong from phong where giaphong in (select max(giaphong) from phong);");
            while (rs.next()) {
                value += rs.getInt("sophong") + "; ";
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static String phieuTtCaoNhat() {
        String value = "";
        try {
            
            ResultSet rs = DB.select("select maptt, tongtien from phieuthanhtoan where tongtien in (select max(tongtien) from phieuthanhtoan);");
            if (rs.next()) {
                DecimalFormat df = new DecimalFormat("###,###,##0.0");
                int ma = rs.getInt("maptt");
                float tt = rs.getFloat("tongtien");
                value = rs.getInt("maptt") + " (" + df.format(tt) + " VNĐ)";
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static String dichVuUaChuongNhat() {
        String value = "";
        try {
            String sql = "with dichvu_soluong as\n"
                    + "	(select madv, sum(soluong) as soluong from sudungdv group by madv) "
                    + "select tendv from dichvu natural join (select madv from dichvu_soluong where soluong in (select max(soluong) from dichvu_soluong)) as ds;";
            ResultSet rs = DB.select(sql);
            System.out.println(sql);
            while (rs.next()) {
                value = rs.getString("tendv") + "; ";
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
    
    public static String khachHangThanhToanNhieuNhat() {
        String value = "";
        try {
            String sql = "with 	tongtt as "
                    + "(select makh, sum(tongtien) as tongtien from phieuthanhtoan natural join phieudangky natural join khachhang group by makh), "
                    + "dsmakh as "
                    + "(select makh from tongtt where tongtien in (select max(tongtien) from tongtt)) "
                    + "select firstname, lastname, makh from khachhang where makh in(select * from dsmakh);";
            ResultSet rs = DB.select(sql);
            System.out.println(sql);
            while (rs.next()) {
                value = rs.getString("lastname") + " " + rs.getString("firstname") + " (" + rs.getInt("makh") + ");";
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaoCaoTongHop.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
}
