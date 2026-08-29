package com.shahaam.lms.exceptions;

import java.time.LocalDateTime;


public class ErrorResponse {
    private final int status;
    private final String error;
    private final String msg;
    private final String path;
    private final LocalDateTime timeStamp;

    public ErrorResponse(int status, String error, String msg, String path) {
        this.error = error;
        this.msg = msg;
        this.path = path;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }

    public int getStatus() {return status;}
    public String getError() {return error;}
    public String getMessage() {return msg;}
    public String getPath() {return path;}
    public LocalDateTime getTimeStamp() {return timeStamp;}
}