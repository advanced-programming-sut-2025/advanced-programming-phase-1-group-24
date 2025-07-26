package io.github.stardew.mini.client;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplate;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.Things.*;
import io.github.stardew.mini.server.Controller.*;
import com.google.gson.JsonObject;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.Things.FoodType;
import io.github.stardew.mini.server.Controller.*;
import io.github.stardew.mini.Model.Animals.AnimalProductType;
import io.github.stardew.mini.client.Assets.CropAssets;
import io.github.stardew.mini.Model.Animals.AnimalType;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.Assets.ShopAssets;
import io.github.stardew.mini.client.Assets.InventoryAssets;
import io.github.stardew.mini.client.Assets.TreeAssets;
import io.github.stardew.mini.Model.Growables.*;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.NPCManagement.NPCtype;
import io.github.stardew.mini.Model.Places.Habitat;
import io.github.stardew.mini.Model.Reccepies.MachineType;
import io.github.stardew.mini.Model.Reccepies.randomStuffType;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;
import io.github.stardew.mini.client.View.*;
import io.github.stardew.mini.server.ServerApp;
import io.github.stardew.mini.client.View.*;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class MainApp extends com.badlogic.gdx.Game {
    // Game instance (LibGDX-style singleton)
    private static MainApp instance;
    private static SpriteBatch batch;
    private ArrayList<io.github.stardew.mini.Model.Game> activeGames = new ArrayList<>(); // Instead of new ArrayList<>()
    private io.github.stardew.mini.Model.Game currentGame;
    private GameView currentGameView;
    private ArrayList<User> users;
    //UserDatabase.loadUsers(); // we should delete this
    private Menu currentMenu = Menu.GameMenu;
    private User loggedInUser = loadLoggedInUser();// instead of null
    private NetworkClient networkClient;
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setCurrentGameId(String gameId) {
        currentGame.setNetworkId(gameId);
    }

    @Override
    public void create() {
        instance = this;
        batch = new SpriteBatch();
        GameAssetManager.load();
        users = UserDatabase.loadUsers();
        if (users == null) {
            users = new ArrayList<>();
        }
        connectToServer();
        //loggedInUser = new User("nikki", "1234", "nik", "aa", true);
        setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.skin));
        if (loggedInUser == null) {
            setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.skin));
        } else
            setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
        //setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));

//       // Initialize game data
       //loadGameData();
        TileType.initTextures();
        AnimalType.initTextures();
        TreeAssets.load();
        CropAssets.load();
        InventoryAssets.load();
        TileType.initTextures();
        Habitat.HabitatType.initTextures();
        ShopAssets.load();
        for (TreeType treeType : TreeType.values()) {
            treeType.initTextures();
        }
        for (FruitType fruitType : FruitType.values()) {
            fruitType.initTexture();
        }
        for (SourceType sourceType : SourceType.values()) {
            sourceType.initTexture();
        }
        for (ForagingCropType foragingCropType : ForagingCropType.values()) {
            foragingCropType.initTexture();
        }
        for (CropType cropType : CropType.values()) {
            if (cropType == CropType.MixedCrop) continue;
            cropType.initTexture();
        }
        for (ForagingMineralType foragingMineralType : ForagingMineralType.values()) {
            foragingMineralType.initTexture();
        }
        for (MachineType machineType : MachineType.values()) {
            machineType.initTexture();
        }
        for (io.github.stardew.mini.Model.Reccepies.randomStuffType randomStuffType : randomStuffType.values()) {
            randomStuffType.initTexture();
        }
        for (AnimalProductType animalProductType : AnimalProductType.values()) {
            animalProductType.initTexture();
        }
        for (FoodType foodType : FoodType.values()) {
            foodType.initTexture();
        }
        for (FishType fishType : FishType.values()) {
            fishType.initTexture();
        }
        for (NPCtype npCtype : NPCtype.values()) {
            npCtype.initTexture();
        }
        // Initialize game data
