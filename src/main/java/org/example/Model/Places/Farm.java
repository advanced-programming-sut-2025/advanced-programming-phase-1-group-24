package org.example.Model.Places;

import java.util.ArrayList;

import org.example.Model.User;
import org.example.Model.ConfigTemplates.FarmTemplate;
import org.example.Model.ConfigTemplates.FarmTemplate.Block;
import org.example.Model.MapManagement.Tile;
import org.example.Model.MapManagement.TileType;

public class Farm extends Place{
    private ArrayList<Habitat> cage;
    private ArrayList<Habitat> barn;
    private ArrayList<Habitat> lake;
    private GreenHouse greenHouse;
    private Quarry quarry;
    private House house;
    private User owner;


    public Farm(User owner, FarmTemplate template, int startX, int startY){
    this.x = startX;
    this.y = startY;
    this.owner = owner;
    this.width = template.width;
    this.height = template.height;

    this.lake = new ArrayList<>();
    if (template.lake != null) {
        for (Block b : template.lake) {
            Habitat habitat = new Habitat(b.startX + startX, b.startY + startY, b.width, b.height);
            lake.add(habitat);
        }
    }

    if (template.house != null) {
        this.house = new House(template.house.startX + startX, template.house.startY + startY, template.house.width, template.house.height);
    }

    if (template.greenHouse != null) {
        this.greenHouse = new GreenHouse(template.greenHouse.startX + startX, template.greenHouse.startY + startY, template.greenHouse.width, template.greenHouse.height);
    }

    if (template.quarry != null) {
        this.quarry = new Quarry(template.quarry.startX + startX, template.quarry.startY + startY, template.quarry.width, template.quarry.height);
    }

    this.cage = new ArrayList<>();
    this.barn = new ArrayList<>();
    }

    public void setTileTypes(Tile[][] map) {
    for (int y = this.y; y < this.y + this.height; y++) {
        for (int x = this.x; x < this.x + this.width; x++) {
            Tile tile = map[y][x];

            if (isInsideGreenHouse(x, y)) {
                tile.setType(TileType.GREENHOUSE);
            }
            else if (isInsideLake(x, y)) {
                tile.setType(TileType.LAKE);
            }
            else if (isInsideQuarry(x, y)) {
                tile.setType(TileType.QUARRY);
            }
            else if (isInsideHouse(x, y)) {
                tile.setType(TileType.HOUSE);
            }
            else {
                tile.setType(TileType.FARM);
                //natural foraging
                //tree contain growable = tree (including tree type)
                //instead of contain growable one contain object
            }
        }
    }
}


private boolean isInsideGreenHouse(int x, int y) {
    return greenHouse != null &&
           x >= greenHouse.getX() && x < greenHouse.getX() + greenHouse.getWidth() &&
           y >= greenHouse.getY() && y < greenHouse.getY() + greenHouse.getHeight();
}

private boolean isInsideLake(int x, int y) {
    for (Habitat habitat : lake) {
        if (x >= habitat.getX() && x < habitat.getX() + habitat.getWidth() &&
            y >= habitat.getY() && y < habitat.getY() + habitat.getHeight()) {
            return true;
        }
    }
    return false;
}

private boolean isInsideQuarry(int x, int y) {
    return quarry != null &&
           x >= quarry.getX() && x < quarry.getX() + quarry.getWidth() &&
           y >= quarry.getY() && y < quarry.getY() + quarry.getHeight();
}

private boolean isInsideHouse(int x, int y) {
    return house != null &&
           x >= house.getX() && x < house.getX() + house.getWidth() &&
           y >= house.getY() && y < house.getY() + house.getHeight();
}


    public ArrayList<Habitat> getCage() {
        return cage;
    }
    public ArrayList<Habitat> getBarn() {
        return barn;
    }
    public ArrayList<Habitat> getLake() {
        return lake;
    }
    public GreenHouse getGreenHouse() {
        return greenHouse;
    }
    public Quarry getQuarry() {
        return quarry;
    }
    public House getHouse() {
        return house;
    }
    public User getOwner() {
        return owner;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }


    public void setLake(ArrayList<Habitat> lake) {
        this.lake = lake;
    }
    public void setCage(ArrayList<Habitat> cage) {
        this.cage = cage;
    }
    public void setBarn(ArrayList<Habitat> barn) {
        this.barn = barn;
    }
    public void setGreenHouse(GreenHouse greenHouse) {
        this.greenHouse = greenHouse;
    }
    public void setQuarry(Quarry quarry) {
        this.quarry = quarry;
    }
    public void setHouse(House house) {
        this.house = house;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
}
