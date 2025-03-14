package com.example.generalfx;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**Some notes for myself:
 * 1. == behind base 64 is base 64 padding (When output not multiple of 24)
 * 2. For PBKDF2WithHmacSHA1 we also have PBKDF2WithHmacSHA256 and PBKDF2WithHmacSHA512
 * 3. Key length (currently 128b [bits]) determines length of output (16B [bytes])
 * -- Base64 (3B = 24b = 4 chars) --> (16+2)B --> 24 chars
 * 4. You could also encode to hex if you want*/

public class Encryption {
    private Encryption() {}

    public static String[] encrypt(String password) {
        try {
            SecureRandom rand = new SecureRandom();
            byte[] salt = new byte[16];
            rand.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();

            // Can't decrypt without salt
            String encryptedSalt= Base64.getEncoder().encodeToString(salt);
            String encryptedHash = Base64.getEncoder().encodeToString(hash);
            return new String[]{encryptedHash, encryptedSalt};

        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }
        return new String[]{"", ""};
    }

    public static boolean verifyPassword(String password, String storedHash, String storedSalt) {
        try {
            byte[] salt = Base64.getDecoder().decode(storedSalt); // Creating a trigger for updating salt in a diff table

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();

            String encodedHash = Base64.getEncoder().encodeToString(hash);
            return encodedHash.equals(storedHash);  // Comparing hashes
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            System.err.println("Verification error: " + e.getMessage());
        }
        return false;
    }
}
