package com.hescha.game.screen;

import static com.hescha.game.screen.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.screen.LoadingScreen.WORLD_WIDTH;
import static com.hescha.game.screen.LoadingScreen.assetManager;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.GameFourZeroNineEight;

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


        Texture x4x4 = assetManager.get("4x4.png");
        Texture x5x5 = assetManager.get("5x5.png");
        Texture x6x6 = assetManager.get("6x6.png");
        Texture x8x8 = assetManager.get("8x8.png");
        Texture settings = assetManager.get("settings.png");
        Image b4x4 = new Image(x4x4);
        Image b5x5 = new Image(x5x5);
        Image b6x6 = new Image(x6x6);
        Image b8x8 = new Image(x8x8);
        Image menu = new Image(settings);

        int padding = 10;
        float newWidth = Gdx.graphics.getWidth()/3;
        float newHeight =newWidth;

        b4x4.setSize(newWidth, newHeight);
        b5x5.setSize(newWidth, newHeight);
        b6x6.setSize(newWidth, newHeight);
        b8x8.setSize(newWidth, newHeight);
        menu.setSize(newWidth, newHeight);

        b4x4.setPosition((float) (centerX - newWidth - padding), centerY - newHeight - padding);
        b5x5.setPosition((float) (centerX  + padding), centerY - newHeight - padding);
        b6x6.setPosition((float) (centerX - newWidth - padding), centerY  + padding);
        b8x8.setPosition((float) (centerX  + padding), centerY  + padding);

        menu.setPosition((float) (centerX - newWidth/2), centerY - newHeight*2 - padding);

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
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameFourZeroNineEight.game.setScreen(new SettingScreen());
            }
        });

        stage.addActor(b4x4);
        stage.addActor(b5x5);
        stage.addActor(b6x6);
        stage.addActor(b8x8);
        stage.addActor(menu);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(new Color(251f / 255f, 208f / 255f, 153f / 255f, 1));
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
