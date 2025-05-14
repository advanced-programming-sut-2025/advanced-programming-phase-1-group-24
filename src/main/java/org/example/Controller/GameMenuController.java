package org.example.Controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Main;
import org.example.Model.*;
import org.example.Model.Animals.Animal;
import org.example.Model.Animals.AnimalProduct;
import org.example.Model.Animals.AnimalProductType;
import org.example.Model.Animals.AnimalType;
import org.example.Model.App;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplateManager;
import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Gift;
import org.example.Model.Friendships.Message;
import org.example.Model.Friendships.Friendship;
import org.example.Model.Friendships.Message;
import org.example.Model.Growables.ForagingCropType;
import org.example.Model.Growables.GrowableFactory;
import org.example.Model.Growables.TreeType;
import org.example.Model.Game;
import org.example.Model.Growables.*;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Menus.GameMenuCommands;
import org.example.Model.Menus.Menu;
import org.example.Model.Places.*;
import org.example.Model.Reccepies.*;
import org.example.Model.Result;
import org.example.Model.Things.*;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;
import org.example.Model.Result;
import org.example.Model.Things.*;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.TimeAndDate;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.Tools.*;
import org.example.Model.User;
import org.example.Model.NPCManagement.*;

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
    private static final Random RANDOM = new Random();

