package com.example.generalfx.Controllers;

import com.example.generalfx.SceneController;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MainPageController {
    private final SceneController sceneController = SceneController.getInstance();

    public void search(KeyEvent keyEvent) {
        // Pulling from database first to array then quick search?
    }

    public void redirectToCart() {
        sceneController.showScene("cart");
    }

    public void redirectToLogin() {
        sceneController.showScene("login");
    }
}
