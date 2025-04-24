package org.example.Model.Growables;

import java.util.*;

import org.example.Model.TimeManagement.Season;

public enum TreeType {
    ApricotTree("Apricot Tree", SourceType.ApricotSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Apricot, false, new ArrayList<>(Arrays.asList(Season.SPRING)), null),
    CherryTree("Cherry Tree", SourceType.CherrySapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Cherry, false, new ArrayList<>(Arrays.asList(Season.SPRING)), null),
    BananaTree("Banana Tree", SourceType.BananaSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Banana, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    MangoTree("Mango Tree", SourceType.MangoSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Mango, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    OrangeTree("Orange Tree", SourceType.OrangeSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Orange, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    PeachTree("Peach Tree", SourceType.PeachSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Peach, false, new ArrayList<>(Arrays.asList(Season.SUMMER)), null),
    AppleTree("Apple Tree", SourceType.AppleSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Apple, false, new ArrayList<>(Arrays.asList(Season.AUTUMN)), null),
    PomegranateTree("Pomegranate Tree", SourceType.PomegranateSapling, Arrays.asList(7, 7, 7, 7), 28, FruitType.Pomegranate, false, new ArrayList<>(Arrays.asList(Season.AUTUMN)), null),
    OakTree("Oak Tree", SourceType.Acorns, Arrays.asList(7, 7, 7, 7), 28, FruitType.OakResin, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MapleTree("Maple Tree", SourceType.MapleSeeds, Arrays.asList(7, 7, 7, 7), 28, FruitType.MapleSyrup, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    PineTree("Pine Tree", SourceType.PineCones, Arrays.asList(7, 7, 7, 7), 28, FruitType.PineTar, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MahoganyTree("Mahogany Tree", SourceType.MahoganySeeds, Arrays.asList(7, 7, 7, 7), 28, FruitType.Sap, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MushroomTree("Mushroom Tree", SourceType.MushroomTreeSeeds, Arrays.asList(7, 7, 7, 7), 28, FruitType.CommonMushroom, true, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER))),
    MysticTree("Mystic Tree", SourceType.MysticTreeSeeds, Arrays.asList(7, 7, 7, 7), 28, FruitType.MysticSyrup, false, new ArrayList<>(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), null);


    
    private final String name;
    private final SourceType source;
    private final List<Integer> satges;
    private final int totalHarvestTime;
    private final FruitType fruitType;
    private final boolean isForagingTree;
    private final ArrayList<Season> foragingSeasons;
    private final ArrayList<Season> normalSeasons;

    TreeType(String name, SourceType source, List<Integer> stages, int totalHarvestTime,
             FruitType fruitType, boolean isForagingTree, ArrayList<Season> normalSeasons, ArrayList<Season> foragingSeasons){
        this.name = name;
        this.source = source;
        this.satges = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.fruitType = fruitType;
        this.isForagingTree = isForagingTree;
        this.normalSeasons = normalSeasons;
        this.foragingSeasons = foragingSeasons;

    }

    public String getName() {
        return name;
    }
    public SourceType getSource() {
        return source;
    }
    public List<Integer> getSatges() {
        return satges;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public FruitType getFruitType() {
        return fruitType;
    }
    public boolean getIsForagingTree() {
        return isForagingTree;
    }
    public ArrayList<Season> getForagingSeasons() {
        return foragingSeasons;
    }
    public ArrayList<Season> getNormalSeasons() {
        return normalSeasons;
    }
}
