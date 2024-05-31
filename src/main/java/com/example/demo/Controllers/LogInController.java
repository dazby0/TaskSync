package com.example.demo.Controllers;

import com.example.demo.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button btn_login;

    @FXML
    private Button btn_switchToSignUp;

    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupButtonActions();
    }

    private void setupButtonActions() {
        btn_login.setOnAction(event -> DBUtils.logInUser(event, tf_username.getText(), tf_password.getText()));
        btn_switchToSignUp.setOnAction(event -> DBUtils.changeScene(event, "signup-view.fxml", "Sign Up!", null));
    }
}
