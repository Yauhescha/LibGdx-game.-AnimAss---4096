package com.hescha.game.service;

import com.badlogic.gdx.graphics.Texture;
import com.hescha.game.screen.LoadingScreen;

import java.util.HashMap;
import java.util.Map;

public class BlackTexture {

    private static final Map<Integer, Texture> textures = new HashMap<>();

    static {
        textures.put(0, LoadingScreen.assetManager.get("black/0.jpg", Texture.class));
        textures.put(2, LoadingScreen.assetManager.get("black/1.jpg", Texture.class));
        textures.put(4,  LoadingScreen.assetManager.get("black/2.jpg", Texture.class));
        textures.put(8,  LoadingScreen.assetManager.get("black/3.jpg", Texture.class));
        textures.put(16, LoadingScreen.assetManager.get("black/4.jpg", Texture.class));
        textures.put(32,  LoadingScreen.assetManager.get("black/5.jpg", Texture.class));
        textures.put(64,  LoadingScreen.assetManager.get("black/6.jpg", Texture.class));
        textures.put(128, LoadingScreen.assetManager.get("black/7.jpg", Texture.class));
        textures.put(256,  LoadingScreen.assetManager.get("black/8.jpg", Texture.class));
        textures.put(512,  LoadingScreen.assetManager.get("black/9.jpg", Texture.class));
        textures.put(1024, LoadingScreen.assetManager.get("black/10.jpg", Texture.class));
        textures.put(2048,  LoadingScreen.assetManager.get("black/11.jpg", Texture.class));
        textures.put(4096,  LoadingScreen.assetManager.get("black/12.jpg", Texture.class));
        textures.put(8192,  LoadingScreen.assetManager.get("black/13.jpg", Texture.class));
        textures.put(16384,  LoadingScreen.assetManager.get("black/14.jpg", Texture.class));
        textures.put(32768,  LoadingScreen.assetManager.get("black/15.jpg", Texture.class));
        textures.put(65536,  LoadingScreen.assetManager.get("black/16.jpg", Texture.class));
        textures.put(131072,  LoadingScreen.assetManager.get("black/17.jpg", Texture.class));
        textures.put(262144,  LoadingScreen.assetManager.get("black/18.jpg", Texture.class));
        textures.put(524288,  LoadingScreen.assetManager.get("black/19.jpg", Texture.class));
        textures.put(1048576,  LoadingScreen.assetManager.get("black/20.jpg", Texture.class));
    }

    public static Texture getTexture(int value) {
        return textures.getOrDefault(value, textures.get(2));
    }
}

