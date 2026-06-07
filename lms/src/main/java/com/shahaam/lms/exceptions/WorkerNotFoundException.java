package com.shahaam.lms.exceptions;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(String workerID) {
        super("No worker found with ID: " + workerID);
    }
}