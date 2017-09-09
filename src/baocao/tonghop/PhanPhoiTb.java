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
public class PhanPhoiTb {
    int soPhong;
    int soThietBi;

    public PhanPhoiTb() {
    }

    public PhanPhoiTb(int soPhong, int soThietBi) {
        this.soPhong = soPhong;
        this.soThietBi = soThietBi;
    }

    public int getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(int soPhong) {
        this.soPhong = soPhong;
    }

    public int getSoThietBi() {
        return soThietBi;
    }

    public void setSoThietBi(int soThietBi) {
        this.soThietBi = soThietBi;
    }
    
    ///////////// database
    public static ArrayList<PhanPhoiTb> querySelectAll() {
        ArrayList<PhanPhoiTb> al = new ArrayList<>();
        try {
            String sql = "select sophong, count(*) as tongtb from danhsachtb group by sophong order by sophong;";
            ResultSet rs = DB.select(sql);
            while (rs.next()) {
                al.add(new PhanPhoiTb(rs.getInt("sophong"), rs.getInt("tongtb")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PhanPhoiTb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return al;
    }
}
