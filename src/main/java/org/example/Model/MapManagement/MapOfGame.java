package org.example.Model.MapManagement;

import org.example.Model.Animals.Animal;
import org.example.Model.Animals.AnimalType;
import org.example.Model.Growables.Growable;
import org.example.Model.Growables.GrowableFactory;
import org.example.Model.Growables.GrowableType;
import org.example.Model.Growables.SourceType;
import org.example.Model.Places.*;
import org.example.Model.Reccepies.Machine;
import org.example.Model.Reccepies.MachineType;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;
import org.example.Model.Things.*;
import org.example.Model.Tools.*;
import org.example.Model.Places.Farm;
import org.example.Model.Places.House;
import org.example.Model.Things.ForagingMineral;
import org.example.Model.Things.ForagingMineralType;
import org.example.Model.Things.ProductQuality;
import org.example.Model.User;

import javax.naming.InsufficientResourcesException;
import java.util.*;


public class MapOfGame {

    private Tile[][] map;
    private int width = 150;
    private int height = 150;
    private ArrayList<Farm> farms = new ArrayList<>();
    private ArrayList<Shop> shops = new ArrayList<>();

    public MapOfGame() {
        initializeMap();
    }

    private void initializeMap() {
        this.map = new Tile[height][width];
        initializeShops();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile();
                if (y > 50 && y < 100 && x > 50 && x < 100) {
                    tile.setType(TileType.NPCLAND);
                } else tile.setType(TileType.EMPTY);
                tile.setWalkable(true);
                tile.setContainedItem(null);
                tile.setContainedGrowable(null);
                tile.setContainedAnimal(null);
                tile.setContainedNPC(null);
                tile.setX(x);
                tile.setY(y);
                map[y][x] = tile;
            }
        }
        //initialize shops
        for (Shop shop : shops) {
            int startX = shop.getX();
            int startY = shop.getY();
            int width = shop.getWidth();
            int height = shop.getHeight();

            for (int y = startY; y < startY + height; y++) {
                for (int x = startX; x < startX + width; x++) {
                    Tile tile = map[y][x];
                    tile.setType(TileType.SHOP); // mark tile as shop
                    tile.setWalkable(true);       // optionally restrict if needed
                }
            }
        }
        //initialize npcHouses
