package com.library.exceptions;

public class CannotDeleteEntityException extends RuntimeException {
    public CannotDeleteEntityException(String entity, String id) {
        super("Cannot delete " + entity + " " + id + ", has an active transaction");
    }   
}