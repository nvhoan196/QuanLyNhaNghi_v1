/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.dichvu;

import database.DB;
import entities.taikhoan.TaiKhoan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class DichVu {
    int maDv;
    String tenDv;
    float giaDv;

    public DichVu() {
    }

    public DichVu(int maDv, String tenDv, float giaDv) {
        this.maDv = maDv;
        this.tenDv = tenDv;
        this.giaDv = giaDv;
    }

    public int getMaDv() {
        return maDv;
    }

    public void setMaDv(int maDv) {
        this.maDv = maDv;
    }

    public String getTenDv() {
        return tenDv;
    }

    public void setTenDv(String tenDv) {
        this.tenDv = tenDv;
    }

    public float getGiaDv() {
        return giaDv;
    }

    public void setGiaDv(float giaDv) {
        this.giaDv = giaDv;
    }
    
     // database
    public static ArrayList<DichVu> querySelectAll() {
        ArrayList<DichVu> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from dichvu order by madv;");
        try {
            while (rs.next()) {
                list.add(new DichVu(rs.getInt("madv"),
                        rs.getString("tendv"),
                        rs.getFloat("giadv")
                        )
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public static boolean queryInsertOne() {
        String sql = "insert into dichvu default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maDv) {
        String sql = "delete from dichvu where "
                + "madv = '"
                + String.valueOf(maDv)
                + "';";
        return DB.delete(sql);
    }

    @Override
    public String toString() {
        return maDv + " - " + tenDv;
    }
    
    
}
