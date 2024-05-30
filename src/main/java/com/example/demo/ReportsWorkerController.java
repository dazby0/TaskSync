package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportsWorkerController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private Label label_avgUserTime;
    @FXML
    private TableView<ReportAverageTime> tab_averageTimeReport;
    @FXML
    private TableColumn<ReportAverageTime, String> col_team;
    @FXML
    private TableColumn<ReportAverageTime, String> col_averageTimeSpent;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_team.setCellValueFactory(new PropertyValueFactory<>("team"));
        col_averageTimeSpent.setCellValueFactory(new PropertyValueFactory<>("averageTimeSpent"));

        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-worker-view.fxml", "Track Your Tasks", loggedUser));
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        loadAverageTimeSpent();
        loadTeamAverageTimes();
    }

    private void loadAverageTimeSpent() {
        long averageTimeSpentSeconds = DBUtils.calculateAverageTimeSpent(loggedUser.getUsername());

        long hours = averageTimeSpentSeconds / 3600;
        long minutes = (averageTimeSpentSeconds % 3600) / 60;
        long seconds = averageTimeSpentSeconds % 60;

        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        label_avgUserTime.setText("Average Time Spent: " + timeFormatted);
    }

    private void loadTeamAverageTimes() {
        ObservableList<ReportAverageTime> teamAverageTimes = DBUtils.getTeamAverageTimes(loggedUser.getUsername());
        tab_averageTimeReport.setItems(teamAverageTimes);
    }
}