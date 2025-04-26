package org.example.Controller;


import org.example.Main;
import org.example.Model.App;
import org.example.Model.Game;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Result;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.Tools.ToolType;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenuController implements MenuController {

    GameMenuCommands command;

    public Result createGame(String users, Scanner scanner) {
        App app = App.getInstance();
        User creator = app.getLoggedInUser();

        if (creator == null)
            return new Result(false, "please login first!");

        // Split usernames and clean empty entries (e.g., if user types extra spaces)
        List<String> usernames = Arrays.stream(users.trim().split("\\s+"))
                .filter(usersString -> !usersString.isEmpty())
                .toList();

        if (usernames.isEmpty())
            return new Result(false, "you must specify at least one username!");

        if (usernames.size() > 3)
            return new Result(false, "you can specify up to 3 usernames!");

        // Check if the creator is already in a game
        for (Game game : app.getActiveGames()) {
            if (game.hasUser(creator))
                return new Result(false, "you are already in another game!");
        }

        ArrayList<User> players = new ArrayList<>();
        players.add(creator); // Add the logged-in user first

        for (String username : usernames) {
            User user = app.getUserByUsername(username);
            if (user == null)
                return new Result(false, "invalid username: " + username);

            // Check if the user is already in a game
            Game game = app.getGameByUser(user);
            if (game != null) {
                return new Result(false, username + " is already in another game!");
            }
            players.add(user);
        }
        for (User player : players) {
            player.updateGameFields();
        }
        // Create and add the game
        Game newGame = new Game(players, creator, creator);
        app.getActiveGames().add(newGame);
        app.setCurrentGame(newGame);

        handleMapSelection(players, scanner);

        return new Result(true, "game created successfully!");
    }

    private void handleMapSelection(List<User> players, Scanner scanner) {
        for (User player : players) {
            System.out.println("hey " + player.getUsername() + " choose between map 1 or map 2");
            boolean hasChosen = false;
            while (!hasChosen) {
                String input = scanner.nextLine().trim();
                Matcher matcher = GameMenuCommands.CHOOSE_MAP.getMatcher(input);
                if (matcher != null) {
                    int mapNumber = Integer.parseInt(matcher.group("mapNumber"));
                    if (mapNumber != 1 && mapNumber != 2) {
                        System.out.println("invalid map number");
                    } else {
                        // Call method to apply map to player ///////////////////////////
                        hasChosen = true;
                    }
                } else {
                    System.out.println("invalid command");
                }
            }
        }
    }

    public Result loadGame() {
        App app = App.getInstance();
        User user = app.getLoggedInUser();

        if (user == null)
            return new Result(false, "please login first!");

        Game savedGameToLoad = app.getGameByUser(user);

        if (savedGameToLoad == null)
            return new Result(false, "no saved game found!");

        savedGameToLoad.setMainPlayer(user);
        app.setCurrentGame(savedGameToLoad);
        return new Result(true, "game loaded successfully!");
    }

    public Result exitGame() {
        App app = App.getInstance();
        User currentUser = app.getLoggedInUser();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null)
            return new Result(false, "no active game to exit!");

        if (!currentGame.getMainPlayer().equals(currentUser))
            return new Result(false, "only the game owner can exit the game!");

        if (!currentGame.getCurrentPlayer().equals(currentUser)) // check if it's their turn
            return new Result(false, "you can only exit the game during your turn!");

        // Save the current game state
        for (User player : currentGame.getPlayers()) {
            player.updateMaxMoney();
        }
        app.saveActiveGames();

        // Exit game: go back to game menu
        app.setCurrentGame(null);
        return new Result(true, "game exited and saved successfully. returning to game menu...");
    }

    public Result startForceTerminateVote(Scanner scanner) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();
        User currentUser = app.getLoggedInUser();

        if (currentUser == null || currentGame == null)
            return new Result(false, "no active game!");

        if (currentGame.isVoteInProgress())
            return new Result(false, "a termination vote is already in progress!");

        // Start the vote and auto-approve for the initiator
        currentGame.setVoteInProgress(true);
        currentGame.getTerminationVotes().clear();
        currentGame.getTerminationVotes().put(currentUser, true);

        return new Result(true, "termination vote started. your vote is recorded as YES.");
    }


    public Result nextTurn(Scanner scanner) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null)
            return new Result(false, "no active game!");

        User pastUser = currentGame.getCurrentPlayer();
        pastUser.resetTurnEnergy();

        currentGame.goToNextTurn();

        User currentUser = currentGame.getCurrentPlayer();

        if (currentGame.isVoteInProgress() && !currentGame.getTerminationVotes().containsKey(currentUser)) {
            return voteToTerminateInteractive(scanner, currentUser);
        }
        handleEndOfDay();

        return new Result(true, "next turn started for " + currentUser.getUsername());
    }

    private Result voteToTerminateInteractive(Scanner scanner, User user) {
        Game currentGame = App.getInstance().getCurrentGame();

        System.out.println("a force terminate vote is in progress. you must vote first. enter 'yes' or 'no':");

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes")) {
                return voteToTerminate(true, user);
            } else if (input.equals("no")) {
                return voteToTerminate(false, user);
            } else {
                System.out.println("invalid vote. enter 'yes' or 'no':");
            }
        }
    }

    public Result voteToTerminate(boolean approve, User user) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null || !currentGame.isVoteInProgress())
            return new Result(false, "no active termination vote!");

        if (!currentGame.isUserTurn(user))
            return new Result(false, "you can only vote during your own turn!");

        if (currentGame.getTerminationVotes().containsKey(user))
            return new Result(false, "you have already voted!");

        currentGame.getTerminationVotes().put(user, approve);

        if (!approve) {
            currentGame.setVoteInProgress(false);
            currentGame.getTerminationVotes().clear();
            return new Result(true, "you voted NO. game termination cancelled.");
        }

        // Check if all players have voted yes
        if (currentGame.getTerminationVotes().size() == currentGame.getPlayers().size()) {
            for (User player : currentGame.getPlayers()) {
                player.updateMaxMoney();
            }
            app.getActiveGames().remove(currentGame);
            app.setCurrentGame(null);
            app.saveActiveGames();
            return new Result(true, "game terminated by unanimous vote. returning to game menu...");
        }

        return new Result(true, "your vote is recorded as YES.");
    }
    private void handleEndOfDay() {
        App app = App.getInstance();
        Game game = app.getCurrentGame();
        if (game.getTimeAndDate().getHour() == 22) {

            for (User user : game.getPlayers()) {
//                if (user.canWalkHome()) {
//                    user.walkHome(); // This should reduce energy and move them to home
//                } else {
//                    user.faint(); // Player faints in place
//                }
            }

            // Skip time to 9 AM
            game.getTimeAndDate().skipToNextMorning();

            // Grow crops and update energy
            for (User user : game.getPlayers()) {
                user.resetEnergyForNewDay();
                //user.getFarm().growCropsOneDay();
               // user.getFarm().generateForageAndMine();
                //user.collectShippingBinProfits();
            }
            //TO DO: update crops and days left to die delete crops and tree

            // Update weather for the new day
           // game.getCurrentWeatherType() = WeatherType.randomWeather();
        }
    }

