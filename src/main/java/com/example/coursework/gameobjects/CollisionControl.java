package com.example.coursework.gameobjects;

import com.example.coursework.dto.PlayerDto;

public class CollisionControl {
    private PlayerDto opponent;
    private double toOpponent;
    private double toImmObj;
    public CollisionControl(PlayerDto opponent) {

        this.opponent = opponent;
    }

    public synchronized CollisionsState checkCollision(double futX, double futY, double xPos, double yPos) {

        for (var immObj : MovableObject.immovableObjects) {
            double x1 = immObj.getX1();
            double x2 = immObj.getX2();
            double y1 = immObj.getY1();
            double y2 = immObj.getY2();
            toImmObj = checkIntersection(xPos,yPos,futX,futY,x1,y1,x2,y2);
            if (toImmObj != -1) break;
        }
        toOpponent = checkIntersection(xPos,yPos,futX,futY,opponent.xPos,opponent.yPos,opponent.xPos + MovableObject.playerWidth,opponent.yPos + MovableObject.playerHeight);
        if (toOpponent != -1) {
            if (toImmObj == -1) {
                return CollisionsState.WITH_PLAYER;
            }
            if (toOpponent < toImmObj && toImmObj != -1) {
                return CollisionsState.WITH_PLAYER;
            }
        }
        if (toImmObj != -1) {
            return CollisionsState.WITH_IMM;
        }
        return CollisionsState.NONE;
    }

    private synchronized double checkIntersection(double xPos, double yPos, double futX, double futY, double x1, double y1, double x2, double y2) {
        if (lineIntersect(xPos,yPos,futX,futY,x1,y1,x2,y1)) {
            double dy = Math.abs(yPos - y1);
            double dx = dy * Math.abs(xPos - futX) / Math.abs(yPos - futY);
            double dc = Math.sqrt(dx * dx + dy * dy);
            return dc;
        }
        if (lineIntersect(xPos,yPos,futX,futY,x2,y1,x2,y2)){
            double dx = Math.abs(xPos - x2);
            double dy = dx / Math.abs(xPos - futX) / Math.abs(yPos - futY);
            double dc = Math.sqrt(dx * dx + dy * dy);
            return dc;
        }
        if (lineIntersect(xPos,yPos,futX,futY,x2,y2,x1,y2)) {
            double dy = Math.abs(yPos - y2);
            double dx = dy * Math.abs(xPos - futX) / Math.abs(yPos - futY);
            double dc = Math.sqrt(dx * dx + dy * dy);
            return dc;
        }
        if (lineIntersect(xPos,yPos,futX,futY,x1,y2,x1,y1)) {
            double dx = Math.abs(xPos - x1);
            double dy = dx / Math.abs(xPos - futX) / Math.abs(yPos - futY);
            double dc = Math.sqrt(dx * dx + dy * dy);
            System.out.println(dc);
            return dc;
        }
        return -1;
    }
    public synchronized boolean checkPlayerGetDamaged(double xPos,double yPos) {

        for (var bullet : opponent.bullets) {
            for (var immObj : MovableObject.immovableObjects) {
                double x1 = immObj.getX1();
                double x2 = immObj.getX2();
                double y1 = immObj.getY1();
                double y2 = immObj.getY2();
                toImmObj = checkIntersection(bullet.xPos, bullet.yPos, bullet.futX, bullet.futY, x1, y1, x2, y2);
                if (toImmObj != -1) break;
            }

            double x1 = xPos;
            double x2 = xPos + MovableObject.playerWidth;
            double y1 = yPos;
            double y2 = yPos + MovableObject.playerHeight;
            double toPlayer = checkIntersection(bullet.xPos, bullet.yPos, bullet.futX, bullet.futY, x1, y1, x2, y2);

            if (toPlayer != -1) {
                if (toImmObj == -1) {
                    return true;
                }
                if (toPlayer < toImmObj && toImmObj != -1) {
                    return true;
                }
            }
            if (toImmObj != -1) {
                return false;
            }
        }
        return false;
    }

    private synchronized boolean lineIntersect(double ax1, double ay1, double ax2, double ay2,
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

    private synchronized double vectorMultiplication(double ax, double ay, double bx, double by) {
        return ax * by - ay * bx;
    }
}
