package org.example.Model;


import com.google.gson.Gson;
import org.example.Model.Menus.Menu;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static App instance;
    private ArrayList<Game> games = new ArrayList<>();
    private Game currentGame;
    private ArrayList<User> users = UserDatabase.loadUsers();
    private Menu currentMenu= Menu.LoginMenu;
    private User loggedInUser= loadLoggedInUser();// instead of null

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
// print options for user to choose

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    private App(){};

    public static App getInstance(){
        if(instance == null){
            instance = new App();
        }
        return instance;
    }





    public ArrayList<User> getUsers() { return users; }
    public User getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(User loggedInUser) { this.loggedInUser = loggedInUser; }
    public void setUsers(ArrayList<User> users) { this.users = users; }

    public Game getCurrentGame() {return currentGame;}

    public void setGames(ArrayList<Game> games) { this.games = games; }
    public void setCurrentGame(Game currentGame) { this.currentGame = currentGame; }
    public void setCurrentMenu(Menu currentMenu) { this.currentMenu = currentMenu; }
    public void getCurrentGame(Game currentGame) { currentGame = currentGame; }
    public void getCurrentMenu(Menu currentMenu) { currentMenu = currentMenu; }
    public void getGames(ArrayList<Game> games) { this.games = games; }

    public List<String> getSecurityQuestions() {
        return securityQuestions;
    }
}
