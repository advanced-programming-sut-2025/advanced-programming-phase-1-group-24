package org.example.Model.Places;

import java.util.ArrayList;

import org.example.Model.Animals.Animal;

public class Habitat extends Place{
    //type of cage and barn delux and ...
    ArrayList<Animal> livingAnimals = new ArrayList<>();

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
