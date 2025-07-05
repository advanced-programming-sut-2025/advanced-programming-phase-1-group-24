package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.GameController;
import io.github.stardew.mini.Controller.StoreMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Menus.HouseMenuCommands;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.Places.Shop;
import io.github.stardew.mini.Model.Places.ShopItem;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameView implements Screen, InputProcessor, AppMenu {
    private Stage stage;
    private GameController controller;
    private SpriteBatch batch;
    private MapOfGame mapOfGame;
    private OrthographicCamera camera;
    private User currentPlayer;  //should change whenever currentPlayer in Game is changed
    private float stateTime = 0f;
    private boolean showFullMap = false;

    private TerminalWindow terminalWindow;
    private boolean terminalVisible = false;

    private Dialog animalMenuDialog;
    private Animal selectedAnimal;

    private float moveCooldown = 0f;
    private static final float MOVE_INTERVAL = 0.1f; // seconds between steps

    private Dialog shopMenuDialog;
    private Shop selectedShop;
    private Dialog shopPurchaseDialog;
    private ShopItem selectedShopItem;
    private int purchaseQuantity = 1;

    private StoreMenuController storeController;
   // private boolean isShowingErrorDialog = false;
   private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();


    public GameView(GameController controller) {
        this.controller = controller;
        storeController = new StoreMenuController();
        controller.setView(this);
        this.batch = MainApp.getBatch();
        this.mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        this.currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!terminalVisible) {
            //&&  button == Input.Buttons.RIGHT) {
            // Convert screen coordinates to world coordinates
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

            // Convert world coordinates to tile coordinates
            int tileX = (int) (worldCoords.x / GameAssetManager.TILE_SIZE);
            int tileY = mapOfGame.getHeight() - (int) (worldCoords.y / GameAssetManager.TILE_SIZE) - 1;

            // Check if click is within map bounds
            if (tileX >= 0 && tileY >= 0 && tileX < mapOfGame.getWidth() && tileY < mapOfGame.getHeight()) {
                Tile tile = mapOfGame.getMap()[tileY][tileX];
                if (tile != null && tile.getContainedAnimal() != null) {
                    selectedAnimal = tile.getContainedAnimal();

                    // Convert screen coordinates to stage coordinates
                    Vector3 stageCoords = stage.getViewport().unproject(new Vector3(screenX, screenY, 0));

                    // Center dialog around mouse position
                    animalMenuDialog.setPosition(
                        stageCoords.x - animalMenuDialog.getWidth() / 2,
                        stageCoords.y - animalMenuDialog.getHeight() / 2
                    );
                    animalMenuDialog.setVisible(true);
                    animalMenuDialog.show(stage);
                    Gdx.input.setInputProcessor(stage);
                    return true;
                }
                if (tile != null && tile.getType() == TileType.SHOP) {
                    selectedShop = mapOfGame.getShopAtPosition(tileX, tileY);
                    if (selectedShop != null) {
                        Vector3 stageCoords = stage.getViewport().unproject(new Vector3(screenX, screenY, 0));
                        showShopMenuDialog(stageCoords.x, stageCoords.y);
                        return true;
                    }
                }

//                if(){
//
//                }
            }
        }
        return false;
    }


