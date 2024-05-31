package com.example.demo.Controllers;

import com.example.demo.DBUtils;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Interfaces.UserAware;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class AddTaskController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private Button btn_addTask;
    @FXML
    private ComboBox<String> cb_assignedTo;
    @FXML
    private TextField tf_taskTitle;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_assignedTo.setItems(DBUtils.loadWorkers());
        cb_assignedTo.setPromptText("Select worker:");

        setupButtonActions();
    }

    private void setupButtonActions() {
        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", loggedUser));
        btn_addTask.setOnAction(this::handleAddTask);
    }

    private void handleAddTask(ActionEvent event) {
        String title = tf_taskTitle.getText();
        String assignedTo = cb_assignedTo.getValue();
        DBUtils.addTask(event, assignedTo, title, loggedUser);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }
}