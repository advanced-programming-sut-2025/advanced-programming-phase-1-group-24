package org.example.Controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Main;
import org.example.Model.*;
import org.example.Model.Animals.Animal;
import org.example.Model.Animals.AnimalProduct;
import org.example.Model.Animals.AnimalProductType;
import org.example.Model.Animals.AnimalType;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplateManager;
import org.example.Model.Growables.ForagingCropType;
import org.example.Model.Growables.GrowableFactory;
import org.example.Model.Growables.TreeType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Places.*;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.Tools.FishingPole;
import org.example.Model.Tools.Tool;
import org.example.Model.Tools.ToolType;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class GameMenuController implements MenuController {

    GameMenuCommands command;

    //    public Result createGame(String users, Scanner scanner) {
//        App app = App.getInstance();
//        User creator = app.getLoggedInUser();
//
//        if (creator == null)
//            return new Result(false, "please login first!");
//
//        // Split usernames and clean empty entries (e.g., if user types extra spaces)
//        List<String> usernames = Arrays.stream(users.trim().split("\\s+"))
//                .filter(usersString -> !usersString.isEmpty())
//                .toList();
//
//        if (usernames.isEmpty())
//            return new Result(false, "you must specify at least one username!");
//
//        if (usernames.size() > 3)
//            return new Result(false, "you can specify up to 3 usernames!");
//
//        // Check if the creator is already in a game
//        for (Game game : app.getActiveGames()) {
//            if (game.hasUser(creator))
//                return new Result(false, "you are already in another game!");
//        }
//
//        ArrayList<User> players = new ArrayList<>();
//        players.add(creator); // Add the logged-in user first
//
//        for (String username : usernames) {
//            User user = app.getUserByUsername(username);
//            if (user == null)
//                return new Result(false, "invalid username: " + username);
//
//            // Check if the user is already in a game
//            Game game = app.getGameByUser(user);
//            if (game != null) {
//                return new Result(false, username + " is already in another game!");
//            }
//            players.add(user);
//        }
//        for (User player : players) {
//            player.updateGameFields();
//        }
//        // Create and add the game
//        Game newGame = new Game(players, creator, creator);
//        //load farm.json            ONLY ONCEEEEEEE
//        if (FarmTemplateManager.getTemplates() == null) {
//            FarmTemplateManager.loadTemplates();
//        }
//        //create a new game and put it as currentgame in app
//        //for the newely created game create a map and initialize it with the function initializeMap that exists in MapOfGame class
//
//        app.getActiveGames().add(newGame);
//        app.setCurrentGame(newGame);
//
//        handleMapSelection(players, scanner);
//
//        return new Result(true, "game created successfully!");
//    }
    public Result createGame(String users, Scanner scanner) {
        App app = App.getInstance();
        User creator = app.getLoggedInUser();

        if (creator == null)
            return new Result(false, "please login first!");

        // Split usernames and clean empty entries
        List<String> usernames = Arrays.stream(users.trim().split("\\s+"))
                .filter(s -> !s.isEmpty())
                .toList();

        if (usernames.isEmpty())
            return new Result(false, "you must specify at least one username!");
        if (usernames.size() > 3)
            return new Result(false, "you can specify up to 3 usernames!");

        // Build a set of all users already in any active game
        Set<User> usersInGames = app.getActiveGames().stream()
                .flatMap(game -> game.getPlayers().stream())
                .collect(Collectors.toSet());

        if (usersInGames.contains(creator))
            return new Result(false, "you are already in another game!");

        // Try to resolve all usernames to actual users
        List<User> invitedUsers = new ArrayList<>();
        for (String username : usernames) {
            User user = app.getUserByUsername(username);
            if (user == null)
                return new Result(false, "invalid username: " + username);
            if (usersInGames.contains(user))
                return new Result(false, username + " is already in another game!");
            invitedUsers.add(user);
        }

        // Add the creator as the first player
        ArrayList<User> players = new ArrayList<>();
        players.add(creator);
        players.addAll(invitedUsers);

        for (User player : players) {
            player.updateGameFields(); // whatever this does
        }

        Game newGame = new Game(players, creator, creator);

        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates(); // only once
        }

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
                        pickGameMap(player, mapNumber);
                        hasChosen = true;
                    }
                } else {
                    System.out.println("invalid command");
                }
            }
        }
        printMap(0, 0, 100);
    }

    //we will call this method for every user
    public void pickGameMap(User player, int mapNumber) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();
        MapOfGame mapOfGame = currentGame.getMap();
        Farm playerFarm;
        if (mapNumber % 2 == 0) {
            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_2");
            //find an empty corner
            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
            playerFarm = new Farm(player, template, (int) farmCoordinate.getX(), (int) farmCoordinate.getY());
            mapOfGame.addFarm(playerFarm);
        } else {
            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_1");
            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
            playerFarm = new Farm(player, template, (int) farmCoordinate.getX(), (int) farmCoordinate.getY());
            mapOfGame.addFarm(playerFarm);
        }
        //update tile types that are in the farm
        Tile[][] map = mapOfGame.getMap();
        for (int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++) {
            for (int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++) {
                Tile tile = map[y][x];
                tile.setTileOwner(player);
                boolean foundSpecial = false;

                for (Habitat lake : playerFarm.getLake()) {

                    if (isInHabitat(x, y, lake)) {
                        tile.setType(TileType.LAKE);
                        tile.setWalkable(false);
                        foundSpecial = true;
                    }
                }

                Quarry quarry = playerFarm.getQuarry();
                Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight());

                if (!foundSpecial && isInHabitat(x, y, quarryHabitat)) {
                    tile.setType(TileType.QUARRY);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                House house = playerFarm.getHouse();
                Habitat houseHabitat = new Habitat(house.getX(), house.getY(), house.getWidth(), house.getHeight());
                if (!foundSpecial && isInHabitat(x, y, houseHabitat)) {
                    tile.setType(TileType.HOUSE);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                Habitat houseWallHabitat = new Habitat(
                        house.getX() - 1,
                        house.getY() - 1,
                        house.getWidth() + 2,
                        house.getHeight() + 2
                );

                if (isOnHabitatBorder(x, y, houseWallHabitat, houseHabitat)) {
                    tile.setType(TileType.WALL);
                    tile.setWalkable(false);
                }

                GreenHouse greenHouse = playerFarm.getGreenHouse();
                Habitat greenHouseHabitat = new Habitat(greenHouse.getX(), greenHouse.getY(), greenHouse.getWidth(), greenHouse.getHeight());
                if (!foundSpecial && isInHabitat(x, y, greenHouseHabitat)) {
                    tile.setType(TileType.GREENHOUSE);
                    tile.setWalkable(false);
                    foundSpecial = true;
                }

                Habitat greenHouseWallHabitat = new Habitat(
                        greenHouse.getX() - 1,
                        greenHouse.getY() - 1,
                        greenHouse.getWidth() + 2,
                        greenHouse.getHeight() + 2
                );

                if (isOnHabitatBorder(x, y, greenHouseWallHabitat, greenHouseHabitat)) {
                    tile.setType(TileType.WALL);
                    tile.setWalkable(false);
                }

                if (x >= greenHouse.getX() && x < greenHouse.getX() + greenHouse.getWidth() && y == greenHouse.getY() - 1) {
                    tile.setType(TileType.WATERCONTAINER);
                    tile.setWalkable(false);
                }

                if (!foundSpecial) {
                    tile.setType(TileType.FARM);
                    tile.setWalkable(true); // walkable by default
                }
            }
        }
        List<Point> validTiles = new ArrayList<>();
        for (int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++) {
            for (int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++) {
                if (map[y][x].getType() == TileType.FARM) {
                    validTiles.add(new Point(x, y));
                }
            }
        }

        int numberOfForagingCrops = 50;
        int numberOfTrees = 50;
        Collections.shuffle(validTiles);

        for (int i = 0; i < Math.min(numberOfForagingCrops, validTiles.size()); i++) {
            Point p = validTiles.get(i);
            map[p.y][p.x].setType(TileType.GROWABLE);
            map[p.y][p.x].setContainedGrowable(GrowableFactory.getInstance().create(getRandomForagingCropBySeason(currentGame.getTimeAndDate().getSeason())));
            map[p.y][p.x].setWalkable(false);
        }

        for (int i = 0; i < Math.min(numberOfTrees, validTiles.size()); i++) {
            Point p = validTiles.get(i);
            Tile tile = map[p.y][p.x];

            TreeType treeType = getRandomForagingTree();
            tile.setContainedGrowable(GrowableFactory.getInstance().create(treeType.getSource()));
            tile.setType(TileType.GROWABLE); // Optional: assign a specific tile type
            tile.setWalkable(false);
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
        UserDatabase.saveUsers(app.getUsers());
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


    //    public Result nextTurn(Scanner scanner) {
//        App app = App.getInstance();
//        Game currentGame = app.getCurrentGame();
//
//        if (currentGame == null)
//            return new Result(false, "no active game!");
//
//        User pastUser = currentGame.getCurrentPlayer();
//        pastUser.resetTurnEnergy();
//
//        currentGame.goToNextTurn();
//
//        User currentUser = currentGame.getCurrentPlayer();
//
//        if (currentGame.isVoteInProgress() && !currentGame.getTerminationVotes().containsKey(currentUser)) {
//            return voteToTerminateInteractive(scanner, currentUser);
//        }
//        handleEndOfDay();
//
//        return new Result(true, "next turn started for " + currentUser.getUsername());
//    }
    public Result nextTurn(Scanner scanner) {
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();

        if (currentGame == null)
            return new Result(false, "no active game!");
        goToNextTurn(currentGame);
        User currentUser = currentGame.getCurrentPlayer();
        if (currentGame.isVoteInProgress() && !currentGame.getTerminationVotes().containsKey(currentUser)) {
            return voteToTerminateInteractive(scanner, currentUser);
        }
        return new Result(true, "next turn started for " + currentUser.getUsername());
    }

    public void goToNextTurn(Game game) {
        List<User> players = game.getPlayers();
        int currentPlayerIndex = game.getCurrentPlayerIndex();
        int turnCounter = game.getTurnCounter();

        User currentPlayer;

        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
            turnCounter++;

            if (!currentPlayer.hasFainted()) {
                currentPlayer.resetTurnEnergy();
            }

            if (turnCounter == players.size()) {
                turnCounter = 0;
                game.advanceTimeByOneHour();
                handleEndOfDay();
            }
        } while (currentPlayer.hasFainted());

        // Update game state back
        game.setCurrentPlayer(currentPlayer);
        game.setCurrentPlayerIndex(currentPlayerIndex);
        game.setTurnCounter(turnCounter);
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
            UserDatabase.saveUsers(app.getUsers());
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
                for (Animal animal : user.getOwnedAnimals()) {
                    animal.updateProductEndDay();
                    animal.endOfDayUpdate();
                }
                //user.getFarm().growCropsOneDay();
                // user.getFarm().generateForageAndMine();
                //user.collectShippingBinProfits();
            }


            // Update weather for the new day
            game.setCurrentWeatherType(game.getTomorrowWeatherType());
            game.predictTomorrowWeather();
            game.getMap().applyLightningStrikeIfStormy(game.getCurrentWeatherType().isCausesLightning());
            // the lightning strike logic should go into your handleEndOfDay() function — specifically after the new day
            //starts and the weather is known, but before or during crop updates (so the lightning can damage crops befor growth).

            //TO DO: update crops and days left to die delete crops and tree
            //TO Do: upadte fpraging and
            //To Do:  update animal days left to produce
            //To Do: crows attack
            // To Do: update animal products

        }
    }

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
        return new Result(true, "current Date: " + currentDate.getHour() + " hour of day " +
                currentDate.getDay() + " of season:" + currentDate.getSeason());
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

    public static Point isCornerAvailable(Tile[][] map, int width, int height) {
        int mapHeight = map.length;
        int mapWidth = map[0].length;

        int[][] corners = {
                {0, 0},                             // Top-left
                {mapWidth - width, 0},             // Top-right
                {0, mapHeight - height},           // Bottom-left
                {mapWidth - width, mapHeight - height} // Bottom-right
        };

        for (int[] corner : corners) {
            int startX = corner[0];
            int startY = corner[1];

            if (startX < 0 || startY < 0 || startX + width > mapWidth || startY + height > mapHeight)
                continue;

            boolean isEmpty = true;
            for (int y = startY; y < startY + height && isEmpty; y++) {
                for (int x = startX; x < startX + width; x++) {
                    if (map[y][x].getType() != TileType.EMPTY) {
                        isEmpty = false;
                        break;
                    }
                }
            }

            if (isEmpty) {
                return new Point(startX, startY);
            }
        }

        return null;
    }

    public void creatingRandomForagingForFarm() {
        //rock ??
        //choose between foraging mineral types to put in Quarry
        //choose between foraging crops to put in map

    }

    private boolean isInHabitat(int x, int y, Habitat h) {
        return x >= h.getX() && x < h.getX() + h.getWidth()
                && y >= h.getY() && y < h.getY() + h.getHeight();
    }

    private boolean isOnHabitatBorder(int x, int y, Habitat wallHabitat, Habitat mainHabitat) {
        // Tile is inside wallHabitat but NOT inside mainHabitat
        return isInHabitat(x, y, wallHabitat) && !isInHabitat(x, y, mainHabitat);
    }

    private ForagingCropType getRandomForagingCropBySeason(Season currentSeason) {
        List<ForagingCropType> valid = Arrays.stream(ForagingCropType.values())
                .filter(crop -> crop.getSeason().contains(currentSeason))
                .collect(Collectors.toList());

        return valid.get(new Random().nextInt(valid.size()));
    }

    private TreeType getRandomForagingTree() {
        List<TreeType> valid = Arrays.stream(TreeType.values())
                .filter(TreeType::getIsForagingTree)
                .collect(Collectors.toList());

        return valid.get(new Random().nextInt(valid.size()));
    }
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

    public void printMap(int x, int y, int size) {
        Game game = App.getInstance().getCurrentGame();
        Tile[][] map = game.getMap().getMap();
        for (int i = y; i < y + size; i++) {
            for (int j = x; j < x + size; j++) {
                System.out.print(map[i][j].getType().getLetterToPrint());
            }
            System.out.print("\n");
        }
    }

    public Result showCurrentWeather() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "current weather: " + currentGame.getCurrentWeatherType());

    }

    public Result showTomorrowWeather() {
        Game currentGame = App.getInstance().getCurrentGame();
        if (currentGame == null) {
            return new Result(false, "no active game!");
        }
        return new Result(true, "tomorrow weather: " + currentGame.getTomorrowWeatherType());

    }


    public Result cheatChangeWeather(String weather) {
        Game game = App.getInstance().getCurrentGame();
        if (game == null) {
            return new Result(false, "no active game!");
        }
        WeatherType desiredWeather;

        try {
            desiredWeather = WeatherType.valueOf(weather.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid weather type.");
        }

        Season currentSeason = game.getTimeAndDate().getSeason();
        if (!currentSeason.getWeatherTypes().contains(desiredWeather)) {
            return new Result(false, "This weather type is not allowed in the current season.");
        }

        game.setTomorrowWeatherType(desiredWeather);
        return new Result(true, "Tomorrow's weather has been changed to " + desiredWeather.name() + ".");
    }

    public Result showEnergy() {
        Game game = App.getInstance().getCurrentGame();
        if (game == null) {
            return new Result(false, "no active game!");
        }

        return new Result(true, "Current player energy: " + game.getCurrentPlayer().getEnergy());
    }

    public Result cheatUnlimitedEnergy() {
        Game game = App.getInstance().getCurrentGame();
        if (game == null) {
            return new Result(false, "No active game!");
        }

        User currentPlayer = game.getCurrentPlayer();
        currentPlayer.setEnergy(Integer.MAX_VALUE);
        currentPlayer.setCurrentTurnEnergy(Integer.MAX_VALUE);

        // Also update the maximums to make this change permanent/persistent
        currentPlayer.setMaxEnergy(Integer.MAX_VALUE);
        currentPlayer.setMaxEnergyTurn(Integer.MAX_VALUE);

        return new Result(true, "Energy is now unlimited!");
    }


    public Result cheatChangeEnergy(String value) {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();
        if (game == null) {
            return new Result(false, "No active game!");
        }

        int newEnergy;
        try {
            newEnergy = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid energy value!");
        }

        if (newEnergy < 0) {
            return new Result(false, "Energy cannot be negative!");
        }
        if (newEnergy > currentPlayer.getMaxEnergy()) {
            return new Result(false, "Energy cannot more than max energy!");
        }

        currentPlayer.setEnergy(newEnergy);
        // currentPlayer.setCurrentTurnEnergy(newEnergy);

        return new Result(true, "Player energy set to " + newEnergy + ".");
    }

    public Result cheatThor(String x, String y) {
        Game game = App.getInstance().getCurrentGame();
        if (game == null) {
            return new Result(false, "No active game!");
        }

        MapOfGame mapOfGame = game.getMap();
        Tile[][] map = mapOfGame.getMap();

        try {
            int xCoord = Integer.parseInt(x.trim());
            int yCoord = Integer.parseInt(y.trim());

            if (xCoord < 0 || xCoord >= mapOfGame.getWidth() || yCoord < 0 || yCoord >= mapOfGame.getHeight()) {
                return new Result(false, "out of bounds!");
            }

            Tile targetTile = map[yCoord][xCoord]; // note: map[y][x] because map is row-major

            mapOfGame.applyLightningEffect(targetTile);

            return new Result(true, "Lightning striked the tile (" + xCoord + "," + yCoord + ").");
        } catch (NumberFormatException e) {
            return new Result(false, "invalid x or y!.");
        }
    }



    public Result petAnimal(String animalName) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Tile playerTile = player.getCurrentTile();
        Animal animal = player.getAnimalByName(animalName);

        if (playerTile == null) {
            return new Result(false, "Player tile not found!");
        }
        if (animal == null) {
            return new Result(false, "Error: No animal found with the name " + animalName + ".");
        }
        Tile animalTile = animal.getCurrentTile();
        if (animalTile == null) {
            return new Result(false, "Error: animal tile not found.");
        }

        if (isAdjacent(playerTile, animalTile)) {
            animal.pet();
            return new Result(false, "You petted " + animalName + ". Friendship is now: " + animal.getFriendship());
        } else {
            return new Result(false, "Error: You must be in one of the 8 tiles around the animal to pet it.");
        }
    }

    private boolean isAdjacent(Tile t1, Tile t2) {
        int dx = Math.abs(t1.getX() - t2.getX());
        int dy = Math.abs(t1.getY() - t2.getY());
        return dx <= 1 && dy <= 1;
        //&& !(dx == 0 && dy == 0); // Exclude the same tile
    }

    public Result cheatAnimalFriendship(String name, String amount) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(name);
        int amountInt = Integer.parseInt(amount);
        if (amountInt < 0 || amountInt > 1000) {
            return new Result(false, "Invalid amount: must be a positve number between 0 and 1000.");
        }
        if (animal == null) {
            return new Result(false, "Error: No animal found with the name " + name + ".");
        }
        animal.setFriendship(amountInt);
        return new Result(true, "Friendship of " + name + " set to " + amountInt + ".");
    }


    public Result showOwnedAnimals() {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();

        if (player.getOwnedAnimals().isEmpty()) {
            return new Result(true, "You have no animals.");
        }
        StringBuilder sb = new StringBuilder("Owned animals:\n");
        for (Animal animal : player.getOwnedAnimals()) {
            sb.append("- ").append(animal.getName())
                    .append(" (Friendship: ").append(animal.getFriendship())
                    .append(", Fed today: ").append(animal.isFedToday())
                    .append(", Petted today: ").append(animal.isPettedToday())
                    .append(")\n");
        }

        return new Result(true, sb.toString());
    }

    public Result feedHay(String name) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(name);
        if (animal == null) {
            return new Result(false, "No animal named " + name + " found.");
        }

        if (animal.isFedToday()) {
            return new Result(false, name + " has already been fed today.");
        }

        // check for hay in storage and deduct one unit here

        animal.feed(); // sets fedToday = true and increases friendship
        return new Result(true, name + " was fed with hay.");
    }

    public Result shepherdAnimal(String name, String x, String y) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        int targetX = Integer.parseInt(x);
        int targetY = Integer.parseInt(y);
        Animal animal = player.getAnimalByName(name);
        Farm farm = game.getMap().getFarmByOwner(player);

        if (animal == null) {
            return new Result(false, "No animal named " + name + " found.");
        }
        Tile animalTile = animal.getCurrentTile();
        if (animalTile == null) {
            return new Result(false, "Error: animal tile not found.");
        }

        // Validate target location is inside player's farm bounds
        if (targetX < farm.getX() || targetX >= farm.getX() + farm.getWidth() ||
                targetY < farm.getY() || targetY >= farm.getY() + farm.getHeight()) {
            return new Result(false, "The coordinates (" + targetX + "," + targetY + ") are outside your farm.");
        }

        // Check weather condition — animals can only go out in SUNNY weather
        if (game.getCurrentWeatherType() != WeatherType.SUNNY) {
            return new Result(false, "Cannot shepherd animals outside in bad weather.");
        }

        // Check tile validity
        Tile targetTile = game.getMap().getTile(targetX, targetY);
        if (targetTile == null || !targetTile.getisWalkable()) {
            return new Result(false, "Invalid location for shepherding.");
        }

        // Move animal to the target tile
        animal.setCurrentTile(targetTile);

        // Feed the animal based on its location
        if (!animal.updateIsInHabitat()) {
            animal.feed(); // Assume this feeds the animal fresh grass
        }
