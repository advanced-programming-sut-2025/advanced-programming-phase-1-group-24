package io.github.stardew.mini.client.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.Friendships.FriendshipMessage;
import io.github.stardew.mini.Model.Reccepies.*;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.server.Controller.GameController;
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.Controller.HouseMenuController;
import io.github.stardew.mini.server.Controller.StoreMenuController;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Animals.AnimalProduct;
import io.github.stardew.mini.Model.*;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Animals.CrowFlight;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.Assets.InventoryAssets;
import io.github.stardew.mini.client.Assets.TreeAssets;
import io.github.stardew.mini.Model.NPCManagement.NPC;
import io.github.stardew.mini.Model.NPCManagement.NPCMission;
import io.github.stardew.mini.Model.Skill;
import io.github.stardew.mini.Model.Growables.*;
import io.github.stardew.mini.Model.Friendships.Friendship;
import io.github.stardew.mini.Model.Friendships.Gift;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Growables.CropType;
import io.github.stardew.mini.Model.Growables.GrowableType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Places.*;
import io.github.stardew.mini.Model.Places.GreenHouse;
import io.github.stardew.mini.Model.Things.*;
import io.github.stardew.mini.Model.Tools.FishingPole;
import io.github.stardew.mini.Model.Tools.FishingAttemptOutcome;
import io.github.stardew.mini.Model.Tools.FishingMinigameData;
import io.github.stardew.mini.Model.Things.ProductQuality;
import io.github.stardew.mini.Model.Tools.Tool;
import io.github.stardew.mini.Model.Tools.TrashCan;
import io.github.stardew.mini.Model.Reccepies.Machine;
import io.github.stardew.mini.Model.Reccepies.randomStuff;
import io.github.stardew.mini.Model.Places.Shop;
import io.github.stardew.mini.Model.Places.ShopItem;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.TimeManagement.*;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.LightningFlash;
import io.github.stardew.mini.Model.TimeManagement.RainDrop;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.User;
import org.jetbrains.annotations.NotNull;

