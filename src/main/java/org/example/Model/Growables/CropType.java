package org.example.Model.Growables;

import org.example.Model.TimeManagement.Season;
import java.util.*;

public enum CropType {
BlueJazz("Blue Jazz",SourceType.JazzSeeds, 7, true, 0, 50, true, 45,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 2)),
Carrot("Carrot",SourceType.CarrotSeeds, 3, true, 0, 35, true, 75,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1)),
Cauliflower("Cauliflower", SourceType.CauliflowerSeeds, 12, true, 0, 175, true, 75,
    new ArrayList<>(List.of(Season.SPRING)), true, List.of(1, 2, 4, 4, 1)),
CoffeeBean("Coffee Bean", SourceType.CoffeeBean, 10, false, 2, 15, false, 0,
    new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER)), false, List.of(1, 2, 2, 3, 2)),
Garlic("Garlic", SourceType.GarlicSeeds, 4, true, 0, 60, true, 20,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 1)),
GreenBean("Green Bean", SourceType.BeanStarter, 10, false, 3, 40, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 3, 4)),
Kale("Kale", SourceType.KaleSeeds, 6, true, 0, 110, true, 50,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 1)),
Parsnip("Parsnip", SourceType.ParsnipSeeds, 4, true, 0, 35, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 1)),
Potato("Potato", SourceType.PotatoSeeds, 6, true, 0, 80, true, 25,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 1, 2, 1)),
Rhubarb("Rhubarb", SourceType.RhubarbSeeds, 13, true, 0, 220, false, 0,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(2, 2, 2, 3, 4)),
Strawberry("Strawberry", SourceType.StrawberrySeeds, 8, false, 4, 120, true, 50,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 2, 2, 2)),
Tulip("Tulip", SourceType.TulipBulb, 6, true, 0, 30, true, 45,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 1, 2, 2)),
UnmilledRice("UnmilledRice", SourceType.RiceShoot, 8, true, 0, 30, true, 3,
    new ArrayList<>(List.of(Season.SPRING)), false, List.of(1, 2, 2, 3)),
Blueberry("Blueberry", SourceType.BlueberrySeeds, 13, false, 4, 50, true, 25,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 3, 3, 4, 2)),
Corn("Corn", SourceType.CornSeeds, 14, false, 4, 50, true, 25,
    new ArrayList<>(Arrays.asList(Season.SUMMER, Season.AUTUMN)), false, List.of(2, 3, 3, 3, 3)),
Hops("Hops", SourceType.HopsStarter, 11, false, 1, 25, true, 45,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 1, 2, 3, 4)),
HotPepper("HotPepper", SourceType.PepperSeeds, 5, false, 3, 40, true, 13,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 1, 1, 1, 1)),
Melon("Melon", SourceType.MelonSeeds, 12, true, 0, 250, true, 113,
    new ArrayList<>(List.of(Season.SUMMER)), true, List.of(1, 2, 3, 3, 3)),
Poppy("Poppy", SourceType.PoppySeeds, 7, true, 0, 140, true, 45,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 2, 2, 2)),
Radish("Radish", SourceType.RadishSeeds, 6, true, 0, 90, true, 45,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(2, 1, 2, 1)),
RedCabbage("Red Cabbage", SourceType.RedCabbageSeeds, 9, true, 0, 260, true, 75,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(2, 1, 2, 2, 2)),
Starfruit("Starfruit", SourceType.StarfruitSeeds, 13, true, 0, 750, true, 125,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(2, 3, 2, 3, 3)),
SummerSpangle("Summer Spangle", SourceType.SpangleSeeds, 8, true, 0, 90, true, 45,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 2, 3, 1)),
SummerSquash("Summer Squash", SourceType.SummerSquashSeeds, 6, false, 3, 45, true, 63,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(1, 1, 1, 2, 1)),
Sunflower("Sunflower", SourceType.SunflowerSeeds, 8, true, 0, 80, true, 45,
    new ArrayList<>(List.of(Season.SUMMER, Season.AUTUMN)), false, List.of(1, 2, 3, 2)),
Tomato("Tomato", SourceType.TomatoSeeds, 11, false, 4, 60, true, 20,
    new ArrayList<>(List.of(Season.SUMMER)), false, List.of(2, 2, 2, 2, 3)),
Wheat("Wheat", SourceType.WheatSeeds, 4, true, 0, 25, false, 0,
    new ArrayList<>(List.of(Season.SUMMER, Season.AUTUMN)), false, List.of(1, 1, 1, 1)),
Amaranth("Amaranth", SourceType.AmaranthSeeds, 7, true, 0, 150, true, 50,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 2, 2, 2)),
Artichoke("Artichoke", SourceType.ArtichokeSeeds, 8, true, 0, 160, true, 30,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(2, 2, 1, 2, 1)),
Beet("Beet", SourceType.BeetSeeds, 6, true, 0, 100, true, 30,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 1, 2, 2)),
BokChoy("Bok Choy", SourceType.BokChoySeeds, 4, true, 0, 80, true, 25,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 1, 1, 1)),
Broccoli("Broccoli", SourceType.BroccoliSeeds, 8, false, 4, 70, true, 63,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(2, 2, 2, 2)),
Cranberries("Cranberries", SourceType.CranberrySeeds, 7, false, 5, 75, true, 38,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 2, 1, 1, 2)),
Eggplant("Eggplant", SourceType.EggplantSeeds, 5, false, 5, 60, true, 20,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 1, 1, 1, 1)),
FairyRose("Fairy Rose", SourceType.FairySeeds, 12, true, 0, 290, true, 90,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 4, 4, 3)),
Grape("Grapes", SourceType.GrapeStarter, 10, false, 3, 80, true, 38,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 1, 2, 3, 3)),
PUMPKIN("Pumpkin", SourceType.PumpkinSeeds, 13, true, 0, 320, false, 0,
    new ArrayList<>(List.of(Season.AUTUMN)), true, List.of(1, 2, 3, 4, 3)),
Yam("Yam", SourceType.YamSeeds, 10, true, 0, 160, true, 45,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(1, 3, 3, 3)),
SweetGemBerry("Sweet Gem Berry", SourceType.RareSeed, 24, true, 0, 3000, false, 0,
    new ArrayList<>(List.of(Season.AUTUMN)), false, List.of(2, 4, 6, 6, 6)),
Powdermelon("Powdermelon", SourceType.PowdermelonSeeds, 7, true, 0, 60, true, 63,
    new ArrayList<>(List.of(Season.WINTER)), true, List.of(1, 2, 1, 2, 1)),
AncientFruit("Ancient Fruit", SourceType.AncientSeeds, 28, false, 7, 550, false, 0,
    new ArrayList<>(List.of(Season.SPRING, Season.SUMMER, Season.AUTUMN)), false, List.of(2, 7, 7, 7, 5));

private final String name;
private final SourceType source;
private final int totalHarvestTime;
private final boolean oneTime;
private final int regrowthTime;
private final int baseSellPrice;
private final boolean isEdible;
private final int energy;
private final ArrayList<Season> seasons;
private final boolean canBeGiant;
private final List<Integer> stages;

    CropType(String name, SourceType source, int totalHarvestTime, boolean oneTime, int regrowthTime, int baseSellPrice,
    boolean isEdible, int energy, ArrayList<Season> seasons, boolean canBeGiant, List<Integer> stages){
        this.name = name;
        this.source = source;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.canBeGiant = canBeGiant;
        this.stages = stages;
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
