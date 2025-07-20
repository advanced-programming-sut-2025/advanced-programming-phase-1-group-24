
package io.github.stardew.mini.client;

import com.badlogic.gdx.Gdx;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Message;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

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
                if ("start-map-selection".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        //TODO:first parse the gaem
                        Game game = MainApp.getInstance().getCurrentGame(); // Already stored on client
                        MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu); // Switch to main game screen
                    });
                }
//                if ("start-game".equalsIgnoreCase(message.getType())) {
//                    Gdx.app.postRunnable(() -> {
//                        Game game = MainApp.getInstance().getCurrentGame(); // Already stored on client
//                        MainApp.getInstance().setCurrentMenu(Menu.GameMenu); // Switch to main game screen
//                    });
//                }
                if ("start-map-selection".equalsIgnoreCase(message.getType())) {
                    Gdx.app.postRunnable(() -> {
                        try {
                            Object bodyRaw = message.getBody();
                            if (bodyRaw instanceof Map<?, ?> bodyMap) {
                                Object gameJsonObj = bodyMap.get("game");

                                if (gameJsonObj instanceof String json) {
                                    Game game = GameSaver.createCustomObjectMapper().readValue(json, Game.class);
                                    MainApp.getInstance().setCurrentGame(game);
                                    System.out.println("✔️ Game deserialized successfully in start-map-selection");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println("❌ Failed to parse game in start-map-selection");
                        }

                        MainApp.getInstance().setCurrentMenu(Menu.MapSelectionMenu); // Now the menu can read the game safely
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

        // Optionally include gameId in the body or add a field if needed (depends on server design)
//        if (params != null && gameId != null) {
//            params.put("gameId", gameId);
//        }
        requestMessage.setGameID(gameId);

        // Serialize and send
        String json = gson.toJson(requestMessage);

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
}
