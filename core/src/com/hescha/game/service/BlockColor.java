package com.hescha.game.service;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class BlockColor {

    private static final Map<Integer, Color> colors = new HashMap<>();

    static {
        colors.put(2, new Color(0.933f, 0.913f, 0.851f, 1));     // #EDE8D9
        colors.put(4, new Color(0.922f, 0.890f, 0.788f, 1));     // #ECE3C9
        colors.put(8, new Color(0.953f, 0.753f, 0.490f, 1));     // #F3C07D
        colors.put(16, new Color(0.953f, 0.580f, 0.396f, 1));    // #F39465
        colors.put(32, new Color(0.961f, 0.486f, 0.376f, 1));    // #F57C60
        colors.put(64, new Color(0.965f, 0.345f, 0.231f, 1));    // #F6583B
        colors.put(128, new Color(0.929f, 0.898f, 0.710f, 1));   // #EDE4B5
        colors.put(256, new Color(0.922f, 0.890f, 0.620f, 1));   // #ECE39F
        colors.put(512, new Color(0.918f, 0.886f, 0.525f, 1));   // #ECE286
        colors.put(1024, new Color(0.914f, 0.882f, 0.431f, 1));  // #E8E16E
        colors.put(2048, new Color(0.910f, 0.878f, 0.337f, 1));  // #E8E156
    }

    public static Color getColor(int value) {
        return colors.getOrDefault(value, new Color(0.8f, 0.8f, 0.8f, 1));  // серый цвет по умолчанию
    }
}

