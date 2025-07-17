package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.server.Controller.GameController;
import io.github.stardew.mini.server.Controller.ServerController;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameServer extends Thread {
    private final List<PlayerConnection> players;
    private volatile boolean running = true;
    private Game game;
    private final ServerController controller = new ServerController();
    private final GameController gameController = new GameController();
    private Timer timer;

    public GameServer(List<PlayerConnection> players) {
        this.players = players;
    }
    @Override
    public void run() {
        System.out.println("GameServer started for players: " + players.size());

        // Start global timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game == null) return;

                game.getTimeAndDate().advanceHour();  // Advance time in game
                gameController.handleEndOfDay();

                // Send game state to all connected clients
//                for (User player : game.getPlayers()) {
//                    WebSocketManager.send(player.getId(), new GameStateUpdateMessage(game));
//                }
            }
        }, 5000, 5000); // delay 5s, repeat every 5s

        // Optional game loop (e.g., for animation ticks or events)
        while (running) {
            broadcastGameState();
            try {
                Thread.sleep(100); // e.g., ~10 FPS
            } catch (InterruptedException e) {
                break;
            }
        }
    }


    public void stopServer() {
        running = false;
        if (timer != null) timer.cancel(); // Stop the global timer
    }


    public void setGame(Game game) {
        this.game = game;
    }

    public void broadcastGameState() {
        for (PlayerConnection player : players) {
            player.send("{\"type\": \"gameState\", \"data\": \"...\"}");
        }
    }



    public void handleRequests(Context ctx) {
        if (ctx.method() == HandlerType.POST) {
            controller.routingTheRequests(ctx , this);
        } else if (ctx.method() == HandlerType.GET) {

        }
    }

    public Game getGame() {
        return game;
    }

}

