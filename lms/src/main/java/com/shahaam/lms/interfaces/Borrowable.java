package com.shahaam.lms.interfaces;

/**
 * interface for borrowable books
 */
public interface Borrowable {
    /**
     * check if book is available
     * @return true if available else false
     */
    boolean isAvailable();
    /**
     * get the number of copies left
     * @return number of copies available
     */
    int getAvailableCount();
}