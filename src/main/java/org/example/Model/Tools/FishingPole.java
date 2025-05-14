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

import java.util.*;

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
//    public Result useFishingPole(FishingPole pole, MapOfGame map,
//                                 Tile currentTile, User currentPlayer,
//                                 Game currentGame, double energyWeatherModifier) {
//
//        int currentX = currentTile.getX();
//        int currentY = currentTile.getY();
//
//        // Calculate required energy
//        int energyRequired = pole.getPoleMaterial().getEnergyRequired();
//        energyRequired = (int) (energyRequired * energyWeatherModifier);
//        if (currentPlayer.getSkillsLevel().get(Skill.FISHING) == 4) {
//            energyRequired -= 1;
//        }
//
//        // Check if player has enough energy
//        if (!currentPlayer.tryConsumeEnergy(energyRequired)) {
//            return new Result(false, "not enough energy!");
//        }
//
//        // Check surrounding tiles for water
//        boolean isNearWater = false;
//        int[] xDirs = {1, 1, 0, -1, -1, -1, 0, 1};
//        int[] yDirs = {0, -1, -1, -1, 0, 1, 1, 1};
//        for (int i = 0; i < 8; i++) {
//            int nx = currentX + xDirs[i];
//            int ny = currentY + yDirs[i];
//            if (nx >= 0 && ny >= 0 && ny < map.getHeight() && nx < map.getWidth()) {
//                if (map.getMap()[ny][nx].getType() == TileType.LAKE) {
//                    isNearWater = true;
//                    break;
//                }
//            }
//        }
//
//        if (!isNearWater) return new Result(false, "you are not near water.");
//
//        // Determine fish number
//        WeatherType weather = currentGame.getCurrentWeatherType();
//        double M = switch (weather) {
//            case SUNNY -> 1.5;
//            case RAIN -> 1.2;
//            case STORM -> 0.5;
//            default -> 1.0;
//        };
//
//        int fishingLevel = currentPlayer.getSkillsLevel().get(Skill.FISHING);
//        double R = Math.random();
//        int fishNum = (int) Math.ceil(R * M * (fishingLevel + 2));
//        if (fishNum > 6) fishNum = 6;
//
//        // Determine fish quality
//        R = Math.random(); // regenerate R
//        double poleValue = pole.getPoleMaterial().getFishQuality();
//        double fishQualityValue = (R * (fishingLevel + 2) * poleValue) / (7 - M);
//        ProductQuality fishQuality = ProductQuality.getQualityByValue(fishQualityValue);
//
//        // Determine possible fish based on season and pole
//        Season season = currentGame.getTimeAndDate().getSeason();
//        List<FishType> possibleFishes = new ArrayList<>();
//
////        if (pole.getPoleMaterial() == FishingPoleMaterial.Training) {
////            switch (season) {
////                case AUTUMN -> possibleFishes.add(FishType.Sardine);
////                case WINTER -> possibleFishes.add(FishType.Perch);
////                case SPRING -> possibleFishes.add(FishType.Herring);
////                case SUMMER -> possibleFishes.add(FishType.Sunfish);
////            }
////        } else {
//            for (FishType fish : FishType.values()) {
//                if (fish.getSeason() == season) {
//                    if (fish.getRareness() == FishType.RarenessType.COMMON ||
//                            (fishingLevel == 4 && fish.getRareness() == FishType.RarenessType.LEGENDARY)) {
//                        possibleFishes.add(fish);
//                    }
//                }
//            }
//       // }
//
//        if (possibleFishes.isEmpty()) return new Result(false, "no fish available for this season.");
//
//        // Catch fish
//        Random rand = new Random();
//        Map<String, Integer> fishCaught = new HashMap<>();
//        Map<String, ProductQuality> fishQualities = new HashMap<>();
//
//        for (int i = 0; i < fishNum; i++) {
//            FishType type = possibleFishes.get(rand.nextInt(possibleFishes.size()));
//            Fish fish = new Fish(fishQuality, type);
//
//            Result result = currentPlayer.getBackpack().addItem(fish, 1);
//            if (!result.isSuccessful()) return result;
//
//            fishCaught.put(type.getName(), fishCaught.getOrDefault(type.getName(), 0) + 1);
//            fishQualities.put(type.getName(), fishQuality);
//        }
//
//        // Add fishing XP
//        currentPlayer.addSkillExperience(Skill.FISHING);
//
//        // Build result message
//        StringBuilder message = new StringBuilder("Caught " + fishNum + " fish:\n");
//        for (String name : fishCaught.keySet()) {
//            message.append("- ").append(name)
//                    .append(" x").append(fishCaught.get(name))
//                    .append(" (").append(fishQualities.get(name)).append(" quality)\n");
//        }
//
//        return new Result(true, message.toString().trim());
//    }

    public Result useFishingPole(FishingPole pole, MapOfGame map,
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
                return new Result(true, "Caught " + fishNum + " " + fish.getName());
            }
        } else {
            return new Result(false, "you are not near water.");
        }
    }
    @Override
    public FishingPole copy() {
        FishingPole copy = new FishingPole(this.getType(), this.poleMaterial);
        copy.upgrade(this.material);
        return copy;
    }

}
