package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.SaveGame.GameDatabase;
import io.javalin.Javalin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainServer {
    private Javalin app;

    public void start() {

//        app = Javalin.create(config -> {
//                // ۱. تنظیم حداکثر طول پیام متنی (به بایت)
//                config.wsConfig(ws -> {
//                    // مثال: 200 کیلوبایت
//                    ws.getSessionHandler().setMaxTextMessageSize(200 * 1024);
//                    // اگر نیاز داشته باشی می‌تونی maxBinaryMessageSize هم ست کنی:
//                    ws.getSessionHandler().setMaxBinaryMessageSize(200 * 1024);
//                });
//
//                // بقیهٔ تنظیمات معمولت...
//                config.showJavalinBanner = false;
//                config.requestCacheSize = 10_000L;
//            })
//            .start(8080);

        app = Javalin.create(config -> {
            // ─── این بلاک را اضافه کن ─────────────────────────────────
            config.jetty.modifyWebSocketServletFactory((factory) -> {
                // 200 KB برای متن
                factory.setMaxTextMessageSize(512 * 1024);
                // 200 KB برای باینری (اگر نیاز است)
                factory.setMaxBinaryMessageSize(512 * 1024);
            });
            // ─── بقیهٔ تنظیمات معمولت ────────────────────────────────
            config.showJavalinBanner = false;
        }).start(8080);
//        app = Javalin.create(config -> {
//            config.showJavalinBanner = false;
//            config.ws.defaultMaxTextMessageSize = 200 * 1024;
//            config.ws.defaultMaxBinaryMessageSize = 200 * 1024;
//            config.requestCacheSize = 10_000L;
//        }).start(8080);
//        app = Javalin.create(config -> {
//            config.showJavalinBanner = false;
//
//            config.jetty.modifyServletContextHandler(handler -> {
//                handler.addBean(new org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainerInitializer.Configurator() {
//                    @Override
//                    public void accept(org.eclipse.jetty.websocket.server.config.JettyWebSocketServletContainer container) {
//                        container.setMaxTextMessageSize(200 * 1024);   // 200 KB
//                        container.setMaxBinaryMessageSize(200 * 1024); // Optional
//                    }
//                });
//            });
//        }).start(8080);

        //app = Javalin.create().start(8080);
        app.before(ctx -> ctx.contentType("application/json"));
        app.get("/", ctx -> ctx.result("{\"message\":\"Hello from Stardew Mini Server!\"}"));

        // ✅ Initialize and start WebSocket endpoints
        AppSocket socketHandler = new AppSocket(app);
        socketHandler.start();

        // (Optional) Create a hardcoded game for testing
        // AppSocket.createHardcodedGame();
        // ✅ Start the periodic cleanup task
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
            // Keep the main thread alive if needed
            // For example, block here or wait for shutdown signal
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
