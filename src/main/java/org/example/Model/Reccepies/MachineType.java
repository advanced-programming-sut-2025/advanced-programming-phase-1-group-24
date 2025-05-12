package org.example.Model.Reccepies;

import org.example.Model.Growables.SourceType;
import org.example.Model.Skill;
import org.example.Model.Things.ForagingMineralType;
import org.example.Model.Things.Item;

import java.util.ArrayList;
import java.util.Map;

public enum MachineType {
    CHERRY_BOMB("Cherry Bomb", Map.of(
            ForagingMineralType.Copper, 4,
            ForagingMineralType.Coal, 1
    ), Map.of(Skill.MINING, 1), 50),

    BOMB("Bomb", Map.of(
            ForagingMineralType.Iron, 4,
            ForagingMineralType.Coal, 1
    ), Map.of(Skill.MINING, 2), 50),

    MEGA_BOMB("Mega Bomb", Map.of(
            ForagingMineralType.Gold, 4,
            ForagingMineralType.Coal, 1
    ), Map.of(Skill.MINING, 3), 50),

    SPRINKLER("Sprinkler", Map.of(
            randomStuffType.Copper_Bar, 1,
            randomStuffType.Iron_Bar, 1
    ), Map.of(Skill.FARMING, 1), 0),

    QUALITY_SPRINKLER("Quality Sprinkler", Map.of(
            randomStuffType.Iron_Bar, 1,
            randomStuffType.Gold_Bar, 1
    ), Map.of(Skill.FARMING, 2), 0),

    IRIDIUM_SPRINKLER("Iridium Sprinkler", Map.of(
            randomStuffType.Gold_Bar, 1,
            randomStuffType.Iridium_Bar, 1
    ), Map.of(Skill.FARMING, 3), 0),

    CHARCOAL_KLIN("Charcoal Klin", Map.of(
            randomStuffType.Wood, 20,
            randomStuffType.Copper_Bar, 2
    ), Map.of(Skill.FORAGING, 1), 0),

    FURNACE("Furnace", Map.of(
            ForagingMineralType.Copper, 20,
            randomStuffType.Stone, 25
    ), Map.of(), 0),

    SCARECROW("Scarecrow", Map.of(
            randomStuffType.Wood, 50,
            ForagingMineralType.Coal, 1,
            randomStuffType.Fiber, 20
    ), Map.of(), 0),

    DELUXE_SCARECROW("Deluxe Scarecrow", Map.of(
            randomStuffType.Wood, 50,
            ForagingMineralType.Coal, 1,
            randomStuffType.Fiber, 20,
            ForagingMineralType.Iriduim, 1
    ), Map.of(Skill.FARMING, 2), 0),

    BEE_HOUSE("Bee House", Map.of(
            randomStuffType.Wood, 40,
            ForagingMineralType.Coal, 8,
            randomStuffType.Iron_Bar, 1
    ), Map.of(Skill.FARMING, 1), 0),

    CHEESE_PRESS("Cheese Press", Map.of(
            randomStuffType.Wood, 45,
            randomStuffType.Stone, 45,
            randomStuffType.Copper_Bar, 1
    ), Map.of(Skill.FARMING, 2), 0),

    KEG("Keg", Map.of(
            randomStuffType.Wood, 30,
            randomStuffType.Copper_Bar, 1,
            randomStuffType.Iron_Bar, 1
    ), Map.of(Skill.FARMING, 2), 0),

    LOOM("Loom", Map.of(
            randomStuffType.Wood, 60,
            randomStuffType.Fiber, 30
    ), Map.of(Skill.FARMING, 3), 0),

    MAYONNAISE_MACHINE("Mayonnaise Machine", Map.of(
            randomStuffType.Wood, 15,
            randomStuffType.Stone, 15,
            randomStuffType.Copper_Bar, 1
    ), Map.of(), 0),

    OIL_MAKER("Oil Maker", Map.of(
            randomStuffType.Wood, 100,
            randomStuffType.Gold_Bar, 1,
            randomStuffType.Iron_Bar, 1
    ), Map.of(Skill.FARMING, 3), 0),

    PRESERVES_JAR("Preserves Jar", Map.of(
            randomStuffType.Wood, 50,
            randomStuffType.Stone, 40,
            ForagingMineralType.Coal, 8
    ), Map.of(Skill.FARMING, 2), 0),

    DEHYDRATOR("Dehydrator", Map.of(
            randomStuffType.Wood, 30,
            randomStuffType.Stone, 20,
            randomStuffType.Fiber, 30
    ), Map.of(), 0),

    GRASS_STARTER("Grass Starter", Map.of(
            randomStuffType.Wood, 1,
            randomStuffType.Fiber, 1
    ), Map.of(), 0),

    FISH_SMOKER("Fish Smoker", Map.of(
            randomStuffType.Wood, 50,
            randomStuffType.Iron_Bar, 3,
            ForagingMineralType.Coal, 10
    ), Map.of(), 0),

    MYSTIC_TREE_SEED("Mystic Tree Seed", Map.of(
            SourceType.Acorns, 5,
            SourceType.MapleSeeds, 5,
            SourceType.PineCones, 5,
            SourceType.MahoganySeeds, 5
    ), Map.of(Skill.FORAGING, 4), 100);

    private final String name;
    private final Map<Enum<?>, Integer> recipe;
    private final Map<Skill, Integer> requiredSkill;
    private final int sellPrice;


    MachineType(String name, Map<Enum<?>, Integer> recipe,Map<Skill, Integer> requiredSkill, int sellPrice)
    {
        this.name = name;
        this.recipe = recipe;
        this.requiredSkill = requiredSkill;
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public Map<Enum<?>, Integer> getRecipe() {
        return recipe;
    }

    public Map<Skill, Integer> getRequiredSkill() {
        return requiredSkill;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void useMachine(){} //overridden later
}
