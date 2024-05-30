package com.example.demo;

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
import java.time.format.DateTimeFormatter;

public class DBUtils {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tasksync";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, LoggedUser loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = loader.load();

            if (loggedUser != null) {
                Object controller = loader.getController();
                if (controller instanceof UserAware) {
                    ((UserAware) controller).setLoggedUser(loggedUser);
                    System.out.println("LoggedUser passed to controller: " + loggedUser.getUsername()); // Debug statement
                } else {
                    System.out.println("Controller is not an instance of UserAware."); // Debug statement
                }
            } else {
                System.out.println("LoggedUser is null in changeScene."); // Debug statement
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

    public static void addTask(ActionEvent event, String assignedTo, String taskTitle, LoggedUser loggedUser) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            if (!taskTitle.isEmpty() && assignedTo != null && !assignedTo.isEmpty()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                preparedStatement = connection.prepareStatement("INSERT INTO tasks (status, assigned_to, assigned_date, task_title, assigned_by) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, "New");
                preparedStatement.setString(2, assignedTo);
                preparedStatement.setObject(3, currentDateTime);
                preparedStatement.setString(4, taskTitle);
                preparedStatement.setString(5, loggedUser.getUsername());
                preparedStatement.executeUpdate();

                changeScene(event, "main-manager-view.fxml", "Welcome Manager!", loggedUser);
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
            preparedStatement = connection.prepareStatement("SELECT task_title, status, assigned_to, assigned_date, time_spent FROM tasks");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String taskTitle = resultSet.getString("task_title");
                String status = resultSet.getString("status");
                String assignedTo = resultSet.getString("assigned_to");
                String assignedDate = resultSet.getString("assigned_date");
                String spentTime = resultSet.getString("time_spent");

                tasks.add(new Task(taskTitle, status, assignedTo, assignedDate, spentTime, null, null));
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

    public static ObservableList<Task> loadYourTasks(LoggedUser loggedUser) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT task_title, status, assigned_to, assigned_date, assigned_by, time_spent, finish_date FROM tasks WHERE assigned_to = ? ORDER BY assigned_date DESC");
            preparedStatement.setString(1, loggedUser.getUsername());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String taskTitle = resultSet.getString("task_title");
                String status = resultSet.getString("status");
                String assignedTo = resultSet.getString("assigned_to");
                String assignedDate = resultSet.getString("assigned_date");
                String assignedBy = resultSet.getString("assigned_by");
                String spentTime = resultSet.getString("time_spent");
                String finishTime = resultSet.getString("finish_date");

                tasks.add(new Task(taskTitle, status, assignedTo, assignedDate, spentTime, assignedBy, finishTime));
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

    public static ObservableList<Team> loadWorkerTeams(String workerUsername) {
        ObservableList<Team> workerTeams = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT team_name, workers, manager FROM teams WHERE workers LIKE ?");
            preparedStatement.setString(1, "%" + workerUsername + "%");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String teamName = resultSet.getString("team_name");
                String workers = resultSet.getString("workers");
                String manager = resultSet.getString("manager");

                workerTeams.add(new Team(manager, workers, teamName));
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
        return workerTeams;
    }

    public static void updateStatus(Task task, String status, LocalDateTime startTime) {
        String query = "UPDATE tasks SET status = ?, start_date = ? WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setString(2, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            statement.setString(3, task.getTaskName());
            statement.setString(4, task.getAssignedBy());
            statement.setString(5, task.getAssignedDate());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateStatus(Task task, String status, String timeSpent, String finishDate) {
        String query = "UPDATE tasks SET status = ?, time_spent = ?, finish_date = ? WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setString(2, timeSpent);
            statement.setString(3, finishDate);
            statement.setString(4, task.getTaskName());
            statement.setString(5, task.getAssignedBy());
            statement.setString(6, task.getAssignedDate());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static LocalDateTime getTaskStartTime(Task task) {
        String query = "SELECT start_date FROM tasks WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?";
        LocalDateTime startTime = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, task.getTaskName());
            statement.setString(2, task.getAssignedBy());
            statement.setString(3, task.getAssignedDate());
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String startTimeStr = rs.getString("start_date");
                startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return startTime;
    }
}
