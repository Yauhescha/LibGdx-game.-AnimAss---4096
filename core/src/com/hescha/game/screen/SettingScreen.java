package com.hescha.game.screen;

import static com.hescha.game.screen.LoadingScreen.WORLD_HEIGHT;
import static com.hescha.game.screen.LoadingScreen.WORLD_WIDTH;
import static com.hescha.game.screen.LoadingScreen.assetManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hescha.game.GameFourZeroNineEight;

import javax.swing.event.ChangeEvent;

public class SettingScreen extends ScreenAdapter {
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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("angrybirds-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.BLACK;
        BitmapFont bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        // Используем стандартные текстуры из LibGDX
        Pixmap pixmapWhite = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapWhite.setColor(Color.WHITE);
        pixmapWhite.fill();

        Pixmap pixmapRed = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapRed.setColor(Color.RED);
        pixmapRed.fill();

        Pixmap pixmapGreen = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapGreen.setColor(Color.GREEN);
        pixmapGreen.fill();

        Drawable whiteDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapWhite)));
        Drawable redDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapRed)));

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = bitmapFont;
        listStyle.selection = redDrawable;

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.vScrollKnob = whiteDrawable;

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = bitmapFont;
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        selectBoxStyle.background = whiteDrawable;

        final SelectBox<LevelType> selboxServer = new SelectBox<LevelType>(selectBoxStyle);
        selboxServer.setItems(LevelType.values());
        selboxServer.setHeight(100);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;

        Label label = new Label("Select Level Type", labelStyle);


        Pixmap pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        Pixmap pixmapChecked = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        Pixmap pixmapUnchecked = new Pixmap(20, 20, Pixmap.Format.RGBA8888);

        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        pixmapChecked.setColor(Color.GREEN);
        pixmapChecked.fill();
        pixmapUnchecked.setColor(Color.RED);
        pixmapUnchecked.fill();

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        Drawable drawableChecked = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapChecked)));
        Drawable drawableUnchecked = new TextureRegionDrawable(new TextureRegion(new Texture(pixmapUnchecked)));

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = bitmapFont;
        checkBoxStyle.checkboxOn = drawableChecked;
        checkBoxStyle.checkboxOff = drawableUnchecked;
        checkBoxStyle.checkboxOver = drawable;

        CheckBox checkBox = new CheckBox(" Show numbers?", checkBoxStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = bitmapFont;
        textButtonStyle.up = whiteDrawable;

        TextButton backButton = new TextButton("Back", textButtonStyle);


        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(label).pad(10).row();;
        table.row();
        table.add(selboxServer).width(Gdx.graphics.getWidth()/2).pad(50).row();
        table.row();
        table.add(checkBox).pad(100).row();;
        table.row();
        table.add(backButton);
        stage.addActor(table);


        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameFourZeroNineEight.game.setScreen(new MainMenuScreen());
            }
        });
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Preferences prefs = Gdx.app.getPreferences("2048-game");
                prefs.putBoolean("showNumber", ((CheckBox) actor).isChecked());
                prefs.flush();
            }
        });
        selboxServer.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println(selboxServer.getSelected());
                Preferences prefs = Gdx.app.getPreferences("2048-game");
                prefs.putInteger("levelType", selboxServer.getSelected().ordinal());
                prefs.flush();
            }
        });

        Preferences preferences = Gdx.app.getPreferences("2048-game");
        int levelType = preferences.getInteger("levelType");
        selboxServer.setSelected(LevelType.values()[levelType]);
        boolean showNumber = preferences.getBoolean("showNumber", true);
        checkBox.setChecked(showNumber);
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
