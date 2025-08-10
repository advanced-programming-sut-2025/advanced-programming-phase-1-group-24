package io.github.stardew.mini.server.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.View.PreGameMenuView;
import io.github.stardew.mini.server.AppSocket;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.PlayerConnection;
import io.github.stardew.mini.server.ServerApp;

import java.util.*;


public class PreGameMenuController implements MenuController {
    private PreGameMenuView view;

    public void setView(PreGameMenuView view) {
        this.view = view;
    }

    public Result loadGame() {
        MainApp app = MainApp.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        Game savedGameToLoad = app.getGameByUser(user);

        if (savedGameToLoad == null)
            return new Result(false, "no saved game found!");

        savedGameToLoad.setMainPlayer(user);
        savedGameToLoad.reloadExtraData();
        app.setCurrentGame(savedGameToLoad);
        // app.getCurrentGame().reloadExtraData();
        //app.setCurrentMenu(Menu.GameMenu);
        return new Result(true, "game loaded successfully!");
    }

    public Message<?> loadGame(User player, String gameId) {
        if (player == null)
            return Message.NOT_FOUND.setMessage("player is null!");

        Game savedGameToLoad = ServerApp.getInstance().getGameById(gameId);

        if (savedGameToLoad == null)
            return Message.NOT_FOUND.setMessage("no saved game found!");
        GameServer gs = AppSocket.getActiveGameById(gameId);
        List<PlayerConnection> connections = new ArrayList<>();
        if(gs == null) {
            List<User> players = savedGameToLoad.getPlayers();

            savedGameToLoad.setMainPlayer(player);
            for (User user : players) {
                System.out.println("[SERVER] Creating game for: " + user);
                PlayerConnection pc = AppSocket.getPlayerConnectionByUsername(user.getUsername());
                if (pc == null) {
                    return Message.NOT_FOUND.setMessage(user + " connection not found");
                } else {
                    System.out.println("[SERVER] Found player connection for " + user + ", sessionId = " + pc.getWsContext().sessionId());
                }
                connections.add(pc);
            }
            if (FarmTemplateManager.getTemplates() == null) {
                FarmTemplateManager.loadTemplates(); // only once
            }
            savedGameToLoad.resetLoadGamesStatus();
            GameServer gameServer = new GameServer(connections, savedGameToLoad);
            AppSocket.addGame(gameServer);
            gameServer.start();
            gs = gameServer;

        }
        savedGameToLoad.markPlayerLoadingGame(player.getUsername());
        Map<String, Object> body = new HashMap<>();
        ObjectMapper mapper = GameSaver.createCustomObjectMapper();

        /////////////////////////////////danger//////////////////////////////////////
        //before loading and when we are creating a new game the reference of user in server app and
        // player connection's user in app socket and the players in game are the same but in a loaded game
        // it cant tell that the user in the game has the same reference as the one in server app and app socket
        Objects.requireNonNull(AppSocket.getPlayerConnectionByUsername(player.getUsername())).setUser(savedGameToLoad.getPlayerByUsername(player.getUsername()));
        ServerApp.getInstance().setUserByUsername(savedGameToLoad.getPlayerByUsername(player.getUsername()));
        ///////////////////////////////////danger//////////////////////////////////////
        if(savedGameToLoad.haveAllPlayersLoadedGame()) {
            for (PlayerConnection playerConnection : gs.getPlayers()) {
                if (playerConnection.getWsContext().session.isOpen()) {
                    savedGameToLoad.setCurrentPlayer(savedGameToLoad.getPlayerByUsername(playerConnection.getUsername()));
                    try {
                        String jsonGame = mapper.writeValueAsString(savedGameToLoad); // serialize Game to JSON string
                        body.put("game", jsonGame);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Message.INTERNAL_SERVER_ERROR.setMessage("Failed to serialize game");
                    }
                    Message<Map<String, Object>> msg = new Message<>(200, "Game started all players have chosen map", body, Message.MessageType.RESPONSE);
                    msg.setType("start-game");
                    playerConnection.getWsContext().send(new Gson().toJson(msg));
                }
            }
            //gs.getGame().resetLoadGamesStatus();
            gs.startGameTimer();
        }

        return new Message<>(200, "Game created", body, Message.MessageType.RESPONSE);
    }
}
