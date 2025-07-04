package io.github.stardew.mini;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.stardew.mini.Controller.GameMenuController;
import io.github.stardew.mini.Model.Assets.CropAssets;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Assets.TreeAssets;
import io.github.stardew.mini.Model.FriendshipLevels;
import io.github.stardew.mini.Model.Growables.*;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Things.ForagingMineral;
import io.github.stardew.mini.Model.Things.ForagingMineralType;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;
import io.github.stardew.mini.View.GameView;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainApp extends com.badlogic.gdx.Game {
    private static MainApp instance;
    private ArrayList<io.github.stardew.mini.Model.Game> activeGames = loadActiveGames(); // Instead of new ArrayList<>()
    private io.github.stardew.mini.Model.Game currentGame;
    private ArrayList<User> users = UserDatabase.loadUsers();
    private Menu currentMenu = Menu.GameMenu;
    private User loggedInUser = null;// instead of null
    private static SpriteBatch batch;

    @Override
    public void create() {
        instance = this;
        batch = new SpriteBatch();
//        // Initialize game data
       //loadGameData();
        GameAssetManager.load();
        TreeAssets.load();
        CropAssets.load();
        TileType.initTextures();
        for (TreeType treeType : TreeType.values()) {
            treeType.initTextures();
        }
        for(FruitType fruitType : FruitType.values()) {
            fruitType.initTexture();
        }
        for(SourceType sourceType : SourceType.values()) {
            sourceType.initTexture();
        }
        for(ForagingCropType foragingCropType : ForagingCropType.values()) {
            foragingCropType.initTexture();
        }
        for(CropType cropType : CropType.values()) {
            cropType.initTexture();
        }
        for(ForagingMineralType foragingMineralType : ForagingMineralType.values()) {
            foragingMineralType.initTexture();
        }
        User logged = getUserByUsername("user208");
        setLoggedInUser(logged);
        GameMenuController controller = new GameMenuController();
        controller.createGame("user207 user206", new Scanner(System.in));
        setScreen(new GameView(controller));

        // Set initial screen
        //getInstance().setScreen(new LoginView(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        GameAssetManager.dispose();
        TreeAssets.dispose();
        CropAssets.dispose();
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

    private ArrayList<io.github.stardew.mini.Model.Game> loadActiveGames() {
        File file = new File("data/active_games.json");
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<io.github.stardew.mini.Model.Game>>() {
            }.getType();
            return new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveActiveGames() {
        File file = new File("data/active_games.json");
        try (Writer writer = new FileWriter(file)) {
            new Gson().toJson(activeGames, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Menu getCurrentMenu() {
        return currentMenu;
    }

    private MainApp() {
    }

    ;

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
        switch(currentMenu) {
            case GameMenu:
                //getInstance().setScreen(new GameMenuView(new GameMenuController()));
                break;
            case MainMenu:
                //getInstance().setScreen(new MainMenuScreen(this));
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
}


