package com.example.test2javafx;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class OrderListCell extends ListCell<CartItem> {
    private Label nameLabel;
    private Label brandLabel;
    private Label descriptionLabel;
    private Label quantityLabel;
    private Label subtotalLabel;

    public OrderListCell() {
        initLabels();
    }

    private void initLabels() {
        nameLabel = new Label();
        brandLabel = new Label();
        descriptionLabel = new Label();
        quantityLabel = new Label();
        subtotalLabel = new Label();
    }

    @Override
    protected void updateItem(CartItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            nameLabel.setText("Product: " + getProductName(item));
            brandLabel.setText("Brand: " + getProductBrand(item));
            descriptionLabel.setText("Description: " + getProductDescription(item));
            quantityLabel.setText("Quantity: " + item.getQuantity());

            float subtotal = item.getSubtotal();
            subtotalLabel.setText(String.format("Subtotal: %.2f$", subtotal));

            VBox vbox = new VBox(nameLabel, brandLabel, descriptionLabel, quantityLabel, subtotalLabel);
            vbox.setSpacing(5);
            setGraphic(vbox);
        }
    }

    private String getProductName(CartItem item) {
        Product product = DBUtils.getProductById(item.getProductId());
        return (product != null) ? product.getName() : "Unknown Product";
    }

    private String getProductBrand(CartItem item) {
        Product product = DBUtils.getProductById(item.getProductId());
        return (product != null) ? product.getBrand() : "Unknown Brand";
    }

    private String getProductDescription(CartItem item) {
        Product product = DBUtils.getProductById(item.getProductId());
        return (product != null) ? product.getDescription() : "No Description";
    }
}
