package com.shahaam.lms.enums;

/**
 * Status for Books
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public enum BookStatus {
    AVAILABLE,
    BORROWED,
    RESERVED,
    LOST,
    UNDER_MAINTENANCE;

    /**
     * checks if book is availabe
     * @return true if available else false
     */
    public boolean canBeBorrowed() {
        return this == AVAILABLE;
    }
}
