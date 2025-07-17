package io.github.stardew.mini.server;

import io.javalin.Javalin;

public class MainServer {
    private Javalin app;

    public void start() {
        app = Javalin.create().start(8080);
        app.before(ctx -> ctx.contentType("application/json"));
        app.get("/", ctx -> ctx.result("{\"message\":\"Hello from Stardew Mini Server!\"}"));

        // ✅ Initialize and start WebSocket endpoints
        AppSocket socketHandler = new AppSocket(app);
        socketHandler.start();
    }

    public void stop() {
        if (app != null) {
            app.stop();
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting the server...");
        MainServer server = new MainServer();
        try {
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
}
