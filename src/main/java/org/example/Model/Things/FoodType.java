package org.example.Model.Things;

import org.example.Model.Reccepies.FoodRecipe;

public enum FoodType {
    FriedEgg(FoodRecipe.FriedEgg,35),
    BakedFish(FoodRecipe.BakedFish,100),
    Salad(FoodRecipe.Salad,110),
    Omelet(FoodRecipe.Omelet,125),
    PumpkinPie(FoodRecipe.PumpkinPie,385),
    Spaghetti(FoodRecipe.Spaghetti,120),
    Pizza(FoodRecipe.Pizza,300),
    Tortilla(FoodRecipe.Tortilla,50),
    MakiRoll(FoodRecipe.MakiRoll,220),
    TripleShotEspresso(FoodRecipe.TripleShotEspresso,450),
    Cookie(FoodRecipe.Cookie,140),
    HashBrown(FoodRecipe.HashBrown,120),
    Pancakes(FoodRecipe.Pancakes,80),
    FruitSalad(FoodRecipe.FruitSalad,450),
    RedPlate(FoodRecipe.RedPlate,400),
    Bread(FoodRecipe.Bread,60),
    SalmonDinner(FoodRecipe.SalmonDinner,300),
    VegetableMedley(FoodRecipe.VegetableMedley,120),
    //FarmersLaunch(FoodRecipe.FarmersLaunch,150),
   // SurvivalBurger(FoodRecipe.SurvivalBurger,180),
    //DishOtheSea(FoodRecipe.DishOtheSea,220),
    SeaformPudding(FoodRecipe.SeaformPudding,300),
    MinersTreat(FoodRecipe.MinersTreat,200);

    private final FoodRecipe recipe;
    private final int sellPrice;

    FoodType(FoodRecipe recipe, int sellPrice) {
        this.recipe = recipe;
        this.sellPrice = sellPrice;
    }

    public FoodRecipe getRecipe() { return recipe; }


}

