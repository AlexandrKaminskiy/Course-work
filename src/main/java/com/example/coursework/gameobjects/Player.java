package com.example.coursework.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends MovableObject {


    private final double startYSpeed = 40;
    private final double startXSpeed = 1;

    private final double maxXSpeed = 7;

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
        if (hasProp) {
            hasProp = false;
            ySpeed = startYSpeed;
        }
    }

    public void moveX(int side) {
        if (!isMoving) {
            acc = 3;
            xSpeed = startXSpeed;
            isMoving = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    xSpeed += acc * dt;
                    double currentX = xPos;
                    xPos += xSpeed * dt * (side);

                    if (xSpeed > maxXSpeed) {
                        xSpeed = maxXSpeed;
                        acc = 0;
                    }


                    for (var immObj : immovableObjects) {
                        if (((immObj.getY1() - yPos) < EXP
                                && (immObj.getY2() - yPos) > EXP)
                                || ((immObj.getY1() - (yPos + playerHeight)) < EXP
                                && (immObj.getY2() - (yPos + playerHeight)) > EXP)) {

                            int s = side == 1 ? FROM_LEFT : FROM_RIGHT;

                            if (s == FROM_RIGHT) {
                                double res = collisionCoord(s, currentX, xPos, immObj);
                                if (Math.abs(res + 100) > EXP) {
                                    xPos = immObj.getX2();

                                    break;
                                }
                            } else {
                                double res = collisionCoord(s, currentX + playerWidth, xPos + playerWidth, immObj);
                                if (Math.abs(res + 100) > EXP) {
                                    xPos = immObj.getX1() - playerWidth;
                                    break;
                                }
                            }


                        }
                    }
                }
            },0, 10);
        }

    }

    public void slowDown(int side) {
        if (isMoving) {
            isMoving = false;
            timer.cancel();
        }

    }

}