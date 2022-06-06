package com.example.coursework.dto;

import com.example.coursework.gameobjects.Player;

import java.util.ArrayList;

public class PlayerMapper {
    public PlayerDto toPlayerDto(Player player) {
        PlayerDto playerDto = new PlayerDto();
        playerDto.xPos = player.xPos;
        playerDto.yPos = player.yPos;
        playerDto.score = player.score;
        playerDto.hp = player.HP;
        playerDto.bullets = new ArrayList<>();
        for (var bullet : player.bullets) {
            playerDto.bullets.add(new BulletDto(bullet.xPos, bullet.yPos, bullet.futX, bullet.futY, bullet.isHit));
        }
        return playerDto;
    }
}
