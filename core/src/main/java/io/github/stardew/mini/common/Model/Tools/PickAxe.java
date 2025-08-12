package io.github.stardew.mini.common.Model.Tools;

import io.github.stardew.mini.common.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.common.Model.MapManagement.Tile;
import io.github.stardew.mini.common.Model.MapManagement.TileType;
import io.github.stardew.mini.common.Model.Result;
import io.github.stardew.mini.common.Model.Skill;
import io.github.stardew.mini.common.Model.Things.Backpack;
import io.github.stardew.mini.common.Model.Things.ForagingMineral;
import io.github.stardew.mini.common.Model.User;

public class PickAxe extends Tool{
    public PickAxe(ToolType type) {
        super(type);
    }
    public PickAxe(){}
    public Result usePickAxe(int xDirection, int yDirection, Tile currentTile,
                             MapOfGame map, User currentPlayer, double energyWeatherModifier){

        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energyRequired = this.material.getEnergyRequiered();
        energyRequired = (int) (energyRequired * energyWeatherModifier);
        if (currentPlayer.isBuffMiningSkill()) energyRequired--;
        int miningLevel = currentPlayer.getSkillsLevel().get(Skill.MINING);
        if (miningLevel == 4) energyRequired -= 1;
        if (!currentPlayer.tryConsumeEnergy(energyRequired)) {
            return new Result(false, "You don't have enough energy");
        }

        Tile nextTile = map.getMap()[currentY + yDirection][currentX + xDirection];
        if(nextTile.getContainedGrowable() != null || nextTile.getProductOfGrowable() != null){
            return new Result(false, "The tile you chose has growable in it!");
        }
        if(nextTile.getType() == TileType.QUARRY){
            if(nextTile.getContainedItem() != null && nextTile.getContainedItem() instanceof ForagingMineral){
                ForagingMineral foragingMineral = (ForagingMineral) nextTile.getContainedItem();
                Backpack playerBackPack = currentPlayer.getBackpack();
                playerBackPack.addItem(foragingMineral, 20);
                nextTile.setContainedItem(null);
                if(miningLevel >= 2){
                    playerBackPack.addItem(foragingMineral, 20);
                }
                currentPlayer.addSkillExperience(Skill.MINING);
                nextTile.setWalkable(true);
                return new Result(true, "successfully picked mineral!");
            }
            else if(nextTile.getContainedItem() != null){
                nextTile.setContainedItem(null);
                nextTile.setWalkable(true);
                return new Result(true, "The item on this tile has been destroyed!");
            }
            else {
                return new Result(true, "There is no mineral in this tile!");
            }
        }
        else if(nextTile.getType() == TileType.FARM || nextTile.getType() == TileType.GREENHOUSE){
            if(nextTile.getIsPlowed()){
                nextTile.setIsPlowed(false);
                System.out.println(map.getMap()[currentY + yDirection][currentX + xDirection].getIsPlowed());
                nextTile.setWalkable(true);
                return new Result(true, "The tile isn't plowed anymore!");
            }
            else if(nextTile.getContainedItem() != null){
                nextTile.setContainedItem(null);
                nextTile.setWalkable(true);
                return new Result(true, "The item on this tile has been destroyed!");
            }
            else{
                return new Result(true, "You misused PickAxe!");
            }
        }
        else if(nextTile.getContainedItem() != null){
            nextTile.setContainedItem(null);
            nextTile.setWalkable(true);
            return new Result(true, "The item on this tile has been destroyed!");
        }
        else{
            return new Result(true, "You misused PickAxe!");
        }
    }
    @Override
    public PickAxe copy() {
        PickAxe copy = new PickAxe(this.getType());
        copy.upgrade(this.material); // Copy any relevant fields (like material if applicable)
        return copy;
    }
}
