package com.hescha.game;

import static com.hescha.game.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.LoadingScreen.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.model.Game4096;
import com.hescha.game.service.BlockColor;
import com.hescha.game.service.Game4096Service;

public class GameScreen extends ScreenAdapter {
    private Viewport viewport;
    private OrthographicCamera camera;

    private SpriteBatch batch;

    private TextureRegion[][] cellTextures;

    private GlyphLayout glyphLayout;
    private BitmapFont bitmapFont;

    private String currentAction = "";

    private Game4096Service game4096Service;
    private Game4096 game4096;
    TextButton exitButton;

    private Stage stage;
    @Override
    public void show() {


        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.BLACK);
        bitmapFont.getData().setScale(5f);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        cellTextures = new TextureRegion[4][4];
        // Создание текстур для каждого значения в массиве

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellTextures[i][j] = createColoredTexture(getColorForValue(i));
            }
        }
        game4096Service = new Game4096Service();
        game4096 = game4096Service.newGame();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(5f);

        stage = new Stage(viewport);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        exitButton = new TextButton("BACK", textButtonStyle);
        exitButton.setPosition(0, WORLD_HEIGHT - exitButton.getHeight());

        stage.addActor(exitButton);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                GameFourZeroNineEight.game.setScreen(new MainMenuScreen());
            }
        });

        InputProcessor myInputProcessor1 = new GestureDetector(new MyGestureListener());
        InputProcessor myInputProcessor2 = stage;

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(myInputProcessor1);
        multiplexer.addProcessor(myInputProcessor2);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private TextureRegion createColoredTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        return new TextureRegion(texture);
    }

    private Color getColorForValue(int value) {
        return BlockColor.getColor(value);
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }

    private void update() {
        if (MyGestureListener.move != null) {
            game4096Service.moveTiles(game4096, MyGestureListener.move);
            MyGestureListener.move = null;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellTextures[i][j].getTexture().dispose();
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pixmap.setColor(BlockColor.getColor(game4096.getTiles()[i][j].getValue()));
                pixmap.fill();
                Texture texture = new Texture(pixmap);
                cellTextures[i][j] = new TextureRegion(texture);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.GRAY);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();


        float cellSize = WORLD_WIDTH / 4f;

        // Отображение таблицы массива
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                TextureRegion cellTexture = cellTextures[i][j];
                float x = j * cellSize;
                float y = i * cellSize;

                batch.draw(cellTexture, x, y, cellSize, cellSize);
                if (game4096.getTiles()[i][j].getValue() != 0) {
                    glyphLayout.setText(bitmapFont, game4096.getTiles()[i][j].getValue() + "");
                    bitmapFont.draw(batch, glyphLayout, x + cellSize / 2 - glyphLayout.width / 2, y + cellSize / 2 + glyphLayout.height / 2);
                }
            }
        }
        if (MyGestureListener.move != null) {
            currentAction = MyGestureListener.move.name();
        }
        glyphLayout.setText(bitmapFont, currentAction);
        bitmapFont.draw(batch, glyphLayout, 100, WORLD_HEIGHT - 100);

        glyphLayout.setText(bitmapFont, "Score: " + game4096.getScore());
        bitmapFont.draw(batch, glyphLayout,
                WORLD_WIDTH - glyphLayout.width - 100,
                WORLD_HEIGHT - glyphLayout.height - 100);


        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cellTextures[i][j].getTexture().dispose();
            }
        }
    }
}
