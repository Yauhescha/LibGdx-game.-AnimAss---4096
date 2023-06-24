package com.hescha.game.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hescha.game.service.BlockColor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tile extends Actor {
    private static final int PADDING = 5;
    private ShapeRenderer shapeRenderer;
    private int value = 0;
    private Color color = BlockColor.getColor(-1);

    public boolean isEmpty() {
        return value == 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(getX() + PADDING, getY() + PADDING*2,
                getWidth() - PADDING, getHeight() - PADDING*2);
        shapeRenderer.end();
    }
}


