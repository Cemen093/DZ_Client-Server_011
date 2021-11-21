package com.example.dz_clientserver_011;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.io.IOException;
import java.util.Properties;

public class AddEmailController {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button addButton;
    @FXML
    public Label errorLabel;

    private String login;

    @FXML
    void initialize(){
        errorLabel.setText("");
        addButton.setOnAction(actionEvent -> {
            if (!checkUser()){
                errorLabel.setText("error");
                return;
            }else {
                Email email = new Email(emailField.getText(), passwordField.getText());
                email.save(login);

                ((Stage) anchorPane.getScene().getWindow()).close();
            }
        });
    }

    public void initData(String login) {
        this.login=login;
    }

    private boolean checkUser(){
        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            Store store = Session.getInstance(props).getStore();
            store.connect("imap.gmail.com", emailField.getText(), passwordField.getText());
            store.close();
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }
}