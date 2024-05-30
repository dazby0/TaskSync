package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;

import java.util.ResourceBundle;

public class TaskInfoManagerController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private Label label_taskTitle;
    @FXML
    private Label label_status;
    @FXML
    private Label label_assignedTo;
    @FXML
    private Label label_assignedDate;
    @FXML
    private Label label_timeSpentHeader;
    @FXML
    private Label label_timeSpent;
    @FXML
    private Label label_finishDateHeader;
    @FXML
    private Label label_finishDate;

    private LoggedUser loggedUser;
    private Task task;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            setupTaskLabels(task);
            configureLabels(task.getStatus());
        }
        setupButtonActions();
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void setupButtonActions() {
        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Track Your Workers Tasks!", loggedUser));
    }

    private void setupTaskLabels(Task task) {
        label_taskTitle.setText(task.getTaskName());
        label_status.setText(task.getStatus());
        label_assignedTo.setText(task.getAssignedTo());
        label_assignedDate.setText(task.getAssignedDate());
    }

    private void configureLabels(String status) {
        switch (status) {
            case "In Progress":
            case "New":
                label_timeSpentHeader.setVisible(false);
                label_timeSpent.setVisible(false);
                label_finishDateHeader.setVisible(false);
                label_finishDate.setVisible(false);
                break;
            default:
                label_timeSpent.setText(task.getTimeSpent());
                label_finishDate.setText(task.getFinishTime());
                label_timeSpentHeader.setVisible(true);
                label_timeSpent.setVisible(true);
                label_finishDateHeader.setVisible(true);
                label_finishDate.setVisible(true);
                break;
        }
    }
}