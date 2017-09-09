/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.doanhthudichvu;

import database.DB;
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
public class DoanhThuDichVu {
    int maDv;
    String dichVu;
    float giaDichVu;
    int soLanDuocDat;
    int soLuongDuocDat;
    float tongTienThuDuoc;
    float doanhThuTbNgay;

    public DoanhThuDichVu() {
    }

    public DoanhThuDichVu(int maDv, String dichVu, float giaDichVu, int soLanDuocDat, int soLuongDuocDat, float tongTienThuDuoc, float doanhThuTbNgay) {
        this.maDv = maDv;
        this.dichVu = dichVu;
        this.giaDichVu = giaDichVu;
        this.soLanDuocDat = soLanDuocDat;
        this.soLuongDuocDat = soLuongDuocDat;
        this.tongTienThuDuoc = tongTienThuDuoc;
        this.doanhThuTbNgay = doanhThuTbNgay;
    }

    public int getSoLanDuocDat() {
        return soLanDuocDat;
    }

    public void setSoLanDuocDat(int soLanDuocDat) {
        this.soLanDuocDat = soLanDuocDat;
    }

    public int getMaDv() {
        return maDv;
    }

    public void setMaDv(int maDv) {
        this.maDv = maDv;
    }

    public String getDichVu() {
        return dichVu;
    }

    public void setDichVu(String dichVu) {
        this.dichVu = dichVu;
    }

    public float getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(float giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public int getSoLuongDuocDat() {
        return soLuongDuocDat;
    }

    public void setSoLuongDuocDat(int soLuongDuocDat) {
        this.soLuongDuocDat = soLuongDuocDat;
    }

    public float getTongTienThuDuoc() {
        return tongTienThuDuoc;
    }

    public void setTongTienThuDuoc(float tongTienThuDuoc) {
        this.tongTienThuDuoc = tongTienThuDuoc;
    }

    public float getDoanhThuTbNgay() {
        return doanhThuTbNgay;
    }

    public void setDoanhThuTbNgay(float doanhThuTbNgay) {
        this.doanhThuTbNgay = doanhThuTbNgay;
    }
    
    //////////////// database
    public static float queryTongDoanhThuDichVu() {
        try {
            String sql = "select sum(tiendichvu) as tongtiendichvu from phieuthanhtoan;";
            ResultSet rs = DB.select(sql);
            float value = 0;
            if (rs.next()) {
                value = rs.getFloat("tongtiendichvu");
                return value;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (float)0;
    }
    public static float queryTongDoanhThuDichVu(String tuNgay, String denNgay) {
        try {
            String sql = "select sum(tiendichvu) as tongtiendichvu from phieuthanhtoan where ngaytt >= '" + tuNgay + "' and ngaytt <= '" +denNgay + "';";
            ResultSet rs = DB.select(sql);
            float value = 0;
            if (rs.next()) {
                value = rs.getFloat("tongtiendichvu");
                return value;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (float)0;
    }
    public static Date queryMinDate() {
        try {
            ResultSet rs = DB.select("select min(ngaytt) as min from phieuthanhtoan;");
            if (rs.next()) {
                Date da = rs.getDate("min");
                if (da != null) {
                    return da;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date(System.currentTimeMillis());
    }
    public static Date queryMaxDate() {
        try {
            ResultSet rs = DB.select("select max(ngaytt) as max from phieuthanhtoan;");
            if (rs.next()) {
                Date da = rs.getDate("max");
                if (da != null) {
                    return da;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date(System.currentTimeMillis());
    }
    
    public static ArrayList<DoanhThuDichVu> querySelectAll() {
        ArrayList<DoanhThuDichVu> al = new ArrayList<>();
        try {
            String sql = "select madv, tendv, giadv, solan, soluong, (giadv * soluong) as tongtien from dichvu natural left join "
                    +"(select madv, count(*) as solan, sum(soluong) as soluong from sudungdv where mapdk in (select mapdk from phieuthanhtoan) group by madv) as dsdv;";
//            System.out.println(sql);
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new DoanhThuDichVu(rs.getInt("madv"),
                        rs.getString("tendv"), 
                        rs.getFloat("giadv"),
                        rs.getInt("solan"),
                        rs.getInt("soluong"),
                        rs.getFloat("tongtien"),
                        (float)0));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
    
    public static ArrayList<DoanhThuDichVu> querySelectAll(String tuNgay, String denNgay) {
        ArrayList<DoanhThuDichVu> al = new ArrayList<>();
        try {
            String sql = "select madv, tendv, giadv, solan, soluong, (giadv * soluong) as tongtien from dichvu natural left join "
                    +"(select madv, count(*) as solan, sum(soluong) as soluong from sudungdv where mapdk in (select mapdk from phieuthanhtoan where ngaytt >= '" + tuNgay + "' and ngaytt <= '" + denNgay + "') group by madv) as dsdv;";
//            System.out.println(sql);
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new DoanhThuDichVu(rs.getInt("madv"),
                        rs.getString("tendv"), 
                        rs.getFloat("giadv"),
                        rs.getInt("solan"),
                        rs.getInt("soluong"),
                        rs.getFloat("tongtien"),
                        (float)0));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuDichVu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
}
