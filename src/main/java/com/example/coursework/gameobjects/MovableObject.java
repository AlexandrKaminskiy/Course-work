package com.example.coursework.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MovableObject implements Serializable {
    public List<ImmovableObject> immovableObjects = new ArrayList<>(){{
        add(new ImmovableObject(0, 600,400,450));
        add(new ImmovableObject(100, 130,370,380));
        add(new ImmovableObject(140, 150,380,400));
    }};
    protected final byte FROM_LEFT = 0;
    protected final byte FROM_TOP = 1;
    protected final byte FROM_RIGHT = 2;
    protected final byte FROM_BOT = 3;

    protected final double g = 9.78;
    protected boolean hasProp;
    protected final double dt = 0.15;
    protected final double playerHeight = 15;
    protected final double playerWidth = 10;
    protected final double EXP = 0.0001;
    public double xPos;
    public double yPos;
    protected double ySpeed;
    protected double xSpeed;


    public MovableObject() {
        gravityImpact();
    }
    protected void gravityImpact() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                double currentY = yPos;
                if (!hasProp) {
                    System.out.println(ySpeed);
                    ySpeed -= g * dt;
                    yPos -= ySpeed * dt;

                    for (var immObj : immovableObjects) {
                        if ((immObj.getX1() < xPos && immObj.getX2() > xPos) ||
                                (immObj.getX1() < xPos + playerWidth && immObj.getX2() > xPos + playerWidth)) {
                            double res = collisionCoord(FROM_BOT, currentY, yPos, immObj);
                            if (Math.abs(res + 100) > EXP) {
                                yPos = immObj.getY2() + 0.2;//kostyl
                                ySpeed = 0;
                                break;
                            }
                        }
                    }
                    checkProp(currentY);
                }

            }
        },0,10);

    }
    private void checkProp(double currentY) {
        for (var immObj : immovableObjects) {
            if ((immObj.getX1() < xPos && immObj.getX2() > xPos) ||
                    (immObj.getX1() < xPos + playerWidth && immObj.getX2() > xPos + playerWidth)) {

                double res = collisionCoord(FROM_TOP, currentY + playerHeight, yPos + playerHeight, immObj);
                if (Math.abs(res + 100) > EXP) {
                    yPos = immObj.getY1() - playerHeight;
                    hasProp = true;
                    System.out.println("есть опора");
                    return;
                }
            }
        }
        hasProp = false;
        System.out.println("нет опоры");
    }
    protected double collisionCoord(int side, double curCrd, double futCrd, ImmovableObject immObj) {
        double c = 0;
        switch (side) {
            case 0 -> c = immObj.getX1();
            case 1 -> c = immObj.getY1();
            case 2 -> c = immObj.getX2();
            case 3 -> c = immObj.getY2();
        }

        double a = c - curCrd;
        double b = c - futCrd;
        if (a * b < 0 || Math.abs(a * b) < EXP) {
            return c;
        }
        return -100;
    }
}
