package com.core2web.dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import java.util.ArrayList;
import java.util.List;

import com.core2web.config.FirebaseInitialize;

import com.core2web.model.Product;

public class ProductDao {

    private static final String COLLECTION = "products";

    Firestore db = FirebaseInitialize.getFirestore();

    public boolean addProduct(Product product) {
        try {
            DocumentReference docRef;
            if (product.getId() == null || product.getId().isBlank()) {
                // Let Firestore generate a unique id and keep it inside the product too
                docRef = db.collection(COLLECTION).document();
                product.setId(docRef.getId());
            } else {
                docRef = db.collection(COLLECTION).document(product.getId());
            }
            // .get() waits until Firestore has actually saved the document
            docRef.set(product).get();
            System.out.println("Product added successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Product getProductById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        try {
            DocumentSnapshot snapshot = db.collection(COLLECTION).document(id).get().get();
            return snapshot.exists() ? snapshot.toObject(Product.class) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts() {
        try {
            return db.collection(COLLECTION).get().get().toObjects(Product.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
