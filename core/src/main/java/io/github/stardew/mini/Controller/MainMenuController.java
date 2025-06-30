package io.github.stardew.mini.Controller;


import io.github.stardew.mini.Model.App;
import io.github.stardew.mini.Model.Menus.MainMenuCommands;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;

import static io.github.stardew.mini.Controller.LoginMenuController.clearLoggedInUserFile;

public class MainMenuController implements MenuController {
    MainMenuCommands command;


    public void userLogout() {
        clearLoggedInUserFile();
        App app = App.getInstance();
        app.setLoggedInUser(null);
        app.setCurrentMenu(Menu.LoginMenu);
    }
}
