package com.core2web.model;

public class CartItem {

    private String productId;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;

    public CartItem() {
    }

    public CartItem(String productId, String name, double price, String imageUrl, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Not named getSubtotal() so Firestore does not save it as an extra field
    public double subtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return "CartItem{"
                + "productId='" + productId + '\''
                + ", name='" + name + '\''
                + ", price=" + price
                + ", quantity=" + quantity
                + '}';
    }
}
