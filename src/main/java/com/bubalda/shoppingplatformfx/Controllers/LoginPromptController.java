package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.*;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.*;

import java.net.URL;
import java.util.*;

public class LoginPromptController extends ControllerRoot {
    private final String DBNAME = "UserAuth";
    private final List<String> AUTHPARAMS = List.of("UserName", "UserPassword", "UserSalt", "UserPermissionLevel");
    public Label registerLink;
    public VBox loginBox;
    public ImageView logo;
    public Parent registerBox;
    public TextField registerUsername;
    public PasswordField registerPassword;
    public PasswordField confirmPassword;
    public ImageView logo2;
    public Button loginBtn;
    public Label registerInfo;

    @FXML
    private TextField loginUsername;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private Label loginInfo;

    @Override // Ensure the TextArea resizes dynamically based on the window width
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        playLogoTransition(); // Play animation on startup
    }

    private void generateError(String errorMessage, Label info) {
        info.setText(errorMessage);
        info.setStyle("-fx-text-fill: red;");
        info.setVisible(true);

        TranslateTransition t = new TranslateTransition(Duration.millis(50), info);
        t.setFromX(-5);
        t.setToX(5);
        t.setCycleCount(6);
        t.setAutoReverse(true);
        t.play();
    }

    @FXML
    private void authenticateUser() {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            generateError("Username and password cannot be empty.", loginInfo);

        } else {
            List<Map<String, Object>> fetch = SQLiteFetch.getElements(DBNAME,
                    List.of(AUTHPARAMS.get(1), AUTHPARAMS.get(2)),
                    Map.of(AUTHPARAMS.get(0), username), false);

            if (!fetch.isEmpty()) {
                if (fetch.size() > 1) { // Should not go wrong, will direct crash idc
                    generateError("Something went wrong. Better go check your database.", loginInfo);
                    System.exit(1);
                }

                // Type cast for added type checking ((String) Can only convert Strings)
                String storedHash = (String) fetch.getFirst().get(AUTHPARAMS.get(1));
                String storedSalt = (String) fetch.getFirst().get(AUTHPARAMS.get(2));

                // Switch to "main";
                if (Encryption.verifyPassword(password, storedHash, storedSalt)) {
                    // Session Cookies?
                    sceneManager.showScene("main");

                } else generateError("Wrong username or password.", loginInfo);
            } else generateError("Invalid username or password.", loginInfo);
        }
    }

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

    public void switchToRegister() {
        switchTo(loginBox, registerBox);
    }

    public void switchToLogin() {
        switchTo(registerBox, loginBox);
    }

    public void registerNewAccount() {
        if (confirmPassword.getText().isEmpty() || registerUsername.getText().isEmpty() || registerPassword.getText().isEmpty()) {
            generateError("All fields should be filled.", registerInfo);
            return;
        }

        if (!SQLiteFetch.getElements(DBNAME,
                List.of(AUTHPARAMS.get(0)), Map.of(AUTHPARAMS.get(0),
                registerUsername.getText()), false).isEmpty()) {
            generateError("Username taken. Please use another one.", registerInfo);
            return;
        }

        if (confirmPassword.getText().equals(registerPassword.getText())) {
            String[] passwd = Encryption.encrypt(confirmPassword.getText());

            SQLiteFetch.addRow(DBNAME, new HashMap<>(Map.of(
                    AUTHPARAMS.get(0), registerUsername.getText(),
                    AUTHPARAMS.get(1), passwd[0],
                    AUTHPARAMS.get(2), passwd[1],
                    AUTHPARAMS.get(3), 0)));

            loginInfo.setText("Successfully registered. You may login now.");
            loginInfo.setStyle("-fx-text-fill: green;");
            loginInfo.setVisible(true);

            switchToLogin();
        } else generateError("Both password fields should match.", registerInfo);

    }
}
