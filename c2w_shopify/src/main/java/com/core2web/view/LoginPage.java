package com.core2web.view;

import com.core2web.controller.UserController;
import com.core2web.model.User;
import com.core2web.view.admin.AdminHomePage;
import com.core2web.view.delivery_partner.DeliveryPartnerHomePage;
import com.core2web.view.user.UserHomePage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginPage {

    public Scene loginScene;
    UserController userController = new UserController();

    public Scene getLoginScene() {

        Label brand = new Label("C2W");
        brand.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 12;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 18px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 12 14;");

        Label title = new Label("Welcome back");
        title.setStyle(
                "-fx-text-fill: #172033;"
                        + "-fx-font-size: 30px;"
                        + "-fx-font-weight: bold;");

        Label subtitle = new Label("Sign in to continue to your account");
        subtitle.setStyle("-fx-text-fill: #70798b; -fx-font-size: 14px;");

        Label emailLabel = new Label("Email address");
        emailLabel.setStyle("-fx-text-fill: #344054; -fx-font-size: 13px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setPrefHeight(46);
        emailField.setStyle(
                "-fx-background-color: #ffffff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 0 12;");

        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-text-fill: #344054; -fx-font-size: 13px; -fx-font-weight: bold;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(46);
        passwordField.setStyle(
                "-fx-background-color: #ffffff;"
                        + "-fx-background-radius: 8;"
                        + "-fx-border-color: #d9deea;"
                        + "-fx-border-radius: 8;"
                        + "-fx-font-size: 14px;"
                        + "-fx-padding: 0 12;");

        CheckBox rememberMe = new CheckBox("Remember me");
        rememberMe.setStyle("-fx-text-fill: #596579; -fx-font-size: 12px;");

        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setStyle(
                "-fx-text-fill: #4f46e5;"
                        + "-fx-font-size: 12px;"
                        + "-fx-padding: 0;");

        HBox options = new HBox(rememberMe, forgotPassword);
        options.setAlignment(Pos.CENTER_LEFT);
        options.setSpacing(10);
        HBox.setHgrow(rememberMe, javafx.scene.layout.Priority.ALWAYS);
        forgotPassword.setAlignment(Pos.CENTER_RIGHT);

        Label messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 13px; -fx-font-weight: bold;");

        Button loginButton = new Button("Sign in");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(48);
        loginButton.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 8;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 15px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;");
        loginButton.setOnAction(event -> {
            Runnable switchToLogin = () -> {
                SplashScreen.primaryStage.setScene(loginScene);
                SplashScreen.primaryStage.setTitle("Login Page");
            };
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText();
            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter email and password.");
                return;
            }

            // Checks the email and password against the Firestore "users" collection
            User user = userController.authenticateUser(email, password);
            if (user == null) {
                messageLabel.setText("Invalid email or password.");
                return;
            }

            messageLabel.setText("");
            passwordField.clear();
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                SplashScreen.primaryStage.setScene(new AdminHomePage().getAdminHomeScene(switchToLogin));
                SplashScreen.primaryStage.setTitle("Admin Home Page");
            } else if ("Delivery".equalsIgnoreCase(user.getRole())) {
                SplashScreen.primaryStage.setScene(new DeliveryPartnerHomePage(user).getDeliveryPartnerHomeScene(switchToLogin));
                SplashScreen.primaryStage.setTitle("Delivery Partner Home Page");
            } else {
                // The logged in user is needed for the cart and orders
                SplashScreen.primaryStage.setScene(new UserHomePage(user).getUserHomeScene(switchToLogin));
                SplashScreen.primaryStage.setTitle("Home Page");
            }
        });

        Label accountPrompt = new Label("Don't have an account?");
        accountPrompt.setStyle("-fx-text-fill: #70798b; -fx-font-size: 13px;");

        Hyperlink signUpLink = new Hyperlink("Sign up");
        signUpLink.setStyle(
                "-fx-text-fill: #4f46e5;"
                        + "-fx-font-size: 13px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 0;");
        signUpLink.setOnAction(event -> {

            Runnable switchToLogin = () -> {
                SplashScreen.primaryStage.setScene(loginScene);
                SplashScreen.primaryStage.setTitle("Login Page");
            };  
            SplashScreen.primaryStage.setScene(new SignupPage().getSignupScene(switchToLogin));
            SplashScreen.primaryStage.setTitle("Signup Page");
        });

        HBox signUpRow = new HBox(4, accountPrompt, signUpLink);
        signUpRow.setAlignment(Pos.CENTER);

        VBox loginCard = new VBox(
                12,
                brand,
                title,
                subtitle,
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                options,
                messageLabel,
                loginButton,
                signUpRow);
        loginCard.setAlignment(Pos.CENTER_LEFT);
        loginCard.setMaxWidth(400);
        loginCard.setMaxHeight(600);
        loginCard.setPadding(new Insets(40));
        loginCard.setStyle(
                "-fx-background-color: white;"
                        + "-fx-background-radius: 16;"
                        + "-fx-effect: dropshadow(three-pass-box, rgba(31, 41, 55, 0.12), 24, 0, 0, 8);");

        StackPane page = new StackPane(loginCard);
        page.setPadding(new Insets(24));
        page.setStyle(
                "-fx-background-color: #040b24;");

        Scene scene = new Scene(page, 1350, 800);
        loginScene = scene;
        return scene;
    }
}
