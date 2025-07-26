package io.github.stardew.mini.server.Controller;


import com.google.gson.Gson;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Message;
import io.github.stardew.mini.Model.SaveGame.GameSaver;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.server.GameServer;
import io.github.stardew.mini.server.PlayerConnection;

import java.util.HashMap;
import java.util.Map;

public interface MenuController {
    default void menuExit() {
        MainApp app = MainApp.getInstance();
        if (app.getCurrentMenu() == Menu.LoginMenu) {
            app.setCurrentMenu(Menu.ExitMenu);
            return;
        }
        app.setCurrentMenu(Menu.MainMenu);
    }

    default Result showCurrentMenu() {
        MainApp app = MainApp.getInstance();
        String menuName = app.getCurrentMenu().name();
        return new Result(true, menuName);
    }

    default Result enterMenu(String menuName) {
        MainApp app = MainApp.getInstance();
        Menu matchedMenu = Menu.fromString(menuName);
        if (matchedMenu == null) {
            return new Result(false, "Menu not found!");
        }
        if (matchedMenu != Menu.MainMenu && app.getCurrentMenu() != Menu.MainMenu) {

            return new Result(false, "You can't enter menu " + menuName + " go to MainMenu first!");
        }
        MainApp.getInstance().setCurrentMenu(matchedMenu);
        return new Result(true, "Entered " + menuName + " successfully!");
    }
    default void broadcastGameStateToAllPlayers(GameServer server, Game game, String messageType) {
        Map<String, Object> body = new HashMap<>();


        for (PlayerConnection pc : server.getPlayers()) {
            if (pc.getWsContext().session.isOpen()) {
                game.setCurrentPlayer(game.getPlayerByUsername(pc.getUsername()));
                try {
                    String jsonGame = GameSaver.createCustomObjectMapper().writeValueAsString(game);
                    body.put("game", jsonGame);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                Message<Map<String, Object>> message = new Message<>(200, "Game updated", body, Message.MessageType.RESPONSE);
                message.setType(messageType);
                pc.getWsContext().send(new Gson().toJson(message));
            }
        }
    }


}
