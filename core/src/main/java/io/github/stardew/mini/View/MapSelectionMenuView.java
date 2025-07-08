package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.stardew.mini.Controller.MapSelectionMenuController;
import io.github.stardew.mini.Controller.PreGameMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Animals.AnimalType;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;

import java.util.Scanner;

//
//public class MapSelectionMenuView implements AppMenu, Screen {
//    private MapSelectionMenuController controller;
//    private Stage stage;
//    public Table table;
//    private Texture background;
//    private int gameWidth = Gdx.graphics.getWidth();
//    private int gameHeight = Gdx.graphics.getHeight();
//
//    public MapSelectionMenuView(MapSelectionMenuController controller) {
//        createUI();
//        this.controller = controller;
//        controller.setView(this);
//    }
//    public void createUI() {
//        Skin skin = GameAssetManager.skin;
//        stage = new Stage(new FitViewport(gameWidth, gameHeight));
//        Gdx.input.setInputProcessor(stage);
//
//        Table table = new Table();
//        table.setFillParent(true);
//
//        // Add title label (new code)
//        Label titleLabel = new Label("Map SELECTION MENU", skin);
//        titleLabel.setFontScale(2.5f); // Make title larger
//        titleLabel.setAlignment(Align.center);
//
//        // Create title style from skin or customize
//        Label.LabelStyle titleStyle = new Label.LabelStyle(
//            skin.getFont("custom-font"),
//            Color.GOLD
//        );
//        titleLabel.setStyle(titleStyle);
//
//        TextButton newGameButton = new TextButton("ready", skin, "custom-button");
//
//        float buttonWidth = (float) gameWidth / 4;
//        float buttonHeight = (float) gameHeight / 7;
//        float bottomPad = (float) gameHeight / 10;
//        table.add(titleLabel).colspan(1).padBottom(bottomPad).row();
//        table.add(newGameButton).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
//        table.row();
//
//        newGameButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                MainApp.getInstance().setCurrentMenu(Menu.NewGameMenu);
//            }
//        });
//        newGameButton.getStyle().over = skin.getDrawable("button-normal-over");
//
//        stage.addActor(table);
//        background = GameAssetManager.getBackground();
//    }
//    @Override
//    public void show() {
//
//    }
//
//    @Override
//    public void render(float v) {
//        ScreenUtils.clear(0, 0, 0, 1);
//        MainApp.getBatch().begin();
//        MainApp.getBatch().draw(GameAssetManager.getBackground(), 0, 0, gameWidth, gameHeight);
//        MainApp.getBatch().end();
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
//    }
//
//    @Override
//    public void resize(int i, int i1) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    @Override
//    public void handleCommand(Scanner scanner) {
//
//    }
//}
public class MapSelectionMenuView implements AppMenu, Screen {
    private MapSelectionMenuController controller;
    private Stage stage;
    public Table table;
    private Texture background;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();
    private String selectedMap = null; // To track the selected map

    public MapSelectionMenuView(MapSelectionMenuController controller) {
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
        TextButton map1Button = new TextButton("Map 1", skin, "custom-button");
        TextButton map2Button = new TextButton("Map 2", skin, "custom-button");
        TextButton confirmButton = new TextButton("Confirm", skin, "custom-button");
        confirmButton.setDisabled(true);

        // Store original styles
        TextButton.TextButtonStyle originalStyle = new TextButton.TextButtonStyle(skin.get("custom-button", TextButton.TextButtonStyle.class));
        TextButton.TextButtonStyle selectedStyle = new TextButton.TextButtonStyle(originalStyle);
        selectedStyle.fontColor = Color.GREEN;

        float buttonWidth = (float) gameWidth / 4;
        float buttonHeight = (float) gameHeight / 7;
        float bottomPad = (float) gameHeight / 10;

        table.add(titleLabel).colspan(2).padBottom(bottomPad).row();
//
//        table.add(map1Button).width(buttonWidth).height(buttonHeight).padRight(20);
//        table.add(map2Button).width(buttonWidth).height(buttonHeight).padBottom(bottomPad).row();
        // Add map selection buttons side by side with consistent padding
        table.add(map1Button).width(buttonWidth).height(buttonHeight).padRight(20).padBottom(bottomPad);
        table.add(map2Button).width(buttonWidth).height(buttonHeight).padLeft(20).padBottom(bottomPad).row();
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
                    //  controller.notifyMapSelected(selectedMap);
                    controller.pickGameMap(MainApp.getInstance().getLoggedInUser(), Integer.parseInt(selectedMap));
                    System.out.println("logged in user " + MainApp.getInstance().getLoggedInUser().getUsername() + " " + selectedMap);
                    for (User user : MainApp.getInstance().getCurrentGame().getPlayers()) {
                        if (!user.equals(MainApp.getInstance().getLoggedInUser())) {
                            int number = Integer.parseInt(selectedMap) + 1;
                            controller.pickGameMap(user, number);
                            System.out.println( user.getUsername() + " " + number +"\n");
                        }
                    }
                    /// /////////// hard code ///////////////////////////////////////////////////////////////
                    Animal moo = new Animal("moo", AnimalType.COW);
                    Tile[][] tiles=MainApp.getInstance().getCurrentGame().getMap().getMap();
                    Tile new_tile = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(MainApp.getInstance().getLoggedInUser()).getRandomFarmTile(tiles);
                    moo.setCurrentTile(new_tile);
                    new_tile.setContainedAnimal(moo);
                    MainApp.getInstance().getLoggedInUser().getOwnedAnimals().add(moo);
                    Animal heny = new Animal("heny", AnimalType.CHICKEN);
                    Tile new_tile_heny = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(MainApp.getInstance().getLoggedInUser()).getRandomFarmTile(tiles);
                    heny.setCurrentTile(new_tile_heny);
                    new_tile.setContainedAnimal(heny);
                    MainApp.getInstance().getLoggedInUser().getOwnedAnimals().add(heny);
                    /// /////////// hard code ///////////////////////////////////////////////////////////////
                    MainApp.getInstance().setCurrentMenu(Menu.GameMenu);
                }
            }
        });

        stage.addActor(table);
        background = GameAssetManager.getBackground();
    }
