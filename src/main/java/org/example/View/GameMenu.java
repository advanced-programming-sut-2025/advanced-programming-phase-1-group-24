package org.example.View;


import org.example.Controller.GameMenuController;
import org.example.Model.Menus.GameMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu implements AppMenu {
    GameMenuController controller = new GameMenuController();

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = GameMenuCommands.NEXT_TURN.getMatcher(input)) != null) {
            System.out.println(controller.nextTurn(scanner));
        } else if ((matcher = GameMenuCommands.TERMINATE_GAME.getMatcher(input)) != null) {
            System.out.println(controller.startForceTerminateVote(scanner));
        } else if ((matcher = GameMenuCommands.EXIT_GAME.getMatcher(input)) != null) {
            System.out.println(controller.exitGame());
        } else if ((matcher = GameMenuCommands.LOAD_GAME.getMatcher(input)) != null) {
            System.out.println(controller.loadGame());
        } else if ((matcher = GameMenuCommands.NEW_GAME.getMatcher(input)) != null) {
            System.out.println(controller.createGame(matcher.group("users"),scanner));
        } else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
            System.out.println(controller.enterMenu(matcher.group("menuName")));
        } else if ((matcher = GameMenuCommands.SHOW_MENU.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentMenu());
        } else if ((matcher = GameMenuCommands.EXIT.getMatcher(input)) != null) {
            controller.menuExit();
        } else {
            System.out.println("invalid command");
        }

    }

}
