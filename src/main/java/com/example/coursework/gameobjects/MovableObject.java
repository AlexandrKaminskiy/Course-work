package com.example.coursework.gameobjects;

import com.example.coursework.dto.PlayerDto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MovableObject implements Serializable {
    public static final List<ImmovableObject> immovableObjects = new ArrayList<>(){{
        add(new ImmovableObject(-100, 700,400,600));
        add(new ImmovableObject(-100, 0,0,600));
        add(new ImmovableObject(600, 700,0,400));
        add(new ImmovableObject(0, 20,280,310));
        add(new ImmovableObject(0, 40,350,400));
        add(new ImmovableObject(0, 60,180,200));
        add(new ImmovableObject(55, 75,240,260));
        add(new ImmovableObject(75, 200,180,260));
        add(new ImmovableObject(75, 120,120,180));
        add(new ImmovableObject(120, 160,360,400));
        add(new ImmovableObject(250, 300,80,110));
        add(new ImmovableObject(280, 380,190,220));
        add(new ImmovableObject(350, 380,80,110));
        add(new ImmovableObject(200, 280,240,280));
        add(new ImmovableObject(260, 340,350,400));
        add(new ImmovableObject(400, 460,360,400));
        add(new ImmovableObject(450, 550,245,320));
        add(new ImmovableObject(580, 600,210,240));
        add(new ImmovableObject(530, 550,140,170));
        add(new ImmovableObject(400, 520,130,190));
        add(new ImmovableObject(350, 410,300,320));
    }};

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    protected final transient byte FROM_LEFT = 0;
    protected final transient byte FROM_TOP = 1;
    protected final transient byte FROM_RIGHT = 2;
    protected final transient byte FROM_BOT = 3;

    protected final transient double g = 9.78;
    public transient boolean hasProp;
    protected final transient double dt = 0.15;
    public static final transient double playerHeight = 15;
    public static final transient double playerWidth = 10;
    protected final transient double EXP = 0.0001;
    public transient double xPos;
    public transient double yPos;
    protected transient double ySpeed;
    protected transient double xSpeed;
    protected transient CollisionControl collisionControl;
    protected transient PlayerDto opponent;
    public MovableObject(PlayerDto opponent) {
        this.opponent = opponent;
        collisionControl = new CollisionControl(opponent);

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
                    var imm = checkProp(currentY);
//                    if (imm != null && imm.equals(immovableObjects.get(immovableObjects.size() - 1))){
//                        new Thread(()->{
//                            while (!hasProp) {
//                                checkProp(currentY);
//                            }
//                        }).start();
//                    }
                }
            }
        },0,10);
    }

    protected synchronized ImmovableObject checkProp(double currentY) {
        for (var immObj : immovableObjects) {
            if (overProp(immObj)) {
                double res = collisionCoord(FROM_TOP, currentY + playerHeight - 0.2, yPos + playerHeight, immObj);
                if (Math.abs(res + 100) > EXP) {
                    yPos = immObj.getY1() - playerHeight;
                    hasProp = true;
                    return immObj;
                }
            }
        }
        hasProp = false;
        return null;
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
            case 0: c = immObj.getX1();break;
            case 1: c = immObj.getY1();break;
            case 2: c = immObj.getX2();break;
            case 3: c = immObj.getY2();break;
        }

        double a = c - curCrd;
        double b = c - futCrd;
        if (a * b < 0 || Math.abs(a * b) < EXP) {
            return c;
        }
        return -100;
    }
}
