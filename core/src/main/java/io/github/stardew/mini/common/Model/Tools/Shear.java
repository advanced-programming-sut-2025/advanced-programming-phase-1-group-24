package io.github.stardew.mini.common.Model.Tools;

import io.github.stardew.mini.common.Model.Animals.AnimalProduct;
import io.github.stardew.mini.common.Model.Animals.AnimalType;
import io.github.stardew.mini.common.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.common.Model.MapManagement.Tile;
import io.github.stardew.mini.common.Model.Result;
import io.github.stardew.mini.common.Model.Skill;
import io.github.stardew.mini.common.Model.User;

public class Shear extends Tool{
    public Shear(ToolType type) {
        super(type);
    }
    public Shear(){}
    public Result useShear(int xDirection, int yDirection, Tile currentTile, User currentPlayer, MapOfGame map, double weatherModifier) {
        int energy = (int) (4 * weatherModifier);
        if (!currentPlayer.tryConsumeEnergy(energy)) {
            return new Result(false,"You dont have enough energy");
        }
        else {
            int currentX = currentTile.getX();
            int currentY = currentTile.getY();
            Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];
            if (nextTile.getContainedAnimal()!=null && nextTile.getContainedAnimal().getAnimalType() == AnimalType.SHEEP) {
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
    public Shear copy() {
        Shear copy = new Shear(this.getType());
        copy.upgrade(this.material);
        return copy;
    }

}
