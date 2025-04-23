package org.example.Model.Growables;

import org.example.Model.TimeManagement.Season;
import java.util.*;

public enum CropType {
    //boolean isforaging crop
BlueJazz,
Carrot,
Cauliflower,
CoffeeBean,
Garlic,
GreenBean,
Kale,
Parsnip,
Potato,
Rhubarb,
Strawberry,
Tulip,
UnmilledRice,
Blueberry,
Corn,
Hops,
HotPepper,
Melon,
Poppy,
Radish,
RedCabbage,
Starfruit,
SummerSpangle,
SummerSquash,
Sunflower,
Tomato,
Wheat,
Amaranth,
Artichoke,
Beet,
BokChoy,
Broccoli,
Cranberries,
Eggplant,
FairyRose,
Grape,
Pumpkin,
Yam,
SweetGemBerry,
Powdermelon,
AncientFruit;

    CropType(Source source, int totalHarvestTime, boolean oneTime, int regrowthTime,
                 int baseSellPrice, boolean isEdible, int energy, Season season,
                 boolean canBeGiant, List<Integer> stages, boolean isForagingCrop) {
        this.source = source;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.season = season;
        this.canBeGiant = canBeGiant;
        this.stages = stages;
    }
    public final Source source;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final Season season;
    public final boolean canBeGiant;
    public final List<Integer> stages;
}
