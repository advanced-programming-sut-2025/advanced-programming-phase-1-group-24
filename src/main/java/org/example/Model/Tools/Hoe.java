package org.example.Model.Tools;

import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Result;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.User;

public class Hoe extends Tool{
    public Hoe(ToolType type) {
        super(type);
    }

    public Result useHoe(int xDirection, int yDirection, Tile currentTile,
                         MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = this.material.getEnergyRequiered();
        energy = (int)(energy * energyWeatherModifier);
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false, "You don't have enough energy");
        }

        Tile newTile = map.getMap()[currentY + yDirection][currentX + xDirection];
        if(newTile.getContainedGrowable() != null || newTile.getProductOfGrowable() != null || newTile.getContainedItem() != null){
            return new Result(false, "The tile you chose is full!");
        }
        if (newTile.getType() == TileType.FARM || newTile.getType() == TileType.GREENHOUSE) {
            map.getMap()[currentY + yDirection][currentX + xDirection].setIsPlowed(true);
            return new Result(true, "Plowed the ground successfully.");
        }
        else {
            return new Result(false, "You have to use the hoe in the farm or greenhouse.");
        }
    }
    @Override
    public Hoe copy() {
        Hoe copy = new Hoe(this.getType());
        copy.upgrade(this.material);
        return copy;
    }

}