// Initialize NPC Houses
        int[][] npsHouseCoordinates = {
                {70, 53}, {70, 63}, {70, 73}, {70, 83}, {70, 93}
        };

        for (int[] coordinates : npsHouseCoordinates) {
            int startX = coordinates[0];
            int startY = coordinates[1];

            // Loop over each 5x5 area to set NPC HOUSE type
            for (int y = startY; y < startY + 6; y++) {
                for (int x = startX; x < startX + 6; x++) {
                    Tile tile = map[y][x];
                    tile.setType(TileType.NPCHOUSE); // mark tile as NPC house
                    // tile.setWalkable(true);          // optionally adjust walkability
                }
            }
        }


    }

    public Tile[][] getMap() {
        return map;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public void addFarm(Farm farm) {
        this.farms.add(farm);
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public void applyLightningStrikeIfStormy(boolean isStormyDay) {
        if (!isStormyDay) return;

        Random random = new Random();

        for (Farm farm : farms) {
            List<Tile> farmTiles = getAllFarmTiles(farm);

            int strikes = 3;

            Set<Integer> selectedIndices = new HashSet<>();
            while (selectedIndices.size() < strikes) {
                selectedIndices.add(random.nextInt(farmTiles.size()));
            }

            for (int index : selectedIndices) {
                Tile tile = farmTiles.get(index);
                applyLightningEffect(tile);
            }
        }
    }

    private List<Tile> getAllFarmTiles(Farm farm) {
        List<Tile> result = new ArrayList<>();
        int startX = farm.getX();
        int startY = farm.getY();
        int width = farm.getWidth();
        int height = farm.getHeight();

        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                if (x >= 0 && x < this.width && y >= 0 && y < this.height) { // safety check
                    result.add(map[x][y]);
                }
            }
        }
        return result;
    }


    public void applyLightningEffect(Tile tile) {
        if (tile.getType() == TileType.GREENHOUSE) {
            return;
        }
        if (tile.getContainedGrowable() != null) {
            tile.setContainedItem(new ForagingMineral(ProductQuality.Normal, ForagingMineralType.Coal));
            tile.getContainedGrowable().setGrowableType(GrowableType.Coal);
            tile.setProductOfGrowable(null);
        }
    }

    public Farm isInsideAnyFarm(int x, int y) {
        for (Farm farm : farms) {
            int farmX = farm.getX();
            int farmY = farm.getY();
            int farmWidth = farm.getWidth();
            int farmHeight = farm.getHeight();

            if (x >= farmX && x < farmX + farmWidth &&
                    y >= farmY && y < farmY + farmHeight) {
                return farm;
            }
        }
        return null;
    }

    public void changeTile(TileType newTile, TileType oldTile) {}

    public Farm getFarmByOwner(User owner) {
        for (Farm farm : farms) {
            if (farm.getOwner().equals(owner)) {
                return farm;
            }
        }
        return null;
    }
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return map[y][x];
        }
        return null; // or throw an exception if you prefer
    }


    public void initializeShops() {
        shops.clear();

        Shop blacksmith = new Shop(
                ShopType.BLACKSMITH,
                "Blacksmith",
                "Clint",
                9, 16,
                createBlacksmithItems(),
                52, 52, 4, 4
        );

        Shop jojamart = new Shop(
                ShopType.JOJA_MART,
                "JojaMart",
                "Morris",
                9, 23,
                createJojaMartItems(),
                52, 62, 4, 4
        );

        Shop pierreStore = new Shop(
                ShopType.PIERRE_STORE,
                "Pierre’s General Store",
                "Pierre",
                9, 17,
                createPierreStoreItems(),
                52, 72, 3, 4
        );

        Shop carpenterShop = new Shop(
                ShopType.CARPENTER_SHOP,
                "Carpenter’s Shop",
                "Robin",
                9, 20,
                createCarpenterShopItems(),
                52, 82, 4, 4
        );

        Shop fishShop = new Shop(
                ShopType.FISH_SHOP,
                "Fish Shop",
                "Willy",
                9, 17,
                createFishShopItems(),
                92, 55, 5, 4
        );

        Shop marnieRanch = new Shop(
                ShopType.MARNIE_RANCH,
                "Marnie’s Ranch",
                "Marnie",
                9, 16,
                createMarnieRanchItems(),
                92, 65, 4, 5
        );

        Shop starDropSaloon = new Shop(
                ShopType.STAR_DROP_SALOON,
                "The Stardrop Saloon",
                "Gus",
                12, 24,
                createSaloonItems(),
                92, 75, 5, 4
        );

        shops.addAll(Arrays.asList(
                blacksmith, jojamart, pierreStore, carpenterShop,
                fishShop, marnieRanch, starDropSaloon
        ));
    }

    private ArrayList<ShopItem> createBlacksmithItems() {
        ArrayList<ShopItem> items = new ArrayList<>();

        // Adding ores and their corresponding prices
        items.add(new ShopItem("Copper", Integer.MAX_VALUE, new ForagingMineral(ProductQuality.Normal, ForagingMineralType.Copper), ShopItemType.FORAGINGMINERAL, 75, 75, 75, 75));
        items.add(new ShopItem("Iron", Integer.MAX_VALUE, new ForagingMineral(ProductQuality.Normal, ForagingMineralType.Iron), ShopItemType.FORAGINGMINERAL, 150, 150, 150, 150));
        items.add(new ShopItem("Coal", Integer.MAX_VALUE, new ForagingMineral(ProductQuality.Normal, ForagingMineralType.Coal), ShopItemType.FORAGINGMINERAL, 150, 150, 150, 150));
        items.add(new ShopItem("Gold", Integer.MAX_VALUE, new ForagingMineral(ProductQuality.Normal, ForagingMineralType.Gold), ShopItemType.FORAGINGMINERAL, 400, 400, 400, 400));

        // Adding tools and their corresponding prices and ingredients
        items.add(new ShopItem("Copper Tool", 1, null, ShopItemType.TOOL_UPGRADE, 2000, 2000, 2000, 2000));
        items.add(new ShopItem("Iron Tool", 1, null, ShopItemType.TOOL_UPGRADE, 5000, 5000, 5000, 5000));
        items.add(new ShopItem("Gold Tool", 1, null, ShopItemType.TOOL_UPGRADE, 10000, 10000, 10000, 10000));
        items.add(new ShopItem("Iridium Tool", 1, null, ShopItemType.TOOL_UPGRADE, 25000, 25000, 25000, 25000));

        // Adding trash cans with materials and corresponding prices
        items.add(new ShopItem("Copper Trash Can", 1, new TrashCan(ToolType.TRASHCAN), ShopItemType.TRASHCAN, 1000, 1000, 1000, 1000));
        items.add(new ShopItem("Iron Trash Can", 1, new TrashCan(ToolType.TRASHCAN), ShopItemType.TRASHCAN, 2500, 2500, 2500, 2500));
        items.add(new ShopItem("Gold Trash Can", 1, new TrashCan(ToolType.TRASHCAN), ShopItemType.TRASHCAN, 5000, 5000, 5000, 5000));
        items.add(new ShopItem("Iridium Trash Can", 1, new TrashCan(ToolType.TRASHCAN), ShopItemType.TRASHCAN, 12500, 12500, 12500, 12500));

        return items;
    }


    private ArrayList<ShopItem> createJojaMartItems() {
        ArrayList<ShopItem> items = new ArrayList<>();
        // Spring Seeds
        items.add(new ShopItem("Parsnip Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Parsnip Seeds")), ShopItemType.Source, 25, 0, 0, 0));
        items.add(new ShopItem("Bean Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Bean Starter")), ShopItemType.Source, 75, 0, 0, 0));
        items.add(new ShopItem("Cauliflower Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Cauliflower Seeds")), ShopItemType.Source, 100, 0, 0, 0));
        items.add(new ShopItem("Potato Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Potato Seeds")), ShopItemType.Source, 62, 0, 0, 0));
        items.add(new ShopItem("Strawberry Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Strawberry Seeds")), ShopItemType.Source, 100, 0, 0, 0));
        items.add(new ShopItem("Tulip Bulb", 5, GrowableFactory.getInstance().create(SourceType.fromName("Tulip Bulb")), ShopItemType.Source, 25, 0, 0, 0));
        items.add(new ShopItem("Kale Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Kale Seeds")), ShopItemType.Source, 87, 0, 0, 0));
        items.add(new ShopItem("Carrot Seeds", 10, GrowableFactory.getInstance().create(SourceType.fromName("Carrot Seeds")), ShopItemType.Source, 5, 0, 0, 0));
        items.add(new ShopItem("Rhubarb Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Rhubarb Seeds")), ShopItemType.Source, 100, 0, 0, 0));
        items.add(new ShopItem("Jazz Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Jazz Seeds")), ShopItemType.Source, 37, 0, 0, 0));
        items.add(new ShopItem("Coffee Beans", 1, GrowableFactory.getInstance().create(SourceType.fromName("Coffee Bean")), ShopItemType.Source, 200, 200, 0, 0));

        // Summer Seeds
        items.add(new ShopItem("Tomato Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Tomato Seeds")), ShopItemType.Source, 0, 62, 0, 0));
        items.add(new ShopItem("Pepper Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Pepper Seeds")), ShopItemType.Source, 0, 50, 0, 0));
        items.add(new ShopItem("Wheat Seeds", 10, GrowableFactory.getInstance().create(SourceType.fromName("Wheat Seeds")), ShopItemType.Source, 0, 12, 12, 0));
        items.add(new ShopItem("Summer Squash Seeds", 10, GrowableFactory.getInstance().create(SourceType.fromName("Summer Squash Seeds")), ShopItemType.Source, 0, 10, 0, 0));
        items.add(new ShopItem("Radish Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Radish Seeds")), ShopItemType.Source, 50, 50, 0, 0));
        items.add(new ShopItem("Melon Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Melon Seeds")), ShopItemType.Source, 0, 100, 0, 0));
        items.add(new ShopItem("Hops Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Hops Starter")), ShopItemType.Source, 0, 75, 0, 0));
        items.add(new ShopItem("Poppy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Poppy Seeds")), ShopItemType.Source, 0, 125, 0, 0));
        items.add(new ShopItem("Spangle Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Spangle Seeds")), ShopItemType.Source, 0, 62, 0, 0));
        items.add(new ShopItem("Starfruit Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Starfruit Seeds")), ShopItemType.Source, 0, 400, 0, 0));
        items.add(new ShopItem("Sunflower Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Sunflower Seeds")), ShopItemType.Source, 0, 125, 125, 0));

        // Fall Seeds
        items.add(new ShopItem("Corn Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Corn Seeds")), ShopItemType.Source, 0, 0, 187, 0));
        items.add(new ShopItem("Eggplant Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Eggplant Seeds")), ShopItemType.Source, 0, 0, 25, 0));
        items.add(new ShopItem("Pumpkin Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Pumpkin Seeds")), ShopItemType.Source, 0, 0, 125, 0));
        items.add(new ShopItem("Broccoli Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Broccoli Seeds")), ShopItemType.Source, 0, 0, 15, 0));
        items.add(new ShopItem("Amaranth Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Amaranth Seeds")), ShopItemType.Source, 0, 0, 87, 0));
        items.add(new ShopItem("Grape Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Grape Starter")), ShopItemType.Source, 0, 0, 75, 0));
        items.add(new ShopItem("Beet Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Beet Seeds")), ShopItemType.Source, 0, 0, 20, 0));
        items.add(new ShopItem("Yam Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Yam Seeds")), ShopItemType.Source, 0, 0, 75, 0));
        items.add(new ShopItem("Bok Choy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Bok Choy Seeds")), ShopItemType.Source, 0, 0, 62, 0));
        items.add(new ShopItem("Cranberry Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Cranberry Seeds")), ShopItemType.Source, 0, 0, 300, 0));
        items.add(new ShopItem("Fairy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Fairy Seeds")), ShopItemType.Source, 0, 0, 250, 0));
        items.add(new ShopItem("Rare Seed", 1, GrowableFactory.getInstance().create(SourceType.fromName("Rare Seed")), ShopItemType.Source, 0, 0, 1000, 0));

        // Winter Seeds
        items.add(new ShopItem("Powdermelon Seeds", 10, GrowableFactory.getInstance().create(SourceType.fromName("Powdermelon Seeds")), ShopItemType.Source, 0, 0, 0, 20));

        //   Year-Round Items
        items.add(new ShopItem("Joja Cola", Integer.MAX_VALUE, new randomStuff(75, randomStuffType.Joja_Cola), ShopItemType.RANDOMSTUFF, 75, 75, 75, 75));
        items.add(new ShopItem("Ancient Seed", 1, GrowableFactory.getInstance().create(SourceType.fromName("Ancient Seeds")), ShopItemType.Source, 500, 500, 500, 500));
        items.add(new ShopItem("Grass Starter", Integer.MAX_VALUE, new Machine(MachineType.GRASS_STARTER), ShopItemType.MACHINERECIPE, 125, 125, 125, 125));
        items.add(new ShopItem("Sugar", Integer.MAX_VALUE, new randomStuff(125, randomStuffType.Sugar), ShopItemType.RANDOMSTUFF, 125, 125, 125, 125));
        items.add(new ShopItem("Wheat Flour", Integer.MAX_VALUE, new randomStuff(125, randomStuffType.WheatFlower), ShopItemType.RANDOMSTUFF, 125, 125, 125, 125));
        items.add(new ShopItem("Rice", Integer.MAX_VALUE, new randomStuff(250, randomStuffType.Rice), ShopItemType.RANDOMSTUFF, 250, 250, 250, 250));
        items.add(new ShopItem("Mixed Seeds", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.MixedSeeds), ShopItemType.Source, 50, 50, 50, 50));

        return items;
    }

    private ArrayList<ShopItem> createPierreStoreItems() {
        ArrayList<ShopItem> items = new ArrayList<>();
        //BackPack
        items.add(new ShopItem("Large Pack", 1, null, ShopItemType.LARGE_BACKPACK, 2000, 2000, 2000, 2000));
        items.add(new ShopItem("Deluxe Pack", 1, null, ShopItemType.DELUXE_BACKPACK, 10000, 10000, 10000, 10000));

        // Year-round items
        items.add(new ShopItem("Rice", Integer.MAX_VALUE, new randomStuff(200, randomStuffType.Rice), ShopItemType.RANDOMSTUFF, 200, 200, 200, 200));
        items.add(new ShopItem("Wheat Flour", Integer.MAX_VALUE, new randomStuff(100, randomStuffType.WheatFlower), ShopItemType.RANDOMSTUFF, 100, 100, 100, 100));
        items.add(new ShopItem("Sugar", Integer.MAX_VALUE, new randomStuff(100, randomStuffType.Sugar), ShopItemType.RANDOMSTUFF, 100, 100, 100, 100));
        items.add(new ShopItem("Oil", Integer.MAX_VALUE, new randomStuff(200, randomStuffType.Oil), ShopItemType.RANDOMSTUFF, 200, 200, 200, 200));
        items.add(new ShopItem("Vinegar", Integer.MAX_VALUE, new randomStuff(200, randomStuffType.Vinegar), ShopItemType.RANDOMSTUFF, 200, 200, 200, 200));
        items.add(new ShopItem("Bouquet", 2, new randomStuff(1000, randomStuffType.Bouquet), ShopItemType.RANDOMSTUFF, 1000, 1000, 1000, 1000));
        items.add(new ShopItem("Wedding Ring", 2, new randomStuff(10000, randomStuffType.WeddingRing), ShopItemType.RANDOMSTUFF, 10000, 10000, 10000, 10000));
        items.add(new ShopItem("Dehydrator", 1, new Machine(MachineType.DEHYDRATOR), ShopItemType.MACHINERECIPE, 10000, 10000, 10000, 10000));
        items.add(new ShopItem("Grass Starter", 1, new Machine(MachineType.GRASS_STARTER), ShopItemType.MACHINERECIPE, 1000, 1000, 1000, 1000));
        items.add(new ShopItem("Retaining Soil", Integer.MAX_VALUE, new randomStuff(150, randomStuffType.RetainingSoil), ShopItemType.RANDOMSTUFF, 150, 150, 150, 150));
        items.add(new ShopItem("Speed-Gro", Integer.MAX_VALUE, new randomStuff(100, randomStuffType.SpeedGro), ShopItemType.RANDOMSTUFF, 100, 100, 100, 100));

        // Saplings
        items.add(new ShopItem("Apple Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Apple Sapling")), ShopItemType.Source, 4000, 4000, 4000, 4000));
        items.add(new ShopItem("Apricot Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Apricot Sapling")), ShopItemType.Source, 2000, 2000, 2000, 2000));
        items.add(new ShopItem("Cherry Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Cherry Sapling")), ShopItemType.Source, 3400, 3400, 3400, 3400));
        items.add(new ShopItem("Orange Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Orange Sapling")), ShopItemType.Source, 4000, 4000, 4000, 4000));
        items.add(new ShopItem("Peach Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Peach Sapling")), ShopItemType.Source, 6000, 6000, 6000, 6000));
        items.add(new ShopItem("Pomegranate Sapling", Integer.MAX_VALUE, GrowableFactory.getInstance().create(SourceType.fromName("Pomegranate Sapling")), ShopItemType.Source, 6000, 6000, 6000, 6000));

        // Spring Seeds
        items.add(new ShopItem("Parsnip Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Parsnip Seeds")), ShopItemType.Source, 20, 30, 30, 30));
        items.add(new ShopItem("Bean Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Bean Starter")), ShopItemType.Source, 60, 90, 90, 90));
        items.add(new ShopItem("Cauliflower Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Cauliflower Seeds")), ShopItemType.Source, 80, 120, 120, 120));
        items.add(new ShopItem("Potato Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Potato Seeds")), ShopItemType.Source, 50, 75, 75, 75));
        items.add(new ShopItem("Tulip Bulb", 5, GrowableFactory.getInstance().create(SourceType.fromName("Tulip Bulb")), ShopItemType.Source, 20, 30, 30, 30));
        items.add(new ShopItem("Kale Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Kale Seeds")), ShopItemType.Source, 70, 105, 105, 105));
        items.add(new ShopItem("Jazz Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Jazz Seeds")), ShopItemType.Source, 30, 45, 45, 45));
        items.add(new ShopItem("Garlic Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Garlic Seeds")), ShopItemType.Source, 40, 60, 60, 60));
        items.add(new ShopItem("Rice Shoot", 5, GrowableFactory.getInstance().create(SourceType.fromName("Rice Shoot")), ShopItemType.Source, 40, 60, 60, 60));

        // Summer Seeds
        items.add(new ShopItem("Melon Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Melon Seeds")), ShopItemType.Source, 120, 80, 120, 120));
        items.add(new ShopItem("Tomato Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Tomato Seeds")), ShopItemType.Source, 75, 50, 75, 75));
        items.add(new ShopItem("Blueberry Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Blueberry Seeds")), ShopItemType.Source, 120, 80, 120, 120));
        items.add(new ShopItem("Pepper Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Pepper Seeds")), ShopItemType.Source, 60, 40, 60, 60));
        items.add(new ShopItem("Wheat Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Wheat Seeds")), ShopItemType.Source, 15, 10, 10, 15));
        items.add(new ShopItem("Radish Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Radish Seeds")), ShopItemType.Source, 60, 40, 60, 60));
        items.add(new ShopItem("Poppy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Poppy Seeds")), ShopItemType.Source, 150, 100, 150, 150));
        items.add(new ShopItem("Spangle Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Spangle Seeds")), ShopItemType.Source, 75, 50, 75, 75));
        items.add(new ShopItem("Hops Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Hops Starter")), ShopItemType.Source, 90, 60, 90, 90));
        items.add(new ShopItem("Corn Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Corn Seeds")), ShopItemType.Source, 225, 150, 150, 225));
        items.add(new ShopItem("Sunflower Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Sunflower Seeds")), ShopItemType.Source, 300, 200, 300, 300));
        items.add(new ShopItem("Red Cabbage Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Red Cabbage Seeds")), ShopItemType.Source, 150, 100, 150, 150));

        // Fall Seeds
        items.add(new ShopItem("Eggplant Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Eggplant Seeds")), ShopItemType.Source, 30, 30, 20, 30));
        items.add(new ShopItem("Pumpkin Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Pumpkin Seeds")), ShopItemType.Source, 150, 150, 100, 150));
        items.add(new ShopItem("Bok Choy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Bok Choy Seeds")), ShopItemType.Source, 75, 75, 50, 75));
        items.add(new ShopItem("Yam Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Yam Seeds")), ShopItemType.Source, 90, 90, 60, 90));
        items.add(new ShopItem("Cranberry Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Cranberry Seeds")), ShopItemType.Source, 360, 360, 240, 360));
        items.add(new ShopItem("Fairy Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Fairy Seeds")), ShopItemType.Source, 300, 300, 200, 300));
        items.add(new ShopItem("Amaranth Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Amaranth Seeds")), ShopItemType.Source, 105, 105, 70, 105));
        items.add(new ShopItem("Grape Starter", 5, GrowableFactory.getInstance().create(SourceType.fromName("Grape Starter")), ShopItemType.Source, 90, 90, 60, 90));
        items.add(new ShopItem("Artichoke Seeds", 5, GrowableFactory.getInstance().create(SourceType.fromName("Artichoke Seeds")), ShopItemType.Source, 45, 45, 30, 45));

        return items;
    }


    private ArrayList<ShopItem> createCarpenterShopItems() {
        ArrayList<ShopItem> items = new ArrayList<>();

        // Barn upgrades
        items.add(new ShopItem("Barn", 1,
                new Habitat(0, 0, 4, 3, StorageType.INITIAL),
                ShopItemType.BARN, 6000, 6000, 6000, 6000));

        items.add(new ShopItem("Big Barn", 1,
                new Habitat(0, 0, 4, 3, StorageType.BIG),
                ShopItemType.BARN, 12000, 12000, 12000, 12000));

        items.add(new ShopItem("Deluxe Barn", 1,
                new Habitat(0, 0, 4, 3, StorageType.DELUX),
                ShopItemType.BARN, 25000, 25000, 25000, 25000));

        // Coop upgrades
        items.add(new ShopItem("Cage", 1,
                new Habitat(0, 0, 3, 3, StorageType.INITIAL),
                ShopItemType.CAGE, 4000, 4000, 4000, 4000));

        items.add(new ShopItem("Big Cage", 1,
                new Habitat(0, 0, 3, 3, StorageType.BIG),
                ShopItemType.CAGE, 10000, 10000, 10000, 10000));

        items.add(new ShopItem("Deluxe Cage", 1,
                new Habitat(0, 0, 3, 3, StorageType.DELUX),
                ShopItemType.CAGE, 20000, 20000, 20000, 20000));

        // Other buildings
        items.add(new ShopItem("Wood", Integer.MAX_VALUE,
                new randomStuff(20, randomStuffType.Wood),
                ShopItemType.RANDOMSTUFF, 10, 10, 10, 10));

        items.add(new ShopItem("Stone", Integer.MAX_VALUE,
                new randomStuff(20, randomStuffType.Stone),
                ShopItemType.RANDOMSTUFF, 20, 20, 20, 20));

        //shipping bin
        items.add(new ShopItem("Shipping Bin", Integer.MAX_VALUE, null, ShopItemType.SHIPPING_BIN, 250, 250, 250, 250));

        return items;
    }
    //       items.add(new ShopItem(20, "Wood", new Material("Wood"), 0, 30));
