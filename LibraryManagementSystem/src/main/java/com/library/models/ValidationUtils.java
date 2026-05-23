package com.library.models;

public class ValidationUtils {
    /**
     * a static helper method to check for empty strings
     * @param string 
     * @param field field of information
     * @throws IllegalArgumentException if string is null or length is 0 or only contains whitespaces
     */
    public static void checkNullString(String string, String field) throws IllegalArgumentException {
        if (string == null || string.trim().length() == 0)
            throw new IllegalArgumentException("Invalid " + field);
    }
    /**
     * a static helper method to check to validate length of strings
     * @param string
     * @param lengthMin minimun acceptable length
     * @param lengthMax maximum acceptable length
     * @param field field of information
     * @throws IllegalArgumentException if length is not in acceptable range
     */
    public static void checkLengthString(String string, int lengthMin, int lengthMax, String field) throws IllegalArgumentException {
        if (string.length() < lengthMin || string.length() > lengthMax)
            throw new IllegalArgumentException("Allowed Length for " + field + ": " + lengthMin + " - " + lengthMax);
    }
    /**
     * method to check and validate ID
     * @param id
     * @param field field, Member ID or Worker ID
     * @throws IllegalArgumentException if ID is not valid
     */
    public static void checkID(String id, String field) throws IllegalArgumentException {
        checkNullString(id, field);
        checkLengthString(id, 9, 9, field);

        for (char c : id.toCharArray())
            if (!Character.isLetterOrDigit(c))
                throw new IllegalArgumentException("Invalid " + field);
    }
}