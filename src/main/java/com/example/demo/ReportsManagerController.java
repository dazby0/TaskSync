package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsManagerController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private TableView<ReportAverageTime> tab_workersAvgTime;
    @FXML
    private TableColumn<ReportAverageTime, String> col_worker;
    @FXML
    private TableColumn<ReportAverageTime, String> col_workerAvgTime;
    @FXML
    private TableView<ReportAverageTime> tab_teamsAvgTime;
    @FXML
    private TableColumn<ReportAverageTime, String> col_team;
    @FXML
    private TableColumn<ReportAverageTime, String> col_teamAvgTime;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", loggedUser));

        // Populate tables with data

    }

    private void populateWorkersAvgTimeTable() {
        // Fetch workers average time
        ObservableList<ReportAverageTime> workersAvgTime = DBUtils.getWorkersAverageTimesManager(loggedUser.getUsername());

        // Bind data to table
        col_worker.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_workerAvgTime.setCellValueFactory(new PropertyValueFactory<>("averageTimeSpent"));
        tab_workersAvgTime.setItems(workersAvgTime);
    }

    private void populateTeamsAvgTimeTable() {
        // Fetch teams average time
        ObservableList<ReportAverageTime> teamsAvgTime = DBUtils.getTeamAverageTimesManager(loggedUser.getUsername());
        // Bind data to table
        col_team.setCellValueFactory(new PropertyValueFactory<>("team"));
        col_teamAvgTime.setCellValueFactory(new PropertyValueFactory<>("averageTimeSpent"));
        tab_teamsAvgTime.setItems(teamsAvgTime);

    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        populateWorkersAvgTimeTable();
        populateTeamsAvgTimeTable();
    }
}
