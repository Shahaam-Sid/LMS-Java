package com.library.exceptions;

public class NoOutputReceivedException extends RuntimeException {
    public NoOutputReceivedException() {
        super("An unexpected error occured, Try again");
    }
}
