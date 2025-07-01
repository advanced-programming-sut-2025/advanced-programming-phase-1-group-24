package io.github.stardew.mini.Model.Places;

import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Things.StorageType;

import java.util.ArrayList;

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


    public Habitat copy() {
        Habitat copy = new Habitat(this.x, this.y, this.width, this.height, this.storageType);
        ArrayList<Animal> copiedAnimals = new ArrayList<>();
        for (Animal animal : this.livingAnimals) {
            copiedAnimals.add(animal.copy());
        }
        copy.setLivingAnimals(copiedAnimals);
        return copy;
    }

}
