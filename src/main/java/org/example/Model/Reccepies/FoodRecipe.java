package org.example.Model.Reccepies;

import org.example.Model.Animals.AnimalProductType;
import org.example.Model.Growables.CropType;
import org.example.Model.Growables.ForagingCropType;
import org.example.Model.Growables.FruitType;
import org.example.Model.Things.FishType;
import org.example.Model.Things.FoodType;

import java.util.Map;

public enum FoodRecipe {
    FriedEgg(Map.of(AnimalProductType.EGG, 1),50,35),
    BakedFish(Map.of(
            FishType.Sardine, 1,
            FishType.Salmon, 1,
            CropType.Wheat, 1),75,100),
    Salad(Map.of(ForagingCropType.Leek,1,
                ForagingCropType.Dandelion,1),113,110),
    Omelet(Map.of(AnimalProductType.EGG,1,
            AnimalProductType.COW_MILK,1),100,125),
    PumpkinPie(Map.of(CropType.PUMPKIN,1,
            randomStuffType.WheatFlower,1,
            AnimalProductType.COW_MILK,1,
            randomStuffType.Sugar,1),225,385),
    Spaghetti(Map.of(randomStuffType.WheatFlower,1,
            CropType.Tomato,1),75,120),
    Pizza(Map.of(randomStuffType.WheatFlower,1,
            CropType.Tomato,1,
            randomStuffType.Cheese,1),150,300),
    Tortilla(Map.of(CropType.Corn,1),50,50),
    MakiRoll(Map.of(SpecialRecipeItem.AnyFish,1,
            randomStuffType.Rice,1,
            randomStuffType.Fiber,1),100,220),
    TripleShotEspresso(Map.of(randomStuffType.Coffee,3),200,450),
    Cookie(Map.of(randomStuffType.WheatFlower,1,
            AnimalProductType.EGG,1,
            randomStuffType.Sugar,1),90,140),
    HashBrown(Map.of(CropType.Potato,1,
            randomStuffType.Oil,1),90,120),
    Pancakes(Map.of(randomStuffType.WheatFlower,1,
            AnimalProductType.EGG,1),90,80),
    FruitSalad(Map.of(CropType.Blueberry,1,
            CropType.Melon,1,
            FruitType.Apricot,1),263,450),
    RedPlate(Map.of(CropType.RedCabbage,1,
            CropType.Radish,1),240,400),
    Bread(Map.of(randomStuffType.WheatFlower,1),50,60),
    SalmonDinner(Map.of(FishType.Salmon,1,
            CropType.Amaranth,1,
            CropType.Kale,1),125,300),
    VegetableMedley(Map.of(CropType.Tomato,1,
            CropType.Beet,1),165,120),
//    FarmersLaunch(Map.of(FoodType.Omelet,1,
//            CropType.Parsnip,1),200,150),
//    SurvivalBurger(Map.of(FoodType.Bread,1,
//            CropType.Eggplant,1,
//            CropType.Carrot,1),125,180),
    //DishOtheSea(Map.of(FishType.Sardine,2,
          //  FoodType.HashBrown,1),150,220),
    SeaformPudding(Map.of(FishType.Flounder,1,
            FishType.Midnight_Carp,1),175,300),
    MinersTreat(Map.of(CropType.Carrot,2,
            randomStuffType.Sugar,1,
            AnimalProductType.COW_MILK,1),125,200);



    private final Map<Enum<?>, Integer> recipe;
    private final  int energy;
    private final  int sellPrice;

    FoodRecipe(Map<Enum<?>, Integer> recipe, int energy, int sellPrice) {
        this.recipe = recipe;
        this.energy = energy;
        this.sellPrice = sellPrice;
    }

    public Map<Enum<?>, Integer> getRecipe() {
        return recipe;
    }
    public int getEnergy() {return energy;}
    public int getSellPrice() {return sellPrice;}
    public static FoodRecipe fromString(String name) {
        for (FoodRecipe recipe : FoodRecipe.values()) {
            if (recipe.name().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

}