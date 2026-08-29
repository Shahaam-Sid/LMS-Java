package com.shahaam.lms.utils;

import com.shahaam.lms.exceptions.InvalidPasswordException;

public class PasswordUtils {

    public static void validate(String password) {

        if (password == null || password.length() < 8)
            throw new InvalidPasswordException("Password must be at least 8 characters");

        boolean hasUpper   = false;
        boolean hasLower   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;

        String specialChars = "!@#$%^&*()-_=+[]{}|;:',.<>?/`~";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))           hasUpper   = true;
            else if (Character.isLowerCase(c))      hasLower   = true;
            else if (Character.isDigit(c))          hasDigit   = true;
            else if (specialChars.indexOf(c) >= 0)  hasSpecial = true;
        }

        if (!hasUpper)   throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        if (!hasLower)   throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        if (!hasDigit)   throw new InvalidPasswordException("Password must contain at least one digit");
        if (!hasSpecial) throw new InvalidPasswordException("Password must contain at least one special character");
    }
}