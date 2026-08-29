package com.shahaam.lms.exceptions;

public class NoBorrowRecordFoundException extends RuntimeException {
    public NoBorrowRecordFoundException(String loanID) {
        super("No Borrow Record Found for loan ID: " + loanID);
    }
}