private void showShopMenuDialog(float x, float y) {
    shopMenuDialog.clear();
    shopMenuDialog.getTitleLabel().setText(selectedShop.getShopName());

    // Create a table for item buttons
    Table itemTable = new Table();
    itemTable.top(); // align buttons to the top
    itemTable.defaults().pad(5).fillX();

    for (ShopItem item : selectedShop.getProducts()) {
        boolean isAvailable = item.getDailyLimit() - item.getSoldToday() > 0;

        TextButton itemButton = new TextButton(item.getName(), GameAssetManager.skin);
        itemButton.setDisabled(!isAvailable);
        itemButton.getLabel().setColor(isAvailable ? Color.WHITE : Color.GRAY);

        itemButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedShopItem = item;
                purchaseQuantity = 1;
                showPurchaseDialog();
                shopMenuDialog.hide();
            }
        });

        itemTable.add(itemButton).expandX().fillX().row();
    }

    // Wrap the table in a ScrollPane
    ScrollPane scrollPane = new ScrollPane(itemTable, GameAssetManager.skin);
    scrollPane.setFadeScrollBars(false);
    scrollPane.setScrollingDisabled(true, false); // only vertical scroll
    scrollPane.setForceScroll(false, true);
    scrollPane.layout(); // force layout of scrollPane

    // Add scrollPane to dialog content
    Table content = shopMenuDialog.getContentTable();
    content.clear();
    content.defaults().pad(10);
    content.add(scrollPane).width(gameWidth/2).height(gameHeight/2); // adjust as needed

    shopMenuDialog.add(content);
    shopMenuDialog.pack();
    shopMenuDialog.setPosition(x - shopMenuDialog.getWidth() / 2, y - shopMenuDialog.getHeight() / 2);

    shopMenuDialog.setVisible(true);
    shopMenuDialog.show(stage);
    Gdx.input.setInputProcessor(stage);
}



    private void showPurchaseDialog() {
        shopPurchaseDialog.clear();
        shopPurchaseDialog.getTitleLabel().setText("Purchase " + selectedShopItem.getName());

        Table content = shopPurchaseDialog.getContentTable();
        content.clear();
        content.defaults().pad(10);

        Label quantityLabel = new Label("Quantity: " + purchaseQuantity, GameAssetManager.skin);
        TextButton plusButton = new TextButton("+", GameAssetManager.skin);
        TextButton minusButton = new TextButton("-", GameAssetManager.skin);
        TextButton buyButton = new TextButton("Buy", GameAssetManager.skin);
        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin);

        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseQuantity++;
                quantityLabel.setText("Quantity: " + purchaseQuantity);
            }
        });

        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (purchaseQuantity > 1) {
                    purchaseQuantity--;
                    quantityLabel.setText("Quantity: " + purchaseQuantity);
                }
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                storeController.purchase(selectedShopItem, purchaseQuantity);
                //buyItem(currentPlayer, selectedShopItem, purchaseQuantity);
                shopPurchaseDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shopPurchaseDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        content.add(quantityLabel).colspan(2).row();
        content.add(minusButton).padRight(5);
        content.add(plusButton).row();
        content.add(buyButton).colspan(2).row();
        content.add(cancelButton).colspan(2);

        shopPurchaseDialog.add(content);
        shopPurchaseDialog.pack();
        shopPurchaseDialog.setPosition(
            (Gdx.graphics.getWidth() - shopPurchaseDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - shopPurchaseDialog.getHeight()) / 2
        );

        shopPurchaseDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    public void createUI() {
        createTerminal();

        createAnimalDialog();

        createShopMenusDialogs();
    }

    private void createTerminal() {
        terminalWindow = new TerminalWindow(GameAssetManager.skin, this);
        terminalWindow.setVisible(false);
        stage.addActor(terminalWindow);
    }

    private void createShopMenusDialogs() {
        shopMenuDialog = new Dialog("Shop Menu", GameAssetManager.skin, "dialog");
        shopMenuDialog.setKeepWithinStage(true);
        shopMenuDialog.setMovable(false);
        shopMenuDialog.setVisible(false);
        stage.addActor(shopMenuDialog);

        shopPurchaseDialog = new Dialog("Purchase", GameAssetManager.skin, "dialog");
        shopPurchaseDialog.setKeepWithinStage(true);
        shopPurchaseDialog.setMovable(false);
        shopPurchaseDialog.setVisible(false);
        stage.addActor(shopPurchaseDialog);
    }

    private void createAnimalDialog() {
        // Create the animal menu dialog (initially hidden)
        animalMenuDialog = new Dialog("Animal Menu", GameAssetManager.skin, "dialog") {
            @Override
            protected void result(Object object) {
                handleAnimalMenuChoice(object.toString());
            }
        };

        animalMenuDialog.getContentTable().defaults().pad(10);

        // Add buttons with their result objects
        TextButton feedButton = new TextButton("Feed", GameAssetManager.skin);
        feedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("feed");
            }
        });

        TextButton petButton = new TextButton("Pet", GameAssetManager.skin);
        petButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("pet");
            }
        });

        TextButton releaseButton = new TextButton("Release", GameAssetManager.skin);
        releaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("release");
            }
        });

        TextButton sellButton = new TextButton("Sell", GameAssetManager.skin);
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("sell");
            }
        });

        TextButton collectButton = new TextButton("Collect Product", GameAssetManager.skin);
        collectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("collect");
            }
        });

        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);  // Return input to game
                selectedAnimal = null;
            }
        });

        animalMenuDialog.getContentTable().add(feedButton).row();
        animalMenuDialog.getContentTable().add(petButton).row();
        animalMenuDialog.getContentTable().add(releaseButton).row();
        animalMenuDialog.getContentTable().add(sellButton).row();
        animalMenuDialog.getContentTable().add(collectButton).row();
        animalMenuDialog.getContentTable().add(cancelButton);

        animalMenuDialog.setKeepWithinStage(true);
        animalMenuDialog.setMovable(false);
        animalMenuDialog.setVisible(false);  // Add this after creation
        stage.addActor(animalMenuDialog);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.M) {
            showFullMap = !showFullMap;  // toggle map mode
            setCameraPosition();         // update camera immediately
            return true;
        }

        if (showFullMap) return true;
        return false;
    }


    private void handleAnimalMenuChoice(String choice) {
        if (selectedAnimal == null) {
            System.out.println("animal is null");
            return;
        }
        System.out.println(selectedAnimal.getName());
        Result result;

        // Handle choices...
        switch (choice) {
            case "feed":
                result = controller.feedHay(selectedAnimal.getName());
                break;
            case "pet":
                result = controller.petAnimal(selectedAnimal.getName());
                break;
            case "release":
                result = new Result(true, "");
                //controller.releaseAnimal(selectedAnimal);
                break;
            case "sell":
                result = controller.sellAnimal(selectedAnimal.getName());
                break;
            case "collect":
                result = controller.collectProduct(selectedAnimal.getName());
                break;
            case "cancel":
                result = new Result(true, "");
                // Do nothing
                break;
            default:
                result = new Result(false, choice);
                break;
        }
       // if (!result.isSuccessful()) {
            showErrorDialog(stage, result.message());
       // }
        animalMenuDialog.hide();
       // Gdx.input.setInputProcessor(this);  // Return input to game
        selectedAnimal = null;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition();
        camera.update();
        Gdx.input.setInputProcessor(this);
        batch.setProjectionMatrix(camera.combined);
        createUI();
    }
    @Override
    public void render(float v) {
        stateTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1); // clear with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setCameraPosition();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // --- DRAW GAME WORLD ---
        Tile[][] tiles = mapOfGame.getMap();
        int tileSize = GameAssetManager.TILE_SIZE;

        int rows = tiles.length;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                TileType tile = tiles[y][x].getType();
                if (tile != null && tile.getTexture() != null) {
                    batch.draw(tile.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
            }
        }

        GreenHouse greenHouseTile = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getGreenHouse();
        int drawX = (greenHouseTile.getX() - 1) * tileSize;
        int drawY = (mapOfGame.getHeight() - greenHouseTile.getY() - greenHouseTile.getHeight() - 1) * tileSize;
        batch.draw(GameAssetManager.greenhouseTexture, drawX, drawY, 8 * tileSize, 7 * tileSize);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null && tile.getContainedAnimal() != null) {
                    batch.draw(tile.getContainedAnimal().getAnimalType().getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
            }
        }

        drawPlayer();

        if (!showFullMap && !terminalVisible) {
            moveCooldown -= v;
            if (moveCooldown <= 0f) {
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    if (tryMove(0, -1, 3)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    if (tryMove(0, +1, 1)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    if (tryMove(-1, 0, 4)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    if (tryMove(+1, 0, 2)) moveCooldown = MOVE_INTERVAL;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            terminalVisible = !terminalVisible;
            terminalWindow.setVisible(terminalVisible);

            if (terminalVisible) {
                Gdx.input.setInputProcessor(stage);
                stage.setKeyboardFocus(terminalWindow.getInputField());
                terminalWindow.getInputField().setText("");
                terminalWindow.getInputField().setCursorPosition(0);
            } else {
                Gdx.input.setInputProcessor(this);
            }
        }

        batch.end(); // ✅ this must come BEFORE stage rendering

        // --- DRAW UI ---
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();

        // Update stage viewport
        stage.getViewport().update(width, height, true);
    }
    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }




//    @Override
//    public void render(float v) {
//        stateTime += Gdx.graphics.getDeltaTime();
//        Gdx.gl.glClearColor(0, 0, 0, 1); // clear with black
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        setCameraPosition();
//
//        batch.setProjectionMatrix(camera.combined);
//
//
//        batch.begin();
//
//        Tile[][] tiles = mapOfGame.getMap();
//        int tileSize = GameAssetManager.TILE_SIZE;
//
//        int rows = tiles.length;
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < tiles[0].length; x++) {
//                TileType tile = tiles[y][x].getType();
//                if (tile != null && tile.getTexture() != null) {
//                    batch.draw(tile.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
//                }
//            }
//        }
//
//        GreenHouse greenHouseTile = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getGreenHouse();
//
//        int drawX = (greenHouseTile.getX() - 1) * tileSize;
//        int drawY = (MainApp.getInstance().getCurrentGame().getMap().getHeight() - greenHouseTile.getY() - greenHouseTile.getHeight() - 1) * tileSize;
//
//        batch.draw(
//            GameAssetManager.greenhouseTexture,
//            drawX,
//            drawY,
//            8 * tileSize,
//            7 * tileSize
//        );
//
//        for (int y = 0; y < rows; y++) {
//            for (int x = 0; x < tiles[0].length; x++) {
//                Tile tile = tiles[y][x];
//                if (tile != null && tile.getContainedAnimal() != null) {
//                    batch.draw(tile.getContainedAnimal().getAnimalType().getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
//                }
//            }
//        }
//
//        drawPlayer();
//        if (!showFullMap && !terminalVisible) {
//            moveCooldown -= v;
//            if (moveCooldown <= 0f) {
//                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//                    if (tryMove(0, -1, 3)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                    if (tryMove(0, +1, 1)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                    if (tryMove(-1, 0, 4)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                    if (tryMove(+1, 0, 2)) moveCooldown = MOVE_INTERVAL;
//                }
//            }
//        }
//        setCameraPosition();
//
//
//        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            terminalVisible = !terminalVisible;
//            terminalWindow.setVisible(terminalVisible);
//
//            if (terminalVisible) {
//                Gdx.input.setInputProcessor(stage);
//                stage.setKeyboardFocus(terminalWindow.getInputField());
//                terminalWindow.getInputField().setText("");
//                terminalWindow.getInputField().setCursorPosition(0);
//            } else {
//                Gdx.input.setInputProcessor(this); // return control to game
//            }
//        }
//
//        batch.end();
//        stage.act(Gdx.graphics.getDeltaTime());
//        stage.draw();
//    }


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

    private void drawPlayer() {
        if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

        Tile tile = currentPlayer.getCurrentTile();
        int tileSize = GameAssetManager.TILE_SIZE;

        int tileX = tile.getX();
        int tileY = tile.getY();

        // Flip the Y-axis to match rendering coordinates
        int drawX = tileX * tileSize;
        int drawY = (mapOfGame.getMap().length - tileY - 1) * tileSize;

        // Clamp moveDirection to valid index range
        int moveDirection = MathUtils.clamp(currentPlayer.getMovingDirection(), 0, GameAssetManager.playerAnimations.size() - 1);

        Animation<TextureRegion> currentAnimation = GameAssetManager.playerAnimations.get(moveDirection);
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // Draw player with height of 2 tiles
        batch.draw(currentFrame, drawX, drawY, tileSize, tileSize * 2);
    }


    private void setCameraPosition() {
        if (showFullMap) {
            // Full map view
            float mapWidth = MainApp.getInstance().getCurrentGame().getMap().getMap()[0].length * GameAssetManager.TILE_SIZE;
            float mapHeight = MainApp.getInstance().getCurrentGame().getMap().getMap().length * GameAssetManager.TILE_SIZE;

            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);

            // Zoom out to fit entire map
            float zoomX = mapWidth / camera.viewportWidth;
            float zoomY = mapHeight / camera.viewportHeight;
            camera.zoom = Math.max(zoomX, zoomY);

        } else {
            if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

            camera.zoom = 1f;
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            int tileSize = GameAssetManager.TILE_SIZE;

            // Get player's tile position
            Tile tile = currentPlayer.getCurrentTile();
            float drawX = tile.getX() * tileSize + tileSize / 2f;
            float drawY = (mapOfGame.getMap().length - tile.getY() - 1) * tileSize + tileSize / 2f;

            // Set camera to center on that position
            camera.position.set(drawX, drawY, 0);
            camera.update();
        }
    }


    private boolean tryMove(int dx, int dy, int direction) {
        int x = currentPlayer.getCurrentTile().getX();
        int y = currentPlayer.getCurrentTile().getY();
        int newX = x + dx;
        int newY = y + dy;

        if (newX >= 0 && newY >= 0 &&
            newY < mapOfGame.getMap().length &&
            newX < mapOfGame.getMap()[0].length &&
            mapOfGame.getMap()[newY][newX].getisWalkable() &&
            !(MainApp.getInstance().getCurrentGame().getMap().isInsideAnyFarm(newX, newY) != null &&
                !(mapOfGame.getMap()[newY][newX].getTileOwner().equals(currentPlayer.getUsername()) ||
                    (currentPlayer.getPartner() != null &&
                        mapOfGame.getMap()[newY][newX].getTileOwner().equals(currentPlayer.getPartner().getUsername()))))) {

            currentPlayer.setCurrentTile(mapOfGame.getMap()[newY][newX]);
            currentPlayer.setEnergy((int) (currentPlayer.getEnergy() - (0.0005 * currentPlayer.getEnergy())));
            int newTurnEnergy = Math.max(0, (int) (currentPlayer.getCurrentTurnEnergy() - (0.0005 * currentPlayer.getEnergy())));
            currentPlayer.setCurrentTurnEnergy(newTurnEnergy);
            currentPlayer.setMovingDirection(direction);
            setCameraPosition();
            return true;
        }

        return false;
    }

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        //        Result canUseCommand = controller.checkEnergy();
        if ((matcher = GameMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else if ((matcher = GameMenuCommands.WALK.getMatcher(input)) != null) {
            controller.walkTo(matcher.group("x"), matcher.group("y"), scanner);
        } else if ((matcher = GameMenuCommands.CHEAT_SET_SKILL.getMatcher(input)) != null) {
            System.out.println(controller.cheatSetSkill(matcher.group("skill"), matcher.group("number")));
        } else if ((matcher = GameMenuCommands.SHOW_MONEY.getMatcher(input)) != null) {
            System.out.println(controller.showMoney());
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceDate(matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_TIME.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceTime(matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddMoney(matcher.group("count")));
        } else if ((matcher = GameMenuCommands.CHEAT_ANIMAL_FRIENDSHIP.getMatcher(input)) != null) {
            System.out.println(controller.cheatAnimalFriendship(matcher.group("name"), matcher.group("amount")));
        } else if ((matcher = GameMenuCommands.CHeat_THOR.getMatcher(input)) != null) {
            System.out.println(controller.cheatThor(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.CHEAT_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeEnergy(matcher.group("value")));
        } else if ((matcher = GameMenuCommands.CHEAT_UNLIMITED_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatUnlimitedEnergy());
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeWeather(matcher.group("weather")));
        } else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
            String itemName = matcher.group("itemName");
            int count = Integer.parseInt(matcher.group("count"));
            System.out.println(controller.cheatAddItem(itemName, count));
        } else if ((matcher = GameMenuCommands.CHEAT_WALK.getMatcher(input)) != null) {
            System.out.println(controller.cheatWalk(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))).message());
        } else if ((matcher = GameMenuCommands.CHEAT_SET_SKILL.getMatcher(input)) != null) {
            System.out.println(controller.cheatSetSkill(matcher.group("skill"), matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_SET_LEVEL.getMatcher(input)) != null) {
            System.out.println(controller.cheatSetFriendshipLevel(Integer.parseInt(matcher.group("level")), matcher.group("username")));
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
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
//        }
        else if ((matcher = GameMenuCommands.ANIMALS_INFO.getMatcher(input)) != null) {
            System.out.println(controller.showOwnedAnimals());
        }
//        else if ((matcher = GameMenuCommands.PET.getMatcher(input)) != null) {
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
        else {
            System.out.println("invalid command");
        }
    }


    //    public void createUI() {
//        terminalWindow = new TerminalWindow(GameAssetManager.skin, this);
//        terminalWindow.setVisible(false);
//        stage.addActor(terminalWindow);
//
//        // Create the animal menu dialog (initially hidden)
//        animalMenuDialog = new Dialog("Animal Menu", GameAssetManager.skin);
//        animalMenuDialog.getContentTable().defaults().pad(10);
//
//        animalMenuDialog.button("Feed", "feed");
//        animalMenuDialog.button("Pet", "pet");
//        animalMenuDialog.button("Release", "release");
//        animalMenuDialog.button("Sell", "sell");
//        animalMenuDialog.button("Collect Product", "collect");
//        animalMenuDialog.button("Cancel", "cancel");
//
//        animalMenuDialog.setVisible(false);
//        stage.addActor(animalMenuDialog);
//
//        animalMenuDialog.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String result = animalMenuDialog.getResult().toString();
//                handleAnimalMenuChoice(result);
//                animalMenuDialog.setVisible(false);
//                Gdx.input.setInputProcessor(GameView.this); // Return control to game
//            }
//        });
//    }
    public void showErrorDialog(Stage stage, String message) {
        Skin skin = GameAssetManager.skin;

        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                // Optional: Handle result
            }
        };

        dialog.setBackground("window"); // make sure "window" drawable exists in your skin

        Label messageLabel = new Label(message, skin, "custom-label");
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        messageLabel.setFontScale(0.7f); // Optional

        TextButton okButton = new TextButton("OK", skin, "custom-button");
        okButton.pad(10f);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide(); // Close dialog
                Gdx.input.setInputProcessor(GameView.this); // return control to GameView if needed
            }
        });

        Table contentTable = new Table();
        contentTable.defaults().pad(10f);
        contentTable.add(messageLabel).width(stage.getWidth() * 0.5f).row();
        contentTable.add(okButton).center();

        dialog.getContentTable().clear();
        dialog.getContentTable().add(contentTable).expand().fill();

        dialog.setMovable(false);
        dialog.setModal(true);
        dialog.setResizable(false);

        float dialogWidth = stage.getWidth() * 0.4f;
        float dialogHeight = stage.getHeight() * 0.25f;
        dialog.setSize(dialogWidth, dialogHeight);
        dialog.setPosition(
            (stage.getWidth() - dialogWidth) / 2f,
            (stage.getHeight() - dialogHeight) / 2f
        );

        stage.addActor(dialog);
        Gdx.input.setInputProcessor(stage); // 🔥 Important: Enable input for stage
    }


}
//    private void showShopMenuDialog(float x, float y) {
//        shopMenuDialog.clear();
//        shopMenuDialog.getTitleLabel().setText(selectedShop.getShopName());
//
//        Table content = shopMenuDialog.getContentTable();
//        content.clear();
//        content.defaults().pad(5);
//
//        System.out.println(selectedShop.getShopName());
//        for (ShopItem item : selectedShop.getProducts()) {
//            System.out.println(item.getName());
//            boolean isAvailable = item.getDailyLimit() - item.getSoldToday() > 0;
//            TextButton itemButton = new TextButton(item.getName(), GameAssetManager.skin);
//            itemButton.setDisabled(!isAvailable);
//            itemButton.getLabel().setColor(isAvailable ? Color.WHITE : Color.GRAY);
//            itemButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    selectedShopItem = item;
//                    purchaseQuantity = 1;
//                    showPurchaseDialog();
//                    shopMenuDialog.hide();
//                }
//            });
//            content.add(itemButton).row();
//        }
//        shopMenuDialog.add(content);
//       // Add this line to force layout update
//        shopMenuDialog.pack(); // ⬅️ This resizes the dialog to fit the content
//
//       // Then set position
//        shopMenuDialog.setPosition(x - shopMenuDialog.getWidth() / 2, y - shopMenuDialog.getHeight() / 2);
//
//       // Show the dialog
//        shopMenuDialog.setVisible(true);
//        shopMenuDialog.show(stage);
//        Gdx.input.setInputProcessor(stage);
//    }
