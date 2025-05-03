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
        if (nextTile.getContainedGrowable().getGrowableType() == GrowableType.Coal) {
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
            int farmingSkill = currentPlayer.getSkillsLevel().get(Skill.FARMING);
            if (farmingSkill == 0) productOfGrowable.setQuality(ProductQuality.Normal);
            else if (farmingSkill == 1) productOfGrowable.setQuality(ProductQuality.Normal);
            else if (farmingSkill == 2) productOfGrowable.setQuality(ProductQuality.Silver);
            else if (farmingSkill == 3) productOfGrowable.setQuality(ProductQuality.Golden);
            else if (farmingSkill == 4) productOfGrowable.setQuality(ProductQuality.Iridium);
            currentPlayer.getBackpack().addItem(productOfGrowable, 1);
            nextTile.setProductOfGrowable(null);
            currentPlayer.addSkillExperience(Skill.FARMING);
            return new Result(true, "Harvested " + productOfGrowable.getName());
        }
    }
}
