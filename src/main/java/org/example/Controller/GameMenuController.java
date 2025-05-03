package org.example.Controller;


import org.example.Model.App;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplateManager;
import org.example.Model.Game;
import org.example.Model.Growables.*;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Places.*;
import org.example.Model.Result;
import org.example.Model.Things.Backpack;
import org.example.Model.Things.ForagingMineral;
import org.example.Model.Things.ForagingMineralType;
import org.example.Model.Things.Item;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.User;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class GameMenuController implements MenuController {

    GameMenuCommands command;
    private static final Random RANDOM = new Random();

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
        //load farm.json            ONLY ONCEEEEEEE
        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates();
        }
        //create a new game and put it as currentgame in app
        //for the newely created game create a map and initialize it with the function initializeMap that exists in MapOfGame class

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
                    foundSpecial = true;
                    tile.setWalkable(false);
                }

                if (x == house.getX() + house.getWidth() / 2 &&
                        y == house.getY() + house.getHeight()) {
                    tile.setType(TileType.DOOR);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                GreenHouse greenHouse = playerFarm.getGreenHouse();
                Habitat greenHouseHabitat = new Habitat(greenHouse.getX(), greenHouse.getY(), greenHouse.getWidth(), greenHouse.getHeight());
                if (!foundSpecial && isInHabitat(x, y, greenHouseHabitat)) {
                    tile.setType(TileType.GREENHOUSE);
                    tile.setWalkable(false);  // it is false because it is not fixed yet
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
                    foundSpecial = true;
                    tile.setWalkable(false);
                }

                if (x >= greenHouse.getX() && x < greenHouse.getX() + greenHouse.getWidth() && y == greenHouse.getY() - 1) {
                    tile.setType(TileType.WATERCONTAINER);
                    foundSpecial = true;
                    tile.setWalkable(false);
                }

                if (x == greenHouse.getX() + greenHouse.getWidth() / 2 &&
                        y == greenHouse.getY() + greenHouse.getHeight()) {
                    tile.setType(TileType.DOOR);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                if (!foundSpecial) {
                    tile.setType(TileType.FARM);
                    tile.setWalkable(true); // walkable by default
                }
            }
        }

        Quarry quarry = playerFarm.getQuarry();
        Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight());
        List<Tile> quarryTiles = new ArrayList<>();

        for (int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++) {
            for (int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++) {
                if (isInHabitat(x, y, quarryHabitat)) {
                    Tile tile = map[y][x];
                    quarryTiles.add(tile);
                }
            }
        }

        Collections.shuffle(quarryTiles);
        int mineralsToPlace = 5;

        for (int i = 0; i < mineralsToPlace && i < quarryTiles.size(); i++) {
            Tile tile = quarryTiles.get(i);
            ForagingMineralType mineral = getRandomForagingMineral();
            tile.setWalkable(false);
            tile.setContainedItem(new ForagingMineral(mineral));
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
            map[p.y][p.x].setProductOfGrowable(GrowableFactory.getInstance().create(getRandomForagingCropBySeason(currentGame.getTimeAndDate().getSeason())));
            map[p.y][p.x].setWalkable(false);
            //System.out.println(map[p.y][p.x].getContainedGrowable().getForagingCropType());
        }

        validTiles.subList(0, Math.min(numberOfForagingCrops, validTiles.size())).clear();

        for (int i = 0; i < Math.min(numberOfTrees, validTiles.size()); i++) {
            Point p = validTiles.get(i);
            Tile tile = map[p.y][p.x];

            TreeType treeType = getRandomForagingTree();
            tile.setContainedGrowable(GrowableFactory.getInstance().create(treeType.getSource()));
            tile.setWalkable(false);
            //System.out.println(map[p.y][p.x].getContainedGrowable().getTreeType());
        }
        player.setCurrentTile(playerFarm.getRandomFarmTile(map));
        System.out.println("You are starting at coordinates " + player.getCurrentTile().getX() + " " + player.getCurrentTile().getY());
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

        if (valid.isEmpty()) {
            return null;
        }
        return valid.get(RANDOM.nextInt(valid.size()));
    }

    private SourceType getRandomForagingSourceBySeason(Season currentSeason) {
        List<SourceType> valid = Arrays.stream(SourceType.values())
                .filter(SourceType::getIsForagingSeed)
                .filter(source -> source.getForagingSeedSeason().contains(currentSeason))
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            return null;
        }
        return valid.get(RANDOM.nextInt(valid.size()));
    }


    private TreeType getRandomForagingTree() {
        List<TreeType> valid = Arrays.stream(TreeType.values())
                .filter(TreeType::getIsForagingTree)
                .collect(Collectors.toList());

        if (valid.isEmpty()) {
            return null;
        }
        return valid.get(RANDOM.nextInt(valid.size()));
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

    public Result printMap(String column,String row,String sizeOfMap){
        int x = Integer.parseInt(column);
        int y = Integer.parseInt(row);
        int size = Integer.parseInt(sizeOfMap);
        Game game = App.getInstance().getCurrentGame();
        if(game.getMap().getHeight() < size + y || game.getMap().getWidth() < size + x){
            return new Result(false, "invalid size");
        }
        if(x > game.getMap().getWidth() || y > game.getMap().getHeight()){
            return new Result(false, "invalid coordinates");
        }
        Tile[][] map = game.getMap().getMap();
        for (int i = y; i < y + size; i++) {
            for (int j = x; j < x + size; j++) {
                TileType type = map[i][j].getType();
                if(map[i][j].getContainedGrowable() != null){
                    if(map[i][j].getContainedGrowable().getTreeType() != null){
                        System.out.print("\u001B[32mT\u001B[0m");
                    }
                    else if(map[i][j].getContainedGrowable().getCropType() != null){
                        System.out.print("\u001B[32mC\u001B[0m");
                    }
                }
                else if(map[i][j].getProductOfGrowable() != null){
                    System.out.print("\u001B[38;5;22mg\u001B[0m");
                }
                else if(map[i][j].getContainedItem() != null){
                    System.out.print("\u001B[38;5;208mf\u001B[0m");
                }
                else System.out.print(type.coloredSymbol());
            }
            System.out.println();
        }
        return new Result(true, "");
    }
    public void helpReadMap(){
        for (TileType type : TileType.values()) {
            System.out.println(type.name() + " (" + type.getLetterToPrint() + "): " + type.coloredSymbol());
        }
    }


    public void walkTo(String destX, String destY, Scanner scanner) {
        int targetX = Integer.parseInt(destX);
        int targetY = Integer.parseInt(destY);
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();

        if(App.getInstance().getCurrentGame().getMap().isInsideAnyFarm(targetX, targetY) != null && map[targetY][targetX].getTileOwner() != player){
            System.out.println("You are not allowed to walk to another player's farm!");
            return;
        }
        int rows = map.length;
        int cols = map[0].length;

        if(targetX >= cols || targetY >= rows){
            System.out.println("invalid coordinates");
            return;
        }

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        // To store best energy so far for each tile
        int[][] minEnergy = new int[rows][cols];
        for (int[] row : minEnergy) Arrays.fill(row, Integer.MAX_VALUE);

        // To rebuild the path later
        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> cameFromDir = new HashMap<>();
        Map<String, Integer> energyUsed = new HashMap<>();

        // BFS Queue: {x, y, distance, turns, dirIndex}
        Queue<int[]> queue = new LinkedList<>();

        Tile currentTile = player.getCurrentTile();
        int startX = currentTile.getX();
        int startY = currentTile.getY();

        for (int d = 0; d < 4; d++) {
            queue.add(new int[]{startX, startY, 0, 0, d});
            minEnergy[startY][startX] = 0;
        }

        String bestEnd = null;
        int bestEnergy = Integer.MAX_VALUE;
        int bestTurns = 0;
        int bestDist = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0], y = curr[1], dist = curr[2], turns = curr[3], dir = curr[4];

            int energy = dist + 10 * turns;

            if (x == targetX && y == targetY && energy < bestEnergy) {
                bestEnergy = energy;
                bestDist = dist;
                bestTurns = turns;
                bestEnd = x + "," + y;
            }

            for (int d = 0; d < 4; d++) {
                int nx = x + directions[d][0];
                int ny = y + directions[d][1];

                if (nx < 0 || ny < 0 || nx >= cols || ny >= rows) continue;
                if (!map[ny][nx].getisWalkable()) continue;

                int newTurns = (d == dir) ? turns : turns + 1;
                int newEnergy = dist + 1 + 10 * newTurns;

                if (newEnergy < minEnergy[ny][nx]) {
                    minEnergy[ny][nx] = newEnergy;
                    queue.add(new int[]{nx, ny, dist + 1, newTurns, d});
                    parent.put(nx + "," + ny, x + "," + y);
                    cameFromDir.put(nx + "," + ny, d);
                    energyUsed.put(nx + "," + ny, newEnergy);
                }
            }
        }

        if (bestEnd == null) {
            System.out.println("No path found");
            return;
        }

        // Rebuild the path
        List<String> path = new ArrayList<>();
        String step = bestEnd;
        while (step != null) {
            path.add(step);
            step = parent.get(step);
        }
        Collections.reverse(path);

        System.out.println("Path:");
        for (String pos : path) {
            String[] parts = pos.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            Tile tile = map[y][x]; // Assuming 'map' is your Tile[][]
            TileType type = tile.getType();

            System.out.println(" -> " + pos + "," + type);
        }

        bestEnergy = bestEnergy /20;

        System.out.println( "min Energy: " + bestEnergy);
        System.out.print("Do you want to walk there? (yes/no): ");
        String response = scanner.nextLine();

        if (!response.equalsIgnoreCase("yes")) {
            System.out.println("Walk cancelled.");
            return;
        }

        int currentEnergy = player.getEnergy();
        if (bestEnergy <= currentEnergy) {
            // Enough energy, walk fully
            for (String pos : path) {
                String[] parts = pos.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);
            }
            player.setEnergy(currentEnergy - bestEnergy);
            System.out.println("Walked to destination. Energy left: " + player.getEnergy());
        } else {
            // Not enough energy, walk as far as possible
            String lastReachable = null;
            for (String pos : path) {
                int rawEnergy = energyUsed.getOrDefault(pos, Integer.MAX_VALUE);
                int required = rawEnergy / 20;
                if (required > currentEnergy) break;
                lastReachable = pos;
            }

            if (lastReachable != null) {
                String[] parts = lastReachable.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);

                int usedEnergy = energyUsed.get(lastReachable) / 20;
                player.setEnergy(player.getEnergy() - usedEnergy);
                System.out.println("You fainted at " + lastReachable + ". Energy left: " + player.getEnergy());
            } else {
                System.out.println("You don’t have enough energy to move.");
            }
        }
    }

    public void printCraftInfo(String craftName) {
        for (CropType crop : CropType.values()) {
            if (crop.getName().equalsIgnoreCase(craftName)) {
                System.out.println("Name: " + crop.getName());
                System.out.println("Source: " + crop.getSource().getName());
                System.out.println("Total Harvest Time: " + crop.getTotalHarvestTime());
                System.out.println("One Time: " + crop.oneTime());
                System.out.println("Regrowth Time: " + crop.getRegrowthTime());
                System.out.println("Base Sell Price: " + crop.getBaseSellPrice());
                System.out.println("Is Edible: " + crop.getIsEdible());
                System.out.println("Energy: " + crop.getEnergy());
                System.out.println("Seasons: " + crop.getSeasons());
                System.out.println("Can Be Giant: " + crop.getCanBeGiant());
                System.out.println("Stages: " + crop.getStages());
                return;
            }
        }
        System.out.println("Crop with name '" + craftName + "' not found.");
    }

    public void printTreeInfo(String treeName){
        for (TreeType tree : TreeType.values()) {
            if (tree.getName().equalsIgnoreCase(treeName)) {
                System.out.println("Name: " + tree.getName());
                System.out.println("Source: " + tree.getSource().getName());
                System.out.println("Total Grow Time: " + tree.getTotalHarvestTime());
                System.out.println("Fruit Type: " + tree.getFruitType().getName());
                System.out.println("Is Fruit Edible: " + tree.getFruitType().getIsFruitEdible());
                System.out.println("Fruit harvest cycle: " + tree.getFruitType().getFullHarvestCycle());
                System.out.println("Fruit Base sell Price: " + tree.getFruitType().getFruitBaseSellPrice());
                System.out.println("Fruit Energy: " + tree.getFruitType().getFruitEnergy());
                System.out.println("Seasons: " + tree.getNormalSeasons());
                System.out.println("Stages: " + tree.getSatges());
                return;
            }
        }
        System.out.println("Tree with name '" + treeName + "' not found.");
    }

    private ForagingMineralType getRandomForagingMineral() {
        ForagingMineralType[] minerals = ForagingMineralType.values();
        return minerals[new Random().nextInt(minerals.length)];
    }

    public void randomForaging(){
        //call this every morning or knight
        Game currentGame = App.getInstance().getCurrentGame();
        Tile[][] map = currentGame.getMap().getMap();
        for(Farm farm : App.getInstance().getCurrentGame().getMap().getFarms()){
            for(int j = 0; j < map.length; j++){
                for(int i = 0; i < map[0].length; i++){
                    int rand = RANDOM.nextInt(100);
                    if(rand == 1 && map[j][i].getType() == TileType.FARM){
                        if(map[j][i].getProductOfGrowable() == null && map[j][i].getContainedGrowable() == null &&
                           map[j][i].getContainedItem() == null){
                            if(map[j][i].getIsPlowed()){
                                  map[j][i].setContainedGrowable(GrowableFactory.getInstance().create(getRandomForagingSourceBySeason(currentGame.getTimeAndDate().getSeason())));
                                  map[j][i].setWalkable(false);
                            }
                            else{
                                map[j][i].setProductOfGrowable(GrowableFactory.getInstance().create(getRandomForagingCropBySeason(currentGame.getTimeAndDate().getSeason())));
                                map[j][i].setWalkable(false);
                            }
                        }
                    }
                    else if(rand == 1 && map[j][i].getType() == TileType.QUARRY){
                        if(map[j][i].getContainedItem() == null){
                            ForagingMineralType mineral = getRandomForagingMineral();
                            map[j][i].setContainedItem(new ForagingMineral(mineral));
                            map[j][i].setWalkable(false);
                        }
                    }
                }
            }
        }
    }

