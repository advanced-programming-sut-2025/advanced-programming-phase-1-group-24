package org.example.Model.Reccepies;

import org.example.Model.Growables.CropType;
import org.example.Model.Growables.ForagingCropType;
import org.example.Model.Things.FishType;
import org.example.Model.Things.FoodType;

import java.util.Map;

public enum FoodRecipe {
    FriedEgg(Map.of("Egg", 1),50,35),
    BakedFish(Map.of(
            "Sardine", 1,
            "Salmon", 1,
            "Wheat", 1),75,100),
    Salad(Map.of("Leek",1,
            "Dandelion",1),113,110),
    Omelet(Map.of("Egg",1,
            "Cow Milk",1),100,125),
    PumpkinPie(Map.of("Pumpkin",1,
            "Wheat Flower",1,
            "Cow Milk",1,
            "Sugar",1),225,385),
    Spaghetti(Map.of("Wheat Flower",1,
            "Tomato",1),75,120),
    Pizza(Map.of("Wheat Flower",1,
            "Tomato",1,
            "Cheese",1),150,300),
    Tortilla(Map.of("Corn",1),50,50),
    MakiRoll(Map.of("Salmon",1,
            "Rice",1,
            "Fiber",1),100,220),
    TripleShotEspresso(Map.of("Coffee",3),200,450),
    Cookie(Map.of("Wheat Flower",1,
            "Egg",1,
            "Sugar",1),90,140),
    HashBrown(Map.of("Potato",1,
            "Oil",1),90,120),
    Pancakes(Map.of("Wheat Flower",1,
            "Egg",1),90,80),
    FruitSalad(Map.of("Blueberry",1,
            "Melon",1,
            "Apricot",1),263,450),
    RedPlate(Map.of("Red Cabbage",1,
            "Radish",1),240,400),
    Bread(Map.of("Wheat Flower",1),50,60),
    SalmonDinner(Map.of("Salmon",1,
            "Amaranth",1,
            "Kale",1),125,300),
    VegetableMedley(Map.of("Tomato",1,
            "Beet",1),165,120),
    FarmersLaunch(Map.of("Omelet",1,
            "Parsnip",1),200,150),
    SurvivalBurger(Map.of("Bread",1,
            "Eggplant",1,
            "Carrot",1),125,180),
    DishOtheSea(Map.of("Sardine",2,
            "Hash Brown",1),150,220),
    SeaformPudding(Map.of("Flounder",1,
            "Midnight Carp",1),175,300),
    MinersTreat(Map.of("Carrot",2,
            "Sugar",1,
            "Cow Milk",1),125,200);



    private final Map<String, Integer> recipe;
    private final  int energy;
    private final  int sellPrice;

    FoodRecipe(Map<String, Integer> recipe, int energy, int sellPrice) {
        this.recipe = recipe;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public Map<String, Integer> getRecipe() {
        return recipe;
    }
    public int getEnergy() {return energy;}
    public int getSellPrice() {return sellPrice;}

    @Override
    public String toString() {
        return name();
    }

    public static FoodRecipe fromString(String name) {
        for (FoodRecipe recipe : FoodRecipe.values()) {
            if (recipe.name().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

}