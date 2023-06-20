package com.hescha.game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tile {
    private int value = 0;

    public boolean isEmpty() {
        return value == 0;
    }
}

