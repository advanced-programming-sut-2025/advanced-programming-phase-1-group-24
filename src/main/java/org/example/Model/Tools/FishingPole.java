package org.example.Model.Tools;

import org.example.Model.Game;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Result;
import org.example.Model.Skill;
import org.example.Model.Things.Fish;
import org.example.Model.Things.FishType;
import org.example.Model.Things.ProductQuality;
import org.example.Model.Things.ToolMaterial;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.WeatherType;
import org.example.Model.User;

import java.util.ArrayList;
import java.util.List;
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

    public Result useFishingPole(FishingPole pole, MapOfGame map,
                                 Tile currentTile, User currentPlayer,
                                 Game currentGame, double energyWeatherModifier) {
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();

        int energyRequired = pole.getPoleMaterial().getEnergyRequired();
        energyRequired = (int) (energyRequired * energyWeatherModifier);
        int fishingLevel = currentPlayer.getSkillsLevel().get(Skill.FISHING);
        if (fishingLevel == 4) energyRequired -= 1;
        if (!currentPlayer.tryConsumeEnergy(energyRequired))
            return new Result(false, "not enough energy!");

        boolean isNearWater = false;
        int[] xDirections = {1, 1, 0, -1, -1, -1, 0, 1};
        int[] yDirections = {0, -1, -1, -1, 0, 1, 1, 1};
        for (int a = 0; a < 8; a++) {
            if (map.getMap()[currentY + yDirections[a]][currentX + xDirections[a]].getType() == TileType.LAKE)
                isNearWater = true;
        }

        if (isNearWater) {
            double randomValue = Math.random();
            WeatherType currentWeather = currentGame.getCurrentWeatherType();
            double m;
            if (currentWeather == WeatherType.SUNNY) m = 1.5;
            else if (currentWeather == WeatherType.RAIN) m = 1.2;
            else if (currentWeather == WeatherType.STORM) m = 0.5;
            else m = 1.0;

            int fishNum = (int) Math.ceil((randomValue * m * (fishingLevel + 2)));
            if (fishNum > 6) fishNum = 6;

            randomValue = Math.random();
            double rodQuality = pole.getPoleMaterial().getFishQuality();
            double fishQuality = (randomValue * (fishingLevel + 2) * rodQuality) / (7 - m);

            Season currentSeason = currentGame.getTimeAndDate().getSeason();

            List<FishType> possibleFishes = new ArrayList<>();

            if (pole.getPoleMaterial() == FishingPoleMaterial.Training) {
                if (currentSeason == Season.AUTUMN) possibleFishes.add(FishType.Sardine);
                else if (currentSeason == Season.WINTER) possibleFishes.add(FishType.Perch);
                else if (currentSeason == Season.SPRING) possibleFishes.add(FishType.Herring);
                else if (currentSeason == Season.SUMMER) possibleFishes.add(FishType.Sunfish);
            } else {
                for (FishType fish : FishType.values()) {
                    if (fish.getSeason() == currentSeason) {
                        if (fish.getRareness() == FishType.RarenessType.COMMON) possibleFishes.add(fish);
                        else if (fishingLevel == 4) possibleFishes.add(fish);
                    }
                }
            }

            Random random = new Random();
            int index = random.nextInt(possibleFishes.size());
            Fish fish = new Fish(ProductQuality.getQualityByValue(fishQuality), possibleFishes.get(index));
            Result result = currentPlayer.getBackpack().addItem(fish, fishNum);
            if (!result.isSuccessful())
                return result;
            else {
                currentPlayer.addSkillExperience(Skill.FISHING);
                return new Result(true, "Caught" + fishNum + " " + fish.getName());
            }
        } else {
            return new Result(false, "you are not near water.");
        }
    }
}
