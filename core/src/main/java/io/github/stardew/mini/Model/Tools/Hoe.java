package io.github.stardew.mini.Model.Tools;

import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.ToolMaterial;
import io.github.stardew.mini.Model.User;

public class Hoe extends Tool{
    public Hoe(ToolType type) {
        super(type);
    }
    public Hoe(){}
    public Result useHoe(int xDirection, int yDirection, Tile currentTile,
                         MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = this.material.getEnergyRequiered();
        energy = (int)(energy * energyWeatherModifier);
        if (currentPlayer.isBuffFarmingSkill()) energy--;
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
