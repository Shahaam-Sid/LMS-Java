package com.library.exceptions;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String memberID) {
        super("No member found with ID: " + memberID);
    }
}