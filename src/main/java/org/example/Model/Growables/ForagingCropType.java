package org.example.Model.Growables;

import java.util.*;

import org.example.Model.TimeManagement.Season;


public enum ForagingCropType {
    CommonMushroom("Common Mushroom", new ArrayList<>(Arrays.asList(
        Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)), 40, 38),
    Daffodil("Daffodil", new ArrayList<>(Arrays.asList(Season.SPRING)), 30, 0),
    Dandelion("Dandelion", new ArrayList<>(Arrays.asList(Season.SPRING)), 40, 25),
    Leek("Leek", new ArrayList<>(Arrays.asList(Season.SPRING)), 60, 40),
    Morel("Morel", new ArrayList<>(Arrays.asList(Season.SPRING)), 150, 20),
    Salmonberry("Salmonberry", new ArrayList<>(Arrays.asList(Season.SPRING)), 5, 25),
    SpringOnion("Spring Onion", new ArrayList<>(Arrays.asList(Season.SPRING)), 8, 13),
    WildHorseradish("Wild Horseradish", new ArrayList<>(Arrays.asList(Season.SPRING)), 50, 13),
    FiddleheadFern("Fiddlehead Fern", new ArrayList<>(Arrays.asList(Season.SUMMER)), 90, 25),
    Grape("Grape", new ArrayList<>(Arrays.asList(Season.SUMMER)), 80, 35),
    RedMushroom("Red Mushroom", new ArrayList<>(Arrays.asList(Season.SUMMER)), 75, -50),
    SpiceBerry("Spice Berry", new ArrayList<>(Arrays.asList(Season.SUMMER)), 80, 25),
    SweetPea("Sweet Pea", new ArrayList<>(Arrays.asList(Season.SUMMER)), 50, 0),
    Blackberry("Blackberry", new ArrayList<>(Arrays.asList(Season.AUTUMN)), 20, 25),
    Chanterelle("Chanterelle", new ArrayList<>(Arrays.asList(Season.AUTUMN)), 160, 50),
    Hazelnut("Hazelnut", new ArrayList<>(Arrays.asList(Season.AUTUMN)), 90, 30),
    PurpleMushroom("Purple Mushroom", new ArrayList<>(Arrays.asList(Season.AUTUMN)), 250, 125),
    WildPlum("Wild Plum", new ArrayList<>(Arrays.asList(Season.AUTUMN)), 80, 25),
    Crocus("Crocus", new ArrayList<>(Arrays.asList(Season.WINTER)), 60, 0),
    CrystalFruit("Crystal Fruit", new ArrayList<>(Arrays.asList(Season.WINTER)), 150, 63),
    Holly("Holly", new ArrayList<>(Arrays.asList(Season.WINTER)), 80, -37),
    SnowYam("Snow Yam", new ArrayList<>(Arrays.asList(Season.WINTER)), 100, 30),
    WinterRoot("Winter Root", new ArrayList<>(Arrays.asList(Season.WINTER)), 70, 25);
    

    private final String name;
    private final ArrayList<Season> season;
    private final int baseSellPrice;
    private final int energy;

    ForagingCropType(String name, ArrayList<Season> season, int baseSellPrice, int energy){
        this.name = name;
        this.season = season;
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }
    public ArrayList<Season> getSeason() {
        return season;
    }
    public int getEnergy() {
        return energy;
    }
    public int getBaseSellPrice() {
        return baseSellPrice;
    }
}
