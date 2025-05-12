package org.example.Model.Tools;

import org.example.Model.Growables.Growable;
import org.example.Model.Growables.GrowableType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.Things.ForagingMineral;
import org.example.Model.Things.ProductQuality;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.User;

public class Scythe extends Tool{
    public Scythe(ToolType type) {
        super(type);
    }

    @Override
    public void upgrade(ToolMaterial material) {
        return;
    }

    public Result useScythe(int xDirection, int yDirection, Tile currentTile,
                            MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = 2;
        energy = (int)(energy * energyWeatherModifier);
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false, "You don't have enough energy");
        }

        Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];
        Growable productOfGrowable = nextTile.getProductOfGrowable();
        if (nextTile.getContainedGrowable() != null && nextTile.getContainedGrowable().getGrowableType() == GrowableType.Coal) {
            ForagingMineral coal = (ForagingMineral) nextTile.getContainedItem();
            currentPlayer.getBackpack().addItem(coal, 1);
            nextTile.setContainedGrowable(null);
            nextTile.setContainedItem(null);
            currentPlayer.addSkillExperience(Skill.FARMING);
            return new Result(true, "Harvested " + coal.getName());
        }
        if (productOfGrowable == null)
            return new Result(false, "Nothing to harvest here.");
        else {
            Tile[][] tiles = map.getMap();
            int farmingSkill = currentPlayer.getSkillsLevel().get(Skill.FARMING);
            if (farmingSkill == 0) productOfGrowable.setQuality(ProductQuality.Normal);
            else if (farmingSkill == 1) productOfGrowable.setQuality(ProductQuality.Normal);
            else if (farmingSkill == 2) productOfGrowable.setQuality(ProductQuality.Silver);
            else if (farmingSkill == 3) productOfGrowable.setQuality(ProductQuality.Golden);
            else if (farmingSkill == 4) productOfGrowable.setQuality(ProductQuality.Iridium);
            if(nextTile.getProductOfGrowable().getGrowableType() == GrowableType.Giant){
                currentPlayer.getBackpack().addItem(productOfGrowable, 10);
                for(int j = Math.max(0 , nextTile.getY() - 1); j <= Math.min(149, nextTile.getY() + 1); j++){
                    for(int i = Math.max(0, nextTile.getX() - 1); i <= Math.min(149, nextTile.getX() + 1); i++){
                        if(tiles[j][i].getProductOfGrowable() != null && tiles[j][i].getProductOfGrowable().getGrowableType() == GrowableType.Giant){
                            tiles[j][i].setProductOfGrowable(null);
                        }
                    }
                }
            }
            else {
                //if(productOfGrowable.getGrowableType() == GrowableType.Fruit) productOfGrowable.setIsEdible;
                currentPlayer.getBackpack().addItem(productOfGrowable, 1);
                nextTile.setProductOfGrowable(null);
            }
            currentPlayer.addSkillExperience(Skill.FARMING);
            return new Result(true, "Harvested " + productOfGrowable.getName());
        }
    }
}
