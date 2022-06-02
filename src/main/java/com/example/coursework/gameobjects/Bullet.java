package com.example.coursework.gameobjects;

import java.util.Timer;
import java.util.TimerTask;

public class Bullet extends MovableObject {
    private final double speed = 60;
    private final double maxYSpeed = -60;
    private boolean isPresent;
    private boolean nextIterDelete;
    private double mouseX;
    private double mouseY;
    private Player player;
    public boolean isPresent() {
        return isPresent;
    }


    public Bullet(double x1, double y1, double x2, double y2,Player player){
        this.player = player;
        this.isPresent = true;
        this.xPos = x1;
        this.yPos = y1;
        this.mouseX = x2;
        this.mouseY = y2;
        double c = Math.sqrt((x2 - x1) * (x2 - x1) + (y1 - y2) * (y1 - y2));
//        System.out.println(c);
        xSpeed = speed * ((x2 - x1) / c);
        ySpeed = speed * ((y1 - y2) / c);

//        System.out.println(xSpeed + " " + ySpeed);

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
                double futX = xPos + xSpeed * dt;
                double futY = yPos - ySpeed * dt;
                if (nextIterDelete) isPresent = false;
                nextIterDelete = checkCollision(futX, futY);

            }
        },0, 10);
    }
    private boolean checkCollision(double futX, double futY) {

        for (var immObj : immovableObjects) {

            double x1 = immObj.getX1();
            double x2 = immObj.getX2();
            double y1 = immObj.getY1();
            double y2 = immObj.getY2();

            if (xPos > x1 && xPos < x2 && yPos > y1 && yPos < y2) return true;

            if (lineIntersect(xPos,yPos,futX,futY,x1,y1,x2,y1)) {
                double dy = Math.abs(yPos - y1);
                double dx = dy * Math.abs(xPos - futX) / Math.abs(yPos - futY);
                double dc = Math.sqrt(dx * dx + dy * dy);
                return true;
            }
            if (lineIntersect(xPos,yPos,futX,futY,x2,y1,x2,y2)){
                double dx = Math.abs(xPos - x2);
                double dy = dx / Math.abs(xPos - futX) / Math.abs(yPos - futY);
                double dc = Math.sqrt(dx * dx + dy * dy);
                return true;
            }
            if (lineIntersect(xPos,yPos,futX,futY,x2,y2,x1,y2)) {
                double dy = Math.abs(yPos - y2);
                double dx = dy * Math.abs(xPos - futX) / Math.abs(yPos - futY);
                double dc = Math.sqrt(dx * dx + dy * dy);
                return true;
            }
            if (lineIntersect(xPos,yPos,futX,futY,x1,y2,x1,y1)) {
                double dx = Math.abs(xPos - x1);
                double dy = dx / Math.abs(xPos - futX) / Math.abs(yPos - futY);
                double dc = Math.sqrt(dx * dx + dy * dy);
                return true;
            }

        }

        return false;
    }

    public static void main(String[] args) {
        new Bullet();
    }
    Bullet() {
        System.out.println(lineIntersect( 3,6,6,1,4,4,8,9));
    }
    private boolean lineIntersect(double ax1, double ay1, double ax2, double ay2,
                                  double bx1, double by1, double bx2, double by2) {
        double abx11 = ax1 - bx1;
        double aax12 = ax1 - ax2;
        double abx12 = ax1 - bx2;

        double aby11 = ay1 - by1;
        double aay12 = ay1 - ay2;
        double aby12 = ay1 - by2;

        double v1 = vectorMultiplication(aax12,aay12,abx11,aby11);
        double v2 = vectorMultiplication(aax12,aay12,abx12,aby12);
        if (v1 * v2 > 0) return false;

        double bax11 = bx1 - ax1;
        double bbx12 = bx1 - bx2;
        double bax12 = bx1 - ax2;

        double bay11 = by1 - ay1;
        double bby12 = by1 - by2;
        double bay12 = by1 - ay2;
        v1 = vectorMultiplication(bbx12,bby12,bax11,bay11);
        v2 = vectorMultiplication(bbx12,bby12,bax12,bay12);
        if (v1 * v2 > 0) return false;

        return true;
    }

    private double vectorMultiplication(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }
}
