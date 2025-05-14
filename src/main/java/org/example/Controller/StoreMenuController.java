package org.example.Controller;

import org.example.Model.*;
import org.example.Model.Animals.Animal;
import org.example.Model.Animals.AnimalType;
import org.example.Model.Growables.Growable;
import org.example.Model.MapManagement.MapOfGame;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;
import org.example.Model.Places.*;
import org.example.Model.Reccepies.*;
import org.example.Model.Things.*;
import org.example.Model.TimeManagement.Season;
import org.example.Model.Tools.*;

import java.util.List;
import java.util.Random;

public class StoreMenuController {
    public Result purchase(String productName, int count) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();
        if (count <= 0) {
            return new Result(false, "Invalid count! Count must be greater than 0.");
        }

        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if (shop == null) {
            return new Result(false, "You must be inside a store to purchase items.");
        }
        if (game.getTimeAndDate().getHour() < shop.getStartHour() || game.getTimeAndDate().getHour() >= shop.getEndHour()) {
            return new Result(false, "the store is closed");
        }

        for (ShopItem item : shop.getProducts()) {
            if (item.getName().equalsIgnoreCase(productName)) {
                int price = getCurrentSeasonPrice(item);
                if (price == 0) {
                    return new Result(false, "This item is not available in the current season.");
                }

                int available = item.getDailyLimit() - item.getSoldToday();
                if (!item.isAvailable(count)) {
                    return new Result(false, "Not enough stock. Only " + available + " left for today.");
                }

                int totalCost = price * count;
                if (player.getMoney() < totalCost) {
                    return new Result(false, "Not enough coins. You need " + totalCost + " coins.");
                }


                // Handle special carpenter shop cases
                if (shop.getShopType() == ShopType.CARPENTER_SHOP &&
                        (item.getShopItemType() == ShopItemType.CAGE || item.getShopItemType() == ShopItemType.BARN)) {
                    /////?????????????????????????????????????????????????????
                    return new Result(false, "use the command for building cage or barn!(build -a <name> -l <x , y>)");
                    // return handleCarpenterSources(player, item);
                }
                if (shop.getShopType() == ShopType.MARNIE_RANCH &&
                        (item.getShopItemType() == ShopItemType.ANIMAL)) {
                    ///// ?????
                    return new Result(false, "use the command for buying animals!(buy animal -a <animal> -n <name>)");
                }
                if (shop.getShopType() == ShopType.BLACKSMITH &&
                        (item.getShopItemType() == ShopItemType.TOOL_UPGRADE || item.getShopItemType() == ShopItemType.TRASHCAN)) {
                    ///// ?????
                    return new Result(false, "use the command for upgrading tools!(tools upgrade <tool_name>)");
                }

                // Regular item purchase
                Result itemResult = buyItem(player, item, count);
                if (!itemResult.isSuccessful()) {
                    return new Result(false, "Purchase failed: " + itemResult.message());
                }

                item.sell(count);
                player.decreaseMoney(totalCost);
                return new Result(true, "Successfully purchased " + count + " " + productName + (count > 1 ? "s." : "."));
            }
        }

