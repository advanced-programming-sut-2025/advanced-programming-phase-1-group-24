package io.github.stardew.mini.client.View;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.client.LobbyMenuController;
import io.github.stardew.mini.Model.LobbyInfo;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.client.NetworkClient;

import java.util.*;
import java.util.List;

public class LobbyMenuView implements Screen, AppMenu {

    private LobbyMenuController controller;
    private Stage stage;
    public Table table;
    private Table lobbyListTable;
    private TextField lobbyNameField;
    private TextField passwordField;
    private CheckBox privateCheckBox;

    public LobbyMenuView(LobbyMenuController controller) {
        this.controller = controller;
        controller.setView(this);
        createUI();
    }

    private void createUI() {
        Skin skin = GameAssetManager.skin;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("LOBBY MENU", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setStyle(new Label.LabelStyle(skin.getFont("custom-font"), Color.GOLD));

        lobbyNameField = new TextField("", skin);
        lobbyNameField.setMessageText("Lobby name");

        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password (optional)");
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);

        privateCheckBox = new CheckBox("Private Lobby", skin);

        TextButton createLobbyButton = new TextButton("Create Lobby", skin, "custom-button");
        TextButton refreshButton = new TextButton("Refresh List", skin, "custom-button");
        TextButton backButton = new TextButton("Back", skin, "custom-button");

        lobbyListTable = new Table();
        ScrollPane scrollPane = new ScrollPane(lobbyListTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        float buttonWidth = (float) Gdx.graphics.getWidth() / 4;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 10;
        float pad = (float) Gdx.graphics.getHeight() / 40;


        table.add(titleLabel).colspan(2).padBottom(pad).row();
        table.add(lobbyNameField).width(buttonWidth).padBottom(pad).colspan(2).row();
        table.add(passwordField).width(buttonWidth).padBottom(pad).colspan(2).row();
        table.add(privateCheckBox).colspan(2).padBottom(pad).row();
        table.add(createLobbyButton).width(buttonWidth).height(buttonHeight).padBottom(pad).colspan(2).row();
        table.add(refreshButton).width(buttonWidth).height(buttonHeight).padBottom(pad).colspan(2).row();
        table.add(new Label("Available Lobbies:", skin, "custom-label")).left().padTop(pad).colspan(2).row();
        table.add(scrollPane).width(buttonWidth*2f).height(buttonHeight * 2f).colspan(2).row();
        table.add(backButton).width(buttonWidth).height(buttonHeight).padTop(pad).colspan(2);


        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.createLobby(
                    lobbyNameField.getText(),
                    passwordField.getText(),
                    privateCheckBox.isChecked()
                );
            }
        });

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.refreshLobbies();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setCurrentMenu(Menu.PreGameMenu);
            }
        });

        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

        stage.addActor(table);

        controller.refreshLobbies();
    }

        public void updateLobbyList(List<LobbyInfo> lobbies) {
        lobbyListTable.clear();
        Skin skin = GameAssetManager.skin;

        for (LobbyInfo lobby : lobbies) {
            Table row = new Table();
            row.align(Align.left).pad(10);

            Label nameLabel = new Label(lobby.getName(), skin, "custom-label");
            nameLabel.setColor(Color.BLUE);
            Label playerCountLabel = new Label("(" + lobby.getPlayerCount() + "/4)", skin, "custom-label");
            playerCountLabel.setColor(Color.BLUE);
            TextButton joinButton = new TextButton("Join", skin, "custom-button");
            joinButton.setColor(Color.BLUE);
            TextButton startButton = new TextButton("Start", skin, "custom-button");
            startButton.setColor(Color.BLUE);
            joinButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.joinLobby(lobby.getId(), lobby.isPrivate());
                }
            });
            startButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(lobby.getOwner().equals(MainApp.getInstance().getLoggedInUser().getUsername())) {
                        System.out.println("lobby owner  "+lobby.getOwner());
                        System.out.println("logged in user"+MainApp.getInstance().getLoggedInUser().getUsername());
                        List<String> dummyPlayers = lobby.getPlayers();
                        System.out.println("players salam:" + dummyPlayers);
                        startGameFromLobby(dummyPlayers);
                    } else {
                        showErrorDialog(stage,"Only the creator can start the game!");
                    }
                    System.out.println(lobby.getPlayers());
                }
            });
            row.add(nameLabel).width(200).left().padRight(5);
            row.add(playerCountLabel).width(30).padRight(8).padLeft(8);
            row.add(joinButton).width(80).padRight(8).padLeft(8);
            row.add(startButton).width(80).padRight(8).padLeft(8);
            lobbyListTable.add(row).row();
        }
    }

    private void startGameFromLobby(List<String> playerNames) {
        String username = MainApp.getInstance().getLoggedInUser().getUsername();

        Map<String, Object> params = new HashMap<>();
        params.put("usernames", new ArrayList<>(playerNames)); // you may need to fetch this from lobby info

        NetworkClient client = MainApp.getInstance().getNetworkClient();

        System.out.println("Sending createGameOnServer request: " + params);
        client.sendPost(
            null,
            "NewGameMenuController",
            "createGameOnServer",
            params,
            username
        ).thenAccept(response -> {
            System.out.println("Response received");
            if (response.getStatus() == 200) {
                Object bodyRaw = response.getBody();

                if (bodyRaw instanceof Map<?, ?> bodyMap) {
                    Object gameIdObj = bodyMap.get("gameId");
                    if (gameIdObj instanceof String gameId) {
                        Gson gson = new Gson();
                        String json = gson.toJson(bodyMap.get("game"));
                        Game game = gson.fromJson(json, Game.class);

                        MainApp.getInstance().setCurrentGame(game);
                        MainApp.getInstance().setCurrentGameId(gameId);
                    }
                }
                Gdx.app.postRunnable(() -> {
                    MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu);
                });
            } else {
                Gdx.app.postRunnable(() -> {
                    showErrorDialog(stage, response.getMessage());
                });
            }
        }).exceptionally(ex -> {
            Gdx.app.postRunnable(() -> {
                showErrorDialog(stage, "Failed to create game: " + ex.getMessage());
            });
            return null;
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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

    public LobbyMenuController getController() {
        return controller;
    }

    public void setController(LobbyMenuController controller) {
        this.controller = controller;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getLobbyListTable() {
        return lobbyListTable;
    }

    public void setLobbyListTable(Table lobbyListTable) {
        this.lobbyListTable = lobbyListTable;
    }

    public TextField getLobbyNameField() {
        return lobbyNameField;
    }

    public void setLobbyNameField(TextField lobbyNameField) {
        this.lobbyNameField = lobbyNameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(TextField passwordField) {
        this.passwordField = passwordField;
    }

    public CheckBox getPrivateCheckBox() {
        return privateCheckBox;
    }

    public void setPrivateCheckBox(CheckBox privateCheckBox) {
        this.privateCheckBox = privateCheckBox;
    }

    @Override
    public void handleCommand(Scanner scanner) {

    }
}
