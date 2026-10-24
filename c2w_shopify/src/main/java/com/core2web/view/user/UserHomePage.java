package com.core2web.view.user;

import java.util.List;

import com.core2web.view.SplashScreen;
import com.core2web.controller.CartController;
import com.core2web.controller.ProductController;
import com.core2web.model.Product;
import com.core2web.model.User;
import com.core2web.util.Format;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UserHomePage {

    private static final String[] PRODUCT_COLORS = {"#ede9fe", "#fef3c7", "#dbeafe", "#f1f5f9", "#fee2e2", "#fce7f3"};
    private static final String CATEGORY_CARD_STYLE =
            "-fx-background-color: white;"
                    + "-fx-background-radius: 12;"
                    + "-fx-border-radius: 12;"
                    + "-fx-border-width: 3;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);"
                    + "-fx-cursor: hand;";

    private final User user;
    private Scene userHomeScene;

    // Parts of the page that change when a category is selected
    private final HBox productRow = new HBox(20);
    private final HBox categoryRow = new HBox(20);
    private final Label productTitle = new Label();
    private final Label productSubtitle = new Label();
    private Button cartButton;
    private List<Product> allProducts;

    ProductController productController = new ProductController();
    CartController cartController = new CartController();

    public UserHomePage(User user) {
        this.user = user;
    }

    public Scene getUserHomeScene(Runnable onLogout) {

        // ---------------- Top bar ----------------
        Label brand = new Label("C2W");
        brand.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 12;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 12;");

        Label shopName = new Label("C2W Shopify");
        shopName.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region topBarSpacer = new Region();
        HBox.setHgrow(topBarSpacer, Priority.ALWAYS);

        // Logout button on the right side of the top bar, same style as the admin page
        Button logoutButton = new Button("Logout");
        logoutButton.setPrefHeight(40);
        logoutButton.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #4f46e5;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: #818cf8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0 20;"
                        + "-fx-cursor: hand;");
        logoutButton.setOnAction(event -> onLogout.run());

        Label welcomeLabel = new Label("Hi, " + user.getName());
        welcomeLabel.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px; -fx-font-weight: bold;");

        String topButtonStyle =
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0 20;"
                        + "-fx-cursor: hand;";

        Button ordersButton = new Button("My Orders");
        ordersButton.setPrefHeight(40);
        ordersButton.setStyle(topButtonStyle);

        cartButton = new Button("Cart (" + cartController.getCartCount(user.getEmail()) + ")");
        cartButton.setPrefHeight(40);
        cartButton.setStyle(topButtonStyle);

        // Rebuilds the home page so the cart count and stock are fresh
        Runnable backToHome = () -> {
            SplashScreen.primaryStage.setScene(userHomeScene);
            SplashScreen.primaryStage.setTitle("Home Page");
        };

        ordersButton.setOnAction(event -> {
            SplashScreen.primaryStage.setScene(new MyOrdersPage(user).getOrdersView(backToHome));
            SplashScreen.primaryStage.setTitle("My Orders");
        });

        cartButton.setOnAction(event -> {
            SplashScreen.primaryStage.setScene(new CartPage(user).getCartView(backToHome));
            SplashScreen.primaryStage.setTitle("My Cart");

        });

        HBox topBar = new HBox(12, brand, shopName, topBarSpacer, welcomeLabel, ordersButton, cartButton, logoutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // ---------------- Top categories header ----------------
        Label categoryTitle = new Label("TOP CATEGORIES");
        categoryTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label categorySubtitle = new Label("Browse collections crafted for every mood");
        categorySubtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

        VBox categoryTitleBox = new VBox(4, categoryTitle, categorySubtitle);

        Region categorySpacer = new Region();
        HBox.setHgrow(categorySpacer, Priority.ALWAYS);

       

        HBox categoryHeader = new HBox(categoryTitleBox, categorySpacer);
        categoryHeader.setAlignment(Pos.CENTER_LEFT);

        // ---------------- Category cards ----------------
        String[] categoryNames = {"CLOTHING", "ACCESSORIES", "SHOES", "ELECTRONICS", "BOOK"};
        String[] categoryImages = {
                "/images/clothing.jpg",
                "/images/accessories.jpg",
                "/images/shoes.jpg",
                "/images/electronics.jpg",
                "/images/book.jpg"
        };
        String[] categoryColors = {"#fce7f3", "#ede9fe", "#ffe4e6", "#e0e7ff", "#fef3c7"};

        categoryRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < categoryNames.length; i++) {

            StackPane categoryImageBox = new StackPane();
            categoryImageBox.setPrefSize(200, 136);
            categoryImageBox.setMaxSize(200, 136);
            categoryImageBox.setStyle(
                    "-fx-background-color: " + categoryColors[i] + ";"
                            + "-fx-background-radius: 8;");

            // Show the image if it exists in src/main/resources/images, otherwise show the color box
            if (getClass().getResource(categoryImages[i]) != null) {
                ImageView categoryImage = new ImageView(new Image(getClass().getResource(categoryImages[i]).toExternalForm()));
                categoryImage.setFitWidth(200);
                categoryImage.setFitHeight(136);
                categoryImageBox.getChildren().add(categoryImage);
            }

            Label categoryName = new Label(categoryNames[i]);
            categoryName.setStyle("-fx-text-fill: #172033; -fx-font-size: 14px; -fx-font-weight: bold;");

            VBox categoryCard = new VBox(14, categoryImageBox, categoryName);
            categoryCard.setAlignment(Pos.TOP_CENTER);
            categoryCard.setPadding(new Insets(14, 14, 18, 14));
            categoryCard.setPrefWidth(228);
            categoryCard.setStyle(CATEGORY_CARD_STYLE + "-fx-border-color: transparent;");

            // Clicking a category shows only the products of that category
            String category = categoryNames[i];
            categoryCard.setOnMouseClicked(event -> showProducts(category));

            categoryRow.getChildren().add(categoryCard);
        }

        // ---------------- Trending products header ----------------
        productTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        productSubtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

        VBox productTitleBox = new VBox(4, productTitle, productSubtitle);

        Region productSpacer = new Region();
        HBox.setHgrow(productSpacer, Priority.ALWAYS);

        
        HBox productHeader = new HBox(productTitleBox, productSpacer);
        productHeader.setAlignment(Pos.CENTER_LEFT);

        // ---------------- Product cards ----------------
        // Products are loaded from the Firestore "products" collection once, then filtered by category
        allProducts = productController.getAllProducts();
        productRow.setPadding(new Insets(10, 4, 16, 4));
        showProducts(null);

        // Horizontal scroll for products
        ScrollPane productScroll = new ScrollPane(productRow);
        productScroll.setFitToHeight(true);
        productScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        productScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        productScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

        // ---------------- Full page ----------------
        VBox page = new VBox(
                24,
                topBar,
                categoryHeader,
                categoryRow,
                productHeader,
                productScroll);
        page.setPadding(new Insets(30, 44, 30, 44));
        page.setStyle("-fx-background-color: #040b24;");

        // Vertical scroll for the whole page
        ScrollPane pageScroll = new ScrollPane(page);
        pageScroll.setFitToWidth(true);
        pageScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pageScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

        Scene scene = new Scene(pageScroll, 1350, 800);
        userHomeScene = scene;
        return scene;
    }

    // Shows the products of the given category, or all products when category is null
    private void showProducts(String category) {
        productRow.getChildren().clear();

        // Highlight the selected category card
        for (int i = 0; i < categoryRow.getChildren().size(); i++) {
            VBox card = (VBox) categoryRow.getChildren().get(i);
            Label name = (Label) card.getChildren().get(1);
            boolean selected = category != null && name.getText().equals(category);
            card.setStyle(CATEGORY_CARD_STYLE + "-fx-border-color: " + (selected ? "#4f46e5" : "transparent") + ";");
        }

        int count = 0;
        for (Product product : allProducts) {
            // Admin saves categories like "Clothing", cards show "CLOTHING", so ignore case
            if (category == null || category.equalsIgnoreCase(product.getCategory())) {
                productRow.getChildren().add(buildProductCard(product, count));
                count++;
            }
        }

        if (category == null) {
            productTitle.setText("TRENDING PRODUCTS");
            productSubtitle.setText("Bestsellers and highly-rated pieces");
        } else {
            productTitle.setText(category);
            productSubtitle.setText(count + (count == 1 ? " product" : " products") + " in this category");
        }

        if (count == 0) {
            Label noProducts = new Label(category == null
                    ? "No products available yet."
                    : "No products in this category yet.");
            noProducts.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");
            productRow.getChildren().add(noProducts);
        }
    }

    private VBox buildProductCard(Product product, int colorIndex) {
        // Product image area
        StackPane productImageBox = new StackPane();
        productImageBox.setPrefSize(280, 256);
        productImageBox.setMinSize(280, 256);
        productImageBox.setStyle(
                "-fx-background-color: " + PRODUCT_COLORS[colorIndex % PRODUCT_COLORS.length] + ";"
                        + "-fx-background-radius: 12 12 0 0;");

        // Image URL comes from Cloudinary; loaded in the background so the page doesn't freeze
        if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
            ImageView productImage = new ImageView(new Image(product.getImageUrl(), true));
            productImage.setFitWidth(280);
            productImage.setFitHeight(256);
            productImage.setPreserveRatio(true);
            productImageBox.getChildren().add(productImage);
        }
        
        // Product name
        Label productName = new Label(product.getName());
        productName.setStyle("-fx-text-fill: #172033; -fx-font-size: 15px; -fx-font-weight: bold;");

        // Price row: price, old price (cut), discount
        Label price = new Label(Format.price(product.getPrice()));
        price.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox priceRow = new HBox(10, price);
        priceRow.setAlignment(Pos.CENTER_LEFT);

        // Show MRP and discount only when MRP is higher than the selling price
        if (product.getMrp() > product.getPrice()) {
            Text oldPrice = new Text(Format.price(product.getMrp()));
            oldPrice.setStrikethrough(true);
            oldPrice.setStyle("-fx-fill: #70798b; -fx-font-size: 13px;");

            long discount = Math.round((product.getMrp() - product.getPrice()) * 100 / product.getMrp());
            Label offer = new Label("(" + discount + "% OFF)");
            offer.setStyle("-fx-text-fill: #4f46e5; -fx-font-size: 13px; -fx-font-weight: bold;");

            priceRow.getChildren().add(oldPrice);
            priceRow.getChildren().add(offer);
        }

        // Add to cart button
        Button addToCartButton = new Button("ADD TO CART");
        addToCartButton.setMaxWidth(Double.MAX_VALUE);
        addToCartButton.setPrefHeight(40);
        addToCartButton.setStyle(
                "-fx-background-color: #eef2ff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #4f46e5;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: #4f46e5;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");

        if (product.getStock() <= 0) {
            addToCartButton.setText("OUT OF STOCK");
            addToCartButton.setDisable(true);
        }

        // Saves the product in the user's cart in Firestore
        addToCartButton.setOnAction(event -> {
            String error = cartController.addToCart(user.getEmail(), product);
            if (error != null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, error);
                alert.setHeaderText(null);
                alert.initOwner(addToCartButton.getScene().getWindow());
                alert.showAndWait();
                return;
            }
            cartButton.setText("Cart (" + cartController.getCartCount(user.getEmail()) + ")");

            // Show "ADDED" for a moment, then go back to the normal text
            addToCartButton.setText("ADDED ✓");
            PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
            pause.setOnFinished(e -> addToCartButton.setText("ADD TO CART"));
            pause.play();
        });


        VBox productDetails = new VBox(12, productName, priceRow, addToCartButton);
        productDetails.setPadding(new Insets(14, 16, 16, 16));

        VBox productCard = new VBox(productImageBox, productDetails);
        productCard.setPrefWidth(280);
        productCard.setMinWidth(280);
        productCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);");

        return productCard;
    }
}
