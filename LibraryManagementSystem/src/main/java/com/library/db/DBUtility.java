package com.library.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class DBUtility {

    private static final Set<String> ALLOWED_TABLES = Set.of("books", "members", "workers",
        "transactions", "reservations", "salt_n_hash");
    private static final Set<String> ALLOWED_UIDS = Set.of("isbn", "worker_id", "member_id", "transaction_id");

    public static boolean isEmpty(String table) throws IllegalArgumentException{
        if (!ALLOWED_TABLES.contains(table)) throw new IllegalArgumentException("Invalid Table Type");

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM "+ table + " LIMIT 1");
        ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return true;
        } catch (SQLException e) {
            SQLExceptionLoop(e);
        }
        return false;
    }
    public static boolean doesRowExists(String table,String uniqueIdCol, String id, Connection conn) 
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
    public static void SQLExceptionLoop(SQLException e) {
        while (e != null) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: "     + e.getSQLState());
            System.out.println("VendorError: "  + e.getErrorCode());
            e = e.getNextException();
        }
    }
}
