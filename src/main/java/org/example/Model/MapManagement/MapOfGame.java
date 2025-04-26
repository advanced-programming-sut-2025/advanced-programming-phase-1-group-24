package org.example.Model.MapManagement;

import org.example.Model.Things.ForagingType;


public class MapOfGame {

    private Tile[][] map;
    private int width = 100;
    private int height = 100;

    public MapOfGame() {
        initializeMap();
    }

    private void initializeMap() {
        this.map = new Tile[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile();
                tile.setType(TileType.EMPTY); 
                tile.setWalkable(false);
                tile.setContainedItem(null);
                tile.setContainedGrowable(null);
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

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public void changeTile(TileType newTile, TileType oldTile) {}

    public void randomForaging(ForagingType newForaging) {

    }
}
