package com.example.generalfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class SceneController {
    private static final Map<String, String> SCENES = new HashMap<>(Map.of(
            "login", "LoginPrompt.fxml",
            "main", "MainPage.fxml",
            "register", "RegisterPrompt.fxml"
    ));

    private final Stage stage;
    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public <T> T switchScene(String sceneName) {
        String useScene = SCENES.get(sceneName);
        if (useScene == null) return null;

        useScene = useScene.substring(0, useScene.indexOf('.')) + '/' + useScene; // New path
        FXMLLoader loader = new FXMLLoader(getClass().getResource(useScene));

        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            return loader.getController();
        }
        catch (IOException e) {
            System.err.println("Failed to load scene: " + sceneName); // Check once for final load
            return null;
        }
    }
}