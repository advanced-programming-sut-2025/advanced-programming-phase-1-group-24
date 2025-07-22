package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApp {

    private static final ServerApp instance = new ServerApp();

    private final ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<GameServer> allGames = new CopyOnWriteArrayList<>();

    private ServerApp() {
        loadAllUsers();
    }

    public static ServerApp getInstance() {
        return instance;
    }
    public User getUserByUsername(String username) {
        for(User user : allUsers.values()) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    private void loadAllUsers() {
        ArrayList<User> loadedUsers = UserDatabase.loadUsers();
        for (User user : loadedUsers) {
            allUsers.put(user.getUsername(), user);  // assuming getUsername() exists
        }
    }


    // Optionally, call this when you want to persist
    public void saveUsers() {
        UserDatabase.saveUsers(new ArrayList<>(allUsers.values()));
    }
    // ==== USER MANAGEMENT ====
    public void addUser(User user) {
        allUsers.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return allUsers.get(username);
    }

    public boolean userExists(String username) {
        return allUsers.containsKey(username);
    }

    public ConcurrentHashMap<String, User> getAllUsers() {
        return allUsers;
    }

    // ==== GAME MANAGEMENT ====
    public void addGame(GameServer game) {
        allGames.add(game);
    }

    public void removeGame(GameServer game) {
        allGames.remove(game);
    }

    public GameServer getGameById(String gameId) {
        for (GameServer g : allGames) {
            if (g.getGame().getNetworkId().equals(gameId)) {
                return g;
            }
        }
        return null;
    }

    public CopyOnWriteArrayList<GameServer> getAllGames() {
        return allGames;
    }
}
