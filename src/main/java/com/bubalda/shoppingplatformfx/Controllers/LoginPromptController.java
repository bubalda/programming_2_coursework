package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.*;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.*;

import java.net.URL;
import java.util.*;

public class LoginPromptController extends ControllerRoot {
    public ImageView logo;
    public VBox loginBox;
    public Button loginBtn;
    public TextField loginUsername;
    public PasswordField loginPassword;
    public Label loginInfo;

    public Label registerLink;
    public Parent registerBox;
    public TextField registerUsername;
    public PasswordField registerPassword;
    public PasswordField registerConfirmPassword;
    public Label registerInfo;
    public ImageView logo2;
    
    public HBox forgotBox;
    public ImageView logo3;
    public TextField forgotUsername;
    public PasswordField forgotPassword;
    public PasswordField forgotConfirmPassword;
    public Label forgotInfo;

    // tools and init
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sceneManager.userID = -1;
        sceneManager.productID = -1;
    }

    private void generateError(String errorMessage, Label info) {
        info.setText(errorMessage); // display error message in passed in label
        info.setStyle("-fx-text-fill: red;");
        info.setVisible(true);

        TranslateTransition t = new TranslateTransition(Duration.millis(50), info); // with some animations
        t.setFromX(-5);
        t.setToX(5);
        t.setCycleCount(6);
        t.setAutoReverse(true);
        t.play();
    }

    private boolean somethingMissing(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().isEmpty()) return true;
        }
        return false;
    }

    // transitions
    public void switchTo(Parent from, Parent to) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), from);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), to);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        fadeOut.play();
        fadeOut.setOnFinished(_ -> {
            from.setVisible(false);
            to.setVisible(true);
            fadeIn.play();
        });
    }

    // On click methods
    public void switchToForgot() {
        switchTo(loginBox, forgotBox);
        loginUsername.setText("");
        loginPassword.setText("");
        loginInfo.setText("");
    }

    public void switchToRegister() {
        switchTo(loginBox, registerBox);
        loginUsername.setText("");
        loginPassword.setText("");
        loginInfo.setText("");
    }

    public void switchToLoginFromForgot() {
        switchTo(forgotBox, loginBox);
        forgotUsername.setText("");
        forgotPassword.setText("");
        forgotConfirmPassword.setText("");
        forgotInfo.setText("");
    }

    public void switchToLoginFromRegister() {
        switchTo(registerBox, loginBox);
        registerUsername.setText("");
        registerPassword.setText("");
        registerConfirmPassword.setText("");
        registerInfo.setText("");
    }
    
    // for login
    @FXML
    private void authenticateUser() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            generateError("Username and password cannot be empty.", loginInfo);

        } else {
            List<LinkedHashMap<String, Object>> fetch = SQLiteFetch.getElements("User",
                    List.of("UserID", "UserName", "UserPassword", "UserSalt"),
                    Map.of("UserName", username), false);

            if (!fetch.isEmpty()) {
                if (fetch.size() > 1) {
                    generateError("Something went wrong. Better go check your database.", loginInfo);
                    return;
                }

                // Type cast for added type checking ((String) Can only convert Strings)
                String storedHash = (String) fetch.getFirst().get("UserPassword");
                String storedSalt = (String) fetch.getFirst().get("UserSalt");

                // Switch to "main";
                if (Encryption.verifyPassword(password, storedHash, storedSalt)) {
                    // Session Cookies?
                    loginInfo.setText("Loading...");
                    loginInfo.setStyle("-fx-text-fill: green;");
                    loginInfo.setVisible(true);

                    sceneManager.userID = (int) fetch.getFirst().get("UserID");
                    sceneManager.showScene("main");

                } else generateError("Wrong username or password.", loginInfo);
            } else generateError("Invalid username or password.", loginInfo);
        }
    }

    public void registerNewAccount() {
        if (somethingMissing(registerUsername, registerPassword, registerConfirmPassword)) {
            generateError("All fields should be filled.", registerInfo);
            return;
        }

        if (!SQLiteFetch.getElements("User", List.of("UserName"), Map.of("UserName", registerUsername.getText()), false).isEmpty()) {
            generateError("Username taken. Please use another one.", registerInfo);
            return;
        }

        if (registerConfirmPassword.getText().equals(registerPassword.getText())) {
            String[] passwd = Encryption.encrypt(registerConfirmPassword.getText());

            if (!SQLiteFetch.addRow("User", new HashMap<>(Map.of(
                    "UserName", registerUsername.getText(),
                    "UserPassword", passwd[0],
                    "UserSalt", passwd[1])))) {
                generateError("Abnormalities occurred in updating account details. Please inform the administrator about this.", registerInfo);
                return;
            }

            // check sqlite output
            if (SQLiteFetch.getElements("User", new HashMap<>(Map.of("UserName", registerUsername.getText())), false).isEmpty()) {
                generateError("Did not find data inserted in database. Please contact an administrator to fix it.", registerInfo);
                return;
            };

            loginInfo.setText("Successfully registered. You may login now.");
            loginInfo.setStyle("-fx-text-fill: green;");
            loginInfo.setVisible(true);

            switchToLoginFromRegister();
        } else generateError("Both password fields should match.", registerInfo);
    }

    public void updateForgottenAccount() {
        if (somethingMissing(forgotUsername, forgotPassword, forgotConfirmPassword)) {
            generateError("All fields should be filled.", forgotInfo);
            return;
        }

        if (SQLiteFetch.getElements("User", List.of("UserID"), Map.of("UserName", forgotUsername.getText()), false).isEmpty()) {
            generateError("There is no user named " + forgotUsername.getText() + ".", forgotInfo);
            return;
        }

        if (forgotConfirmPassword.getText().equals(forgotPassword.getText())) {
            String[] passwd = Encryption.encrypt(forgotConfirmPassword.getText());

            if (!SQLiteFetch.updateRow("User",
                    new HashMap<>(Map.of("UserPassword", passwd[0], "UserSalt", passwd[1])),
                    new HashMap<> (Map.of("UserName", forgotUsername.getText())),
                    false)) {
                generateError("Abnormalities occurred in updating account details. Please inform the administrator about this.", forgotInfo);
                return;
            }

            // check sqlite output
            if (SQLiteFetch.getElements("User", new HashMap<>(Map.of("UserPassword", passwd[0])), false).isEmpty()) {
                generateError("Abnormalities occurred in checking update of account details. Please inform the administrator about this.", forgotInfo);
                return;
            };

            loginInfo.setText("Successfully updated password. You may login now.");
            loginInfo.setStyle("-fx-text-fill: green;");
            loginInfo.setVisible(true);
            switchToLoginFromForgot();

        } else generateError("Both password fields should match.", forgotInfo);
    }
}

