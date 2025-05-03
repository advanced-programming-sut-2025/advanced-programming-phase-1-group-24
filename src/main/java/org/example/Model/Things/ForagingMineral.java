package org.example.Model.Things;

public class ForagingMineral extends Item{
    ForagingMineralType type;

    public ForagingMineral(String name, ProductQuality productQuality, ForagingMineralType type) {
        super(name, true, type.getSellPrice(), true, productQuality);
        this.type = type;
    }
}
