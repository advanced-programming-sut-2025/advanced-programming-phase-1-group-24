package org.example.Model.Things;

import java.util.Objects;

public class Fish extends Item {
    private FishType type;

    public Fish(ProductQuality productQuality, FishType type) {
        super(type.name(), true, type.getSellPrice(), false, productQuality,true);
        this.type = type;
    }


    public FishType getType() {
        return type;
    }
    @Override
    public Fish copy() {
        return new Fish(productQuality, type);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fish that)) return false;
        if (!super.equals(o)) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }

}

