package com.core2web.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.core2web.config.FirebaseInitialize;

import com.core2web.model.User;

public class UserDao {

    private static final String COLLECTION = "users";

    Firestore db = FirebaseInitialize.getFirestore();

    // Email is used as the document id, so one email = one account
    public boolean addUser(User user) {
        try {
            // .get() waits until Firestore has actually saved the document
            db.collection(COLLECTION).document(user.getEmail()).set(user).get();
            System.out.println("User added successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        try {
            DocumentSnapshot snapshot = db.collection(COLLECTION).document(email).get().get();
            return snapshot.exists() ? snapshot.toObject(User.class) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean userExists(String email) {
        return getUserByEmail(email) != null;
    }

    public List<User> getAllUsers() {
        try {
            return db.collection(COLLECTION).get().get().toObjects(User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
