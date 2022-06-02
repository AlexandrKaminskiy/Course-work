package com.example.coursework.controller;

import com.example.coursework.dto.PlayerDto;
import com.example.coursework.gameobjects.Player;
import com.example.coursework.handlers.KeyInputHandler;
import com.example.coursework.handlers.MouseInputHandler;
import com.example.coursework.network.TCPConnection;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    @FXML
    private Canvas canvas;
    private GraphicsContext context;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;
    private TCPConnection tcpConnection;
    PlayerDto opponent;
    Player player;

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
            for (var bullet : opponent.getBullets()) {
                context.fillOval(bullet.xPos, bullet.yPos, 3,3);
            }
        }
    }

    private void receivingObjects() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    byte[] arr = tcpConnection.receivingObject();
                    var opp = opponent.byteArrayToObject(arr);
                    opponent.xPos = opp.xPos;
                    opponent.yPos = opp.yPos;
                    opponent.hp = opp.hp;
                    opponent.score = opp.score;
                    opponent.bullets = opp.bullets;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        },0,1);

    }

    private void sendingObjects() {

        tcpConnection = new TCPConnection();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tcpConnection.sendObject(player);
            }
        },0,1);
    }

    @FXML
    void initialize() {
        opponent = new PlayerDto();
        player = new Player(200, canvas.getHeight() - 50, opponent);
        context = canvas.getGraphicsContext2D();
        keyEventHandler = new KeyInputHandler(player);
        mouseEventHandler = new MouseInputHandler(player);
        canvas.setOnKeyPressed(keyEventHandler);
        canvas.setOnKeyReleased(keyEventHandler);
        canvas.setOnMouseClicked(mouseEventHandler);
        Timer timer = new Timer();
        sendingObjects();
        receivingObjects();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 0, 20);
    }

}