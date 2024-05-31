package com.example.demo.Controllers;

import com.example.demo.DBUtils;
import com.example.demo.Models.LoggedUser;
import com.example.demo.Models.ReportAverageTime;
import com.example.demo.Interfaces.UserAware;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
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
        setupButtonActions();
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        populateWorkersAvgTimeTable();
        populateTeamsAvgTimeTable();
    }

    private void setupButtonActions() {
        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", loggedUser));
    }

    private void populateWorkersAvgTimeTable() {
        ObservableList<ReportAverageTime> workersAvgTime = DBUtils.getWorkersAverageTimesManager(loggedUser.getUsername());

        col_worker.setCellValueFactory(new PropertyValueFactory<>("username"));
        col_workerAvgTime.setCellValueFactory(new PropertyValueFactory<>("averageTimeSpent"));
        tab_workersAvgTime.setItems(workersAvgTime);
    }

    private void populateTeamsAvgTimeTable() {
        ObservableList<ReportAverageTime> teamsAvgTime = DBUtils.getTeamAverageTimesManager(loggedUser.getUsername());

        col_team.setCellValueFactory(new PropertyValueFactory<>("team"));
        col_teamAvgTime.setCellValueFactory(new PropertyValueFactory<>("averageTimeSpent"));
        tab_teamsAvgTime.setItems(teamsAvgTime);
    }
}