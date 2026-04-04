package com.library.interfaces;

/**
 * Interface for Searchable Classes
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public interface Searchable {
    /**
     * Checks if the query matches in the fields
     * @param query to be searched
     * @return true if found else false
     */
    boolean matchesQuery(String query);
}