//    public boolean checkEnergy() {
//        Game game = App.getInstance().getCurrentGame();
//        if (game == null) {
//            return true;
//        }
//        User player = game.getCurrentPlayer();
//        if (player.getCurrentTurnEnergy() <= 0 || player.hasFainted()) {
//            return false;
//        }
//        return true;
//    }

    public Result checkEnergy() {
        Game game = App.getInstance().getCurrentGame();
        if (game == null) {
            return new Result(true, "");
        }
        User player = game.getCurrentPlayer();
        if (player.hasFainted() || player.getEnergy() <= 0) {
            return new Result(false, "You have fainted :/ go to next turn! Your turn will be skipped through the day!");
        }
        if (player.getCurrentTurnEnergy() <= 0) {
            return new Result(false, "Your turn energy is over! Go to next turn and wait until your turn!");
        }
        return new Result(true, "");
    }

    public Result useTool(String direction) {
//        App.getInstance().getCurrentGame().getCurrentPlayer().setEquippedTool(App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().getTools().get(0));
        //App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().addItem(GrowableFactory.getInstance().create(SourceType.CauliflowerSeeds), 10);
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tool currentTool = player.getEquippedTool();
        if (currentTool == null) {
            return new Result(false, "You don't have an equipped tool");
        }
        int x = 0;
        int y = 0;
        if (direction.equals("up")) y--;
        else if (direction.equals("down")) y++;
        else if (direction.equals("left")) x--;
        else if (direction.equals("right")) x++;
        Tile currentTile = player.getCurrentTile();
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        if (currentTile.getX() + x < 0 || currentTile.getX() + x >= map[0].length || currentTile.getY() + y < 0 || currentTile.getY() + y >= map.length) {
            return new Result(false, "Direction is wrong");
        } else {
            if (currentTool instanceof Axe) {
                return ((Axe) currentTool).useAxe(x, y, currentTile, App.getInstance().getCurrentGame().getMap(), player,
                        App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
            } else if (currentTool instanceof FishingPole) {
                return ((FishingPole) currentTool).useFishingPole((FishingPole) currentTool, App.getInstance().getCurrentGame().getMap(), currentTile,
                        player, App.getInstance().getCurrentGame(), App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
            } else if (currentTool instanceof Hoe) {
                Result result = ((Hoe) currentTool).useHoe(x, y, currentTile, App.getInstance().getCurrentGame().getMap(), player, App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
                System.out.println(map[currentTile.getY() + y][currentTile.getX() + x].getIsPlowed());
                return result;
            } else if (currentTool instanceof MilkPail) {
                return ((MilkPail) currentTool).useMilkPail(x, y, currentTile, player, App.getInstance().getCurrentGame().getMap());
            } else if (currentTool instanceof PickAxe) {
                return ((PickAxe) currentTool).usePickAxe(x, y, currentTile, App.getInstance().getCurrentGame().getMap(), player, App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
            } else if (currentTool instanceof Scythe) {
                return ((Scythe) currentTool).useScythe(x, y, currentTile, App.getInstance().getCurrentGame().getMap(), player, App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
            } else if (currentTool instanceof WateringCan) {
                return ((WateringCan) currentTool).useWateringCan(x, y, currentTile, App.getInstance().getCurrentGame().getMap(), player, App.getInstance().getCurrentGame().getCurrentWeatherType().getEnergyOfToolsModifier());
            } else if (currentTool instanceof Shear) {
                return ((Shear) currentTool).useShear(x, y, currentTile, player, App.getInstance().getCurrentGame().getMap());
            }
            return new Result(true, "You have used a tool");
        }
    }

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
                tile.setTileOwner(player.getUsername());
                boolean foundSpecial = false;

                for (Habitat lake : playerFarm.getLake()) {

                    if (isInHabitat(x, y, lake)) {
                        tile.setType(TileType.LAKE);
                        tile.setWalkable(false);
                        foundSpecial = true;
                    }
                }

                Quarry quarry = playerFarm.getQuarry();
                Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight(), StorageType.INITIAL);

                if (!foundSpecial && isInHabitat(x, y, quarryHabitat)) {
                    tile.setType(TileType.QUARRY);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                House house = playerFarm.getHouse();
                Habitat houseHabitat = new Habitat(house.getX(), house.getY(), house.getWidth(), house.getHeight(), StorageType.INITIAL);
                if (!foundSpecial && isInHabitat(x, y, houseHabitat)) {
                    tile.setType(TileType.HOUSE);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                Habitat houseWallHabitat = new Habitat(
                        house.getX() - 1,
                        house.getY() - 1,
                        house.getWidth() + 2,
                        house.getHeight() + 2,
                        StorageType.INITIAL
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
                if (x == house.getX() + house.getWidth() / 2 &&
                        y == house.getY() + house.getHeight() - 1) {
                    player.setHomeTile(tile);
                }
                GreenHouse greenHouse = playerFarm.getGreenHouse();
                Habitat greenHouseHabitat = new Habitat(greenHouse.getX(), greenHouse.getY(), greenHouse.getWidth(), greenHouse.getHeight(), StorageType.INITIAL);
                if (!foundSpecial && isInHabitat(x, y, greenHouseHabitat)) {
                    tile.setType(TileType.GREENHOUSE);
                    tile.setWalkable(false);  // it is false because it is not fixed yet
                    foundSpecial = true;
                }

                Habitat greenHouseWallHabitat = new Habitat(
                        greenHouse.getX() - 1,
                        greenHouse.getY() - 1,
                        greenHouse.getWidth() + 2,
                        greenHouse.getHeight() + 2,
                        StorageType.INITIAL
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
        Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight(), StorageType.INITIAL);
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
            tile.setContainedItem(new ForagingMineral(ProductQuality.Normal, mineral));
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
            tile.getContainedGrowable().setName(findTreeBySourceName(tile.getContainedGrowable().getName()).getName());
            tile.setWalkable(false);
            //System.out.println(map[p.y][p.x].getContainedGrowable().getTreeType());
        }
        player.setCurrentTile(playerFarm.getRandomFarmTile(map));
        System.out.println("You are starting at coordinates " + player.getCurrentTile().getX() + " " + player.getCurrentTile().getY());
    }

    public Result buildGreenHouse() {
        Backpack playerBackPack = App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack();
        if (App.getInstance().getCurrentGame().getCurrentPlayer().getMoney() < 1000 ||
                !playerBackPack.hasItem("Stone", 500)) {
            return new Result(false, "green house build failed");
        }
        Farm farm = App.getInstance().getCurrentGame().getMap().getFarmByOwner(App.getInstance().getCurrentGame().getCurrentPlayer());
        GreenHouse greenHouse = farm.getGreenHouse();
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        for (int j = greenHouse.getY(); j < greenHouse.getY() + greenHouse.getHeight(); j++) {
            for (int i = greenHouse.getX(); i < greenHouse.getX() + greenHouse.getWidth(); i++) {
                map[j][i].setWalkable(true);
            }
        }
        App.getInstance().getCurrentGame().getCurrentPlayer().decreaseMoney(1000);
        App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().grabItem("Stone", 500);
        return new Result(true, "green house build successful");
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
        StringBuilder result = new StringBuilder();
        result.append("next turn started for " + currentUser.getUsername());
        if (!currentUser.getNotifications().isEmpty()) {
            result.append("\nYou have new notifications:\n");
            for (Message notification : currentUser.getNotifications()) {
                result.append("- From ").append(notification.getSender())
                        .append(": ").append(notification.getMessage()).append("\n");
            }
        }
        currentUser.getNotifications().clear();
        return new Result(true, result.toString());
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
        Tile[][] map = game.getMap().getMap();

        if (game.getTimeAndDate().getHour() == 22) {

            for (Shop shop : game.getMap().getShops()) {
                for (ShopItem shopItem : shop.getProducts()) {
                    shopItem.setSoldToday(0);
                }
            }
            processShippingBinsAtNight();

//            for (User user : game.getPlayers()) {
//                Tile home = user.getHomeTile();
//                walkToInternal(user, home.getX(), home.getY());
//            }
            handleMachinRecipes(game);

            // Skip time to 9 AM
            game.getTimeAndDate().skipToNextMorning();

            // Grow crops and update energy
            for (User user : game.getPlayers()) {
                user.resetEnergyForNewDay();
                if (user.getOwnedAnimals() != null || !user.getOwnedAnimals().isEmpty()) {
                    for (Animal animal : user.getOwnedAnimals()) {
                        animal.updateProductEndDay();
                        animal.endOfDayUpdate();
                    }
                    //user.getFarm().growCropsOneDay();
                    // user.getFarm().generateForageAndMine();
                    //user.collectShippingBinProfits();
                }
                if (user.getDaysSinceRejection() != 0) {
                    user.setDaysSinceRejection(Math.min(user.getDaysSinceRejection() - 1, 0));
                }

            }
            crowAttack();
            Tile[][] tiles = game.getMap().getMap();
            for (int j = 0; j < tiles.length; j++) {
                for (int i = 0; i < tiles[0].length; i++) {
                    if(j == 0 && i == 1)
                        updateGrowable(tiles[j][i]);
                }
            }
            randomForaging();

            // Update weather for the new day
            game.setCurrentWeatherType(game.getTomorrowWeatherType());
            game.predictTomorrowWeather();
            rainOnGrowables(game.getCurrentWeatherType());
            game.getMap().applyLightningStrikeIfStormy(game.getCurrentWeatherType().isCausesLightning());
            // the lightning strike logic should go into your handleEndOfDay() function — specifically after the new day
            //starts and the weather is known, but before or during crop updates (so the lightning can damage crops befor growth).

            //TO DO: update crops and days left to die delete crops and tree
            //TO Do: upadte fpraging and
            //To Do:  update animal days left to produce
            //To Do: crows attack
            // To Do: update animal products

            NPC.endOfDay(game);
            for (User user : game.getPlayers()) {
                game.handleFoodRecipe(user);
            }
        }
    }

    public void processShippingBinsAtNight() {
        Game game = App.getInstance().getCurrentGame();

        for (Farm farm : game.getMap().getFarms()) {
            Map<Item, Integer> shippingBin = farm.getShippingBin();
            User owner = farm.getOwner();
            int totalEarnings = 0;

            for (Map.Entry<Item, Integer> entry : shippingBin.entrySet()) {
                Item item = entry.getKey();
                int count = entry.getValue();
                int basePrice = item.getPrice(); // Assume this is the base price
                double qualityMultiplier = item.getProductQuality().getPriceCoefficient();
                int finalPrice = (int) (basePrice * qualityMultiplier); // Truncate decimals
                totalEarnings += finalPrice * count;
            }

            // Add money to the owner
            owner.addMoney(totalEarnings);

            // Clear the shipping bin
            shippingBin.clear();
        }
    }


    private void handleMachinRecipes(Game game) {
        for (User user : game.getPlayers()) {
            Map<Skill, Integer> userSkills = user.getSkillsLevel();
            ArrayList<MachineType> unlockedMachines = user.getMachineRecepies();

            for (MachineType machine : MachineType.values()) {
                Map<Skill, Integer> requiredSkills = machine.getRequiredSkill();
                if (requiredSkills.isEmpty()) continue;

                boolean hasAllSkills = true;
                for (Map.Entry<Skill, Integer> requirement : requiredSkills.entrySet()) {
                    Skill skill = requirement.getKey();
                    int requiredLevel = requirement.getValue();

                    int userLevel = userSkills.getOrDefault(skill, 0);
                    if (userLevel < requiredLevel) {
                        hasAllSkills = false;
                        break;
                    }
                }

                if (hasAllSkills && !unlockedMachines.contains(machine)) {
                    unlockedMachines.add(machine);
                }
            }
        }
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

    public Result printMap(String column, String row, String sizeOfMap) {
        int x = Integer.parseInt(column);
        int y = Integer.parseInt(row);
        int size = Integer.parseInt(sizeOfMap);
        Game game = App.getInstance().getCurrentGame();
        if (game.getMap().getHeight() < size + y || game.getMap().getWidth() < size + x) {
            return new Result(false, "invalid size");
        }
        if (x > game.getMap().getWidth() || y > game.getMap().getHeight()) {
            return new Result(false, "invalid coordinates");
        }
        Tile[][] map = game.getMap().getMap();
        for (int i = y; i < y + size; i++) {
            for (int j = x; j < x + size; j++) {
                TileType type = map[i][j].getType();
                if (map[i][j].getContainedGrowable() != null) {
                    if (map[i][j].getContainedGrowable().getGrowableType() == GrowableType.Coal) {
                        System.out.print("\u001B[37;40mO\u001B[0m");
                    } else if (map[i][j].getContainedGrowable().getTreeType() != null) {
                        System.out.print("\u001B[37;42mT\u001B[0m");
                    } else if (map[i][j].getContainedGrowable().getCropType() != null) {
                        System.out.print("\u001B[37;42mC\u001B[0m");
                    }
                } else if (map[i][j].getProductOfGrowable() != null) {
                    System.out.print("\u001B[37;48;5;22mg\u001B[0m");
                } else if (map[i][j].getContainedItem() != null) {
                    System.out.print("\u001B[37;48;5;208mf\u001B[0m");

                } else System.out.print(type.coloredSymbol());
            }
            System.out.println();
        }
        return new Result(true, "");
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
        // Also update the maximums to make this change permanent/persistent
        currentPlayer.setMaxEnergy(Integer.MAX_VALUE);
        currentPlayer.setMaxEnergyTurn(Integer.MAX_VALUE);

        currentPlayer.setEnergy(Integer.MAX_VALUE);
        currentPlayer.setCurrentTurnEnergy(Integer.MAX_VALUE);


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
        if (currentPlayer.getEnergy() == 0) {
            currentPlayer.setFainted(true);
        }
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
                    .append(", ")
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
        if (player.getBackpack().hasItem("Hay", 1)) {
            player.getBackpack().grabItem("Hay", 1);
        } else {
            return new Result(false, "not enough Hay!");
        }

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
        targetTile.setContainedAnimal(animal);

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
            if (!player.getBackpack().hasTool("milkpail"))
                return new Result(false, "You need a milk pail to collect from this animal.");
        } else if (type == AnimalType.SHEEP) {
            //check if the player have shear
            if (!player.getBackpack().hasTool("shear"))
                return new Result(false, "You need a shear to collect from this animal.");
        }
        // Check pig must be outside
        else if (type == AnimalType.PIG && animal.isInHabitat())
            return new Result(false, "Pig must be outside to collect its product.");

        AnimalProduct collected = animal.collectProduct();
        Result result = player.getBackpack().addItem(collected, 1);
        if (!result.isSuccessful()) {
            animal.setProduct(collected);
            return new Result(false, "Invetory is full");
        }
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
        if (tool == null) {
            return new Result(false, "No equipped tool found.");
        }
        return new Result(true, tool.toString());
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

    public Result sellAnimal(String name) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Animal animal = player.getAnimalByName(name);
        if (animal == null)
            return new Result(false, "No animal found with name: " + name);

        double price = animal.getAnimalType().getBasePrice();
        double friendship = animal.getFriendship();

        int sellingPrice = (int) ((1000 / price + 0.3) * friendship);

        // Remove the animal and add money to the player
        animal.getLivingPlace().getLivingAnimals().remove(animal);
        player.getOwnedAnimals().remove(animal);
        player.addMoney(sellingPrice);

        return new Result(true, "you sold " + name + " for " + sellingPrice + " coins!");
    }

    public void helpReadMap() {
        for (TileType type : TileType.values()) {
            System.out.println(type.name() + " (" + type.getLetterToPrint() + "): " + type.coloredSymbol());
        }
        System.out.println("Coal (O): \u001B[37;40mO\u001B[0m");
        System.out.println("Tree (T): \u001B[37;42mT\u001B[0m");
        System.out.println("Crop (C): \u001B[37;42mC\u001B[0m");
        System.out.println("Grown product (g): \u001B[37;48;5;22mg\u001B[0m");
        System.out.println("Item on ground (f): \u001B[37;48;5;208mf\u001B[0m");
    }


    public void walkTo(String destX, String destY, Scanner scanner) {
        int targetX = Integer.parseInt(destX);
        int targetY = Integer.parseInt(destY);
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();

        if (App.getInstance().getCurrentGame().getMap().isInsideAnyFarm(targetX, targetY) != null && !(map[targetY][targetX].getTileOwner().equals(player.getUsername()) || (player.getPartner() != null && map[targetY][targetX].getTileOwner().equals(player.getPartner().getUsername())))) {
            System.out.println("You are not allowed to walk to another player's farm!");
            return;
        }
        int rows = map.length;
        int cols = map[0].length;

        if (targetX >= cols || targetY >= rows) {
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

        energyUsed.put(startX + "," + startY, 0);


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

        bestEnergy = bestEnergy / 20;

        System.out.println("min Energy: " + bestEnergy);
        System.out.print("Do you want to walk there? (yes/no): ");
        String response = scanner.nextLine();

        if (!response.equalsIgnoreCase("yes")) {
            System.out.println("Walk cancelled.");
            return;
        }

        int currentEnergy = player.getEnergy();
        if (bestEnergy < currentEnergy) {
            // Enough energy, walk fully
            for (String pos : path) {
                String[] parts = pos.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);
            }
            player.setEnergy(currentEnergy - bestEnergy);
            int newTurnEnergy = Math.max(0, player.getCurrentTurnEnergy() - bestEnergy);
            player.setCurrentTurnEnergy(newTurnEnergy);
            System.out.println("Walked to destination. Energy left: " + player.getEnergy());
        } else {
            // Not enough energy, walk as far as possible
            String lastReachable = null;
            for (String pos : path) {
                Integer rawEnergy = energyUsed.get(pos);
                if (rawEnergy == null) break; // can't go further
                int required = rawEnergy / 20;
                if (required > currentEnergy) break;
                lastReachable = pos;
            }

            if (lastReachable != null) {
                String[] parts = lastReachable.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);

                //int usedEnergy = energyUsed.get(lastReachable) / 20;
                player.setEnergy(0);
                player.setCurrentTurnEnergy(0);
                player.setFainted(true);
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
        for (ForagingCropType forage : ForagingCropType.values()) {
            if (forage.getName().equalsIgnoreCase(craftName)) {
                System.out.println("Name: " + forage.getName());
                System.out.println("Seasons: " + forage.getSeason());
                System.out.println("Base Sell Price: " + forage.getBaseSellPrice());
                System.out.println("Energy: " + forage.getEnergy());
                return;
            }
        }
        for (TreeType tree : TreeType.values()) {
            if (tree.getName().equalsIgnoreCase(craftName)) {
                System.out.println("Name: " + tree.getName());
                System.out.println("Source: " + tree.getSource().getName());
                System.out.println("Total Grow Time: " + tree.getTotalHarvestTime());
                System.out.println("Fruit Type: " + tree.getFruitType().getName());
                System.out.println("Is Fruit Edible: " + tree.getFruitType().getIsFruitEdible());
                System.out.println("Is Foraging Tree : " + tree.getIsForagingTree());
                System.out.println("Fruit harvest cycle: " + tree.getFruitType().getFullHarvestCycle());
                System.out.println("Fruit Base sell Price: " + tree.getFruitType().getFruitBaseSellPrice());
                System.out.println("Fruit Energy: " + tree.getFruitType().getFruitEnergy());
                System.out.println("Seasons: " + tree.getNormalSeasons());
                System.out.println("Stages: " + tree.getSatges());
                return;
            }
        }
        System.out.println("Growable with name '" + craftName + "' not found.");
    }

    public void printTreeInfo(String treeName) {
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

    public void randomForaging() {
        //call this every morning or night
        Game currentGame = App.getInstance().getCurrentGame();
        Tile[][] map = currentGame.getMap().getMap();
        for (Farm farm : App.getInstance().getCurrentGame().getMap().getFarms()) {
            for (int j = farm.getY(); j < farm.getHeight() + farm.getY(); j++) {
                for (int i = farm.getX(); i < farm.getX() + farm.getWidth(); i++) {
                    int rand = RANDOM.nextInt(100);
                    if (rand == 1 && map[j][i].getType() == TileType.FARM) {
                        if (map[j][i].getProductOfGrowable() == null && map[j][i].getContainedGrowable() == null &&
                                map[j][i].getContainedItem() == null) {
                            if (map[j][i].getIsPlowed()) {
                                map[j][i].setContainedGrowable(GrowableFactory.getInstance().create(getRandomForagingSourceBySeason(currentGame.getTimeAndDate().getSeason())));
                                map[j][i].getContainedGrowable().setName(findCropBySourceName(map[j][i].getContainedGrowable().getName()).getName());
                                map[j][i].setWalkable(false);
                            } else {
                                map[j][i].setProductOfGrowable(GrowableFactory.getInstance().create(getRandomForagingCropBySeason(currentGame.getTimeAndDate().getSeason())));
                                map[j][i].setWalkable(false);
                            }
                        }
                    } else if (rand == 1 && map[j][i].getType() == TileType.QUARRY) {
                        if (map[j][i].getContainedItem() == null) {
                            ForagingMineralType mineral = getRandomForagingMineral();
                            map[j][i].setContainedItem(new ForagingMineral(ProductQuality.Normal, mineral));
                            map[j][i].setWalkable(false);
                        }
                    }
                }
            }
        }
    }

    public Result showPlant(String x, String y) {
        int tileX = Integer.parseInt(x);
        int tileY = Integer.parseInt(y);
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        if (map[tileY][tileX].getContainedGrowable() == null && map[tileY][tileX].getProductOfGrowable() == null) {
            return new Result(false, "There is no product in this tile!");
        }
        Growable growable = null;
        if (map[tileY][tileX].getContainedGrowable() != null) growable = map[tileY][tileX].getContainedGrowable();
        else if (map[tileY][tileX].getProductOfGrowable() != null) growable = map[tileY][tileX].getProductOfGrowable();
        StringBuilder sb = new StringBuilder();
        assert growable != null;
        if (growable.getTreeType() != null) {
            sb.append("TreeType: ").append(growable.getTreeType().getName()).append("\n");
        } else if (growable.getCropType() != null) {
            sb.append("CropType: ").append(growable.getCropType().getName()).append("\n");
        } else if (growable.getForagingCropType() != null) {
            sb.append("ForagingCropType: ").append(growable.getForagingCropType().getName()).append("\n");
        }

        // Append other fields
        if (map[tileY][tileX].getContainedGrowable() != null)
            sb.append("SourceType: ").append(growable.getSource().getName()).append("\n");
        sb.append("GrowableType: ").append(growable.getGrowableType().name()).append("\n");
        sb.append("Age: ").append(growable.getAge()).append("\n");
        sb.append("Current Stage: ").append(growable.getCurrentStage()).append("\n");
        sb.append("Watered Today: ").append(growable.getIsWateredToday()).append("\n");
        sb.append("Fertilized: ").append(growable.hasBeenFertalized()).append("\n");
        sb.append("Days Left to Die: ").append(growable.getDaysLeftToDie()).append("\n");
        return new Result(true, sb.toString());
    }

    public Result plantGrowable(String seedName, String direction) {
        SourceType sourceType = SourceType.fromName(seedName);
        if (sourceType == null) return new Result(false, "There is no such source type!");
        Backpack playerBackPack = App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack();
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        //Growable growable = findGrowableInBackpackBySourceType(playerBackPack, sourceType);
        Growable growable = (Growable) playerBackPack.grabItemAndReturn(seedName, 1);
        if (growable == null) {
            return new Result(false, "Growable with name '" + seedName + "' not found in inventory.");
        }
//        growable = (Growable) playerBackPack.grabItemAndReturn(seedName, 1);
//        Result result = playerBackPack.grabItem(growable.getName(), 1);
        //if (result.isSuccessful()) {
            //System.out.println(result.message());
            int x = currentPlayer.getCurrentTile().getX();
            int y = currentPlayer.getCurrentTile().getY();
            if (direction.equals("up")) y--;
            else if (direction.equals("down")) y++;
            else if (direction.equals("left")) x--;
            else if (direction.equals("right")) x++;
            if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
                return new Result(false, "direction is invalid.");
            }
            if (hasGiantNeighbor(map, y, x)) {
                return new Result(false, "You cannot plant next to a giant crop!");
            }
            if (map[y][x].getType() != TileType.FARM && map[y][x].getType() != TileType.GREENHOUSE) {
                return new Result(false, "You cannot plant in this tile!");
            }
            if (map[y][x].getContainedItem() != null || map[y][x].getContainedGrowable() != null || map[y][x].getProductOfGrowable() != null) {
                return new Result(false, "This tile is full!");
            }
            if (growable.getGrowableType() == GrowableType.MixedSeeds) {
                growable = GrowableFactory.getInstance().create(Growable.getRandomSourceType(App.getInstance()
                        .getCurrentGame().getTimeAndDate().getSeason()));
            } else if (map[y][x].getType() != TileType.GREENHOUSE) {
                Season currentSeason = App.getInstance().getCurrentGame().getTimeAndDate().getSeason();
                if ((growable.getCropType() != null && !growable.getCropType().getSeasons().contains(currentSeason)) ||
                        (growable.getTreeType() != null && !growable.getTreeType().getNormalSeasons().contains(currentSeason))) {
                    return new Result(false, "You cannot plant this seed out of season!");
                }
            }
            if (!map[y][x].getIsPlowed()) {
                return new Result(false, "The tile isn't plowed!");
            }
//            if(growable.getCropType() != null){
//                tryFormGiant(y, x, growable.getCropType());
//            }
            map[y][x].setWalkable(false);
            if (growable.getCropType() != null) growable.setName(findCropBySourceName(growable.getName()).getName());
            if (growable.getTreeType() != null) growable.setName(findTreeBySourceName(growable.getName()).getName());
            map[y][x].setContainedGrowable(growable);
            //System.out.println(showPlant(String.valueOf(x), String.valueOf(y)).message());
            map[y][x].getContainedGrowable().setCurrentStage(1);
            map[y][x].setIsPlowed(false);
            if (growable.getCropType() != null) {
                tryFormGiant(y, x, growable.getCropType());
            }
            return new Result(true, "Growable with name '" + seedName + "' has been planted in " + x + ", " + y);
        //}
//        else {
//            return result;
//        }
    }

    public Result fertalizeGrowable(String fertalizer, String direction) {
        Result result = App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().grabItem(fertalizer, 1);
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        int x = currentPlayer.getCurrentTile().getX();
        int y = currentPlayer.getCurrentTile().getY();
        if (direction.equals("up")) y--;
        else if (direction.equals("down")) y++;
        else if (direction.equals("left")) x--;
        else if (direction.equals("right")) x++;
        if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
            return new Result(false, "direction is invalid.");
        }
        if (map[y][x].getContainedGrowable() == null) {
            return new Result(false, "There is no growable in the chosen tile!");
        }
        if (result.isSuccessful() && randomStuffType.fromName(fertalizer) == randomStuffType.RetainingSoil) {
            System.out.println(result.message());
            map[y][x].getContainedGrowable().setHasBeenFertalized(true);
            Growable growable = map[y][x].getContainedGrowable();
//            if (growable.getGrowableType() == GrowableType.Giant) {
//                for (int j = Math.max(0, map[y][x].getY() - 1); j <= Math.min(149, map[y][x].getY() + 1); j++) {
//                    for (int i = Math.max(0, map[y][x].getX() - 1); i <= Math.min(149, map[y][x].getX() + 1); i++) {
//                        if (map[j][i].getContainedGrowable() != null && map[j][i].getContainedGrowable().getGrowableType() == GrowableType.Giant) {
//                            map[j][i].getContainedGrowable().setHasBeenFertalized(true);
//                        }
//                    }
//                }
//            }
            return new Result(true, "Growable with name '" + growable.getName() + "' has been fertalized.");
        } else if (result.isSuccessful() && randomStuffType.fromName(fertalizer) == randomStuffType.SpeedGro) {
            System.out.println(result.message());
            //map[y][x].getContainedGrowable().setHasBeenFertalized(true);
            //if (map[y][x].getContainedGrowable().getGrowableType() != GrowableType.Giant)
            map[y][x].getContainedGrowable().setAge(map[y][x].getContainedGrowable().getAge() + 1);
            Growable growable = map[y][x].getContainedGrowable();
//            if (growable.getGrowableType() == GrowableType.Giant) {
//                for (int j = Math.max(0, map[y][x].getY() - 1); j <= Math.min(149, map[y][x].getY() + 1); j++) {
//                    for (int i = Math.max(0, map[y][x].getX() - 1); i <= Math.min(149, map[y][x].getX() + 1); i++) {
//                        if (map[j][i].getContainedGrowable() != null && map[j][i].getContainedGrowable().getGrowableType() == GrowableType.Giant) {
//                            map[j][i].getContainedGrowable().setHasBeenFertalized(true);
//                            map[j][i].getContainedGrowable().setAge(map[j][i].getContainedGrowable().getAge() + 1);
//                        }
//                    }
//                }
//            }
            return new Result(true, "Growable with name '" + growable.getName() + "' has been fertalized.");
        } else {
            return result;
        }
    }

    public void tryFormGiant(int y, int x, CropType cropType) {
        Tile[][] grid = App.getInstance().getCurrentGame().getMap().getMap();
        int height = grid.length;
        int width = grid[0].length;

        if (y + 1 < height && x + 1 < width) {
            if (isValidGiantSquare(y, x, y, x + 1, y + 1, x, y + 1, x + 1, cropType)) return;
        }

        // 2. (y, x) is top-right
        if (y + 1 < height && x - 1 >= 0) {
            if (isValidGiantSquare(y, x - 1, y, x, y + 1, x - 1, y + 1, x, cropType)) return;
        }

        // 3. (y, x) is bottom-left
        if (y - 1 >= 0 && x + 1 < width) {
            if (isValidGiantSquare(y - 1, x, y - 1, x + 1, y, x, y, x + 1, cropType)) return;
        }

        // 4. (y, x) is bottom-right
        if (y - 1 >= 0 && x - 1 >= 0) {
            if (isValidGiantSquare(y - 1, x - 1, y - 1, x, y, x - 1, y, x, cropType)) return;
        }
    }

    private boolean isValidGiantSquare(int y1, int x1, int y2, int x2,
                                       int y3, int x3, int y4, int x4, CropType cropType) {
        Tile[][] grid = App.getInstance().getCurrentGame().getMap().getMap();
        Tile[] tiles = {
                grid[y1][x1], grid[y2][x2], grid[y3][x3], grid[y4][x4]
        };

        for (Tile tile : tiles) {
            Growable g = tile.getContainedGrowable();
            if (tile.getType() == TileType.GREENHOUSE || g == null || g.getCropType() != cropType) {
                return false;
            }
        }

        int maxAge = 0;
        int maxStage = 0;
        int maxDaysLeft = 2;

        for (Tile tile : tiles) {
            Growable g = tile.getContainedGrowable();
            g.setGrowableType(GrowableType.Giant);
            if (g.getAge() > maxAge) maxAge = g.getAge();
            if (g.getCurrentStage() > maxStage) maxStage = g.getCurrentStage();
            if (g.getDaysLeftToDie() > maxDaysLeft) maxDaysLeft = g.getDaysLeftToDie();
        }

//        for (Tile tile : tiles) {
//            Growable g = tile.getContainedGrowable();
//            g.setAge(maxAge);
//            g.setCurrentStage(maxStage);
//            g.setDaysLeftToDie(maxDaysLeft);
//        }

        Growable shared = grid[y1][x1].getContainedGrowable();
        shared.setGrowableType(GrowableType.Giant);
        shared.setAge(maxAge);
        shared.setCurrentStage(maxStage);
        shared.setDaysLeftToDie(maxDaysLeft);


        for (Tile tile : tiles) {
            tile.setContainedGrowable(shared);
        }

        return true;
    }


    public boolean hasGiantNeighbor(Tile[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                // Skip the center tile itself
                if (dRow == 0 && dCol == 0) continue;

                int newRow = row + dRow;
                int newCol = col + dCol;

                // Check bounds
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    Growable growable = grid[newRow][newCol].getContainedGrowable();
                    Growable productOfGrowable = grid[newRow][newCol].getProductOfGrowable();
                    if ((growable != null && growable.getGrowableType() == GrowableType.Giant) ||
                            (productOfGrowable != null && productOfGrowable.getGrowableType() == GrowableType.Giant)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void crowAttack() {
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        Farm playerFarm = App.getInstance().getCurrentGame().getMap().getFarmByOwner(App.getInstance().getCurrentGame().getCurrentPlayer());

        List<Tile> growableTiles = new ArrayList<>();

        for (int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++) {
            for (int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++) {
                if (map[y][x].getType() == TileType.FARM && map[y][x].getProductOfGrowable() != null) {
                    growableTiles.add(map[y][x]);
                }
            }
        }
        int rand = RANDOM.nextInt(100);
        if (growableTiles.size() >= 16 && RANDOM.nextInt(100) <= 25) {
            Tile target = growableTiles.get(RANDOM.nextInt(growableTiles.size()));
            int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
            int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

            boolean hasScarecrowNearby = false;

            for (int i = 0; i < 8; i++) {
                int nx = target.getX() + dx[i];
                int ny = target.getY() + dy[i];

                // Check bounds
                if (ny >= 0 && ny < map.length && nx >= 0 && nx < map[0].length) {
                    Tile neighbor = map[ny][nx];

                    if (neighbor.getContainedItem() != null &&
                            (neighbor.getContainedItem().getName().equals("Scarecrow") ||
                                    neighbor.getContainedItem().getName().equals("Deluxe Scarecrow"))) {
                        hasScarecrowNearby = true;
                        break;
                    }
                }
            }
            if(!hasScarecrowNearby) {
                if (target.getProductOfGrowable() != null) {
                    target.setProductOfGrowable(null);
                    if (target.getContainedGrowable() != null) {
                        target.getContainedGrowable().setHasBeenAttackedByCrow(true);
                    }
                    System.out.println("A crow destroyed a crop at tile (" + target.getX() + ", " + target.getY() + ")!" + "GHAR GHAR");
                }
            }
        }
    }


    public void updateGrowable(Tile tile) {
        //this should be called at the end of the days
        //when we are not in the required season the growables won't grow in this function so naturally they won't produce any product
        Game currentGame = App.getInstance().getCurrentGame();
        Season currentSeason = currentGame.getTimeAndDate().getSeason();
        if (tile.getContainedGrowable() != null) {
            if(tile.getContainedGrowable().getGrowableType() == GrowableType.Coal) {
                return;
            }
            if (tile.getContainedGrowable().getDaysLeftToDie() <= 0) {
                tile.setContainedGrowable(null);
                tile.setProductOfGrowable(null);
                return;
            }
            if (!tile.getContainedGrowable().getIsWateredToday() && !tile.getContainedGrowable().hasBeenFertalized()) {
                tile.getContainedGrowable().setDaysLeftToDie(tile.getContainedGrowable().getDaysLeftToDie() - 1);
                return;
            }
            if (tile.getContainedGrowable().hasBeenAttackedByCrow()) {
                tile.getContainedGrowable().setHasBeenAttackedByCrow(false);
                return;
            }
            //tile.getContainedGrowable().setHasBeenFertalized(false);
            if(!tile.getContainedGrowable().hasBeenFertalized()) tile.getContainedGrowable().setIsWateredToday(false);
            Growable growable = tile.getContainedGrowable();
            if (growable != null && growable.getTreeType() != null && growable.getTreeType().getNormalSeasons().contains(currentSeason)) {
                growable.setAge(growable.getAge() + 1);
                int currentStage = growable.getCurrentStage();
                int daysPast = 0;
                //manteghan current stage vaghti treetype pore 0 nist
                if (growable.getTreeType().getTotalHarvestTime() > growable.getAge()) {
                    for (int i = 0; i < currentStage; i++) {
                        daysPast += growable.getTreeType().getSatges().get(i);
                    }
                    if (daysPast <= growable.getAge()) {
                        growable.setCurrentStage(growable.getCurrentStage() + 1);
                    }
                } else {
                    growable.setGrowableType(GrowableType.Tree);
                    if (tile.getProductOfGrowable() == null) {
                        if (growable.getTreeType().getTotalHarvestTime() +
                                growable.getTreeType().getFruitType().getFullHarvestCycle() <= growable.getAge()) {
                            growable.setAge(growable.getTreeType().getTotalHarvestTime());
                            Growable fruit = GrowableFactory.getInstance().create(growable.getTreeType().getSource());
                            fruit.setGrowableType(GrowableType.Fruit);
                            fruit.setName(growable.getTreeType().getFruitType().getName());
                            tile.setProductOfGrowable(fruit);
                            //System.out.println(fruit.toString());
                        }
                    }
                }
            } else if (growable != null && growable.getCropType() != null && growable.getCropType().getSeasons().contains(currentSeason)) {
                growable.setAge(growable.getAge() + 1);
                int currentStage = growable.getCurrentStage();
                int daysPast = 0;
                if (growable.getCropType().getTotalHarvestTime() > growable.getAge()) {
                    for (int i = 0; i < currentStage; i++) {
                        daysPast += growable.getCropType().getStages().get(i);
                    }
                    if (daysPast <= growable.getAge()) {
                        //System.out.println(daysPast);
                        //System.out.println(growable.getAge());
                        growable.setCurrentStage(growable.getCurrentStage() + 1);
                    }
                } else {
                    if (tile.getProductOfGrowable() == null) {
                        if (growable.getCropType().oneTime()) {
                            Growable product = GrowableFactory.getInstance().create(growable.getCropType().getSource());
                            if (growable.getGrowableType() != GrowableType.Giant)
                                product.setGrowableType(GrowableType.CropProduct);
                            else product.setGrowableType(GrowableType.Giant);
                            product.setName(findCropBySourceName(product.getName()).getName());
                            tile.setProductOfGrowable(product);
                            tile.setContainedGrowable(null);
                        } else {
                            if (growable.getCropType().getTotalHarvestTime() +
                                    growable.getCropType().getRegrowthTime() <= growable.getAge()) {
                                growable.setAge(0);
                                growable.setCurrentStage(1);
                                Growable product = GrowableFactory.getInstance().create(growable.getCropType().getSource());
                                if (growable.getGrowableType() != GrowableType.Giant)
                                    product.setGrowableType(GrowableType.CropProduct);
                                else product.setGrowableType(GrowableType.Giant);
                                product.setName(findCropBySourceName(product.getName()).getName());
                                tile.setProductOfGrowable(product);
                            }
                        }
                    }
                }

            }
        }
    }

    public void rainOnGrowables(WeatherType tommorowWeather) {
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        if (tommorowWeather == WeatherType.RAIN) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[j][i].getType() == TileType.FARM && map[j][i].getContainedGrowable() != null) {
                        map[j][i].getContainedGrowable().setIsWateredToday(true);
                    }
                }
            }
        }
    }

    public CropType findCropBySourceName(String sourceName) {
        for (CropType crop : CropType.values()) {
            if (crop.getSource().getName().equalsIgnoreCase(sourceName)) {
                return crop;
            }
        }
        return null; // Or Optional.empty() if preferred
    }

    public TreeType findTreeBySourceName(String sourceName) {
        for (TreeType tree : TreeType.values()) {
            if (tree.getSource().getName().equalsIgnoreCase(sourceName)) {
                return tree;
            }
        }
        return null;
    }

    public Result talk(String username, String message) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null) {
            return new Result(false, "One or both users of the relation not found.");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        if (sender.getPartner() != null && receiver.getPartner() != null && sender.getPartner() == receiver) {
            friendship.addXp(50);
            sender.addEnergy(50);
            receiver.addEnergy(50);
        } else friendship.addXp(20);
        receiver.addToNotifications(new Message(senderUsername, receiver.getUsername(), senderUsername + "sent you a message :" + message));
        friendship.getTalkHistory().add(new Message(sender.getUsername(), receiver.getUsername(), message));
        return new Result(true, "message sent successfully to " + receiver.getUsername());
    }

    public Result showTalkHistory(String username) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Friendship friendship = game.getFriendship(player.getUsername(), username);
        ArrayList<Message> relevantMessages = new ArrayList<>();

        StringBuilder historyBuilder = new StringBuilder();
        ArrayList<Message> talkHistory = friendship.getTalkHistory();

        if (talkHistory.isEmpty()) {
            return new Result(false, "No messages found between you and " + username);
        }

        historyBuilder.append("Talk history with ").append(username).append(":\n");

        for (Message msg : talkHistory) {
            String direction = msg.getSender().equals(player.getUsername()) ? "You" : msg.getSender();
            String recipient = msg.getRecipient().equals(player.getUsername()) ? "You" : msg.getRecipient();

            historyBuilder
                    .append("- From ").append(direction)
                    .append(" to ").append(recipient)
                    .append(": ").append(msg.getMessage())
                    .append("\n");
        }

        return new Result(true, historyBuilder.toString());
    }

    public Result hug(String username) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null)
            return new Result(false, "One or both users of the relation not found.");
        if (friendship.getLevel() < 2) {
            return new Result(false, "You have not enough level!");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        if (sender.getPartner() != null && sender.getPartner().equals(receiver)) {
            friendship.addXp(50);
            sender.addEnergy(50);
            receiver.addEnergy(50);
        } else friendship.addXp(60);
        return new Result(true, "You succesfully hugged " + username + "(ah che chendeshi)");
    }

    public Result askMarriage(String username, String ring) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(senderUsername, username);

        if (sender == null || receiver == null || friendship == null)
            return new Result(false, "One or both users of the relation not found.");
        if (friendship.getLevel() < 3) {
            return new Result(false, "You have not enough level!");
        }
        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile()))
            return new Result(false, "Players are not adjacent.");
        if (sender.isGender())
            return new Result(false, "Only the male players can ask marriage.");
        if(sender.isGender() == receiver.isGender()){
            return new Result(false, "The male player can only ask a woman to marry him.");
        }
        //Item item = sender.getBackpack().grabItemAndReturn("Wedding Ring", 1);
        if (!sender.getBackpack().hasItem("Wedding Ring", 1))
            return new Result(false, "You don't have any ring!");
        receiver.addToNotifications(new Message(senderUsername, receiver.getUsername(), senderUsername + "has asked to marry you"));
        return new Result(true, "You're marriage request has been sent successfully to " + receiver.getUsername());
    }

    public Result respondToMarriage(String response, String username) {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(username);
        Friendship friendship = game.getFriendship(currentPlayer.getUsername(), username);
        if (response.equals("accept")) {
            randomStuff ring = (randomStuff) receiver.getBackpack().grabItemAndReturn("Wedding Ring", 1);
            currentPlayer.getBackpack().addItem(ring, 1);
            friendship.setLevel(4);
            currentPlayer.setPartner(receiver);
            receiver.setPartner(currentPlayer);
            int sharedMoney = currentPlayer.getMoney() + receiver.getMoney();
            currentPlayer.setMoney(sharedMoney);
            receiver.setMoney(sharedMoney);
            return new Result(true, "You are married now. Ishalla mobarakesh bad!");
        } else {
            friendship.setLevel(0);
            receiver.setEnergy(receiver.getEnergy() / 2);
            receiver.setDaysSinceRejection(7);
            return new Result(false, "fekr kardi pool dari ya ghiafe!");
        }
    }

    public void startTrade() {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (!currentPlayer.getTradeNotifications().isEmpty()) {
            System.out.println("You have the following trade notifications:");
            for (Message message : currentPlayer.getTradeNotifications()) {
                System.out.println("- " + message.getMessage());
            }
        } else {
            System.out.println("You have no trade notifications.");
        }
        currentPlayer.getTradeNotifications().clear();
        App.getInstance().setCurrentMenu(Menu.TradeMenu);
    }

    private boolean walkToInternal(User player, int targetX, int targetY) {
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        //User player = App.getInstance().getCurrentGame().getCurrentPlayer();

        int rows = map.length;
        int cols = map[0].length;

        if (targetX >= cols || targetY >= rows) return false;

        int[][] directions = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        int[][] minEnergy = new int[rows][cols];
        for (int[] row : minEnergy) Arrays.fill(row, Integer.MAX_VALUE);

        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> cameFromDir = new HashMap<>();
        Map<String, Integer> energyUsed = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        Tile currentTile = player.getCurrentTile();
        int startX = currentTile.getX();
        int startY = currentTile.getY();

        energyUsed.put(startX + "," + startY, 0);

        for (int d = 0; d < 4; d++) {
            queue.add(new int[]{startX, startY, 0, 0, d});
            minEnergy[startY][startX] = 0;
        }

        String bestEnd = null;
        int bestEnergy = Integer.MAX_VALUE;
        int bestDist = 0, bestTurns = 0;

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

        if (bestEnd == null) return false;

        List<String> path = new ArrayList<>();
        String step = bestEnd;
        while (step != null) {
            path.add(step);
            step = parent.get(step);
        }
        Collections.reverse(path);

        int finalEnergy = bestEnergy / 20;
        int currentEnergy = player.getEnergy();

        if (finalEnergy < currentEnergy) {
            for (String pos : path) {
                String[] parts = pos.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);
            }
            player.setEnergy(currentEnergy - finalEnergy);
            int newTurnEnergy = Math.max(0, player.getCurrentTurnEnergy() - finalEnergy);
            player.setCurrentTurnEnergy(newTurnEnergy);
        } else {
            String lastReachable = null;
            for (String pos : path) {
                Integer rawEnergy = energyUsed.get(pos);
                if (rawEnergy == null) break;
                int required = rawEnergy / 20;
                if (required > currentEnergy) break;
                lastReachable = pos;
            }

            if (lastReachable != null) {
                String[] parts = lastReachable.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                player.setCurrentTile(map[y][x]);
                player.setEnergy(0);
                player.setCurrentTurnEnergy(0);
                player.setFainted(true);
            }
        }

        return true;
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

    public Result showAnimalProducts() {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();

        List<Animal> animals = player.getOwnedAnimals();
        StringBuilder output = new StringBuilder();

        boolean hasUncollected = false;
        for (Animal animal : animals) {
            if (animal.hasProduct()) {
                AnimalProduct product = animal.getProduct();
                output.append(animal.getName())
                        .append(": ")
                        .append(product.getName())
                        .append(" (quality: ")
                        .append(product.getProductQuality())
                        .append(")\n");
                hasUncollected = true;
            }
        }

        if (!hasUncollected) {
            return new Result(true, "no uncollected animal products!");
        }

        return new Result(true, output.toString().trim());
    }

    public Result cheatAddMoney(String countString) {
        Game game = App.getInstance().getCurrentGame();
        if(game==null){
            return new Result(false,"a new game hasnt started yet");
        }
        User player = game.getCurrentPlayer();

        int count = Integer.parseInt(countString);
        if (count <= 0) {
            return new Result(true, "Invalid count!");
        }
        player.addMoney(count);
        return new Result(true, "Your current money: " + player.getMoney());
    }

    public Result showFriendships() {
        Game game = App.getInstance().getCurrentGame();
        StringBuilder result = new StringBuilder();
        String currentUsername = game.getCurrentPlayer().getUsername();

        result.append("Friendships of ").append(currentUsername).append(":\n");

        for (Friendship friendship : game.getAllFriendships()) {
            if (friendship.getPlayer1().equals(currentUsername) || friendship.getPlayer2().equals(currentUsername)) {
                String other = friendship.getPlayer1().equals(currentUsername)
                        ? friendship.getPlayer2()
                        : friendship.getPlayer1();

                result.append("With ").append(other)
                        .append(" - Level: ").append(friendship.getLevel())
                        .append(", XP: ").append(friendship.getXp()).append("\n");
            }
        }

        return new Result(true, result.toString().trim());
    }


    public Result sendGift(String receiverUsername, String item, String amountString) {
        Game game = App.getInstance().getCurrentGame();
        int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid amount.");
        }

        String senderUsername = game.getCurrentPlayer().getUsername();
        User sender = game.getCurrentPlayer();
        User receiver = game.getPlayerByUsername(receiverUsername);
        Friendship friendship = game.getFriendship(senderUsername, receiverUsername);
        if (sender == null) {
            return new Result(false, "Sender not found.");
        }
        if (receiver == null) {
            return new Result(false, "Receiver not found.");
        }
        if (friendship == null) {
            return new Result(false, "Friendship not found between users.");
        }

        if (friendship.getLevel() < 1) {
            return new Result(false, "You don't have enough level!");
        }

        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile())) {
            return new Result(false, "Players are not adjacent.");
        }

        int availableCount = sender.getBackpack().getItemCount(item);
        if (availableCount < amount) {
            return new Result(false, "Sender does not have enough of the specified item.");
        }

        Item itemToGift = sender.getBackpack().grabItemAndReturn(item, amount);
        if (itemToGift == null) {
            return new Result(false, "Sender does not have enough of the specified item.");
        }

        // Remove from sender and add to receiver
        Result result = receiver.getBackpack().addItem(itemToGift.copy(), amount);
        if (!result.isSuccessful()) {
            return new Result(false, "Receiver inventory is full!");
        }
        //sender.getBackpack().grabItem(item, amount);

        // Log the gift in friendship
        Gift gift = new Gift(senderUsername, receiverUsername, itemToGift, amount);
        friendship.addToGifts(gift);

        // Notify receiver to rate the gift
        receiver.addToNotifications(new Message(senderUsername, receiverUsername, "You received " + amount + " of " + itemToGift.getName()));
        receiver.addRecievedGift(gift);

        return new Result(true, "Gift sent successfully. Waiting for receiver to rate the gift.");
    }


    public Result listGift() {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();

        // Retrieve the list of received gifts
        List<Gift> receivedGifts = currentPlayer.getRecievedGift();

        if (receivedGifts == null || receivedGifts.isEmpty()) {
            return new Result(false, "No gifts received yet.");
        }

        // Prepare a message with all the received gifts
        StringBuilder giftListMessage = new StringBuilder("Received gifts:\n");

        for (int i = 0; i < receivedGifts.size(); i++) {
            Gift gift = receivedGifts.get(i);
            // Format each gift details with 1-based index
            String giftDetails = String.format("%d) From: %s, Item: %s, Amount: %d, Rating: %d\n",
                    i + 1,
                    gift.getSender(),
                    gift.getItem().getName(),
                    gift.getAmount(),
                    gift.getRate());
            giftListMessage.append(giftDetails);
        }

        return new Result(true, giftListMessage.toString());
    }


    public Result rateGifts(String giftNumberString, String rateString) {
        Game game = App.getInstance().getCurrentGame();
        User currentPlayer = game.getCurrentPlayer();
        // Retrieve the received gift
        List<Gift> receivedGifts = currentPlayer.getRecievedGift();

        // Convert gift number and rating to appropriate types
        int giftNumber = Integer.parseInt(giftNumberString);
        int rating = Integer.parseInt(rateString);

        if (receivedGifts == null || receivedGifts.isEmpty()) {
            return new Result(false, "No gifts received yet.");
        }

        // Validate rating range
        if (rating < 1 || rating > 5) {
            return new Result(false, "Rating must be between 1 and 5.");
        }


        if (giftNumber < 1 || giftNumber > receivedGifts.size()) {
            return new Result(false, "No gift found with the specified gift number.");
        }

        Gift gift = receivedGifts.get(giftNumber - 1); // Gift number is 1-based

        // Apply the rating to the friendship
        User sender = game.getPlayerByUsername(gift.getSender());
        Friendship friendship = game.getFriendship(currentPlayer.getUsername(), sender.getUsername());

        if (friendship == null) {
            return new Result(false, "No friendship exists between you and the sender.");
        }
        if (friendship.getLevel() < 1) {
            return new Result(false, "You don't have enough level!");
        }

        // Calculate the friendship level change based on the formula
        int friendshipChange = 15 + 30 * (rating - 3);
        if (currentPlayer.getPartner() != null && currentPlayer.getPartner().getUsername().equals(gift.getSender())) {
            friendship.addXp(50);
            currentPlayer.addEnergy(50);
            sender.addEnergy(50);
        } else {
            friendship.addXp(friendshipChange);
        }

        // Log the rating
        gift.setRate(rating);  // Assuming Gift has a setRating method

        return new Result(true, "Gift rated successfully. Friendship level adjusted.");
    }

    public Result giftHistory(String receiverUsername) {
        Game game = App.getInstance().getCurrentGame();
        String senderUsername = game.getCurrentPlayer().getUsername();

        User receiver = game.getPlayerByUsername(receiverUsername);
        if (receiver == null) {
            return new Result(false, "Receiver user not found.");
        }

        Friendship friendship = game.getFriendship(senderUsername, receiverUsername);
        if (friendship == null) {
            return new Result(false, "No friendship exists between you and " + receiverUsername + ".");
        }

        List<Gift> allGifts = friendship.getGifts();
        if (allGifts == null || allGifts.isEmpty()) {
            return new Result(false, "There are no gifts exchanged between you and " + receiverUsername + ".");
        }

        StringBuilder history = new StringBuilder();
        history.append("Gift History between ").append(senderUsername)
                .append(" and ").append(receiverUsername).append(":\n\n");

        for (Gift gift : allGifts) {
            history.append("Sent: ").append(gift.getSender())
                    .append(" Reciever: ").append(gift.getReceiver())
                    .append(" | Item: ").append(gift.getItem().getName())
                    .append(" | Amount: ").append(gift.getAmount())
                    .append(" | Rating: ").append(gift.getRate())
                    .append("\n");
        }

        return new Result(true, history.toString());
    }


    public Result sendFlower(String receiverUsername) {
        Game game = App.getInstance().getCurrentGame();
        User sender = game.getCurrentPlayer();
        String senderUsername = sender.getUsername();
        User receiver = game.getPlayerByUsername(receiverUsername);
        //sender.getBackpack().addItem(new randomStuff(20, randomStuffType.Bouquet), 1);
        if (receiver == null) {
            return new Result(false, "Receiver not found.");
        }
        if (sender == null) {
            return new Result(false, "Sender not found.");
        }

        Friendship friendship = game.getFriendship(senderUsername, receiverUsername);
        if (friendship == null) {
            return new Result(false, "friendship not found.");

        }
        if (friendship.getLevel() < 2) {
            return new Result(false, "You don't have enough level!");
        }

        if (!isAdjacent(sender.getCurrentTile(), receiver.getCurrentTile())) {
            return new Result(false, "Players are not adjacent.");
        }

        final String FLOWER_ITEM_NAME = "Bouquet";
        int flowerCount = sender.getBackpack().getItemCount(FLOWER_ITEM_NAME);
        if (flowerCount < 1) {
            return new Result(false, "You don't have a flower to send.");
        }

        // Transfer 1 flower
        Item flower = sender.getBackpack().grabItemAndReturn(FLOWER_ITEM_NAME, 1);
        if (flower == null) {
            return new Result(false, "Failed to grab flower from inventory.");
        }

        Result addResult = receiver.getBackpack().addItem(flower.copy(), 1);
        if (!addResult.isSuccessful()) {
            return new Result(false, "Receiver's inventory is full.");
        }

        sender.getBackpack().grabItem(FLOWER_ITEM_NAME, 1);
        String reez = receiver.isGender() ? "pretty" : "handsome";
        receiver.addToNotifications(new Message(senderUsername, receiverUsername, "Here is a flower " + reez + " :)"));
        // Friendship level logic
        if (friendship.getLevel() == 2) {
            friendship.setLevel(3);
        }

        return new Result(true, "Flower sent successfully.");
    }

    public Result cheatWalk(int x, int y){
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        if(x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
            return new Result(false, "You are out of bounds.");
        }
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        currentPlayer.setCurrentTile(map[y][x]);
        return new Result(true, "You are now in tile " + x + ", " + y + ".");
    }

    public Result cheatSetSkill(String skillString, String number) {
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        int amount = Integer.parseInt(number);
        Skill skill = Skill.fromString(skillString);
        if (amount > 4 || amount < 0) {
            return new Result(false, "invalid number");
        }
        if (skill == null) {
            return new Result(false, "invalid skill");
        }

        player.getSkillsLevel().put(skill, amount);
        int currentLevel = player.getSkillsLevel().getOrDefault(skill, 0);
        return new Result(true, "Your " + skill.name() + " skill is " + currentLevel);
    }

    public Result cheatSetFriendshipLevel(int level, String username) {
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Friendship friendships = App.getInstance().getCurrentGame().getFriendship(player.getUsername(), username);
        if (friendships == null) {
            return new Result(false, "Friendship not found.");
        }
        if(level < 0 || level > 3){
            return new Result(false, "Level is out of bounds.");
        }
        friendships.setLevel(level);
        return new Result(true, "You're friendship with " + username + " is now in level " + level + ".");
    }


    public Result cheatAddItem(String itemName, int count) {
        Item item = Item.getRandomItem(itemName);
        if (item == null) {
            return new Result(false, "No item found.");
        }
        return App.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().addItem(item, count);
    }

    public Result meetNPC(String npcName) {
        NPC npc = App.getInstance().getCurrentGame().getNPC(npcName);
        if (npc == null) {return new Result(false, "No npc found.");}
        Tile currentTile =  App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        if (!npc.checkIfIsNearNPC(currentTile)) {
            return new Result(false, "You are not near this npc.");
        }
        WeatherType currentWeatherType = App.getInstance().getCurrentGame().getCurrentWeatherType();
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        return npc.talkToNPC(currentWeatherType, currentPlayer);
    }

    public Result giftNPC(String npcName, String itemName) {
        NPC npc = App.getInstance().getCurrentGame().getNPC(npcName);
        if (npc == null) {
            return new Result(false, "No npc found.");
        }
        Tile currentTile = App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        if (!npc.checkIfIsNearNPC(currentTile)) {
            return new Result(false, "You are not near this npc.");
        }
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        if (currentPlayer.getBackpack().hasItem(itemName, 1)) {
            Result result = npc.giveGift(itemName, currentPlayer);
            if (result.isSuccessful()) {
                currentPlayer.getBackpack().grabItem(itemName, 1);
            }
            return result;
        }
        else return new Result(false, "you dont have that item.");
    }

    public Result npcFriendshipList() {
        StringBuilder result = new StringBuilder();
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        for (NPC npc : App.getInstance().getCurrentGame().getNpcs()) {
            result.append(npc.getNpcName() + " : Level ");
            result.append(npc.getFriendshipLevels().get(currentPlayer.getUsername()));
            result.append(", " + npc.getFriendshipPoints().get(currentPlayer.getUsername()) + " points\n");
        }
        return new Result(true, result.toString());
    }

    public Result npcQuestList() {
        Tile currentTile = App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        NPC wantedNPC = null;
        for (NPC npc : App.getInstance().getCurrentGame().getNpcs()) {
            if (npc.checkIfIsNearNPC(currentTile)) {
                wantedNPC = npc;
                break;
            }
        }
        if (wantedNPC == null) {return new Result(false, "You are not standing next to an npc.");}
        StringBuilder result = new StringBuilder();
        result.append(wantedNPC.getNpcName() + " Unlocked Missions for you :\n");
        int index = 1;
        for (NPCMission mission : wantedNPC.getUnlockedMissions().get(currentPlayer)) {
            result.append("\n" + index + " :\n");
            result.append("Required items:\n");
            for (String item : mission.getRequiredItems().keySet()) {
                result.append(item + " : " + mission.getRequiredItems().get(item) + "\n");
            }
            result.append("Prizes:\n");
            for (String item : mission.getPrizeItems().keySet()) {
                result.append(item + " : " + mission.getPrizeItems().get(item) + "\n");
            }
            if (mission.getAlreadyDone()) result.append("Already done!");
            else result.append("Not done yet!");
            index++;
        }
        return new Result(true, result.toString());
    }

    public Result doMission(int missionIndex) {
        Tile currentTile = App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        NPC wantedNPC = null;
        for (NPC npc : App.getInstance().getCurrentGame().getNpcs()) {
            if (npc.checkIfIsNearNPC(currentTile)) {
                wantedNPC = npc;
                break;
            }
        }
        if (wantedNPC == null) {return new Result(false, "You are not standing next to an npc.");}
        return wantedNPC.doMission(missionIndex, currentPlayer);
    }

    public Result putFoodInFridge(String itemName) {
        Tile currentTile = App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        if (!currentTile.getType().equals(TileType.HOUSE)) {
            return new Result(false, "You are not in your house.");
        }
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        House house = App.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getHouse();
        for (Item item : currentPlayer.getBackpack().getInventoryItems().keySet()) {
            if (item.getName().equals(itemName)) {
                if (!(item instanceof Food)) { return new Result(false, "Thats not a food!"); }
                house.getFridge().put((Food)item, house.getFridge().getOrDefault((Food)item, 0)
                        + currentPlayer.getBackpack().getInventoryItems().get(item));
                currentPlayer.getBackpack().getInventoryItems().remove(item);
                return new Result(true,"item added to fridge successfully.");
            }
        }
        return new Result(false, "Item not found.");
    }

    public Result pickFoodFromFridge(String itemName) {
        Tile currentTile = App.getInstance().getCurrentGame().getCurrentPlayer().getCurrentTile();
        if (!currentTile.getType().equals(TileType.HOUSE)) {
            return new Result(false, "You are not in your house.");
        }
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        House house = App.getInstance().getCurrentGame().getMap().getFarmByOwner(currentPlayer).getHouse();
        for (Item item : house.getFridge().keySet()) {
            if (item.getName().equals(itemName)) {
                Result result = currentPlayer.getBackpack().addItem(item, house.getFridge().get(item));
                if (result.isSuccessful()) house.getFridge().remove(item);
                return result;
            }
        }
        return new Result(false, "Item not found.");
    }

    public Result showCookingRecipes() {
        StringBuilder result = new StringBuilder();
        for (FoodRecipe recipe : App.getInstance().getCurrentGame().getCurrentPlayer().getCookingRecepies()) {
            result.append(recipe.toString() + "\n");
            for (String name : recipe.getRecipe().keySet()) {
                result.append("    " + name + ": " + recipe.getRecipe().get(name) + "\n");
            }
        }
        return new Result(true, result.toString());
    }

    public Result cook(String recipeName) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        String recipe = recipeName.replace(" ", "");
        for (FoodRecipe unlockedRecipe : App.getInstance().getCurrentGame().getCurrentPlayer().getCookingRecepies()) {
            if (unlockedRecipe.toString().equals(recipe)) {
                for (String itemName : unlockedRecipe.getRecipe().keySet()) {
                    if (!currentPlayer.getBackpack().hasItem(itemName, unlockedRecipe.getRecipe().get(itemName))) {
                        return new Result(false, "You dont have the ingredients.");
                    }
                }
                Item food = Item.getRandomItem(recipeName);
                Result result = currentPlayer.getBackpack().addItem(food,1);
                if (!result.isSuccessful()) return result;
                for (String itemName : unlockedRecipe.getRecipe().keySet()) {
                    currentPlayer.getBackpack().grabItem(itemName, unlockedRecipe.getRecipe().get(itemName));
                }
                currentPlayer.reduceEnergy(3);
                return new Result(true, "food added successfully.");
            }
        }
        return new Result(false, "No recipe found.");
    }

    public Result eat(String foodName) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        for (Item item : currentPlayer.getBackpack().getInventoryItems().keySet()) {
            if (item.getName().equals(foodName)) {
                return currentPlayer.eat(item);
            }
        }
        return new Result(false, "Item not found.");
    }

    public Result artisanGet(MapOfGame map, String artisanName) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile currentTile = currentPlayer.getCurrentTile();
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();
        int[] xDirections = {1,1,0,-1,-1,-1,0,1};
        int[] yDirections = {0,-1,-1,-1,0,1,1,1};
        for (int i = 0; i < 8; i++) {
            if (map.getTile(currentX + xDirections[i], currentY + yDirections[i]).getContainedItem() instanceof Machine) {
                Machine machine = ((Machine) map.getTile(currentX + xDirections[i], currentY + yDirections[i]).getContainedItem());
                if (machine.getName().equals(artisanName)) {
                    if (!machine.getReady() && machine.getActivated()) {
                        return new Result(false, "The product isnt ready yet.");
                    } else if (machine.getReady()) {
                        return machine.grabPreparedProduct(currentPlayer);
                    } else if (!machine.getActivated()) {
                        return new Result(false, "No product available");
                    }
                }
            }
        }
        return new Result(false,"No machine found.");
    }

    public Result artisanUse(String artisanName, String itemName1, String itemName2, MapOfGame map) {
        User currentPlayer = App.getInstance().getCurrentGame().getCurrentPlayer();
        Tile currentTile = currentPlayer.getCurrentTile();
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();
        int[] xDirections = {1,1,0,-1,-1,-1,0,1};
        int[] yDirections = {0,-1,-1,-1,0,1,1,1};
        for (int i = 0; i < 8; i++) {
            if (map.getTile(currentX + xDirections[i], currentY + yDirections[i]).getContainedItem() instanceof Machine) {
                Machine machine = ((Machine) map.getTile(currentX + xDirections[i], currentY + yDirections[i]).getContainedItem());
                if (machine.getName().equals(artisanName)) {
                    for (randomStuffType productsName : machine.getType().getProducts()) {
                        boolean hasItem1 = false;
                        boolean hasItem2 = false;
                        if (machine.getType().equals(MachineType.BEE_HOUSE) &&
                                (itemName1 != null || itemName2 != null))
                            return new Result(false, "You cant put ingredients in this machine.");
                        if (itemName2 == null && !machine.getType().equals(MachineType.FURNACE)
                                && !machine.getType().equals(MachineType.FISH_SMOKER)) hasItem2 = true;
                        for (String ingredientName : productsName.getIngredients().keySet()) {
                            if (itemName1 != null) {
                                if (ingredientName.equals(itemName1)) hasItem1 = true;
                            }
                            if (itemName2 != null) {
                                if (ingredientName.equals(itemName2)) hasItem2 = true;
                            }
                        }
                        if (machine.getName().equalsIgnoreCase(MachineType.BEE_HOUSE.getName())) hasItem1 = true;
                        if (hasItem1 && hasItem2) {
                            if (machine.getActivated()) return new Result(false, "This machine is already in use.");
                            return machine.useMachine(productsName.getName(), currentPlayer);
                        }
                        if (hasItem1 && !hasItem2) return new Result(false, "You need more ingredients.");
                    }
                    if (itemName1 == null) return new Result(false, "No item selected!");
                    return new Result(false, "You cant put this item in this machine.");
                }
            }
        }
        return new Result(false,"No machine found.");
    }

    public Result showMoney() {
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        return new Result(true, "Your current money is " + player.getMoney());
    }

    public Result showSkill(String skillString) {
        User player = App.getInstance().getCurrentGame().getCurrentPlayer();
        Skill skill = Skill.fromString(skillString);

        if (skill == null) {
            return new Result(false, "invalid skill");
        }

        int currentLevel = player.getSkillsLevel().getOrDefault(skill, 0);
        return new Result(true, "Your " + skill.name() + " skill is " + currentLevel);

    }


}