//    public Result plantGrowable(String seedName, String direction){
//        SourceType sourceType = SourceType.fromName(seedName);
//        Backpack playerBackPack = App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack();
//        Growable growable = findGrowableInBackpackBySourceType(playerBackPack, sourceType);
//        if(growable == null){
//            return new Result(false, "Growable with name '" + seedName + "' not found in inventory.");
//        }
//        //playerBackPack.getInventoryItems().
//
//
//    }

    public void crowAttack(){                                                                   //TODO
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        Farm playerFarm = App.getInstance().getCurrentGame().getMap().getFarmByOwner(App.getInstance().getCurrentGame().getCurrentPlayer());
        int numOfGrowables = 0;
        for(int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++){
            for(int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++){
                if(map[y][x].getType() == TileType.FARM && map[y][x].getContainedGrowable() != null){
                    numOfGrowables++;
                }
            }
        }
        int rand = RANDOM.nextInt(100);
        if(numOfGrowables >= 16 && rand <= 25){


        }
    }



    public void updateGrowable(Tile tile){
        //this should be called at the end of the days
        //when we are not in the required season the growables won't grow in this function so naturally they won't produce any product
        Game currentGame = App.getInstance().getCurrentGame();
        Season currentSeason = currentGame.getTimeAndDate().getSeason();
        if(tile.getContainedGrowable() != null){
            if(tile.getContainedGrowable().getDaysLeftToDie() <= 0){
                tile.setContainedGrowable(null);
                tile.setProductOfGrowable(null);
                return;
            }
            if(!tile.getContainedGrowable().getIsWateredToday()){
                tile.getContainedGrowable().setDaysLeftToDie(tile.getContainedGrowable().getDaysLeftToDie() - 1);
                return;
            }
            Growable growable = tile.getContainedGrowable();
            if(growable != null && growable.getTreeType() != null && growable.getTreeType().getNormalSeasons().contains(currentSeason)){
                growable.setAge(growable.getAge() + 1);
                int currentStage = growable.getCurrentStage();
                int daysPast = 0;
                //manteghan current stage vaghti treetype pore 0 nist
                if(growable.getTreeType().getTotalHarvestTime() > growable.getAge()){
                    for(int i = 0; i < currentStage - 1; i++){
                        daysPast += growable.getTreeType().getSatges().get(i);
                    }
                    if(daysPast >= growable.getAge()){
                        growable.setCurrentStage(growable.getCurrentStage() + 1);
                    }
                }
                else {
                    if(tile.getProductOfGrowable() == null){
                        if(growable.getTreeType().getTotalHarvestTime() +
                                growable.getTreeType().getFruitType().getFullHarvestCycle() >= growable.getAge()){
                            growable.setAge(growable.getTreeType().getTotalHarvestTime());
                            Growable fruit = GrowableFactory.getInstance().create(growable.getTreeType().getSource());
                            fruit.setGrowableType(GrowableType.Fruit);
                            tile.setProductOfGrowable(fruit);
                        }
                    }
                }
            }
            else if(growable != null && growable.getCropType() != null && growable.getCropType().getSeasons().contains(currentSeason)){
                growable.setAge(growable.getAge() + 1);
                int currentStage = growable.getCurrentStage();
                int daysPast = 0;
                if(growable.getCropType().getTotalHarvestTime() > growable.getAge()){
                    for(int i = 0; i < currentStage - 1; i++){
                        daysPast += growable.getCropType().getStages().get(i);
                    }
                    if(daysPast >= growable.getAge()){
                        growable.setCurrentStage(growable.getCurrentStage() + 1);
                    }
                }
                else{
                    if(tile.getProductOfGrowable() == null){
                        if(growable.getCropType().oneTime()){
                            tile.setContainedGrowable(null);
                            Growable product = GrowableFactory.getInstance().create(growable.getCropType().getSource());
                            product.setGrowableType(GrowableType.CropProduct);
                            tile.setProductOfGrowable(product);
                        }
                        else{
                            if(growable.getCropType().getTotalHarvestTime() +
                                    growable.getCropType().getRegrowthTime() >= growable.getAge()){
                                growable.setAge(0);
                                growable.setCurrentStage(1);
                                Growable product = GrowableFactory.getInstance().create(growable.getCropType().getSource());
                                product.setGrowableType(GrowableType.CropProduct);
                                tile.setProductOfGrowable(product);
                            }
                        }
                    }
                }

            }
        }
    }

    public Growable findGrowableInBackpackBySourceType(Backpack playerBackPack, SourceType targetSourceType) {
        for (Item item : playerBackPack.getInventoryItems().keySet()) {
            if (item instanceof Growable growable) {
                if (growable.getSource() == targetSourceType) {
                    return growable;
                }
            }
        }
        return null; // or throw exception / return Optional if preferred
    }

}
