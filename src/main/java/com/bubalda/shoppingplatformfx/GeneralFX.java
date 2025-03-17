package com.bubalda.shoppingplatformfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class GeneralFX extends Application {
    public static void stageSetup(Stage stage) {
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

    @Override
    public void start(Stage stage) { // loading progress bar??
        stageSetup(stage);

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setPrimaryStage(stage);
        sceneManager.showScene("login"); // For sceneKeys refer to SceneManager.java
    }

    public static void main(String[] args) {
        launch();
    }
}