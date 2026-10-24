package com.core2web.config;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseInitialize {
    static {
        initialize();
    }
    public static void initialize() {
        // initializeApp() throws if called twice, so only do it once
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }
        // Load the key from the classpath so it works no matter where the app is started from
        try (InputStream serviceAccount = FirebaseInitialize.class.getResourceAsStream("/java2026.json")) {
            if (serviceAccount == null) {
                throw new IllegalStateException("java2026.json not found in src/main/resources");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            System.out.println("Firebase initialized!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
