package com.example.coursework.gameobjects;

import java.util.*;

public class Player extends MovableObject {

    private final transient double startYSpeed = 40;
    private final transient double startXSpeed = 1;
    private final double maxXSpeed = 7;
    private transient double acc;
    private transient boolean isMoving;
    private transient Timer timer;
    private List<Bullet> bullets = Collections.synchronizedList(new ArrayList<>());

    public List<Bullet> getBullets() {
        return bullets;
    }

    public Player(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        bulletControl();
    }

    private void bulletControl() {
        Timer bulletTimer = new Timer();
        bulletTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < bullets.size(); i++) {
                    if (!bullets.get(i).isPresent()) {
                        bullets.remove(i);
                        i--;
                    }
                }
            }
        },0,10);
    }

    public void attack(double mouseX, double mouseY) {
        bullets.add(new Bullet(xPos + playerWidth / 2,yPos + playerHeight / 2,mouseX,mouseY,this));
        System.out.println(bullets.size());
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
                        if (((immObj.getY1() - yPos) <= 0
                                && (immObj.getY2() - yPos) >= 0)
                                || ((immObj.getY1() - (yPos + playerHeight)) <= 0
                                && (immObj.getY2() - (yPos + playerHeight)) >= 0)) {

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
                    if (hasProp) {
                        checkProp(yPos + playerHeight);
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