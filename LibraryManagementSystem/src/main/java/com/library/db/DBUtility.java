package com.library.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.library.exceptions.DatabaseException;
import com.library.exceptions.NoOutputReceivedException;

/**
 * A Class for Database Utility
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class DBUtility {

    private static final Set<String> ALLOWED_TABLES = Set.of("books", "members", "workers",
        "transactions", "reservations", "salt_n_hash");
    private static final Set<String> ALLOWED_UIDS = Set.of("isbn", "worker_id", "member_id", "transaction_id");

    /**
     * Checks if table is empty or not
     * @param table to check for empty, Allowed: "books", "members", "workers", "transactions", "reservations", "salt_n_hash"
     * @return true if empty else false
     * @throws IllegalArgumentException if invalid table type is given 
     * @throws DatabaseException from Database
     */
    public static boolean isEmpty(String table) throws IllegalArgumentException, DatabaseException {
        if (!ALLOWED_TABLES.contains(table)) throw new IllegalArgumentException("Invalid Table Type");

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM "+ table + " LIMIT 1");
        ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return true;
        } catch (SQLException e) {
            throw new DatabaseException(e.getErrorCode(), e);
        }
        return false;
    }
    /**
     * does specific row exist in table
     * @param table to check, Allowed: "books", "members", "workers", "transactions", "reservations", "salt_n_hash"
     * @param uniqueIdCol Primary key of that table, Allowed: "isbn", "worker_id", "member_id", "transaction_id"
     * @param ID target id
     * @param conn Database Connection
     * @return true if exists else false
     * @throws IllegalArgumentException if invalid table or uniqueIdCol given
     * @throws SQLException from Database
     */
    public static boolean doesRowExists(String table, String uniqueIdCol, String id, Connection conn) 
    throws IllegalArgumentException, SQLException {
        if (!ALLOWED_TABLES.contains(table)) throw new IllegalArgumentException("Invalid Table Type");
        if (!ALLOWED_UIDS.contains(uniqueIdCol)) throw new IllegalArgumentException("Invalid Unique Identifier");
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE " + uniqueIdCol + " = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return true;
            }
        }
        return false;
    }
    /**
     * count number of rows from table
     * @param table to count rows in
     * @return int number of counts
     * @throws IllegalArgumentException invalid table given
     * @throws SQLException from Database
     */
    public static int countRows(String table) throws IllegalArgumentException, SQLException{
        if (!ALLOWED_TABLES.contains(table)) throw new IllegalArgumentException("Invalid Table Type");
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + table);
        ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        throw new NoOutputReceivedException();
    }
    /**
     * Iterates and Prints All Errors from Database
     * @param e error
     */
    public static void SQLExceptionLoop(SQLException e) {
        while (e != null) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: "     + e.getSQLState());
            System.out.println("VendorError: "  + e.getErrorCode());
            e = e.getNextException();
        }
    }
}