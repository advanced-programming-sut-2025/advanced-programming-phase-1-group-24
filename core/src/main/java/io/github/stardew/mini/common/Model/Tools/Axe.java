package io.github.stardew.mini.common.Model.Tools;

import io.github.stardew.mini.common.Model.Growables.Growable;
import io.github.stardew.mini.common.Model.Growables.GrowableFactory;
import io.github.stardew.mini.common.Model.Growables.GrowableType;
import io.github.stardew.mini.common.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.common.Model.MapManagement.Tile;
import io.github.stardew.mini.common.Model.Reccepies.randomStuff;
import io.github.stardew.mini.common.Model.Reccepies.randomStuffType;
import io.github.stardew.mini.common.Model.Result;
import io.github.stardew.mini.common.Model.Skill;
import io.github.stardew.mini.common.Model.User;

public class Axe extends Tool {
    public Axe(ToolType type) {
        super(type);
    }
    public Axe(){}
    public Result useAxe(int xDirection, int yDirection, Tile currentTile,
                         MapOfGame map, User currentPlayer, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energy = this.material.getEnergyRequiered();
        energy = (int)(energy * energyWeatherModifier);
        if (currentPlayer.isBuffForagingSkill()) energy--;
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
            randomStuff temp = new randomStuff(200, randomStuffType.Wood); //might change later
            currentPlayer.getBackpack().addItem(temp, 20); //might change later
            //if (containedGrowable.getTreeType().getIsForagingTree()) {
            Growable seeds = GrowableFactory.getInstance().create(containedGrowable.getTreeType().getSource());
            currentPlayer.getBackpack().addItem(seeds, 10); //might change later
            //}
            nextTile.setProductOfGrowable(null);
            nextTile.setContainedGrowable(null);
            nextTile.setWalkable(true);
            currentPlayer.addSkillExperience(Skill.FORAGING);
            return new Result(true, "The tree was cut successfully.");
        }
        else if(nextTile.getContainedItem() != null && nextTile.getContainedItem() instanceof randomStuff &&
                ((randomStuff) nextTile.getContainedItem()).getType() == randomStuffType.Wood) {
            nextTile.setContainedItem(null);
            nextTile.setWalkable(true);
            return new Result(true, "The woods on the ground have been destroyed.");
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
