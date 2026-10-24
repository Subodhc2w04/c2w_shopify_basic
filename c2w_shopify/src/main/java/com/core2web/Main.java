package com.core2web;

import com.core2web.view.SplashScreen;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting the application...");
        Application.launch(SplashScreen.class, args);
    }
}