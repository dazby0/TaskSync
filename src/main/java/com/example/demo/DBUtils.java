package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tasksync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username, String role) {
        Parent root = null;

        if (username != null && role != null) {
            try {
                FXMLLoader loader = new FXMLLoader((DBUtils.class.getResource(fxmlFile)));
                root = loader.load();
                MainController mainController = loader.getController();
                mainController.setUserInformation(username);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 900, 700));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String role) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cant use this username");
                alert.show();
            }
            else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, role);
                psInsert.executeUpdate();

                changeScene(event, "main-manager-view.fxml", "Welcome to TaskSync", username, role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT password, role FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedRole = resultSet.getString("role");

                    System.out.println(retrievedRole);

                    if (retrievedPassword.equals(password)) {
                        if (retrievedRole.equals("Manager")) {
                            changeScene(event, "main-manager-view.fxml", "Welcome manager!", username, retrievedRole);
                        }
                        if (retrievedRole.equals(("Worker"))) {
                            changeScene(event, "main-worker-view.fxml", "Welcome worker!", username, retrievedRole);
                        }
                    } else {
                        System.out.println("Passwords did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadWorkers() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList data = FXCollections.observableArrayList();
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT username FROM users WHERE role = 'Worker'");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new String(resultSet.getString(1)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void addTask(ActionEvent event, String username, String taskTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            if (!taskTitle.isEmpty()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                preparedStatement = connection.prepareStatement("INSERT INTO tasks (status, assigned_to, start_date, task_title) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, "In progres...");
                preparedStatement.setString(2, username);
                preparedStatement.setObject(3, currentDateTime);
                preparedStatement.setString(4, taskTitle);
                preparedStatement.executeUpdate();

                changeScene(event, "main-manager-view.fxml", "Welcome Manager!", null, null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Added new Task!");
                alert.show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Set the task title!");
                alert.show();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
