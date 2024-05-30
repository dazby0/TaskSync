package com.example.demo;

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
import java.util.ResourceBundle;

public class TeamsController implements Initializable, UserAware {
    @FXML
    private Button btn_createTeam;
    @FXML
    private Button btn_switchToMain;

    @FXML
    TableView<Team> tab_teams;

    @FXML
    TableColumn<Team, String> col_teamName;
    @FXML
    TableColumn<Team, String> col_workers;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_teamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        col_workers.setCellValueFactory(new PropertyValueFactory<>("workers"));

        btn_createTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "create-team-view.fxml", "Create Team", loggedUser);
            }
        });

        btn_switchToMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome manager", loggedUser);
            }
        });

        loadTeams();
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    private void loadTeams() {
        ObservableList<Team> teams = DBUtils.loadTeams();
        tab_teams.setItems(teams);
    }


}
