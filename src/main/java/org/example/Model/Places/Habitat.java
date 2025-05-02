package org.example.Model.Places;

import java.util.ArrayList;

import org.example.Model.Animals.Animal;
import org.example.Model.Things.StorageType;

public class Habitat extends Place{
    ArrayList<Animal> livingAnimals = new ArrayList<>();
    StorageType storageType;

    public Habitat(int startX, int startY, int width, int height){
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    public ArrayList<Animal> getLivingAnimals() {
        return livingAnimals;
    }
    public void setLivingAnimals(ArrayList<Animal> livingAnimals) {
        this.livingAnimals = livingAnimals;
    }
    
}
