package com.hescha.game;

import static com.hescha.game.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.LoadingScreen.WORLD_WIDTH;
import static com.hescha.game.LoadingScreen.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.hescha.game.model.Game4096;
import com.hescha.game.service.BlockColor;
import com.hescha.game.service.Game4096Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameScreen extends ScreenAdapter {
    private final int dimension;
    private Viewport viewport;
    private OrthographicCamera camera;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private TextureRegion[][] cellTextures;

    private GlyphLayout glyphLayout;
    private BitmapFont bitmapFont;

    private String currentAction = "";

    private Game4096Service game4096Service;
    private Game4096 game4096;
    private Stage stage;
    private int bestScore;

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        glyphLayout = new GlyphLayout();
        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.BLACK);
        bitmapFont.getData().setScale(5f);

        game4096Service = new Game4096Service();
        game4096 = game4096Service.newGame(dimension);

        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        stage = new Stage(viewport);


        cellTextures = new TextureRegion[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cellTextures[i][j] = createColoredTexture(getColorForValue(i));
            }
        }

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
                game4096 = game4096Service.newGame(dimension);
            }
        });
        stage.addActor(btnRestart);
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

        ScreenUtils.clear(new Color(251f / 255f, 208f / 255f, 153f / 255f, 1));
        drawDebug();
        draw();
    }

    private void update() {
        if (MyGestureListener.move != null) {
            game4096Service.moveTiles(game4096, MyGestureListener.move);
            MyGestureListener.move = null;
            saveScore(game4096.getScore());
        }
        updateTexxtures();
    }

    private void updateTexxtures() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
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
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();


        float cellSize = WORLD_WIDTH / dimension - 6;

        // Отображение таблицы массива
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {

                TextureRegion cellTexture = cellTextures[i][j];
                float x = j * (cellSize + 3) + 10;
                float y = i * (cellSize + 3) + 10;

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

        bitmapFont.getData().setScale(4f);
        glyphLayout.setText(bitmapFont, currentAction);
        bitmapFont.draw(batch, glyphLayout, 100, WORLD_HEIGHT - 100);

        glyphLayout.setText(bitmapFont, "Score\r\n" + game4096.getScore());
        bitmapFont.draw(batch, glyphLayout,
                WORLD_WIDTH - glyphLayout.width - 30,
                WORLD_HEIGHT - glyphLayout.height);

        glyphLayout.setText(bitmapFont, "Best\r\n" + bestScore);
        bitmapFont.draw(batch, glyphLayout,
                WORLD_WIDTH - glyphLayout.width - 350,
                WORLD_HEIGHT - glyphLayout.height);

        batch.end();

        stage.draw();


    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        glyphLayout.setText(bitmapFont, "Score\r\n" + game4096.getScore());
        // Рисуем прямоугольник с координатами (x, y), шириной width и высотой height, цветом (r, g, b, a)

        float width = 200;
        float height = 150;
        float r = 207f / 255f;
        float g = 165f / 255f;
        float b = 140f / 255f;
        float a = 1.0f;
        float x = WORLD_WIDTH - glyphLayout.width - 30;
        float y = WORLD_HEIGHT - glyphLayout.height - height;
        shapeRenderer.setColor(r, g, b, a);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.rect(x - 330, y, width, height);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                cellTextures[i][j].getTexture().dispose();
            }
        }
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

