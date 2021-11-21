package com.example.dz_clientserver_011;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import com.example.dz_clientserver_011.User;

public class LoginController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Label errorLabel;
    @FXML
    public CheckBox rememberMeCheckBox;
    @FXML
    public Button registrationButton;
    @FXML
    public AnchorPane anchorPane;

    @FXML
    void initialize(){
        errorLabel.setText("");

        registrationButton.setOnAction(actionEvent -> {
            goTo("registration.fxml");
        });

        loginButton.setOnAction(actionEvent -> {
            errorLabel.setText("");

            if (!checkUser()){
                errorLabel.setText("login error");
                return;
            }

            if (rememberMeCheckBox.isSelected()){
                User user = new User(loginField.getText(), passwordField.getText());
                user.saveAsRememberUser();
            }

            goTo("app.fxml", loginField.getText());
        });
    }

    private boolean checkUser(){
        User user = User.getUser(loginField.getText());
        return user != null && user.checkPassword(passwordField.getText());
    }

    private void goTo(String resourceName){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
            stage.show();
            ((Stage) anchorPane.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goTo(String resourceName, String str1){
        // FIXME: 20.11.2021 принимаемые аргументы
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceName));

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
            loader.<AppController>getController().initData(str1);
            // FIXME: 20.11.2021 тип контраллера не универсальный
            stage.show();
            ((Stage) anchorPane.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}