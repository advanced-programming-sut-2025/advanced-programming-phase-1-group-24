package org.example.Model.Menus;


import org.example.View.*;

import java.util.Scanner;

public enum Menu {
    MainMenu(new MainMenu()),
    LoginMenu(new LoginMenu()),
    GameMenu(new GameMenu()),
    ProfileMenu(new ProfileMenu()),
    TradeMenu(new TradeMenu()),
    ExitMenu(new ExitMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public static Menu fromString(String menuName) {
        for (Menu menu : Menu.values()) {
            if (menu.name().equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return null;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.handleCommand(scanner);
    }
}
