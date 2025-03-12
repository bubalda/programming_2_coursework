package com.example.generalfx;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

//import javafx.scene.control.*;
//import javafx.scene.layout.*;
// stage.setMaximized(true);
// stage.setFullScreen(true);
//import java.util.function.Consumer;
//import javax.security.auth.callback.Callback;

import javafx.application.Application;

import java.io.IOException;
import java.util.*;


public class GeneralFX extends Application {
    private void stageSetup(Stage stage) {
        stage.setTitle("Student Info Form");
        stage.setWidth(1380);
        stage.setHeight(780);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // loading progress bar??
        stageSetup(stage);

        Parent parent = FXMLLoader.load(getClass().getResource("MainPage/MainPage.fxml"));
//        Parent parent = FXMLLoader.load(getClass().getResource("LoginPrompt/LoginPrompt.fxml"));
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}