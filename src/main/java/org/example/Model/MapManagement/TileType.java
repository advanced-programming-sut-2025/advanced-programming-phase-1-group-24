package org.example.Model.MapManagement;

public enum TileType {
    EMPTY("E"), //for the start of the game 
    LAKE("L"), //farm
    ROCK("R"),
    GREENHOUSE("G"), //farm
    WATERCONTAINER("W"), //northern wall in green house
    CAGE("C"), //farm
    BARN("B"), //farm
    QUARRY("Q"),//farm
    FORAGINGMINERAL("F"), //Quarry
    HOUSE("H"), //farm
    GRASS("G"), //outside farm
    FARM("F"), //in farm but not a specific place in it
    WALL("W"),
    NPCLAND("N"),
    PRODUCTOGROWABLE("P"),
    GROWABLE("G");
    //Letter for printing the map

    private final String letterToPrint;

    TileType(String letterToPrint){
        this.letterToPrint = letterToPrint;
    }

    public String getLetterToPrint() {
        return letterToPrint;
    }
}
