package org.example.Model.Animals;

import org.example.Model.Things.StorageType;

import java.util.List;

//Map of cage animal and barn animals;

public enum AnimalType {
    // Cage Animals
    CHICKEN(800, "Cage", List.of(StorageType.INITIAL, StorageType.BIG, StorageType.DELUX), 1,
            AnimalProductType.EGG, AnimalProductType.LARGE_EGG),
    DUCK(1200, "Cage", List.of(StorageType.BIG, StorageType.DELUX), 2,
            AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHERS),
    BUNNY(8000, "Cage", List.of(StorageType.DELUX), 4,
            AnimalProductType.BUNNY_WOOL, AnimalProductType.BUNNY_FEET),
    DINOSAUR(14000, "Cage", List.of(StorageType.BIG), 7,
            AnimalProductType.DINOSAUR_EGG, null),

    // Barn Animals
    COW(1500, "Barn", List.of(StorageType.INITIAL, StorageType.BIG, StorageType.DELUX), 1,
            AnimalProductType.COW_MILK, AnimalProductType.LARGE_COW_MILK),
    GOAT(4000, "Barn", List.of(StorageType.BIG, StorageType.DELUX), 2,
            AnimalProductType.GOAT_MILK, AnimalProductType.LARGE_GOAT_MILK),
    SHEEP(8000, "Barn", List.of(StorageType.DELUX), 3,
            AnimalProductType.SHEEP_WOOL, null),
    PIG(16000, "Barn", List.of(StorageType.DELUX), 0,
            AnimalProductType.TRUFFLE, null);

    private final int basePrice;
    private final String habitat;
    private final List<StorageType> storageTypes;
    private final int daysToProduce;
    private final AnimalProductType primaryProduct;
    private final AnimalProductType secondaryProduct;

    AnimalType(int basePrice, String habitat, List<StorageType> storageTypes, int daysToProduce,
               AnimalProductType primaryProduct, AnimalProductType secondaryProduct) {
        this.basePrice = basePrice;
        this.habitat = habitat;
        this.storageTypes = storageTypes;
        this.daysToProduce = daysToProduce;
        this.primaryProduct = primaryProduct;
        this.secondaryProduct = secondaryProduct;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public String getHabitat() {
        return habitat;
    }

    public List<StorageType> getStorageTypes() {
        return storageTypes;
    }

    public int getDaysToProduce() {
        return daysToProduce;
    }

    public AnimalProductType getPrimaryProduct() {
        return primaryProduct;
    }

    public AnimalProductType getSecondaryProduct() {
        return secondaryProduct;
    }
    public boolean hasSecondaryProduct(){
        return secondaryProduct != null;
    }
}




