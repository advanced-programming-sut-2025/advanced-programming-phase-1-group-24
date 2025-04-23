package org.example.View;


import org.example.Controller.GameMenuController;
import org.example.Model.Menus.LoginMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu implements AppMenu {
    GameMenuController controller = new GameMenuController();
    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = LoginMenuCommands.EXIT.getMatcher(input)) != null) {
            controller.menuExit();
        } else if ((matcher = LoginMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.enterMenu(matcher.group("menuName")));
        } else if ((matcher = LoginMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else {
            System.out.println("invalid command");
        }

    }

}
