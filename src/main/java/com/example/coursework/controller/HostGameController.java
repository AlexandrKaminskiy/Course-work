package com.example.coursework.controller;

import com.example.coursework.HelloApplication;
import com.example.coursework.network.TCPConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostGameController {

    TCPConnection tcpConnection;
    @FXML
    private Button createGameButton;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField portTextField;
    @FXML
    private GameController gameController;

    @FXML
    void createGame(ActionEvent event) {
        try {
            tcpConnection = new TCPConnection(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(portTextField.getText()));
            infoLabel.setText("Server opened in: " + InetAddress.getLocalHost().getHostAddress() + " " + portTextField.getText());

            tcpConnection.createServer();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game_window.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Connect");
            stage.setScene(new Scene(root1));
            stage.show();
            } catch (IOException e) {
                infoLabel.setText("Port already in use!");
            }

    }
}
