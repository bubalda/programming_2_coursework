/*
Naming conventions
1. camelCase
2. Long names for important elements, short names / aliases for "temporary" elements
3. CAPS FOR CONSTANTS (FINAL)
4. Capitalized styles are from root.css, other than that check local stylesheets
5. 3 types of button moving (redirect [To next scene], switch [Switching context], navigate [Same page])
*/

package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.ControllerRoot;
import com.bubalda.shoppingplatformfx.SQLiteFetch;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainPageController extends ControllerRoot {
    public ToolBar header;
    public Menu accountMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // banner setup
        banner.setImage(bannerImages[0]);
        bannerArrows.setSpacing(banner.getLayoutBounds().getWidth() - 100);
        bannerSlideShow();

        // product setup
        addProductDisplay();

        // display account login as
        accountMenu.setText(
                SQLiteFetch.getElements("User", List.of("UserName"), Map.of("UserID", sceneManager.userID), false)
                        .getFirst().get("UserName").toString());

        // header width
        header.widthProperty().addListener((_, _, width) -> {
            int numElements = header.getItems().size();
            if (numElements > 1) {
                double spacing = width.doubleValue() / (numElements * 2);
                header.setStyle("-fx-spacing: " + spacing);
            }
        });

        searchBar.widthProperty().addListener((_, _, width) -> {
            searchSuggestions.setStyle(searchSuggestions.getStyle() + "-fx-pref-width: " + width);
        });
    }

    private int bannerIndex = 0;
    static Timeline timeline;
    static Image[] bannerImages = new Image[]{
            new Image(ControllerRoot.getResourcePath("_images/bannerCyberSale.png")),
            new Image(ControllerRoot.getResourcePath("_images/bannerHariRayaOffer.png")),
            new Image(ControllerRoot.getResourcePath("_images/bannerSchool.png")),
    };
    public ImageView banner;
    public HBox bannerArrows;

    // Products
    public ScrollPane mainScrollPane;
    public Label forYouLabel;
    public Label promotionLabel;
    public Label allProductLabel;
    private static final int MAX_PRODUCT_IN_COLUMN = 6;
    public GridPane allProductGridPane;

    public void nextBanner() {
        bannerIndex = (bannerIndex + 1) % bannerImages.length;
        applyFadeTransition(bannerImages[bannerIndex]);
    }

    public void prevBanner() {
        bannerIndex = (bannerIndex - 1 + bannerImages.length) % bannerImages.length;
        applyFadeTransition(bannerImages[bannerIndex]);
    }

    // Interactive Methods
    public void bannerSlideShow() {
        /* Main (center) */
        // Banner
        int BANNER_SHOWN_TIME = 5;
        timeline = new Timeline(new KeyFrame(Duration.seconds(BANNER_SHOWN_TIME), _ -> nextBanner()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void applyFadeTransition(Image newImage) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), banner);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(_ -> {
            banner.setImage(newImage);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), banner);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
        resetTimer();
    }

    private static void resetTimer() {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
            timeline.playFromStart();
        }
    }

    public void addProductDisplay() {
        List<LinkedHashMap<String, Object>> allProducts = SQLiteFetch.getElements("Product");
        int row = 0, col = 0, boxSize = 150, translate = 20;

        for (LinkedHashMap<String, Object> product : allProducts) {
            Image productImage = new Image(ControllerRoot.getResourcePath("_images/productTemplate.png"));
            ImageView productImageView = new ImageView(productImage);
            productImageView.setFitWidth(boxSize);
            productImageView.setPreserveRatio(true);

            // Adjusting text wrap and formatting
            Text productNameText = new Text(product.get("ProductName").toString());
            productNameText.setWrappingWidth(boxSize); // Define max width
            TextFlow productName = new TextFlow(productNameText);
            productName.getStyleClass().add("productDetails");
            Label productPrice = new Label("$" + product.get("ProductPrice").toString());

            AtomicBoolean hasAdjusted = new AtomicBoolean(false);
            productNameText.boundsInLocalProperty().addListener((_, _, bounds) -> {
                double textWidth = bounds.getWidth();
                boolean isWrapped = textWidth > boxSize;

                if (isWrapped) {
                    productName.setTranslateY(-translate);
                } else if (!hasAdjusted.get()) {
                    productPrice.setTranslateY(translate);
                }
                hasAdjusted.set(true);
            });

            // rating at row 2

            VBox productBox = new VBox(productImageView, productName, productPrice);
            productBox.setPadding(new Insets(0, 0, 10, 0));
            productBox.setMaxWidth(boxSize); // Same as img
            productBox.setAlignment(Pos.BASELINE_LEFT);

            productBox.getStyleClass().addAll("productBox", "Clickable");
            productBox.setOnMouseClicked(_ -> redirectToProduct((int) product.get("ProductID")));
            allProductGridPane.add(productBox, col, row);

            col++;
            if (col >= MAX_PRODUCT_IN_COLUMN) {
                col = 0;
                row++;
            }
        }
    }

    // Helper methods
    private void redirectToProduct(int productID) {
        sceneManager.productID = productID;
        sceneManager.showScene("product");
        LinkedHashMap<String, Object> targetProduct = SQLiteFetch.getElements("Product", new HashMap<>(Map.of("ProductID", sceneManager.productID)), false).getFirst();
    }

    /* Header (top) */
    private static final Map<String, BootstrapIcons> ICON_REDIRECTS = Map.of(
            "redeem", BootstrapIcons.CASH_STACK,
            "wallet", BootstrapIcons.WALLET2,
            "cart", BootstrapIcons.CART2
    );

    public ImageView logo;
    public ContextMenu searchSuggestions = new ContextMenu();
    public TextField searchBar;

    public void scrollToTop() {
        RotateTransition rt = new RotateTransition(Duration.millis(250), logo);
        rt.setByAngle(360);
        rt.setAutoReverse(true);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.millis(100),
                        new KeyValue(mainScrollPane.vvalueProperty(), 0, Interpolator.EASE_BOTH)
                )
        );
        tl.play();
        rt.play();
    }

    public void navToRecommendations() {
        scrollToMainHeaders(forYouLabel);
    }

    public void navToDeals() {
        scrollToMainHeaders(promotionLabel);
    }

    public void navToProducts() {
        scrollToMainHeaders(allProductLabel);
    }

    public void searchCompletion() {
        // Normal search bar dynamic, search content (array?)
        // Check and add search to table
        // Remember to limit text input
        searchSuggestions.getItems().clear();
        String searchText = searchBar.getText();
        if (searchText.isEmpty()) {
            searchSuggestions.hide();
            return;
        }

        String sql = "SELECT ProductID, ProductName FROM PRODUCT WHERE ProductName LIKE '%" + searchText + "%' LIMIT 5;";
//                + "%' OR " + "ProductDescription LIKE '%" + searchText + "%'";
        List<LinkedHashMap<String, Object>> results =
                SQLiteFetch.arbitraryGet(sql);

        for (LinkedHashMap<String, Object> result : results) {
            MenuItem item = new MenuItem((String) result.get("ProductName"));
            item.setOnAction(_ -> {
                searchBar.setText(item.getText());
                redirectToProduct((int) result.get("ProductID")); // Similar products????
            });
            searchSuggestions.getItems().add(item);
        }

        if (!searchSuggestions.getItems().isEmpty()) {
            searchSuggestions.show(searchBar, Side.BOTTOM, 0, 0);
        } else {
            searchSuggestions.hide();
        }
    }

    public void redirectToCart() {
        redirectButtons("cart");
    }

    public void redirectToAccount() {
        redirectButtons("account");
    }

    public void redirectToLogin() {
        redirectButtons("login");
    }

    // Helper methods
    private void scrollToMainHeaders(Label label) {
        if (label != null) {
            Timeline scrollTimeline = new Timeline(
                    new KeyFrame(Duration.millis(300),
                            new KeyValue(mainScrollPane.vvalueProperty(), label.getLayoutY() / mainScrollPane.getHeight(), Interpolator.EASE_BOTH)
                    )
            );
            scrollTimeline.play();
        }
    }

    private void redirectButtons(String key) {
        sceneManager.showScene(key);
    }
}