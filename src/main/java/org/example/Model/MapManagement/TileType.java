package org.example.Model.MapManagement;

public enum TileType {
    EMPTY("E", "\u001B[37m"), //for the start of the game
    LAKE("L", "\u001B[34m"), //farm
    GREENHOUSE("G", "\u001B[92m"), //farm
    WATERCONTAINER("W", "\u001B[36m"), //northern wall in green house
    CAGE("C", "\u001B[33m"), //farm
    BARN("B", "\u001B[31m"), //farm
    QUARRY("Q", "\u001B[35m"),//farm
    HOUSE("H", "\u001B[95m"), //farm
    GRASS("G", "\u001B[32m"), //outside farm
    FARM("F", "\u001B[93m"), //in farm but not a specific place in it
    WALL("W", "\u001B[37m"),
    NPCLAND("N", "\u001B[96m"),
    DOOR("D", "\u001B[37m"),
    SHOP("S", "\u001B[32m");
    //Letter for printing the map

    private final String letterToPrint;
    private final String color;

    TileType(String letterToPrint, String color){
        this.letterToPrint = letterToPrint;
        this.color = color;
    }

    public String getLetterToPrint() {
        return letterToPrint;
    }
    public String coloredSymbol() {
        return color + letterToPrint + "\u001B[0m";
    }
}