//import java.awt.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class GameView implements Screen, InputProcessor, AppMenu, FishingMinigameDialog.FishingMinigameCallback {
    private Stage stage;
    private TextButton friendsButton;
    private Dialog friendsDialog;
    private Dialog skillsDialog;
    private GameController controller;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private User currentPlayer;  //should change whenever currentPlayer in Game is changed
    private float stateTime = 0f;
    private boolean showFullMap = true;
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
    private TextButton recipesButton, cancelButton, cheatButton, exitButton, grabButton;
    private String pendingMachineName;
    private String pendingProductName;
    private Item itemToPlace;

    private NPC selectedNPC;
    private Dialog npcMenuDialog;
    private Dialog npcQuestDialog;
    private Dialog npcFriendshipDialog;
    private Dialog npcSpeechBubbleDialog;
    private Map<NPC, TextButton> npcTalkButtons = new HashMap<>();

    private Map<User, ImageButton> playerFridgeButtons = new HashMap<>();
    private Dialog fridgeMenuDialog;
    private Table fridgeMenuTable;
    private boolean showFridgeMenu = false;
    private int selectedFridgeSlot = 0;
    private House currentHouseForFridge;

    private Dialog settingsMenuDialog = null;
    private Table settingsMenuTable;
    private boolean isSettingsMenuCurrentlyVisible = false;

    private Dialog cookingMenuDialog;
    private Table cookingMenuTable;
    private boolean showCookingMenu = false;

    private Dialog buildingMenuDialog;
    private Table buildingMenuTable;
    private boolean showBuildingMenu = false;

    private Texture buffActiveTexture;

    private float moveCooldown = 0f;
    private static final float MOVE_INTERVAL = 0.1f; // seconds between steps

    private Dialog shopMenuDialog;
    private Shop selectedShop;
    private Dialog shopPurchaseDialog;
    private Dialog numItemDialog;
    private ShopItem selectedShopItem;
    private int purchaseQuantity = 1;
    private Dialog buyAnimalDialog;

    private StoreMenuController storeController;
    private HouseMenuController houseMenuController;
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
    public static TextureRegion faintTexture;
    public static TextureRegion proposingTexture;
    public static TextureRegion acceptingTexture;
    public static TextureRegion rejectingTexture;


    private boolean showToolsMenu = false;
    private boolean showInventoryMenu = false;
    private boolean showBackpackMenu = false;
    private BitmapFont smallFont;
    private BitmapFont smallerButtonFont;
    private int selectedSlot = 0;
    private Table toolMenuTable;
    private Table inventoryMenuTable;
    private Table backpackMenuTable;
    public static float toolUsageStateTime = 0f;
    public static boolean isToolBeingUsed = false;
    private Label animalInfoLabel;

    private Dialog relationshipDialog;
    private final List<Flower> activeFlowers = new ArrayList<>();
    private final List<Hay> activeHays = new ArrayList<>();

    //private TextButton nextTurnButton;
    private TextButton exitGameButton;
    private TextButton forceTerminateButton;
    private Label energyLabel;

    private Dialog socialMenuDialog;
    private Dialog MissionsMenuDialog;

    private FishingMinigameDialog fishingMinigameDialog;
    private Fish currentCaughtFish;
    private boolean isFishingActive = false;

    private Item equippedItem = null;
    private Table equippedItemSlotTable;
    private Timer.Task gameTickTask;
    private boolean hasShownFaintMessage = false; // Add this to your screen class
    private String scenario = "";
    String giftReciever, artisanName;

    private Table leaderboardTable;


    private void loadFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/stardew-valley.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        smallFont = generator.generateFont(parameter);

        FreeTypeFontGenerator.FreeTypeFontParameter smallerParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        smallerParameter.size = 24;
        smallerButtonFont = generator.generateFont(smallerParameter);

        generator.dispose();
    }

    public GameView(GameController controller) {
        this.controller = controller;
        storeController = new StoreMenuController();
        houseMenuController = new HouseMenuController();
        controller.setView(this);
        this.batch = MainApp.getBatch();
        this.currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        loadFont();
        for (User user : MainApp.getInstance().getCurrentGame().getPlayers()) {
            MainApp.getInstance().getCurrentGame().getPlayerAddedMissions().put(user.getUsername(), new ArrayList<>());
        }
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

    public void startPlacingItem(Item item) {
        showFullMap = false;
        this.isPlacingBuilding = true;
        this.itemToPlace = item;
        this.currentFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer);
        setCameraPosition();
        camera.update();
        Gdx.input.setInputProcessor(this);
        showErrorDialog(stage, "Select a tile inside your farm to place item");
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
        Tile tile = tiles[farm.getY() + (farm.getHeight() / 2)][farm.getX() + (farm.getWidth() / 2)];
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
//            animal.updateIsInHabitat();
                if (!animal.updateIsInHabitat()) {
                    animal.feed();
                }

                // Only try to assign a new path if animal is not moving
                // and its personal cooldown allows it
                if (!animal.itMoving() && !animal.isInHabitat()) {
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
                if (candidate != null && candidate.canBuildOn() &&
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
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // 4-directional

        for (int[] dir : directions) {
            int nx = tile.getX() + dir[0];
            int ny = tile.getY() + dir[1];
            Tile neighbor = MainApp.getInstance().getCurrentGame().getMap().getTile(nx, ny);
            if (neighbor != null && neighbor.canBuildOn() && neighbor.getContainedAnimal() == null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private void drawAnimals(int rows, int tileSize) {
        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            for (Animal animal : player.getOwnedAnimals()) {
                float x, y;

                if (animal.itMoving()) {
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
                    if (animal.getCurrentTile() == null) continue;
                    x = animal.getCurrentTile().getX() * tileSize;
                    y = (rows - animal.getCurrentTile().getY() - 1) * tileSize;
                }
                if (animal.getCurrentTile() == null) continue;
                batch.draw(animal.getAnimalType().getTexture(), x, y, tileSize, tileSize);
                if(animal.getProduct() !=null ){
                    batch.draw(animal.getProduct().getAnimalProductType().getTexture(), x, y, tileSize/2, tileSize/2);
                }
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

                    if (tile != null && tile.canBuildOn()) {
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
        if (isFishingActive) {
            if (keycode == Input.Keys.SPACE) {
                fishingMinigameDialog.setGreenBarMovingUp(true);
                return true;
            }
            return true;
        }
        if (keycode == Input.Keys.J) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);
            String direction = "";
            if (mousePos.x > camera.position.x) {
                direction = "right";
            } else if (mousePos.x < camera.position.x) {
                direction = "left";
            } else if (mousePos.y > camera.position.y) {
                direction = "up";
            } else if (mousePos.y < camera.position.y) {
                direction = "down";
            }
            Result result;
            if (equippedItem == null) {
                showErrorDialog(stage, "Pick a seed first!");
            } else {
                //result = controller.plantGrowable(equippedItem.getName(), direction);
                //if(!result.isSuccessful()) showErrorDialog(stage, result.getMessage());
                Map<String, Object> params = new HashMap<>();
                params.put("seedName", equippedItem.getName());
                params.put("direction", direction);

                MainApp.getInstance().getNetworkClient()
                    .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                        "GameController", "plantGrowable", params, currentPlayer.getUsername())
                    .thenAccept(response -> {
                        if (response.getStatus() != 200) {
                            Gdx.app.postRunnable(() -> {
                                showErrorDialog(stage, response.getMessage());
                        });
                        }
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        Gdx.app.postRunnable(() -> {
                            showErrorDialog(stage, "Failed to plant growbale: " + ex.getMessage());
                        });
                        return null;
                    });
            }
        }
        if(keycode == Input.Keys.O){
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);
            String direction = "";
            if (mousePos.x > camera.position.x) {
                direction = "right";
            } else if (mousePos.x < camera.position.x) {
                direction = "left";
            } else if (mousePos.y > camera.position.y) {
                direction = "up";
            } else if (mousePos.y < camera.position.y) {
                direction = "down";
            }
            if(equippedItem == null) {
                showErrorDialog(stage, "Pick a fertilizer first!");
            }
            else{
                Result result = controller.fertalizeGrowable(equippedItem.getName(), direction);
                showErrorDialog(stage,result.getMessage());
            }
        }
        if (keycode == Input.Keys.C) {
            if (showToolsMenu) {
                if (showInventoryMenu || showBackpackMenu || (cookingMenuDialog != null && showCookingMenu)) return false;
                Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(mousePos);
                useSelectedTool(mousePos.x, mousePos.y);
                return true;
            } else {
                showCookingMenu = !showCookingMenu;
                showCookingMenu();
                return true;
            }
        }

        if (keycode == Input.Keys.B) {
            showBuildingMenu = !showBuildingMenu;
            showBuildingMenu();
            return true;
        }

        if (showFridgeMenu) {
            int totalSlots = 24;
            int maxItemsPerRow = 6;
            if (keycode == Input.Keys.LEFT) {
                selectedFridgeSlot--;
                if (selectedFridgeSlot < 0) selectedFridgeSlot = totalSlots - 1;
                updateFridgeMenuTable();
                return true;
            }
            if (keycode == Input.Keys.RIGHT) {
                selectedFridgeSlot++;
                if (selectedFridgeSlot >= totalSlots) selectedFridgeSlot = 0;
                updateFridgeMenuTable();
                return true;
            }
            if (keycode == Input.Keys.UP) {
                selectedFridgeSlot -= maxItemsPerRow;
                if (selectedFridgeSlot < 0) selectedFridgeSlot = Math.max(0, totalSlots - 1);
                updateFridgeMenuTable();
                return true;
            }
            if (keycode == Input.Keys.DOWN) {
                selectedFridgeSlot += maxItemsPerRow;
                if (selectedFridgeSlot >= totalSlots) selectedFridgeSlot = Math.min(totalSlots - 1, selectedFridgeSlot % maxItemsPerRow);
                updateFridgeMenuTable();
                return true;
            }
            if (keycode == Input.Keys.ENTER) {
                handleGrabFromFridge();
                return true;
            }
            if (keycode == Input.Keys.ESCAPE) {
                fridgeMenuDialog.hide();
                showFridgeMenu = false;
                Gdx.input.setInputProcessor(GameView.this);
                return true;
            }
        }
        if (keycode == Input.Keys.ENTER) {
            if (equippedItem != null) {
                equippedItem = null;
            }
        }
        if (keycode == Input.Keys.G) {
            if (equippedItem != null) {
                Result eatResult = controller.eat(equippedItem.getName());
                if (eatResult.isSuccessful()) {
                    showErrorDialog(stage, eatResult.message());
                    equippedItem = null;
                    updateEquippedItemSlot();
                } else {
                    showErrorDialog(stage, eatResult.message());
                }
            } else {
                showErrorDialog(stage, "No item equipped to eat!");
            }
            return true;
        }

        if (keycode == Input.Keys.E) {
            if (socialMenuDialog != null && socialMenuDialog.getStage() != null && socialMenuDialog.isVisible()) {
                socialMenuDialog.hide();
                Gdx.input.setInputProcessor(this);
                return true;
            }
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
                        if (skillsDialog != null && skillsDialog.getStage() != null) {
                            skillsDialog.hide();
                            skillsDialog = null;
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
                showBackpack();
                return true;
            }
            if (keycode == Input.Keys.RIGHT) {
                selectedSlot++;
                if (selectedSlot >= totalItems) selectedSlot = 0;
                showBackpack();
                return true;
            }
            if (keycode == Input.Keys.UP) {
                selectedSlot -= maxItemsPerRow;
                if (selectedSlot < 0) selectedSlot = Math.max(0, totalItems - 1);
                showBackpack();
                return true;
            }
            if (keycode == Input.Keys.DOWN) {
                selectedSlot += maxItemsPerRow;
                if (selectedSlot >= totalItems)
                    selectedSlot = Math.min(totalItems - 1, selectedSlot % maxItemsPerRow); // Wrap to first row, maintaining column
                showBackpack();
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

        if (keycode == Input.Keys.NUM_3) {
            if (showInventoryMenu || showBackpackMenu) return false;
            for(Friendship friendship : MainApp.getInstance().getCurrentGame().getAllFriendships()){
                friendship.setLevel(3);
            }
            return true;
        }
        if(keycode == Input.Keys.NUM_2){
            if (showInventoryMenu || showBackpackMenu) return false;
            for(Friendship friendship : MainApp.getInstance().getCurrentGame().getAllFriendships()){
                friendship.setLevel(2);
            }
            return true;

        }
        if(keycode == Input.Keys.NUM_1){
            if (showInventoryMenu || showBackpackMenu) return false;
            for(Friendship friendship : MainApp.getInstance().getCurrentGame().getAllFriendships()){
                friendship.setLevel(1);
            }
            return true;
        }
        if(keycode == Input.Keys.NUM_0){
            if (showInventoryMenu || showBackpackMenu) return false;
            for(Friendship friendship : MainApp.getInstance().getCurrentGame().getAllFriendships()){
                friendship.setLevel(0);
            }
            return true;
        }
        if (keycode == Input.Keys.K) {
            Tile tile = currentPlayer.getCurrentTile();
            Machine machine = (Machine) tile.getContainedItem();
            machine.setHoursLeft(machine.getHoursLeft() - 10);

        }
        if (keycode == Input.Keys.Z) {
            Tile tile = currentPlayer.getCurrentTile();
            Machine machine = (Machine) tile.getContainedItem();
            //machine.useMachine("Coffee",currentPlayer);
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
        if (keycode == Input.Keys.L) {
            currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
            Tile tile = currentPlayer.getCurrentTile();
            Tile neededTile = MainApp.getInstance().getCurrentGame().getMap().getTile(tile.getX() - 1, tile.getY());
            System.out.println(neededTile);
            System.out.println(neededTile.getContainedGrowable());
            System.out.println(neededTile.getContainedGrowable().getCurrentStage());
            if(neededTile.getContainedGrowable() != null) {
                System.out.println(neededTile.getContainedGrowable().getAge());
                System.out.println(neededTile.getContainedGrowable().getGrowableType());
                System.out.println(neededTile.getContainedGrowable().getDaysLeftToDie());
            }
            System.out.println(neededTile.getContainedItem());
            System.out.println(neededTile.getContainedNPC());
            System.out.println(neededTile.getProductOfGrowable());
            if(neededTile.getProductOfGrowable() != null){
                System.out.println(neededTile.getProductOfGrowable().getGrowableType());
            }
            System.out.println(neededTile.isHasBeenBurt());
            System.out.println(neededTile.getisWalkable());
            System.out.println(neededTile.getContainedAnimal());
        }

        if (keycode == Input.Keys.V) {
            controller.crowAttack();
        }

        if(keycode == Input.Keys.R){
            if(equippedItem == null){
                showErrorDialog(stage,"First choose an item!");
            }
            else {
                itemToPlace = equippedItem;
                startPlacingItem(itemToPlace);
            }

        }
        if (keycode == Input.Keys.I) {
            currentPlayer.setEnergy(200);
            currentPlayer.setFainted(false);
        }
        if (keycode == Input.Keys.Q) {
            MainApp.getInstance().getCurrentGame().getTimeAndDate().setHour(23);
            controller.handleEndOfDay();
            //MainApp.getInstance().getCurrentGame().getFriendship("kimia8", "user2").setLevel(3);
            //currentPlayer.getRecievedGift().add(new Gift("john", "kimia8", new randomStuff(10, randomStuffType.Stone), 5));
            //controller.sendGift("john", "Stone", "10");
        }
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
        if (showInventoryMenu || showBackpackMenu) return false;
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
        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        stage.getCamera().unproject(touchPos); // convert to stage coords
        float mouseX = touchPos.x;
        float mouseY = touchPos.y;

        if (isClickInside(mouseX, mouseY, friendsButton)) {
            if (friendsDialog != null) {
                friendsDialog.remove();
            }
            createFriendsDialog();
            friendsDialog.setVisible(true);
            friendsDialog.show(stage);
            Gdx.input.setInputProcessor(stage);
            return true;
        }
        if (isClickInside(mouseX, mouseY, exitGameButton)) {
            //Result result = controller.exitGame();
            Map<String, Object> params = new HashMap<>();
            MainApp.getInstance().getNetworkClient()
                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController", "exitGame", params, currentPlayer.getUsername())
                .thenAccept(response -> {
                    if (response.getStatus() == 200) {
                        Gson gson = new Gson();
                        // convert response.body (which is Object) to JSON string then parse as Result
                        Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);

                        if (!result.isSuccessful()) {
                            Gdx.app.postRunnable(() -> showErrorDialog(stage, result.message()));
                        } else {
                            Gdx.app.postRunnable(() -> {
                                MainApp.getInstance().setCurrentGame(null);
                                MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
                                MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
                            });
                        }
                    } else {
                        Gdx.app.postRunnable(() -> {
                            showErrorDialog(stage, "Failed to exit game: " + response.getMessage());
                        });
                    }
                });
            return true;
        }
        if (isClickInside(mouseX, mouseY, forceTerminateButton)) {
            Result result = controller.startForceTerminateVote();
            if (!result.isSuccessful()) {
                showErrorDialog(stage, result.message());
            }
            return true;
        }
//        if (isClickInside(mouseX, mouseY, nextTurnButton)) {
//            if(equippedItem != null) {
//                equippedItem = null;
//            }
//            //if (Gdx.input.getInputProcessor() != GameView.this || isAnyDialogOpen()) {
////                System.out.println("touchdown");
////                if (isAnyDialogOpen()) {
////                    //showErrorDialog(stage,"Cannot end turn while another menu is open.");
////                    showTimedErrorLabel(stage, "Cannot end turn while another menu is open.", 2f);
////                    return true;
////                }
//            Result result = controller.nextTurn();
//            if (!result.isSuccessful()) {
//                showErrorDialog(stage, result.message());
//            }
//            return true;
//        }
        if (stage.touchDown(screenX, screenY, pointer, button)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu) {
            return stage.touchDown(screenX, screenY, pointer, button);
        }
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            return stage.touchDown(screenX, screenY, pointer, button);
        }
        if (isPlacingBuilding && currentFarm != null && !terminalVisible && itemToPlace != null) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            int tileX = (int) (worldCoords.x / GameAssetManager.TILE_SIZE);
            int tileY = MainApp.getInstance().getCurrentGame().getMap().getHeight() - (int) (worldCoords.y / GameAssetManager.TILE_SIZE) - 1;

            if (isInsideFarm(tileX, tileY)) {

                Tile tile = MainApp.getInstance().getCurrentGame().getMap().getMap()[tileY][tileX];

                if (tile != null && tile.canBuildOn()) {

                    Result result = houseMenuController.placeItem(itemToPlace.getName(),tile);
                    showErrorDialog(stage,result.getMessage());

                } else {
                    showErrorDialog(stage, "Tile is not buildable.");
                }
            } else {
                showErrorDialog(stage, "Please click inside your farm.");
            }
            //setBuilding(new Building(buildingToPlace));
            isPlacingBuilding = false;
            itemToPlace = null;
            buildingToPlace = null;
            currentFarm = null;
            return true;
        }
        if (isPlacingBuilding && currentFarm != null && !terminalVisible) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            int tileX = (int) (worldCoords.x / GameAssetManager.TILE_SIZE);
            int tileY = MainApp.getInstance().getCurrentGame().getMap().getHeight() - (int) (worldCoords.y / GameAssetManager.TILE_SIZE) - 1;

            if (isInsideFarm(tileX, tileY)) {

                Tile tile = MainApp.getInstance().getCurrentGame().getMap().getMap()[tileY][tileX];

                if (tile != null && tile.canBuildOn()) {
                    if (buildingToPlace == null || isAreaPlaceable(tileX, tileY, buildingToPlace.getWidth(), buildingToPlace.getHeight())) {
                        //Result result;
                        ////////////////////////////////////////////////////////////////////////////////////////////////////
//                        if (buildingToPlace == null) {
//                            result = storeController.buyFromCarpenter(selectedShop,"Shipping Bin", Integer.toString(tileX), Integer.toString(tileY));
//                        } else {
//                            buildingToPlace.setX(tileX);
//                            buildingToPlace.setY(tileY);
//                            result = storeController.buyFromCarpenter(selectedShop,buildingToPlace.getHabitatType().getName(), Integer.toString(tileX), Integer.toString(tileY));
//                        }
//                        if (result.isSuccessful()) {
//                            //updateHabitatTiles();
//                            showErrorDialog(stage, "Building placed!");
//                        } else {
//                            showErrorDialog(stage, result.message());
//                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        String username = MainApp.getInstance().getLoggedInUser().getUsername();

                        Map<String, Object> body = new HashMap<>();
                        body.put("shopName", selectedShop.getShopName());

                        if (buildingToPlace == null) {
                            body.put("itemName", "Shipping Bin");
                        } else {
                            body.put("itemName", buildingToPlace.getHabitatType().getName());
                        }
                        body.put("x", tileX + "");
                        body.put("y", tileY + "");

                        MainApp.getInstance().getNetworkClient()
                            .sendPost(
                                MainApp.getInstance().getCurrentGame().getNetworkId(),
                                "StoreMenuController",
                                "buyFromCarpenter",
                                body,
                                username
                            ).thenAccept(response -> {
                                Gson gson = new Gson();
                                Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);

                                Gdx.app.postRunnable(() -> {
                                    if (result.isSuccessful()) {
                                        showErrorDialog(stage, "Building placed!");
                                        // Optionally: refresh map/buildings
                                    } else {
                                        showErrorDialog(stage, result.message());
                                    }
                                });
                            });

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
                if (button == Input.Buttons.RIGHT) { // Check for right mouse button click
                    if (tile != null && tile.getContainedNPC() != null) {
                        selectedNPC = tile.getContainedNPC();

                        Vector3 stageCoords = stage.getViewport().unproject(new Vector3(screenX, screenY, 0));

                        npcMenuDialog.setPosition(
                            stageCoords.x - npcMenuDialog.getWidth() / 2,
                            stageCoords.y - npcMenuDialog.getHeight() / 2
                        );
                        npcMenuDialog.setVisible(true);
                        npcMenuDialog.show(stage);
                        Gdx.input.setInputProcessor(stage);
                        return true;
                    }
                }

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
                    updateAnimalInfoLabel();
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
                if (tile != null && tile.getType() == TileType.SHIPPINGBIN) {
                    //TODO: open inventory
                    //TODO: open a very similar dialog to purchase window
                    //TODO: call storeController.placingInShippingBin
                    scenario = "Sell";
                    Gdx.input.setInputProcessor(GameView.this);
                    showBackpack();
                }
                if (tile != null && tile.getContainedItem() != null && tile.getContainedItem() instanceof Machine) {
                    System.out.println("machine");
                    selectedMachine = (Machine) tile.getContainedItem();

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
                if (tile != null && tile.getType() == TileType.GREENHOUSE &&
                    !MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getGreenHouse().getIsGreenHouseFixed()) {
                    Result result = controller.buildGreenHouse();
                    if (!result.isSuccessful()) {
                        showErrorDialog(stage, result.message());
                    }
                }
                if (tile != null) {
                    for (User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()) {
                        if (otherPlayer.getUsername().equals(currentPlayer.getUsername())) continue;
                        if (otherPlayer.getCurrentTile().getX() == tileX && otherPlayer.getCurrentTile().getY() == tileY
                            && controller.isAdjacent(currentPlayer.getCurrentTile(), otherPlayer.getCurrentTile())) {
                            if (relationshipDialog != null) {
                                relationshipDialog.remove();
                            }
                            createRelationshipDialog(otherPlayer.getUsername());
                            relationshipDialog.setVisible(true);
                            relationshipDialog.show(stage);
                            Gdx.input.setInputProcessor(stage);
                            return true;
                        }
                    }
                }
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

    public boolean isAreaPlaceable(int x, int y, int width, int height) {
        Game game = MainApp.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();
        Farm farm = map.getFarmByOwner(player);

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                // Check bounds
                if (i < farm.getX() || i >= farm.getX() + farm.getWidth() ||
                    j < farm.getY() || j >= farm.getY() + farm.getHeight()) {
                    return false;
                }
                // Check occupation
                if (isOccupied(i, j)) {
                    return false;
                }
                Tile tile = map.getTile(x, y);
                if (tile.getType() != TileType.FARM) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean isOccupied(int x, int y) {
        MapOfGame map = MainApp.getInstance().getCurrentGame().getMap();
        Tile tile = map.getTile(x, y);
        return tile != null && (
            tile.getContainedGrowable() != null ||
                tile.getProductOfGrowable() != null ||
                tile.getContainedItem() != null
        ); // or tile.isBlocked() if such a method exists
    }
    @Override
    public boolean keyUp(int i) {
        if (isFishingActive) {
            if (i == Input.Keys.SPACE) {
                fishingMinigameDialog.setGreenBarMovingUp(false);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        if (stage.touchUp(i, i1, i2, i3)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu || showFridgeMenu) {
            return stage.touchUp(i, i1, i2, i3);
        }
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            return stage.touchUp(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        if (stage.touchCancelled(i, i1, i2, i3)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu || showFridgeMenu) {
            return stage.touchCancelled(i, i1, i2, i3);
        }
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            return stage.touchUp(i, i1, i2, i3);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        if (stage.touchDragged(i, i1, i2)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu || showFridgeMenu) {
            return stage.touchDragged(i, i1, i2);
        }
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            return stage.touchDragged(i, i1, i2);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        if (stage.mouseMoved(i, i1)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu || (skillsDialog != null && skillsDialog.getStage() != null) || showFridgeMenu) {
            return stage.mouseMoved(i, i1);
        }
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        if (stage.scrolled(v, v1)) {
            return true;
        }
        if (showInventoryMenu || showBackpackMenu || showFridgeMenu) {
            return stage.scrolled(v, v1);
        }
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            return stage.scrolled(v, v1);
        }
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
                        } else if (item.getShopItemType() == ShopItemType.TOOL_UPGRADE) {
                            String username = MainApp.getInstance().getLoggedInUser().getUsername();

                            Map<String, Object> body = new HashMap<>();
                            body.put("shopName", selectedShop.getShopName());
                            body.put("itemName", item.getName());

                            MainApp.getInstance().getNetworkClient()
                                .sendPost(
                                    MainApp.getInstance().getCurrentGame().getNetworkId(),
                                    "StoreMenuController",
                                    "upgradeTool",
                                    body,
                                    username
                                ).thenAccept(response -> {
                                    Gdx.app.postRunnable(() -> {
                                        showErrorDialog(stage, response.getMessage());
                                    });
                                });
                            //////////////////////////////////////////////////////////////////////////////////////////
//                            Result result = storeController.upgradeTool(selectedShop,item.getName());
//                            showErrorDialog(stage,result.message());
                        } else {
                            purchaseQuantity = 1;
                            showPurchaseDialog();
                        }
                        shopMenuDialog.hide();
                        shopMenuDialog.setVisible(false);
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
                ///    ////////////////////////////////////////////////////////////////
                String username = MainApp.getInstance().getLoggedInUser().getUsername();

                Map<String, Object> body = new HashMap<>();
                body.put("shopName", selectedShop.getShopName());
                body.put("itemName", item.getName());
                body.put("animalName", enteredName);

                MainApp.getInstance().getNetworkClient()
                    .sendPost(
                        MainApp.getInstance().getCurrentGame().getNetworkId(),
                        "StoreMenuController",
                        "buyAnimal",
                        body,
                        username
                    ).thenAccept(response -> {
                        Gdx.app.postRunnable(() -> {
                            showErrorDialog(stage, response.getMessage());
                        });
                    });
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                Result result = storeController.buyAnimal(selectedShop,item.getName(), enteredName);
//                buyAnimalDialog.hide();
//                showErrorDialog(stage, result.message());
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
                /// ///////////////////////////////////////////////////////////////////////////////////////////
                String username = MainApp.getInstance().getLoggedInUser().getUsername();

                Map<String, Object> body = new HashMap<>();
                body.put("shopName", selectedShop.getShopName()); // Or whatever uniquely identifies the shop
                body.put("itemName", selectedShopItem.getName());
                body.put("count", purchaseQuantity);

                MainApp.getInstance().getNetworkClient()
                    .sendPost(
                        MainApp.getInstance().getCurrentGame().getNetworkId(),
                        "StoreMenuController",
                        "purchase",
                        body,
                        username
                    ).thenAccept(response -> {
                    Gdx.app.postRunnable(() -> {
                        showErrorDialog(stage, response.getMessage());
                       // if (response.getStatus() == 200) {
                            shopPurchaseDialog.hide();
                            shopPurchaseDialog.setVisible(false);
                       // }
                    });
                });
                // Result result = storeController.purchase(selectedShop,selectedShopItem, purchaseQuantity);
                //buyItem(currentPlayer, selectedShopItem, purchaseQuantity);
//                shopPurchaseDialog.hide();
//                shopPurchaseDialog.setVisible(false);
               // showErrorDialog(stage, result.message());
                /// ///////////////////////////////////////////////////////////////////////////////////////////
                //Gdx.input.setInputProcessor(GameView.this);
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                shopPurchaseDialog.hide();
                shopPurchaseDialog.setVisible(false);
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

    private void showNumItemDialog() {
        purchaseQuantity = 1;
        //numItemDialog.getContentTable().clearChildren();
        //numItemDialog.getTitleLabel().setText("Select Quantity " + selectedShopItem.getName());
        numItemDialog = new Dialog("select Number", GameAssetManager.skin);
        numItemDialog.padTop(40f);
        numItemDialog.setKeepWithinStage(true);
        numItemDialog.setMovable(false);
        numItemDialog.setVisible(false);
        numItemDialog.setModal(true);
        numItemDialog.setResizable(false);
        showBackpackMenu = false;
        backpackMenuTable.setVisible(false);
        inventoryMenuTable.setVisible(false);
        showInventoryMenu = false;

        Table content = numItemDialog.getContentTable();
        content.clear();
        content.defaults().pad(10);

        Label quantityLabel = new Label("Quantity: " + purchaseQuantity, GameAssetManager.skin, "custom-label");
        TextButton plusButton = new TextButton("+", GameAssetManager.skin, "custom-button");
        TextButton minusButton = new TextButton("-", GameAssetManager.skin, "custom-button");
        TextButton applyButton = new TextButton("Apply", GameAssetManager.skin, "custom-button");
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

        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result;
                switch (scenario){
                    case "Gift":
                        result=controller.sendGift(giftReciever,equippedItem.getName(),Integer.toString(purchaseQuantity));
                        break;
                    case "Machine":
                        result=controller.artisanUse(pendingMachineName,equippedItem.getName(),null,MainApp.getInstance().getCurrentGame().getMap());
                        break;
                    case "Sell":
                        result=storeController.placeInShippingBin(equippedItem.getName(),purchaseQuantity);
                        break;
                    default:
                        result = new Result(true,"");
                        break;

                }
                scenario = "";
                giftReciever = "";
                pendingMachineName = null;
                pendingProductName = null;
                //buyItem(currentPlayer, selectedShopItem, purchaseQuantity);
                numItemDialog.hide();
                if(!result.message().equals("")){
                    showTimedErrorLabel(stage, result.message(), 2);
                }
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                numItemDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        content.add(quantityLabel).colspan(2).row();
        content.add(minusButton).padRight(5);
        content.add(plusButton).row();
        content.add(applyButton).colspan(2).row();
        content.add(cancelButton).colspan(2);

        numItemDialog.add(content);
        numItemDialog.pack();
        numItemDialog.setPosition(
            (Gdx.graphics.getWidth() - numItemDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - numItemDialog.getHeight()) / 2
        );
        numItemDialog.pack();
        numItemDialog.setVisible(true);
        stage.addActor(numItemDialog);
        Gdx.input.setInputProcessor(stage);

    }

    private void createFridgeDialog() {
        fridgeMenuDialog = new Dialog("Fridge", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object object) {
            }
        };
        fridgeMenuDialog.padTop(40);
        fridgeMenuDialog.setKeepWithinStage(true);
        fridgeMenuDialog.setMovable(false);
        fridgeMenuDialog.setVisible(false);
        fridgeMenuDialog.setModal(true);
        fridgeMenuDialog.setResizable(false);
        fridgeMenuDialog.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!fridgeMenuDialog.isVisible()) {
                    Gdx.input.setInputProcessor(GameView.this);
                }
            }
        });
        fridgeMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

        fridgeMenuTable = new Table(GameAssetManager.skin);
        fridgeMenuTable.center().pad(10);
        fridgeMenuTable.defaults().pad(5);

        float slotSize = GameAssetManager.TILE_SIZE;
        for (int i = 0; i < 24; i++) {
            Image slotBg = new Image(InventoryAssets.slot);
            slotBg.setSize(slotSize, slotSize);
            fridgeMenuTable.add(new Stack(slotBg)).size(slotSize).pad(5);
            if ((i + 1) % 6 == 0) {
                fridgeMenuTable.row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(fridgeMenuTable, GameAssetManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        fridgeMenuDialog.getContentTable().add(scrollPane).expand().fill().row();

        TextButton putButton = new TextButton("Put", GameAssetManager.skin, "custom-button");
        putButton.setColor(Color.BLUE);
        putButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scenario = "PutToFridge";
                fridgeMenuDialog.hide();
                showFridgeMenu = false;
                showBackpackMenu = true;
                Gdx.input.setInputProcessor(GameView.this);
                showBackpack();
            }
        });

        TextButton grabButton = new TextButton("Grab", GameAssetManager.skin, "custom-button");
        grabButton.setColor(Color.GREEN);
        grabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleGrabFromFridge();
                fridgeMenuDialog.hide();
                showFridgeMenu = false;
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.setColor(Color.RED);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fridgeMenuDialog.hide();
                showFridgeMenu = false;
                Gdx.input.setInputProcessor(GameView.this); // Return input to game
            }
        });

        Table buttonTable = new Table();
        buttonTable.defaults().pad(10);
        buttonTable.add(putButton).width(100).height(40);
        buttonTable.add(grabButton).width(100).height(40);
        buttonTable.add(closeButton).width(100).height(40);
        fridgeMenuDialog.getContentTable().add(buttonTable).row();


        fridgeMenuDialog.pack();
        fridgeMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - fridgeMenuDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - fridgeMenuDialog.getHeight()) / 2
        );
        stage.addActor(fridgeMenuDialog);
    }

    public void createUI() {
        createTerminal();

        createAnimalDialog();

        createShopMenusDialogs();

        createMachineDialog();

        createNPCDialog();

        createNPCSpeechBubbleDialog();

        createFridgeDialog();
    }
    private void createNumItemDialog() {
        numItemDialog = new Dialog("select Number", GameAssetManager.skin);
        numItemDialog.padTop(40f);
        numItemDialog.setKeepWithinStage(true);
        numItemDialog.setMovable(false);
        numItemDialog.setVisible(false);
        numItemDialog.setModal(true);
        numItemDialog.setResizable(false);

        stage.addActor(numItemDialog);
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

    //    private void createAnimalDialog() {
//        // Create the animal menu dialog (initially hidden)
//        animalMenuDialog = new Dialog("Animal Menu", GameAssetManager.skin, "custom-window") {
//            @Override
//            protected void result(Object object) {
//                handleAnimalMenuChoice(object.toString());
//            }
//        };
//        animalMenuDialog.padTop(40);
//        animalMenuDialog.getContentTable().defaults().pad(10);
//
//        // Add buttons with their result objects
//        TextButton feedButton = new TextButton("Feed", GameAssetManager.skin, "custom-button");
//        feedButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                handleAnimalMenuChoice("feed");
//            }
//        });
//
//        TextButton petButton = new TextButton("Pet", GameAssetManager.skin, "custom-button");
//        petButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                handleAnimalMenuChoice("pet");
//            }
//        });
//
//        TextButton releaseButton = new TextButton("Release", GameAssetManager.skin, "custom-button");
//        releaseButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                handleAnimalMenuChoice("release");
//            }
//        });
//
//        TextButton sellButton = new TextButton("Sell", GameAssetManager.skin, "custom-button");
//        sellButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                handleAnimalMenuChoice("sell");
//            }
//        });
//
//        TextButton collectButton = new TextButton("Collect Product", GameAssetManager.skin, "custom-button");
//        collectButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                handleAnimalMenuChoice("collect");
//            }
//        });
//
//        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");
//        cancelButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                animalMenuDialog.hide();
//                Gdx.input.setInputProcessor(GameView.this);  // Return input to game
//                selectedAnimal = null;
//            }
//        });
//
//        animalMenuDialog.getContentTable().add(feedButton).row();
//        animalMenuDialog.getContentTable().add(petButton).row();
//        animalMenuDialog.getContentTable().add(releaseButton).row();
//        animalMenuDialog.getContentTable().add(sellButton).row();
//        animalMenuDialog.getContentTable().add(collectButton).row();
//        animalMenuDialog.getContentTable().add(cancelButton);
//
//        animalMenuDialog.setKeepWithinStage(true);
//        animalMenuDialog.setMovable(false);
//        animalMenuDialog.setVisible(false);  // Add this after creation
//        stage.addActor(animalMenuDialog);
//    }
    private void createAnimalDialog() {
        animalMenuDialog = new Dialog("Animal Menu", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object object) {
                handleAnimalMenuChoice(object.toString());
            }
        };

        animalMenuDialog.padTop(40);
        animalMenuDialog.getContentTable().defaults().pad(5);

        // ========== TOP INFO AREA ==========
        animalInfoLabel = new Label("", GameAssetManager.skin,"custom-label"); // <-- fixed here
        animalInfoLabel.setWrap(true);
        animalMenuDialog.getContentTable().add(animalInfoLabel).width(300).row();


        // ========== SHEPHERD INPUT FIELDS ==========
        TextField xField = new TextField("", GameAssetManager.skin);
        TextField yField = new TextField("", GameAssetManager.skin);
        xField.setMessageText("X");
        yField.setMessageText("Y");

        HorizontalGroup shepherdGroup = new HorizontalGroup();
        shepherdGroup.space(10);
        shepherdGroup.addActor(new Label("To:", GameAssetManager.skin,"custom-label"));
        shepherdGroup.addActor(xField);
        shepherdGroup.addActor(yField);

        TextButton shepherdButton = new TextButton("Shepherd", GameAssetManager.skin, "custom-button");
        shepherdButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int targetX = Integer.parseInt(xField.getText());
                    int targetY = Integer.parseInt(yField.getText());
                    handleAnimalMenuChoice("shepherd:" + targetX + "," + targetY);
                    xField.setText("");
                    yField.setText("");
                } catch (NumberFormatException e) {
                    showErrorDialog(stage, "Please enter valid coordinates.");
                }
            }
        });

        animalMenuDialog.getContentTable().add(shepherdGroup).row();
        animalMenuDialog.getContentTable().add(shepherdButton).row();

        // ========== BUTTONS ==========
        TextButton releaseButton = new TextButton("Release", GameAssetManager.skin, "custom-button");
        releaseButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("release");
            }
        });
        animalMenuDialog.getContentTable().add(releaseButton).row();
        TextButton feedButton = new TextButton("Feed", GameAssetManager.skin, "custom-button");
        feedButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("feed");
            }
        });
        animalMenuDialog.getContentTable().add(feedButton).row();

        TextButton petButton = new TextButton("Pet", GameAssetManager.skin, "custom-button");
        petButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("pet");
            }
        });
        animalMenuDialog.getContentTable().add(petButton).row();

        TextButton sellButton = new TextButton("Sell", GameAssetManager.skin, "custom-button");
        sellButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("sell");
            }
        });
        animalMenuDialog.getContentTable().add(sellButton).row();

        TextButton collectButton = new TextButton("Collect Product", GameAssetManager.skin, "custom-button");
        collectButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                handleAnimalMenuChoice("collect");
            }
        });
        animalMenuDialog.getContentTable().add(collectButton).row();

        TextButton cancelButton = new TextButton("Cancel", GameAssetManager.skin, "custom-button");
        cancelButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                animalMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
                selectedAnimal = null;
                xField.setText("");
                yField.setText("");
            }
        });
        animalMenuDialog.getContentTable().add(cancelButton).row();

        animalMenuDialog.setKeepWithinStage(true);
        animalMenuDialog.setMovable(false);
        animalMenuDialog.setVisible(false);
        stage.addActor(animalMenuDialog);
    }
    private void updateAnimalInfoLabel() {
        if (selectedAnimal == null || animalInfoLabel == null) return;

        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(selectedAnimal.getName()).append("\n");
        // info.append("Type: ").append(selectedAnimal.getAnimalType()).append("\n");
        info.append("Friendship: ").append(selectedAnimal.getFriendship()).append("\n");
        info.append("Fed: ").append(selectedAnimal.isFedToday()).append("\n");
        info.append("Petted: ").append(selectedAnimal.isPettedToday()).append("\n");
        info.append("In Habitat: ").append(selectedAnimal.isInHabitat()).append("\n");
        currentFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer);
        if (selectedAnimal.getLivingPlace() != null) {
            Habitat habitat = selectedAnimal.getLivingPlace();
            info.append("Habitat: (").append(habitat.getX()).append(",").append(habitat.getY())
                .append(")-(").append(habitat.getX() + habitat.getWidth()-1).append(",").append(habitat.getY()+habitat.getHeight()-1).append(")\n");
        }
        if (currentFarm != null) {
            info.append("Farm: (").append(currentFarm.getX()).append(",").append(currentFarm.getY())
                .append(")-(").append(currentFarm.getX() + currentFarm.getWidth()-1).append(",").append(currentFarm.getY()+currentFarm.getHeight()-1).append(")\n");
        }
        animalInfoLabel.setText(info.toString());
        animalInfoLabel.setFontScale(0.5f);
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
        recipesButton = new TextButton("Recepies", GameAssetManager.skin);
        recipesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();                       // hide the main menu
                handleMachineMenuChoice("Recepies");
            }
        });

        cancelButton = new TextButton("Cancel", GameAssetManager.skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();
                handleMachineMenuChoice("Cancel");

            }
        });

        cheatButton = new TextButton("Cheat", GameAssetManager.skin);
        cheatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleMachineMenuChoice("Cheat");
                machineMenuDialog.hide();
            }
        });



        exitButton = new TextButton("Exit", GameAssetManager.skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);  // Return input to game
                selectedMachine = null;
            }
        });


        grabButton = new TextButton("Grab Product", GameAssetManager.skin);
        grabButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                machineMenuDialog.hide();
                handleMachineMenuChoice("Grab Product");
                Gdx.input.setInputProcessor(GameView.this);  // Return input to game
                selectedMachine = null;
            }
        });

        grabButton.setVisible(false);




        machineMenuDialog.getContentTable().add(recipesButton).row();
        machineMenuDialog.getContentTable().add(cancelButton).row();
        machineMenuDialog.getContentTable().add(cheatButton).row();
        machineMenuDialog.getContentTable().add(exitButton).row();
        machineMenuDialog.getContentTable().add(grabButton).row();

        machineMenuDialog.setKeepWithinStage(true);
        machineMenuDialog.setMovable(false);
        machineMenuDialog.setVisible(false);  // Add this after creation
        stage.addActor(machineMenuDialog);

    }

    private void showRecipeDialog(Machine machine) {
        Dialog dlg = new Dialog("", GameAssetManager.skin) {
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
        tbl.add(new Label("", GameAssetManager.skin)).row();
        // One line per product
        for (randomStuffType prod : machine.getType().getProducts()) {
            StringBuilder ing = new StringBuilder();
            prod.getIngredients().forEach((name, qty) ->
                ing.append(name).append(" x").append(qty).append(", ")
            );
            // strip trailing comma
            if (ing.length() > 0) ing.setLength(ing.length() - 2);

            tbl.add(new Label(prod.getName(), GameAssetManager.skin));
            tbl.add(new Label(ing.length() > 0 ? ing.toString() : "—", GameAssetManager.skin))
                .row();

            TextButton makeBtn = new TextButton("Make", GameAssetManager.skin, "custom-button");
            makeBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // این‌جا فراخوانی می‌کنید که محصول prod ساخته شود
                    //handleMakeRecipe(machine, prod);
                    pendingMachineName = machine.getName();
                    pendingProductName = prod.getName();
                    scenario = "Machine";
                    showBackpack();
                    dlg.hide();
                    Gdx.input.setInputProcessor(GameView.this);
                }
            });
            tbl.add(makeBtn).colspan(3).padTop(4).row();


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
                result = new Result(false, "Cancelled!");
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
            case "Grab Product":
                Result resultt = selectedMachine.grabPreparedProduct(currentPlayer);
                System.out.println(resultt.message());
                Gdx.input.setInputProcessor(this);
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
            int XP = MainApp.getInstance().getCurrentGame().getFriendship(currentPlayer.getUsername(), friend.getUsername()).getXp();
            System.out.println(level);
            Label levelLabel = new Label("Lvl: " + level, GameAssetManager.skin, "custom-label");
            Label XPLabel = new Label("XP: " + XP, GameAssetManager.skin, "custom-label");
            TextButton giftButton = new TextButton("Gift", GameAssetManager.skin, "custom-button");
            TextButton receivedButton = new TextButton("Received", GameAssetManager.skin, "custom-button");
            TextButton sentButton = new TextButton("Sent", GameAssetManager.skin, "custom-button");

            giftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    scenario = "Gift";
                    giftReciever = friend.getUsername();
                    friendsDialog.hide();
                    Gdx.input.setInputProcessor(GameView.this);
                    showBackpack();
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
                    for (User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()) {
                        if (otherPlayer.getUsername().equals(currentPlayer.getUsername())) {
                            continue;
                        }
                        Friendship friendship = MainApp.getInstance().getCurrentGame().getFriendship(currentPlayer.getUsername(), otherPlayer.getUsername());
                        List<Gift> allGits = friendship.getGifts();
                        for (Gift gift : allGits) {
                            if (gift.getSender().equals(currentPlayer.getUsername())) {
                                sentGifts.add(gift);
                            }
                        }
                    }
                    showSentGiftsDialog(sentGifts);
                }
            });

            row.add(nameLabel).left().pad(100);
            row.add(levelLabel).pad(100);
            row.add(XPLabel).pad(100);
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

    private void createRelationshipDialog(String targetUsername) {
        relationshipDialog = new Dialog("Interact with " + targetUsername, GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object obj) {
                String action = (String) obj;
                Result result;

                switch (action) {
                    case "hug":
                        result = controller.hug(targetUsername);
                        if (!result.isSuccessful()) showErrorDialog(stage, result.message());
                        else {
                            Tile tile1 = currentPlayer.getCurrentTile();
                            Tile tile2 = MainApp.getInstance().getCurrentGame().getPlayerByUsername(targetUsername).getCurrentTile();
                            float heartX = (float) (GameAssetManager.TILE_SIZE * (tile1.getX() + tile2.getX())) / 2;
                            float heartY = GameAssetManager.TILE_SIZE * (MainApp.getInstance().getCurrentGame().getMap().getHeight() - ((float) (tile2.getY() + tile1.getY()) / 2));
                            heartEffects.add(new HeartEffect(heartX, heartY));
                            Gdx.input.setInputProcessor(GameView.this);
                        }
                        break;
                    case "flower":
                        result = controller.sendFlower(targetUsername);
                        if (!result.isSuccessful()) showErrorDialog(stage, result.message());
                        else {
                            Tile tile1 = currentPlayer.getCurrentTile();
                            Tile tile2 = MainApp.getInstance().getCurrentGame().getPlayerByUsername(targetUsername).getCurrentTile();

                            float midX = GameAssetManager.TILE_SIZE * (tile1.getX() + tile2.getX()) / 2f;
                            float midY = GameAssetManager.TILE_SIZE * (MainApp.getInstance().getCurrentGame().getMap().getHeight() - ((tile1.getY() + tile2.getY()) / 2f));

                            Flower flower = new Flower(randomStuffType.Bouquet.getTexture(), midX, midY);
                            activeFlowers.add(flower);
                            Gdx.input.setInputProcessor(GameView.this);
                        }
                        break;
                    case "propose":
                        result = controller.askMarriage(targetUsername, "ring");
                        if (!result.isSuccessful()) showErrorDialog(stage, result.message());
                        else {
                            currentPlayer.setProposing(true);
                            Gdx.input.setInputProcessor(GameView.this);
                        }
                        break;
                    case "close":
                        relationshipDialog.hide();
                        Gdx.input.setInputProcessor(GameView.this);
                    default:
                        relationshipDialog.hide();
                        Gdx.input.setInputProcessor(GameView.this);
                }
            }
        };
        TextButton hugButton = new TextButton("Hug", GameAssetManager.skin, "custom-button");
        TextButton giftButton = new TextButton("Gift Flower", GameAssetManager.skin, "custom-button");
        TextButton proposeButton = new TextButton("Propose", GameAssetManager.skin, "custom-button");
        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");

        relationshipDialog.button(hugButton, "hug");
        relationshipDialog.button(giftButton, "flower");
        relationshipDialog.button(proposeButton, "propose");
        relationshipDialog.button(closeButton, "close");

    }

    private void showNPCFriendshipDialog(final NPC npc, final User player) {
        if (npcFriendshipDialog == null) {
            npcFriendshipDialog = new Dialog("Friendship with " + npc.getNpcName().getName(), GameAssetManager.skin, "custom-window");
            npcFriendshipDialog.padTop(40);
            npcFriendshipDialog.getTitleLabel().setAlignment(Align.center);
            npcFriendshipDialog.setKeepWithinStage(true);
            npcFriendshipDialog.setMovable(false);
            stage.addActor(npcFriendshipDialog);
        } else {
            npcFriendshipDialog.getTitleLabel().setText("Friendship with " + npc.getNpcName().getName());
            npcFriendshipDialog.getContentTable().clear();
            npcFriendshipDialog.getButtonTable().clear();
        }

        int currentLevel = npc.getFriendshipLevels().get(currentPlayer.getUsername());
        int currentPoints = (npc.getFriendshipPoints().get(currentPlayer.getUsername())) % 200;

        Table content = npcFriendshipDialog.getContentTable();
        content.defaults().pad(5).align(Align.left);

        Label levelLabel = new Label("Level: " + currentLevel, GameAssetManager.skin, "custom-label");
        levelLabel.setFontScale(0.7f);
        content.add(levelLabel).row();

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        TextureRegion pixelTextureRegion = new TextureRegion(GameAssetManager.pixel);

        Drawable progressBarBackground = new TextureRegionDrawable(pixelTextureRegion).tint(Color.DARK_GRAY);
        progressBarStyle.background = progressBarBackground;
        progressBarStyle.background.setMinHeight(20);

        progressBarStyle.knob = new TextureRegionDrawable(pixelTextureRegion);
        progressBarStyle.knob.setMinWidth(0);

        Drawable progressBarKnobBefore = new TextureRegionDrawable(pixelTextureRegion).tint(Color.YELLOW);
        progressBarStyle.knobBefore = progressBarKnobBefore;
        progressBarStyle.knobBefore.setMinHeight(20);

        ProgressBar xpBar = new ProgressBar(0, 200, 1, false, progressBarStyle);
        xpBar.setValue(currentPoints);

        Label xpTextLabel = new Label(currentPoints + "/" + 200, GameAssetManager.skin, "custom-label");
        xpTextLabel.setFontScale(0.5f);
        xpTextLabel.setColor(Color.LIGHT_GRAY);

        content.add().width(20);
        content.add(xpBar).width(150).height(20);
        content.add(xpTextLabel).width(70).row();

        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.getLabel().setFontScale(0.7f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcFriendshipDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });
        npcFriendshipDialog.getButtonTable().add(closeButton).pad(10);

        npcFriendshipDialog.pack();
        npcFriendshipDialog.setPosition(
            (Gdx.graphics.getWidth() - npcFriendshipDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - npcFriendshipDialog.getHeight()) / 2
        );

        npcFriendshipDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    private void showNPCQuestDialog(final NPC npc, final User player) {
        if (npcQuestDialog == null) {
            npcQuestDialog = new Dialog("Quests for " + npc.getNpcName().getName(), GameAssetManager.skin, "custom-window");
            npcQuestDialog.padTop(40);
            npcQuestDialog.getTitleLabel().setAlignment(Align.center);
            npcQuestDialog.setKeepWithinStage(true);
            npcQuestDialog.setMovable(false);
            stage.addActor(npcQuestDialog);
        } else {
            npcQuestDialog.getTitleLabel().setText("Quests for " + npc.getNpcName().getName());
            npcQuestDialog.getContentTable().clear();
            npcQuestDialog.getButtonTable().clear();
        }

        Table questsTable = new Table(GameAssetManager.skin);
        questsTable.defaults().pad(5).align(Align.left);

        Map<String, ArrayList<NPCMission>> unlockedMissions = npc.getUnlockedMissions();
        List<NPCMission> missionsForPlayer = unlockedMissions.get(player.getUsername());

        if (missionsForPlayer == null || missionsForPlayer.isEmpty()) {
            questsTable.add(new Label("No missions available from " + npc.getNpcName().getName() + ".", GameAssetManager.skin, "custom-label")).colspan(2).row();
        } else {
            int missionIndex = 1;
            for (NPCMission mission : missionsForPlayer) {
                Label missionLabel = new Label("Mission " + missionIndex + ":", GameAssetManager.skin, "custom-label");
                missionLabel.setColor(Color.CORAL);
                missionLabel.setFontScale(0.8f);
                questsTable.add(missionLabel).colspan(2).row();

                Label requiredLabel = new Label("  Required:", GameAssetManager.skin, "custom-label");
                requiredLabel.setFontScale(0.7f);
                requiredLabel.setColor(Color.ORANGE);
                questsTable.add(requiredLabel).row();
                Table requiredItemsTable = new Table(GameAssetManager.skin);
                requiredItemsTable.defaults().padLeft(10);
                if (mission.getRequiredItems().isEmpty()) {
                    Label itemLabel = new Label("None", GameAssetManager.skin, "custom-label");
                    itemLabel.setFontScale(0.5f);
                    requiredItemsTable.add(itemLabel);
                } else {
                    for (Map.Entry<String, Integer> entry : mission.getRequiredItems().entrySet()) {
                        Label itemLabel = new Label(entry.getKey() + ": " + entry.getValue(), GameAssetManager.skin, "custom-label");
                        itemLabel.setFontScale(0.5f);
                        requiredItemsTable.add(itemLabel).row();
                    }
                }
                questsTable.add(requiredItemsTable).colspan(2).row();

                Label prizeLabel = new Label("  Prizes:", GameAssetManager.skin, "custom-label");
                prizeLabel.setFontScale(0.7f);
                prizeLabel.setColor(Color.ORANGE);
                questsTable.add(prizeLabel).row();
                Table prizeItemsTable = new Table(GameAssetManager.skin);
                prizeItemsTable.defaults().padLeft(10);
                if (mission.getPrizeItems().isEmpty()) {
                    Label itemLabel = new Label("None", GameAssetManager.skin, "custom-label");
                    itemLabel.setFontScale(0.5f);
                    prizeItemsTable.add(itemLabel);
                } else {
                    for (Map.Entry<String, Integer> entry : mission.getPrizeItems().entrySet()) {
                        Label itemLabel = new Label(entry.getKey() + ": " + entry.getValue(), GameAssetManager.skin, "custom-label");
                        itemLabel.setFontScale(0.5f);
                        prizeItemsTable.add(itemLabel).row();
                    }
                }
                questsTable.add(prizeItemsTable).colspan(2).row();

                Label statusLabel = new Label(mission.getAlreadyDone() ? "Status: Done" : "Status: Not Done", GameAssetManager.skin, "custom-label");
                if (mission.getAlreadyDone()) {
                    statusLabel = new Label("Status: Done", GameAssetManager.skin, "custom-label");
                    statusLabel.setColor(Color.GREEN);
                } else {
                    boolean isAdded = MainApp.getInstance().getCurrentGame().getPlayerAddedMissions().get(currentPlayer.getUsername()).contains(mission);
                    if (isAdded) {
                        statusLabel = new Label("Status: Added", GameAssetManager.skin, "custom-label");
                        statusLabel.setColor(Color.BLUE);
                    } else {
                        statusLabel = new Label("Status: Not Added", GameAssetManager.skin, "custom-label");
                        statusLabel.setColor(Color.RED);
                    }
                }
                statusLabel.setFontScale(0.6f);

                TextButton doMissionButton = new TextButton("Add", GameAssetManager.skin, "custom-button");
                doMissionButton.getLabel().setFontScale(0.7f);
                doMissionButton.setDisabled(mission.getAlreadyDone());

                doMissionButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (MainApp.getInstance().getCurrentGame().getPlayerAddedMissions().get(currentPlayer.getUsername()).contains(mission)) {
                            showErrorDialog(stage, "Mission is already in your mission list!");
                        } else {
                            MainApp.getInstance().getCurrentGame().getPlayerAddedMissions().get(player.getUsername()).add(mission);
                            showErrorDialog(stage, "Mission added successfully!");
                        }
                        npcQuestDialog.hide();
                    }
                });

                questsTable.add(statusLabel).padRight(10);
                questsTable.add(doMissionButton).width(120).height(30).padBottom(10).row();
                questsTable.add().colspan(2).height(10).row();
                missionIndex++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(questsTable, GameAssetManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        npcQuestDialog.getContentTable().add(scrollPane).expand().fill().row();

        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.getLabel().setFontScale(0.7f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcQuestDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });
        npcQuestDialog.getButtonTable().add(closeButton).pad(10);

        npcQuestDialog.pack();
        npcQuestDialog.setSize(Math.min(npcQuestDialog.getPrefWidth(), Gdx.graphics.getWidth() * 0.7f),
            Math.min(npcQuestDialog.getPrefHeight(), Gdx.graphics.getHeight() * 0.8f));
        npcQuestDialog.setPosition(
            (Gdx.graphics.getWidth() - npcQuestDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - npcQuestDialog.getHeight()) / 2
        );

        npcQuestDialog.show(stage);
        Gdx.input.setInputProcessor(stage);
    }

    private void handleNPCMenuChoice(String choice) {
        if (selectedNPC == null) {
            System.err.println("Error: No NPC selected when handling menu choice: " + choice);
            return;
        }

        switch (choice) {
            case "gift":
                if (equippedItem == null) {
                    showErrorDialog(stage, "You haven't selected any gift yet.");
                }
                else{
                    Result npcResult = selectedNPC.giveGift(equippedItem.getName(),currentPlayer);
                    if (npcResult.isSuccessful()) {
                        currentPlayer.getBackpack().grabItem(equippedItem.getName(), 1);
                    }
                    showErrorDialog(stage, npcResult.message());
                }
                break;
            case "quests":
                showNPCQuestDialog(selectedNPC, currentPlayer);
                break;
            case "friendship":
                showNPCFriendshipDialog(selectedNPC, currentPlayer);
                break;
            case "cancel":
                break;
            default:
                break;
        }

        npcMenuDialog.hide();
        Gdx.input.setInputProcessor(GameView.this);
        selectedNPC = null;
    }

    private void createNPCDialog() {
        npcMenuDialog = new Dialog("NPC Interaction", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object object) {
                handleNPCMenuChoice(object.toString());
            }
        };
        npcMenuDialog.getTitleLabel().setAlignment(Align.center);
        npcMenuDialog.padTop(40);
        npcMenuDialog.getContentTable().defaults().pad(10);

        npcMenuDialog.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!npcMenuDialog.isVisible()) {
                    Gdx.input.setInputProcessor(GameView.this);
                }
            }
        });

        TextButton.TextButtonStyle smallerButtonStyle = new TextButton.TextButtonStyle(
            GameAssetManager.skin.get("custom-button", TextButton.TextButtonStyle.class)
        );
        smallerButtonStyle.font = smallerButtonFont;


        TextButton giftButton = new TextButton("Gift", smallerButtonStyle);
        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcMenuDialog.hide();
                handleNPCMenuChoice("gift");
            }
        });

        TextButton questsButton = new TextButton("Quests", smallerButtonStyle);
        questsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcMenuDialog.hide();
                handleNPCMenuChoice("quests");
            }
        });

        TextButton friendshipButton = new TextButton("Friendship", smallerButtonStyle);
        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcMenuDialog.hide();
                handleNPCMenuChoice("friendship");
            }
        });

        TextButton cancelButton = new TextButton("Cancel", smallerButtonStyle);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                npcMenuDialog.hide();
                handleNPCMenuChoice("cancel");
            }
        });

        npcMenuDialog.getContentTable().add(giftButton).row();
        npcMenuDialog.getContentTable().add(questsButton).row();
        npcMenuDialog.getContentTable().add(friendshipButton).row();
        npcMenuDialog.getContentTable().add(cancelButton);

        npcMenuDialog.setKeepWithinStage(true);
        npcMenuDialog.setMovable(false);
        npcMenuDialog.setVisible(false);
        stage.addActor(npcMenuDialog);

        npcMenuDialog.pack();
        npcMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - npcMenuDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - npcMenuDialog.getHeight()) / 2
        );
    }

    private void createNPCSpeechBubbleDialog() {
        npcSpeechBubbleDialog = new Dialog("", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object object) {
                this.hide();
            }
        };
        npcSpeechBubbleDialog.getTitleLabel().remove();
        npcSpeechBubbleDialog.pad(10);
        npcSpeechBubbleDialog.setKeepWithinStage(true);
        npcSpeechBubbleDialog.setMovable(false);
        npcSpeechBubbleDialog.setVisible(false);
        npcSpeechBubbleDialog.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!npcSpeechBubbleDialog.isVisible()) {
                    Gdx.input.setInputProcessor(GameView.this);
                }
            }
        });
        TextButton okButton = new TextButton("OK", GameAssetManager.skin, "custom-button");
        npcSpeechBubbleDialog.button(okButton, true);
        stage.addActor(npcSpeechBubbleDialog);
    }

    private void showNPCSpeechBubble(NPC npc, String message) {
        if (npcSpeechBubbleDialog == null) {
            createNPCSpeechBubbleDialog();
        }

        npcSpeechBubbleDialog.getContentTable().clear();
        Label messageLabel = new Label(message, GameAssetManager.skin, "custom-label");
        messageLabel.setWrap(true);
        messageLabel.setAlignment(Align.center);
        npcSpeechBubbleDialog.getContentTable().add(messageLabel).width(200).pad(5).row();

        npcSpeechBubbleDialog.pack();

        // Position the speech bubble above the NPC's head
        Tile npcTile = npc.currentTileGetter();
        int tileSize = GameAssetManager.TILE_SIZE;
        int rows = MainApp.getInstance().getCurrentGame().getMap().getMap().length;

        float npcCenterX = npcTile.getX() * tileSize + tileSize / 2f;
        float npcTopY = (rows - npcTile.getY() - 1) * tileSize + tileSize;

        Vector3 worldPos = new Vector3(npcCenterX, npcTopY + tileSize / 2f, 0);
        camera.project(worldPos);
        stage.getViewport().unproject(worldPos);

        npcSpeechBubbleDialog.setPosition(
            worldPos.x - npcSpeechBubbleDialog.getWidth() / 2,
            worldPos.y
        );

        npcSpeechBubbleDialog.setVisible(true);
        npcSpeechBubbleDialog.show(stage);
        Gdx.input.setInputProcessor(GameView.this);
    }


    private void handleAnimalMenuChoice(String choice) {
        if (selectedAnimal == null) {
            System.out.println("animal is null");
            return;
        }
        Result result;

        if (choice.startsWith("shepherd:")) {
            String[] coords = choice.split(":")[1].split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            result = controller.shepherdAnimal(selectedAnimal.getName(), Integer.toString(x), Integer.toString(y)); // implement this
        } else {
            switch (choice) {
                case "feed": result = controller.feedHay(selectedAnimal.getName());
                    if (!result.isSuccessful()) showErrorDialog(stage, result.message());
                    else {
                        Tile tile1 = currentPlayer.getCurrentTile();
                        Tile tile2 = selectedAnimal.getCurrentTile();

                        float midX = GameAssetManager.TILE_SIZE * (tile1.getX() + tile2.getX()) / 2f;
                        float midY = GameAssetManager.TILE_SIZE * (MainApp.getInstance().getCurrentGame().getMap().getHeight() - ((tile1.getY() + tile2.getY()) / 2f));

                        Hay hay = new Hay(randomStuffType.Hay.getTexture(), midX, midY);
                        activeHays.add(hay);
                        Gdx.input.setInputProcessor(GameView.this);
                    }
                    break;
                case "pet": result =
                    controller.petAnimal(selectedAnimal.getName());
                    if (!result.isSuccessful()) showErrorDialog(stage, result.message());
                    else {
                        Tile tile = selectedAnimal.getCurrentTile();
                        Tile[][] tiles = MainApp.getInstance().getCurrentGame().getMap().getMap();
                        int tileSize = GameAssetManager.TILE_SIZE;
                        int rows = tiles.length;
                        float heartX = tile.getX() * tileSize + (tileSize / 2);
                        float heartY = (rows - tile.getY() - 1) * tileSize + (tileSize);
                        heartEffects.add(new HeartEffect(heartX, heartY));
                        Gdx.input.setInputProcessor(GameView.this);
                    }
                    break;
                case "sell": result = controller.sellAnimal(selectedAnimal.getName()); break;
                case "collect": result = controller.collectProduct(selectedAnimal.getName()); break;
                case "release": result = controller.releaseAnimal(selectedAnimal.getName()); break;
                case "cancel": result = new Result(true, ""); break;
                default: result = new Result(false, choice); break;
            }
        }
        if(!choice.equalsIgnoreCase("feed") && !choice.equalsIgnoreCase("pet")) {
            showErrorDialog(stage, result.message());
        }
        animalMenuDialog.hide();
        selectedAnimal = null;
    }

    private void showFridgeMenu(House house) {
        float dialogWidth = Gdx.graphics.getWidth() * 0.4f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.6f;
        this.currentHouseForFridge = house;
        showFridgeMenu = true;
        selectedFridgeSlot = 0;
        updateFridgeMenuTable();
        fridgeMenuDialog.setVisible(true);
        fridgeMenuDialog.show(stage);
        fridgeMenuDialog.setSize(dialogWidth, dialogHeight);
        fridgeMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - fridgeMenuDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - fridgeMenuDialog.getHeight()) / 2
        );
        Gdx.input.setInputProcessor(GameView.this);
    }

    private boolean isAnyDialogOpen() {
        System.out.println(shopMenuDialog != null);
        System.out.println(shopMenuDialog.isVisible());
        System.out.println(shopPurchaseDialog != null);
        System.out.println(shopPurchaseDialog.isVisible());
        System.out.println("/////////////////////////////");

        return
            //(skillsDialog != null && skillsDialog.isVisible()) ||
            (shopMenuDialog != null && shopMenuDialog.isVisible()) ||
                // (buyAnimalDialog != null && buyAnimalDialog.isVisible()) ||
                (shopPurchaseDialog != null && shopPurchaseDialog.isVisible()) ;
        //(machineMenuDialog != null && machineMenuDialog.isVisible()) ||
        // (animalMenuDialog != null &&  animalMenuDialog.isVisible()) ||
        // (friendsDialog != null && friendsDialog.isVisible());
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
        friendsButton.setSize(100, 100);
        friendsButton.setColor(Color.PURPLE);
        friendsButton.setPosition(Gdx.graphics.getWidth() - 100, 10);
        friendsButton.setTouchable(Touchable.enabled);
        stage.addActor(friendsButton);

//        nextTurnButton = new TextButton("Next Turn", GameAssetManager.skin, "custom-button");
//        nextTurnButton.setSize(200, 100);
//        nextTurnButton.setColor(Color.MAGENTA);
//        nextTurnButton.setPosition(Gdx.graphics.getWidth() - 300, 10);
//        nextTurnButton.setTouchable(Touchable.enabled);
//        nextTurnButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (Gdx.input.getInputProcessor() != GameView.this) {
//                    // showErrorDialog(stage, "Cannot end turn while another menu is open.");
//                    showTimedErrorLabel( stage, "Cannot end turn while another menu is open.", 2f) ;
//                    // return;
//                }
////                if (isAnyDialogOpen()) {
////                    showTimedErrorLabel(stage, "Cannot end turn while another menu is open.", 2f);
////                    return;
////                }
//
////                Result result = controller.nextTurn();
////                if (!result.isSuccessful()) {
////                    showErrorDialog(stage, result.message());
////                }
//            }
//        });
//        stage.addActor(nextTurnButton);


        exitGameButton = new TextButton("Exit", GameAssetManager.skin, "custom-button");
        exitGameButton.setSize(100, 100);
        exitGameButton.setColor(Color.MAGENTA);
        exitGameButton.setPosition(10, Gdx.graphics.getHeight() - 100);
        exitGameButton.setTouchable(Touchable.enabled);
        stage.addActor(exitGameButton);

        forceTerminateButton = new TextButton("Force Terminate", GameAssetManager.skin, "custom-button");
        forceTerminateButton.setSize(200, 100);
        forceTerminateButton.setColor(Color.PINK);
        forceTerminateButton.setPosition(110, Gdx.graphics.getHeight() - 100);
        forceTerminateButton.setTouchable(Touchable.enabled);
        stage.addActor(forceTerminateButton);

        energyLabel = new Label("Energy", GameAssetManager.skin, "custom-label");
        energyLabel.setPosition(Gdx.graphics.getWidth() - 200, 120);
        stage.addActor(energyLabel);

        this.toolMenuTable = new Table();
        toolMenuTable.bottom().center();
        toolMenuTable.padBottom(GameAssetManager.TILE_SIZE * 0.75f);
        toolMenuTable.setVisible(showToolsMenu);
        stage.addActor(toolMenuTable);

        equippedItemSlotTable = new Table();
        equippedItemSlotTable.bottom().center();
        equippedItemSlotTable.padBottom(10);
        equippedItemSlotTable.setVisible(true);
        stage.addActor(equippedItemSlotTable);

        updateEquippedItemSlot();


//        gameTickTask = Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                MainApp.getInstance().getCurrentGame().getTimeAndDate().advanceHour();
//                controller.handleEndOfDay();
//               // updateLighting(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
//            }
//        }, 5, 5);

        determineAvatar();

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
        TextButton settingBtn = new TextButton("Setting", GameAssetManager.skin, "custom-button");
        settingBtn.setColor(Color.ORANGE);

        inventoryBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBackpack();
            }
        });
        skillsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleSkillsDialog();
            }
        });
        socialBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSocialMenu();
            }
        });
        missionsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMissionsMenu();
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
        settingBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSettingsMenu();
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
        inventoryMenuTable.add(settingBtn).width(buttonWidth).height(buttonHeight).pad(buttonPad).row();

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

        fishingMinigameDialog = new FishingMinigameDialog();
        fishingMinigameDialog.setPosition(
            (Gdx.graphics.getWidth() - fishingMinigameDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - fishingMinigameDialog.getHeight()) / 2
        );
        fishingMinigameDialog.setMinigameCallback(this);
        stage.addActor(fishingMinigameDialog);

        ImageButton.ImageButtonStyle fridgeButtonStyle = new ImageButton.ImageButtonStyle();
        fridgeButtonStyle.imageUp = new TextureRegionDrawable(GameAssetManager.fridgeTexture);
        fridgeButtonStyle.imageDown = new TextureRegionDrawable(GameAssetManager.fridgeTexture);

        fridgeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(1, 1, Pixmap.Format.RGBA8888)));
        fridgeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(1, 1, Pixmap.Format.RGBA8888)));
        fridgeButtonStyle.over = new TextureRegionDrawable(new TextureRegion(new Texture(1, 1, Pixmap.Format.RGBA8888)));


        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            Farm playerFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(player);
            if (playerFarm != null && playerFarm.getHouse() != null) {
                ImageButton fridgeButton = new ImageButton(fridgeButtonStyle);
                fridgeButton.setSize(GameAssetManager.TILE_SIZE, GameAssetManager.TILE_SIZE);
                fridgeButton.setTouchable(Touchable.enabled);
                fridgeButton.setVisible(false);

                fridgeButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showFridgeMenu(playerFarm.getHouse());
                        event.stop();
                    }
                });
                stage.addActor(fridgeButton);
                playerFridgeButtons.put(player, fridgeButton);
            }
        }

        MainApp.getInstance().setCurrentGameView(this);

        leaderboardTable = new Table(GameAssetManager.skin);
        leaderboardTable.left().pad(10);
        leaderboardTable.setFillParent(true);

        // ستون‌های ثابت
        leaderboardTable.add("Rank").pad(5);
        leaderboardTable.add("Player").pad(5);
        leaderboardTable.add("Money").pad(5);
        leaderboardTable.add("Skills").pad(5);
        leaderboardTable.add("Missions").pad(5);
        leaderboardTable.add("Score").pad(5);
        leaderboardTable.row();

        stage.addActor(leaderboardTable);

        // 2. پس‌فرستادن درخواست به سرور برای دریافت اولیه‌ی لیست
        MainApp.getInstance().getNetworkClient()
            .sendPost(
                MainApp.getInstance().getCurrentGame().getNetworkId(),
                "ServerController",         // controller name
                "getLeaderboard",         // method name
                Collections.emptyMap(),// no params
                MainApp.getInstance().getCurrentGameView().currentPlayer.getUsername()
            )
            .thenAccept(response -> {
                if (response.getStatus() == 200) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> body = (Map<String,Object>) response.getBody();
                    @SuppressWarnings("unchecked")
                    List<Map<String,Object>> lb = (List<Map<String,Object>>) body.get("leaderboard");
                    Gdx.app.postRunnable(() -> updateLeaderboard(lb));
                } else {
                    Gdx.app.postRunnable(() ->
                        showErrorDialog(stage, "Failed to load leaderboard: " + response.getMessage())
                    );
                }
            });
        //MainApp.getInstance().setCurrentGameViewIfNull(this);
        //MainApp.getInstance().setCurrentGameView(this);

    }




    private void determineAvatar() {
        switch (MainApp.getInstance().getCurrentGame().getCurrentPlayer().getAvatar()) {
            case Abigail -> {
                playerAnimations = GameAssetManager.abigailAnimations;
                faintTexture = GameAssetManager.abigialFaint;
                proposingTexture = null;
                acceptingTexture = GameAssetManager.abigailAccepting;
                rejectingTexture = GameAssetManager.abigailRejecting;
            }
            case Alex -> {
                playerAnimations = GameAssetManager.alexAnimations;
                faintTexture = GameAssetManager.alexFaint;
                proposingTexture = GameAssetManager.alexProposing;
                acceptingTexture = null;
                rejectingTexture = null;
            }
            case Shane -> {
                playerAnimations = GameAssetManager.shaneAnimations;
                faintTexture = GameAssetManager.shaneFaint;
                proposingTexture = GameAssetManager.shaneProposing;
                acceptingTexture = null;
                rejectingTexture = null;
            }
            case Haley -> {
                playerAnimations = GameAssetManager.haleyAnimations;
                faintTexture = GameAssetManager.haleyFaint;
                proposingTexture = null;
                acceptingTexture = GameAssetManager.haleyAccepting;
                rejectingTexture = GameAssetManager.haleyRejecting;
            }
        }
    }


    @Override
    public void render(float v) {
        //////////////////////hard code /////////////////////////
//        currentPlayer =  MainApp.getInstance().getLoggedInUser();
//        MainApp.getInstance().getCurrentGame().setCurrentPlayer(MainApp.getInstance().getLoggedInUser());
        //////////////////////hard code /////////////////////////
        updateLighting(MainApp.getInstance().getCurrentGame().getTimeAndDate().getHour());
        currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        currentFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer);
