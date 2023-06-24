package com.hescha.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.GameFourZeroNineEight;

public class LoadingScreen extends ScreenAdapter {
    public static final AssetManager assetManager = new AssetManager();
    public static final float WORLD_WIDTH = 720;
    public static final float WORLD_HEIGHT = 1280;

    private GlyphLayout glyphLayout;
    private BitmapFont bitmapFont;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    public LoadingScreen() {
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        bitmapFont = new BitmapFont();

        assetManager.load("4x4.jpeg", Texture.class);
        assetManager.load("5x5.jpeg", Texture.class);
        assetManager.load("6x6.jpeg", Texture.class);
        assetManager.load("8x8.jpeg", Texture.class);
        assetManager.load("btnRestart.png", Texture.class);
        assetManager.load("btnHome.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        if (assetManager.update()) {
            System.out.println("Resources are uploaded");
            GameFourZeroNineEight.game.setScreen(new MainMenuScreen());
        } else {
            batch.setProjectionMatrix(camera.projection);
            batch.setTransformMatrix(camera.view);
            ScreenUtils.clear(Color.GRAY);
            batch.begin();
            glyphLayout.setText(bitmapFont, "Progress: " + (int) (assetManager.getProgress() * 100));
            bitmapFont.draw(batch, glyphLayout, (viewport.getWorldWidth() - glyphLayout.width) / 2, viewport.getWorldHeight() / 2);
            batch.end();
            System.out.println("Uploading progress: " + assetManager.getProgress() * 100);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
