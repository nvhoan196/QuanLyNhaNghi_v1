/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.sudungdv;

import database.DB;
import entities.phong.Phong;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class SuDungDv {
    int idDv;
    int maPdk;
    int maDv;
    int soLuong;

    public SuDungDv() {
    }

    public SuDungDv(int idDv, int maPdk, int maDv, int soLuong) {
        this.idDv = idDv;
        this.maPdk = maPdk;
        this.maDv = maDv;
        this.soLuong = soLuong;
    }

    public int getIdDv() {
        return idDv;
    }

    public void setIdDv(int idDv) {
        this.idDv = idDv;
    }

    public int getMaPdk() {
        return maPdk;
    }

    public void setMaPdk(int maPdk) {
        this.maPdk = maPdk;
    }

    public int getMaDv() {
        return maDv;
    }

    public void setMaDv(int maDv) {
        this.maDv = maDv;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    
    
    
    // database
    public static ArrayList<SuDungDv> querySelectAll() {
        ArrayList<SuDungDv> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from sudungdv order by iddv;");
        try {
            while (rs.next()) {
                list.add(new SuDungDv(rs.getInt("iddv"), 
                                        rs.getInt("mapdk"), 
                                        rs.getInt("madv"), rs.getInt("soluong")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static SuDungDv querySelectOne(int iddv) {
        String sql = "select * from sudungdv where " 
                + "iddv = '"
                + String.valueOf(iddv)
                + "';";
        ResultSet rs = DB.select(sql);
        try {
            if (rs.next()) {
                return new SuDungDv(rs.getInt("iddv"), rs.getInt("mapdk"), rs.getInt("madv"), rs.getInt("soluong"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
            return new SuDungDv();
        }
        return new SuDungDv();
    }
    
    public static boolean queryInsertOne() {
        String sql = "insert into sudungdv default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int iddv) {
        String sql = "delete from sudungdv where "
                + "iddv = '"
                + String.valueOf(iddv)
                + "';";
        return DB.delete(sql);
    }
    
    public static float queryTienDichVu(int maPdk) {
        try {
            String sql = "select sum(giadv * soluong) as tongtien from sudungdv natural join dichvu where mapdk = '" + maPdk + "';";
            System.out.println(sql);
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getFloat("tongtien");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SuDungDv.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (float)0;
    }
}
