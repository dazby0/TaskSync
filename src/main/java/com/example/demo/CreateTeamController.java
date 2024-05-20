package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateTeamController implements Initializable {
    @FXML
    private Button btn_switchToTeamsView;
    @FXML
    private Button btn_createTeam;
    @FXML
    private Button btn_loadWorkers;

    @FXML
    private ComboBox<String> cb_workers;

    @FXML
    private Label label_addedWorkers;

    private ObservableList<String> availableUsers;
    private ObservableList<String> disabledUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        availableUsers = FXCollections.observableArrayList();
        disabledUsers = FXCollections.observableArrayList();

        cb_workers.setItems(availableUsers);
        cb_workers.setPromptText("Show Workers:");

        btn_switchToTeamsView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "teams-manager-view.fxml", "Your teams!", null, null);
            }
        });

        btn_loadWorkers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                availableUsers.setAll(DBUtils.loadWorkers());
                cb_workers.setItems(availableUsers);
            }
        });

        cb_workers.setOnAction(event -> {
            String selectedItem = cb_workers.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                label_addedWorkers.setText(selectedItem);
                availableUsers.remove(selectedItem);
                disabledUsers.add(selectedItem);
                cb_workers.getSelectionModel().clearSelection();
            }
        });

        label_addedWorkers.setOnMouseClicked(event -> {
            String selectedItemText = label_addedWorkers.getText();
            if (selectedItemText != null && !selectedItemText.isEmpty()) {
                availableUsers.add(selectedItemText);
                disabledUsers.remove(selectedItemText);
                label_addedWorkers.setText("");
            }
        });
    }
}
