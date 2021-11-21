package com.example.dz_clientserver_011;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AppController {

    public AnchorPane anchorPane;
    @FXML
    public ListView emailListView;
    @FXML
    public ListView lettersListView;
    @FXML
    public Button logoutButton;
    @FXML
    public Button newMessageButton;
    @FXML
    public TextArea textLetterTextArea;
    @FXML
    public Label loginLabel;
    @FXML
    public Button addEmailButton;

    private String login;
    private Email currentEmail;

    @FXML
    void initialize(){
        logoutButton.setOnAction(actionEvent -> {
            User.cleanRememberUser();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Stage stage = new Stage();
            try{
                stage.setScene(new Scene(loader.load()));
                stage.show();
                ((Stage) logoutButton.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addEmailButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEmail.fxml"));
            Stage stage = new Stage();
            try{
                stage.setScene(new Scene(loader.load()));
                loader.<AddEmailController>getController().initData(login);
                stage.show();// FIXME: 21.11.2021 Основное окно не обновляется
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        newMessageButton.setOnAction(actionEvent -> {
            if (currentEmail != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("newMessage.fxml"));
                Stage stage = new Stage();
                try {
                    stage.setScene(new Scene(loader.load()));
                    loader.<NewMessageController>getController().initData(currentEmail);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        emailListView.setCellFactory(param -> new ListCell<Email>() {
            @Override
            protected void updateItem(Email item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getEmail() == null) {
                    setText(null);
                } else {
                    setText(item.getEmail());
                }
            }
        });
        emailListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentEmail = (Email) emailListView.getSelectionModel().getSelectedItem();
                // FIXME: 21.11.2021 Выделить выбраный email
                if (currentEmail == null){
                    return;
                }
                ObservableList<Letter> obListLetters = FXCollections.observableArrayList();
                obListLetters.addAll(currentEmail.getLetters());
                lettersListView.setItems(obListLetters);
            }
        });

        lettersListView.setCellFactory(param -> new ListCell<Letter>() {
            @Override
            protected void updateItem(Letter item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getFrom() == null) {
                    setText(null);
                } else {
                    setText("From: "+item.getFrom()+"\n Subject: "+item.getSubject());
                }
            }
        });
        lettersListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Letter letter =(Letter)lettersListView.getSelectionModel().getSelectedItem();
                if (letter == null){
                    return;
                }
                textLetterTextArea.setText(letter.getText());
            }
        });
    }

    public void initData(String login){
        loginLabel.setText(login);
        this.login = login;

        ObservableList<Email> observableList = FXCollections.observableArrayList();
        List<Email> emails = Email.getEmails(login);
        observableList.addAll(emails);
        emailListView.setItems(observableList);
    }
}