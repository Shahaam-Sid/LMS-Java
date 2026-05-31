package com.library.models;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Objects;

import com.library.exceptions.DatabaseException;
import com.library.exceptions.InvalidPasswordException;

/**
 * A class for Admin object
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class Admin extends AbstractPupil {

    private String workerID;
    private String[] saltNHash = new String[2];
    
    public Admin(String name, String phone, String email, String address, int age) throws SQLException {
        this(generateWorkerIDString(), name, phone, email, address, age);
    }
    public Admin(String workerID, String name, String phone, String email, String address, int age) {
        super(name, phone, email, address, age);
        setWorkerID(workerID);

    }
    public Admin(String name, String phone, String email, String address, int age, String password) throws Exception, SQLException {
        this(generateWorkerIDString(), name, phone, email, address, age, password);
    }
    public Admin(String workerID, String name, String phone, String email, String address, int age, String password) throws Exception {
        this(workerID, name, phone, email, address, age);
        setPassword(password);
    }

    // setter
    public final void setWorkerID(String workerID) throws IllegalArgumentException {
        ValidationUtils.checkID(workerID, "Worker ID");

        this.workerID = workerID;
    }
    public final void setPassword(String password) throws InvalidPasswordException, Exception {
        PasswordUtils.validate(password);
        saltNHash = PasswordUtils.hashPassword(password);
    }
    public final void setSalt(String salt) {
        saltNHash[0] = salt;
    }
    public final void setHash(String hash) {
        saltNHash[1] = hash;
    }
    
    //getter
    public final String getWorkerID() {return workerID;}
    public String getSalt() {return saltNHash[0];}
    public String getHash() {return saltNHash[1];}

    @Override
    public boolean isAdmin() {return true;}

    @Override
    public String toString() {
        return "Worker ID: " + workerID + "\n" + super.toString();
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Admin)) return false;
        Admin otherPupil = (Admin) other; 

        return (workerID.equals(otherPupil.workerID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(workerID);
    }
    /**
     * generates workerID String
     * @return String workerID
     * @throws DatabaseException from Database
     */
    public static String generateWorkerIDString() throws DatabaseException {
        StringBuilder sb = new StringBuilder("W");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String yearString = String.valueOf(year);
        sb.append(yearString.substring(2));

        int c = AbstractPupil.countRowsPerCurrYear("workers");

        sb.append(String.format("%06d", c));
        return sb.toString();
    }
}