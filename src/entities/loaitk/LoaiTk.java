/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.loaitk;

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
public class LoaiTk {
    int maLoaiTk;
    String tenLoaiTk;

    public LoaiTk() {
    }

    public LoaiTk(int maLoaiTk, String tenLoaiTk) {
        this.maLoaiTk = maLoaiTk;
        this.tenLoaiTk = tenLoaiTk;
    }

    public int getMaLoaiTk() {
        return maLoaiTk;
    }

    public void setMaLoaiTk(int maLoaiTk) {
        this.maLoaiTk = maLoaiTk;
    }

    public String getTenLoaiTk() {
        return tenLoaiTk;
    }

    public void setTenLoaiTk(String tenLoaiTk) {
        this.tenLoaiTk = tenLoaiTk;
    }
    
    // query
    public static ArrayList<LoaiTk> querySelectAll() {
        ArrayList<LoaiTk> list = new ArrayList();
        ResultSet rs = DB.select("select * from loaitk order by maloaitk;");
        try {
            while (rs.next()) {
                list.add(new LoaiTk(rs.getInt("maloaitk"), rs.getString("tenloaitk")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiTk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static boolean queryInsertOne() {
        String sql = "insert into loaitk default values;";
        return DB.insert(sql);
    }

    public static boolean queryDeleteOne(int maLoaiTk) {
        String sql = "delete from loaitk where maloaitk = " + String.valueOf(maLoaiTk) + ";";
        return DB.delete(sql);
    }
    
    public boolean queryUpdate(int maLoaiTkCu) {
        String sql = "update loaitk set maloaitk = '"
                + String.valueOf(maLoaiTk)
                +"', tenloaitk = '"
                + tenLoaiTk
                + "' where maloaitk = '"
                + String.valueOf(maLoaiTkCu)
                + "';";
        return DB.update(sql);         
    }
    
    public static String querySelectTenByMa(int maLoaiTk) {
        try {
            String sql = "select tenloaitk from loaitk where maloaitk = " + String.valueOf(maLoaiTk) + ";";
            //System.out.println(sql);
            ResultSet rs = DB.select(sql);
            
            if (rs.next()) {
                return rs.getString("tenloaitk");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLloaitkController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public static int querySelectMaByTen(String tenLoaiTk) {
        try {
            String sql = "select maloaitk from loaitk where tenloaitk = '"
                    + tenLoaiTk + "';";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getInt("maloaitk");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoaiTk.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return tenLoaiTk;
    }
    
    
}
