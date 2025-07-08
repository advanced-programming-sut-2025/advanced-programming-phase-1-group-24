package io.github.stardew.mini.Model.Things;

import com.badlogic.gdx.graphics.Texture;
import io.github.stardew.mini.Model.Reccepies.FoodRecipe;

public enum FoodType {
    Fried_Egg("Fried Egg", FoodRecipe.FriedEgg, 35),
    Baked_Fish("Baked Fish", FoodRecipe.BakedFish, 100),
    Salad("Salad", FoodRecipe.Salad, 110),
    Omelet("Omelet", FoodRecipe.Omelet, 125),
    Pumpkin_Pie("Pumpkin Pie", FoodRecipe.PumpkinPie, 385),
    Spaghetti("Spaghetti", FoodRecipe.Spaghetti, 120),
    Pizza("Pizza", FoodRecipe.Pizza, 300),
    Tortilla("Tortilla", FoodRecipe.Tortilla, 50),
    Maki_Roll("Maki Roll", FoodRecipe.MakiRoll, 220),
    Triple_Shot_Espresso("Triple Shot Espresso", FoodRecipe.TripleShotEspresso, 450),
    Cookie("Cookie", FoodRecipe.Cookie, 140),
    Hashbrowns("Hash Brown", FoodRecipe.HashBrown, 120),
    Pancakes("Pancakes", FoodRecipe.Pancakes, 80),
    Fruit_Salad("FruitSalad", FoodRecipe.FruitSalad, 450),
    Red_Plate("Red Plate", FoodRecipe.RedPlate, 400),
    Bread("Bread", FoodRecipe.Bread, 60),
    Salmon_Dinner("Salmon Dinner", FoodRecipe.SalmonDinner, 300),
    Vegetable_Medley("Vegetable Medley", FoodRecipe.VegetableMedley, 120),
    Farmers_Lunch("Farmers Lunch",FoodRecipe.FarmersLaunch,150),
    Survival_Burger("Survival Burger",FoodRecipe.SurvivalBurger,180),
    Dish_O_The_Sea("Dish O the Sea",FoodRecipe.DishOtheSea,220),
    Seafoam_Pudding("Sea form Pudding",FoodRecipe.SeaformPudding,300),
    Miners_Treat("Miners Treat", FoodRecipe.MinersTreat, 200);

    private final FoodRecipe recipe;
    private final int sellPrice;
    private final int energy;
    private final Texture texture;

    FoodType(String name, FoodRecipe recipe, int sellPrice) {
        this.recipe = recipe;
        this.sellPrice = sellPrice;
        this.energy = recipe.getEnergy();
        this.texture = new Texture("Recipe/" + this.name() + ".png");
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

    public Texture getTexture() { return texture; }
}

