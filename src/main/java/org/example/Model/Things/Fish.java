package org.example.Model.Things;

public class Fish extends Item {
    private FishType type;

    public Fish(ProductQuality productQuality, FishType type) {
        super(type.name(), true, type.getSellPrice(), false, productQuality);
        this.type = type;
    }


    public FishType getType() {
        return type;
    }
}

