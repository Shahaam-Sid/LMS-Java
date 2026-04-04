package com.library.exceptions;

public class MemberLimitExceededException extends RuntimeException {
    public MemberLimitExceededException(String memberId) {
        super("Member " + memberId + " has reached the borrow limit of 3 books.");
    }
}