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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.stardew.mini.Model.*;
import io.github.stardew.mini.client.NetworkClient;
import io.github.stardew.mini.server.Controller.PreGameMenuController;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Menus.Menu;

import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;

public class PreGameMenuView implements AppMenu, Screen {
    private PreGameMenuController controller;
    private Stage stage;
    public Table table;
    private Texture background;
    //    private int gameWidth = Gdx.graphics.getWidth();
//    private int gameHeight = Gdx.graphics.getHeight();
    private Label savedGameInfoLabel;

    public PreGameMenuView(PreGameMenuController controller) {
        createUI();
        this.controller = controller;
        controller.setView(this);
    }
//
//    public void createUI() {
//        Skin skin = GameAssetManager.skin;
//        stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//        table = new Table();
//        table.setFillParent(true);
//
//        // Add title label (new code)
//        Label titleLabel = new Label("PRE GAME MENU", skin);
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
//        TextButton newGameButton = new TextButton("Play new game", skin, "custom-button");
//        TextButton lastGameButton = new TextButton("Play last game", skin, "custom-button");
//        // check if there's a saved game

    /// /        Game savedGame = MainApp.getInstance().getGameByUser(MainApp.getInstance().getLoggedInUser());
    /// /        if (savedGame != null) {
    /// /            String gameDetails = "Saved Game: " + savedGame.getTimeAndDate().toString();
    /// /            // You can customize this string to show season, day, farm name, etc.
    /// /            savedGameInfoLabel = new Label(gameDetails, skin,"custom-label");
    /// /        } else {
    /// /            savedGameInfoLabel = new Label("No saved game found.", skin,"custom-label");
    /// /        }
    /// /        savedGameInfoLabel.setFontScale(0.5f);
//        // Check for saved game
//        //savedGameInfoLabel = new Label("Loading saved games...", skin, "custom-label");
//        Map<String, Object> params = new HashMap<>();
//        MainApp.getInstance().getNetworkClient()
//            .sendPost(null, "GameController", "getSavedGames", params, MainApp.getInstance().getLoggedInUser().getUsername())
//            .thenAccept(response -> {
//                if (response.getStatus() == 200) {
//                    Type listType = new TypeToken<List<GameSummary>>() {
//                    }.getType();
//                    List<GameSummary> summaries = new Gson().fromJson(response.getBody().toString(), listType);
//                    Gdx.app.postRunnable(() -> {
//                        updateSavedGamesUI(summaries); // Your method to fill buttons/info
//                    });
//                }
//            });
//
//    //        Game savedGame = MainApp.getInstance().getGameByUser(MainApp.getInstance().getLoggedInUser());
//    //        if (savedGame != null) {
//    //            StringBuilder details = new StringBuilder();
//    //            details.append("Saved Game Info:\n");
//    //            details.append("Day: ").append(savedGame.getTimeAndDate().getDay()).append("\n");
//    //            details.append("Season: ").append(savedGame.getTimeAndDate().getSeason()).append("\n");
//    //            details.append("Time: ").append(savedGame.getTimeAndDate().getHour()).append(":00\n");
//    //            details.append("Owner: ").append(savedGame.getMainPlayer().getUsername()).append("\n");
//    //            List<User> players = savedGame.getPlayers();
//    //            details.append("Players: ");
//    //            for (int i = 0; i < players.size(); i++) {
//    //                details.append(players.get(i).getUsername());
//    //                if (i != players.size() - 1) {
//    //                    details.append(", ");
//    //                }
//    //            }
//    //            savedGameInfoLabel = new Label(details.toString(), skin, "custom-label");
//    //            savedGameInfoLabel.setFontScale(0.6f);
//    //        } else {
//    //            savedGameInfoLabel = new Label("No saved game found.", skin, "custom-label");
//    //        }
//    //        savedGameInfoLabel.setColor(Color.BLUE);
//        TextButton backButton = new TextButton("Back", skin, "custom-button");
//
//        float buttonWidth = (float) Gdx.graphics.getWidth() / 4;
//        float buttonHeight = (float) Gdx.graphics.getHeight() / 7;
//        float bottomPad = (float) Gdx.graphics.getHeight() / 24;
//        table.add(titleLabel).colspan(1).padBottom(bottomPad).row();
//        table.add(newGameButton).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
//        table.row();
//        table.add(lastGameButton).width(buttonWidth).height(buttonHeight).padBottom(bottomPad);
//        table.row();
//        table.add(savedGameInfoLabel).padBottom(bottomPad);
//        table.row();
//        table.add(backButton).width(buttonWidth).height(buttonHeight);
//
//        newGameButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                //MainApp.getInstance().setCurrentMenu(Menu.NewGameMenu);
//                MainApp.getInstance().setCurrentMenu(Menu.LobbyMenu);
//            }
//        });
//        newGameButton.getStyle().over = skin.getDrawable("button-normal-over");
//
//
//        lastGameButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                Result result = controller.loadGame();
//                if (result.isSuccessful()) {
//                    MainApp.getInstance().getCurrentGame().getMap().initializeShops();
//                    MainApp.getInstance().setCurrentMenu(Menu.GameMenu);
//                } else {
//                    showErrorDialog(stage, result.message());
//                }
//            }
//        });
//        lastGameButton.getStyle().over = skin.getDrawable("button-normal-over");
//
//        backButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
//
//            }
//        });
//        backButton.getStyle().over = skin.getDrawable("button-normal-over");
//
//        //background = GameAssetManager.getBackground();
//        Texture bg = GameAssetManager.getBackground();
//        Image bgImage = new Image(bg);
//        bgImage.setFillParent(true);
//        stage.addActor(bgImage);
//
//        stage.addActor(table);
//    }
    public void createUI() {
        Skin skin = GameAssetManager.skin;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setFillParent(true);

        Label titleLabel = new Label("PRE GAME MENU", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.getFont("custom-font"), Color.GOLD);
        titleLabel.setStyle(titleStyle);

        TextButton newGameButton = new TextButton("Play New Game", skin, "custom-button");
        TextButton backButton = new TextButton("Back", skin, "custom-button");

        float buttonWidth = (float) Gdx.graphics.getWidth() / 4;
        float buttonHeight = (float) Gdx.graphics.getHeight() / 7;
        float bottomPad = (float) Gdx.graphics.getHeight() / 24;

        table.add(titleLabel).center().padBottom(bottomPad).row();
        table.add(newGameButton).center().width(buttonWidth).height(buttonHeight).padBottom(bottomPad).row();
        table.add(backButton).center().width(buttonWidth).height(buttonHeight).padTop(30).row();

        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setCurrentMenu(Menu.LobbyMenu);
            }
        });
        newGameButton.getStyle().over = skin.getDrawable("button-normal-over");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
            }
        });
        backButton.getStyle().over = skin.getDrawable("button-normal-over");
        Texture bg = GameAssetManager.getBackground();
        Image bgImage = new Image(bg);
        bgImage.setFillParent(true);
        stage.addActor(bgImage);
        stage.addActor(table);

        // Fetch saved games
        Map<String, Object> params = new HashMap<>();
        MainApp.getInstance().getNetworkClient()
            .sendPost(null, "GameController", "getSavedGames", params, MainApp.getInstance().getLoggedInUser().getUsername())
