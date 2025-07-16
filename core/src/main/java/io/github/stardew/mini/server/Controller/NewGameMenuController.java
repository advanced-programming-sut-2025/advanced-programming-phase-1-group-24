package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.Model.*;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.client.View.NewGameMenuView;
import io.github.stardew.mini.server.AppSocket;
import io.github.stardew.mini.server.GameServer;
import io.javalin.http.Context;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NewGameMenuController implements MenuController{
    private NewGameMenuView view;
    public void setView(NewGameMenuView view) {
        this.view = view;
    }
    private static final Avatar[] FEMALE_AVATARS = {Avatar.Abigail, Avatar.Haley};
    private static final Avatar[] MALE_AVATARS = {Avatar.Shane, Avatar.Alex};

    private static final Random random = new Random();

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
    public void handleGetRequests(Context ctx) {
        String gameId = ctx.pathParam("gameId");
        GameServer gs = AppSocket.getActiveGameById(gameId);
        if (gs == null) {
            ctx.json(Message.NOT_FOUND.setMessage("Game not found"));
            return;
        }
        gs.handleRequests(ctx);
    }

    public void handlePostRequests(Context ctx) {
        String gameId = ctx.pathParam("gameId");
        GameServer gs = AppSocket.getActiveGameById(gameId);
        if (gs == null) {
            ctx.json(Message.NOT_FOUND.setMessage("Game not found"));
            return;
        }
        gs.handleRequests(ctx);
    }
}
