/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.khachhang;

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
public class KhachHang {
    int maKh;
    String firstName;
    String lastName;
    String gioitinh;
    String sdt;
    String cmnd;
    String diaChi;
    String quocTich;
    String email;

    public KhachHang() {
    }

    public KhachHang(int maKh, String firstName, String lastName, String gioitinh, String sdt, String cmnd, String diaChi, String quocTich, String email) {
        this.maKh = maKh;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.diaChi = diaChi;
        this.quocTich = quocTich;
        this.email = email;
    }

    public int getMaKh() {
        return maKh;
    }

    public void setMaKh(int maKh) {
        this.maKh = maKh;
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

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
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

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // database
    public static ArrayList<KhachHang> querySelectAll() {
        ArrayList<KhachHang> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from khachhang order by makh;");
        try {
            while (rs.next()) {
                list.add(
                        new KhachHang(rs.getInt("makh"),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("gioitinh"),
                                rs.getString("sdt"),
                                rs.getString("cmnd"), 
                                rs.getString("diachi"),
                                rs.getString("quoctich"), 
                                rs.getString("email"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public static boolean queryInsertOne() {
        String sql = "insert into khachhang default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maKh) {
        String sql = "delete from khachhang where "
                + "makh = '"
                + String.valueOf(maKh)
                + "';";
        return DB.delete(sql);
    }
    
    public boolean insertToDatabase() {
        String sql = "insert into khachhang(makh, firstname, lastname, gioitinh, sdt, cmnd, diachi, quoctich, email) values('"+maKh+"', '"+firstName+"', '"+lastName+"', '"+gioitinh+"', '"+sdt+"', '"+cmnd+"', '"+diaChi+"', '"+quocTich+"', '"+email+"');";
        return DB.insert(sql);
    }
    
    public boolean updateToDatabase(int maKhCu) {
        String sql = "update khachhang set "
                + "makh = '" + maKh + "', "
                + "firstname = '" + firstName + "', "
                + "lastname = '" + lastName + "', "
                + "gioitinh = '" + gioitinh + "', "
                + "sdt = '" + sdt + "', "
                + "cmnd = '" + cmnd + "', "
                + "diachi = '" + diaChi + "', "
                + "quoctich = '" + quocTich + "', "
                + "email = '" + email + "' where makh = '" + maKhCu + "';";
//        System.out.println(sql);
        return DB.update(sql);
    }
    
    public static KhachHang querySelectOne(int maKh) {
        try {
            String sql = "select * from khachhang where makh = '" + maKh + "';";
            ResultSet rs =  DB.select(sql);
            if (rs.next()) {
                return new KhachHang(rs.getInt("makh"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("gioitinh"),
                        rs.getString("sdt"),
                        rs.getString("cmnd"),
                        rs.getString("diachi"),
                        rs.getString("quoctich"),
                        rs.getString("email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhachHang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new KhachHang();
    }

    @Override
    public String toString() {
        return maKh + " - " + lastName + " " + firstName + " - " + cmnd;
    }
    
    
}
