package org.example.Model.MapManagement;

import org.example.Model.Growables.GrowableType;
import org.example.Model.Places.Farm;
import org.example.Model.User;

import java.util.*;


public class MapOfGame {

    private Tile[][] map;
    private int width = 150;
    private int height = 150;
    private ArrayList<Farm> farms = new ArrayList<>();

    public MapOfGame() {
        initializeMap();
    }

    private void initializeMap() {
        this.map = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile();
                if(y > 50 && y < 100 && x > 50 && x < 100) {tile.setType(TileType.NPCLAND);}
                else tile.setType(TileType.EMPTY);
                tile.setWalkable(true);
                tile.setContainedItem(null);
                tile.setContainedGrowable(null);
                tile.setX(x);
                tile.setY(y);
                map[y][x] = tile;
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
            tile.getContainedGrowable().setGrowableType(GrowableType.Coal);
            tile.setProductOfGrowable(null);
            tile.setContainedItem(null);
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
}