package com.example.test2javafx;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_login;

    @FXML
    private Button button_sign_up;

    @FXML
    private ChoiceBox<String> choiceBoxUserType = new ChoiceBox<>();

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_email;

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_last_name;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_phone_number;

    @FXML
    private TextField tf_username;

    private String selectedUserType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_sign_up.setDefaultButton(true);
        choiceBoxUserType.getItems().add("admin");
        choiceBoxUserType.getItems().add("client");

        choiceBoxUserType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedUserType = choiceBoxUserType.getSelectionModel().getSelectedItem();
            }
        });

        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "log-in.fxml", "Log in", null);
            }
        });

        button_sign_up.setDefaultButton(true);
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.signUpUser(event, tf_first_name.getText(), tf_last_name.getText(), tf_username.getText(), tf_email.getText(), tf_password.getText(), tf_address.getText(), tf_phone_number.getText(), selectedUserType);
            }

        });
    }
}
