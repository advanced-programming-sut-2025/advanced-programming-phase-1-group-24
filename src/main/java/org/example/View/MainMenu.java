package org.example.View;


import org.example.Controller.MainMenuController;
import org.example.Model.Menus.LoginMenuCommands;
import org.example.Model.Menus.MainMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu implements AppMenu{
    MainMenuController controller = new MainMenuController();


    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = MainMenuCommands.LOGOUT.getMatcher(input)) != null) {
            controller.userLogout();
        } else if ((matcher = MainMenuCommands.EXIT.getMatcher(input)) != null) {
            controller.menuExit();
        } else if ((matcher = MainMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.enterMenu(matcher.group("menuName")));
        } else if ((matcher = MainMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else {
            System.out.println("invalid command");
        }


    }
}
