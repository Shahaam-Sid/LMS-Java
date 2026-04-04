package com.library.models;

import com.library.enums.MemberStatus;
import com.library.interfaces.Searchable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member extends AbstractPupil implements Searchable {

    protected String memberID;
    protected MemberStatus status;
    protected List<Transaction> borrowedTransaction;

    public static final int MAX_BORROW_LIMIT = 3;

    public Member(String memberID, String name, String phone, String email, String address, int age) {
        super(name, phone, email, address, age);
        setMemberID(memberID);
        this.status = MemberStatus.ACTIVE;
        this.borrowedTransaction = new ArrayList<>();
    }

    // setter
    public final void setMemberID(String memberID) {
        checkID(memberID, "Member ID");

        this.memberID = memberID;
    }
    //getter
    public String getWorkerID() {return memberID;}

    /**
     * checks if member has borrow's left
     * @return true if member is active and has less then 3 borrows
     */
    public boolean canBorrow() {
        return status == MemberStatus.ACTIVE && borrowedTransaction.size() < MAX_BORROW_LIMIT;
    }
    /**
     * add new transaction
     * @param t transaction
     */
    public void addTransaction(Transaction t) {borrowedTransaction.add(t);}
    /**
     * removes transaction
     * @param t transaction
     */
    public void removeTransaction(Transaction t) {borrowedTransaction.remove(t);}
    /**
     * get borrowed count list
     * @return number of current borrows
     */
    public int getBorrowedCount() {return borrowedTransaction.size();}
    /**
     * get list of borrows
     * @return list of books
     */
    public String getBorrowedList() {return borrowedTransaction.toString();} 


    @Override
    public boolean isAdmin() {return false;}
    @Override
    public boolean matchesQuery(String query) {
        String q = query.toLowerCase();
        return name.toLowerCase().contains(q) ||
        memberID.toLowerCase().contains(q) ||
        email.toLowerCase().contains(q);
    }
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