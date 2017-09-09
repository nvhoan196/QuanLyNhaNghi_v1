/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.thietbi;

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
public class ThietBi {
    int maTb;
    String tenTb;

    public ThietBi() {
    }

    public ThietBi(int maTb, String tenTb) {
        this.maTb = maTb;
        this.tenTb = tenTb;
    }

    public int getMaTb() {
        return maTb;
    }

    public void setMaTb(int maTb) {
        this.maTb = maTb;
    }

    public String getTenTb() {
        return tenTb;
    }

    public void setTenTb(String tenTb) {
        this.tenTb = tenTb;
    }
    
    // database
    public static ArrayList<ThietBi> querySelectAll() {
        ArrayList<ThietBi> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from thietbi order by matb;");
        try {
            while (rs.next()) {
                list.add(new ThietBi(rs.getInt("matb"),
                        rs.getString("tentb")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public static boolean queryInsertOne() {
        String sql = "insert into thietbi default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maTb) {
        String sql = "delete from thietbi where "
                + "matb = '"
                + String.valueOf(maTb)
                + "';";
        return DB.delete(sql);
    }

    @Override
    public String toString() {
        return maTb + " - " + tenTb;
    }
    
    
    
}
