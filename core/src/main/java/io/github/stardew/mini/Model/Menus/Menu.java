package io.github.stardew.mini.Model.Menus;


import io.github.stardew.mini.View.*;

import java.util.Scanner;

public enum Menu {
    MainMenu(new MainMenuView()),
    LoginMenu(new LoginMenuView()),
    GameMenu(new GameMenu()),
    ProfileMenu(new ProfileMenuView()),
    TradeMenu(new TradeMenuView()),
    ExitMenu(new ExitMenuView());

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
