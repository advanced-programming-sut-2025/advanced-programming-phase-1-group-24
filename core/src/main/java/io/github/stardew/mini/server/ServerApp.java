package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.Model.UserDatabase;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerApp {

    private static final ServerApp instance = new ServerApp();

    private final ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Game> allGames = new ConcurrentHashMap<>();
   // private final CopyOnWriteArrayList<GameServer> allActiveGames = new CopyOnWriteArrayList<>();


    private ServerApp() {
        loadAllUsers();
        loadAllGames();
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
        List<User> loadedUsers = UserDatabase.loadUsers();
        if (loadedUsers == null) return;
        for (User user : loadedUsers) {
            allUsers.put(user.getUsername(), user);
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

    public void saveAllGames() {
        try {
            List<Game> gamesToSave = new ArrayList<>(allGames.values());
            GameSaver.saveGames(gamesToSave, "data/active_games.json.gz");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllGames() {
        File file = new File("data/active_games.json.gz");
        if (!file.exists()) return;

        try {
            List<Game> loadedGames = GameSaver.loadGames(file.getPath());
            for (Game g : loadedGames) {
                allGames.put(g.getNetworkId(), g); // Make sure Game has a getNetworkId() method
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, Game> getAllGames() {
        return allGames;
    }


    public Game getGameById(String gameId) {
        return allGames.get(gameId);
    }

//    public GameServer getOrStartGameServer(String gameId) {
//        for (GameServer gs : allActiveGames) {
//            if (gs.getGame().getNetworkId().equals(gameId)) {
//                return gs;
//            }
//        }
//
//        Game game = allGames.get(gameId);
//        if (game == null) return null;
//
//        GameServer newServer = new GameServer(game); // requires constructor: GameServer(Game)
//        newServer.start();
//        allActiveGames.add(newServer);
//        return newServer;
//    }
public void addGame(Game game) {
    if (game != null && game.getNetworkId() != null) {
        allGames.put(game.getNetworkId(), game);
    } else {
        System.err.println("Failed to add game: Game or NetworkId is null.");
    }
}

}
