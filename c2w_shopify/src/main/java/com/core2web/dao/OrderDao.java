package com.core2web.dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.core2web.config.FirebaseInitialize;

import com.core2web.model.CartItem;
import com.core2web.model.Order;

public class OrderDao {

    private static final String COLLECTION = "orders";

    Firestore db = FirebaseInitialize.getFirestore();

    /*
     * Saves the order, reduces product stock and empties the user's cart in one transaction,
     * so either everything is saved or nothing is.
     * Returns null on success, otherwise an error message to show on the screen.
     */
    public String placeOrder(Order order) {
        DocumentReference orderRef = db.collection(COLLECTION).document();
        order.setId(orderRef.getId());

        try {
            db.runTransaction(transaction -> {
                // Firestore needs all reads before any writes in a transaction
                List<DocumentReference> productRefs = new ArrayList<>();
                List<Long> stocks = new ArrayList<>();
                for (CartItem item : order.getItems()) {
                    DocumentReference productRef = db.collection("products").document(item.getProductId());
                    DocumentSnapshot product = transaction.get(productRef).get();
                    if (!product.exists()) {
                        throw new IllegalStateException("\"" + item.getName() + "\" is no longer available.");
                    }
                    Long stock = product.getLong("stock");
                    if (stock == null || stock < item.getQuantity()) {
                        throw new IllegalStateException("Only " + (stock == null ? 0 : stock)
                                + " left in stock for \"" + item.getName() + "\".");
                    }
                    productRefs.add(productRef);
                    stocks.add(stock);
                }

                for (int i = 0; i < productRefs.size(); i++) {
                    CartItem item = order.getItems().get(i);
                    transaction.update(productRefs.get(i), "stock", stocks.get(i) - item.getQuantity());
                    transaction.delete(db.collection("carts").document(order.getUserEmail())
                            .collection("items").document(item.getProductId()));
                }
                transaction.set(orderRef, order);
                return null;
            }).get();
            System.out.println("Order placed successfully!");
            return null;
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IllegalStateException) {
                return e.getCause().getMessage();
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Could not place the order. Please try again.";
    }

    // Newest orders first; sorted here so Firestore does not need a composite index
    public List<Order> getOrdersByUser(String email) {
        try {
            List<Order> orders = db.collection(COLLECTION).whereEqualTo("userEmail", email)
                    .get().get().toObjects(Order.class);
            List<Order> sorted = new ArrayList<>(orders);
            sorted.sort(Comparator.comparingLong(Order::getOrderTime).reversed());
            return sorted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Order> getAllOrders() {
        try {
            List<Order> sorted = new ArrayList<>(db.collection(COLLECTION).get().get().toObjects(Order.class));
            sorted.sort(Comparator.comparingLong(Order::getOrderTime).reversed());
            return sorted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean updateStatus(String orderId, String status) {
        try {
            db.collection(COLLECTION).document(orderId).update("status", status).get();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
