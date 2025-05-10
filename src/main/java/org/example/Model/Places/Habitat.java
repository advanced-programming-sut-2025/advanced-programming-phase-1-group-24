package org.example.Model.Places;

import java.util.ArrayList;

import org.example.Model.Animals.Animal;
import org.example.Model.Things.StorageType;

public class Habitat extends Place{
    //type of cage and barn delux and ...
    ArrayList<Animal> livingAnimals = new ArrayList<>();
    StorageType storageType;

    public Habitat(int startX, int startY, int width, int height, StorageType storageType) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.storageType = storageType;
    }

    public ArrayList<Animal> getLivingAnimals() {
        return livingAnimals;
    }
    public void setLivingAnimals(ArrayList<Animal> livingAnimals) {
        this.livingAnimals = livingAnimals;
    }
    public StorageType getStorageType() {
        return storageType;
    }
    
}
