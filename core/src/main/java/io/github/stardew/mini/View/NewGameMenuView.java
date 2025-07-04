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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stardew.mini.Controller.NewGameMenuController;
import io.github.stardew.mini.Controller.PreGameMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;
import java.util.ArrayList;
import java.util.Scanner;

public class NewGameMenuView implements AppMenu, Screen {

    private NewGameMenuController controller;
    private Stage stage;
    private Table table;
    private Texture background;
    private ArrayList<String> playerNames = new ArrayList<>();

    private TextField nameInput;
    private Label errorLabel;
    private TextButton addPlayerButton;
    private TextButton startGameButton;
    private ArrayList<Label> playerLabels = new ArrayList<>();

    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();

    public NewGameMenuView(NewGameMenuController controller) {
        this.controller = controller;
        controller.setView(this);
        createUI();
    }

    public void createUI() {
        Skin skin = GameAssetManager.skin;
        stage = new Stage(new FitViewport(gameWidth, gameHeight));
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        background = GameAssetManager.getBackground();

        Label titleLabel = new Label("NEW GAME MENU", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setStyle(new Label.LabelStyle(skin.getFont("custom-font"), Color.GOLD));

        nameInput = new TextField("", skin);
        nameInput.getStyle().font.getData().setScale(2.2f);
        nameInput.setAlignment(Align.center);
        nameInput.setMessageText("Enter username");

        addPlayerButton = new TextButton("Add Player", skin, "custom-button");
        startGameButton = new TextButton("Start Game", skin, "custom-button");
        startGameButton.setDisabled(true); // disabled initially

        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        float buttonWidth = (float) gameWidth / 4;
        float buttonHeight = (float) gameHeight / 8;
        float bottomPad = (float) gameHeight / 30;

        table.add(titleLabel).colspan(2).padBottom(bottomPad * 2).row();
        table.add(nameInput).width(buttonWidth).height(buttonHeight / 2).padBottom(bottomPad);
        table.add(addPlayerButton).width(buttonWidth ).height(buttonHeight).padBottom(bottomPad).row();
        table.add(errorLabel).colspan(2).padBottom(bottomPad).row();

        // Placeholder for player list display
        updatePlayerListUI();

        table.add(startGameButton).colspan(2).padTop(bottomPad * 2).width(buttonWidth).height(buttonHeight).row();

        // Add listeners
        addPlayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String username = nameInput.getText().trim();
                    playerNames.add(username);
                    nameInput.setText("");
                    updatePlayerListUI();
            }
        });

        startGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String usersString = String.join(" ", playerNames);
                Result result = controller.createGame(usersString);
                if(!result.isSuccessful()){
                    showErrorDialog(stage,result.message());
                    usersString = "";
                    playerNames.clear();
                    updatePlayerListUI();
                } else {
                    MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu);
                }
            }
        });
        stage.addActor(table);
    }

    private void updatePlayerListUI() {
        for (Label label : playerLabels) {
            table.removeActor(label);
        }
        playerLabels.clear();

        for (String name : playerNames) {
            Label nameLabel = new Label(name, GameAssetManager.skin,"custom-label");
            playerLabels.add(nameLabel);
            table.add(nameLabel).colspan(2).row();
        }
    }


    //    @Override
//    public void render(float delta) {
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.getBatch().begin();
//        stage.getBatch().draw(background, 0, 0, gameWidth, gameHeight);
//        stage.getBatch().end();
//        stage.act(delta);
//        stage.draw();
//    }
    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        MainApp.getBatch().begin();
        MainApp.getBatch().draw(background, 0, 0, gameWidth, gameHeight);
        MainApp.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        stage.dispose();
    }

    @Override
    public void handleCommand(Scanner scanner) {
    }
}
