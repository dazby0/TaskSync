package com.example.demo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.demo.DBUtils.loadWorkers;

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

        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", loggedUser));

        btn_addTask.setOnAction(event -> {
            String title = tf_taskTitle.getText();
            String assignedTo = cb_assignedTo.getValue();
            if (loggedUser != null) {
                System.out.println("Adding task for user: " + loggedUser.getUsername()); // Debug statement
                DBUtils.addTask(event, assignedTo, title, loggedUser);
            } else {
                System.out.println("LoggedUser is null in AddTaskController!"); // Debug statement
            }
        });
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        System.out.println("LoggedUser set in AddTaskController: " + loggedUser.getUsername()); // Debug statement
        this.loggedUser = loggedUser;
    }
}