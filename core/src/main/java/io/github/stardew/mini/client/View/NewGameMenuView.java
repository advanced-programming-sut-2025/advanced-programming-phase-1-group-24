package io.github.stardew.mini.client.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.tools.javac.Main;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.client.NetworkClient;
import io.github.stardew.mini.server.Controller.NewGameMenuController;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

public class NewGameMenuView implements AppMenu, Screen {

    private NewGameMenuController controller;
    private Stage stage;
    private Table table;
    private Texture background;
    private ArrayList<String> playerNames = new ArrayList<>();

    private TextField nameInput;
    //    private Label errorLabel;
    private TextButton addPlayerButton;
    private TextButton startGameButton;
    private TextButton backButton;
    private ArrayList<Label> playerLabels = new ArrayList<>();

//    private int gameWidth = Gdx.graphics.getWidth();
//    private int gameHeight = Gdx.graphics.getHeight();

    public NewGameMenuView(NewGameMenuController controller) {
        this.controller = controller;
        controller.setView(this);
        createUI();
    }
//    stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//    // background
//    Texture bg = GameAssetManager.getBackground();
//    Image bgImage = new Image(bg);
//        bgImage.setFillParent(true);
//        stage.addActor(bgImage);
//

    public void createUI() {
        Skin skin = GameAssetManager.skin;
//        stage = new Stage(new FitViewport(gameWidth, gameHeight));
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);

        //background = GameAssetManager.getBackground();
        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);

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
        backButton =  new TextButton("Back", skin, "custom-button");
        startGameButton.setDisabled(true); // disabled initially

//        errorLabel = new Label("", skin);
//        errorLabel.setColor(Color.RED);

        float buttonWidth = Gdx.graphics.getWidth() / 4f;
        float buttonHeight = Gdx.graphics.getHeight() / 8f;
//
//        float buttonWidth = (float) gameWidth / 4;
//        float buttonHeight = (float) gameHeight / 8;
        float bottomPad = (float) Gdx.graphics.getHeight() / 30;

        table.add(titleLabel).colspan(2).padBottom(bottomPad * 2).row();
        table.add(nameInput).width(buttonWidth).height(buttonHeight / 2).padBottom(bottomPad);
        table.add(addPlayerButton).width(buttonWidth ).height(buttonHeight).padBottom(bottomPad).row();
        //  table.add(errorLabel).colspan(2).padBottom(bottomPad).row();

        // Placeholder for player list display
        updatePlayerListUI();

        table.add(backButton).colspan(2).padTop(bottomPad * 2).width(buttonWidth).height(buttonHeight).row();
        table.add(startGameButton).colspan(2).padTop(bottomPad * 2).width(buttonWidth).height(buttonHeight).row();

        // Add listeners
//        addPlayerButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                String username = nameInput.getText().trim();
//                    playerNames.add(username);
//                    nameInput.setText("");
//                    updatePlayerListUI();
//            }
//        });
        addPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = nameInput.getText().trim();
                if (username.isEmpty()) return;

                playerNames.add(username);
                nameInput.setText("");
                updatePlayerListUI();

                // Use your network client to send the connect message
                if (MainApp.getInstance().getNetworkClient() != null && MainApp.getInstance().getNetworkClient().isOpen()) {
                    MainApp.getInstance().getNetworkClient().sendConnect(username);
                } else {
                    System.err.println("NetworkClient is not connected.");
                }
            }
        });


        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setCurrentMenu(Menu.PreGameMenu);
            }
        });


