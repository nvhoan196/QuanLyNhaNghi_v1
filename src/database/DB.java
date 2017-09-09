/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Admin
 */
public class DB {
    public static final String className = "org.postgresql.Driver";
    public static Connection connection = null;
    public static Statement stmt = null;
    public static ResultSet rs = null;
    
    
    public static String dbName = "", 
            user = "",
            password = "",
            ip = "",
            port = "";
    
    public DB() {}
    
//    public DB(String dbName, String user, String password) {
//        DB.dbName = dbName;
//        DB.user = user;
//        DB.password = password;
//    }
    
    public static void setInfo(String dbName, String user, String password, String ip, String port) {
        DB.dbName = dbName;
        DB.user = user;
        DB.password = password;
        DB.ip = ip;
        DB.port = port;
    }
    
    public static boolean connect() {  
        try {
            Class.forName(className);
            connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":"+port+"/" + dbName, user, password);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Class.forName() failed!");
            return false;
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Driver.getConnection() failed!");
            return false;
        }
        
        if (connection == null) {
//            System.out.println("Ket noi KHONG THANH CONG!");
            return false;
        } else {
//            System.out.println("Ket noi THANH CONG!");
            return true;
        }
    }
    
    public static boolean closeRSandSTMT() {
        try {
            if (rs!=null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("rs.close() and stmt.close() failed!");
            return false;
        }
        return true;
    }
    
    public static boolean closeConnect() {
        try {
            if (connection != null) {
            connection.close();
            connection = null;
            }
//            if (rs != null || stmt != null) {
//                if(closeRSandSTMT() == false) return false;
//            }
        } catch (SQLException ex) {
            System.out.println("connection.close() failed!");
            return false;
        }
        return true;
    }
    
    public static ResultSet select(String sql) {
        connect();
        if (connection == null) return null;
        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Select failed!");
            closeRSandSTMT();
            return null;
        }
        closeConnect();
        return rs;
    }
    
    public static boolean insert(String sql) {
        connect();
        if (connection == null) return false;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            connection.commit();
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Select failed!");
            closeRSandSTMT();
            return false;
        }
        closeConnect();
        return true;
    }
    
    public static boolean update(String sql) {
        connect();
        if (connection == null) return false;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            connection.commit();
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Update failed!");
            closeRSandSTMT();
            return false;
        }
        closeConnect();
        return true;
    }
    
    public static boolean delete(String sql) {
        connect();
        if (connection == null) return false;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            connection.commit();
        } catch (SQLException ex) {
            //Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Delete failed!");
            closeRSandSTMT();
            return false;
        }
        closeConnect();
        return true;
    }
    
    public static boolean isConnected() {
        return connection != null;
    }
    
}
