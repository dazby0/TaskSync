package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWorkerController implements Initializable, UserAware {
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_yourTeams;
    @FXML
    private Button btn_workerReports;
    @FXML
    private Label label_welcome;
    @FXML
    private TableView<Task> tab_tasks;
    @FXML
    private TableColumn<Task, String> col_taskName;
    @FXML
    private TableColumn<Task, String> col_status;
    @FXML
    private TableColumn<Task, String> col_startDate;
    @FXML
    private TableColumn<Task, String> col_spentTime;
    @FXML
    private TableColumn<Task, String> col_assignedBy;
    @FXML
    private TableColumn<Task, Void> col_info;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupButtonActions();
        setupColumns();
        setupTaskTableView();
    }

    private void setupColumns() {
        col_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        col_assignedBy.setCellValueFactory(new PropertyValueFactory<>("assignedBy"));
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
        btn_yourTeams.setOnAction(event -> DBUtils.changeScene(event, "teams-worker-view.fxml", "Check your teams!", loggedUser));
        btn_workerReports.setOnAction(event -> DBUtils.changeScene(event, "reports-worker-view.fxml", "Look at Your Reports!", loggedUser));
    }

    private void setupTaskTableView() {
        col_info.setCellFactory(param -> new TableCell<>() {
            private final Button btnMore = new Button("More...");
            private final HBox pane = new HBox(btnMore);

            {
                btnMore.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    SharedService.getInstance().setCurrentTask(task);
                    DBUtils.changeScene(event, "task-info-worker-view.fxml", "Task Information", loggedUser);
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

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        setUserInformation();
        loadYourTasks();
    }

    private void setUserInformation() {
        label_welcome.setText("Hello " + loggedUser.getUsername() + "!");
    }

    private void loadYourTasks() {
        ObservableList<Task> tasks = DBUtils.loadYourTasks(loggedUser);
        tab_tasks.setItems(tasks);
    }
}