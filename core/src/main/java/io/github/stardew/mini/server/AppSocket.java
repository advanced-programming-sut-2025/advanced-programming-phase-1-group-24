package io.github.stardew.mini.server;

import io.javalin.Javalin;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import io.github.stardew.mini.Model.Message;

import java.util.concurrent.CopyOnWriteArrayList;

public class AppSocket {
    private final Javalin app;
    private static final Gson gson = new Gson();
    private static final ConcurrentHashMap<String, PlayerConnection> connectedPlayers = new ConcurrentHashMap<>();
    private static final CopyOnWriteArrayList<GameServer> activeGames = new CopyOnWriteArrayList<>();

    public AppSocket(Javalin app) {
        this.app = app;
    }

    public void start() {


        app.ws("/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("WebSocket connected: " + ctx.sessionId());
            });

            ws.onMessage(ctx -> {
                String rawMessage = ctx.message();
                System.out.println("Received message: " + rawMessage);

                // Handle ping messages (e.g., from client keep-alive)
                if ("ping".equals(rawMessage)) {
                    // Optionally respond with pong (not required unless client expects it)
                    ctx.send("pong");
                    return;
                }

                try {
                    Message<?> message = gson.fromJson(rawMessage, Message.class);
                    if ("connect".equals(message.getType()) && message.getUsername() != null) {
                        PlayerConnection connection = new PlayerConnection(message.getUsername(), ctx);
                        connectedPlayers.put(ctx.sessionId(), connection);
                        System.out.println("User connected: " + message.getUsername());
                    }

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


}
