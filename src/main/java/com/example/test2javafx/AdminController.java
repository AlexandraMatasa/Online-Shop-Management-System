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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    private static Stage adminStage;

    @FXML
    private Button button_add_user;

    @FXML
    private Button button_remove_user;

    @FXML
    private Button buttonDeleteAccount;

    @FXML
    private Button button_bodyCare;

    @FXML
    private Button button_hairCare;

    @FXML
    private Button button_makeUp;

    @FXML
    private Button button_menPerfume;

    @FXML
    private Button button_nailCare;

    @FXML
    private Button button_womenPerfume;

    @FXML
    private Button button_logout;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Tab tab_users;

    @FXML
    private Tab tab_details;

    @FXML
    private Tab tab_products;

    @FXML
    private ListView<User> usersListView;

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
    private Button buttonSwitchToClientAccount;

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
    private ImageView editProduct;

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
        // Get the list of products in the makeup category
        List<Product> bodyProducts = DBUtils.getProductsByCategory(1);
        if(bodyProducts == null){
            System.out.println("Null list of products!\n");
        }

        // Check each product for a stock quantity of 0
        for (Product product : bodyProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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
        // Get the list of products in the makeup category
        List<Product> makeupProducts = DBUtils.getProductsByCategory(3);
        if(makeupProducts == null){
            System.out.println("Null list of products!\n");
        }

        // Check each product for a stock quantity of 0
        for (Product product : makeupProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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

        List<Product> nailProducts = DBUtils.getProductsByCategory(2);
        if(nailProducts == null){
            System.out.println("Null list of products!\n");
        }

        for (Product product : nailProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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

        List<Product> womenParfumeryProducts = DBUtils.getProductsByCategory(4);
        if(womenParfumeryProducts == null){
            System.out.println("Null list of products!\n");
        }

        // Check each product for a stock quantity of 0
        for (Product product : womenParfumeryProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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

        List<Product> menPerfumeryProducts = DBUtils.getProductsByCategory(5);
        if(menPerfumeryProducts == null){
            System.out.println("Null list of products!\n");
        }

        for (Product product : menPerfumeryProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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

        List<Product> hairProducts = DBUtils.getProductsByCategory(6);
        if(hairProducts == null){
            System.out.println("Null list of products!\n");
        }

        for (Product product : hairProducts) {
            if (product.getStockQuantity() == 0) {
                // Show an alert for each product with stock quantity 0
                showRestockAlertForProduct(product);
            }
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
        // Save the stage reference when the window is created
        adminStage = new Stage();
        adminStage.setTitle("Admin Window");
        adminStage.setOnCloseRequest(event -> adminStage = null);

        ImageView viewProducts = addGraphic("cosmetic.png");
        tab_products.setGraphic(viewProducts);
        ImageView viewUsers = addGraphic("users.png");
        tab_users.setGraphic(viewUsers);
        ImageView viewDetails = addGraphic("people.png");
        tab_details.setGraphic(viewDetails);

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logOutUser();
                DBUtils.changeScene(event, "log-in.fxml", "Login", null);
            }
        });

        if (DBUtils.getCurrentUser() != null) {
            User currentUser = DBUtils.getCurrentUser();
            getUsernameLabel.setText(currentUser.getUsername());
            getFirstNameLabel.setText(currentUser.getFirstName());
            getlastNameLabel.setText(currentUser.getLastName());
            getEmailLabel.setText(currentUser.getEmail());
            getAddressLabel.setText(currentUser.getAddress());
            getPNumberLabel.setText(currentUser.getPhoneNumber());
        }

        if (usersListView != null) {
            usersListView.getItems().addAll(DBUtils.getUsers());
            usersListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        } else {
            System.err.println("usersListView is null");
        }

        button_add_user.setOnAction(event -> {
            showAddUserDialog();
        });

        button_remove_user.setOnAction(event -> {
            ObservableList<User> selectedUsers = usersListView.getSelectionModel().getSelectedItems();

            if (!selectedUsers.isEmpty()) {
                if (isCurrentUserSelected(selectedUsers)) {
                    // Display an error message if the admin is trying to delete their own account
                    showErrorAlert("Error", "Cannot delete your own account.");
                } else {
                    // Display confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Remove Users");
                    alert.setContentText("Are you sure you want to remove the selected user(s)?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Remove selected users from the list and update the database
                        removeSelectedUsers(selectedUsers);
                        // Update the usersListView
                        updateUsersListView();
                    }
                }
            } else {
                // Inform the user that no user is selected
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select user(s) to remove.");
                alert.showAndWait();
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

        buttonSwitchToClientAccount.setOnAction(event -> {
            // Create a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Switch to Client Account");
            alert.setContentText("Are you sure you want to permanently switch to your Client account? This action cannot be undone.");

            // Customize the buttons in the dialog
            ButtonType switchButtonType = new ButtonType("Switch");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(switchButtonType, cancelButtonType);

            // Show the confirmation dialog and wait for the user's response
            Optional<ButtonType> result = alert.showAndWait();

            // Perform the action if the user confirms
            if (result.isPresent() && result.get() == switchButtonType) {
                DBUtils.updateUserInDatabase("userType", "client");
                DBUtils.logOutUser();
                DBUtils.changeScene(event, "log-in.fxml", "Login", null);
            }
        });

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

    private boolean isCurrentUserSelected(ObservableList<User> selectedUsers) {
        User currentUser = DBUtils.getCurrentUser();
        return selectedUsers.contains(currentUser);
    }

    public static Stage getAdminStage() {
        return adminStage;
    }

    private void removeSelectedUsers(ObservableList<User> selectedUsers) {
        for(User user : selectedUsers) {
            deleteAllCartItemsOfUser(user);
            deleteAllOrderItemsOfUser(user);
            deleteAllOrdersOfUser(user);
            deleteUser(user);
        }
    }

    private void deleteAllCartItemsOfUser(User user) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "DELETE FROM cartItem c WHERE c.userId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, user.getUserId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart items of " + user.getUsername() + " deleted successfully!");
            } else {
                System.out.println("Cannot delete " + user.getUsername() + "'s cart items");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllOrderItemsOfUser(User user) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "DELETE FROM orderitem WHERE userId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, user.getUserId());
            int rowsAffected = pst.executeUpdate();   //eroare -> dupa ce se sterg toate produsele din comanda, total amount pt comanda devine zero
            if (rowsAffected > 0) {                   //am modificat in bd total amount din order sa nu mai fie "not null" si acuma i ok :)
                System.out.println("Order items of " + user.getUsername() + " deleted successfully!");
            } else {
                System.out.println("Cannot delete " + user.getUsername() + "'s order items");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllOrdersOfUser(User user) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            conn.createStatement().executeUpdate("SET foreign_key_checks = 0");
            String sql = "DELETE FROM `order` WHERE userId = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, user.getUserId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Order(s) of " + user.getUsername() + " deleted successfully!");
            } else {
                System.out.println("Cannot delete " + user.getUsername() + "'s order(s)");
            }
            conn.createStatement().executeUpdate("SET foreign_key_checks = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(User user) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            // Disable foreign key checks temporarily
            conn.createStatement().executeUpdate("SET foreign_key_checks = 0");

            // Delete the user from the user table
            String sql = "DELETE FROM user WHERE userId = ?";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setInt(1, user.getUserId());
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User " + user.getUsername() + " deleted successfully!");
                } else {
                    System.out.println("Cannot delete the user " + user.getUsername());
                }
            }

            // Enable foreign key checks again
            conn.createStatement().executeUpdate("SET foreign_key_checks = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void showAddUserDialog() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Add New User");

        // Set the button types (OK and Cancel)
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the GridPane for the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        // Create form controls
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        TextField username = new TextField();
        TextField email = new TextField();
        PasswordField password = new PasswordField();
        TextField address = new TextField();
        TextField phoneNumber = new TextField();
        ChoiceBox<String> userType = new ChoiceBox<>();
        userType.getItems().addAll("admin", "client");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstName, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastName, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(username, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(email, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(password, 1, 4);
        grid.add(new Label("Address:"), 0, 5);
        grid.add(address, 1, 5);
        grid.add(new Label("Phone Number:"), 0, 6);
        grid.add(phoneNumber, 1, 6);
        grid.add(new Label("User Type:"), 0, 7);
        grid.add(userType, 1, 7);

        // Enable/Disable add button based on whether required fields are entered
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        List<TextField> requiredFields = Arrays.asList(firstName, lastName, email, password, address, phoneNumber);

        for (TextField field : requiredFields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                addButtonNode.setDisable(requiredFields.stream().anyMatch(tf -> tf.getText().trim().isEmpty()));
            });
        }

        // Show the dialog and wait for the user's response
        dialog.getDialogPane().setContent(grid);

        //     Convert the result to a User object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new User(0, firstName.getText(), lastName.getText(), email.getText(), password.getText(),
                        address.getText(), phoneNumber.getText(), username.getText(), User.setRoleByUserType(userType.getValue()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(user -> {
            // Perform the action based on the user input
            if (user != null) {
                // Call a method to add the user to the database using the User object
                addUserToDatabase(user);
            }
        });
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

    private boolean validatePassword(String password) {   //not sure if I need this
        // Implement your password validation logic here
        // For example, check if the provided password matches the user's actual password
        return true;
    }


    private void addUserToDatabase(User user) {
        try (Connection conn = MySQLJDBCUtil.getConnection()) {
            String sql = "INSERT INTO user (firstName, lastName, email, password, address, phoneNumber, userType, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getAddress());
            pst.setString(6, user.getPhoneNumber());
            pst.setString(7, user.getUserTypeByRole(user.getRole()));
            pst.setString(8, user.getUsername());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User added to the database: " + user.getUsername());

                // If you have an auto-incremented primary key (userId), retrieve it
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int userId = generatedKeys.getInt(1);
                        user.setUserId(userId);
                        System.out.println("Generated userId: " + userId);
                        updateUsersListView();
                    } else {
                        System.err.println("Failed to retrieve userId.");
                    }
                }
            } else {
                System.err.println("Failed to add user to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUsersListView() {
        if (usersListView != null) {
            usersListView.getItems().clear();
            usersListView.getItems().addAll(DBUtils.getUsers());
        } else {
            System.err.println("usersListView is null");
        }
    }

    private void showRestockAlertForProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Restock Required");
        alert.setHeaderText(null);
        alert.setContentText("Product '" + product.getName() + "' is out of stock. Please restock before proceeding.");
        alert.showAndWait();
    }
}


