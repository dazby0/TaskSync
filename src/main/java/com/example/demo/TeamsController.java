package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamsController implements Initializable {
    @FXML
    private Button btn_createTeam;
    @FXML
    private Button btn_switchToMain;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_createTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "create-team-view.fxml", "Create New Team!", null, null);
            }
        });

        btn_switchToMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", null, null);
            }
        });
    }
}
