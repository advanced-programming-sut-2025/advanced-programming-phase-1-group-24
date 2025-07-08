package io.github.stardew.mini.Model.Places;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import io.github.stardew.mini.Model.Animals.Animal;
import io.github.stardew.mini.Model.Assets.GameAssetManager;
import io.github.stardew.mini.Model.Things.StorageType;

import java.util.ArrayList;

public class Habitat extends Place{
    //type of cage and barn delux and ...
    ArrayList<Animal> livingAnimals = new ArrayList<>();
    StorageType storageType;
    HabitatType habitatType;

    public Habitat(int startX, int startY, int width, int height, StorageType storageType,HabitatType habitatType) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.storageType = storageType;
        this.habitatType = habitatType;
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
        Habitat copy = new Habitat(this.x, this.y, this.width, this.height, this.storageType, this.habitatType);
        ArrayList<Animal> copiedAnimals = new ArrayList<>();
        for (Animal animal : this.livingAnimals) {
            copiedAnimals.add(animal.copy());
        }
        copy.setLivingAnimals(copiedAnimals);
        return copy;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public HabitatType getHabitatType() {
        return habitatType;
    }

    public void setHabitatType(HabitatType habitatType) {
        this.habitatType = habitatType;
    }

    public enum HabitatType {
        Barn("Barn"),
        Big_Barn("Big Barn"),
        Deluxe_Barn("Deluxe Barn"),
        CAGE("Cage"),
        Big_Cage("Big Cage"),
        Deluxe_Cage("Deluxe Cage");

        String name;
        Texture texture;

        HabitatType(String name) {
            this.name = name;
            this.texture = texture;
        }

        public Texture getTexture() {
            return texture;
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public static void initTextures() {
            Barn.setTexture(GameAssetManager.Barn);
            Big_Barn.setTexture(GameAssetManager.Big_Barn);
            Deluxe_Barn.setTexture(GameAssetManager.Deluxe_Barn);
            CAGE.setTexture(GameAssetManager.Coop);
            Big_Cage.setTexture(GameAssetManager.Big_Coop);
            Deluxe_Cage.setTexture(GameAssetManager.Deluxe_Coop);
        }
    }

}
