/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.tonghop;

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
public class ChiTietTb {
    int idTb;
    String tenTb;

    public ChiTietTb() {
    }

    public ChiTietTb(int idTb, String tenTb) {
        this.idTb = idTb;
        this.tenTb = tenTb;
    }

    public int getIdTb() {
        return idTb;
    }

    public void setIdTb(int idTb) {
        this.idTb = idTb;
    }

    public String getTenTb() {
        return tenTb;
    }

    public void setTenTb(String tenTb) {
        this.tenTb = tenTb;
    }
    
    ////////// database
    public static ArrayList<ChiTietTb> querySelectAll(int phong) {
        ArrayList<ChiTietTb> al = new ArrayList<>();
        try {
            String sql = "select idtb, tentb from thietbi natural join "
                    + "(select * from danhsachtb where sophong = '" + phong + "') as dstb;";
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new ChiTietTb(rs.getInt("idtb"), rs.getString("tentb")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietTb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
    
    public static ArrayList<String> querySelectAllPhong() {
        ArrayList<String> al = new ArrayList<>();
        try {
            String sql = "select distinct sophong from danhsachtb order by sophong;";
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                String value = rs.getString("sophong");
                if (value != null && !value.isEmpty()) {
                    al.add(value);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ChiTietTb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
}
