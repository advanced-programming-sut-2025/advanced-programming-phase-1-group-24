package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.MainMenuController;
import io.github.stardew.mini.Controller.ProfileMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;
import io.github.stardew.mini.Model.Result;

public class ProfileMenuView implements Screen {
    private final ProfileMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private Label infoLabel;
    private TextField usernameField, emailField, nicknameField;
    private TextField oldPasswordField, newPasswordField;
    private TextButton changeUsernameButton, changeEmailButton, changeNicknameButton, changePasswordButton;
    private TextButton backButton;
    //private Label errorLabel;
    public ProfileMenuView(ProfileMenuController controller, Skin skin) {
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

        // UI
        table = new Table(skin);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label title = new Label("Profile", skin, "custom-label");
        title.setFontScale(2);
        infoLabel = new Label("", skin, "custom-label");
        usernameField = new TextField("", skin); usernameField.setMessageText("New Username");
        changeUsernameButton = new TextButton("Change Username", skin, "custom-button");
        emailField = new TextField("", skin); emailField.setMessageText("New Email");
        changeEmailButton = new TextButton("Change Email", skin, "custom-button");
        nicknameField = new TextField("", skin); nicknameField.setMessageText("New Nickname");
        changeNicknameButton = new TextButton("Change Nickname", skin, "custom-button");
        oldPasswordField = new TextField("", skin); oldPasswordField.setMessageText("Old Password");
        newPasswordField = new TextField("", skin); newPasswordField.setMessageText("New Password");
        changePasswordButton = new TextButton("Change Password", skin, "custom-button");
        backButton = new TextButton("Back", skin, "custom-button");
        //errorLabel = new Label("", skin, "custom-label");

        // Layout
        table.add(title).colspan(2).padBottom(10).row();
        infoLabel.setColor(Color.ORANGE);
        table.add(infoLabel).colspan(2).padBottom(10).row();

        table.add(usernameField).width(200).pad(5).height(50);
        table.add(changeUsernameButton).width(450).pad(5).height(50).row();
        table.add(emailField).width(200).pad(5).height(50);
        table.add(changeEmailButton).width(450).pad(5).height(50).row();
        table.add(nicknameField).width(200).pad(5).height(50);
        table.add(changeNicknameButton).width(450).height(50).pad(5).row();
        table.add(oldPasswordField).width(200).pad(5).height(50);
        table.row();
        table.add(newPasswordField).width(200).pad(5).height(50);
        table.add(changePasswordButton).width(450).height(50).pad(5).row();
        table.row().pad(15);

        table.add(backButton).colspan(2).width(200).padTop(20).height(50);

        // Populate initial info
        updateInfo();

        // Listeners
        changeUsernameButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                Result res = controller.changeUsername(usernameField.getText().trim());
                //errorLabel.setText(res.message());
                updateInfo(res);
            }
        });
        changeEmailButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                Result res = controller.changeEmail(emailField.getText().trim());
                //errorLabel.setText(res.message());
                updateInfo(res);
            }
        });
        changeNicknameButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                Result res = controller.changeNickname(nicknameField.getText().trim());
                //errorLabel.setText(res.message());
                updateInfo(res);
            }
        });
        changePasswordButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                Result res = controller.changePassword(
                    newPasswordField.getText().trim(),
                    oldPasswordField.getText().trim()
                );
                //errorLabel.setText(res.message());
                updateInfo(res);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                MainApp.getInstance().setScreen(
                    new MainMenuView(
                        new MainMenuController(), skin
                    )
                );
            }
        });
    }

    private void updateInfo() {
        Result res = controller.showUserInfo();
        infoLabel.setText(res.isSuccessful() ? res.message() : "");
    }
    private void updateInfo(Result res) {
        infoLabel.setText(res.message());
        if (res.isSuccessful()) {
            // refresh full info
            updateInfo();
        }
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
