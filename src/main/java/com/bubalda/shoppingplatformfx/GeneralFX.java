package com.bubalda.shoppingplatformfx;

import com.bubalda.shoppingplatformfx.Controllers.MainPageController;
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
        sceneManager.userID = 1; // TODO: Fast login, delete afterwards
        sceneManager.showScene("main"); // For sceneKeys refer to SceneManager.java
    }

    public static void main(String[] args) {
        launch();
    }
}