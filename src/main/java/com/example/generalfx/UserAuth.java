package com.example.generalfx;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;

enum PermissionStatus {
    ROOT, ADMIN, USER
}

public class UserAuth {
    private static String encrypt(String password) {
        try {
            SecureRandom rand = new SecureRandom();
            byte[] salt = new byte[16];
            rand.nextBytes(salt);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            try {
                byte[] hash = f.generateSecret(spec).getEncoded();
                return new String(hash);

            } catch (InvalidKeySpecException e) {
                throw new RuntimeException(e); // Error handling
            }

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to find encryption algorithm."); // Crash it?
            System.exit(1);
            return null;
        }
    }

    public static void addUser(String username, String password) {
        PermissionStatus defaultPermission = PermissionStatus.USER; // For other permissions?

        String sqlInsert = "INSERT INTO UserAuth (UserName, UserPassword, UserPermissionLevel) VALUES (?, ?, ?)";
//        String sqlInsert = "INSERT INTO hello (UserName, UserPassword, UserPermissionLevel) VALUES (?, ?, ?)";
        try {
            Connection connection = MySQLDatabase.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, encrypt(password));
            preparedStatement.setInt(3, defaultPermission.ordinal());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully");

            connection.close();

        } catch (SQLException e) {
            System.out.println("Failed to add user"); // Resort to offline?
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        addUser("hanch", "password");
    }
}
