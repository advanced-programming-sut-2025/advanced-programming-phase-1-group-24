package io.github.stardew.mini.Model.Menus;
import com.badlogic.gdx.Screen;
import io.github.stardew.mini.Controller.*;
import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.GameAssetManager;
import io.github.stardew.mini.View.*;

import java.util.Scanner;

public enum Menu {
    //dige niaze?
    MainMenu,
    LoginMenu,
    PreGameMenu,
    GameMenu,
    ProfileMenu,
    TradeMenu,
    ExitMenu;

    public static Menu fromString(String menuName) {
        for (Menu menu : Menu.values()) {
            if (menu.name().equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return null;
    }

    public void checkCommand(Scanner scanner) {

        //this.menu.handleCommand(scanner);
    }
}
