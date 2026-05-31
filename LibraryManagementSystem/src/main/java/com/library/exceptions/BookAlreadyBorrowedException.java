package com.library.exceptions;

public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(String title, String memberID) {
        super("Book " + title + " is already borrowed by Member " + memberID);
    }
}