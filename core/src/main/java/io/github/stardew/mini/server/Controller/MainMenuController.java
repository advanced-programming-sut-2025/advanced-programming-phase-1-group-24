package io.github.stardew.mini.server.Controller;


import io.github.stardew.mini.client.Assets.GameAssetManager;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.common.Model.Menus.Menu;
import io.github.stardew.mini.common.Model.Result;
import io.github.stardew.mini.common.Model.User;
import io.github.stardew.mini.client.View.MainMenuView;
import io.github.stardew.mini.client.View.LoginMenuView;
import io.github.stardew.mini.server.ServerApp;

import static io.github.stardew.mini.server.Controller.LoginMenuController.clearLoggedInUserFile;

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
        app.setScreen(new LoginMenuView(
            new LoginMenuController(), GameAssetManager.skin));
    }
//
//    public Result showUserInfoMainMenu() {
//        MainApp app = MainApp.getInstance();
//        User user = app.getLoggedInUser();
//
//        String info = String.format(
//            "username: %s\nnickname: %s\nMax money in a game: %d\nplayed games: %d\n",
//            user.getUsername(), user.getNickname(), user.getMaxMoneyInGames(), user.getPlayedGames()
//        );
//
//        return new Result(true, info);
//    }
public Result showUserInfo(String username) {
    User user = ServerApp.getInstance().getUser(username);
    if (user == null) return new Result(false, "User not found");

    String info = String.format(
        "username: %s\nnickname: %s\nMax money in a game: %d\nplayed games: %d\n",
        user.getUsername(),
        user.getNickname(),
        user.getMaxMoneyInGames(),
        user.getPlayedGames()
    );

    return new Result(true, info);
}
}