//    public void createUI() {
//        Skin skin = GameAssetManager.skin;
//        stage = new Stage(new FitViewport(gameWidth, gameHeight));
//        Gdx.input.setInputProcessor(stage);
//
//        Table table = new Table();
//        table.setFillParent(true);
//
//        // Add title label
//        Label titleLabel = new Label("SELECT YOUR MAP", skin);
//        titleLabel.setFontScale(2.5f);
//        titleLabel.setAlignment(Align.center);
//
//        Label.LabelStyle titleStyle = new Label.LabelStyle(
//            skin.getFont("custom-font"),
//            Color.GOLD
//        );
//        titleLabel.setStyle(titleStyle);
//
//        // Create map selection buttons
//        TextButton map1Button = new TextButton("Map 1", skin, "custom-button");
//        TextButton map2Button = new TextButton("Map 2", skin, "custom-button");
//        TextButton confirmButton = new TextButton("Confirm Selection", skin, "custom-button");
//        confirmButton.setDisabled(true); // Disabled until a map is selected
//
//        float buttonWidth = (float) gameWidth / 4;
//        float buttonHeight = (float) gameHeight / 7;
//        float bottomPad = (float) gameHeight / 10;
//
//        table.add(titleLabel).colspan(2).padBottom(bottomPad).row();
//
//        // Add map selection buttons side by side
//        table.add(map1Button).width(buttonWidth).height(buttonHeight).padRight(20);
//        table.add(map2Button).width(buttonWidth).height(buttonHeight).padBottom(bottomPad).row();
//
//        // Add confirm button
//        table.add(confirmButton).colspan(2).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
//
//        // Map selection button listeners
//        map1Button.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                selectedMap = "map1";
//                confirmButton.setDisabled(false);
//                map1Button.getStyle().fontColor = Color.GREEN;
//                map2Button.getStyle().fontColor = Color.WHITE;
//            }
//        });
//
//        map2Button.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                selectedMap = "map2";
//                confirmButton.setDisabled(false);
//                map2Button.getStyle().fontColor = Color.GREEN;
//                map1Button.getStyle().fontColor = Color.WHITE;
//            }
//        });
//
//        // Confirm button listener
//        confirmButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (selectedMap != null) {
//                    //controller.notifyMapSelected(selectedMap);
//                    // The controller will handle the transition to the game
//                    // after all players have selected their maps
//                }
//            }
//        });
//
//        // Style adjustments
//        map1Button.getStyle().over = skin.getDrawable("button-normal-over");
//        map2Button.getStyle().over = skin.getDrawable("button-normal-over");
//        confirmButton.getStyle().over = skin.getDrawable("button-normal-over");
//
//        stage.addActor(table);
//        background = GameAssetManager.getBackground();
//    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        MainApp.getBatch().begin();
        MainApp.getBatch().draw(GameAssetManager.getBackground(), 0, 0, gameWidth, gameHeight);
        MainApp.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    // Show a message when all players have selected their maps
    public void showAllPlayersReady() {
        Label readyLabel = new Label("All players ready! Starting game...", GameAssetManager.skin);
        readyLabel.setFontScale(1.5f);
        readyLabel.setColor(Color.GREEN);
        readyLabel.setPosition(
            gameWidth / 2 - readyLabel.getWidth() / 2,
            gameHeight / 2 - readyLabel.getHeight() / 2
        );
        stage.addActor(readyLabel);
    }

    // Other existing methods remain the same...
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
