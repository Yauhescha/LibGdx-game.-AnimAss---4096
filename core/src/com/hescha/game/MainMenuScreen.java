package com.hescha.game;

import static com.hescha.game.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.LoadingScreen.WORLD_WIDTH;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends ScreenAdapter {
    private Viewport viewport;
    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;

    @Override
    public void show() {
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        float centerX = Gdx.graphics.getDisplayMode().width / 2;
        float centerY = Gdx.graphics.getDisplayMode().height / 2;
        camera.position.set(centerX, centerY, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        batch = new SpriteBatch();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);


        Texture x4x4 = new Texture(Gdx.files.internal("4x4.jpeg"));
        Texture x5x5 = new Texture(Gdx.files.internal("5x5.jpeg"));
        Texture x6x6 = new Texture(Gdx.files.internal("6x6.jpeg"));
        Texture x8x8 = new Texture(Gdx.files.internal("8x8.jpeg"));
        Image b4x4 = new Image(x4x4);
        Image b5x5 = new Image(x5x5);
        Image b6x6 = new Image(x6x6);
        Image b8x8 = new Image(x8x8);

        int padding = 10;
        float imageWidth = (int) b4x4.getWidth();
        float imageHeight = (int) b4x4.getHeight();
        b4x4.setPosition((float) (centerX - imageWidth * 1.5 - padding), centerY - imageHeight - padding);
        b5x5.setPosition((float) (centerX + imageWidth * 0.5 + padding), centerY - imageHeight - padding);
        b6x6.setPosition((float) (centerX - imageWidth * 1.5 - padding), centerY + imageHeight + padding);
        b8x8.setPosition((float) (centerX + imageWidth * 0.5 + padding), centerY + imageHeight + padding);
        b4x4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameFourZeroNineEight.game.setScreen(new GameScreen(4));
            }
        });
        b5x5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameFourZeroNineEight.game.setScreen(new GameScreen(5));
            }
        });
        b6x6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameFourZeroNineEight.game.setScreen(new GameScreen(6));
            }
        });
        b8x8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameFourZeroNineEight.game.setScreen(new GameScreen(8));
            }
        });

        stage.addActor(b4x4);
        stage.addActor(b5x5);
        stage.addActor(b6x6);
        stage.addActor(b8x8);

    }

    @Override
    public void render(float delta) {
        Color color = new Color(238, 228, 219, 1);
        ScreenUtils.clear(color);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
