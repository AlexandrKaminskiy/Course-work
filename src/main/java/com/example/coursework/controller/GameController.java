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
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
    private int gameLimit = -15;
    private Thread receivingThread;
    private String message = new String();
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane connectAnchor;
    @FXML
    private AnchorPane gameAnchor;
    @FXML
    private Label iplabel;
    @FXML
    private Label winLabel;
    @FXML
    private Label loseLabel;
    @FXML
    private TextField ipTextField;
    @FXML
    private AnchorPane startAnchor;
    private String ipinfo;
    private int port;
    @FXML
    void onConnectGameButton(ActionEvent event) {
        winLabel.setVisible(false);
        loseLabel.setVisible(false);
        var strings = ipTextField.getText().split(":");
        if (strings.length == 0) {
            ipTextField.setText("192.168.56.1:");
            return;
        }
        try {
            int port = Integer.parseInt(strings[1]);
            if (port == this.port) {
                ipTextField.setText("192.168.56.1:");
                return;
            }
        } catch (Exception e) {
            ipTextField.setText("192.168.56.1:");
            return;
        }
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
        winLabel.setVisible(false);
        loseLabel.setVisible(false);
        port = (int) (Math.random() * 1000 + 9000);
        try {
            String localhost = InetAddress.getLocalHost().getHostAddress();
            iplabel.setVisible(true);
            new Thread(()->{
                tcpConnection = new TCPConnection(localhost,port);
                tcpConnection.createServer();
                iplabel.setVisible(false);
                initGame();
            }).start();
            iplabel.setText("Game opened at " + localhost + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    private void drawEnemyAmogus() {
        context.setFill(Color.rgb(255,0,0));
        context.fillRect(opponent.xPos, opponent.yPos, 10, 15);
        context.setFill(Color.rgb(0,200,255));
        context.fillRect(opponent.xPos+5, opponent.yPos + 3, 5, 2);
    }

    private void drawMyAmogus() {
        context.setFill(Color.rgb(0,0,255));
        context.fillRect(player.xPos, player.yPos, 10, 15);
        context.setFill(Color.rgb(0,200,255));
        context.fillRect(player.xPos+5, player.yPos + 3, 5, 2);
    }
    private void draw() {
        try {
            context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            drawMyAmogus();
            context.setFill(Color.rgb(100,100,100));
            for (var immObj : player.immovableObjects) {
                double w = immObj.getX2() - immObj.getX1();
                double h = immObj.getY2() - immObj.getY1();
                context.fillRect(immObj.getX1(), immObj.getY1(), w, h);
            }

            context.setFill(Color.rgb(0,0,150));
            for (var bullet : player.getBullets()) {
                context.fillOval(bullet.xPos, bullet.yPos, 3, 3);
            }

            if (opponent != null) {
                drawEnemyAmogus();
                context.setFill(Color.rgb(150,0,0));
                for (var bullet : opponent.bullets) {
                    context.fillOval(bullet.xPos, bullet.yPos, 3, 3);
                }
            }
            context.setFill(Color.rgb(0,0,0));
            context.fillText("Me " + -opponent.score, 10, 20);
            context.fillText("Opponent " + -player.score, 10, 50);
        } catch (NullPointerException e){}
    }

    private void receivingObjects() {

        receivingThread = new Thread(()-> {
            while (true){

                try {
                    var opp = tcpConnection.receivingObject(playerMapper.toPlayerDto(player));

                    opponent.bullets = opp.bullets;
                    opponent.hp = opp.hp;
                    opponent.score = opp.score;
                    opponent.xPos = opp.xPos;
                    opponent.yPos = opp.yPos;
                    for (var bullet : opponent.bullets) {
                        if (bullet.isHit) {
                            player.getDamaged = true;
//                            player.getDamaged();
                        }
                    }
                    opponent.bullets.stream().forEach(e -> {if (e.isHit) System.out.println(e.isHit);});
//                    System.out.println(opponent.bullets);
                } catch (Exception e) {
                    endGame("You winn!");
                    if (opponent.score == gameLimit) {
                        showMsg("You win!");
                    }
                    if (player.score == gameLimit) {
                        showMsg("You lose!");
                    }
                    return;
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        receivingThread.start();
    }

    void initGame() {
        canvas.setFocusTraversable(true);
        connectAnchor.setVisible(false);
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

        receivingObjects();

        new Thread(()->{
            while (true) {
                draw();
                if (!gameAnchor.isVisible()) break;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        receivingObjects();
        new Thread(()->{
            while (true) {
                if (opponent != null && opponent.score <= gameLimit) {
                    endGame("You win!");
                    showMsg("You win!");
                    break;
                }
                if (player != null && player.score <= gameLimit) {
                    endGame("You lose!");
                    showMsg("You lose!");
                    break;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    void showMsg(String message) {
        if (message.equals("You win!")) {
            winLabel.setVisible(true);
            loseLabel.setVisible(false);
        }

        if (message.equals("You lose!")) {
            loseLabel.setVisible(true);
            winLabel.setVisible(false);
        }
    }
    void endGame(String message) {
        canvas.setFocusTraversable(false);
        gameAnchor.setVisible(false);
        startAnchor.setVisible(true);
        loseLabel.setVisible(false);
        winLabel.setVisible(false);
        iplabel.setVisible(false);
        try {
            tcpConnection.close();
        } catch (IOException | NullPointerException e) {
        }
        try {
            player.attackedThread.stop();
        } catch (NullPointerException e) {

        }
        tcpConnection = null;
//        player = null;
//        opponent = null;
    }
    @FXML
    void initialize() {

//        iplabel.setText("32332443243324432");
    }
}