//        startGameButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
//                String usersString = String.join(" ", playerNames);
//                Result result = controller.createGame(usersString);
//                if(!result.isSuccessful()){
//                    showErrorDialog(stage,result.message());
//                    usersString = "";
//                    playerNames.clear();
//                    updatePlayerListUI();
//                } else {
//                    MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu);
//                }
//            }
//        });
//        startGameButton.addListener(new ClickListener() {
//            public void clicked(InputEvent event, float x, float y) {
////                if (playerNames.isEmpty()) {
////                    showErrorDialog(stage, "You must add at least one player!");
////                    return;
////                }
//
//                String username = MainApp.getInstance().getLoggedInUser().getUsername();
//
//                Map<String, Object> params = new HashMap<>();
//                params.put("usernames", new ArrayList<>(playerNames));
//
//                NetworkClient client = MainApp.getInstance().getNetworkClient(); // adjust if needed
//
//                System.out.println("Sending createGameOnServer request: " + params);
//                client.sendPost(
//                    null,                     // gameId (null for new game)
//                    "NewGameMenuController",  // controller name to route to
//                    "createGameOnServer",     // method name
//                    params,
//                    username
//                ).thenAccept(response -> {
//                    if (response.getStatus() == 200) {
//                        Object bodyRaw = response.getBody();
//                        if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                            Object compressedGameObj = bodyMap.get("compressedGame");
//
//                            if (compressedGameObj instanceof String base64Game) {
//                                System.out.println("Received compressedGame from server.");
//
//                                try {
//                                    byte[] compressedBytes = Base64.getDecoder().decode(base64Game);
//                                    ByteArrayInputStream byteStream = new ByteArrayInputStream(compressedBytes);
//                                    GZIPInputStream gzip = new GZIPInputStream(byteStream);
//                                    InputStreamReader reader = new InputStreamReader(gzip, StandardCharsets.UTF_8);
//
//                                    ObjectMapper mapper = new ObjectMapper();
//                                    Game game = mapper.readValue(reader, Game.class);
//
//                                    MainApp.getInstance().setCurrentGame(game);
//                                    MainApp.getInstance().getActiveGames().add(game);
//
//                                    System.out.println("Game deserialized and set successfully.");
//
//                                    Gdx.app.postRunnable(() -> {
//                                        MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu);
//                                    });
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    System.err.println("Failed to deserialize game: " + e.getMessage());
//                                }
//
//                            } else {
//                                System.err.println("Missing or invalid compressedGame in response.");
//                            }
//                        } else {
//                            System.err.println("Response body is not a map: " + bodyRaw);
//                        }
//                    } else {
//                        Gdx.app.postRunnable(() -> {
//                            showErrorDialog(stage, response.getMessage());
//                        });
//                    }
//                }).exceptionally(ex -> {
//                    Gdx.app.postRunnable(() -> {
//                        showErrorDialog(stage, "Failed to create game: " + ex.getMessage());
//                    });
//                    return null;
//                });
//            }
//        });
//
//        stage.addActor(table);
//    }
        startGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
//                if (playerNames.isEmpty()) {
//                    showErrorDialog(stage, "You must add at least one player!");
//                    return;
//                }

                String username = MainApp.getInstance().getLoggedInUser().getUsername();

                Map<String, Object> params = new HashMap<>();
                params.put("usernames", new ArrayList<>(playerNames));

                NetworkClient client = MainApp.getInstance().getNetworkClient(); // adjust if needed

                System.out.println("Sending createGameOnServer request: " + params);
                client.sendPost(
                    null,                     // gameId (null for new game)
                    "NewGameMenuController",  // controller name to route to
                    "createGameOnServer",     // method name
                    params,
                    username
                ).thenAccept(response -> {
                    System.out.println("Response received");
                    if (response.getStatus() == 200) {
                        Object bodyRaw = response.getBody();

                        if (bodyRaw instanceof Map<?, ?> bodyMap) {
                            Object gameIdObj = bodyMap.get("gameId");
                            if (gameIdObj instanceof String gameId) {
                                // Now you can use gameId
                                System.out.println("Game ID: " + gameId);
                                Object gameObj = bodyMap.get("game");

                                Gson gson = new Gson();
                                String json = gson.toJson(gameObj); // serialize raw object to JSON string
                                Game game = gson.fromJson(json, Game.class); // deserialize back into Game object

                                MainApp.getInstance().setCurrentGame(game);
                                MainApp.getInstance().setCurrentGameId(gameId);
                            } else {
                                System.err.println("gameId is not a string or is null");
                            }
                        } else {
                            System.err.println("Response body is not a map");
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
//    @Override
//    public void render(float v) {
//        ScreenUtils.clear(0, 0, 0, 1);
//        MainApp.getBatch().begin();
//        MainApp.getBatch().draw(background, 0, 0, gameWidth, gameHeight);
//        MainApp.getBatch().end();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
//    }
    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    @Override
    public void show() {

    }

    //    @Override
//    public void resize(int i, int i1) {
//
//    }
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
        stage.dispose();
    }

    @Override
    public void handleCommand(Scanner scanner, Consumer<String> callback) {
    }
}
