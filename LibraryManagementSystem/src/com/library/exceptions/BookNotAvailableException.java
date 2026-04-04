package com.library.exceptions;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(String title) {
        super("Book " + title + " is currently not available");
    }
}