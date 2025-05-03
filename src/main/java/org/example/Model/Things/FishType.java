package org.example.Model.Things;

import org.example.Model.TimeManagement.Season;

public enum FishType {
    Salmon(Season.AUTUMN, 75, RarenessType.COMMON),
    Sardine(Season.AUTUMN, 40, RarenessType.COMMON),
    Shad(Season.AUTUMN, 60, RarenessType.COMMON),
    Blue_Discus(Season.AUTUMN, 120, RarenessType.COMMON),
    Midnight_Carp(Season.WINTER, 150, RarenessType.COMMON),
    Squid(Season.WINTER, 80, RarenessType.COMMON),
    Tuna(Season.WINTER, 100, RarenessType.COMMON),
    Perch(Season.WINTER, 55, RarenessType.COMMON),
    Flounder(Season.SPRING, 100, RarenessType.COMMON),
    Lionfish(Season.SPRING, 100, RarenessType.COMMON),
    Herring(Season.SPRING, 30, RarenessType.COMMON),
    Ghostfish(Season.SPRING, 45, RarenessType.COMMON),
    Tilapia(Season.SUMMER, 75, RarenessType.COMMON),
    Dorado(Season.SUMMER, 100, RarenessType.COMMON),
    Sunfish(Season.SUMMER, 30, RarenessType.COMMON),
    Rainbow_Trout(Season.SUMMER, 65, RarenessType.COMMON),
    Legend(Season.SPRING, 5000, RarenessType.LEGENDARY),
    Glacierfish(Season.WINTER, 1000, RarenessType.LEGENDARY),
    Angler(Season.AUTUMN, 900, RarenessType.LEGENDARY),
    Crimsonfish(Season.SUMMER, 1500, RarenessType.LEGENDARY);

    private final Season season;
    private final int sellPrice;
    private final RarenessType rareness;

    FishType(Season season, int sellPrice, RarenessType rareness) {
        this.season = season;
        this.sellPrice = sellPrice;
        this.rareness = rareness;
    }

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
