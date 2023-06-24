package com.hescha.game.service;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hescha.game.model.Direction;
import com.hescha.game.model.Game4096;
import com.hescha.game.model.Tile;

import java.util.Random;

import static com.hescha.game.screen.LoadingScreen.WORLD_WIDTH;

public class Game4096Service {

    public Game4096 newGame(Stage stage, ShapeRenderer shapeRenderer, int dimension) {
        Game4096 game = new Game4096();
        game.setTiles(getStartTiles(stage, shapeRenderer, dimension));
        addRandomTile(game);
        addRandomTile(game);
        updateTileColor(game);
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
        updateTileColor(game4096);
    }

    private void updateTileColor(Game4096 game4096) {
        int length = game4096.getTiles().length;
        int width = (int) (WORLD_WIDTH / length);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Tile tile = game4096.getTiles()[i][j];
                tile.setColor(BlockColor.getColor(tile.getValue()));
                tile.setX(width * j);
                tile.setY(width * i);
            }
        }
    }

    private Tile[][] getStartTiles(Stage stage, ShapeRenderer shapeRenderer, int size) {
        int width = (int) (WORLD_WIDTH / size);
        Tile[][] tiles = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = new Tile();
                tile.setShapeRenderer(shapeRenderer);
                tile.setWidth(width);
                tile.setHeight(width);
                tile.setX(j * width);
                tile.setY(i * width);
                stage.addActor(tile);
                tiles[i][j] = tile;
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
