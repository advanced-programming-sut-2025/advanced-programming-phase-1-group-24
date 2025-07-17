package io.github.stardew.mini.server;


import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.server.Controller.ServerController;
import io.javalin.Javalin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.Message;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppSocket {
    private final Javalin app;
    private static final Gson gson = new Gson();
    private static final ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();
    private final ServerController serverController = new ServerController();

    public AppSocket(Javalin app) {
        this.app = app;
    }

    public void start() {
        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("WebSocket connected: " + ctx.sessionId());
            });

//            ws.onMessage(ctx -> {
//                String rawMessage = ctx.message();
//                System.out.println("Received message: " + rawMessage);
//
//                try {
//                    Message<?> message = gson.fromJson(rawMessage, Message.class);
//                    if ("connect".equals(message.getType()) && message.getUsername() != null) {
//                        PlayerConnection connection = new PlayerConnection(message.getUsername(), ctx);
//                        connectedPlayers.put(ctx.sessionId(), connection);
//                        System.out.println("User connected: " + message.getUsername());
//                    }
//                } catch (Exception e) {
//                    System.err.println("Failed to parse message: " + e.getMessage());
//                    ctx.send(gson.toJson(Message.BAD_REQUEST));
//                }
//            });
            ws.onMessage(ctx -> {
                String rawMessage = ctx.message();
                System.out.println("Received message: " + rawMessage);

                // Handle ping messages (e.g., from client keep-alive)
                if ("ping".equals(rawMessage)) {
                    // Optionally respond with pong (not required unless client expects it)
                    //ctx.send("pong");
                    ctx.send(gson.toJson(Message.ok("pong")));
                    return;
                }

                try {
                    Message<?> message = gson.fromJson(rawMessage, Message.class);
                    if ("connect".equals(message.getType()) && message.getUsername() != null) {
                        PlayerConnection connection = new PlayerConnection(message.getUsername(), ctx);
                        connectedPlayers.put(ctx.sessionId(), connection);

                        User player = new User(message.getUsername(), "", "", "", true);
                        ServerApp.getInstance().addUser(player);

                        System.out.println("User connected: " + message.getUsername());
                    }


/// ///////////////////////////////////////////////////////////////////////////////////////////////
//                    GameServer gameServer = getActiveGameById(message.getGameID());
//                    if (gameServer == null) {
//                        ctx.send(gson.toJson(Message.NOT_FOUND.setMessage("Game not found for user.")));
//                        return;
//                    }
//
//                    Message<?> response = serverController.routingTheRequests((Message<Map<String, Object>>) message, gameServer);
//                    System.out.println("Sending response: " + gson.toJson(response));
//                    ctx.send(gson.toJson(response));

                    Message<?> response;
                    System.out.println("message.getType(): " + message.getType());
                    System.out.println("message.getUsername(): " + message.getUsername());
                    System.out.println("message.getControllerName(): " + message.getControllerName());
                    System.out.println("message.getMethodName(): " + message.getMethodName());


                    if ("NewGameMenuController".equalsIgnoreCase(message.getControllerName().trim())
                        && "createGameOnServer".equalsIgnoreCase(message.getMethodName().trim())) {
                        System.out.println("hereeeeeeeeee");
                        // This method doesn't require a game ID
                        response = serverController.routingTheRequests((Message<Map<String, Object>>) message, null);
                    } else {
                        // Other messages need a game ID
                        GameServer gameServer = getActiveGameById(message.getGameID());
                        if (gameServer == null) {
                            ctx.send(gson.toJson(Message.NOT_FOUND.setMessage("Game not found for user.")));
                            return;
                        }
                        response = serverController.routingTheRequests((Message<Map<String, Object>>) message, gameServer);
                    }

                    ctx.send(gson.toJson(response));

                    // Handle other message types here...

                } catch (Exception e) {
                    System.err.println("Failed to parse message: " + e.getMessage());
                    ctx.send(gson.toJson(Message.BAD_REQUEST));
                }
            });


            // ✅ WsCloseContext
            ws.onClose(ctx -> {
                String sessionId = ctx.sessionId(); // this is the correct method
                PlayerConnection connection = connectedPlayers.remove(sessionId);
                if (connection != null) {
                    System.out.println("User disconnected: " + connection.getUsername());
                }
            });

            // ✅ WsErrorContext
            ws.onError(ctx -> {
                System.err.println("WebSocket error: " + ctx.error().getMessage());
            });
        });

    }

    public static PlayerConnection getPlayer(String sessionId) {
        return connectedPlayers.get(sessionId);
    }

    public static CopyOnWriteArrayList<GameServer> getActiveGames() {
        return activeGames;
    }

    public static void addGame(GameServer gameServer) {
        activeGames.add(gameServer);
    }

    public static void removeGame(GameServer gameServer) {
        activeGames.remove(gameServer);
    }

    public static GameServer getActiveGameById(String gameId) {
        for (GameServer gs : activeGames) {
            if (gs.getGame().getNetworkId().equals(gameId)) {
                return gs;
            }
        }
        return null;
    }

    public static PlayerConnection getPlayerConnectionByUsername(String username) {
        for (PlayerConnection pc : connectedPlayers.values()) {
            System.out.println("Connected: " + pc.getUsername());
            if (pc.getUsername().equals(username)) {
                return pc;
            }
        }
        System.out.println("PlayerConnection not found for: " + username);
        return null;
    }



}
