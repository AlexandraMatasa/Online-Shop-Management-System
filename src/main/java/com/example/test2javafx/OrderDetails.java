package com.example.test2javafx;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class OrderDetails {
    private int orderId;
    private Date orderDate;
    private float totalAmount;
    private List<Product> products;
    private Map<Integer, Integer> productQuantities; // Map productId to quantity
    private int paymentMethodId;


    public OrderDetails(int orderId, Date orderDate, float totalAmount, List<Product> products, Map<Integer, Integer> productQuantities, int paymentMethodId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethodId = paymentMethodId;
        // Ensure that products and productQuantities are not null
        if (products != null && productQuantities != null) {
            this.products = products;
            this.productQuantities = productQuantities;
        } else {
            throw new IllegalArgumentException("Products and productQuantities cannot be null.");
        }
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Map<Integer, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<Integer, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }
    public int getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPaymentMethodString() {
        // You can implement logic to retrieve the payment method string based on paymentMethodId
        switch (paymentMethodId) {
            case 1:
                return "Credit Card";
            case 2:
                return "PayPal";
            case 3:
                return "Cash on Delivery";
            default:
                return "Unknown Payment Method";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order Date: ").append(orderDate).append("\n");
        sb.append("Total Amount: $").append(totalAmount).append("\n");
        sb.append("Payment Method: ").append(getPaymentMethodString()).append("\n");
        sb.append("Products:\n");

        if (products != null) {
            for (Product product : products) {
                int productId = product.getProductId();
                sb.append("\t").append(product.getName()).append(" by ").append(product.getBrand()).append(" - $").append(product.getPrice());

                if (productQuantities != null) {
                    if (productQuantities.containsKey(productId)) {
                        sb.append(" (Quantity: ").append(productQuantities.get(productId)).append(")");
                    } else {
                        System.out.println("Quantity not found for product ID: " + productId);
                    }
                } else {
                    System.out.println("Product quantities map is null.");
                }

                sb.append("\n");
            }
        } else {
            System.out.println("Products list is null.");
        }

        return sb.toString();
    }




}
