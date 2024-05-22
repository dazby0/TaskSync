package com.example.demo.Controllers;

import com.example.demo.Utils.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static com.example.demo.Utils.DBUtils.loadWorkers;

public class AddTaskController implements Initializable {
    @FXML
    private Button btn_switchToMain;
    @FXML
    private Button btn_addTask;

    @FXML
    private ComboBox<String> cb_assignedTo;

    @FXML
    private TextField tf_taskTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_assignedTo.setItems(loadWorkers());
        cb_assignedTo.setPromptText("Select worker:");

        btn_switchToMain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main-manager-view.fxml", "Welcome Manager!", null);
            }
        });

        btn_addTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = tf_taskTitle.getText();
                String assignedTo = cb_assignedTo.getValue();

                DBUtils.addTask(event, assignedTo, title);
            }
        });
    }

}
