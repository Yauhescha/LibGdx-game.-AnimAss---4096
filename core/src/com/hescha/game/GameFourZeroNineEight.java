package com.hescha.game;

import com.badlogic.gdx.Game;
import com.hescha.game.screen.LoadingScreen;

public class GameFourZeroNineEight extends Game {
    public static Game game;
    @Override
    public void create() {
        game = this;
        setScreen(new LoadingScreen());
    }
}
