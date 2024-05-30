package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TaskInfoController implements Initializable, UserAware {
    @FXML
    private Label label_taskName;
    @FXML
    private Label label_status;
    @FXML
    private Label label_assignedDate;
    @FXML
    private Label label_assignedBy;
    @FXML
    private Label label_timeSpent;
    @FXML
    private Label label_finishDate;
    @FXML
    private Button btn_back;
    @FXML
    private Button btn_startTask;
    @FXML
    private Button btn_finishTask;

    private LoggedUser loggedUser;
    private Timeline timer;
    private LocalDateTime startTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            label_taskName.setText("Title: " + task.getTaskName());
            label_status.setText("Status: " + task.getStatus());
            label_assignedDate.setText("Assigned Date: " + task.getAssignedDate());
            label_assignedBy.setText("Assigned By: " + task.getAssignedBy());
            label_timeSpent.setText("Time Spent: " + task.getTimeSpent());
            label_finishDate.setText("Finish Date: "+ task.getFinishTime());

            switch (task.getStatus()) {
                case "New":
                    btn_startTask.setVisible(true);
                    btn_finishTask.setVisible(false);
                    label_timeSpent.setVisible(false);
                    label_finishDate.setVisible(false);
                    break;
                case "In Progress":
                    btn_startTask.setVisible(false);
                    btn_finishTask.setVisible(true);
                    label_timeSpent.setVisible(true);
                    startTime = DBUtils.getTaskStartTime(task);
                    if (startTime != null) {
                        startTimer();
                    }
                    break;
                case "Finished":
                    btn_startTask.setVisible(false);
                    btn_finishTask.setVisible(false);
                    label_timeSpent.setVisible(true);
                    label_finishDate.setVisible(true);
                    break;
            }
        }

        btn_back.setOnAction(this::handleBackButton);
        btn_startTask.setOnAction(this::handleStartTaskButton);
        btn_finishTask.setOnAction(this::handleFinishTaskButton);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void handleBackButton(ActionEvent event) {
        DBUtils.changeScene(event, "main-worker-view.fxml", "Welcome!", loggedUser);
    }

    private void handleStartTaskButton(ActionEvent event) {
        Task task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            startTime = LocalDateTime.now();
            DBUtils.updateStatus(task, "In Progress", startTime);

            startTimer();

            btn_startTask.setVisible(false);
            btn_finishTask.setVisible(true);
            label_timeSpent.setVisible(true);
            label_status.setText("Status: In Progress");
        }
    }

    private void handleFinishTaskButton(ActionEvent event) {
        Task task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            stopTimer();

            LocalDateTime finishTime = LocalDateTime.now();
            java.time.Duration duration = java.time.Duration.between(startTime, finishTime);
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            long seconds = duration.getSeconds() % 60;

            String timeSpent = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            String finishDate = finishTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            label_timeSpent.setText("Time Spent: " + timeSpent);
            label_finishDate.setText("Finish Date: " + finishDate);
            label_timeSpent.setVisible(true);
            label_finishDate.setVisible(true);

            DBUtils.updateStatus(task, "Finished", timeSpent, finishDate);

            btn_finishTask.setVisible(false);
            label_status.setText("Status: Finished");
        }
    }

    private void startTimer() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            java.time.Duration duration = java.time.Duration.between(startTime, LocalDateTime.now());
            long seconds = duration.getSeconds();
            String timeSpent = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
            label_timeSpent.setText("Time Spent: " + timeSpent);
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}