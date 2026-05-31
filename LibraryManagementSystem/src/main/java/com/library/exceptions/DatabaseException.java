package com.library.exceptions;

public class DatabaseException extends RuntimeException {

    private final int errorCode;
    private final String userMessage;

    public DatabaseException(int errorCode, Throwable cause) {
        super(resolveUserMessage(errorCode), cause);
        this.errorCode = errorCode;
        this.userMessage = resolveUserMessage(errorCode);
    }

    public String getUserMessage() {
        return userMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    private static String resolveUserMessage(int errorCode) {
        return switch (errorCode) {
            case 1062 -> "This record already exists. Please check for duplicates.";
            case 1451 -> "This record is linked to other data and cannot be removed.";
            case 1452 -> "The referenced record does not exist.";
            case 1048 -> "Some required fields are missing. Please fill in all details.";
            case 1213 -> "A conflict occurred. Please try again.";
            case 1205 -> "The system is busy. Please try again in a moment.";
            case 1044, 1045 -> "You do not have permission to perform this action.";
            default   -> "Something went wrong. Please try again or contact support.";
        };
    }
}