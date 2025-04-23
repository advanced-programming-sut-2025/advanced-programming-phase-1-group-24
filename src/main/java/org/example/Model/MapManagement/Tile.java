package org.example.Model.MapManagement;

import org.example.Model.Growables.Growable;
import org.example.Model.Things.Item;

public class Tile {
    private TileType type;
    private boolean isWalkable;
    private Item containedItem;
    private Growable containedGrowable;

    public TileType getType() {
        return type;
    }
    public Item getContainedItem() {
        return containedItem;
    }
    public Growable getContainedGrowable() {
        return containedGrowable;
    }
    public boolean getisWalkable(){
        return isWalkable;
    }

    public void setWalkable(boolean isWalkable) {
        this.isWalkable = isWalkable;
    }
    public void setType(TileType type) {
        this.type = type;
    }
    public void setContainedItem(Item containedItem) {
        this.containedItem = containedItem;
    }
    public void setContainedGrowable(Growable containedGrowable) {
        this.containedGrowable = containedGrowable;
    }
    public void changeTile(){}

}
