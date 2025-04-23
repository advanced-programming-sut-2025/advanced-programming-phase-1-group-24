package org.example.Controller;


import org.example.Model.App;
import org.example.Model.Menus.MainMenuCommands;
import org.example.Model.Menus.Menu;
import org.example.Model.Result;

import static org.example.Controller.LoginMenuController.clearLoggedInUserFile;

public class MainMenuController implements MenuController {
    MainMenuCommands command;


    public void userLogout() {
        clearLoggedInUserFile();
        App app = App.getInstance();
        app.setLoggedInUser(null);
        app.setCurrentMenu(Menu.MainMenu);
    }
}
