package org.example.Model.Growables;

import org.example.Model.TimeManagement.Season;
import java.util.*;

public enum CropType {
BlueJazz("Blue Jazz",SourceType.JazzSeeds, 7, true, 0, 50, true, 45,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 2), false),
Carrot("Carrot",SourceType.CarrotSeeds, 3, true, 0, 35, true, 75,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1), false),
Cauliflower("Cauliflower", SourceType.CauliflowerSeeds, 12, true, 0, 175, true, 75,
    new ArrayList<>(List.of(Season.SPRING)), true, List.of(1, 2, 4, 4, 1), false),
CoffeeBean("Coffee Bean", SourceType.CoffeeBean, 10, false, 2, 15, false, 0,
    new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER)), false, List.of(1, 2, 2, 3, 2), false),
Garlic("Garlic", SourceType.GarlicSeeds, 4, true, 0, 60, true, 20,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 1), false),
GreenBean("Green Bean", SourceType.BeanStarter, 10, false, 3, 40, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 3, 4), false),
Kale("Kale", SourceType.KaleSeeds, 6, true, 0, 110, true, 50,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 1), false),
Parsnip("Parsnip", SourceType.ParsnipSeeds, 4, true, 0, 35, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 1), false),
Potato("Potato", SourceType.PotatoSeeds, 6, true, 0, 80, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 2, 1), false),
Rhubarb("Rhubarb", SourceType.RhubarbSeeds, 13, true, 0, 220, false, 0,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(2, 2, 2, 3, 4), false),
Strawberry("Strawberry", SourceType.StrawberrySeeds, 8, false, 4, 120, true, 50,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 2, 2, 2), false),
Tulip("Tulip", SourceType.TulipBulb, 6, true, 0, 30, true, 45,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 2, 2), false),
UnmilledRice("UnmilledRice", SourceType.RiceShoot, 8, true, 0, 30, true, 3,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 3), false),
Blueberry("Blueberry", SourceType.BlueberrySeeds, 13, false, 4, 50, true, 25,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 3, 3, 4, 2), false),
Corn("Corn", SourceType.CornSeeds, 14, false, 4, 50, true, 25,
    new ArrayList<>(Arrays.asList(Season.SUMMER, Season.AUTUMN)), false, List.of(2, 3, 3, 3, 3), false),
Hops("Hops", SourceType.HopsStarter, 11, false, 1, 25, true, 45,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 1, 2, 3, 4), false),
HotPepper("HotPepper", SourceType.PepperSeeds, 5, false, 3, 40, true, 13,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 1, 1, 1, 1), false),
Melon("Melon", SourceType.MelonSeeds, 12, true, 0, 250, true, 113,
    new ArrayList<>(List.of(Season.SUMMER)), true, List.of(1, 2, 3, 3, 3), false);

private final String name;
private final SourceType source;
private final int totalHarvestTime;
private final boolean oneTime;
private final int regrowthTime;
private final int baseSellPrice;
private final boolean isEdible;
private final int energy;
private final ArrayList<Season> seasons;
private final List<Integer> stages;
private final boolean canBeGiant;

    CropType(String name, SourceType source, int totalHarvestTime, boolean oneTime, int regrowthTime,
                 int baseSellPrice, boolean isEdible, int energy, ArrayList<Season> seasons,
                 boolean canBeGiant, List<Integer> stages, boolean isForagingCrop) {
        this.name = name;
        this.source = source;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.canBeGiant = canBeGiant;
        this.stages = stages;
        this.seasons = seasons;
    }

    public String getName() {
        return name;
    }
    public SourceType getSource() {
        return source;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public int getRegrowthTime() {
        return regrowthTime;
    }
    public int getBaseSellPrice() {
        return baseSellPrice;
    }
    public int getEnergy() {
        return energy;
    }
    public List<Integer> getStages() {
        return stages;
    }
    public ArrayList<Season> getSeasons() {
        return seasons;
    }
    public boolean getIsEdible(){
        return isEdible;
    }
    public boolean getCanBeGiant(){
        return canBeGiant;
    }
    public boolean oneTime(){
        return oneTime;
    }
}
