
package io.github.stardew.mini.client;

import com.badlogic.gdx.Gdx;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Growables.Growable;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.View.MainMenuView;
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.PlayerConnection;
import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.View.MainMenuView;
import io.github.stardew.mini.server.Controller.MainMenuController;
import io.github.stardew.mini.server.PlayerConnection;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class NetworkClient extends WebSocketClient {

    private static final Gson gson = new Gson();

    // Map to track requests waiting for a response by requestId
    private final Map<String, CompletableFuture<Message<?>>> pendingRequests = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public NetworkClient(URI serverUri) {
        super(serverUri);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connected");

        // Schedule ping every 10 seconds
        scheduler.scheduleAtFixedRate(() -> {
            if (this.isOpen()) {
                try {
                    this.send("ping"); // or a proper ping JSON if your server expects it
                } catch (Exception e) {
                    System.err.println("Failed to send ping: " + e.getMessage());
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }


//    @Override
//    public void onMessage(String messageJson) {
//        System.out.println("Received: " + messageJson);
//
//        // Deserialize incoming message to Message class
//        Message<?> message = gson.fromJson(messageJson, Message.class);
//
//        String requestId = message.getRequestId();
//        if (requestId != null) {
//            CompletableFuture<Message<?>> future = pendingRequests.remove(requestId);
//            if (future != null) {
//                future.complete(message);
//            }
//        } else {
//                if ("time-update".equalsIgnoreCase(message.getType())) {
//                    Map<String, Object> data = (Map<String, Object>) message.getBody();
//                    int hour = ((Double) data.get("hour")).intValue();
//                    int day = ((Double) data.get("day")).intValue();
//                    String dayOfWeekString = (String) data.get("dayOfWeek");
//                    String seasonString = (String) data.get("season");
//                    DayOfWeek dayOfWeek = DayOfWeek.fromString(dayOfWeekString);
//                    Season season = Season.fromString(seasonString);
//
//                    // Optional: store or update this data somewhere globally
//                    MainApp.getInstance().getCurrentGame().getTimeAndDate().updateTime(hour, day, dayOfWeek, season);
//
//                    // Notify your UI or game loop (if any)
//                    System.out.printf("[CLIENT] Time updated: %02d:00, Day %d (%s), Season: %s%n",
//                        hour, day, dayOfWeek, seasonString);
//                }
//            }
//
    ////        } else {
    ////            // Handle unsolicited messages if any (e.g., broadcasts)
    ////        }
//    }
    @Override
    public void onMessage(String messageJson) {
        System.out.println("Received raw JSON: " + messageJson);

        try {
            Message<?> message = gson.fromJson(messageJson, Message.class);
            System.out.println("Parsed message object: " + message);
            System.out.println("RequestId: " + message.getRequestId());

            String requestId = message.getRequestId();
            if (requestId != null) {
                CompletableFuture<Message<?>> future = pendingRequests.remove(requestId);
                if (future != null) {
                    System.out.println("✅ Completing future for requestId: " + requestId);
                    future.complete(message);
                } else {
                    System.err.println("❌ No future found for requestId: " + requestId);
                }
            } else {

                if ("online-players".equalsIgnoreCase(message.getType())) {
                    @SuppressWarnings("unchecked")
                    Map<String,Object> body = (Map<String,Object>) message.getBody();
                    @SuppressWarnings("unchecked")
                    List<Map<String,String>> list = (List<Map<String,String>>) body.get("players");
                    MainApp.getInstance().updateOnlinePlayers(list);
                    return;
                }

                if ("player-disconnected".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    String username = (String) data.get("username");

                    Gdx.app.postRunnable(() -> {
                        // نمایش دیالوگ یا پیام برای DC
                        MainApp.getInstance().showPlayerDisconnectedMessage(username);
                    });
                    return;
                }


                if ("time-update".equalsIgnoreCase(message.getType())) {
                    Map<String, Object> data = (Map<String, Object>) message.getBody();
                    int hour = ((Double) data.get("hour")).intValue();
                    int day = ((Double) data.get("day")).intValue();
                    String dayOfWeekString = (String) data.get("dayOfWeek");
                    String seasonString = (String) data.get("season");
                    DayOfWeek dayOfWeek = DayOfWeek.fromString(dayOfWeekString);
                    Season season = Season.fromString(seasonString);

                    // Optional: store or update this data somewhere globally
                    MainApp.getInstance().getCurrentGame().getTimeAndDate().updateTime(hour, day, dayOfWeek, season);

                    // Notify your UI or game loop (if any)
                    System.out.printf("[CLIENT] Time updated: %02d:00, Day %d (%s), Season: %s%n",
                        hour, day, dayOfWeek, seasonString);
                }
                if ("endOfDay".equalsIgnoreCase(message.getType())) {
                    Object bodyRaw = message.getBody();

                    if (bodyRaw instanceof Map<?, ?> bodyMap) {
                        Object gameJsonObj = bodyMap.get("game");

                        if (gameJsonObj instanceof  String json) {
                            System.out.println("////////////////////////////////////////////////////");
                            try {
                                Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                game.reloadExtraData();
                                MainApp.getInstance().setCurrentGame(game);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.err.println("Response body is not a map");
                    }
                    System.out.printf("[CLIENT] Game updated handle end of day");
                }
                if ("shop-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        Object bodyRaw = message.getBody();
                        if (bodyRaw instanceof Map<?, ?> bodyMap) {
                            Object gameJson = bodyMap.get("game");
                            if (gameJson instanceof String json) {
                                try {
                                    Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                    MainApp.getInstance().setCurrentGame(game);
                                    game.reloadExtraData();
                                    System.out.println("[CLIENT] Shop state updated.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }


                if ("start-game".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();

                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof  String json) {
                                    try {
                                        Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                        game.reloadExtraData();
                                        MainApp.getInstance().setCurrentGame(game);
                                       // System.out.println("Farms: " + MainApp.getInstance().getCurrentGame().getMap().getFarms().size());
                                        System.out.println("Game successfully deserialized");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("failed to deserialize game after map selection");
                                    }
                                }
                            } else {
                                System.err.println("Response body is not a map");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse game in start-map-selection");
                        }
                        MainApp.getInstance().setCurrentMenu(Menu.GameMenu); // Now the menu can read the game safely
                    });
                }
                if ("start-map-selection".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            System.out.println("addddddddddd//////////////////////////////////////////");
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof  String json) {
                                    try {
                                        Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                        MainApp.getInstance().setCurrentGame(game);
                                        // System.out.println("Farms: " + MainApp.getInstance().getCurrentGame().getMap().getFarms().size());
                                        System.out.println("Game successfully deserialized");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("failed to deserialize game after map selection");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse game in start-map-selection");
                        }
                        MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu); // Now the menu can read the game safely
                    });
                }
                if ("game-ended".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        MainApp.getInstance().setCurrentGame(null);
                        MainApp.getInstance().setCurrentMenu(Menu.MainMenu);
//                        MainApp.getInstance().setScreen(new MainMenuView(new MainMenuController(), GameAssetManager.skin));
                    });
                }
                if ("faint-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object faintedUserObj = bodyMap.get("fainted");
                                if (faintedUserObj instanceof String faintedUsername) {
                                    User user = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
                                    if (user != null && user.getUsername().equals(faintedUsername)) {
                                        user.setFainted(true);
                                    } else {
                                        System.err.println("User not found: " + faintedUsername);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse fainted User");
                        }
                    });
                }
//                if ("leaderboard-update".equalsIgnoreCase(message.getType())) {
//                    @SuppressWarnings("unchecked")
//                    List<Map<String,Object>> lb = (List<Map<String,Object>>)
//                        ((Map<?,?>)message.getBody()).get("leaderboard");
//                    Gdx.app.postRunnable(() -> {
//                        MainApp.getInstance().getCurrentGameView().updateLeaderboard(lb);
//                    });
//                    return;
//                }




                if("tile-update".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                String username = (String) bodyMap.get("username");
                                Object tileRaw = bodyMap.get("tile");
                                Tile tile = GameSaver.convertObject(tileRaw, Tile.class);
                                int movingDirection = ((Number) bodyMap.get("movingDirection")).intValue();
                                boolean hasFainted = (Boolean) bodyMap.get("hasFainted");
                                for(User otherPlayer : MainApp.getInstance().getCurrentGame().getPlayers()){
                                    if(otherPlayer.getUsername().equals(username)){
                                        otherPlayer.setCurrentTile(tile);
                                        otherPlayer.setMovingDirection(movingDirection);
                                        otherPlayer.setFainted(hasFainted);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("❌ Failed to parse updated user.");
                        }
                    });
                }
                if("plant-growable".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try{
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                int x = ((Number) bodyMap.get("x")).intValue();
                                int y = ((Number) bodyMap.get("y")).intValue();
                                boolean walkable = (Boolean) bodyMap.get("walkable");
                                boolean plowed = (Boolean) bodyMap.get("plowed");

//                               Object backpackRaw = bodyMap.get("inventory");
//                               Backpack backpack = GameSaver.convertObject(backpackRaw, Backpack.class);

                                Map<Item, Integer> inventory = convertToInventory((Map<String, Integer>) bodyMap.get("inventoryItems"));


                                Object growableRaw = bodyMap.get("containedGrowable");
                                Growable growable = GameSaver.convertObject(growableRaw, Growable.class);
                                String username = (String) bodyMap.get("username");
                                if(MainApp.getInstance().getCurrentGame().getCurrentPlayer().getUsername().equals(username)){
                                    MainApp.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().getInventoryItems().putAll(inventory);
                                }
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setWalkable(walkable);
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setIsPlowed(plowed);
                                MainApp.getInstance().getCurrentGame().getMap().getTile(x, y).setContainedGrowable(growable);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                            System.out.println("❌ Failed to parse updated plant.");
                        }
                    });
                }
                System.err.println("❌ requestId was null");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to parse message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + reason);
        scheduler.shutdownNow(); // stop pings
        pendingRequests.forEach((id, future) -> future.completeExceptionally(
            new RuntimeException("Connection closed before response")));
        pendingRequests.clear();
    }


    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    /**
     * Send a request using Message object and return CompletableFuture for the response.
     */
    public CompletableFuture<Message<?>> sendRequest(
        String gameId,
        String controllerName,
        String methodName,
        String httpMethod,
        Map<String, Object> params,
        String username // optionally pass username if you want
    ) {
        if (!isOpen()) {
            CompletableFuture<Message<?>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new IllegalStateException("WebSocket is not open"));
            return failedFuture;
        }

        String requestId = UUID.randomUUID().toString();

        // Create Message object with all relevant info
        Message<Map<String, Object>> requestMessage = new Message<>(0, "Client Request", params, Message.MessageType.REQUEST);
        requestMessage.setControllerName(controllerName);
        requestMessage.setMethodName(methodName);
        requestMessage.setRequestId(requestId);
        requestMessage.setType(httpMethod); // "GET" or "POST"
        requestMessage.setUsername(username);
        requestMessage.setMessageType(Message.MessageType.REQUEST);
        requestMessage.setToken(MainApp.getInstance().getJwtToken());
        // Optionally include gameId in the body or add a field if needed (depends on server design)
