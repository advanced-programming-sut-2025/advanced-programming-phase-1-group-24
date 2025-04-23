package org.example.Model;

import org.example.Model.Menus.Menu;

import java.util.ArrayList;

public class App {
    private static App instance;
    private ArrayList<Game> games = new ArrayList<>();
    private Game currentGame;

    private Menu currentMenu;

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

    private ArrayList<User> users = new ArrayList<>();

    private User loggedInUser;

    public ArrayList<User> getUsers() { return users; }
    public User getLoggedInUser() { return loggedInUser; }
    public void setLoggedInUser(User loggedInUser) { this.loggedInUser = loggedInUser; }
    public void getUsers(ArrayList<User> users) { this.users = users; }
    public void setUsers(ArrayList<User> users) { this.users = users; }

    public void setGames(ArrayList<Game> games) { this.games = games; }
    public void setCurrentGame(Game currentGame) { this.currentGame = currentGame; }
    public void setCurrentMenu(Menu currentMenu) { this.currentMenu = currentMenu; }
    public Game getCurrentGame() {
        return currentGame;
    }
    public void getGames(ArrayList<Game> games) { this.games = games; }

}
