package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class PasswordHasher {

    /**
     * Hashes a given password using MD5 algorithm.
     * This is for demonstration purposes and matches the current SQL schema.
     * For production, consider stronger hashing algorithms like SHA-256 or bcrypt.
     * @param password The plain text password.
     * @return The MD5 hashed password as a hexadecimal string.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Example main method for testing
    public static void main(String[] args) {
        String password = "password123";
        String hashedPassword = hashPassword(password);
        System.out.println("Original: " + password);
        System.out.println("Hashed (MD5): " + hashedPassword);
        // Expected for "password123": e10adc3949ba59abbe56e057f20f883e
    }
}
    