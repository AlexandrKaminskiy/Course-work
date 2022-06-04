package com.example.coursework.controller;

import com.example.coursework.HelloApplication;
import com.example.coursework.network.TCPConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectGameController {

    @FXML
    private Button connectButton;

    @FXML
    private TextField ipAddress;

    @FXML
    private GameController gameController;
    TCPConnection tcpConnection;

    @FXML
    void connectGame(ActionEvent event) {
        tcpConnection = new TCPConnection("192.168.56.1", 1234);

        tcpConnection.connect();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_window.fxml"));

        try {
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Connect");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
