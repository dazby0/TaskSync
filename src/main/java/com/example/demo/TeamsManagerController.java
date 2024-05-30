package com.example.demo;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TeamsManagerController implements Initializable, UserAware {
    @FXML
    private Button btn_createTeam;
    @FXML
    private Button btn_switchToMain;

    @FXML
    private TableView<Team> tab_teams;

    @FXML
    private TableColumn<Team, String> col_teamName;
    @FXML
    private TableColumn<Team, String> col_workers;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupButtonActions();
    }

    private void setupTableColumns() {
        col_teamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        col_workers.setCellValueFactory(new PropertyValueFactory<>("workers"));
    }

    private void setupButtonActions() {
        btn_createTeam.setOnAction(event -> DBUtils.changeScene(event, "create-team-view.fxml", "Create Team", loggedUser));
        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome manager", loggedUser));
    }

    private void loadTeams() {
        ObservableList<Team> teams = DBUtils.loadTeams();
        tab_teams.setItems(teams);
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        loadTeams();
    }
}

