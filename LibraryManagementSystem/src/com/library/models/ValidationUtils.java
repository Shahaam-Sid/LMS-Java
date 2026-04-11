package com.library.models;

public class ValidationUtils {
    /**
     * a static helper method to check for empty strings
     * @param string 
     * @param field field of information
     * @throws IllegalArgumentException if string is null or length is 0 or only contains whitespaces
     */
    static void checkNullString(String string, String field) throws IllegalArgumentException {
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
    static void checkLengthString(String string, int lengthMin, int lengthMax, String field) throws IllegalArgumentException {
        if (string.length() < lengthMin || string.length() > lengthMax)
            throw new IllegalArgumentException("Allowed Length for " + field + ": " + lengthMin + " - " + lengthMax);
    }
    /**
     * method to check and validate ID
     * @param id
     * @param field field, Member ID or Worker ID
     * @throws IllegalArgumentException if ID is not valid
     */
    static void checkID(String id, String field) throws IllegalArgumentException {
        checkNullString(id, field);
        checkLengthString(id, 9, 9, field);

        for (char c : id.toCharArray())
            if (!Character.isLetterOrDigit(c))
                throw new IllegalArgumentException("Invalid " + field);
    }
    /**
     * checks if ISBN number is valid
     * @param isbn string
     * @return true if valid else alse
     */
    static boolean isValidISBN(String isbn) {
        // length must be 10
        int n = isbn.length();
        if (n != 10)
            return false;

        // Computing weighted sum
        // of first 9 digits
        int sum = 0;
        for (int i = 0; i < 9; i++) 
        {
        int digit = isbn.charAt(i) - '0';
            if (0 > digit || 9 < digit)
                return false;
            sum += (digit * (10 - i));
        }

        // Checking last digit.
        char last = isbn.charAt(9);
        if (last != 'X' && (last < '0' || 
                            last > '9'))
            return false;

        // If last digit is 'X', add 10 
        // to sum, else add its value
        sum += ((last == 'X') ? 10 : (last - '0'));

        // Return true if weighted sum 
        // of digits is divisible by 11.
        return (sum % 11 == 0);
    }
}