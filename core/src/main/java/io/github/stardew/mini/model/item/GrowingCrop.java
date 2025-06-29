package io.github.stardew.mini.model.item;

public class GrowingCrop {
    private final ItemDescriptionId result;
    private int growth = 0;
    private boolean isWatered = false;

    public GrowingCrop(ItemDescriptionId result) {
        this.result = result;
    }

    public void advance() {
        if (isWatered) {
            growth += 1;
            isWatered = false;
        }
    }

    public void water() {
        isWatered = true;
    }

    public boolean isReady() {
        return growth > 5;
    }

    public ItemDescriptionId getResult() {
        return result;
    }

    public int getGrowth() {
        return growth;
    }

    public boolean watered() {
        return isWatered;
    }

}
