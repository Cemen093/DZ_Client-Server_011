package com.example.dz_clientserver_011;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import com.example.dz_clientserver_011.User;

public class RegistrationController {

    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public Button registrationButton;
    @FXML
    public Button backButton;
    @FXML
    public Label errorLabel;
    @FXML
    public AnchorPane anchorPane;

    @FXML
    void initialize(){
        errorLabel.setText("");
        backButton.setOnAction(actionEvent -> {
            goTo("login.fxml");
        });

        registrationButton.setOnAction(actionEvent -> {
            if (!checkInput()){
                errorLabel.setText("Not correct input");
                return;
            }
            User user = new User(loginField.getText(), passwordField.getText());
            if (User.contains(user)){
                errorLabel.setText("User already exists");
                return;
            }
            if (!user.saveUser()){
                errorLabel.setText("registration error, sorry");
                return;
            }

            goTo("login.fxml");
        });
    }

    private boolean checkInput(){
        // FIXME: 20.11.2021 Доп проверки?
        errorLabel.setText("");
        if (loginField.getText().equals("")){
            errorLabel.setText("field login is empty");
            return false;
        }
        if (passwordField.getText().equals("")){
            errorLabel.setText("field password is empty");
            return false;
        }
        if (repeatPasswordField.getText().equals("")){
            errorLabel.setText("field repeat password is empty");
            return false;
        }
        if (!passwordField.getText().equals(repeatPasswordField.getText())){
            errorLabel.setText("field password and repeat password dont match");
            return false;
        }
        return true;
    }

    // FIXME: 20.11.2021 fix
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
}