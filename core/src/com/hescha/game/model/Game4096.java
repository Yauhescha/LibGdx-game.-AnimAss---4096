package com.hescha.game.model;

import lombok.Data;
import lombok.Getter;

@Data
public class Game4096 {
    @Getter
    private int score = 0;
    private Tile[][] tiles;
    private int level;
}


