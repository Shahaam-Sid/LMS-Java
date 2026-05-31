package com.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;
/**
 * A Utility Class for Database Connectivity
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class DBConnection {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USERNAME");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    /**
     * loads and returns connection, No need of creating DriverManager Obj everytime
     * @return Connection
     * @throws SQLException from Database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    /**
     * tests Database Connectivity
     * @return true if connected else false
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("DB connection failed: " + e.getMessage());
            return false;
        }
    }
}
