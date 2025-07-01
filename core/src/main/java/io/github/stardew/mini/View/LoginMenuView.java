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
import io.github.stardew.mini.Controller.SignupMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;
import io.github.stardew.mini.Model.Menus.LoginMenuCommands;
import io.github.stardew.mini.Model.Result;

import java.util.regex.Matcher;

public class LoginMenuView implements Screen {
    private final LoginMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextField usernameField;
    private TextField passwordField;
    private TextButton loginButton;
    private TextButton registerButton;
    private Label errorLabel;

    public LoginMenuView(LoginMenuController controller, Skin skin) {
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

        // UI Table
        table = new Table(skin);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("Login", skin, "default");
        title.setFontScale(2);
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Username");
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setMessageText("Password");

        loginButton = new TextButton("Login", skin);
        registerButton = new TextButton("Register", skin);
        errorLabel = new Label("", skin);

        table.add(title).colspan(2).padBottom(20);
        table.row().pad(10);
        table.add(new Label("Username:", skin)).right().padRight(10);
        table.add(usernameField).width(200);
        table.row().pad(10);
        table.add(new Label("Password:", skin)).right().padRight(10);
        table.add(passwordField).width(200);
        table.row().pad(20);
        table.add(loginButton).width(150).padRight(10);
        table.add(registerButton).width(150);
        table.row().pad(15);
        table.add(errorLabel).colspan(2);

        // Listeners
        loginButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                String cmd = String.format("login -u %s -p %s",
                    usernameField.getText(), passwordField.getText());
                Matcher m = LoginMenuCommands.LOGIN.getMatcher(cmd);
                if (m != null) {
                    Result res = controller.login(m);
                    errorLabel.setText(res.message());
                    if (res.isSuccessful()) {
                        MainApp.getInstance().setScreen(
                            new MainMenuView(new MainMenuController())
                        );
                    }
                } else {
                    errorLabel.setText("Invalid login format");
                }
            }
        });
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
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