//        if (currentPlayer.hasFainted()) {
//            if (!hasShownFaintMessage) {
//                showTimedErrorLabel(stage, "You don't have enough energy! Go to next turn!", 5f);
//                hasShownFaintMessage = true;
//            }
//        } else {
//            hasShownFaintMessage = false; // Reset if player regains energy
//        }

            if (currentPlayer.hasFainted()) {
                if (!hasShownFaintMessage) {
                    showTimedErrorLabel(stage, "You don't have enough energy!", 5f, () -> {
                        //controller.nextTurn();
                    });
                    hasShownFaintMessage = true;
                }
            } else {
                hasShownFaintMessage = false;
            }



        determineAvatar();
        showNotifications();
        energyLabel.setText("Energy: " + currentPlayer.getEnergy());
        if (currentPlayer.isProposing()) {
            currentPlayer.setProposingTimer(currentPlayer.getProposingTimer() + v);
            if (currentPlayer.getProposingTimer() > 1f) {
                currentPlayer.setProposingTimer(0);
                currentPlayer.setProposing(false);
            }
        }
        if (currentPlayer.isRejecting()) {
            currentPlayer.setRejectingTimer(currentPlayer.getRejectingTimer() + v);
            if (currentPlayer.getRejectingTimer() > 1f) {
                currentPlayer.setRejectingTimer(0);
                currentPlayer.setRejecting(false);
            }
        }
        if (currentPlayer.isAccepting()) {
            currentPlayer.setAcceptingTimer(currentPlayer.getAcceptingTimer() + v);
            if (currentPlayer.getAcceptingTimer() > 1f) {
                currentPlayer.setAcceptingTimer(0);
                currentPlayer.setAccepting(false);
            }
        }
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
        drawTiles(rows, tiles, tileSize);
        drawGreenHouse(tileSize, rows);
        drawHabitats(tileSize, rows);
        drawShops(tileSize, rows);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                if (tiles[y][x].getIsPlowed()) {
                    batch.draw(GameAssetManager.FLOORING_21,
                        x * tileSize,
                        (rows - y - 1) * tileSize,
                        tileSize, tileSize);
                }
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

        updateNPCMovement(v);
        drawNPCs(rows, tileSize);

        drawPlayer();
        drawAllPlayers();
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


        // Draw all active flowers
        Iterator<Flower> iter = activeFlowers.iterator();
        while (iter.hasNext()) {
            Flower flower = iter.next();
            flower.update(v);
            flower.draw(batch);
            if (flower.isFinished()) {
                iter.remove();
            }
        }

        //Draw all active Hays
        Iterator<Hay> itere = activeHays.iterator();
        while (itere.hasNext()) {
            Hay hay = itere.next();
            hay.update(Gdx.graphics.getDeltaTime());
            hay.draw(batch);
            if (hay.isFinished()) {
                itere.remove();
            }
        }
        updateToolsMenuTable();
        if (isToolBeingUsed) {
            toolUsageStateTime += Gdx.graphics.getDeltaTime();
            TextureRegion currentFrame = InventoryAssets.toolUsageAnimation.getKeyFrame(toolUsageStateTime);
            if (currentFrame != null) {
                // Draw the animation centered on the player's tile
                int drawX = currentPlayer.getCurrentTile().getX() * tileSize;
                int drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - currentPlayer.getCurrentTile().getY() - 1) * tileSize;
                batch.draw(currentFrame, drawX, drawY, tileSize, tileSize);
            }
            if (InventoryAssets.toolUsageAnimation.isAnimationFinished(toolUsageStateTime)) {
                isToolBeingUsed = false;
                toolUsageStateTime = 0f; // Reset for next usage
            }
        }

        if (isFishingActive) {
            fishingMinigameDialog.act(v);
        }


        updateEquippedItemSlot();
        if (equippedItem != null && currentPlayer != null && currentPlayer.getCurrentTile() != null) {
            int drawX = currentPlayer.getCurrentTile().getX() * tileSize;
            int drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - currentPlayer.getCurrentTile().getY() - 1) * tileSize;
            batch.draw(getItemTexture(equippedItem), drawX, drawY, tileSize, tileSize);
        }


        float camX = camera.position.x - camera.viewportWidth / 2f;
        float camY = camera.position.y - camera.viewportHeight / 2f;

        currentWeather = MainApp.getInstance().getCurrentGame().getCurrentWeatherType();
        if (currentWeather == WeatherType.STORM) {
            batch.setColor(1f, 1f, 1f, 0.5f);
            batch.draw(GameAssetManager.stormOverlay, camX, camY, camera.viewportWidth, camera.viewportHeight);
            batch.setColor(Color.WHITE);
        } else if (currentWeather == WeatherType.SNOW) {
            batch.draw(GameAssetManager.snowOverlay, camX, camY, camera.viewportWidth, camera.viewportHeight);
        } else if (currentWeather == WeatherType.RAIN) {
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
        batch.draw(GameAssetManager.pixel, camX, camY, camera.viewportWidth * 50, camera.viewportHeight * 50);
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

//        if (!showFullMap && !terminalVisible && !currentPlayer.hasFainted() && !isFishingActive) {
//            moveCooldown -= v;
//            if (moveCooldown <= 0f) {
//                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//                    if (tryMove(0, -1, 3)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                    if (tryMove(0, +1, 1)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                    if (tryMove(-1, 0, 0)) moveCooldown = MOVE_INTERVAL;
//                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                    if (tryMove(+1, 0, 2)) moveCooldown = MOVE_INTERVAL;
//                }
//            }
//        }
//        if (!showFullMap && !terminalVisible && !currentPlayer.hasFainted() && !isFishingActive) {
//            moveCooldown -= v;
//            if (moveCooldown <= 0f) {
//                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("dx","0");
//                    params.put("dy","-1");
//                    params.put("direction","3");
//                    MainApp.getInstance().getNetworkClient().sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
//                        "GameController", "tryMove", params, currentPlayer.getUsername()).thenAccept(response -> {
//                        if (response.getStatus() == 200) {
//                            moveCooldown = MOVE_INTERVAL;
//                            Object bodyRaw = response.getBody();
//
//                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                                Object tileRaw = bodyMap.get("tile");
//                                Object energyRaw = bodyMap.get("energy");
//                                Object dirRaw = bodyMap.get("movingDirection");
//
//                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
//                                int energy = ((Number) energyRaw).intValue();
//                                int direction = ((Number) dirRaw).intValue();
//
//                                User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
//                                currentPlayer.setCurrentTile(tile);
//                                currentPlayer.setEnergy(energy);
//                                currentPlayer.setMovingDirection(direction);
//
//                            } else {
//                                System.err.println("Response body is not a map");
//                            }
//                        }
//                    }).exceptionally(ex -> {
//                        Gdx.app.postRunnable(() -> {
//                            showErrorDialog(stage, "Failed to walk: " + ex.getMessage());
//                        });
//                        return null;
//                    });
//                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("dx","0");
//                    params.put("dy","1");
//                    params.put("direction","1");
//                    MainApp.getInstance().getNetworkClient().sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
//                        "GameController", "tryMove", params, currentPlayer.getUsername()).thenAccept(response -> {
//                        if (response.getStatus() == 200) {
//                            moveCooldown = MOVE_INTERVAL;
//                            Object bodyRaw = response.getBody();
//
//                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                                Object tileRaw = bodyMap.get("tile");
//                                Object energyRaw = bodyMap.get("energy");
//                                Object dirRaw = bodyMap.get("movingDirection");
//
//                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
//                                int energy = ((Number) energyRaw).intValue();
//                                int direction = ((Number) dirRaw).intValue();
//
//                                User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
//                                currentPlayer.setCurrentTile(tile);
//                                currentPlayer.setEnergy(energy);
//                                currentPlayer.setMovingDirection(direction);
//
//                            } else {
//                                System.err.println("Response body is not a map");
//                            }
//                        }
//                    }).exceptionally(ex -> {
//                        Gdx.app.postRunnable(() -> {
//                            showErrorDialog(stage, "Failed to walk: " + ex.getMessage());
//                        });
//                        return null;
//                    });
//                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("dx","-1");
//                    params.put("dy","0");
//                    params.put("direction","0");
//                    MainApp.getInstance().getNetworkClient().sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
//                        "GameController", "tryMove", params, currentPlayer.getUsername()).thenAccept(response -> {
//                        if (response.getStatus() == 200) {
//                            moveCooldown = MOVE_INTERVAL;
//                            Object bodyRaw = response.getBody();
//
//                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                                Object tileRaw = bodyMap.get("tile");
//                                Object energyRaw = bodyMap.get("energy");
//                                Object dirRaw = bodyMap.get("movingDirection");
//
//                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
//                                int energy = ((Number) energyRaw).intValue();
//                                int direction = ((Number) dirRaw).intValue();
//
//                                User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
//                                currentPlayer.setCurrentTile(tile);
//                                currentPlayer.setEnergy(energy);
//                                currentPlayer.setMovingDirection(direction);
//
//                            } else {
//                                System.err.println("Response body is not a map");
//                            }
//                        }
//                    }).exceptionally(ex -> {
//                        Gdx.app.postRunnable(() -> {
//                            showErrorDialog(stage, "Failed to walk: " + ex.getMessage());
//                        });
//                        return null;
//                    });
//                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("dx","1");
//                    params.put("dy","0");
//                    params.put("direction","2");
//                    MainApp.getInstance().getNetworkClient().sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
//                        "GameController", "tryMove", params, currentPlayer.getUsername()).thenAccept(response -> {
//                        if (response.getStatus() == 200) {
//                            moveCooldown = MOVE_INTERVAL;
//                            Object bodyRaw = response.getBody();
//
//                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                                Object tileRaw = bodyMap.get("tile");
//                                Object energyRaw = bodyMap.get("energy");
//                                Object dirRaw = bodyMap.get("movingDirection");
//
//                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
//                                int energy = ((Number) energyRaw).intValue();
//                                int direction = ((Number) dirRaw).intValue();
//
//                                User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
//                                currentPlayer.setCurrentTile(tile);
//                                currentPlayer.setEnergy(energy);
//                                currentPlayer.setMovingDirection(direction);
//
//                            } else {
//                                System.err.println("Response body is not a map");
//                            }
//                        }
//                    }).exceptionally(ex -> {
//                        Gdx.app.postRunnable(() -> {
//                            showErrorDialog(stage, "Failed to walk: " + ex.getMessage());
//                        });
//                        return null;
//                    });
//                }
//            }
//        }
        if (!showFullMap && !terminalVisible && !currentPlayer.hasFainted() && !isFishingActive) {
            moveCooldown -= v;
            if (moveCooldown <= 0f) {
                int dx = 0, dy = 0, direction = -1;

                if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                    dx = 0; dy = -1; direction = 3;
                } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                    dx = 0; dy = 1; direction = 1;
                } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    dx = -1; dy = 0; direction = 0;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    dx = 1; dy = 0; direction = 2;
                }

                if (direction != -1) {
                    // ✅ Optimistically move the player locally
//                    Tile currentTile = currentPlayer.getCurrentTile();
//                    Tile nextTile = currentTile.offset(dx, dy); // You must implement this logic
//                    if (nextTile != null && !nextTile.isBlocked()) {
//                        currentPlayer.setCurrentTile(nextTile);
//                        currentPlayer.setMovingDirection(direction);
//                        moveCooldown = MOVE_INTERVAL;
//                    }
                    if (tryMove(dx, dy, direction)) {
                        moveCooldown = MOVE_INTERVAL;
                    }

                    // ✅ Send move request to server (still needed for real game state)
                    Map<String, Object> params = new HashMap<>();
                    params.put("dx", String.valueOf(dx));
                    params.put("dy", String.valueOf(dy));
                    params.put("direction", String.valueOf(direction));

                    MainApp.getInstance().getNetworkClient()
                        .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                            "GameController", "tryMove", params, currentPlayer.getUsername())
                        .thenAccept(response -> {
                            if (response.getStatus() == 200) {
                                Object bodyRaw = response.getBody();
                                if (bodyRaw instanceof Map<?, ?> bodyMap) {
//                                    Tile serverTile = GameSaver.convertObject(bodyMap.get("tile"), Tile.class);
//                                    int energy = ((Number) bodyMap.get("energy")).intValue();
//                                    int dir = ((Number) bodyMap.get("movingDirection")).intValue();
//
//                                    // ✅ Reconcile — if client and server differ, correct it
//                                    if (!currentPlayer.getCurrentTile().equals(serverTile)) {
//                                        currentPlayer.setCurrentTile(serverTile);
//                                    }
//                                    currentPlayer.setEnergy(energy);
//                                    currentPlayer.setMovingDirection(dir);
                                    Object tileRaw = bodyMap.get("tile");
                                    Object energyRaw = bodyMap.get("energy");
                                    Object dirRaw = bodyMap.get("movingDirection");
                                    Object faintedRaw = bodyMap.get("hasFainted");

                                    Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
                                    int energy = ((Number) energyRaw).intValue();
                                    int dir = ((Number) dirRaw).intValue();
                                    boolean fainted = ((Boolean) faintedRaw).booleanValue();

                                    User currentPlayer = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
                                    currentPlayer.setCurrentTile(tile);
                                    currentPlayer.setEnergy(energy);
                                    currentPlayer.setFainted(fainted);
                                    currentPlayer.setMovingDirection(dir);
                                } else {
                                  System.err.println("Response body is not a map");
                                }
                            }
                        }).exceptionally(ex -> {
                            Gdx.app.postRunnable(() -> {
                                showErrorDialog(stage, "Failed to walk: " + ex.getMessage());
                            });
                            return null;
                        });
                }
            }
        }

        setCameraPosition();
        camera.update();

        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            for (Tile[] tileRow : MainApp.getInstance().getCurrentGame().getMap().getMap()) {
                for (Tile tile : tileRow) {
                    if (tile.getContainedNPC() != null) {
                        NPC npc = tile.getContainedNPC();
                        TextButton talkButton = npcTalkButtons.get(npc);

                        if (talkButton == null) {
                            talkButton = new TextButton("...", GameAssetManager.skin, "custom-button");
                            talkButton.setSize(tileSize / 2f, tileSize / 2f);
                            talkButton.getLabel().setFontScale(0.5f);
                            talkButton.setColor(Color.WHITE);
                            talkButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    showNPCSpeechBubble(npc, npc.talkToNPC(MainApp.getInstance().getCurrentGame().getCurrentWeatherType(),currentPlayer).message());
                                    event.stop();
                                }
                            });
                            stage.addActor(talkButton);
                            npcTalkButtons.put(npc, talkButton);
                        }

                        // Calculate screen position for the button above the NPC
                        float npcCenterX = tile.getX() * tileSize + tileSize / 2f;
                        float npcTopY = (rows - tile.getY() - 1) * tileSize + tileSize * 2f;

                        Vector3 buttonWorldCoords = new Vector3(npcCenterX, npcTopY, 0);
                        camera.project(buttonWorldCoords);

                        talkButton.setPosition(buttonWorldCoords.x - talkButton.getWidth() / 2f, buttonWorldCoords.y);
                        talkButton.setVisible(true);
                    }
                }
            }
        }

        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            for (Tile[] tileRow : MainApp.getInstance().getCurrentGame().getMap().getMap()) {
                for (Tile tile : tileRow) {
                    if (tile.getContainedNPC() != null) {
                        NPC npc = tile.getContainedNPC();
                        TextButton talkButton = npcTalkButtons.get(npc);

                        if (talkButton == null) {
                            talkButton = new TextButton("...", GameAssetManager.skin, "custom-button");
                            talkButton.setSize(tileSize / 2f, tileSize / 2f);
                            talkButton.getLabel().setFontScale(0.5f);
                            talkButton.setColor(Color.WHITE);
                            talkButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    showNPCSpeechBubble(npc, npc.talkToNPC(MainApp.getInstance().getCurrentGame().getCurrentWeatherType(),currentPlayer).message());
                                    event.stop();
                                }
                            });
                            stage.addActor(talkButton);
                            npcTalkButtons.put(npc, talkButton);
                        }

                        float talkButtonSize = tileSize / 2f;
                        if (showFullMap) {
                            talkButtonSize *= 0.5f;
                        }
                        talkButton.setSize(talkButtonSize, talkButtonSize);

                        float npcCenterX = tile.getX() * tileSize + tileSize / 2f;
                        float npcTopY = (rows - tile.getY() - 1) * tileSize + tileSize * 2f;

                        Vector3 buttonWorldCoords = new Vector3(npcCenterX, npcTopY, 0);
                        camera.project(buttonWorldCoords);

                        talkButton.setPosition(buttonWorldCoords.x - talkButton.getWidth() / 2f, buttonWorldCoords.y);
                        talkButton.setVisible(true);
                    }
                }
            }
        }

        for (Map.Entry<User, ImageButton> entry : playerFridgeButtons.entrySet()) {
            User player = entry.getKey();
            ImageButton fridgeButton = entry.getValue();

            Farm playerFarm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(player);
            if (playerFarm != null && playerFarm.getHouse() != null) {
                House house = playerFarm.getHouse();
                int houseTileX = house.getX();
                int houseTileY = house.getY();

                float buttonSize = GameAssetManager.TILE_SIZE;
                if (showFullMap) {
                    buttonSize *= 0.05f;
                }

                fridgeButton.setSize(buttonSize, buttonSize);

                float fridgeWorldX = houseTileX * tileSize + (tileSize / 4f);
                float fridgeWorldY = (rows - houseTileY - 1) * tileSize + (tileSize / 4f);

                Vector3 buttonWorldCoords = new Vector3(fridgeWorldX, fridgeWorldY, 0);
                camera.project(buttonWorldCoords);

                fridgeButton.setPosition(buttonWorldCoords.x, buttonWorldCoords.y);
                if (showBackpackMenu || showFridgeMenu || showInventoryMenu || terminalVisible ||
                    showCookingMenu) fridgeButton.setVisible(false);
                else fridgeButton.setVisible(true);
            } else {
                fridgeButton.setVisible(false);
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

    @Override
    public void onMinigameEnd(boolean caughtSuccessfully, boolean perfectCatch) {
        // This method is called by FishingMinigameDialog when it finishes
        isFishingActive = false;
        Gdx.input.setInputProcessor(this);

        if (caughtSuccessfully) {
            // Determine final quality based on player's skill, pole, and perfect catch
            int fishingLevel = currentPlayer.getSkillsLevel().get(Skill.FISHING);
            FishingPole currentPole = (FishingPole) currentPlayer.getEquippedTool();
            double rodQualityFactor = currentPole.getPoleMaterial().getFishQuality();

            double baseQualityValue = (Math.random() * (fishingLevel + 2) * rodQualityFactor) / 7.0;

            ProductQuality finalQuality = ProductQuality.getQualityByValue(baseQualityValue);

            if (perfectCatch) {
                // SILVER -> GOLD -> IRIDIUM
                if (finalQuality == ProductQuality.Silver) {
                    finalQuality = ProductQuality.Golden;
                } else if (finalQuality == ProductQuality.Golden) {
                    finalQuality = ProductQuality.Iridium;
                }
               currentPlayer.perfectFishingSkillUpgrade();
            }

            Fish finalFish = new Fish(finalQuality, currentCaughtFish.getType());

            Result addFishResult = currentPlayer.getBackpack().addItem(finalFish, 1);
            if (addFishResult.isSuccessful()) {
                if (perfectCatch) showErrorDialog(stage,"Perfect catch!");
                showErrorDialog(stage, "You caught a " + finalQuality.name() + " " + finalFish.getName() + "!");
                currentPlayer.addSkillExperience(Skill.FISHING);
            } else {
                showErrorDialog(stage, "You caught the fish, but your backpack is full!");
            }
        } else {
            showErrorDialog(stage, "The " + currentCaughtFish.getName() + " got away!");
        }
        currentCaughtFish = null;
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
            batch.draw(machine.getType().getTexture(), x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);

            float drawX = x * tileSize;
            float drawY = (rows - y - 1) * tileSize + tileSize + 4;

            if (machine.getActivated() && !machine.getReady()) {
                float progress = 1f - (machine.getHoursLeft() / (float) machine.getMaxProcessTime());
                float barWidth = tileSize;
                float barHeight = 10f;

                batch.setColor(Color.DARK_GRAY); // پس‌زمینه نوار
                batch.draw(GameAssetManager.pixel, drawX, drawY, barWidth, barHeight);
                batch.setColor(Color.GREEN); // نوار پر شده
                batch.draw(GameAssetManager.pixel, drawX, drawY, barWidth * progress, barHeight);
                batch.setColor(Color.WHITE);
            } else if (machine.getReady()) {
                GameAssetManager.customFont.draw(batch, "Done!", drawX + tileSize / 2f - 50, drawY + 20);
                grabButton.setVisible(true);
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
            batch.draw(tiles[y][x].getProductOfGrowable().getCropType().getCropProductTexture(),
                x * tileSize,
                (rows - y - 1) * tileSize,
                tileSize, tileSize);
        } else if (tiles[y][x].getProductOfGrowable().getGrowableType() == GrowableType.Giant) {
            Point point = findTopLeftOfGiantCropSquare(x, y, rows, tiles[0].length, true);
            if (point != null) {
                int topleftX = point.x;
                int topleftY = point.y;
                batch.draw(CropType.fromName(tiles[y][x].getProductOfGrowable().getName()).getGiantTexture(),
                    topleftX * tileSize,
                    (rows - topleftY - 2) * tileSize,
                    2 * tileSize,
                    2 * tileSize);
            }
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
                Point point = findTopLeftOfGiantCropSquare(x, y, rows, tiles[0].length, false);
                if (point != null) {
                    int topleftX = point.x;
                    int topleftY = point.y;
                    batch.draw(tiles[y][x].getContainedGrowable().getCropType().getGiantTexture(),
                        topleftX * tileSize,
                        (rows - topleftY - 2) * tileSize,
                        2 * tileSize,
                        2 * tileSize);
                }
            } else if (tiles[y][x].getProductOfGrowable() != null && !tiles[y][x].getContainedGrowable().getCropType().oneTime()) {
                batch.draw(tiles[y][x].getContainedGrowable().getCropType().getCropProductTexture(),
                    x * tileSize,
                    (rows - y - 1) * tileSize,
                    tileSize, tileSize);
            } else {
                int currentStage = tiles[y][x].getContainedGrowable().getCurrentStage();
                currentStage--;
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
        GlyphLayout layout = new GlyphLayout();
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
            if (!greenHouseTile.getIsGreenHouseFixed()) {
                String message = "To build the greenhouse,\nclick here!\n(Need 1000$ and 500 stones)";

                float centerX = drawX + (8 * tileSize) / 2f;
                float centerY = drawY + (7 * tileSize) / 2f;

                layout.setText(smallFont, message, Color.WHITE, 8 * tileSize, Align.center, true);

                smallFont.draw(batch, layout, centerX - layout.width / 2f - 200, centerY + layout.height / 2f - 100);
            }
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
                if(tile == TileType.EMPTY){
                    Season season = MainApp.getInstance().getCurrentGame().getTimeAndDate().getSeason();
                    switch (season) {
                        case SUMMER:
                            batch.draw(GameAssetManager.FlOORING_50,x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                            break;
                        case AUTUMN:
                            batch.draw(GameAssetManager.FLOORING_64, x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                            break;
                        case WINTER:
                            batch.draw(GameAssetManager.FLOORING_25, x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                            break;
                    }
                }
                if (tiles[y][x].isHasBeenBurt()) {
                    batch.draw(GameAssetManager.burntTile, x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize);
                }
                if (tiles[y][x].getContainedNPC() != null) {
                    batch.draw(tiles[y][x].getContainedNPC().getNpcName().getTextureRegion(), x * tileSize, (rows - y - 1) * tileSize, tileSize, tileSize * 2f);
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
        stage.dispose();
        if (smallFont != null) {
            smallFont.dispose();
        }
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

        if (!currentPlayer.hasFainted() && !currentPlayer.isProposing() && !currentPlayer.isAccepting() && !currentPlayer.isRejecting()) {
            // Clamp moveDirection to valid index range
            int moveDirection = MathUtils.clamp(currentPlayer.getMovingDirection(), 0, playerAnimations.size() - 1);

            Animation<TextureRegion> currentAnimation = playerAnimations.get(moveDirection);
            TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

            // Draw player with height of 2 tiles
            batch.draw(currentFrame, drawX, drawY, tileSize, tileSize * 2);
        } else if (currentPlayer.hasFainted()) {
            batch.draw(faintTexture, drawX, drawY, tileSize, tileSize * 2);
        } else if (currentPlayer.isProposing()) {
            batch.draw(proposingTexture, drawX, drawY, tileSize, tileSize * 2);
        } else if (currentPlayer.isAccepting()) {
            batch.draw(acceptingTexture, drawX, drawY, tileSize, tileSize * 2);
        } else if (currentPlayer.isRejecting()) {
            batch.draw(rejectingTexture, drawX, drawY, tileSize, tileSize * 2);
        }

        boolean anyBuffActive = currentPlayer.isBuffMaxEnergy() ||
            currentPlayer.isBuffForagingSkill() ||
            currentPlayer.isBuffFarmingSkill() ||
            currentPlayer.isBuffFishingSkill() ||
            currentPlayer.isBuffMiningSkill();

        if (anyBuffActive) {
            if (currentPlayer.isBuffMaxEnergy()) buffActiveTexture = InventoryAssets.maxEnergyBuff;
            else if (currentPlayer.isBuffFarmingSkill()) buffActiveTexture = InventoryAssets.farmingBuff;
            else if (currentPlayer.isBuffFishingSkill()) buffActiveTexture = InventoryAssets.fishingBuff;
            else if (currentPlayer.isBuffForagingSkill()) buffActiveTexture = InventoryAssets.foragingBuff;
            else if (currentPlayer.isBuffMiningSkill()) buffActiveTexture = InventoryAssets.miningBuff;

            float iconDrawX = drawX + (tileSize / 4f);
            float iconDrawY = drawY + (tileSize * 1.8f);

            float iconWidth = tileSize / 2f;
            float iconHeight = tileSize / 2f;

            batch.draw(buffActiveTexture, iconDrawX, iconDrawY, iconWidth, iconHeight);
        }
    }

    private void drawAllPlayers() {
        for (User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()) {
            if (otherPlayer.getUsername().equals(currentPlayer.getUsername())) {
                continue;
            }
            if (otherPlayer == null || otherPlayer.getCurrentTile() == null) {
                return;
            }
            Tile tile = otherPlayer.getCurrentTile();

            int tileSize = GameAssetManager.TILE_SIZE;
            int tileX = tile.getX();
            int tileY = tile.getY();

            int drawX = tileX * tileSize;
            int drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - tileY - 1) * tileSize;
            Avatar avatar = otherPlayer.getAvatar();
            ArrayList<Animation<TextureRegion>> animation = null;
            TextureRegion faintedFrame = null;
            TextureRegion proposingFrame = null;
            TextureRegion acceptingFrame = null;
            TextureRegion rejectingFrame = null;
            switch (avatar) {
                case Abigail -> {
                    animation = GameAssetManager.abigailAnimations;
                    faintedFrame = GameAssetManager.abigialFaint;
                    acceptingFrame = GameAssetManager.abigailAccepting;
                    rejectingFrame = GameAssetManager.abigailRejecting;
                }
                case Alex -> {
                    animation = GameAssetManager.alexAnimations;
                    faintedFrame = GameAssetManager.alexFaint;
                    proposingFrame = GameAssetManager.alexProposing;
                }
                case Shane -> {
                    animation = GameAssetManager.shaneAnimations;
                    faintedFrame = GameAssetManager.shaneFaint;
                    proposingFrame = GameAssetManager.shaneProposing;
                }
                case Haley -> {
                    animation = GameAssetManager.haleyAnimations;
                    faintedFrame = GameAssetManager.haleyFaint;
                    acceptingFrame = GameAssetManager.haleyAccepting;
                    rejectingFrame = GameAssetManager.haleyRejecting;
                }
            }
            if (!otherPlayer.hasFainted() && !otherPlayer.isProposing() && !otherPlayer.isAccepting() && !otherPlayer.isRejecting()) {
                Animation<TextureRegion> currentAnimation = animation.get(otherPlayer.getMovingDirection());
                TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
                batch.draw(currentFrame, drawX, drawY, tileSize, tileSize * 2);
            } else if (otherPlayer.hasFainted()) {
                batch.draw(faintedFrame, drawX, drawY, tileSize, tileSize * 2);
            } else if (otherPlayer.isProposing()) {
                batch.draw(proposingFrame, drawX, drawY, tileSize, tileSize * 2);
            } else if (otherPlayer.isAccepting()) {
                batch.draw(acceptingFrame, drawX, drawY, tileSize, tileSize * 2);
            } else if (otherPlayer.isRejecting()) {
                batch.draw(rejectingFrame, drawX, drawY, tileSize, tileSize * 2);
            }
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
            Growable product = map[y][x].getProductOfGrowable();
            return product != null && product.getGrowableType() == GrowableType.Giant;
        } else {
            Growable growable = map[y][x].getContainedGrowable();
            return growable != null && growable.getGrowableType() == GrowableType.Giant;
        }
    }


    private boolean isClickInside(float x, float y, Actor actor) {
        return x >= actor.getX() && x <= actor.getX() + actor.getWidth() &&
            y >= actor.getY() && y <= actor.getY() + actor.getHeight();
    }

    private void updateLighting(int gameHour) {
        float alpha = 0f;

        if (gameHour >= 18 && gameHour < 22) {
            alpha = (gameHour - 18) / 4f * 0.8f;
        } else if (gameHour >= 22) {
            alpha = 0.8f;
        } else {
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
            currentPlayer.reduceEnergy(1);
            currentPlayer.setMovingDirection(direction);
            //setCameraPosition();
            //camera.update();
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
            String textureOrigin;
            if (tool instanceof FishingPole) {
                textureOrigin = ((FishingPole) tool).getPoleMaterial().name().toUpperCase() + tool.getType().name().toUpperCase();
            } else {
                textureOrigin = tool.getMaterial().name().toUpperCase() + tool.getType().name().toUpperCase();
            }
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
        if (itemTex == null) { return; }
        if (currentPlayer == null || currentPlayer.getCurrentTile() == null) return;

        Tile tile = currentPlayer.getCurrentTile();
        int tileSize = GameAssetManager.TILE_SIZE;

        int tileX = tile.getX();
        int tileY = tile.getY();

        int drawX = tileX * tileSize;
        int drawY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - tileY - 1) * tileSize;

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

        // Determine energy weather modifier
        double energyWeatherModifier = 1.0;
        if (MainApp.getInstance().getCurrentGame().getCurrentWeatherType() == WeatherType.STORM) {
            energyWeatherModifier = 1.2;
        }

        if (toolToUse instanceof FishingPole) {
            FishingPole fishingPole = (FishingPole) toolToUse;

            FishingAttemptOutcome fishingOutcome = fishingPole.useFishingPole(
                fishingPole,
                MainApp.getInstance().getCurrentGame().getMap(),
                currentPlayer.getCurrentTile(),
                MainApp.getInstance().getCurrentGame().getCurrentPlayer(),
                MainApp.getInstance().getCurrentGame(),
                energyWeatherModifier
            );


            if (fishingOutcome.generalResult().isSuccessful() && fishingOutcome.minigameData().isPresent()) {
                FishingMinigameData data = fishingOutcome.minigameData().get();
                currentCaughtFish = data.hookedFish();

                int playerFishingLevel = currentPlayer.getSkillsLevel().get(Skill.FISHING);
                fishingMinigameDialog.startMinigame(
                    playerFishingLevel,
                    data.movementType(),
                    currentCaughtFish
                );
                isFishingActive = true;
                Gdx.input.setInputProcessor(this);
            }
        } else {
            int tileSize = GameAssetManager.TILE_SIZE;

            float playerTileGridX = currentPlayer.getCurrentTile().getX();
            float playerTileGridY = currentPlayer.getCurrentTile().getY();

            float playerWorldX = playerTileGridX * tileSize + tileSize / 2f;
            float playerWorldY = (MainApp.getInstance().getCurrentGame().getMap().getMap().length - 1 - playerTileGridY) * tileSize + tileSize / 2f;

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

            // Start the tool usage animation (for non-fishing tools)
            isToolBeingUsed = true;
            toolUsageStateTime = 0f;

            if (InventoryAssets.DIRECTION_NAMES != null && InventoryAssets.DIRECTION_NAMES.containsKey(direction)) {
                Result result = controller.useTool(InventoryAssets.DIRECTION_NAMES.get(direction));
                if (!result.isSuccessful()) showErrorDialog(stage, result.message());
            } else {
                Result result = controller.useTool("Down");
                if (!result.isSuccessful()) showErrorDialog(stage, result.message());
            }
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
        float itemImagePadding = slotSize * 0.1f;
        float itemImageRenderSize = slotSize - (itemImagePadding * 2);
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

            if (currentSlotIndex == selectedSlot) {
                Image highlightImage = new Image(InventoryAssets.highlightedSlot);
                highlightImage.setSize(slotSize, slotSize);
                itemSlotStack.add(highlightImage);
            }

            System.out.println(item.getName());
            System.out.println(item.getClass());
            Texture itemTex = getItemTexture(item);
            if (itemTex != null) {
                Image itemImage = new Image(itemTex);
                itemImage.setSize(itemImageRenderSize, itemImageRenderSize);
                itemImage.setScaling(Scaling.fit);
                itemImage.setAlign(com.badlogic.gdx.utils.Align.center);

                Container<Image> itemImageContainer = new Container<>(itemImage);
                itemImageContainer.pad(itemImagePadding);
                itemImageContainer.fill();

                itemSlotStack.add(itemImageContainer);
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


            itemsContainer.add(itemSlotStack).size(slotSize).pad(5);

            currentSlotIndex++;

            if (currentSlotIndex % 6 == 0) {
                itemsContainer.row();
            }
        }

        // Fill remaining empty slots
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
            trashcanImage.setTouchable(Touchable.disabled);
            trashcanButton.clearChildren();
            trashcanButton.add(trashcanImage).expand().fill().center();
        } else {
            trashcanButton.setText("TRASH");
            Gdx.app.error("GameView", "Trashcan texture is null. Using text fallback.");
        }

        trashcanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlot != -1 && selectedSlot < sortedItems.size()) {
                    Item itemToTrash = sortedItems.get(selectedSlot);
                    backpack.removeItem(itemToTrash.getName(), 1);
                    showBackpack();
                } else {
                    System.out.println("No item selected to trash.");
                }
            }
        });

        TextButton selectButton = new TextButton("Select", GameAssetManager.skin, "custom-button");
        selectButton.setColor(Color.BLUE);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedSlot != -1 && selectedSlot < sortedItems.size()) {
                    Item selectedBackpackItem = sortedItems.get(selectedSlot);
                    if ("PutToFridge".equals(scenario)) {
                        handlePutToFridge(selectedBackpackItem);
                        backpackMenuTable.setVisible(false);
                        showBackpackMenu = false;
                        showFridgeMenu = false;
                        scenario = "";
                    } else {
                        equippedItem = selectedBackpackItem;
                        updateEquippedItemSlot();
                        showNumItemDialog();
                    }
                } else {
                    showErrorDialog(stage, "No item selected.");
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
                showBackpackMenu = false;
                backpackMenuTable.setVisible(false);
                inventoryMenuTable.setVisible(true);
                showInventoryMenu = true;
                selectedSlot = 0;
                scenario = "";
            }
        });
        controlButtonsTable.add(backButton).width(100).height(40);
        controlButtonsTable.add(trashcanButton).width(slotSize * 0.7f).height(slotSize * 0.7f);
        controlButtonsTable.add(selectButton).width(100).height(40);

        backpackMenuTable.add(controlButtonsTable).bottom().center().row();

        backpackMenuTable.setVisible(true);
        showBackpackMenu = true;
    }

    public Texture getItemTexture(Item item) {
        if(item == null) {return null;}
        if (item instanceof Fish) {
            return ((Fish) item).getType().getTexture();
        }
        if (item instanceof Food) {
            return ((Food) item).getType().getTexture();
        }
        if (item instanceof ForagingMineral) {
            return ((ForagingMineral) item).getType().getTexture();
        }
        if (item instanceof randomStuff) {
            return ((randomStuff) item).getType().getTexture();
        }
        if (item instanceof AnimalProduct) {
            return ((AnimalProduct) item).getAnimalProductType().getTexture();
        }
        if (item instanceof Machine) {
            return ((Machine) item).getType().getTexture();
        }
        for(SourceType sourceType : SourceType.values()) {
            if(sourceType.getName().equalsIgnoreCase(item.getName())) {
                return sourceType.getTexture();
            }
        }
        for(ForagingCropType foragingCropType : ForagingCropType.values()) {
            if(foragingCropType.getName().equalsIgnoreCase(item.getName())) {
                return foragingCropType.getTexture();
            }
        }
        for(CropType cropType : CropType.values()) {
            if(cropType.getName().equalsIgnoreCase(item.getName())) {
                return cropType.getCropProductTexture();
            }
        }
        for(FruitType fruitType : FruitType.values()) {
            if(fruitType.getName().equalsIgnoreCase(item.getName())) {
                return fruitType.getTexture();
            }
        }
        return null;
    }

    private void showSocialMenu() {
        if (socialMenuDialog != null && socialMenuDialog.getStage() != null && socialMenuDialog.isVisible()) {
            socialMenuDialog.hide();
            return;
        }

        socialMenuDialog = new Dialog("Social Menu", GameAssetManager.skin, "custom-window");

        socialMenuDialog.padTop(80);
        socialMenuDialog.getTitleLabel().setAlignment(com.badlogic.gdx.utils.Align.center);
        socialMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

        float dialogWidth = Gdx.graphics.getWidth() * 0.4f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.6f;

        socialMenuDialog.setSize(dialogWidth, dialogHeight);


        Table contentTable = new Table(GameAssetManager.skin);
        contentTable.defaults().pad(5).align(com.badlogic.gdx.utils.Align.left);
        contentTable.row();

        // --- NPC Friendship Section ---
        Label npcTitle = new Label("NPC Friendship Levels:", GameAssetManager.skin, "custom-label");
        npcTitle.setColor(Color.YELLOW);
        contentTable.add(npcTitle).colspan(4).padBottom(10).row();

        int XP_PER_LEVEL = 200;

        for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
            int level = npc.getFriendshipLevels().get(currentPlayer.getUsername());
            int currentXP = npc.getFriendshipPoints().get(currentPlayer.getUsername());

            if (currentXP >= XP_PER_LEVEL) currentXP %= XP_PER_LEVEL;

            Label nameLabel = new Label(npc.getNpcName().getName(), new Label.LabelStyle(smallFont, Color.WHITE));
            Label levelLabel = new Label("Lvl: " + level, new Label.LabelStyle(smallFont, Color.WHITE));

            ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
            TextureRegion pixelTextureRegion = new TextureRegion(GameAssetManager.pixel);

            Drawable progressBarBackground = new TextureRegionDrawable(pixelTextureRegion).tint(Color.DARK_GRAY);
            progressBarStyle.background = progressBarBackground;
            progressBarStyle.background.setMinHeight(20);

            progressBarStyle.knob = new TextureRegionDrawable(pixelTextureRegion);
            progressBarStyle.knob.setMinWidth(0);

            Drawable progressBarKnobBefore = new TextureRegionDrawable(pixelTextureRegion).tint(Color.YELLOW);
            progressBarStyle.knobBefore = progressBarKnobBefore;
            progressBarStyle.knobBefore.setMinHeight(20);

            ProgressBar xpBar = new ProgressBar(0, XP_PER_LEVEL, 1, false, progressBarStyle);
            xpBar.setValue(currentXP);

            Label xpTextLabel = new Label(currentXP + "/" + XP_PER_LEVEL, GameAssetManager.skin, "custom-label");
            xpTextLabel.setFontScale(0.5f);
            xpTextLabel.setColor(Color.LIGHT_GRAY);

            contentTable.add(nameLabel).width(150);
            contentTable.add(levelLabel).width(80).row();
            contentTable.add(xpBar).width(200).height(20).padLeft(10).colspan(2);
            contentTable.add(xpTextLabel).width(70).padLeft(5).row();
        }

        // --- Player Friendship Section ---
        Label playerTitle = new Label("Player Friendship Levels:", GameAssetManager.skin, "custom-label");
        playerTitle.setColor(Color.YELLOW);
        contentTable.add(playerTitle).colspan(4).padTop(20).padBottom(10).row();

        for (User player : MainApp.getInstance().getCurrentGame().getPlayers()) {
            if (player.getUsername().equals(currentPlayer.getUsername())) continue;
            Friendship friendship = MainApp.getInstance().getCurrentGame().getFriendship(player.getUsername(), currentPlayer.getUsername());
            int level = friendship.getLevel();
            int XP_PER_LEVEL_Player = 100 * (1 + level);

            int currentXP = friendship.getXp();
            if (currentXP >= XP_PER_LEVEL_Player) currentXP %= XP_PER_LEVEL_Player;

            Label nameLabel = new Label(player.getUsername(), new Label.LabelStyle(smallFont, Color.WHITE));
            Label levelLabel = new Label("Lvl: " + level, new Label.LabelStyle(smallFont, Color.WHITE));

            ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
            TextureRegion pixelTextureRegion = new TextureRegion(GameAssetManager.pixel);

            Drawable progressBarBackground = new TextureRegionDrawable(pixelTextureRegion).tint(Color.DARK_GRAY);
            progressBarStyle.background = progressBarBackground;
            progressBarStyle.background.setMinHeight(20);

            progressBarStyle.knob = new TextureRegionDrawable(pixelTextureRegion);
            progressBarStyle.knob.setMinWidth(0);

            Drawable progressBarKnobBefore = new TextureRegionDrawable(pixelTextureRegion).tint(Color.YELLOW);
            progressBarStyle.knobBefore = progressBarKnobBefore;
            progressBarStyle.knobBefore.setMinHeight(20);

            ProgressBar xpBar = new ProgressBar(0, XP_PER_LEVEL_Player, 1, false, progressBarStyle);
            xpBar.setValue(currentXP);

            Label xpTextLabel = new Label(currentXP + "/" + XP_PER_LEVEL_Player, GameAssetManager.skin, "custom-label");
            xpTextLabel.setFontScale(0.5f);
            xpTextLabel.setColor(Color.LIGHT_GRAY);

            contentTable.add(nameLabel).width(150);
            contentTable.add(levelLabel).width(80).row();
            contentTable.add(xpBar).width(200).height(20).padLeft(10).colspan(2);
            contentTable.add(xpTextLabel).width(70).padLeft(5).row();
        }

        ScrollPane scrollPane = new ScrollPane(contentTable, GameAssetManager.skin);
        scrollPane.setFadeScrollBars(false);

        socialMenuDialog.getContentTable().add(scrollPane)
            .width(dialogWidth)
            .height(dialogHeight - 150)
            .expand().fill().row();

        // Close button
        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.setColor(Color.RED);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                socialMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });
        socialMenuDialog.getButtonTable().add(closeButton).pad(10);

        socialMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - socialMenuDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - socialMenuDialog.getHeight()) / 2
        );
        stage.addActor(socialMenuDialog);
    }

    private void showMissionsMenu() {
        float dialogWidth = Gdx.graphics.getWidth() * 0.4f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.6f;

        if (MissionsMenuDialog != null && MissionsMenuDialog.getStage() != null && MissionsMenuDialog.isVisible()) {
            MissionsMenuDialog.hide();
            Gdx.input.setInputProcessor(this);
            return;
        }
        if (MissionsMenuDialog == null) {
            MissionsMenuDialog = new Dialog("Missions", GameAssetManager.skin, "custom-window");
            MissionsMenuDialog.padTop(80);
            MissionsMenuDialog.getTitleLabel().setAlignment(com.badlogic.gdx.utils.Align.center);
            MissionsMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

            MissionsMenuDialog.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!MissionsMenuDialog.isVisible()) {
                        Gdx.input.setInputProcessor(GameView.this);
                    }
                }
            });
            MissionsMenuDialog.setSize(dialogWidth, dialogHeight);

            stage.addActor(MissionsMenuDialog);
        } else {
            MissionsMenuDialog.getContentTable().clear();
            MissionsMenuDialog.getButtonTable().clear();
            MissionsMenuDialog.setSize(dialogWidth, dialogHeight);
        }

        Table questsTable = new Table(GameAssetManager.skin);
        questsTable.defaults().pad(5).align(Align.left);

        List<NPCMission> missionsForPlayer = MainApp.getInstance().getCurrentGame().getPlayerAddedMissions().get(currentPlayer.getUsername());

        if (missionsForPlayer == null || missionsForPlayer.isEmpty()) {
            questsTable.add(new Label("No missions selected.", GameAssetManager.skin, "custom-label")).colspan(2).row();
        } else {
            int missionIndex = 1;
            for (NPCMission mission : missionsForPlayer) {
                Label missionLabel = new Label("Mission " + missionIndex + ":", GameAssetManager.skin, "custom-label");
                missionLabel.setColor(Color.CORAL);
                missionLabel.setFontScale(0.8f);
                questsTable.add(missionLabel).colspan(2).row();

                Label requiredLabel = new Label("  Required:", GameAssetManager.skin, "custom-label");
                requiredLabel.setFontScale(0.7f);
                requiredLabel.setColor(Color.ORANGE);
                questsTable.add(requiredLabel).row();
                Table requiredItemsTable = new Table(GameAssetManager.skin);
                requiredItemsTable.defaults().padLeft(10);
                if (mission.getRequiredItems().isEmpty()) {
                    Label itemLabel = new Label("None", GameAssetManager.skin, "custom-label");
                    itemLabel.setFontScale(0.5f);
                    requiredItemsTable.add(itemLabel);
                } else {
                    for (Map.Entry<String, Integer> entry : mission.getRequiredItems().entrySet()) {
                        Label itemLabel = new Label(entry.getKey() + ": " + entry.getValue(), GameAssetManager.skin, "custom-label");
                        itemLabel.setFontScale(0.5f);
                        requiredItemsTable.add(itemLabel).row();
                    }
                }
                questsTable.add(requiredItemsTable).colspan(2).row();

                Label prizeLabel = new Label("  Prizes:", GameAssetManager.skin, "custom-label");
                prizeLabel.setFontScale(0.7f);
                prizeLabel.setColor(Color.ORANGE);
                questsTable.add(prizeLabel).row();
                Table prizeItemsTable = new Table(GameAssetManager.skin);
                prizeItemsTable.defaults().padLeft(10);
                if (mission.getPrizeItems().isEmpty()) {
                    Label itemLabel = new Label("None", GameAssetManager.skin, "custom-label");
                    itemLabel.setFontScale(0.5f);
                    prizeItemsTable.add(itemLabel);
                } else {
                    for (Map.Entry<String, Integer> entry : mission.getPrizeItems().entrySet()) {
                        Label itemLabel = new Label(entry.getKey() + ": " + entry.getValue(), GameAssetManager.skin, "custom-label");
                        itemLabel.setFontScale(0.5f);
                        prizeItemsTable.add(itemLabel).row();
                    }
                }
                questsTable.add(prizeItemsTable).colspan(2).row();

                Label statusLabel = new Label(mission.getAlreadyDone() ? "Status: Done" : "Status: Not Done", GameAssetManager.skin, "custom-label");
                if (mission.getAlreadyDone()) {
                    statusLabel.setColor(Color.GREEN);
                } else {
                    statusLabel.setColor(Color.RED);
                }
                statusLabel.setFontScale(0.6f);
                TextButton doMissionButton = new TextButton("Complete", GameAssetManager.skin, "custom-button");
                doMissionButton.getLabel().setFontScale(0.7f);
                doMissionButton.setDisabled(mission.getAlreadyDone());

                doMissionButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Result result = NPCMission.doMission(mission, currentPlayer);
                        showErrorDialog(stage, result.getMessage());
                        MissionsMenuDialog.hide();
                    }
                });

                questsTable.add(statusLabel).padRight(10);
                questsTable.add(doMissionButton).width(120).height(30).padBottom(10).row();
                questsTable.add().colspan(2).height(10).row();
                missionIndex++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(questsTable, GameAssetManager.skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        MissionsMenuDialog.getContentTable().add(scrollPane).expand().fill().row();

        // Close button
        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.setColor(Color.RED);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MissionsMenuDialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });
        MissionsMenuDialog.getButtonTable().add(closeButton).pad(10);

        MissionsMenuDialog.show(stage);
        MissionsMenuDialog.setSize(dialogWidth, dialogHeight);
        MissionsMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - MissionsMenuDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - MissionsMenuDialog.getHeight()) / 2
        );
        Gdx.input.setInputProcessor(stage);
    }

    private void toggleSkillsDialog() {
        if (skillsDialog != null && skillsDialog.getStage() != null) {
            skillsDialog.hide();
            skillsDialog = null;
            return;
        }

        if (showInventoryMenu) {
            showInventoryMenu = false;
            inventoryMenuTable.setVisible(false);
        }
        if (showBackpackMenu) {
            showBackpackMenu = false;
            backpackMenuTable.setVisible(false);
        }
        if (friendsDialog != null && friendsDialog.getStage() != null) {
            friendsDialog.hide();
            friendsDialog = null;
        }

        skillsDialog = new Dialog("Skills", GameAssetManager.skin, "custom-window");
        skillsDialog.padTop(120);
        skillsDialog.getTitleLabel().setAlignment(com.badlogic.gdx.utils.Align.center);

        skillsDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

        float dialogWidth = Gdx.graphics.getWidth() * 0.4f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.6f;
        skillsDialog.setSize(dialogWidth, dialogHeight);

        Table skillsTable = new Table();
        skillsTable.defaults().pad(5);


        Map<String, String> skillDescriptions = Map.of(
            "Farming", "Improves your ability to grow crops and raise animals.",
            "Mining", "Enhances your efficiency when gathering ores and minerals.",
            "Foraging", "Increases your chances of finding rare items and improves gathering wild plants.",
            "Fishing", "Makes it easier to catch fish and improves the quality of your catches."
        );

        Map<String, Color> skillColors = Map.of(
            "Farming", new Color(0.8f, 0.6f, 0.2f, 2f),
            "Mining", new Color(0.6f, 0.6f, 0.6f, 2f),
            "Foraging", new Color(0.3f, 0.5f, 0.2f, 3f),
            "Fishing", new Color(0.2f, 0.5f, 0.8f, 2f)
        );


        TextureRegion pixelTextureRegion = new TextureRegion(GameAssetManager.pixel);

        TooltipManager tooltipManager = TooltipManager.getInstance();
        tooltipManager.initialTime = 0.1f;
        tooltipManager.resetTime = 0.5f;
        tooltipManager.hideAll();


        for (Map.Entry<String, String> entry : skillDescriptions.entrySet()) {
            String skillName = entry.getKey();
            String description = entry.getValue();
            int currentLevel = 0;
            int currentXP = 0;

            for (Skill skill : Skill.values()) {
                if (skill.name().equalsIgnoreCase(skillName)) {
                    currentXP = currentPlayer.getSkillExperience().get(skill);
                    currentLevel = currentPlayer.getSkillsLevel().get(skill);
                }
            }
            int maxXPForLevel = currentLevel * 100 + 50;

            Label skillLabel = new Label(skillName + ": Lvl " + currentLevel, GameAssetManager.skin, "custom-label");
            skillLabel.setColor(skillColors.getOrDefault(skillName, Color.WHITE));

            skillsTable.add(skillLabel).width(240).center().colspan(3).row();


            ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();

            Drawable progressBarBackground = new TextureRegionDrawable(pixelTextureRegion).tint(Color.DARK_GRAY);
            progressBarStyle.background = progressBarBackground;
            progressBarStyle.background.setMinHeight(20);

            progressBarStyle.knob = new TextureRegionDrawable(pixelTextureRegion);
            progressBarStyle.knob.setMinWidth(0);

            Drawable progressBarKnobBefore = new TextureRegionDrawable(pixelTextureRegion).tint(new Color(0.2f, 0.8f, 0.2f, 1)); // Green
            progressBarStyle.knobBefore = progressBarKnobBefore;
            progressBarStyle.knobBefore.setMinHeight(20);

            ProgressBar xpBar = new ProgressBar(0, maxXPForLevel, 1, false, progressBarStyle);
            xpBar.setValue(currentXP);

            Label xpTextLabel = new Label(currentXP + "/" + maxXPForLevel, GameAssetManager.skin, "custom-label");
            xpTextLabel.setFontScale(0.7f);
            xpTextLabel.setColor(Color.LIGHT_GRAY);

            skillsTable.add().width(20);
            skillsTable.add(xpBar).width(150).height(20);
            skillsTable.add(xpTextLabel).width(70).row();

            Label tooltipLabelContent = new Label(description, GameAssetManager.skin, "custom-label");
            tooltipLabelContent.setFontScale(0.6f);
            tooltipLabelContent.setWrap(true);
            tooltipLabelContent.setAlignment(com.badlogic.gdx.utils.Align.center);

            final Tooltip<Label> tooltip = new Tooltip<>(tooltipLabelContent, tooltipManager);

            Drawable tooltipBackground = new TextureRegionDrawable(pixelTextureRegion).tint(Color.GOLDENROD);
            tooltip.getContainer().width(200).pad(5).background(tooltipBackground);

            skillLabel.addListener(tooltip);
        }

        skillsDialog.getContentTable().add(skillsTable).expand().fill().center().row();


        TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
        closeButton.setColor(Color.RED);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skillsDialog.hide();
                skillsDialog = null;
            }
        });
        skillsDialog.getButtonTable().add(closeButton).pad(10);


        skillsDialog.setPosition(
            (Gdx.graphics.getWidth() - skillsDialog.getWidth()) / 2,
            (Gdx.graphics.getHeight() - skillsDialog.getHeight()) / 2
        );
        stage.addActor(skillsDialog);
    }

    private void showNotifications() {
        List<FriendshipMessage> notifications = currentPlayer.getNotifications();
        if (notifications.isEmpty()) return;
        Gdx.input.setInputProcessor(stage);

        StringBuilder generalNotifications = new StringBuilder();

        for (FriendshipMessage notification : notifications) {
            String message = notification.getMessage();

            if (message.endsWith("has asked to marry you")) {
                Dialog proposalDialog = proposalNotification(notification);
                Gdx.input.setInputProcessor(stage);
                proposalDialog.show(stage);
            } else if (message.equals("force terminate has started!")) {
                Dialog forceTerminationDialog = forceTerminationNotification();
                Gdx.input.setInputProcessor(stage);
                forceTerminationDialog.show(stage);
            } else {
                generalNotifications.append("- From ").append(notification.getSender())
                    .append(": ").append(message).append("\n");
            }
        }

        if (!generalNotifications.isEmpty()) {
            Dialog generalDialog = new Dialog("Notifications", GameAssetManager.skin, "custom-window") {
                @Override
                protected void result(Object obj) {
                    Gdx.input.setInputProcessor(GameView.this);
                }
            };
            Label notificationLabel = new Label(generalNotifications.toString(), GameAssetManager.skin, "custom-label");
            notificationLabel.setWrap(true);

            generalDialog.getContentTable().add(notificationLabel).width(400).pad(20);
            generalDialog.getContentTable().row();

            TextButton okButton = new TextButton("OK", GameAssetManager.skin, "custom-button");
            generalDialog.button(okButton, true);
            Gdx.input.setInputProcessor(stage);
            generalDialog.show(stage);
        }

        notifications.clear();
    }

    @NotNull
    private Dialog proposalNotification(FriendshipMessage notification) {
        String sender = notification.getSender();

        Dialog proposalDialog = new Dialog("Marriage Proposal", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object obj) {
                Gdx.input.setInputProcessor(GameView.this);
                boolean accepted = (Boolean) obj;
                if (accepted) {
                    controller.respondToMarriage("accept", sender);
                    currentPlayer.setAccepting(true);
                } else {
                    controller.respondToMarriage("reject", sender);
                    currentPlayer.setRejecting(true);
                }
            }
        };
        Label label = new Label(sender + " has asked to marry you", GameAssetManager.skin, "custom-label");
        proposalDialog.getContentTable().add(label).pad(10);

        TextButton acceptButton = new TextButton("Accept", GameAssetManager.skin, "custom-button");
        TextButton rejectButton = new TextButton("Reject", GameAssetManager.skin, "custom-button");

        proposalDialog.button(acceptButton, true);
        proposalDialog.button(rejectButton, false);
        return proposalDialog;
    }

    @NotNull
    private Dialog forceTerminationNotification() {
        Dialog forceTerminationDialog = new Dialog("Force Termination", GameAssetManager.skin, "custom-window") {
            @Override
            protected void result(Object obj) {
                Gdx.input.setInputProcessor(GameView.this);
                boolean accepted = (Boolean) obj;
                if (accepted) {
                    Result result = controller.voteToTerminate(true, currentPlayer);
                    if(!result.isSuccessful()) showErrorDialog(stage, result.message());
                    else {
                        if (gameTickTask != null) {
                            gameTickTask.cancel();
                        }
                        MainApp.getInstance().setCurrentGame(null);
                        MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
                        MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(),GameAssetManager.skin));
                    }
                } else {
                    Result result = controller.voteToTerminate(false, currentPlayer);
                    showErrorDialog(stage, result.message());
                }
            }
        };
        Label label = new Label("Do you want to force terminate this game ?", GameAssetManager.skin, "custom-label");
        forceTerminationDialog.getContentTable().add(label).pad(10);

        TextButton acceptButton = new TextButton("Yes", GameAssetManager.skin, "custom-button");
        TextButton rejectButton = new TextButton("No", GameAssetManager.skin, "custom-button");

        forceTerminationDialog.button(acceptButton, true);
        forceTerminationDialog.button(rejectButton, false);
        return forceTerminationDialog;
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
        }  else if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
