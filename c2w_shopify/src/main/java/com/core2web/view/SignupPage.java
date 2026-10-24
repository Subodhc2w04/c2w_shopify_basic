package com.core2web.view;

import com.core2web.controller.UserController;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SignupPage {

    private static final String LABEL_STYLE =
            "-fx-text-fill: #344054; -fx-font-size: 13px; -fx-font-weight: bold;";

    private static final String FIELD_STYLE =
            "-fx-background-color: #ffffff;"
                    + "-fx-background-radius: 8;"
                    + "-fx-border-color: #d9deea;"
                    + "-fx-border-radius: 8;"
                    + "-fx-font-size: 14px;"
                    + "-fx-padding: 0 12;";

    private static final String ERROR_STYLE =
            "-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;";

    private static final String SUCCESS_STYLE =
            "-fx-text-fill: #16a34a; -fx-font-size: 13px; -fx-font-weight: bold;";

    UserController userController = new UserController();

    public Scene getSignupScene(Runnable switchToLogin) {
        Label brand = new Label("C2W");
        brand.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 12;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12 14;");

        Label title = new Label("Create account");
        title.setStyle(
                "-fx-text-fill: #172033;"
                        + "-fx-font-size: 30px;"
                        + "-fx-font-weight: bold;");

        Label subtitle = new Label("Sign up to start shopping with C2W");
        subtitle.setStyle("-fx-text-fill: #70798b; -fx-font-size: 14px;");

        Label nameLabel = new Label("Full name");
        nameLabel.setStyle(LABEL_STYLE);
        TextField nameField = new TextField();
        nameField.setPromptText("John Doe");
        nameField.setPrefHeight(46);
        nameField.setStyle(
                FIELD_STYLE);
        
        Label emailLabel = new Label("Email address");
        emailLabel.setStyle(LABEL_STYLE);
        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(46);
        emailField.setStyle(
                FIELD_STYLE);

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle(LABEL_STYLE  );
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");
        passwordField.setPrefHeight(46);
        passwordField.setStyle(
                FIELD_STYLE);

        Label confirmLabel = new Label("Confirm password");
        confirmLabel.setStyle(LABEL_STYLE);
        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Re-enter your password");
        confirmField.setPrefHeight(46);
        confirmField.setStyle(
                FIELD_STYLE);

        Label roleLabel = new Label("Role");
        roleLabel.setStyle(LABEL_STYLE);
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("User", "Admin", "Delivery");
        roleBox.setValue("User");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.setPrefHeight(46);
        roleBox.setStyle(
                FIELD_STYLE);

        CheckBox termsCheck = new CheckBox("I agree to the Terms & Privacy Policy");
        termsCheck.setStyle("-fx-text-fill: #596579; -fx-font-size: 12px;");

        Button signupButton = new Button("Create account");
        signupButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setPrefHeight(48);
        signupButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");
        signupButton.disableProperty().bind(termsCheck.selectedProperty().not());

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setStyle(ERROR_STYLE);

        signupButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setStyle(ERROR_STYLE);
                messageLabel.setText("Please fill name, email and password.");
            } else if (!email.contains("@")) {
                messageLabel.setStyle(ERROR_STYLE);
                messageLabel.setText("Please enter a valid email address.");
            } else if (!password.equals(confirmField.getText())) {
                messageLabel.setStyle(ERROR_STYLE);
                messageLabel.setText("Passwords do not match.");
            } else {
                // Saves the user in the Firestore "users" collection
                String error = userController.addUser(name, email, password, roleBox.getValue());
                if (error != null) {
                    messageLabel.setStyle(ERROR_STYLE);
                    messageLabel.setText(error);
                } else {
                    messageLabel.setStyle(SUCCESS_STYLE);
                    messageLabel.setText("Account created! You can now sign in.");
                    nameField.clear();
                    emailField.clear();
                    passwordField.clear();
                    confirmField.clear();
                    roleBox.setValue("User");
                    termsCheck.setSelected(false);
                }
            }
        });

        Label accountPrompt = new Label("Already have an account?");
        accountPrompt.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px;");

        Hyperlink signInLink = new Hyperlink("Sign in");
        signInLink.setStyle(
                "-fx-text-fill: #4f46e5;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;");

        signInLink.setOnAction(event -> switchToLogin.run());

        HBox signInRow = new HBox(4, accountPrompt, signInLink);
        signInRow.setAlignment(Pos.CENTER);

        VBox signupCard = new VBox(
                12,
                brand,
                title,
                subtitle,
                nameLabel,
                nameField,
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                confirmLabel,
                confirmField,
                roleLabel,
                roleBox,
                termsCheck,
                messageLabel,
                signupButton,
                signInRow);
        signupCard.setAlignment(Pos.CENTER_LEFT);
        signupCard.setMaxWidth(400);
        signupCard.setMaxHeight(Double.NEGATIVE_INFINITY);
        signupCard.setPadding(new Insets(40));
        signupCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(31, 41, 55, 0.12), 24, 0, 0, 8);");

        StackPane page = new StackPane(signupCard);
        page.setPadding(new Insets(24));
        page.setStyle("-fx-background-color: #040b24;");

        // Card is taller now; scroll instead of clipping on short windows
        ScrollPane scrollPane = new ScrollPane(page);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #040b24; -fx-background-color: #040b24;");

        return new Scene(scrollPane, 1350, 800);
    }

}
