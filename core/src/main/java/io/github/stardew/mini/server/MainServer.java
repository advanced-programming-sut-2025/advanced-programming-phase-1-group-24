package io.github.stardew.mini.server;

import io.github.stardew.mini.common.Model.SaveGame.GameDatabase;
import io.javalin.Javalin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainServer {
    private Javalin app;

    public void start() {
        app = Javalin.create(config -> {
            config.jetty.modifyWebSocketServletFactory((factory) -> {
                factory.setMaxTextMessageSize(512 * 1024);
                factory.setMaxBinaryMessageSize(512 * 1024);
            });
            config.showJavalinBanner = false;
        }).start(8080);

        app.before(ctx -> ctx.contentType("application/json"));
        app.get("/", ctx -> ctx.result("{\"message\":\"Hello from Stardew Mini Server!\"}"));

        AppSocket socketHandler = new AppSocket(app);
        socketHandler.start();

        startCleanupTask();
    }

    public void stop() {
        if (app != null) {
            app.stop();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting the server...");
        MainServer server = new MainServer();
        ServerApp app = ServerApp.getInstance();
        try {
            GameDatabase.initDatabase();
            server.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Failed to start the server:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void startCleanupTask() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            for (Lobby lobby : LobbyManager.getInstance().getActiveLobbies().values()) {
                if (lobby.isExpired()) {
                    System.out.println("🗑️ Removing expired lobby: " + lobby.getId());
                    LobbyManager.getInstance().getActiveLobbies().remove(lobby.getId());
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
