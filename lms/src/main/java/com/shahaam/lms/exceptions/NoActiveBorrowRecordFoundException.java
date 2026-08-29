package com.shahaam.lms.exceptions;

public class NoActiveBorrowRecordFoundException extends RuntimeException {
    public NoActiveBorrowRecordFoundException(String title, String memberID) {
        super("No Active Borrow Record Found for Book: " + title + " | Member ID: " + memberID);
    }
    public NoActiveBorrowRecordFoundException(String loanID) {
        super("No Active Borrow Record Found for loan ID: " + loanID);
    } 
}