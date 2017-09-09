/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phong;

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
public class Phong {
    
    int soPhong;
    int maLoaiP;
    float giaPhong;
    String trangThai;

    public Phong() {
        
    }

    public Phong(int soPhong, int maLoaiP, float giaPhong, String trangThai) {
        this.soPhong = soPhong;
        this.maLoaiP = maLoaiP;
        this.giaPhong = giaPhong;
        this.trangThai = trangThai;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getMaLoaiP() {
        return maLoaiP;
    }

    public void setMaLoaiP(int maLoaiP) {
        this.maLoaiP = maLoaiP;
    }

    public float getGiaPhong() {
        return giaPhong;
    }

    public void setGiaPhong(float giaPhong) {
        this.giaPhong = giaPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    
    
    // database
    public static ArrayList<Phong> querySelectAll() {
        ArrayList<Phong> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from phong order by sophong;");
        try {
            while (rs.next()) {
                list.add(new Phong(rs.getInt("sophong"),
                        rs.getInt("maloaip"),
                        rs.getFloat("giaphong"),
                        rs.getString("trangthai")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static ArrayList<Phong> querySelectAllEmpty() {
        ArrayList<Phong> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from phong where trangthai = 'Đang trống' order by sophong;");
        try {
            while (rs.next()) {
                list.add(new Phong(rs.getInt("sophong"),
                        rs.getInt("maloaip"),
                        rs.getFloat("giaphong"),
                        rs.getString("trangthai")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static Phong querySelectOne(int soPhong) {
        String sql = "select * from phong where " 
                + "sophong = '"
                + String.valueOf(soPhong)
                + "';";
        ResultSet rs = DB.select(sql);
        try {
            if (rs.next()) {
                return new Phong(rs.getInt("sophong"),
                        rs.getInt("maloaip"),
                        rs.getFloat("giaphong"),
                        rs.getString("trangthai"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
            return new Phong();
        }
        return new Phong();
    }
    
    public static boolean queryInsertOne() {
        String sql = "insert into phong default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int soPhong) {
        String sql = "delete from phong where "
                + "sophong = '"
                + String.valueOf(soPhong)
                + "';";
        return DB.delete(sql);
    }
    
    public static float queryTienPhong(int soPhong) {
        try {
            String sql = "select giaphong from phong where sophong = '" + soPhong + "';";
            ResultSet rs = DB.select(sql);
            float tienPhong = 0;
            if (rs.next()) {
                tienPhong = rs.getFloat("giaphong");
                return tienPhong;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Phong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (float)0;
    }

    @Override
    public String toString() {
        return soPhong + "-" + maLoaiP + "-" + giaPhong + "-" + trangThai; //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
