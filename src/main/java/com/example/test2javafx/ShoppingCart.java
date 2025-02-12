package com.example.test2javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCart {
    private ObservableList<Product> cartItems;

    public ShoppingCart() {
        this.cartItems = FXCollections.observableArrayList();
    }

    public void addToCart(Product product) {
        cartItems.add(product);
        System.out.println("Added to cart: " + product.getName());
    }

    public ObservableList<Product> getCartItems() {
        return FXCollections.unmodifiableObservableList(cartItems);
    }
}
