package com.example.test2javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private Button buttonSaveChanges;

    @FXML
    private TextField textNewPassword;

    @FXML
    private TextField textOldPassword;

    @FXML
    void handleSaveChanges(ActionEvent event) {
        String oldPassword = textOldPassword.getText();
        String newPassword = textNewPassword.getText();

        if (newPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("New password cannot be empty. Please enter a new password.");
            alert.show();
            return; // Stop execution if the new password is empty
        }

        if (oldPassword.equals(newPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("New password must be different from the old password.");
            alert.show();
            return; // Stop execution if the old and new passwords are the same
        }

        boolean passwordChanged = updatePasswordInDatabase(oldPassword, newPassword);
        if(passwordChanged){
            Stage stage = (Stage) buttonSaveChanges.getScene().getWindow();
            stage.close();
            DBUtils.changeScene(event, "log-in.fxml", "Login", null);
            closeOtherWindowsButLogin(event);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to change password. Please check your old password.");
            alert.show();
        }
    }

    private boolean updatePasswordInDatabase(String oldPassword, String newPassword) {
        return DBUtils.updatePasswordInDatabase(oldPassword, newPassword);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonSaveChanges.setDefaultButton(true);
    }

    private void closeOtherWindowsButLogin(ActionEvent event) {
        Window currentWindow = ((Button) event.getSource()).getScene().getWindow();
        for (Window window : Window.getWindows()) {
            if (window != currentWindow && !isLoginWindow(window)) {
                window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
    }

    private boolean isLoginWindow(Window window) {
        return window instanceof Stage && "Login".equals(((Stage) window).getTitle());
    }

}
