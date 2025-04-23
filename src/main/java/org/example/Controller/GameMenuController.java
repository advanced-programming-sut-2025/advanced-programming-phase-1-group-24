package org.example.Controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Point;

import org.example.Model.*;
import org.example.Model.ConfigTemplates.FarmLoader;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplateManager;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Places.Farm;
import org.example.Model.Tools.ToolType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GameMenuController {

    //GameMenuCommands command;

    public Result createNewGame(String username1, String username2, String username3){
        //load farm.json            ONLY ONCEEEEEEE
        if (FarmTemplateManager.getTemplates() == null) {
            FarmTemplateManager.loadTemplates();
        }
        //create a new game and put it as currentgame in app
        //for the newely created game create a map and initialize it with the function initializeMap that exists in MapOfGame class
        
    }

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
                
            }
        }


    }
    public Result loadGame(){}
    public Result exitGame(){}
    public Result removeGame(){}
    public Result nextTurn(){}
    public Result showTime(){}
    public Result showDate(){}
    public Result showTimeAndDate(){}
    public Result showDayOfWeek(){}
    public Result showSeason(){}
    public Result cheatAdvanceTime(int hours){}
    public Result cheatAdvanceDate(int days){}
    public void strikeRandomFarm(){}

    public void CheatStrikeLightening(){}

    public Result showCurrentWeather(){}
    public Result showPredictedWeather(){}
    public Result cheatSetWeather(){}
    public void buildGreenhouse(){}
    public Result walk(int x, int y){}
    public Result printMap(int x, int y, int size){}
    public Result helpReadMap(){}
    public Result showEnergy(){}
    public Result cheatEnergySet(int value){}
    public Result cheatEnergyUnlimited(){}
    public Result trashInventory(ToolType item, int quantity){}
    public Result showTradingMenu(){}


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

    


}
