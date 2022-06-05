package com.example.coursework.controller;

import com.example.coursework.dto.PlayerDto;
import com.example.coursework.dto.PlayerMapper;
import com.example.coursework.gameobjects.Player;
import com.example.coursework.handlers.KeyInputHandler;
import com.example.coursework.handlers.MouseInputHandler;
import com.example.coursework.network.TCPConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    @FXML
    private Canvas canvas;
    private GraphicsContext context;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;
    private TCPConnection tcpConnection;
    private PlayerDto opponent;
    private Player player;
    private PlayerMapper playerMapper;
    private int gameLimit = -10;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane connectAnchor;
    @FXML
    private AnchorPane gameAnchor;
    @FXML
    private Label iplabel;
    @FXML
    private TextField ipTextField;
    @FXML
    private AnchorPane startAnchor;

    @FXML
    void onConnectGameButton(ActionEvent event) {

        var strings = ipTextField.getText().split(":");

        new Thread(()->{
            tcpConnection = new TCPConnection(strings[0],Integer.parseInt(strings[1]));
            tcpConnection.connect();
            initGame();
        }).start();

        connectAnchor.setVisible(false);

    }

    @FXML
    void onConnectGame(ActionEvent event) {
        startAnchor.setVisible(false);
        connectAnchor.setVisible(true);
        ipTextField.setText("192.168.56.1:");
    }

    @FXML
    void onCreateGame(ActionEvent event) {
        int port = (int) (Math.random() * 1000 + 9000);
        try {
            String localhost = InetAddress.getLocalHost().getHostAddress();
            new Thread(()->{
                tcpConnection = new TCPConnection(localhost,port);
                tcpConnection.createServer();
                initGame();
            }).start();
            iplabel.setText("Game opened at " + localhost + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void draw() {

        context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        context.fillRect(player.xPos, player.yPos, 10, 15);
        for (var immObj : player.immovableObjects) {
            double w = immObj.getX2() - immObj.getX1();
            double h = immObj.getY2() - immObj.getY1();
            context.fillRect(immObj.getX1(), immObj.getY1(), w, h);
        }

        for (var bullet : player.getBullets()) {
            context.fillOval(bullet.xPos, bullet.yPos, 3,3);
        }

        if (opponent != null) {
            context.fillRect(opponent.xPos, opponent.yPos, 10, 15);
            for (var bullet : opponent.bullets) {
                context.fillOval(bullet.xPos, bullet.yPos, 3,3);
            }
        }
        context.fillText("Me " + -opponent.score, 10, 20);
        context.fillText("Opp " + -player.score, 10, 50);
    }

    private void receivingObjects() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    var opp = tcpConnection.receivingObject(playerMapper.toPlayerDto(player));
                    if (opp == null) {
                        System.out.println("connection refused");

                        cancel();
                        return;
                    }
                    opponent.bullets = opp.bullets;
                    opponent.hp = opp.hp;
                    opponent.score = opp.score;
                    opponent.xPos = opp.xPos;
                    opponent.yPos = opp.yPos;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },0,2);
    }

    void initGame() {
        gameAnchor.setVisible(true);
        startAnchor.setVisible(false);
        playerMapper = new PlayerMapper();
        opponent = new PlayerDto();
        player = new Player(200, canvas.getHeight() - 50, opponent);
        context = canvas.getGraphicsContext2D();
        keyEventHandler = new KeyInputHandler(player);
        mouseEventHandler = new MouseInputHandler(player);
        canvas.setOnKeyPressed(keyEventHandler);
        canvas.setOnKeyReleased(keyEventHandler);
        canvas.setOnMouseClicked(mouseEventHandler);
        Timer timer = new Timer();
        receivingObjects();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 0, 20);

        Timer winTimer = new Timer();
        receivingObjects();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (opponent.score <= gameLimit) {
                    canvas.setFocusTraversable(false);
                    return;
                }
                if (player.score <= gameLimit) {
                    canvas.setFocusTraversable(false);
                    return;
                }
            }
        }, 0, 20);
    }

    void endGame() {
        tcpConnection = null;
        player = null;
        opponent = null;
        gameAnchor.setVisible(false);
        startAnchor.setVisible(true);
    }
    @FXML
    void initialize() {

    }
}