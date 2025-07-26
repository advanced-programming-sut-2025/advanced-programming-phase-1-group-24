package io.github.stardew.mini.server.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.stardew.mini.Model.*;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.client.View.NewGameMenuView;
import io.github.stardew.mini.server.AppSocket;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.PlayerConnection;
import io.javalin.http.Context;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NewGameMenuController implements MenuController {
    private NewGameMenuView view;

    public void setView(NewGameMenuView view) {
        this.view = view;
    }

    private static final Avatar[] FEMALE_AVATARS = {Avatar.Abigail, Avatar.Haley};
    private static final Avatar[] MALE_AVATARS = {Avatar.Shane, Avatar.Alex};

    private static final Random random = new Random();

    public Message<?> createGameOnServer(List<String> usernames) {
        System.out.println("called2");
        if (usernames == null || usernames.isEmpty())
            return Message.FORBIDDEN;
        if (usernames.size() > 4)
            return Message.FORBIDDEN;

        List<PlayerConnection> connections = new ArrayList<>();
        Set<String> usersInGames = AppSocket.getActiveGames().stream()
            .flatMap(gs -> gs.getGame().getPlayers().stream())
            .map(user -> user.getUsername())
            .collect(Collectors.toSet());

        for (String username : usernames) {
            PlayerConnection pc = AppSocket.getPlayerConnectionByUsername(username);
            if (pc == null) return Message.NOT_FOUND.setMessage("Player not found");
            if (usersInGames.contains(username))
                return Message.FORBIDDEN;
            connections.add(pc);
        }

        // Create User list from connections
        ArrayList<User> users = (ArrayList<User>) connections.stream()
            .map(PlayerConnection::getUser)
            .toList();

        // Update game fields for each user
        for (User user : users) {
            user.updateGameFields();
        }

        Game game = new Game(users, users.get(0), users.get(0));


        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates();
        }

        GameServer gameServer = new GameServer(connections, game);
        gameServer.setGame(game);

        AppSocket.addGame(gameServer);
        gameServer.start();  // Start the game loop thread

        Map<String, Object> body = new HashMap<>();
        body.put("gameId", game.getNetworkId());
        body.put("message", "Game created successfully");

        return new Message<>(200, "Game created", body, Message.MessageType.RESPONSE);
    }

    public Message<?> createGameOnServer(List<String> usernames, User creator) {
        ArrayList<User> players = new ArrayList<>();
//        if (usernames.size() < 2) {
//            return Message.FORBIDDEN.setMessage("You cant start the game with less than 2 players!");
//        }

        List<PlayerConnection> connections = new ArrayList<>();

        for (String user : usernames) {
            System.out.println("[SERVER] Creating game for: " + user);
            PlayerConnection pc = AppSocket.getPlayerConnectionByUsername(user);
            if (pc == null) {
                return Message.NOT_FOUND.setMessage(user + " connection not found");
            } else if (AppSocket.isPlayerInAnyGame(pc.getUsername())) {
                return Message.FORBIDDEN.setMessage(user + " is already in an online game!");
            } else {
                System.out.println("[SERVER] Found player connection for " + user + ", sessionId = " + pc.getWsContext().sessionId());
            }
            players.add(pc.getUser());
            connections.add(pc);
        }
        for (User player : players) {
            player.updateGameFields();
        }

        Game game = new Game(players, creator, creator);

        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates(); // only once
        }


        GameServer gameServer = new GameServer(connections, game);
        gameServer.setGame(game);

        AppSocket.addGame(gameServer);
        gameServer.start();
        Map<String, Object> body = new HashMap<>();
        body.put("gameId", game.getNetworkId());
        body.put("message", "Game created successfully");
//        String jsonGame = null;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            jsonGame = mapper.writeValueAsString(game);
//            // send jsonGame in response
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return Message.INTERNAL_SERVER_ERROR.setMessage("Failed to serialize game");
//        }
        body.put("game", game);
        Message<Map<String, Object>> msg = new Message<>(200, "Game created", body, Message.MessageType.RESPONSE);
        msg.setType("start-map-selection");

        for (PlayerConnection player : connections) {
            if (!player.getUser().equals(creator) && player.getWsContext().session.isOpen()) {
                player.getWsContext().send(new Gson().toJson(msg));
            }
        }

        return new Message<>(200, "Game created", body, Message.MessageType.RESPONSE);
    }


    public Result createGame(String users) {
        MainApp app = MainApp.getInstance();
        User creator = app.getLoggedInUser();

        if (creator == null)
            return new Result(false, "please login first!");
        // Split usernames and clean empty entries
        List<String> usernames = Arrays.stream(users.trim().split("\\s+"))
            .filter(s -> !s.isEmpty())
            .toList();

        if (usernames.isEmpty())
            return new Result(false, "you must specify at least one username!");
        if (usernames.size() > 3)
            return new Result(false, "you can specify up to 3 usernames!");

        // Build a set of all users already in any active game
        Set<User> usersInGames = app.getActiveGames().stream()
            .flatMap(game -> game.getPlayers().stream())
            .collect(Collectors.toSet());

        if (usersInGames.contains(creator))
            return new Result(false, "you are already in another game!");

        // Try to resolve all usernames to actual users
        List<User> invitedUsers = new ArrayList<>();
        for (String username : usernames) {
            User user = app.getUserByUsername(username);
            if (user == null)
                return new Result(false, "invalid username: " + username);
            if (usersInGames.contains(user))
                return new Result(false, username + " is already in another game!");
            invitedUsers.add(user);
        }

        // Add the creator as the first player
        ArrayList<User> players = new ArrayList<>();
        players.add(creator);
        players.addAll(invitedUsers);

        for (User player : players) {
            player.updateGameFields();
        }

        Game newGame = new Game(players, creator, creator);

        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates(); // only once
        }

        app.getActiveGames().add(newGame);
        app.setCurrentGame(newGame);

        //handleMapSelection(players, scanner);

        return new Result(true, "game created successfully!");
    }
//    public void handleGetRequests(Context ctx) {
//        String gameId = ctx.pathParam("gameId");
//        GameServer gs = AppSocket.getActiveGameById(gameId);
//        if (gs == null) {
//            ctx.json(Message.NOT_FOUND.setMessage("Game not found"));
//            return;
//        }
//        gs.handleRequests(ctx);
//    }
//
//    public void handlePostRequests(Context ctx) {
//        String gameId = ctx.pathParam("gameId");
//        GameServer gs = AppSocket.getActiveGameById(gameId);
//        if (gs == null) {
//            ctx.json(Message.NOT_FOUND.setMessage("Game not found"));
//            return;
//        }
//        gs.handleRequests(ctx);
//    }
}
