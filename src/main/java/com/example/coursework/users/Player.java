package com.example.coursework.users;

import java.io.Serializable;

public class UserModel implements Serializable {
    public double xPos;
    public double yPos;


    public UserModel(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }


}
