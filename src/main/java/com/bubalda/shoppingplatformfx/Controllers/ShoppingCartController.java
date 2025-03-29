package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.ControllerRoot;
import com.bubalda.shoppingplatformfx.SQLiteFetch;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class ShoppingCartController extends ControllerRoot {
    public Label buttonBack;
    public VBox orders;
    public HBox h1;


    public void redirectToMain() {
        sceneManager.showScene("main");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addOrders();
    }

    private void addOrders() {
        List<LinkedHashMap<String, Object>> cart = SQLiteFetch.getElements("Orders",
                Map.of("OrderToUserID", sceneManager.userID), false);

        for (LinkedHashMap<String, Object> order : cart) {
            LinkedHashMap<String, Object> productDetails = SQLiteFetch.getElements("Product",
                    Map.of("ProductID", order.get("OrderProductID")), false).getFirst();

            HBox root = new HBox();
            root.setSpacing(30);

            Image image = new Image("productTemplate.png");
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(60);

            VBox details = new VBox();
            details.setSpacing(10);
            Label productName = new Label(productDetails.get("ProductName").toString());
            Label productPrice = new Label(productDetails.get("ProductPrice").toString());
            Label orderAmount = new Label(order.get("OrderAmount").toString());

            HBox priceQuantity = new HBox(productPrice, orderAmount);
            priceQuantity.setSpacing(20);

            details.getChildren().addAll(productName, priceQuantity);
            root.getChildren().addAll(imageView, details);

            orders.getChildren().add(root);
        }
    }
}
