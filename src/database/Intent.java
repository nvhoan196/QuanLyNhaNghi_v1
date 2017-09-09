/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author Admin
 */
public class Intent {
    
    MucDich mucDich;
    Object ob;

    public Intent(MucDich mucDich, Object ob) {
        this.mucDich = mucDich;
        this.ob = ob;
    }

    public Intent() {
    }

    public MucDich getMucDich() {
        return mucDich;
    }

    public void setMucDich(MucDich mucDich) {
        this.mucDich = mucDich;
    }

    public Object getOb() {
        return ob;
    }

    public void setOb(Object ob) {
        this.ob = ob;
    }
    
    public enum MucDich {
        THEM, XOA, SUA
    }
}
