package org.example.Model.Tools;

import org.example.Model.Growables.Growable;
import org.example.Model.Growables.GrowableType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.User;

public class WateringCan extends Tool{
    private int maxCapacity;
    private int waterLeft;

    public WateringCan(ToolType type) {
        super(type);
        this.maxCapacity = 40;
        this.waterLeft = 0;
    }


    public int getMaxCapacity() { return maxCapacity; }
    public int getWaterLeft() { return waterLeft; }
    public void setWaterLeft(int waterLeft) { this.waterLeft = waterLeft; }

    @Override
    public void upgrade(ToolMaterial material) {
        super.upgrade(material);
        if (material == ToolMaterial.Copper) maxCapacity = 55;
        else if (material == ToolMaterial.Iron) maxCapacity = 70;
        else if (material == ToolMaterial.Gold) maxCapacity = 85;
        else if (material == ToolMaterial.Iridium) maxCapacity = 100;
    }

    public Result useWateringCan(int xDirection, int yDirection, Tile currentTile,
                                 MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = this.material.getEnergyRequiered();
        energy = (int)(energy * energyWeatherModifier);
        if (currentPlayer.getSkillsLevel().get(Skill.FARMING) == 4)  energy -= 1;
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false, "You don't have enough energy");
        }

        Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];

        boolean isNearWater = false;
        if (nextTile.getType() == TileType.LAKE || nextTile.getType() == TileType.WATERCONTAINER)
            isNearWater = true;
        if (isNearWater) {
            this.waterLeft = this.maxCapacity;
            return new Result(true, "WateringCan is full!");
        }

        if (nextTile.getType() == TileType.FARM || nextTile.getType() == TileType.GREENHOUSE) {
            Growable growable = nextTile.getContainedGrowable();
            if (growable == null)
                return new Result(false, "You have not planted anything!");
            else {
                growable.setIsWateredToday(true);
                Tile[][] tiles = map.getMap();
                if(growable.getGrowableType() == GrowableType.Giant){
                    for(int j = Math.max(0 , nextTile.getY() - 1); j <= Math.min(149, nextTile.getY() + 1); j++){
                        for(int i = Math.max(0, nextTile.getX() - 1); i <= Math.min(149, nextTile.getX() + 1); i++){
                            if(tiles[j][i].getContainedGrowable() != null && tiles[j][i].getContainedGrowable().getGrowableType() == GrowableType.Giant){
                                tiles[j][i].getContainedGrowable().setIsWateredToday(true);
                            }
                        }
                    }
                }
                this.waterLeft -= 1;
                return new Result(true, "Watered successfully!");
            }
        }

        else
            return new Result(false, "You need to be near the farm!");
    }
}
