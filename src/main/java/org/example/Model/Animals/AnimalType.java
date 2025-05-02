package org.example.Model.Animals;

import org.example.Model.Things.StorageType;

import java.util.List;

//Map of cage animal and barn animals
public enum AnimalType {
    //Cage
    CHICKEN(800, "Cage", List.of(StorageType.INITIAL,StorageType.BIG,StorageType.DELUX), 1, new AnimalProductType[]{AnimalProductType.EGG, AnimalProductType.LARGE_EGG}),
    DUCK(1200, "Cage", List.of(StorageType.BIG,StorageType.DELUX), 2, new AnimalProductType[]{AnimalProductType.DUCK_EGG, AnimalProductType.DUCK_FEATHERS}),
    BUNNY(8000, "Cage", List.of(StorageType.DELUX), 4, new AnimalProductType[]{AnimalProductType.BUNNY_WOOL,AnimalProductType.BUNNY_FEET}),
    DINOSAUR(14000, "Cage", List.of(StorageType.BIG), 7, new AnimalProductType[]{AnimalProductType.DINOSAUR_EGG}),
    //Barn
    COW(1500, "Barn", List.of(StorageType.INITIAL,StorageType.BIG,StorageType.DELUX), 1, new AnimalProductType[]{AnimalProductType.COW_MILK, AnimalProductType.LARGE_COW_MILK}),
    GOAT(4000, "Barn", List.of(StorageType.BIG,StorageType.DELUX), 2, new AnimalProductType[]{AnimalProductType.GOAT_MILK, AnimalProductType.LARGE_GOAT_MILK}),
    SHEEP(8000, "Barn", List.of(StorageType.DELUX), 3, new AnimalProductType[]{AnimalProductType.SHEEP_WOOL}),
    PIG(16000, "Barn", List.of(StorageType.DELUX), 0, new AnimalProductType[]{AnimalProductType.TRUFFLE});

    private final int basePrice;
    private final String habitat;
    private final List<StorageType> storageTypes; // List to support multiple storage types
    private final int daysToProduce; // Number of days to produce the product
    private final AnimalProductType[] products;

    AnimalType(int basePrice, String habitat, List<StorageType> storageTypes, int daysToProduce, AnimalProductType[] products) {
        this.basePrice = basePrice;
        this.habitat = habitat;
        this.storageTypes = storageTypes;
        this.daysToProduce = daysToProduce;
        this.products = products;
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

    public AnimalProductType[] getProducts() {
        return products;
    }
}



