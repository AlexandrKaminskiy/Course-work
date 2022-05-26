package com.example.coursework.network;


import java.io.Serializable;

public class UserModel implements Serializable {
    private double xPos;
    private double yPos;

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
}
