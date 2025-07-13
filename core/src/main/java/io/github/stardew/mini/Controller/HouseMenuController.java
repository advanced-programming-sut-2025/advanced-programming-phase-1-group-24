package io.github.stardew.mini.Controller;

import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Places.Farm;
import io.github.stardew.mini.Model.Places.House;
import io.github.stardew.mini.Model.Reccepies.Machine;
import io.github.stardew.mini.Model.Reccepies.MachineType;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.Backpack;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.User;

import java.util.ArrayList;
import java.util.Map;

public class HouseMenuController implements MenuController {
    public Result showRecipes() {
        User player = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        ArrayList<MachineType> machineRecipes = player.getMachineRecepies();
        House house = MainApp.getInstance().getCurrentGame().getMap().getHousePosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if(house == null){
            return new Result(false, "You need to be in house to use this menu!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Machine Recipes:\n");

        for (MachineType recipe : machineRecipes) {
            sb.append("- ").append(recipe.getName()).append(" | Ingredients: ");
            recipe.getRecipe().forEach((ingredient, amount) ->
                    sb.append(ingredient).append(": ").append(amount).append(", ")
            );
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }


        return new Result(true, sb.toString());
    }

    public Result craft(String itemName) {
        User player = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        House house = MainApp.getInstance().getCurrentGame().getMap().getHousePosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if(house == null){
            return new Result(false, "You need to be in house to use this menu!");
        }
        MachineType machineToCraft = null;
        for (MachineType type : MachineType.values()) {
            if (type.getName().equalsIgnoreCase(itemName)) {
                machineToCraft = type;
                break;
            }
        }

        if (machineToCraft == null) {
            return new Result(false, "No machine with name: " + itemName);
        }
        if(!player.getMachineRecepies().contains(machineToCraft)){
            return new Result(false, "You haven't learnt this recipe to add this machine!");
        }
        Map<String, Integer> recipe = machineToCraft.getRecipe();
        Backpack backpack = player.getBackpack();

        for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
            String item = entry.getKey();
            int requiredAmount = entry.getValue();
            if (!backpack.hasItem(item, requiredAmount)) {
                return new Result(false, "Not enough " + item + " to craft " + itemName);
            }
        }

        for (Map.Entry<String, Integer> entry : recipe.entrySet()) {
            backpack.grabItem(entry.getKey(), entry.getValue());
        }
        player.reduceEnergy(2);
        Result result = player.getBackpack().addItem(new Machine(machineToCraft), 1);
        if(!result.isSuccessful()) return result;
        result = placeItemOnTheGround(itemName);
        return result;
    }
    public Result placeItemOnTheGround(String itemName) {
        Farm farm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(MainApp.getInstance().getCurrentGame().getCurrentPlayer());
        Tile[][] map = MainApp.getInstance().getCurrentGame().getMap().getMap();
        House house = farm.getHouse();
        User player = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        Item item = player.getBackpack().grabItemAndReturn(itemName, 1);
        if(item == null || !item.isPlaceable()){
            return new Result(false, "the item is not placeable or doesn't exist!");
        }
        for(int i = farm.getX(); i < farm.getX() + farm.getWidth(); i++){
            for(int j = farm.getY(); j < farm.getY() + farm.getWidth(); j++){
                if(map[j][i].getProductOfGrowable() != null || map[j][i].getContainedGrowable() != null ||
                    map[j][i].getContainedItem() != null || !map[j][i].getisWalkable()){
                    continue;
                }
                map[j][i].setContainedItem(item);
                if(item instanceof Machine) house.getMachines().add((Machine) item);
                return new Result(true, itemName + " placed successfully!");
            }

        }
        return new Result(false, "No Place Found!");
    }

    public Result placeItem(String itemName, String direction) {
        User player = MainApp.getInstance().getCurrentGame().getCurrentPlayer();
        Farm farm = MainApp.getInstance().getCurrentGame().getMap().getFarmByOwner(player);
        House house = farm.getHouse();
        Tile[][] map = MainApp.getInstance().getCurrentGame().getMap().getMap();
        int x = player.getCurrentTile().getX();
        int y = player.getCurrentTile().getY();
        if(direction.equals("up")) y--;
        else if(direction.equals("down")) y++;
        else if(direction.equals("left")) x--;
        else if(direction.equals("right")) x++;
        if(x < 0 || y < 0 || x >= map[0].length || y >= map.length || !direction.matches("up|down|left|right")) {
            return new Result(false, "direction is invalid.");
        }

        if(map[y][x].getProductOfGrowable() != null || map[y][x].getContainedGrowable() != null ||
                map[y][x].getContainedItem() != null || !map[y][x].getisWalkable()){
            return new Result(false, "tile is full!");
        }

        if(map[y][x].getType() == TileType.SHIPPINGBIN){
            return new Result(false, "You cannot place item in shipping bin!");
        }

        Item item = player.getBackpack().grabItemAndReturn(itemName, 1);
        if(item == null || !item.isPlaceable()){
            return new Result(false, "the item is not placeable or doesn't exist!");
        }

        map[y][x].setContainedItem(item);
        if(item instanceof Machine) house.getMachines().add((Machine) item);
        return new Result(true, itemName + " placed successfully!");

    }



}
