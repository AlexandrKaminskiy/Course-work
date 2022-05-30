package com.example.coursework.users;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements Serializable {
    public double xPos;
    public double yPos;
    private final double g = 9.78;
    private final double startYSpeed = 50;
    private final double startXSpeed = 1;
    private final double dt = 0.15;
    private final double maxXSpeed = 7;
    private double ySpeed;
    private double xSpeed;
    private double acc;
    private boolean isJump;
    private boolean isMoving;
    Timer timer;
    public Player(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void attack() {

    }

    public void jump() {
        if (!isJump) {
            isJump = true;
            double startPoint = yPos;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ySpeed -= g * dt;
                    yPos -= ySpeed * dt;

                    if (yPos > startPoint) {
                        isJump = false;
                        ySpeed = startYSpeed;
                        yPos = startPoint;
                        cancel();
                    }
                }
            },0, 10);
        }
    }

    public void moveX(int side) {
        if (!isMoving) {
            acc = 3;
            xSpeed = 0;
            isMoving = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    xSpeed += acc * dt;
                    xPos += xSpeed * dt * (side);
//                    System.out.println(xSpeed);
                    if (xSpeed > maxXSpeed) {
                        xSpeed = maxXSpeed;
                        acc = 0;
                    }
                }
            },0, 10);
        }
//        xPos-=startXSpeed;
    }


    public void slowDown(int side) {
        if (isMoving) {
            isMoving = false;
            timer.cancel();

//            acc = 3;
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//
//                    xSpeed -= acc * dt * side;
//                    xPos -= xSpeed * dt * (side);
//
//                    if (Math.abs(xSpeed) < 0.0001) {
//                        xSpeed = 0;
//                        acc = 0;
//                        cancel();
//                    }
//                }
//            },0, 10);
        }

    }

}

