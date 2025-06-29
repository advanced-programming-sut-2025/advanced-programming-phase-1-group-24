package io.github.stardew.mini.model.game;

import io.github.stardew.mini.model.Pair;
import io.github.stardew.mini.model.item.ItemDescriptionId;
import io.github.stardew.mini.model.item.TileDescriptionId;

import java.util.*;

public class Player {
    private Map<ItemDescriptionId, Pair<Integer, Integer>> inventory;
    private Pair<Float, Float> playerPosition;
    private Stack<Integer> freeIndexes;
    private final Integer maxInventorySize = 9;
    private int selectedSlot = -1;
    private int movingDirection = 0;

    public Player() {
        inventory = new HashMap<>();
        freeIndexes = new Stack<>();
        for (int i = maxInventorySize - 1; i >= 0; i--) {
            freeIndexes.push(i);
        }
        playerPosition = new Pair<>(3f, 3f);

        addItem(ItemDescriptionId.HOE, 1);
        addItem(ItemDescriptionId.SCYTHE, 1);
        addItem(ItemDescriptionId.WATERING_CAN, 1);
        addItem(ItemDescriptionId.CARROT_SEED, 5);
    }

    public void addItem(ItemDescriptionId itemId, int count) {
        Pair<Integer, Integer> pair = inventory.getOrDefault(itemId, new Pair<>(0, freeIndexes.pop()));
        pair.first = pair.first + count;
        inventory.put(itemId, pair);
    }

    public void useActiveItem(float worldX, float worldY) {
        // Implement item usage logic
    }

    public Pair<Float, Float> getPosition() {
        return playerPosition;
    }

    private float speed = 2f;
    private float vx = 0, vy = 0;

    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void update(float delta, TileDescriptionId[][] tiles) {
        tryMove(vx * delta, vy * delta, tiles);
    }

    public boolean tryMove(float dx, float dy, TileDescriptionId[][] tiles) {
        int newX = (int) (playerPosition.first + dx);
        int newY = (int) (playerPosition.second + dy);

        if (newX < 0 || newX >= tiles.length || newY < 0 || newY >= tiles[0].length) return false;

        if (tiles[newX][newY] != TileDescriptionId.WATER) {
            playerPosition.first += dx;
            playerPosition.second += dy;
            return true;
        }
        return false;
    }


    public Map<ItemDescriptionId, Pair<Integer, Integer>> getInventory() {
        return inventory;
    }

    public void setSelectedSlot(int selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    public ItemDescriptionId getSelectedItem() {
        return inventory.entrySet().stream().filter(
            entry -> entry.getValue().second == selectedSlot
        ).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public int getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(int direction) {
        this.movingDirection = direction;
    }

    public int getMaxInventorySize() {
        return maxInventorySize;
    }

    public float getSpeed() {
        return speed;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public void useSelectedItem() {
        ItemDescriptionId selectedItem = getSelectedItem();
        if (selectedItem == ItemDescriptionId.CARROT_SEED) {
            reduceItem();
        }
    }

    private void reduceItem() {
        Pair<Integer, Integer> pair = inventory.getOrDefault(ItemDescriptionId.CARROT_SEED, null);
        if (pair == null) {
            return;
        }
        pair.first = pair.first - 1;
        if (pair.first == 0) {
            inventory.remove(ItemDescriptionId.CARROT_SEED);
            freeIndexes.push(pair.second);
        } else {
            inventory.put(ItemDescriptionId.CARROT_SEED, pair);
        }
    }
}

