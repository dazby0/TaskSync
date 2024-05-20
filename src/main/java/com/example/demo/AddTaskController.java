package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddTaskController implements Initializable {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private Button btn_loadWorkers;
    @FXML
    private Button btn_addTask;

    @FXML
    private ComboBox cb_assignedTo;

    @FXML
    private TextField tf_taskTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_switchToMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", null, null);
            }
        });

        btn_loadWorkers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cb_assignedTo.setItems(DBUtils.loadWorkers());
                cb_assignedTo.setPromptText("Show workers:");
            }
        });

        btn_addTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = tf_taskTitle.getText();
                String assignedTo = (String) cb_assignedTo.getValue();

                DBUtils.addTask(event, assignedTo, title);
            }
        });
    }

}
