/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phieuthanhtoan;

import database.DB;
import entities.taikhoan.TaiKhoan;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class PhieuThanhToan {
    int maPtt;
    Date ngayTt;
    int maPdk;
    float tienPhong;
    float tienDichVu;
    float tongTien;

    public PhieuThanhToan() {
    }

    public PhieuThanhToan(int maPtt, Date ngayTt, int maPdk, float tienPhong, float tienDichVu, float tongTien) {
        this.maPtt = maPtt;
        this.ngayTt = ngayTt;
        this.maPdk = maPdk;
        this.tienPhong = tienPhong;
        this.tienDichVu = tienDichVu;
        this.tongTien = tongTien;
    }

    public int getMaPtt() {
        return maPtt;
    }

    public void setMaPtt(int maPtt) {
        this.maPtt = maPtt;
    }

    public Date getNgayTt() {
        return ngayTt;
    }

    public void setNgayTt(Date ngayTt) {
        this.ngayTt = ngayTt;
    }

    public int getMaPdk() {
        return maPdk;
    }

    public void setMaPdk(int maPdk) {
        this.maPdk = maPdk;
    }

    public float getTienPhong() {
        return tienPhong;
    }

    public void setTienPhong(float tienPhong) {
        this.tienPhong = tienPhong;
    }

    public float getTienDichVu() {
        return tienDichVu;
    }

    public void setTienDichVu(float tienDichVu) {
        this.tienDichVu = tienDichVu;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    
    
    // database
    public static ArrayList<PhieuThanhToan> querySelectAll() {
        ArrayList<PhieuThanhToan> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from phieuthanhtoan order by maptt;");
        try {
            while (rs.next()) {
                list.add(new PhieuThanhToan(rs.getInt("maptt"), 
                                            rs.getDate("ngaytt"), 
                                            rs.getInt("mapdk"), 
                                            rs.getFloat("tienphong"), 
                                            rs.getFloat("tiendichvu"), 
                                            rs.getFloat("tongtien"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public static boolean queryInsertOne() {
        String sql = "insert into phieuthanhtoan default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maPtt) {
        String sql = "delete from phieuthanhtoan where "
                + "maptt = '"
                + String.valueOf(maPtt)
                + "';";
        return DB.delete(sql);
    }
    
}
