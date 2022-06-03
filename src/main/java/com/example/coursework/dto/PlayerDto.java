package com.example.coursework.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerDto implements Serializable {
    public double xPos;
    public double yPos;
    public List<BulletDto> bullets = new ArrayList<>();
    public int hp;
    public int score;
}