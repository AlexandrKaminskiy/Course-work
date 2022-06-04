package com.example.coursework.controller;

import com.example.coursework.network.TCPConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    void createGame(ActionEvent event) {
        try {
            tcpConnection = new TCPConnection(InetAddress.getLocalHost().getHostAddress(), Integer.parseInt(portTextField.getText()));
            infoLabel.setText("Server opened in: " + InetAddress.getLocalHost().getHostAddress() + " " + portTextField.getText());
            new Thread(()-> {
                try {
                    tcpConnection.createServer();
                } catch (IOException e) {
                    infoLabel.setText("Port already in use!");
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
