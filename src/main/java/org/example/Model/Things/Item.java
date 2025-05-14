package org.example.Model.Things;

import org.example.Model.Animals.AnimalProduct;
import org.example.Model.Animals.AnimalProductType;
import org.example.Model.Growables.*;
import org.example.Model.Reccepies.Machine;
import org.example.Model.Reccepies.MachineType;
import org.example.Model.Reccepies.randomStuff;
import org.example.Model.Reccepies.randomStuffType;

import java.util.Objects;

public class Item {
    protected String name;
    protected boolean isSellable;
    protected int price;
    protected boolean isPlaceable;
    protected boolean isEatable;
    protected ProductQuality productQuality = ProductQuality.Normal;

    public Item(String name, boolean isSellable, int price, boolean isPlaceable, ProductQuality productQuality, boolean isEatable) {
        this.name = name;
        this.isSellable = isSellable;
        this.price = price;
        this.isPlaceable = isPlaceable;
        this.productQuality = productQuality;
        this.isEatable = isEatable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSellable() {
        return isSellable;
    }

    public void setSellable(boolean sellable) {
        isSellable = sellable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPlaceable() {
        return isPlaceable;
    }

    public void setPlaceable(boolean placeable) {
        isPlaceable = placeable;
    }

    public ProductQuality getProductQuality() {
        return productQuality;
    }

    public void setQuality(ProductQuality productQuality) {
        this.productQuality = productQuality;
    }

    public String toString() {
        return name;
    }

    public boolean isEatable() {
        return isEatable;
    }

    public void setEatable(boolean eatable) {
        isEatable = eatable;
    }

    public static Item getRandomItem(String name) {
        for (FishType fishType : FishType.values()) {
            if (fishType.getName().equalsIgnoreCase(name)) {
                return new Fish(ProductQuality.Normal, fishType);
            }
        }
        for (FoodType foodType : FoodType.values()) {
            if (foodType.getName().equalsIgnoreCase(name)) {
                return new Food(foodType);
            }
        }
        for (ForagingMineralType foragingMineralType : ForagingMineralType.values()) {
            if (foragingMineralType.getName().equalsIgnoreCase(name)) {
                return new ForagingMineral(ProductQuality.Normal, foragingMineralType);
            }
        }
        for (randomStuffType randomStuffType : randomStuffType.values()) {
            if (randomStuffType.getName().equalsIgnoreCase(name)) {
                return new randomStuff(randomStuffType.getSellPrice(), randomStuffType);
            }
        }
        for (AnimalProductType animalProductType : AnimalProductType.values()) {
            if (animalProductType.getProductName().equalsIgnoreCase(name)) {
                return new AnimalProduct(ProductQuality.Normal, animalProductType);
            }
        }
        for(SourceType sourceType : SourceType.values()) {
            if(sourceType.getName().equalsIgnoreCase(name)) {
                return GrowableFactory.getInstance().create(sourceType);
            }
        }
        for(ForagingCropType foragingCropType : ForagingCropType.values()) {
            if(foragingCropType.getName().equalsIgnoreCase(name)) {
                return GrowableFactory.getInstance().create(foragingCropType);
            }
        }
        for(CropType cropType : CropType.values()) {
            if(cropType.getName().equalsIgnoreCase(name)) {
                Growable newCrop = GrowableFactory.getInstance().create(cropType.getSource());
                newCrop.setName(cropType.getName());
                return newCrop;
            }
        }
        for(FruitType fruitType : FruitType.values()) {
            if(fruitType.getName().equalsIgnoreCase(name)) {
                for (TreeType treeType : TreeType.values()) {
                    if (treeType.getFruitType() == fruitType) {
                        Growable newFruit = GrowableFactory.getInstance().create(treeType.getSource());
                        newFruit.setName(fruitType.getName());
                        return newFruit;
                    }
                }
            }
        }
        for (MachineType machineType : MachineType.values()) {
            if (machineType.getName().equalsIgnoreCase(name)) {
                return new Machine(machineType);
            }
        }
        return null;
    }

    public Item copy() {
        return new Item(name, isSellable, price, isPlaceable, productQuality, isEatable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        if (isSellable != item.isSellable) return false;
        if (price != item.price) return false;
        if (isEatable != item.isEatable) return false;
        if (isPlaceable != item.isPlaceable) return false;
        if (!name.equalsIgnoreCase(item.name)) return false;
        return productQuality == item.productQuality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isSellable, price, isPlaceable, productQuality);
    }
}

