package com.example.coursework.gameobjects;

import com.example.coursework.dto.PlayerDto;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends MovableObject {
    private final transient double speed = 60;
    private final transient  double maxYSpeed = -60;
    private transient boolean isPresent;
    private transient boolean nextIterDelete;
    private transient double mouseX;
    private transient double mouseY;
    private transient Player player;
    public double futX;
    public double futY;

    public boolean isPresent() {
        return isPresent;
    }


    public Bullet(double x1, double y1, double x2, double y2, Player player, PlayerDto opponent){
        super(opponent);
        this.player = player;
        this.isPresent = true;
        this.xPos = x1;
        this.yPos = y1;
        this.mouseX = x2;
        this.mouseY = y2;
        double c = Math.sqrt((x2 - x1) * (x2 - x1) + (y1 - y2) * (y1 - y2));
        xSpeed = speed * ((x2 - x1) / c);
        ySpeed = speed * ((y1 - y2) / c);

    }

    @Override
    protected void gravityImpact() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                xPos += xSpeed * dt;
                yPos -= (ySpeed * dt);
                ySpeed -= g * dt;
                if (ySpeed < maxYSpeed) {
                    ySpeed = maxYSpeed;
                }
                futX = xPos + xSpeed * dt;
                futY = yPos - ySpeed * dt;
                if (nextIterDelete) isPresent = false;
                if (collisionControl.checkCollision(futX, futY, xPos, yPos).equals(CollisionsState.WITH_IMM) ||
                    collisionControl.checkCollision(futX, futY, xPos, yPos).equals(CollisionsState.WITH_PLAYER)){
                    nextIterDelete = true;
                }

            }
        },0, 10);
    }

}
