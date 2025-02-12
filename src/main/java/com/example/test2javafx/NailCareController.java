package com.example.test2javafx;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class NailCareController implements Initializable {
    @FXML
    private ListView<Product> productListView;
    private ListView<CartItem> cartListView;

    @FXML
    private Button button_add_product;

    @FXML
    private Button button_remove_product;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User currentUser = DBUtils.getCurrentUser();
        if (productListView != null) {
            if(currentUser.getRole() == UserRole.CLIENT) {
                productListView.getItems().addAll(DBUtils.getVisibleProductsInStockFromBD(2));
            } else {
                if(currentUser.getRole() == UserRole.ADMIN) {
                    productListView.getItems().addAll(DBUtils.getAllVisibleProductsFromBD(2));
                }
            }
            productListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
            productListView.setCellFactory(param -> new ListCell<Product>() {
                @Override
                protected void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    if (empty || product == null) {
                        setGraphic(null);
                    } else {
                        Image image = null;
                        try {
                            image = new Image(String.valueOf(product.getImageUrl()));
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(90);
                        imageView.setFitHeight(90);
                        HBox imageContainer = new HBox(imageView);
                        imageContainer.setAlignment(Pos.CENTER_LEFT);
                        imageContainer.setPrefWidth(getListView().getWidth() / 5.5);

                        HBox infoContainer = new HBox();
                        infoContainer.setAlignment(Pos.CENTER_LEFT);
                        infoContainer.getChildren().add(createProductInfoNode(product));
                        infoContainer.setPrefWidth(getListView().getWidth() / 2);

                        HBox buttonContainer = new HBox();
                        if (DBUtils.getCurrentUser() != null && DBUtils.getCurrentUser().getRole() == UserRole.ADMIN) {
                            Button editButton = new Button();
                            Image editImage = new Image(getClass().getResourceAsStream("edit_icon.png"));
                            ImageView editIcon = new ImageView(editImage);
                            editIcon.setFitWidth(16);
                            editIcon.setFitHeight(16);
                            HBox editBox = new HBox();
                            editBox.getChildren().addAll(new Label("Edit product     "), editIcon);
                            editBox.setPrefWidth(100);
                            editButton.setGraphic(editBox);
                            buttonContainer.getChildren().add(editButton);

                            buttonContainer.setSpacing(10);
                            buttonContainer.setAlignment(Pos.CENTER);
                            editButton.setOnAction(event -> {
                                CompletableFuture<Product> newProduct = showEditProductDialog(product);
                                // Callback to handle the updated product when it's available
                                newProduct.thenAccept(updatedProduct -> {
                                    // Access properties of the updated product, for example, stockQuantity
                                    int updatedStockQuantity = updatedProduct.getStockQuantity();
                                    if(updatedStockQuantity <= 0){
                                        Alert alert = new Alert(Alert.AlertType.WARNING);
                                        alert.setTitle("Restock Required");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Product '" + updatedProduct.getName() + "' is out of stock. Please restock before proceeding.");
                                        alert.showAndWait();
                                    }
                                    System.out.println("Updated Stock Quantity: " + updatedStockQuantity);
                                });

                                System.out.println("Editing product: " + product.getName());
                            });
                        } else {
                            if (DBUtils.getCurrentUser() != null && DBUtils.getCurrentUser().getRole() == UserRole.CLIENT) {
                                Button addToCartButton = new Button("Add to Cart");
                                addToCartButton.setOnAction(event -> {
                                    System.out.println("Product added to cart: " + product.getName());
                                    addToCart(product);
                                });
                                buttonContainer.getChildren().add(addToCartButton);
                                buttonContainer.setAlignment(Pos.CENTER_LEFT);
                                buttonContainer.setPrefWidth(getListView().getWidth() / 3);
                            }
                        }
                        HBox mainContainer = new HBox(imageContainer, infoContainer, buttonContainer);
                        mainContainer.setAlignment(Pos.CENTER);
                        mainContainer.setPrefWidth(getListView().getWidth());
                        setGraphic(mainContainer);
                    }
                }
            });
        } else {
            System.err.println("productListView is null");
        }

        if (currentUser.getRole() == UserRole.ADMIN) {
            button_add_product.setVisible(true);
            button_remove_product.setVisible(true);
            productListView.setPrefHeight(360);
            productListView.setPrefWidth(600);
        } else {
            button_add_product.setVisible(false);
            button_remove_product.setVisible(false);
            productListView.setPrefHeight(400);
            productListView.setPrefWidth(600);
        }

        button_add_product.setOnAction(event -> {
            showAddProductDialog();
        });

        button_remove_product.setOnAction(event -> {
            productListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
            ObservableList<Product> selectedProducts = productListView.getSelectionModel().getSelectedItems();

            if (!selectedProducts.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Remove products");
                alert.setContentText("Are you sure you want to remove the selected product(s)?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    removeSelectedProducts(selectedProducts);
                    updateAdminNailCareProductListView();   //pt admin
                }
            } else {
                // Inform the user that no user is selected
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select product(s) to remove.");
                alert.showAndWait();
            }
        });
    }

    private void removeSelectedProducts(ObservableList<Product> selectedProducts) {
        List<Product> copyOfSelectedProducts = new ArrayList<>(selectedProducts);
        for (Product product : copyOfSelectedProducts) {
            product.setVisible(false);
            setProductNotVisibleInDB(product);
        }
    }

    private void showAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");

        // Set the button types (OK and Cancel)
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the GridPane for the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // Create form controls
        TextField name = new TextField();
        TextField brand = new TextField();
        TextField description = new TextField();
        TextField price = new TextField();
        TextField stockQuantity = new TextField();
        TextField imageUrl = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Brand:"), 0, 1);
        grid.add(brand, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(description, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(price, 1, 3);
        grid.add(new Label("Stock quantity:"), 0, 4);
        grid.add(stockQuantity, 1, 4);
        grid.add(new Label("Image Url:"), 0, 5);
        grid.add(imageUrl, 1, 5);

        // Enable/Disable add button based on whether required fields are entered
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        List<TextField> requiredFields = Arrays.asList(name, brand, price, description, price, stockQuantity, imageUrl);

        for (TextField field : requiredFields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                addButtonNode.setDisable(requiredFields.stream().anyMatch(tf -> tf.getText().trim().isEmpty()));
            });
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Product(0, name.getText(), brand.getText(), description.getText(), Float.parseFloat(price.getText()),
                        Integer.parseInt(stockQuantity.getText()), 1, 0, imageUrl.getText(), true);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(product -> {
            if (product != null) {
                addProductToDatabase(product);
            }
        });
    }

    private void addProductToDatabase(Product product) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "INSERT INTO product (name, brand, description, price, stockQuantity, categoryId, avgRating, imageUrl, isVisible) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, product.getName());
            pst.setString(2, product.getBrand());
            pst.setString(3, product.getDescription());
            pst.setFloat(4, product.getPrice());
            pst.setInt(5, product.getStockQuantity());
            pst.setInt(6, product.getCategoryId());
            pst.setFloat(7, product.getAvgRating());
            pst.setString(8, product.getStringImageUrl());
            pst.setBoolean(9, product.getVisible());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added to the database: " + product.getName());

                // If you have an auto-incremented primary key (userId), retrieve it
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productId = generatedKeys.getInt(1);
                        product.setProductId(productId);
                        System.out.println("Generated productId: " + productId);
                        updateAdminNailCareProductListView();  //pt admin
                    } else {
                        System.err.println("Failed to retrieve productId.");
                    }
                }
            } else {
                System.err.println("Failed to add product to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToCart(Product product) {
        DBUtils.addToCart(DBUtils.getCurrentUser(), product, 1);
        if (cartListView != null) {
            cartListView.getItems().setAll(DBUtils.getCartItems(DBUtils.getCurrentUser()));
        }
    }

    private CompletableFuture<Product> showEditProductDialog(Product product) {
        CompletableFuture<Product> resultFuture = new CompletableFuture<>();

        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Edit Product");
        dialog.setHeaderText("Editing product: " + product.getName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(product.getName());
        TextField brandField = new TextField(product.getBrand());
        TextField descriptionField = new TextField(product.getDescription());
        TextField priceField = new TextField(String.valueOf(product.getPrice()));
        TextField stockQuantityField = new TextField(String.valueOf(product.getStockQuantity()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Brand:"), 0, 1);
        grid.add(brandField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Stock Quantity:"), 0, 4);
        grid.add(stockQuantityField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);
        List<TextField> requiredFields = Arrays.asList(nameField, brandField, descriptionField, priceField, stockQuantityField);
        for (TextField field : requiredFields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(requiredFields.stream().anyMatch(tf -> tf.getText().trim().isEmpty()));
            });
        }
        Platform.runLater(() -> nameField.requestFocus());    // Request focus on the name field by default

        // Convert the result to a product when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Product updatedProduct = new Product(product.getProductId(), nameField.getText(),
                        brandField.getText(), descriptionField.getText(),
                        Float.parseFloat(priceField.getText()), Integer.parseInt(stockQuantityField.getText()),
                        product.getCategoryId(), product.getAvgRating(), product.getStringImageUrl(), true);
                resultFuture.complete(updatedProduct); // Complete the CompletableFuture with the updated product
                return updatedProduct;
            }
            return null;
        });
        // Show the dialog and wait for the user's response
        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(updatedProduct -> {
            updateProductInDatabase(updatedProduct);  //pt admin
        });
        return resultFuture;
    }

    private boolean updateProductInDatabase(Product updatedProduct) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "UPDATE product p SET name = ?, brand = ?, description = ?, price = ?, stockQuantity = ? WHERE p.productId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, updatedProduct.getName());
            pst.setString(2, updatedProduct.getBrand());
            pst.setString(3, updatedProduct.getDescription());
            pst.setFloat(4, updatedProduct.getPrice());
            pst.setInt(5, updatedProduct.getStockQuantity());
            pst.setInt(6, updatedProduct.getProductId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully!");
                updateAdminNailCareProductListView();  //pt admin
                return true;
            } else {
                System.err.println("Failed to update product in the database.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean setProductNotVisibleInDB(Product product) {
        try(Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "UPDATE product SET isVisible = false WHERE productId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, product.getProductId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product's visibility updated successfully!");
                updateAdminNailCareProductListView();  //admin
                return true;
            } else {
                System.err.println("Failed to update product's visibility in the database.");
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Node createProductInfoNode(Product product) {
        Label nameLabel = new Label("Name: " + product.getName());
        Label brandLabel = new Label("Brand: " + product.getBrand());
        Label descriptionLabel = new Label("Description: " + product.getDescription());
        Label priceLabel = new Label("Price: $" + product.getPrice());
        Label stockQuantityLabel = new Label("Quantity: " + product.getStockQuantity());
        Label avgRatingLabel = new Label("Rating: " + product.getAvgRating());
        VBox productInfoBox = new VBox(nameLabel, brandLabel, descriptionLabel, priceLabel, stockQuantityLabel, avgRatingLabel);
        productInfoBox.setAlignment(Pos.CENTER_LEFT);
        return productInfoBox;
    }

    private void updateAdminNailCareProductListView() {
        if (productListView != null) {
            productListView.getItems().clear();
            productListView.getItems().addAll(DBUtils.getAllVisibleProductsFromBD(2));
        } else {
            System.err.println("productListView is null");
        }
    }
}
