package org.example.Model.Things;

public enum StorageType {
    INITIAL(12, 4),
    BIG(24, 8),
    DELUX(100000, 12); // Unlimited

    private final int backpackCapacity;
    private final int habitatCapacity;

    StorageType(int backpackCapacity, int habitatCapacity) {
        this.backpackCapacity = backpackCapacity;
        this.habitatCapacity = habitatCapacity;
    }

    public int getBackpackCapacity() {
        return backpackCapacity;
    }

    public int getHabitatCapacity() {
        return habitatCapacity;
    }
}