package org.example.Controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.awt.Point;
import java.lang.reflect.Type;


import org.example.Model.*;
import org.example.Model.ConfigTemplates.FarmLoader;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplateManager;
import org.example.Model.Growables.ForagingCropType;
import org.example.Model.Growables.GrowableFactory;
import org.example.Model.Growables.TreeType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Places.Farm;
import org.example.Model.Places.GreenHouse;
import org.example.Model.Places.Habitat;
import org.example.Model.Places.House;
import org.example.Model.Places.Quarry;
import org.example.Model.TimeManagement.Season;
import org.example.Model.Tools.ToolType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GameMenuController {

    //GameMenuCommands command;

    // public Result createNewGame(String username1, String username2, String username3){
    //     //load farm.json            ONLY ONCEEEEEEE
    //     if (FarmTemplateManager.getTemplates() == null) {
    //         FarmTemplateManager.loadTemplates();
    //     }
    //     //create a new game and put it as currentgame in app
    //     //for the newely created game create a map and initialize it with the function initializeMap that exists in MapOfGame class
        
    // }

    //we will call this method for every user 
    public void pickGameMap(User player, int mapNumber){
        App app = App.getInstance();
        Game currentGame = app.getCurrentGame();
        MapOfGame mapOfGame = currentGame.getMap();
        Farm playerFarm;
        if(mapNumber % 2 == 0){
            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_2");
            //find an empty corner
            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
            playerFarm = new Farm(player, template, (int)farmCoordinate.getX(), (int)farmCoordinate.getY());
        }
        else{
            FarmTemplate template = FarmTemplateManager.getTemplateByType("farm_1");
            Point farmCoordinate = isCornerAvailable(mapOfGame.getMap(), template.width, template.height);
            playerFarm = new Farm(player, template, (int)farmCoordinate.getX(), (int)farmCoordinate.getY());
        }
        //update tile types that are in the farm
        Tile[][] map = mapOfGame.getMap(); 
        for(int y = playerFarm.getY(); y < playerFarm.getY() + playerFarm.getHeight(); y++){
            for(int x = playerFarm.getX(); x < playerFarm.getX() + playerFarm.getWidth(); x++){
                Tile tile = map[y][x];
                tile.setTileOwner(player);
                boolean foundSpecial = false;

        for (Habitat lake : playerFarm.getLake()) {
            if (isInHabitat(x, y, lake)) {
                tile.setType(TileType.LAKE);
                tile.setWalkable(false);
                foundSpecial = true;
                break;
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
        if(!foundSpecial && isInHabitat(x, y, greenHouseHabitat)){
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

    // public Result loadGame(){}
    // public Result exitGame(){}
    // public Result removeGame(){}
    // public Result nextTurn(){}
    // public Result showTime(){}
    // public Result showDate(){}
    // public Result showTimeAndDate(){}
    // public Result showDayOfWeek(){}
    // public Result showSeason(){}
    // public Result cheatAdvanceTime(int hours){}
    // public Result cheatAdvanceDate(int days){}
    public void strikeRandomFarm(){}

    public void CheatStrikeLightening(){}

    // public Result showCurrentWeather(){}
    // public Result showPredictedWeather(){}
    // public Result cheatSetWeather(){}
    // public void buildGreenhouse(){}
    // //walk home method 
    // public Result walk(int x, int y){
    //     //in every step call if in any place energy = 0 call faint method 
    // }
    public void printMap(int x, int y, int size){
       Game game = App.getInstance().getCurrentGame();
       Tile[][] map = game.getMap().getMap();
       for(int i = y; i < y + size; i++){
        for(int j = x; j < x + size; j++){
            System.out.print(map[y][x].getType().getLetterToPrint());
        }
        System.out.print("\n");
       }
    }
    // public Result helpReadMap(){}
    // public Result showEnergy(){}
    // public Result cheatEnergySet(int value){}
    // public Result cheatEnergyUnlimited(){}
    // public Result trashInventory(ToolType item, int quantity){}
    // public Result showTradingMenu(){}


    public void startNewDay(){}


    public List<Farm> loadFarmTemplates() {
    Gson gson = new Gson();
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("farm.json")) {
        if (inputStream == null) {
            throw new RuntimeException("farm.json not found in resources.");
        }

        InputStreamReader reader = new InputStreamReader(inputStream);
        Type listType = new TypeToken<List<FarmTemplate>>() {}.getType();
        return gson.fromJson(reader, listType);

    } catch (Exception e) {
            e.printStackTrace();
            return null;
    }
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

    public void creatingRandomForagingForFarm(){
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



    


}
