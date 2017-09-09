/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.loaiphong;

import database.DB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class LoaiPhong {
    
    ///////////////////////////////
    int maLoaiP = -1;
    String tenLoaiP = "";

    public LoaiPhong() {
        
    }

    public LoaiPhong(int maLoaiP, String tenLoaiP) {
        this.maLoaiP = maLoaiP;
        this.tenLoaiP = tenLoaiP;
    }

    public int getMaLoaiP() {
        return maLoaiP;
    }

    public void setMaLoaiP(int maLoaiP) {
        this.maLoaiP = maLoaiP;
    }

    public String getTenLoaiP() {
        return tenLoaiP;
    }

    public void setTenLoaiP(String tenLoaiP) {
        this.tenLoaiP = tenLoaiP;
    }
    
    // database
    public static ArrayList<LoaiPhong> querySelectAll() {
        ArrayList<LoaiPhong> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from loaiphong order by maloaip;");
        try {
            while (rs.next()) {
                list.add(new LoaiPhong(rs.getInt("maloaip"),
                        rs.getString("tenloaip")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static LoaiPhong querySelectOne(int maLoaiP) {
        String sql = "select * from loaiphong where " 
                + "maloaip = '"
                + String.valueOf(maLoaiP)
                + "';";
        ResultSet rs = DB.select(sql);
        try {
            if (rs.next()) {
                return new LoaiPhong(rs.getInt("maloaip"),
                        rs.getString("tenloaip"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiPhong.class.getName()).log(Level.SEVERE, null, ex);
            return new LoaiPhong();
        }
        return new LoaiPhong();
    }
    
    public static boolean queryInsertOne() {
        String sql = "insert into loaiphong default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maLoaiP) {
        String sql = "delete from loaiphong where "
                + "maloaip = '"
                + String.valueOf(maLoaiP)
                + "';";
        return DB.delete(sql);
    }
    
    public static int queryMaLoaiPByTenLoaiP(String tenLoaiP) {
        try {
            String sql = "select maloaip from loaiphong where tenloaip = '" + tenLoaiP + "';";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getInt("maloaip");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static String queryTenLoaiPByMaLoaiP(int maLoaiP) {
        try {
            String sql = "select tenloaip from loaiphong where maloaip = '" + String.valueOf(maLoaiP) + "';";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getString("tenloaip");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
