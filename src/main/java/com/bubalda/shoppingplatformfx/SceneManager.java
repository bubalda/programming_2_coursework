package com.bubalda.shoppingplatformfx;
//https://github.com/FTMahringer/SceneManager/blob/master/SceneManager.java

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** A singleton class which manages scene transitions, derived from GitHub*/
public class SceneManager {
    private SceneManager() {}; // Constructor override
    private static SceneManager instance = null; // Lazy initialization (singleton)
    private Stage primaryStage;
    public Scene currentScene;

    public int userID;
    public int productID;

//    public Stack<Scene> sceneStack = new Stack<>();

    public static final Map<String, String> SCENES = new HashMap<>(Map.of(
            "login", "LoginPrompt.fxml",

            "main", "MainPage.fxml",
            "profile", "AccountProfile.fxml",
            "search", "SearchResults.fxml",
            "product", "ProductDetails.fxml",
//            "redeem", "RedeemPage.fxml",

            "cart", "ShoppingCart.fxml",
            "checkout", "CheckOut.fxml", // clear cache

            "admin", "AdminPage.fxml" // + other special elements from other pages that only admin sees (enter from main page)
    ));

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public static String getPath(String sceneKey) {
        String filePath = SCENES.get(sceneKey);
        if (filePath != null) {
            filePath = filePath.substring(0, filePath.lastIndexOf('.')) + "/" + filePath;

        } else {
            System.out.println("No scene found for key: " + sceneKey);
            System.exit(1); // Redirect to 404 website?
        }
        return filePath;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getStage() {
        return this.primaryStage;
    }
    public Scene getScene() {
        return this.currentScene;
    }

    public void showScene(String sceneKey) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getPath(sceneKey)));

        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
            this.currentScene = scene;
        }
        catch (IOException e) {
            System.out.println("Error loading scene: " + sceneKey);
            System.exit(1); // Redirect to 404?
        }
    }
}
