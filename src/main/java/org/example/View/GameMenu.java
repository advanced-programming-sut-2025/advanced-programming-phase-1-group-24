package org.example.View;


import jdk.jfr.RecordingState;
import org.example.Controller.GameMenuController;
import org.example.Controller.StoreMenuController;
import org.example.Controller.HouseMenuController;
import org.example.Controller.TradeMenuController;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Menus.StoreMenuCommands;
import org.example.Model.Result;
import org.example.Model.Menus.HouseMenuCommands;
import org.example.Model.Menus.TradeMenuCommands;

import javax.imageio.spi.ImageReaderWriterSpi;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu implements AppMenu {
    GameMenuController controller = new GameMenuController();
    HouseMenuController houseController = new HouseMenuController();
    StoreMenuController storeController = new StoreMenuController();

    public void handleCommand(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        Result canUseCommand = controller.checkEnergy();
        if ((matcher = GameMenuCommands.NEXT_TURN.getMatcher(input)) != null) {
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
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_DATE.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceDate(matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCE_TIME.getMatcher(input)) != null) {
            System.out.println(controller.cheatAdvanceTime(matcher.group("number")));
        } else if ((matcher = GameMenuCommands.CHEAT_SET_SKILL.getMatcher(input)) != null) {
            System.out.println(controller.cheatSetSkill(matcher.group("skill"), matcher.group("number")));
        } else if ((matcher = GameMenuCommands.SHOW_MONEY.getMatcher(input)) != null) {
            System.out.println(controller.showMoney());
        } else if ((matcher = GameMenuCommands.SHOW_SKILL.getMatcher(input)) != null) {
            System.out.println(controller.showSkill(matcher.group("skill")));
        } else if (!canUseCommand.isSuccessful()) {  /////////////////////////////////////////////////////////////////////////
            System.out.println(canUseCommand);
        } else if ((matcher = StoreMenuCommands.BUILD_HABITAT.getMatcher(input)) != null) {
            System.out.println(storeController.buyFromCarpenter(matcher.group("name").trim(), matcher.group("x"), matcher.group("y")));
        } else if ((matcher = StoreMenuCommands.BUY_ANIMAL.getMatcher(input)) != null) {
            System.out.println(storeController.buyAnimal(matcher.group("animal").trim(), matcher.group("name")));
        } else if ((matcher = StoreMenuCommands.SHOW_ALL_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(storeController.showAllProducts());
        } else if ((matcher = StoreMenuCommands.SHOW_ALL_AVAILABLE_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(storeController.showAllAvailableProducts());
        } else if ((matcher = StoreMenuCommands.PURCHASE.getMatcher(input)) != null) {
            String product = matcher.group("product").trim();
            String countStr = matcher.group("count");
            int count = (countStr != null) ? Integer.parseInt(countStr) : 1;
            System.out.println(storeController.purchase(product, count));
        }  else if ((matcher = StoreMenuCommands.UPGRADE_TOOL.getMatcher(input)) != null) {
            System.out.println(storeController.upgradeTool(matcher.group("tool").trim()));
        } else if ((matcher = GameMenuCommands.CHEAT_ADD_MONEY.getMatcher(input)) != null) {
            System.out.println(controller.cheatAddMoney(matcher.group("count")));
        }
        /// ////friendship
        else if ((matcher = GameMenuCommands.FRIEND_SHIP.getMatcher(input)) != null) {
            System.out.println(controller.showFriendships());
        } else if ((matcher = GameMenuCommands.SEND_GIFT.getMatcher(input)) != null) {
            System.out.println(controller.sendGift(matcher.group("username"), matcher.group("item").trim(), matcher.group("amount")));
        } else if ((matcher = GameMenuCommands.LIST_GIFT.getMatcher(input)) != null) {
            System.out.println(controller.listGift());
        } else if ((matcher = GameMenuCommands.RATE_GIFTS.getMatcher(input)) != null) {
            System.out.println(controller.rateGifts(matcher.group("gift"), matcher.group("rate")));
        } else if ((matcher = GameMenuCommands.GIFT_HISTORY.getMatcher(input)) != null) {
            System.out.println(controller.giftHistory(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.FLOWER_SEND.getMatcher(input)) != null) {
            System.out.println(controller.sendFlower(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.SELL_ANIMAL.getMatcher(input)) != null) {
            System.out.println(controller.sellAnimal(matcher.group("name")));
        } else if ((matcher = StoreMenuCommands.SHIPPING_BIN.getMatcher(input)) != null) {
            String productString = matcher.group("product").trim();
            String countString = matcher.group("count");
            int count = (countString != null) ? Integer.parseInt(countString) : -1;
            System.out.println(storeController.placeInShippingBin(productString, count));
        }else if ((matcher = GameMenuCommands.SHOW_PRODUCTS.getMatcher(input)) != null) {
            System.out.println(controller.showAnimalProducts());
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
            System.out.println(controller.createGame(matcher.group("users"), scanner));
        } else if ((matcher = GameMenuCommands.PRINT_GAME.getMatcher(input)) != null) {
            System.out.println(controller.printMap(matcher.group("x"), matcher.group("y"), matcher.group("size")).message());
        } else if ((matcher = GameMenuCommands.HELP_READ_MAP.getMatcher(input)) != null) {
            controller.helpReadMap();
        } else if ((matcher = GameMenuCommands.WALK.getMatcher(input)) != null) {
            controller.walkTo(matcher.group("x"), matcher.group("y"), scanner);
        } else if ((matcher = GameMenuCommands.CAFTINFO.getMatcher(input)) != null) {
            controller.printCraftInfo(matcher.group("craftname"));
        } else if ((matcher = GameMenuCommands.TREEINFO.getMatcher(input)) != null) {
            controller.printTreeInfo(matcher.group("treename"));
        } else if ((matcher = GameMenuCommands.MENU_ENTER.getMatcher(input)) != null) {
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
            System.out.println(controller.useTool(matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.FISH.getMatcher(input)) != null) {
            String fishingPole = matcher.group("fishingPole");
            System.out.println(controller.fish(fishingPole));
        }else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMatcher(input)) != null) {
            String itemName = matcher.group("itemName");
            int count = Integer.parseInt(matcher.group("count"));
            System.out.println(controller.cheatAddItem(itemName, count));
        }else if ((matcher = GameMenuCommands.PLANT.getMatcher(input)) != null) {
            System.out.println(controller.plantGrowable(matcher.group("seedName"), matcher.group("direction")).message());
        } else if ((matcher = GameMenuCommands.SHOWPLANT.getMatcher(input)) != null) {
            System.out.println(controller.showPlant(matcher.group("x"), matcher.group("y")).message());
        } else if ((matcher = GameMenuCommands.FERTALISE.getMatcher(input)) != null) {
            System.out.println(controller.fertalizeGrowable(matcher.group("fertilizer"), matcher.group("direction")).message());
        } else if ((matcher = GameMenuCommands.BUILDGREENHOUSE.getMatcher(input)) != null) {
            System.out.println(controller.buildGreenHouse().message());
        } else if ((matcher = HouseMenuCommands.SHOW_RECIPIES.getMatcher(input)) != null) {
            System.out.println(houseController.showRecipes());
        } else if ((matcher = HouseMenuCommands.CRAFT.getMatcher(input)) != null) {
            System.out.println(houseController.craft(matcher.group("itemName")));
        } else if ((matcher = HouseMenuCommands.PLACE_ITEM.getMatcher(input)) != null) {
            System.out.println(houseController.placeItem(matcher.group("itemName"), matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.TALK.getMatcher(input)) != null) {
            System.out.println(controller.talk(matcher.group("username"), matcher.group("message")).message());
        } else if ((matcher = GameMenuCommands.SHOW_TALK_HISTORY.getMatcher(input)) != null) {
            System.out.println(controller.showTalkHistory(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.HUG.getMatcher(input)) != null) {
            System.out.println(controller.hug(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMatcher(input)) != null) {
            System.out.println(controller.askMarriage(matcher.group("username"), matcher.group("ring")));
        } else if ((matcher = GameMenuCommands.RESPOND.getMatcher(input)) != null) {
            System.out.println(controller.respondToMarriage(matcher.group("response"), matcher.group("username")));
        } else if ((matcher = GameMenuCommands.START_TRADE.getMatcher(input)) != null) {
            controller.startTrade();
        } else if ((matcher = GameMenuCommands.CHEAT_WALK.getMatcher(input)) != null) {
            System.out.println(controller.cheatWalk(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y"))).message());
        } else {
            System.out.println("invalid command");
        }

    }

}
