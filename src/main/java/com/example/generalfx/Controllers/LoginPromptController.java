package com.example.generalfx.Controllers;

import com.example.generalfx.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.*;

/**0 1 2 in db*/
enum PermissionStatus {
    ROOT, ADMIN, USER
}

public class LoginPromptController {
    private final String DBNAME = "UserAuth";
    private final SceneController sceneController = SceneController.getInstance();
    private final List<String> AUTHPARAMS = List.of("UserName", "UserPassword", "UserSalt");

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Label loginError;

    private void generateError(String errorMessage) {
        loginError.setText(errorMessage);
        loginError.setTextFill(javafx.scene.paint.Color.RED);
        loginError.setVisible(true);
    }

    @FXML
    private void authenticateUser() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            generateError("Username and password cannot be empty");

        } else {
            List<Map<String, Object>> fetch = SQLiteFetch.getElements("UserAuth",
                    List.of(AUTHPARAMS.get(1), AUTHPARAMS.get(2)),
                    Map.of(AUTHPARAMS.get(0), username));

            if (!fetch.isEmpty()) {
                if (fetch.size() > 1) { // Should not go wrong, will direct crash idc
                    generateError("Something went wrong. Better go check your database.");
                    System.exit(1);
                }

                // Type cast for added type checking ((String) Can only convert Strings)
                String storedHash = (String) fetch.getFirst().get(AUTHPARAMS.get(1));
                String storedSalt = (String) fetch.getFirst().get(AUTHPARAMS.get(2));

                // Switch to "main";
                if (Encryption.verifyPassword(password, storedHash, storedSalt)) {
                    // Session Cookies?
                    sceneController.showScene("main");

                } else generateError("Wrong username or password.");
            } else generateError("Invalid username or password.");
        }
    }

    public void redirectToRegister() {
        sceneController.showScene("register");
    }
}