        return new Result(false, "Product '" + productName + "' not found in this store.");
    }


    private Result handleCarpenterSources(User player, ShopItem item) {
        int requiredWood = 0;
        int requiredStone = 0;
        int price = 0;

        switch (item.getName()) {
            case "Barn":
                requiredWood = 350;
                requiredStone = 150;
                price = 6000;
                break;
            case "Big Barn":
                requiredWood = 450;
                requiredStone = 200;
                price = 12000;
                break;
            case "Deluxe Barn":
                requiredWood = 550;
                requiredStone = 300;
                price = 25000;
                break;
            case "Cage":
                requiredWood = 300;
                requiredStone = 100;
                price = 4000;
                break;
            case "Big Cage":
                requiredWood = 400;
                requiredStone = 150;
                price = 10000;
                break;
            case "Deluxe Cage":
                requiredWood = 500;
                requiredStone = 200;
                price = 20000;
                break;
            case "Shipping Bin":
                requiredWood = 150;
                requiredStone = 0;
                price = 250;
                break;
            default:
                return new Result(false, "Unknown carpenter item: " + item.getName());
        }
//        /// ///////////////////////////////////////////for testing process
//        randomStuff wood = new randomStuff(12, randomStuffType.Wood);
//        randomStuff stone = new randomStuff(12, randomStuffType.Stone);
//        player.getBackpack().addItem(wood, requiredWood + 1);
//        player.getBackpack().addItem(stone, requiredStone + 1);
//        /// /////////////////////////////
        int playerWood = player.getBackpack().getItemCount("Wood");
        int playerStone = player.getBackpack().getItemCount("Stone");
        int playerCoins = player.getMoney();

        if (playerWood < requiredWood) {
            return new Result(false, "Not enough wood. Required: " + requiredWood + ", you have: " + playerWood);
        }

        if (playerStone < requiredStone) {
            return new Result(false, "Not enough stone. Required: " + requiredStone + ", you have: " + playerStone);
        }

        if (playerCoins < price) {
            return new Result(false, "Not enough coins. Required: " + price + ", you have: " + playerCoins);
        }

        // Deduct resources
        player.getBackpack().grabItem("Wood", requiredWood);
        player.getBackpack().grabItem("Stone", requiredStone);
        player.decreaseMoney(price);

        return new Result(true, "Successfully purchased: " + item.getName());
    }


    private int getCurrentSeasonPrice(ShopItem item) {
        Season currentSeason = App.getInstance().getCurrentGame().getTimeAndDate().getSeason();
        switch (currentSeason) {
            case SPRING:
                return item.getSpringPrice();
            case SUMMER:
                return item.getSummerPrice();
            case AUTUMN:
                return item.getAutumnPrice();
            case WINTER:
                return item.getWinterPrice();
            default:
                return 0;
        }
    }

    public Result buyItem(User player, ShopItem shopItem, int count) {
        MapOfGame map = App.getInstance().getCurrentGame().getMap();
        Object item = shopItem.getItem();
        Object itemCopy = null;
        Result result;

        if (item instanceof Animal) {
            itemCopy = ((Animal) item).copy();
            player.getOwnedAnimals().add((Animal) itemCopy);
            //update habitat? and add to habitat and place the animal tile in habitat
            result = new Result(true, "Successfully purchased: " + shopItem.getName());
        } else if (item instanceof Growable) {
            Growable growableCopy = ((Growable) item).copy();
            result = player.getBackpack().addItem(growableCopy, count);
        } else if (item instanceof Habitat) {
            Habitat habitatCopy = ((Habitat) item).copy();
            map.getFarmByOwner(player).getBarn().add(habitatCopy);
            result = new Result(true, "Successfully purchased: " + habitatCopy);
        } else if (item instanceof Food) {
            Food foodCopy = ((Food) item).copy();
            result = player.getBackpack().addItem(foodCopy, count);
        } else if (item instanceof Machine) {
            Machine machineCopy = ((Machine) item).copy();
            player.getMachineRecepies().add(machineCopy.getType());
            result = new Result(true, "Successfully purchased: " + machineCopy + " recipe");
        } else if (item instanceof MilkPail) {
            MilkPail milkPailCopy = ((MilkPail) item).copy();
            if (player.getBackpack().hasTool("milkpail")) {
                result = new Result(false, "You already have milkpail!");
            } else {
                player.getBackpack().getTools().add(milkPailCopy);
                result = new Result(true, "Successfully purchased: " + milkPailCopy.getName());
            }
        } else if (item instanceof FishingPole) {
            FishingPole fishingPolePailCopy = ((FishingPole) item).copy();
            if ((fishingPolePailCopy.getPoleMaterial() == FishingPoleMaterial.Iridium && player.getSkillsLevel().get(Skill.FISHING) < 4) ||
                    (fishingPolePailCopy.getPoleMaterial() == FishingPoleMaterial.FiberGlass && player.getSkillsLevel().get(Skill.FISHING) < 2)) {
                result = new Result(false, "You dont have the required fishing level to buy this fishing pole!");
            } else {
                if (player.getBackpack().hasTool("fishingpole")) {
                    result = new Result(false, "You already have fishing pole!");
                } else {
                    player.getBackpack().getTools().add(fishingPolePailCopy);
                    result = new Result(true, "Successfully purchased: " + fishingPolePailCopy.getName());
                }
            }
        } else if (item instanceof Shear) {
            Shear shearCopy = ((Shear) item).copy();
            if (player.getBackpack().hasTool("shear")) {
                result = new Result(false, "You already have shear!");
            } else {
                player.getBackpack().getTools().add(shearCopy);
                result = new Result(true, "Successfully purchased: " + shearCopy.getName());
            }
        } else if (item == null && shopItem.getShopItemType() == ShopItemType.FOODRECIPE) {
            String[] parts = shopItem.getName().split("\\s+");
            FoodRecipe foodRecipe = FoodRecipe.fromString(parts[0]);
            if (foodRecipe != null) {
                player.getCookingRecepies().add(foodRecipe);
                result = new Result(true, "Successfully purchased: " + foodRecipe);
            } else {
                result = new Result(false, "unable to find food recipe");
            }
        } else if (item == null && shopItem.getShopItemType() == ShopItemType.LARGE_BACKPACK) {
            player.getBackpack().upgrade(StorageType.BIG);
            result = new Result(true, "BackPack upgraded to big Successfully!");
        } else if (item == null && shopItem.getShopItemType() == ShopItemType.DELUXE_BACKPACK) {
            player.getBackpack().upgrade(StorageType.DELUX);
            result = new Result(true, "BackPack upgraded to deluxe Successfully!");
        } else if (item == null && shopItem.getShopItemType() == ShopItemType.SHIPPING_BIN) {
            if (canCreateShippingBin(player)) {
                result = new Result(true, "Shipping bin created");
            } else {
                result = new Result(false, "no empty space for shipping bin!");
            }
        } else if (item instanceof Fish) {
            Fish fishCopy = ((Fish) item).copy();
            result = player.getBackpack().addItem(fishCopy, count);
        } else if (item instanceof randomStuff) {
            randomStuff randomStuffCopy = ((randomStuff) item).copy();
            result = player.getBackpack().addItem(randomStuffCopy, count);
        } else {
            result = new Result(false, "Unknown item type");
        }
        if ((item instanceof Tool || item instanceof Animal || item instanceof Habitat) && count > 1) {
            return new Result(false, "You can only buy one of this item at a time.");
        }
//        else if (item instanceof Backpack) {
//            //delete the copy fucntion in backpack ????????????????????????????????
////            Backpack randomStuffCopy = ((Backpack) item).copy();
////            player.getBackpack().addItem(randomStuffCopy,1);
//        }
//        else if (item instanceof ForagingMineral) {
//            ForagingMineral foragingMineralCopy = ((ForagingMineral) item).copy();
//            result =  player.getBackpack().addItem(foragingMineralCopy,1);
//        }
//        else if (item instanceof TrashCan) {
//            TrashCan trashCanCopy = ((TrashCan) item).copy();
//            //player.getBackpack().addItem(trashCanCopy,1);
//        }
//        else if (item instanceof Tool) {
//            itemCopy = ((Tool) item).copy();
//            player.getToolInventory().add((Tool) itemCopy);
//        }
//        // ... handle other types
//
//        shopItem.incrementSoldToday();
//        player.decreaseCoins(shopItem.getPrice());
        return result;
    }


    private boolean canCreateShippingBin(User player) {
        Game game = App.getInstance().getCurrentGame();
        Farm farm = game.getMap().getFarmByOwner(player);
        Tile[][] map = game.getMap().getMap();
        // Create a random object to generate random numbers
        Random random = new Random();

        // Get the width and height of the farm
        int farmWidth = farm.getWidth();
        int farmHeight = farm.getHeight();

        // Try up to 10 times to find a suitable tile
        for (int attempt = 0; attempt < 20; attempt++) {
            // Generate random coordinates within the farm's bounds
            int randomX = farm.getX() + random.nextInt(farmWidth); // Random X coordinate within farm
            int randomY = farm.getY() + random.nextInt(farmHeight); // Random Y coordinate within farm

            Tile tile = map[randomY][randomX]; // Get the tile at the random coordinate

            // Check if the tile is of type FARM, growable is true, and item is null
            if (tile.getType() == TileType.FARM && tile.getContainedItem() == null &&
                    tile.getProductOfGrowable() == null && tile.getContainedGrowable() == null) {
                // Set the tile's type to SHIPPING_BIN
                tile.setType(TileType.SHIPPINGBIN);
                return true; // Indicating that we successfully found and updated a tile
            }
        }

        return false; // No suitable tile found after 20 attempts
    }


