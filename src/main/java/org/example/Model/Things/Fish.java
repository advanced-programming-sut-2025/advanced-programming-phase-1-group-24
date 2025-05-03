package org.example.Model.Things;

public class Fish extends Item {
    private FishType type;

    public Fish(FishType type, ProductQuality quality) {
        this.type = type;
        this.quality = quality;
    }

    public FishType getType() {
        return type;
    }
}
