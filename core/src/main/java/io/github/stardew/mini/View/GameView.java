package io.github.stardew.mini.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stardew.mini.Controller.GameController;
import io.github.stardew.mini.Controller.StoreMenuController;
import com.sun.tools.javac.Main;
import io.github.stardew.mini.Controller.GameController;
import io.github.stardew.mini.Controller.StoreMenuController;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Animals.CrowFlight;
import io.github.stardew.mini.Model.Animals.AnimalType;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.Friendships.Friendship;
import io.github.stardew.mini.Model.Friendships.Gift;
import io.github.stardew.mini.Model.Growables.CropType;
import io.github.stardew.mini.Model.Growables.GrowableType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Places.*;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.Places.Shop;
import io.github.stardew.mini.Model.Places.ShopItem;
import io.github.stardew.mini.Model.Reccepies.Machine;
import io.github.stardew.mini.Model.Reccepies.randomStuffType;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Places.Shop;
import io.github.stardew.mini.Model.Places.ShopItem;
import io.github.stardew.mini.Model.Reccepies.randomStuff;
import io.github.stardew.mini.Model.Reccepies.randomStuffType;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.TimeManagement.*;
import io.github.stardew.mini.Model.Things.StorageType;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.LightningFlash;
import io.github.stardew.mini.Model.TimeManagement.RainDrop;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.User;

//import java.awt.*;
import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.regex.Matcher;

public class GameView implements Screen, InputProcessor, AppMenu {
    private Stage stage;
    private TextButton friendsButton;
    private Dialog friendsDialog;
    private GameController controller;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private User currentPlayer;  //should change whenever currentPlayer in Game is changed
    private float stateTime = 0f;
    private boolean showFullMap = false;
    private final Color darkOverlayColor = new Color(0, 0, 0, 0); // black with 0 alpha
    private WeatherType currentWeather;
    public static List<LightningFlash> scheduledFlashes = new ArrayList<>();
    private List<LightningFlash> activeFlashes = new ArrayList<>();
    private Array<RainDrop> raindrops = new Array<>();
    private float spawnTimer = 0f;
    private static final float DROP_INTERVAL = 0.15f;

    private TerminalWindow terminalWindow;
    private boolean terminalVisible = false;

    private Dialog animalMenuDialog;
    private Animal selectedAnimal;

    private Dialog machineMenuDialog;
    private Machine selectedMachine;


    private float moveCooldown = 0f;
    private static final float MOVE_INTERVAL = 0.1f; // seconds between steps

    private Dialog shopMenuDialog;
    private Shop selectedShop;
    private Dialog shopPurchaseDialog;
    private ShopItem selectedShopItem;
    private int purchaseQuantity = 1;
    private Dialog buyAnimalDialog;

    private StoreMenuController storeController;
    private int gameWidth = Gdx.graphics.getWidth();
    private int gameHeight = Gdx.graphics.getHeight();

    public List<Integer> crowAttacks = new ArrayList<>();
    private List<CrowFlight> activeCrows = new ArrayList<>();

    private ClockHud clockHud;
    private boolean isPlacingBuilding = false;
    private Habitat buildingToPlace = null;
    private Farm currentFarm = null;

    private final Array<HeartEffect> heartEffects = new Array<>();
    public static ArrayList<Animation<TextureRegion>> playerAnimations = new ArrayList<>();


    public GameView(GameController controller) {
        this.controller = controller;
        storeController = new StoreMenuController();
        controller.setView(this);
        this.batch = MainApp.getBatch();
        this.currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
    }

    public void startPlacingBuilding(Habitat building) {
        this.isPlacingBuilding = true;
        this.buildingToPlace = building;
        this.currentFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer);
        setCameraPosition();
        camera.update();
        Gdx.input.setInputProcessor(this);
        showErrorDialog(stage, "Select a tile inside your farm to build");
        //+ building.getDisplayName());
    }

    private void setCameraPosition() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (showFullMap) {
            // Full map view
            float mapWidth = MainApp.getInstance().getCurrentGame().getMap().getMap()[0].length * GameAssetManager.TILE_SIZE;
            float mapHeight = MainApp.getInstance().getCurrentGame().getMap().getMap().length * GameAssetManager.TILE_SIZE;

            camera.position.set(mapWidth / 2f, mapHeight / 2f, 0);

            // Zoom out to fit entire map
            float zoomX = mapWidth / camera.viewportWidth;
            float zoomY = mapHeight / camera.viewportHeight;
            camera.zoom = Math.max(zoomX, zoomY);
        } else if (isPlacingBuilding) {
            // Focus on current player's farm
            if (currentFarm == null) {
                currentFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer);
            }
            if (currentFarm != null) {
                setCameraToFarm(currentFarm);  //  use your clean method
                return;  // camera already updated
            }
        } else {
            // Focus on player
            if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

            camera.zoom = 1f;
            int tileSize = GameAssetManager.TILE_SIZE;

            // Get player's tile position
            Tile tile = currentPlayer.getCurrentTile();
            float drawX = tile.getX() * tileSize + tileSize / 2f;
            float drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - tile.getY() - 1) * tileSize + tileSize / 2f;

            camera.position.set(drawX, drawY, 0);
        }

        camera.update();  //  always update unless you return early
    }

    private void setCameraToFarm(Farm farm) {
        int tileSize = GameAssetManager.TILE_SIZE;
        Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
        Tile tile = tiles[farm.getX() + (farm.getWidth() / 2)][farm.getY() + (farm.getHeight() / 2)];
        float centerX = tile.getX() * tileSize + tileSize / 2f;
        float centerY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - tile.getY() - 1) * tileSize + tileSize / 2f;

        camera.position.set(centerX, centerY, 0);
        float zoomX = (float) farm.getWidth() * GameAssetManager.TILE_SIZE / camera.viewportWidth;
        float zoomY = (float) farm.getHeight() * GameAssetManager.TILE_SIZE / camera.viewportHeight;
        camera.zoom = Math.max(zoomX, zoomY);
    }


