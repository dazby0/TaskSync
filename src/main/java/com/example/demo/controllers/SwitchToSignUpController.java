package com.example.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SwitchToSignUpController {
    @FXML
    public void switchToSignUp(ActionEvent event) throws Exception {
        Parent signUpView = FXMLLoader.load(getClass().getResource("/com/example/demo/resources/com/example/demo/signup-view.fxml"));
        Scene scene = new Scene(signUpView);

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
