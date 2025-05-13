package org.example.Model.Things;

import org.example.Model.TimeManagement.Season;
public enum FishType {
    Salmon("Salmon",Season.AUTUMN,75, RarenessType.COMMON),
    Sardine("Sardine",Season.AUTUMN,40, RarenessType.COMMON),
    Shad("Shad",Season.AUTUMN,60, RarenessType.COMMON),
    Blue_Discus("Blue Discus",Season.AUTUMN,120, RarenessType.COMMON),
    Midnight_Carp("Midnight Carp",Season.WINTER,150, RarenessType.COMMON),
    Squid("Squid",Season.WINTER,80, RarenessType.COMMON),
    Tuna("Tuna",Season.WINTER,100, RarenessType.COMMON),
    Perch("Perch",Season.WINTER,55, RarenessType.COMMON),
    Flounder("Flounder",Season.SPRING,100, RarenessType.COMMON),
    Lionfish("Lion Fish",Season.SPRING,100, RarenessType.COMMON),
    Herring("Herring",Season.SPRING,30, RarenessType.COMMON),
    Ghostfish("Ghost Fish",Season.SPRING,45, RarenessType.COMMON),
    Tilapia("Tilapia",Season.SUMMER,75, RarenessType.COMMON),
    Dorado("Dorado",Season.SUMMER,100, RarenessType.COMMON),
    Sunfish("Sun Fish",Season.SUMMER,30, RarenessType.COMMON),
    Rainbow_Trout("Rainbow Trout",Season.SUMMER,65, RarenessType.COMMON),
    Legend("Legend",Season.SPRING,5000, RarenessType.LEGENDARY),
    Glacierfish("Glacier Fish",Season.WINTER, 1000, RarenessType.LEGENDARY),
    Angler("Angler",Season.AUTUMN, 900, RarenessType.LEGENDARY),
    Crimsonfish("Crimson Fish",Season.SUMMER, 1500, RarenessType.LEGENDARY);

    private final String name;
    private final Season season;
    private final int sellPrice;
    private final RarenessType rareness;

    FishType(String name, Season season, int sellPrice, RarenessType rareness) {
        this.name = name;
        this.season = season;
        this.sellPrice = sellPrice;
        this.rareness = rareness;
    }

    public String getName() { return name; }

    public Season getSeason() {
        return season;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public RarenessType getRareness() {
        return rareness;
    }

    public enum RarenessType {
        COMMON,
        LEGENDARY;
    }
}