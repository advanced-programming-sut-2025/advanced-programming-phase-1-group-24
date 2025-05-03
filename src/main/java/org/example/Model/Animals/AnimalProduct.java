package org.example.Model.Animals;

import org.example.Model.Things.Item;
import org.example.Model.Things.ProductQuality;

public class AnimalProduct extends Item {
    private AnimalProductType animalProductType;

    public AnimalProduct(ProductQuality productQuality, AnimalProductType animalProductType) {
        super(animalProductType.getProductName(), true, animalProductType.getPrice(), false, productQuality);
        this.animalProductType = animalProductType;
    }

    public AnimalProductType getAnimalProductType() {
        return animalProductType;
    }

    public void setAnimalProductType(AnimalProductType animalProductType) {
        this.animalProductType = animalProductType;
    }
}
