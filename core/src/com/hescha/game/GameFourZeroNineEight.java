package com.hescha.game;

import com.badlogic.gdx.Game;

public class GameFourZeroNineEight extends Game {

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}
