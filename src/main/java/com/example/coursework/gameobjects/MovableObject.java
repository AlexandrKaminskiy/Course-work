package com.example.coursework.gameobjects;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class MovableObjects implements Serializable {
    protected final double g = 9.78;
    protected boolean hasProp;
    protected final double dt = 0.15;

    public double xPos;
    public double yPos;
    protected double ySpeed;
    protected double xSpeed;


    public MovableObjects() {
        hasProp = true;
        gravityImpact();
    }
    protected void gravityImpact() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!hasProp) {
                    System.out.println(yPos);
                    ySpeed -= g * dt;
                    yPos -= ySpeed * dt;
                }
            }
        },0,10);

    }
}
