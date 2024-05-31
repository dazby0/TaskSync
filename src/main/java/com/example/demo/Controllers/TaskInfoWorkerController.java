package com.example.demo.Controllers;

import com.example.demo.*;
import com.example.demo.Interfaces.UserAware;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Models.SharedService;
import com.example.demo.Models.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TaskInfoWorkerController implements Initializable, UserAware {
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
    private LocalDateTime startTime;
    private Task task;
    private Timeline timer;
    private boolean stopTimerRequested = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        task = SharedService.getInstance().getCurrentTask();
        if (task != null) {
            setTaskLabels(task);
            configureButtons(task.getStatus());
        }

        setupButtonActions();
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void setupButtonActions() {
        btn_back.setOnAction(event -> DBUtils.changeScene(event, "main-worker-view.fxml", "Welcome!", loggedUser));

        btn_startTask.setOnAction(event -> {
            if (task != null) {
                startTask(task);
            }
        });

        btn_finishTask.setOnAction(event -> {
            if (task != null) {
                finishTask(task);
            }
        });
    }

    private void setTaskLabels(Task task) {
        label_taskName.setText("Title: " + task.getTaskName());
        label_status.setText("Status: " + task.getStatus());
        label_assignedDate.setText("Assigned Date: " + task.getAssignedDate());
        label_assignedBy.setText("Assigned By: " + task.getAssignedBy());
        label_timeSpent.setText("Time Spent: " + task.getTimeSpent());
        label_finishDate.setText("Finish Date: " + task.getFinishTime());
    }

    private void configureButtons(String status) {
        switch (status) {
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
                label_finishDate.setVisible(false);
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

    private void startTask(Task task) {
        startTime = LocalDateTime.now();
        DBUtils.updateStatus(task, "In Progress", startTime);

        startTimer();

        btn_startTask.setVisible(false);
        btn_finishTask.setVisible(true);
        label_timeSpent.setVisible(true);
        label_status.setText("Status: In Progress");
    }

    private void finishTask(Task task) {
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

        stopTimerRequested = true;
        DBUtils.updateStatus(task, "Finished", timeSpent, finishDate);

        btn_finishTask.setVisible(false);
        label_status.setText("Status: Finished");
    }


    private void startTimer() {
        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateElapsedTime()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateElapsedTime() {
        if (startTime != null && !stopTimerRequested) {
            java.time.Duration duration = java.time.Duration.between(startTime, LocalDateTime.now());
            long seconds = duration.getSeconds();
            String timeSpent = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
            label_timeSpent.setText("Time Spent: " + timeSpent);
        }
    }
}