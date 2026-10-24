package com.core2web.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private String id;
    private String userEmail;
    private String userName;
    private List<CartItem> items = new ArrayList<>();
    private double totalAmount;
    private String address;
    private String phone;
    private String paymentMethod;
    private String status;
    private long orderTime;

    public Order() {
    }

    public Order(String userEmail, String userName, List<CartItem> items, double totalAmount,
            String address, String phone, String paymentMethod, String status, long orderTime) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.address = address;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.orderTime = orderTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "Order{"
                + "id='" + id + '\''
                + ", userEmail='" + userEmail + '\''
                + ", items=" + items
                + ", totalAmount=" + totalAmount
                + ", status='" + status + '\''
                + '}';
    }
}