//    public Result cheatAdvanceTime(int hours){}
//    public Result cheatAdvanceDate(int days){}
    public void strikeRandomFarm() {
    }

    public void CheatStrikeLightening() {
    }

//    public Result showCurrentWeather(){}
//    public Result showPredictedWeather(){}
//    public Result cheatSetWeather(){}
//    public void buildGreenhouse(){}
//    public Result walk(int x, int y){}
//    public Result printMap(int x, int y, int size){}
//    public Result helpReadMap(){}
//    public Result showEnergy(){}
//    public Result cheatEnergySet(int value){}
//    public Result cheatEnergyUnlimited(){}
//    public Result trashInventory(ToolType item, int quantity){}
//    public Result showTradingMenu(){}


    public void startNewDay() {
    }

    public Result printDayOfWeek() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Day of week: " + currentGame.getTimeAndDate().getDayOfWeek());
    }

    public Result printDateTime() {
        Game currentGame = App.getInstance().getCurrentGame();
        TimeAndDate currentDate = currentGame.getTimeAndDate();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Date: " + currentDate.getHour() + " hour of day " + currentDate.getDay());
    }

    public Result printDate() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Day of season: " + currentGame.getTimeAndDate().getDay());

    }

    public Result printHour() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Hour: " + currentGame.getTimeAndDate().getHour());
    }

    public Result printSeason() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current Season: " + currentGame.getTimeAndDate().getSeason());

    }


    public Result cheatAdvanceDate(String number) {
    int days = Integer.parseInt(number);
        if (days <= 0) {
            return new Result(false, "invalid number of days!");
        }

        Game game = App.getInstance().getCurrentGame();
        if (game == null) return new Result(false, "no active game!");

        for (int i = 0; i < days; i++) {
            // Force day to end if not already at 22
            game.getTimeAndDate().setHour(22);
            handleEndOfDay(); // Will skip to 9 AM next day and apply effects
        }

        return new Result(true, "advanced time by " + days + " days.");
    }

    public Result cheatAdvanceTime(String number) {
        int hours = Integer.parseInt(number);
        if (hours <= 0) {
            return new Result(false, "invalid number of hours!");
        }

        Game game = App.getInstance().getCurrentGame();
        if (game == null) return new Result(false, "no active game!");

        for (int i = 0; i < hours; i++) {
            game.getTimeAndDate().advanceHour(); // You need to implement this method
            if (game.getTimeAndDate().getHour() == 22) {
                handleEndOfDay();
            }
        }

        return new Result(true, "advanced time by " + hours + " hours.");
    }
}
//package org.example.Controller;
//
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//import java.util.stream.Collectors;
//import java.awt.Point;
//import java.lang.reflect.Type;
//
//
//import org.example.Model.*;
//        import org.example.Model.ConfigTemplates.FarmLoader;
//import org.example.Model.ConfigTemplates.FarmTemplate;
//import org.example.Model.ConfigTemplates.FarmTemplateManager;
//import org.example.Model.Growables.ForagingCropType;
//import org.example.Model.Growables.GrowableFactory;
//import org.example.Model.Growables.TreeType;
//import org.example.Model.MapManagement.MapOfGame;
//import org.example.Model.MapManagement.Tile;
//import org.example.Model.MapManagement.TileType;
//import org.example.Model.Places.Farm;
//import org.example.Model.Places.GreenHouse;
//import org.example.Model.Places.Habitat;
//import org.example.Model.Places.House;
//import org.example.Model.Places.Quarry;
//import org.example.Model.TimeManagement.Season;
//import org.example.Model.Tools.ToolType;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//public class GameMenuController {
//
//    //GameMenuCommands command;
//
//    // public Result createNewGame(String username1, String username2, String username3){
//    //     //load farm.json            ONLY ONCEEEEEEE
//    //     if (FarmTemplateManager.getTemplates() == null) {
//    //         FarmTemplateManager.loadTemplates();
//    //     }
//    //     //create a new game and put it as currentgame in app
//    //     //for the newely created game create a map and initialize it with the function initializeMap that exists in MapOfGame class
//
//    // }
//
//    //we will call this method for every user
//    public void pickGameMap(User player, int mapNumber){
//        App app = App.getInstance();
//        Game currentGame = app.getCurrentGame();
//        MapOfGame mapOfGame = currentGame.getMap();
//        Farm playerFarm;
//        if(mapNumber % 2 == 0){
//            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_2");
//            //find an empty corner
//            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
//            playerFarm = new Farm(player, template, (int)farmCoordinate.getX(), (int)farmCoordinate.getY());
//        }
//        else{
//            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_1");
//            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
//            playerFarm = new Farm(player, template, (int)farmCoordinate.getX(), (int)farmCoordinate.getY());
//        }
//        //update tile types that are in the farm
//        Tile[][] map = mapOfGame.getMap();
//        for(int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++){
//            for(int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++){
//                Tile tile = map[y][x];
//                tile.setTileOwner(player);
//                boolean foundSpecial = false;
//
//                for (Habitat lake : playerFarm.getLake()) {
//                    if (isInHabitat(x, y, lake)) {
//                        tile.setType(TileType.LAKE);
//                        tile.setWalkable(false);
//                        foundSpecial = true;
//                        break;
//                    }
//                }
//
//                Quarry quarry = playerFarm.getQuarry();
//                Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight());
//                if (!foundSpecial && isInHabitat(x, y, quarryHabitat)) {
//                    tile.setType(TileType.QUARRY);
//                    tile.setWalkable(true);
//                    foundSpecial = true;
//                }
//
//                House house = playerFarm.getHouse();
//                Habitat houseHabitat = new Habitat(house.getX(), house.getY(), house.getWidth(), house.getHeight());
//                if (!foundSpecial && isInHabitat(x, y, houseHabitat)) {
//                    tile.setType(TileType.HOUSE);
//                    tile.setWalkable(true);
//                    foundSpecial = true;
//                }
//
//                Habitat houseWallHabitat = new Habitat(
//                        house.getX() - 1,
//                        house.getY() - 1,
//                        house.getWidth() + 2,
//                        house.getHeight() + 2
//                );
//
//                if (isOnHabitatBorder(x, y, houseWallHabitat, houseHabitat)) {
//                    tile.setType(TileType.WALL);
//                    tile.setWalkable(false);
//                }
//
//                GreenHouse greenHouse = playerFarm.getGreenHouse();
//                Habitat greenHouseHabitat = new Habitat(greenHouse.getX(), greenHouse.getY(), greenHouse.getWidth(), greenHouse.getHeight());
//                if(!foundSpecial && isInHabitat(x, y, greenHouseHabitat)){
//                    tile.setType(TileType.GREENHOUSE);
//                    tile.setWalkable(false);
//                    foundSpecial = true;
//                }
//
//                Habitat greenHouseWallHabitat = new Habitat(
//                        greenHouse.getX() - 1,
//                        greenHouse.getY() - 1,
//                        greenHouse.getWidth() + 2,
//                        greenHouse.getHeight() + 2
//                );
//
//                if (isOnHabitatBorder(x, y, greenHouseWallHabitat, greenHouseHabitat)) {
//                    tile.setType(TileType.WALL);
//                    tile.setWalkable(false);
//                }
//
//                if (x >= greenHouse.getX() && x < greenHouse.getX() + greenHouse.getWidth() && y == greenHouse.getY() - 1) {
//                    tile.setType(TileType.WATERCONTAINER);
//                    tile.setWalkable(false);
//                }
//
//                if (!foundSpecial) {
//                    tile.setType(TileType.FARM);
//                    tile.setWalkable(true); // walkable by default
//                }
//            }
//        }
//        List<Point> validTiles = new ArrayList<>();
//        for (int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++) {
//            for (int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++) {
//                if (map[y][x].getType() == TileType.FARM) {
//                    validTiles.add(new Point(x, y));
//                }
//            }
//        }
//
//        int numberOfForagingCrops = 50;
//        int numberOfTrees = 50;
//        Collections.shuffle(validTiles);
//
//        for (int i = 0; i < Math.min(numberOfForagingCrops, validTiles.size()); i++) {
//            Point p = validTiles.get(i);
//            map[p.y][p.x].setType(TileType.GROWABLE);
//            map[p.y][p.x].setContainedGrowable(GrowableFactory.getInstance().create(getRandomForagingCropBySeason(currentGame.getTimeAndDate().getSeason())));
//            map[p.y][p.x].setWalkable(false);
//        }
//
//        for (int i = 0; i < Math.min(numberOfTrees, validTiles.size()); i++) {
//            Point p = validTiles.get(i);
//            Tile tile = map[p.y][p.x];
//
//            TreeType treeType = getRandomForagingTree();
//            tile.setContainedGrowable(GrowableFactory.getInstance().create(treeType.getSource()));
//            tile.setType(TileType.GROWABLE); // Optional: assign a specific tile type
//            tile.setWalkable(false);
//        }
//    }
//
//    // public Result loadGame(){}
//    // public Result exitGame(){}
//    // public Result removeGame(){}
//    // public Result nextTurn(){}
//    // public Result showTime(){}
//    // public Result showDate(){}
//    // public Result showTimeAndDate(){}
//    // public Result showDayOfWeek(){}
//    // public Result showSeason(){}
//    // public Result cheatAdvanceTime(int hours){}
//    // public Result cheatAdvanceDate(int days){}
//    public void strikeRandomFarm(){}
//
//    public void CheatStrikeLightening(){}
//
//    // public Result showCurrentWeather(){}
//    // public Result showPredictedWeather(){}
//    // public Result cheatSetWeather(){}
//    // public void buildGreenhouse(){}
//    // //walk home method
//    // public Result walk(int x, int y){
//    //     //in every step call if in any place energy = 0 call faint method
//    // }
//    public void printMap(int x, int y, int size){
//        Game game = App.getInstance().getCurrentGame();
//        Tile[][] map = game.getMap().getMap();
//        for(int i = y; i < y + size; i++){
//            for(int j = x; j < x + size; j++){
//                System.out.print(map[y][x].getType().getLetterToPrint());
//            }
//            System.out.print("\n");
//        }
//    }
//    // public Result helpReadMap(){}
//    // public Result showEnergy(){}
//    // public Result cheatEnergySet(int value){}
//    // public Result cheatEnergyUnlimited(){}
//    // public Result trashInventory(ToolType item, int quantity){}
//    // public Result showTradingMenu(){}
//
//
//    public void startNewDay(){}
//
//
//    public List<Farm> loadFarmTemplates() {
//        Gson gson = new Gson();
//        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("farm.json")) {
//            if (inputStream == null) {
//                throw new RuntimeException("farm.json not found in resources.");
//            }
//
//            InputStreamReader reader = new InputStreamReader(inputStream);
//            Type listType = new TypeToken<List<FarmTemplate>>() {}.getType();
//            return gson.fromJson(reader, listType);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    public static Point isCornerAvailable(Tile[][] map, int width, int height) {
//        int mapHeight = map.length;
//        int mapWidth = map[0].length;
//
//        int[][] corners = {
//                {0, 0},                             // Top-left
//                {mapWidth - width, 0},             // Top-right
//                {0, mapHeight - height},           // Bottom-left
//                {mapWidth - width, mapHeight - height} // Bottom-right
//        };
//
//        for (int[] corner : corners) {
//            int startX = corner[0];
//            int startY = corner[1];
//
//            if (startX < 0 || startY < 0 || startX + width > mapWidth || startY + height > mapHeight)
//                continue;
//
//            boolean isEmpty = true;
//            for (int y = startY; y < startY + height && isEmpty; y++) {
//                for (int x = startX; x < startX + width; x++) {
//                    if (map[y][x].getType() != TileType.EMPTY) {
//                        isEmpty = false;
//                        break;
//                    }
//                }
//            }
//
//            if (isEmpty) {
//                return new Point(startX, startY);
//            }
//        }
//
//        return null;
//    }
//
//    public void creatingRandomForagingForFarm(){
//        //rock ??
//        //choose between foraging mineral types to put in Quarry
//        //choose between foraging crops to put in map
//
//    }
//
//    private boolean isInHabitat(int x, int y, Habitat h) {
//        return x >= h.getX() && x < h.getX() + h.getWidth()
//                && y >= h.getY() && y < h.getY() + h.getHeight();
//    }
//
//    private boolean isOnHabitatBorder(int x, int y, Habitat wallHabitat, Habitat mainHabitat) {
//        // Tile is inside wallHabitat but NOT inside mainHabitat
//        return isInHabitat(x, y, wallHabitat) && !isInHabitat(x, y, mainHabitat);
//    }
//
//    private ForagingCropType getRandomForagingCropBySeason(Season currentSeason) {
//        List<ForagingCropType> valid = Arrays.stream(ForagingCropType.values())
//                .filter(crop -> crop.getSeason().contains(currentSeason))
//                .collect(Collectors.toList());
//
//        return valid.get(new Random().nextInt(valid.size()));
//    }
//
//    private TreeType getRandomForagingTree() {
//        List<TreeType> valid = Arrays.stream(TreeType.values())
//                .filter(TreeType::getIsForagingTree)
//                .collect(Collectors.toList());
//
//        return valid.get(new Random().nextInt(valid.size()));
//    }
//
//
//
//
//
//
//}
