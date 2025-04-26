package org.example.Controller;


import org.example.Model.App;
import org.example.Model.Menus.Menu;
import org.example.Model.Result;

import java.awt.*;

public interface MenuController {
    default void menuExit() {
        App app = App.getInstance();
        if (app.getCurrentMenu() == Menu.LoginMenu) {
            app.setCurrentMenu(Menu.ExitMenu);
            return;
        }
        app.setCurrentMenu(Menu.MainMenu);
    }

    default Result showCurrentMenu() {
        App app = App.getInstance();
        String menuName = app.getCurrentMenu().name();
        return new Result(true, menuName);
    }

    default Result enterMenu(String menuName) {
        App app = App.getInstance();
        Menu matchedMenu = Menu.fromString(menuName);
        if (matchedMenu == null) {
            return new Result(false, "Menu not found!");
        }
        if (matchedMenu != Menu.MainMenu && app.getCurrentMenu() != Menu.MainMenu) {
            return new Result(false, "You can't enter menu " + menuName + " go to MainMenu first!");
        }
        App.getInstance().setCurrentMenu(matchedMenu);
        return new Result(true, "Entered " + menuName + " successfully!");
    }

}
