/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baocao.doanhthuphong;

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
public class DoanhThuPhong {
    int soPhong;
    int tongPdk;
    int tongPtt;
    float tongTienPhong;
    float tongTienDv;
    float tongDoanhThuPhong;

    public DoanhThuPhong() {
    }

    public DoanhThuPhong(int soPhong, int tongPdk, int tongPtt, float tongTienPhong, float tongTienDv, float tongDoanhThuPhong) {
        this.soPhong = soPhong;
        this.tongPdk = tongPdk;
        this.tongPtt = tongPtt;
        this.tongTienPhong = tongTienPhong;
        this.tongTienDv = tongTienDv;
        this.tongDoanhThuPhong = tongDoanhThuPhong;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getTongPdk() {
        return tongPdk;
    }

    public void setTongPdk(int tongPdk) {
        this.tongPdk = tongPdk;
    }

    public int getTongPtt() {
        return tongPtt;
    }

    public void setTongPtt(int tongPtt) {
        this.tongPtt = tongPtt;
    }

    public float getTongTienPhong() {
        return tongTienPhong;
    }

    public void setTongTienPhong(float tongTienPhong) {
        this.tongTienPhong = tongTienPhong;
    }

    public float getTongTienDv() {
        return tongTienDv;
    }

    public void setTongTienDv(float tongTienDv) {
        this.tongTienDv = tongTienDv;
    }

    public float getTongDoanhThuPhong() {
        return tongDoanhThuPhong;
    }

    public void setTongDoanhThuPhong(float tongDoanhThuPhong) {
        this.tongDoanhThuPhong = tongDoanhThuPhong;
    }

    
    
    //// database
    public static ArrayList<DoanhThuPhong> queryDoanhThuPhong(String tuNgay, String denNgay) {
        ArrayList<DoanhThuPhong> al = new ArrayList<>();
        try {
            String sql = "select sophong, tongpdk, tongptt, tongtienphong, tongtiendichvu, (tongtienphong+tongtiendichvu) as tongdoanhthuphong from phong natural left join "
                    + "(select sophong, count(*) as tongpdk from phieudangky where ngaydk >= '" + tuNgay + "' and ngaydk <= '" + denNgay + "' group by sophong) as pdk "
                    + "natural left join "
                    + "(select sophong, count(*) as tongptt, sum(tienphong) as tongtienphong, sum(tiendichvu) as tongtiendichvu from phieuthanhtoan natural join phieudangky where ngaytt >= '" + tuNgay + "' and ngaytt <= '" + denNgay + "' group by sophong) as ptt order by sophong;";
//        System.out.println(sql);
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new DoanhThuPhong(rs.getInt("sophong"),
                        rs.getInt("tongpdk"),
                        rs.getInt("tongptt"),
                        rs.getFloat("tongtienphong"),
                        rs.getFloat("tongtiendichvu"), 
                        rs.getFloat("tongdoanhthuphong")));
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
    
    public static ArrayList<DoanhThuPhong> queryDoanhThuPhong() {
        ArrayList<DoanhThuPhong> al = new ArrayList<>();
        try {
            String sql = "select sophong, tongpdk, tongptt, tongtienphong, tongtiendichvu, (tongtienphong+tongtiendichvu) as tongdoanhthuphong from phong natural left join "
                    + "(select sophong, count(*) as tongpdk from phieudangky group by sophong) as pdk "
                    + "natural left join "
                    + "(select sophong, count(*) as tongptt, sum(tienphong) as tongtienphong, sum(tiendichvu) as tongtiendichvu from phieuthanhtoan natural join phieudangky group by sophong) as ptt order by sophong;";
//        System.out.println(sql);
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new DoanhThuPhong(rs.getInt("sophong"),
                        rs.getInt("tongpdk"),
                        rs.getInt("tongptt"),
                        rs.getFloat("tongtienphong"),
                        rs.getFloat("tongtiendichvu"), 
                        rs.getFloat("tongdoanhthuphong")));
            }
            return al;
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
    
    public static float queryTongTienPhong() {
        try {
            String sql = "select sum(tienphong) as tongtienphong from phieuthanhtoan;";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getFloat("tongtienphong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0f;
    }
    
    public static float queryTongTienPhong(String tuNgay, String denNgay) {
        try {
            String sql = "select sum(tienphong) as tongtienphong from phieuthanhtoan where ngaytt >= '" + tuNgay + "' and ngaytt <= '" + denNgay +"';";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getFloat("tongtienphong");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0f;
    }
    
    public static float queryTongTienDichVu() {
        try {
            String sql = "select sum(tiendichvu) as tongtiendichvu from phieuthanhtoan;";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getFloat("tongtiendichvu");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0f;
    }
    
    public static float queryTongTienDichVu(String tuNgay, String denNgay) {
        try {
            String sql = "select sum(tiendichvu) as tongtiendichvu from phieuthanhtoan where ngaytt >= '" + tuNgay + "' and ngaytt <= '" + denNgay +"';";
            ResultSet rs = DB.select(sql);
            if (rs.next()) {
                return rs.getFloat("tongtiendichvu");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DoanhThuPhong.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0f;
    }
}