//            .thenAccept(response -> {
////                if (response.getStatus() == 200) {
////                    Gson gson = new Gson();
////                    Type listType = new TypeToken<List<GameSummary>>() {}.getType();
////                    Map<String, Object> summariesObj =( Map<String, Object>) response.getBody();
////                    String json = gson.toJson(summariesObj);
////                    List<GameSummary> summaries = new Gson().fromJson(json, listType);
////                    Gdx.app.postRunnable(() -> updateSavedGamesUI(summaries));
////                }
//            });
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<GameSummary>>() {}.getType();
                    String json = gson.toJson(response.getBody()); // Serialize raw body
                    List<GameSummary> summaries = gson.fromJson(json, listType); // Deserialize to list
                    Gdx.app.postRunnable(() -> updateSavedGamesUI(summaries));
                }
            });

    }

    private void updateSavedGamesUI(List<GameSummary> summaries) {
        Skin skin = GameAssetManager.skin;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float summaryWidth = screenWidth / 2f; // You can tweak this for tighter or looser layout
        float buttonWidth = screenWidth / 7f;
        float buttonHeight = screenHeight / 14f;
        float scrollHeight = screenHeight / 2.5f;
        if (summaries == null || summaries.isEmpty()) {
            Label noGamesLabel = new Label("No saved games found.", skin, "custom-label");
            table.add(noGamesLabel).center().padTop(20).row();
            return;
        }
        Table savedGamesTable = new Table();
        savedGamesTable.top().center().pad(10);


        for (GameSummary summary : summaries) {
            String labelText = "Owner: " + summary.getOwnerUsername() +
                "\nPlayers: " + String.join(", ", summary.getPlayerUsernames()) +
                "\nTime: " + summary.getLastSavedDate();

            System.out.println("formattedTime"+ summary.getLastSavedDate());
            Table gameEntryTable = new Table();
            gameEntryTable.center().padBottom(20);

            Label gameLabel = new Label(labelText, skin, "custom-label");
            gameLabel.setWrap(true);
            gameLabel.setAlignment(Align.center);
            gameLabel.pack();

            TextButton loadButton = new TextButton("Load", skin, "custom-button");
            loadButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // controller.requestToLoadGame(summary.getGameId());
                    List<String> dummyPlayers = summary.getPlayerUsernames();
                    requestToLoadGame(summary.getGameId(), dummyPlayers);
                }
            });
            loadButton.getStyle().over = skin.getDrawable("button-normal-over");

            gameEntryTable.add(gameLabel).width(summaryWidth).row();
            gameEntryTable.add(loadButton).width(buttonWidth).height(buttonHeight).padTop(10).row();

            savedGamesTable.add(gameEntryTable).center().row();
        }

        ScrollPane scrollPane = new ScrollPane(savedGamesTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);

        table.add(scrollPane)
            .width(screenWidth / 1.3f)
            .height(scrollHeight)
            .padTop(10)
            .padBottom(20)
            .row();
    }

    private void requestToLoadGame(String gameId, List<String> playerNames) {
        String username = MainApp.getInstance().getLoggedInUser().getUsername();

        Map<String, Object> params = new HashMap<>();
        params.put("gameId", gameId);
        NetworkClient client = MainApp.getInstance().getNetworkClient();


        System.out.println("Sending createGameOnServer request: " + params);
        client.sendPost(
            null,
            "PreGameMenuController",
            "loadGame",
            params,
            username
        ).thenAccept(response -> {
            System.out.println("Response received");
            if (response.getStatus() == 200) {

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
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
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


//    public TextButton getPlayButton() {
//        return playButton;
//    }
//
//    public String getSelectedHero() {
//        return (String) selectHero.getSelected().toString();
//    }
//
//    public String getSelectedWeapon() {
//        return selectWeapon.getSelected().toString();
//    }
//
//    public String getSelectedDuration() {
//        return selectDuration.getSelected().toString();
//    }

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
//        Result canUseCommand = controller.checkEnergy();
//        if ((matcher = GameMenuCommands.NEXT_TURN.getMatcher(input)) != null) {
//            System.out.println(controller.nextTurn(scanner));
//        } else if ((matcher = GameMenuCommands.TERMINATE_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.startForceTerminateVote(scanner));
//        } else if ((matcher = GameMenuCommands.EXIT_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.exitGame());
//        } else if ((matcher = GameMenuCommands.LOAD_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.loadGame());
//        } else if ((matcher = GameMenuCommands.NEW_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.createGame(matcher.group("users"), scanner));
//        } else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
//            System.out.println(controller.enterMenu(matcher.group("menuName")));
//        } else if ((matcher = GameMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
//            System.out.println(controller.showCurrentMenu());
//        } else if ((matcher = GameMenuCommands.EXIT.getMatcher(input)) != null) {
//            controller.menuExit();
//        } else if ((matcher = GameMenuCommands.CHEAT_SET_SKILL.getMatcher(input)) != null) {
//            System.out.println(controller.cheatSetSkill(matcher.group("skill"), matcher.group("number")));
//        } else if ((matcher = GameMenuCommands.SHOW_MONEY.getMatcher(input)) != null) {
//            System.out.println(controller.showMoney());
//        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
//            System.out.println(controller.cheatAdvanceDate(matcher.group("number")));
//        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_TIME.getMatcher(input)) != null) {
//            System.out.println(controller.cheatAdvanceTime(matcher.group("number")));
//        } else if (!canUseCommand.isSuccessful()) {  /////////////////////////////////////////////////////////////////////////
//            System.out.println(canUseCommand);
//        } else if ((matcher = StoreMenuCommands.BUILD_HABITAT.getMatcher(input)) != null) {
//            System.out.println(storeController.buyFromCarpenter(matcher.group("name").trim(), matcher.group("x"), matcher.group("y")));
//        } else if ((matcher = StoreMenuCommands.BUY_ANIMAL.getMatcher(input)) != null) {
//            System.out.println(storeController.buyAnimal(matcher.group("animal").trim(), matcher.group("name")));
//        } else if ((matcher = StoreMenuCommands.SHOW_ALL_PRODUCTS.getMatcher(input)) != null) {
//            System.out.println(storeController.showAllProducts());
//        } else if ((matcher = StoreMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.getMatcher(input)) != null) {
//            System.out.println(storeController.showAllAvailableProducts());
//        } else if ((matcher = StoreMenuCommands.PURCHASE.getMatcher(input)) != null) {
//            String product = matcher.group("product").trim();
//            String countStr = matcher.group("count");
//            int count = (countStr != null) ? Integer.parseInt(countStr) : 1;
//            System.out.println(storeController.purchase(product, count));
//        }  else if ((matcher = StoreMenuCommands.UPGRADE_TOOL.getMatcher(input)) != null) {
//            System.out.println(storeController.upgradeTool(matcher.group("tool").trim()));
//        } else if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
//            System.out.println(controller.cheatAddMoney(matcher.group("count")));
//        }
//        /// ////friendship
//        else if ((matcher = GameMenuCommands.FRIEND_SHIP.getMatcher(input)) != null) {
//            System.out.println(controller.showFriendships());
//        } else if ((matcher = GameMenuCommands.SEND_GIFT.getMatcher(input)) != null) {
//            System.out.println(controller.sendGift(matcher.group("username"), matcher.group("item").trim(), matcher.group("amount")));
//        } else if ((matcher = GameMenuCommands.LIST_GIFT.getMatcher(input)) != null) {
//            System.out.println(controller.listGift());
//        } else if ((matcher = GameMenuCommands.RATE_GIFTS.getMatcher(input)) != null) {
//            System.out.println(controller.rateGifts(matcher.group("gift"), matcher.group("rate")));
//        } else if ((matcher = GameMenuCommands.GIFT_HISTORY.getMatcher(input)) != null) {
//            System.out.println(controller.giftHistory(matcher.group("username")));
//        } else if ((matcher = GameMenuCommands.FLOWER_SEND.getMatcher(input)) != null) {
//            System.out.println(controller.sendFlower(matcher.group("username")));
//        } else if ((matcher = GameMenuCommands.SELL_ANIMAL.getMatcher(input)) != null) {
//            System.out.println(controller.sellAnimal(matcher.group("name")));
//        }else if ((matcher = StoreMenuCommands.SHIPPING_BIN.getMatcher(input)) != null) {
//            String productString = matcher.group("product").trim();
//            String countString = matcher.group("count");
//            int count = (countString != null) ? Integer.parseInt(countString) : -1;
//            System.out.println(storeController.placeInShippingBin(productString, count));
//        } else if ((matcher = GameMenuCommands.SHOW_PRODUCTS.getMatcher(input)) != null) {
//            System.out.println(controller.showAnimalProducts());
//        } else if ((matcher = GameMenuCommands.COLLECT_PRODUCTS.getMatcher(input)) != null) {
//            System.out.println(controller.collectProduct(matcher.group("name")));
//        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMatcher(input)) != null) {
//            System.out.println(controller.shepherdAnimal(matcher.group("name"), matcher.group("x"), matcher.group("y")));
//        } else if ((matcher = GameMenuCommands.FEED_HAY.getMatcher(input)) != null) {
//            System.out.println(controller.feedHay(matcher.group("name")));
//        } else if ((matcher = GameMenuCommands.CHEAT_ANIMAL_FRIENDSHIP.getMatcher(input)) != null) {
//            System.out.println(controller.cheatAnimalFriendship(matcher.group("name"), matcher.group("amount")));
//        } else if ((matcher = GameMenuCommands.ANIMALS_INFO.getMatcher(input)) != null) {
//            System.out.println(controller.showOwnedAnimals());
//        } else if ((matcher = GameMenuCommands.PET.getMatcher(input)) != null) {
//            System.out.println(controller.petAnimal(matcher.group("name")));
//        } else if ((matcher = GameMenuCommands.CHeat_THOR.getMatcher(input)) != null) {
//            System.out.println(controller.cheatThor(matcher.group("x"), matcher.group("y")));
//        } else if ((matcher = GameMenuCommands.CHEAT_ENERGY.getMatcher(input)) != null) {
//            System.out.println(controller.cheatChangeEnergy(matcher.group("value")));
//        } else if ((matcher = GameMenuCommands.CHEAT_UNLIMITED_ENERGY.getMatcher(input)) != null) {
//            System.out.println(controller.cheatUnlimitedEnergy());
//        } else if ((matcher = GameMenuCommands.ENERGY.getMatcher(input)) != null) {
//            System.out.println(controller.showEnergy());
//        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER.getMatcher(input)) != null) {
//            System.out.println(controller.cheatChangeWeather(matcher.group("weather")));
//        } else if ((matcher = GameMenuCommands.WEATHER.getMatcher(input)) != null) {
//            System.out.println(controller.showCurrentWeather());
//        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMatcher(input)) != null) {
//            System.out.println(controller.showTomorrowWeather());
//        } else if (input.equals("season")) {
//            System.out.println(controller.printSeason());
//        } else if (input.equals("time")) {
//            System.out.println(controller.printHour());
//        } else if (input.equals("date")) {
//            System.out.println(controller.printDate());
//        } else if (input.equals("datetime")) {
//            System.out.println(controller.printDateTime());
//        } else if (input.matches("^day\\s+of\\s+the\\s+week$")) {
//            System.out.println(controller.printDayOfWeek());
//        } else if ((matcher = GameMenuCommands.NEXT_TURN.getMatcher(input)) != null) {
//            System.out.println(controller.nextTurn(scanner));
//        } else if ((matcher = GameMenuCommands.TERMINATE_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.startForceTerminateVote(scanner));
//        } else if ((matcher = GameMenuCommands.EXIT_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.exitGame());
//        } else if ((matcher = GameMenuCommands.LOAD_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.loadGame());
//        } else if ((matcher = GameMenuCommands.NEW_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.createGame(matcher.group("users"), scanner));
//        } else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
//            System.out.println(controller.createGame(matcher.group("users"), scanner));
//        } else if ((matcher = GameMenuCommands.PRINT_GAME.getMatcher(input)) != null) {
//            System.out.println(controller.printMap(matcher.group("x"), matcher.group("y"), matcher.group("size")).message());
//        } else if ((matcher = GameMenuCommands.HELP_READ_MAP.getMatcher(input)) != null) {
//            controller.helpReadMap();
//        } else if ((matcher = GameMenuCommands.WALK.getMatcher(input)) != null) {
//            controller.walkTo(matcher.group("x"), matcher.group("y"), scanner);
//        } else if ((matcher = GameMenuCommands.CAFTINFO.getMatcher(input)) != null) {
//            controller.printCraftInfo(matcher.group("craftname"));
//        } else if ((matcher = GameMenuCommands.TREEINFO.getMatcher(input)) != null) {
//            controller.printTreeInfo(matcher.group("treename"));
//        } else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
//            System.out.println(controller.enterMenu(matcher.group("menuName")));
//        } else if ((matcher = GameMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
//            System.out.println(controller.showCurrentMenu());
//        } else if ((matcher = GameMenuCommands.EXIT.getMatcher(input)) != null) {
//            controller.menuExit();
//        } else if ((matcher = GameMenuCommands.SHOW_INVENTORY.getMatcher(input)) != null) {
//            System.out.println(controller.showInventory());
//        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMatcher(input)) != null) {
//            String itemName = matcher.group("itemName");
//            String countString = matcher.group("number");
//            int count;
//            if (countString == null) count = 1000;
//            else count = Integer.parseInt(countString);
//            System.out.println(controller.trashInventory(itemName, count));
//        } else if ((matcher = GameMenuCommands.EQUIP_TOOL.getMatcher(input)) != null) {
//            String toolName = matcher.group("toolName");
//            System.out.println(controller.equipTool(toolName));
//        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TOOL.getMatcher(input)) != null) {
//            System.out.println(controller.showCurrentTool());
//        } else if ((matcher = GameMenuCommands.SHOW_AVAILABLE_TOOLS.getMatcher(input)) != null) {
//            System.out.println(controller.showAllTools());
//        } else if ((matcher = GameMenuCommands.TOOL_UPGRADE.getMatcher(input)) != null) {
//            //COMPLETE THIS AFTER MAKING SHOP
//        } else if ((matcher = GameMenuCommands.USE_TOOL.getMatcher(input)) != null) {
//            System.out.println(controller.useTool(matcher.group("direction")));
//        } else if ((matcher = GameMenuCommands.FISH.getMatcher(input)) != null) {
//            String fishingPole = matcher.group("fishingPole");
//            System.out.println(controller.fish(fishingPole));
//        } else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
//            String itemName = matcher.group("itemName");
//            int count = Integer.parseInt(matcher.group("count"));
//            System.out.println(controller.cheatAddItem(itemName, count));
//        } else if ((matcher = GameMenuCommands.PLANT.getMatcher(input)) != null) {
//            System.out.println(controller.plantGrowable(matcher.group("seedName"), matcher.group("direction")).message());
//        } else if ((matcher = GameMenuCommands.SHOWPLANT.getMatcher(input)) != null) {
//            System.out.println(controller.showPlant(matcher.group("x"), matcher.group("y")).message());
//        } else if ((matcher = GameMenuCommands.FERTALISE.getMatcher(input)) != null) {
//            System.out.println(controller.fertalizeGrowable(matcher.group("fertilizer"), matcher.group("direction")).message());
//        } else if ((matcher = GameMenuCommands.BUILDGREENHOUSE.getMatcher(input)) != null) {
//            System.out.println(controller.buildGreenHouse().message());
//        } else if ((matcher = HouseMenuCommands.SHOW_RECIPIES.getMatcher(input)) != null) {
//            System.out.println(houseController.showRecipes());
//        } else if ((matcher = HouseMenuCommands.CRAFT.getMatcher(input)) != null) {
//            System.out.println(houseController.craft(matcher.group("itemName")));
//        } else if ((matcher = HouseMenuCommands.PLACE_ITEM.getMatcher(input)) != null) {
//            System.out.println(houseController.placeItem(matcher.group("itemName"), matcher.group("direction")));
//        } else if ((matcher = GameMenuCommands.TALK.getMatcher(input)) != null) {
//            System.out.println(controller.talk(matcher.group("username"), matcher.group("message")).message());
//        } else if ((matcher = GameMenuCommands.SHOW_TALK_HISTORY.getMatcher(input)) != null) {
//            System.out.println(controller.showTalkHistory(matcher.group("username")));
//        } else if ((matcher = GameMenuCommands.HUG.getMatcher(input)) != null) {
//            System.out.println(controller.hug(matcher.group("username")));
//        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMatcher(input)) != null) {
//            System.out.println(controller.askMarriage(matcher.group("username"), matcher.group("ring")));
//        } else if ((matcher = GameMenuCommands.RESPOND.getMatcher(input)) != null) {
//            System.out.println(controller.respondToMarriage(matcher.group("response"), matcher.group("username")));
//        } else if ((matcher = GameMenuCommands.START_TRADE.getMatcher(input)) != null) {
//            controller.startTrade();
//        } else if ((matcher = GameMenuCommands.CHEAT_WALK.getMatcher(input)) != null) {
//            System.out.println(controller.cheatWalk(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))).message());
//        } else if ((matcher = GameMenuCommands.CHEAT_SET_SKILL.getMatcher(input)) != null) {
//            System.out.println(controller.cheatSetSkill(matcher.group("skill"),matcher.group("number")));
//        }
//        else if((matcher = GameMenuCommands.CHEAT_SET_LEVEL.getMatcher(input)) != null) {
//            System.out.println(controller.cheatSetFriendshipLevel(Integer.parseInt(matcher.group("level")),matcher.group("username")));
//        }
//        else if ((matcher = GameMenuCommands.MEET_NPC.getMatcher(input)) != null) {
//            String npcName = matcher.group("npcName");
//            System.out.println(controller.meetNPC(npcName));
//        } else if ((matcher = GameMenuCommands.GIFT_NPC.getMatcher(input)) != null) {
//            String npcName = matcher.group("npcName");
//            String itemName = matcher.group("item");
//            System.out.println(controller.giftNPC(npcName, itemName));
//        } else if ((matcher = GameMenuCommands.NPC_FRIENDSHIP_LIST.getMatcher(input)) != null) {
//            System.out.println(controller.npcFriendshipList());
//        } else if ((matcher = GameMenuCommands.NPC_QUEST_LIST.getMatcher(input)) != null) {
//            System.out.println(controller.npcQuestList());
//        } else if ((matcher = GameMenuCommands.DO_MISSION.getMatcher(input)) != null) {
//            int missionIndex = Integer.parseInt(matcher.group("index"));
//            System.out.println(controller.doMission(missionIndex));
//        } else if ((matcher = GameMenuCommands.PICK_FOOD_FROM_FRIDGE.getMatcher(input)) != null) {
//            String itemName = matcher.group("item").trim();
//            System.out.println(controller.pickFoodFromFridge(itemName));
//        } else if ((matcher = GameMenuCommands.PUT_FOOD_IN_FRIDGE.getMatcher(input)) != null) {
//            String itemName = matcher.group("item").trim();
//            System.out.println(controller.putFoodInFridge(itemName));
//        } else if ((matcher = GameMenuCommands.SHOW_COOKING_RECIPES.getMatcher(input)) != null) {
//            System.out.println(controller.showCookingRecipes());
//        } else if ((matcher = GameMenuCommands.COOK.getMatcher(input)) != null) {
//            String recipeName = matcher.group("recipe").trim();
//            System.out.println(controller.cook(recipeName));
//        } else if ((matcher = GameMenuCommands.EAT.getMatcher(input)) != null) {
//            String food = matcher.group("food").trim();
//            System.out.println(controller.eat(food));
//        } else if ((matcher = GameMenuCommands.ARTISAN_USE.getMatcher(input)) != null) {
//            String artisanName = matcher.group("artisanName").trim();
//            String itemName1 = matcher.group("itemName1");
//            if (itemName1 != null) itemName1 = itemName1.trim();
//            String itemName2 = matcher.group("itemName2");
//            if (itemName2 != null) itemName2 = itemName2.trim();
//            System.out.println(controller.artisanUse(artisanName, itemName1, itemName2, MainApp.getInstance().getCurrentGame().getMap()));
//        } else if ((matcher = GameMenuCommands.ARTISAN_GET.getMatcher(input)) != null) {
//            String artisanName = matcher.group("artisanName").trim();
//            System.out.println(controller.artisanGet(MainApp.getInstance().getCurrentGame().getMap(), artisanName));
//        }
//        else {
//            System.out.println("invalid command");
//        }

    }
}
