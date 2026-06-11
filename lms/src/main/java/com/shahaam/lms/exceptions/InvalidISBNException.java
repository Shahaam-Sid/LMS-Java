package com.shahaam.lms.exceptions;

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException(String isbn) {
        super("Invalid ISBN Format: " + isbn);
    }
}