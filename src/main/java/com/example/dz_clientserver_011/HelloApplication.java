package com.example.dz_clientserver_011;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader;

        User user = User.getRememberUser();
        if (user != null){
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("app.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Email Client");
            stage.setScene(scene);
            fxmlLoader.<AppController>getController().initData(user.getLogin());
        } else {
            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Email Client");
            stage.setScene(scene);
        }
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}