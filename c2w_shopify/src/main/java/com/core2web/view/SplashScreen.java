package com.core2web.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen extends Application {

    private static final Duration LOADING_TIME = Duration.seconds(3);
    public static Stage primaryStage;
    @Override
    public void start(Stage splashStage) {
        primaryStage = splashStage;
        Label brand = new Label("C2W");
        brand.setStyle(
                "-fx-background-color: #4f46e5;"
                        + "-fx-background-radius: 16;"
                        + "-fx-text-fill: white;"
                        + "-fx-font-size: 32px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-padding: 18 22;");

        Label title = new Label("C2W Shopify");
        title.setStyle(
                "-fx-text-fill: white;"
                        + "-fx-font-size: 30px;"
                        + "-fx-font-weight: bold;");

        Label subtitle = new Label("Shop smarter, faster, better");
        subtitle.setStyle("-fx-text-fill: #a5b4fc; -fx-font-size: 14px;");

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(260);
        progressBar.setPrefHeight(6);
        progressBar.setStyle(
                "-fx-accent: #4f46e5;"
                        + "-fx-control-inner-background: #1e2747;"
                        + "-fx-background-color: transparent;");

        Label loadingLabel = new Label("Loading...");
        loadingLabel.setStyle("-fx-text-fill: #70798b; -fx-font-size: 12px;");

        VBox content = new VBox(14, brand, title, subtitle, progressBar, loadingLabel);
        content.setAlignment(Pos.CENTER);
        VBox.setMargin(progressBar, new Insets(24, 0, 0, 0));

        StackPane page = new StackPane(content);
        page.setPadding(new Insets(40));
        page.setStyle(
                "-fx-background-color: #040b24;"
                        + "-fx-border-color: #1e2747;"
                        + "-fx-border-width: 1;");

        Scene scene = new Scene(page, 1350, 800);
        // splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setScene(scene);
        splashStage.setTitle("C2W Shopify");
        splashStage.centerOnScreen();
        splashStage.show();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Fills the progress bar, then opens the login page
        Timeline loading = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(LOADING_TIME, new KeyValue(progressBar.progressProperty(), 1)));
        loading.setOnFinished(event -> {
            // splashStage.close();
            SplashScreen.primaryStage.setScene(new LoginPage().getLoginScene());
            SplashScreen.primaryStage.setTitle("Login Page");
        });
        loading.play();
    }
}
