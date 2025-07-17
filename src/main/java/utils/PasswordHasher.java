package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {

    /**
     * Hashes a given password using MD5 algorithm.
     * Note: For production environments, stronger hashing algorithms like BCrypt or Argon2 should be used.
     * This is a simple implementation for demonstration purposes.
     * @param password The plain text password to hash.
     * @return The hashed password as a Base64 encoded string.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            // This should ideally not happen for MD5
            throw new RuntimeException("MD5 algorithm not found.", e);
        }
    }

    /**
     * Verifies a plain text password against a hashed password.
     * @param plainPassword The plain text password.
     * @param hashedPassword The hashed password to compare against.
     * @return true if the plain password matches the hashed password, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedPlainPassword = hashPassword(plainPassword);
        return hashedPlainPassword.equals(hashedPassword);
    }
}
