package io.github.stardew.mini;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;
import io.github.stardew.mini.View.GameMenu;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends com.badlogic.gdx.Game {
    // Game instance (LibGDX-style singleton)
    private static MainApp instance;
    private ArrayList<io.github.stardew.mini.Model.Game> activeGames ; // Instead of new ArrayList<>()
    private io.github.stardew.mini.Model.Game currentGame;
    private ArrayList<User> users ;
    private Menu currentMenu = Menu.GameMenu;
    private User loggedInUser ;// instead of null

    @Override
    public void create() {
        instance = this;

        // Initialize game data
        loadGameData();

        // Set initial screen
        getInstance().setScreen(new GameMenu(this));
    }

    private void loadGameData() {
        // LibGDX file handling
        FileHandle usersFile = Gdx.files.local("data/users.json");
        FileHandle gamesFile = Gdx.files.local("data/active_games.json");
        FileHandle loggedInUserFile = Gdx.files.local("data/logged_in_user.json");

        Json json = new Json();

        // Load users
        if (usersFile.exists()) {
            users = json.fromJson(ArrayList.class, User.class, usersFile);
        } else {
            users = new ArrayList<>();
        }

        // Load active games
        if (gamesFile.exists()) {
            activeGames = json.fromJson(ArrayList.class, io.github.stardew.mini.Model.Game.class, gamesFile);
        } else {
            activeGames = new ArrayList<>();
        }

        // Load logged in user
        if (loggedInUserFile.exists()) {
            loggedInUser = json.fromJson(User.class, loggedInUserFile);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        saveGameData();
    }

    private void saveGameData() {
        Json json = new Json();
        json.setUsePrototypes(false); // For proper serialization

        // Save users
        Gdx.files.local("data/users.json")
            .writeString(json.prettyPrint(users), false);

        // Save active games
        Gdx.files.local("data/active_games.json")
            .writeString(json.prettyPrint(activeGames), false);

        // Save logged in user
        if (loggedInUser != null) {
            Gdx.files.local("data/logged_in_user.json")
                .writeString(json.toJson(loggedInUser), false);
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

}
