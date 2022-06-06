package com.example.coursework.dto;

import java.io.Serializable;

public class BulletDto implements Serializable {
    public double xPos;
    public double yPos;
    public double futX;
    public double futY;
    public boolean isHit;
    public BulletDto(double xPos, double yPos, double futX, double futY, boolean isHit) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.futX = futX;
        this.futY = futY;
        this.isHit = isHit;
    }
}
