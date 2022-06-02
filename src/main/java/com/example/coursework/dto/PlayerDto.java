package com.example.coursework.dto;

import com.example.coursework.gameobjects.Bullet;
import com.example.coursework.gameobjects.Player;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerDto {
    public double xPos;
    public double yPos;
    private List<BulletDto> bullets = Collections.synchronizedList(new ArrayList<>());
    int hp;

    public List<BulletDto> getBullets() {
        return bullets;
    }

    public PlayerDto byteArrayToObject(byte[] arr) {
        PlayerDto player = new PlayerDto();
        player.xPos = toDouble(Arrays.copyOfRange(arr, 0,8));
        player.yPos = toDouble(Arrays.copyOfRange(arr, 8,16));
        int counter = 16;
        while (counter < arr.length - 4 && (arr[counter + 4] != 0 && arr[counter + 5] != 0)) {
            player.bullets.add(new BulletDto(toDouble(Arrays.copyOfRange(arr, counter, counter + 8)),
                    toDouble(Arrays.copyOfRange(arr, counter + 8, counter + 16))));
            counter += 16;
        }
        player.hp = toInt(Arrays.copyOfRange(arr, counter, counter + 4));
        return player;
    }

    private double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    private int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

}
