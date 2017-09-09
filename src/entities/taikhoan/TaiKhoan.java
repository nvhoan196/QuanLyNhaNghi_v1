/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.taikhoan;

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
public class TaiKhoan {
    
    ///////////////////////////////
    String username = "";
    String password = "";
    int maLoaiTk = -1;
    String firstName = "";
    String lastName = "";
    String gioiTinh = "Không xác định";
    String cmnd = "";
    String diaChi = "";
    String email = "";

    public TaiKhoan() {
        
    }
    
    public TaiKhoan(String username,
            String password,
            int maLoaiTk,
            String firstName,
            String lastName,
            String gioiTinh,
            String cmnd,
            String diaChi,
            String email) {
        this.username = username;
        this.password = password;
        this.maLoaiTk = maLoaiTk;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gioiTinh = gioiTinh;
        this.cmnd = cmnd;
        this.diaChi = diaChi;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaLoaiTk() {
        return maLoaiTk;
    }

    public void setMaLoaiTk(int maLoaiTk) {
        this.maLoaiTk = maLoaiTk;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    // database
    public static ArrayList<TaiKhoan> querySelectAll() {
        ArrayList<TaiKhoan> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from taikhoan order by username;");
        try {
            while (rs.next()) {
                list.add(new TaiKhoan(rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("maloaitk"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("gioitinh"),
                        rs.getString("cmnd"),
                        rs.getString("diachi"),
                        rs.getString("email")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    public static TaiKhoan querySelectOne(String username) {
        String sql = "select * from taikhoan where " 
                + "username = '"
                + username
                + "';";
        ResultSet rs = DB.select(sql);
        try {
            if (rs.next()) {
                return new TaiKhoan(rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("maloaitk"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("gioitinh"),
                        rs.getString("cmnd"),
                        rs.getString("diachi"),
                        rs.getString("email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
            return new TaiKhoan();
        }
        return new TaiKhoan();
    }
    
    public static boolean queryInsertOne(String username, String password) {
        String sqlRole = "create role " + username + " with superuser login password '" + password + "';";
//        System.out.println(sqlRole);
        String sql = "insert into taikhoan(username, password, maloaitk) values('"+username+"','"+password+"','2');";
//        System.out.println(sqlRole);
//        System.out.println(sql);
        if (DB.insert(sqlRole)) {
            if (DB.insert(sql)) {
                return true;
            } else {
                DB.insert("drop role if exists " + username);
            }
        }
        
        return false;
    }
    
    public static boolean queryDeleteOne(String username) {
        String sql = "delete from taikhoan where "
                + "username = '"
                +username
                + "';";
        if (DB.insert("drop role if exists " + username)) {
            if (DB.delete(sql)) {
                return true;
            }
        }
        return false; 
    }
    
    public boolean queryUpdate(String usernameCu) {
        String sql = "update taikhoan set username = '"
                + username
                + "', password = '"
                + password
                + "', maloaitk = '"
                + String.valueOf(maLoaiTk)
                + "', firstname = '"
                + firstName
                + "', lastname = '"
                + lastName
                + "', gioitinh = '"
                + gioiTinh
                + "', cmnd = '"
                + cmnd
                + "', diachi = '"
                + diaChi
                + "', email = '"
                + email
                + "' where username = '"
                + usernameCu
                + "';";
        return DB.update(sql);   
    }
    
    public boolean queryUpdateInfo() {
        String sql = "update taikhoan set firstname = '"
                + firstName
                + "', lastname = '"
                + lastName
                + "', gioitinh = '"
                + gioiTinh
                + "', cmnd = '"
                + cmnd
                + "', diachi = '"
                + diaChi
                + "', email = '"
                + email
                + "' where username = '"
                + username
                + "';";
        System.out.println(sql);
        return DB.update(sql);   
    }

    @Override
    public String toString() {
        return username + "-" + password + "-" + maLoaiTk + "-" + firstName + "-" + lastName + "-" + gioiTinh +"-" + cmnd + "-" + email + "-"; //To change body of generated methods, choose Tools | Templates.
    }

    
}
