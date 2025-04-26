package org.example.Model.Animals;


import org.example.Model.MapManagement.Tile;

public class Animal {
    private AnimalType animalType;
    private Tile currentTile;
    private Habitat livingPlace;

    public AnimalType getAnimalType() {
        return animalType;
    }
}
