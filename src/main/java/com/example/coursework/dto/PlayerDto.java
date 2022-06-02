package com.example.coursework.dto;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerDto {
    public double xPos;
    public double yPos;
    public List<BulletDto> bullets = Collections.synchronizedList(new ArrayList<>());
    public int hp;
    public int score;
    public List<BulletDto> getBullets() {
        return bullets;
    }

    public static void main(String[] args) {
        
    }
    public PlayerDto byteArrayToObject(byte[] arr) {
        PlayerDto player = new PlayerDto();
        player.xPos = toDouble(Arrays.copyOfRange(arr, 0,8));
        player.yPos = toDouble(Arrays.copyOfRange(arr, 8,16));
        int counter = 16;
        while (counter < arr.length - 8 && (arr[counter + 8] != 0 && arr[counter + 9] != 0)) {
            player.bullets.add(new BulletDto(toDouble(Arrays.copyOfRange(arr, counter, counter + 8)),
                    toDouble(Arrays.copyOfRange(arr, counter + 8, counter + 16)),
                    toDouble(Arrays.copyOfRange(arr, counter + 16, counter + 24)),
                    toDouble(Arrays.copyOfRange(arr, counter + 24, counter + 32))));
            counter += 32;
        }

        player.hp = toInt(Arrays.copyOfRange(arr, counter, counter + 4));
        player.score = toInt(Arrays.copyOfRange(arr, counter + 4, counter + 8));
        return player;
    }

    private double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    private int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

}