//    public Result buyFromCarpenter(String name, String x, String y) {
//        Game game = App.getInstance().getCurrentGame();
//        User player = game.getCurrentPlayer();
//        MapOfGame map = game.getMap();
//        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
//
//        if (shop == null) {
//            return new Result(false, "You should be inside a shop to use this method!");
//        }
//        if (shop.getShopType() != ShopType.CARPENTER_SHOP) {
//            return new Result(false, "You should be inside the Carpenter's Shop to use this method!");
//        }
//
//        // Parse coordinates
//        int targetX;
//        int targetY;
//        try {
//            targetX = Integer.parseInt(x);
//            targetY = Integer.parseInt(y);
//        } catch (NumberFormatException e) {
//            return new Result(false, "Invalid coordinates provided.");
//        }
//
//        // Check bounds
//        if (!map.isInsideFarm(targetX, targetY)) {
//            return new Result(false, "Coordinates are outside the farm bounds.");
//        }
//
//        if (map.getTile(targetX, targetY).hasObstacle()) {
//            return new Result(false, "Cannot place building on an obstacle such as a plant or tree.");
//        }
//
//        ShopItem item = shop.getShopItemByName(name);
//        if (item == null) {
//            return new Result(false, "Item not found in Carpenter's Shop: " + name);
//        }
//
//        Result resourceCheck = handleCarpenterSources(player, item);
//        if (!resourceCheck.isSuccess()) {
//            return resourceCheck;
//        }
//
//        // All conditions passed, build the structure
//        Object product = item.getItem();
//        if (!(product instanceof Habitat)) {
//            return new Result(false, "The selected item is not a building.");
//        }
//
//        Habitat habitat = ((Habitat) product).copy();
//        map.placeHabitatOnFarm(player, habitat, targetX, targetY);
//
//        return new Result(true, "Successfully built: " + name + " at (" + targetX + ", " + targetY + ")");
//    }

    public Result buyFromCarpenter(String name, String x, String y) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();
        Farm farm = map.getFarmByOwner(player);

        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if (shop == null) {
            return new Result(false, "You should be inside a shop to use this method!");
        }
        if (shop.getShopType() != ShopType.CARPENTER_SHOP) {
            return new Result(false, "You should be inside the Carpenter's Shop to use this method!");
        }
        if (game.getTimeAndDate().getHour() < shop.getStartHour() || game.getTimeAndDate().getHour() >= shop.getEndHour()) {
            return new Result(false, "the store is closed");
        }

        int xCoord, yCoord;
        try {
            xCoord = Integer.parseInt(x);
            yCoord = Integer.parseInt(y);
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid coordinates.");
        }

        ShopItem item = shop.getItemByName(name);
        if (item == null) {
            return new Result(false, "Item not found in Carpenter's shop: " + name);
        }
        if (!item.isAvailable(1)) {
            return new Result(false, "We sold out the item " + name);
        }

        // Check resources
        Result resourceCheck = handleCarpenterSources(player, item);
        if (!resourceCheck.isSuccessful()) {
            return resourceCheck;
        }
        if (item.getShopItemType() == ShopItemType.SHIPPING_BIN) {
            return tryCreatingShippingBin(xCoord, yCoord, player);
        }
        // Validate placement area
        Habitat habitat = (Habitat) item.getItem();
        int width = habitat.getWidth();
        int height = habitat.getHeight();

        // Check if all the intended tiles are within farm and empty
        if (!isAreaPlaceable(xCoord, yCoord, width, height)) {
            return new Result(false, "Target area is not valid or already occupied.");
        }
        item.sell(1);

        // Place habitat
        Habitat newHabitat = habitat.copy();
        newHabitat.setX(xCoord);
        newHabitat.setY(yCoord);
        TileType tileTypeToSet = null;

        if (item.getShopItemType() == ShopItemType.BARN) {
            farm.getBarn().add(newHabitat);
            tileTypeToSet = TileType.BARN;
        } else if (item.getShopItemType() == ShopItemType.CAGE) {
            farm.getCage().add(newHabitat);
            tileTypeToSet = TileType.CAGE;
        }

