package io.github.stardew.mini.server;

import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.server.Controller.ServerController;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;

import java.util.List;

public class GameServer extends Thread {
    private final List<PlayerConnection> players;
    private volatile boolean running = true;
    private Game game;
    private final ServerController controller = new ServerController();


    public GameServer(List<PlayerConnection> players) {
        this.players = players;
    }

    @Override
    public void run() {
        System.out.println("GameServer started for players: " + players.size());
        while (running) {
            // game loop (tick, update state, send updates)
            broadcastGameState();

            try {
                Thread.sleep(100); // ~10 FPS game tick
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void broadcastGameState() {
        for (PlayerConnection player : players) {
            player.send("{\"type\": \"gameState\", \"data\": \"...\"}");
        }
    }

    public void stopServer() {
        running = false;
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

    public void setGame(Game game) {
        this.game = game;
    }
}

