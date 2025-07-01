package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stardew.mini.Controller.NewGameMenuController;
import io.github.stardew.mini.Controller.PreGameMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;

import javax.swing.text.View;
import java.util.Scanner;

public class NewGameMenuView implements AppMenu , Screen {
    private NewGameMenuController controller;
    private Stage stage;
    public Table table;
    private Texture background;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();

    public NewGameMenuView(NewGameMenuController controller) {
        createUI();
        this.controller = controller;
        controller.setView(this);
    }

    public void createUI() {
        Skin skin = GameAssetManager.skin;
        stage = new Stage(new FitViewport(gameWidth, gameHeight));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        TextButton playButton = new TextButton("salam", skin);
        TextButton settingsButton = new TextButton("phosindsn", skin);
        TextButton exitButton = new TextButton("gbvubh", skin);

        float buttonWidth = (float) gameWidth / 4;
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
                MainApp.getInstance().setCurrentMenu(Menu.NewGameMenu);
            }
        });
        playButton.getStyle().over = skin.getDrawable("button-normal-over");


        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // mainController.goToSettings();
            }
        });
        settingsButton.getStyle().over = skin.getDrawable("button-normal-over");

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // mainController.exit();
            }
        });
        exitButton.getStyle().over = skin.getDrawable("button-normal-over");

        stage.addActor(table);
        background = GameAssetManager.getBackground();
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
    public void show() {

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

    }

    @Override
    public void handleCommand(Scanner scanner) {

    }
}
