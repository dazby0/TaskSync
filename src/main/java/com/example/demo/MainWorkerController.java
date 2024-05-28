package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class MainWorkerController implements Initializable, UserAware {
    @FXML
    private Button btn_logout;
    @FXML
    private Button btn_yourTeams;
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
        col_taskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_startDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        col_assignedBy.setCellValueFactory(new PropertyValueFactory<>("assignedBy"));

        // Custom cell factory for col_spentTime
        col_spentTime.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Task, String> param) {
                Task task = param.getValue();
                if ("Finished".equals(task.getStatus())) {
                    return new SimpleStringProperty(task.getTimeSpent());
                } else {
                    return new SimpleStringProperty("Not Finished");
                }
            }
        });

        col_info.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Task, Void> call(final TableColumn<Task, Void> param) {
                return new TableCell<>() {
                    private final Button btnMore = new Button("More...");
                    private final HBox pane = new HBox(btnMore);

                    {
                        btnMore.setOnAction(event -> {
                            Task task = getTableView().getItems().get(getIndex());
                            SharedService.getInstance().setCurrentTask(task);
                            DBUtils.changeScene(event, "task-info-view.fxml", "Task Information", loggedUser);
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
                };
            }
        });

        // Custom row factory to set background color based on task status with opacity 0.1
        tab_tasks.setRowFactory(tv -> new TableRow<Task>() {
            @Override
            protected void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    switch (item.getStatus()) {
                        case "New":
                            setStyle("-fx-background-color: rgba(191, 146, 107, 0.3);");
                            break;
                        case "In Progress":
                            setStyle("-fx-background-color: rgba(140, 71, 46, 0.3);");
                            break;
                        case "Finished":
                            setStyle("-fx-background-color: rgba(64, 56, 20, 0.3);");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });

        btn_logout.setOnAction(event -> DBUtils.changeScene(event, "login-view.fxml", "Log In to TaskSync!", null));
        btn_yourTeams.setOnAction(event -> DBUtils.changeScene(event, "teams-worker-view.fxml", "Check your teams!", loggedUser));
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