//        if (params != null && gameId != null) {
//            params.put("gameId", gameId);
//        }
        requestMessage.setGameID(gameId);

        // Serialize and send
        String json = gson.toJson(requestMessage);

        System.out.println("Sending JSON: " + json);

        CompletableFuture<Message<?>> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        send(json);

        return future;
    }

    // Convenience overloads without username parameter:

    public CompletableFuture<Message<?>> sendGet(
        String gameId,
        String controllerName,
        String methodName,
        Map<String, Object> params,
        String username
    ) {
        return sendRequest(gameId, controllerName, methodName, "GET", params, username);
    }

    public CompletableFuture<Message<?>> sendPost(
        String gameId,
        String controllerName,
        String methodName,
        Map<String, Object> params,
        String username
    ) {
        return sendRequest(gameId, controllerName, methodName, "POST", params, username);
    }
    /**
     * Send an AuthController.login request over WS, returning the server’s Message<String>.
     */
    public CompletableFuture<Message<String>> login(String username, String password) {
        // Build the credentials payload
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        // controllerName = "AuthController", methodName = "login"
        // No gameId, no username header needed here
        @SuppressWarnings("unchecked")
        CompletableFuture<Message<String>> future =
            (CompletableFuture<Message<String>>)(CompletableFuture<?>)
                sendRequest(
                    null,
                    "AuthController",
                    "login",
                    "POST",
                    params,
                    null
                );
        return future;
    }


    public void sendConnect(String username) {
        if (!isOpen()) {
            System.err.println("WebSocket is not open");
            return;
        }

        Message<String> connectMsg = new Message(0, "connect", null, Message.MessageType.REQUEST);
        connectMsg.setUsername(username);

        String json = gson.toJson(connectMsg);
        send(json);
        System.out.println("Sent connect for user: " + username);
    }

    public static Map<Item, Integer> convertToInventory(Map<String, ?> rawInventory) {
        Map<Item, Integer> result = new HashMap<>();
        for (Map.Entry<String, ?> entry : rawInventory.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            int amount;
            if (value instanceof Number) {
                amount = ((Number) value).intValue(); // works for both Integer and Double
            } else {
                continue; // or throw an error/log
            }

            Item item = Item.getRandomItem(name);
            if (item != null) {
                result.put(item, amount);
            } else {
                System.err.println("Unknown item: " + name);
            }
        }
        return result;
    }


}
