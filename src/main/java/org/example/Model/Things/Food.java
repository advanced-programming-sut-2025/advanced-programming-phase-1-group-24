package org.example.Model.Things;

public class Food extends Item{
    FoodType type;
    int energy;

    public Food(String name, boolean isSellable, int price, boolean isPlaceable, ProductQuality productQuality, FoodType type, int energy) {
        super(name, isSellable, price, isPlaceable, productQuality);
        this.type = type;
        this.energy = energy;
    }
//    public Food(FoodType type) {
//        this.type = type;
//        this.energy = type.getRecipe().getEnergy();
//    }
}

