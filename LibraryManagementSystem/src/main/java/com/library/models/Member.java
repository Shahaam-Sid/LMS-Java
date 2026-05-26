package com.library.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.library.db.DBConnection;
import com.library.db.DBUtility;
import com.library.enums.MemberStatus;
import com.library.services.TransactionServices;
/**
 * A class for Member Object
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class Member extends AbstractPupil {

    private String memberID;
    private MemberStatus status;

    public static final int MAX_BORROW_LIMIT = 3;

    public Member(String memberID, String name, String phone, String email, String address, int age) {
        super(name, phone, email, address, age);
        setMemberID(memberID);
        setStatus(MemberStatus.ACTIVE);
    }

    // setter
    public final void setMemberID(String memberID) throws IllegalArgumentException {
        ValidationUtils.checkID(memberID, "Member ID");

        this.memberID = memberID;
    }
    public final void setStatus(MemberStatus status) {this.status = status;}
    //getter
    public String getMemberID() {return memberID;}
    public String getStatus() {return status.name();}

    /**
     * checks if member has borrow's left
     * @return true if member is active and has less then 3 borrows
     */
    public boolean canBorrow() {
        return status == MemberStatus.ACTIVE && getBorrowedCount() < MAX_BORROW_LIMIT;
    }
    /**
     * add new transaction
     * @param t transaction
     */
    public int getBorrowedCount() {
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM transactions WHERE return_date IS NULL AND member_id = ?")) {
            ps.setString(1, memberID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        throw new RuntimeException("An Unexpected Error Occured");
    }
    /**
     * get list of borrows
     * @return list of transactions
     */
    public String getBorrowedList() {
        List<Transaction> borrowedList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions WHERE return_date IS NULL AND member_id = ?")) {
            ps.setString(1, memberID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) borrowedList.add(TransactionServices.mapTransactionFromDB(rs));
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return borrowedList.toString();
    } 


    @Override
    public boolean isAdmin() {return false;}
    @Override
    public String toString() {
        return "Member ID: " + memberID + "\n" + super.toString();
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Member)) return false;
        Member otherPupil = (Member) other; 

        return (memberID.equals(otherPupil.memberID));
    }
    @Override
    public int hashCode() {
        return Objects.hash(memberID);
    }
}
// => Add auto id generator