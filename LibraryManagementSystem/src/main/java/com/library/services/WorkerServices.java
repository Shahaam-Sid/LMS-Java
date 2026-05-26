package com.library.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.db.DBConnection;
import com.library.db.DBUtility;
import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.MemberNotFoundException;
import com.library.exceptions.WorkerNotFoundException;
import com.library.models.Admin;

/**
 * Class for WorkerServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class WorkerServices {

    private static final String TABLE = "workers";
    private static final String UIDCOL = "worker_id";

    /**
     * Checks if database is empty
     * @return true if empty, else not
     * @throws SQLException Error from Database
     */
    public boolean isEmpty() {return DBUtility.isEmpty(TABLE);}

    /**
     * register new member
     * @param worker to register
     * @throws DuplicatePupilException if worker already exist
     * @throws RuntimeException if query not executes correctly
     * @throws SQLException Error from Database
     */
    public void admitWorker(Admin worker) throws DuplicatePupilException {
        
        try (Connection conn = DBConnection.getConnection()) {
            if (DBUtility.doesRowExists(TABLE, UIDCOL, worker.getWorkerID(), conn))
                throw new DuplicatePupilException(worker.getWorkerID());
            int workerTableAffected;
            int SnHTableAffected;
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO workers VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setString(1, worker.getWorkerID());
                    ps.setString(2, worker.getName());
                    ps.setString(3, worker.getPhone());
                    ps.setString(4, worker.getEmail());
                    ps.setString(5, worker.getAddress());
                    ps.setInt(6, worker.getAge());

                    workerTableAffected = ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO salt_n_hash VALUES (?, ?, ?)")) {
                    ps.setString(1, worker.getWorkerID());
                    ps.setString(2, worker.getSalt());
                    ps.setString(3, worker.getHash());

                    SnHTableAffected = ps.executeUpdate();
                }

                if ((SnHTableAffected + workerTableAffected) == 2) conn.commit();
                else conn.rollback();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    /**
     * get admin object
     * @param id of worker to get
     * @return admin
     * @throws WorkerNotFoundException if worker not found
     * @throws SQLException Error from Database
     */
    public Admin getWorker(String id) throws WorkerNotFoundException {
        String sql = """
                SELECT *
                FROM workers INNER JOIN salt_n_hash
                ON workers.worker_id = salt_n_hash.worker_id
                WHERE workers.worker_id = ?;
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapAdminFromDB(rs);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        throw new WorkerNotFoundException(id);
    }
    public void updateWorker(String targetId, String name, String phone, String email, String address,
        String password) throws MemberNotFoundException, IllegalArgumentException, Exception {
            Admin worker = getWorker(targetId);

            try (Connection conn = DBConnection.getConnection()) {
                int rowsAffected = 0;
                int countChangesMade = 0;
                try {
                    conn.setAutoCommit(false);

                    if ((name != null && !name.isEmpty()) && !name.equals(worker.getName())) {
                        countChangesMade++;
                        worker.setName(name);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE workers SET worker_name = ? WHERE worker_id = ?")) {
                            ps.setString(1, worker.getName());
                            ps.setString(2, worker.getWorkerID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((phone != null && !phone.isEmpty()) && !phone.equals(worker.getPhone())) {
                        countChangesMade++;
                        worker.setPhone(phone);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE workers SET phone = ? WHERE worker_id = ?")) {
                            ps.setString(1, worker.getPhone());
                            ps.setString(2, worker.getWorkerID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((email != null && !email.isEmpty()) && !email.equals(worker.getEmail())) {
                        countChangesMade++;
                        worker.setEmail(email);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE workers SET email = ? WHERE worker_id = ?")) {
                            ps.setString(1, worker.getEmail());
                            ps.setString(2, worker.getWorkerID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((address != null && !address.isEmpty()) && !address.equals(worker.getAddress())) {
                        countChangesMade++;
                        worker.setAddress(address);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE workers SET address = ? WHERE worker_id = ?")) {
                            ps.setString(1, worker.getAddress());
                            ps.setString(2, worker.getWorkerID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if (password != null && !password.isEmpty()) {
                        countChangesMade++;
                        worker.setPassword(password);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE salt_n_hash SET password_salt = ?, password_hash = ? WHERE worker_id = ?")) {
                            ps.setString(1, worker.getSalt());
                            ps.setString(2, worker.getHash());
                            ps.setString(3, worker.getWorkerID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    if (countChangesMade == rowsAffected) conn.commit();
                    else conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                DBUtility.SQLExceptionLoop(e);
            }
        }
    /**
     * removes worker
     * @param id of worker to remove
     * @throws WorkerNotFoundException if worker not found
     */
    public void removeWorker(String id) throws WorkerNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            if (!DBUtility.doesRowExists(TABLE, UIDCOL, id, conn))
                throw new WorkerNotFoundException(id);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM workers WHERE worker_id = ?")) {
                ps.setString(1, id);
                int output = ps.executeUpdate();
                if (output == 0) throw new RuntimeException("An Unexpected Error Occured");
            }
            
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    /**
     * searches for worker
     * @param query string for worker to search
     * @return list of results matched
     */
    public List<Admin> searchWorker(String query) {
        List<Admin> workers = new ArrayList<>();
        String sql = """
                SELECT *
                FROM workers 
                INNER JOIN salt_n_hash ON workers.worker_id = salt_n_hash.worker_id
                WHERE 
                    CAST(workers.worker_id AS CHAR) LIKE LOWER(CONCAT('%', ?, '%')) OR
                    LOWER(worker_name) LIKE LOWER(CONCAT('%', ?, '%')) OR
                    LOWER(email) LIKE LOWER(CONCAT('%', ?, '%')) OR
                    LOWER(phone) LIKE LOWER(CONCAT('%', ?, '%'))
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ps.setString(4, query);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) workers.add(mapAdminFromDB(rs));
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return workers;
    }
    /**
     * returns list of all workers
     * @return list of workers
     */
    public List<Admin> getAllWorkers() {
        List<Admin> workers = new ArrayList<>();
        String sql = """
                SELECT *
                FROM workers INNER JOIN salt_n_hash
                ON workers.worker_id = salt_n_hash.worker_id
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) workers.add(mapAdminFromDB(rs));
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return workers;
    }

    public static Admin mapAdminFromDB(ResultSet rs) throws IllegalArgumentException, SQLException{
        if (rs == null) throw new IllegalArgumentException("Invalid Response from Database");

        Admin w = new Admin(rs.getString("worker_id"), rs.getString("worker_name"), 
        rs.getString("phone"), rs.getString("email"), rs.getString("address"), rs.getInt("age"));

        w.setSalt(rs.getString("password_salt"));
        w.setHash(rs.getString("password_hash"));

        return w;
    }
}

// !! UPDATE ALL RELATIONS IN TABLES