//            System.out.println(controller.cheatAddMoney(matcher.group("count")));
            String sountString = matcher.group("count").trim();
            Map<String, Object> params = new HashMap<>();
            params.put("money",matcher.group("count"));
            MainApp.getInstance().getNetworkClient()
                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController", "cheatAddMoney", params, currentPlayer.getUsername())
                .thenAccept(response -> {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);
                    if (response.getStatus() == 200) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("yasssssssssss");
                            int count = Integer.parseInt(sountString);
                            currentPlayer.addMoney(count);
//                            if (!result.isSuccessful()) {
//                                showErrorDialog(stage, result.message());
//                            } else {
//                                showErrorDialog(stage, result.message()); // or update the UI
//                            }
                            System.out.println(result.message());
                        });
                    } else {
                        System.out.println("nooooo wayyyyy???");
                        Gdx.app.postRunnable(() -> {
                            System.out.println(result.message());
                            // showErrorDialog(stage, "Failed to use cheat: " + response.getMessage());
                        });
                    }
                });
        }
        else if ((matcher = GameMenuCommands.CHEAT_ANIMAL_FRIENDSHIP.getMatcher(input)) != null) {
            System.out.println(controller.cheatAnimalFriendship(matcher.group("name"), matcher.group("amount")));
        } else if ((matcher = GameMenuCommands.CHeat_THOR.getMatcher(input)) != null) {
            System.out.println(controller.cheatThor(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.CHEAT_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeEnergy(matcher.group("value")));
        } else if ((matcher = GameMenuCommands.CHEAT_UNLIMITED_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatUnlimitedEnergy());
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeWeather(matcher.group("weather")));
        }
        else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
