package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.ControllerRoot;
import com.bubalda.shoppingplatformfx.SQLiteFetch;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class MainPageController extends ControllerRoot {

    public TextField searchBar;

    public void search() {
        // Pulling from database first to array then quick search?
        List<Map<String, Object>> results = SQLiteFetch.getElements("Products", List.of("ProductName"), Map.of("ProductName", "%" + searchBar.getText() + "%"), true);
        for (Map<String, Object> result : results) {
            System.out.println(result.get("ProductName"));
        }
    }

    public void redirectToCart() {
        sceneManager.showScene("cart");
    }

    public void redirectToLogin() {
        sceneManager.showScene("login");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
