package io.github.stardew.mini.Controller;


import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Menus.MainMenuCommands;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.View.MainMenuView;

import static io.github.stardew.mini.Controller.LoginMenuController.clearLoggedInUserFile;

public class MainMenuController implements MenuController {
    private MainMenuView view;

    public void setView(MainMenuView view) {
        this.view = view;
    }

    // Logout and return to login screen
    public void userLogout() {
        clearLoggedInUserFile();
        MainApp app = MainApp.getInstance();
        app.setLoggedInUser(null);
        app.setCurrentMenu(Menu.LoginMenu);
        // switch screen
        app.setScreen(new io.github.stardew.mini.View.LoginMenuView(
            new LoginMenuController(), io.github.stardew.mini.Model.Assets.GameAssetManager.skin));
    }

    public Result showUserInfoMainMenu() {
        MainApp app = MainApp.getInstance();
        User user = app.getLoggedInUser();

        String info = String.format(
            "username: %s\nnickname: %s\nMax money in a game: %d\nplayed games: %d\n",
            user.getUsername(), user.getNickname(), user.getMaxMoneyInGames(), user.getPlayedGames()
        );

        return new Result(true, info);
    }
}