private void updateAnimals(float delta) {
    for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
        for (Animal animal : player.getOwnedAnimals()) {
            animal.updateMovement(delta);
            animal.updateIsInHabitat();

            // Only try to assign a new path if animal is not moving
            // and its personal cooldown allows it
            if (!animal.isMoving() && animal.isInHabitat()) {
                animal.reduceCooldown(delta);

                if (animal.getMovementCooldown() <= 0f) {
                    List<Tile> path = generateStepwisePath(animal);

                    // ✅ Check Euclidean distance
                    if (!path.isEmpty()) {
                        Tile first = animal.getCurrentTile();
                        Tile last = path.get(path.size() - 1);
                        double distance = Math.sqrt(Math.pow(first.getX() - last.getX(), 2) +
                            Math.pow(first.getY() - last.getY(), 2));
                        if (distance > 5) {
                            path.clear(); // Ignore
                        }
                    }

                    if (!path.isEmpty()) {
                        animal.setPathToTarget(path);
                        animal.resetCooldown(); // reset after assigning path
                    }
                }
            }
        }
    }
}


    private List<Tile> generateStepwisePath(Animal animal) {
        Tile start = animal.getCurrentTile();
        Tile target = findRandomTargetTileWithinDistance(start, 5);
        if (target == null) return new ArrayList<>();

        return findShortestPath(start, target, 5);
    }
    private Tile findRandomTargetTileWithinDistance(Tile start, int maxDistance) {
        List<Tile> candidates = new ArrayList<>();
        MapOfGame map = MainApp.getInstance().getCurrentGame().getMap();

        for (int dx = -maxDistance; dx <= maxDistance; dx++) {
            for (int dy = -maxDistance; dy <= maxDistance; dy++) {
                int nx = start.getX() + dx;
                int ny = start.getY() + dy;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > maxDistance || distance == 0) continue;

                Tile candidate = map.getTile(nx, ny);
                if (candidate != null && candidate.isBuildable() &&
                    candidate.getContainedAnimal() == null) {
                    candidates.add(candidate);
                }
            }
        }

        if (candidates.isEmpty()) return null;

        return candidates.get(new Random().nextInt(candidates.size()));
    }
    private List<Tile> findShortestPath(Tile start, Tile goal, int maxSteps) {
        Queue<Tile> queue = new LinkedList<>();
        Map<Tile, Tile> cameFrom = new HashMap<>();
        Set<Tile> visited = new HashSet<>();

        queue.add(start);
        visited.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Tile current = queue.poll();

            if (current.equals(goal)) break;

            for (Tile neighbor : getWalkableNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // Reconstruct path
        List<Tile> path = new LinkedList<>();
        Tile step = goal;
        while (step != null && !step.equals(start)) {
            path.add(0, step);
            step = cameFrom.get(step);
        }

        if (path.size() > maxSteps) return new ArrayList<>();
        return path;
    }
    private List<Tile> getWalkableNeighbors(Tile tile) {
        List<Tile> neighbors = new ArrayList<>();
        int[][] directions = { {1,0}, {-1,0}, {0,1}, {0,-1} }; // 4-directional

        for (int[] dir : directions) {
            int nx = tile.getX() + dir[0];
            int ny = tile.getY() + dir[1];
            Tile neighbor = MainApp.getInstance().getCurrentGame().getMap().getTile(nx, ny);
            if (neighbor != null && neighbor.isBuildable() && neighbor.getContainedAnimal() == null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private void drawAnimals(int rows, int tileSize) {
        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            for (Animal animal : player.getOwnedAnimals()) {
                float x, y;

                if (animal.isMoving()) {
                    Tile from = animal.getMovingFrom();
                    Tile to = animal.getMovingTo();
                    float p = animal.getMoveProgress();

                    x = MathUtils.lerp(from.getX(), to.getX(), p) * tileSize;
                    y = MathUtils.lerp(
                        rows - from.getY() - 1,
                        rows - to.getY() - 1,
                        p
                    ) * tileSize;
                } else {
                    x = animal.getCurrentTile().getX() * tileSize;
                    y = (rows - animal.getCurrentTile().getY() - 1) * tileSize;
                }

                batch.draw(animal.getAnimalType().getTexture(), x, y, tileSize, tileSize);
            }
        }
    }

    private void drawShapeRenderer(Tile[][] tiles, int tileSize) {
        if (isPlacingBuilding && currentFarm != null) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (int y = currentFarm.getY(); y < currentFarm.getY() + currentFarm.getHeight(); y++) {
                for (int x = currentFarm.getX(); x < currentFarm.getX() + currentFarm.getWidth(); x++) {
                    Tile tile = tiles[y][x];

                    float drawX = x * tileSize;
                    float drawY = (tiles.length - y - 1) * tileSize;

                    if (tile != null && tile.isBuildable()) {
                        shapeRenderer.setColor(1f, 1f, 1f, 0.0f);
                    } else {
                        shapeRenderer.setColor(0f, 0f, 0f, 0.35f);
                    }
                    shapeRenderer.rect(drawX, drawY, tileSize, tileSize);
                }
            }

            shapeRenderer.end();
            shapeRenderer.dispose();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.M) {
            showFullMap = !showFullMap;  // toggle map mode
            setCameraPosition();         // update camera immediately
            //controller.printMap("0","0","150");
            return true;
        }

        if (keycode == Input.Keys.F) {
            Tile[][] map = MainApp.getInstance().getCurrentGame().getMap().getMap();
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
//            MainApp.getInstance().getCurrentGame().getTimeAndDate().setHour(22);
//            controller.handleEndOfDay();
            System.out.println(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
            return true;
        }
        if(keycode == Input.Keys.K) {
            Tile tile = currentPlayer.getCurrentTile();
            Machine machine = (Machine) tile.getContainedItem();
            machine.setHoursLeft(machine.getHoursLeft() - 10);

        }
        if (keycode == Input.Keys.Z) {
            Tile tile = currentPlayer.getCurrentTile();
            Machine machine = (Machine) tile.getContainedItem();
            machine.useMachine("Coffee",currentPlayer);
            MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getHouse().getMachines().add(machine);
        }
        if (keycode == Input.Keys.U) {
            Tile tile = currentPlayer.getCurrentTile();
            Machine machine = (Machine) tile.getContainedItem();
            System.out.println(machine.getHoursLeft());
            System.out.println(machine.getMaxProcessTime());
        }
        if (keycode == Input.Keys.X) {
            showFullMap = false;  // ✅ disable full map so farm zoom works
            // buildingToPlace = new Habitat(0, 0, 2, 2, StorageType.INITIAL, Habitat.HabitatType.Barn);
            isPlacingBuilding = !isPlacingBuilding;
            //startPlacingBuilding(buildingToPlace);
            return true;

        }
        if(keycode == Input.Keys.L) {
            currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
            Tile tile = currentPlayer.getCurrentTile();
            Tile neededTile = MainApp.getInstance().getCurrentGame().getMap().getTile(tile.getX() - 1, tile.getY());
            System.out.println(neededTile);
            System.out.println(neededTile.getContainedGrowable());
            System.out.println(neededTile.getContainedItem());
            System.out.println(neededTile.getContainedNPC());
            System.out.println(neededTile.getProductOfGrowable());
            System.out.println(neededTile.isHasBeenBurt());
            System.out.println(neededTile.getisWalkable());
            System.out.println(neededTile.getContainedAnimal());
        }

        if(keycode == Input.Keys.C){
            controller.crowAttack();
        }

        if(keycode == Input.Keys.E){
            System.out.println(currentPlayer.getEnergy());
        }
        if(keycode == Input.Keys.I){
            currentPlayer.setEnergy(200);
            currentPlayer.setFainted(false);
        }
//        if(keycode == Input.Keys.Q) {
//            MainApp.getInstance().getCurrentGame().getFriendship("user44", "john").setLevel(3);
//            currentPlayer.getRecievedGift().add(new Gift("john", "user44", new randomStuff(10, randomStuffType.Stone), 5));
//            controller.sendGift("john", "Stone", "10");
//        }
        if (keycode == Input.Keys.P) {
            Animal animal = getAnimalNearPlayer();
            if (animal != null) {
                Result result = controller.petAnimal(animal.getName());
                if (result.isSuccessful()) {
                    Tile tile = animal.getCurrentTile();
                    Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
                    int tileSize = GameAssetManager.TILE_SIZE;
                    int rows = tiles.length;
                    float heartX = tile.getX() * tileSize + (tileSize / 2);
                    float heartY = (rows - tile.getY() - 1) * tileSize + (tileSize);
                    //float drawY = (rows - y - 1) * tileSize + tileSize + 4;
//                    float heartX = tile.getX() * TILE_SIZE;
//                    float heartY = tile.getY() * TILE_SIZE + 32;
                    heartEffects.add(new HeartEffect(heartX, heartY));
                }
            }
        }

        if (showFullMap) return true;
        return false;
}
    private Animal getAnimalNearPlayer() {
        int playerX = currentPlayer.getCurrentTile().getX();
        int playerY = currentPlayer.getCurrentTile().getY();

        for (Animal animal : currentPlayer.getOwnedAnimals()) {
            Tile tile = animal.getCurrentTile();
            int dx = Math.abs(tile.getX() - playerX);
            int dy = Math.abs(tile.getY() - playerY);
            if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
                return animal;
            }
        }
        return null;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (isPlacingBuilding && currentFarm != null && !terminalVisible) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            int tileX = (int) (worldCoords.x / GameAssetManager.TILE_SIZE);
            int tileY = MainApp.getInstance().getCurrentGame().getMap().getHeight() - (int) (worldCoords.y / GameAssetManager.TILE_SIZE) - 1;

            if (isInsideFarm(tileX, tileY)) {

                Tile tile = MainApp.getInstance().getCurrentGame().getMap().getMap()[tileY][tileX];

                if (tile != null && tile.isBuildable()) {
                    if (buildingToPlace == null || storeController.isAreaPlaceable(tileX, tileY, buildingToPlace.getWidth(), buildingToPlace.getHeight())) {
                        Result result;
                        if (buildingToPlace == null) {
                            result = storeController.buyFromCarpenter("Shipping Bin", Integer.toString(tileX), Integer.toString(tileY));
                        } else {
                            buildingToPlace.setX(tileX);
                            buildingToPlace.setY(tileY);
                            result = storeController.buyFromCarpenter(buildingToPlace.getHabitatType().getName(), Integer.toString(tileX), Integer.toString(tileY));
                        }
                        if (result.isSuccessful()) {
                            //updateHabitatTiles();
                            showErrorDialog(stage, "Building placed!");
                        } else {
                            showErrorDialog(stage, result.message());
                        }
                    } else {
                        showErrorDialog(stage, "Building doesnt fit here!");
                    }
                } else {
                    showErrorDialog(stage, "Tile is not buildable.");
                }
            } else {
                showErrorDialog(stage, "Please click inside your farm.");
            }
            //setBuilding(new Building(buildingToPlace));
            isPlacingBuilding = false;
            buildingToPlace = null;
            currentFarm = null;
            return true;
        }

        if (!terminalVisible) {
            //&&  button == Input.Buttons.RIGHT) {
            // Convert screen coordinates to world coordinates
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));

            // Convert world coordinates to tile coordinates
            int tileX = (int) (worldCoords.x / GameAssetManager.TILE_SIZE);
            int tileY = MainApp.getInstance().getCurrentGame().getMap().getHeight() - (int) (worldCoords.y / GameAssetManager.TILE_SIZE) - 1;

            // Check if click is within map bounds
            if (tileX >= 0 && tileY >= 0 && tileX < MainApp.getInstance().getCurrentGame().getMap().getWidth() && tileY < MainApp.getInstance().getCurrentGame().getMap().getHeight()) {
                Tile tile = MainApp.getInstance().getCurrentGame().getMap().getMap()[tileY][tileX];
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
                    selectedShop = MainApp.getInstance().getCurrentGame().getMap().getShopAtPosition(tileX, tileY);
                    if (selectedShop != null) {
                        Vector3 stageCoords = stage.getViewport().unproject(new Vector3(screenX, screenY, 0));
                        showShopMenuDialog(stageCoords.x, stageCoords.y);
                        return true;
                    }
                }
//                if (tile != null && tile.getType() == TileType.SHIPPINGBIN) {
//                    //TODO: open inventory
//                    //TODO: open a very similar dialog to purchase window
//                    //TODO: call storeController.placingInShippingBin
//                }
                if (tile != null && tile.getContainedItem() != null && tile.getContainedItem() instanceof Machine) {
                    System.out.println("machine");
                    selectedMachine = (Machine)tile.getContainedItem();

                    // Convert screen coordinates to stage coordinates
                    Vector3 stageCoords = stage.getViewport().unproject(new Vector3(screenX, screenY, 0));

                    // Center dialog around mouse position
                    machineMenuDialog.setPosition(
                        stageCoords.x - machineMenuDialog.getWidth() / 2,
                        stageCoords.y - machineMenuDialog.getHeight() / 2
                    );
                    machineMenuDialog.setVisible(true);
                    machineMenuDialog.show(stage);
                    Gdx.input.setInputProcessor(stage);
                    return true;
                }
            }
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            stage.getCamera().unproject(touchPos); // convert to stage coords
            float mouseX = touchPos.x;
            float mouseY = touchPos.y;

            if(isClickInside(mouseX, mouseY, friendsButton)){
                if (friendsDialog != null) {
                    friendsDialog.remove();
                }
                createFriendsDialog();
                friendsDialog.setVisible(true);
                friendsDialog.show(stage);
                Gdx.input.setInputProcessor(stage);
                return true;
            }


        }
        return false;
    }

    public void updateHabitatTiles() {
        Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
        for (int y = buildingToPlace.getY(); y < buildingToPlace.getY() + buildingToPlace.getHeight(); y++) {
            for (int x = buildingToPlace.getX(); x < buildingToPlace.getX() + buildingToPlace.getWidth(); x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    if (buildingToPlace.getHabitatType() == Habitat.HabitatType.Barn ||
                        buildingToPlace.getHabitatType() == Habitat.HabitatType.Big_Barn ||
                        buildingToPlace.getHabitatType() == Habitat.HabitatType.Deluxe_Barn) {
                        tile.setType(TileType.BARN);
                    } else {
                        tile.setType(TileType.CAGE);
                    }
                }
            }
        }
    }

    public boolean isInsideFarm(int tileX, int tileY) {
        return tileX >= currentFarm.getX() && tileX < currentFarm.getX() + currentFarm.getWidth() &&
            tileY >= currentFarm.getY() && tileY < currentFarm.getY() + currentFarm.getHeight();
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

            private void showShopMenuDialog(float x, float y) {
                shopMenuDialog.getContentTable().clear();
                shopMenuDialog.getTitleLabel().setText(selectedShop.getShopName());

                Table content = shopMenuDialog.getContentTable();
                content.defaults().pad(10);
                content.clear();

                // Filter dropdown (SelectBox)
                Table filterTable = new Table();
                SelectBox<String> filterSelectBox = new SelectBox<>(GameAssetManager.skin.get("custom-selectbox", SelectBox.SelectBoxStyle.class));
                filterSelectBox.setItems("All Products", "Available Products");
                filterSelectBox.setSelected("All Products"); // default selection
                filterTable.add(new Label("Filter:", GameAssetManager.skin, "custom-label")).padRight(10);
                filterTable.add(filterSelectBox).left();

                content.add(filterTable).left().row();

                // Item list table
                final Table itemTable = new Table();
                itemTable.top();
                itemTable.defaults().pad(5).fillX();

                // Refresh logic for filtering
                Runnable refreshItems = () -> {
                    itemTable.clear();
                    boolean onlyAvailable = filterSelectBox.getSelected().equals("Available Products");

                    for (final ShopItem item : selectedShop.getProducts()) {
                        boolean isAvailable = item.getDailyLimit() - item.getSoldToday() > 0;

                        if (onlyAvailable && !isAvailable) continue;

                        TextButton itemButton = new TextButton(item.getName(), GameAssetManager.skin, "custom-button");
                        itemButton.setDisabled(!isAvailable);
                        itemButton.getLabel().setColor(isAvailable ? Color.WHITE : Color.GRAY);

                        itemButton.addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                selectedShopItem = item;
                                if (selectedShop.getShopType() == ShopType.CARPENTER_SHOP && (item.getShopItemType() == ShopItemType.CAGE
                                    || item.getShopItemType() == ShopItemType.BARN || item.getShopItemType() == ShopItemType.SHIPPING_BIN)) {
                                    showFullFarm(item);
                                } else if (item.getShopItemType() == ShopItemType.ANIMAL) {
                                    showBuyAnimalDialog(item);
                                } else {
                                    purchaseQuantity = 1;
                                    showPurchaseDialog();
                                }
                                //TODO:upgrade tool menu?
                                shopMenuDialog.hide();
                            }
                        });

                        itemTable.add(itemButton).expandX().fillX().row();
                    }
                };

                // Listener for dropdown change
                filterSelectBox.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        refreshItems.run();
                    }
                });

                // Scrollable list for items
                ScrollPane scrollPane = new ScrollPane(itemTable, GameAssetManager.skin);
                scrollPane.setFadeScrollBars(false);
                scrollPane.setScrollingDisabled(true, false);
                scrollPane.setForceScroll(false, true);
                scrollPane.layout();

                content.add(scrollPane).width(gameWidth / 2).height(gameHeight / 2).row();

                // Initial load
                refreshItems.run();

                shopMenuDialog.pack();
                shopMenuDialog.setPosition(x - shopMenuDialog.getWidth() / 2, y - shopMenuDialog.getHeight() / 2);

                shopMenuDialog.setVisible(true);
                shopMenuDialog.show(stage);
                Gdx.input.setInputProcessor(stage);
            }

    public void showBuyAnimalDialog(ShopItem item) {
        buyAnimalDialog.getContentTable().clear();
        buyAnimalDialog.getTitleLabel().setText("Buy " + item.getName());

        Table content = buyAnimalDialog.getContentTable();
        content.clear();
        content.defaults().pad(10);

        // Label + TextField for animal name input
        Label nameLabel = new Label("Animal Name:", GameAssetManager.skin, "custom-label");
        final TextField nameField = new TextField("", GameAssetManager.skin);
        nameField.setMessageText("Enter name...");

        TextButton buyButton = new TextButton("Buy", GameAssetManager.skin, "custom-button");
        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String enteredName = nameField.getText().trim();

                if (enteredName.isEmpty()) {
                    showErrorDialog(stage, "Please enter a name for the animal.");
                    return;
                }

                Result result = storeController.buyAnimal(item.getName(), enteredName);
                buyAnimalDialog.hide();
                showErrorDialog(stage, result.message());
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buyAnimalDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        content.add(nameLabel).left().row();
        content.add(nameField).width(300).row();
        content.add(buyButton).colspan(2).padTop(10).row();
        content.add(cancelButton).colspan(2);

        buyAnimalDialog.pack();
        buyAnimalDialog.setPosition(
            (Gdx.graphics.getWidth() - buyAnimalDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - buyAnimalDialog.getHeight()) / 2
        );

        buyAnimalDialog.setVisible(true);
        buyAnimalDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }


    public void showFullFarm(ShopItem item) {
        showFullMap = false;  // ✅ disable full map so farm zoom works
        if (item.getShopItemType() != ShopItemType.SHIPPING_BIN) {
            buildingToPlace = (Habitat) item.getItem();
        }
        isPlacingBuilding = true;
        startPlacingBuilding(buildingToPlace);
    }

    private void showPurchaseDialog() {
        shopPurchaseDialog.clear();
        shopPurchaseDialog.getTitleLabel().setText("Purchase " + selectedShopItem.getName());

        Table content = shopPurchaseDialog.getContentTable();
        content.clear();
        content.defaults().pad(10);

        Label quantityLabel = new Label("Quantity: " + purchaseQuantity, GameAssetManager.skin, "custom-label");
        TextButton plusButton = new TextButton("+", GameAssetManager.skin, "custom-button");
        TextButton minusButton = new TextButton("-", GameAssetManager.skin, "custom-button");
        TextButton buyButton = new TextButton("Buy", GameAssetManager.skin, "custom-button");
        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");


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
                Result result = storeController.purchase(selectedShopItem, purchaseQuantity);
                //buyItem(currentPlayer, selectedShopItem, purchaseQuantity);
                shopPurchaseDialog.hide();
                showErrorDialog(stage, result.message());
                //Gdx.input.setInputProcessor(GameView.this);
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

        shopPurchaseDialog.setVisible(true);
        shopPurchaseDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    public void createUI() {
        createTerminal();

        createAnimalDialog();

        createShopMenusDialogs();

        createMachineDialog();
    }

    private void createShopMenusDialogs() {
        shopMenuDialog = new Dialog("Shop Menu", GameAssetManager.skin, "custom-window");
        shopMenuDialog.padTop(40f);
        shopMenuDialog.setKeepWithinStage(true);
        shopMenuDialog.setMovable(false);
        shopMenuDialog.setVisible(false);
        stage.addActor(shopMenuDialog);

        shopPurchaseDialog = new Dialog("Purchase", GameAssetManager.skin, "custom-window");
        shopPurchaseDialog.padTop(40f);
        shopPurchaseDialog.setKeepWithinStage(true);
        shopPurchaseDialog.setMovable(false);
        shopPurchaseDialog.setVisible(false);
        stage.addActor(shopPurchaseDialog);

        buyAnimalDialog = new Dialog("Buy Animal", GameAssetManager.skin, "custom-window");
        buyAnimalDialog.padTop(40f);
        buyAnimalDialog.setKeepWithinStage(true);
        buyAnimalDialog.setMovable(false);
        buyAnimalDialog.setVisible(false);
        stage.addActor(buyAnimalDialog);
    }

    private void createTerminal() {
        terminalWindow = new TerminalWindow(GameAssetManager.skin, this);
        terminalWindow.setVisible(false);
        stage.addActor(terminalWindow);
    }

    private void createAnimalDialog() {
        // Create the animal menu dialog (initially hidden)
        animalMenuDialog = new Dialog("Animal Menu", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object object) {
                handleAnimalMenuChoice(object.toString());
            }
        };
        animalMenuDialog.padTop(40);
        animalMenuDialog.getContentTable().defaults().pad(10);

        // Add buttons with their result objects
        TextButton feedButton = new TextButton("Feed", GameAssetManager.skin, "custom-button");
        feedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("feed");
            }
        });

        TextButton petButton = new TextButton("Pet", GameAssetManager.skin, "custom-button");
        petButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("pet");
            }
        });

        TextButton releaseButton = new TextButton("Release", GameAssetManager.skin, "custom-button");
        releaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("release");
            }
        });

        TextButton sellButton = new TextButton("Sell", GameAssetManager.skin, "custom-button");
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("sell");
            }
        });

        TextButton collectButton = new TextButton("Collect Product", GameAssetManager.skin, "custom-button");
        collectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("collect");
            }
        });

        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");
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
    private void createMachineDialog() {
        // Create the animal menu dialog (initially hidden)
        machineMenuDialog = new Dialog("Machine Menu", GameAssetManager.skin, "dialog") {
            @Override
            protected void result(Object object) {
                handleMachineMenuChoice(object.toString());
            }
        };

        machineMenuDialog.getContentTable().defaults().pad(10);

        // Add buttons with their result objects
        TextButton RecepiesButton = new TextButton("Recepies", GameAssetManager.skin);
        RecepiesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();                       // hide the main menu
                handleMachineMenuChoice("Recepies");
            }
        });

        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();
                handleMachineMenuChoice("Cancel");

            }
        });

        TextButton cheatButton = new TextButton("Cheat", GameAssetManager.skin);
        cheatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleMachineMenuChoice("Cheat");
                machineMenuDialog.hide();
            }
        });



        TextButton exitButton = new TextButton("Exit", GameAssetManager.skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);  // Return input to game
                selectedMachine = null;
            }
        });

        machineMenuDialog.getContentTable().add(RecepiesButton).row();
        machineMenuDialog.getContentTable().add(cancelButton).row();
        machineMenuDialog.getContentTable().add(cheatButton).row();
        machineMenuDialog.getContentTable().add(exitButton).row();

        machineMenuDialog.setKeepWithinStage(true);
        machineMenuDialog.setMovable(false);
        machineMenuDialog.setVisible(false);  // Add this after creation
        stage.addActor(machineMenuDialog);
    }

    private void showRecipeDialog(Machine machine) {
        Dialog dlg = new Dialog("Recipes for " + machine.getType().getName(), GameAssetManager.skin) {
            @Override
            protected void result(Object obj) {
                // only “Close” exists here, so just hide
                this.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        };

        Table tbl = dlg.getContentTable();
        tbl.defaults().pad(6).left();

        // Header row
        tbl.add(new Label("Product", GameAssetManager.skin)).padRight(20);
        tbl.add(new Label("Ingredients", GameAssetManager.skin)).row();

        // One line per product
        for (randomStuffType prod : machine.getType().getProducts()) {
            StringBuilder ing = new StringBuilder();
            prod.getIngredients().forEach((name, qty) ->
                ing.append(name).append(" x").append(qty).append(", ")
            );
            // strip trailing comma
            if (ing.length() > 0) ing.setLength(ing.length() - 2);

            tbl.add(new Label(prod.getName(), GameAssetManager.skin));
            tbl.add(new Label(ing.length()>0 ? ing.toString() : "—", GameAssetManager.skin))
                .row();
        }

        // Close button
        dlg.button("Close", "Close");
        dlg.show(stage);
    }


    private void handleMachineMenuChoice(String choice) {
        if (selectedMachine == null) {
            //System.out.println("animal is null");
            return;
        }
        //System.out.println(selectedAnimal.getName());
        Result result = null;

        // Handle choices...
        switch (choice) {
            case "Cancel":
                selectedMachine.setActivated(false);
                result = new Result (false, "Cancelled!");
                break;
            case "Recepies":
                showRecipeDialog(selectedMachine);

                break;
            case "Cheat":
                selectedMachine.setHoursLeft(0);
                Gdx.input.setInputProcessor(this);
                //selectedMachine.setReady(true);

                //controller.releaseAnimal(selectedAnimal);
                break;
            case "Exit":
                //selectedMachine.setActivated(false);
                Gdx.input.setInputProcessor(this);
                // Do nothing
                break;
            default:
                result = new Result(false, choice);
                break;
        }
        if (result != null) {
            showErrorDialog(stage, result.message());
        }
        machineMenuDialog.hide();
        // Gdx.input.setInputProcessor(this);  // Return input to game
        selectedMachine = null;
    }

    private void createFriendsDialog() {
        friendsDialog = new Dialog("Friends", GameAssetManager.skin, "custom-window");
        friendsDialog.padTop(40);
        //friendsDialog.getContentTable().debug(); // show layout
        Table content = friendsDialog.getContentTable();
        content.defaults().pad(10).expandX().fillX();
        //content.debug();

        for (User friend : MainApp.getInstance().getCurrentGame().getPlayers()) {
            if (MainApp.getInstance().getCurrentGame().getCurrentPlayer().getUsername().equals(friend.getUsername())) {
                continue;
            }
            Table row = new Table();
            Label nameLabel = new Label(friend.getUsername(), GameAssetManager.skin, "custom-label");
            int level = MainApp.getInstance().getCurrentGame().getFriendship(currentPlayer.getUsername(), friend.getUsername()).getLevel();
            System.out.println(level);
            Label levelLabel = new Label("Lvl: " + level, GameAssetManager.skin, "custom-label");
            TextButton giftButton = new TextButton("Gift", GameAssetManager.skin, "custom-button");
            TextButton receivedButton = new TextButton("Received", GameAssetManager.skin, "custom-button");
            TextButton sentButton = new TextButton("Sent", GameAssetManager.skin, "custom-button");

            giftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    //TODO : send gift via inventory (sendGift method)
                    //send a notif for the friend that we sent the gift for
                }
            });
            receivedButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showReceivedGifts(friend);

                }
            });
            sentButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    List<Gift> sentGifts = new ArrayList<>();
                    for(User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()) {
                        if(otherPlayer.getUsername().equals(currentPlayer.getUsername())) {
                            continue;
                        }
                        Friendship friendship = MainApp.getInstance().getCurrentGame().getFriendship(currentPlayer.getUsername(), otherPlayer.getUsername());
                        List<Gift> allGits = friendship.getGifts();
                        for(Gift gift : allGits) {
                            if(gift.getSender().equals(currentPlayer.getUsername())) {
                                sentGifts.add(gift);
                            }
                        }
                    }
                    showSentGiftsDialog(sentGifts);
                }
            });

            row.add(nameLabel).left().pad(100);
            row.add(levelLabel).pad(100);
            row.add(giftButton).pad(100);
            row.add(receivedButton).pad(100);
            row.add(sentButton).right().pad(100);
            content.add(row).fillX().row();
        }

        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                friendsDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });
        content.add(closeButton).row();

        friendsDialog.pack();
        friendsDialog.setPosition(20, 220);
        friendsDialog.setVisible(false);
        stage.addActor(friendsDialog);
    }

    private void showReceivedGifts(User friend) {
        Dialog receivedGiftsDialog = new Dialog("Received Gifts from " + friend.getUsername(), GameAssetManager.skin, "custom-window");
        receivedGiftsDialog.padTop(40);
        Table giftTable = receivedGiftsDialog.getContentTable();
        giftTable.defaults().pad(10).expandX().fillX();

        List<Gift> receivedGifts = MainApp.getInstance().getCurrentGame()
            .getCurrentPlayer().getRecievedGift()
            .stream().filter(g -> g.getSender().equals(friend.getUsername()))
            .collect(Collectors.toList());

        if (receivedGifts.isEmpty()) {
            giftTable.add(new Label("No gifts received from " + friend.getUsername(), GameAssetManager.skin, "custom-label")).row();
        } else {
            int index = 1;
            for (Gift gift : receivedGifts) {
                final int giftIndex = index;

                Label giftLabel = new Label(gift.getItem().getName() + "(from :" + gift.getSender() + ")",
                    GameAssetManager.skin, "custom-label");

                // Create a dropdown or rating selector
                SelectBox<String> ratingBox = new SelectBox<>(GameAssetManager.skin);
                ratingBox.setItems("1", "2", "3", "4", "5");
                ratingBox.setSelected("3");

                TextButton rateButton = new TextButton("Rate", GameAssetManager.skin, "custom-button");
                rateButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        String resultMessage;
                        try {
                            Result result = controller.rateGifts(String.valueOf(giftIndex), ratingBox.getSelected());
                            resultMessage = result.getMessage();
                        } catch (Exception e) {
                            resultMessage = "Error rating gift: " + e.getMessage();
                        }

                        Dialog resultDialog = new Dialog("Result", GameAssetManager.skin, "custom-window");
                        Label resultLabel = new Label(resultMessage, GameAssetManager.skin, "custom-label");
                        resultDialog.getContentTable().add(resultLabel).pad(20).row();
                        TextButton okButton = new TextButton("OK", GameAssetManager.skin, "custom-button");
                        resultDialog.button(okButton, true);
                        resultDialog.show(stage);
                    }
                });

                Table giftRow = new Table();
                giftRow.add(giftLabel).pad(10).left();
                giftRow.add(ratingBox).pad(10).width(80);
                giftRow.add(rateButton).pad(10).right();
                giftTable.add(giftRow).row();

                index++;
            }
        }

        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                receivedGiftsDialog.hide();
            }
        });

        giftTable.add(closeButton).colspan(3).center().row();

        receivedGiftsDialog.pack();
        receivedGiftsDialog.setPosition(100, 200);
        receivedGiftsDialog.show(stage);
    }

    public void showSentGiftsDialog(List<Gift> sentGifts) {
        Dialog sentDialog = new Dialog("Sent Gifts", GameAssetManager.skin, "custom-window");
        sentDialog.padTop(40);

        Table content = sentDialog.getContentTable();
        content.defaults().pad(10).expandX().fillX();

        if (sentGifts == null || sentGifts.isEmpty()) {
            Label emptyLabel = new Label("You haven't sent any gifts yet.", GameAssetManager.skin, "custom-label");
            content.add(emptyLabel).center().row();
        } else {
            int index = 1;
            for (Gift gift : sentGifts) {
                Label giftLabel = new Label("Gift #" + index + " → " + gift.getReceiver()
                    + " (" + gift.getItem().getName() + ")", GameAssetManager.skin, "custom-label");

                content.add(giftLabel).left().padBottom(5).row();
                index++;
            }
        }

        // Add OK button with custom style
        TextButton okButton = new TextButton("OK", GameAssetManager.skin, "custom-button");
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sentDialog.hide();
            }
        });

        content.add(okButton).center().padTop(20).row();

        sentDialog.pack();
        sentDialog.setPosition(100, 200);
        sentDialog.show(stage);
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
        Gdx.input.setInputProcessor(this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        setCameraPosition();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        createUI();

        clockHud = new ClockHud(stage);
        friendsButton = new TextButton("Friends", GameAssetManager.skin, "custom-button");
        friendsButton.setSize(200, 200);
        friendsButton.setColor(Color.PURPLE);
        friendsButton.setPosition(0, 10);
        friendsButton.setTouchable(Touchable.enabled);

        stage.addActor(friendsButton);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                MainApp.getInstance().getCurrentGame().getTimeAndDate().advanceHour();
                controller.handleEndOfDay();
                updateLighting(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
            }
        }, 5, 5);

        determineAvatar();
    }

    private void determineAvatar() {
        switch (MainApp.getInstance().getCurrentGame().getCurrentPlayer().getAvatar()){
            case Abigail -> playerAnimations = GameAssetManager.abigailAnimations;
            case Alex -> playerAnimations = GameAssetManager.alexAnimations;
            case Shane -> playerAnimations = GameAssetManager.shaneAnimations;
            case Haley -> playerAnimations = GameAssetManager.haleyAnimations;
        }
    }


    @Override
    public void render(float v) {
        for (LightningFlash flash : activeFlashes) {
            flash.update(v);
        }
        spawnTimer += v;
        if (spawnTimer > DROP_INTERVAL) {
            float viewLeft = camera.position.x - camera.viewportWidth / 2f;
            float viewRight = camera.position.x + camera.viewportWidth / 2f;

            float x = MathUtils.random(viewLeft, viewRight);
            float y = camera.position.y + camera.viewportHeight / 2f + 20;

            raindrops.add(new RainDrop(x, y));
            spawnTimer = 0f;
        }


        for (int i = raindrops.size - 1; i >= 0; i--) {
            RainDrop drop = raindrops.get(i);
            drop.update(v, camera);
            if (drop.finished) {
                raindrops.removeIndex(i);
            }
        }

        int currentHour = MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour(); // get current game hour as int

        Iterator<LightningFlash> it = scheduledFlashes.iterator();
        while (it.hasNext()) {
            LightningFlash flash = it.next();
            if (flash.scheduledTime == currentHour) {
                flash.trigger();
                activeFlashes.add(flash);
                it.remove(); // remove from scheduled list
            }
        }

        activeFlashes.removeIf(flash -> !flash.isActive());

        updateCrowFlightSpawn();

        stateTime += Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1); // clear with black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        setCameraPosition();
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // --- DRAW GAME WORLD ---
        Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
        int tileSize = GameAssetManager.TILE_SIZE;

        int rows = tiles.length;
        drawTiles(rows,tiles,tileSize);
        drawGreenHouse(tileSize,rows);
                drawHabitats(tileSize, rows);
                drawShops(tileSize,rows);
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


                updateAnimals(v);
                drawAnimals(rows, tileSize);

        drawPlayer();
        // --- DRAW HEART EFFECTS ---
        Iterator<HeartEffect> iterator = heartEffects.iterator();
        while (iterator.hasNext()) {
            HeartEffect effect = iterator.next();
            effect.update(Gdx.graphics.getDeltaTime());
            effect.draw(batch);
            if (effect.isFinished()) {
                iterator.remove();
            }
        }
        float camX = camera.position.x - camera.viewportWidth / 2f;
        float camY = camera.position.y - camera.viewportHeight / 2f;

        currentWeather = MainApp.getInstance().getCurrentGame().getCurrentWeatherType();
        if(currentWeather == WeatherType.STORM) {
            batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(GameAssetManager.stormOverlay, camX, camY, camera.viewportWidth, camera.viewportHeight);
            batch.setColor(Color.WHITE);
        }
        else if(currentWeather == WeatherType.SNOW){
            batch.draw(GameAssetManager.snowOverlay, camX, camY, camera.viewportWidth, camera.viewportHeight);
        }
        else if(currentWeather == WeatherType.RAIN){
            for (RainDrop drop : raindrops) {
                drop.render(batch);
            }
        }

        renderCrowFlights(batch, v);


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
        setCameraPosition();
        camera.update();
        batch.setColor(darkOverlayColor);
        batch.draw(GameAssetManager.pixel, camX, camY, camera.viewportWidth * 50 , camera.viewportHeight * 50);
        batch.setColor(Color.WHITE);
        for (LightningFlash flash : activeFlashes) {
            if (flash.isActive()) {
                batch.setColor(new Color(0, 0, 0, flash.getAlpha())); // use black if preferred
                batch.draw(GameAssetManager.pixel, camX, camY, camera.viewportWidth, camera.viewportHeight);
                //controller.printMap("0", "0", "150");
            }
        }
       batch.setColor(Color.WHITE);
