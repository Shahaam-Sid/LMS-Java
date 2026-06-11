package com.shahaam.lms.exceptions;

public class InvalidBookTypeException extends RuntimeException {
    public InvalidBookTypeException(String title) {
        super("Book " + title + " is not valid");
    }
    public InvalidBookTypeException() {
        super("Book is not valid");
    }
}
