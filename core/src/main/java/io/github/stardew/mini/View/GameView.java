package io.github.stardew.mini.View;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.stardew.mini.Controller.GameMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.InventoryAssets;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.FriendshipLevels;
import io.github.stardew.mini.Model.Growables.CropType;
import io.github.stardew.mini.Model.Growables.GrowableType;
import io.github.stardew.mini.Model.Growables.TreeType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.TimeAndDate;
import io.github.stardew.mini.Model.Tools.Tool;
import io.github.stardew.mini.Model.Tools.TrashCan;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;

//import java.awt.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class GameView implements Screen, InputProcessor, AppMenu {
    private Stage stage;
    private TextButton friendsButton;
    //private Window friendsWindow;
    private Dialog friendsDialog;
    private GameMenuController controller;
    private SpriteBatch batch;
    private MapOfGame mapOfGame;
    private OrthographicCamera camera;
    private User currentPlayer;  //should change whenever currentPlayer in Game is changed
    private float stateTime = 0f;
    private boolean showFullMap = false;
    private final Color darkOverlayColor = new Color(0, 0, 0, 0); // black with 0 alpha

    private boolean showToolsMenu = false;
    private boolean showInventoryMenu = false;
    private boolean showBackpackMenu = false;
    private BitmapFont smallFont;
    private int selectedSlot = 0;
    private Table toolMenuTable;
    private Table inventoryMenuTable;
    private Table backpackMenuTable;
    public static float toolUsageStateTime = 0f;
    public static boolean isToolBeingUsed = false;

    private void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/stardew-valley.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        smallFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public GameView(GameMenuController controller) {
        this.controller = controller;
        controller.setGameView(this);
        this.batch = MainApp.getBatch();
        this.mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        this.currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        loadFont();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (showBackpackMenu) {
                showBackpackMenu = false;
                backpackMenuTable.setVisible(false);
                inventoryMenuTable.setVisible(true);
                showInventoryMenu = true;
            } else {
                showInventoryMenu = !showInventoryMenu;
                if (inventoryMenuTable != null) {
                    inventoryMenuTable.setVisible(showInventoryMenu);

                    if (showInventoryMenu) {
                        showToolsMenu = false;
                        if (toolMenuTable != null) toolMenuTable.setVisible(false);
                        // showBackpackMenu is handled above for direct close
                        if (friendsDialog != null && friendsDialog.getStage() != null) {
                            friendsDialog.hide();
                            friendsDialog = null;
                        }
                    }
                }
            }
            return true;
        }
        if (keycode == Input.Keys.M) {
            if (showInventoryMenu) {
                showFullMap = !showFullMap;
                setCameraPosition();
                showInventoryMenu = false;
                if (inventoryMenuTable != null) inventoryMenuTable.setVisible(false);
                return true;
            }
            if (showBackpackMenu) return false;
            showFullMap = !showFullMap;
            setCameraPosition();
            return true;
        }
        if (keycode == Input.Keys.T) {
            if (showInventoryMenu || showBackpackMenu) return false;
            showToolsMenu = !showToolsMenu;
            selectedSlot = 0;
            if (toolMenuTable != null) {
                toolMenuTable.setVisible(showToolsMenu);
            }
            return true;
        }
        if (showBackpackMenu) { // Handle selection movement in backpack
            int totalItems = currentPlayer.getBackpack().getInventoryItems().size();
            int maxItemsPerRow = 6;

            if (keycode == Input.Keys.LEFT) {
                selectedSlot--;
                if (selectedSlot < 0) selectedSlot = totalItems - 1;
                return true;
            }
            if (keycode == Input.Keys.RIGHT) {
                selectedSlot++;
                if (selectedSlot >= totalItems) selectedSlot = 0;
                return true;
            }
            if (keycode == Input.Keys.UP) {
                selectedSlot -= maxItemsPerRow;
                if (selectedSlot < 0) selectedSlot = Math.max(0, totalItems - 1);
                return true;
            }
            if (keycode == Input.Keys.DOWN) {
                selectedSlot += maxItemsPerRow;
                if (selectedSlot >= totalItems)
                    selectedSlot = Math.min(totalItems - 1, selectedSlot % maxItemsPerRow); // Wrap to first row, maintaining column
                return true;
            }
        }
        if (keycode == Input.Keys.LEFT && showToolsMenu) {
            if (showInventoryMenu || showBackpackMenu) return false;
            selectedSlot--;
        }
        if (keycode == Input.Keys.RIGHT && showToolsMenu) {
            if (showInventoryMenu || showBackpackMenu) return false;
            selectedSlot++;
        }
        if (keycode == Input.Keys.C && showToolsMenu) {
            if (showInventoryMenu || showBackpackMenu) return false;
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);
            useSelectedTool(mousePos.x, mousePos.y);
            return true;
        }

        if (keycode == Input.Keys.F) {
            if (showInventoryMenu || showBackpackMenu) return false;
            Tile[][] map = mapOfGame.getMap();
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j].getContainedGrowable() != null) {
                        map[i][j].getContainedGrowable().setIsWateredToday(true);
                    }
                }
            }
            return true;
        }

        if (keycode == Input.Keys.N) {
            if (showInventoryMenu || showBackpackMenu) return false;
//            MainApp.getInstance().getCurrentGame().getTimeAndDate().setHour(22);
//            controller.handleEndOfDay();
            System.out.println(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
            return true;
        }

        if (showFullMap) return true;
        if (showInventoryMenu || showBackpackMenu) return false;

        int x = currentPlayer.getCurrentTile().getX();
        int y = currentPlayer.getCurrentTile().getY();
        int dir = 0;

        switch (keycode) {
            case Input.Keys.A:
                x -= 1;
                dir = 4;
                break;
            case Input.Keys.D:
                x += 1;
                dir = 2;
                break;
            case Input.Keys.W:
                y -= 1;
                dir = 3;
                break;
            case Input.Keys.S:
                y += 1;
                dir = 1;
                break;
        }

        if (x >= 0 && y >= 0 && y < mapOfGame.getMap().length && x < mapOfGame.getMap()[0].length && mapOfGame.getMap()[y][x].getisWalkable() &&
            !(MainApp.getInstance().getCurrentGame().getMap().isInsideAnyFarm(x, y) != null &&
                !(mapOfGame.getMap()[y][x].getTileOwner().equals(currentPlayer.getUsername()) ||
                    (currentPlayer.getPartner() != null && mapOfGame.getMap()[y][x].getTileOwner().equals(currentPlayer.getPartner().getUsername()))))) {
            currentPlayer.setCurrentTile(mapOfGame.getMap()[y][x]);
            currentPlayer.setEnergy((int) (currentPlayer.getEnergy() - (0.0005 * currentPlayer.getEnergy())));
            int newTurnEnergy = Math.max(0, (int) (currentPlayer.getCurrentTurnEnergy() - (0.0005 * currentPlayer.getEnergy())));
            currentPlayer.setCurrentTurnEnergy(newTurnEnergy);
        }
        currentPlayer.setMovingDirection(dir);

        setCameraPosition();
        return true;
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
    public boolean touchDown(int i, int i1, int i2, int i3) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.touchDown(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.touchUp(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.touchCancelled(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.touchDragged(i, i1, i2);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.mouseMoved(i, i1);
        }
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        if (showInventoryMenu || showBackpackMenu) {
            return stage.scrolled(v, v1);
        }
        return false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(multiplexer);

        mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition();
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        friendsButton = new TextButton("Friends", GameAssetManager.skin, "custom-button");
        friendsButton.setSize(200, 200);
        friendsButton.setColor(Color.PURPLE);
        friendsButton.setPosition(Gdx.graphics.getWidth() - 220, Gdx.graphics.getHeight() - 220);
        friendsButton.setTouchable(Touchable.enabled);

        stage.addActor(friendsButton);

        this.toolMenuTable = new Table();
        toolMenuTable.bottom().center();
        toolMenuTable.padBottom(GameAssetManager.TILE_SIZE * 0.75f);
        toolMenuTable.setVisible(showToolsMenu);
        stage.addActor(toolMenuTable);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                MainApp.getInstance().getCurrentGame().advanceTimeByOneHour();
                controller.handleEndOfDay();
                updateLighting(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
            }
        }, 5, 5);

        inventoryMenuTable = new Table(GameAssetManager.skin);
        inventoryMenuTable.setWidth(Gdx.graphics.getWidth() * 0.4f);
        inventoryMenuTable.setHeight(Gdx.graphics.getHeight() * 0.6f);
        inventoryMenuTable.setPosition(
            (Gdx.graphics.getWidth() - inventoryMenuTable.getWidth()) / 2,
            (Gdx.graphics.getHeight() - inventoryMenuTable.getHeight()) / 2
        );
        inventoryMenuTable.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

        TextButton inventoryBtn = new TextButton("Inventory", GameAssetManager.skin, "custom-button");
        inventoryBtn.setColor(Color.LIME);
        TextButton skillsBtn = new TextButton("Skills", GameAssetManager.skin, "custom-button");
        skillsBtn.setColor(Color.CORAL);
        TextButton socialBtn = new TextButton("Social", GameAssetManager.skin, "custom-button");
        socialBtn.setColor(Color.OLIVE);
        TextButton missionsBtn = new TextButton("Missions", GameAssetManager.skin, "custom-button");
        missionsBtn.setColor(Color.TEAL);
        TextButton mapBtn = new TextButton("Map", GameAssetManager.skin, "custom-button");
        mapBtn.setColor(Color.YELLOW);

        inventoryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBackpack();
            }
        });
        skillsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        socialBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        missionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        mapBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFullMap = !showFullMap;
                setCameraPosition();
                showInventoryMenu = false;
                if (inventoryMenuTable != null) inventoryMenuTable.setVisible(false);
            }
        });

        float buttonPad = 10f;
        float buttonWidth = 250f;
        float buttonHeight = 60f;

        inventoryMenuTable.add(inventoryBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();
        inventoryMenuTable.add(skillsBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();
        inventoryMenuTable.add(socialBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();
        inventoryMenuTable.add(missionsBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();
        inventoryMenuTable.add(mapBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();

        inventoryMenuTable.setVisible(false);
        stage.addActor(inventoryMenuTable);

        backpackMenuTable = new Table(GameAssetManager.skin);
        backpackMenuTable.setWidth(Gdx.graphics.getWidth() * 0.4f);
        backpackMenuTable.setHeight(Gdx.graphics.getHeight() * 0.6f);
        backpackMenuTable.setPosition(
            (Gdx.graphics.getWidth() - backpackMenuTable.getWidth()) / 2,
            (Gdx.graphics.getHeight() - backpackMenuTable.getHeight()) / 2
        );
        backpackMenuTable.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));
        backpackMenuTable.setVisible(false);
        stage.addActor(backpackMenuTable);

    }

    @Override
    public void render(float v) {
        stateTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1); // clear with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setCameraPosition();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(TreeAssets.getHorizontalSlice(TreeType.AppleTree.getTextures().get(4), 1, 4),
            0, 0);

        Tile[][] tiles = mapOfGame.getMap();
        int tileSize = GameAssetManager.TILE_SIZE;

        int rows = tiles.length;
        drawTiles(rows, tiles, tileSize);

        drawGreenHouse(tileSize, rows);

        //TODO : handle Giant Crop
        //TODO : handle burnt plants
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[y][x].getContainedGrowable() != null) {
                    drawGrowables(tiles, y, x, tileSize, rows);
                } else if (tiles[y][x].getProductOfGrowable() != null) {
                    drawProductOfGrowables(tiles, y, x, tileSize, rows);
                }
                if (tiles[y][x].getContainedItem() != null) {
                    drawItems(tiles, y, x, tileSize, rows);
                }
            }
        }

        drawPlayer();

        /// ////////////////////////////////////////////

        updateToolsMenuTable();
        if (isToolBeingUsed) {
            toolUsageStateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = InventoryAssets.toolUsageAnimation.getKeyFrame(toolUsageStateTime);
            if (currentFrame != null) {
                // Draw the animation centered on the player's tile
                int drawX = currentPlayer.getCurrentTile().getX() * tileSize;
                int drawY = (mapOfGame.getMap().length - currentPlayer.getCurrentTile().getY() - 1) * tileSize;
                batch.draw(currentFrame, drawX, drawY, tileSize, tileSize);
            }
            if (InventoryAssets.toolUsageAnimation.isAnimationFinished(toolUsageStateTime)) {
                isToolBeingUsed = false;
                toolUsageStateTime = 0f; // Reset for next usage
            }
        }

        /// /////////////////////////////////////////////////////////////////////////
        float camX = camera.position.x - camera.viewportWidth / 2f;
        float camY = camera.position.y - camera.viewportHeight / 2f;
        batch.setColor(darkOverlayColor);
        batch.draw(GameAssetManager.pixel, camX, camY, camera.viewportWidth, camera.viewportHeight);
        batch.setColor(Color.WHITE);
        batch.end();

        handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void drawItems(Tile[][] tiles, int y, int x, int tileSize, int rows) {
        if (tiles[y][x].getContainedItem() instanceof ForagingMineral foraging) {
            batch.draw(foraging.getType().getTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        }
    }

    private void drawProductOfGrowables(Tile[][] tiles, int y, int x, int tileSize, int rows) {
        if (tiles[y][x].getProductOfGrowable().getGrowableType() == GrowableType.ForagingCrop) {
            batch.draw(tiles[y][x].getProductOfGrowable().getForagingCropType().getTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        } else if (tiles[y][x].getProductOfGrowable().getGrowableType() == GrowableType.CropProduct) {
            //TODO : Handle products of crops (one time growth)
            batch.draw(tiles[y][x].getContainedGrowable().getCropType().getCropProductTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        } else if (tiles[y][x].getProductOfGrowable().getGrowableType() == GrowableType.Giant) {
            Point point = findTopLeftOfGiantCropSquare(x, y, rows, tiles[0].length, true);
            int topleftX = point.x;
            int topleftY = point.y;
            batch.draw(CropType.fromName(tiles[y][x].getProductOfGrowable().getName()).getGiantTexture(),
                topleftX * tileSize,
                topleftY * tileSize,
                2 * tileSize,
                2 * tileSize);
        }
    }

    private void drawGrowables(Tile[][] tiles, int y, int x, int tileSize, int rows) {
        if (tiles[y][x].getContainedGrowable().getTreeType() != null) {
            if (tiles[y][x].getProductOfGrowable() != null && tiles[y][x].getContainedGrowable().getTreeType().getFruitedTexture() != null) {
                batch.draw(tiles[y][x].getContainedGrowable().getTreeType().getFruitedTexture(),
                    x * tileSize,
                    (rows - y - 1) * tileSize,
                    tileSize, tileSize);
            } else if (tiles[y][x].isHasBeenBurt() && tiles[y][x].getContainedGrowable().getTreeType().getBurnTexture() != null) {
                batch.draw(tiles[y][x].getContainedGrowable().getTreeType().getBurnTexture(),
                    x * tileSize,
                    (rows - y - 1) * tileSize,
                    tileSize, tileSize);
            } else {
                int currentStage = tiles[y][x].getContainedGrowable().getCurrentStage();
                if (currentStage == 4) {
                    batch.draw(TreeAssets.getHorizontalSlice(tiles[y][x].getContainedGrowable().getTreeType().getTextures().get(currentStage), 1, 4),
                        x * tileSize,
                        (rows - y - 1) * tileSize);
                } else {
                    batch.draw(tiles[y][x].getContainedGrowable().getTreeType().getTextures().get(currentStage),
                        x * tileSize,
                        (rows - y - 1) * tileSize,
                        tileSize, tileSize);
                }
            }
        } else if (tiles[y][x].getContainedGrowable().getCropType() != null) {
            //TODO : handling the products of a crop that can regrow(just like tree)
            if (tiles[y][x].getContainedGrowable().getGrowableType() == GrowableType.Giant) {
                Point point = findTopLeftOfGiantCropSquare(x, y, rows, tiles[0].length, true);
                int topleftX = point.x;
                int topleftY = point.y;
                batch.draw(tiles[y][x].getContainedGrowable().getCropType().getGiantTexture(),
                    topleftX * tileSize,
                    topleftY * tileSize,
                    2 * tileSize,
                    2 * tileSize);
            } else if (tiles[y][x].getProductOfGrowable() != null && !tiles[y][x].getContainedGrowable().getCropType().oneTime()) {
                batch.draw(tiles[y][x].getContainedGrowable().getCropType().getCropProductTexture(),
                    x * tileSize,
                    (rows - y - 1) * tileSize,
                    tileSize, tileSize);
            } else {
                int currentStage = tiles[y][x].getContainedGrowable().getCurrentStage();
                batch.draw(tiles[y][x].getContainedGrowable().getCropType().getTextures().get(currentStage),
                    x * tileSize,
                    (rows - y - 1) * tileSize,
                    tileSize, tileSize);
            }
        } else {
            //Will it ever go to this else block ??
            batch.draw(tiles[y][x].getContainedGrowable().getForagingCropType().getTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        }
    }

    private void drawGreenHouse(int tileSize, int rows) {
        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            GreenHouse greenHouseTile = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(player).getGreenHouse();

            int drawX = (greenHouseTile.getX() - 1) * tileSize;
            int drawY = (MainApp.getInstance().getCurrentGame().getMap().getHeight() - greenHouseTile.getY() - greenHouseTile.getHeight() - 1) * tileSize;

            for (int j = greenHouseTile.getX() - 1; j < greenHouseTile.getX() + greenHouseTile.getWidth() + 1; j++) {
                for (int i = greenHouseTile.getY() - 1; i < greenHouseTile.getY() + greenHouseTile.getHeight() + 1; i++) {
                    batch.draw(TileType.FARM.getTexture(), j * tileSize, (rows - i - 1) * tileSize, tileSize, tileSize);
                }
            }

            batch.draw(
                GameAssetManager.greenhouseTexture,
                drawX,
                drawY,
                8 * tileSize,
                7 * tileSize
            );
        }
    }

    private void drawTiles(int rows, Tile[][] tiles, int tileSize) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                TileType tile = tiles[y][x].getType();
                if (tile != null && tile.getTexture() != null) {
                    batch.draw(tile.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, i, i1);
        camera.update();
        stage.getViewport().update(i, i1, true);
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

    public Point findTopLeftOfGiantCropSquare(int x, int y, int rows, int cols, boolean isProduct) {
        // 4 possible 2x2 square orientations around (x, y)
        int[][] offsets = {
            {0, 0},     // (x, y) is top-left
            {-1, 0},    // (x, y) is top-right
            {0, -1},    // (x, y) is bottom-left
            {-1, -1}    // (x, y) is bottom-right
        };

        for (int[] offset : offsets) {
            int baseX = x + offset[0];
            int baseY = y + offset[1];

            // Skip if square goes out of bounds
            if (baseX < 0 || baseY < 0 || baseX + 1 >= cols || baseY + 1 >= rows)
                continue;

            // Check all 4 tiles in the 2x2 square
            if (isGiantCrop(baseX, baseY, isProduct) &&
                isGiantCrop(baseX + 1, baseY, isProduct) &&
                isGiantCrop(baseX, baseY + 1, isProduct) &&
                isGiantCrop(baseX + 1, baseY + 1, isProduct)) {
                return new Point(baseX, baseY);
            }
        }

        return null;
    }

    public boolean isGiantCrop(int x, int y, boolean isProduct) {
        Tile[][] map = mapOfGame.getMap();
        if (isProduct) {
            return map[y][x].getProductOfGrowable().getGrowableType() == GrowableType.Giant;
        } else {
            return map[y][x].getContainedGrowable().getGrowableType() == GrowableType.Giant;
        }
    }

    //TODO : debug
    private void toggleFriendsDialog() {
        if (friendsDialog != null && friendsDialog.getStage() != null) {
            friendsDialog.hide();
            friendsDialog = null;
            return;
        }

        friendsDialog = new Dialog("Friends :", GameAssetManager.skin, "custom-window");

        friendsDialog.padTop(40);

        User player = MainApp.getInstance().getCurrentGame().getCurrentPlayer();

        for (User friend : MainApp.getInstance().getCurrentGame().getPlayers()) {
            if (player.getUsername().equals(friend.getUsername())) {
                continue;
            }
            Table row = new Table();
            Label nameLabel = new Label(friend.getUsername(), GameAssetManager.skin, "custom-label");
            nameLabel.setColor(Color.WHITE);
            int level = MainApp.getInstance().getCurrentGame().getFriendship(player.getUsername(), friend.getUsername()).getLevel();
            Label levelLabel = new Label("Lvl: " + level, GameAssetManager.skin, "custom-label");
            levelLabel.setColor(Color.WHITE);
            TextButton giftButton = new TextButton("Gift", GameAssetManager.skin, "custom-button");
            giftButton.setColor(Color.WHITE);

            giftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Gifting " + friend.getUsername());
                    // Your gift logic here
                }
            });

            row.add(nameLabel).expandX().left().pad(10);
            row.add(levelLabel).pad(10);
            row.add(giftButton).right().pad(10);
            friendsDialog.getContentTable().add(row).fillX().row();
        }


        friendsDialog.pack();

        stage.addActor(friendsDialog); // 👈 manually add it
        friendsDialog.setPosition(
            friendsButton.getX(),
            friendsButton.getY() - friendsDialog.getHeight() - 10
        );
        friendsDialog.invalidate(); // 👈 force layout update

        System.out.println(friendsDialog.getColor());
    }


    private void handleInput() {
        /// ///////////////////////////////////////////////////////
        if (showInventoryMenu || showBackpackMenu) {
        }
        /// /////////////////////////////////////////////////////////

        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touchPos); // convert to stage coords
            float mouseX = touchPos.x;
            float mouseY = touchPos.y;

            /// ////////////////////////////////////////////////////////////////////////////
            // Prevent game world clicks if any menu is active, unless it's the friends button which is always allowed (for now)
            if (showInventoryMenu || showBackpackMenu) {
                // If a menu is open, only allow clicks on stage actors (handled by stage.touchDown etc.)
                // and explicitly check if friends button is clicked, as it's separate from other menus.
                if (isClickInside(mouseX, mouseY, friendsButton)) {
                    toggleFriendsDialog();
                }
                return;
            }
            /// /////////////////////////////////////////////////////////////////////////////

            if (isClickInside(mouseX, mouseY, friendsButton)) {
                toggleFriendsDialog();
            }

            /// /////////////////////////////////////////////////////////
            else if (showToolsMenu && !isClickInside(mouseX, mouseY, friendsButton)) {
                useSelectedTool(mouseX, mouseY);
            }
            /// /////////////////////////////////////////////////////////////
        }
    }


    private boolean isClickInside(float x, float y, Actor actor) {
        return x >= actor.getX() && x <= actor.getX() + actor.getWidth() &&
            y >= actor.getY() && y <= actor.getY() + actor.getHeight();
    }

    private void updateLighting(int gameHour) {
        float alpha = 0f;

        if (gameHour >= 18 && gameHour < 22) {
            // 18 to 22 => fade from 0 to 0.8
            alpha = (gameHour - 18) / 4f * 0.8f;
        } else if (gameHour >= 22 || gameHour < 9) {
            // Nighttime
            alpha = 0.8f;
        } else if (gameHour >= 9 && gameHour < 10) {
            // 09:00 to 10:00 — fade back to daylight
            alpha = 0.8f - ((gameHour - 9) / 1f * 0.8f);
        } else {
            // Daytime
            alpha = 0f;
        }

        darkOverlayColor.a = MathUtils.clamp(alpha, 0f, 0.8f); // max darkness = 0.8
    }

    private void updateToolsMenuTable() {
        toolMenuTable.clearChildren();

        if (!showToolsMenu || showInventoryMenu || showBackpackMenu) {
            if (toolMenuTable != null) toolMenuTable.setVisible(false);
            return;
        }

        Backpack backpack = currentPlayer.getBackpack();
        ArrayList<Tool> tools = backpack.getTools();

        if (tools == null || tools.isEmpty()) {
            return;
        }

        Label.LabelStyle labelStyle;
        if (GameAssetManager.skin.has("default-label", Label.LabelStyle.class)) {
            labelStyle = GameAssetManager.skin.get("default-label", Label.LabelStyle.class);
        } else if (GameAssetManager.skin.has("custom-label", Label.LabelStyle.class)) {
            labelStyle = GameAssetManager.skin.get("custom-label", Label.LabelStyle.class);
        } else {
            labelStyle = new Label.LabelStyle(smallFont, Color.WHITE);
        }

        float slotImageSize = GameAssetManager.TILE_SIZE * 1.0f;
        float labelPad = 2f;

        if (selectedSlot >= tools.size()) {
            selectedSlot = 0;
        }
        if (selectedSlot < 0) {
            selectedSlot = tools.size() - 1;
        }

        for (int i = 0; i < tools.size(); i++) {
            Stack slotStack = new Stack();

            Image slotBg = new Image(InventoryAssets.slot);
            slotBg.setSize(slotImageSize, slotImageSize);
            slotStack.add(slotBg);

            Tool tool = tools.get(i);
            String textureOrigin = tool.getMaterial().name().toUpperCase() + tool.getType().name().toUpperCase();
            Texture itemTex = InventoryAssets.getToolTexture(textureOrigin);

            if (i == selectedSlot && InventoryAssets.highlightedSlot != null) {
                Image highlightImage = new Image(InventoryAssets.highlightedSlot);
                highlightImage.setSize(slotImageSize, slotImageSize);
                slotStack.add(highlightImage);
                if (!isToolBeingUsed) {
                    drawSelectedTool(itemTex);
                }
            }

            if (itemTex != null) {
                Image itemImage = new Image(itemTex);
                itemImage.setSize(slotImageSize, slotImageSize);
                slotStack.add(itemImage);
            } else {
                Gdx.app.error("GameView", "Texture for tool " + textureOrigin + " is null!");
            }

            Label slotNumLabel = new Label(String.valueOf(i + 1), labelStyle);
            Container<Label> labelContainer = new Container<>(slotNumLabel);
            labelContainer.align(com.badlogic.gdx.utils.Align.topLeft);
            labelContainer.pad(labelPad);
            labelContainer.fill();
            slotStack.add(labelContainer);

            toolMenuTable.add(slotStack).size(slotImageSize, slotImageSize).pad(2f);
        }

        toolMenuTable.pack();
    }

    private void drawSelectedTool(Texture itemTex) {
        if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

        Tile tile = currentPlayer.getCurrentTile();
        int tileSize = GameAssetManager.TILE_SIZE;

        int tileX = tile.getX();
        int tileY = tile.getY();

        int drawX = tileX * tileSize;
        int drawY = (mapOfGame.getMap().length - tileY - 1) * tileSize;

        batch.draw(itemTex, drawX, drawY, tileSize, tileSize);
    }

    private void useSelectedTool(float mouseWorldX, float mouseWorldY) {
        if (!showToolsMenu || showInventoryMenu || showBackpackMenu) {
            if (toolMenuTable != null) toolMenuTable.setVisible(false);
            return;
        }

        ArrayList<Tool> tools = currentPlayer.getBackpack().getTools();
        if (tools == null || tools.isEmpty() || selectedSlot >= tools.size()) {
            return;
        }

        Tool toolToUse = tools.get(selectedSlot);
        currentPlayer.setEquippedTool(toolToUse);

        int tileSize = GameAssetManager.TILE_SIZE;

        float playerTileGridX = currentPlayer.getCurrentTile().getX();
        float playerTileGridY = currentPlayer.getCurrentTile().getY();

        float playerWorldX = playerTileGridX * tileSize + tileSize / 2f;
        float playerWorldY = (mapOfGame.getMap().length - 1 - playerTileGridY) * tileSize + tileSize / 2f;

        Vector3 touchPosOnScreen = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(touchPosOnScreen);
        float actualMouseWorldX = touchPosOnScreen.x;
        float actualMouseWorldY = touchPosOnScreen.y;

        float deltaX = actualMouseWorldX - playerWorldX;
        float deltaY = actualMouseWorldY - playerWorldY;

        int direction = get4DirectionalAngle(deltaX, deltaY);

        float angleRad = MathUtils.atan2(deltaY, deltaX);
        float angleDeg = angleRad * MathUtils.radDeg;
        if (angleDeg < 0) {
            angleDeg += 360;
        }

        // Start the tool usage animation
        isToolBeingUsed = true;
        toolUsageStateTime = 0f;

        if (InventoryAssets.DIRECTION_NAMES != null && InventoryAssets.DIRECTION_NAMES.containsKey(direction)) {
            controller.useTool(InventoryAssets.DIRECTION_NAMES.get(direction));
        } else {
            controller.useTool("Down");
        }
    }

    private int get4DirectionalAngle(float dx, float dy) {
        if (dx == 0 && dy == 0) {
            return 2; // Down
        }

        float angleRad = MathUtils.atan2(dy, dx);
        float angleDeg = angleRad * MathUtils.radDeg;

        if (angleDeg < 0) {
            angleDeg += 360;
        }

        // Up:     45   to 135  (centered at 90)
        // Left:  135   to 225  (centered at 180)
        // Down:  225   to 315  (centered at 270)
        // Right: 315   to 360  (centered at 0/360) OR 0 to 45

        if (angleDeg >= 45 && angleDeg < 135) {
            return 0; // Up
        } else if (angleDeg >= 135 && angleDeg < 225) {
            return 3; // Left
        } else if (angleDeg >= 225 && angleDeg < 315) {
            return 2; // Down
        } else {
            return 1; // Right
        }
    }

    private void showBackpack() {
        inventoryMenuTable.setVisible(false);
        showInventoryMenu = false;

        backpackMenuTable.clearChildren();

        Backpack backpack = currentPlayer.getBackpack();
        Map<Item, Integer> items = backpack.getInventoryItems();
        int totalSlots = backpack.getMaxSize();

        Table itemsContainer = new Table(GameAssetManager.skin);
        itemsContainer.center();
        itemsContainer.pad(10);

        float slotSize = GameAssetManager.TILE_SIZE;
        float itemImageSize = slotSize * 0.8f;
        float labelOffset = 5f;

        ArrayList<Item> sortedItems = new ArrayList<>(items.keySet());

        int currentSlotIndex = 0;

        for (Item item : sortedItems) {
            Integer count = items.get(item);
            if (count == null || count <= 0) continue;

            Stack itemSlotStack = new Stack();

            Image slotBg = new Image(InventoryAssets.slot);
            slotBg.setSize(slotSize, slotSize);
            itemSlotStack.add(slotBg);

            Texture itemTex = null;  //fix later
            if (itemTex != null) {
                Image itemImage = new Image(itemTex);
                itemImage.setSize(itemImageSize, itemImageSize);
                itemImage.setOrigin(itemImage.getWidth() / 2, itemImage.getHeight() / 2);
                itemSlotStack.add(itemImage);
            } else {
                Gdx.app.error("GameView", "Texture for item " + item.getName() + " is null!");
            }

            Label countLabel = new Label(String.valueOf(count), new Label.LabelStyle(smallFont, Color.WHITE));
            Container<Label> labelContainer = new Container<>(countLabel);
            labelContainer.align(com.badlogic.gdx.utils.Align.bottomRight);
            labelContainer.padRight(labelOffset);
            labelContainer.padBottom(labelOffset);
            labelContainer.fill();
            itemSlotStack.add(labelContainer);

            if (currentSlotIndex == selectedSlot) {
                Image highlightImage = new Image(InventoryAssets.highlightedSlot);
                highlightImage.setSize(slotSize, slotSize);
                itemSlotStack.add(highlightImage);
            }

            itemsContainer.add(itemSlotStack).size(slotSize).pad(5);

            currentSlotIndex++;

            if (currentSlotIndex % 6 == 0) {
                itemsContainer.row();
            }
        }

        for (int i = sortedItems.size(); i < totalSlots; i++) {
            Stack emptySlotStack = new Stack();
            Image slotBg = new Image(InventoryAssets.slot);
            slotBg.setSize(slotSize, slotSize);
            emptySlotStack.add(slotBg);

            if (currentSlotIndex == selectedSlot) {
                Image highlightImage = new Image(InventoryAssets.highlightedSlot);
                highlightImage.setSize(slotSize, slotSize);
                emptySlotStack.add(highlightImage);
            }

            itemsContainer.add(emptySlotStack).size(slotSize).pad(5);
            currentSlotIndex++;
            if (currentSlotIndex % 6 == 0) {
                itemsContainer.row();
            }
        }

        if (selectedSlot >= currentSlotIndex) {
            selectedSlot = Math.max(0, currentSlotIndex - 1);
        }
        if (currentSlotIndex == 0) {
            selectedSlot = -1;
        }

        ScrollPane scrollPane = new ScrollPane(itemsContainer, GameAssetManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        backpackMenuTable.padTop(20f);
        backpackMenuTable.row();
        backpackMenuTable.add(scrollPane).expand().fill().row();

        TextButton trashcanButton = new TextButton("", GameAssetManager.skin, "custom-button");
        trashcanButton.setSize(slotSize, slotSize);
        trashcanButton.setColor(Color.DARK_GRAY);
        TrashCan trashcan = backpack.getTrashcan();
        String textureOrigin = trashcan.getMaterial().name().toUpperCase() + trashcan.getType().name().toUpperCase();
        Texture trashcanTex = InventoryAssets.getToolTexture(textureOrigin);
        if (trashcanTex != null) {
            Image trashcanImage = new Image(trashcanTex);
            trashcanButton.clearChildren();
            trashcanButton.add(trashcanImage).expand().fill().center();
        } else {
            trashcanButton.setText("TRASH");
            Gdx.app.error("GameView", "Trashcan texture is null. Using text fallback.");
        }

        trashcanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Trashcan button clicked!");
                if (selectedSlot != -1 && selectedSlot < sortedItems.size()) {
                    Item itemToTrash = sortedItems.get(selectedSlot);
                    trashcan.useTrashCan(itemToTrash, 1);
                    showBackpack();
                } else {
                    System.out.println("No item selected to trash.");
                }
            }
        });

        Table controlButtonsTable = new Table();
        controlButtonsTable.defaults().pad(10);

        TextButton backButton = new TextButton("Back", GameAssetManager.skin, "custom-button");
        backButton.setColor(Color.RED);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked!");
                showBackpackMenu = false;
                backpackMenuTable.setVisible(false);
                inventoryMenuTable.setVisible(true);
                showInventoryMenu = true;
                selectedSlot = 0;
            }
        });
        controlButtonsTable.add(backButton).width(100).height(40);
        controlButtonsTable.add(trashcanButton).width(slotSize * 0.7f).height(slotSize * 0.7f);

        backpackMenuTable.add(controlButtonsTable).bottom().center().row();

        backpackMenuTable.setVisible(true);
        showBackpackMenu = true;
    }

}
