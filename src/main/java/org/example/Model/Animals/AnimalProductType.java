package org.example.Model.Animals;

public enum AnimalProductType {
    EGG("Egg", 50),
    LARGE_EGG("Large Egg", 95),
    DUCK_EGG("Duck Egg", 95),
    DUCK_FEATHERS("Duck Feathers", 250),
    BUNNY_WOOL("Bunny Wool", 340),
    BUNNY_FEET("Bunny Feet", 565),
    DINOSAUR_EGG("Dinosaur Egg", 350),
    COW_MILK("Cow Milk", 125),
    LARGE_COW_MILK("Large Cow Milk", 190),
    GOAT_MILK("Goat Milk", 225),
    LARGE_GOAT_MILK("Large Goat Milk", 345),
    SHEEP_WOOL("Sheep Wool", 340),
    TRUFFLE("Truffle", 625);

    private final String productName;
    private final int price;

    AnimalProductType(String productName, int price) {
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }
}
