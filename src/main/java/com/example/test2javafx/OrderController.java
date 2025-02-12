package com.example.test2javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderController {
    @FXML
    private ListView<CartItem> orderDetailsListView;
    @FXML
    private Label totalLabel;
    public void initializeData(ObservableList<CartItem> cartItems) {
        orderDetailsListView.setItems(cartItems);
        orderDetailsListView.setCellFactory(param -> new OrderListCell());
        // Calculate and set the total
        float total = calculateTotal(cartItems);
        totalLabel.setText("Total: " + String.format("%.2f$", total));
    }
    private float calculateTotal(ObservableList<CartItem> cartItems) {
        float total = 0.0f;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getSubtotal();
        }
        return total;
    }

    public void handleConfirmOrder(ActionEvent event) {
        // Get the current user and cart items
        User currentUser = DBUtils.getCurrentUser();
        List<CartItem> cartItems = new ArrayList<>(DBUtils.getCartItems(currentUser));

        // Show a payment method selection dialog
        List<String> paymentMethods = Arrays.asList("Credit Card", "PayPal", "Cash on Delivery");
        ChoiceDialog<String> paymentDialog = new ChoiceDialog<>(paymentMethods.get(0), paymentMethods);
        paymentDialog.setTitle("Select Payment Method");
        paymentDialog.setHeaderText("Choose a payment method:");
        paymentDialog.setContentText("Payment Method:");
        Optional<String> selectedPayment = paymentDialog.showAndWait();

        if (selectedPayment.isPresent()) {
            // Insert order into the database
            float totalAmount = calculateTotal(FXCollections.observableArrayList(cartItems));
            String selectedPaymentMethod = selectedPayment.get();
            int orderId = DBUtils.insertOrder(currentUser, totalAmount, cartItems, selectedPaymentMethod);

            // Show a confirmation dialog before placing the order
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Place Order");
            confirmationAlert.setHeaderText("Are you sure you want to place the order?");
            // confirmationAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Update stockQuantity in the product table
                for (CartItem cartItem : cartItems) {
                    int productId = cartItem.getProductId();
                    int newStockQuantity = DBUtils.getProductStockQuantity(productId) - cartItem.getQuantity();
                    DBUtils.updateProductStockQuantity(productId, newStockQuantity);

                    // Additional logic...

                }

                // Clear the cart after placing the order
                DBUtils.clearCart(currentUser);

                Node source = (Node) event.getSource();
                Stage stageOrder = (Stage) source.getScene().getWindow();
                stageOrder.close();

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Order Placed");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your order has been placed successfully!\nPayment Method: " + selectedPaymentMethod);
                successAlert.showAndWait();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("shop.fxml"));
                    Parent root = loader.load();

                    // Get the controller of the shop.fxml file
                    ShopController shopController = loader.getController();

                    shopController.switchToProductsTab();

                    // Show the stage
                    Stage stage = new Stage();
                    stage.setTitle("Shop");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }
    }


    public void handleBackToCart(ActionEvent event) {
        // Get the source Node from the event
        Node source = (Node) event.getSource();
        // Get the Stage from the source Node
        Stage stageOrder = (Stage) source.getScene().getWindow();
        // Close the stage
        stageOrder.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("shop.fxml"));
            Parent root = loader.load();

            // Get the controller of the shop.fxml file
            ShopController shopController = loader.getController();

            // Switch to the "cart" tab
            shopController.switchToCartTab();

            // Show the stage
            Stage stage = new Stage();
            stage.setTitle("Shop");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}

