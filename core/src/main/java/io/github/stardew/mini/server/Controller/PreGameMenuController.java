package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.View.PreGameMenuView;


public class PreGameMenuController implements MenuController{
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
}
