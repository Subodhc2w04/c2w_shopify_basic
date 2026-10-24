package com.core2web.view.admin;

import java.io.File;

import com.core2web.controller.ProductController;
import com.core2web.model.Product;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.stage.FileChooser;

public class AddProductPage {

    ProductController productController = new ProductController();

    public ScrollPane getAddProductView(Runnable onBack) {

        String labelStyle = "-fx-text-fill: #344054; -fx-font-size: 13px; -fx-font-weight: bold;";

        String fieldStyle =
                "-fx-background-color: #ffffff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 0 12;";

        // ---------------- Header ----------------
        Hyperlink backLink = new Hyperlink("< Back to Products");
        backLink.setStyle(
                "-fx-text-fill: #818cf8;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;");
        backLink.setOnAction(event -> onBack.run());

        Label title = new Label("Add Product");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Fill in the details to add a new product to the store");
        subtitle.setStyle("-fx-text-fill: #a5adbf; -fx-font-size: 14px;");

        VBox header = new VBox(6, backLink, title, subtitle);

        // ---------------- Product name ----------------
        Label nameLabel = new Label("Product name");
        nameLabel.setStyle(labelStyle);
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Plain Cotton Shirt");
        nameField.setPrefHeight(46);
        nameField.setStyle(fieldStyle);
        VBox nameBox = new VBox(8, nameLabel, nameField);

        // ---------------- Category and stock ----------------
        Label categoryLabel = new Label("Category");
        categoryLabel.setStyle(labelStyle);
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Clothing", "Accessories", "Shoes", "Electronics", "Book");
        categoryBox.setPromptText("Select category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        categoryBox.setPrefHeight(46);
        categoryBox.setStyle(fieldStyle);
        VBox categoryFieldBox = new VBox(8, categoryLabel, categoryBox);
        HBox.setHgrow(categoryFieldBox, Priority.ALWAYS);

        Label stockLabel = new Label("Stock quantity");
        stockLabel.setStyle(labelStyle);
        TextField stockField = new TextField();
        stockField.setPromptText("e.g. 25");
        stockField.setPrefHeight(46);
        stockField.setStyle(fieldStyle);
        VBox stockBox = new VBox(8, stockLabel, stockField);
        HBox.setHgrow(stockBox, Priority.ALWAYS);

        HBox categoryRow = new HBox(20, categoryFieldBox, stockBox);

        // ---------------- Price and original price ----------------
        Label priceLabel = new Label("Selling price (₹)");
        priceLabel.setStyle(labelStyle);
        TextField priceField = new TextField();
        priceField.setPromptText("e.g. 700");
        priceField.setPrefHeight(46);
        priceField.setStyle(fieldStyle);
        VBox priceBox = new VBox(8, priceLabel, priceField);
        HBox.setHgrow(priceBox, Priority.ALWAYS);

        Label mrpLabel = new Label("Original price / MRP (₹)");
        mrpLabel.setStyle(labelStyle);
        TextField mrpField = new TextField();
        mrpField.setPromptText("e.g. 799 (optional)");
        mrpField.setPrefHeight(46);
        mrpField.setStyle(fieldStyle);
        VBox mrpBox = new VBox(8, mrpLabel, mrpField);
        HBox.setHgrow(mrpBox, Priority.ALWAYS);

        HBox priceRow = new HBox(20, priceBox, mrpBox);

        // ---------------- Description ----------------
        Label descriptionLabel = new Label("Description");
        descriptionLabel.setStyle(labelStyle);
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Write a short description of the product");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);
        descriptionArea.setStyle(
                "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;");
        VBox descriptionBox = new VBox(8, descriptionLabel, descriptionArea);

        // ---------------- Product image ----------------
        Label imageLabel = new Label("Product image");
        imageLabel.setStyle(labelStyle);

        Label noImageText = new Label("No image");
        noImageText.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px;");

        ImageView imagePreview = new ImageView();
        imagePreview.setFitWidth(140);
        imagePreview.setFitHeight(140);
        imagePreview.setPreserveRatio(true);

        StackPane imageBox = new StackPane(noImageText, imagePreview);
        imageBox.setPrefSize(160, 160);
        imageBox.setMinSize(160, 160);
        imageBox.setMaxSize(160, 160);
        imageBox.setStyle(
                "-fx-background-color: #f4f6fb;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-radius: 8;");

        Button chooseImageButton = new Button("Choose Image");
        chooseImageButton.setPrefHeight(40);
        chooseImageButton.setStyle(
                "-fx-background-color: #eef2ff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #4f46e5;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: #4f46e5;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0 16;"
                        + "-fx-cursor: hand;");

        Label imageFileName = new Label("PNG or JPG file");
        imageFileName.setStyle("-fx-text-fill: #70798b; -fx-font-size: 12px;");

        // Array so the lambdas below can change the chosen file
        File[] selectedImage = new File[1];

        chooseImageButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Product Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

            File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());
            if (selectedFile != null) {
                selectedImage[0] = selectedFile;
                imagePreview.setImage(new Image(selectedFile.toURI().toString()));
                noImageText.setVisible(false);
                imageFileName.setText(selectedFile.getName());
            }
        });

        VBox imageButtonBox = new VBox(10, chooseImageButton, imageFileName);
        imageButtonBox.setAlignment(Pos.CENTER_LEFT);

        HBox imageRow = new HBox(20, imageBox, imageButtonBox);
        imageRow.setAlignment(Pos.CENTER_LEFT);

        VBox imageSection = new VBox(8, imageLabel, imageRow);

        // ---------------- Message and buttons ----------------
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        Region buttonSpacer = new Region();
        HBox.setHgrow(buttonSpacer, Priority.ALWAYS);

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefHeight(46);
        cancelButton.setPrefWidth(140);
        cancelButton.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-text-fill: #344054;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");
        cancelButton.setOnAction(event -> onBack.run());

        Button saveButton = new Button("Save Product");
        saveButton.setPrefHeight(46);
        saveButton.setPrefWidth(160);
        saveButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");

        saveButton.setOnAction(event -> {
            if (nameField.getText().trim().isEmpty()
                    || categoryBox.getValue() == null
                    || priceField.getText().trim().isEmpty()
                    || stockField.getText().trim().isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;");
                messageLabel.setText("Please fill product name, category, price and stock.");
            } else {
                double price;
                double mrp;
                int stock;
                try {
                    price = Double.parseDouble(priceField.getText().trim());
                    stock = Integer.parseInt(stockField.getText().trim());
                    // MRP is optional, use the selling price when it is empty
                    mrp = mrpField.getText().trim().isEmpty() ? price : Double.parseDouble(mrpField.getText().trim());
                } catch (NumberFormatException e) {
                    messageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;");
                    messageLabel.setText("Price, MRP and stock must be numbers.");
                    return;
                }

                // id is null so Firestore generates one; imageUrl is filled after the Cloudinary upload
                Product product = new Product(null, nameField.getText().trim(), categoryBox.getValue(),
                        price, mrp, stock, descriptionArea.getText().trim(), null);

                if (!productController.addProduct(product, selectedImage[0])) {
                    messageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;");
                    messageLabel.setText("Could not save the product. Please try again.");
                    return;
                }

                messageLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 13px; -fx-font-weight: bold;");
                messageLabel.setText("Product \"" + product.getName() + "\" added successfully.");

                selectedImage[0] = null;

                nameField.clear();
                categoryBox.setValue(null);
                stockField.clear();
                priceField.clear();
                mrpField.clear();
                descriptionArea.clear();
                imagePreview.setImage(null);
                noImageText.setVisible(true);
                imageFileName.setText("PNG or JPG file");
            }
        });

        HBox buttonRow = new HBox(12, messageLabel, buttonSpacer, cancelButton, saveButton);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        // ---------------- Form card ----------------
        Label formTitle = new Label("Product Details");
        formTitle.setStyle("-fx-text-fill: #172033; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox formCard = new VBox(
                20,
                formTitle,
                nameBox,
                categoryRow,
                priceRow,
                descriptionBox,
                imageSection,
                buttonRow);
        formCard.setPadding(new Insets(28));
        formCard.setMaxWidth(820);
        formCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 12;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.25), 16, 0, 0, 6);");

        // ---------------- Page ----------------
        VBox content = new VBox(28, header, formCard);
        content.setPadding(new Insets(32, 40, 32, 40));
        content.setStyle("-fx-background-color: #040b24;");

        ScrollPane contentScroll = new ScrollPane(content);
        contentScroll.setFitToWidth(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScroll.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

        return contentScroll;
    }
}
