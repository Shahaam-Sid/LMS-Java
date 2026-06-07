package com.shahaam.lms.exceptions;

public class DuplicatePupilException extends RuntimeException{
    public DuplicatePupilException(String id) {
        super("Person already Exists");
    }
}