package com.library.models;

import com.library.interfaces.Searchable;
import java.util.Objects;

/**
 * A class for Admin object
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class Admin extends AbstractPupil implements Searchable {

    private String workerID;

    public Admin(String workerID, String name, String phone, String email, String address, int age) {
        super(name, phone, email, address, age);
        setWorkerID(workerID);
    }

    // setter
    public final void setWorkerID(String workerID) throws IllegalArgumentException {
        ValidationUtils.checkID(workerID, "Worker ID");

        this.workerID = workerID;
    }
    
    //getter
    public String getWorkerID() {return workerID;}

    @Override
    public boolean isAdmin() {return true;}
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return getName().toLowerCase().contains(q) ||
        getWorkerID().toLowerCase().contains(q) ||
        getEmail().toLowerCase().contains(q);
    }
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