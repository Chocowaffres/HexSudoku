/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hexsudoku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    
    // SQLite connection string
    private static final String URL = "jdbc:sqlite:sudoku.db";
    
    public static void createTables() {
        
        // SQL statement for creating user table
        String sqlUser = "CREATE TABLE IF NOT EXISTS user ("
                + " id integer PRIMARY KEY AUTOINCREMENT,"
                + " nome varchar(50) NOT NULL,"
                + " password varchar(50) NOT NULL"
                + ");";
        
        // SQL statement for creating user table
        String sqlStats = "CREATE TABLE IF NOT EXISTS stat ("
                + " id integer PRIMARY KEY AUTOINCREMENT,"
                + " dificuldade varchar(50) NOT NULL,"
                + " tempo varchar(50) NOT NULL,"
                + " iduser integer NOT NULL, FOREIGN KEY(iduser) REFERENCES user(id)"
                + ");";
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            // create user table
            stmt.execute(sqlUser);
            // create stat table
            stmt.execute(sqlStats);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            System.out.println(e.getMessage());
            }
        }
    }
    
}