//        TimeAndDate timeAndDate = MainApp.getInstance().getCurrentGame().getTimeAndDate();
//        renderHud(batch, camera, timeAndDate.getSeason().name() + timeAndDate.getDay(), Integer.toString(timeAndDate.getHour()) ,
//            Integer.toString(currentPlayer.getMoney()));

        if (!showFullMap && !terminalVisible && !currentPlayer.hasFainted()) {
            moveCooldown -= v;
            if (moveCooldown <= 0f) {
                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    if (tryMove(0, -1, 3)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    if (tryMove(0, +1, 1)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    if (tryMove(-1, 0, 0)) moveCooldown = MOVE_INTERVAL;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    if (tryMove(+1, 0, 2)) moveCooldown = MOVE_INTERVAL;
                }
            }
        }

        batch.end(); // ✅ this must come BEFORE stage rendering

                                drawShapeRenderer(tiles, tileSize);

        drawClock(v);

        // --- DRAW UI ---
        //handleInput();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void drawClock(float v) {
        int hour = MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour(); // 0 to 23

        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;

        String timeStr = displayHour + (hour >= 12 ? " p.m." : " a.m.");
        clockHud.updateTime(hour, timeStr);
        clockHud.updateDate(MainApp.getInstance().getCurrentGame().getTimeAndDate().getDayOfWeek().name() +
            MainApp.getInstance().getCurrentGame().getTimeAndDate().getDay());
        clockHud.updateMoney(currentPlayer.getMoney());
        clockHud.updateWeather(MainApp.getInstance().getCurrentGame().getCurrentWeatherType());
        clockHud.updateSeason(MainApp.getInstance().getCurrentGame().getTimeAndDate().getSeason());

        clockHud.draw(v);
    }

    private void drawItems(Tile[][] tiles, int y, int x, int tileSize, int rows) {
        if (tiles[y][x].getContainedItem() instanceof ForagingMineral foraging) {
            batch.draw(foraging.getType().getTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        }
        else if(tiles[y][x].getContainedItem() instanceof Machine machine) {
            batch.draw(GameAssetManager.FLOORING_01,x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);

            float drawX = x * tileSize;
            float drawY = (rows - y - 1) * tileSize + tileSize + 4;

            if (machine.getActivated() && !machine.getReady()) {
                float progress = 1f - (machine.getHoursLeft() / (float)machine.getMaxProcessTime());
                float barWidth = tileSize;
                float barHeight = 10f;

                batch.setColor(Color.DARK_GRAY); // پس‌زمینه نوار
                batch.draw(GameAssetManager.pixel, drawX, drawY, barWidth, barHeight);
                batch.setColor(Color.GREEN); // نوار پر شده
                batch.draw(GameAssetManager.pixel, drawX, drawY, barWidth * progress, barHeight);
                batch.setColor(Color.WHITE);
            } else if (machine.getReady()) {
                GameAssetManager.customFont.draw(batch, "Done!", drawX + tileSize / 2f - 50, drawY + 20);
            }
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
    private void drawShops(int tileSize, int rows) {
        List<Shop> shops = MainApp.getInstance().getCurrentGame().getMap().getShops(); // <-- Ensure you have this method
        for (Shop shop : shops) {
            for (int i = shop.getX(); i < shop.getX() + shop.getWidth(); i++) {
                for (int j = shop.getY(); j < shop.getY() + shop.getHeight(); j++) {
                    batch.draw(TileType.NPCLAND.getTexture(), i * tileSize, (rows-j-1) * tileSize, tileSize, tileSize);
                }
            }
        }
        for (Shop shop : shops) {


            int drawX = shop.getX() * tileSize;
            int drawY = (rows - shop.getY() - shop.getHeight()) * tileSize;

            Texture texture = shop.getShopType().getTexture();
            if (texture != null) {
                batch.draw(texture, drawX, drawY, shop.getWidth() * tileSize, shop.getHeight() * tileSize);
            }
        }
    }
    private void drawHabitats(int tileSize, int rows) {
        for (Farm farm : MainApp.getInstance().getCurrentGame().getMap().getFarms()) {
            for (Habitat barn : farm.getBarn()) {
                int drawX = (barn.getX()) * tileSize;
                int drawY = (MainApp.getInstance().getCurrentGame().getMap().getHeight() - barn.getY() - barn.getHeight()) * tileSize;

                for (int j = barn.getX(); j < barn.getX() + barn.getWidth(); j++) {
                    for (int i = barn.getY(); i < barn.getY() + barn.getHeight(); i++) {
                        batch.draw(TileType.FARM.getTexture(), j * tileSize, (rows - i - 1) * tileSize, tileSize, tileSize);
                    }
                }
                Texture texture = barn.getHabitatType().getTexture();
                batch.draw(
                    texture,
                    drawX,
                    drawY,
                    barn.getWidth() * tileSize,
                    barn.getHeight() * tileSize
                );
            }
            for (Habitat cage : farm.getCage()) {
                int drawX = (cage.getX()) * tileSize;
                int drawY = (MainApp.getInstance().getCurrentGame().getMap().getHeight() - cage.getY() - cage.getHeight()) * tileSize;

                for (int j = cage.getX(); j < cage.getX() + cage.getWidth(); j++) {
                    for (int i = cage.getY(); i < cage.getY() + cage.getHeight(); i++) {
                        batch.draw(TileType.FARM.getTexture(), j * tileSize, (rows - i - 1) * tileSize, tileSize, tileSize);
                    }
                }
                Texture texture = cage.getHabitatType().getTexture();
                batch.draw(
                    texture,
                    drawX,
                    drawY,
                    cage.getWidth() * tileSize,
                    cage.getHeight() * tileSize
                );
            }
            Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < tiles[0].length; x++) {
                    Tile tile = tiles[y][x];
                    if (tile != null && tile.getType() == TileType.SHIPPINGBIN) {
                        batch.draw(TileType.FARM.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                        batch.draw(GameAssetManager.Shipping_Bin, x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                    }
                }
            }

        }
    }
    private void drawTiles(int rows, Tile[][] tiles, int tileSize) {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                TileType tile = tiles[y][x].getType();
                if (tile != null && tile.getTexture() != null) {
                    batch.draw(tile.getTexture(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
                if (tiles[y][x].isHasBeenBurt()) {
                    batch.draw(GameAssetManager.burntTile, x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
        clockHud.resize(width, height);
        // Update stage viewport
        //stage.getViewport().update(width, height, true);
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
        clockHud.dispose();
    }

    private void drawPlayer() {
        if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

        Tile tile = currentPlayer.getCurrentTile();
        int tileSize = GameAssetManager.TILE_SIZE;

        int tileX = tile.getX();
        int tileY = tile.getY();

        // Flip the Y-axis to match rendering coordinates
        int drawX = tileX * tileSize;
        int drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - tileY - 1) * tileSize;

        if(!currentPlayer.hasFainted()) {
            // Clamp moveDirection to valid index range
            int moveDirection = MathUtils.clamp(currentPlayer.getMovingDirection(), 0, playerAnimations.size() - 1);

            Animation<TextureRegion> currentAnimation = playerAnimations.get(moveDirection);
            TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

            // Draw player with height of 2 tiles
            batch.draw(currentFrame, drawX, drawY, tileSize, tileSize * 2);
        }
        else{
            //batch.draw(GameAssetManager.playerAtlas.findRegion("player_0_1"), drawX, drawY, tileSize, tileSize * 2);
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
        Tile[][] map = MainApp.getInstance().getCurrentGame().getMap().getMap();
        if (isProduct) {
            return map[y][x].getProductOfGrowable().getGrowableType() == GrowableType.Giant;
        } else {
            return map[y][x].getContainedGrowable().getGrowableType() == GrowableType.Giant;
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

    private boolean tryMove(int dx, int dy, int direction) {
        int x = currentPlayer.getCurrentTile().getX();
        int y = currentPlayer.getCurrentTile().getY();
        int newX = x + dx;
        int newY = y + dy;

        if (newX >= 0 && newY >= 0 &&
            newY < MainApp.getInstance().getCurrentGame().getMap().getMap().length &&
            newX < MainApp.getInstance().getCurrentGame().getMap().getMap()[0].length &&
            MainApp.getInstance().getCurrentGame().getMap().getMap()[newY][newX].getisWalkable() &&
            !(MainApp.getInstance().getCurrentGame().getMap().isInsideAnyFarm(newX, newY) != null &&
                !(MainApp.getInstance().getCurrentGame().getMap().getMap()[newY][newX].getTileOwner().equals(currentPlayer.getUsername()) ||
                    (currentPlayer.getPartner() != null &&
                        MainApp.getInstance().getCurrentGame().getMap().getMap()[newY][newX].getTileOwner().equals(currentPlayer.getPartner().getUsername()))))) {

            currentPlayer.setCurrentTile(MainApp.getInstance().getCurrentGame().getMap().getMap()[newY][newX]);
//            currentPlayer.setEnergy((int) (currentPlayer.getEnergy() - (0.0005 * currentPlayer.getEnergy())));
//            int newTurnEnergy = Math.max(0, (int) (currentPlayer.getCurrentTurnEnergy() - (0.0005 * currentPlayer.getEnergy())));
//            currentPlayer.setCurrentTurnEnergy(newTurnEnergy);
            //currentPlayer.reduceEnergy(5);
            currentPlayer.setMovingDirection(direction);
            setCameraPosition();
            camera.update();
            return true;
        }
        return false;
    }

    private void updateCrowFlightSpawn() {
        int currentHour = MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour();

        Iterator<Integer> it = crowAttacks.iterator();
        while (it.hasNext()) {
            int scheduledHour = it.next();
            if (scheduledHour == currentHour) {
                float startX = camera.position.x + camera.viewportWidth / 2f; // spawn just off the right side
                float screenHeight = camera.viewportHeight;

                activeCrows.add(new CrowFlight(camera, GameAssetManager.crowAnimation));
                it.remove(); // Prevent retriggering the same attack
            }
        }
    }


    private void renderCrowFlights(SpriteBatch batch, float deltaTime) {
        float screenLeft = camera.position.x - camera.viewportWidth / 2f;

        Iterator<CrowFlight> iterator = activeCrows.iterator();
        while (iterator.hasNext()) {
            CrowFlight crow = iterator.next();
            crow.time += deltaTime;

            // Move based on duration to cross screen
            crow.x -= (camera.viewportWidth / crow.duration) * deltaTime;

            TextureRegion frame = crow.animation.getKeyFrame(crow.time, true);

            float crowWidth = 128;
            float crowHeight = 128;

            batch.draw(frame, crow.x, crow.y, crowWidth, crowHeight);

            if (crow.isFinished(screenLeft)) {
                iterator.remove();
            }
        }
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
        //           System.out.println(storeController.upgradeTool(matcher.group("tool").trim()));
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