//        else {
//            boolean fed = player.useHay(1); // Try to feed from hay (1 unit)
//            if (!fed) {
//                return new Result(false, name + " could not be fed (no hay available).");
//            }
//            animal.feedWithHay();
//        }
        return new Result(true, name + " was shepherded to (" + targetX + ", " + targetY + ").");
    }

    public Result collectProduct(String name) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(name);
        if (animal == null)
            return new Result(false, "No animal found with name: " + name);

        AnimalProduct product = animal.getProduct();
        if (product == null)
            return new Result(false, "This animal has no product to collect.");

        AnimalType type = animal.getAnimalType();

        // Check tool requirement for cow, goat, sheep
        if (type == AnimalType.COW || type == AnimalType.GOAT) {
            //check if the player have milk pail
            if (!player.getBackpack().hasTool(""))
                return new Result(false, "You need a milk pail to collect from this animal.");
        } else if (type == AnimalType.SHEEP) {
            //check if the player have shear
            if (!player.getBackpack().hasTool(""))
                return new Result(false, "You need a shear to collect from this animal.");

        }
        // Check pig must be outside
        else if (type == AnimalType.PIG && animal.isInHabitat())
            return new Result(false, "Pig must be outside to collect its product.");

        AnimalProduct collected = animal.collectProduct();
        player.getBackpack().addItem(collected, 1); // assuming such method exists
        player.addSkillExperience(Skill.FARMING);

        return new Result(true, "Collected " + collected.getAnimalProductType() + " of " + collected.getProductQuality() + " quality.");
    }
    public Result showInventory() {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        return currentPlayer.getBackpack().showInventory();
    }

    public Result trashInventory(String itemName, int count) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        return currentPlayer.getBackpack().removeItem(itemName, count);
    }

    public Result equipTool(String toolName) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        return currentPlayer.getBackpack().equipTool(toolName);
    }

    public Result showCurrentTool() {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tool tool = currentPlayer.getEquippedTool();
        if (tool == null) {return new Result(false, "No equipped tool found.");}
        return new Result (true,tool.toString());
    }

    public Result showAllTools() {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        return currentPlayer.getBackpack().showTools();
    }

    public Result fish(String fishingPole) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Game currentGame = App.getInstance().getCurrentGame();
        MapOfGame map = currentGame.getMap();
        Tile currentTile = currentPlayer.getCurrentTile();
        FishingPole pole;
        for (Tool tool : currentPlayer.getBackpack().getTools()) {
            if (tool.getName().equalsIgnoreCase(fishingPole)) {
                pole = (FishingPole) tool;
                double modifier = currentGame.getCurrentWeatherType().getEnergyOfToolsModifier();
                return pole.useFishingPole(pole, map, currentTile, currentPlayer, currentGame, modifier);
            }
        }
        return new Result(false, "No fishing pole found.");
    }
//    public Result sellAnimal(String name) {
//    }
}
