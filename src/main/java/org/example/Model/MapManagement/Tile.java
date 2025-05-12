package org.example.Model.MapManagement;

import org.example.Model.Animals.Animal;
import org.example.Model.Growables.Growable;
import org.example.Model.NPCManagement.NPC;
import org.example.Model.Things.Item;
import org.example.Model.User;;

public class Tile {
    private TileType type;
    private boolean isWalkable;
    private boolean isPlowed;
    private Item containedItem;
    private Growable containedGrowable;
    private Growable productOfGrowable;  //when ever it is time for the giah to create a product we will check if this field is full we will not create another product
    //also we wil check if the season is correct for giving product
    private String tileOwner;
    private int x;
    private int y;
    private Animal containedAnimal;
    private NPC containedNPC;


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
    public Growable getProductOfGrowable() {
        return productOfGrowable;
    }
    public String getTileOwner() {
        return tileOwner;
    }
    public boolean getIsPlowed() {
        return isPlowed;
    }

    public void setIsPlowed(boolean plowed) {
        isPlowed = plowed;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public Animal getContainedAnimal() {
        return containedAnimal;
    }

    public NPC getContainedNPC() {
        return containedNPC;
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
    public void setProductOfGrowable(Growable productOfGrowable) {
        this.productOfGrowable = productOfGrowable;
    }
    public void setTileOwner(String tileOwner) {
        this.tileOwner = tileOwner;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setContainedAnimal(Animal containedAnimal) {
        this.containedAnimal = containedAnimal;
    }

    public void setContainedNPC(NPC containedNPC) {
        this.containedNPC = containedNPC;
    }

    public void changeTile(){}

}
