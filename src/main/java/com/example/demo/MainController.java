package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_addTask;

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

        btn_addTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "addTask-view.fxml", "Add new Task!", null, null);
            }
        });
    }

    public void setUserInformation(String username) {
        label_welcome.setText("Hello " + username + "!");
    }
}
