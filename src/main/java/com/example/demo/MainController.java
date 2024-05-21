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
    private Button btn_switchToTeams;
    @FXML
    private Button btn_switchToReports;

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
                DBUtils.changeScene(event, "add-task-view.fxml", "Add new Task!", null, null);
            }
        });

        btn_switchToTeams.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "teams-manager-view.fxml", "Your teams!", null, null);
            }
        });

        btn_switchToReports.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "reports-manager-view.fxml", "View Reports!", null, null);
            }
        });
    }

    public void setUserInformation(String username) {
        label_welcome.setText("Hello " + username + "!");
    }

//    public String getUsername( ) {
//        String workingString = label_welcome.getText();
//        String[] parts = workingString.split(" ");
//        return parts[2];
//    }
}
