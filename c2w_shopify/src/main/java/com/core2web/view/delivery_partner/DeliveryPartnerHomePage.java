package com.core2web.view.delivery_partner;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DeliveryPartnerHomePage {

    private final User user;

    OrderController orderController = new OrderController();

    public DeliveryPartnerHomePage(User user) {
        this.user = user;
    }

    public Scene getDeliveryPartnerHomeScene(Runnable onLogout) {
        return getDeliveryPartnerHomeScene(onLogout, 0);
    }

    // selectedSection: 0 = Orders, 1 = Current Orders, 2 = Order History
    private Scene getDeliveryPartnerHomeScene(Runnable onLogout, int selectedSection) {

        // Styles for the left panel menu buttons
        String activeMenuStyle =
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-alignment: center-left;"
                        + "-fx-padding: 0 16;"
                        + "-fx-cursor: hand;";

        String normalMenuStyle =
                "-fx-background-color: transparent;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: #a5adbf;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-alignment: center-left;"
                        + "-fx-padding: 0 16;"
                        + "-fx-cursor: hand;";

        // ---------------- Left panel ----------------
        Label brand = new Label("C2W");
        brand.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 12;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 10 12;");

        Label panelTitle = new Label("Delivery Panel");
        panelTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox brandRow = new HBox(12, brand, panelTitle);
        brandRow.setAlignment(Pos.CENTER_LEFT);
        brandRow.setPadding(new Insets(0, 0, 20, 0));

        Button ordersButton = new Button("Orders");
        Button currentOrdersButton = new Button("Current Orders");
        Button orderHistoryButton = new Button("Order History");

        Button[] menuButtons = {ordersButton, currentOrdersButton, orderHistoryButton};
        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i].setMaxWidth(Double.MAX_VALUE);
            menuButtons[i].setPrefHeight(44);
            menuButtons[i].setStyle(normalMenuStyle);
        }
        menuButtons[selectedSection].setStyle(activeMenuStyle);

        Region panelSpacer = new Region();
        VBox.setVgrow(panelSpacer, Priority.ALWAYS);

        Label partnerName = new Label(user.getName());
        partnerName.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label partnerEmail = new Label(user.getEmail());
        partnerEmail.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 12px;");

        VBox partnerBox = new VBox(2, partnerName, partnerEmail);
        partnerBox.setPadding(new Insets(0, 0, 8, 4));

        Button logoutButton = new Button("Logout");
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setPrefHeight(44);
        logoutButton.setStyle(
                "-fx-background-color: transparent;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #4f46e5;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: #818cf8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");
        logoutButton.setOnAction(event -> onLogout.run());

        VBox leftPanel = new VBox(
                8,
                brandRow,
                ordersButton,
                currentOrdersButton,
                orderHistoryButton,
                panelSpacer,
                partnerBox,
                logoutButton);
        leftPanel.setPrefWidth(250);
        leftPanel.setPadding(new Insets(28, 18, 28, 18));
        leftPanel.setStyle(
                "-fx-background-color: #0b1433;"
                        + "-fx-border-color: #1c2752;"
                        + "-fx-border-width: 0 1 0 0;");

        // ---------------- Load orders from Firestore ----------------
        // Orders: new orders waiting to be picked up
        // Current Orders: orders out for delivery
        // Order History: orders already delivered
        List<Order> pendingOrders = new ArrayList<>();
        List<Order> shippedOrders = new ArrayList<>();
        List<Order> deliveredOrders = new ArrayList<>();
        for (Order order : orderController.getAllOrders()) {
            if ("Pending".equals(order.getStatus())) {
                pendingOrders.add(order);
            } else if ("Shipped".equals(order.getStatus())) {
                shippedOrders.add(order);
            } else if ("Delivered".equals(order.getStatus())) {
                deliveredOrders.add(order);
            }
        }

        BorderPane root = new BorderPane();

        // Reloads this page so the latest orders are shown, keeping the given section open
        Runnable[] reload = new Runnable[3];
        for (int s = 0; s < 3; s++) {
            int section = s;
            reload[s] = () -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(getDeliveryPartnerHomeScene(onLogout, section));
            };
        }

        // ---------------- Build one content page for each section ----------------
        String[] sectionTitles = {"Orders", "Current Orders", "Order History"};
        String[] sectionSubtitles = {
                "New orders waiting to be picked up",
                "Orders you are delivering right now",
                "Orders that have been delivered"
        };
        String[] emptyMessages = {
                "No new orders right now.",
                "You have no orders out for delivery.",
                "No delivered orders yet."
        };
        List<List<Order>> sectionOrders = List.of(pendingOrders, shippedOrders, deliveredOrders);

        // Numbers shown in the stat cards at the top
        String[] statNames = {"New Orders", "Out for Delivery", "Delivered"};
        String[] statValues = {
                String.valueOf(pendingOrders.size()),
                String.valueOf(shippedOrders.size()),
                String.valueOf(deliveredOrders.size())
        };

        ScrollPane[] sectionPages = new ScrollPane[3];

        for (int s = 0; s < 3; s++) {

            // Header: title and subtitle
            Label title = new Label(sectionTitles[s]);
            title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

            Label subtitle = new Label(sectionSubtitles[s]);
            subtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

            VBox header = new VBox(4, title, subtitle);

            // Stat cards
            HBox statRow = new HBox(20);
            for (int i = 0; i < statNames.length; i++) {
                Label statName = new Label(statNames[i]);
                statName.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px; -fx-font-weight: bold;");

                Label statValue = new Label(statValues[i]);
                statValue.setStyle("-fx-text-fill: #172033; -fx-font-size: 28px; -fx-font-weight: bold;");

                VBox statCard = new VBox(6, statName, statValue);
                statCard.setPadding(new Insets(20));
                statCard.setMaxWidth(Double.MAX_VALUE);
                statCard.setStyle(
                        "-fx-background-color: white;"
                                + "-fx-background-radius: 12;"
                                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);");
                HBox.setHgrow(statCard, Priority.ALWAYS);

                statRow.getChildren().add(statCard);
            }

            // Order cards
            VBox ordersBox = new VBox(20);
            if (sectionOrders.get(s).isEmpty()) {
                Label empty = new Label(emptyMessages[s]);
                empty.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");
                ordersBox.getChildren().add(empty);
            }
            for (Order order : sectionOrders.get(s)) {
                if (s == 0) {
                    // Accepting an order moves it to Current Orders
                    ordersBox.getChildren().add(createOrderCard(order, "Accept Order", "Shipped", reload[1]));
                } else if (s == 1) {
                    // Delivered orders move to Order History
                    ordersBox.getChildren().add(createOrderCard(order, "Mark as Delivered", "Delivered", reload[2]));
                } else {
                    // History cards are read only, so no action button
                    ordersBox.getChildren().add(createOrderCard(order, null, null, null));
                }
            }

            VBox content = new VBox(28, header, statRow, ordersBox);
            content.setPadding(new Insets(32, 40, 32, 40));
            content.setStyle("-fx-background-color: #040b24;");

            ScrollPane contentScroll = new ScrollPane(content);
            contentScroll.setFitToWidth(true);
            contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

            sectionPages[s] = contentScroll;
        }

        // ---------------- Page layout ----------------
        root.setLeft(leftPanel);
        root.setCenter(sectionPages[selectedSection]);
        root.setStyle("-fx-background-color: #040b24;");

        // ---------------- Menu button actions ----------------
        for (int s = 0; s < menuButtons.length; s++) {
            int section = s;
            menuButtons[s].setOnAction(event -> {
                for (int i = 0; i < menuButtons.length; i++) {
                    menuButtons[i].setStyle(normalMenuStyle);
                }
                menuButtons[section].setStyle(activeMenuStyle);
                root.setCenter(sectionPages[section]);
            });
        }

        Scene scene = new Scene(root, 1350, 800);
        return scene;
    }

    // One card per order with a button that moves the order to the next status
    // Pass actionText as null for a read only card (Order History)
    private VBox createOrderCard(Order order, String actionText, String nextStatus, Runnable afterUpdate) {
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
            itemsBox.getChildren().add(itemName);
        }

        Label customer = new Label("Customer: " + order.getUserName() + "  |  Phone: " + order.getPhone());
        customer.setStyle("-fx-text-fill: #596579; -fx-font-size: 13px;");

        Label address = new Label("Deliver to: " + order.getAddress());
        address.setWrapText(true);
        address.setStyle("-fx-text-fill: #596579; -fx-font-size: 13px;");

        Label amount = new Label((actionText == null ? "Collected: " : "Collect: ") + Format.price(order.getTotalAmount()) + " (" + order.getPaymentMethod() + ")");
        amount.setStyle("-fx-text-fill: #172033; -fx-font-size: 16px; -fx-font-weight: bold;");

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Button actionButton = new Button(actionText);
        actionButton.setPrefHeight(40);
        actionButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0 20;"
                        + "-fx-cursor: hand;");
        actionButton.setOnAction(event -> {
            if (orderController.updateStatus(order.getId(), nextStatus)) {
                afterUpdate.run();
            } else {
                new Alert(Alert.AlertType.ERROR, "Could not update the order status.").showAndWait();
            }
        });

        HBox bottomRow = new HBox(amount, bottomSpacer);
        if (actionText != null) {
            bottomRow.getChildren().add(actionButton);
        }
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(12, topRow, itemsBox, customer, address, bottomRow);
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
