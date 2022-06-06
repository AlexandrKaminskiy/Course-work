package com.example.coursework.handlers;

import com.example.coursework.gameobjects.Player;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MouseInputHandler implements EventHandler<MouseEvent> {
    private final Player player;

    public MouseInputHandler(Player player) {
        this.player = player;
    }

 


    @Override
    public void handle(MouseEvent mouseEvent) {
        if (MouseEvent.MOUSE_CLICKED.equals(mouseEvent.getEventType())) {
            player.attack(mouseEvent.getX(),mouseEvent.getY());

        }
    }
}