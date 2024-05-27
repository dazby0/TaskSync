package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskInfoController implements Initializable, UserAware {
    @FXML
    private Label label_taskName;
    @FXML
    private Label label_status;
    @FXML
    private Label label_startDate;
    @FXML
    private Label label_assignedBy;
    @FXML
    private Button btn_back;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            label_taskName.setText(task.getTaskName());
            label_status.setText(task.getStatus());
            label_startDate.setText(task.getStartDate());
            label_assignedBy.setText(task.getAssignedBy());
        }

        btn_back.setOnAction(this::handleBackButton);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void handleBackButton(ActionEvent event) {
        DBUtils.changeScene(event, "main-worker-view.fxml", "Welcome!", loggedUser);
    }
}
