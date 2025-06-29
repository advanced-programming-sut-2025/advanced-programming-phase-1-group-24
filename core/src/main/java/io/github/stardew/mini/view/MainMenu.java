package io.github.stardew.mini.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stardew.mini.control.MainController;
import io.github.stardew.mini.view.asset.Assets;

import static io.github.stardew.mini.view.asset.Assets.skin;

public class MainMenu implements Screen {
    private final MainController mainController;
    private Stage stage;
    private Texture background;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();


    public MainMenu(MainController mainController) {
        this.mainController = mainController;
    }

    public void createUI() {
        stage = new Stage(new FitViewport(gameWidth, gameHeight));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        float buttonWidth = (float) gameWidth / 5;
        float buttonHeight = (float) gameHeight / 7;
        float bottomPad = (float) gameHeight / 10;
        table.add(playButton).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
        table.row();
        table.add(settingsButton).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
        table.row();
        table.add(exitButton).width(buttonWidth).height(buttonHeight);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainController.goToGame();
            }
        });
        playButton.getStyle().over = skin.getDrawable("button-normal-over");


        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainController.goToSettings();
            }
        });
        settingsButton.getStyle().over = skin.getDrawable("button-normal-over");

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainController.exit();
            }
        });
        exitButton.getStyle().over = skin.getDrawable("button-normal-over");

        stage.addActor(table);
        background = Assets.getBackground();
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
        background.dispose();
    }
}
