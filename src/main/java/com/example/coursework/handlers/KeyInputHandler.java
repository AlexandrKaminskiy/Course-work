package com.example.coursework.handlers;

import com.example.coursework.gameobjects.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyInputHandler implements EventHandler<KeyEvent> {
    final private Set<KeyCode> activeKeys = new HashSet<>();
    private final Player player;

    public KeyInputHandler(Player player) {
        this.player = player;
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            activeKeys.add(event.getCode());
            System.out.println(activeKeys);
            for (var key : activeKeys) {
                switch (key.getCode()) {
                    case 87: player.jump();break;
                    case 65: player.moveX(-1);break;
                    case 68: player.moveX(1);break;
                }
            }
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {

            switch (event.getCode().getCode()) {

                case 65: player.slowDown(-1);break;
                case 68: player.slowDown(1);break;
            }
            activeKeys.remove(event.getCode());
        }
    }


}