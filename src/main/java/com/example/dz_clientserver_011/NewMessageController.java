package com.example.dz_clientserver_011;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class NewMessageController {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public TextField toEmailsTextField;
    @FXML
    public TextField subjectTextField;
    @FXML
    public TextArea textLetterTextArea;
    @FXML
    public TextArea pathsAttachmentTextArea;
    @FXML
    public Button selectFiles;
    @FXML
    public Button sendButton;

    private Email email;

    public void initData(Email email) {
        this.email = email;
    }

    @FXML
    void initialize(){
        final FileChooser fileChooser = new FileChooser();
        selectFiles.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                pathsAttachmentTextArea.clear();
                List<File> files = fileChooser.showOpenMultipleDialog((Stage) anchorPane.getScene().getWindow());

                printLog(pathsAttachmentTextArea, files);
            }
        });

        sendButton.setOnAction(actionEvent -> {
            // FIXME: 21.11.2021 Проверки на входящие данные

            email.sendLetter(toEmailsTextField.getText().split(";"), subjectTextField.getText(),
                    textLetterTextArea.getText(), pathsAttachmentTextArea.getText().split("\n"));
            ((Stage) anchorPane.getScene().getWindow()).close();
        });
    }

    private void printLog(TextArea textArea, List<File> files) {
        if (files == null || files.isEmpty()) {
            return;
        }
        for (File file : files) {
            textArea.appendText(file.getAbsolutePath() + "\n");
        }
    }
}