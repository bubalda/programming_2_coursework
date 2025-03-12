package com.example.generalfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class LoginController extends Control {
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

        } else if ("admin".equals(username) && "password".equals(password)) { // Swap to main scene
            // Session Cookies?
            this.sceneController.switchScene("main");

        } else { // Can't find from sqlite
            generateError("Invalid username or password");
        }
    }

}