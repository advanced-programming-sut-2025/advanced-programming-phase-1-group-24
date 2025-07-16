package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.User;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApp {

    private static final ServerApp instance = new ServerApp();

    private final ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<GameServer> allGames = new CopyOnWriteArrayList<>();
//    List<User> loadedUsers = UserDatabase.loadUsers();
//for (User user : loadedUsers) {
//        UserManager.registerUser(user);
//    }


    private ServerApp() {
        // private constructor for singleton
    }

    public static ServerApp getInstance() {
        return instance;
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