//        items.add(new ShopItem(50, "Stone", new Material("Stone"), 0, 30));


    private ArrayList<ShopItem> createFishShopItems() {
        ArrayList<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("Fish Smoker", 1, new Machine(MachineType.GRASS_STARTER), ShopItemType.MACHINERECIPE, 10000, 10000, 10000, 10000));
        //Trout soup??
        items.add(new ShopItem("Bamboo Pole", 1, new FishingPole(ToolType.FISHINGPOLE, FishingPoleMaterial.Bamboo), ShopItemType.FISHINGPOLE, 500, 500, 500, 500));
        items.add(new ShopItem("Training Rod", 1, new FishingPole(ToolType.FISHINGPOLE, FishingPoleMaterial.Training), ShopItemType.FISHINGPOLE, 25, 25, 25, 25));
        items.add(new ShopItem("Fiberglass Rod", 1, new FishingPole(ToolType.FISHINGPOLE, FishingPoleMaterial.FiberGlass), ShopItemType.FISHINGPOLE, 1800, 1800, 1800, 1800));
        items.add(new ShopItem("Iridium Rod", 1, new FishingPole(ToolType.FISHINGPOLE, FishingPoleMaterial.Iridium), ShopItemType.FISHINGPOLE, 7500, 7500, 7500, 7500));

        return items;
    }
