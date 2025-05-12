package org.example.Model.Things;

import java.util.Objects;

public class Food extends Item {
    FoodType type;
    int energy;

    public Food(String name, FoodType type) {
        super(name, true, type.getSellPrice(), false, ProductQuality.Normal,true
        );
        this.type = type;
        this.energy = type.getEnergy();
    }
    @Override
    public Food copy() {
        Food newFood = new Food(name, type);
        newFood.setSellable(isSellable);
        newFood.setPlaceable(isPlaceable);
        newFood.setQuality(productQuality);
        this.setEatable(isEatable);
        return newFood;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food that)) return false;
        if (!super.equals(o)) return false;
        return energy == that.energy && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, energy);
    }


}

