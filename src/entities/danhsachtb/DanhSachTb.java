/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.danhsachtb;

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
public class DanhSachTb {
    int soPhong;
    int maTb;
    int idTb;

    public DanhSachTb() {
        
    }

    public DanhSachTb(int soPhong, int maTb, int idTb) {
        this.soPhong = soPhong;
        this.maTb = maTb;
        this.idTb = idTb;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getMaTb() {
        return maTb;
    }

    public void setMaTb(int maTb) {
        this.maTb = maTb;
    }

    public int getIdTb() {
        return idTb;
    }

    public void setIdTb(int idTb) {
        this.idTb = idTb;
    }
    
    
    // database
    public static ArrayList<DanhSachTb> querySelectAll() {
        ArrayList<DanhSachTb> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from danhsachtb order by idtb;");
        try {
            while (rs.next()) {
                list.add(new DanhSachTb(rs.getInt("sophong"), 
                                        rs.getInt("matb"), 
                                        rs.getInt("idtb")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static DanhSachTb querySelectOne(int idTb) {
        String sql = "select * from danhsachtb where " 
                + "idtb = '"
                + String.valueOf(idTb)
                + "';";
        ResultSet rs = DB.select(sql);
        try {
            if (rs.next()) {
                return new DanhSachTb(rs.getInt("sophong"), rs.getInt("matb"), rs.getInt("idtb"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
            return new DanhSachTb();
        }
        return new DanhSachTb();
    }
    
    public static boolean queryInsertOne() {
        String sql = "insert into danhsachtb default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int idtb) {
        String sql = "delete from danhsachtb where "
                + "idtb = '"
                + String.valueOf(idtb)
                + "';";
        return DB.delete(sql);
    }
}
