package com.hescha.game.service;


import com.hescha.game.model.Direction;
import com.hescha.game.model.Game4096;
import com.hescha.game.model.Tile;

import java.util.Random;

public class Game4096Service {

    public Game4096 newGame(int dimension) {
        Game4096 game = new Game4096();
        game.setTiles(getStartTiles(dimension));
        addRandomTile(game);
        addRandomTile(game);
        return game;
    }


    public void moveTiles(Game4096 game4096, Direction direction) {
        boolean moved = false;
        for (int pass = 0; pass < game4096.getTiles().length; pass++) {
            for (int i = 0; i < game4096.getTiles().length; i++) {
                for (int j = 0; j < game4096.getTiles().length; j++) {
                    int x, y;
                    switch (direction) {
                        case LEFT:
                            x = j;
                            y = i;
                            break;
                        case RIGHT:
                            x = game4096.getTiles().length - j - 1;
                            y = i;
                            break;
                        case UP:
                            x = i;
                            y = j;
                            break;
                        case DOWN:
                            x = i;
                            y = game4096.getTiles().length - j - 1;
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + direction);
                    }
                    moved |= moveTile(game4096, x, y, direction);
                }
            }
        }

        if (moved) {
            addRandomTile(game4096);
        }
    }

    public boolean isGameOver(Game4096 game4096) {
        if (countFreeTiles(game4096) != 0) {
            return false;
        }
        Tile[][] tiles = game4096.getTiles();
        for (int y = 0; y < game4096.getTiles().length; y++) {
            for (int x = 0; x < game4096.getTiles().length; x++) {
                Tile tile = tiles[y][x];
                if ((x < game4096.getTiles().length - 1 && tile.getValue() == tiles[y][x + 1].getValue())
                        || (y < game4096.getTiles().length - 1 && tile.getValue() == tiles[y + 1][x].getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameWon(Game4096 game4096) {
        for (int y = 0; y < game4096.getTiles().length; y++) {
            for (int x = 0; x < game4096.getTiles().length; x++) {
                if (game4096.getTiles()[y][x].getValue() == 4096) {
                    return true;
                }
            }
        }
        return false;
    }

    private Tile[][] getStartTiles(int size) {
        Tile[][] tiles = new Tile[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                tiles[y][x] = new Tile();
            }
        }
        return tiles;
    }

    private void addRandomTile(Game4096 game4096) {
        int freeTiles = countFreeTiles(game4096);
        if (freeTiles == 0) {
            return;
        }

        Tile[][] tiles = game4096.getTiles();
        int position = new Random().nextInt(freeTiles);
        int currentPos = 0;

        for (int i = 0; i < game4096.getTiles().length; i++) {
            for (int j = 0; j < game4096.getTiles().length; j++) {
                if (tiles[i][j].isEmpty()) {
                    if (currentPos == position) {
                        tiles[i][j].setValue(Math.random() < 0.9 ? 2 : 4);
                        return;
                    }
                    currentPos++;
                }
            }
        }
    }

    private int countFreeTiles(Game4096 game4096) {
        int freeTiles = 0;
        for (int i = 0; i < game4096.getTiles().length; i++) {
            for (int j = 0; j < game4096.getTiles().length; j++) {
                if (game4096.getTiles()[i][j].isEmpty()) {
                    freeTiles++;
                }
            }
        }
        return freeTiles;
    }

    private boolean moveTile(Game4096 game4096, int x, int y, Direction direction) {
        Tile[][] tiles = game4096.getTiles();
        Tile tile = tiles[y][x];
        if (tile.isEmpty()) {
            return false;
        }

        int nextX = x + direction.getDx();
        int nextY = y + direction.getDy();

        if (nextX < 0 || nextX >= game4096.getTiles().length || nextY < 0 || nextY >= game4096.getTiles().length) {
            return false;
        }

        Tile nextTile = tiles[nextY][nextX];
        if (nextTile.isEmpty()) {
            nextTile.setValue(tile.getValue());
            tile.setValue(0);
            return true;
        }

        if (tile.getValue() == nextTile.getValue()) {
            nextTile.setValue(nextTile.getValue() * 2);
            game4096.setScore(game4096.getScore() + nextTile.getValue());
            tile.setValue(0);
            return true;
        }

        return false;
    }
}
