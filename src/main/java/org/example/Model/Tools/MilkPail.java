package org.example.Model.Tools;

import org.example.Model.Animals.AnimalProduct;
import org.example.Model.Animals.AnimalType;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.User;

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
