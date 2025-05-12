package org.example.Model.Things;

import org.example.Model.Reccepies.FoodRecipe;

public enum FoodType {
    FriedEgg("Fried Egg", FoodRecipe.FriedEgg, 35),
    BakedFish("Baked Fish", FoodRecipe.BakedFish, 100),
    Salad("Salad", FoodRecipe.Salad, 110),
    Omelet("Omelet", FoodRecipe.Omelet, 125),
    PumpkinPie("Pumpkin Pie", FoodRecipe.PumpkinPie, 385),
    Spaghetti("Spaghetti", FoodRecipe.Spaghetti, 120),
    Pizza("Pizza", FoodRecipe.Pizza, 300),
    Tortilla("Tortilla", FoodRecipe.Tortilla, 50),
    MakiRoll("Maki Roll", FoodRecipe.MakiRoll, 220),
    TripleShotEspresso("Triple Shot Espresso", FoodRecipe.TripleShotEspresso, 450),
    Cookie("Cookie", FoodRecipe.Cookie, 140),
    HashBrown("Hash Brown", FoodRecipe.HashBrown, 120),
    Pancakes("Pancakes", FoodRecipe.Pancakes, 80),
    FruitSalad("FruitSalad", FoodRecipe.FruitSalad, 450),
    RedPlate("Red Plate", FoodRecipe.RedPlate, 400),
    Bread("Bread", FoodRecipe.Bread, 60),
    SalmonDinner("Salmon Dinner", FoodRecipe.SalmonDinner, 300),
    VegetableMedley("Vegetable Medley", FoodRecipe.VegetableMedley, 120),
   // FarmersLaunch("Farmers Launch", FoodRecipe.FarmersLaunch, 150),
  //    SurvivalBurger(FoodRecipe.SurvivalBurger,180),
//    DishOtheSea(FoodRecipe.DishOtheSea,220),
    SeaformPudding("Sea form Pudding",FoodRecipe.SeaformPudding,300),
    MinersTreat("Miners Treat", FoodRecipe.MinersTreat, 200);

    private final FoodRecipe recipe;
    private final int sellPrice;
    private final int energy;

    FoodType(String name, FoodRecipe recipe, int sellPrice) {
        this.recipe = recipe;
        this.sellPrice = sellPrice;
        this.energy = recipe.getEnergy();
    }

    public String getName() {
        return this.name();
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getEnergy() {
        return energy;
    }
}

