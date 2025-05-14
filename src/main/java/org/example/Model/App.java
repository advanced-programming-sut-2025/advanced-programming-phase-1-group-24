package org.example.Model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Model.Menus.Menu;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static App instance;
    private ArrayList<Game> activeGames = loadActiveGames(); // Instead of new ArrayList<>()
    private Game currentGame;
    private ArrayList<User> users = UserDatabase.loadUsers();
    private Menu currentMenu = Menu.LoginMenu;
    private User loggedInUser = loadLoggedInUser();// instead of null

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

    private ArrayList<Game> loadActiveGames() {
        File file = new File("data/active_games.json");
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Game>>() {
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

    private App() {
    }

    ;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
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

    public Game getCurrentGame() {
        return currentGame;
    }

    public ArrayList<Game> getActiveGames() {
        return activeGames;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void getCurrentGame(Game currentGame) {
        currentGame = currentGame;
    }

    public void getCurrentMenu(Menu currentMenu) {
        currentMenu = currentMenu;
    }

    public List<String> getSecurityQuestions() {
        return securityQuestions;
    }

    public Game getGameByUser(User user) {
        for (Game game : activeGames) {
            if (game.hasUser(user)) return game;
        }
        return null;
    }

    public void setSecurityQuestions(List<String> securityQuestions) {
        this.securityQuestions = securityQuestions;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }
}
