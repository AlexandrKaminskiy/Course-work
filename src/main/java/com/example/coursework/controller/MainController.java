package com.example.coursework.controller;

import com.example.coursework.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button connectToGameButton;

    @FXML
    private Button createGameButton;

    @FXML
    void connectToGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("connect_game.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Connect");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (IOException e) {e.printStackTrace();}
    }

    @FXML
    void createGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("create_game.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create game");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (IOException e) {e.printStackTrace();}
    }
}