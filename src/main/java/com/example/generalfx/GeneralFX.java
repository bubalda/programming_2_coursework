package com.example.generalfx;

import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;
import java.util.*;


public class GeneralFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // loading progress bar??

        SceneController sceneController = SceneController.getInstance();
        sceneController.showScene("main"); // For sceneKey refer to SceneController.java
    }

    public static void main(String[] args) {
        launch();
    }
}