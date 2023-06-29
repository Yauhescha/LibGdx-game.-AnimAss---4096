package com.hescha.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.hescha.game.screen.LevelType;
import com.hescha.game.screen.LoadingScreen;

public class AssTexture {

    public static Texture getTexture(LevelType levelType, int value) {
        if (value == 0)
            return LoadingScreen.assetManager.get("0.jpg", Texture.class);
        return LoadingScreen.assetManager.get(levelType.name().toLowerCase() + "/" + getLog2Value(value) + ".jpg", Texture.class);
    }

    private static int getLog2Value(int value) {
        return (int) (Math.log(value) / Math.log(2));
    }
}

