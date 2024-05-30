package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button btn_signup;
    @FXML
    private Button btn_switchToLogin;

    @FXML
    private RadioButton radio_manager;

    @FXML
    private RadioButton radio_worker;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureRadioButtons();
        setupButtonActions();
    }

    private void configureRadioButtons() {
        ToggleGroup toggleGroup = new ToggleGroup();
        radio_manager.setToggleGroup(toggleGroup);
        radio_worker.setToggleGroup(toggleGroup);

        radio_worker.setSelected(true);
    }

    private void setupButtonActions() {
        btn_signup.setOnAction(event -> {
            String toggleName = ((RadioButton) radio_manager.getToggleGroup().getSelectedToggle()).getText();

            if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
                DBUtils.signUpUser(event, tf_username.getText(), tf_password.getText(), toggleName);
            } else {
                System.out.println("Please fill all fields!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all fields");
                alert.show();
            }
        });

        btn_switchToLogin.setOnAction(event -> DBUtils.changeScene(event, "login-view.fxml", "Log In!", null));
    }
}
