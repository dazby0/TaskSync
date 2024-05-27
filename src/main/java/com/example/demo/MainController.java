package com.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, UserAware {
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_addTask;
    @FXML
    private Button btn_switchToTeams;
    @FXML
    private Button btn_switchToReports;

    @FXML
    private Label label_welcome;

    @FXML
    private TableView<Task> tab_tasks;
    @FXML
    private TableColumn<Task, String> col_taskName;
    @FXML
    private TableColumn<Task, String> col_status;
    @FXML
    private TableColumn<Task, String> col_assignedTo;
    @FXML
    private TableColumn<Task, String> col_startDate;
    @FXML

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_assignedTo.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login-view.fxml", "Log In to TaskSync!", null);
            }
        });

        btn_addTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "add-task-view.fxml", "Add New Task!", loggedUser);
                System.out.println(loggedUser.getUsername());
            }
        });


        btn_switchToTeams.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "teams-manager-view.fxml", "Your teams!", loggedUser);
            }
        });

        btn_switchToReports.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "reports-manager-view.fxml", "View Reports!", loggedUser);
            }
        });

        loadTasks();
    }

    private void loadTasks() {
        ObservableList<Task> tasks = DBUtils.loadTasks();
        tab_tasks.setItems(tasks);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        setUserInformation(loggedUser);
    }

    public void setUserInformation(LoggedUser loggedUser) {
        label_welcome.setText("Hello " + loggedUser.getUsername() + "!");
    }
}
