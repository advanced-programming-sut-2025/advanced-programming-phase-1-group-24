package io.github.stardew.mini.client.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.gson.Gson;
import com.sun.tools.javac.Main;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.client.NetworkClient;
import io.github.stardew.mini.server.Controller.MapSelectionMenuController;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MapSelectionMenuView implements AppMenu, Screen {
    private MapSelectionMenuController controller;
    private Stage stage;
    public Table table;
    private String selectedMap = null; // To track the selected map

    public MapSelectionMenuView(MapSelectionMenuController controller) {
        createUI();
        this.controller = controller;
        controller.setView(this);
    }

    public void createUI() {
        Skin skin = GameAssetManager.skin;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        // Add title label
        Label titleLabel = new Label("SELECT YOUR MAP", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);

        Label.LabelStyle titleStyle = new Label.LabelStyle(
            skin.getFont("custom-font"),
            Color.GOLD
        );
        titleLabel.setStyle(titleStyle);

        // Create map selection buttons
        TextureRegionDrawable map1Drawable = new TextureRegionDrawable(new TextureRegion(GameAssetManager.farm1));
        TextureRegionDrawable map2Drawable = new TextureRegionDrawable(new TextureRegion(GameAssetManager.farm2));
        Image map1Image = new Image(map1Drawable);
        Image map2Image = new Image(map2Drawable);
        map1Image.setScaling(Scaling.fit);  // Optional: scale images to fit
        map2Image.setScaling(Scaling.fit);
        TextButton map1Button = new TextButton("Map 1", skin, "custom-button");
        TextButton map2Button = new TextButton("Map 2", skin, "custom-button");

        TextButton confirmButton = new TextButton("Confirm", skin, "custom-button");
        confirmButton.setDisabled(true);

        // Store original styles
        TextButton.TextButtonStyle originalStyle = new TextButton.TextButtonStyle(skin.get("custom-button", TextButton.TextButtonStyle.class));
        TextButton.TextButtonStyle selectedStyle = new TextButton.TextButtonStyle(originalStyle);
        selectedStyle.fontColor = Color.GREEN;

        float buttonWidth = (float) Gdx.graphics.getWidth() / 4;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 7;
        float bottomPad = (float) Gdx.graphics.getHeight() / 10;

        table.add(titleLabel).colspan(2).padBottom(bottomPad).row();
        // Add map selection buttons side by side with consistent padding
        Table map1Table = new Table();
        map1Table.add(map1Image).width(buttonWidth * 1.8f).height(buttonHeight * 1.8f).row();
        map1Table.add(map1Button).width(buttonWidth).height(buttonHeight);

        Table map2Table = new Table();
        map2Table.add(map2Image).width(buttonWidth * 1.8f).height(buttonHeight * 1.8f).row();
        map2Table.add(map2Button).width(buttonWidth).height(buttonHeight);

        table.add(map1Table).padRight(20).padBottom(bottomPad);
        table.add(map2Table).padLeft(20).padBottom(bottomPad).row();
//
//        table.add(map1Button).width(buttonWidth).height(buttonHeight).padRight(20).padBottom(bottomPad);
//        table.add(map2Button).width(buttonWidth).height(buttonHeight).padLeft(20).padBottom(bottomPad).row();
        table.add(confirmButton).colspan(2).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);

        // Map selection button listeners
        map1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedMap = "1";
                confirmButton.setDisabled(false);
                map1Button.setStyle(selectedStyle);
                map2Button.setStyle(originalStyle);
            }
        });

        map2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedMap = "2";
                confirmButton.setDisabled(false);
                map2Button.setStyle(selectedStyle);
                map1Button.setStyle(originalStyle);
            }
        });

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedMap != null) {
//                    //  controller.notifyMapSelected(selectedMap);
//                    controller.pickGameMap(MainApp.getInstance().getCurrentGame().getCurrentPlayer(), Integer.parseInt(selectedMap));
//                    System.out.println("logged in user " + MainApp.getInstance().getLoggedInUser().getUsername() + " " + selectedMap);
//                    for (User user : MainApp.getInstance().getCurrentGame().getPlayers()) {
//                        if (!user.equals(MainApp.getInstance().getLoggedInUser())) {
//                            int number = Integer.parseInt(selectedMap) + 1;
//                            controller.pickGameMap(user, number);
//                            System.out.println( user.getUsername() + " " + number +"\n");
//                        }
//                    }
//                    MainApp.getInstance().setCurrentMenu(Menu.GameMenu);

                    Map<String, Object> params = new HashMap<>();
                    if(selectedMap.equals("1")) params.put("mapNumber", 1);
                    else if(selectedMap.equals("2")) params.put("mapNumber", 2);
                    MainApp.getInstance().getNetworkClient().sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                        "MapSelectionMenuController","pickGameMap",params,
                        MainApp.getInstance().getCurrentGame().getCurrentPlayer().getUsername()).thenAccept(response -> {
                        if(response.getStatus() == 200) {
                            Object bodyRaw = response.getBody();

                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof  String json) {
                                    System.out.println("////////////////////////////////////////////////////");
                                    try {
                                        Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                        MainApp.getInstance().setCurrentGame(game);
                                        System.out.println("Farms: " + MainApp.getInstance().getCurrentGame().getMap().getFarms().size());
                                        System.out.println("Game successfully deserialized");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        showErrorDialog(stage, "Deserialization failed: " + e.getMessage());
                                    }
                                }
                            } else {
                                System.err.println("Response body is not a map");
                            }
//                            Gdx.app.postRunnable(() -> {
//                                MainApp.getInstance().setCurrentMenu(Menu.GameMenu);
//                            });
                        }
                        else {
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
            }
        });
        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);
        stage.addActor(table);
//        background = GameAssetManager.getBackground();
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // Show a message when all players have selected their maps
    public void showAllPlayersReady() {
        Label readyLabel = new Label("All players ready! Starting game...", GameAssetManager.skin);
        readyLabel.setFontScale(1.5f);
        readyLabel.setColor(Color.GREEN);
        readyLabel.setPosition(
            Gdx.graphics.getWidth() / 2 - readyLabel.getWidth() / 2,
            Gdx.graphics.getHeight() / 2 - readyLabel.getHeight() / 2
        );
        stage.addActor(readyLabel);
    }

    // Other existing methods remain the same...
    @Override
    public void show() {
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

    @Override
    public void handleCommand(Scanner scanner) {
    }
}
