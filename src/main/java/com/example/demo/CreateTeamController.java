package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateTeamController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToTeamsView;
    @FXML
    private Button btn_createTeam;
    @FXML
    private Button btn_addWorkerToTeam;
    @FXML
    private Button btn_clearWorkers;
    @FXML
    private ComboBox<String> cb_workers;
    @FXML
    private Label label_addedWorkers;
    @FXML
    private TextField tf_teamName;

    private LoggedUser loggedUser;

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_workers.setItems(DBUtils.loadWorkers());
        cb_workers.setPromptText("Show Workers:");

        btn_switchToTeamsView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "teams-manager-view.fxml", "Your teams!", loggedUser);
            }
        });

        btn_addWorkerToTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = cb_workers.getValue();
                String labelText = label_addedWorkers.getText();
                if (labelText.isEmpty()) {
                    label_addedWorkers.setText(username);
                } else {
                    label_addedWorkers.setText(labelText + ", " + username);
                }
                cb_workers.getItems().remove(username);
            }
        });

        btn_clearWorkers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cb_workers.setItems(DBUtils.loadWorkers());
                label_addedWorkers.setText("");
            }
        });

        btn_createTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (loggedUser != null) {
                    String teamName = tf_teamName.getText();
                    String workers = label_addedWorkers.getText();
                    DBUtils.createTeam(event, loggedUser, teamName, workers);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("User is not logged in!");
                    alert.show();
                }
            }
        });
    }
}
