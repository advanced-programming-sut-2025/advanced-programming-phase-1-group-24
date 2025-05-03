package org.example.Model.Things;

public class ForagingMineral extends Item {
    private ForagingMineralType type;

    public ForagingMineral(ForagingMineralType type) {
        this.type = type;
    }

    public ForagingMineralType getType() {
        return type;
    }
}
