package org.example.Model.Reccepies;

import org.example.Model.Growables.SourceType;

import java.util.HashMap;
import java.util.Map;

public enum randomStuffType {
        WheatFlower("Wheat Flower", 125,0,0,null,false),
        Sugar("Sugar",125,0,0,null,false),
        Rice("Rice",250,0,0,null,false),
        Fiber("Fiber",200,0,0,null,false),
        Wood("Wood",200,0,0,null,false),
        Soil("Soil",200,0,0,null,false),
        //the prices are random

        Honey("Honey",350,75,96,null,true),
        Cheese("Cheese",230,100,3,Map.of("Milk",1),true),
        Goat_Cheese("Goat Cheese",400,100,3,Map.of("Goat Milk",1),true),
        Beer("Beer",200,50,24,Map.of("Wheat",1),true),
        Vinegar("Vinegar",100,13,10,Map.of("Rice",1),true),
        Coffee("Coffee",150,75,2, Map.of("Coffee Beans",5),true),
        Juice("Juice",225,80,96,Map.of("Orange",1),true),
        Mead("Mead",300,100,10,Map.of("Honey",1),true),
        Pale_Ale("Pale Ale",300,50,72,Map.of("Hops",1),true),
        Wine("Wine",240,80,168,Map.of("Grapes",1),true),
        Dried_Mushroom("Dried Mushroom",325,50,10,Map.of("Common Mushroom",5),true),
        Dried_Fruit("Dried Fruit",350,75,10,Map.of("Apricot",5),true),
        Raisins("Raisins",600,125,10,Map.of("Grapes",5),true),
        Cloth("Cloth",470,0,4,Map.of("Wool",1),false),
        Mayonnaise("Mayonnaise",190,50,3,Map.of("Egg",1),true),
        Duck_Mayonnaise("Duck Mayonnaise",37,75,3,Map.of("Duck Egg",1),true),
        Dinosaur_Mayonnaise("Dinosaur Mayonnaise",800,125,3,Map.of("Dinosaur Egg",1),true),
        Truffle_Oil("Truffle Oil",1065,38,6,Map.of("Truffle",1),true),
        Oil("Oil",100,13,6,Map.of("Corn",1),true),
        Pickles("Pickles",570,150,6,Map.of("Red Cabbage",1),true),
        Jelly("Jelly",225,80,72,Map.of("Orange",1),true),
        Smoked_Fish("Smoked Fish",150,200,72,Map.of("Salmon",1),true),
        Copper_Bar("Copper Bar",50,0,4,Map.of("Copper",5,"Coal",1),false),
        Iron_Bar("Iron Bar",100,0,4,Map.of("Iron",5,"Coal",1),false),
        Gold_Bar("Gold Bar",250,0,4,Map.of("Gold",5,"Coal",1),false),
        Stone("Stone", 20,0, 0,Map.of(), false),
        Hay("Hay", 50, 0, 0, Map.of(), false),
        Joja_Cola("Joja Cola", 75, 0, 0, Map.of(), false),
        RetainingSoil("Retaining Soil",150,0,4,Map.of(), false),
        SpeedGro("Speed-Gro",100, 0, 0, Map.of(), false),
        Bouquet("Bouquet",1000,0,0,Map.of(), false),
        WeddingRing("Wedding Ring",10000,0,0,Map.of(), false),
        Iridium_Bar("Iridium Bar",1000,0,4,Map.of("Iridium",5,"Coal",1),false);





        private final String name;
        private final int sellPrice;
        private final int energy;
        private final int processingTime;
        private final Map<String ,Integer> ingredients;
        private final Boolean isEatable;

        randomStuffType(String name, int sellPrice, int energy, int processingTime,
                        Map<String ,Integer> ingredients, Boolean isEatable) {
                this.name = name;
                this.sellPrice = sellPrice;
                this.energy = energy;
                this.processingTime = processingTime;
                this.ingredients = ingredients;
                this.isEatable = isEatable;
        }

        public String getName() {
                return name;
        }

        public int getSellPrice() {
                return sellPrice;
        }

        public int getEnergy() {
                return energy;
        }

        public static randomStuffType fromName(String name) {
                for (randomStuffType type : values()) {
                        if (type.getName().equalsIgnoreCase(name)) {
                                return type;
                        }
                }
                return null;
        }
}