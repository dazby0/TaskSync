package com.example.demo.Controllers;

import com.example.demo.*;
import com.example.demo.Interfaces.UserAware;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Models.SharedService;
import com.example.demo.Models.Task;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

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
    private TableColumn<Task, String> col_spentTime;
    @FXML
    private TableColumn<Task, Void> col_taskInfo;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupColumns();
        setupButtonActions();
        setupTaskTableView();
    }

    private void setupColumns() {
        col_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_assignedTo.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        col_spentTime.setCellValueFactory(param -> {
            Task task = param.getValue();
            if ("Finished".equals(task.getStatus())) {
                return new SimpleStringProperty(task.getTimeSpent());
            } else {
                return new SimpleStringProperty("Not Finished");
            }
        });
    }

    private void setupButtonActions() {
        btn_logout.setOnAction(event -> DBUtils.changeScene(event, "login-view.fxml", "Log In to TaskSync!", null));
        btn_addTask.setOnAction(event -> {
            DBUtils.changeScene(event, "add-task-view.fxml", "Add New Task!", loggedUser);
        });
        btn_switchToTeams.setOnAction(event -> DBUtils.changeScene(event, "teams-manager-view.fxml", "Your teams!", loggedUser));
        btn_switchToReports.setOnAction(event -> DBUtils.changeScene(event, "reports-manager-view.fxml", "View Reports!", loggedUser));
    }

    private void setupTaskTableView() {
        col_taskInfo.setCellFactory(param -> new TableCell<>() {
            private final Button btnMore = new Button("More...");
            private final HBox pane = new HBox(btnMore);

            {
                btnMore.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    SharedService.getInstance().setCurrentTask(task);
                    DBUtils.changeScene(event, "task-info-manager-view.fxml", "Task Information", loggedUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        tab_tasks.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    String style = switch (item.getStatus()) {
                        case "New" -> "-fx-background-color: rgba(191, 146, 107, 0.3);";
                        case "In Progress" -> "-fx-background-color: rgba(140, 71, 46, 0.3);";
                        case "Finished" -> "-fx-background-color: rgba(64, 56, 20, 0.3);";
                        default -> "";
                    };
                    setStyle(style);
                }
            }
        });
    }

    private void loadTasks() {
        ObservableList<Task> tasks = DBUtils.loadTasks();
        tab_tasks.setItems(tasks);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        setUserInformation(loggedUser);
        loadTasks();
    }

    public void setUserInformation(LoggedUser loggedUser) {
        label_welcome.setText("Hello " + loggedUser.getUsername() + "!");
    }
}