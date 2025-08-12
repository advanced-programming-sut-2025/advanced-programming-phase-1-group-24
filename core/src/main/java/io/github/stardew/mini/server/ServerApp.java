package io.github.stardew.mini.server;

import com.google.gson.Gson;
import io.github.stardew.mini.common.Model.Game;
import io.github.stardew.mini.common.Model.SaveGame.GameSaver;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.stardew.mini.common.Model.User;
import io.github.stardew.mini.common.Model.UserDatabase;
import io.github.stardew.mini.common.Model.UserDatabaseSQL;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.io.File;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import java.util.zip.GZIPInputStream;

public class ServerApp {

    private static final ServerApp instance = new ServerApp();
    private RedissonClient redissonClient;

    private final ConcurrentHashMap<String, User> allUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Game> allGames = new ConcurrentHashMap<>();
   private final RadioService radioService = new RadioService();

    private final Gson gson = new Gson();
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
       //ArrayList<User> loadedUsers = UserDatabase.loadUsers();
        ///    //////////////////////////////////////////////////////
        ArrayList<User> loadedUsers = UserDatabaseSQL.loadUsers();
        ///////////////////////////////////////////////////////////////////////
        if (loadedUsers == null) return;
        for (User user : loadedUsers) {
            user.updateGameFields();
            allUsers.put(user.getUsername(), user);  // assuming getUsername() exists
        }
    }

    public RedissonClient getRedisson() {
        return redissonClient;
    }

    public void saveUsers() {

        UserDatabase.saveUsers(new ArrayList<>(allUsers.values()));
        UserDatabaseSQL.saveUsers(new ArrayList<>(allUsers.values()));
    }

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

public void addGame(Game game) {
    if (game != null && game.getNetworkId() != null) {
        allGames.put(game.getNetworkId(), game);
    } else {
        System.err.println("Failed to add game: Game or NetworkId is null.");
    }
}
    public void setUserByUsername(User player) {
        allUsers.put(player.getUsername(), player);
    }
    public void saveAllGamesToRedis() {
        try {
            RedissonClient redisson = ServerApp.getInstance().getRedisson();
            RMap<String, String> gameMap = redisson.getMap("savedGames");

            for (Game game : allGames.values()) {
                String compressed = GameSaver.serializeAndCompressGame(game);
                gameMap.put(game.getNetworkId(), compressed); // gameId is the key
            }

            System.out.println("✅ Games saved to Redis.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Game loadGameFromRedis(String gameId) throws IOException {
        RedissonClient redisson = ServerApp.getInstance().getRedisson();
        RMap<String, String> gameMap = redisson.getMap("savedGames");
        String base64Data = gameMap.get(gameId);

        if (base64Data == null) return null;

        byte[] decoded = Base64.getDecoder().decode(base64Data);
        ObjectMapper mapper = GameSaver.createCustomObjectMapper();
        try (InputStream is = new GZIPInputStream(new ByteArrayInputStream(decoded));
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return mapper.readValue(reader, Game.class);
        }
    }

    public RadioService getRadioService() { return radioService; }


    public Gson getGson() { return gson; }

}
