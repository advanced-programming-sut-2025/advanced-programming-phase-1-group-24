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
    private boolean paused = false;
    private boolean isWaitingForPlayersToGoHome = false;

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

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                broadcastLeaderboard();
            }
        }, 0, 5_000);
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

//                if (isWaitingForPlayersToGoHome) {
//                    System.out.println("Waiting for players to return home...");
//                    return;
//                }

                if(!isWaitingForPlayersToGoHome) game.advanceTimeByOneHour();

                if (game.getTimeAndDate().getHour() == 22) {
                    if (!isWaitingForPlayersToGoHome) {
                        System.out.println("Players not all home at 11 PM, pausing time.");
                        isWaitingForPlayersToGoHome = true;
                        return;
                    }
                }

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
                broadcastLeaderboard();
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
    public void pauseGame() {
        this.paused = true;
        if (timer != null) timer.cancel();
    }

    public void resumeGame() {
        if (paused) {
            paused = false;
            startGameTimer();
        }
    }


    public void notifyPlayerDisconnected(PlayerConnection disconnectedPlayer) {
        for (PlayerConnection player : players) {
            if (!player.equals(disconnectedPlayer)) {
                Map<String, String> body = new HashMap<>();
                body.put("username", disconnectedPlayer.getUsername());

                Message<Map<String, String>> msg = Message.ok(body);
                msg.setType("player-disconnected");

                String json = new Gson().toJson(msg);
                player.getWsContext().send(json);
            }
        }
    }
    public void setWaitingForPlayersToGoHome(boolean waitingForPlayersToGoHome) {
        isWaitingForPlayersToGoHome = waitingForPlayersToGoHome;
    }

//    public void broadcastLeaderboard() {
//        List<Map<String, Object>> leaderboard = new ArrayList<>();
//
//        for (PlayerConnection player : players) {
//            User user = player.getUser();
//            Map<String, Object> entry = new HashMap<>();
//            entry.put("username", user.getUsername());
//            entry.put("money", user.getMoney());
//            entry.put("skills", user.getSkillsLevel());
//
//            int skillSum = user.getSkillsLevel().values().stream().mapToInt(i -> i).sum();
//            entry.put("skillSum", skillSum);
//            entry.put("missions", 0); // hardcoded
//
//            int score = user.getMoney() + skillSum + 0;
//            entry.put("score", score);
//
//            leaderboard.add(entry);
//        }
//
//        // Sort by score descending
//        leaderboard.sort((a, b) -> ((Integer) b.get("score")) - ((Integer) a.get("score")));
//
//        // Send to all players
//        Map<String, Object> body = new HashMap<>();
//        body.put("leaderboard", leaderboard);
//
//        Message<Map<String, Object>> msg = new Message<>(200, "LeaderboardUpdate", body, Message.MessageType.RESPONSE);
//        msg.setType("leaderboard-update");
//
//        String json = new Gson().toJson(msg);
//
//        for (PlayerConnection player : players) {
//            player.getWsContext().send(json);
//        }
//    }
//public void broadcastLeaderboard() {
//    List<Map<String, Object>> leaderboard = new ArrayList<>();
//
//    for (PlayerConnection pc : players) {
//        User user = pc.getUser();
//        int money = user.getMoney();
//        int skillSum = user.getSkillsLevel().values().stream().mapToInt(Integer::intValue).sum();
//        int missions = 0; // موقتا هاردکد
//        int score = money + skillSum + missions;
//
//        Map<String, Object> entry = new HashMap<>();
//        entry.put("username", user.getUsername());
//        entry.put("money", money);
//        entry.put("skillSum", skillSum);
//        entry.put("missions", missions);
//        entry.put("score", score);
//
//        leaderboard.add(entry);
//    }
//
//    // مرتب‌سازی نزولی بر اساس امتیاز
//    leaderboard.sort((a, b) -> ((Integer) b.get("score")).compareTo((Integer) a.get("score")));
//
//    // ارسال به‌روزرسانی برای همه
//    Map<String, Object> body = new HashMap<>();
//    body.put("leaderboard", leaderboard);
//    Message<Map<String, Object>> msg = new Message<>(200, "LeaderboardUpdate", body, Message.MessageType.RESPONSE);
//    msg.setType("leaderboard-update");
//    String json = new Gson().toJson(msg);
//
//    for (PlayerConnection pc : players) {
//        if (pc.getWsContext().session.isOpen()) {
//            pc.getWsContext().send(json);
//        }
//    }
//}
// در کلاس GameServer
public List<Map<String, Object>> buildLeaderboard() {
    List<Map<String, Object>> leaderboard = new ArrayList<>();

    for (PlayerConnection pc : players) {
        User user = pc.getUser();
        int money = user.getMoney();
        int skillSum = user.getSkillsLevel().values().stream().mapToInt(Integer::intValue).sum();
        int missions = 0; // temporarily hardcoded
        int score = money + skillSum + missions;

        Map<String, Object> entry = new HashMap<>();
        entry.put("username", user.getUsername());
        entry.put("money", money);
        entry.put("skillSum", skillSum);
        entry.put("missions", missions);
        entry.put("score", score);

        leaderboard.add(entry);
    }

    // Sort descending by score
    leaderboard.sort((a,b) -> ((Integer)b.get("score")).compareTo((Integer)a.get("score")));
    return leaderboard;
}

    // و متد broadcastLeaderboard را هم می‌توانی بر اساس همین بنویسی:
    public void broadcastLeaderboard() {
        Map<String,Object> body = Map.of("leaderboard", buildLeaderboard());
        Message<Map<String,Object>> msg = new Message<>(200, "LeaderboardUpdate", body, Message.MessageType.RESPONSE);
        msg.setType("leaderboard-update");
        String json = new Gson().toJson(msg);

        for (PlayerConnection pc : players) {
            if (pc.getWsContext().session.isOpen()) {
                pc.getWsContext().send(json);
            }
        }
    }
    public void replacePlayerConnection(PlayerConnection pc) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(pc.getUsername())) {
                players.set(i, pc);
                break;
            }
        }
    }



}