//        items.add(new ShopItem(150, "Bait", new Item("Bait"), 0, 15));
//        items.add(new ShopItem(2000, "Fishing Rod", new Tool("Fishing Rod"), 0, 2));

    private ArrayList<ShopItem> createMarnieRanchItems() {
        ArrayList<ShopItem> items = new ArrayList<>();

        items.add(new ShopItem("Chicken", 2, new Animal("default", AnimalType.CHICKEN), ShopItemType.ANIMAL, 800, 800, 800, 800));
        items.add(new ShopItem("Cow", 2, new Animal("default", AnimalType.COW), ShopItemType.ANIMAL, 1500, 1500, 1500, 1500));
        items.add(new ShopItem("Goat", 2, new Animal("default", AnimalType.GOAT), ShopItemType.ANIMAL, 4000, 4000, 4000, 4000));
        items.add(new ShopItem("Duck", 2, new Animal("default", AnimalType.DUCK), ShopItemType.ANIMAL, 1200, 1200, 1200, 1200));
        items.add(new ShopItem("Sheep", 2, new Animal("default", AnimalType.SHEEP), ShopItemType.ANIMAL, 8000, 8000, 8000, 8000));
        items.add(new ShopItem("Rabbit", 2, new Animal("default", AnimalType.BUNNY), ShopItemType.ANIMAL, 8000, 8000, 8000, 8000));
        items.add(new ShopItem("Dinosaur", 2, new Animal("default", AnimalType.DINOSAUR), ShopItemType.ANIMAL, 14000, 14000, 14000, 14000));
        items.add(new ShopItem("Pig", 2, new Animal("default", AnimalType.PIG), ShopItemType.ANIMAL, 16000, 16000, 16000, 16000));

        items.add(new ShopItem("Hay", Integer.MAX_VALUE, new randomStuff(50, randomStuffType.Hay), ShopItemType.RANDOMSTUFF, 50, 50, 50, 50));
        items.add(new ShopItem("Milk Pail", 1, new MilkPail(ToolType.MILKPAIL), ShopItemType.MILKPAIL, 1000, 1000, 1000, 1000));
        items.add(new ShopItem("Shear", 1, new Shear(ToolType.SHEAR), ShopItemType.SHEAR, 1000, 1000, 1000, 1000));

        return items;
    }


    private ArrayList<ShopItem> createSaloonItems() {
        ArrayList<ShopItem> items = new ArrayList<>();

        // Foods (unlimited purchase)
        items.add(new ShopItem("Beer", Integer.MAX_VALUE, new randomStuff(400, randomStuffType.Coffee), ShopItemType.FOOD, 400, 400, 400, 400));
        items.add(new ShopItem("Salad", Integer.MAX_VALUE, new Food(FoodType.Salad), ShopItemType.FOOD, 220, 220, 220, 220));
        items.add(new ShopItem("Bread", Integer.MAX_VALUE, new Food(FoodType.Bread), ShopItemType.FOOD, 120, 120, 120, 120));
        items.add(new ShopItem("Spaghetti", Integer.MAX_VALUE, new Food(FoodType.Spaghetti), ShopItemType.FOOD, 240, 240, 240, 240));
        items.add(new ShopItem("Pizza", Integer.MAX_VALUE, new Food(FoodType.Pizza), ShopItemType.FOOD, 600, 600, 600, 600));
        items.add(new ShopItem("Coffee", Integer.MAX_VALUE, new randomStuff(300, randomStuffType.Coffee), ShopItemType.FOOD, 300, 300, 300, 300));

        // Recipes (limited to 1 purchase)
        items.add(new ShopItem("HashBrown Recipe", 1, null, ShopItemType.FOODRECIPE, 50, 50, 50, 50));
        items.add(new ShopItem("Omelet Recipe", 1, null, ShopItemType.FOODRECIPE, 100, 100, 100, 100));
        items.add(new ShopItem("Pancakes Recipe", 1, null, ShopItemType.FOODRECIPE, 100, 100, 100, 100));
        items.add(new ShopItem("Bread Recipe", 1, null, ShopItemType.FOODRECIPE, 100, 100, 100, 100));
        items.add(new ShopItem("Tortilla Recipe", 1, null, ShopItemType.FOODRECIPE, 100, 100, 100, 100));
        items.add(new ShopItem("Pizza Recipe", 1, null, ShopItemType.FOODRECIPE, 150, 150, 150, 150));
        items.add(new ShopItem("MakiRoll Recipe", 1, null, ShopItemType.FOODRECIPE, 300, 300, 300, 300));
        items.add(new ShopItem("TripleShotEspresso Recipe", 1, null, ShopItemType.FOODRECIPE, 5000, 5000, 5000, 5000));
        items.add(new ShopItem("Cookie Recipe", 1, null, ShopItemType.FOODRECIPE, 300, 300, 300, 300));

        return items;
    }


    public ArrayList<Shop> getShops() {
        return shops;
    }

    public Shop getShopAtPosition(int x, int y) {
        for (Shop shop : shops) {
            int shopX = shop.getX();
            int shopY = shop.getY();
            int shopWidth = shop.getWidth();
            int shopHeight = shop.getHeight();

            if (x >= shopX && x < shopX + shopWidth &&
                    y >= shopY && y < shopY + shopHeight) {
                return shop;
            }
        }
        return null;
    }



    public House getHousePosition(int x, int y) {
        for (Farm farm : farms) {
            House house = farm.getHouse();
            if (isInsideHouse(x, y, house)) {
                return house;
            }
        }
        return null; // No house contains the coordinates
    }

    private boolean isInsideHouse(int x, int y, House house) {
        int houseX = house.getX();
        int houseY = house.getY();
        int width = house.getWidth();
        int height = house.getHeight();

        return x >= houseX && x < houseX + width &&
                y >= houseY && y < houseY + height;
    }

}
