package com.example.demo.Utils;

import com.example.demo.Interfaces.UserAware;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Models.Task;
import com.example.demo.Models.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class DBUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tasksync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, LoggedUser loggedUser) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            root = loader.load();

            if (loggedUser != null) {
                Object controller = loader.getController();

                if (controller instanceof UserAware) {
                    ((UserAware) controller).setLoggedUser(loggedUser);
                }
            }

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                alert.setContentText("You can't use this username");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, role);
                psInsert.executeUpdate();

                LoggedUser loggedUser = new LoggedUser(username, role);

                if (role.equals("Manager")) {
                    DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome to TaskSync", loggedUser);
                } else if (role.equals("Worker")) {
                    DBUtils.changeScene(event, "main-worker-view.fxml", "Welcome to TaskSync", loggedUser);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (psCheckUserExists != null) psCheckUserExists.close();
                if (psInsert != null) psInsert.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

                    if (retrievedPassword.equals(password)) {
                        LoggedUser loggedUser = new LoggedUser(username, retrievedRole);
                        if (retrievedRole.equals("Manager")) {
                            changeScene(event, "main-manager-view.fxml", "Welcome manager!", loggedUser);
                        } else if (retrievedRole.equals("Worker")) {
                            changeScene(event, "main-worker-view.fxml", "Welcome worker!", loggedUser);
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
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public static void addTask(ActionEvent event, String assignedTo, String taskTitle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            if (!taskTitle.isEmpty() && assignedTo != null && !assignedTo.isEmpty()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                preparedStatement = connection.prepareStatement("INSERT INTO tasks (status, assigned_to, start_date, task_title) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, "In progress...");
                preparedStatement.setString(2, assignedTo);
                preparedStatement.setObject(3, currentDateTime);
                preparedStatement.setString(4, taskTitle);
                preparedStatement.executeUpdate();

                changeScene(event, "main-manager-view.fxml", "Welcome Manager!", null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Added new Task!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all fields!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createTeam(ActionEvent event, LoggedUser loggedUser, String teamName, String workers) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            if (!workers.isEmpty() && !teamName.isEmpty()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                preparedStatement = connection.prepareStatement("INSERT INTO teams (manager, workers, team_name) VALUES (?, ?, ?)");
                preparedStatement.setString(1, loggedUser.getUsername());
                preparedStatement.setString(2, workers);
                preparedStatement.setString(3, teamName);
                preparedStatement.executeUpdate();

                changeScene(event, "teams-manager-view.fxml", "Check Your Teams!", loggedUser);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Created new Team!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please specify the workers!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static ObservableList<Task> loadTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT task_title, status, assigned_to, start_date FROM tasks");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String taskTitle = resultSet.getString("task_title");
                String status = resultSet.getString("status");
                String assignedTo = resultSet.getString("assigned_to");
                String startDate = resultSet.getString("start_date");

                tasks.add(new Task(taskTitle, status, assignedTo, startDate, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tasks;
    }

    public static ObservableList<Team> loadTeams() {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT team_name, workers FROM teams");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String teamName = resultSet.getString("team_name");
                String workers = resultSet.getString("workers");

                teams.add(new Team(null, workers, teamName));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return teams;
    }
}
