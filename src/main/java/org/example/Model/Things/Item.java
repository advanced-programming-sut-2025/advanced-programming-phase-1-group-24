package org.example.Model.Things;

public abstract class Item {
    protected String name;
    protected boolean isSellable;
    protected int price;
    protected boolean isPlacable;
    protected ProductQuality quality;

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public boolean isSellable() {
        return isSellable;
    }

    public boolean isPlacable() {
        return isPlacable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSellable(boolean sellable) {
        isSellable = sellable;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPlacable(boolean placable) {
        isPlacable = placable;
    }

    public void setQuality(ProductQuality quality) {
        this.quality = quality;
    }

    public String toString() {
        return name;
    }

    public ProductQuality getQuality() {
        return quality;
    }

}