// Set the type of all tiles in the habitat area
        for (int i = xCoord; i < xCoord + newHabitat.getWidth(); i++) {
            for (int j = yCoord; j < yCoord + newHabitat.getHeight(); j++) {
                Tile tile = map.getTile(i, j);
                if (tile != null && tileTypeToSet != null) {
                    tile.setType(tileTypeToSet);
                }
            }
        }

        if (item.getShopItemType() == ShopItemType.BARN) {
            farm.getBarn().add(newHabitat);
        }
        if (item.getShopItemType() == ShopItemType.CAGE) {
            farm.getCage().add(newHabitat);
        }

        map.getFarmByOwner(player).getBarn().add(newHabitat); // You may want to separate barns vs coops by type
        return new Result(true, "Successfully built " + name + " at (" + xCoord + ", " + yCoord + ")");
    }

    private Result tryCreatingShippingBin(int xCoord, int yCoord, User player) {
        Game game = App.getInstance().getCurrentGame();
        Farm farm = game.getMap().getFarmByOwner(player);
        Tile[][] map = game.getMap().getMap();
        Tile tile = map[yCoord][xCoord];
        if (xCoord < farm.getX() || yCoord < farm.getY() || xCoord > farm.getX() + farm.getWidth()
                || yCoord > farm.getY() + farm.getHeight()) {
            return new Result(false, "You cant create a shipping bin inside other player's farm");
        }
        if (tile.getType() == TileType.FARM && tile.getContainedItem() == null &&
                tile.getProductOfGrowable() == null && tile.getContainedGrowable() == null) {
            tile.setType(TileType.SHIPPINGBIN);
            return new Result(true, "Shipping bin created");
        }
        return new Result(false, "Invalid location for creating shipping bin");
    }

    public boolean isAreaPlaceable(int x, int y, int width, int height) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();
        Farm farm = map.getFarmByOwner(player);

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                // Check bounds
                if (i < farm.getX() || i >= farm.getX() + farm.getWidth() ||
                        j < farm.getY() || j >= farm.getY() + farm.getHeight()) {
                    return false;
                }
                // Check occupation
                if (isOccupied(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isOccupied(int x, int y) {
        MapOfGame map = App.getInstance().getCurrentGame().getMap();
        Tile tile = map.getTile(x, y);
        return tile != null && (
                tile.getContainedGrowable() != null ||
                        tile.getProductOfGrowable() != null ||
                        tile.getContainedItem() != null
        ); // or tile.isBlocked() if such a method exists
    }


    public Result buyAnimal(String animal, String name) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();
        Tile playerTile = player.getCurrentTile();
        Shop shop = map.getShopAtPosition(playerTile.getX(), playerTile.getY());

        // 1. Validate shop
        if (shop == null || shop.getShopType() != ShopType.MARNIE_RANCH) {
            return new Result(false, "You must be inside Marnie’s Ranch to buy an animal.");
        }
        if (game.getTimeAndDate().getHour() < shop.getStartHour() || game.getTimeAndDate().getHour() >= shop.getEndHour()) {
            return new Result(false, "the store is closed");
        }

        // 2. Check if the name is unique
        if (player.getOwnedAnimals() != null) {
            if (!player.getOwnedAnimals().isEmpty()) {
                for (Animal ownedAnimal : player.getOwnedAnimals()) {
                    if (ownedAnimal.getName().equalsIgnoreCase(name)) {
                        return new Result(false, "You already have an animal with this name.");
                    }
                }
            }
        }
        // 3. Retrieve the shop item
        ShopItem selectedItem = shop.getItemByName(animal);
        if (selectedItem == null || !(selectedItem.getItem() instanceof Animal)) {
            return new Result(false, "Animal not found in the shop.");
        }
        if (!selectedItem.isAvailable(1)) {
            return new Result(false, "We sold out for today.");
        }

        Animal newAnimal = ((Animal) selectedItem.getItem()).copy();
        newAnimal.setName(name);

        // 4. Check for suitable habitat
        AnimalType animalType = newAnimal.getAnimalType();
        List<Habitat> habitats = animalType.getHabitat().equalsIgnoreCase("Barn")
                ? map.getFarmByOwner(player).getBarn()
                : map.getFarmByOwner(player).getCage();

        boolean placed = false;
        for (Habitat habitat : habitats) {
            // Check if this habitat type matches any of the animal's allowed storage types AND has space
            if (animalType.getStorageTypes().contains(habitat.getStorageType()) &&
                    habitat.getLivingAnimals().size() < habitat.getStorageType().getHabitatCapacity()) {
                habitat.getLivingAnimals().add(newAnimal);
                newAnimal.setLivingPlace(habitat);
                placed = true;
                break;
            }
        }

        if (!placed) {
            return new Result(false, "No suitable habitat with enough space for this animal.");
        }

        // 5. Deduct price
        int price = getCurrentSeasonPrice(selectedItem);
        if (player.getMoney() < price) {
            return new Result(false, "You don't have enough coins. Required: " + price);
        }

        player.decreaseMoney(price);
        player.getOwnedAnimals().add(newAnimal);
        // fix animal current tile in the habitat and place the animal in the containedAnimal field of the tile in that Tile

        // Find a free tile in that habitat
        Tile freeTile = map.getTile(newAnimal.getLivingPlace().getX(), newAnimal.getLivingPlace().getY());
        if (freeTile == null) {
            return new Result(false, "No free space in the habitat for the animal.");
        }

        // Set references both ways
        newAnimal.setCurrentTile(freeTile);
        freeTile.setContainedAnimal(newAnimal);
        selectedItem.sell(1);
        return new Result(true, "Successfully bought " + name + " the " + animal +
                "! now it is on " + freeTile.getX() + "," + freeTile.getY());
    }

    public Result showAllProducts() {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();

        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if (shop == null) {
            return new Result(false, "You must be inside a store to purchase items.");
        }
        if (game.getTimeAndDate().getHour() < shop.getStartHour() || game.getTimeAndDate().getHour() >= shop.getEndHour()) {
            return new Result(false, "the store is closed");
        }
        StringBuilder result = new StringBuilder("All Products in Store " + shop.getShopName() + ":\n");

        for (ShopItem product : shop.getProducts()) { // assumed method
            result.append(String.format("- %s | Price: %d | %s\n",
                    product.getName(),
                    getCurrentSeasonPrice(product),
                    product.getDailyLimit() - product.getSoldToday() > 0 ? "Available" : "Out of stock"));
        }

        return new Result(true, result.toString());
    }

    public Result showAllAvailableProducts() {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();

        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if (shop == null) {
            return new Result(false, "You must be inside a store to purchase items.");
        }
        if (game.getTimeAndDate().getHour() < shop.getStartHour() || game.getTimeAndDate().getHour() >= shop.getEndHour()) {
            return new Result(false, "the store is closed");
        }
        StringBuilder result = new StringBuilder("Available Products in Store " + shop.getShopName() + ":\n");

        for (ShopItem product : shop.getProducts()) {
            if (product.isAvailable(1) && getCurrentSeasonPrice(product) > 0) {
                result.append(String.format("- %s | Price: %d | In stock: %d\n",
                        product.getName(),
                        getCurrentSeasonPrice(product),
                        product.getDailyLimit() - product.getSoldToday()));
            }
        }

        return new Result(true, result.toString());
    }


    public Result placeInShippingBin(String productString, int count) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        Farm farm = game.getMap().getFarmByOwner(player);

        // Check if the player is near the shipping bin
        if (!isNearShippingBin(player)) {
            return new Result(false, "You must be near the shipping bin to sell items.");
        }

        // Tools cannot be sold
        if (player.getBackpack().hasTool(productString)) {
            return new Result(false, "This product cannot be sold.");
        }

        int availableCount = player.getBackpack().getItemCount(productString);
        if (availableCount == 0) {
            return new Result(false, "You do not have the specified product in your inventory.");
        }

        // If count == -1, sell all
        if (count == -1) {
            count = availableCount;
        } else if (count > availableCount) {
            return new Result(false, "You do not have that many units of the product.");
        }

        // Remove item from inventory
        Item product = player.getBackpack().grabItemAndReturn(productString, count);
        if (product == null) {
            return new Result(false, "You do not have enough product in your inventory.");
        }

        // Check if the product is sellable
        if (!product.isSellable()) {
            return new Result(false, "This product cannot be sold.");
        }

        // Place in shipping bin
        farm.getShippingBin().put(product, count);

        return new Result(true, "Successfully sold " + count + " " + product.getName());
    }


    private boolean isNearShippingBin(User player) {
        // Get the player's current tile position (assuming player has a position method)
        Tile[][] map = App.getInstance().getCurrentGame().getMap().getMap();
        Tile playerPosition = player.getCurrentTile();
        int playerX = playerPosition.getX();
        int playerY = playerPosition.getY();

        // Check all 8 adjacent tiles
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the player's own tile (0, 0 displacement)
                if (dx == 0 && dy == 0) continue;

                // Calculate the adjacent tile coordinates
                int adjacentX = playerX + dx;
                int adjacentY = playerY + dy;
                if (adjacentY < 0 || adjacentX < 0 || adjacentY >= map.length || adjacentX >= map[0].length) {
                    continue;
                }
                // Get the tile at the adjacent coordinates (assuming farm's getTileAt method)
                Tile adjacentTile = map[adjacentY][adjacentX]; // You may need to modify this method based on how your tiles are stored

                // Check if the adjacent tile is a SHIPPING_BIN
                if (adjacentTile != null && adjacentTile.getType() == TileType.SHIPPINGBIN) {
                    return true; // Return true if we find a shipping bin
                }
            }
        }

        // Return false if no adjacent tile is a shipping bin
        return false;
    }


    public Result upgradeTool(String tool) {
        Game game = App.getInstance().getCurrentGame();
        User player = game.getCurrentPlayer();
        MapOfGame map = game.getMap();

        Shop shop = map.getShopAtPosition(player.getCurrentTile().getX(), player.getCurrentTile().getY());
        if (shop == null || shop.getShopType() != ShopType.BLACKSMITH) {
            return new Result(false, "You must be inside the blacksmith store to upgrade tools.");
        }

        int hour = game.getTimeAndDate().getHour();
        if (hour < shop.getStartHour() || hour >= shop.getEndHour()) {
            return new Result(false, "The store is closed.");
        }

        ToolType toolType = ToolType.fromString(tool);
        if (toolType == null) {
            return new Result(false, "Invalid tool type.");
        }
        Tool currentTool;
        if (toolType == ToolType.TRASHCAN) {
            currentTool = player.getBackpack().getTrashcan();
        } else {
            currentTool = player.getBackpack().getTool(toolType.getName());
        }
        if (currentTool == null) {
            return new Result(false, "You don't have this tool.");
        }

        ToolMaterial currentMaterial = currentTool.getMaterial();
        ToolMaterial nextMaterial = currentMaterial.getNext(); // You must implement this method

        if (nextMaterial == null) {
            return new Result(false, "Your tool is already at the highest upgrade level.");
        }

        // Determine upgrade requirements
        String requiredBar = getRequiredBarName(nextMaterial);
        int requiredBarCount = 5;
        int cost = getUpgradeCost(nextMaterial, toolType == ToolType.TRASHCAN);
///    ////////////////////
        player.getBackpack().addItem(new randomStuff(10, randomStuffType.Copper_Bar), requiredBarCount);
        /// /////////////////
        int playerBarCount = player.getBackpack().getItemCount(requiredBar);
        int playerMoney = player.getMoney();

        if (playerBarCount < requiredBarCount) {
            return new Result(false, "Not enough " + requiredBar + ". Required: " + requiredBarCount + ", you have: " + playerBarCount);
        }

        if (playerMoney < cost) {
            return new Result(false, "Not enough coins. Required: " + cost + ", you have: " + playerMoney);
        }

        // Deduct resources
        player.getBackpack().grabItem(requiredBar, requiredBarCount);
        player.decreaseMoney(cost);

        // Upgrade the tool
        currentTool.upgrade(nextMaterial);

        return new Result(true, "Successfully upgraded " + tool + " to " + nextMaterial + " level!");
    }

    private String getRequiredBarName(ToolMaterial material) {
        switch (material) {
            case Copper:
                return "Copper Bar";
            case Iron:
                return "Iron Bar";
            case Gold:
                return "Gold Bar";
            case Iridium:
                return "Iridium Bar";
            default:
                return "";
        }
    }

    private int getUpgradeCost(ToolMaterial material, boolean isTrashCan) {
        switch (material) {
            case Copper:
                return isTrashCan ? 1000 : 2000;
            case Iron:
                return isTrashCan ? 2500 : 5000;
            case Gold:
                return isTrashCan ? 5000 : 10000;
            case Iridium:
                return isTrashCan ? 12500 : 25000;
            default:
                return 0;
        }
    }


}
