package org.example.Model.Growables;

public enum FruitType {
    Apricot("Apricot", 1, 59, true, 38),
    Cherry("Cherry", 1, 80, true, 38),
    Banana("Banana", 1, 150, true, 75),
    Mango("Mango", 1, 130, true, 100),
    Orange("Orange", 1, 100, true, 38),
    Peach("Peach", 1, 140, true, 38),
    Apple("Apple", 1, 100, true, 38),
    Pomegranate("Pomegranate", 1, 140, true, 38),
    OakResin("Oak Resin", 7, 150, false, 0),
    MapleSyrup("Maple Syrup", 9, 200, false, 0),
    PineTar("Pine Tar", 5, 100, false, 0),
    Sap("Sap", 1, 2, true, -2),
    CommonMushroom("Common Mushroom", 1, 40, true, 38),
    MysticSyrup("Mystic Syrup", 7, 1000, true, 500);

    private final String name;
    private final int fullHarvestCycle;
    private final int fruitBaseSellPrice;
    private final boolean isFruitEdible;
    private final int fruitEnergy;

    FruitType(String name, int fullHarvestCycle, int fruitBaseSellPrice, boolean isFruitEdible, int fruitEnergy){
        this.name = name;
        this.fullHarvestCycle = fullHarvestCycle;
        this.fruitBaseSellPrice = fruitBaseSellPrice;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
    }

    public String getName() {
        return name;
    }
    public int getFullHarvestCycle() {
        return fullHarvestCycle;
    }
    public int getFruitBaseSellPrice() {
        return fruitBaseSellPrice;
    }
    public boolean getIsFruitEdible(){
        return isFruitEdible;
    }
    public int getFruitEnergy() {
        return fruitEnergy;
    }
}
