package org.example.Model.Reccepies;

import org.example.Model.Growables.CropType;
import org.example.Model.Growables.SourceType;
import org.example.Model.Skill;
import org.example.Model.Things.ForagingMineralType;
import org.example.Model.Things.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum MachineType {
    CHERRY_BOMB("Cherry Bomb", Map.of(
            ForagingMineralType.Copper.getName(), 4,
            ForagingMineralType.Coal.getName(), 1
    ), Map.of(Skill.MINING, 1), 50,null),

    BOMB("Bomb", Map.of(
            ForagingMineralType.Iron.getName(), 4,
            ForagingMineralType.Coal.getName(), 1
    ), Map.of(Skill.MINING, 2), 50,null),

    MEGA_BOMB("Mega Bomb", Map.of(
            ForagingMineralType.Gold.getName(), 4,
            ForagingMineralType.Coal.getName(), 1
    ), Map.of(Skill.MINING, 3), 50,null),

    SPRINKLER("Sprinkler", Map.of(
            randomStuffType.Copper_Bar.getName(), 1,
            randomStuffType.Iron_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 1), 0,null),

    QUALITY_SPRINKLER("Quality Sprinkler", Map.of(
            randomStuffType.Iron_Bar.getName(), 1,
            randomStuffType.Gold_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 2), 0,null),

    IRIDIUM_SPRINKLER("Iridium Sprinkler", Map.of(
            randomStuffType.Gold_Bar.getName(), 1,
            randomStuffType.Iridium_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 3), 0,null),

    FURNACE("Furnace", Map.of(
            ForagingMineralType.Copper.getName(), 20,
            randomStuffType.Stone.getName(), 25
    ), Map.of(), 0,
            Arrays.asList(randomStuffType.Iron_Bar,
                    randomStuffType.Copper_Bar,randomStuffType.Iridium_Bar,randomStuffType.Gold_Bar)),

    SCARECROW("Scarecrow", Map.of(
            randomStuffType.Wood.getName(), 50,
            ForagingMineralType.Coal.getName(), 1,
            randomStuffType.Fiber.getName(), 20
    ), Map.of(), 0,null),

    DELUXE_SCARECROW("Deluxe Scarecrow", Map.of(
            randomStuffType.Wood.getName(), 50,
            ForagingMineralType.Coal.getName(), 1,
            randomStuffType.Fiber.getName(), 20,
            ForagingMineralType.Iriduim.getName(), 1
    ), Map.of(Skill.FARMING, 2), 0,null),

    BEE_HOUSE("Bee House", Map.of(
            randomStuffType.Wood.getName(), 40,
            ForagingMineralType.Coal.getName(), 8,
            randomStuffType.Iron_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 1), 0,
            Arrays.asList(randomStuffType.Honey)),

    CHEESE_PRESS("Cheese Press", Map.of(
            randomStuffType.Wood.getName(), 45,
            randomStuffType.Stone.getName(), 45,
            randomStuffType.Copper_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 2), 0,
            Arrays.asList(randomStuffType.Cheese,randomStuffType.Goat_Cheese)),

    KEG("Keg", Map.of(
            randomStuffType.Wood.getName(), 30,
            randomStuffType.Copper_Bar.getName(), 1,
            randomStuffType.Iron_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 2), 0,
            Arrays.asList(randomStuffType.Coffee,randomStuffType.Beer,
                    randomStuffType.Vinegar,randomStuffType.Juice,
                    randomStuffType.Pale_Ale,randomStuffType.Wine,
                    randomStuffType.Mead)),

    LOOM("Loom", Map.of(
            randomStuffType.Wood.getName(), 60,
            randomStuffType.Fiber.getName(), 30
    ), Map.of(Skill.FARMING, 3), 0,
            Arrays.asList(randomStuffType.Cloth)),

    MAYONNAISE_MACHINE("Mayonnaise Machine", Map.of(
            randomStuffType.Wood.getName(), 15,
            randomStuffType.Stone.getName(), 15,
            randomStuffType.Copper_Bar.getName(), 1
    ), Map.of(), 0,
            Arrays.asList(randomStuffType.Mayonnaise,randomStuffType.Dinosaur_Mayonnaise,
                    randomStuffType.Duck_Mayonnaise)),

    OIL_MAKER("Oil Maker", Map.of(
            randomStuffType.Wood.getName(), 100,
            randomStuffType.Gold_Bar.getName(), 1,
            randomStuffType.Iron_Bar.getName(), 1
    ), Map.of(Skill.FARMING, 3), 0,
            Arrays.asList(randomStuffType.Oil,randomStuffType.Truffle_Oil)),

    PRESERVES_JAR("Preserves Jar", Map.of(
            randomStuffType.Wood.getName(), 50,
            randomStuffType.Stone.getName(), 40,
            ForagingMineralType.Coal.getName(), 8
    ), Map.of(Skill.FARMING, 2), 0,
            Arrays.asList(randomStuffType.Jelly,randomStuffType.Pickles)),

    DEHYDRATOR("Dehydrator", Map.of(
            randomStuffType.Wood.getName(), 30,
            randomStuffType.Stone.getName(), 20,
            randomStuffType.Fiber.getName(), 30
    ), Map.of(), 0,
            Arrays.asList(randomStuffType.Dried_Fruit,randomStuffType.Dried_Mushroom,
                    randomStuffType.Raisins)),

    GRASS_STARTER("Grass Starter", Map.of(
            randomStuffType.Wood.getName(), 1,
            randomStuffType.Fiber.getName(), 1
    ), Map.of(), 0,null),

    FISH_SMOKER("Fish Smoker", Map.of(
            randomStuffType.Wood.getName(), 50,
            randomStuffType.Iron_Bar.getName(), 3,
            ForagingMineralType.Coal.getName(), 10
    ), Map.of(), 0,
            Arrays.asList(randomStuffType.Smoked_Fish)),

    MYSTIC_TREE_SEED("Mystic Tree Seed", Map.of(
            SourceType.Acorns.getName(), 5,
            SourceType.MapleSeeds.getName(), 5,
            SourceType.PineCones.getName(), 5,
            SourceType.MahoganySeeds.getName(), 5
    ), Map.of(Skill.FORAGING, 4), 100, null);

    private final String name;
    private final Map<String, Integer> recipe;
    private final Map<Skill, Integer> requiredSkill;
    private final int sellPrice;
    private final List<randomStuffType> products;


    MachineType(String name, Map<String, Integer> recipe,Map<Skill,
            Integer> requiredSkill, int sellPrice, List<randomStuffType> products)
    {
        this.name = name;
        this.recipe = recipe;
        this.requiredSkill = requiredSkill;
        this.sellPrice = sellPrice;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getRecipe() {
        return recipe;
    }

    public Map<Skill, Integer> getRequiredSkill() {
        return requiredSkill;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public List<randomStuffType> getProducts() {
        return products;
    }
}