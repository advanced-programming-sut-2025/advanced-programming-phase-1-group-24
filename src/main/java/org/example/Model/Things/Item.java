package org.example.Model.Things;

public abstract class Item {
    protected String name;
    protected boolean isSellable;
    protected int price;
    protected boolean isPlacable;
    protected ProductQuality productQuality = ProductQuality.Normal;

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

    public boolean isPlacable() {
        return isPlacable;
    }

    public void setPlacable(boolean placable) {
        isPlacable = placable;
    }

    public ProductQuality getProductQuality() {
        return productQuality;
    }

    public void setProductQuality(ProductQuality productQuality) {
        this.productQuality = productQuality;
    }
}

