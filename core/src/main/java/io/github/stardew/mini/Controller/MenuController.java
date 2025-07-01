package io.github.stardew.mini.Controller;


import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Menus.Menu;
import io.github.stardew.mini.Model.Result;

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

}
