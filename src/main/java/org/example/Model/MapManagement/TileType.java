package org.example.Model.MapManagement;

public enum TileType {
    EMPTY, //for the start of the game 
    LAKE, //farm
    ROCK,
    GREENHOUSE, //farm
    WATERCONTAINER, //northern wall in green house
    CAGE, //farm
    BARN, //farm
    QUARRY,//farm
    HOUSE, //farm
    GRASS, //outside farm
    FARM, //in farm but not a specific place in it
    FORAGING,
    WALL,
    NPCLAND,
    TREE;
    //Letter for printing the map
}
