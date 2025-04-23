package org.example.Model.Things;

public enum BackpackType {
    INITIAL(12),
    BIG(24),
    DELUX(-1);

    private int capacity;
    BackpackType(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
}
