package io.github.stardew.mini.Controller;


import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Menus.MainMenuCommands;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.View.MainMenuView;

import static io.github.stardew.mini.Controller.LoginMenuController.clearLoggedInUserFile;

public class MainMenuController implements MenuController {
    MainMenuCommands command;
MainMenuView view;

    public void setView(MainMenuView view) {
        this.view = view;
    }

    public void userLogout() {
        clearLoggedInUserFile();
        MainApp app = MainApp.getInstance();
        app.setLoggedInUser(null);
        app.setCurrentMenu(Menu.LoginMenu);
    }
}
