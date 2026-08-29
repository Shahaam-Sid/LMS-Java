package com.shahaam.lms.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String reason) {
        super("Invalid Password: " + reason);
    }   
}