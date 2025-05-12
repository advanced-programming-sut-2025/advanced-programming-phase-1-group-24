package org.example.Model.Places;

public class ShopItem {
    private int springPrice;
    private int summerPrice;
    private int autumnPrice;
    private int winterPrice;
    private String name;
    private Object item; // Better to use a generic or interface
    private int soldToday = 0;
    private final int dailyLimit;
    private ShopItemType shopItemType;

    public ShopItem(String name, int dailyLimit, Object item, ShopItemType shopItemType, int springPrice, int summerPrice, int autumnPrice, int winterPrice) {
        this.name = name;
        this.dailyLimit = dailyLimit;
        this.item = item;
        this.shopItemType = shopItemType;
        this.springPrice = springPrice;
        this.summerPrice = summerPrice;
        this.autumnPrice = autumnPrice;
        this.winterPrice = winterPrice;
    }

    public boolean isAvailable(int quantity) {
        return soldToday + quantity <= dailyLimit;
    }

    public void sell(int quantity) {
        soldToday += quantity;
    }

    public void resetDailyLimit() {
        soldToday = 0;
    }

    public int getSpringPrice() {
        return springPrice;
    }

    public void setSpringPrice(int springPrice) {
        this.springPrice = springPrice;
    }

    public int getSummerPrice() {
        return summerPrice;
    }

    public void setSummerPrice(int summerPrice) {
        this.summerPrice = summerPrice;
    }

    public int getWinterPrice() {
        return winterPrice;
    }

    public void setWinterPrice(int winterPrice) {
        this.winterPrice = winterPrice;
    }

    public int getAutumnPrice() {
        return autumnPrice;
    }

    public void setAutumnPrice(int autumnPrice) {
        this.autumnPrice = autumnPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public int getSoldToday() {
        return soldToday;
    }

    public void setSoldToday(int soldToday) {
        this.soldToday = soldToday;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public ShopItemType getShopItemType() {
        return shopItemType;
    }

    public void setShopItemType(ShopItemType shopItemType) {
        this.shopItemType = shopItemType;
    }
}