//            String itemName = matcher.group("itemName");
//            int count = Integer.parseInt(matcher.group("count"));
//            System.out.println(controller.cheatAddItem(itemName, count));
            String sountString = matcher.group("count").trim();
            String itemName =matcher.group("itemName").trim();
            Map<String, Object> params = new HashMap<>();
            params.put("itemName",matcher.group("itemName"));
            params.put("count",matcher.group("count"));
            MainApp.getInstance().getNetworkClient()
                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController", "cheatAddItem", params, currentPlayer.getUsername())
                .thenAccept(response -> {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);
                    System.out.println("salamsalasmsalmsalsams");
                    if (response.getStatus() == 200) {
                        Gdx.app.postRunnable(() -> {
                            System.out.println("yasssssssssss");
                            int count = Integer.parseInt(sountString);
                            Item item = Item.getRandomItem(itemName);
                            if (item == null) {
                                System.out.println("CLIENT: Item not found: " + itemName); // <-- ADD THIS
                                return;
                            }
                            currentPlayer.getBackpack().addItem(item,count);
//                            if (!result.isSuccessful()) {
//                                showErrorDialog(stage, result.message());
//                            } else {
//                                showErrorDialog(stage, result.message()); // or update the UI
//                            }
                            System.out.println(result.message());
                        });
                    } else {
                        System.out.println("nooooo wayyyyy???");
                        Gdx.app.postRunnable(() -> {
                            System.out.println(result.message());
                            // showErrorDialog(stage, "Failed to use cheat: " + response.getMessage());
                        });
                    }
                });
        }
        else if ((matcher = GameMenuCommands.CHEAT_WALK.getMatcher(input)) != null) {
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
        //}
        else if ((matcher = GameMenuCommands.CAFTINFO.getMatcher(input)) != null) {
            controller.printCraftInfo(matcher.group("craftname"));
        }
        //else if ((matcher = GameMenuCommands.TREEINFO.getMatcher(input)) != null) {
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
//    public void handleCommand(Scanner scanner, Consumer<String> outputCallback){
//    String input = scanner.nextLine().trim();
//        Matcher matcher;
//
//        if ((matcher = GameMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
//            //outputCallback.accept(controller.showCurrentMenu());
//        } else if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
//            String countStr = matcher.group("count").trim();
//            Map<String, Object> params = new HashMap<>();
//            params.put("money", countStr);
//            MainApp.getInstance().getNetworkClient()
//                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
//                    "GameController", "cheatAddMoney", params, currentPlayer.getUsername())
//                .thenAccept(response -> {
//                    Gson gson = new Gson();
//                    Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);
//                    if (response.getStatus() == 200) {
//                        Gdx.app.postRunnable(() -> {
//                            int count = Integer.parseInt(countStr);
//                            currentPlayer.addMoney(count);
//                            outputCallback.accept(result.message());
//                        });
//                    } else {
//                        Gdx.app.postRunnable(() -> {
//                            outputCallback.accept("Failed: " + result.message());
//                        });
//                    }
//                });
//        }
//        // Add outputCallback.accept(...) in all other cases where System.out.println is used
//        else {
//            outputCallback.accept("invalid command");
//        }
//    }

    public void handleCommand(Scanner scanner, Consumer<String> callback) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

         if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
            String amount = matcher.group("count").trim();
            Map<String, Object> params = new HashMap<>();
            params.put("money", amount);

            MainApp.getInstance().getNetworkClient()
                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController", "cheatAddMoney", params, currentPlayer.getUsername())
                .thenAccept(response -> {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);
                    Gdx.app.postRunnable(() -> {
                        if (response.getStatus() == 200) {
                            currentPlayer.addMoney(Integer.parseInt(amount));
                            callback.accept(result.message());
                        } else {
                            callback.accept("Error: " + result.message());
                        }
                    });
                });

        } else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {

            String sountString = matcher.group("count").trim();
            String itemName =matcher.group("itemName").trim();
            Map<String, Object> params = new HashMap<>();
            params.put("itemName",matcher.group("itemName"));
            params.put("count",matcher.group("count"));
            MainApp.getInstance().getNetworkClient()
                .sendPost(MainApp.getInstance().getCurrentGame().getNetworkId(),
                    "GameController", "cheatAddItem", params, currentPlayer.getUsername())
                .thenAccept(response -> {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(gson.toJson(response.getBody()), Result.class);
                    if (response.getStatus() == 200) {
                        Gdx.app.postRunnable(() -> {
                            int count = Integer.parseInt(sountString);
                            Item item = Item.getRandomItem(itemName);
                            if (item == null) {
                                callback.accept("CLIENT: Item not found: " + itemName); // <-- ADD THIS
                                return;
                            }
                            currentPlayer.getBackpack().addItem(item,count);
                            callback.accept(result.message());
                        });
                    } else {
                        Gdx.app.postRunnable(() -> {
                            callback.accept(result.message());
                        });
                    }
                });
        }else {
            callback.accept("Invalid command");
        }
    }

    //    public void showErrorDialog(Stage stage, String message) {
