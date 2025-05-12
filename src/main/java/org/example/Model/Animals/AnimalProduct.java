package org.example.Model.Animals;

import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;

import java.util.Objects;

public class AnimalProduct extends Item {
    private AnimalProductType animalProductType;

    public AnimalProduct(ProductQuality productQuality, AnimalProductType animalProductType) {
        super(animalProductType.getProductName(), true, animalProductType.getPrice(), false, productQuality,true);
        this.animalProductType = animalProductType;
    }

    public AnimalProductType getAnimalProductType() {
        return animalProductType;
    }

    public void setAnimalProductType(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimalProduct that)) return false;
        if (!super.equals(o)) return false; // compares superclass fields
        return animalProductType == that.animalProductType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), animalProductType);
    }

}
