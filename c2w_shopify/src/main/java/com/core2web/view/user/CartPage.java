package com.core2web.view.user;

import java.util.List;

import com.core2web.controller.CartController;
import com.core2web.controller.OrderController;
import com.core2web.model.CartItem;
import com.core2web.model.Order;
import com.core2web.model.User;
import com.core2web.util.Format;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CartPage {

    private static final String LABEL_STYLE = "-fx-text-fill: #344054; -fx-font-size: 13px; -fx-font-weight: bold;";
    private static final String ERROR_STYLE = "-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;";
    private static final String SMALL_BUTTON_STYLE =
            "-fx-background-color: #eef2ff;"
                    + "-fx-background-radius: 6;"
                    + "-fx-border-color: #4f46e5;"
                    + "-fx-border-radius: 6;"
                    + "-fx-text-fill: #4f46e5;"
                    + "-fx-font-size: 14px;"
                    + "-fx-font-weight: bold;"
                    + "-fx-cursor: hand;";

    private final User user;

    CartController cartController = new CartController();
    OrderController orderController = new OrderController();

    // Parts of the page that change when the cart changes
    private final VBox itemsBox = new VBox(14);
    private final Label itemCountLabel = new Label();
    private final Label totalLabel = new Label();
    private final Label messageLabel = new Label();
    private final Button placeOrderButton = new Button("PLACE ORDER");

    public CartPage(User user) {
        this.user = user;
    }

    public Scene getCartView(Runnable onBack) {

        // ---------------- Header ----------------
        Hyperlink backLink = new Hyperlink("< Continue Shopping");
        backLink.setStyle(
                "-fx-text-fill: #818cf8;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;");
        backLink.setOnAction(event -> onBack.run());

        Label title = new Label("My Cart");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Review your items and place the order");
        subtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

        VBox header = new VBox(6, backLink, title, subtitle);

        // ---------------- Cart items card ----------------
        Label itemsTitle = new Label("Items in your cart");
        itemsTitle.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox itemsCard = new VBox(16, itemsTitle, itemsBox);
        itemsCard.setPadding(new Insets(24));
        itemsCard.setStyle(cardStyle());
        HBox.setHgrow(itemsCard, Priority.ALWAYS);

        // ---------------- Order summary card ----------------
        Label summaryTitle = new Label("Order Summary");
        summaryTitle.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

        itemCountLabel.setStyle("-fx-text-fill: #596579; -fx-font-size: 14px;");

        Label deliveryLabel = new Label("Delivery: FREE");
        deliveryLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 14px; -fx-font-weight: bold;");

        totalLabel.setStyle("-fx-text-fill: #172033; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label addressLabel = new Label("Delivery address");
        addressLabel.setStyle(LABEL_STYLE);
        TextArea addressArea = new TextArea();
        addressArea.setPromptText("House no, street, city, pincode");
        addressArea.setPrefRowCount(3);
        addressArea.setWrapText(true);
        addressArea.setStyle(
                "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;");

        Label phoneLabel = new Label("Phone number");
        phoneLabel.setStyle(LABEL_STYLE);
        TextField phoneField = new TextField();
        phoneField.setPromptText("10 digit mobile number");
        phoneField.setPrefHeight(42);
        phoneField.setStyle(
                "-fx-background-color: #ffffff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 0 12;");

        // No payment gateway: every order is Cash on Delivery
        Label paymentLabel = new Label("Payment: Cash on Delivery");
        paymentLabel.setStyle(
                "-fx-background-color: #f4f6fb;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: #344054;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 12;");
        paymentLabel.setMaxWidth(Double.MAX_VALUE);

        messageLabel.setWrapText(true);
        messageLabel.setStyle(ERROR_STYLE);

        placeOrderButton.setMaxWidth(Double.MAX_VALUE);
        placeOrderButton.setPrefHeight(48);
        placeOrderButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");

        placeOrderButton.setOnAction(event -> {
            String[] error = new String[1];
            Order order = orderController.placeOrder(user, addressArea.getText(), phoneField.getText().trim(), error);
            if (order == null) {
                messageLabel.setText(error[0]);
                // Stock may have changed, so show the latest cart
                refresh();
                return;
            }

            // Order saved in Firestore: show the success popup
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText("Order placed successfully!");
            alert.setContentText(
                    "Order ID: " + order.getId()
                            + "\nTotal: " + Format.price(order.getTotalAmount())
                            + "\nPayment: " + order.getPaymentMethod()
                            + "\nDeliver to: " + order.getAddress());
            alert.initOwner(placeOrderButton.getScene().getWindow());
            alert.showAndWait();
        });

        VBox summaryCard = new VBox(
                14,
                summaryTitle,
                itemCountLabel,
                deliveryLabel,
                totalLabel,
                addressLabel,
                addressArea,
                phoneLabel,
                phoneField,
                paymentLabel,
                messageLabel,
                placeOrderButton);
        summaryCard.setPadding(new Insets(24));
        summaryCard.setPrefWidth(360);
        summaryCard.setMinWidth(360);
        summaryCard.setStyle(cardStyle());

        HBox body = new HBox(24, itemsCard, summaryCard);
        body.setAlignment(Pos.TOP_LEFT);

        // ---------------- Page ----------------
        VBox content = new VBox(28, header, body);
        content.setPadding(new Insets(32, 44, 32, 44));
        content.setStyle("-fx-background-color: #040b24;");

        ScrollPane contentScroll = new ScrollPane(content);
        contentScroll.setFitToWidth(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

        refresh();
        Scene scene = new Scene(contentScroll, 1350, 800);
        return scene;
    }

    // Reloads the cart from Firestore and redraws the items and totals
    private void refresh() {
        itemsBox.getChildren().clear();
        List<CartItem> items = cartController.getCartItems(user.getEmail());

        if (items.isEmpty()) {
            Label empty = new Label("Your cart is empty. Add some products to place an order.");
            empty.setStyle("-fx-text-fill: #70798b; -fx-font-size: 14px;");
            itemsBox.getChildren().add(empty);
        }

        int count = 0;
        double total = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
            total += item.subtotal();
            itemsBox.getChildren().add(createItemRow(item));
        }

        itemCountLabel.setText("Items: " + count);
        totalLabel.setText("Total: " + Format.price(total));
        placeOrderButton.setDisable(items.isEmpty());
    }

    private HBox createItemRow(CartItem item) {
        StackPane imageBox = new StackPane();
        imageBox.setPrefSize(80, 80);
        imageBox.setMinSize(80, 80);
        imageBox.setStyle("-fx-background-color: #f4f6fb; -fx-background-radius: 8;");
        if (item.getImageUrl() != null && !item.getImageUrl().isBlank()) {
            ImageView image = new ImageView(new Image(item.getImageUrl(), true));
            image.setFitWidth(80);
            image.setFitHeight(80);
            image.setPreserveRatio(true);
            imageBox.getChildren().add(image);
        }

        Label name = new Label(item.getName());
        name.setStyle("-fx-text-fill: #172033; -fx-font-size: 15px; -fx-font-weight: bold;");
        Label price = new Label(Format.price(item.getPrice()) + " each");
        price.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px;");
        VBox nameBox = new VBox(6, name, price);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Quantity: - qty +
        Button minusButton = new Button("-");
        minusButton.setPrefSize(34, 34);
        minusButton.setStyle(SMALL_BUTTON_STYLE);
        minusButton.setOnAction(event -> changeQuantity(item, item.getQuantity() - 1));

        Label quantity = new Label(String.valueOf(item.getQuantity()));
        quantity.setMinWidth(30);
        quantity.setAlignment(Pos.CENTER);
        quantity.setStyle("-fx-text-fill: #172033; -fx-font-size: 15px; -fx-font-weight: bold;");

        Button plusButton = new Button("+");
        plusButton.setPrefSize(34, 34);
        plusButton.setStyle(SMALL_BUTTON_STYLE);
        plusButton.setOnAction(event -> changeQuantity(item, item.getQuantity() + 1));

        HBox quantityBox = new HBox(6, minusButton, quantity, plusButton);
        quantityBox.setAlignment(Pos.CENTER);

        Label subtotal = new Label(Format.price(item.subtotal()));
        subtotal.setMinWidth(100);
        subtotal.setAlignment(Pos.CENTER_RIGHT);
        subtotal.setStyle("-fx-text-fill: #172033; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button removeButton = new Button("Remove");
        removeButton.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-text-fill: #dc2626;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");
        removeButton.setOnAction(event -> {
            cartController.removeFromCart(user.getEmail(), item.getProductId());
            messageLabel.setText("");
            refresh();
        });

        HBox row = new HBox(16, imageBox, nameBox, spacer, quantityBox, subtotal, removeButton);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(0, 0, 14, 0));
        row.setStyle("-fx-border-color: #eef0f5; -fx-border-width: 0 0 1 0;");
        return row;
    }

    private void changeQuantity(CartItem item, int newQuantity) {
        String error = cartController.changeQuantity(user.getEmail(), item, newQuantity);
        messageLabel.setText(error == null ? "" : error);
        refresh();
    }

    private String cardStyle() {
        return "-fx-background-color: white;"
                + "-fx-background-radius: 12;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);";
    }
}
