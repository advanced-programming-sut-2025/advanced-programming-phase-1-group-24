package org.example.Model.Tools;

import org.example.Model.Growables.Growable;
import org.example.Model.Growables.GrowableFactory;
import org.example.Model.Growables.GrowableType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.User;

public class Axe extends Tool {
    public Axe(ToolType type) {
        super(type);
    }

    public Result useAxe(int xDirection, int yDirection, Tile currentTile,
                         MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = this.material.getEnergyRequiered();
        energy = (int)(energy * energyWeatherModifier);
        if (currentPlayer.getSkillsLevel().get(Skill.FORAGING) == 4)  energy -= 1;
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false, "You don't have enough energy");
        }

        Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];
        Growable containedGrowable = nextTile.getContainedGrowable();
        if (containedGrowable != null && containedGrowable.getGrowableType() == GrowableType.Tree) {
            Growable productOfGrowable = nextTile.getProductOfGrowable();
            if (productOfGrowable != null)
                currentPlayer.getBackpack().addItem(productOfGrowable, 20); //might change later
            // randomStuff temp = new randomStuff(randomStuffType.Wood); //might change later
            //currentPlayer.getBackpack().addItem(temp, 20); //might change later
            //if (containedGrowable.getTreeType().getIsForagingTree()) {
                Growable seeds = GrowableFactory.getInstance().create(containedGrowable.getTreeType().getSource());
                currentPlayer.getBackpack().addItem(seeds, 10); //might change later
            //}
            nextTile.setProductOfGrowable(null);
            nextTile.setContainedGrowable(null);
            currentPlayer.addSkillExperience(Skill.FORAGING);
            return new Result(true, "The tree was cut successfully.");
        }

        return new Result(false, "No tree to cut here!");

    }
    @Override
    public Axe copy() {
        Axe copy = new Axe(this.getType());
        copy.upgrade(this.material); // Copy any relevant fields (like material if applicable)
        return copy;
    }


}
