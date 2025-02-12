package com.example.test2javafx;

public class CartItem {
    private int cartItemId;
    private int productId;
    private int quantity;
    private int userId;
    private float subtotal;

    public CartItem(int cartItemId, int productId, int quantity, int userId, float subtotal) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.subtotal = subtotal;
    }

    // Constructor without subtotal (for existing items in the database)
    public CartItem(int cartItemId, int productId, int quantity, int userId) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.subtotal = calculateSubtotal();  // Calculate subtotal based on price and quantity
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return DBUtils.getProductById(productId);
    }

    private float calculateSubtotal() {
        Product product = DBUtils.getProductById(productId);
        return product.getPrice() * quantity;
    }


    @Override
    public String toString() {
        Product product = DBUtils.getProductById(productId);
        System.out.println(product.toString());
        return String.format("%s by %s\nDescription: %s\nPrice: %.2f$\nAverage Rating: %.2f\n",
                product.getName(), product.getBrand(), product.getDescription(), product.getPrice(), product.getAvgRating(), quantity);
    }


}