//        Skin skin = GameAssetManager.skin;
//
//        Dialog dialog = new Dialog("", skin) {
//            @Override
//            protected void result(Object object) {
//                // Optional: Handle result
//            }
//        };
//
//        dialog.setBackground("window"); // make sure "window" drawable exists in your skin
//
//        Label messageLabel = new Label(message, skin, "custom-label");
//        messageLabel.setWrap(true);
//        messageLabel.setAlignment(Align.center);
//        messageLabel.setFontScale(0.7f); // Optional
//
//        TextButton okButton = new TextButton("OK", skin, "custom-button");
//        okButton.pad(10f);
//        okButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                dialog.hide(); // Close dialog
//                Gdx.input.setInputProcessor(GameView.this); // return control to GameView if needed
//            }
//        });
//
//        Table contentTable = new Table();
//        contentTable.defaults().pad(10f);
//        contentTable.add(messageLabel).width(stage.getWidth() * 0.5f).row();
//        contentTable.add(okButton).center();
//
//        dialog.getContentTable().clear();
//        dialog.getContentTable().add(contentTable).expand().fill();
//
//        dialog.setMovable(false);
//        dialog.setModal(true);
//        dialog.setResizable(false);
//
//        float dialogWidth = stage.getWidth() * 0.4f;
//        float dialogHeight = stage.getHeight() * 0.25f;
//        dialog.setSize(dialogWidth, dialogHeight);
//        dialog.setPosition(
//            (stage.getWidth() - dialogWidth) / 2f,
//            (stage.getHeight() - dialogHeight) / 2f
//        );
//
//        stage.addActor(dialog);
//        Gdx.input.setInputProcessor(stage); // 🔥 Important: Enable input for stage
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
        messageLabel.setFontScale(0.7f);

        float maxWidth = stage.getWidth() * 0.6f;
        messageLabel.setWidth(maxWidth); // Required for wrapping to work
        messageLabel.invalidateHierarchy(); // Force layout to recalculate size

        // Let the label wrap and calculate the height
        Table contentTable = new Table();
        contentTable.defaults().pad(10f);
        contentTable.add(messageLabel).width(maxWidth).row();

        TextButton okButton = new TextButton("OK", skin, "custom-button");
        okButton.pad(10f);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                Gdx.input.setInputProcessor(GameView.this);
            }
        });

        contentTable.add(okButton).center().padTop(10f);

        dialog.getContentTable().clear();
        dialog.getContentTable().add(contentTable).expand().fill();
        dialog.setMovable(false);
        dialog.setModal(true);
        dialog.setResizable(false);

        dialog.pack(); // Automatically size dialog based on contents

        // Clamp width/height to screen size if needed
        float clampedWidth = Math.min(dialog.getWidth(), stage.getWidth() * 0.95f);
        float clampedHeight = Math.min(dialog.getHeight(), stage.getHeight() * 0.95f);
        dialog.setSize(clampedWidth, clampedHeight);

        // Center on screen
        dialog.setPosition(
            (stage.getWidth() - clampedWidth) / 2f,
            (stage.getHeight() - clampedHeight) / 2f
        );

        stage.addActor(dialog);
        Gdx.input.setInputProcessor(stage);
    }

