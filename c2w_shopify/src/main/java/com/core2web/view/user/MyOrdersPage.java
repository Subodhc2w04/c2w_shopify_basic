package com.core2web.view.user;

import java.util.List;

import com.core2web.controller.OrderController;
import com.core2web.model.CartItem;
import com.core2web.model.Order;
import com.core2web.model.User;
import com.core2web.util.Format;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class MyOrdersPage {

    private final User user;

    OrderController orderController = new OrderController();

    public MyOrdersPage(User user) {
        this.user = user;
    }

    public Scene getOrdersView(Runnable onBack) {

        // ---------------- Header ----------------
        Hyperlink backLink = new Hyperlink("< Continue Shopping");
        backLink.setStyle(
                "-fx-text-fill: #818cf8;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;");
        backLink.setOnAction(event -> onBack.run());

        Label title = new Label("My Orders");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Track all the orders you have placed");
        subtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

        VBox header = new VBox(6, backLink, title, subtitle);

        // ---------------- Order cards (loaded from Firestore) ----------------
        VBox ordersBox = new VBox(20);
        List<Order> orders = orderController.getOrdersByUser(user.getEmail());

        if (orders.isEmpty()) {
            Label empty = new Label("You have not placed any orders yet.");
            empty.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");
            ordersBox.getChildren().add(empty);
        }

        for (Order order : orders) {
            ordersBox.getChildren().add(createOrderCard(order));
        }

        // ---------------- Page ----------------
        VBox content = new VBox(28, header, ordersBox);
        content.setPadding(new Insets(32, 44, 32, 44));
        content.setStyle("-fx-background-color: #040b24;");

        ScrollPane contentScroll = new ScrollPane(content);
        contentScroll.setFitToWidth(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");
        return new Scene(contentScroll,1350, 800);
    }

    private VBox createOrderCard(Order order) {
        Label orderId = new Label("Order #" + order.getId());
        orderId.setStyle("-fx-text-fill: #172033; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label orderDate = new Label("Placed on " + Format.dateTime(order.getOrderTime()));
        orderDate.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px;");

        VBox idBox = new VBox(4, orderId, orderDate);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label status = new Label(order.getStatus());
        status.setStyle(
                "-fx-background-color: " + statusColor(order.getStatus()) + ";"
                        + "-fx-background-radius: 20;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 12px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 6 14;");

        HBox topRow = new HBox(idBox, spacer, status);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // One line per product
        VBox itemsBox = new VBox(8);
        itemsBox.setPadding(new Insets(12, 0, 12, 0));
        itemsBox.setStyle("-fx-border-color: #eef0f5; -fx-border-width: 1 0 1 0;");
        for (CartItem item : order.getItems()) {
            Label itemName = new Label(item.getName() + "  x " + item.getQuantity());
            itemName.setStyle("-fx-text-fill: #344054; -fx-font-size: 14px;");

            Region itemSpacer = new Region();
            HBox.setHgrow(itemSpacer, Priority.ALWAYS);

            Label itemPrice = new Label(Format.price(item.subtotal()));
            itemPrice.setStyle("-fx-text-fill: #172033; -fx-font-size: 14px; -fx-font-weight: bold;");

            itemsBox.getChildren().add(new HBox(itemName, itemSpacer, itemPrice));
        }

        Label address = new Label("Deliver to: " + order.getAddress() + "  |  Phone: " + order.getPhone());
        address.setWrapText(true);
        address.setStyle("-fx-text-fill: #596579; -fx-font-size: 13px;");

        Label payment = new Label("Payment: " + order.getPaymentMethod());
        payment.setStyle("-fx-text-fill: #596579; -fx-font-size: 13px;");

        Region totalSpacer = new Region();
        HBox.setHgrow(totalSpacer, Priority.ALWAYS);

        Label total = new Label("Total: " + Format.price(order.getTotalAmount()));
        total.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox bottomRow = new HBox(payment, totalSpacer, total);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, topRow, itemsBox, address, bottomRow);
        card.setPadding(new Insets(22));
        card.setMaxWidth(900);
        card.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);");
        return card;
    }

    private String statusColor(String status) {
        if ("Delivered".equals(status)) {
            return "#16a34a";
        } else if ("Shipped".equals(status)) {
            return "#2563eb";
        } else if ("Cancelled".equals(status)) {
            return "#dc2626";
        }
        return "#f59e0b";
    }
}
