package com.example.coursework.controller;

import com.example.coursework.gameobjects.ImmovableObject;
import com.example.coursework.handlers.KeyInputHandler;
import com.example.coursework.handlers.MouseInputHandler;
import com.example.coursework.gameobjects.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    @FXML
    private Canvas canvas;
    private GraphicsContext context;
    private EventHandler<KeyEvent> keyEventHandler;
    private EventHandler<MouseEvent> mouseEventHandler;


    Player player;
    private void draw() {
        context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        context.fillRect(player.xPos, player.yPos, 10, 15);
        for (var immObj : player.immovableObjects) {
            double w = immObj.getX2() - immObj.getX1();
            double h = immObj.getY2() - immObj.getY1();
            context.fillRect(immObj.getX1(), immObj.getY1(), w, h);
        }
    }

    @FXML
    void initialize() {
        player = new Player(200, canvas.getHeight() - 50);
        context = canvas.getGraphicsContext2D();
        keyEventHandler = new KeyInputHandler(player);
        mouseEventHandler = new MouseInputHandler(player);
        canvas.setOnKeyPressed(keyEventHandler);
        canvas.setOnKeyReleased(keyEventHandler);
        canvas.setOnMouseClicked(mouseEventHandler);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 0, 20);

    }
}