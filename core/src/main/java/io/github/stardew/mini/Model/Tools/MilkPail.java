package io.github.stardew.mini.Model.Tools;

import io.github.stardew.mini.Model.Animals.AnimalProduct;
import io.github.stardew.mini.Model.Animals.AnimalType;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Skill;
import io.github.stardew.mini.Model.User;

public class MilkPail extends Tool {
    public MilkPail(ToolType type) {
        super(type);
    }

    public Result useMilkPail(int xDirection, int yDirection, Tile currentTile, User currentPlayer, MapOfGame map, double weatherModifier) {
        int energy = (int) (4 * weatherModifier);
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false,"You dont have enough energy");
        }
        else {
            int currentX = currentTile.getX();
            int currentY = currentTile.getY();
            Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];
            if (nextTile.getContainedAnimal()!=null && (nextTile.getContainedAnimal().getAnimalType() == AnimalType.COW
                    || nextTile.getContainedAnimal().getAnimalType() == AnimalType.GOAT)) {
                if (nextTile.getContainedAnimal().hasProduct()) {
                    AnimalProduct collectedProduct = nextTile.getContainedAnimal().collectProduct();
                    currentPlayer.getBackpack().addItem(collectedProduct, 1);
                    currentPlayer.addSkillExperience(Skill.FARMING);
                    return new Result(true,"You have collected " + collectedProduct.getName());
                }
                else {
                    return new Result(false,"No product to collect.");
                }
            }
            else {
                return new Result(false,"No animal to collect from.");
            }

        }
    }
    @Override
    public MilkPail copy() {
        MilkPail copy = new MilkPail(this.getType());
        // Make sure the material is properly copied
        copy.upgrade(this.material);
        return copy;
    }


}
