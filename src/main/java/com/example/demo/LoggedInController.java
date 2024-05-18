package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button btn_logout;

    @FXML
    private Label label_welcome;

    @FXML
    private Label label_role;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login-view.fxml", "Log In to TaskSync!", null, null);
            }
        });
    }

    public void setUserInformation(String username, String role) {
        label_welcome.setText("Welcome " + username);
        label_role.setText("Your role is:");
    }
}