//        activeGames = loadActiveGames();
        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates();
        }
    }


    public void connectToServer() {
        try {
            URI serverUri = new URI("ws://localhost:8080/ws"); // Make sure port matches AppSocket server
            networkClient = new NetworkClient(serverUri);
            networkClient.connect();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (networkClient != null && networkClient.isOpen()) {
                    try {
                        networkClient.close();
                        System.out.println("WebSocket closed via shutdown hook.");
                    } catch (Exception e) {
                        System.err.println("Error in shutdown hook: " + e.getMessage());
                    }
                }
            }));


            // Wait for WebSocket to open and then send the connect message
            new Thread(() -> {
                while (!networkClient.isOpen()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                }

                if (loggedInUser != null) {
                    Message<String> connectMsg = new Message<>(200);
                    connectMsg.setType("connect"); // AppSocket listens for "connect"
                    connectMsg.setUsername(loggedInUser.getUsername());
                    connectMsg.setMessageType(Message.MessageType.REQUEST);

                    String json = new Gson().toJson(connectMsg);
                    networkClient.send(json); // Sends to AppSocket
                    System.out.println("Connect message sent to server for user: " + loggedInUser.getUsername());
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to WebSocket server.");
        }
    }

    public NetworkClient getNetworkClient() {
        return networkClient;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        GameAssetManager.dispose();
        TreeAssets.dispose();
        CropAssets.dispose();
        InventoryAssets.dispose();
        ShopAssets.dispose();
        batch.dispose();
        // save games
//        if (currentGame != null) {
//            currentGame.getMap().getShops().clear();
//        }
        //saveActiveGames();
        // ✅ Gracefully close WebSocket
        if (networkClient != null && networkClient.isOpen()) {
            try {
                networkClient.close();  // This triggers server's onClose
                System.out.println("WebSocket connection closed gracefully.");
            } catch (Exception e) {
                System.err.println("Error while closing WebSocket: " + e.getMessage());
            }
        }
    }
////////////////////////////////////////saving with .json : just replace .json.gz with .json //////////////////////
    public void saveActiveGames() {
        try {
            GameSaver.saveGames(activeGames, "data/active_games.json.gz");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ArrayList<io.github.stardew.mini.Model.Game> loadActiveGames() {
        File file = new File("data/active_games.json.gz");
        if (!file.exists()) return new ArrayList<>();

        try {
            List<io.github.stardew.mini.Model.Game> list = GameSaver.loadGames(file.getPath());
            return new ArrayList<>(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private User loadLoggedInUser() {
        File file = new File("data/logged_in_user.json");
        if (!file.exists()) return null;
        try (Reader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, User.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> securityQuestions = List.of(
        "What is your favorite food?",
        "What is your first pet's name?",
        "What city were you born in?",
        "Amoo chand salete?",
        "Riazi 2 to chand shodi?",
        "In my little pony what is appleJack's pet name?",
        "how many times did SpongeBob take the driving test?"
    );

    public CompletableFuture<Message<String>> wsLogin(String user, String pass) {
        return networkClient.login(user, pass);
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    private MainApp() {
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    public User getUserByUsername(String username) {
        for (User user : getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }


    public ArrayList<User> getUsers() {
        return users;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public io.github.stardew.mini.Model.Game getCurrentGame() {
        return currentGame;
    }

    public ArrayList<io.github.stardew.mini.Model.Game> getActiveGames() {
        return activeGames;
    }

    public void setCurrentGame(io.github.stardew.mini.Model.Game currentGame) {
        this.currentGame = currentGame;
    }

    public void setSecurityQuestions(List<String> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
        changeScreen();
    }
    public void changeScreen() {
        switch (currentMenu) {
            case GameMenu:
                getInstance().setScreen(new GameView(new GameController()));
                break;
            case MainMenu:
                getInstance().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
                break;
            case PreGameMenu:
                getInstance().setScreen(new PreGameMenuView(new PreGameMenuController()));
                break;
            case NewGameMenu:
                getInstance().setScreen(new NewGameMenuView(new NewGameMenuController()));
                break;
            case MapSelectionMenu:
                getInstance().setScreen(new MapSelectionMenuView(new MapSelectionMenuController()));
                break;
            case LobbyMenu:
                getInstance().setScreen(new LobbyMenuView(new LobbyMenuController()));
                break;


            // ... other cases
        }
    }

    public void getCurrentGame(io.github.stardew.mini.Model.Game currentGame) {
        currentGame = currentGame;
    }

    public void getCurrentMenu(Menu currentMenu) {
        currentMenu = currentMenu;
    }

    public List<String> getSecurityQuestions() {
        return securityQuestions;
    }

    public io.github.stardew.mini.Model.Game getGameByUser(User user) {
        for (io.github.stardew.mini.Model.Game game : activeGames) {
            if (game.hasUser(user)) return game;
        }
        return null;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    public GameView getCurrentGameView() {
        return currentGameView;
    }

    public void setCurrentGameView(GameView currentGameView) {
        this.currentGameView = currentGameView;
    }
}
