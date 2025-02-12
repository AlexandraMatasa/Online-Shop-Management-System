package com.example.test2javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderHistoryController implements Initializable {

    @FXML
    private ListView<OrderDetails> orderHistoryListView;

    @FXML
    private Button buttonBackToCart;

    @FXML
    private Button buttonAddReview;

    @FXML
    void handleButtonAddReview(ActionEvent event) {
        OrderDetails selectedOrder = orderHistoryListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            for (Product product : selectedOrder.getProducts()) {
                showReviewDialog(selectedOrder, product);
            }
        } else {
            showWarningDialog("Error", "Please select an order to add a review.");
        }
    }

    @FXML
    void handleButtonBackToCart(ActionEvent event) {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Fetch the user's order history from the database
        User currentUser = DBUtils.getCurrentUser();
        ObservableList<OrderDetails> orderHistory = FXCollections.observableArrayList();

        try {
            orderHistory.addAll(DBUtils.getOrderHistory(currentUser));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        // Set items for orderHistoryListView
        setOrderHistoryItems(orderHistory);
    }

    // Public method to set items for orderHistoryListView
    public void setOrderHistoryItems(ObservableList<OrderDetails> orderHistory) {
        orderHistoryListView.setItems(orderHistory);
    }

    private void showReviewDialog(OrderDetails order, Product product) {
        Dialog<ProductReview> dialog = new Dialog<>();
        dialog.setTitle("Add Review");
        dialog.setHeaderText("Add a review for " + product.getName());

        // Create a new GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create a list to hold the product review
        List<ProductReview> productReviews = new ArrayList<>();

        // For the selected product, create a text field for the review
        TextField reviewField = new TextField();
        reviewField.setPromptText("Review for " + product.getName());

        // Create a choice box for the rating
        ChoiceBox<Float> ratingChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f));
        ratingChoiceBox.setTooltip(new Tooltip("Select rating"));

        // Add labels and fields to the grid
        Label productNameLabel = new Label(product.getName() + " by " + product.getBrand());
        grid.add(productNameLabel, 0, 0);
        grid.add(new Label("Rating:"), 1, 0);
        grid.add(ratingChoiceBox, 2, 0);
        grid.add(new Label("Review:"), 3, 0);
        grid.add(reviewField, 4, 0);

        dialog.getDialogPane().setContent(grid);

        // Create custom buttons
        ButtonType addReviewButtonType = new ButtonType("Add review", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addReviewButtonType, cancelButtonType);

        // Request focus on the review field by default
        Platform.runLater(() -> reviewField.requestFocus());

        // Handle the "Add Review" button
        Button addReviewButton = (Button) dialog.getDialogPane().lookupButton(addReviewButtonType);

        // Add an event filter to prevent the dialog from closing when the input is invalid
        addReviewButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateReviewInput(reviewField.getText(), ratingChoiceBox.getValue())) {
                event.consume(); // Consume the event to prevent closing the dialog
                showWarningDialog("Error", "A valid review is required. Please provide valid values.");
            }
        });

        // Add a ProductReview to the list when the user clicks "Add Review"
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addReviewButtonType) {
                String reviewText = reviewField.getText();
                Float rating = ratingChoiceBox.getValue();

                // Check if the review is valid
                if (!validateReviewInput(reviewText, rating)) {
                    // Show a warning that at least one valid review is required
                    showWarningDialog("Error", "A valid review is required. Please provide valid values.");
                    return null; // Return null to prevent closing the dialog
                }

                ProductReview review = new ProductReview(
                        DBUtils.getCurrentUser().getUserId(),
                        product.getProductId(),
                        rating,
                        reviewText,
                        new java.sql.Date(System.currentTimeMillis())  // Use java.sql.Date here
                );
                // Save the review in the database
                DBUtils.saveProductReview(order.getOrderId(), review);
                showInformationDialog("Review Added", "Review added successfully!");

//                productReviews.add(review);
                return review; // Return the review
            }
            return null;
        });

        // Show the dialog and wait for the user's interaction
        Optional<ProductReview> result = dialog.showAndWait();

        // You can perform additional actions based on the result if needed
        result.ifPresent(review -> {
            // Handle the result if necessary
//            showInformationDialog("Review Added", "Review added successfully!");
        });
    }

    // Helper method to validate review input
    private boolean validateReviewInput(String reviewText, Float rating) {
        return reviewText != null && !reviewText.trim().isEmpty() && rating != null;
    }

    private void showWarningDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
