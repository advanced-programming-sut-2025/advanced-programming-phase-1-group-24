package io.github.stardew.mini.client.View;

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
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.Controller.PreGameMenuController;
import io.github.stardew.mini.server.Controller.ProfileMenuController;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.common.Model.Avatar;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class MainMenuView implements Screen,AppMenu {
    private final MainMenuController controller;
    private final Skin skin;
    private Stage stage;
    private Table table;
    private TextButton logoutButton;
    private TextButton profileButton;
    private TextButton pregameButton;
    private Label infoLabelMainMenu;
    private Image avatarImage;
    private TextButton exitGame;
    private Table onlineTable;


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
        Avatar avatarName = MainApp.getInstance().getLoggedInUser().getAvatar();


        Label title = new Label("Main Menu", skin, "custom-label");
        title.setFontScale(2);
        avatarImage = new Image(GameAssetManager.getAvatarDrawable(avatarName));
        infoLabelMainMenu = new Label("", skin,"custom-label");
        logoutButton = new TextButton("Logout", skin, "custom-button");
        profileButton = new TextButton("Profile", skin, "custom-button");
        pregameButton = new TextButton("Pre-Game", skin, "custom-button");
        exitGame = new TextButton("Exit Game", skin, "custom-button");

        onlineTable = new Table(skin);
        ScrollPane scroll = new ScrollPane(onlineTable, skin);
        scroll.setFadeScrollBars(false);
        Label onlineTitle = new Label("Online Players", skin, "custom-label");
        onlineTitle.setColor(Color.ORANGE);


        // Row layout


        table.add(title).colspan(2).padBottom(20).row();
        avatarImage.setSize(100, 100);  // or TILE_SIZE, whatever you like
        table.add(avatarImage).colspan(2).padBottom(20).row();
        infoLabelMainMenu.setColor(Color.BLACK);
        table.add(infoLabelMainMenu).colspan(2).padBottom(20).row();
        table.add(profileButton).width(300).padRight(10).height(70);
        table.add(pregameButton).width(300).height(70);
        table.row().pad(10);
        table.add(exitGame).width(300).padRight(10).height(70);
        table.add(logoutButton).colspan(2).width(300).height(70);
        table.row().pad(20);
        table.add(onlineTitle).colspan(2).padBottom(10).row();
        table.add(scroll).colspan(2).width(600).height(200).row();
        updateOnlinePlayers(MainApp.getInstance().getOnlinePlayers());
        showInfoMainMenu();

        // Button listeners
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setScreen(new ProfileMenuView(new ProfileMenuController(), skin));
            }
        });

        pregameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            MainApp.getInstance().setScreen(new PreGameMenuView(new PreGameMenuController()));
            }
        });

        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.userLogout();
            }
        });

        exitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // اول LibGDX رو ببند
                Gdx.app.exit();
                // بعد JVM رو کامل خاموش کن
//                System.exit(0);
            }
        });
    }
    private void showInfoMainMenu() {
        String username = MainApp.getInstance().getLoggedInUser().getUsername();
        Map<String, Object> body = new HashMap<>(); // No body needed for just fetching info

        MainApp.getInstance().getNetworkClient()
            .sendPost(
                null,
                "MainMenuController",
                "showUserInfo",
                body,
                username
            ).thenAccept(response -> {
               // Gdx.app.postRunnable(() -> {
                    infoLabelMainMenu.setText(response.getMessage());
               // });
            });

//        Result res = controller.showUserInfoMainMenu();
//        infoLabelMainMenu.setText(res.message());
    }

    public void updateOnlinePlayers(java.util.List<Map<String, String>> players) {
        onlineTable.clear();
        for (Map<String,String> p : players) {
            String text = p.get("username");
            String lobby = p.get("lobby");
            if (lobby != null && !lobby.isEmpty()) {
                text += " (" + lobby + ")";
            }
            Label lbl = new Label(text, skin, "custom-label");
            lbl.setColor(Color.WHITE);
            onlineTable.add(lbl).left().row();
        }
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

    @Override
    public void handleCommand(Scanner scanner, Consumer<String> callback) {

    }
}
