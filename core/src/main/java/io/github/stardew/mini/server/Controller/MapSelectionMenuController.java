package io.github.stardew.mini.server.Controller;

import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplate;
import io.github.stardew.mini.Model.ConfigTemplates.FarmTemplateManager;
import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.Growables.ForagingCropType;
import io.github.stardew.mini.Model.Growables.GrowableFactory;
import io.github.stardew.mini.Model.Growables.TreeType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Menus.GameMenuCommands;
import io.github.stardew.mini.Model.Places.*;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.*;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.User;
import io.github.stardew.mini.client.View.MapSelectionMenuView;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class MapSelectionMenuController implements MenuController {
    private MapSelectionMenuView view;

    public void setView(MapSelectionMenuView view) {
        this.view = view;
    }

    private static final Random RANDOM = new Random();

    public void handleMapSelection(List<User> players, Scanner scanner) {
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
        MainApp app = MainApp.getInstance();
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
                Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight(), StorageType.INITIAL, Habitat.HabitatType.Barn);

                if (!foundSpecial && isInHabitat(x, y, quarryHabitat)) {
                    tile.setType(TileType.QUARRY);
                    tile.setWalkable(true);
                    foundSpecial = true;
                }

                House house = playerFarm.getHouse();
                Habitat houseHabitat = new Habitat(house.getX(), house.getY(), house.getWidth(), house.getHeight(), StorageType.INITIAL, Habitat.HabitatType.Barn);
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
                    StorageType.INITIAL,
                    Habitat.HabitatType.Barn
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
                Habitat greenHouseHabitat = new Habitat(greenHouse.getX(), greenHouse.getY(), greenHouse.getWidth(), greenHouse.getHeight(), StorageType.INITIAL, Habitat.HabitatType.Barn);
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
                    , Habitat.HabitatType.Barn
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
        Habitat quarryHabitat = new Habitat(quarry.getX(), quarry.getY(), quarry.getWidth(), quarry.getHeight(), StorageType.INITIAL, Habitat.HabitatType.Barn);
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
        System.out.println("You are starting at coordinates " + player + " " + player.getCurrentTile().getX() + " " + player.getCurrentTile().getY());
    }

    public TreeType findTreeBySourceName(String sourceName) {
        for (TreeType tree : TreeType.values()) {
            if (tree.getSource().getName().equalsIgnoreCase(sourceName)) {
                return tree;
            }
        }
        return null;
    }

    public Result buildGreenHouse() {
        Backpack playerBackPack = MainApp.getInstance().getCurrentGame().getCurrentPlayer().getBackpack();
        if (MainApp.getInstance().getCurrentGame().getCurrentPlayer().getMoney() < 1000 ||
            !playerBackPack.hasItem("Stone", 500)) {
            return new Result(false, "green house build failed");
        }
        Farm farm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(MainApp.getInstance().getCurrentGame().getCurrentPlayer());
        GreenHouse greenHouse = farm.getGreenHouse();
        Tile[][] map = MainApp.getInstance().getCurrentGame().getMap().getMap();
        for (int j = greenHouse.getY(); j < greenHouse.getY() + greenHouse.getHeight(); j++) {
            for (int i = greenHouse.getX(); i < greenHouse.getX() + greenHouse.getWidth(); i++) {
                map[j][i].setWalkable(true);
            }
        }
        MainApp.getInstance().getCurrentGame().getCurrentPlayer().decreaseMoney(1000);
        MainApp.getInstance().getCurrentGame().getCurrentPlayer().getBackpack().grabItem("Stone", 500);
        return new Result(true, "green house build successful");
    }



    public Result exitGame() {
        MainApp app = MainApp.getInstance();
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

    private ForagingMineralType getRandomForagingMineral() {
        ForagingMineralType[] minerals = ForagingMineralType.values();
        return minerals[new Random().nextInt(minerals.length)];
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

    private ForagingCropType getRandomForagingCropBySeason(Season currentSeason) {
        List<ForagingCropType> valid = Arrays.stream(ForagingCropType.values())
            .filter(crop -> crop.getSeason().contains(currentSeason))
            .collect(Collectors.toList());

        if (valid.isEmpty()) {
            return null;
        }
        return valid.get(RANDOM.nextInt(valid.size()));
    }

    private boolean isOnHabitatBorder(int x, int y, Habitat wallHabitat, Habitat mainHabitat) {
        // Tile is inside wallHabitat but NOT inside mainHabitat
        return isInHabitat(x, y, wallHabitat) && !isInHabitat(x, y, mainHabitat);
    }

    private boolean isInHabitat(int x, int y, Habitat h) {
        return x >= h.getX() && x < h.getX() + h.getWidth()
            && y >= h.getY() && y < h.getY() + h.getHeight();
    }

}
