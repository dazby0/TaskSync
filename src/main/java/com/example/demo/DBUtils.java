package com.example.demo;

import com.example.demo.Interfaces.UserAware;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Models.ReportAverageTime;
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
import java.time.format.DateTimeFormatter;

public class DBUtils {

    private static final String DB_URL = "";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title, LoggedUser loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
            Parent root = loader.load();

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
            connection = getConnection();
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
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
            closeResources(connection, psInsert, resultSet);
        }
    }


    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("SELECT password, role FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
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
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
    }


    public static ObservableList<String> loadWorkers() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList data = FXCollections.observableArrayList();

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("SELECT username FROM users WHERE role = 'Worker'");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new String(resultSet.getString(1)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return data;
    }

    public static void addTask(ActionEvent event, String assignedTo, String taskTitle, LoggedUser loggedUser) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            if (!taskTitle.isEmpty() && assignedTo != null && !assignedTo.isEmpty()) {
                connection = getConnection();
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
            closeResources(connection, preparedStatement, null);
        }
    }


    public static void createTeam(ActionEvent event, LoggedUser loggedUser, String teamName, String workers) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            if (!workers.isEmpty() && !teamName.isEmpty()) {
                connection = getConnection();
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
            closeResources(connection, preparedStatement, null);
        }
    }


    public static ObservableList<Task> loadTasks() {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("SELECT task_title, status, assigned_to, assigned_date, time_spent, finish_date FROM tasks ORDER BY assigned_date DESC");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String taskTitle = resultSet.getString("task_title");
                String status = resultSet.getString("status");
                String assignedTo = resultSet.getString("assigned_to");
                String assignedDate = resultSet.getString("assigned_date");
                String spentTime = resultSet.getString("time_spent");
                String finishDate = resultSet.getString("finish_date");

                tasks.add(new Task(taskTitle, status, assignedTo, assignedDate, spentTime, null, finishDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return tasks;
    }

    public static ObservableList<Task> loadYourTasks(LoggedUser loggedUser) {
        ObservableList<Task> tasks = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
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
            closeResources(connection, preparedStatement, resultSet);
        }
        return tasks;
    }

    public static ObservableList<Team> loadTeams() {
        ObservableList<Team> teams = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
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
            closeResources(connection, preparedStatement, resultSet);
        }
        return teams;
    }

    public static ObservableList<Team> loadWorkerTeams(String workerUsername) {
        ObservableList<Team> workerTeams = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
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
            closeResources(connection, preparedStatement, resultSet);
        }
        return workerTeams;
    }

    public static void updateStatus(Task task, String status, LocalDateTime startTime) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("UPDATE tasks SET status = ?, start_date = ? WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?");

            preparedStatement.setString(1, status);
            preparedStatement.setString(2, startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            preparedStatement.setString(3, task.getTaskName());
            preparedStatement.setString(4, task.getAssignedBy());
            preparedStatement.setString(5, task.getAssignedDate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static void updateStatus(Task task, String status, String timeSpent, String finishDate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("UPDATE tasks SET status = ?, time_spent = ?, finish_date = ? WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?");

            preparedStatement.setString(1, status);
            preparedStatement.setString(2, timeSpent);
            preparedStatement.setString(3, finishDate);
            preparedStatement.setString(4, task.getTaskName());
            preparedStatement.setString(5, task.getAssignedBy());
            preparedStatement.setString(6, task.getAssignedDate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, null);
        }
    }

    public static LocalDateTime getTaskStartTime(Task task) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        LocalDateTime startTime = null;

        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT start_date FROM tasks WHERE task_title = ? AND assigned_by = ? AND assigned_date = ?");

            statement.setString(1, task.getTaskName());
            statement.setString(2, task.getAssignedBy());
            statement.setString(3, task.getAssignedDate());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String startTimeStr = resultSet.getString("start_date");
                startTime = LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return startTime;
    }

    public static long calculateAverageTimeSpent(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long averageTimeSpentSeconds = 0;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement(
                    "SELECT AVG(TIME_TO_SEC(time_spent)) AS average_time FROM tasks WHERE assigned_to = ? AND status = 'Finished'"
            );
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                averageTimeSpentSeconds = resultSet.getLong("average_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }
        return averageTimeSpentSeconds;
    }

    public static ObservableList<ReportAverageTime> getTeamAverageTimes(String username) {
        ObservableList<ReportAverageTime> teamAverageTimes = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT t.team_name, AVG(TIME_TO_SEC(ta.time_spent)) AS average_time_spent " +
                            "FROM teams t " +
                            "JOIN tasks ta ON t.workers LIKE CONCAT('%', ta.assigned_to, '%') " +
                            "WHERE t.workers LIKE CONCAT('%', ?, '%') AND ta.status = 'Finished' " +
                            "GROUP BY t.team_name"
            );
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String teamName = resultSet.getString("team_name");
                long averageTimeSpentSeconds = resultSet.getLong("average_time_spent");

                long hours = averageTimeSpentSeconds / 3600;
                long minutes = (averageTimeSpentSeconds % 3600) / 60;
                long seconds = averageTimeSpentSeconds % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                teamAverageTimes.add(new ReportAverageTime(null, teamName, timeFormatted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return teamAverageTimes;
    }

    public static ObservableList<ReportAverageTime> getTeamAverageTimesManager(String username) {
        ObservableList<ReportAverageTime> teamAverageTimes = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            preparedStatement = connection.prepareStatement(
                    "SELECT t.team_name, AVG(TIME_TO_SEC(ta.time_spent)) AS average_time_spent " +
                            "FROM teams t " +
                            "JOIN tasks ta ON FIND_IN_SET(ta.assigned_to, t.workers) " +
                            "WHERE t.manager = ? AND ta.status = 'Finished' AND ta.assigned_by = t.manager " +
                            "GROUP BY t.team_name"
            );

            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String teamName = resultSet.getString("team_name");
                long averageTimeSpentSeconds = resultSet.getLong("average_time_spent");

                long hours = averageTimeSpentSeconds / 3600;
                long minutes = (averageTimeSpentSeconds % 3600) / 60;
                long seconds = averageTimeSpentSeconds % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                teamAverageTimes.add(new ReportAverageTime(null, teamName, timeFormatted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return teamAverageTimes;
    }


    public static ObservableList<ReportAverageTime> getWorkersAverageTimesManager(String username) {
        ObservableList<ReportAverageTime> workersAverageTimes = FXCollections.observableArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();

            preparedStatement = connection.prepareStatement(
                    "SELECT u.username, AVG(TIME_TO_SEC(ta.time_spent)) AS average_time_spent " +
                            "FROM users u " +
                            "JOIN teams t ON FIND_IN_SET(u.username, t.workers) " +
                            "JOIN tasks ta ON FIND_IN_SET(u.username, ta.assigned_to) " +
                            "WHERE t.manager = ? AND ta.status = 'Finished' AND ta.assigned_by = t.manager " +
                            "GROUP BY u.username"
            );

            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String workerUsername = resultSet.getString("username");
                long averageTimeSpentSeconds = resultSet.getLong("average_time_spent");

                long hours = averageTimeSpentSeconds / 3600;
                long minutes = (averageTimeSpentSeconds % 3600) / 60;
                long seconds = averageTimeSpentSeconds % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                workersAverageTimes.add(new ReportAverageTime(workerUsername, null, timeFormatted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, preparedStatement, resultSet);
        }

        return workersAverageTimes;
    }

}


