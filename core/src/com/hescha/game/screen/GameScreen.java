package com.hescha.game.screen;

import static com.hescha.game.screen.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.screen.LoadingScreen.WORLD_WIDTH;
import static com.hescha.game.screen.LoadingScreen.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.GameFourZeroNineEight;
import com.hescha.game.MyGestureListener;
import com.hescha.game.model.Game4096;
import com.hescha.game.service.Game4096Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameScreen extends ScreenAdapter {
    public static final String SCORE = "Score\r\n";
    public static final String BEST = "Best\r\n";
    public static final Color COLOR = new Color(251f / 255f, 208f / 255f, 153f / 255f, 1);
    private final int dimension;
    private Viewport viewport;
    private OrthographicCamera camera;

    private SpriteBatch batch;

    private GlyphLayout glyphLayout;
    private BitmapFont bitmapFont;

    private Game4096Service game4096Service;
    private Game4096 game4096;
    private Stage stage;
    private Stage stage2;
    private int bestScore;

    @Override
    public void show() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("angrybirds-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        bitmapFont.setColor(Color.BLACK);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        stage = new Stage(viewport);
        stage2 = new Stage(viewport);

        game4096Service = new Game4096Service();
        game4096 = game4096Service.newGame(stage2, shapeRenderer, dimension);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(5f);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;

        initButtons();

        InputProcessor myInputProcessor1 = new GestureDetector(new MyGestureListener());
        InputProcessor myInputProcessor2 = stage;

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(myInputProcessor1);
        multiplexer.addProcessor(myInputProcessor2);
        Gdx.input.setInputProcessor(multiplexer);

        bestScore = Gdx.app.getPreferences("2048-dimension-" + dimension).getInteger("score");
    }

    private void initButtons() {
        Image btnHome = new Image(assetManager.get("btnHome.png", Texture.class));
        btnHome.setPosition(btnHome.getWidth() / 2,
                WORLD_HEIGHT - WORLD_WIDTH + btnHome.getHeight() * 2 + 6 * dimension);
        btnHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                GameFourZeroNineEight.game.setScreen(new MainMenuScreen());
            }
        });
        stage.addActor(btnHome);


        Image btnRestart = new Image(assetManager.get("btnRestart.png", Texture.class));
        btnRestart.setPosition(WORLD_WIDTH - btnHome.getWidth() * 1.5f,
                WORLD_HEIGHT - WORLD_WIDTH + btnHome.getHeight() * 2 + 6 * dimension);
        btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                GameFourZeroNineEight.game.setScreen(new GameScreen(dimension));
            }
        });
        stage.addActor(btnRestart);
    }

    @Override
    public void render(float delta) {
        update();
        ScreenUtils.clear(COLOR);
        stage.draw();
        stage2.draw();
        draw();
    }

    private void update() {
        if (MyGestureListener.move != null) {
            game4096Service.moveTiles(game4096, MyGestureListener.move);
            MyGestureListener.move = null;
            saveScore(game4096.getScore());
        }
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        drawTableNumbers();
        drawScore();
        batch.end();
    }

    private void drawScore() {
        bitmapFont.getData().setScale(4f);

        glyphLayout.setText(bitmapFont, SCORE + game4096.getScore());
        bitmapFont.draw(batch, glyphLayout,
                WORLD_WIDTH - glyphLayout.width - 30,
                WORLD_HEIGHT - glyphLayout.height);

        glyphLayout.setText(bitmapFont, BEST + bestScore);
        bitmapFont.draw(batch, glyphLayout,
                WORLD_WIDTH - glyphLayout.width - 350,
                WORLD_HEIGHT - glyphLayout.height);
    }

    private void drawTableNumbers() {
        float cellSize = WORLD_WIDTH / dimension - 6;
        // Отображение таблицы массива
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int scaleXY = 10;
                bitmapFont.getData().setScale(scaleXY);
                float x = j * (cellSize + 3) + 10;
                float y = i * (cellSize + 3) + 10;
                if (game4096.getTiles()[i][j].getValue() != 0) {
                    glyphLayout.setText(bitmapFont, game4096.getTiles()[i][j].getValue() + "");
                    while (glyphLayout.width > cellSize || glyphLayout.height > cellSize) {
                        scaleXY--;
                        bitmapFont.getData().setScale(scaleXY);
                        glyphLayout.setText(bitmapFont, game4096.getTiles()[i][j].getValue() + "");
                    }
                    bitmapFont.draw(batch, glyphLayout, x + cellSize / 2 - glyphLayout.width / 2,
                            y + cellSize / 2 + glyphLayout.height / 2);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void saveScore(int score) {
        if (score < bestScore) {
            return;
        }
        bestScore = score;
        Preferences prefs = Gdx.app.getPreferences("2048-dimension-" + dimension);
        prefs.putInteger("score", bestScore);
        prefs.flush();
    }
}