//    public void showTimedErrorLabel(Stage stage, String message, float durationSeconds) {
//        Skin skin = GameAssetManager.skin;
//
//        Label errorLabel = new Label(message, skin, "custom-label");
//        errorLabel.setAlignment(Align.center);
//        errorLabel.setColor(Color.SCARLET);
//        errorLabel.setFontScale(1.2f);
//
//        // Optional background for visibility
    ////       errorLabel.setBackground(skin.getDrawable("window"));
//
//        float width = Gdx.graphics.getWidth() * 0.4f;
//        float height = Gdx.graphics.getHeight() * 0.15f;
//
//        errorLabel.setSize(width, height);
//        errorLabel.setPosition(
//            (Gdx.graphics.getWidth() - width) / 2f,
//            (Gdx.graphics.getHeight() - height) / 2f
//        );
//
//        stage.addActor(errorLabel);
//
//        // Fade out and remove after delay
//        errorLabel.addAction(Actions.sequence(
//            Actions.delay(durationSeconds),
//            Actions.fadeOut(0.5f),
//            Actions.run(errorLabel::remove)
//        ));
//    }
    public void showTimedErrorLabel(Stage stage, String message, float durationSeconds, Runnable onComplete) {
        Skin skin = GameAssetManager.skin;

        Label errorLabel = new Label(message, skin, "custom-label");
        errorLabel.setAlignment(Align.center);
        errorLabel.setColor(Color.SCARLET);
        errorLabel.setFontScale(1.2f);

        float width = Gdx.graphics.getWidth() * 0.4f;
        float height = Gdx.graphics.getHeight() * 0.15f;

        errorLabel.setSize(width, height);
        errorLabel.setPosition(
            (Gdx.graphics.getWidth() - width) / 2f,
            (Gdx.graphics.getHeight() - height) / 2f
        );

        stage.addActor(errorLabel);

        errorLabel.addAction(Actions.sequence(
            Actions.delay(durationSeconds),
            Actions.fadeOut(0.5f),
            Actions.run(() -> {
                errorLabel.remove();
                if (onComplete != null) {
                    onComplete.run();
                }
            })
        ));
    }
    public void showTimedErrorLabel(Stage stage, String message, float durationSeconds) {
        Skin skin = GameAssetManager.skin;

        Label errorLabel = new Label(message, skin, "custom-label");
        errorLabel.setAlignment(Align.center);
        errorLabel.setColor(Color.RED);
        errorLabel.setFontScale(1.2f);

        // Optional background for visibility
//        errorLabel.setBackground(skin.getDrawable("window"));

        float width = Gdx.graphics.getWidth() * 0.4f;
        float height = Gdx.graphics.getHeight() * 0.15f;

        errorLabel.setSize(width, height);
        errorLabel.setPosition(
            (Gdx.graphics.getWidth() - width) / 2f,
            (Gdx.graphics.getHeight() - height) / 2f
        );

        stage.addActor(errorLabel);

        // Fade out and remove after delay
        errorLabel.addAction(Actions.sequence(
            Actions.delay(durationSeconds),
            Actions.fadeOut(0.5f),
            Actions.run(errorLabel::remove)
        ));
    }

    private void updateEquippedItemSlot() {
        equippedItemSlotTable.clearChildren();

        float slotSize = GameAssetManager.TILE_SIZE;
        float itemImagePadding = slotSize * 0.1f;
        float itemImageRenderSize = slotSize - (itemImagePadding * 2);
        float labelOffset = 5f;

        Stack itemSlotStack = new Stack();

        Image slotBg = new Image(InventoryAssets.slot);
        slotBg.setSize(slotSize, slotSize);
        itemSlotStack.add(slotBg);

        if (equippedItem != null) {
            Texture itemTex = getItemTexture(equippedItem);
            if (itemTex != null) {
                Image itemImage = new Image(itemTex);
                itemImage.setSize(itemImageRenderSize, itemImageRenderSize);
                itemImage.setScaling(Scaling.fit);
                itemImage.setAlign(com.badlogic.gdx.utils.Align.center);

                Container<Image> itemImageContainer = new Container<>(itemImage);
                itemImageContainer.pad(itemImagePadding);
                itemImageContainer.fill();

                itemSlotStack.add(itemImageContainer);
            } else {
                Gdx.app.error("GameView", "Texture for equipped item " + equippedItem.getName() + " is null!");
            }

            Integer count = currentPlayer.getBackpack().getInventoryItems().get(equippedItem);
            if (count != null && count > 1) {
                Label countLabel = new Label(String.valueOf(count), new Label.LabelStyle(smallFont, Color.WHITE));
                Container<Label> labelContainer = new Container<>(countLabel);
                labelContainer.align(com.badlogic.gdx.utils.Align.bottomRight);
                labelContainer.padRight(labelOffset);
                labelContainer.padBottom(labelOffset);
                labelContainer.fill();
                itemSlotStack.add(labelContainer);
            }
        }

        equippedItemSlotTable.add(itemSlotStack).size(slotSize).pad(5);

        equippedItemSlotTable.pack();
        equippedItemSlotTable.setPosition(
            (stage.getWidth() - equippedItemSlotTable.getWidth()) / 2,
            10);
    }

    private void updateFridgeMenuTable() {
        fridgeMenuTable.clearChildren();

        if (currentHouseForFridge == null) {
            return;
        }
        Map<Food, Integer> fridgeItems = currentHouseForFridge.getFridge();
        if (fridgeItems == null) {
            fridgeItems = new HashMap<>();
        }
        List<Item> sortedFridgeItems = new ArrayList<>(fridgeItems.keySet());

        float slotSize = GameAssetManager.TILE_SIZE;
        float itemImagePadding = slotSize * 0.1f;
        float itemImageRenderSize = slotSize - (itemImagePadding * 2);
        float labelOffset = 5f;
        int totalSlots = 24;
        int itemsInFridge = sortedFridgeItems.size();

        for (int i = 0; i < totalSlots; i++) {
            Stack itemSlotStack = new Stack();
            Image slotBg = new Image(InventoryAssets.slot);
            slotBg.setSize(slotSize, slotSize);
            itemSlotStack.add(slotBg);

            if (i == selectedFridgeSlot) {
                Image highlightImage = new Image(InventoryAssets.highlightedSlot);
                highlightImage.setSize(slotSize, slotSize);
                itemSlotStack.add(highlightImage);
            }

            if (i < itemsInFridge) {
                Item item = sortedFridgeItems.get(i);
                Integer count = fridgeItems.get(item);

                Texture itemTex = getItemTexture(item);
                if (itemTex != null) {
                    Image itemImage = new Image(itemTex);
                    itemImage.setSize(itemImageRenderSize, itemImageRenderSize);
                    itemImage.setScaling(Scaling.fit);
                    itemImage.setAlign(Align.center);

                    Container<Image> itemImageContainer = new Container<>(itemImage);
                    itemImageContainer.pad(itemImagePadding);
                    itemImageContainer.fill();
                    itemSlotStack.add(itemImageContainer);
                } else {
                    Gdx.app.error("GameView", "Texture for fridge item " + item.getName() + " is null!");
                }

                if (count != null && count > 1) {
                    Label countLabel = new Label(String.valueOf(count), new Label.LabelStyle(smallFont, Color.WHITE));
                    Container<Label> labelContainer = new Container<>(countLabel);
                    labelContainer.align(Align.bottomRight);
                    labelContainer.padRight(labelOffset);
                    labelContainer.padBottom(labelOffset);
                    labelContainer.fill();
                    itemSlotStack.add(labelContainer);
                }
            }

            fridgeMenuTable.add(itemSlotStack).size(slotSize).pad(5);
            if ((i + 1) % 6 == 0) {
                fridgeMenuTable.row();
            }
        }
        fridgeMenuTable.pack();
    }

    private void handleGrabFromFridge() {
        if (currentHouseForFridge == null) return;

        Map<Food, Integer> fridgeItems = currentHouseForFridge.getFridge();
        List<Item> sortedFridgeItems = new ArrayList<>(fridgeItems.keySet());

        if (selectedFridgeSlot >= 0 && selectedFridgeSlot < sortedFridgeItems.size()) {
            Item itemToGrab = sortedFridgeItems.get(selectedFridgeSlot);
            Result fridgeRemoveResult = controller.pickFoodFromFridge(itemToGrab.getName());
            showErrorDialog(stage, fridgeRemoveResult.message());

            updateFridgeMenuTable();
            updateEquippedItemSlot();
        } else {
            showErrorDialog(stage, "No item selected in fridge to grab.");
        }
    }

    private void handlePutToFridge(Item item) {
        if (currentHouseForFridge == null) return;
        if (item == null) {
            showErrorDialog(stage, "No item selected to put into the fridge.");
            return;
        }

        Result fridgeAddResult = controller.putFoodInFridge(item.getName());
        showErrorDialog(stage, fridgeAddResult.message());

        updateFridgeMenuTable();
        updateEquippedItemSlot();
    }

    private void showSettingsMenu() {
        if (settingsMenuDialog != null && isSettingsMenuCurrentlyVisible) {
            settingsMenuDialog.hide();
            isSettingsMenuCurrentlyVisible = false;
            Gdx.input.setInputProcessor(GameView.this);
            System.out.println("DEBUG: showSettingsMenu toggling off. isSettingsMenuCurrentlyVisible: " + isSettingsMenuCurrentlyVisible); // ADD DEBUG
            return;
        }

        if (settingsMenuDialog == null) {
            settingsMenuDialog = new Dialog("Settings", GameAssetManager.skin, "custom-window");
            settingsMenuDialog.padTop(40);
            settingsMenuDialog.setKeepWithinStage(true);
            settingsMenuDialog.setMovable(false);
            settingsMenuDialog.setModal(true);
            settingsMenuDialog.setResizable(false);

            settingsMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

            settingsMenuTable = new Table(GameAssetManager.skin);
            settingsMenuTable.defaults().pad(10).expandX().fillX();
            settingsMenuDialog.getContentTable().add(settingsMenuTable).expand().fill().row();

            TextButton voteOutButton = new TextButton("Vote Out Players", GameAssetManager.skin, "custom-button");
            voteOutButton.setColor(Color.ORANGE);
            voteOutButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showErrorDialog(stage, "Vote Out Players functionality not yet implemented.");
                    settingsMenuDialog.hide();
                    isSettingsMenuCurrentlyVisible = false;
                }
            });
            settingsMenuTable.add(voteOutButton).height(60).row();

            TextButton exitButtonInSettings = new TextButton("Exit Game", GameAssetManager.skin, "custom-button");
            exitButtonInSettings.setColor(Color.RED);
            exitButtonInSettings.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result result = controller.exitGame();
                    if (!result.isSuccessful()) {
                        showErrorDialog(stage, result.message());
                    } else {
                        if (gameTickTask != null) {
                            gameTickTask.cancel();
                        }
                        MainApp.getInstance().setCurrentGame(null);
                        MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
                        MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
                    }
                    settingsMenuDialog.hide();
                    isSettingsMenuCurrentlyVisible = false;
                }
            });
            settingsMenuTable.add(exitButtonInSettings).height(60).row();

            // Close Button
            TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
            closeButton.setColor(Color.GRAY);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    settingsMenuDialog.hide();
                    isSettingsMenuCurrentlyVisible = false;
                    Gdx.input.setInputProcessor(GameView.this);
                }
            });
            settingsMenuTable.add(closeButton).height(60).padTop(20).row();

            stage.addActor(settingsMenuDialog);
        }

        settingsMenuDialog.show(stage);
        isSettingsMenuCurrentlyVisible = true;
        Gdx.input.setInputProcessor(stage);

        float dialogWidth = Gdx.graphics.getWidth() * 0.4f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.5f;
        settingsMenuDialog.setSize(dialogWidth, dialogHeight);
        settingsMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - dialogWidth) / 2,
            (Gdx.graphics.getHeight() - dialogHeight) / 2
        );

        showInventoryMenu = false;
        if (inventoryMenuTable != null) inventoryMenuTable.setVisible(false);
    }

    private void showCookingMenu() {
        if (cookingMenuDialog != null && !showCookingMenu) {
            cookingMenuDialog.hide();
            Gdx.input.setInputProcessor(GameView.this);
            return;
        }

        if (cookingMenuDialog == null) {
            cookingMenuDialog = new Dialog("Cooking Recipes", GameAssetManager.skin, "custom-window");
            cookingMenuDialog.padTop(80);
            cookingMenuDialog.padRight(40);
            cookingMenuDialog.setKeepWithinStage(true);
            cookingMenuDialog.setMovable(false);
            cookingMenuDialog.setModal(true);
            cookingMenuDialog.setResizable(false);
            cookingMenuDialog.setVisible(false);

            cookingMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

            cookingMenuTable = new Table(GameAssetManager.skin);
            cookingMenuTable.top().defaults().pad(5).expandX().fillX();

            ScrollPane scrollPane = new ScrollPane(cookingMenuTable, GameAssetManager.skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollingDisabled(true, false);

            cookingMenuDialog.getContentTable().add(scrollPane).expand().fill().row();

            TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
            closeButton.setColor(Color.RED);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    cookingMenuDialog.hide();
                    showCookingMenu = false;
                    Gdx.input.setInputProcessor(GameView.this);
                }
            });
            cookingMenuDialog.getContentTable().add(closeButton).width(150).height(50).pad(10).row();

            cookingMenuDialog.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!cookingMenuDialog.isVisible()) {
                        Gdx.input.setInputProcessor(GameView.this);
                    }
                }
            });

            stage.addActor(cookingMenuDialog);
        }

        if (showInventoryMenu) { showInventoryMenu = false; if(inventoryMenuTable != null) inventoryMenuTable.setVisible(false); }
        if (showBackpackMenu) { showBackpackMenu = false; if(backpackMenuTable != null) backpackMenuTable.setVisible(false); }

        cookingMenuDialog.setVisible(true);
        cookingMenuDialog.show(stage);
        Gdx.input.setInputProcessor(stage);

        float dialogWidth = Gdx.graphics.getWidth() * 0.5f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.7f;
        cookingMenuDialog.setSize(dialogWidth, dialogHeight);
        cookingMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - dialogWidth) / 2,
            (Gdx.graphics.getHeight() - dialogHeight) / 2
        );

        updateCookingMenuTable();

    }


    private void updateCookingMenuTable() {
        cookingMenuTable.clearChildren();

        List<FoodRecipe> unlockedRecipes = currentPlayer.getCookingRecepies();

        if (unlockedRecipes == null || unlockedRecipes.isEmpty()) {
            cookingMenuTable.add(new Label("No recipes unlocked yet!", GameAssetManager.skin, "custom-label")).colspan(1).center().pad(20).row();
            return;
        }

        float iconSize = GameAssetManager.TILE_SIZE;
        Label.LabelStyle labelStyle = GameAssetManager.skin.get("custom-label", Label.LabelStyle.class);
        labelStyle.font.getData().setScale(0.8f);

        for (final FoodRecipe recipe : unlockedRecipes) {
            Table recipeRow = new Table();
            recipeRow.defaults().pad(5);

            FoodType food = FoodType.foodTypeGetterFromRecipe(recipe);
            Label foodNameLabel = new Label(food.getName(), labelStyle);
            recipeRow.padLeft(40f);
            recipeRow.padRight(40f);
            recipeRow.add(foodNameLabel).center();

            Texture foodTexture = food.getTexture();
            if (foodTexture != null) {
                Image foodImage = new Image(foodTexture);
                foodImage.setScaling(Scaling.fit);
                recipeRow.add(foodImage).size(iconSize, iconSize).align(Align.right).row();
            } else {
                Gdx.app.error("GameView", "Texture for food recipe " + recipe.name() + " is null!");
                recipeRow.add(new Label("?", labelStyle)).size(iconSize, iconSize).align(Align.right).row();
            }

            Table ingredientsTable = new Table();
            ingredientsTable.defaults().align(Align.left);
            StringBuilder ingredientsText = new StringBuilder("Ingredients: ");
            Map<String, Integer> ingredients = recipe.getRecipe();
            if (ingredients != null && !ingredients.isEmpty()) {
                boolean first = true;
                for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
                    if (!first) {
                        ingredientsText.append(", ");
                    }
                    ingredientsText.append(entry.getKey()).append(" :").append(entry.getValue());
                    first = false;
                }
            } else {
                ingredientsText.append("None");
            }
            Label ingredientsLabel = new Label(ingredientsText.toString(), labelStyle);
            ingredientsLabel.setWrap(true);
            ingredientsTable.add(ingredientsLabel).width(cookingMenuDialog.getWidth() * 0.4f);

            recipeRow.add(ingredientsTable).colspan(2).expandX().fillX().align(Align.left).row();

            TextButton cookButton = new TextButton("Cook", GameAssetManager.skin, "custom-button");
            cookButton.getLabel().setStyle(labelStyle);
            cookButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result cookResult = controller.cook(food.getName());
                    showErrorDialog(stage, cookResult.message());
                    cookingMenuDialog.hide();
                    showCookingMenu = false;
                    Gdx.input.setInputProcessor(GameView.this);
                    updateCookingMenuTable();
                }
            });
            recipeRow.add(cookButton).width(iconSize * 2).height(iconSize).colspan(2).center().row();

            cookingMenuTable.add(recipeRow).expandX().fillX().row();
        }
    }

    private void showBuildingMenu() {
        if (buildingMenuDialog != null && !showBuildingMenu) {
            buildingMenuDialog.hide();
            Gdx.input.setInputProcessor(GameView.this);
            return;
        }

        if (buildingMenuDialog == null) {
            buildingMenuDialog = new Dialog("Crafting Recipes", GameAssetManager.skin, "custom-window");
            buildingMenuDialog.padTop(80);
            buildingMenuDialog.padRight(40);
            buildingMenuDialog.setKeepWithinStage(true);
            buildingMenuDialog.setMovable(false);
            buildingMenuDialog.setModal(true);
            buildingMenuDialog.setResizable(false);
            buildingMenuDialog.setVisible(false);

            buildingMenuDialog.setBackground(new TextureRegionDrawable(InventoryAssets.inventoryMenuBackground));

            buildingMenuTable = new Table(GameAssetManager.skin);
            buildingMenuTable.top().defaults().pad(5).expandX().fillX();

            ScrollPane scrollPane = new ScrollPane(buildingMenuTable, GameAssetManager.skin);
            scrollPane.setFadeScrollBars(false);
            scrollPane.setScrollingDisabled(true, false);

            buildingMenuDialog.getContentTable().add(scrollPane).expand().fill().row();

            TextButton closeButton = new TextButton("Close", GameAssetManager.skin, "custom-button");
            closeButton.setColor(Color.RED);
            closeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    buildingMenuDialog.hide();
                    showBuildingMenu = false;
                    Gdx.input.setInputProcessor(GameView.this);
                }
            });
            buildingMenuDialog.getContentTable().add(closeButton).width(150).height(50).pad(10).row();

            buildingMenuDialog.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!buildingMenuDialog.isVisible()) {
                        Gdx.input.setInputProcessor(GameView.this);
                    }
                }
            });

            stage.addActor(buildingMenuDialog);
        }

        if (showInventoryMenu) { showInventoryMenu = false; if(inventoryMenuTable != null) inventoryMenuTable.setVisible(false); }
        if (showBackpackMenu) { showBackpackMenu = false; if(backpackMenuTable != null) backpackMenuTable.setVisible(false); }

        buildingMenuDialog.setVisible(true);
        buildingMenuDialog.show(stage);
        Gdx.input.setInputProcessor(stage);

        float dialogWidth = Gdx.graphics.getWidth() * 0.5f;
        float dialogHeight = Gdx.graphics.getHeight() * 0.7f;
        buildingMenuDialog.setSize(dialogWidth, dialogHeight);
        buildingMenuDialog.setPosition(
            (Gdx.graphics.getWidth() - dialogWidth) / 2,
            (Gdx.graphics.getHeight() - dialogHeight) / 2
        );

        updateBuildingMenuTable();

    }


    private void updateBuildingMenuTable() {
        buildingMenuTable.clearChildren();

        List<MachineType> unlockedRecipes = currentPlayer.getMachineRecepies();

        if (unlockedRecipes == null || unlockedRecipes.isEmpty()) {
            buildingMenuTable.add(new Label("No recipes unlocked yet!", GameAssetManager.skin, "custom-label")).colspan(1).center().pad(20).row();
            return;
        }

        float iconSize = GameAssetManager.TILE_SIZE;
        Label.LabelStyle labelStyle = GameAssetManager.skin.get("custom-label", Label.LabelStyle.class);
        labelStyle.font.getData().setScale(0.8f);

        for (final MachineType recipe : unlockedRecipes) {
            Table recipeRow = new Table();
            recipeRow.defaults().pad(5);

            Label machineNameLabel = new Label(recipe.getName(), labelStyle);
            recipeRow.padLeft(40f);
            recipeRow.padRight(40f);
            recipeRow.add(machineNameLabel).center();

            Texture machineTexture = recipe.getTexture();
            if (machineTexture != null) {
                Image machineImage = new Image(machineTexture);
                machineImage.setScaling(Scaling.fit);
                recipeRow.add(machineImage).size(iconSize, iconSize).align(Align.right).row();
            } else {
                Gdx.app.error("GameView", "Texture for machine recipe " + recipe.name() + " is null!");
                recipeRow.add(new Label("?", labelStyle)).size(iconSize, iconSize).align(Align.right).row();
            }

            Table ingredientsTable = new Table();
            ingredientsTable.defaults().align(Align.left);
            StringBuilder ingredientsText = new StringBuilder("Ingredients: ");
            Map<String, Integer> ingredients = recipe.getRecipe();
            if (ingredients != null && !ingredients.isEmpty()) {
                boolean first = true;
                for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
                    if (!first) {
                        ingredientsText.append(", ");
                    }
                    ingredientsText.append(entry.getKey()).append(" :").append(entry.getValue());
                    first = false;
                }
            } else {
                ingredientsText.append("None");
            }
            Label ingredientsLabel = new Label(ingredientsText.toString(), labelStyle);
            ingredientsLabel.setWrap(true);
            ingredientsTable.add(ingredientsLabel).width(buildingMenuDialog.getWidth() * 0.4f);

            recipeRow.add(ingredientsTable).colspan(2).expandX().fillX().align(Align.left).row();

            TextButton craftButton = new TextButton("Craft", GameAssetManager.skin, "custom-button");
            craftButton.getLabel().setStyle(labelStyle);
            craftButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Result craftResult = controller.craftMachine(recipe);
                    showErrorDialog(stage, craftResult.message());
                    buildingMenuDialog.hide();
                    showBuildingMenu = false;
                    Gdx.input.setInputProcessor(GameView.this);
                    updateBuildingMenuTable();
                }
            });
            recipeRow.add(craftButton).width(iconSize * 2).height(iconSize).colspan(2).center().row();

            buildingMenuTable.add(recipeRow).expandX().fillX().row();
        }
    }

    private void updateNPCMovement(float delta) {
        for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
            npc.updateMovement(delta);
        }
    }

    private void drawNPCs(int rows, int tileSize) {
        for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
            if (npc.currentTileGetter() == null) continue;

            float x, y;

            if (npc.isMoving()) {
                Tile from = npc.getMovingFrom();
                Tile to = npc.getMovingTo();
                float p = npc.getMoveProgress();

                x = MathUtils.lerp(from.getX(), to.getX(), p) * tileSize;
                y = MathUtils.lerp(
                    rows - from.getY() - 1,
                    rows - to.getY() - 1,
                    p
                ) * tileSize;
            } else {
                x = npc.currentTileGetter().getX() * tileSize;
                y = (rows - npc.currentTileGetter().getY() - 1) * tileSize;
            }

            batch.draw(npc.getNpcName().getTextureRegion(), x, y, tileSize, tileSize * 2f);
        }
    }

    public void showDisconnectedDialog(String username) {
        Dialog dialog = new Dialog("Disconnected", GameAssetManager.skin, "dialog") {
            @Override
            protected void result(Object object) {
                // در صورت نیاز کاری انجام بده
            }
        };
        dialog.text(username + " has been disconnected. Waiting for reconnection...");
        dialog.button("OK");
        dialog.show(stage);  // فرض بر اینکه `stage` داخل GameView هست
    }

    public void updateLeaderboard(List<Map<String, Object>> leaderboard) {
        // پاک کردن ردیف‌های قبلی، ولی نگه‌داشتن عنوان ستون:
        leaderboardTable.clearChildren();
        // دوباره عنوان ستون
        leaderboardTable.add("Rank").pad(5);
        leaderboardTable.add("Player").pad(5);
        leaderboardTable.add("Money").pad(5);
        leaderboardTable.add("Skills").pad(5);
        leaderboardTable.add("Missions").pad(5);
        leaderboardTable.add("Score").pad(5);
        leaderboardTable.row();

        for (int i = 0; i < leaderboard.size(); i++) {
            Map<String, Object> entry = leaderboard.get(i);
            String username = (String) entry.get("username");
            int money      = ((Number)entry.get("money")).intValue();
            int skillSum   = ((Number)entry.get("skillSum")).intValue();
            int missions   = ((Number)entry.get("missions")).intValue();
            int score      = ((Number)entry.get("score")).intValue();

            leaderboardTable.add(String.valueOf(i+1)).pad(5);
            leaderboardTable.add(username).pad(5);
            leaderboardTable.add(String.valueOf(money)).pad(5);
            leaderboardTable.add(String.valueOf(skillSum)).pad(5);
            leaderboardTable.add(String.valueOf(missions)).pad(5);
            leaderboardTable.add(String.valueOf(score)).pad(5);
            leaderboardTable.row();
        }
    }



}
