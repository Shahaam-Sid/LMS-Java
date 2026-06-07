package com.shahaam.lms.utils;

public class ValidationUtils {

    protected static boolean isValidISBN(String isbn) {
        int n = isbn.length();

        if (n == 10) {
            return isValidISBN10(isbn);
        } else if (n == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }
    private static boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = isbn.charAt(i) - '0';
            if (digit < 0 || digit > 9)
                return false;
            sum += digit * (10 - i);
        }
        char last = isbn.charAt(9);
        if (last != 'X' && (last < '0' || last > '9'))
            return false;
        sum += (last == 'X') ? 10 : (last - '0');
        return sum % 11 == 0;
    }
    private static boolean isValidISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0';
            if (digit < 0 || digit > 9)
                return false;
            sum += (i % 2 == 0) ? digit : digit * 3; // alternating weights 1 and 3
        }
        int checkDigit = isbn.charAt(12) - '0';
        if (checkDigit < 0 || checkDigit > 9)
            return false;
        return (sum + checkDigit) % 10 == 0;
    }

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