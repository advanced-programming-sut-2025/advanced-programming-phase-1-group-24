package io.github.stardew.mini.server.Router;

import io.github.stardew.mini.server.Controller.NewGameMenuController;
import io.javalin.Javalin;
import io.github.stardew.mini.server.Controller.GameController;

public class GameRouter {
    private final Javalin app;
    private final NewGameMenuController controller = new NewGameMenuController();


    public GameRouter(Javalin app) {
        this.app = app;
    }

    public void initializeRoutes() {
        // Apply auth middleware to all game endpoints
       // app.before("/api/game/*", Auth::validate);

        // GET routes
       // app.get("/api/game/startGame/{lobbyId}", controller::createGame);
        app.get("/api/game/{gameId}", controller::handleGetRequests);

        // POST routes
        app.post("/api/game/{gameId}/{controllerName}/{methodName}", controller::handlePostRequests);
    }
}

