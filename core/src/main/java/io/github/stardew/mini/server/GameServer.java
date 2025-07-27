package io.github.stardew.mini.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.Model.TimeManagement.DayOfWeek;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.server.Controller.GameController;
import io.github.stardew.mini.server.Controller.ServerController;

import java.util.*;

public class GameServer extends Thread {
    private final List<PlayerConnection> players;
    private volatile boolean running = true;
    private Game game;
    private final ServerController controller = new ServerController();
    private final GameController gameController = new GameController();
    private Timer timer;

    public GameServer(List<PlayerConnection> players,Game game) {
        this.players = players;
        this.game = game;
    }

    @Override
    public void run() {
        System.out.println("GameServer started for players: " + players.size());

//        // Start global timer
//        timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (game == null ) return;
//
//                game.advanceTimeByOneHour();  // Advance time in game
//                gameController.handleEndOfDay(GameServer.this);
//                // Step 2: Prepare message to send to players
//                for (PlayerConnection player : players) {
//                    if (player.getWsContext().session.isOpen()) {
//                        Map<String, Object> timeUpdate = new HashMap<>();
//                        timeUpdate.put("gameId", game.getNetworkId());
//                        timeUpdate.put("hour", game.getTimeAndDate().getHour());
//                        timeUpdate.put("day", game.getTimeAndDate().getDay());
//                        timeUpdate.put("dayOfWeek", game.getTimeAndDate().getDayOfWeek());
//                        timeUpdate.put("season", game.getTimeAndDate().getSeason());
//
//                        Message<Map<String, Object>> msg = new Message<>(200, "TimeUpdate", timeUpdate, Message.MessageType.RESPONSE);
//                        msg.setType("time-update");
//                        player.getWsContext().send(new Gson().toJson(msg));
//                    }
//                }
//            }
//        }, 5000, 5000); // delay 5s, repeat every 5s

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

    public void startGameTimer() {
        if (timer != null) return; // Prevent double start

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (game == null ) return;

                game.advanceTimeByOneHour();
                gameController.handleEndOfDay(GameServer.this);

                for (PlayerConnection player : players) {
                    if (player.getWsContext().session.isOpen()) {
                        Map<String, Object> timeUpdate = new HashMap<>();
                        timeUpdate.put("gameId", game.getNetworkId());
                        timeUpdate.put("hour", game.getTimeAndDate().getHour());
                        timeUpdate.put("day", game.getTimeAndDate().getDay());
                        timeUpdate.put("dayOfWeek", game.getTimeAndDate().getDayOfWeek());
                        timeUpdate.put("season", game.getTimeAndDate().getSeason());

                        Message<Map<String, Object>> msg = new Message<>(200, "TimeUpdate", timeUpdate, Message.MessageType.RESPONSE);
                        msg.setType("time-update");
                        player.getWsContext().send(new Gson().toJson(msg));
                    }
                }
            }
        }, 5000, 5000);
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
            // player.send("{\"type\": \"gameState\", \"data\": \"...\"}");
        }
    }

//    public void handleRequests(Context ctx) {
//        if (ctx.method() == HandlerType.POST) {
//            controller.routingTheRequests(ctx , this);
//        } else if (ctx.method() == HandlerType.GET) {
//        }
//    }

    public Game getGame() {
        return game;
    }

    public List<PlayerConnection> getPlayers() {
        return players;
    }

    public User getUserByUsername(String username) {
        for(PlayerConnection player : players){
            if(player.getUsername().equals(username)){
                return player.getUser();
            }
        }
        return null;
    }

}

