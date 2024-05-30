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

public class TeamsWorkerController implements Initializable, UserAware {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private TableView<Team> tab_workerTeams;
    @FXML
    private TableColumn<Team, String> col_teamName;
    @FXML
    private TableColumn<Team, String> col_teamManager;
    @FXML
    private TableColumn<Team, String> col_colleagues;

    private LoggedUser loggedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_teamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        col_teamManager.setCellValueFactory(new PropertyValueFactory<>("manager"));
        col_colleagues.setCellValueFactory(new PropertyValueFactory<>("workers"));

        btn_switchToMain.setOnAction(event -> DBUtils.changeScene(event, "main-worker-view.fxml", "Check Your Tasks!", loggedUser));
    }

    @Override
    public void setLoggedUser(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
        loadWorkerTeams();
    }

    private void loadWorkerTeams() {
        if (loggedUser != null) {
            ObservableList<Team> workerTeams = DBUtils.loadWorkerTeams(loggedUser.getUsername());
            for (Team team : workerTeams) {
                String workers = team.getWorkers();
                String[] workerArray = workers.split(",\\s*");
                StringBuilder newWorkers = new StringBuilder();

                for (String worker : workerArray) {
                    if (!worker.equals(loggedUser.getUsername())) {
                        if (!newWorkers.isEmpty()) {
                            newWorkers.append(", ");
                        }
                        newWorkers.append(worker);
                    }
                }

                team.setWorkers(newWorkers.toString());
            }
            tab_workerTeams.setItems(workerTeams);
        }
    }
}
