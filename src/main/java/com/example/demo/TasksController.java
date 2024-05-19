package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

import java.sql.SQLException;

public class TasksController {
    @FXML
    private FlowPane tasksContainer;

    private int currentPage = 1;
    private static final int PAGE_SIZE = 6;

    @FXML
    private void initialize() {
        displayTasks();
    }

    @FXML
    private void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            displayTasks();
        }
    }

    @FXML
    private void nextPage() {
        currentPage++;
        displayTasks();
    }

    private void displayTasks() {
        tasksContainer.getChildren().clear();

        try {
            DBUtils.getTasks((currentPage - 1) * PAGE_SIZE, PAGE_SIZE).forEach(task -> {
//                TaskCard taskCard = new TaskCard(task.getUsername(), task.getRole());
//                tasksContainer.getChildren().add(taskCard);
            });
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
