package io.github.stardew.mini.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stardew.mini.StardewMini;
import io.github.stardew.mini.control.SettingController;

import static io.github.stardew.mini.view.asset.Assets.skin;

// 5. Settings Screen
public class SettingMenu implements Screen {
    private final SettingController settingController;
    private Stage stage;
    private Texture background;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();

    public SettingMenu(SettingController settingController) {
        this.settingController = settingController;
        createUI();
    }

    private void createUI() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        TextButton backButton = new TextButton("Back", skin);
        TextButton applyButton = new TextButton("Apply", skin);

        float columnWidth = (float) gameWidth / 5;
        float bottomPad = (float) gameHeight / 10;
        float buttonHeight = (float) gameHeight / 7;
        float rightPad = (float) gameWidth / 20;
        float tablePad = (float) gameWidth / 20;

        Label label = new Label("Volume:", skin);
        label.setFontScale(3f);
        table.add(label).left().width(columnWidth).height(buttonHeight);
        table.add(volumeSlider).width(columnWidth).padBottom(bottomPad).height(buttonHeight);
        table.row();
        table.add(backButton).width(columnWidth).padRight(rightPad).height(buttonHeight);
        table.add(applyButton).width(columnWidth).height(buttonHeight);
        table.setBackground(skin.newDrawable("white", 0, 0, 0, 0.6f)); // RGBA (black, 60% opacity)

        table.pad(tablePad).center();

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingController.goToMain();
            }
        });

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingController.saveSettings(volumeSlider.getValue());
            }
        });

        background = new Texture("menu_bg.png");
        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, gameWidth, gameHeight);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
