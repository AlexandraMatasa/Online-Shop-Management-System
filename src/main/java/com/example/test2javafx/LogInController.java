package com.example.test2javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable{
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the login button as the default button, so it responds to Enter key
        button_login.setDefaultButton(true);
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                User user = DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());
                if(user != null) {
                    switch (user.getRole()) {
                        case ADMIN:
                            DBUtils.changeScene(event, "admin.fxml", "Admin Dashboard", user.getUsername());
                            break;
                        case CLIENT:
                            DBUtils.changeScene(event, "shop.fxml", "Shop", user.getUsername());
                            break;
                        default:
                            System.out.println("Unknown user type");
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Unknown user type. Contact support.");
                            alert.show();
                            break;
                    }
                } else {
                    System.out.println("Invalid username or password");
                }
            }
        });

        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "Sign up", null);
            }
        });
    }
}
