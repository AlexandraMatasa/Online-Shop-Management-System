package com.example.test2javafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShopController implements Initializable {
    private static Stage clientStage;
    private static Stage shopStage;

    @FXML
    private Button buttonOrderHistory;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button buttonDeleteAccount;

    @FXML
    private Button buttonPlaceOrder;

    @FXML
    private Button button_bodyCare;

    @FXML
    private Button button_hairCare;

    @FXML
    private Button button_makeUp;

    @FXML
    private Button button_menPerfumes;

    @FXML
    private Button button_nailCare;

    @FXML
    private Button button_womenPerfumes;

    @FXML
    private Button button_logout;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Tab tab_cart;

    @FXML
    private Tab tab_details;

    @FXML
    private Tab tab_products;

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    private Label getAddressLabel;

    @FXML
    private Label getEmailLabel;

    @FXML
    private Label getFirstNameLabel;

    @FXML
    private Label getPNumberLabel;

    @FXML
    private Label getUsernameLabel;

    @FXML
    private Label getlastNameLabel;

    @FXML
    private Button buttonChangePassword;

    @FXML
    private ImageView editUsernameIcon;

    @FXML
    private ImageView editFirstNameIcon;

    @FXML
    private ImageView editLastNameIcon;

    @FXML
    private ImageView editEmailIcon;

    @FXML
    private ImageView editAddressIcon;

    @FXML
    private ImageView editPhoneNumberIcon;

    @FXML
    private ImageView emptyCartImage;

    @FXML
    void handleButtonOrderHistory(ActionEvent event) {
        Node source = (Node) event.getSource();
        // Get the Stage from the source Node
        Stage stageOrder = (Stage) source.getScene().getWindow();
        // Close the stage
        stageOrder.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orderHistory.fxml"));
            Parent root = loader.load();

            // Set up the controller
            OrderHistoryController orderHistoryController = loader.getController();

            // Directly populate the order history ListView
            User currentUser = DBUtils.getCurrentUser();
            ObservableList<OrderDetails> orderHistory = FXCollections.observableArrayList();

            try {
                orderHistory.addAll(DBUtils.getOrderHistory(currentUser));
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }

            orderHistoryController.setOrderHistoryItems(orderHistory);

            Stage stage = new Stage();
            stage.setTitle("Order History");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonPlaceOrderOnAction(ActionEvent event) {
        System.out.println("\n\nYou clicked on Place Order button\n");

        // Get the current user and cart items
        User currentUser = DBUtils.getCurrentUser();
        ArrayList<CartItem> cartItems = new ArrayList<>(DBUtils.getCartItems(currentUser));

        // Check if the cart is not empty
        if (cartItems.isEmpty()) {
            showWarning_dialog("Empty Cart", "Your cart is empty. Add products to your cart before placing an order.");
            return;
        }

        // Get the source Node from the event
        Node source = (Node) event.getSource();
        // Get the Stage from the source Node
        Stage stageSop = (Stage) source.getScene().getWindow();
        // Close the stage
        stageSop.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order.fxml"));
            Parent root = loader.load();

            // Set up the controller and pass necessary data (if needed)
            OrderController orderController = loader.getController();
            orderController.initializeData(cartListView.getItems());

            Stage stage = new Stage();
            stage.setTitle("Order");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showWarning_dialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleChangePassword(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("changePassword.fxml"));
        Parent root;
        try {
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(root, 300, 230));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleButtonBodyCareOnAction(ActionEvent event) {
        System.out.println("You clicked on body care button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("bodyCare");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }


    @FXML
    void handleButtonMakeUpOnAction(ActionEvent event) {
        System.out.println("You clicked on make up button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("makeup");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    @FXML
    void handleButtonNailCareOnAction(ActionEvent event) {
        System.out.println("You clicked on nail care button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("nailCare");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    @FXML
    void handleButtonWomenPerfumeryOnAction(ActionEvent event) {
        System.out.println("You clicked on women's perfumery button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("womenPerfumery");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    @FXML
    void handleButtonMenPerfumeryOnAction(ActionEvent event) {
        System.out.println("You clicked on men's perfumery button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("menPerfumery");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    @FXML
    void handleButtonHairCareOnAction(ActionEvent event) {
        System.out.println("You clicked on hair care button!");
        ShopFxmlLoader loader = new ShopFxmlLoader();
        Pane view = loader.getPage("hairCare");

        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    private ImageView addGraphic(String imageFile) {
        ImageView imageView = null;
        Image image = new Image(getClass().getResourceAsStream(imageFile));
        imageView = new ImageView(image);
        imageView.setFitHeight(22);
        imageView.setFitWidth(22);
        return imageView;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shopStage = new Stage();

        // Save the stage reference when the window is created
        clientStage = new Stage();
        clientStage.setTitle("Admin Window");
        clientStage.setOnCloseRequest(event -> clientStage = null);

        ImageView viewProducts = addGraphic("cosmetic.png");
        tab_products.setGraphic(viewProducts);
        ImageView viewCart = addGraphic("shopping-cart.png");
        tab_cart.setGraphic(viewCart);
        ImageView viewDetails = addGraphic("people.png");
        tab_details.setGraphic(viewDetails);


        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logOutUser();
                DBUtils.changeScene(event, "log-in.fxml", "Login", null);
            }
        });

        if(DBUtils.getCurrentUser() != null){
            User currentUser = DBUtils.getCurrentUser();
            getUsernameLabel.setText(currentUser.getUsername());
            getFirstNameLabel.setText(currentUser.getFirstName());
            getlastNameLabel.setText(currentUser.getLastName());
            getEmailLabel.setText(currentUser.getEmail());
            getAddressLabel.setText(currentUser.getAddress());
            getPNumberLabel.setText(currentUser.getPhoneNumber());
        }

        if (cartListView != null) {
            ///adaugam spinner ul
            cartListView.setCellFactory(param -> new SpinnerListCell());
            cartListView.getItems().addAll(DBUtils.getCartItems(DBUtils.getCurrentUser()));
            cartListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        } else {
            System.err.println("productListView is null");
        }


        tab_cart.setOnSelectionChanged(event -> {
            if (tab_cart.isSelected()) {
                updateCartListView();
                checkProductVisibility();
                deleteCartItemsWithNotVisibleProducts();
                updateCartListView();
            }
        });


        buttonDeleteAccount.setOnAction(event -> {
            showDeleteAccountDialog(event);
        });

        // Set edit icons
        setEditIcon(editUsernameIcon, getUsernameLabel);
        setEditIcon(editFirstNameIcon, getFirstNameLabel);
        setEditIcon(editLastNameIcon, getlastNameLabel);
        setEditIcon(editEmailIcon, getEmailLabel);
        setEditIcon(editAddressIcon, getAddressLabel);
        setEditIcon(editPhoneNumberIcon, getPNumberLabel);

    }

    private void deleteCartItemsWithNotVisibleProducts() {
        List<CartItem> cartItems = DBUtils.getCartItems(DBUtils.getCurrentUser());
        for(CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if(!product.getVisible() || (product.getVisible() && product.getStockQuantity() <= 0)) {
                deleteProductFromCartItem(product);
            }
        }
    }

    private void updateCartListView() {
        //cartListView.getItems().setAll(DBUtils.getCartItems(DBUtils.getCurrentUser()));
        ObservableList<CartItem> cartItemsList = FXCollections.observableArrayList(DBUtils.getCartItems(DBUtils.getCurrentUser()));
        cartListView.setItems(cartItemsList);

        // Show/hide empty cart image based on whether the cart is empty
        if (cartItemsList.isEmpty()) {
            // Display the empty cart image
            Image image = new Image(getClass().getResourceAsStream("/com/example/test2javafx/emptyCart.jpeg"));
            emptyCartImage.setImage(image);
            emptyCartImage.setVisible(true);
        } else {
            // Hide the empty cart image
            emptyCartImage.setVisible(false);
        }
    }

    private void checkProductVisibility() {
        List<CartItem> cartItems = DBUtils.getCartItems(DBUtils.getCurrentUser());
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (!product.getVisible()) {
                showProductNotAvailableMessage(product);
            }
            if(product.getVisible() && product.getStockQuantity() <= 0) {  //nu mai e in stock
                showProductNotInStockMessage(product);
            }
        }
    }

    private void showProductNotInStockMessage(Product product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Out of stock");
        alert.setHeaderText(null);
        alert.setContentText("We are sorry, the product '" + product.getName() +
                "' is currently out of stock.");
        alert.showAndWait();
    }

    private void showProductNotAvailableMessage(Product product) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product Not Available");
        alert.setHeaderText(null);
        alert.setContentText("We are sorry, the product '" + product.getName() +
                "' was deleted by an administrator and is no longer available.");
        alert.showAndWait();
    }

    private void deleteProductFromCartItem(Product product) {
        try(Connection conn = MySQLJDBCUtil.getConnection()){
            String sql = "DELETE FROM cartItem WHERE productId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, product.getProductId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product deleted from cartItem successfully for all users!");
            } else {
                System.out.println("Product not found in cartItem.");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void setEditIcon(ImageView icon, Label label) {
        Image editImage = new Image(getClass().getResourceAsStream("edit_icon.png"));
        icon.setImage(editImage);
        icon.setFitHeight(16);
        icon.setFitWidth(16);
        icon.setOnMouseClicked(event -> showEditDialog(label));
    }

    private void showEditDialog(Label label) {
        TextInputDialog dialog = new TextInputDialog(label.getText());
        dialog.setTitle("Edit Information");
        switch (label.getId()) {
            case "getUsernameLabel":
                dialog.setHeaderText("Edit username");
                dialog.setContentText("New username:");
                break;
            case "getFirstNameLabel":
                dialog.setHeaderText("Edit first name");
                dialog.setContentText("New first name:");
                break;
            case "getlastNameLabel":
                dialog.setHeaderText("Edit last name");
                dialog.setContentText("New last name:");
                break;
            case "getEmailLabel":
                dialog.setHeaderText("Edit email");
                dialog.setContentText("New email:");
                break;
            case "getAddressLabel":
                dialog.setHeaderText("Edit address");
                dialog.setContentText("New address:");
                break;
            case "getPNumberLabel":
                dialog.setHeaderText("Edit phone number");
                dialog.setContentText("New phone number:");
                break;
            default:
                break;
        }

        // Show the dialog and wait for the user's response
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newText -> {
            // Validate input: Check if the new text is not empty
            if (newText.trim().isEmpty()) {
                showWarningDialog("Error", "Input cannot be empty.");
                return;
            }

            // Validate input: Check if the new text is different from the existing information
            User currentUser = DBUtils.getCurrentUser();

            if (currentUser != null) {
                String existingValue = DBUtils.getExistingValue(label.getId());

                if (existingValue.equals(newText)) {
                    showWarningDialog("Error", "New input must be different from the existing information.");
                    return;
                }
                // Update the label with the new text
                label.setText(newText);

                switch (label.getId()) {
                    case "getUsernameLabel":
                        DBUtils.updateUserInDatabase("username", newText);
                        break;
                    case "getFirstNameLabel":
                        DBUtils.updateUserInDatabase("firstName", newText);
                        break;
                    case "getlastNameLabel":
                        DBUtils.updateUserInDatabase("lastName", newText);
                        break;
                    case "getEmailLabel":
                        DBUtils.updateUserInDatabase("email", newText);
                        break;
                    case "getAddressLabel":
                        DBUtils.updateUserInDatabase("address", newText);
                        break;
                    case "getPNumberLabel":
                        DBUtils.updateUserInDatabase("phoneNumber", newText);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showWarningDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Stage getClientStage() {
        return clientStage;
    }

    private void removeSelectedUsers(ObservableList<User> selectedUsers) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql2 = "DELETE FROM cartItem c WHERE c.userId = ?";
            PreparedStatement pst2 = conn.prepareStatement(sql2);
            for (User user : selectedUsers) {
                pst2.setInt(1, user.getUserId());
                int rowsAffected = pst2.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Cart items deleted successfully!");
                } else {
                    System.out.println("Cannot delete the cartItems");
                }
            }

            String sql = "DELETE FROM user u WHERE u.userId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            for (User user : selectedUsers) {
                pst.setInt(1, user.getUserId());
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User " + user.getUsername() + " deleted successfully!");
                } else {
                    System.out.println("Cannot delete the user " + user.getUsername());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error deleting users", "An error occurred while deleting users.");
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showDeleteAccountDialog(ActionEvent event) {
        // Create the custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete Account");
        dialog.setHeaderText("Are you sure you want to delete your account?");

        // Set the button types
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(deleteButtonType, ButtonType.CANCEL);

        // Create and set the content of the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(passwordField, 1, 0);

        // Enable/Disable delete button based on whether password is entered
        Node deleteButton = dialog.getDialogPane().lookupButton(deleteButtonType);
        deleteButton.setDisable(true);

        // Listen for changes in text field
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            deleteButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the password field by default
        Platform.runLater(() -> passwordField.requestFocus());

        // Convert the result to a password when the delete button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == deleteButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(password -> {
            // Check the password and proceed with deletion
            if (validatePassword(password)) {
                ObservableList<User> user = FXCollections.observableArrayList(DBUtils.getCurrentUser());
                removeSelectedUsers(user);
                DBUtils.logOutUser();
                DBUtils.changeScene(event, "log-in.fxml", "Login", null);
                System.out.println("Account deleted successfully!");
            } else {
                // Show an error message for invalid password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Password");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password. Please try again.");
                alert.showAndWait();
            }
        });
    }

    private boolean validatePassword(String password) {   //not sure if I need this :)
        // Implement your password validation logic here
        // For example, check if the provided password matches the user's actual password
        return true;
    }

    public static Stage getShopStage() {
        return shopStage;
    }

    public void switchToCartTab() {
        tabPane.getSelectionModel().select(tab_cart);
    }

    public void switchToAccountTab() {
        tabPane.getSelectionModel().select(tab_details);
    }

    public void switchToProductsTab() {
        tabPane.getSelectionModel().select(tab_products);
    }

}
