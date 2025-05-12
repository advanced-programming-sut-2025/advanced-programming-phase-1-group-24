package org.example.Model.Things;

public class ForagingMineral extends Item{
    ForagingMineralType type;

    public ForagingMineral(ProductQuality productQuality, ForagingMineralType type) {
        super(type.getName(), true, type.getSellPrice(), true, productQuality,false);
        this.type = type;
    }
    @Override
    public ForagingMineral copy() {
        return new ForagingMineral(this.getProductQuality(), this.type);
    }

}
