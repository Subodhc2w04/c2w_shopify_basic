package com.core2web.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.core2web.config.FirebaseInitialize;

import com.core2web.model.CartItem;

public class CartDao {

    Firestore db = FirebaseInitialize.getFirestore();

    // Each user's cart is stored at carts/{email}/items/{productId}
    private CollectionReference itemsOf(String email) {
        return db.collection("carts").document(email).collection("items");
    }

    // Adds the product, or increases its quantity if it is already in the cart
    public boolean addToCart(String email, CartItem item) {
        try {
            DocumentReference docRef = itemsOf(email).document(item.getProductId());
            DocumentSnapshot snapshot = docRef.get().get();
            if (snapshot.exists()) {
                CartItem existing = snapshot.toObject(CartItem.class);
                item.setQuantity(existing.getQuantity() + item.getQuantity());
            }
            docRef.set(item).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public CartItem getCartItem(String email, String productId) {
        try {
            DocumentSnapshot snapshot = itemsOf(email).document(productId).get().get();
            return snapshot.exists() ? snapshot.toObject(CartItem.class) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<CartItem> getCartItems(String email) {
        try {
            return itemsOf(email).get().get().toObjects(CartItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean updateQuantity(String email, String productId, int quantity) {
        try {
            itemsOf(email).document(productId).update("quantity", quantity).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFromCart(String email, String productId) {
        try {
            itemsOf(email).document(productId).delete().get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clearCart(String email) {
        try {
            WriteBatch batch = db.batch();
            for (QueryDocumentSnapshot doc : itemsOf(email).get().get()) {
                batch.delete(doc.getReference());
            }
            batch.commit().get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
