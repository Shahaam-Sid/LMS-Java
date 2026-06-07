package com.shahaam.lms.utils;

import com.shahaam.lms.exceptions.InvalidPasswordException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {
    private static final int ITERATIONS  = 310_000;
    private static final int KEY_LENGTH  = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    // Hash a new password — call this when registering a worker
    public static String[] hashPassword(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt);

        HexFormat hex = HexFormat.of();
        return new String[]{
            hex.formatHex(salt),   // index 0 → salt
            hex.formatHex(hash)    // index 1 → hash
        };
    }

    // Verify at login — call this when a worker tries to log in
    public static boolean verifyPassword(String password, String storedSalt, String storedHash) throws Exception {
        HexFormat hex = HexFormat.of();
        byte[] salt = hex.parseHex(storedSalt);
        byte[] hash = pbkdf2(password, salt);
        return hex.formatHex(hash).equals(storedHash);
    }

    // Internal helper — runs the actual PBKDF2 computation
    private static byte[] pbkdf2(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }

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