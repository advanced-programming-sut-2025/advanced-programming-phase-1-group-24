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


        if ((matcher = GameMenuCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceDate(matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_TIME.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceTime(matcher.group("number")));
        } else if (input.equals("season")) {
            System.out.println(controller.printSeason());
        } else if (input.equals("time")) {
            System.out.println(controller.printHour());
        } else if (input.equals("date")) {
            System.out.println(controller.printDate());
        } else if (input.equals("datetime")) {
            System.out.println(controller.printDateTime());
        } else if (input.matches("^day\\s+of\\s+the\\s+week$")) {
            System.out.println(controller.printDayOfWeek());
        } else if ((matcher = GameMenuCommands.NEXT_TURN.getMatcher(input)) != null) {
            System.out.println(controller.nextTurn(scanner));
        } else if ((matcher = GameMenuCommands.TERMINATE_GAME.getMatcher(input)) != null) {
            System.out.println(controller.startForceTerminateVote(scanner));
        } else if ((matcher = GameMenuCommands.EXIT_GAME.getMatcher(input)) != null) {
            System.out.println(controller.exitGame());
        } else if ((matcher = GameMenuCommands.LOAD_GAME.getMatcher(input)) != null) {
            System.out.println(controller.loadGame());
        } else if ((matcher = GameMenuCommands.NEW_GAME.getMatcher(input)) != null) {
            System.out.println(controller.createGame(matcher.group("users"),scanner));
        } else if((matcher = GameMenuCommands.PRINT_GAME.getMatcher(input)) != null) {
            System.out.println(controller.printMap(matcher.group("x"), matcher.group("y"), matcher.group("size")).message());
        }
        else if((matcher = GameMenuCommands.HELP_READ_MAP.getMatcher(input)) != null) {
            controller.helpReadMap();
        }
        else if((matcher = GameMenuCommands.WALK.getMatcher(input)) != null) {
            controller.walkTo(matcher.group("x"), matcher.group("y"), scanner);
        }
        else if((matcher = GameMenuCommands.CAFTINFO.getMatcher(input)) != null) {
            controller.printCraftInfo(matcher.group("craftname"));
        }
        else if((matcher = GameMenuCommands.TREEINFO.getMatcher(input)) != null) {
            controller.printTreeInfo(matcher.group("treename"));
        }
        else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
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
