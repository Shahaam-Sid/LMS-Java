package com.library.models;

import java.util.Objects;

import com.library.exceptions.InvalidPasswordException;

/**
 * A class for Admin object
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class Admin extends AbstractPupil {

    private String workerID;
    private String[] saltNHash = new String[2];
    
    public Admin(String workerID, String name, String phone, String email, String address, int age) {
        super(name, phone, email, address, age);
        setWorkerID(workerID);
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
    public String getWorkerID() {return workerID;}
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
}
// => Add auto id generator