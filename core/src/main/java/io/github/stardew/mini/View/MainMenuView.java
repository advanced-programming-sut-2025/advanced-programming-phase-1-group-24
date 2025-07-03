package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.LoginMenuController;
import io.github.stardew.mini.Controller.MainMenuController;
import io.github.stardew.mini.Controller.ProfileMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;

public class MainMenuView implements Screen {
    private final MainMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextButton logoutButton;
    private TextButton profileButton;
    private TextButton pregameButton;

    public MainMenuView(MainMenuController controller, Skin skin) {
        this.controller = controller;
        this.skin = skin;
        controller.setView(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // background
        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        // UI layout
        table = new Table(skin);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("Main Menu", skin, "custom-label");
        title.setFontScale(2);

        logoutButton = new TextButton("Logout", skin, "custom-button");
        profileButton = new TextButton("Profile", skin, "custom-button");
        pregameButton = new TextButton("Pre-Game", skin, "custom-button");

        // Row layout
        table.add(title).colspan(2).padBottom(20);
        table.row().pad(10);
        table.add(profileButton).width(300).padRight(10).height(70);
        table.add(pregameButton).width(300).height(70);
        table.row().pad(10);
        table.add(logoutButton).colspan(2).width(300).height(70);

        // Button listeners
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setScreen(new ProfileMenuView(new ProfileMenuController(), skin));
            }
        });

//        pregameButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//            MainApp.getInstance().setScreen(new PreGameView(new PreGameController(), skin));
//            }
//        });

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.userLogout();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
