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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.stardew.mini.Controller.GameMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.FriendshipLevels;
import io.github.stardew.mini.Model.Growables.CropType;
import io.github.stardew.mini.Model.Growables.GrowableType;
import io.github.stardew.mini.Model.Growables.TreeType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.TimeManagement.TimeAndDate;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;

//import java.awt.*;
import java.awt.*;
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

    public GameView(GameMenuController controller) {
        this.controller = controller;
        controller.setGameView(this);
        this.batch = MainApp.getBatch();
        this.mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        this.currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.M) {
            showFullMap = !showFullMap;  // toggle map mode
            setCameraPosition();         // update camera immediately
            return true;
        }

        if (keycode == Input.Keys.F) {
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
            MainApp.getInstance().getCurrentGame().getTimeAndDate().setHour(22);
            controller.handleEndOfDay();
            return true;
        }

        if (showFullMap) return true;

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

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);
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
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                TileType tile = tiles[y][x].getType();
                if (tile != null && tile.getTexture() != null) {
                    batch.draw(tile.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
            }
        }

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

        //TODO : handle Giant Crop
        //TODO : handle burnt plants
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[y][x].getContainedGrowable() != null) {
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
                } else if (tiles[y][x].getProductOfGrowable() != null) {
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
                if (tiles[y][x].getContainedItem() != null) {
                    if (tiles[y][x].getContainedItem() instanceof ForagingMineral foraging) {
                        batch.draw(foraging.getType().getTexture(),
                            x * tileSize,
                            (rows - y - 1) * tileSize,
                            tileSize, tileSize);
                    }
                }
            }
        }

        drawPlayer();
        batch.end();

        handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        camera.setToOrtho(false, i, i1);
        camera.update();
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
            if(player.getUsername().equals(friend.getUsername())) {
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
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touchPos); // convert to stage coords
            float mouseX = touchPos.x;
            float mouseY = touchPos.y;

            if (isClickInside(mouseX, mouseY, friendsButton)) {
                toggleFriendsDialog();
            }
        }
    }


    private boolean isClickInside(float x, float y, Actor actor) {
        return x >= actor.getX() && x <= actor.getX() + actor.getWidth() &&
            y >= actor.getY() && y <= actor.getY() + actor.getHeight();
    }

}
