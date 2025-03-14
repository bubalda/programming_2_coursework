package com.example.generalfx;
//https://github.com/FTMahringer/SceneManager/blob/master/SceneManager.java
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;
import java.util.*;

/** A singleton class which manages scene transitions, derived from GitHub
 * For session cookies mainly?????
 * But harder tho*/
public class SceneController {
    public static void stageSetup() { // Will be called internally, no hassle
        // Preserve saved size state
        boolean wasMaximized = stage.isMaximized();
        stage.setMaximized(wasMaximized);

        stage.setTitle("Shopping Platform");
        stage.setMinWidth(1380);
        stage.setMinHeight(780);
        stage.centerOnScreen();

//        stage.setMaximized(true);
//        stage.setFullScreen(true);
//        stage.setFullScreenExitHint("");
    }

    private static final Map<String, String> SCENES = new HashMap<>(Map.of(
            "login", "LoginPrompt.fxml",
            "register", "RegisterPrompt.fxml",

            "main", "MainPage.fxml",
            "profile", "AccountProfile.fxml",
            "search", "SearchResults.fxml",

            "cart", "ShoppingCart.fxml",
            "checkout", "CheckOut.fxml",

            "admin", "AdminPage.fxml" // + other special elements from other pages that only admin sees
    ));

    // Directly init when start (for thread safety in case)
    private static final SceneController instance = new SceneController();
    private static final Stage stage = new Stage();
    private SceneController () {}; // Constructor override

    public static SceneController getInstance () {
        return instance;
    }

    public void showScene (String sceneKey) {
        String filePath = SCENES.get(sceneKey);
        if (filePath != null) {
            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + "/" + filePath;

        } else {
            System.out.println("No scene found for key: " + sceneKey);
            System.exit(1); // Relatively small project, thus crash
        }
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filePath));

        Parent root = null;
        try {
            root = fxmlLoader.load();
        }
        catch (IOException e) {
            System.out.println("Error loading scene: " + sceneKey);
            System.err.println(e.getMessage());
            System.exit(1); // Relatively small project, thus crash
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stageSetup(); // Order is important
        stage.show();
    }
}
