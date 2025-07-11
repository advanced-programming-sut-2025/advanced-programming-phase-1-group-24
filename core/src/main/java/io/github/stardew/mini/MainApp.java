package io.github.stardew.mini;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.stardew.mini.Controller.*;
import io.github.stardew.mini.Controller.GameController;
import io.github.stardew.mini.Model.Animals.AnimalProductType;
import io.github.stardew.mini.Model.Assets.CropAssets;
import io.github.stardew.mini.Model.Animals.AnimalType;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.ShopAssets;
import io.github.stardew.mini.Model.Assets.InventoryAssets;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Growables.*;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Places.Habitat;
import io.github.stardew.mini.Model.Reccepies.MachineType;
import io.github.stardew.mini.Model.Reccepies.randomStuffType;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.Things.ForagingMineralType;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;
import io.github.stardew.mini.View.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends com.badlogic.gdx.Game {
    // Game instance (LibGDX-style singleton)
    private static MainApp instance;
    private static SpriteBatch batch;
    private ArrayList<io.github.stardew.mini.Model.Game> activeGames; // Instead of new ArrayList<>()
    private io.github.stardew.mini.Model.Game currentGame;
    private GameView currentGameView;
    private ArrayList<User> users = UserDatabase.loadUsers();
    private Menu currentMenu = Menu.GameMenu;
    private User loggedInUser = loadLoggedInUser();// instead of null


    @Override
    public void create() {
        instance = this;
        batch = new SpriteBatch();
        GameAssetManager.load();
        setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.skin));
        if (loggedInUser == null) {
            setScreen(new SignupMenuView(new SignupMenuController(), GameAssetManager.skin));
        } else
            setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));

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
        // Initialize game data
        activeGames = loadActiveGames();
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
        if( currentGame!=null ) {
            currentGame.getMap().getShops().clear();
        }
        saveActiveGames();

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
/// /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
