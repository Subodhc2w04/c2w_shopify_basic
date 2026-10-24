package com.core2web.model;

public class Product {

    private String id;
    private String name;
    private String category;
    private double price;
    private double mrp;
    private int stock;
    private String description;
    private String imageUrl;

    public Product() {
    }

    public Product(String id, String name, String category, double price, double mrp,
            int stock, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.mrp = mrp;
        this.stock = stock;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Product{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", category='" + category + '\''
                + ", price=" + price
                + ", mrp=" + mrp
                + ", stock=" + stock
                + ", description='" + description + '\''
                + ", imageUrl='" + imageUrl + '\''
                + '}';
    }
}
