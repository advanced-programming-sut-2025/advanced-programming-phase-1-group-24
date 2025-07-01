package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.GameController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.User;

import java.util.Scanner;

public class GameView implements Screen, InputProcessor, AppMenu {
    private Stage stage;
    private GameController controller;
    private SpriteBatch batch;
    private MapOfGame mapOfGame;
    private OrthographicCamera camera;
    private User currentPlayer;  //should change whenever currentPlayer in Game is changed
    private float stateTime = 0f;
    private boolean showFullMap = false;

    public GameView(GameController controller) {
        this.controller = controller;
        controller.setView(this);
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
        mapOfGame = MainApp.getInstance().getCurrentGame().getMap();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition();
        camera.update();
        Gdx.input.setInputProcessor(this);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float v) {
        stateTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1); // clear with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setCameraPosition();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

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
        int drawY = (MainApp.getInstance().getCurrentGame().getMap().getHeight() - greenHouseTile.getY() - greenHouseTile.getHeight() - 1) * tileSize;

        batch.draw(
            GameAssetManager.greenhouseTexture,
            drawX,
            drawY,
            8 * tileSize,
            7 * tileSize
        );



        drawPlayer();
        batch.end();
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

}
