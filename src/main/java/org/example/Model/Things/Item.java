package org.example.Model.Things;

public abstract class Item {
    protected String name;
    protected boolean isSellable;
    protected int price;
    protected boolean isPlaceable;
    protected ProductQuality productQuality = ProductQuality.Normal;

    public Item(String name, boolean isSellable, int price, boolean isPlaceable, ProductQuality productQuality) {
        this.name = name;
        this.isSellable = isSellable;
        this.price = price;
        this.isPlaceable = isPlaceable;
        this.productQuality = productQuality;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSellable() {
        return isSellable;
    }

    public void setSellable(boolean sellable) {
        isSellable = sellable;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPlaceable() {
        return isPlaceable;
    }

    public void setPlaceable(boolean placeable) {
        isPlaceable = placeable;
    }

    public ProductQuality getProductQuality() {
        return productQuality;
    }

    public void setQuality(ProductQuality productQuality) {
        this.productQuality = productQuality;
    }
    public String toString(){
        return name;
    }
}

