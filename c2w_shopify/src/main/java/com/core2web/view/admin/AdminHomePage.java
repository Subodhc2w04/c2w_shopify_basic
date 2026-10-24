package com.core2web.view.admin;

import java.util.ArrayList;
import java.util.List;

import com.core2web.controller.OrderController;
import com.core2web.controller.ProductController;
import com.core2web.controller.UserController;
import com.core2web.model.CartItem;
import com.core2web.model.Order;
import com.core2web.model.Product;
import com.core2web.model.User;
import com.core2web.util.Format;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminHomePage {

    ProductController productController = new ProductController();
    UserController userController = new UserController();
    OrderController orderController = new OrderController();

    public Scene getAdminHomeScene(Runnable onLogout) {

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

        Label panelTitle = new Label("Admin Panel");
        panelTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        HBox brandRow = new HBox(12, brand, panelTitle);
        brandRow.setAlignment(Pos.CENTER_LEFT);
        brandRow.setPadding(new Insets(0, 0, 20, 0));

        Button productsButton = new Button("Products");
        Button ordersButton = new Button("Orders");
        Button customersButton = new Button("Customers");
        Button deliveryButton = new Button("Delivery Partners");

        Button[] menuButtons = {productsButton, ordersButton, customersButton, deliveryButton};
        for (int i = 0; i < menuButtons.length; i++) {
            menuButtons[i].setMaxWidth(Double.MAX_VALUE);
            menuButtons[i].setPrefHeight(44);
            menuButtons[i].setStyle(normalMenuStyle);
        }
        productsButton.setStyle(activeMenuStyle);

        Region panelSpacer = new Region();
        VBox.setVgrow(panelSpacer, Priority.ALWAYS);

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
                productsButton,
                ordersButton,
                customersButton,
                deliveryButton,
                panelSpacer,
                logoutButton);
        leftPanel.setPrefWidth(250);
        leftPanel.setPadding(new Insets(28, 18, 28, 18));
        leftPanel.setStyle(
                "-fx-background-color: #0b1433;"
                        + "-fx-border-color: #1c2752;"
                        + "-fx-border-width: 0 1 0 0;");

        // ---------------- Data for each section ----------------
        String[] sectionTitles = {"Products", "Orders", "Customers", "Delivery Partners"};
        String[] sectionSubtitles = {
                "Manage all products available in the store",
                "Track and update customer orders",
                "View registered customers",
                "Manage delivery partners and their areas"
        };
        String[][] tableHeaders = {
                {"ID", "Product Name", "Category", "Price", "Stock"},
                {"Order ID", "Customer", "Product", "Amount", "Status"},
                {"No.", "Name", "Email", "Role"},
                {"No.", "Name", "Email", "Role"}
        };

        // ---------------- Load data from Firestore ----------------
        List<Product> products = productController.getAllProducts();
        List<User> customers = new ArrayList<>();
        List<User> deliveryPartners = new ArrayList<>();
        for (User user : userController.getAllUsers()) {
            if ("Delivery".equalsIgnoreCase(user.getRole())) {
                deliveryPartners.add(user);
            } else if ("User".equalsIgnoreCase(user.getRole())) {
                customers.add(user);
            }
        }

        String[][] productRows = new String[products.size()][];
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            productRows[i] = new String[] {
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    Format.price(product.getPrice()),
                    String.valueOf(product.getStock())
            };
        }

        List<Order> orders = orderController.getAllOrders();
        String[][] orderRows = new String[orders.size()][];
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            StringBuilder itemNames = new StringBuilder();
            for (CartItem item : order.getItems()) {
                if (itemNames.length() > 0) {
                    itemNames.append(", ");
                }
                itemNames.append(item.getName()).append(" x").append(item.getQuantity());
            }
            orderRows[i] = new String[] {
                    order.getId(),
                    order.getUserName(),
                    itemNames.toString(),
                    Format.price(order.getTotalAmount()),
                    order.getStatus()
            };
        }

        String[][][] tableRows = {
                productRows,
                orderRows,
                toUserRows(customers),
                toUserRows(deliveryPartners)
        };

        // Numbers shown in the stat cards at the top
        String[] statNames = {"Total Products", "Total Orders", "Customers", "Delivery Partners"};
        String[] statValues = {
                String.valueOf(products.size()),
                String.valueOf(tableRows[1].length),
                String.valueOf(customers.size()),
                String.valueOf(deliveryPartners.size())
        };

        // ---------------- Build one content page for each section ----------------
        ScrollPane[] sectionPages = new ScrollPane[4];

        // Only the Products page has an add button
        Button addProductButton = new Button("+ Add Product");
        addProductButton.setPrefHeight(44);
        addProductButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0 20;"
                        + "-fx-cursor: hand;");

        for (int s = 0; s < 4; s++) {

            // Header: title and subtitle
            Label title = new Label(sectionTitles[s]);
            title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

            Label subtitle = new Label(sectionSubtitles[s]);
            subtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

            VBox titleBox = new VBox(4, title, subtitle);

            Region headerSpacer = new Region();
            HBox.setHgrow(headerSpacer, Priority.ALWAYS);

            HBox header = new HBox(titleBox, headerSpacer);
            header.setAlignment(Pos.CENTER_LEFT);
            if (s == 0) {
                header.getChildren().add(addProductButton);
            }

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

            // Table
            GridPane table = new GridPane();

            int columnCount = tableHeaders[s].length;
            for (int c = 0; c < columnCount; c++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPercentWidth(100.0 / columnCount);
                table.getColumnConstraints().add(column);
            }

            // Header row
            for (int c = 0; c < columnCount; c++) {
                Label headerCell = new Label(tableHeaders[s][c]);
                headerCell.setMaxWidth(Double.MAX_VALUE);
                headerCell.setStyle(
                        "-fx-background-color: #f4f6fb;"
                                + "-fx-text-fill: #344054;"
                                + "-fx-font-size: 13px;"
                                + "-fx-font-weight: bold;"
                                + "-fx-padding: 14 16;");
                table.add(headerCell, c, 0);
            }

            // Data rows
            for (int r = 0; r < tableRows[s].length; r++) {
                for (int c = 0; c < columnCount; c++) {
                    // Orders: the admin can change the status, which is saved to Firestore
                    if (s == 1 && tableHeaders[s][c].equals("Status")) {
                        String orderId = tableRows[s][r][0];
                        ComboBox<String> statusBox = new ComboBox<>();
                        statusBox.getItems().addAll(OrderController.STATUSES);
                        statusBox.setValue(tableRows[s][r][c]);
                        statusBox.setMaxWidth(Double.MAX_VALUE);
                        statusBox.setOnAction(event -> {
                            if (!orderController.updateStatus(orderId, statusBox.getValue())) {
                                new Alert(Alert.AlertType.ERROR, "Could not update the order status.").showAndWait();
                            }
                        });
                        StackPane statusCell = new StackPane(statusBox);
                        statusCell.setPadding(new Insets(8, 16, 8, 16));
                        statusCell.setStyle("-fx-border-color: #eef0f5; -fx-border-width: 0 0 1 0;");
                        table.add(statusCell, c, r + 1);
                        continue;
                    }

                    Label cell = new Label(tableRows[s][r][c]);
                    cell.setMaxWidth(Double.MAX_VALUE);

                    String textStyle = "-fx-text-fill: #172033; -fx-font-size: 14px;";
                    if (tableHeaders[s][c].equals("Status")) {
                        textStyle = "-fx-text-fill: #4f46e5; -fx-font-size: 14px; -fx-font-weight: bold;";
                    }

                    cell.setStyle(
                            textStyle
                                    + "-fx-padding: 14 16;"
                                    + "-fx-border-color: #eef0f5;"
                                    + "-fx-border-width: 0 0 1 0;");
                    table.add(cell, c, r + 1);
                }
            }

            Label tableTitle = new Label("All " + sectionTitles[s]);
            tableTitle.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

            VBox tableCard = new VBox(16, tableTitle, table);
            tableCard.setPadding(new Insets(24));
            tableCard.setStyle(
                    "-fx-background-color: white;"
                            + "-fx-background-radius: 12;"
                            + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);");

            VBox content = new VBox(28, header, statRow, tableCard);
            content.setPadding(new Insets(32, 40, 32, 40));
            content.setStyle("-fx-background-color: #040b24;");

            ScrollPane contentScroll = new ScrollPane(content);
            contentScroll.setFitToWidth(true);
            contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            contentScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

            sectionPages[s] = contentScroll;
        }

        // ---------------- Page layout ----------------
        BorderPane root = new BorderPane();
        root.setLeft(leftPanel);
        root.setCenter(sectionPages[0]);
        root.setStyle("-fx-background-color: #040b24;");

        // ---------------- Menu button actions ----------------
        productsButton.setOnAction(event -> {
            for (int i = 0; i < menuButtons.length; i++) {
                menuButtons[i].setStyle(normalMenuStyle);
            }
            productsButton.setStyle(activeMenuStyle);
            root.setCenter(sectionPages[0]);
        });

        ordersButton.setOnAction(event -> {
            for (int i = 0; i < menuButtons.length; i++) {
                menuButtons[i].setStyle(normalMenuStyle);
            }
            ordersButton.setStyle(activeMenuStyle);
            root.setCenter(sectionPages[1]);
        });

        customersButton.setOnAction(event -> {
            for (int i = 0; i < menuButtons.length; i++) {
                menuButtons[i].setStyle(normalMenuStyle);
            }
            customersButton.setStyle(activeMenuStyle);
            root.setCenter(sectionPages[2]);
        });

        deliveryButton.setOnAction(event -> {
            for (int i = 0; i < menuButtons.length; i++) {
                menuButtons[i].setStyle(normalMenuStyle);
            }
            deliveryButton.setStyle(activeMenuStyle);
            root.setCenter(sectionPages[3]);
        });

        // "+ Add Product" opens the Add Product form; Back/Cancel returns to the Products page
        addProductButton.setOnAction(event -> {
            // Rebuild the admin page so the new product is loaded from Firestore
            Runnable backToProducts = () -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(getAdminHomeScene(onLogout));
            };
            root.setCenter(new AddProductPage().getAddProductView(backToProducts));
        });

        Scene scene = new Scene(root, 1350, 800);
        return scene;
    }

    private String[][] toUserRows(List<User> users) {
        String[][] rows = new String[users.size()][];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            rows[i] = new String[] {String.valueOf(i + 1), user.getName(), user.getEmail(), user.getRole()};
        }
        return rows;
    }
}
