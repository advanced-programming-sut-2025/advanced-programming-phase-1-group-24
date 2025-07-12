package io.github.stardew.mini.Model.Tools;

import io.github.stardew.mini.Model.Game;
import io.github.stardew.mini.Model.MapManagement.MapOfGame;
import io.github.stardew.mini.Model.MapManagement.Tile;
import io.github.stardew.mini.Model.MapManagement.TileType;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Skill;
import io.github.stardew.mini.Model.Things.Fish;
import io.github.stardew.mini.Model.Things.FishMovementType;
import io.github.stardew.mini.Model.Things.FishType;
import io.github.stardew.mini.Model.Things.ProductQuality;
import io.github.stardew.mini.Model.TimeManagement.Season;
import io.github.stardew.mini.Model.TimeManagement.WeatherType;
import io.github.stardew.mini.Model.User;

import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FishingPole extends Tool {
    FishingPoleMaterial poleMaterial;

    public FishingPole(ToolType type, FishingPoleMaterial poleMaterial) {
        super(type);
        this.poleMaterial = poleMaterial;
    }

    public FishingPoleMaterial getPoleMaterial() {
        return poleMaterial;
    }

    public void upgradeFishingPole(FishingPoleMaterial poleMaterial) {
        this.poleMaterial = poleMaterial;
    }

    public FishingAttemptOutcome useFishingPole(FishingPole pole, MapOfGame map,
                                                Tile currentTile, User currentPlayer,
                                                Game currentGame, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energyRequired = pole.getPoleMaterial().getEnergyRequired();
        energyRequired = (int) (energyRequired * energyWeatherModifier);
        if (currentPlayer.isBuffFishingSkill()) energyRequired--;
        int fishingLevel = currentPlayer.getSkillsLevel().get(Skill.FISHING);
        if (fishingLevel == 4) energyRequired -= 1;
        if (!currentPlayer.tryConsumeEnergy(energyRequired))
            return new FishingAttemptOutcome(new Result(false, "Not enough energy!"), Optional.empty());

        boolean isNearWater = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int checkX = currentX + dx;
                int checkY = currentY + dy;

                if (checkX >= 0 && checkX < map.getWidth() && checkY >= 0 && checkY < map.getHeight()) {
                    if (map.getMap()[checkY][checkX].getType() == TileType.LAKE) {
                        isNearWater = true;
                        break;
                    }
                }
            }
            if (isNearWater) break;
        }

        if (isNearWater) {
            Random random = new Random();

            List<FishType> possibleFishes = new ArrayList<>();
            Season currentSeason = currentGame.getTimeAndDate().getSeason();

            if (pole.getPoleMaterial() == FishingPoleMaterial.Training) {
                if (currentSeason == Season.AUTUMN) possibleFishes.add(FishType.Sardine);
                else if (currentSeason == Season.WINTER) possibleFishes.add(FishType.Perch);
                else if (currentSeason == Season.SPRING) possibleFishes.add(FishType.Herring);
                else if (currentSeason == Season.SUMMER) possibleFishes.add(FishType.Sunfish);
            } else {
                for (FishType fish : FishType.values()) {
                    if (fish.getSeason() == currentSeason) {
                        if (fish.getRareness() == FishType.RarenessType.COMMON) possibleFishes.add(fish);
                        else if (fishingLevel >= 4) possibleFishes.add(fish);
                    }
                }
                if (possibleFishes.isEmpty()) {
                    return new FishingAttemptOutcome(new Result(false, "No fish available in this season/location!"), Optional.empty());
                }
            }

            FishType chosenFishType = possibleFishes.get(random.nextInt(possibleFishes.size()));


            // The hooked fish. Its quality will be determined *after* the minigame.
            Fish hookedFish = new Fish(ProductQuality.Normal, chosenFishType);

            //Randomly select a movement type for the fish
            FishMovementType[] movementTypes = FishMovementType.values();
            FishMovementType chosenMovementType = movementTypes[random.nextInt(movementTypes.length)];


            return new FishingAttemptOutcome(
                new Result(true, "A fish is on the line!"),
                Optional.of(new FishingMinigameData(hookedFish, chosenMovementType))
            );

        } else {
            return new FishingAttemptOutcome(new Result(false, "You are not near water."), Optional.empty());
        }
    }

    @Override
    public FishingPole copy() {
        FishingPole copy = new FishingPole(this.getType(), this.poleMaterial);
        copy.upgrade(this.getMaterial());
        return copy;
    }
}
