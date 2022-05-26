package com.example.coursework.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class GameController {

    @FXML
    private Canvas canvas;
    GraphicsContext context;

    int x = 0;
    @FXML
    void onKeyPressed(KeyEvent event) {
        context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        context.fillRect(x, 0, 100, 100);
        x += 1;
    }


    private void draw() {

    }

    @FXML
    void initialize() {
        context = canvas.getGraphicsContext2D();

        draw();

    }



}