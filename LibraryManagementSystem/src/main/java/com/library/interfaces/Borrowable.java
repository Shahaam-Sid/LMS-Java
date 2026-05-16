package com.library.interfaces;

import com.library.models.Member;
import java.time.LocalDate;

/**
 * interface for borrowable books
 */
public interface Borrowable {
    /**
     * Method to borrow a book
     * @param member that borrows
     * @return true if successful else false
     */
    boolean borrow(Member member);
    /**
     * Method to return a book
     * @param member that returns
     * @return true if successful else false
     */
    boolean returnItem(Member member);
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
    /**
     * calculate Due Date
     * @return Due Date
     */
    LocalDate calculateDueDate();
}