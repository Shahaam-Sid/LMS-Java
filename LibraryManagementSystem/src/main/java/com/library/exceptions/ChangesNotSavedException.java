package com.library.exceptions;

public class ChangesNotSavedException extends RuntimeException {
    public ChangesNotSavedException() {
        super("Changes were not saved due to an unexpected error, Check or retry");
    }
}
