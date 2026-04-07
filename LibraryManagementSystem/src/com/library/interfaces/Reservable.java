package com.library.interfaces;

import com.library.models.Member;

/**
 * interface for reservable books
 */
public interface Reservable {
    /**
     * reserve a book for a member
     * @param member that reserves
     * @return true if successful else false
     */
    boolean reserve(Member member);
    /**
     * cancels a reservation
     * @param member that cancels reservation
     * @return true if successful else false
     */
    boolean cancelReservation(Member member);
    /**
     * returns the Position of reserver in queue
     * @param member that reserves
     * @return position
     */
    int getQueuePosition(Member member);
    /**
     * returns the list of all Members in Queue
     * @return members
     */
    public String getQueue();
}