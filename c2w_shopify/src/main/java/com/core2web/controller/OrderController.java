package com.core2web.controller;

import java.util.List;

import com.core2web.dao.CartDao;
import com.core2web.dao.OrderDao;
import com.core2web.model.CartItem;
import com.core2web.model.Order;
import com.core2web.model.User;

public class OrderController {

    public static final String[] STATUSES = {"Pending", "Shipped", "Delivered", "Cancelled"};

    OrderDao orderDao = new OrderDao();
    CartDao cartDao = new CartDao();

    /*
     * Places an order for everything in the user's cart. No payment gateway: payment is Cash on Delivery.
     * Returns the saved Order on success; on failure returns null and puts the reason in error[0].
     */
    public Order placeOrder(User user, String address, String phone, String[] error) {
        if (address == null || address.isBlank()) {
            error[0] = "Please enter the delivery address.";
            return null;
        }
        if (phone == null || !phone.matches("\\d{10}")) {
            error[0] = "Please enter a valid 10 digit phone number.";
            return null;
        }

        List<CartItem> items = cartDao.getCartItems(user.getEmail());
        if (items.isEmpty()) {
            error[0] = "Your cart is empty.";
            return null;
        }

        double total = 0;
        for (CartItem item : items) {
            total += item.subtotal();
        }

        Order order = new Order(user.getEmail(), user.getName(), items, total,
                address.trim(), phone, "Cash on Delivery", "Pending", System.currentTimeMillis());

        String result = orderDao.placeOrder(order);
        if (result != null) {
            error[0] = result;
            return null;
        }
        return order;
    }

    public List<Order> getOrdersByUser(String email) {
        return orderDao.getOrdersByUser(email);
    }

    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public boolean updateStatus(String orderId, String status) {
        return orderDao.updateStatus(orderId, status);
    }
}
