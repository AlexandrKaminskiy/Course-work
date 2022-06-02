package com.example.coursework.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MovableObject implements Serializable {
    public List<ImmovableObject> immovableObjects = new ArrayList<>(){{
        add(new ImmovableObject(-100, 700,400,600));
        add(new ImmovableObject(-100, 0,0,600));
        add(new ImmovableObject(600, 700,0,400));
        add(new ImmovableObject(100, 140,370,380));
        add(new ImmovableObject(140, 150,380,400));
        add(new ImmovableObject(340, 380,350,370));
        add(new ImmovableObject(300, 330,300,320));
        add(new ImmovableObject(400, 410,385,400));
    }};
    protected final transient byte FROM_LEFT = 0;
    protected final transient byte FROM_TOP = 1;
    protected final transient byte FROM_RIGHT = 2;
    protected final transient byte FROM_BOT = 3;

    protected final transient double g = 9.78;
    protected transient boolean hasProp;
    protected final transient double dt = 0.15;
    protected final transient double playerHeight = 15;
    protected final transient double playerWidth = 10;
    protected final transient double EXP = 0.0001;
    public double xPos;
    public double yPos;
    protected transient double ySpeed;
    protected transient double xSpeed;


    public MovableObject() {
        hasProp = false;
        gravityImpact();
    }
    protected void gravityImpact() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                double currentY = yPos;
                if (!hasProp) {
                    System.out.println(yPos);
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

    protected void checkProp(double currentY) {
        for (var immObj : immovableObjects) {
            if (overProp(immObj)) {
                double res = collisionCoord(FROM_TOP, currentY + playerHeight - 0.2, yPos + playerHeight, immObj);
                if (Math.abs(res + 100) > EXP) {
                    yPos = immObj.getY1() - playerHeight;
                    hasProp = true;
                    return;
                }
            }
        }
        hasProp = false;

    }

    private boolean overProp(ImmovableObject immObj) {
        if (immObj.getX1() < xPos && immObj.getX2() > xPos) return true;
        if (immObj.getX1() < xPos + playerWidth && immObj.getX2() > xPos + playerWidth) return true;

        if (immObj.getX1() > xPos && immObj.getX1() < xPos + playerWidth) return true;
        if (immObj.getX2() > xPos && immObj.getX2() < xPos + playerWidth) return true;

        if (immObj.getX1() == xPos && immObj.getX2() == xPos + playerWidth) return true;
        return false;
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
