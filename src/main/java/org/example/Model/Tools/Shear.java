package org.example.Model.Tools;

import org.example.Model.Animals.AnimalProduct;
import org.example.Model.Animals.AnimalType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.User;

public class Shear extends Tool{
    public Shear(ToolType type) {
        super(type);
    }
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
