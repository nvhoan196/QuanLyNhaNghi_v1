/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.phieudangky;

import database.DB;
import entities.taikhoan.TaiKhoan;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class PhieuDangKy {
    int maPdk;
    int maKh;
    Date ngayDk;
    Date ngayDen;
    int thoiGianO;
    int treem;
    int nguoilon;
    int soPhong;
    float traTruoc;
    String chuThich;
    String daNhanPhong;
    String daThanhToan;

    public PhieuDangKy() {
    }

    public PhieuDangKy(int maPdk, int maKh, Date ngayDk, Date ngayDen, int thoiGianO, int treem, int nguoilon, int soPhong, float traTruoc, String chuThich, String daNhanPhong, String daThanhToan) {
        this.maPdk = maPdk;
        this.maKh = maKh;
        this.ngayDk = ngayDk;
        this.ngayDen = ngayDen;
        this.thoiGianO = thoiGianO;
        this.treem = treem;
        this.nguoilon = nguoilon;
        this.soPhong = soPhong;
        this.traTruoc = traTruoc;
        this.chuThich = chuThich;
        this.daNhanPhong = daNhanPhong;
        this.daThanhToan = daThanhToan;
    }

    public int getMaPdk() {
        return maPdk;
    }

    public void setMaPdk(int maPdk) {
        this.maPdk = maPdk;
    }

    public int getMaKh() {
        return maKh;
    }

    public void setMaKh(int maKh) {
        this.maKh = maKh;
    }

    public Date getNgayDk() {
        return ngayDk;
    }

    public void setNgayDk(Date ngayDk) {
        this.ngayDk = ngayDk;
    }

    public Date getNgayDen() {
        return ngayDen;
    }

    public void setNgayDen(Date ngayDen) {
        this.ngayDen = ngayDen;
    }

    public int getThoiGianO() {
        return thoiGianO;
    }

    public void setThoiGianO(int thoiGianO) {
        this.thoiGianO = thoiGianO;
    }

    public int getTreem() {
        return treem;
    }

    public void setTreem(int treem) {
        this.treem = treem;
    }

    public int getNguoilon() {
        return nguoilon;
    }

    public void setNguoilon(int nguoilon) {
        this.nguoilon = nguoilon;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public float getTraTruoc() {
        return traTruoc;
    }

    public void setTraTruoc(float traTruoc) {
        this.traTruoc = traTruoc;
    }

    public String getChuThich() {
        return chuThich;
    }

    public void setChuThich(String chuThich) {
        this.chuThich = chuThich;
    }

    public String getDaNhanPhong() {
        return daNhanPhong;
    }

    public void setDaNhanPhong(String daNhanPhong) {
        this.daNhanPhong = daNhanPhong;
    }

    public String getDaThanhToan() {
        return daThanhToan;
    }

    public void setDaThanhToan(String daThanhToan) {
        this.daThanhToan = daThanhToan;
    }
    
    // database
    public static ArrayList<PhieuDangKy> querySelectAll() {
        ArrayList<PhieuDangKy> list = new ArrayList<>();
        ResultSet rs = DB.select("select * from phieudangky order by mapdk;");
        try {
            while (rs.next()) {
                list.add(
                        new PhieuDangKy(rs.getInt("mapdk"),
                                rs.getInt("makh"),
                                rs.getDate("ngaydk"), 
                                rs.getDate("ngayden"),
                                rs.getInt("thoigiano"), 
                                rs.getInt("treem"),
                                rs.getInt("nguoilon"),
                                rs.getInt("sophong"),
                                rs.getFloat("tratruoc"),
                                rs.getString("chuthich"),
                                rs.getString("danhanphong"), 
                                rs.getString("dathanhtoan"))
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    
    public static boolean queryInsertOne() {
        String sql = "insert into phieudangky default values;";
        return DB.insert(sql);
    }
    
    public static boolean queryDeleteOne(int maPdk) {
        String sql = "delete from phieudangky where "
                + "mapdk = '"
                + String.valueOf(maPdk)
                + "';";
        System.out.println(sql);
        return DB.delete(sql);
    }

    @Override
    public String toString() {
        if (chuThich != null && !chuThich.isEmpty()) {
            return maPdk + " - " + chuThich;
        }
        return String.valueOf(maPdk);
    }
    
    
}
