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

        if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMatcher(input)) != null) {
            System.out.println(controller.shepherdAnimal(matcher.group("name"), matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.FEED_HAY.getMatcher(input)) != null) {
            System.out.println(controller.feedHay(matcher.group("name")));
        } else if ((matcher = GameMenuCommands.CHEAT_ANIMAL_FRIENDSHIP.getMatcher(input)) != null) {
            System.out.println(controller.cheatAnimalFriendship(matcher.group("name"), matcher.group("amount")));
        } else if ((matcher = GameMenuCommands.ANIMALS_INFO.getMatcher(input)) != null) {
            System.out.println(controller.showOwnedAnimals());
        } else if ((matcher = GameMenuCommands.PET.getMatcher(input)) != null) {
            System.out.println(controller.petAnimal(matcher.group("name")));
        } else if ((matcher = GameMenuCommands.CHeat_THOR.getMatcher(input)) != null) {
            System.out.println(controller.cheatThor(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.CHEAT_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeEnergy(matcher.group("value")));
        } else if ((matcher = GameMenuCommands.CHEAT_UNLIMITED_ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.cheatUnlimitedEnergy());
        } else if ((matcher = GameMenuCommands.ENERGY.getMatcher(input)) != null) {
            System.out.println(controller.showEnergy());
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER.getMatcher(input)) != null) {
            System.out.println(controller.cheatChangeWeather(matcher.group("weather")));
        } else if ((matcher = GameMenuCommands.WEATHER.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentWeather());
        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMatcher(input)) != null) {
            System.out.println(controller.showTomorrowWeather());
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
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
            System.out.println(controller.createGame(matcher.group("users"), scanner));
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
