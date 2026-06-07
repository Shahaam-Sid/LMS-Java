package com.shahaam.lms.interfaces;

import java.time.LocalDateTime;

import com.shahaam.lms.models.Member;

/**
 * interface for borrowing Services
 */
public interface BorrowingService {
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
     * calculate Due Date
     * @return Due Date
     */
    LocalDateTime calculateDueDate();
}