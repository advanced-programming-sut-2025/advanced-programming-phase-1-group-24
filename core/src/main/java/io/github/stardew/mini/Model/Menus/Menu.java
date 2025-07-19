package io.github.stardew.mini.Model.Menus;


import java.util.Scanner;

public enum Menu {
    //dige niaze?
    MainMenu,
    LoginMenu,
    PreGameMenu,
    NewGameMenu,
    MapSelectionMenu,
    GameMenu,
    ProfileMenu,
    TradeMenu,
    ExitMenu,
    LobbyMenu;

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
