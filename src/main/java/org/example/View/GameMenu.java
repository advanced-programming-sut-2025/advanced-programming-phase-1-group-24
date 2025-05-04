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
        boolean canUseCommand = controller.checkEnergy();
        if ((matcher = GameMenuCommands.SELL_ANIMAL.getMatcher(input)) != null) {
           // System.out.println(controller.sellAnimal(matcher.group("name")));
        } else if ((matcher = GameMenuCommands.COLLECT_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(controller.collectProduct(matcher.group("name")));
        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMatcher(input)) != null) {
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
        } else if ((matcher = GameMenuCommands.SHOW_INVENTORY.getMatcher(input)) != null) {
            System.out.println(controller.showInventory());
        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMatcher(input)) != null) {
            String itemName = matcher.group("itemName");
            String countString = matcher.group("number");
            int count;
            if (countString == null) count = 1000;
            else count = Integer.parseInt(countString);
            System.out.println(controller.trashInventory(itemName, count));
        } else if ((matcher = GameMenuCommands.EQUIP_TOOL.getMatcher(input)) != null) {
            String toolName = matcher.group("toolName");
            System.out.println(controller.equipTool(toolName));
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TOOL.getMatcher(input)) != null) {
            System.out.println(controller.showCurrentTool());
        } else if ((matcher = GameMenuCommands.SHOW_AVAILABLE_TOOLS.getMatcher(input)) != null) {
            System.out.println(controller.showAllTools());
        } else if ((matcher = GameMenuCommands.TOOL_UPGRADE.getMatcher(input)) != null) {
            //COMPLETE THIS AFTER MAKING SHOP
        } else if ((matcher = GameMenuCommands.USE_TOOL.getMatcher(input)) != null) {
            //COMPLETE THIS AFTER COMPLETEING TOOLS
        } else if ((matcher = GameMenuCommands.FISH.getMatcher(input)) != null) {
            String fishingPole = matcher.group("fishingPole");
            System.out.println(controller.fish(fishingPole));
        } else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
            String itemName = matcher.group("itemName");
            int count = Integer.parseInt(matcher.group("count"));
            //System.out.println(controller.cheatAddItem(itemName, count));
            //COMPLETE THIS AFTER WRITING LIST OF ALL ITEMS
        }
        else if((matcher = GameMenuCommands.PLANT.getMatcher(input)) != null) {
            System.out.println(controller.plantGrowable(matcher.group("seedName"), matcher.group("direction")).message());
        }
        else if((matcher = GameMenuCommands.SHOWPLANT.getMatcher(input)) != null) {
             System.out.println(controller.showPlant(matcher.group("x"), matcher.group("y")).message());
        }
        else if((matcher = GameMenuCommands.FERTALISE.getMatcher(input)) != null) {
            System.out.println(controller.fertalizeGrowable(matcher.group("fertilizer"), matcher.group("direction")).message());
        }
        else {
            System.out.println("invalid command");
        }

    }

}
