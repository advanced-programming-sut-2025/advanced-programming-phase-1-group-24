package org.example.Model.MapManagement;

import org.example.Model.Things.ForagingType;


public class MapOfGame {

    private Tile[][] map;
    private int width = 300;
    private int height = 200;


    private void initializeMap() {
        map = new Tile[height][width]; 

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


    public void changeTile(TileType newTile, TileType oldTile) {}

    public void randomForaging(ForagingType newForaging) {

    }
}
