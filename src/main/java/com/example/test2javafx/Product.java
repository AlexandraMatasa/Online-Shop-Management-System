package com.example.test2javafx;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Product {
    private int productId;
    private String name;
    private String brand;
    private String description;
    private float price;
    private int stockQuantity;
    private int categoryId;
    private float avgRating;
    private String imageUrl;
    private Boolean isVisible;

    public Product () {

    }

    public Product(int productId, String name, String brand, String description, float price, int stockQuantity, int categoryId, float avgRating, String imageUrl, Boolean isVisible){
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.avgRating = avgRating;
        this.imageUrl = imageUrl;
        this.isVisible = isVisible;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public URL getImageUrl() throws MalformedURLException {
        File file = new File(imageUrl);
        URL fileUrl = file.toURI().toURL();
        return fileUrl;
    }
    public String getStringImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    @Override
    public String toString() {
        return String.format("%s by %s\nDescription: %s\nPrice: %.2f$\nStock Quantity: %d\nAverage Rating: %.2f",
                name, brand, description, price, stockQuantity, avgRating);
    }

}
