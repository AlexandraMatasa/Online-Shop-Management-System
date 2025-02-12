package com.example.test2javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountCreatedController implements Initializable {

    @FXML
    private Button button_to_login;

    @FXML
    private Label lable_welcome;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_to_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "log-in.fxml", "Log in", null);
            }
        });
    }
}
