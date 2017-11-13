/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author javierfuenteshuertas
 */
public class ConnectionDB {
    final String url="jdbc:mysql://localhost:3306/";
    final String dbName="kipudb";
    final String driver="com.mysql.jdbc.Driver";
    final String user="usuario";
    final String password="*******";
    
    public ConnectionDB(){
        try {
            Class.forName(driver).newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection connect(Connection conn){
        try {
            conn=DriverManager.getConnection(url+dbName, user, password);
            //if(!conn.isClosed())System.out.println("Conectado a" +dbName);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Existen problemas al conectar con la BD en " + url);
            System.exit(1);
        }
        return conn;
    }
}
